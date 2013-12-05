package com.agrovet.es;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agrovet.es.libs.CustomTypeFace;
import com.agrovet.es.libs.verifica_internet;
import com.agrovet.es.utils.AgrovetUtils;
import com.agrovet.es.utils.ConstantsUtils;
import com.beardedhen.androidbootstrap.BootstrapButton;

public class MainActivity extends Activity {

	private static final String	TAG				= "MainActivity";
	private static final int	RESULT_SETTING	= 1;
	private static final String	metodo			= "loginAndroid";
	public static String		url				= null;
	public static String		id_sucursal		= null;
	String						datos;
	private EditText			txt_usuario, txt_pass;
	private BootstrapButton		btnlogin, btnweb;
	private String				usuario, pass;
	public ProgressDialog		pd;
	public Context				context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView custom = (TextView) findViewById(titleId);
		custom.setTextAppearance(getApplicationContext(), R.style.CustomText);
		custom.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());
		context = this;
		txt_usuario = (EditText) findViewById(R.id.text_usuario);
		txt_pass = (EditText) findViewById(R.id.text_pass);
		btnlogin = (BootstrapButton) findViewById(R.id.btn_login);
		btnweb = (BootstrapButton) findViewById(R.id.btn_web);

		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.url = getUrl();
				if (!MainActivity.url.equals("nada")) {
					usuario = txt_usuario.getText().toString().trim();
					pass = txt_pass.getText().toString().trim();
					if (!usuario.equals("") && !pass.equals("")) {

						login(usuario, pass);

					} else {
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.error_login),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.error_url),
							Toast.LENGTH_SHORT).show();
					openOptionsMenu();
				}
			}
		});

		btnweb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intentweb = new Intent(getApplicationContext(), WebActivity.class);
				startActivity(intentweb);
			}
		});

		// agrovetutil = new AgrovetUtils(getApplicationContext(),
		// MainActivity.url, param);
	}

	// optenemos la url de sharePrefrences
	private String getUrl() {
		SharedPreferences pref = getSharedPreferences("AgrovetPreferences",
				MODE_PRIVATE);
		return pref.getString("url", "nada");
	}

	private void login(String usuario, String pass) {
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
		if (verifica_internet.checkConex(getApplicationContext())) {
			new datosAsyncTask(url_servidor, usuario, pass).execute();
		}else{
			Toast.makeText(getApplicationContext(), "Verifique su conección de Internet",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:
				Intent intent = new Intent(MainActivity.this,
						ConfigActivity.class);
				startActivityForResult(intent, RESULT_SETTING);
				break;

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	// metodo para recuperar la url de preferences
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case RESULT_SETTING:
				showSettings();
				break;

			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showSettings() {
		SharedPreferences mSharePreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String url = mSharePreferences.getString("prefUrl", "NULL");
		String web = mSharePreferences.getString("prefWeb", "NULL");

		mSharePreferences = getSharedPreferences("AgrovetPreferences",
				MODE_PRIVATE);

		// guardamos las preferencias en un xml
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putString("url", url);
		editor.putString("web", web);
		editor.commit();
	}

	private class datosAsyncTask extends AsyncTask<String, Void, Void> {
		private String			usuario, clave, url;
		private AgrovetUtils	agrovetutils;
		private String			content;
		private Intent			intent;

		public datosAsyncTask(String url, String user, String pass) {
			this.url = url;
			this.usuario = user;
			this.clave = pass;
		}

		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			pd = ProgressDialog.show(context, "", "Verificando datos...", true);
			try {
				String param = URLEncoder.encode("usuario", "UTF-8") + "="
						+ URLEncoder.encode(usuario, "UTF-8");
				param += "&" + URLEncoder.encode("clave", "UTF-8") + "="
						+ URLEncoder.encode(clave, "UTF-8");
				datos = param;
			} catch(UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(String... arg0) {
			agrovetutils = new AgrovetUtils(getApplicationContext(), url, datos);
			content = agrovetutils.getResponse();
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			String OutputData = "";
			JSONObject jsonResponse;
			ArrayList<String> datosUsusario;

			pd.dismiss();

			Log.v(TAG, content);

			try {

				jsonResponse = new JSONObject(content);

				// recuperamos el array
				JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

				int lengthJsonArr = jsonMainNode.length();

				if (lengthJsonArr > 0) {
					datosUsusario = new ArrayList<String>();
					for (int i = 0; i < lengthJsonArr; i++) {
						/****** Get Object for each JSON node. ***********/
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						datosUsusario.add(jsonChildNode.optString("NOMBRE")
								.toString());
						datosUsusario.add(jsonChildNode.optString("APELLIDO")
								.toString());
						datosUsusario.add(jsonChildNode
								.optString("DESCRIPCION").toString());
						datosUsusario.add(jsonChildNode.optString("DIRECCION")
								.toString());
						datosUsusario.add(jsonChildNode.optString("DNI")
								.toString());
						datosUsusario.add(jsonChildNode.optString(
								"FECHANACIMIENTO").toString());
						datosUsusario.add(jsonChildNode.optString("USUARIO")
								.toString());
						datosUsusario.add(jsonChildNode.optString("TELEFONO")
								.toString());
						MainActivity.id_sucursal = jsonChildNode.optString(
								"ID_SUCURSAL").toString();

					}

					Log.v(TAG, OutputData);
					intent = new Intent(getApplicationContext(),
							IndexActivity.class);
					intent.putExtra("datosUsuario", datosUsusario);
					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(),
							"Ingrese correctamente sus datos",
							Toast.LENGTH_SHORT).show();
				}
			} catch(JSONException e) {

				e.printStackTrace();
			}
		}

	}
}
