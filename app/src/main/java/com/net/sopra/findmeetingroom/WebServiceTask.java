package com.net.sopra.findmeetingroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WebServiceTask extends AsyncTask<String, Integer, String> {

    public static final String preferencesname = "FMRprefs";
    public static final String preferencesBuildings = "bPrefs";
    public static final String preferencesLocations = "lPrefs";
    public static final String preferencesOptions = "oPrefs";

    public static final String favoriteBuilding = "fBui";
    public static final String favoriteLocation = "fLoc";

    public static final int GET_PREF_TASK = 1;
    public static final int POST_REQ_TASK = 2;

    private static final String TAG = "WebServiceTask";

    // connection and socket timeouts, in milliseconds
    private static final int CONN_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;

    private int taskType;

    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

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
        }
        else {

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
            switch (taskType) {

                case POST_REQ_TASK:

                    HttpPost httppost = new HttpPost(url);
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    response = httpclient.execute(httppost);
                    break;

                case GET_PREF_TASK:

                    HttpGet httpget = new HttpGet(url);
                    response = httpclient.execute(httpget);
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return response;
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();
        // wrapping a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // response read until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        // returning the full string
        return total.toString();
    }

    // TRAITEMENTS A FAIRE EVOLUER
    public void handleResponse(String response) {

        try {
            JSONObject jso = new JSONObject(response);

            switch (taskType) {

                case GET_PREF_TASK:

                    // example of expected JSON : {"buildingsList":"B1/B2","locationList":"L1/L2","optionsList":"Telephone/Videoprojecteur","result":""}
                    String buildingsList = jso.getString("buildingsList");
                    String locationsList = jso.getString("locationList");
                    String optionsList = jso.getString("optionsList");

                    String[] partsB = buildingsList.split("#");
                    int nbB = partsB.length;
                    String[] partsL = locationsList.split("#");
                    int nbL = partsL.length;
                    String[] partsO = optionsList.split("#");
                    int nbO = partsL.length;

                    // saving in SharedPreferences
                    SharedPreferences prefs = mContext.getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putInt(preferencesBuildings + "_size", nbB);
                    for(int i=0;i<nbB;i++) {
                        editor.putString(preferencesBuildings + "_" + i, partsB[i]);
                    }
                    editor.putInt(preferencesLocations +"_size", nbL);
                    for(int i=0;i<nbL;i++) {
                        editor.putString(preferencesLocations + "_" + i, partsL[i]);
                    }
                    editor.putInt(preferencesOptions +"_size", nbO);
                    for(int i=0;i<nbO;i++) {
                        editor.putString(preferencesOptions + "_" + i, partsO[i]);
                    }
                    editor.commit();
                    break;

                case POST_REQ_TASK:

                    ResultActivity.baseResult = jso.getString("result");
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }

    public void addNameValuePair(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

}