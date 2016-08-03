package com.example.mg_win.cloudfaceapi.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mg_win.cloudfaceapi.IdentResultActivity;
import com.example.mg_win.cloudfaceapi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by developer on 8/2/16.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> resultContent;
    //private Integer[] imageid;
    private Activity context;


    public CustomListAdapter(Activity context, ArrayList<String> resultContent) {
        super(context, R.layout.activity_ident_result2, resultContent);
        this.context = context;
        this.resultContent = resultContent;
        //this.imageid = imageid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_ident_result2, null, true);

        if (position % 2 == 0) {

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_score);
            textViewName.setText(resultContent.get(position));

            ImageView imageView = (ImageView) listViewItem.findViewById(R.id.imageView_score);
            //imageView.setImageBitmap(IdentResultActivity.splittedBitmaps.get(position + 1));

            Picasso.with(this.context).load(resultContent.get(position + 1)).into(imageView);

        }


        //imageView.setImageResource(imageid[position]);
        return  listViewItem;

        /*
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(1, 1, 1, 1);

        //Set Image Bitmap Here*****
        imageView.setImageBitmap(EnrollResultActivity.splittedBitmaps.get(position));

        return imageView;
        */
    }
}