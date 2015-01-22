package com.m2dl.android.androidchallenge;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;


public class LaunchPlayerGameActivity extends ActionBarActivity {


    private Color color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_player_game);

        //lecture et set nom joueur
        String playerName = "temp";
        TextView playerNameTextView = (TextView)findViewById(R.id.textPlayerName);
        playerNameTextView.setText(this.getString(R.string.player_turn) + playerName);


        //détermination couleur random
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        color = new Color();

        //ajout couleur sur l'interface


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
