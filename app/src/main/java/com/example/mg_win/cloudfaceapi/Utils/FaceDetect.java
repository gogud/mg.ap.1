package com.example.mg_win.cloudfaceapi.Utils;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.camera2.params.Face;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mg-Win on 27.07.2016.
 */
public class FaceDetect extends AsyncTask<Object, Boolean, FaceDetect.FaceBounds[]> {

    public FaceDetectResponse delegate = null;



    public static class FaceBounds {
        public int x1;
        public int y1;
        public int x2;
        public int y2;
    }


    public static FaceBounds[] detect(byte[] imageArray) {

        FaceBounds[] faceBoundses = null;
        int faceCount = 0;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://api.findface.pro/detect/");
            post.setHeader("Authorization", "Token bd25a609ed43f3ff9661435548144405");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("photo", new ByteArrayBody(imageArray, "image/jpeg", "photo"));

            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);

            if (response.getEntity().getContentLength() > 0) {

                String json_string = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(json_string);

                faceCount = jsonObject.getJSONArray("faces").length();

                faceBoundses = new FaceBounds[faceCount];

                for (int i = 0; i < faceCount; i++) {
                    faceBoundses[i] = new FaceBounds();

                    JSONArray faceArray = jsonObject.getJSONArray("faces");

                    faceBoundses[i].x1 = Integer.parseInt(faceArray.getJSONObject(i).getString("x1"));
                    faceBoundses[i].y1 = Integer.parseInt(faceArray.getJSONObject(i).getString("y1"));
                    faceBoundses[i].x2 = Integer.parseInt(faceArray.getJSONObject(i).getString("x2"));
                    faceBoundses[i].y2 = Integer.parseInt(faceArray.getJSONObject(i).getString("y2"));

                }

            }

            Log.d("Http Post Request: ", response.toString());

            return faceBoundses;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return faceBoundses;
    }

    @Override
    protected FaceBounds[] doInBackground(Object[] params) {

        if (params[0].toString() == "detect") {
            Log.d("DoInBackground: ", params[0].toString());

            FaceBounds[] detectResponse = detect((byte[]) params[1]);

            if (detectResponse != null) {
                return detectResponse;
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(FaceBounds[] result) {
        //super.execute(result);

        delegate.processDetectFinish(result);
    }

    @Override
    protected void onPreExecute() {

    }
}
