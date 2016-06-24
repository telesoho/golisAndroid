package jp.co.ucl.golis;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tanwenhong on 2016/05/25.
 */
public class MyAppWebViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // Force links and redirects to open in the WebView instead of in a browser
        return false;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        view.getContext().startActivity(intent);
//        return true;
    }
//
    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        if (errorCode < 0) {
            webView.loadUrl("file:///android_asset/error.html");  //  エラーページを表示
        }
    }

}