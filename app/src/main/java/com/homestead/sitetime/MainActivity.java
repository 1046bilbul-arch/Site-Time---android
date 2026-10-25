package com.homestead.sitetime;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

public class MainActivity extends Activity {
  private WebView view;
  private WebView printView;

  public class PrintBridge {
    @JavascriptInterface
    public void printReport(final String reportText) {
      runOnUiThread(() -> showPrintDialog(reportText));
    }
  }

  private String escapeHtml(String text) {
    return text.replace("&", "&amp;").replace("<", "&lt;")
      .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
  }

  private void showPrintDialog(String reportText) {
    if (printView != null) {
      printView.destroy();
      printView = null;
    }
    final WebView printable = new WebView(this);
    printView = printable;
    printable.getSettings().setDefaultTextEncodingName("UTF-8");
    String html = "<!doctype html><html><head><meta charset='UTF-8'>" +
      "<meta name='viewport' content='width=device-width,initial-scale=1'>" +
      "<style>@page{size:A4;margin:16mm}body{font-family:sans-serif;color:#222;font-size:11pt}" +
      "h1{font-size:19pt;margin-bottom:16px}pre{font-family:sans-serif;" +
      "white-space:pre-wrap;overflow-wrap:anywhere;line-height:1.55}</style></head>" +
      "<body><h1>Site Time Report</h1><pre>" + escapeHtml(reportText) + "</pre></body></html>";
    printable.setWebViewClient(new WebViewClient() {
      private boolean started = false;
      @Override
      public void onPageFinished(WebView webView, String url) {
        if (started) return;
        started = true;
        PrintManager manager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        if (manager != null && !isFinishing()) {
          manager.print("Site Time Report", printable.createPrintDocumentAdapter("Site Time Report"),
            new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4)
              .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).build());
        }
      }
    });
    printable.loadDataWithBaseURL("https://sitetime.local/", html, "text/html", "UTF-8", null);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = new WebView(this);
    setContentView(view);
    WebSettings settings = view.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setGeolocationEnabled(true);
    view.addJavascriptInterface(new PrintBridge(), "SiteTimePrint");
    view.setWebViewClient(new WebViewClient());
    view.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 9);
        }
        callback.invoke(origin, checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED, false);
      }
    });
    view.loadUrl("file:///android_asset/index.html");
  }

  @Override
  public void onBackPressed() {
    if (view.canGoBack()) view.goBack();
    else super.onBackPressed();
  }
}
