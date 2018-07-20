package com.arihanteducationgroup.online.indore.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import com.arihanteducationgroup.online.indore.adapter.CourseAdapter;
import com.arihanteducationgroup.online.indore.adapter.VacancyAdapter;
import com.arihanteducationgroup.online.indore.other.Course;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.arihanteducationgroup.online.indore.other.Vacancy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        recyclerView = findViewById(R.id.recycler_view);
        TextView headerTv =findViewById(R.id.header_text);
        ImageView back_btn =findViewById(R.id.back_btn);


            headerTv.setText("College Courses");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CourseActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(CourseActivity.this, LinearLayoutManager.VERTICAL));
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
            getExams();
    }

    private void getExams() {
        final Dialog dialog = new Dialog(CourseActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        client.post(NetworkClass.BASE_URL_NEW + "course_name",params ,new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("============ course_name ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                        ArrayList<Course> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("courses");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String exam_id = jsonObject.getString("courses_id");
                            String name = jsonObject.getString("courses_name");
                            String description = jsonObject.getString("cours_full_name");
                            String duration = jsonObject.getString("courses_duration");
                            String courses_eligibility = jsonObject.getString("courses_eligibility");
                            String courses_seats = jsonObject.getString("courses_seats");
                            String admission_process = jsonObject.getString("admission_process");
                            String fees_scholarship = jsonObject.getString("fees_scholarship");

                            Course vacancy = new Course(exam_id,name,duration,description,courses_eligibility,courses_seats,admission_process,fees_scholarship);
                            vacancydata.add(vacancy);

                        }

                        CourseAdapter mAdapter = new CourseAdapter(vacancydata, CourseActivity.this);

                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();


                        return;
                    }
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

    private void getNews() {
        final Dialog dialog = new Dialog(CourseActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(NetworkClass.BASE_URL_NEW + "news_list", new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("============ news_list ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                        ArrayList<Vacancy> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("news");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String vacancies_id = jsonObject.getString("news_id");
                            String vacancies_title = jsonObject.getString("title");
                            String vacancies_icon_img ="";
                            String vacancies_noti_pdf ="";
                            String vacancies_desc = jsonObject.getString("notice");
                            String date =jsonObject.getString("date");
                            Vacancy vacancy = new Vacancy(vacancies_id,vacancies_title,vacancies_icon_img,vacancies_desc,vacancies_noti_pdf,date,"");
                            vacancydata.add(vacancy);

                        }

                        VacancyAdapter mAdapter = new VacancyAdapter(vacancydata, CourseActivity.this);

                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();


                        return;
                    }
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
