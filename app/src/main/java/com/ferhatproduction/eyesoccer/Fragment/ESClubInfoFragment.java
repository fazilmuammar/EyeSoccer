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

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Activity.ESClubDetail;
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

public class ESClubInfoFragment extends Fragment {

    private ImageView img;
    private TextView description;
    private RecyclerView list;

    private ProgressBar progressBar;
    private ScrollView content;

    private OnClubInfoListener mListener;

    public ESClubInfoFragment() {
        // Required empty public constructor
    }

    public static ESClubInfoFragment newInstance(String param1, String param2) {
        ESClubInfoFragment fragment = new ESClubInfoFragment();
        Bundle args = new Bundle();
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

        View view = inflater.inflate(R.layout.fragment_club_info, container, false);

        img = (ImageView)view.findViewById(R.id.img);
        description = (TextView)view.findViewById(R.id.description);

        content = (ScrollView)view.findViewById(R.id.content);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        if (mListener != null) {
//            String m = mListener.onGetInfo();
//            Log.d("info", " ---> message : "+m);
        }

        ((ESClubDetail)getActivity()).getInfoData(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClubInfoListener) {
            mListener = (OnClubInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void updateInfo(String par1, String par2){
        Log.d("info", " ---> par : "+par1);
        description.setText(par2);
        Glide.with(getActivity()).
                load(par1).
                placeholder(android.R.mipmap.sym_def_app_icon).
                into(img);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnClubInfoListener {
        String onGetInfo();
    }
}
