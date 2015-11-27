package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setprofile);
    }





  protected void ButtonOK()
  {
      Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
      startActivity(intent);

  }






}