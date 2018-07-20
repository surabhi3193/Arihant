package com.arihanteducationgroup.online.indore.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.arihanteducationgroup.online.indore.R;

import java.util.ArrayList;

/**
 * Created by Mind Info- Android on 18-Nov-17.
 */

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

    Context context;
    private ArrayList<String> alName;
    private ArrayList<String> alImage;
    private ArrayList<String> alurl;

    public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<String> alImage, ArrayList<String> alurl) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
        this.alurl = alurl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tvSpecies.setText(alName.get(i));
//        viewHolder.imgThumbnail.setBackgroundResource(alImage.get(i));
        Glide.with(context).load(alImage.get(i)).into(viewHolder.imgThumbnail);

        viewHolder.play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    playvideo(context, alurl.get(i));

            }
        });
        System.out.println("-------- video at " + i + " " + alurl.get(i));
    }

    private void playvideo(Context context, String uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));

    }

    @Override
    public int getItemCount() {
        return alImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail, play_btn;
        private TextView tvSpecies;


        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            play_btn = (ImageView) itemView.findViewById(R.id.play_btn);
            tvSpecies =itemView.findViewById(R.id.nameTV);

        }

    }

}
