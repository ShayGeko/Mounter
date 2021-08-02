package com.example.mounter.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mounter.MounterBaseActivity;
import com.example.mounter.R;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.example.mounter.databinding.ActivityLoginBinding;
import com.example.mounter.databinding.ActivityUserProfileBinding;

import io.realm.Realm;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class UserProfileActivity extends MounterBaseActivity {
    private UserProfileViewModel viewModel;

    private ActivityUserProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.getUserInfo().observe(this, userInfo -> {
            if(userInfo.isLoaded()){
                setUpUserProfile(userInfo);
            }
        });

        ImageButton back = binding.back;
        back.setOnClickListener(view -> {
            finish();
        });
    }

    private void setUpUserProfile(UserInfoModel userInfo){
        binding.userName.setText(userInfo.getName());
        binding.userSurname.setText(userInfo.getSurname());
        binding.rating.setText(""+userInfo.getRating());
        binding.sex.setText(userInfo.getSex());
    }
}