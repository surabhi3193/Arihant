package com.arihanteducationgroup.online.indore.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.ExamResult;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Activity mContext;
    private String act_name;
    private ArrayList<ExamResult> dataSet;

    public ResultAdapter(ArrayList<ExamResult> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_row, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (position==0)
            viewHolder.row1.setVisibility(View.VISIBLE);
        else
            viewHolder.row1.setVisibility(View.GONE);

        final ExamResult vacancy = dataSet.get(position);
        viewHolder.txtName.setText(vacancy.getExam_name());
        viewHolder.quesTv.setText(vacancy.getTotalQuestion());
        viewHolder.scoreTv.setText(vacancy.getScore());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,quesTv,scoreTv;
        private TableRow row1;

        private ViewHolder(View convertView) {
            super(convertView);
            txtName = convertView.findViewById(R.id.nameTV);
            quesTv = convertView.findViewById(R.id.qusTv);
            scoreTv = convertView.findViewById(R.id.marksTv);
            row1 = convertView.findViewById(R.id.row1);
        }
    }
}
