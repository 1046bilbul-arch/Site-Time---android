package com.homestead.sitetime;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.Manifest;
import android.content.pm.PackageManager;
public class MainActivity extends Activity {
 WebView view;
 @Override public void onCreate(Bundle b){super.onCreate(b);view=new WebView(this);setContentView(view);WebSettings s=view.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setGeolocationEnabled(true);view.setWebViewClient(new WebViewClient());view.setWebChromeClient(new WebChromeClient(){@Override public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback cb){if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},9);}cb.invoke(origin,checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED,false);} });view.loadUrl("file:///android_asset/index.html");}
 @Override public void onBackPressed(){if(view.canGoBack())view.goBack();else super.onBackPressed();}
}