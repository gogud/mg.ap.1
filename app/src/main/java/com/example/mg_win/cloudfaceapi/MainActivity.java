package com.example.mg_win.cloudfaceapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.mg_win.cloudfaceapi.Utils.ImagePicker;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pap_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext());
                                                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                                            }
                                        }
        );


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

  //              Uri imageUri = ImagePicker.getImageUriFromResult(this,resultCode, data);
                // TODO use bitmap

                if(bitmap != null) {

                    // TODO Exceptable Image (!Not Face)

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray_image = stream.toByteArray();

                    Intent intent_SearchImage = new Intent(this, SearchImage.class);
                    intent_SearchImage.putExtra("ImageArray", byteArray_image);
                 //   intent_SearchImage.putExtra("ImageUri", imageUri);
                    startActivity(intent_SearchImage);
                    finish();

                } else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Lütfen Resim Seçinizt";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();
                }


                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
