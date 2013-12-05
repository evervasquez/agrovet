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

package com.agrovet.es.cards;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agrovet.es.R;
import com.agrovet.es.libs.verifica_internet;
/**
 * This class provides a simple card as Google Now Birthday
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowBirthCard extends Card {
	
	private ArrayList<String> datosPersonales;
	
    public GoogleNowBirthCard(Context context, ArrayList<String> a) {
        super(context);
        datosPersonales = a;
        init();
    }

  /*  public GoogleNowBirthCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }*/

    private void init() {

        //Add Header
        GoogleNowBirthHeader header = new GoogleNowBirthHeader(getContext(), R.layout.carddemo_googlenowbirth_inner_header);
        header.setButtonExpandVisible(true);
        header.mName = datosPersonales.get(0);
        header.mSubName = datosPersonales.get(1);
        addCardHeader(header);

        //Add Expand Area
        CardExpand expand = new GoogleNowExpandCard(getContext(),datosPersonales);
        addCardExpand(expand);

        //Set clickListener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card", Toast.LENGTH_LONG).show();
            }
        });
        
        //Add Thumbnail
        GoogleNowBirthThumb thumbnail = new GoogleNowBirthThumb(getContext());
        if(verifica_internet.checkConex(getContext())) {
        thumbnail.setUrlResource("https://plus.google.com/s2/photos/profile/114432517923423045208?sz=250");
        }else {
        	thumbnail.setDrawableResource(R.drawable.avatar);	
        }
        addCardThumbnail(thumbnail);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.card_main_inner_simple_title);
        title.setText("Bienvenido "+ datosPersonales.get(2));
        title.setTextColor(mContext.getResources().getColor(R.color.carddemo_googlenowbirth_title));
        title.setGravity(Gravity.CENTER_VERTICAL);

    }


    class GoogleNowBirthThumb extends CardThumbnail {

        public GoogleNowBirthThumb(Context context) {
            super(context);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            viewImage.getLayoutParams().width = 120;
            viewImage.getLayoutParams().height = 120;
        }
    }


    class GoogleNowBirthHeader extends CardHeader {

        String mName;
        String mSubName;

        public GoogleNowBirthHeader(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView txName = (TextView) view.findViewById(R.id.text_birth1);
            TextView txSubName = (TextView) view.findViewById(R.id.text_birth2);

            txName.setText(mName);
            txSubName.setText(mSubName);
        }
    }
}
