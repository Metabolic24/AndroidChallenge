package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SelectionPlayerActivity extends Activity {
    private static final int MAX_PLAYERS = 6;
    private static final int LAUNCH_ACTIVITY = 28;
    private LinearLayout layoutPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectionplayer);

        layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayer);
        final Spinner spinnerDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Facile");
        spinnerArray.add("Moyen");
        spinnerArray.add("Difficile");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

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
                    Toast.makeText(SelectionPlayerActivity.this, MAX_PLAYERS + " joueurs maximum", Toast.LENGTH_SHORT).show();
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
                    startActivityForResult(startGameIntent,LAUNCH_ACTIVITY);
                }
                else {
                    Toast.makeText(SelectionPlayerActivity.this, "Les pseudos vides ne sont pas autorisés", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LAUNCH_ACTIVITY:
                resetComponents();
                break;
        }
    }

    public void resetComponents() {
        layoutPlayer.removeViews(0,layoutPlayer.getChildCount());
        players.clear();
        ((Button) findViewById(R.id.btnAddPlayer)).performClick();
        ((Spinner) findViewById(R.id.spinnerDifficulty)).setSelection(0);
    }

    @Override
    public void onBackPressed() {
        System.exit(RESULT_CANCELED);
    }
}
