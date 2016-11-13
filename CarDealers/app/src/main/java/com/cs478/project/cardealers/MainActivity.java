package com.cs478.project.cardealers;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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

        carsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,viewImage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",position);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select Action");
        menu.add(0, v.getId(), 0, "View Image");
        menu.add(0, v.getId(), 0, "Go to official website");
        menu.add(0, v.getId(), 0, "Dealers");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo listInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = listInfo.position;

        if(item.getTitle() == "View Image"){
            Intent intent=new Intent(MainActivity.this,viewImage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id",position);
            startActivity(intent);
        }

        if(item.getTitle() == "Go to official website"){
            String[] webLinksArray = getResources().getStringArray(R.array.webLinksArray);
            String url = webLinksArray[position];
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);
        }

        if(item.getTitle() == "Dealers"){
            String[] weblinks = getResources().getStringArray(R.array.dealerArray);
            String dealer = weblinks[position];
            int id=getResources().getIdentifier(dealer,"array",getPackageName());
            if(id!=0) {
                Intent intent = new Intent(MainActivity.this, dealers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                startActivity(intent);
            }else {
                Toast.makeText(this,"Couldnt find dealers",Toast.LENGTH_SHORT).show();

            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        carsGridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
        super.onConfigurationChanged(newConfig);
    }
}
