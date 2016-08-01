package com.example.mg_win.cloudfaceapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mg_win.cloudfaceapi.Utils.FaceDetect;
import com.example.mg_win.cloudfaceapi.Utils.FaceDetectResponse;
import com.example.mg_win.cloudfaceapi.Utils.FaceEnroll;
import com.example.mg_win.cloudfaceapi.Utils.FaceEnrollResponse;
import com.example.mg_win.cloudfaceapi.Utils.FaceIdentify;
import com.example.mg_win.cloudfaceapi.Utils.FaceIdentifyResponse;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchImage extends AppCompatActivity implements FaceDetectResponse, FaceIdentifyResponse, FaceEnrollResponse {


    byte[] rawImage = null;
    Bitmap finalImage = null;
    String[] faceBounds = null;

    FaceDetect.FaceBounds[] faceBoundses = null;

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
                showImagesOnImageGrid();
            }
        });

        // image on Click for ident images

    }

    @Override
    public void processDetectFinish(FaceDetect.FaceBounds[] output) {
        faceBoundses = new FaceDetect.FaceBounds[output.length];
        faceBoundses = output;

        setImagetoImageView(faceBoundses[0]);
    }


    public void setImagetoImageView(FaceDetect.FaceBounds faceBoundings) {

        /*
        int x1 = faceBoundings.x1;
        int y1 = faceBoundings.y1;
        int x2 = Integer.parseInt(faceBoundings[2]);
        int y2 = Integer.parseInt(faceBoundings[3]);
        */

        finalImage = convertToBitmap(faceBoundings);

        ImageView image = (ImageView) findViewById(R.id.imageView);

        image.setImageBitmap(finalImage);
    }

    public Bitmap convertToBitmap(FaceDetect.FaceBounds faceBoundings) {

        int height = faceBoundings.y2 - faceBoundings.y1;
        int width = faceBoundings.x2 - faceBoundings.x1;

        Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        Bitmap tempImage = Bitmap.createBitmap(bmp, faceBoundings.x1, faceBoundings.y1, width, height);

        return tempImage;
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


    public void showImagesOnImageGrid() {

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();


        for(int i = 0; i < faceBoundses.length; i++) {

            bitmapArray.add(convertToBitmap(faceBoundses[i]));
        }

        Intent enroll_Result = new Intent(this, EnrollResultActivity.class);
        enroll_Result.putExtra("images", bitmapArray);
        startActivity(enroll_Result);
    }

}
