package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 1/28/17.
 */

public class ESListEventsAdapter extends RecyclerView.Adapter<ESListEventsAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;


    private static ListItemEventListener mOnEventClickListener;

    public static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RoundedImageView img;
        private TextView txt;
        private HashMap<String, Object> item;
        private Context context;

        public ContentHolder(View v){
            super(v);

            img = (RoundedImageView) v.findViewById(R.id.thumbNews);
            txt = (TextView) v.findViewById(R.id.textNews);
            v.setOnClickListener(this);
            context = v.getContext();
        }

        @Override
        public void onClick(View view) {
//            Log.d("log", "click --> "+item.get("id"));
            mOnEventClickListener.onListEventItemClick(item.get("id").toString());

        }

        public void bindContent(HashMap<String, Object> item) {
            this.item = item;

            String imgPath = item.get("featured_image_url").toString();
            Glide.with(context).load(imgPath).into(img);

            String title = item.get("title").toString();
            txt.setText(title);
        }
    }

    public interface ListItemEventListener {
        void onListEventItemClick(String id);
    }

    public ESListEventsAdapter(ArrayList<HashMap<String,Object>> items, ListItemEventListener context){
        this.items = items;
        this.mOnEventClickListener = context;

    }
    @Override
    public ESListEventsAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_es_home_events_listview, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESListEventsAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }




}
