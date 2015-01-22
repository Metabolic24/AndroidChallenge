package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class LaunchPlayerGameActivity extends Activity {

    private int color;
    private ArrayList<Player> players;
    private int currentPlayer = 0;

    private final static int CAPTURE_IMAGE = 666;
    private final static double maxValue = Math.sqrt(255 * 255 * 3);
    private double ratio;
    private ArrayList<Bitmap> bitmapList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_player_game);

        players = getIntent().getExtras().getParcelableArrayList("PLAYERS");

        switch(getIntent().getExtras().getInt("DIFFICULTY")) {
            case 1:
                ratio = 0.30; //MOYEN
                break;
            case 2:
                ratio = 0.25; //DIFFICILE
                break;
            default:
                ratio = 0.35; //FACILE
        }

        bitmapList = new ArrayList<>();

        launchNewGameInterface();
    }

    private void launchNewGameInterface(){

        //lecture et set nom joueur
        String playerName = players.get(currentPlayer).getPseudo();
        TextView playerNameTextView = (TextView)findViewById(R.id.textPlayerName);
        playerNameTextView.setText(this.getString(R.string.player_turn) + playerName);

        //détermination couleur random
        Random rand = new Random();

        int Red = rand.nextInt(255);
        int Green = rand.nextInt(255);
        int Blue = rand.nextInt(255);

        color = Color.rgb(Red, Green, Blue);

        //ajout couleur sur l'interface
        FrameLayout colorLayout = (FrameLayout)findViewById(R.id.color_layout);
        colorLayout.setBackgroundColor(color);

        TextView colorNameTextView = (TextView)findViewById(R.id.textColorName);
        colorNameTextView.setText(String.valueOf(Red) + " " + String.valueOf(Green) + " " + String.valueOf(Blue));

        //ajout couleur dans la classe Player
        players.get(currentPlayer).setColor(color);

        //lancement chrono 7 sec
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            TextView chronoTextView = (TextView)findViewById(R.id.textChronoLaunch);
            int cpt = 6;

            @Override
            public void run() {
                if(cpt > 0) {
                    chronoTextView.setText(String.valueOf(cpt));
                    cpt--;
                    handler.postDelayed(this, 1000);
                }
                else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAPTURE_IMAGE);
                }
            }
        }, 1000);
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
                        setPlayerScore(treatBitmap(bitmap));
                    }

                    //test si il reste des joueurs devant jouer
                    if(currentPlayer < players.size()-1){
                        currentPlayer++;
                        launchNewGameInterface();

                    }
                    else{
                        Intent reviewIntent = new Intent(this, ReviewActivity.class);
                        reviewIntent.putParcelableArrayListExtra("PLAYERS", players);
                        startActivity(reviewIntent);
                        Log.e("launch", "fini");
                    }
                }

                break;
        }
    }

    public void setPlayerScore(int pixelsNumber) {
        Bitmap bitmap = bitmapList.get(currentPlayer);
        double score = (pixelsNumber * 100)/(bitmap.getHeight()*bitmap.getWidth());
        players.get(currentPlayer).setScore((int) score);
    }

    public int treatBitmap(Bitmap bitmap) {
        int score = 0;

        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/10,bitmap.getHeight()/10,false);

        //Pour chaque pixel
        for(int i=0;i<bitmap.getHeight();i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                //On récupère sa couleur
                int currentColor = bitmap.getPixel(j, i);

                //On calcule les deltas en RGB
                int deltaR = Color.red(currentColor) - Color.red(color);
                int deltaG = Color.green(currentColor) - Color.green(color);
                int deltaB = Color.blue(currentColor) - Color.blue(color);

                //On récupère l'écart entre les deux couleurs
                //En utilisant la racine carrée de la somme des carrés
                double delta = Math.sqrt(deltaR * deltaR + deltaG * deltaG + deltaB * deltaB);

                //Si le delta obtenu est supérieur au maximum autorisé
                if (delta >= maxValue * ratio) {
                    //On réduit l'affichage du pixel
                    bitmap.setPixel(j,i,Color.argb(50,Color.red(color),Color.green(color),Color.blue(color)));
                }
                //Sinon on incrémente le score
                else {
                    score++;
                    //On colorie le pixel validé de la couleur recherchée
                    bitmap.setPixel(j,i,color);
                }
            }
        }

        bitmapList.add(currentPlayer, bitmap);
        return score;
    }
}
