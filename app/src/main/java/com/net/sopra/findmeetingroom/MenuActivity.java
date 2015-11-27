package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener {

    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        b1 = (Button) findViewById(R.id.Research);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Research:
                this.goSearch();
                break;
        }
    }

    // switching to the next activity
    public void goSearch() {
        Intent intent = new Intent(MenuActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    // prevents returning to MainActivity
    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
    }

}