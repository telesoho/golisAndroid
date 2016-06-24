package jp.co.ucl.golis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // preferenceに格納されている値を取得 20160615追加
    public String getIpUrl() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString("settingtextbox", "https://www.google.com");
    }

    private WebView mWebView;

    /**
     * Load page to webView by url
     *
     * @param url
     */
    private void loadPage(String url) {

        if (url == null || url.isEmpty()) {
            mWebView.loadUrl("file:///android_asset/hello.html");
        } else {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mWebView = (WebView) findViewById(R.id.webView);
        if (mWebView == null) {
            return;
        }
        WebSettings webSettings = mWebView.getSettings();
        // Enable javascript
        webSettings.setJavaScriptEnabled(true);

        // enable to load page from cache while network is not available.
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

        if (!isNetworkAvailable()) { // loading offline
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webSettings.setDefaultTextEncodingName("UTF-8");

        mWebView.addJavascriptInterface(new WebAppInterface(this), "Golis");
        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new MyAppWebViewClient());

        //  接続先に何も入力されていなかった場合エラーページを表示
        if (getIpUrl() == null || getIpUrl().isEmpty()) {
            mWebView.loadUrl("file:///android_asset/error.html");
        } else {
            loadPage(getIpUrl());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //  OptionMenuの表示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //  OptionMenuの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  設定画面が押下された時の処理
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent1 = new android.content.Intent(this, SettingsPreferenceActivity.class);
                startActivity(intent1);
                return true;
            //  終了が押下された時の処理
            case R.id.finish:
//                this.finish();
                this.moveTaskToBack(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  Navegationbarの表示
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            loadPage(null);
        } else if (id == R.id.nav_gallery) {
            //  nav_galleryが選択されたときに preferenceに保存されているURLを渡す
            loadPage(getIpUrl());

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //    戻るボタンが押下された時、WebViewに履歴がある場合は戻り、履歴がない場合は戻るボタンを無効
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                mWebView.goBack();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Parsing bar code reader result
                    IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                    if (scanningResult != null) {
                        String scanContent = scanningResult.getContents();
                        String scanFormat = scanningResult.getFormatName();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                scanContent + ":" + scanContent , Toast.LENGTH_SHORT);
                        toast.show();
                        mWebView.loadUrl("javascript:onReadBarcode('"+ scanContent + "')");
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No scan data received!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
        }
    }
}
