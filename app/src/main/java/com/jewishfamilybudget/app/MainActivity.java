package com.jewishfamilybudget.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.graphics.Color;

public class MainActivity extends Activity {
  private WebView web;
  @Override public void onCreate(Bundle b){ super.onCreate(b); requestWindowFeature(Window.FEATURE_NO_TITLE); web=new WebView(this); web.setBackgroundColor(Color.WHITE); setContentView(web); WebSettings s=web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setAllowFileAccess(true); s.setAllowContentAccess(true); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false); web.setWebViewClient(new WebViewClient()); web.loadUrl("file:///android_asset/index.html"); }
  @Override public void onBackPressed(){ if(web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
