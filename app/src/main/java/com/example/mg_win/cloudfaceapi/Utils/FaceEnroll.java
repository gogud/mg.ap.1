package com.example.mg_win.cloudfaceapi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;

/**
 * Created by mg-Win on 28.07.2016.
 */
public class FaceEnroll extends AsyncTask<Object, Boolean, Boolean> {



    public FaceEnrollResponse delegate = null;

    public static Boolean isEnrolled = false;

    public static Boolean enroll(byte[] imageArray) {


        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://api.findface.pro/face/");
            post.setHeader("Authorization", "Token bd25a609ed43f3ff9661435548144405");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("photo", new ByteArrayBody(imageArray, "image/jpeg", "photo.jpg"));

            //entity.addPart("photo", new FileBody(imageArray));

            //entity.addPart("photo", new StringBody("http://oi68.tinypic.com/1h8ck3.jpg"));

            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);

            if (response.getEntity().getContentLength() > 0) {

                String json_string = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(json_string);

                isEnrolled = true;
                return true;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params[0].toString() == "enroll") {
            Log.d("DoInBackground: ", params[0].toString());

            enroll((byte[]) params[1]);
        }
        return isEnrolled;
    }


    @Override
    protected void onPostExecute(Boolean isEnrolled) {
        //super.execute(result);
        delegate.processEnrollFinish(isEnrolled);

    }

    @Override
    protected void onPreExecute() {

    }
}
