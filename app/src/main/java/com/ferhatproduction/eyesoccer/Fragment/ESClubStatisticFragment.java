package com.ferhatproduction.eyesoccer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Class.Globals;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ESClubStatisticFragment extends Fragment {

//    private ImageView img;
//    private TextView description;
//    private RecyclerView list;
//    private ProgressBar progressBar;
//    private ScrollView content;
    private JSONObject data;

    private ProgressBar progressBar;
    private ScrollView content;
    private LinearLayout infoStatistik;
    private TextView jumlahPertandingan;
    private TextView menang;
    private TextView imbang;
    private TextView kalah;
    private TextView gol;
    private TextView kemasukan;
    private TextView cleanSheet;
    private TextView shotOnTarget;
    private TextView shotOffTarget;
    private TextView kartuKuning;
    private TextView kartuMerah;
    private TextView penonton;

    private CardView cardView;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-19 09:29:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        cardView = (CardView)view.findViewById(R.id.card);
//        content = (ScrollView)view.findViewById( R.id.content );
        infoStatistik = (LinearLayout)view.findViewById( R.id.infoStatistik );
//        jumlahPertandingan = (TextView)view.findViewById( R.id.jumlahPertandingan );
//        menang = (TextView)view.findViewById( R.id.menang );
//        imbang = (TextView)view.findViewById( R.id.imbang );
//        kalah = (TextView)view.findViewById( R.id.kalah );
//        gol = (TextView)view.findViewById( R.id.gol );
//        kemasukan = (TextView)view.findViewById( R.id.kemasukan );
//        cleanSheet = (TextView)view.findViewById( R.id.cleanSheet );
//        shotOnTarget = (TextView)view.findViewById( R.id.shotOnTarget );
//        shotOffTarget = (TextView)view.findViewById( R.id.shotOffTarget );
//        kartuKuning = (TextView)view.findViewById( R.id.kartuKuning );
//        kartuMerah = (TextView)view.findViewById( R.id.kartuMerah );
//        penonton = (TextView)view.findViewById( R.id.penonton );
    }


    public ESClubStatisticFragment() {
        // Required empty public constructor
    }

    public static ESClubStatisticFragment newInstance(String param1, String param2) {
        ESClubStatisticFragment fragment = new ESClubStatisticFragment();
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

        View view = inflater.inflate(R.layout.fragment_club_statistik, container, false);

        findViews(view);

        content = (ScrollView)view.findViewById(R.id.content);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        Globals globals = (Globals)getActivity().getApplication();
        data = globals.getClubDetail();



        try {
            JSONArray d = (JSONArray) data.get("statistics");



            for(int i=0; i<d.length(); i++){
                JSONObject item = (JSONObject) d.get(i);

                LinearLayout.LayoutParams layoutParamMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamMain.setMargins(10,20,10,0);

                RelativeLayout.LayoutParams layoutParamRelative = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParamRelative.setMargins(5,5,5,5);

                LinearLayout.LayoutParams layoutParamTextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamTextView.setMargins(5,15,15,5);

                LinearLayout.LayoutParams layoutParamLine = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                layoutParamLine.setMargins(10,5,5,5);

                /*** create relative layout ***/
                RelativeLayout rl = new RelativeLayout(getActivity());

//                rl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                rl.setLayoutParams(layoutParamMain);

                /*** left textview ***/
                TextView tvKiri = new TextView(getActivity());
                tvKiri.setText(item.get("type").toString());
                rl.addView(tvKiri, layoutParamTextView);

                /*** right textview ***/
                TextView tvKanan = new TextView(getActivity());
                tvKanan.setText(item.get("result").toString());
                tvKanan.setGravity(View.FOCUS_RIGHT);
                rl.addView(tvKanan, layoutParamRelative);

                infoStatistik.addView(rl, layoutParamMain);

                View v = new View(getActivity());
                v.setBackgroundColor(getResources().getColor(R.color.gray_light));
                infoStatistik.addView(v, layoutParamLine);
            }
//            jumlahPertandingan.setText(d.get(0));
//            menang.setText(data.get("").toString());
//            imbang.setText(data.get("").toString());
//            kalah.setText(data.get("").toString());
//            gol.setText(data.get("").toString());
//            kemasukan.setText(data.get("").toString());
//            cleanSheet.setText(data.get("").toString());
//            shotOnTarget.setText(data.get("").toString());
//            shotOffTarget.setText(data.get("").toString());
//            kartuKuning.setText(data.get("").toString());
//            kartuMerah.setText(data.get("").toString());
//            penonton.setText(data.get("").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
