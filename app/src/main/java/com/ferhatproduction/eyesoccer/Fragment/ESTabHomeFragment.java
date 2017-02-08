package com.ferhatproduction.eyesoccer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Adapter.ESHomeListEventsAdapter;
import com.ferhatproduction.eyesoccer.Adapter.ESHomeListNewsAdapter;
import com.ferhatproduction.eyesoccer.Adapter.ESHomeListVideoAdapter;
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
import java.util.HashMap;

public class ESTabHomeFragment extends Fragment implements
        ESHomeListNewsAdapter.ListItemClickListener,
        ESHomeListVideoAdapter.ListItemVideoListener,
        View.OnClickListener{
    private ArrayList<HashMap<String,Object>> resultNews, resultWatch, resultEvents;
    private RecyclerView listNews, listWatch, listEvents;
    private LinearLayoutManager newsLinearLayoutManager, watchLinearLayoutManager, eventsLinearLayoutManager;
    private ESHomeListNewsAdapter listNewsAdapter;
    private ESHomeListVideoAdapter listWatchAdapter;
    private ESHomeListEventsAdapter listEventsAdapter;

    private TextView tAllnews, tAllvideo, tAllevent;

    private OnFragmentInteractionListener mListener;
//    private OnFragmentVideoInteractionListener mListenerVideo;

    public ESTabHomeFragment() {
        // Required empty public constructor
    }

//    public static ESTabHomeFragment newInstance() {
//        ESTabHomeFragment fragment = new ESTabHomeFragment();
//        Bundle args = new Bundle();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estab_home, container, false);

        listNews = (RecyclerView) view.findViewById(R.id.listNews);
        newsLinearLayoutManager = new LinearLayoutManager(getActivity());
        newsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listNews.setLayoutManager(newsLinearLayoutManager);


        listWatch = (RecyclerView) view.findViewById(R.id.listWatch);
        watchLinearLayoutManager = new LinearLayoutManager(getActivity());
        watchLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listWatch.setLayoutManager(watchLinearLayoutManager);

        listEvents = (RecyclerView) view.findViewById(R.id.listEvents);
        eventsLinearLayoutManager = new LinearLayoutManager(getActivity());
        eventsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listEvents.setLayoutManager(eventsLinearLayoutManager);

        tAllnews = (TextView)view.findViewById(R.id.allNews);
        tAllvideo = (TextView)view.findViewById(R.id.allVideo);
        tAllevent = (TextView)view.findViewById(R.id.allEvent);
        tAllnews.setOnClickListener(this);
        tAllvideo.setOnClickListener(this);
        tAllevent.setOnClickListener(this);

        new RequestTaskNews().execute();

        return view;
    }

    @Override
    public void onListItemClick(String id, int type) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id, type);
        }
    }

    @Override
    public void onListItemVideoClick(String id, int type, String path) {
        if (mListener != null) {
            mListener.onFragmentVideoInteraction(id, type, path);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.allNews){
            mListener.onGoToList(2);
        } else if(view.getId() == R.id.allVideo){
            mListener.onGoToList(1);
        } else if(view.getId() == R.id.allEvent){

        }
    }

//    @Override
//    public void onVideoListItemClick(String id, int type) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(id, type);
//        }
//    }

