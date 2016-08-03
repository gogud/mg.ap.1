package com.example.mg_win.cloudfaceapi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by mg-Win on 27.07.2016.
 */
public class FaceIdentify extends AsyncTask<Object, Boolean, FaceIdentify.FaceIdentResultParams[]> {



    public FaceIdentifyResponse delegate = null;

    public static class FaceIdentResultParams {
        public String confidence;
        public String thumbnail;
    }

    public static FaceIdentResultParams[] identify(byte[] imageArray, String[] imageBoundings) {

        String[] identResult = new String[2];

        FaceIdentResultParams[] resultParams = null;
        int identResultCount = 0;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://api.findface.pro/identify/");
            post.setHeader("Authorization", "Token bd25a609ed43f3ff9661435548144405");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("photo", new ByteArrayBody(imageArray, "image/jpeg", "photo"));
            entity.addPart("confidence", new StringBody("Strict"));
            entity.addPart("n", new StringBody("10"));

            if (imageBoundings != null) {
                entity.addPart("x1", new StringBody(imageBoundings[0]));
                entity.addPart("y1", new StringBody(imageBoundings[1]));
                entity.addPart("x2", new StringBody(imageBoundings[2]));
                entity.addPart("y2", new StringBody(imageBoundings[3]));
            }
            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);


            if (response.getEntity().getContentLength() > 0) {

                String json_string = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(json_string);

                identResultCount = jsonObject.getJSONArray("results").length();

                resultParams = new FaceIdentResultParams[identResultCount];

                for (int i = 0; i < identResultCount; i++) {

                    resultParams[i] = new FaceIdentResultParams();

                    resultParams[i].confidence = jsonObject.getJSONArray("results").getJSONObject(i).getString("confidence");

                    resultParams[i].thumbnail = jsonObject.getJSONArray("results").getJSONObject(i).getJSONObject("face").getString("thumbnail");


                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return resultParams;
    }


    @Override
    protected FaceIdentResultParams[] doInBackground(Object... params) {
        if (params[0].toString() == "identify") {

            FaceIdentResultParams[] identifyResponse = identify((byte[]) params[1], (String[]) params[2]);

            if (identifyResponse != null) {
                return identifyResponse;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(FaceIdentResultParams[] result) {
        //super.execute(result);
        delegate.processIdentifyFinish(result);

    }

    @Override
    protected void onPreExecute() {

    }
}
