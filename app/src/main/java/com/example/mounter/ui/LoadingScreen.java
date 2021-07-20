package com.example.mounter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.mounter.R;
import com.example.mounter.ridesearch.RideSearchActivity;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_loading_screen);

        Thread LoadingThread = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                } finally {
                    Intent intent = new Intent(LoadingScreen.this, RideSearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        LoadingThread.start();
    }
}