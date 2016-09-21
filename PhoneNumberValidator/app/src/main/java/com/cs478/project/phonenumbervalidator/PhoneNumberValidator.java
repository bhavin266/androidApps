package com.cs478.project.phonenumbervalidator;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator extends AppCompatActivity {
    EditText inputNumber;
    TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_validator);
        Button callButton = (Button) findViewById(R.id.callButton);
        inputNumber = (EditText) findViewById(R.id.mobNumeditText);
        err = (TextView) findViewById(R.id.error);

        callButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        String phone = inputNumber.getText().toString();
                        err.setText("");
                        if (isValid(phone)) {
                            Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
                            Log.e("this", "valid number" + phone);
                            phoneIntent.setData(Uri.parse("tel:" + phone));
                            startActivity(phoneIntent);
                        }
                        else {
                            err.setText("Invalid phone number!\nRequired format (xxx) xxx-yyyy or (xxx)xxx-yyyy");
                        }
                    }
                }
        );
    }

    public boolean isValid(String textString){
        Pattern pattern = Pattern.compile("\\(\\d{3}\\)\\s?\\d{3}-\\d{4}");
        Matcher matchPattern = pattern.matcher(textString);
        if(matchPattern.find()){
            return true;
        }
        return false;
    }

  /*  @Override
    public void onBackPressed() {
        // do something on back.
        startActivity(new Intent(this,MainActivity.class));
        return;
    }*/
}
