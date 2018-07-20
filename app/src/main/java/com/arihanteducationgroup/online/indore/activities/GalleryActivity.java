package com.arihanteducationgroup.online.indore.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.adapter.GalleryAdapter;
import com.arihanteducationgroup.online.indore.adapter.HLVAdapter;
import com.arihanteducationgroup.online.indore.other.ImageModel;
import com.example.admin.arihant.other.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GalleryActivity extends BaseActivity {

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerView_vdo;
    ArrayList<ImageModel> data = new ArrayList<>();

    public static String IMGS[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TextView headerTv = findViewById(R.id.header_text);
        ImageView back_btn = findViewById(R.id.back_btn);

        getDashBoardData();



            mRecyclerView_vdo = findViewById(R.id.recycler_view);
            mRecyclerView = (RecyclerView) findViewById(R.id.list);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mRecyclerView.setHasFixedSize(true);



            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                    new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent = new Intent(GalleryActivity.this, DetailActivity.class);
                            intent.putParcelableArrayListExtra("data", data);
                            intent.putExtra("pos", position);
                            startActivity(intent);

                        }
                    }));

          RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GalleryActivity.this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView_vdo.setLayoutManager(mLayoutManager);



        headerTv.setText("Gallery");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void getDashBoardData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(NetworkClass.BASE_URL_NEW + "video_list", new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("============ video_list ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("1")) {

                       String imageSet = response.getString("gallery");
                        IMGS = imageSet.split(",");
                       JSONArray vdo_array = response.getJSONArray("video");
                        ArrayList<String> alName = new ArrayList<>();
                        ArrayList<String> alurl = new ArrayList<>();
                        ArrayList<String> alImage = new ArrayList<>();

                        for (int i = 0; i < IMGS.length; i++)
                        {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName("Image " + i);
                            imageModel.setUrl(IMGS[i]);
                            data.add(imageModel);

                        }

                        mAdapter = new GalleryAdapter(GalleryActivity.this, data);
                        mRecyclerView.setAdapter(mAdapter);

                        if (vdo_array.length()>0) {
                            for (int i = 0; i < vdo_array.length(); i++) {
                                JSONObject jsonObject = vdo_array.getJSONObject(i);
                                System.out.println("--------- vdos obj -------");
                                System.out.println(jsonObject);

                                String name = jsonObject.getString("video_name");
                                String img = jsonObject.getString("video_thumbnail");
                                String url = jsonObject.getString("video_url");
                                alName.add(name);
                                alImage.add(img);
                                alurl.add(url);
                            }
                            HLVAdapter mAdapter = new HLVAdapter(GalleryActivity.this, alName, alImage, alurl);
                            mRecyclerView_vdo.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }

                        return;
                    }

                    Toast.makeText(GalleryActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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