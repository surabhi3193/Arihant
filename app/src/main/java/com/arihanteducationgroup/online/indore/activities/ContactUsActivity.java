package com.arihanteducationgroup.online.indore.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.Constants.NetworkClass;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        TextView headerTv =findViewById(R.id.header_text);
        ImageView back_btn =findViewById(R.id.back_btn);
         final EditText nameEt = findViewById(R.id.nameEt);
        final EditText emailEt = findViewById(R.id.emailEt);
        final  EditText phoneEt = findViewById(R.id.phoneEt);
        final  EditText msgEt = findViewById(R.id.msgEt);
        final  TextView htmltxt = findViewById(R.id.htmltext);
        Button submit_btn = findViewById(R.id.submit_btn);

        headerTv.setText("Contact Us");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            htmltxt.setText(Html.fromHtml("<div class=\"wpb_text_column wpb_content_element \">\n" +
                    "\t\t<div class=\"wpb_wrapper\">\n" +
                    "\t\t\t<p class=\"adrs_text\">453 – Khandwa Road,</p>\n" +
                    "<p class=\"adrs_text\">Opp. Radha Swami Satsang</p>\n" +
                    "<p class=\"adrs_text\">Indore – 452017</p>\n" +
                    "<p class=\"adrs_text\">Madhya Pradesh</p>\n" +
                    "<p class=\"adrs_text\">Landline No. : 0731-2468502, 0731-2461302</p>\n" +
                    "<p class=\"adrs_text\">Mob. No. – 9826899779 , 9302333650 </p>\n" +
                    "<p class=\"adrs_text\"><a href=\"mailto:arihantonlineexam@gmail.com\">E-mail: arihantonlineexam@gmail.com</a></p>\n" +
                    "\n" +
                    "\t\t</div> \n" +
                    "\t</div>", Html.FROM_HTML_MODE_COMPACT));

        else
            htmltxt.setText(Html.fromHtml( "<div class=\"wpb_text_column wpb_content_element \">\n" +
                    "\t\t<div class=\"wpb_wrapper\">\n" +
                    "\t\t\t" +
                    "<p class=\"adrs_text\">Arihant Education Group.</p>\n" +
                    "<p class=\"adrs_text\">453 – Khandwa Road,</p>\n" +
                    "<p class=\"adrs_text\">Opp. Radha Swami Satsang</p>\n" +
                    "<p class=\"adrs_text\">Indore – 452017</p>\n" +
                    "<p class=\"adrs_text\">Madhya Pradesh</p>\n" +
                    "<p class=\"adrs_text\">Landline No. : 0731-2468502, 0731-2461302</p>\n" +
                    "<p class=\"adrs_text\">Mob. No. – 9826899779 , 9302333650 </p>\n" +
                    "<p class=\"adrs_text\"><a href=\"mailto:arihantonlineexam@gmail.com\">E-mail: arihantonlineexam@gmail.com</a></p>\n" +
                    "\n" +
                    "\t\t</div> \n" +
                    "\t</div>"));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString();
                String email = emailEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String msg = msgEt.getText().toString();

                if (name.length() == 0) {
                    nameEt.setError("Mandatory field");
                    return;
                }

                if (email.length() == 0 || !email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    emailEt.setError("Invalid email");
                    return;
                }
                if (phone.length() != 10) {
                    phoneEt.setError("Mandatory field");
                    return;
                }
                if (msg.length() == 0) {
                    msgEt.setError("Mandatory field");
                    return;
                }

                contactUS(name,email,phone,msg);

            }
        });
    }

    private void contactUS(String name, String email, String phone, String msg) {

        final Dialog dialog = new Dialog(ContactUsActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        String id = getData(ContactUsActivity.this,"student_id","");
        params.put("studentID",id);
        params.put("contact_username", name);
        params.put("contact_useremail", email);
        params.put("contact_number", phone);
        params.put("contact_msg", msg);

        System.out.println("================ contact_users api ==============");
        System.err.println(params);

        client.post(NetworkClass.BASE_URL_NEW + "contact_users", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();
                System.out.println("================ contact_users api ========== sucsess ======");
                System.err.println(response);
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(ContactUsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ContactUsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ContactUsActivity.this, Main2Activity.class));
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
