package com.yaksha.nota.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.yaksha.nota.R;
import com.yaksha.nota.ViewImageActivity;
import com.yaksha.nota.model.GalleryItem;

/**
 * Created by QUINTUSLABS on 13/04/2018.
 */

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<GalleryItem> icon;


    public SliderAdapter(Context context, ArrayList<GalleryItem> icon) {
        this.context = context;
        this.icon = icon;

    }

    @Override
    public int getCount() {
        return icon.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView imageView = view.findViewById(R.id.imageview);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.activity_app_bg)
                .error(R.drawable.activity_app_bg);

        Glide.with(context).load(icon.get(position).getImgUrl()).apply(options).into(imageView);

        imageView.setOnClickListener((View.OnClickListener) view1 -> {
            GalleryItem img = (GalleryItem) icon.get(position);

            Intent intent = new Intent(context, ViewImageActivity.class);
            intent.putExtra("itemId", img.getId());
            context.startActivity(intent);
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
