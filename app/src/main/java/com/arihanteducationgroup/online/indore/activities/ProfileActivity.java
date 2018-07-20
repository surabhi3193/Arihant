package com.arihanteducationgroup.online.indore.activities;

import android.os.Bundle;


import com.arihanteducationgroup.online.indore.R;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String id = getData(ProfileActivity.this, "student_id", "");
        String full_name = getData(ProfileActivity.this, "full_name", "");
        String dob = getData(ProfileActivity.this, "dob", "");
        String email = getData(ProfileActivity.this, "email", "");
        String phone = getData(ProfileActivity.this, "phone", "");
        String address = getData(ProfileActivity.this, "address", "");
        String username = getData(ProfileActivity.this, "username", "");
        String user_photo = getData(ProfileActivity.this, "user_photo", "");

//        if (user_photo.length()>0)
//            Glide.with(ProfileActivity.this).load(user_photo).into("");
    }
}
