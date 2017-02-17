package com.ferhatproduction.eyesoccer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by leo on 1/28/17.
 */

public class ESListEventMatchesAdapter extends RecyclerView.Adapter<ESListEventMatchesAdapter.ContentHolder> {
    private ArrayList<HashMap<String,Object>> items;
    private Context context;

    private ListItemClickListener mOnClickListener;

    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView logoKiri, logoKanan;
        private TextView timKiri, timKanan, jam, tanggal;
        private Context context;
        private HashMap<String, Object> item;
        private LinearLayout header, info;
        private SimpleDateFormat dateFormat;

        public ContentHolder(View v){
            super(v);

            logoKiri = (ImageView) v.findViewById(R.id.logoKiri);
            logoKanan = (ImageView) v.findViewById(R.id.logoKanan);
            timKiri = (TextView) v.findViewById(R.id.timKiri);
            timKanan = (TextView) v.findViewById(R.id.timKanan);
            jam = (TextView) v.findViewById(R.id.jam);
            tanggal = (TextView) v.findViewById(R.id.tanggal);
            dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            header = (LinearLayout)v.findViewById(R.id.header);
            info = (LinearLayout)v.findViewById(R.id.info);
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
            String type = item.get("type").toString();

            if(type.equals("header")){
                info.setVisibility(View.GONE);
                Long d = Long.parseLong((String)item.get("value"));
                String tanggalString = dateFormat.format(new Date(d));
                tanggal.setText(tanggalString);

            } else {
                header.setVisibility(View.GONE);
                timKiri.setText(item.get("home_team").toString());
                timKanan.setText(item.get("away_team").toString());
                jam.setText(item.get("match_time").toString());

                Glide.with(context).load(item.get("home_team_image_url")).into(logoKiri);
                Glide.with(context).load(item.get("away_team_image_url")).into(logoKanan);
            }


        }
    }

    public interface ListItemClickListener{
        void onListItemClick(String id, int type);
    }

    public ESListEventMatchesAdapter(ArrayList<HashMap<String,Object>> items, Context context){
        this.items = items;
        this.context = context;
//        this.mOnClickListener = mOnClickListener;

    }
    @Override
    public ESListEventMatchesAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.es_events_matches_listview_item, parent,  false);
        return new ContentHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ESListEventMatchesAdapter.ContentHolder holder, int position) {
        holder.bindContent(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
