package com.m2dl.android.androidchallenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loic on 22/01/15.
 */
public class SelectionPlayerActivity extends ActionBarActivity{
    private static final int MAX_PLAYERS = 6;
    private LinearLayout layoutPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectionplayer);
        layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayer);
        final Spinner spinnerDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Easy");
        spinnerArray.add("Medium");
        spinnerArray.add("Hard");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        Button btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(players.size() < MAX_PLAYERS) {
                    EditText txtNewPlayer = new EditText(SelectionPlayerActivity.this);
                    txtNewPlayer.setText("Player" + (players.size() + 1));
                    txtNewPlayer.setTextColor(Color.BLACK);
                    txtNewPlayer.setId(players.size());
                    txtNewPlayer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    layoutPlayer.addView(txtNewPlayer);

                    players.add(new Player());
                }
                else {
                    Toast.makeText(SelectionPlayerActivity.this, MAX_PLAYERS + " players are authorized", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAddPlayer.performClick();

        Button btnPlay = (Button) findViewById(R.id.btnGo);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean goToActivity = true;
                for(int i = 0; i < layoutPlayer.getChildCount(); i++) {
                    EditText txtTemp = (EditText) layoutPlayer.getChildAt(i);
                    String pseudoTemp = txtTemp.getText().toString();
                    if(pseudoTemp == null || pseudoTemp.equals(""))
                    {
                        goToActivity = false;
                    }
                    players.get(i).setPseudo(pseudoTemp);
                }
                if(goToActivity)
                {
                    Intent startGameIntent = new Intent(SelectionPlayerActivity.this, LaunchPlayerGameActivity.class);
                    startGameIntent.putParcelableArrayListExtra("PLAYERS", players);
                    startGameIntent.putExtra("DIFFICULTY", spinnerDifficulty.getSelectedItemPosition());
                    startActivity(startGameIntent);
                }
                else {
                    Toast.makeText(SelectionPlayerActivity.this, "Empty pseudo are not allowed", Toast.LENGTH_SHORT).show();
                }
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
