package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

public class SearchActivity extends Activity implements View.OnClickListener {

    private Collection<String> sOptions;

    private Button bSearch;
    private Button bSpecifics;

    private EditText text;
    private EditText textBis;
    private DatePickerDialog mDiag;
    private DatePickerDialog mDiagBis;
    private SimpleDateFormat dateF;

    private EditText hBegin;
    private EditText hEnd;
    private TimePickerDialog tDiag;
    private TimePickerDialog tDiagBis;
    private SimpleDateFormat dateFTime;

    private Context ref;

    private ArrayAdapter<String> adapterL;
    private ArrayAdapter<String> adapterB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        setSpinner();
        initDPTPNP();

        bSearch = (Button) findViewById(R.id.Search);
        bSearch.setOnClickListener(this);
        bSpecifics = (Button) findViewById(R.id.Specifics);
        bSpecifics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Date:
                mDiag.show();
                break;

            case R.id.DateF:
                mDiagBis.show();
                break;

            case R.id.TimeA:
                tDiag.show();
                break;

            case R.id.TimeB:
                tDiagBis.show();
                break;

            case R.id.Specifics:
                this.showOptions();
                break;

            case R.id.Search:
                Spinner spinnerL = (Spinner) findViewById(R.id.LocationsS);
                String selectedLocation = spinnerL.getSelectedItem().toString();
                EditText etextBD = (EditText) findViewById(R.id.Date);
                String selectedBeginDate = etextBD.getText().toString();
                EditText etextBH = (EditText) findViewById(R.id.TimeA);
                String selectedBeginHour = etextBH.getText().toString();
                EditText etextED= (EditText) findViewById(R.id.DateF);
                String selectedEndDate = etextED.getText().toString();
                EditText etextEH = (EditText) findViewById(R.id.TimeB);
                String selectedEndHour = etextEH.getText().toString();
                EditText etextN = (EditText) findViewById(R.id.NumberPeople);
                String selectedNumber = etextN.getText().toString();

