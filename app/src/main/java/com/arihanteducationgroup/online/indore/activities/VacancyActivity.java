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
import com.arihanteducationgroup.online.indore.adapter.VacancyAdapter;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.arihanteducationgroup.online.indore.other.Vacancy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VacancyActivity extends BaseActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy);
         recyclerView = findViewById(R.id.recycler_view);
        TextView headerTv =findViewById(R.id.header_text);
        ImageView back_btn =findViewById(R.id.back_btn);

        headerTv.setText("Vacancy");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VacancyActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(VacancyActivity.this, LinearLayoutManager.VERTICAL));
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
        getVacancy();
    }

    private void getVacancy() {
            final Dialog dialog = new Dialog(VacancyActivity.this, R.style.Theme_AppCompat_Dialog);
            dialog.setContentView(R.layout.loading);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            GifImageView gifview = dialog.findViewById(R.id.loaderGif);
            gifview.setGifImageResource(R.drawable.books);

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(NetworkClass.BASE_URL_NEW + "vacancy", new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialog.dismiss();
                    System.out.println("============ vacancy ========== sucsess ======");
                    System.err.println(response);
                    try {
                        if (response.getString("status").equals("1")) {

                            ArrayList<Vacancy> vacancydata = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("vacancy");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                String vacancies_id = jsonObject.getString("vacancies_id");
                                String vacancies_title = jsonObject.getString("vacancies_title");
                                String vacancies_icon_img = jsonObject.getString("vacancies_icon_img");
                                String vacancies_noti_pdf = jsonObject.getString("vacancies_noti_pdf");
                                String vacancies_desc = jsonObject.getString("vacancies_desc");
                                String date = jsonObject.getString("vacancies_date");

                                Vacancy vacancy = new Vacancy(vacancies_id,vacancies_title,vacancies_icon_img,vacancies_desc,vacancies_noti_pdf,date,"");
                                vacancydata.add(vacancy);

                            }

                            VacancyAdapter mAdapter = new VacancyAdapter(vacancydata, VacancyActivity.this);

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
