package com.agrovet.es;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.agrovet.es.utils.verMiWeb;

public class WebActivity extends Activity {
		private WebView mWebView;
		private String web;
		private verMiWeb vermiweb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings mwebsettings = mWebView.getSettings();
		// Activo JavaScript
		mwebsettings.setJavaScriptEnabled(true);
		
		mWebView.setWebViewClient(new WebViewClient());
		//cargamos la pagina
		web = getWeb();
		
		// Cargamos la url que necesitamos
		//vermiweb = new verMiWeb();
		//vermiweb.doUpdateVisitedHistory(mWebView, web, true);
		mWebView.loadUrl(web);
		
	}
	
	private String getWeb() {
		SharedPreferences pref = getSharedPreferences("AgrovetPreferences",
				MODE_PRIVATE);
		return pref.getString("web", "nada");
	}

}
