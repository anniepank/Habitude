package com.github.anniepank.hability.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.anniepank.hability.R;
import com.github.anniepank.hability.data.Settings;

public class LoginActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("/appKey/")) {
                    String key = url.substring(url.lastIndexOf("/") + 1).replace("#", "");
                    Log.i("KEY: ", key);
                    Settings.get(LoginActivity.this).syncKey = key;
                    Settings.get(LoginActivity.this).save(LoginActivity.this);
                    finish();
                }
            }
        });
        webView.getSettings().setUserAgentString("user-agent-string");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://habitude.by:9000/api/app-google-login");
    }

}