//    @Override
//    public void onVideoListItemClick(String id, int type) {
//        if (mListenerVideo != null) {
//            mListenerVideo.onFragmentVideoInteraction(id, type);
//        }
//    }

    private class RequestTaskNews extends AsyncTask<String, Void, String> {

        public RequestTaskNews(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_NEWS);
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

//            Log.d("log","news : "+s);

            try{

                JSONObject result = new JSONObject(s);
                String status = result.get("status").toString();
                JSONObject data = (JSONObject) result.get("data");
                JSONArray news = data.getJSONArray("news");

//                Log.d("log","result -> "+ news);

                // Save to News array
                resultNews = new ArrayList<HashMap<String,Object>>();
                for(int i=0; i<news.length(); i++){
                    JSONObject item = (JSONObject) news.get(i);
                    HashMap<String,Object> temp = new HashMap<String,Object>();
                    temp.put("id", item.get("id").toString());
                    temp.put("featured_image_url", item.get("featured_image_url").toString());
                    temp.put("create_date", item.get("create_date").toString());
                    temp.put("title", item.get("title").toString());
                    temp.put("categories", item.get("categories"));
                    resultNews.add(temp);
                }

                listNewsAdapter = new ESHomeListNewsAdapter(resultNews, ESTabHomeFragment.this);
                listNews.invalidate();
                listNews.setAdapter(listNewsAdapter);

                new RequestTaskWatch().execute();


            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private class RequestTaskWatch extends AsyncTask<String, Void, String> {

        public RequestTaskWatch(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_WATCH);
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

//            Log.d("log"," ---> videos : "+s);

            try{

                JSONObject result = new JSONObject(s);
                String status = result.get("status").toString();
                JSONObject data = (JSONObject) result.get("data");
                JSONArray videos = data.getJSONArray("videos");

//                Log.d("log","result -> "+ news);

                // Save to News array
                resultWatch = new ArrayList<HashMap<String,Object>>();
                for(int i=0; i<videos.length(); i++){
                    JSONObject item = (JSONObject) videos.get(i);
                    HashMap<String,Object> temp = new HashMap<String,Object>();
                    temp.put("id", item.get("id").toString());
                    temp.put("featured_image_url", item.get("featured_image_url").toString());
                    temp.put("create_date", item.get("create_date").toString());
                    temp.put("title", item.get("title").toString());
                    temp.put("categories", item.get("categories"));
                    temp.put("duration", item.get("duration").toString());
                    temp.put("video_url", item.get("video_url").toString());
                    resultWatch.add(temp);
                }

                listWatchAdapter = new ESHomeListVideoAdapter(resultWatch, (ESHomeListVideoAdapter.ListItemVideoListener)ESTabHomeFragment.this);
                listWatch.invalidate();
                listWatch.setAdapter(listWatchAdapter);

                new RequestTaskEvents().execute();

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private class RequestTaskEvents extends AsyncTask<String, Void, String> {

        public RequestTaskEvents(){
        }

        @Override
        protected String doInBackground(String...strings) {

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                /*** set url ***/
                URL url = new URL(Params.URL_EVENTS);
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

            try{

                JSONObject result = new JSONObject(s);
                String status = result.get("status").toString();
//                JSONObject data = (JSONObject) result.get("data");
//                JSONArray videos = data.getJSONArray("videos");
                JSONArray events = result.getJSONArray("data");

//                Log.d("log","result -> "+ news);

                // Save to News array
                resultEvents = new ArrayList<HashMap<String,Object>>();
                for(int i=0; i<events.length(); i++){
                    JSONObject item = (JSONObject) events.get(i);
                    HashMap<String,Object> temp = new HashMap<String,Object>();
                    temp.put("id", item.get("id").toString());
                    temp.put("featured_image_url", item.get("featured_image_url").toString());
                    temp.put("create_date", item.get("create_date").toString());
                    temp.put("title", item.get("title").toString());
                    temp.put("event_start_date", item.get("event_start_date"));
                    temp.put("event_finish_date", item.get("event_finish_date"));
                    temp.put("location", item.get("location").toString());
                    resultEvents.add(temp);
                }

                listEventsAdapter = new ESHomeListEventsAdapter(resultEvents, getActivity());
                listEvents.invalidate();
                listEvents.setAdapter(listEventsAdapter);


            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
//            mListenerVideo = (OnFragmentInteractionListenerVideo) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id, int type);
        void onFragmentVideoInteraction(String id, int type, String path);
        void onGoToList(int tabIndex);
    }

    public interface OnFragmentInteractionListenerVideo {
//        void onFragmentInteraction(String id, int type);
        void onFragmentVideoInteraction(String id, int type);
    }
}
