package com.cs478.project.cardealers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class viewImage extends AppCompatActivity {
        int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        TextView car=(TextView)findViewById(R.id.carTextView);
        ImageView image=(ImageView) findViewById(R.id.image);
        position= (int) getIntent().getExtras().get("id");
        image.setImageDrawable( getResources().obtainTypedArray(R.array.images).getDrawable(position));
        car.setText(getResources().getStringArray(R.array.cars)[position]);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] webLinksArray = getResources().getStringArray(R.array.webLinksArray);
                String url = webLinksArray[position];
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            }
        });

    }
}
