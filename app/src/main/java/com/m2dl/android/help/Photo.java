
/*
package com.m2dl.helloandroid.helloandroid2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

*/
/**
 * Created by root on 08/01/15.
 *//*

public class Photo extends Activity {

    private static final int CAPTURE_IMAGE = 5654;

    private Uri imageUri;
    private ImageView iv;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv =  (ImageView) findViewById(R.id.imageView);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");

        imageUri = Uri.fromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent,CAPTURE_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();

        switch (requestCode) {
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        iv.setImageBitmap(bitmap);

                        Bitmap bitmap2 = bitmap.copy(bitmap.getConfig(), true);

                        //Affichage de l'infobulle
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();

                        for(int i=0;i<bitmap2.getHeight();i++) {
                            for(int j=0;j<bitmap2.getWidth();j++) {
                                int color = bitmap2.getPixel(i,j);
                                int r = Color.red(color);
                                int g = Color.green(color);
                                int b = Color.blue(color);
                                if(r>b && r>g){
                                    bitmap2.setPixel(i,j,Color.RED);
                                }
                                else if(b>r && b>g) {
                                    bitmap2.setPixel(i,j,Color.GREEN);
                                }
                                else {
                                    bitmap2.setPixel(i,j,Color.BLUE);
                                }
                            }
                        }

                        iv.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }

    }

}
*/