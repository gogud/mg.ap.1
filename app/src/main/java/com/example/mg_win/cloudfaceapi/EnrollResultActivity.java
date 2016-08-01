package com.example.mg_win.cloudfaceapi;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mg_win.cloudfaceapi.Utils.ImageAdapter;

import java.util.List;

public class EnrollResultActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView image;
    public static List<Bitmap> splittedBitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_result);

        splittedBitmaps = getIntent().getParcelableArrayListExtra("images");
        GridView gridView = (GridView) findViewById(R.id.enrollGridView);
        gridView.setAdapter(new ImageAdapter(this));


        ImageAdapter imageAdapter = new ImageAdapter(this);


    }
}
