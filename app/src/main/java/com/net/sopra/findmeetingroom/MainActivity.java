package com.net.sopra.findmeetingroom;

// import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private TextView b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (TextView) findViewById(R.id.about);
        b.setOnTouchListener(this);
        b.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

        // getting the view id and reacting accordingly
        switch(v.getId()) {

            case R.id.about:
                break;

            case R.id.connect:
                break;
        }
    }
}
