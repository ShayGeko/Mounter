package com.example.mounter.common;

import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.ridePostingCreator.RidePostingCreatorActivity;

import java.util.Calendar;

public class MounterDateUtil {
    /**
     *
     * @return an integer representing number of days passed since Epoch (1/1/1970)
     */
    public static int getCurrentDateInDays() {
        Calendar myCalendar = Calendar.getInstance();
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        month += 1;
        int year = myCalendar.get(Calendar.YEAR);

        //Converts the month to the number of days in the given month
        int daysInMonth = getMonthValue(convertMonth(month));

        return day + daysInMonth + (year * 365);
    }
    /**
     *
     * @param month
     * @return approximate number of days in the specified month
     */
    public static int getMonthValue(String month){
        switch(month){
            case "Jan":
            case "Mar":
            case "May":
            case "Jul":
            case "Aug":
            case "Oct":
            case "Dec":
                return 31;
            case "Feb":
                return 29;
            case "Apr":
            case "Jun":
            case "Sep":
            case "Nov":
                return 30;
        }
        return 31;
    }


    public static String convertMonth(int month) {

        switch(month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "Jan";
    }
    public static String convertDateToString(int day, int month, int year) {
        return "" + day + "/" + convertMonth(month) + "/" + year;
    }

    public static String getCurrentDate() {
        Calendar myCalendar = Calendar.getInstance();
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        month += 1;
        int year = myCalendar.get(Calendar.YEAR);

        return convertDateToString(day, month, year);
    }
    public static int getNumberOfDaysSinceEpoch(String datetime){
        String date = datetime.split(" ")[0];
        String[] dateValues = date.split("/");   //[0] = days, [1] = months, [2] = years

        return (Integer.parseInt(dateValues[0]) + getMonthValue(dateValues[1]) + (Integer.parseInt(dateValues[2]) * 365));
    }
}
