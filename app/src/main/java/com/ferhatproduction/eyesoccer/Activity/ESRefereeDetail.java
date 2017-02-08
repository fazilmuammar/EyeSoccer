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

public class ESRefereeDetail extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private int type;
    private JSONArray categories, relatedNews;
    private int createDate;

    private ImageView img;
    private TextView tName, tDescription;
    private ProgressBar progressBar;
    private ScrollView content;
    private RecyclerView listRelatedNews;
    private String name;

    private TextView tWarganegara, tProfesi, tTempatLahir, tTglLahir, tTampil, tBerat, tTinggi, tLisensi, tDebut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_es_referee_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        Log.d("log"," id : "+id);

        content = (ScrollView)findViewById(R.id.content);
        content.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        img = (ImageView)findViewById(R.id.img);
        tName = (TextView) findViewById(R.id.tName);
        tDescription = (TextView) findViewById(R.id.description);
        tWarganegara = (TextView) findViewById(R.id.warganegara);
        tTempatLahir = (TextView) findViewById(R.id.tempatLahir);
        tTglLahir = (TextView) findViewById(R.id.tanggalLahir);
        tBerat = (TextView) findViewById(R.id.berat);
        tTinggi = (TextView) findViewById(R.id.tinggi);
        tDebut = (TextView) findViewById(R.id.debut);
        tLisensi = (TextView) findViewById(R.id.lisensi);
        tTampil = (TextView) findViewById(R.id.tampil);

        tName.setText(name);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBackActionBar);
        btnBack.setOnClickListener(this);

//        listRelatedNews = (RecyclerView)findViewById(R.id.listRelatedNews);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        listRelatedNews.setLayoutManager(layoutManager);

        showProgress(true);
        new RequestTaskRefereeDetail().execute();



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

    private class RequestTaskRefereeDetail extends AsyncTask<String, Void, String> {

        public RequestTaskRefereeDetail(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_REFEREE+"/"+id);
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

                Log.d("log"," result : "+s);

                if(status.equals("success")){
                    JSONObject data = (JSONObject) result.get("data");

                    String imgPath = data.get("photo_url").toString();
                    String profession = data.get("profession").toString();
                    String nationality = data.get("nationality").toString();
                    String birth_date = data.get("birth_date").toString();
                    String birth_place = data.get("birth_place").toString();
                    String debut_date = data.get("debut_date").toString();
                    String height = data.get("height").toString();
                    String weight = data.get("weight").toString();
                    String license = data.get("fifa_licence").toString();
                    String appearance = data.get("appearance").toString();
                    JSONArray careers = (JSONArray) data.get("careers");

                    Glide.with(getBaseContext()).load(imgPath).placeholder(R.mipmap.ic_launcher).into(img);


                    tWarganegara.setText(nationality);
                    tTempatLahir.setText(birth_place);
                    tTglLahir.setText(birth_date);
                    tDebut.setText(debut_date);
                    tTampil.setText(appearance);
                    tBerat.setText(weight);
                    tTinggi.setText(height);
                    tLisensi.setText(license);

                    // TODO: 2/8/17 careers

                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }
}
