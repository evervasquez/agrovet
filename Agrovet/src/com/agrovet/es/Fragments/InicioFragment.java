/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.agrovet.es.Fragments;

import it.gmariotti.cardslib.library.Constants;
import it.gmariotti.cardslib.library.utils.BitmapUtils;
import it.gmariotti.cardslib.library.view.CardView;

import java.io.File;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;

import com.agrovet.es.R;
import com.agrovet.es.cards.GoogleNowBirthCard;

/**
 * Card Examples.
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class InicioFragment extends BaseFragment {

	protected ScrollView			mScrollView;
	private CardView				cardView;
	private GoogleNowBirthCard		birthCard;
	private ShareActionProvider		mShareActionProvider;
	private File					photofile;
	private ImageBroadcastReceiver	mReceiver;
	private ArrayList<String>		datosPersonales;

	@Override
	public int getTitleResourceId() {
		return R.string.carddemo_title_birthday_card;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		datosPersonales = this.getArguments().getStringArrayList("datosPersonales");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.demo_fragment_birthday_card,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mScrollView = (ScrollView) getView().findViewById(
				R.id.card_scrollview);
		initCard();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.v("InicioFragmant", "terminando");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mReceiver != null)
			getActivity().unregisterReceiver(mReceiver);
	}

	/**
	 * This method builds a simple card
	 */
	private void initCard() {

		// Create a Card
		birthCard = new GoogleNowBirthCard(getView().getContext(),datosPersonales);
		birthCard.setId("myId");

		// Set card in the cardView
		cardView = (CardView) getView().findViewById(
				R.id.carddemo_cardBirth);
		cardView.setCard(birthCard);
	}

	private void updateIntentToShare() {
		if (mShareActionProvider != null) {

			photofile = BitmapUtils.createFileFromBitmap(cardView
					.createBitmap());
			getActivity().invalidateOptionsMenu();
		}
	}

	private Intent getShareIntent() {
		if (photofile != null) {
			return BitmapUtils.createIntentFromImage(photofile);
		} else {
			return getDefaultIntent();
		}
	}

	/**
	 * Defines a default (dummy) share intent to initialze the action provider.
	 * However, as soon as the actual content to be used in the intent is known
	 * or changes, you must update the share intent by again calling
	 * mShareActionProvider.setShareIntent()
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * Broadcast for image downloaded by CardThumbnail
	 */
	private class ImageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				boolean result = extras
						.getBoolean(Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED_EXTRA_RESULT);
				String id = extras
						.getString(Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED_EXTRA_CARD_ID);
				if (result) {
					if (id != null && id.equalsIgnoreCase(birthCard.getId())) {
						updateIntentToShare();
					}
				}
			}
		}
	}

}
