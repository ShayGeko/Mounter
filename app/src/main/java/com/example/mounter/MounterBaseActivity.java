package com.example.mounter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mounter.login.LoginActivity;

import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public abstract class MounterBaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = mounter.currentUser();
        if(user == null || !user.isLoggedIn()){
            Intent intent = new Intent(this, LoginActivity.class);

            finish();
            startActivity(intent);
        }
    }
}
