package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private final static int CAPTURE_IMAGE = 666;
    private final static double maxValue = Math.sqrt(255 * 255 * 3);
    private int currentColor;
    private double ratio;
    private ArrayList<Coords> okPixels;

    public class Coords {
        int x,y;

        public Coords(int x,int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test
        currentColor = Color.RED;
        ratio = 0.25; //On autorise 25% de marge

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = null;
                    ContentResolver cr = getContentResolver();

                    try {
                        //Récupération de l'image
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }

                    if (bitmap!=null) {
                        treatBitmap(bitmap);
                    }
                }
        }
    }

    public void treatBitmap(Bitmap bitmap) {
        ImageView iv = (ImageView)findViewById(R.id.imageView);

        //Initialisation des variables dépendant de l'image

        okPixels = new ArrayList<>();

        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/10,bitmap.getHeight()/10,false);

        //Pour chaque pixel
        for(int i=0;i<bitmap.getHeight();i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                //On récupère sa couleur
                int color = bitmap.getPixel(j, i);

                //On calcule les deltas en RGB
                int deltaR = Color.red(color) - Color.red(currentColor);
                int deltaG = Color.green(color) - Color.green(currentColor);
                int deltaB = Color.blue(color) - Color.blue(currentColor);

                //On récupère l'écart entre les deux couleurs
                //En utilisant la racine carrée de la somme des carrés
                double delta = Math.sqrt(deltaR * deltaR + deltaG * deltaG + deltaB * deltaB);

                //Si le delta obtenu est supérieur au maximum autorisé
                if (delta >= maxValue * ratio) {
                    bitmap.setPixel(j,i,Color.argb(50,Color.red(color),Color.green(color),Color.blue(color)));
                    continue;
                } else {
                    //Sinon on le stocke dans notre map
                    okPixels.add(new Coords(j,i));
                    bitmap.setPixel(j,i,currentColor);

                }

            }
        }

        iv.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

