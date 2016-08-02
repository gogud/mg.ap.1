package com.example.mg_win.cloudfaceapi.Utils;

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
public class FaceIdentify extends AsyncTask<Object, Boolean, String[]> {

    public FaceIdentifyResponse delegate = null;

    public static String[] identify(byte[] imageArray, String[] imageBoundings) {

        String[] identResult = new String[2];

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://api.findface.pro/identify/");
            post.setHeader("Authorization", "Token bd25a609ed43f3ff9661435548144405");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("photo", new ByteArrayBody(imageArray, "image/jpeg", "photo"));
            entity.addPart("confidence", new StringBody("Strict"));

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

                String confidence = jsonObject.getJSONArray("results").getJSONObject(0).getString("confidence");
                String thumbnail = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("face").getString("thumbnail");

                identResult[0] = confidence;
                identResult[1] = thumbnail;

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return identResult;
    }


    @Override
    protected String[] doInBackground(Object... params) {
        if (params[0].toString() == "identify") {

            String[] identifyResponse = identify((byte[]) params[1], (String[]) params[2]);

            if (identifyResponse != null) {
                return identifyResponse;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        //super.execute(result);
        delegate.processIdentifyFinish(result);
    }
}
