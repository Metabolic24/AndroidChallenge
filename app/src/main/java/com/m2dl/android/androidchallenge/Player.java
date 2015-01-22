package com.m2dl.android.androidchallenge;

import android.graphics.Color;

/**
 * Created by loic on 22/01/15.
 */
public class Player {
    private String pseudo;
    private int score;
    private Color color;


    public Player() {
        this("", 0, new Color());
    }

    public Player(String pseudo, int score, Color color) {
        this.pseudo = pseudo;
        this.score = score;
        this.color = color;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
