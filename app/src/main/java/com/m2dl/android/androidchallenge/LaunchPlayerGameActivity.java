package com.m2dl.android.androidchallenge;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class LaunchPlayerGameActivity extends ActionBarActivity {

    private Color color;
    private ArrayList<Player> players;
    private int currentPlayer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_player_game);

        players = getIntent().getExtras().getParcelableArrayList("PLAYERS");

        //lecture et set nom joueur
        String playerName = players.get(currentPlayer).getPseudo();
        TextView playerNameTextView = (TextView)findViewById(R.id.textPlayerName);
        playerNameTextView.setText(this.getString(R.string.player_turn) + playerName);


        //détermination couleur random
        Random rand = new Random();

        int Red = rand.nextInt(255);
        int Green= rand.nextInt(255);
        int Blue= rand.nextInt(255);

        color = new Color();
        color.argb(0, Red, Green, Blue);

        //ajout couleur sur l'interface
        FrameLayout colorLayout = (FrameLayout)findViewById(R.id.color_layout);
        colorLayout.setBackgroundColor(color.rgb(Red, Green, Blue));

        TextView colorNameTextView = (TextView)findViewById(R.id.textColorName);
        colorNameTextView.setText(String.valueOf(Red) + " " + String.valueOf(Green) + " " + String.valueOf(Blue));

        //ajout couleur dans la classe Player


        //lancement chrono 5 sec

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_player_game, menu);
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
