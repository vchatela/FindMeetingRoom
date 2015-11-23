package com.net.sopra.findmeetingroom;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private TextView b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (TextView)findViewById(R.id.About);
        b.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

        // On récupère l'identifiant de la vue, et en fonction de cet identifiant…
        switch(v.getId()) {

            case R.id.About:
                this.showAbout();
                break;
            case R.id.connect:
                Intent intent = new Intent(MainActivity.this, MenuActivity.class );
                startActivity(intent);
                break;
        }
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();

    }
}
