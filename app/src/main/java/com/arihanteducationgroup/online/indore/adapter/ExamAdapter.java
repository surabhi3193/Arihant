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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.activities.DetailVacancy;
import com.arihanteducationgroup.online.indore.other.Exam;

import java.util.ArrayList;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private Activity mContext;
    private String act_name;
    private ArrayList<Exam> dataSet;

    public ExamAdapter(ArrayList<Exam> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String date ="";
        final Exam vacancy = dataSet.get(position);
        viewHolder.txtName.setText(vacancy.getExam_title());
        viewHolder.dateTV.setText(vacancy.getpayment_type());

        if (vacancy.getpayment_type().equalsIgnoreCase("free"))
            viewHolder.dateTV.setTextColor(mContext.getResources().getColor(R.color.green));
        else {
            viewHolder.dateTV.setText(vacancy.getexam_fees()+" INR");
            viewHolder.dateTV.setTextColor(mContext.getResources().getColor(R.color.light_red));
        }


        viewHolder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("vacancies_title", vacancy.getExam_title());
                bundle.putString("vacancies_icon_img", "");
                bundle.putString("vacancies_desc", vacancy.getExam_desc());
                bundle.putString("date", "Duration : "+vacancy.getExam_duration() + "  Minutes");
                mContext.startActivity(new Intent(mContext, DetailVacancy.class).putExtras(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,dateTV;
        private LinearLayout mainLay;

        private ViewHolder(View convertView) {
            super(convertView);
            dateTV = convertView.findViewById(R.id.dateTV);
            txtName = convertView.findViewById(R.id.title);
            mainLay = convertView.findViewById(R.id.mainLay);
        }
    }
}
