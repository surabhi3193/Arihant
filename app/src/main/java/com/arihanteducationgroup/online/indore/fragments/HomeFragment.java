package com.arihanteducationgroup.online.indore.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.activities.ContactUsActivity;
import com.arihanteducationgroup.online.indore.activities.CourseActivity;
import com.arihanteducationgroup.online.indore.activities.GalleryActivity;
import com.arihanteducationgroup.online.indore.activities.HtmlActvity;
import com.arihanteducationgroup.online.indore.activities.NewsActivity;
import com.arihanteducationgroup.online.indore.activities.ResultActivity;
import com.arihanteducationgroup.online.indore.activities.VacancyActivity;
import com.arihanteducationgroup.online.indore.adapter.SliderAdapter;
import com.arihanteducationgroup.online.indore.other.GPSTracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkLocationPermission;

public class HomeFragment extends Fragment {



    RecyclerView.LayoutManager mLayoutManager;
    Class fragmentClass;
    private ViewPager mPager;
    private CircleIndicator indicator;
    int currentPage = 0;
    JSONArray vdo_array;
    String  imageSet;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, null);

        LinearLayout about_btn = v.findViewById(R.id.about_btn);
        LinearLayout contact_btn = v.findViewById(R.id.contact_btn);
        LinearLayout vacancy_btn = v.findViewById(R.id.vacancy_btn);
        LinearLayout result_btn = v.findViewById(R.id.result_btn);
        LinearLayout course_btn = v.findViewById(R.id.courses_btn);
        LinearLayout address_btn = v.findViewById(R.id.address_btn);
        LinearLayout exam_btn = v.findViewById(R.id.exam_btn);
        LinearLayout news_btn = v.findViewById(R.id.news_btn);
        LinearLayout gallery_btn = v.findViewById(R.id.gallery_btn);
        LinearLayout affairs_btn = v.findViewById(R.id.affairs_btn);

        mPager = v.findViewById(R.id.pager);
        indicator = v.findViewById(R.id.indicator);


        ScrollView scroll = v.findViewById(R.id.scrollview);

        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);



        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HtmlActvity.class);
                i.putExtra("header_name", "About Us");
                startActivity(i);
            }
        });

        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ContactUsActivity.class);
                i.putExtra("header_name", "Contact");
                startActivity(i);
            }
        });

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getActivity(), GalleryActivity.class);

                    startActivity(i);
            }
        });


        vacancy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), VacancyActivity.class);
                i.putExtra("header_name", "Vacancy");
                startActivity(i);
            }
        });
        news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewsActivity.class);
                i.putExtra("header_name", "News");
                startActivity(i);
            }
        });

        affairs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewsActivity.class);
                i.putExtra("header_name", "Current affairs");
                startActivity(i);
            }
        });

        exam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(getActivity(), HtmlActvity.class);
                i.putExtra("header_name", "Online Exam");
                startActivity(i);
            }
        });

        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ResultActivity.class);

                startActivity(i);
            }
        });


        course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CourseActivity.class);

                startActivity(i);
            }
        });

        address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean per = checkLocationPermission(getActivity());
                if (per) {

                    GPSTracker gps = new GPSTracker(getActivity());
                    double latitude = 0.0;
                    latitude = gps.getLatitude();
                    double longitude = 0.0;
                    longitude = gps.getLongitude();

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:"+latitude+","+longitude+"?q=22.676156,75.8786764 (" + "Arihant Education Group" + ")"));
                    startActivity(intent);
                }
            }
        });
        getDashBoardData();
        return v;
    }

    private void getDashBoardData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(NetworkClass.BASE_URL_NEW + "slider_image", new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("============ video_list ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {
                        ArrayList<String> imageArray = new ArrayList<>();

                        imageSet = response.getString("slide");
                        String []images = imageSet.split(",");
                        Collections.addAll(imageArray, images);
                        init(imageArray);

                        return;
                    }
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.err.println(errorResponse);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.err.println(responseString);
            }
        });


    }

    private void init(final ArrayList<String> imageArray) {
        mPager.setAdapter(new SliderAdapter(getActivity(), imageArray));
        mPager.setBackgroundColor(getResources().getColor(R.color.black));
        indicator.setViewPager(mPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageArray.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
}
