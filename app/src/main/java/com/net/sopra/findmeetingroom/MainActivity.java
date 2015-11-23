package com.net.sopra.findmeetingroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private TextView b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (TextView) findViewById(R.id.Apropos);
        b.setOnTouchListener(this);
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

            // Si l'identifiant de la vue est celui du premier bouton
            case R.id.Apropos:
    /* Agir pour notre "label" */


                break;
            case R.id.Connect:
    /* Agir pour la pression du bouton connect*/


                break;


    /* etc. */
        }


    }
}
