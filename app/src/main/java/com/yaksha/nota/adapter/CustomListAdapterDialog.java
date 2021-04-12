package com.yaksha.nota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.yaksha.nota.CreateEventFragment;
import com.yaksha.nota.R;
import com.yaksha.nota.model.MelaList;

public class CustomListAdapterDialog extends BaseAdapter {

    private ArrayList<MelaList> listData;
    private LayoutInflater layoutInflater;
    private Context ctx;
    public final PositionClickListener listener;

    public CustomListAdapterDialog(Context context, CreateEventFragment lfragment, ArrayList<MelaList> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.ctx = context;
        this.listener = lfragment;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.unitView = (TextView) convertView.findViewById(R.id.unit);
            holder.quantityView = (TextView) convertView.findViewById(R.id.quantity);
            holder.img = (ImageView) convertView.findViewById(R.id.video_image) ;
            holder.main_layout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.unitView.setText(listData.get(position).getFullname().toString());
        holder.quantityView.setText(listData.get(position).getLogin().toString());
        Picasso.with(ctx)
                .load(listData.get(position).getNormalPhotoUrl())
                .into(holder.img, new Callback() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        holder.img.setImageResource(R.drawable.img_loading_error);
                    }
                });
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView unitView;
        TextView quantityView;
        RelativeLayout main_layout;
    }


    public interface PositionClickListener {
        void itemClicked(int position); }
}




