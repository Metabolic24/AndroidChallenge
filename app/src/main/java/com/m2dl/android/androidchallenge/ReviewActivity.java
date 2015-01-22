package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by loic on 22/01/15.
 */
public class ReviewActivity extends ActionBarActivity {
    private LinearLayout layoutPlayer;
    private ArrayList<Player> players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayerReview);
        players = getIntent().getExtras().getParcelableArrayList("PLAYERS");
        Collections.sort(players);
        int i = 0;

        for(Player p : players) {
            TextView lblPlayer = new TextView(this);
            lblPlayer.setText(p.getPseudo() + ": " + p.getScore());
            lblPlayer.setId(i);
            lblPlayer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutPlayer.addView(lblPlayer);
            i++;
        }
        Button btnViewResult = (Button) findViewById(R.id.btnViewResult);
        btnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        final Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnReGame = (Button) findViewById(R.id.btnRegame);
        btnReGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    public void newGame() {
        Intent intent = new Intent(this,SelectionPlayerActivity.class);
        startActivity(intent);
    }

    public void playAgain() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        newGame();
    }
}
