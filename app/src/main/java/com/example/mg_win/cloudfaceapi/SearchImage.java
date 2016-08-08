package com.example.mg_win.cloudfaceapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

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
    byte[] finalImageByteArray = null;
    String[] faceBounds = null;
    String className = null;

    FaceDetect.FaceBounds[] faceBoundses = null;
    FaceIdentify.FaceIdentResultParams[] faceIdentResultParamses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        // Bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pap_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        byte[] imageArray = extras.getByteArray("ImageArray");
        className = extras.getString("ClassName");
        int imgPos = extras.getInt("ImagePos");

        rawImage = imageArray;

        if (className.equalsIgnoreCase("MainActivity")) {


            FaceDetect faceDetect = new FaceDetect();
            faceDetect.delegate = this;

            faceDetect.execute("detect", imageArray);

            // Lock GUI
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            imgFavorite.setClickable(true);

            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagesOnImageGrid();
                }
            });


        } else {
            Bitmap bmpSelected = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            ImageView image = (ImageView) findViewById(R.id.imageView);

            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            image.setImageBitmap(bmpSelected);
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            finalImage = bmpSelected;

            imgFavorite.setClickable(false);
        }



        // image on Click for ident images

    }

    @Override
    public void processDetectFinish(FaceDetect.FaceBounds[] output) {

        if (output.length == 0) {
            Toast.makeText(this,"Yüz Bulunamadı!",Toast.LENGTH_LONG).show();
            SystemClock.sleep(1000);

            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();

            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            return;
        }

        faceBoundses = new FaceDetect.FaceBounds[output.length];
        faceBoundses = output;

        setImagetoImageViewByBounds(faceBoundses[0]);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void setImagetoImageViewByBounds(FaceDetect.FaceBounds faceBoundings) {

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        finalImage = convertToBitmap(faceBoundings);

        ImageView image = (ImageView) findViewById(R.id.imageView);

        image.setImageBitmap(finalImage);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }



    public Bitmap convertToBitmap(FaceDetect.FaceBounds faceBoundings) {

        int height = faceBoundings.y2 - faceBoundings.y1;
        int width = faceBoundings.x2 - faceBoundings.x1;

        Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        Bitmap tempImage = Bitmap.createBitmap(bmp, faceBoundings.x1, faceBoundings.y1, width, height);


        return tempImage;
    }


    public void makeIdentify(View view) {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FaceIdentify faceIdentify = new FaceIdentify();
        faceIdentify.delegate = this;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        finalImageByteArray = stream.toByteArray();


        faceIdentify.execute("identify", finalImageByteArray, faceBounds);
    }

    @Override
    public void processIdentifyFinish(FaceIdentify.FaceIdentResultParams[] output) {

        if (output == null) {
            Toast.makeText(this,"Yüz Bulunamadı!",Toast.LENGTH_LONG).show();
            SystemClock.sleep(1000);

            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();

            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        faceIdentResultParamses = new FaceIdentify.FaceIdentResultParams[output.length];
        faceIdentResultParamses = output;

        ArrayList<String> resultArrayList = new ArrayList<String>();

        for (int i = 0; i < faceIdentResultParamses.length; i++) {
            resultArrayList.add(faceIdentResultParamses[i].confidence);
            resultArrayList.add(faceIdentResultParamses[i].thumbnail);
        }

        Intent intent_identResult = new Intent(this, IdentResultActivity.class);
        intent_identResult.putExtra("IdentResults", resultArrayList);
        intent_identResult.putExtra("BaseImage", finalImageByteArray);
        startActivity(intent_identResult);
        finish();
    }

    public void makeEnrollment(View view) {

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FaceEnroll faceEnroll = new FaceEnroll();
        faceEnroll.delegate = this;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        faceEnroll.execute("enroll", byteArray);
    }




    @Override
    public void processEnrollFinish(Boolean output) {

        if (output == true) {
            Toast.makeText(this,"Fotoğraf Kaydedildi!",Toast.LENGTH_LONG).show();

            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();

            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            return;
        }
    }


    public void showImagesOnImageGrid() {

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();


        for(int i = 0; i < faceBoundses.length; i++) {
            bitmapArray.add(convertToBitmap(faceBoundses[i]));
        }

        Intent enroll_Result = new Intent(this, EnrollResultActivity.class);
        enroll_Result.putExtra("images", bitmapArray);
        startActivity(enroll_Result);
    }

    @Override
    public void onBackPressed() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
