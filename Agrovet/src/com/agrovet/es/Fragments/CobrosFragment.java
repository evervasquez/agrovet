package com.agrovet.es.Fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agrovet.es.MainActivity;
import com.agrovet.es.R;
import com.agrovet.es.libs.verifica_internet;
import com.agrovet.es.models.Movimientoss;
import com.agrovet.es.utils.AgrovetUtils;
import com.agrovet.es.utils.ConstantsUtils;
import com.agrovet.es.utils.SwipeDismissTouchListener;

/**
 * List of expandable cards Example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class CobrosFragment extends BaseFragment {
	public static final String	metodo		= "movimientosDinero";
	public static final String	TAG			= "CobrosFragment";
	public String				fechaR		= null;
	public ProgressDialog		pd;
	public Context				context;
	private TextView			tvDisplayDate;
	private View				root;
	public static ArrayList<Movimientoss> movimientos;
	private final Calendar		mCalendar	= Calendar.getInstance();


	private int					day			= mCalendar
													.get(Calendar.DAY_OF_MONTH);

	private int					month		= mCalendar.get(Calendar.MONTH);

	private int					year		= mCalendar.get(Calendar.YEAR);

	@Override
	public int getTitleResourceId() {
		return R.string.carddemo_title_list_expand;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.cobros_fragment, container, false);
		context = getActivity();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tvDisplayDate = (TextView) getView().findViewById(R.id.tvDate);

		tvDisplayDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// nothing to do, just to let onTouchListener work

			}
		});

		tvDisplayDate.setOnTouchListener(new SwipeDismissTouchListener(
				tvDisplayDate, null,
				new SwipeDismissTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(Object token) {
						return true;
					}

					@Override
					public void onDismiss(View view, Object token) {
						resetDate();
					}
				}));

		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
				new OnDateSetListener() {

					public void onDateSet(DatePickerDialog datePickerDialog,
							int year, int month, int day) {

						tvDisplayDate.setText(new StringBuilder()
								.append(pad(day)).append("-")
								.append(pad(month + 1)).append("-")
								.append(pad(year)));
						tvDisplayDate.setTextColor(getResources().getColor(
								android.R.color.holo_blue_dark));
						fechaR = pad(year) + "-" + pad(month + 1) + "-"
								+ pad(day);
						caja(fechaR);

					}

				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH));

		getActivity().findViewById(R.id.btnChangeDate).setOnClickListener(
				new OnClickListener() {

					private String	tag;

					@Override
					public void onClick(View v) {
						datePickerDialog.show(getActivity()
								.getFragmentManager(), tag);
					}
				});
	}

	private void caja(String fecha) {
		int numslash = MainActivity.url.indexOf('/');
		String url_servidor;
		if (numslash > 3) {
			url_servidor = MainActivity.url + ConstantsUtils.CONTROLLER + "/"
					+ metodo;
		} else {
			url_servidor = MainActivity.url + "/" + ConstantsUtils.CONTROLLER
					+ "/" + metodo;
		}
		// Toast.makeText(getApplicationContext(), url_servidor,
		// Toast.LENGTH_SHORT).show();
		if(verifica_internet.checkConex(getActivity().getApplicationContext())) {
		new movimientosAsyncTask(url_servidor, fecha).execute();
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Verifique su conección de Internet", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void resetDate() {
		tvDisplayDate.setText(new StringBuilder().append(pad(day)).append("-")
				.append(pad(month + 1)).append("-").append(pad(year)));
		tvDisplayDate.setTextColor(getResources().getColor(
				android.R.color.darker_gray));

	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getBaseContext());
		boolean previouslyStarted = prefs.getBoolean(
				getString(R.string.app_name), false);
		if (!previouslyStarted) {
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean(getString(R.string.app_name), Boolean.TRUE);
			edit.commit();
		}

	}

	private class movimientosAsyncTask extends AsyncTask<String, Void, Void> {
		private String			url, content, datos;
		private AgrovetUtils	agrovetutils;
		private String			fecha;
		private Movimientoss mov;
		
		public movimientosAsyncTask(String url, String fecha) {
			this.url = url;
			this.fecha = fecha;
		}
		
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(context, "", "Descargando Movimientos...",
					true);
			String param;
			try {
				Log.v(TAG, fecha);
				param = URLEncoder.encode("fecha", "UTF-8") + "="
						+ URLEncoder.encode(fecha, "UTF-8");
				datos = param;
				
			} catch(UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String... arg0) {
			agrovetutils = new AgrovetUtils(getActivity()
					.getApplicationContext(), url, datos);
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

				// recuperamos el array
				JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

				int lengthJsonArr = jsonMainNode.length();

			//	if (lengthJsonArr > 0) {
					movimientos = new ArrayList<Movimientoss>();
					for (int i = 0; i < lengthJsonArr; i++) {
						/****** Get Object for each JSON node. ***********/
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						mov = new Movimientoss();
						
						
						mov.setConcepto(jsonChildNode.optString("XCONCEPTO"));
						mov.setFecha(jsonChildNode.optString("FECHA"));
						mov.setMonto(jsonChildNode.optString("MONTO"));
						mov.setTipoconcepto(jsonChildNode.optString("ID_TIPOCONCEPTO"));
						movimientos.add(mov);
					}
					
					FragmentManager fm = getFragmentManager();
		            FragmentTransaction ft = fm.beginTransaction();
		            BaseFragment fragTwo = new ListCobrosFragments();
		            
		            ft.replace(R.id.listaFragment, fragTwo);
		            ft.commit();
		         
			} catch(JSONException e) {

				e.printStackTrace();
			}
		}



	}

}
