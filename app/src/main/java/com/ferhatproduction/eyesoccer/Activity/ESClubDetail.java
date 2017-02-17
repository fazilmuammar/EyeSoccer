package com.ferhatproduction.eyesoccer.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.Fragment.ESClubInfoFragment;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ESClubDetail extends AppCompatActivity implements
        View.OnClickListener,
        ESClubInfoFragment.OnClubInfoListener{

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    TextView mainTitle;
    String clubId, clubName;
    ProgressBar progressBar;
    String _imageUrl, _description;

    ESClubInfoFragment fragmentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.es_activity_club_detail);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        clubId = getIntent().getStringExtra("id");
        clubName = getIntent().getStringExtra("name");

        mainTitle = (TextView)findViewById(R.id.mainTitle);
        mainTitle.setText(clubName);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

//        setupTabs();
        new RequestTaskDetail().execute();
    }

    private void setupTabs(){

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        createTabIcons();
//        new RequestEventDetail().execute();
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter.addFragment(new ESClubInfoFragment(), "INFO");
        adapter.addFragment(new ESClubInfoFragment(), "PEMAIN");
        adapter.addFragment(new ESClubInfoFragment(), "STATISTIK");
        adapter.addFragment(new ESClubInfoFragment(), "GALERI");
        adapter.addFragment(new ESClubInfoFragment(), "KONTAK");
        viewPager.setAdapter(adapter);
    }

    @Override
    public String onGetInfo() {
//        new RequestEventDetail().execute();
        return _imageUrl;
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
                URL url = new URL(Params.URL_CLUB+"/"+clubId);
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
                    _description = (String)data.get("description");
                    _imageUrl = (String)data.get("image_url");

//                    fragmentInfo.updateInfo(_imageUrl, _description);
                    setupTabs();
                }

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private void showProgress(boolean show) {
        if(show){
            progressBar.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabOne.setText("INFO");
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabTwo.setText("PEMAIN");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabThree.setText("STATISTIK");
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabFour.setText("GALERI");
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabFive.setText("KONTAK");
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    @Override
    public void onClick(View view) {


    }

    public String getInfoData(Fragment fragment){
//        new RequestEventDetail().execute();
        ((ESClubInfoFragment)fragment).updateInfo(_imageUrl, _description);
        return _imageUrl;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter implements ViewTreeObserver.OnScrollChangedListener {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void onScrollChanged() {

        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);

        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }



    }



}
