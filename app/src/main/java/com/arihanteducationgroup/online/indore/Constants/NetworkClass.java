package com.arihanteducationgroup.online.indore.Constants;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import android.widget.TextView;
import android.widget.Toast;

import com.arihanteducationgroup.online.indore.activities.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NetworkClass extends BaseActivity
{

        public static final String BASE_URL_NEW = "http://mock.arihanteducationgroup.com/Api/";
        public static final String BASE_IMAGE_URL = "http://mock.arihanteducationgroup.com/uploads/images/defualt.png";

        public static void showTOast(String msg, Context context) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }
        public static String getRealPathFromURI(Uri uri, Context context) {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
        }

        public static void updateLabel(TextView textEdit, Calendar myCalendar) {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textEdit.setText(sdf.format(myCalendar.getTime()));
        }


}