package com.cs478.project.playerclient;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //array to store all db data
        String songs[] = new String[1000];
        try {
            if (Player.mPlaybackService != null) {
                //get data from DB to array if service not null
                songs = Player.mPlaybackService.getPlayData();
            }
        } catch (RemoteException e) {
            e.getMessage();
        }
        if (songs != null) {
            //Initialize list if songArr has data
            List<String> songActivity = new ArrayList<String>();

            //Iterating over the array
            for (String song : songs) {
                if (song != null && song.length() > 0) {
                    //adding data to list
                    songActivity.add(song);
                }
            }

            ListView listView = (ListView) findViewById(R.id.audioList);
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, songActivity);
            listView.setAdapter(adapter);

        }

    }
}
