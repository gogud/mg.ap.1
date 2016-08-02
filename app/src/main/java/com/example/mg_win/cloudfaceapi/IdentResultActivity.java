package com.example.mg_win.cloudfaceapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mg_win.cloudfaceapi.Utils.CustomListAdapter;

public class IdentResultActivity extends AppCompatActivity {
    ListView list;

    Toolbar toolbar = null;

    private ListView listView;
    private String names[] = {
            "1",
            "2",
            "3",
            "4"
    };



    private Integer imageid[] = {
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ident_result);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        // collapsingToolbar.setTitle("Into The Wild");

        Bundle extras = getIntent().getExtras();
        byte[] baseImage = extras.getByteArray("BaseImage");
        String[] identResult = extras.getStringArray("IdentResult");



        Bitmap bmp = BitmapFactory.decodeByteArray(baseImage, 0, baseImage.length);
        ImageView imageBase = (ImageView) findViewById(R.id.imageViewSource);

        imageBase.setImageBitmap(bmp);


        CustomListAdapter customListAdapter = new CustomListAdapter(this, names,  imageid);
        listView = (ListView) findViewById(R.id.listview_score);
        listView.setAdapter(customListAdapter);

     //   new ImageLoader((ImageView) findViewById(R.id.imageViewFound)).execute(identResult[1]);




    }

}

