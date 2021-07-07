package com.example.mounter.data;

import com.example.mounter.data.model.LoggedInUserModel;

import org.bson.types.ObjectId;

import java.io.IOException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUserModel> login(String username, String password) {

        try {
            LoggedInUserModel fakeUser =
                    new LoggedInUserModel(
                            new ObjectId(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}