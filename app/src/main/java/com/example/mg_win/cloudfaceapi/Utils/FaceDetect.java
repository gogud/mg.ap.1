package com.example.mg_win.cloudfaceapi.Utils;


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

/**
 * Created by mg-Win on 27.07.2016.
 */
public class FaceDetect extends AsyncTask<Object, Boolean, String[]> {

    public FaceDetectResponse delegate = null;

    public static String[] detect(byte[] imageArray) {

        String[] faceBoundings = new String[4];
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

                int faceCount = jsonObject.getJSONArray("faces").length();

                if (faceCount == 1) {

                    JSONArray faceArray = jsonObject.getJSONArray("faces");
                    faceBoundings[0] = faceArray.getJSONObject(0).getString("x1");
                    faceBoundings[1] = faceArray.getJSONObject(0).getString("y1");
                    faceBoundings[2] = faceArray.getJSONObject(0).getString("x2");
                    faceBoundings[3] = faceArray.getJSONObject(0).getString("y2");

                } else {

                }
            }

            Log.d("Http Post Request: ", response.toString());

            return faceBoundings;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected String[] doInBackground(Object[] params) {

        if (params[0].toString() == "detect") {
            Log.d("DoInBackground: ", params[0].toString());

            String[] detectResponse = detect((byte[]) params[1]);

            if (detectResponse != null) {
                return detectResponse;
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        //super.execute(result);
        delegate.processDetectFinish(result);
    }
}
