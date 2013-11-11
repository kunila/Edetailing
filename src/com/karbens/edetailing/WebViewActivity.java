package com.karbens.edetailing;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{

	WebView mWebView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		mWebView = (WebView) findViewById(R.id.pdfWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		
		mWebView.setWebViewClient(new Callback());
	       
        String pdfURL = "http://www.newamericancentury.org/RebuildingAmericasDefenses.pdf";
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);

    }

    private class Callback extends WebViewClient {
    	
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }
		
		
		
	}

