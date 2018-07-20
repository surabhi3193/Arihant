package com.arihanteducationgroup.online.indore.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import android.os.Bundle;
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
import com.arihanteducationgroup.online.indore.adapter.ExamAdapter;
import com.arihanteducationgroup.online.indore.adapter.VacancyAdapter;
import com.arihanteducationgroup.online.indore.other.Exam;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.arihanteducationgroup.online.indore.other.Vacancy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;

public class NewsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private  String header_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.recycler_view);
        TextView headerTv =findViewById(R.id.header_text);
        ImageView back_btn =findViewById(R.id.back_btn);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
             header_name = bundle.getString("header_name","");
            headerTv.setText(header_name);
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(NewsActivity.this, LinearLayoutManager.VERTICAL));
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
        if (header_name.equalsIgnoreCase("News"))
        getNews();

        else if(header_name.equalsIgnoreCase("Current affairs"))
            getCurrentAffairs();

        else
            getExams();
    }

    private void getExams() {
        final Dialog dialog = new Dialog(NewsActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        String id = getData(NewsActivity.this, "student_id", "");
        params.put("studentID", id);

        client.post(NetworkClass.BASE_URL_NEW + "online_exam",params ,new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("============ online_exam ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                        ArrayList<Exam> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("exams");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String exam_id = jsonObject.getString("onlineExamID");
                            String name = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            String duration = jsonObject.getString("duration");
                            String payment_type = jsonObject.getString("payment_type");
                            String exam_fees = jsonObject.getString("exam_fees");
                            String date = jsonObject.getString("create_date");
                            Exam vacancy = new Exam(exam_id,name,duration,description,payment_type,exam_fees,date);
                            vacancydata.add(vacancy);

                        }

                        ExamAdapter mAdapter = new ExamAdapter(vacancydata, NewsActivity.this);

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
        final Dialog dialog = new Dialog(NewsActivity.this, R.style.Theme_AppCompat_Dialog);
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

                        VacancyAdapter mAdapter = new VacancyAdapter(vacancydata, NewsActivity.this);

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


    private void getCurrentAffairs() {
        final Dialog dialog = new Dialog(NewsActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(NetworkClass.BASE_URL_NEW + "currentaffairs_list", new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("============ currentaffairs_list ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                        ArrayList<Vacancy> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("current_affairs_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String vacancies_id = jsonObject.getString("c_affairs_id");
                            String vacancies_title = jsonObject.getString("title");
                            String vacancies_icon_img = jsonObject.getString("image_pdf");
                            String vacancies_noti_pdf = jsonObject.getString("pdf");
                            String vacancies_desc = jsonObject.getString("\tnotice");
                            String date =jsonObject.getString("time");
                            Vacancy vacancy = new Vacancy(vacancies_id,vacancies_title,vacancies_icon_img,vacancies_desc,vacancies_noti_pdf,date,"");
                            vacancydata.add(vacancy);

                        }

                        VacancyAdapter mAdapter = new VacancyAdapter(vacancydata, NewsActivity.this);

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
