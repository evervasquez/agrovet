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

import it.gmariotti.cardslib.library.internal.CardExpand;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agrovet.es.R;

/**
 * This class provides a simple example of expand area
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowExpandCard extends CardExpand {
	private ArrayList<String>	datosPersonales;

	public GoogleNowExpandCard(Context context, ArrayList<String> datos) {
		super(context, R.layout.carddemo_googlenow_inner_expand);
		this.datosPersonales = datos;
	}
	
	public GoogleNowExpandCard(Context context) {
		super(context, R.layout.carddemo_googlenow_inner_expand);
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		if (view == null)
			return;

		// Retrieve TextView elements
		TextView tx1 = (TextView) view.findViewById(R.id.carddemo_expand_text1);
		TextView tx2 = (TextView) view.findViewById(R.id.carddemo_expand_text2);
		TextView tx3 = (TextView) view.findViewById(R.id.carddemo_expand_text3);
		TextView tx4 = (TextView) view.findViewById(R.id.carddemo_expand_text4);
		TextView tx5 = (TextView) view.findViewById(R.id.carddemo_expand_text5);

		tx1.setText("Perfil :  " + datosPersonales.get(2));
		tx2.setText("Usuario :  " + datosPersonales.get(6));
		tx3.setText("Fecha nacimiento :  " + datosPersonales.get(5));
		tx4.setText("DNI :  " + datosPersonales.get(4));
		tx5.setText("Telefono :  " + datosPersonales.get(7));

	}

}
