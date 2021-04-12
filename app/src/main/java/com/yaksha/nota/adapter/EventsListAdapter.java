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
import com.yaksha.nota.model.MEvent;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> implements Constants {

    private Context ctx;
    private List<MEvent> items;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username, txt_desc,txt_place,txt_date;
        public ImageView video_image;
        public ProgressBar video_progress_bar;
        MaterialRippleLayout btn_like;
        private RelativeLayout event_item;

        public ViewHolder(View view) {

            super(view);
            event_item = (RelativeLayout) view.findViewById(R.id.event_item);
            username = (TextView) view.findViewById(R.id.username);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            video_image = (ImageView) view.findViewById(R.id.video_image);
            txt_place = (TextView) view.findViewById(R.id.txt_place) ;
            video_progress_bar = (ProgressBar) view.findViewById(R.id.video_progress_bar);
            btn_like = (MaterialRippleLayout) view.findViewById(R.id.itemLikeButton);
            txt_date = (TextView) view.findViewById(R.id.txt_date);

        }
    }

    public EventsListAdapter(Context mContext, List<MEvent> items) {

        this.ctx = mContext;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MEvent item = items.get(position);

        holder.username.setText(item.getEvent_name());
        holder.txt_desc.setText(item.getEvent_description());
        holder.txt_place.setText(item.getEvent_mela());
        holder.txt_date.setText(item.getEvent_date());
        Picasso.with(ctx)
                .load(item.getEvent_image())
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

        holder.event_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, EventViewerActivity.class);
                i.putExtra("EventName", item.getEvent_name());
                i.putExtra("Desc",item.getEvent_description());
                i.putExtra("EventMela", item.getEvent_mela());
                i.putExtra("Date",item.getEvent_date());
                i.putExtra("Image",item.getEvent_image());
                ctx.startActivity(i);
            }
        });

    }

    public MEvent getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

}