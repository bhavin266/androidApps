package com.cs478.project.simplereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        //Called when hotel intent is received
        if (action.equals("com.cs478.project.hotelIntent")) {
            Toast.makeText(context, "User chose Hotels", Toast.LENGTH_SHORT).show();
        }
        //Called when restaurant intent is received
        if (action.equals("com.cs478.project.restaurantIntent")) {
            Toast.makeText(context, "User chose Restaurants", Toast.LENGTH_SHORT).show();
        }
    }
}
