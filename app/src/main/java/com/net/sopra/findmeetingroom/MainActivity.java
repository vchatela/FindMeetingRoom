package com.net.sopra.findmeetingroom;

// import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private TextView b;
    private final static int ID_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (TextView) findViewById(R.id.about);
        b.setOnClickListener(ablistener);
    }

    private OnClickListener ablistener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(ID_DIALOG);
        }
    };

    public Dialog onCreateDialog(int id) {
        Dialog myD = new Dialog(this);
        myD.setTitle("Informations");
        return myD;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        }
    }
}
