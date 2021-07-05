package com.example.mounter.ui.createListings;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

import com.example.mounter.R;
import com.google.android.material.snackbar.Snackbar;

import static android.view.Gravity.CENTER;

public class CreateListings extends AppCompatActivity {

    private boolean userIsDriver;   //Determines whether user has driving privileges or not.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userIsDriver = false;
        ImageButton passenger = findViewById(R.id.passenger_imgButton);
        ImageButton driver = findViewById(R.id.driver_imgButton);


        passenger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i("MyApp", "Passenger image button is working!");
                Snackbar.make(v, "Nice! You clicked the Passenger's listing.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                if(!userIsDriver){
                    Snackbar.make(v, "Please make sure to fill our Driver's form " +
                            "\nbefore making a Driver's listing.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

}