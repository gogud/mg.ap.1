package com.example.mg_win.cloudfaceapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.mg_win.cloudfaceapi.Utils.FaceDetect;
import com.example.mg_win.cloudfaceapi.Utils.FaceDetectResponse;
import com.example.mg_win.cloudfaceapi.Utils.FaceEnroll;
import com.example.mg_win.cloudfaceapi.Utils.FaceEnrollResponse;
import com.example.mg_win.cloudfaceapi.Utils.FaceIdentify;
import com.example.mg_win.cloudfaceapi.Utils.FaceIdentifyResponse;
import com.example.mg_win.cloudfaceapi.Utils.PopUpScreen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SearchImage extends AppCompatActivity implements FaceDetectResponse, FaceIdentifyResponse, FaceEnrollResponse {

    byte[] rawImage = null;
    Bitmap finalImage = null;
    String[] faceBounds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        Bundle extras = getIntent().getExtras();
        byte[] imageArray = extras.getByteArray("ImageArray");
        rawImage = imageArray;

        FaceDetect faceDetect = new FaceDetect();
        faceDetect.delegate = this;

        faceDetect.execute("detect", imageArray);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageView);
        imgFavorite.setClickable(true);
      imgFavorite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startGalleryActivity();
          }
      });

    }

    @Override
    public void processDetectFinish(String[] output) {
        Log.d("processFaceFinish = ", "x1="+ output[0] + ", y1=" + output[1] + ",x2=" + output[2] + ",y2=" + output[3]);

        faceBounds = output;
        setImagetoImageView(output);
    }





    public void setImagetoImageView(String[] faceBoundings) {

        int x1 = Integer.parseInt(faceBoundings[0]);
        int y1 = Integer.parseInt(faceBoundings[1]);
        int x2 = Integer.parseInt(faceBoundings[2]);
        int y2 = Integer.parseInt(faceBoundings[3]);

        int height = y2 - y1;
        int width = x2 - x1;

        Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        finalImage = Bitmap.createBitmap(bmp, x1, y1, width, height);

        ImageView image = (ImageView) findViewById(R.id.imageView);

        image.setImageBitmap(finalImage);
    }


    public void makeIdentify(View view) {
        FaceIdentify faceIdentify = new FaceIdentify();
        faceIdentify.delegate = this;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        faceIdentify.execute("identify", byteArray, faceBounds);
    }

    @Override
    public void processIdentifyFinish(String[] output) {

        Intent intent_identResult = new Intent(this, IdentResultActivity.class);
        intent_identResult.putExtra("IdentResult", output);
        intent_identResult.putExtra("BaseImage", rawImage);
        startActivity(intent_identResult);
        finish();
    }

    public void makeEnrollment(View view) {
        FaceEnroll faceEnroll = new FaceEnroll();
        faceEnroll.delegate = this;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        faceEnroll.execute("enroll", byteArray);
    }



    @Override
    public void processEnrollFinish(String[] output) {

    }



    public void startGalleryActivity() {

        ArrayList<String> images = new ArrayList<String>();
        images.add("http://sourcey.com/images/stock/salvador-dali-metamorphosis-of-narcissus.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-the-dream.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-persistence-of-memory.jpg");
        images.add("http://sourcey.com/images/stock/simpsons-persistence-of-memory.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-the-great-masturbator.jpg");
        Intent intent = new Intent(this, PopUpScreen.class);
        intent.putStringArrayListExtra(PopUpScreen.EXTRA_NAME, images);
        startActivity(intent);

    }


}
