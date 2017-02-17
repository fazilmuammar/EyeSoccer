package com.ferhatproduction.eyesoccer.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class ESNewsDetail extends AppCompatActivity implements View.OnClickListener, ESListRelatedNewsAdapter.RelatedListItemClickListener {
    private String id;
    private int type;
    private JSONArray categories, relatedNews;
    private int createDate;

    private ImageView img;
    private TextView tTitle, tContent, tTime;
    private ProgressBar progressBar;
    private ScrollView vContent;
    private RecyclerView listRelatedNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_es_news_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", Params.TYPE_NEWS);

        vContent = (ScrollView)findViewById(R.id.content);
        vContent.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        img = (ImageView)findViewById(R.id.img);
        tTitle = (TextView) findViewById(R.id.tTitle);
        tTime = (TextView) findViewById(R.id.tTime);
        tContent = (TextView) findViewById(R.id.tContent);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBackActionBar);
        btnBack.setOnClickListener(this);

        listRelatedNews = (RecyclerView)findViewById(R.id.listRelatedNews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRelatedNews.setLayoutManager(layoutManager);

        new RequestTaskDetail().execute();

    }

    private void showProgress(boolean show){
        if(show){
            vContent.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            vContent.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnBackActionBar){
            finish();
        }
    }

    private class RequestTaskDetail extends AsyncTask<String, Void, String> {

        public RequestTaskDetail(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_NEWS+"/"+id);
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

                JSONObject result = new JSONObject(s);
                String status = result.get("status").toString();

//                Log.d("log"," result : "+s);

                if(status.equals("success")){
                    JSONObject data = (JSONObject) result.get("data");
                    categories = (JSONArray) data.get("categories");
                    relatedNews = (JSONArray) data.get("related_news");
                    createDate = (int) data.get("create_date");

                    // TODO: 2/4/17 fix time difference info
                    long time = (long)createDate;
                    Date createdDate = new Date(time*1000);
                    Date now = new Date(System.currentTimeMillis());

//                    Log.d("log","---> getDifference("+createdDate+", "+now+")");

                    String elapsetime = Params.getCreateTime(createdDate, now);

                    Log.d("log","---> News create date : "+ elapsetime);



                    String imgPath = data.get("featured_image_url").toString();
                    String title = data.get("title").toString();
                    String content = data.get("content").toString();

                    Glide.with(getBaseContext()).load(imgPath).into(img);
                    tTitle.setText(title);
                    tContent.setText(content);
                    tTime.setText(elapsetime);

//                    Log.d("log","---> Related News : "+ relatedNews);
                    /*** update related news ***/
                    updateRelatedNews();

                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private void updateRelatedNews() {

        ArrayList<HashMap<String,Object>> relatedNewsArray = new ArrayList<HashMap<String,Object>>();
        try {
            for (int i = 0; i < relatedNews.length(); i++) {
                JSONObject item = (JSONObject) relatedNews.get(i);
                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("id", item.get("id").toString());
                temp.put("featured_image_url", item.get("featuredImageUrl").toString());
                temp.put("title", item.get("title").toString());
                relatedNewsArray.add(temp);
            }
            ESListRelatedNewsAdapter listRelatedNewsAdapter = new ESListRelatedNewsAdapter(relatedNewsArray, ESNewsDetail.this);
            listRelatedNews.invalidate();
            listRelatedNews.setAdapter(listRelatedNewsAdapter);

//            Log.d("log","---> relatedNewsArray : "+ relatedNewsArray);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRelatedListItemClick(String id, int type) {
//        Toast.makeText(getApplicationContext(), "clicked id : "+id+" /ntype : "+type, Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, ESNewsDetail.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        startActivity(intent);
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
