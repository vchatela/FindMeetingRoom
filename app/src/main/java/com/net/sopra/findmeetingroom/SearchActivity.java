package com.net.sopra.findmeetingroom;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchActivity extends Activity implements View.OnClickListener {

    private Button bSearch;

    private EditText text;
    private EditText textBis;
    private DatePickerDialog mDiag;
    private DatePickerDialog mDiagBis;
    private SimpleDateFormat dateF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initDP();
    }

    private void initDP() {

        dateF = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);

        text = (EditText) findViewById(R.id.Date);
        text.setInputType(InputType.TYPE_NULL);
        text.setOnClickListener(this);
        textBis = (EditText) findViewById(R.id.DateF);
        textBis.setInputType(InputType.TYPE_NULL);
        textBis.setOnClickListener(this);

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
        }
    }

}
