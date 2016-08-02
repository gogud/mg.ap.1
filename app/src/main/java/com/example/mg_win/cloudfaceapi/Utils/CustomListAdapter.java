package com.example.mg_win.cloudfaceapi.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mg_win.cloudfaceapi.R;

/**
 * Created by developer on 8/2/16.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private String[] names;
    private Integer[] imageid;
    private Activity context;


    public CustomListAdapter(Activity context, String[] names, Integer[] imageid) {
        super(context, R.layout.activity_ident_result2, names);
        this.context = context;
        this.names = names;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_ident_result2, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_score);

        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageViewFound);

        textViewName.setText(names[position]);

        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}