package com.cs478.project.cardealers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class dealers extends AppCompatActivity {
    ListView dealersListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers);
        dealersListView=(ListView) findViewById(R.id.listView);
        String[] dealers = getResources().getStringArray((int)getIntent().getExtras().get("id"));
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dealers);
        dealersListView.setAdapter(adapter);
    }
}
