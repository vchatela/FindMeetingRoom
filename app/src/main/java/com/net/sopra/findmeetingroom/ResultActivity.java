package com.net.sopra.findmeetingroom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class ResultActivity extends Activity {

    public static String baseResult;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        String[] partsR;
        baseResult="[{\"ID\":1,\"idBuilding\":1,\"nom\":\"Salle 1\",\"etage\":1},{\"ID\":4,\"idBuilding\":1,\"nom\":\"Salle 4\",\"etage\":3},{\"ID\":8,\"idBuilding\":1,\"nom\":\"Salle 8\",\"etage\":0},{\"ID\":13,\"idBuilding\":1,\"nom\":\"Salle 13\",\"etage\":1}]";

        if(baseResult==null || baseResult=="") {
            partsR = new String[1];
            partsR[0] = getResources().getString(R.string.aures);
        }
        else {
            StringReader reader = new StringReader(baseResult);
            Gson gson = new Gson();
            Type typeR = new TypeToken<ArrayList<Room>>() {
            }.getType();
            ArrayList<Room> roomsList = gson.fromJson(reader, typeR);
            int nbR = roomsList.size();
            partsR = new String[nbR];

            int i = 0;
            Iterator<Room> itR = roomsList.iterator();
            Room tempR;
            while (itR.hasNext()) {
                tempR = itR.next();
                partsR[i] = tempR.getnom() + " (" + String.valueOf(tempR.getetage()) + ")";
                i++;
            }
        }

        lv = (ListView) findViewById(R.id.resultsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partsR);
        lv.setAdapter(adapter);
    }

}
