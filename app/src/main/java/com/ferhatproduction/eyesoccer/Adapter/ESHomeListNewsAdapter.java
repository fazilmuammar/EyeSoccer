package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ESHomeListNewsAdapter extends RecyclerView.Adapter<ESHomeListNewsAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;

    private ListItemClickListener mOnClickListener;

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(items.get(clickedPosition).get("id").toString(), Params.TYPE_NEWS);
        }

        public void bindContent(HashMap<String, Object> item) {
            this.item = item;

            String imgPath = item.get("featured_image_url").toString();
            Glide.with(context).load(imgPath).into(img);

            String title = item.get("title").toString();
            txt.setText(title);

        }
    }

    public interface ListItemClickListener{
        void onListItemClick(String id, int type);
    }

    public ESHomeListNewsAdapter(ArrayList<HashMap<String,Object>> items, ListItemClickListener mOnClickListener){
        this.items = items;
//        this.context = context;
        this.mOnClickListener = mOnClickListener;

    }
    @Override
    public ESHomeListNewsAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_home_news_listview_item, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESHomeListNewsAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
