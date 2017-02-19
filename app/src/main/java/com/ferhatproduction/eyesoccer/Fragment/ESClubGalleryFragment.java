package com.ferhatproduction.eyesoccer.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Adapter.ESClubGalleryListAdapter;
import com.ferhatproduction.eyesoccer.Class.Globals;
import com.ferhatproduction.eyesoccer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.photodraweeview.PhotoDraweeView;

public class ESClubGalleryFragment extends Fragment implements
        ESClubGalleryListAdapter.ListClubGalleryItemClickListener,
        View.OnClickListener{

//    private ImageView img;
//    private TextView description;
//    private RecyclerView list;
//    private ProgressBar progressBar;
//    private ScrollView content;
    private JSONObject data;

    private ProgressBar progressBar;
    private RecyclerView list;

    private CardView cardView;
    private ArrayList<HashMap<String, Object>> coll;

    private OnGalleryListener mListener;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-19 09:29:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        list = (RecyclerView)view.findViewById(R.id.listview);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);

//        imageView = (PhotoDraweeView) view.findViewById(R.id.imageView);
//        imageGallery = (RelativeLayout) view.findViewById(R.id.imageGallery);
//        imageGallery.setVisibility(View.GONE);
    }


    public ESClubGalleryFragment() {
        // Required empty public constructor
    }

    public static ESClubGalleryFragment newInstance(String param1, String param2) {
        ESClubGalleryFragment fragment = new ESClubGalleryFragment();
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

        View view = inflater.inflate(R.layout.fragment_club_galeri, container, false);

        findViews(view);

        Globals globals = (Globals)getActivity().getApplication();
        data = globals.getClubDetail();

        try {
            JSONArray d = (JSONArray) data.get("galleries");
            coll = new ArrayList<>();
            for(int i=0; i<d.length(); i++){
                JSONObject item = (JSONObject) d.get(i);
                HashMap<String, Object> h = new HashMap<>();
                h.put("id", item.get("id"));
                h.put("title", item.get("title"));
                h.put("description", item.get("description"));
                h.put("url", item.get("url"));
                h.put("upload_date", item.get("upload_date"));
                coll.add(h);
            }

            ESClubGalleryListAdapter adapter = new ESClubGalleryListAdapter(coll, this);
            list.invalidate();
            list.setAdapter(adapter);
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
    public void onListGalleryItemClick(int clickedItemIndex) {
        String imgDetailPath = coll.get(clickedItemIndex).get("url").toString();
//        imageView.setPhotoUri(Uri.parse(imgDetailPath));
//        imageGallery.setVisibility(View.VISIBLE);
        mListener.onShowFullImage(imgDetailPath);
    }

    @Override
    public void onClick(View view) {
//        if(view.getId() == R.id.btCloseGallery){
//            imageGallery.setVisibility(View.GONE);
//        }
    }

    public interface OnGalleryListener {
        void onShowFullImage(String url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGalleryListener) {
            mListener = (OnGalleryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
