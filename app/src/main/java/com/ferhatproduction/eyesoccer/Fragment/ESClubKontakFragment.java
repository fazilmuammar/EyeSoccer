package com.ferhatproduction.eyesoccer.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Class.Globals;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ESClubKontakFragment extends Fragment {

//    private ImageView img;
    private TextView alamat;
    private TextView telp;
    private TextView email;
    private JSONObject data;

    private ProgressBar progressBar;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-19 09:29:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        alamat = (TextView)view.findViewById( R.id.alamat );
        telp = (TextView)view.findViewById( R.id.telp );
        email = (TextView)view.findViewById( R.id.email );
    }


    public ESClubKontakFragment() {
        // Required empty public constructor
    }

    public static ESClubKontakFragment newInstance(String param1, String param2) {
        ESClubKontakFragment fragment = new ESClubKontakFragment();
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

        View view = inflater.inflate(R.layout.fragment_club_kontak, container, false);

        findViews(view);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        Globals globals = (Globals)getActivity().getApplication();
        data = globals.getClubDetail();



        try {
            alamat.setText(data.get("address").toString());
            telp.setText(data.get("phone").toString());
            email.setText(data.get("email").toString());
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
