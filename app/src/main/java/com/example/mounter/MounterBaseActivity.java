package com.example.mounter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mounter.login.LoginActivity;

import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public abstract class MounterBaseActivity extends AppCompatActivity {
    protected boolean userNotLoggedIn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", "activity starting");
        User user = mounter.currentUser();
        if(user == null || !user.isLoggedIn()){
            userNotLoggedIn = true;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Log.d("BaseActivity", user.getState().toString());
        }
    }
}
