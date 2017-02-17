package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 1/28/17.
 */

public class ESListVideoAdapter extends RecyclerView.Adapter<ESListVideoAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;

    private ListItemVideoClickListener mOnClickListener;

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView img, imgBig;
        private TextView txt, txtBig, time, timeBig;
        private HashMap<String, Object> item;
        private Context context;

        public ContentHolder(View v){
            super(v);

            img = (ImageView) v.findViewById(R.id.img);
            txt = (TextView) v.findViewById(R.id.tTitle);
            time = (TextView) v.findViewById(R.id.tTime);
            imgBig = (ImageView) v.findViewById(R.id.imgBig);
            txtBig = (TextView) v.findViewById(R.id.tTitleBig);
            timeBig = (TextView) v.findViewById(R.id.tTimeBig);
            v.setOnClickListener(this);

            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
//            Log.d("log", "click --> "+item.get("id"));
            int clickedPosition = getAdapterPosition();
            String title = items.get(clickedPosition).get("title").toString();
            String path = items.get(clickedPosition).get("video_url").toString();
            String pos = items.get(clickedPosition).get("id").toString();
            int duration = Integer.parseInt((String)items.get(clickedPosition).get("duration"));
            long createdate = (long)items.get(clickedPosition).get("duration");
            mOnClickListener.onListItemClick(pos, Params.TYPE_WATCH, path, duration, title, createdate);

        }

        public void bindContent(HashMap<String, Object> item, int position) {
            this.item = item;

            if(position == 0){
                img.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
                String imgPath = item.get("featured_image_url").toString();
                Glide.with(context).load(imgPath).into(imgBig);

                String title = item.get("title").toString();
                txtBig.setText(title);
            } else {
                imgBig.setVisibility(View.GONE);
                txtBig.setVisibility(View.GONE);
                timeBig.setVisibility(View.GONE);
                String imgPath = item.get("featured_image_url").toString();
                Glide.with(context).load(imgPath).into(img);

                String title = item.get("title").toString();
                txt.setText(title);
            }


        }
    }

    public interface ListItemVideoClickListener{
        void onListItemClick(String id, int type, String path, int duration, String title, long createdate);
    }

    public ESListVideoAdapter(ArrayList<HashMap<String,Object>> items, ListItemVideoClickListener mOnClickListener){
        this.items = items;
//        this.context = context;
        this.mOnClickListener = mOnClickListener;

    }
    @Override
    public ESListVideoAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_video_listview_item, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESListVideoAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position), position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
