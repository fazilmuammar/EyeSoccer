package com.ferhatproduction.eyesoccer.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Adapter.ESListRefereeAdapter;
import com.ferhatproduction.eyesoccer.Adapter.ESListRelatedNewsAdapter;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ESListReferee extends AppCompatActivity implements View.OnClickListener, ESListRefereeAdapter.ListItemClickListener {
    private String id;
    private int type;
    private JSONArray categories, relatedNews;
    private int createDate;

    private ImageView img;
    private TextView tTitle, tContent;
    private ProgressBar progressBar;
    private ScrollView vContent;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_es_referee_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBackActionBar);
        btnBack.setOnClickListener(this);

        list = (RecyclerView)findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        new RequestTaskReferee().execute();

    }

    private void showProgress(boolean show){
        if(show){
            list.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnBackActionBar){
            finish();
        }
    }

    @Override
    public void onListItemClick(String id, int type) {

    }

    private class RequestTaskReferee extends AsyncTask<String, Void, String> {

        public RequestTaskReferee(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_REFEREE);
                Log.d("log","url:"+url);
                conn = (HttpURLConnection) url.openConnection();

                /*** set method ***/
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                /*** set header ***/
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Language", "id");
                conn.setRequestProperty("Authorization", Params.AUTH_TOKEN);

                InputStream inputStream;
                try
                {
                    inputStream = conn.getInputStream();
                }
                catch(IOException exception)
                {
                    inputStream = conn.getErrorStream();
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if(conn != null){
                    conn.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            showProgress(false);

            try{

                JSONObject response = new JSONObject(s);
                String status = response.get("status").toString();

//                Log.d("log"," result : "+s);

                if(status.equals("success")){
                    JSONArray data = (JSONArray) response.get("data");

                    ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String,Object>>();
                    for(int i=0; i<data.length(); i++){
                        JSONObject item = (JSONObject) data.get(i);
                        HashMap<String,Object> temp = new HashMap<String,Object>();
                        temp.put("id", item.get("id").toString());
                        temp.put("photo_url", item.get("photo_url").toString());
                        temp.put("profession", item.get("profession").toString());
                        temp.put("name", item.get("name").toString());
                        result.add(temp);
                    }

                    ESListRefereeAdapter listNewsAdapter = new ESListRefereeAdapter(result, ESListReferee.this);
                    list.invalidate();
                    list.setAdapter(listNewsAdapter);


                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private Date getDate(int time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        Date date = (Date) DateFormat.format("dd-MM-yyyy hh:mm:ss", cal);
        return date;
    }

    public String getDifference(Date date1, Date date2){

        //milliseconds
        long different = date2.getTime() - date1.getTime();

//        Log.d("log","now : "+ date2);
//        Log.d("log","create date : "+ date1);
//        Log.d("log","different : " + different);
//        Log.d("log","different : " + different);

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
}
