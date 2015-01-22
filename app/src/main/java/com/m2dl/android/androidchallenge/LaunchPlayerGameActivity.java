package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class LaunchPlayerGameActivity extends Activity implements SensorEventListener{

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final static int CAPTURE_IMAGE = 666;
    private final static int REVIEW_ACTIVITY = 69;
    private final static double MAX_DELTA = Math.sqrt(255 * 255 * 3);

    private int color;
    private ArrayList<Player> players;
    private int currentPlayer;

    private final Handler handlerVibration = new Handler();
    private boolean galery = false;

    private double ratio;
    private ArrayList<Bitmap> bitmapList;

    private SensorManager sm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_player_game);
        changeColorOnTilt();
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

    public void newGame() {
        setContentView(R.layout.activity_launch_player_game);
        //Initialisation des variables globales
        currentPlayer = 0;
        bitmapList.clear();
        launchNewGameInterface();
    }

    private void launchNewGameInterface(){

        //lecture et set nom joueur
        String playerName = players.get(currentPlayer).getPseudo();
        TextView playerNameTextView = (TextView)findViewById(R.id.textPlayerName);
        playerNameTextView.setText(this.getString(R.string.player_turn) + " " + playerName);

        //détermination couleur random
        Random rand = new Random();

        int Red = rand.nextInt(255);
        int Green = rand.nextInt(255);
        int Blue = rand.nextInt(255);

        color = Color.rgb(Red, Green, Blue);

        //ajout couleur sur l'interface
        FrameLayout colorLayout = (FrameLayout)findViewById(R.id.color_layout);
        colorLayout.setBackgroundColor(color);

        //ajout couleur dans la classe Player
        players.get(currentPlayer).setColor(color);

        //lancement chrono 7 sec
        TextView chronoTextView = (TextView)findViewById(R.id.textChronoLaunch);
        chronoTextView.setText("6");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            TextView chronoTextView = (TextView)findViewById(R.id.textChronoLaunch);
            int cpt = 5;

            @Override
            public void run() {
                if(cpt > 0) {
                    chronoTextView.setText(String.valueOf(cpt));
                    cpt--;
                    handler.postDelayed(this, 1000);
                }
                else{
                    handlerVibration.postDelayed(new Runnable() {
                        int cptTempsRestant = 10;

                        @Override
                        public void run() {
                            if(cptTempsRestant > 0) {
                                if(cptTempsRestant < 5) {
                                    ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
                                }
                                cptTempsRestant--;
                                handlerVibration.postDelayed(this, 2000);
                            }
                            else{
                                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000);
                                //stop activite photo
                            }
                        }
                    }, 2000);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAPTURE_IMAGE);
                }
            }
        }, 1000);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //Si l'activité est le récapitulatif des scores
            case REVIEW_ACTIVITY:
                if(resultCode==RESULT_OK) {
                    showBitmap(0);
                }
                else if(resultCode==RESULT_CANCELED) {
                    newGame();
                }
                else if(resultCode==RESULT_FIRST_USER) {
                    finish();
                }
                break;
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
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

                    //arret vibration
                    handlerVibration.removeCallbacksAndMessages(null);

                    //test si il reste des joueurs devant jouer
                    if(currentPlayer < players.size()-1){
                        currentPlayer++;
                        deletePhoto();
                        launchNewGameInterface();

                    }
                    else{
                        deletePhoto();
                        Intent reviewIntent = new Intent(this, ReviewActivity.class);
                        reviewIntent.putParcelableArrayListExtra("PLAYERS", players);
                        startActivityForResult(reviewIntent,REVIEW_ACTIVITY);
                    }
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAPTURE_IMAGE);
                }

                break;
        }
    }

    public void setPlayerScore(int pixelsNumber) {
        Bitmap bitmap = bitmapList.get(currentPlayer);
        int score = (pixelsNumber * 100)/(bitmap.getHeight()*bitmap.getWidth());
        players.get(currentPlayer).setScore(score);
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
                if (delta >= MAX_DELTA * ratio) {
                    //On réduit l'affichage du pixel
                    bitmap.setPixel(j,i,Color.argb(50,Color.red(currentColor),Color.green(currentColor),Color.blue(currentColor)));
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

    public void deletePhoto() {
        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            imageFile.delete();
            if (imageFile.exists()) {
                try {
                    imageFile.getCanonicalFile().delete();
                    if (imageFile.exists()) {
                        getApplicationContext().deleteFile(imageFile.getName());
                    }
                } catch (IOException e) {
                    Log.e("EIO", e.getCause().toString() + ", " + e.getMessage());
                }

            }
        }

    }

    public void showBitmap(final int playerIndex) {
        galery = true;
        setContentView(R.layout.activity_galery);

        final ImageView input = (ImageView)findViewById(R.id.galeryView);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            int player = playerIndex;
            int nbPlayers = players.size();

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        return false;
                    // right to left swipe
                    if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        player = (player + 1);
                        if(player==nbPlayers) {
                            player = 0;
                        }
                        input.setImageBitmap(bitmapList.get(player));
                    }
                    //left to right swipe
                    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        player = (player - 1);
                        if(player<0) {
                            player = nbPlayers - 1;
                        }
                        input.setImageBitmap(bitmapList.get(player));
                    }
                } catch (Exception e) {
                    // nothing
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        input.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        input.setImageBitmap(bitmapList.get(playerIndex));
    }

    @Override
    public void onBackPressed() {
        if(galery) {
            final Intent nextIntent = new Intent(this, ReviewActivity.class);
            nextIntent.putParcelableArrayListExtra("PLAYERS", players);
            startActivityForResult(nextIntent, REVIEW_ACTIVITY);
        }
    }

    public void changeColorOnTilt() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    protected void onResume() {
        super.onResume();
        Sensor mMagneticField = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStop() {
        sm.unregisterListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        super.onStop();
    }

    public void onSensorChanged(SensorEvent event) {
        int sensor = event.sensor.getType();
        float [] values = event.values;

        synchronized (this) {
            if (sensor == Sensor.TYPE_ACCELEROMETER) {
                float accField_x = values[0];
                float accField_y = values[1];
                float accField_z = values[2];
                if(accField_x > 18 || accField_y > 18 || accField_z > 18) {
                    Random rand = new Random();

                    int Red = rand.nextInt(255);
                    int Green = rand.nextInt(255);
                    int Blue = rand.nextInt(255);

                    color = Color.rgb(Red, Green, Blue);

                    //ajout couleur sur l'interface
                    FrameLayout colorLayout = (FrameLayout)findViewById(R.id.color_layout);
                    colorLayout.setBackgroundColor(color);

                    //ajout couleur dans la classe Player
                    players.get(currentPlayer).setColor(color);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
