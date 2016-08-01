package com.example.mg_win.cloudfaceapi.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mg_win.cloudfaceapi.EnrollResultActivity;

/**
 * Created by mg-Win on 1.08.2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return EnrollResultActivity.splittedBitmaps.size();
    }

    @Override
    public Bitmap getItem(int position) {

        return EnrollResultActivity.splittedBitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(1, 1, 1, 1);

        //Set Image Bitmap Here*****
        imageView.setImageBitmap(EnrollResultActivity.splittedBitmaps.get(position));

        return imageView;
    }
}
