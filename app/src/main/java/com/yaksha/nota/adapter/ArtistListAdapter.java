package com.yaksha.nota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.yaksha.nota.R;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.model.Artist;
import com.yaksha.nota.model.Chat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> implements Constants {

    private Context ctx;
    private List<Artist> items;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Artist item, int position);
    }

    public void setOnItemClickListener(final ArtistListAdapter.OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username, subname;
        public LinearLayout parent;
        public CircularImageView image;

        public ViewHolder(View view) {

            super(view);

            username = (TextView) view.findViewById(R.id.username);
            subname = (TextView) view.findViewById(R.id.subname);
            parent = (LinearLayout) view.findViewById(R.id.parent);
            image = (CircularImageView) view.findViewById(R.id.image);
        }
    }

    public ArtistListAdapter(Context mContext, List<Artist> items) {

        this.ctx = mContext;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_list_row, parent, false);

        return new ArtistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Artist item = items.get(position);

        holder.username.setText(item.getFullname());
        holder.subname.setText(item.getMela());

        if (item.getLowPhotoUrl().length() > 0) {
            Glide.with(ctx).load(item.getLowPhotoUrl())
                    .into(holder.image);

        } else {

            holder.image.setImageResource(R.drawable.profile_default_photo);
        }

        holder.parent.setOnClickListener(v -> {

            if (mOnItemClickListener != null) {

                mOnItemClickListener.onItemClick(v, items.get(position), position);
            }
        });
    }

    public Artist getItem(int position) {

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