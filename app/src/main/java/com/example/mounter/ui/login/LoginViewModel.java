package com.example.mounter.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.mounter.data.LoginRepository;
import com.example.mounter.data.Result;
import com.example.mounter.data.model.LoggedInUserModel;
import com.example.mounter.R;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.RealmAsyncTask;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        try {
            Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);

            AtomicReference<User> user = new AtomicReference<>();
            RealmAsyncTask loginTask = mounter.loginAsync(emailPasswordCredentials, it -> {
                if(it.isSuccess()){
                    user.set(mounter.currentUser());

                    loginResult.setValue(new LoginResult(new LoggedInUserView(email)));
                }
                else{
                    System.out.println(it.getError());
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            });

        } catch (Exception e) {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}