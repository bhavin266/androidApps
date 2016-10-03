package com.cs478.project.cardealers;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Bhavin on 02-10-2016.
 */
public class CustomListAdaptor extends BaseAdapter {

    private final Activity context;
    private final TypedArray thumbnails;
    private final String[] cars;
    private static LayoutInflater inflater=null;



    public CustomListAdaptor(Activity context, String[] cars, TypedArray thumbnails) {
       //super(context, R.layout.activity_main, cars);
        this.context = context;
        this.cars = cars;
        this.thumbnails = thumbnails;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return cars.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
     //   LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.single_car, null, true);

        TextView manufacturer = (TextView) rowView.findViewById(R.id.manufacturerTextView);
        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);

        manufacturer.setText(cars[position]);
        thumbnail.setImageDrawable(thumbnails.getDrawable(position));

        return rowView;
    }
}
