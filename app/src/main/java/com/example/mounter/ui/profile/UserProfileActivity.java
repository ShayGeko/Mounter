package com.example.mounter.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.mounter.ui.MounterBaseActivity;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.example.mounter.databinding.ActivityUserProfileBinding;

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