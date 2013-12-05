package com.agrovet.es;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.agrovet.es.libs.CustomTypeFace;
import com.agrovet.es.libs.UTF8;
import com.agrovet.es.utils.AgrovetUtils;
import com.agrovet.es.utils.ConstantsUtils;

public class ProductosActivity extends Activity {
	public static final String	metodo	= "productosxAlmacen";
	public static final String	TAG		= "ProductosActivity";
	public ProgressDialog		pd;
	public int id_almacen;
	public Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		Bundle bundle = getIntent().getExtras();
		id_almacen = bundle.getInt("ID_ALMACEN", 0);	
		context = this;
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView custom = (TextView) findViewById(titleId);
		custom.setTextAppearance(getApplicationContext(), R.style.CustomText);
		custom.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());
		
		productos();
		Toast.makeText(getApplicationContext(), ""+id_almacen, Toast.LENGTH_SHORT).show();
	}
	

	private void productos() {
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
		new productosAsyncTask(url_servidor).execute();

	}

	private class productosAsyncTask extends AsyncTask<String, Void, Void> {
		private UITableView tableView1;
		private String url, content,datos;
		private AgrovetUtils		agrovetutils;
		
		public productosAsyncTask(String url) {
			tableView1 = (UITableView)findViewById(R.id.tableView);
			this.url = url;
		}

		@Override
		protected Void doInBackground(String... arg0) {
			agrovetutils = new AgrovetUtils(getApplicationContext()
					.getApplicationContext(), url, datos);
			content = agrovetutils.getResponse();
			return null;
		}
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(context, "",
					"Descargando productos...", true);
			String param;
			try {
				String _id = id_almacen+"";
				param = URLEncoder.encode("almacen", "UTF-8") + "="
						+ URLEncoder.encode(_id, "UTF-8");
				datos = param;
			} catch(UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomClickListener listener = new CustomClickListener();
			tableView1.setClickListener(listener);
			tableView1.clear();
			
			JSONObject jsonResponse;

			pd.dismiss();

			Log.v(TAG, content);
			
			try {
				jsonResponse = new JSONObject(content);
				JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
				int tamañoJsonArray = jsonMainNode.length();
				
				if(tamañoJsonArray > 0) {
					for (int i = 0; i < tamañoJsonArray; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						
						tableView1.addBasicItem(UTF8.convertirA_UTF8(jsonChildNode.optString("PPRODUCTO")),
								"Subcategoría : " + UTF8.convertirA_UTF8(jsonChildNode.optString("SSUBCATEGORIA")), 
								"Categoría : " + jsonChildNode.optString("CCATEGORIA"), "Stock : "
								+ jsonChildNode.optDouble("STOCK"),
								"Almacen : " + UTF8.convertirA_UTF8(jsonChildNode.optString("AALMACEN")));
						
					}
					
					tableView1.commit();
				}
			} catch(JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}
		
	}
	
	private class CustomClickListener implements ClickListener {
		@Override
		public void onClick(int index) {
			
		}

	}
}
