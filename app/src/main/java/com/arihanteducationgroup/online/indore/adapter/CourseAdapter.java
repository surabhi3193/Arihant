package com.arihanteducationgroup.online.indore.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.Course;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Activity mContext;
    private String act_name;
    private ArrayList<Course> dataSet;

    public CourseAdapter(ArrayList<Course> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String date = "";
        final Course vacancy = dataSet.get(position);
        viewHolder.nameTV.setText(vacancy.getcourses_name());
        viewHolder.durationTV.setText(vacancy.getcourses_duration());
        viewHolder.eligibilityTV.setText(vacancy.getcourses_eligibility());
        viewHolder.feeTv.setText(vacancy.getFees_scholarship());
        viewHolder.admissionTV.setText(vacancy.getadmission_process());
        viewHolder.seatsTv.setText(vacancy.getcourses_seats());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTV, durationTV, feeTv, admissionTV, seatsTv, eligibilityTV;

        private ViewHolder(View convertView) {
            super(convertView);

            nameTV = convertView.findViewById(R.id.title);
            eligibilityTV = convertView.findViewById(R.id.eligibilityTV);
            seatsTv = convertView.findViewById(R.id.seatsTv);
            feeTv = convertView.findViewById(R.id.feeTv);
            admissionTV = convertView.findViewById(R.id.admissionTV);
            durationTV = convertView.findViewById(R.id.durationTv);


        }
    }
}
