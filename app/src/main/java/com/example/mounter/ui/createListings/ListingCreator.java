package com.example.mounter.ui.createListings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.mounter.R;

public class ListingCreator extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_creator);

        ImageButton submit = findViewById(R.id.submit);
        ImageButton back = findViewById(R.id.back);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i("MyApp", "Clicked on submit");
                intent = new Intent(getApplicationContext(), ChooseListing.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Log.i("MyApp", "Clicked on back");
                intent = new Intent(getApplicationContext(), ChooseListing.class);
                startActivity(intent);
                finish();
            }
        });
    }

}