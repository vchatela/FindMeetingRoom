package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ProfileActivity extends Activity implements View.OnClickListener {

    private Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setprofile);

        OK = (Button) findViewById(R.id.Oprofile);
        OK.setOnClickListener(this);
        setSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Oprofile:

                // saving the choices
                Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsP);
                Spinner spinnerL = (Spinner) findViewById(R.id.LocationsP);
                String favB = spinnerB.getSelectedItem().toString();
                String favL = spinnerL.getSelectedItem().toString();
                SharedPreferences prefs = this.getSharedPreferences(WebServiceTask.preferencesname, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(WebServiceTask.favoriteBuilding, favB);
                editor.putString(WebServiceTask.favoriteLocation, favL);
                editor.commit();

                this.buttonOK();

                break;
        }
    }

    // used to retrieve an array of String from the SharedPreferences
    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(WebServiceTask.preferencesname, Context.MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
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
        String[] buildingstemp = loadArray(WebServiceTask.preferencesBuildings, this);
        String[] locationstemp = loadArray(WebServiceTask.preferencesLocations, this);

        String theB = loadString(WebServiceTask.favoriteBuilding, this);
        String theL = loadString(WebServiceTask.favoriteLocation, this);

        if  (theB==null)
            theB = "Batiment";
        if  (theL==null)
            theL = "Site";

        String[] buildings = new String[buildingstemp.length + 1];
        String[] locations = new String[locationstemp.length + 1];
        locations[0]=theL;
        buildings[0]=theB;
        System.arraycopy(locationstemp, 0, locations, 1, locationstemp.length);
        System.arraycopy(buildingstemp, 0, buildings, 1, buildingstemp.length);

        final Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsP);
        final Spinner spinnerL = (Spinner) findViewById(R.id.LocationsP);

        ArrayAdapter<String> adapterB = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, buildings) {

            @Override
            public int getCount() {
                int c = super.getCount();
                if (spinnerB.getSelectedItemPosition() < c - 1) return c;
                return c > 0 ? c - 1 : c;
            }
        };
        spinnerB.setAdapter(adapterB);

        ArrayAdapter<String> adapterL = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locations) {

            @Override
            public int getCount() {
                int c = super.getCount();
                if (spinnerL.getSelectedItemPosition() < c - 1) return c;
                return c > 0 ? c - 1 : c;
            }
        };
        spinnerL.setAdapter(adapterL);
    }

    protected void buttonOK()
    {
        onBackPressed();
    }

}