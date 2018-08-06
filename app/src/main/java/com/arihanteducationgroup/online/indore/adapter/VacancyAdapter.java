package com.arihanteducationgroup.online.indore.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.activities.DetailVacancy;
import com.arihanteducationgroup.online.indore.other.Vacancy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.ViewHolder> {

    private Activity mContext;
    private String act_name;
    private ArrayList<Vacancy> dataSet;

    public VacancyAdapter(ArrayList<Vacancy> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacancy_row, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String date = "";
        final Vacancy vacancy = dataSet.get(position);
        viewHolder.txtName.setText(vacancy.getvacancies_title());
        viewHolder.dateTV.setText(vacancy.getPost_date());


        viewHolder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("------- clicked--- vacancy adapter ");

                Bundle bundle = new Bundle();
                bundle.putString("vacancies_title", vacancy.getvacancies_title());
                bundle.putString("vacancies_icon_img", vacancy.getvacancies_icon_img());
                bundle.putString("vacancies_desc", vacancy.getvacancies_desc());
                bundle.putString("PDF", vacancy.getvacancies_noti_pdf());
                bundle.putString("date", vacancy.getPost_date());
                mContext.startActivity(new Intent(mContext, DetailVacancy.class).putExtras(bundle));
            }
        });


        if (vacancy.getvacancies_icon_img().length() > 0)
            Glide.with(mContext).load(vacancy.getvacancies_icon_img()).apply(RequestOptions
                    .placeholderOf(R.drawable.logo_icon)).into(viewHolder.imageView);
        else
            viewHolder.imageView.setVisibility(View.GONE);


        viewHolder.txtName.setMaxLines(2);
        viewHolder.txtName.setTextSize(13);
        viewHolder.dateTV.setTextSize(10);
        viewHolder.imageView.setPadding(15, 15, 15, 15);
        Glide.with(mContext).load(vacancy.getvacancies_icon_img()).apply(RequestOptions
                .placeholderOf(R.drawable.logo_icon)).into(viewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, dateTV;
        private ImageView imageView;
        private LinearLayout mainLay;


        private ViewHolder(View convertView) {
            super(convertView);
            txtName = convertView.findViewById(R.id.title);
            dateTV = convertView.findViewById(R.id.dateTV);
            imageView = convertView.findViewById(R.id.imageView);
            mainLay = convertView.findViewById(R.id.mainLay);
        }
    }
}
