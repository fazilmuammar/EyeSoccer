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
            String title = items.get(clickedPosition).get("title").toString();
            String path = items.get(clickedPosition).get("video_url").toString();
            String _id = items.get(clickedPosition).get("id").toString();
            Float f = Float.parseFloat((String) items.get(clickedPosition).get("duration"));

            int _duration = Integer.parseInt(String.valueOf(Math.ceil(f)));
            Log.d("log", String.valueOf(_duration));
            long createdate = (long)items.get(clickedPosition).get("create_date");
            mOnClickListener.onListItemVideoClick(_id, Params.TYPE_WATCH, path, _duration, title, createdate);
        }

//        private String getDurationString(int seconds) {
//
//            int hours = seconds / 3600;
//            int minutes = (seconds % 3600) / 60;
//            seconds = seconds % 60;
//
//            Log.d("log"," duration ---> "+twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds));
////            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
//            return twoDigitString(hours) + " : " + twoDigitString(minutes);
//        }
//
//        private String twoDigitString(int number) {
//
//            if (number == 0) {
//                return "00";
//            }
//
//            if (number / 10 == 0) {
//                return "0" + number;
//            }
//
//            return String.valueOf(number);
//        }

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

//            int duration;
            try {
                Double duration = Double.parseDouble(item.get("duration").toString());
                int durationInt = (int) Math.ceil(duration);
                tDuration.setText(Params.getDurationString(durationInt));
            } catch (Exception e){
                Log.d("log", "Error bindContent : "+e.getMessage());
                tDuration.setText("00:00");
            }



        }
    }



    public interface ListItemVideoListener{
        void onListItemVideoClick(String id, int type, String path, int duration, String title, long createdate);
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
