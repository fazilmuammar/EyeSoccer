package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 1/28/17.
 */

public class ESHomeListVideoAdapter extends RecyclerView.Adapter<ESHomeListVideoAdapter.ContentHolderVideo> {
    private static ArrayList<HashMap<String,Object>> items;
    private Context context;

    private static ListItemVideoListener mOnClickListener;

    public static class ContentHolderVideo extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RoundedImageView img;
        private TextView txt;
        private TextView tDuration;
        private HashMap<String, Object> item;
        private Context context;

        public ContentHolderVideo(View v){
            super(v);

            img = (RoundedImageView) v.findViewById(R.id.thumbWatch);
            txt = (TextView) v.findViewById(R.id.textWatch);
            tDuration = (TextView) v.findViewById(R.id.duration);
            v.setOnClickListener(this);

            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
            Log.d("log", "click --> "+item.get("id"));

            int clickedPosition = getAdapterPosition();
            String path = items.get(clickedPosition).get("video_url").toString();
            mOnClickListener.onListItemVideoClick(items.get(clickedPosition).get("id").toString(), Params.TYPE_WATCH, path);
        }

        public void bindContent(HashMap<String, Object> item) {
            this.item = item;

            String imgPath;
            try {
                imgPath = item.get("featured_image_url").toString();
                Glide.with(context).load(imgPath).into(img);
            } catch (Exception e){
                //
            }

            String title;
            try {
                title = item.get("title").toString();
                txt.setText(title);
            } catch (Exception e){
                txt.setText("undefined");
            }

            String duration;
            try {
                duration = item.get("duration").toString();
                tDuration.setText(duration);
            } catch (Exception e){
                tDuration.setText("00:00");
            }

        }
    }

    public interface ListItemVideoListener{
        void onListItemVideoClick(String id, int type, String path);
    }

    public ESHomeListVideoAdapter(ArrayList<HashMap<String,Object>> items, ListItemVideoListener context){
        this.items = items;
        this.mOnClickListener = context;

    }
    @Override
    public ESHomeListVideoAdapter.ContentHolderVideo onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_home_watch_listview_item, parent,  false);
        return new ContentHolderVideo(thisView);
    }

    @Override
    public void onBindViewHolder(ESHomeListVideoAdapter.ContentHolderVideo holder, int position) {
        holder.bindContent(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
