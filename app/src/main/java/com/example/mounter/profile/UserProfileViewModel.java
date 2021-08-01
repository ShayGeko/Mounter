package com.example.mounter.profile;

import androidx.lifecycle.ViewModel;

import com.example.mounter.data.RealmLiveObject;
import com.example.mounter.data.realmModels.UserInfoModel;

import org.bson.types.ObjectId;

import io.realm.Realm;

import static com.example.mounter.Mounter.mounter;

public class UserProfileViewModel extends ViewModel {
    private Realm mRealm;
    private RealmLiveObject<UserInfoModel> userInfo;
    public UserProfileViewModel(){
        mRealm = Realm.getDefaultInstance();
        userInfo = loadUserInfo();
    }
    public RealmLiveObject<UserInfoModel> getUserInfo(){
        return userInfo;
    }

    private RealmLiveObject<UserInfoModel> loadUserInfo(){
        return new RealmLiveObject<>(mRealm.where(UserInfoModel.class)
                .equalTo("_userId",mounter.currentUser().getId())
                .findFirstAsync());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mRealm.close();
    }
}
