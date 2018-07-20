package com.arihanteducationgroup.online.indore.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.Constants.NetworkClass.updateLabel;
import static com.arihanteducationgroup.online.indore.other.MySharedPref.saveData;

public class SignupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView login_tv = findViewById(R.id.login_tv);
        final EditText nameEt = findViewById(R.id.nameEt);
        final EditText emailEt = findViewById(R.id.emailEt);
        final EditText countryEt = findViewById(R.id.countryEt);
        final EditText adharEt = findViewById(R.id.adharEt);
        final EditText cityEt = findViewById(R.id.cityEt);
        final EditText passEt = findViewById(R.id.passEt);
        final TextView dobEt = findViewById(R.id.dobEt);
        final EditText useridEt = findViewById(R.id.userid);
        final EditText contactEt = findViewById(R.id.contactEt);
        final CheckBox chTerms = findViewById(R.id.chTerms);
        Button register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString();
                String email = emailEt.getText().toString();
                String country = countryEt.getText().toString();
                String city = cityEt.getText().toString();
                String dob = dobEt.getText().toString();
                String userid = useridEt.getText().toString();
                String contact = contactEt.getText().toString();
                String adhar = adharEt.getText().toString();
                String password = passEt.getText().toString();

                if (name.length() == 0) {
                    nameEt.setError("Mandatory field");
                    showTOast("Enter your First name");
                    return;
                }
                if (adhar.length() != 12) {
                    adharEt.setError("Mandatory field");
                    showTOast("Enter your valid aadhaar card number");
                    return;
                }

                if (email.length() == 0 || !email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    showTOast("Invalid email");
                    emailEt.setError("Mandatory field");
                    return;
                }
                if (country.length() == 0) {
                    countryEt.setError("Mandatory field");
                    showTOast("Enter your country");
                    return;
                }
                if (city.length() == 0) {
                    cityEt.setError("Mandatory field");
                    showTOast("Enter your city");
                    return;
                }

                if (dob.length() == 0) {

                    showTOast("Enter your date of birth");
                    return;
                }

                if (userid.length() == 0) {
                    useridEt.setError("Mandatory field");
                    showTOast("Enter your username");
                    return;
                }

                if (contact.length() != 10) {
                    contactEt.setError("Mandatory field");
                    showTOast("Enter your 10 digit mobile number");
                    return;
                }

                if (password.length() == 0) {
                    passEt.setError("Mandatory field");
                    showTOast("Enter your password");
                    return;
                }

                if (!chTerms.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Please accept terms and conditions", Toast.LENGTH_LONG).show();
                    return;
                }
                signup(adhar,name, email, country, city, dob, userid, contact, password);
            }
        });

        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(dobEt, myCalendar);
            }
        };
        dobEt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View  v) {

                DatePickerDialog d = new DatePickerDialog(SignupActivity.this, dob, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                d.getDatePicker().setMinDate(System.currentTimeMillis());
                d.show();
            }
        });

    }

    private void showTOast(String msg) {
        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    private void signup(String adhaar, String name, String email, String country, String city, String dob, String userid, String contact, final String password) {

        final Dialog dialog = new Dialog(SignupActivity.this, R.style.Theme_AppCompat_Dialog);
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

        System.out.println("- firebase token -------------");
        System.out.println(regId);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("full_name", name);
        params.put("username", userid);
        params.put("email", email);
        params.put("password", password);
        params.put("phone", contact);
        params.put("user_photo", "Mandatory field");
        params.put("device_type", "1");
        params.put("device_id", device_id);
        params.put("device_token", regId);
        params.put("dob", dob);
        params.put("country", country);
        params.put("address", city);
        params.put("aadhar_number", adhaar);
        System.out.println("================ signup api ==============");
        System.err.println(params);

        client.post(NetworkClass.BASE_URL_NEW + "student_signup", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("================ signup api ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saveData(SignupActivity.this, "login", true);
                    saveData(SignupActivity.this, "student_id", response.getString("student_id"));
                    saveData(SignupActivity.this, "full_name", response.getString("full_name"));
                    saveData(SignupActivity.this, "email", response.getString("email"));
                    saveData(SignupActivity.this, "phone", response.getString("phone"));
                    saveData(SignupActivity.this, "address", response.getString("address"));
                    saveData(SignupActivity.this, "country", response.getString("country"));
                    saveData(SignupActivity.this, "username", response.getString("username"));
                    saveData(SignupActivity.this, "user_photo", response.getString("user_photo"));
                    saveData(SignupActivity.this, "gender", response.getString("gender"));
                    saveData(SignupActivity.this, "aadhaar", response.getString("aadhar_number"));
                    saveData(SignupActivity.this,"dob",response.getString("dob"));
                    saveData(SignupActivity.this, "password", password);

                    startActivity(new Intent(SignupActivity.this, Main2Activity.class));
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

}
