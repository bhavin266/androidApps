package com.cs478.project.cardealers;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    GridView carsGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carsGridView = (GridView) findViewById(R.id.gridView);
        registerForContextMenu(carsGridView);
        TypedArray thumbnails = getResources().obtainTypedArray(R.array.thumbnails);
        String[] cars = getResources().getStringArray(R.array.cars);

        CustomListAdaptor customListAdaptor = new CustomListAdaptor(this, cars, thumbnails);
        carsGridView.setAdapter(customListAdaptor);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select Action");
        menu.add(0, v.getId(), 0, "View Image");
        menu.add(0, v.getId(), 0, "Go to official website");
        menu.add(0, v.getId(), 0, "Dealers");
    }


}
