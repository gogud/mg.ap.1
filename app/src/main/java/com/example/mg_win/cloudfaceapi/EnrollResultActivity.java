package com.example.mg_win.cloudfaceapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mg_win.cloudfaceapi.Utils.ImageAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class EnrollResultActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView image;
    public static List<Bitmap> splittedBitmaps;
    ImageAdapter imageAdapter = null;
    GridView gridView = null;

    String className = "EnrollResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_result);

        splittedBitmaps = getIntent().getParcelableArrayListExtra("images");
        gridView = (GridView) findViewById(R.id.enrollGridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setClickable(true);

        imageAdapter = new ImageAdapter(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                changeImage(position);

            }
        });

    }


    public void changeImage(int pos) {

        Bitmap tmpImg = imageAdapter.getItem(pos);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        tmpImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();



        Intent intent = new Intent(this, SearchImage.class);
        intent.putExtra("ImageArray", byteArray);
        intent.putExtra("ImagePos", pos);
        intent.putExtra("ClassName", className);
        startActivity(intent);
    }
}
