package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

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
                this.retrieveLBO();
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

    public void retrieveLBO() {
        String theURLB = getResources().getString(R.string.services_url) + "/buildinglist";
        //String theURLB = "http://pastebin.com/raw/1jeP7EDM";
        WebServiceTask wstB = new WebServiceTask(WebServiceTask.GET_BL_TASK, this, getResources().getString(R.string.cea));
        wstB.execute(new String[]{theURLB});
        String theURLL = getResources().getString(R.string.services_url) + "/locationlist";
        //String theURLL = "http://pastebin.com/raw/C4Uvgjhp";
        WebServiceTask wstL = new WebServiceTask(WebServiceTask.GET_LL_TASK, this, getResources().getString(R.string.ceb));
        wstL.execute(new String[]{theURLL});
        String theURLS = getResources().getString(R.string.services_url) + "/specificationlist";
        //String theURLS = "http://pastebin.com/raw/x4fakwHS";
        WebServiceTask wstS = new WebServiceTask(WebServiceTask.GET_SL_TASK, this, getResources().getString(R.string.cec));
        wstS.execute(new String[]{theURLS});

        /*
            test avec pastebin :

            [{"ID":1,"nom":"BA","idLocation":1},{"ID":2,"nom":"BB","idLocation":1},{"ID":3,"nom":"BC","idLocation":2},{"ID":4,"nom":"BD","idLocation":2},{"ID":5,"nom":"BE","idLocation":3}]

            [{"ID":1,"nom":"LA"},{"ID":2,"nom":"LB"},{"ID":3,"nom":"LC"}]

            [{"ID":1,"SpecificationName":"Telephone"},{"ID":2,"SpecificationName":"Visio"}]
         */
    }

}
