package com.cs478.project.chicagohub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        Log.i("Receiver", "Broadcast received");
        //Called when hotel intent is received
        if (action.equals("com.cs478.project.hotelIntent")) {
            Log.i("Receiver", "Hotel broadcast received");
            Intent intentInd = new Intent();
            intentInd.setClassName(context,"com.cs478.project.chicagohub.HotelActivity");
            intentInd.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentInd);
        }
        //Called when restaurant intent is received
        if (action.equals("com.cs478.project.restaurantIntent")) {
            Log.i("Receiver", "Restaurant broadcast received");
            Intent intentInd = new Intent();
            intentInd.setClassName(context,"com.cs478.project.chicagohub.RestaurantActivity");
            intentInd.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentInd);    }
    }
}
