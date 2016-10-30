package com.cs478.project.broadcaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Intent to be broadcasted when user chooses HOTELS
    private static final String HOTEL_INTENT =
            "com.cs478.project.hotelIntent";
    //Intent to be broadcasted when user chooses RESTAURANTS
    private static final String RESTAURANT_INTENT =
            "com.cs478.project.restaurantIntent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button hotels = (Button) findViewById(R.id.hotelButton);
        Button restaurants = (Button) findViewById(R.id.restaurantsButton);

        hotels.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hotelIntent = new Intent();
                hotelIntent.setAction(HOTEL_INTENT);
                hotelIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); //Also include stopped packages, in case receiver app is not started
                sendOrderedBroadcast(hotelIntent,null);
            }
        });


        restaurants.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent restaurantIntent = new Intent();
                restaurantIntent.setAction(RESTAURANT_INTENT);
                restaurantIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendOrderedBroadcast(restaurantIntent,null);
            }
        });
    }
}
