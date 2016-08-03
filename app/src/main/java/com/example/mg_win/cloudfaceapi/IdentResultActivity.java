package com.example.mg_win.cloudfaceapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mg_win.cloudfaceapi.Utils.CustomListAdapter;
import com.example.mg_win.cloudfaceapi.Utils.FaceIdentify;

import java.util.ArrayList;
import java.util.List;

public class IdentResultActivity extends AppCompatActivity {
    ListView list;

    Toolbar toolbar = null;

    public static List<Bitmap> splittedBitmaps;
    public static ArrayList<String> resultArrayList;

    public static FaceIdentify.FaceIdentResultParams[] faceIdentResultParamses;

    private ListView listView;



/*
    private Integer imageid[] = {
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ident_result);

        // Bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pap_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        // collapsingToolbar.setTitle("Into The Wild");

        Bundle extras = getIntent().getExtras();
        byte[] baseImage = extras.getByteArray("BaseImage");
        resultArrayList = extras.getStringArrayList("IdentResults");

        // Resimleri aldık burda bastıracağız, confidence ve thumbnail.

        Bitmap bmp = BitmapFactory.decodeByteArray(baseImage, 0, baseImage.length);
        ImageView imageBase = (ImageView) findViewById(R.id.imageViewSource);

        imageBase.setImageBitmap(bmp);


        CustomListAdapter customListAdapter = new CustomListAdapter(this, resultArrayList);
        listView = (ListView) findViewById(R.id.listview_score);
        listView.setAdapter(customListAdapter);

     //   new ImageLoader((ImageView) findViewById(R.id.imageViewFound)).execute(identResult[1]);




    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

}

