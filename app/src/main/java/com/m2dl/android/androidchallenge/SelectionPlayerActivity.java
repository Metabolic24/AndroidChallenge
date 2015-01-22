package com.m2dl.android.androidchallenge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
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
    private static int playerName = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectionplayer);

        layoutPlayer = (LinearLayout) findViewById(R.id.layoutPlayer);
        final Spinner spinnerDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Facile");
        spinnerArray.add("Moyen");
        spinnerArray.add("Difficile");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.custom_spinner_item, spinnerArray);

        spinnerDifficulty.setAdapter(adapter);

        final Button btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(players.size() < MAX_PLAYERS) {
                    final LinearLayout layout = new LinearLayout(SelectionPlayerActivity.this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);

                    EditText txtNewPlayer = new EditText(SelectionPlayerActivity.this);
                    txtNewPlayer.setText("Player" + (playerName++));
                    txtNewPlayer.setTextColor(Color.BLACK);
                    txtNewPlayer.setMaxLines(1);
                    txtNewPlayer.setId(players.size());
                    txtNewPlayer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    layout.addView(txtNewPlayer);

                    final Player player = new Player();
                    players.add(player);

                    if(players.size()==MAX_PLAYERS) {
                        btnAddPlayer.setVisibility(View.INVISIBLE);
                        btnAddPlayer.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    }

                    if(players.size()!=1) {
                        Button button = new Button(SelectionPlayerActivity.this);
                        button.setText("X");
                        button.setTextSize(20.0f);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                players.remove(player);
                                layoutPlayer.removeView(layout);
                                btnAddPlayer.setVisibility(View.VISIBLE);
                                btnAddPlayer.setLayoutParams(new LinearLayout.LayoutParams(58, LinearLayout.LayoutParams.WRAP_CONTENT));
                            }
                        });
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75,75);
                        layoutParams.setMargins(20,5,5,5);

                        button.setLayoutParams(layoutParams);
                        layout.addView(button);
                    }

                    layoutPlayer.addView(layout);
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
