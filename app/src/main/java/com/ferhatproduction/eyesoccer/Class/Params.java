package com.ferhatproduction.eyesoccer.Class;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leo on 1/28/17.
 */

public class Params {
    /*** real ***/
    public static final String BASE_URL = "http://103.229.72.232:8080/WebApi/v1";
//    public static final String AUTH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0aW5nQGdtYWlsLmNvbSIsImF1ZGllbmNlIjoibW9iaWxlIiwiY3JlYXRlZCI6MTQ4NjIxMzA1MTExNiwiZXhwIjoxNDg2ODE3ODUxfQ.b19CAp9lPuUkEwhp6o7rSuk4nSTBbf9hwHQ--Oau6c6LP7FAOhRNkug6rDQv09sjcjro52309oOY_1bRaNLTXw";

    /*** mock ***/
//    public static final String BASE_URL = "https://private-e0fe4d-eyesoccer.apiary-mock.com";
    public static final String AUTH_TOKEN = "Basic ZXllc29jY2VyOmV5ZXNvY2NlcjIwMTc=";

    public static final String URL_NEWS = BASE_URL+"/news";
    public static final String URL_WATCH = BASE_URL+"/videos";
    public static final String URL_EVENTS = BASE_URL+"/events";

    public static final String URL_REFEREE = BASE_URL+"/referees";
    public static final String URL_CLUB = BASE_URL+"/clubs";

    //http://private-68f902-eyesoccer.apiary-mock.com/news
    //http://eyesoccer-didikh.rhcloud.com/WebApi/v1/secure/news

    //https://private-e0fe4d-eyesoccer.apiary-mock.com/
    //Basic ZXllc29jY2VyOmV5ZXNvY2NlcjIwMTc=

    public static final int TYPE_NEWS = 1;
    public static final int TYPE_WATCH = 2;

    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        Log.d("log"," duration ---> "+twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds));
//            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
        return twoDigitString(hours) + " : " + twoDigitString(minutes);
    }

    public static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    /*** Elapse time ***/
    public static String getCreateTime(Date date1, Date date2){

        //milliseconds
        long different = date2.getTime() - date1.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String dayInfo, hourInfo, minInfo;
        if(elapsedDays > 1){
            dayInfo = elapsedDays+" days ago";
            return dayInfo;
        } else if(elapsedDays == 1) {
            dayInfo = elapsedDays+" day ago";
            return dayInfo;
        }

        if(elapsedHours > 0){
            hourInfo = ", "+elapsedHours+" hrs ago";
            return hourInfo;
        } else if(elapsedHours == 1) {
            hourInfo = ", "+elapsedHours+" hr ago";
            return hourInfo;
        }

        if(elapsedMinutes > 0){
            minInfo = ", "+elapsedMinutes+" minutes ago";
            return minInfo;
        } else if(elapsedMinutes == 1) {
            minInfo = ", "+elapsedMinutes+" minute ago";
            return minInfo;
        }
//        tRemain.setText(dayInfo+hourInfo);
        return "";
    }

    public static String miliToDateString(int date){
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.UK);
        String dateString = df.format(date);
        return dateString;
    }


}
