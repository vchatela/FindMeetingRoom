package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity implements View.OnClickListener {

    private Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setprofile);

        OK = (Button) findViewById(R.id.Oprofile);
        OK.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Oprofile:
                this.buttonOK();
                break;
        }
    }

    protected void buttonOK()
    {
        onBackPressed();
    }
}