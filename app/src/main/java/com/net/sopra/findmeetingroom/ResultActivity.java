package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResultActivity extends Activity {

    public static String baseResult;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        final String[] contents = baseResult.split("#");
        int nb = contents.length;
        String[] items = new String[nb];
        for(int i=0;i<nb;i++) {
            items[i] = "Créneau " + ("" + (i+1));
        }

        lv = (ListView) findViewById(R.id.resultsList);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), contents[position], Toast.LENGTH_LONG).show();
            }
        });
    }

}
