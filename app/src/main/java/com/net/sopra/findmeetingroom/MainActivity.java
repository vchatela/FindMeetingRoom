package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String preferencesBuildings = "bPrefs";
    private static final String preferencesLocations = "lPrefs";

    // used to print the about
    private TextView a;

    // used to launch the tool
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = (TextView) findViewById(R.id.About);
        a.setOnClickListener(this);
        a.setOnTouchListener(this);
        b = (Button) findViewById(R.id.Connect);
        b.setOnClickListener(this);
        b.setOnTouchListener(this);
    }

    // nothing on touch ?
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

        // getting the view id
        switch (v.getId()) {

            case R.id.About:
                this.showAbout();
                break;
            case R.id.Connect:
                this.retrieveLocationsAndBuildings();
                this.Menu();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.finish();
    }

    // inflating the about message content
    protected void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        TextView textView = (TextView) messageView.findViewById(R.id.AboutCredits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    // switching to the next activity
    public void Menu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void retrieveLocationsAndBuildings() {

        // A MODIFIER AVEC  A) LA BONNE IP POUR LE TEST  B) L'URL CORRECTE DU SERVICE
        String theURL = getResources().getString(R.string.services_url) + "/sample/";

        WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "Connecting...");

        wst.execute(new String[]{theURL});

    }

    public void handleResponse(String response) {

        try {

            JSONObject jso = new JSONObject(response);

            // example of expected JSON : {"buildingsList":"B1|B2","locationList":"L1|L2"}
            String buildingsList = jso.getString("buildingsList");
            String locationList = jso.getString("locationList");
            String[] partsB = buildingsList.split("|");
            int nbB = partsB.length;
            String[] partsL = buildingsList.split("|");
            int nbL = partsL.length;

            // SAVING IN SHAREDPREFERENCES
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("preferencename", 0);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt(preferencesBuildings +"_size", nbB);
            for(int i=0;i<nbB;i++)
                editor.putString(preferencesBuildings + "_" + i, partsB[i]);
            editor.putInt(preferencesLocations +"_size", nbL);
            for(int i=0;i<nbL;i++)
                editor.putString(preferencesBuildings + "_" + i, partsL[i]);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

    }

    private class WebServiceTask extends AsyncTask<String, Integer, String> {

        public static final int GET_TASK = 2;

        private static final String TAG = "WebServiceTask";

        // connection and socket timeouts, in milliseconds
        private static final int CONN_TIMEOUT = 3000;
        private static final int SOCKET_TIMEOUT = 5000;

        private int taskType = GET_TASK;
        private Context mContext = null;
        private String processMessage = "Processing...";

        private ProgressDialog pDlg = null;

        public WebServiceTask(int taskType, Context mContext, String processMessage) {

            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }

        private void showProgressDialog() {

            pDlg = new ProgressDialog(mContext);
            pDlg.setMessage(processMessage);
            pDlg.setProgressDrawable(mContext.getWallpaper());
            pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDlg.setCancelable(false);
            pDlg.show();

        }

        @Override
        protected void onPreExecute() {

            showProgressDialog();

        }

        @Override
        protected void onPostExecute(String response) {

            handleResponse(response);
            pDlg.dismiss();

        }

        protected String doInBackground(String... urls) {

            String url = urls[0];
            String result = "";

            HttpResponse response = doResponse(url);

            if (response == null) {
                return result;
            } else {

                try {

                    result = inputStreamToString(response.getEntity().getContent());

                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }

            }

            return result;
        }

        // establishing connection and socket timeouts
        private HttpParams getHttpParams() {

            HttpParams htpp = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

            return htpp;
        }

        private HttpResponse doResponse(String url) {

            HttpClient httpclient = new DefaultHttpClient(getHttpParams());

            HttpResponse response = null;

            try {
                HttpGet httpget = new HttpGet(url);
                response = httpclient.execute(httpget);

            } catch (Exception e) {

                Log.e(TAG, e.getLocalizedMessage(), e);

            }

            return response;
        }

        private String inputStreamToString(InputStream is) {

            String line = "";
            StringBuilder total = new StringBuilder();

            // wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                // read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            // return full string
            return total.toString();
        }

    }
}
