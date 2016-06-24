package jp.co.ucl.golis;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by TWH on 2016/06/06.
 */
public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void scanBarcode() {
        IntentIntegrator scanIntegrator = new IntentIntegrator((MainActivity)mContext);
        scanIntegrator.initiateScan();
    }

}
