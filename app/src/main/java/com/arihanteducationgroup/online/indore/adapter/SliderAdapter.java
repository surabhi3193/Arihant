package com.arihanteducationgroup.online.indore.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.arihanteducationgroup.online.indore.R;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private ArrayList<String> images;

    private int size;
    private LayoutInflater inflater;
    private Activity context;
    private boolean clickable;

    public SliderAdapter(Activity context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        this.size = images.size();
        inflater = LayoutInflater.from(context);
        clickable = true;
    }

    public SliderAdapter(Activity context, ArrayList<String> images, int size) {
        this.context = context;
        this.images = images;
        this.size = size;
        inflater = LayoutInflater.from(context);
        clickable = false;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        if (size>4)
            size=4;

        return size;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = myImageLayout
                .findViewById(R.id.image);

        Glide.with(context).load(images.get(position)).into(myImage);

        if (clickable) {
            myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    context.startActivity(new Intent(context, FullImageActivity.class)
//                            .putParcelableArrayListExtra("imageArray", images)
//                    );

                }
            });
        }
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}