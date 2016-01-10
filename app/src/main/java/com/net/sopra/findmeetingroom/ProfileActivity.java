package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class ProfileActivity extends Activity implements View.OnClickListener {

    private Button OK;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private Context ref;

    private ArrayAdapter<String> adapterL;
    private ArrayAdapter<String> adapterB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setprofile);

        OK = (Button) findViewById(R.id.Oprofile);
        OK.setOnClickListener(this);
        setSpinner();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Oprofile:

                Spinner spinnerL = (Spinner) findViewById(R.id.LocationsP);
                String favL = spinnerL.getSelectedItem().toString();

                // saving the choices
                if (favL == getResources().getString(R.string.loc))
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.sel_error), Toast.LENGTH_LONG).show();
                else {
                    Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsP);
                    String favB = spinnerB.getSelectedItem().toString();

                    SharedPreferences prefs = this.getSharedPreferences(WebServiceTask.preferencesname, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(WebServiceTask.favoriteBuilding, favB);
                    editor.putString(WebServiceTask.favoriteLocation, favL);
                    editor.commit();

                    this.buttonOK();
                }

                break;
        }
    }

    // used to retrieve an array of String from the SharedPreferences
    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(WebServiceTask.preferencesname, Context.MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    // used to retrieve a simple String from the SharedPreferences
    public static String loadString(String key, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(WebServiceTask.preferencesname, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    // used to set the content of the Spinners
    public void setSpinner() {

        final String[] locationstemp = loadArray(WebServiceTask.preferencesLocations, this);
        final String[] locationstempID = loadArray(WebServiceTask.preferencesLocationsID, this);

        final String[] buildingstemp = loadArray(WebServiceTask.preferencesBuildings, this);
        final String[] buildingstempIDREF = loadArray(WebServiceTask.preferencesBuildingsIDREF, this);

        String theL = loadString(WebServiceTask.favoriteLocation, this);
        String theB = loadString(WebServiceTask.favoriteBuilding, this);

        final int nbL = locationstemp.length;
        final int nbB = buildingstemp.length;

        String[] locations;

        String IDtheL;
        IDtheL = "";
        if (theL == null) {
            theL = getResources().getString(R.string.loc);
            locations = new String[nbL + 1];
            locations[0] = theL;
            System.arraycopy(locationstemp, 0, locations, 1, locationstemp.length);
        }
        else {
            for (int i = 0; i < nbL; i++) {
                if (locationstemp[i] == theL)
                    IDtheL = locationstempID[i];
            }
            locations = locationstemp;
        }

        final Spinner spinnerL = (Spinner) findViewById(R.id.LocationsP);

        adapterL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        spinnerL.setAdapter(adapterL);
        spinnerL.setPrompt(getResources().getString(R.string.loc));
        spinnerL.setSelection(adapterL.getPosition(theL));

        String[] buildings;

        if (theB == null) {
            theB = getResources().getString(R.string.bui);
            buildings = new String[1];
            buildings[0] = theB;
        } else {
            int nbPossib = 0;
            for (int i = 0; i < nbB; i++) {
                if (buildingstempIDREF[i] == IDtheL)
                    nbPossib++;
            }
            buildings = new String[nbPossib];
            int j=0;
            for (int i = 0; i < nbB; i++) {
                if (buildingstempIDREF[i] == IDtheL) {
                    buildings[j] = buildingstemp[i];
                    j++;
                }
            }
        }

        final Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsP);

        adapterB = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings);
        spinnerB.setAdapter(adapterB);
        spinnerB.setPrompt(getResources().getString(R.string.bui));
        spinnerB.setSelection(adapterB.getPosition(theB));

        ref = this;

        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String temp = spinnerL.getSelectedItem().toString();

                String[] buildings;

                if (temp != getResources().getString(R.string.loc)) {

                    String IDtheL;
                    IDtheL = null;
                    for (int i = 0; i < nbL; i++) {
                        if (temp.equals(locationstemp[i]))
                            IDtheL = locationstempID[i];
                    }

                    int nbPossib = 0;
                    for (int i = 0; i < nbB; i++) {
                        if (IDtheL.equals(buildingstempIDREF[i]))
                            nbPossib++;
                    }

                    buildings = new String[nbPossib];

                    int j = 0;
                    for (int i = 0; i < nbB; i++) {
                        if (IDtheL.equals(buildingstempIDREF[i])) {
                            buildings[j] = buildingstemp[i];
                            j++;
                        }
                    }
                }
                else {
                    buildings = new String[1];
                    buildings[0] = getResources().getString(R.string.bui);
                }

                adapterB = new ArrayAdapter<String>(ref, android.R.layout.simple_spinner_item, buildings);
                spinnerB.setAdapter(adapterB);
                spinnerB.setSelection(0);

                // AE
                for (int z = 0; z < buildings.length; z++) {
                    System.out.println(buildings[z]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    protected void buttonOK() {
        onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.net.sopra.findmeetingroom/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.net.sopra.findmeetingroom/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}