package com.yaksha.nota.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.yaksha.nota.EventViewerActivity;
import com.yaksha.nota.R;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.model.Adds;

public class AddsListAdapter extends RecyclerView.Adapter<AddsListAdapter.ViewHolder> implements Constants {

    private Context ctx;
    private List<Adds> items;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username, txt_desc;
        public ImageView video_image, itemLikeImg;
        public ProgressBar video_progress_bar;
        MaterialRippleLayout btn_like;
        RelativeLayout item_view;

        public ViewHolder(View view) {

            super(view);

            username = (TextView) view.findViewById(R.id.username);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            video_image = (ImageView) view.findViewById(R.id.video_image);
            itemLikeImg = (ImageView) view.findViewById(R.id.itemLikeImg);
            video_progress_bar = (ProgressBar) view.findViewById(R.id.video_progress_bar);
            btn_like = (MaterialRippleLayout) view.findViewById(R.id.itemLikeButton);
            item_view = (RelativeLayout) view.findViewById(R.id.item_view);
        }
    }

    public AddsListAdapter(Context mContext, List<Adds> items) {

        this.ctx = mContext;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adds_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Adds item = items.get(position);

        holder.username.setText(item.getName());
        holder.txt_desc.setText(item.getDesc());


        Picasso.with(ctx)
                .load(item.getOriginImgUrl())
                .into(holder.video_image, new Callback() {

                    @Override
                    public void onSuccess() {

                        holder.video_progress_bar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {

                        holder.video_progress_bar.setVisibility(View.GONE);
                        holder.video_image.setImageResource(R.drawable.img_loading_error);
                    }
                });
        if (item.getActive().equals("1")) {
            holder.itemLikeImg.setImageResource(R.drawable.ic_like_active);
        }

        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, EventViewerActivity.class);
                i.putExtra("EventName", item.getName());
                i.putExtra("Desc",item.getDesc());
                i.putExtra("EventMela", "");
                i.putExtra("Date","");
                i.putExtra("Image",item.getOriginImgUrl());
                ctx.startActivity(i);
            }
        });

    }

    public Adds getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

}
