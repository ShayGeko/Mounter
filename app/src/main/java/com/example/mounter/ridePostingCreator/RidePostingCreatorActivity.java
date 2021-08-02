package com.example.mounter.ridePostingCreator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModelProvider;

import com.example.mounter.MounterBaseActivity;

import com.example.mounter.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import static com.example.mounter.common.MounterDateUtil.convertDateToString;
import static com.example.mounter.common.MounterDateUtil.getCurrentDate;

public class RidePostingCreatorActivity extends MounterBaseActivity {

    protected DatePickerDialog datePickerDialog;
    protected TextInputEditText fillTo;
    protected TextInputEditText fillFrom;
    protected TextInputEditText fillHourOfDeparture;
    protected TextInputEditText fillDescription;
    protected Button submit;
    protected Button fillDate;
    protected ImageButton back;


    protected RidePostingCreatorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_creator);

        initDatePicker();
        variablePropertyInit();


        submit.setOnClickListener(view -> {
            String to = fillTo.getText().toString();
            String from = fillFrom.getText().toString();
            String hourOfDeparture = fillHourOfDeparture.getText().toString();
            String date = fillDate.getText().toString();
            String description = fillDescription.getText().toString();


            if(to.length() == 0 || from.length() == 0 || fillHourOfDeparture.length() == 0){    //Checks if all key components have been filled up by the user
                hideKeyboard();
                Log.i("MyApp", "Key details are not filled in!");
                Snackbar.make(view, R.string.fill_in_components,
                        Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return;
            }

            viewModel = new ViewModelProvider(this).get(RidePostingCreatorViewModel.class);
            //Setting all the data into the ridePosting model
            viewModel.createPassengerRidePosting(to, from, hourOfDeparture, date, description);


            viewModel.getResult().observe(this, result->{
                if(result.isSuccess()){
                    finish();
                }
                else{
                    Snackbar.make(view, R.string.something_went_wrong,
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        });

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
    private void variablePropertyInit() {
        fillTo = findViewById(R.id.fillTo);
        fillFrom = findViewById(R.id.fillFrom);
        fillHourOfDeparture = findViewById(R.id.fillHourOfDeparture);
        fillDescription = findViewById(R.id.fillDescription);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        fillDate = findViewById(R.id.fillDate);
        fillDate.setText(getCurrentDate());
    }

    protected void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month += 1; //By default January is represented as 0, therefore we add 1 so January corresponds to the integer 1.
            String date = convertDateToString(day, month, year);
            fillDate.setText(date);
        };

        Calendar myCalendar = Calendar.getInstance();
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        int year = myCalendar.get(Calendar.YEAR);
        Log.d("MyApp", "" + year);

        datePickerDialog = new DatePickerDialog(this, 0, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    public void showDatePicker(View view){
        datePickerDialog.show();
    }

    /**
     * Hides Keyboard
     */
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}