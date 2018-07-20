package com.arihanteducationgroup.online.indore.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.GifImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkCameraPermission;
import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkReadStoragePermission;
import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkWriteStoragePermission;
import static com.arihanteducationgroup.online.indore.Constants.NetworkClass.BASE_URL_NEW;
import static com.arihanteducationgroup.online.indore.Constants.NetworkClass.getRealPathFromURI;
import static com.arihanteducationgroup.online.indore.Constants.NetworkClass.showTOast;
import static com.arihanteducationgroup.online.indore.Constants.NetworkClass.updateLabel;
import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;
import static com.arihanteducationgroup.online.indore.other.MySharedPref.saveData;

public class EditProfileActivity extends BaseActivity {

    private static final int GALLERY_PICTURE = 3;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 4;

    private Uri profile_uri;
    private boolean isVisible = false;
    private EditText passEt, nameEt, emaiLEt, phoneEt, cityEt, countryEt, adhaarEt;
    private TextView done_btn, cancel_btn, change_picTV, dobEt;
    private String name, email, phone, gender, password, city, country, id, user_photo, aadhar_number, dob;
    private Spinner genderSpinner;
    private ImageView profile_pic;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final ImageView show_btn = findViewById(R.id.show_btn);
        passEt = findViewById(R.id.passEt);
        done_btn = findViewById(R.id.done_btn);
        emaiLEt = findViewById(R.id.emaiLEt);
        dobEt = findViewById(R.id.dobEt);
        phoneEt = findViewById(R.id.phoneEt);
        cityEt = findViewById(R.id.cityEt);
        countryEt = findViewById(R.id.countryEt);
        adhaarEt = findViewById(R.id.adhaarEt);
        nameEt = findViewById(R.id.nameEt);
        profile_pic = findViewById(R.id.profile_pic);
        change_picTV = findViewById(R.id.change_picTV);
        genderSpinner = findViewById(R.id.spinner);
        cancel_btn = findViewById(R.id.cancel_btn);


