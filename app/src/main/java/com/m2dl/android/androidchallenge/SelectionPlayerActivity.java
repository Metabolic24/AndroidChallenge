package com.m2dl.android.androidchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loic on 22/01/15.
 */
public class SelectionPlayerActivity extends ActionBarActivity{
    private static final int MAX_PLAYERS = 6;
    private LinearLayout layoutPlayer;
    private List<Player> players = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectionplayer);
        layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayer);
        Button btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(players.size() < MAX_PLAYERS) {
                    EditText txtNewPlayer = new EditText(SelectionPlayerActivity.this);

                    txtNewPlayer.setId(players.size());
                    txtNewPlayer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    layoutPlayer.addView(txtNewPlayer);

                    players.add(new Player());
                }
            }
        });

        Button btnPlay = (Button) findViewById(R.id.btnGo);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionPlayerActivity.this, SelectionPlayerActivity.class));
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
}
