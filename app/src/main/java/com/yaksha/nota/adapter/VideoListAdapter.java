package com.yaksha.nota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.yaksha.nota.R;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.model.Chat;
import com.yaksha.nota.model.NewVideo;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements Constants {

    private Context ctx;
    private List<NewVideo> items;
    private VideoListAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, NewVideo item, int position);
    }

    public void setOnItemClickListener(final VideoListAdapter.OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username, txt_desc,txt_count;
        public ImageView video_image,video_play_image;
        public ProgressBar video_progress_bar;
        public RelativeLayout video_layout;

        public ViewHolder(View view) {

            super(view);

            username = (TextView) view.findViewById(R.id.username);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            video_image = (ImageView) view.findViewById(R.id.video_image);
            video_play_image = (ImageView) view.findViewById(R.id.video_play_image);
            video_progress_bar = (ProgressBar) view.findViewById(R.id.video_progress_bar);
            video_layout = (RelativeLayout) view.findViewById(R.id.video_layout);

        }
    }

    public VideoListAdapter(Context mContext, List<NewVideo> items) {

        this.ctx = mContext;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final NewVideo item = items.get(position);

        holder.username.setText(item.getName());
        holder.txt_desc.setText(item.getDescription());
        holder.txt_count.setText(item.getLikesCount());

        Picasso.with(ctx)
                .load(item.getPreviewVideoImgUrl())
                .into(holder.video_image, new Callback() {

                    @Override
                    public void onSuccess() {

                        holder.video_progress_bar.setVisibility(View.GONE);
                        holder.video_play_image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                        holder.video_progress_bar.setVisibility(View.GONE);
                        holder.video_play_image.setVisibility(View.VISIBLE);
                        holder.video_image.setImageResource(R.drawable.img_loading_error);
                    }
                });

        holder.video_layout.setOnClickListener(v -> {

            if (mOnItemClickListener != null) {

                mOnItemClickListener.onItemClick(v, items.get(position), position);
            }
        });
    }

    public NewVideo getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public interface OnClickListener {

        void onItemClick(View view, Chat item, int pos);
    }
}