        List<String> genderlist = new ArrayList<>();
        genderlist.add("Select");
        genderlist.add("Female");
        genderlist.add("Male");
        genderlist.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_item, genderlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("-------- isvisible ---- " + isVisible);
                if (!isVisible) {
                    Glide.with(EditProfileActivity.this).load(R.drawable.show).into(show_btn);
                    isVisible = true;
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Glide.with(EditProfileActivity.this).load(R.drawable.hide).into(show_btn);
                    isVisible = false;

                }

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dobpicker = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(dobEt, myCalendar);
            }
        };
        dobEt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(EditProfileActivity.this, dobpicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                d.getDatePicker().setMinDate(System.currentTimeMillis());
                d.show();
            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        change_picTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                imageDialog();
            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();
                email = emaiLEt.getText().toString();
                dob = dobEt.getText().toString();
                phone = phoneEt.getText().toString();
                gender = genderSpinner.getSelectedItem().toString();
                password = passEt.getText().toString();
                city = cityEt.getText().toString();
                country = countryEt.getText().toString();
                aadhar_number = adhaarEt.getText().toString();

                if (name.length() == 0) {
                    nameEt.setFocusable(true);
                    showTOast("Enter Your Name", EditProfileActivity.this);
                    return;
                }
                if (aadhar_number.length() != 12) {
                    nameEt.setFocusable(true);
                    showTOast("Enter Your Valid Aadhaar Number", EditProfileActivity.this);
                    return;
                }

                if (email.length() == 0) {
                    emaiLEt.setFocusable(true);
                    showTOast("Enter Your Email", EditProfileActivity.this);
                    return;
                }

                if (phone.length() == 0) {
                    phoneEt.setFocusable(true);
                    showTOast("Enter your phone number", EditProfileActivity.this);
                    return;
                }

                if (gender.equalsIgnoreCase("Select")) {
                    showTOast("Select your gender", EditProfileActivity.this);
                    return;
                }

                if (password.length() == 0) {
                    passEt.setFocusable(true);
                    showTOast("Enter your password", EditProfileActivity.this);
                    return;
                }

                if (city.length() == 0) {
                    cityEt.setFocusable(true);
                    showTOast("Enter your city", EditProfileActivity.this);
                    return;
                }

                if (country.length() == 0) {
                    countryEt.setFocusable(true);
                    showTOast("Enter your country", EditProfileActivity.this);
                    return;
                }
                if (dob.length() == 0) {
                    countryEt.setFocusable(true);
                    showTOast("Enter your DOB", EditProfileActivity.this);
                    return;
                }

                updateProfile(name, aadhar_number, email, phone, gender, password, city, country, dob);

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("=========== after clicking image ============");
        System.out.println(requestCode);
        System.out.println(resultCode);
        switch (requestCode) {

            case GALLERY_PICTURE:
                if (resultCode == -1) {
                    onSelectFromGalleryResult(data);
                }
                break;
            case CAPTURE_IMAGES_FROM_CAMERA:
                if (profile_uri != null && profile_uri.getPath().length() > 0) {
                    String pathid = getRealPathFromURI(profile_uri, Objects.requireNonNull(EditProfileActivity.this));
                    File imageFile = new File(pathid);
                    updatePIc(imageFile);
                }

                break;


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(EditProfileActivity.this).getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bm != null) {
            Uri uri = getImageUri(EditProfileActivity.this, bm);
            updatePIc(new File(getRealPathFromURI(uri, EditProfileActivity.this)));
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void imageDialog() {
        checkReadStoragePermission(EditProfileActivity.this);
        dialog = new Dialog(Objects.requireNonNull(EditProfileActivity.this), R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.upload_image);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView gallery_btn = dialog.findViewById(R.id.gallery_btn);
        TextView cam_btn = dialog.findViewById(R.id.camera_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_PICTURE);
                dialog.dismiss();
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean per = checkWriteStoragePermission(EditProfileActivity.this);
                if (per) {
                    {
                        boolean CameraPEr = checkCameraPermission(EditProfileActivity.this);
                        if (CameraPEr) {
                            String fileName = "Camera_Example.jpg";
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, fileName);
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                            profile_uri = EditProfileActivity.this.getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            System.out.println("========== image uri ========= ");
                            if (profile_uri != null)
                                System.out.println(profile_uri.getPath());
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, profile_uri);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, CAPTURE_IMAGES_FROM_CAMERA);

                        }


                    }
                }
                dialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateProfile(final String name, final String aadhar_number, final String email, final String phone, final String gender,
                               final String password, final String city, final String country, final String dob) {
        final Dialog ringProgressDialog = new Dialog(Objects.requireNonNull(EditProfileActivity.this), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        Objects.requireNonNull(ringProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();


        System.out.println("========== updatepic=========");

        params.put("studentID", id);
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("gender", gender);
        params.put("password", password);
        params.put("country", country);
        params.put("address", city);
        params.put("aadhar_number", aadhar_number);
        params.put("dob", dob);

        System.out.println(params);
        client.post(BASE_URL_NEW + "profile_update", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(EditProfileActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        saveData(EditProfileActivity.this, "student_id", response.getString("student_id"));
                        saveData(EditProfileActivity.this, "full_name", response.getString("full_name"));
                        saveData(EditProfileActivity.this, "email", response.getString("email"));
                        saveData(EditProfileActivity.this, "phone", response.getString("phone"));
                        saveData(EditProfileActivity.this, "address", response.getString("address"));
                        saveData(EditProfileActivity.this, "country", response.getString("country"));
                        saveData(EditProfileActivity.this, "username", response.getString("username"));
                        saveData(EditProfileActivity.this, "user_photo", response.getString("user_photo"));
                        saveData(EditProfileActivity.this, "gender", response.getString("gender"));
                        saveData(EditProfileActivity.this, "aadhaar", response.getString("aadhar_number"));
                        saveData(EditProfileActivity.this, "dob", response.getString("dob"));
                        saveData(EditProfileActivity.this, "password", password);
                        onBackPressed();
                        finish();
                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(EditProfileActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//               Toast.makeText(EditProfileActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        id = getData(EditProfileActivity.this, "student_id", "");
        name = getData(EditProfileActivity.this, "full_name", "");
        email = getData(EditProfileActivity.this, "email", "");
        phone = getData(EditProfileActivity.this, "phone", "");
        city = getData(EditProfileActivity.this, "address", "");
        country = getData(EditProfileActivity.this, "country", "");
        gender = getData(EditProfileActivity.this, "gender", "");
        password = getData(EditProfileActivity.this, "password", "");
        user_photo = getData(EditProfileActivity.this, "user_photo", "");
        aadhar_number = getData(EditProfileActivity.this, "aadhaar", "");
        dob = getData(EditProfileActivity.this, "dob", "");

        adhaarEt.setText(aadhar_number);
        nameEt.setText(name);
        emaiLEt.setText(email);
        dobEt.setText(dob);
        phoneEt.setText(phone);
        passEt.setText(password);
        cityEt.setText(city);
        countryEt.setText(country);

        if (gender.equalsIgnoreCase("female"))
            genderSpinner.setSelection(1);

        else if (gender.equalsIgnoreCase("male"))
            genderSpinner.setSelection(2);

        else if (gender.equalsIgnoreCase("other"))
            genderSpinner.setSelection(3);
        else
            genderSpinner.setSelection(0);

        if (user_photo.length() > 0)
            Glide.with(EditProfileActivity.this).load(user_photo).into(profile_pic);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updatePIc(File file) {
        final Dialog ringProgressDialog = new Dialog(Objects.requireNonNull(EditProfileActivity.this), R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        Objects.requireNonNull(ringProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.books);

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        System.out.println("========== updatepic=========");
        System.out.println(file);
        params.put("studentID", id);
        try {
            params.put("user_photo", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ringProgressDialog.dismiss();
        }

        client.post(BASE_URL_NEW + "profile_update", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(EditProfileActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        saveData(EditProfileActivity.this, "user_photo", response.getString("user_photo"));
                        Glide.with(EditProfileActivity.this).load(response.getString("user_photo")).into(profile_pic);
                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(EditProfileActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//               Toast.makeText(EditProfileActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }

}
