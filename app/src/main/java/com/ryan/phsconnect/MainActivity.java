package com.ryan.phsconnect;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebViewClient;
import android.net.wifi.SupplicantState;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import java.util.List;

import android.util.Log;

public class MainActivity extends Activity {

    private Context theC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.theC = this;

        /*ConnectivityManager connManager = (ConnectivityManager) theC.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) theC.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
                log(connectionInfo.getSSID()); //guest
            }
        }*/


        /*WifiConfiguration conf = new WifiConfiguration();
        WifiManager wifiManager = (WifiManager)theC.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i != null && i.SSID.equals("GUEST")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
            }
        }*/

        WifiManager wifiManager = (WifiManager)theC.getSystemService(Context.WIFI_SERVICE);
        wifiManager.disconnect();
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\""+"GUEST"+"\""; //IMPORTANT! This should be in Quotes!!
        //wc.priority = 40;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int res = wifiManager.addNetwork(wc);
        log("add Network returned " + res );
        boolean es = wifiManager.saveConfiguration();
        log("saveConfiguration returned " + es );
        boolean b = wifiManager.enableNetwork(res, true);
        log("enableNetwork returned " + b );

        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("http://www.google.com");


        for(int i = 0; i < 10; i++) {
            SupplicantState supState;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            supState = wifiInfo.getSupplicantState();

            log("STATE: " + supState.toString());
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
            }
        }
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        log("connected: " + mWifi.isConnected());
        webView.loadUrl("https://webauth.internal/login.html?redirect=www.yahoo.com/");
        //if wifi connected
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl("https://webauth.internal/login.html?redirect=www.yahoo.com/");
            return true;
        }
    }

    private void log(final String message) {
        Log.e("com.ryan.phsconnect", message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
