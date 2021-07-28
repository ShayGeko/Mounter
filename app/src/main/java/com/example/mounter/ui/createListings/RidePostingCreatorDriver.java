package com.example.mounter.ui.createListings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mounter.Mounter;
import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.mounter.ui.createListings.RidePostingCreator;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import io.realm.Realm;

public class RidePostingCreatorDriver extends RidePostingCreator{

    private Intent intent;
    private RidePostingModel ridePostingModel;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText fillTo;
    private TextInputEditText fillFrom;
    private TextInputEditText fillHourOfDeparture;
    private TextInputEditText fillEstimatedPrice;
    private TextInputEditText fillDescription;
    private Button fillDate;
    private Button submit;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_creator_driver);
        ridePostingModel = new RidePostingModel(Mounter.mounter.currentUser());
        initDatePicker();

        fillTo = findViewById(R.id.fillTo);
        fillFrom = findViewById(R.id.fillFrom);
        fillHourOfDeparture = findViewById(R.id.fillHourOfDeparture);
        fillEstimatedPrice = findViewById(R.id.fillEstimatedPrice);
        fillDescription = findViewById(R.id.fillDescription);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        fillDate = findViewById(R.id.fillDate);
        fillDate.setText(getCurrentDate());


        submit.setOnClickListener(view -> {
            Log.i("MyApp", "Clicked on SUBMIT");
            CharSequence to = fillTo.getText();
            CharSequence from = fillFrom.getText();
            CharSequence hourOfDeparture = fillHourOfDeparture.getText();
            CharSequence estimatedPrice = fillEstimatedPrice.getText();
            CharSequence date = fillDate.getText();
            CharSequence description = fillDescription.getText();
            if(to.length() == 0 || from.length() == 0 || fillHourOfDeparture.length() == 0){
                hideKeyboard(this);
                Log.i("MyApp", "Key details are not filled in!");
                fillTo.setText("");
                fillFrom.setText("");
                fillHourOfDeparture.setText("");
                fillEstimatedPrice.setText("");
                fillDescription.setText("");
                Snackbar.make(view, "Please make sure to fill in all key components.",
                        Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return;
            }
            //Setting all the data into the ridePosting model
            ridePostingModel.setDestinationAddress(to.toString());
            ridePostingModel.setOriginAddress(from.toString());
            ridePostingModel.setDepartureTime(date.toString() + " " + hourOfDeparture.toString());
            ridePostingModel.setEstimatedPrice(estimatedPrice.toString());

            //Inputs the collected data into the database
            @NotNull
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.insert(ridePostingModel);
                }
            }, new Realm.Transaction.OnSuccess(){
                @Override
                public void onSuccess() {
                    realm.close();
                }
            }, new Realm.Transaction.OnError(){
                @Override
                public void onError(Throwable error){
                    //TODO: Transaction failed, do something!
                    realm.close();
                }
            });
            finish();
        });


        back.setOnClickListener(view -> {
            Log.i("MyApp", "Clicked on BACK");
            intent = new Intent(getApplicationContext(), ChooseListing.class);
            startActivity(intent);
            finish();
        });

    }

}