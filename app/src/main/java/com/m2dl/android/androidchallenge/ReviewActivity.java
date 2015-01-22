package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        LinearLayout layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayerReview);
        ArrayList<Player> players = getIntent().getExtras().getParcelableArrayList("PLAYERS");
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
                newGame();
            }
        });

        Button btnReGame = (Button) findViewById(R.id.btnRegame);
        btnReGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void newGame() {
        setResult(RESULT_FIRST_USER);
        finish();
    }

    @Override
    public void onBackPressed() {
        newGame();
    }
}
