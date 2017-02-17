package com.ferhatproduction.eyesoccer.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Adapter.ESListEventMatchesAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ESEventDetail extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private int type;
    private JSONArray categories, relatedNews;
    private int createDate;

    private ImageView img;
    private TextView tTitle, tContent, tPlace, tDate, judul;
    private ProgressBar progressBar;
    private ScrollView content;
    private RecyclerView list;
    private ArrayList<HashMap<String,Object>> events;
    private SimpleDateFormat dateFormat;
    private HashMap<String,Object> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_es_event_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        content = (ScrollView)findViewById(R.id.content);
        content.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        img = (ImageView)findViewById(R.id.logo);
        tTitle = (TextView) findViewById(R.id.tTitle);
        tPlace = (TextView) findViewById(R.id.place);
        tDate = (TextView) findViewById(R.id.date);
        judul = (TextView) findViewById(R.id.judul);
        dateFormat = new SimpleDateFormat("dd MMMM yyyy");

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBackActionBar);
        btnBack.setOnClickListener(this);

        list = (RecyclerView)findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        new RequestEventDetail().execute();

    }

    private void showProgress(boolean show){
        if(show){
            content.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnBackActionBar){
            finish();
        }
    }

    private class RequestEventDetail extends AsyncTask<String, Void, String> {

        public RequestEventDetail(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_EVENTS+"/"+id);
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
                    String name = (String) data.get("name");
                    Long startDate = (Long) data.get("start_date");
                    Long endDate = (Long) data.get("end_date");
                    String location = (String) data.get("location");

                    String startDateString = dateFormat.format(new Date(startDate));
                    String endDateString = dateFormat.format(new Date(endDate));

                    String imgPath = data.get("logo_url").toString();

                    Glide.with(getBaseContext()).load(imgPath).into(img);
                    judul.setText(name);
                    tDate.setText(startDateString+" - "+endDateString);
                    tPlace.setText(location);

                    /*** Get Matches ***/
                    JSONArray matches = (JSONArray)data.get("matches");
                    JSONObject matchItem = (JSONObject) matches.get(0);
                    Log.d("log","matchItem : "+matchItem.length()+" --> "+matchItem);

                    Long d = Long.parseLong((String)matchItem.names().get(0));
                    String dString = dateFormat.format(new Date(d));
//                    Log.d("log","date : "+dString);

                    ArrayList<HashMap<String,Object>> matchArray = new ArrayList<HashMap<String, Object>>();
                    Iterator iterator = matchItem.keys();
                    while(iterator.hasNext()){
                        item = new HashMap<>();

                        String key = (String)iterator.next();

                        item.put("type","header");
                        item.put("value", key);
                        matchArray.add(item);

                        JSONArray matchKey = matchItem.getJSONArray(key);
                        for(int i=0; i<matchKey.length(); i++){
                            item = new HashMap<>();
                            JSONObject matchKeyItem = (JSONObject) matchKey.get(i);
                            item.put("type","content");
                            item.put("home_team", matchKeyItem.get("home_team"));
                            item.put("away_team", matchKeyItem.get("away_team"));
                            item.put("match_time", matchKeyItem.get("match_time"));
                            item.put("home_team_image_url", matchKeyItem.get("home_team_image_url"));
                            item.put("away_team_image_url", matchKeyItem.get("away_team_image_url"));
                            matchArray.add(item);
                        }
                    }

                    ESListEventMatchesAdapter adapter = new ESListEventMatchesAdapter(matchArray, ESEventDetail.this);
                    list.setAdapter(adapter);

//                    Log.d("log","matches : ---> "+ matchArray);
                    for(int i=0; i<matchArray.size(); i++){
                        String type = (String)matchArray.get(i).get("type");
//                        Long d1 = Long.parseLong((String)matchArray.get(i).get("value"));
//                        String tanggalString = dateFormat.format(new Date(d1));
                        if(type.equals("header")){
                            Log.d("log","header : ---> "+ matchArray.get(i).get("value") + " -> ");
                        } else {
                            Log.d("log","content : ---> "+ matchArray.get(i).get("home_team"));
                        }
                    }
                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }


}
