package com.ferhatproduction.eyesoccer.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Adapter.ESHomeListNewsAdapter;
import com.ferhatproduction.eyesoccer.Adapter.ESListNewsAdapter;
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

public class ESTabNewsFragment extends Fragment implements ESListNewsAdapter.ListItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView img;
    private TextView title;
    private RecyclerView list;

    private ProgressBar progressBar;
    private ScrollView vContent;

    private OnFragmentNewsInteractionListener mListener;

    public ESTabNewsFragment() {
        // Required empty public constructor
    }

    public static ESTabNewsFragment newInstance(String param1, String param2) {
        ESTabNewsFragment fragment = new ESTabNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estab_news, container, false);

        img = (ImageView)view.findViewById(R.id.img);
        title = (TextView)view.findViewById(R.id.tTitle);
        list = (RecyclerView)view.findViewById(R.id.list);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
//        lm.setOrientation(LinearLayoutManager.);
        list.setLayoutManager(lm);

//        vContent = (ScrollView)view.findViewById(R.id.content);
//        vContent.setVisibility(View.GONE);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    public void onButtonPressed(String id, int type) {
        if (mListener != null) {
            mListener.onFragmentNewsInteraction(id,type);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new RequestTaskNews().execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentNewsInteractionListener) {
            mListener = (OnFragmentNewsInteractionListener) context;
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

    @Override
    public void onListItemClick(String id, int type) {
        if (mListener != null) {
            mListener.onFragmentNewsInteraction(id, type);
        }
    }


    public interface OnFragmentNewsInteractionListener {
        // TODO: Update argument type and name
        void onFragmentNewsInteraction(String id, int type);
    }

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

            showProgress(false);

            try{

                JSONObject result = new JSONObject(s);
                String status = result.get("status").toString();
                JSONObject data = (JSONObject) result.get("data");
                JSONArray news = data.getJSONArray("news");

                ArrayList<HashMap<String,Object>> resultNews = new ArrayList<HashMap<String,Object>>();
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

                ESListNewsAdapter listNewsAdapter = new ESListNewsAdapter(resultNews, ESTabNewsFragment.this);
                list.invalidate();
                list.setAdapter(listNewsAdapter);

            } catch (Exception e){
                Log.d("log","exception -> "+ e.getMessage());
            }
        }
    }

    private void showProgress(boolean show){
        if(show){
//            vContent.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
//            vContent.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
