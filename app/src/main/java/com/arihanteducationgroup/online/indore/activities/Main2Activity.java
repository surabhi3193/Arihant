package com.arihanteducationgroup.online.indore.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.adapter.VacancyAdapter;
import com.arihanteducationgroup.online.indore.fragments.HomeFragment;
import com.arihanteducationgroup.online.indore.notification.Config;
import com.arihanteducationgroup.online.indore.other.GPSTracker;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.arihanteducationgroup.online.indore.other.Vacancy;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;
import static com.arihanteducationgroup.online.indore.other.MySharedPref.saveData;

public class Main2Activity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment;
    AdvanceDrawerLayout drawer;
    Class fragmentClass;
    Intent i;
    boolean doubleBackToExitPressedOnce = false;
    NavigationView nav_view_notification;
    private TextView user_nameTV;
    private ImageView imageView;
    private String student_id;
    private RecyclerView recyclerView;

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        student_id = getData(Main2Activity.this, "student_id", "");
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        nav_view_notification = findViewById(R.id.nav_view_notification);
        drawer = findViewById(R.id.drawer_layout);
        View headerview = navigationView.getHeaderView(0);
        user_nameTV = headerview.findViewById(R.id.user_nameTV);
        imageView = headerview.findViewById(R.id.imageView);

        navigationView.setItemIconTintList(null);
        navigationView.setVerticalScrollBarEnabled(false);

        disableNavigationViewScrollbars(navigationView);
        recyclerView = nav_view_notification.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main2Activity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(Main2Activity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 10);
        drawer.setViewElevation(Gravity.START, 60);
        fragmentClass = HomeFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, EditProfileActivity.class));
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                System.out.println(" onDrawerSlide");
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                System.out.println(" onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                System.out.println("Drawer onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                System.out.println("State " + newState);

            }
        });

    }

    @Override
    public void onBackPressed() {
        String fr = "";
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            System.out.println("---------- right nav open ---------");
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
            System.out.println(frag);
            if (frag != null && !frag.getTag().equalsIgnoreCase("home")) {
                super.onBackPressed();
                drawer.closeDrawer(GravityCompat.END);

            } else if (doubleBackToExitPressedOnce) {
                finishAffinity();

            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home_bar:
                fragmentClass = HomeFragment.class;
                drawer.closeDrawer(GravityCompat.START);

                break;

            case R.id.aboutus:
                i = new Intent(Main2Activity.this, HtmlActvity.class);
                i.putExtra("header_name", "About Us");
                startActivity(i);
                break;

            case R.id.contact:
                i = new Intent(Main2Activity.this, ContactUsActivity.class);
                i.putExtra("header_name", "Contact");
                startActivity(i);
                break;
            case R.id.vacancy:
                i = new Intent(Main2Activity.this, VacancyActivity.class);
                i.putExtra("header_name", "Vacancy");
                startActivity(i);
                break;


            case R.id.exams:

//                Intent i = new Intent(Main2Activity.this, NewsActivity.class);
//                i.putExtra("header_name", "Online Exams");
//                startActivity(i);

                i = new Intent(Main2Activity.this, HtmlActvity.class);
                i.putExtra("header_name", "Online Exam");
                startActivity(i);
                break;


            case R.id.address:
                GPSTracker gps = new GPSTracker(Main2Activity.this);
                double latitude = 0.0;
                latitude = gps.getLatitude();
                double longitude = 0.0;
                longitude = gps.getLongitude();

//              String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Arihant College" + ")";
//
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=22.676156,75.8786764"));
//                startActivity(intent);


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:"+latitude+","+longitude+"?q=22.676156,75.8786764 (" + "Arihant Education Group " + ")"));
                startActivity(intent);

                break;

            case R.id.share:
                try {
                    i = new Intent("android.intent.action.SEND");
                    i.setType(HTTP.PLAIN_TEXT_TYPE);
                    i.putExtra("android.intent.extra.SUBJECT", "Arihant Education Group");
                    i.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id="+getPackageName() + "&hl=en");
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.logout:
                LogoutAlertDialog();
                break;

                case R.id.rate:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
                    break;
        }

        return true;
    }

    private void LogoutAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this, R.style.Theme_AppCompat_Dialog);
        ab.setMessage("Are you sure you want to logout");
        ab.setNegativeButton("logout", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout(student_id);
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_right_drawer:
                drawer.openDrawer(Gravity.END);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = getData(Main2Activity.this, "full_name", "");
        String user_photo = getData(Main2Activity.this, "user_photo", "");

        user_nameTV.setText(username);

        if (user_photo.length() > 0)
            Glide.with(Main2Activity.this).load(user_photo).into(imageView);

        getNotifications();
    }


    private void logout(String userid) {

        final Dialog dialog = new Dialog(Main2Activity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        System.out.println("- ---  logout --------");
        System.out.println(regId);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("studentID", userid);
        System.err.println(params);

        client.post(NetworkClass.BASE_URL_NEW + "logout", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("================ logout api ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    saveData(Main2Activity.this, "login", false);
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                    finish();
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


    private void getNotifications() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(NetworkClass.BASE_URL_NEW + "notification_list", new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("============ notification_list ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                        ArrayList<Vacancy> vacancydata = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("notification");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String vacancies_id = jsonObject.getString("noti_id");
                            String vacancies_title = jsonObject.getString("noti_title");
                            String vacancies_icon_img = jsonObject.getString("noti_img");
                            String vacancies_noti_pdf ="";
                            String vacancies_desc = jsonObject.getString("noti_desc");
                            String date = jsonObject.getString("time");
                            String type = jsonObject.getString("noti_type");
                            Vacancy vacancy = new Vacancy(vacancies_id, vacancies_title, vacancies_icon_img, vacancies_desc, vacancies_noti_pdf, date, type);
                            vacancydata.add(vacancy);

                        }

                        VacancyAdapter mAdapter = new VacancyAdapter(vacancydata, Main2Activity.this);

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

                System.err.println(errorResponse);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.err.println(responseString);
            }
        });
    }
}
