package com.net.sopra.findmeetingroom;

import android.support.v7.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity{

    private Button myB1;
    private Button myB2;
    private final static int ID_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views retrieval and listener association
        myB1 = (Button)findViewById(R.id.b1);
        myB2 = (Button)findViewById(R.id.b2);
        myB1.setOnClickListener(b1listener);
        myB2.setOnClickListener(b2listener);
    }

    private OnClickListener b1listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private OnClickListener b2listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(ID_DIALOG);
        }
    };

    public Dialog onCreateDialog(int id) {
        Dialog myD = new Dialog(this);
        myD.setTitle("Edition du profil...");
        return myD;
    }
}
