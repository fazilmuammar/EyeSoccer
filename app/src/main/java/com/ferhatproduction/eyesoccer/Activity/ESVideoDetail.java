package com.ferhatproduction.eyesoccer.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class ESVideoDetail extends AppCompatActivity implements View.OnClickListener, ESListRelatedNewsAdapter.RelatedListItemClickListener {
    private String id, videoPath;
    private int type;
    private JSONArray categories, relatedNews;
    private int createDate;

    private ImageView img;
    private TextView tTitle, tContent;
    private ProgressBar progressBar;
    private LinearLayout vContent;
    private RecyclerView listRelatedVideo;
    private VideoView videoView;
    private MediaController mediaController;
    private RelativeLayout.LayoutParams paramsNotFullscreen; //if you're using RelativeLatout
    private AppBarLayout bar;
    private RelativeLayout frameVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_es_video_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", Params.TYPE_WATCH);
        videoPath = getIntent().getStringExtra("path");

        Log.d("log"," ---> type : "+type);
        Log.d("log"," ---> path : "+videoPath);

        vContent = (LinearLayout)findViewById(R.id.content);
        vContent.setVisibility(View.VISIBLE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
//        img = (ImageView)findViewById(R.id.img);
        videoView = (VideoView)findViewById(R.id.video);
        tTitle = (TextView) findViewById(R.id.tTitle);
        tContent = (TextView) findViewById(R.id.tContent);

        bar = (AppBarLayout)findViewById(R.id.appBarLayout);
        frameVideo = (RelativeLayout)findViewById(R.id.frameVideo);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBackActionBar);
        btnBack.setOnClickListener(this);

        listRelatedVideo = (RecyclerView)findViewById(R.id.listRelatedVideo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRelatedVideo.setLayoutManager(layoutManager);

        videoView.setVideoPath(videoPath);

        if(mediaController == null){
            mediaController = new MediaController(this);
        }

        videoView.setMediaController(mediaController);
        videoView.start();
        mediaController.setAnchorView(videoView);




//        new RequestTaskDetail().execute();

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

                    String elapsetime = getDifference(createdDate, now);

//                    Log.d("log","---> create date : "+ elapsetime);



                    String imgPath = data.get("featured_image_url").toString();
                    String title = data.get("title").toString();
                    String content = data.get("content").toString();

                    Glide.with(getBaseContext()).load(imgPath).into(img);
                    tTitle.setText(title);
                    tContent.setText(content);

//                    Log.d("log","---> Related News : "+ relatedNews);
                    /*** update related news ***/
                    updateRelatedVideos();

                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private void updateRelatedVideos() {

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
            ESListRelatedNewsAdapter listRelatedNewsAdapter = new ESListRelatedNewsAdapter(relatedNewsArray, ESVideoDetail.this);
            listRelatedVideo.invalidate();
            listRelatedVideo.setAdapter(listRelatedNewsAdapter);

//            Log.d("log","---> relatedNewsArray : "+ relatedNewsArray);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRelatedListItemClick(String id, int type) {
//        Toast.makeText(getApplicationContext(), "clicked id : "+id+" /ntype : "+type, Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, ESVideoDetail.class);
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

    /*** ***/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
//            paramsNotFullscreen=(RelativeLayout.LayoutParams)videoView.getLayoutParams();
//            RelativeLayout.LayoutParams params= null;
//            params = new RelativeLayout.LayoutParams(paramsNotFullscreen);
//            params.setMargins(0, 0, 0, 0);
//            params.height=ViewGroup.LayoutParams.MATCH_PARENT;
//            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            videoView.setLayoutParams(params);

            paramsNotFullscreen=(RelativeLayout.LayoutParams)frameVideo.getLayoutParams();
            RelativeLayout.LayoutParams params= null;
            params = new RelativeLayout.LayoutParams(paramsNotFullscreen);
            params.setMargins(0, 0, 0, 0);
            params.height=ViewGroup.LayoutParams.MATCH_PARENT;
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            frameVideo.setLayoutParams(params);
            bar.setVisibility(View.GONE);

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
//            videoView.setLayoutParams(paramsNotFullscreen);
            frameVideo.setLayoutParams(paramsNotFullscreen);
            bar.setVisibility(View.VISIBLE);
        }
    }
}
