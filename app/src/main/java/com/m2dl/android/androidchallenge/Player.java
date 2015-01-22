package com.m2dl.android.androidchallenge;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by loic on 22/01/15.
 */
public class Player implements Parcelable {
    private String pseudo;
    private int score;
    private int color;

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };


    public Player() {
        this("", 0, 0);
    }

    public Player(String pseudo, int score, int color) {
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pseudo);
        dest.writeInt(score);
        dest.writeInt(color);
    }
}
