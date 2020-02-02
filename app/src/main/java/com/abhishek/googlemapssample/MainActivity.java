package com.abhishek.googlemapssample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    public final static int ERROR_CODE = 9001;

    Button mapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapButton = findViewById(R.id.buttonMaps);
        if(is_available())
            init();
    }

    private void init() {
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public boolean is_available()
    {
        int check = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(check == ConnectionResult.SUCCESS)
        {
            Log.d("bosdk","MAPS IS WOKKING PERFECTLy");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(check))
        {
            Log.d("bosdk","MAPS HAS MINOR ISSUE");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,check,ERROR_CODE);
            dialog.show();
            return true;
        }
        else
        {
            Toast.makeText(this,"WE CANT CONNECT",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
