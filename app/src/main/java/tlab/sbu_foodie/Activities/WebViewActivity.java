package tlab.sbu_foodie.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import tlab.sbu_foodie.DataHandler.ExtendedDataHolder;
import tlab.sbu_foodie.R;

/**
 * Created by ilsung on 8/15/2018.
 */

public class WebViewActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView =findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        String pdfUrl = (String)ExtendedDataHolder.getInstance().getExtra("pdfUrl");

        if (pdfUrl != null && pdfUrl.length() != 0) {
            webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+pdfUrl);
        } else {
            Toast.makeText(getBaseContext(),"Sorry, could not open the dining hour pdf file",Toast.LENGTH_SHORT);
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}