                SimpleDateFormat parsertime = new SimpleDateFormat("HH:mm");
                SimpleDateFormat parserdate = new SimpleDateFormat("dd/MM/yyyy");
                Date selectedEndDateDATE;
                Date selectedBeginDateDATE;
                Date selectedBeginHourTIME;
                Date selectedEndHourTIME;
                Boolean check7 = Boolean.FALSE;
                Boolean check8 = Boolean.FALSE;
                try {
                    selectedEndDateDATE = parserdate.parse(selectedEndDate);
                    selectedBeginDateDATE = parserdate.parse(selectedBeginDate);
                    selectedBeginHourTIME = parsertime.parse(selectedBeginHour);
                    selectedEndHourTIME = parsertime.parse(selectedEndHour);
                    int tempdate = (selectedBeginDateDATE.compareTo(selectedEndDateDATE));
                    if (tempdate>0){
                        check7 = Boolean.TRUE;
                    }
                    int temptime = (selectedBeginHourTIME.compareTo(selectedEndHourTIME));
                    if (temptime>=0){
                        check7 = Boolean.TRUE;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Boolean check1 = (selectedLocation.equals(""));
                Boolean check2 = (selectedBeginDate.equals(""));
                Boolean check3 = (selectedEndDate.equals(""));
                Boolean check4 = (selectedBeginHour.equals(""));
                Boolean check5 = (selectedEndHour.equals(""));
                Boolean check6 = (selectedNumber.equals(""));

                if (check1 || check2 || check3 || check4 || check5 || check6 || check7 || check8)
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.sel_error), Toast.LENGTH_LONG).show();
                else {
                    retrieveR();
                    Results();
                }
                break;
        }
    }

    // used to set the content of the Spinners
    public void setSpinner() {

        final String[] locationstemp = ProfileActivity.loadArray(WebServiceTask.preferencesLocations, this);
        final String[] locationstempID = ProfileActivity.loadArray(WebServiceTask.preferencesLocationsID, this);

        final String[] buildingstemp = ProfileActivity.loadArray(WebServiceTask.preferencesBuildings, this);
        final String[] buildingstempIDREF = ProfileActivity.loadArray(WebServiceTask.preferencesBuildingsIDREF, this);

        String theL = ProfileActivity.loadString(WebServiceTask.favoriteLocation, this);
        String theB = ProfileActivity.loadString(WebServiceTask.favoriteBuilding, this);

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

        final Spinner spinnerL = (Spinner) findViewById(R.id.LocationsS);

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

        final Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsS);

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

    private void initDPTPNP() {
        dateF = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        dateFTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);

        text = (EditText) findViewById(R.id.Date);
        text.setInputType(InputType.TYPE_NULL);
        text.setOnClickListener(this);
        textBis = (EditText) findViewById(R.id.DateF);
        textBis.setInputType(InputType.TYPE_NULL);
        textBis.setOnClickListener(this);

        hBegin = (EditText) findViewById(R.id.TimeA);
        hBegin.setInputType(InputType.TYPE_NULL);
        hBegin.setOnClickListener(this);
        hEnd = (EditText) findViewById(R.id.TimeB);
        hEnd.setInputType(InputType.TYPE_NULL);
        hEnd.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        mDiag = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                text.setText(dateF.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mDiagBis = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textBis.setText(dateF.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        tDiag = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minutes) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hour);
                newTime.set(Calendar.MINUTE, minutes);
                hBegin.setText(dateFTime.format(newTime.getTime()));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        tDiagBis = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minutes) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hour);
                newTime.set(Calendar.MINUTE, minutes);
                hEnd.setText(dateFTime.format(newTime.getTime()));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    // inflating the options selector
    protected void showOptions() {
        sOptions = new TreeSet<String>(Collator.getInstance());
        View newView = getLayoutInflater().inflate(R.layout.options, null, false);
        newView.setBackgroundColor(Color.WHITE);

        String[] options = ProfileActivity.loadArray(WebServiceTask.preferencesSpecifications, this);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, options);
        ListView lv = (ListView) newView.findViewById(R.id.OptionsList);

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sOptions.contains((String) parent.getItemAtPosition(position)))
                    sOptions.remove((String) parent.getItemAtPosition(position));
                else
                    sOptions.add((String) parent.getItemAtPosition(position));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(newView);
        builder.create();
        builder.show();
    }

    public void retrieveR() {

        WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_REQ_TASK, this, getResources().getString(R.string.req));

        Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsS);
        String selectedBuilding = spinnerB.getSelectedItem().toString();
        Spinner spinnerL = (Spinner) findViewById(R.id.LocationsS);
        String selectedLocation = spinnerL.getSelectedItem().toString();
        EditText etextBD = (EditText) findViewById(R.id.Date);
        String selectedBeginDate = etextBD.getText().toString();
        EditText etextBH = (EditText) findViewById(R.id.TimeA);
        String selectedBeginHour = etextBH.getText().toString();
        EditText etextED= (EditText) findViewById(R.id.DateF);
        String selectedEndDate = etextED.getText().toString();
        EditText etextEH = (EditText) findViewById(R.id.TimeB);
        String selectedEndHour = etextEH.getText().toString();
        EditText etextN = (EditText) findViewById(R.id.NumberPeople);
        String selectedNumber = etextN.getText().toString();

        String selectedOptions = "";
        String[] optionstemp = ProfileActivity.loadArray(WebServiceTask.preferencesSpecifications, this);
        String[] optionstempID = ProfileActivity.loadArray(WebServiceTask.preferencesSpecificationsID, this);
        int nbO = optionstemp.length;
        String toption ="";
        if (sOptions!=null) {
            Iterator<String> iterator = sOptions.iterator();
            while (iterator.hasNext()) {
                toption = iterator.next();
                for (int i = 0; i < nbO; i++) {
                    if (optionstemp[i].equals(toption))
                        toption = optionstempID[i];
                }
                if (selectedOptions.equals(""))
                    selectedOptions = toption;
                else
                    selectedOptions = selectedOptions + "#" + toption;
            }
        }

        String[] locationstemp = ProfileActivity.loadArray(WebServiceTask.preferencesLocations, this);
        String[] locationstempID = ProfileActivity.loadArray(WebServiceTask.preferencesLocationsID, this);
        int nbL = locationstemp.length;
        for (int i = 0; i < nbL; i++) {
            if (locationstemp[i].equals(selectedLocation))
                selectedLocation = locationstempID[i];
        }

        String[] buildingstemp = ProfileActivity.loadArray(WebServiceTask.preferencesBuildings, this);
        String[] buildingstempIDREF = ProfileActivity.loadArray(WebServiceTask.preferencesBuildingsIDREF, this);
        int nbB = buildingstemp.length;
        for (int i = 0; i < nbB; i++) {
            if (buildingstemp[i].equals(selectedBuilding))
                selectedBuilding = buildingstempIDREF[i];
        }

        wst.addNameValuePair("selectedBuilding", selectedBuilding);
        wst.addNameValuePair("selectedLocation", selectedLocation);
        wst.addNameValuePair("selectedBeginDate", selectedBeginDate);
        wst.addNameValuePair("selectedBeginHour", selectedBeginHour);
        wst.addNameValuePair("selectedEndDate", selectedEndDate);
        wst.addNameValuePair("selectedEndHour", selectedEndHour);
        wst.addNameValuePair("selectedNumber", selectedNumber);
        wst.addNameValuePair("selectedOptions", selectedOptions);

        String theURL = getResources().getString(R.string.services_url) + "roomlist";
        //String theURL = "http://pastebin.com/raw/1jeP7EDM";
        wst.execute(theURL);

        /*
            test avec pastebin :

            [{"ID":1,"idBuilding":1,"nom":"N1","etage":0},{"ID":2,"idBuilding":1,"nom":"N2","etage":1},{"ID":3,"idBuilding":1,"nom":"N3","etage":2}]
         */
    }

    public void Results() {
        Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
        startActivity(intent);
    }

}
