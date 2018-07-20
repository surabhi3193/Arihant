package com.arihanteducationgroup.online.indore.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.adapter.ResultAdapter;
import com.arihanteducationgroup.online.indore.other.ExamResult;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;

public class ResultActivity extends BaseActivity {

    private GifImageView loader;
    private RecyclerView recyclerView;
    private TextView loadetv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.recycler_view);
        TextView headerTv = findViewById(R.id.header_text);
        ImageView back_btn = findViewById(R.id.back_btn);
        loader = findViewById(R.id.gifview);
        loadetv = findViewById(R.id.loadertxt);
        loader.setGifImageResource(R.drawable.exam);
        headerTv.setText("Results");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ResultActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ResultActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getResult();
    }

    private void getResult() {
        final Dialog dialog = new Dialog(ResultActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        String id = getData(ResultActivity.this, "student_id", "");
        params.put("studentID", id);

        client.post(NetworkClass.BASE_URL_NEW + "exam_results", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("============ exam_results ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {
                        recyclerView.setVisibility(View.VISIBLE);

                        loader.setVisibility(View.GONE);
                        loadetv.setVisibility(View.GONE);
                        ArrayList<ExamResult> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String exam_id = jsonObject.getString("exam_id");
                            String exam_name = jsonObject.getString("exam_name");
                            String totalQuestion = jsonObject.getString("totalQuestion");
                            String score = jsonObject.getString("score");
                            ExamResult vacancy = new ExamResult(exam_id, exam_name, totalQuestion, score);
                            vacancydata.add(vacancy);

                        }

                        ResultAdapter mAdapter = new ResultAdapter(vacancydata, ResultActivity.this);

                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    recyclerView.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);
//                    loadetv.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                System.err.println(errorResponse);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();
                System.err.println(responseString);
            }
        });


    }
}
