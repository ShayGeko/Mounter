package com.example.mounter.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mounter.R;
import com.example.mounter.data.model.UserInfoModel;
import com.example.mounter.ui.createListings.ChooseListing;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        User user = mounter.currentUser();
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(transactionRealm -> {
            UserInfoModel userInfo = transactionRealm
                    .where(UserInfoModel.class)
                    .equalTo("_userId", user.getId())
                    .findFirst();

            setUpUserProfile(userInfo);
            transactionRealm.close();
        });

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

    }



    private void setUpUserProfile(UserInfoModel userInfo){
        TextView nameTextView = findViewById(R.id.user_name);
        TextView surnameTextView = findViewById(R.id.user_surname);
        TextView ratingTextView = findViewById(R.id.rating);
        TextView sexTextView = findViewById(R.id.sex);
        nameTextView.setText(userInfo.getName());
        surnameTextView.setText(userInfo.getSurname());
        ratingTextView.setText(String.valueOf(userInfo.getRating()));
        sexTextView.setText(userInfo.getSex());
    }
}