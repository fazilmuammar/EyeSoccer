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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leo on 1/28/17.
 */

public class ESListRefereeAdapter extends RecyclerView.Adapter<ESListRefereeAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;

    private ListItemClickListener mOnClickListener;

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView img;
        private TextView name, profesi;
        private HashMap<String, Object> item;
        private Context context;

        public ContentHolder(View v){
            super(v);

            img = (CircleImageView) v.findViewById(R.id.img);
            name = (TextView) v.findViewById(R.id.tName);
            profesi = (TextView) v.findViewById(R.id.tProfesi);
            v.setOnClickListener(this);

            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
//            Log.d("log", "click --> "+item.get("id"));
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(items.get(clickedPosition).get("id").toString(), Params.TYPE_NEWS);
        }

        public void bindContent(HashMap<String, Object> item, int position) {
            this.item = item;

            String imgPath = item.get("photo_url").toString();
            Glide.with(context)
                    .load(imgPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(img);

            String title = item.get("name").toString();
            name.setText(title);

            String sProfesi = item.get("profession").toString();
            profesi.setText(sProfesi);

        }
    }

    public interface ListItemClickListener{
        void onListItemClick(String id, int type);
    }

    public ESListRefereeAdapter(ArrayList<HashMap<String,Object>> items, ListItemClickListener mOnClickListener){
        this.items = items;
//        this.context = context;
        this.mOnClickListener = mOnClickListener;

    }
    @Override
    public ESListRefereeAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_referee_listview_item, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESListRefereeAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position), position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
