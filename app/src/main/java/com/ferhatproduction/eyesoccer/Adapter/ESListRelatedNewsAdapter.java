package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ESListRelatedNewsAdapter extends RecyclerView.Adapter<ESListRelatedNewsAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;

    private RelatedListItemClickListener mOnClickListener;

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView img;
        private TextView newsTitle, newsTime;
        private HashMap<String, Object> item;
        private Context context;

        public ContentHolder(View v){
            super(v);

            img = (ImageView) v.findViewById(R.id.thumbNews);
            newsTitle = (TextView) v.findViewById(R.id.tTitle);
//            newsTime = (TextView) v.findViewById(R.id.tTime);
            v.setOnClickListener(this);

            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRelatedListItemClick(items.get(clickedPosition).get("id").toString(), Params.TYPE_NEWS);
        }

        public void bindContent(HashMap<String, Object> item) {
            this.item = item;

            String imgPath = item.get("featured_image_url").toString();
            Glide.with(context).load(imgPath).into(img);

            String title = item.get("title").toString();
            newsTitle.setText(title);

//            Log.d("log","---> related title : "+ item);
        }
    }

    public interface RelatedListItemClickListener{
        void onRelatedListItemClick(String id, int type);
    }

    public ESListRelatedNewsAdapter(ArrayList<HashMap<String,Object>> items, RelatedListItemClickListener mOnClickListener){
        this.items = items;
        this.mOnClickListener = mOnClickListener;


    }
    @Override
    public ESListRelatedNewsAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_news_related_listview_item, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESListRelatedNewsAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position));
        Log.d("log","---> items ("+position+")"+ items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
