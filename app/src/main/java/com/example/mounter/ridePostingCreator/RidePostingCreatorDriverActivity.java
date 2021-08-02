package com.example.mounter.ridePostingCreator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.mounter.Mounter;
import com.example.mounter.R;
import com.example.mounter.databinding.ActivityListingCreatorBinding;
import com.example.mounter.databinding.ActivityListingCreatorDriverBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.mounter.common.MounterDateUtil.getCurrentDate;

public class RidePostingCreatorDriverActivity extends RidePostingCreatorActivity {

    protected TextInputEditText fillEstimatedPrice;

    private ActivityListingCreatorDriverBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_creator_driver);

        initDatePicker();
        passengerVariablesPropertyInit();
        fillEstimatedPrice = findViewById(R.id.fillEstimatedPrice);
        viewModel = new ViewModelProvider(this, new RidePostingCreatorViewModelFactory((Mounter)getApplicationContext()))
                .get(RidePostingCreatorViewModel.class);

        //Retrieves data from the layout and checks if it is acceptable
        submit.setOnClickListener(view -> {
            String to = fillTo.getText().toString();
            String from = fillFrom.getText().toString();
            String hourOfDeparture = fillHourOfDeparture.getText().toString();
            String estimatedPrice = fillEstimatedPrice.getText().toString();
            String date = fillDate.getText().toString();
            String description = fillDescription.getText().toString();

            if(to.length() == 0 || from.length() == 0 || fillHourOfDeparture.length() == 0){
                hideKeyboard();
                Snackbar.make(view, R.string.fill_in_components,
                        Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return;
            }


            //Setting all the data into the ridePosting model
            viewModel.createDriverRidePosting(to, from, hourOfDeparture, date, description, estimatedPrice);


            observeRidePostingCreation(view);
        });

        //Changes Activity
        back.setOnClickListener(view -> {
            Log.i("MyApp", "Clicked on BACK");
            Intent intent = new Intent(getApplicationContext(), ChooseRidePosting.class);
            startActivity(intent);
            finish();
        });

    }

    /**
     * Assigns these variables their corresponding properties from the Activity layout
     */
    private void passengerVariablesPropertyInit() {
        fillTo = findViewById(R.id.fillTo);
        fillFrom = findViewById(R.id.fillFrom);
        fillHourOfDeparture = findViewById(R.id.fillHourOfDeparture);
        fillEstimatedPrice = findViewById(R.id.fillEstimatedPrice);
        fillDescription = findViewById(R.id.fillDescription);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        fillDate = findViewById(R.id.fillDate);
        fillDate.setText(getCurrentDate());
    }
}