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
import com.ferhatproduction.eyesoccer.Class.Globals;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONArray;
import org.json.JSONException;
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

//    private ImageView img;
//    private TextView description;
//    private RecyclerView list;
//    private ProgressBar progressBar;
//    private ScrollView content;
    private JSONObject data;

    private OnClubInfoListener mListener;

    private ProgressBar progressBar;
    private ScrollView content;
    private ImageView img;
    private TextView description;
    private TextView nama;
    private TextView julukan;
    private TextView tanggalBerdiri;
    private TextView alamat;
    private TextView telp;
    private TextView email;
    private TextView stadiun;
    private TextView suporter;
    private RecyclerView list;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-19 09:29:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        progressBar = (ProgressBar)view.findViewById( R.id.progressBar );
        content = (ScrollView)view.findViewById( R.id.content );
        img = (ImageView)view.findViewById( R.id.img );
        description = (TextView)view.findViewById( R.id.description );
        nama = (TextView)view.findViewById( R.id.nama );
        julukan = (TextView)view.findViewById( R.id.julukan );
        tanggalBerdiri = (TextView)view.findViewById( R.id.tanggalBerdiri );
        alamat = (TextView)view.findViewById( R.id.alamat );
        telp = (TextView)view.findViewById( R.id.telp );
        email = (TextView)view.findViewById( R.id.email );
        stadiun = (TextView)view.findViewById( R.id.stadiun );
        suporter = (TextView)view.findViewById( R.id.suporter );
        list = (RecyclerView)view.findViewById( R.id.list );
    }


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

        findViews(view);

        if (mListener != null) {
//            String m = mListener.onGetInfo();
//            Log.d("info", " ---> message : "+m);
        }

        /*** get global data ***/

//        ((ESClubDetail)getActivity()).getInfoData(this);

        Globals globals = (Globals)getActivity().getApplication();
        data = globals.getClubDetail();

        try {
            description.setText(data.get("description").toString());
            String imageUrl = (String)data.get("image_url");
            Glide.with(getActivity()).load(imageUrl).placeholder(android.R.mipmap.sym_def_app_icon).into(img);

            nama.setText(data.get("name").toString());
            julukan.setText(data.get("nickname").toString());

            tanggalBerdiri.setText(Params.miliToDateString((Integer) data.get("established_date")));
            alamat.setText(data.get("address").toString());
            telp.setText(data.get("phone").toString());
            email.setText(data.get("email").toString());
            stadiun.setText(data.get("stadium").toString());
            suporter.setText((String)data.get("supporter"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
