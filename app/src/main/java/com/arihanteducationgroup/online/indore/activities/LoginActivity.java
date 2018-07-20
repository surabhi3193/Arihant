package com.arihanteducationgroup.online.indore.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.notification.Config;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.saveData;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView create_account = findViewById(R.id.create_account);
        TextView forgot_pass = findViewById(R.id.forgot_pass);
        final EditText usernameEt = findViewById(R.id.usernameET);
        final EditText passwordEt = findViewById(R.id.passEt);
        Button login_btn = findViewById(R.id.login_btn);

        CheckBox mCbShowPwd = findViewById(R.id.cbShowPwd);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = usernameEt.getText().toString();

                String password = passwordEt.getText().toString();


                if (userid.length() == 0) {
                    usernameEt.setFocusable(true);
                    showTOast("Enter your username");
                    return;
                }


                if (password.length() == 0) {
                    showTOast("Enter your password");
                    return;
                }
                login( userid, password);
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotDialog();
            }
        });

    }

    private void forgotDialog() {
            final Dialog dialog = new Dialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
            dialog.setContentView(R.layout.alertdialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            TextView ok_btn = dialog.findViewById(R.id.ok_btn);
            ImageView cancel_btn = dialog.findViewById(R.id.cancel_btn);
            final EditText email_et = dialog.findViewById(R.id.emailEt);

            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = email_et.getText().toString();
                    if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        email_et.setError("Invalid Email Address");
                        return;
                    }

                    forgotPassword(email);
                    dialog.dismiss();
                }
            });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

    }

    private void forgotPassword(String email) {

        final Dialog dialog = new Dialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);


        System.out.println("================ forgetpassword api ==============");
        System.err.println(params);

        client.post(NetworkClass.BASE_URL_NEW + "forgetpassword", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("================ forgetpassword api ========== sucsess ======");
                System.err.println(response);
                try {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
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

    private void login(String userid, final String password) {

        final Dialog dialog = new Dialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
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


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("password", password);
        params.put("username", userid);
        params.put("device_type", "1");
        params.put("device_id", device_id);
        params.put("device_token", regId);

        System.out.println("================ login api ==============");
        System.err.println(params);

        client.post(NetworkClass.BASE_URL_NEW + "student_login", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("================ login api ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    saveData(LoginActivity.this,"login",true);
                    saveData(LoginActivity.this,"student_id",response.getString("student_id"));
                    saveData(LoginActivity.this,"full_name",response.getString("full_name"));
                    saveData(LoginActivity.this,"email",response.getString("email"));
                    saveData(LoginActivity.this,"phone",response.getString("phone"));
                    saveData(LoginActivity.this,"gender",response.getString("gender"));
                    saveData(LoginActivity.this,"address",response.getString("address"));
                    saveData(LoginActivity.this,"country",response.getString("country"));
                    saveData(LoginActivity.this,"username",response.getString("username"));
                    saveData(LoginActivity.this,"user_photo",response.getString("user_photo"));
                    saveData(LoginActivity.this,"aadhaar",response.getString("aadhar_number"));
                    saveData(LoginActivity.this,"dob",response.getString("dob"));
                    saveData(LoginActivity.this,"password",password);


                    startActivity(new Intent(LoginActivity.this, Main2Activity.class));
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

    private void showTOast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
}
