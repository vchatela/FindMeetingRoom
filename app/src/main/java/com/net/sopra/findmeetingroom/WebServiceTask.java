package com.net.sopra.findmeetingroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class WebServiceTask extends AsyncTask<String, Integer, String> {

    public static final String preferencesname = "FMRprefs";
    public static final String preferencesBuildings = "bPrefs";
    public static final String preferencesBuildingsIDREF = "bPrefsIDREF";
    public static final String preferencesLocations = "lPrefs";
    public static final String preferencesLocationsID = "lPrefsID";
    public static final String preferencesSpecifications = "sPrefs";

    public static final String favoriteBuilding = "fBui";
    public static final String favoriteLocation = "fLoc";

    public static final int GET_BL_TASK = 1;
    public static final int GET_LL_TASK = 2;
    public static final int GET_SL_TASK = 3;
    public static final int POST_REQ_TASK = 4;

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

                case GET_BL_TASK:
                case GET_LL_TASK:
                case GET_SL_TASK:

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

    // TRAITEMENTS
    public void handleResponse(String response) {

        try {

            // saving in SharedPreferences
            SharedPreferences prefs = mContext.getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            //String reader;
            StringReader reader = new StringReader(response);

            Gson gson = new Gson();

            int i;

            switch (taskType) {

                case GET_BL_TASK:

                    Type typeB = new TypeToken<ArrayList<Building>>() {}.getType();
                    ArrayList<Building> buildingsList = gson.fromJson(reader, typeB);
                    int nbB = buildingsList.size();
                    String[] partsB = new String[nbB];
                    String[] partsBIDREF = new String[nbB];

                    i=0;
                    Iterator<Building> itB = buildingsList.iterator();
                    Building tempB;
                    while (itB.hasNext()) {
                        tempB = itB.next();
                        partsB[i] = tempB.getnom();
                        partsBIDREF[i] = String.valueOf(tempB.getLocation());
                        i++;
                    }

                    editor.putInt(preferencesBuildings + "_size", nbB);
                    editor.putInt(preferencesBuildingsIDREF + "_size", nbB);
                    for(int j=0;j<nbB;j++) {
                        editor.putString(preferencesBuildings + "_" + j, partsB[j]);
                        editor.putString(preferencesBuildingsIDREF + "_" + j, partsBIDREF[j]);
                    }
                    editor.commit();

                    break;

                case GET_LL_TASK:

                    Type typeL = new TypeToken<ArrayList<Location>>() {}.getType();
                    ArrayList<Location> locationsList = gson.fromJson(reader, typeL);
                    int nbL = locationsList.size();
                    String[] partsL = new String[nbL];
                    String[] partsLID = new String[nbL];

                    i=0;
                    Iterator<Location> itL = locationsList.iterator();
                    Location tempL;
                    while (itL.hasNext()) {
                        tempL = itL.next();
                        partsL[i] = tempL.getnom();
                        partsLID[i] = String.valueOf(tempL.getID());
                        i++;
                    }

                    editor.putInt(preferencesLocations + "_size", nbL);
                    editor.putInt(preferencesLocationsID + "_size", nbL);
                    for(int j=0;j<nbL;j++) {
                        editor.putString(preferencesLocations + "_" + j, partsL[j]);
                        editor.putString(preferencesLocationsID + "_" + j, partsLID[j]);
                    }
                    editor.commit();

                    break;

                case GET_SL_TASK:

                    Type typeS = new TypeToken<ArrayList<Specification>>() {}.getType();
                    ArrayList<Specification> specificationsList = gson.fromJson(reader, typeS);
                    int nbS = specificationsList.size();
                    String[] partsS = new String[nbS];

                    i=0;
                    Iterator<Specification> itS = specificationsList.iterator();
                    while (itS.hasNext()) {
                        partsS[i] = itS.next().getSpecificationName();
                        i++;
                    }

                    editor.putInt(preferencesSpecifications + "_size", nbS);
                    for(int j=0;j<nbS;j++) {
                        editor.putString(preferencesSpecifications + "_" + j, partsS[j]);
                    }
                    editor.commit();

                    break;

                case POST_REQ_TASK:

                    ResultActivity.baseResult = response;
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