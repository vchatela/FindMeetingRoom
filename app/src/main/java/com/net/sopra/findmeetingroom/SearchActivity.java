package com.net.sopra.findmeetingroom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
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

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
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
                retrieveR();
                Results();
                break;
        }
    }

    // used to set the content of the Spinners
    public void setSpinner() {
        String[] buildingstemp = ProfileActivity.loadArray(WebServiceTask.preferencesBuildings, this);
        String[] locationstemp = ProfileActivity.loadArray(WebServiceTask.preferencesLocations, this);

        String theB = ProfileActivity.loadString(WebServiceTask.favoriteBuilding, this);
        String theL = ProfileActivity.loadString(WebServiceTask.favoriteLocation, this);

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

        final Spinner spinnerB = (Spinner) findViewById(R.id.BuildingsS);
        final Spinner spinnerL = (Spinner) findViewById(R.id.LocationsS);

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

        String[] options = ProfileActivity.loadArray(WebServiceTask.preferencesOptions, this);

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

        WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_REQ_TASK, this, "Inquiring...");

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
        if (sOptions!=null) {
            Iterator<String> iterator = sOptions.iterator();
            while (iterator.hasNext()) {
                if (selectedOptions == "")
                    selectedOptions = iterator.next();
                else
                    selectedOptions = selectedOptions + "#" + iterator.next();
            }
        }

        wst.addNameValuePair("selectedBuilding", selectedBuilding);
        wst.addNameValuePair("selectedLocation", selectedLocation);
        wst.addNameValuePair("selectedBeginDate", selectedBeginDate);
        wst.addNameValuePair("selectedBeginHour", selectedBeginHour);
        wst.addNameValuePair("selectedEndDate", selectedEndDate);
        wst.addNameValuePair("selectedEndHour", selectedEndHour);
        wst.addNameValuePair("selectedNumber", selectedNumber);
        wst.addNameValuePair("selectedOptions", selectedOptions);

        wst.execute(new String[]{getResources().getString(R.string.services_url) });
    }

    public void Results() {
        Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
        startActivity(intent);
    }

}
