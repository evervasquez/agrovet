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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agrovet.es.MainActivity;
import com.agrovet.es.ProductosActivity;
import com.agrovet.es.R;
import com.agrovet.es.libs.verifica_internet;
import com.agrovet.es.models.Almacen;
import com.agrovet.es.utils.AgrovetUtils;
import com.agrovet.es.utils.ConstantsUtils;

/**
 * List of Google Play cards Example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class AlmacenFragment extends BaseFragment {
	public static final String	metodo	= "seleccionaAlmacenes";
	public static final String	TAG		= "AlmacenFragment";
	public ProgressDialog		pd;

	public Almacen				almacen;

	@Override
	public int getTitleResourceId() {
		return R.string.carddemo_title_list_gplaycard;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.demo_fragment_list_gplaycard,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		almacenes();
		// initCards();

	}

	private void almacenes() {
		int numslash = MainActivity.url.indexOf('/');
		String url_servidor;
		if (numslash > 3) {
			url_servidor = MainActivity.url + ConstantsUtils.CONTROLLER + "/"
					+ metodo;
		} else {
			url_servidor = MainActivity.url + "/" + ConstantsUtils.CONTROLLER
					+ "/" + metodo;
		}
		
		if (verifica_internet.checkConex(getActivity().getApplicationContext())) {
			new almacenesAsyncTask(url_servidor).execute();
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Verifique su conección de Internet", Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void initCards(ArrayList<Almacen> a) {

		// Init an array of Cards
		ArrayList<Card> cards = new ArrayList<Card>();
		GooglePlaySmallCard card;
		for (int i = 0; i < a.size(); i++) {
			card = new GooglePlaySmallCard(this.getActivity());
			card.setTitle(a.get(i).getNombreAlmacen());
			card.setSecondaryTitle(a.get(i).getUbicacion());
			card.setRating((float) (Math.random() * (5.0)));
			card.count = i;
			card.setType(a.get(i).getId_almacen());
			card.setResourceIdThumbnail(R.drawable.ic_launcher);

			card.init();

			cards.add(card);
		}

		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(
				getActivity(), cards);

		CardListView listView = (CardListView) getActivity().findViewById(
				R.id.carddemo_list_gplaycard);
		if (listView != null) {
			listView.setAdapter(mCardArrayAdapter);
		}
	}

	/**
	 * This class provides a simple card as Google Play
	 * 
	 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
	 */

	public class GooglePlaySmallCard extends Card {

		protected TextView	mTitle;
		protected TextView	mSecondaryTitle;
		protected RatingBar	mRatingBar;
		protected int		resourceIdThumbnail;
		protected int		count;

		protected String	title;
		protected String	secondaryTitle;
		protected float		rating;

		public GooglePlaySmallCard(Context context) {
			this(context, R.layout.carddemo_mycard_inner_content);
		}

		public GooglePlaySmallCard(Context context, int innerLayout) {
			super(context, innerLayout);
			// init();
		}

		private void init() {

			// Add thumbnail
			CardThumbnail cardThumbnail = new CardThumbnail(mContext);

			cardThumbnail.setDrawableResource(R.drawable.ic_std_launcher);

			addCardThumbnail(cardThumbnail);

			setOnClickListener(new OnCardClickListener() {
				@Override
				public void onClick(Card card, View view) {
					Intent intent = new Intent(getContext(),
							ProductosActivity.class);
					intent.putExtra("ID_ALMACEN", card.getType());
					startActivity(intent);
				}
			});

		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {

			// Retrieve elements
			mTitle = (TextView) parent
					.findViewById(R.id.carddemo_myapps_main_inner_title);
			mSecondaryTitle = (TextView) parent
					.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);
			mRatingBar = (RatingBar) parent
					.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);

			if (mTitle != null)
				mTitle.setText(title);

			if (mSecondaryTitle != null)
				mSecondaryTitle.setText(secondaryTitle);

			if (mRatingBar != null) {
				mRatingBar.setNumStars(5);
				mRatingBar.setMax(5);
				mRatingBar.setStepSize(0.5f);
				mRatingBar.setRating(rating);
			}

		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSecondaryTitle() {
			return secondaryTitle;
		}

		public void setSecondaryTitle(String secondaryTitle) {
			this.secondaryTitle = secondaryTitle;
		}

		public float getRating() {
			return rating;
		}

		public void setRating(float rating) {
			this.rating = rating;
		}

		public int getResourceIdThumbnail() {
			return resourceIdThumbnail;
		}

		public void setResourceIdThumbnail(int resourceIdThumbnail) {
			this.resourceIdThumbnail = resourceIdThumbnail;
		}
	}

	private class almacenesAsyncTask extends AsyncTask<String, Void, Void> {
		private String				url, content;
		private AgrovetUtils		agrovetutils;
		private ArrayList<Almacen>	almacenes;

		public almacenesAsyncTask(String url) {
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getView().getContext(), "",
					"Verificando datos...", true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			agrovetutils = new AgrovetUtils(getActivity()
					.getApplicationContext(), url, "");
			content = agrovetutils.getResponse();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			JSONObject jsonResponse;

			pd.dismiss();

			Log.v(TAG, content);

			try {
				jsonResponse = new JSONObject(content);
				JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
				int tamañoJsonArray = jsonMainNode.length();
				Log.v(TAG, tamañoJsonArray + "");

				if (tamañoJsonArray > 0) {
					almacenes = new ArrayList<Almacen>();

					for (int i = 0; i < tamañoJsonArray; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						almacen = new Almacen();
						almacen.setId_almacen(jsonChildNode
								.optInt("ID_ALMACEN"));
						almacen.setNombreAlmacen(jsonChildNode
								.optString("DESCRIPCION"));
						almacen.setUbicacion(jsonChildNode
								.optString("UBICACION"));

						almacenes.add(almacen);
					}

					initCards(almacenes);
				}

			} catch(JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
}
