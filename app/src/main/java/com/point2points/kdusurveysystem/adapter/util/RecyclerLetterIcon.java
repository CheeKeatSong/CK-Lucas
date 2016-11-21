package com.point2points.kdusurveysystem.adapter.util;

import android.graphics.Color;
import android.graphics.Typeface;

import com.amulyakhare.textdrawable.TextDrawable;

import org.w3c.dom.Text;

public class RecyclerLetterIcon {

    public static TextDrawable GenerateRecyclerLetterIcon(String SentenceInput, int fontSize){

        String firstletter = SentenceInput.substring(0,1);

        String color = "#ff0400";

        switch (firstletter){
            case "A":color = "#ff0400";break;
            case "B":color = "#ff8800";break;
            case "C":color = "#ffdd00";break;
            case "D":color = "#bfeb00";break;
            case "E":color = "#59ff00";break;
            case "F":color = "#00ff7b";break;
            case "G":color = "#00eede";break;
            case "H":color = "#0095ff";break;
            case "I":color = "#0015ff";break;
            case "J":color = "#a200ed";break;
            case "K":color = "#ff12ef";break;
            case "L":color = "#e500a1";break;
            case "M":color = "#ff655c";break;
            case "N":color = "#ffcc00";break;
            case "O":color = "#b80c0c";break;
            case "P":color = "#b8590c";break;
            case "Q":color = "#27850a";break;
            case "R":color = "#ae81f2";break;
            case "S":color = "#97a0ff";break;
            case "T":color = "#642f95";break;
            case "U":color = "#ce40c2";break;
            case "V":color = "#f584c8";break;
            case "W":color = "#01517d";break;
            case "X":color = "#d5d5d5";break;
            case "Y":color = "#ffc756";break;
            case "Z":color = "#239fa5";break;
        }

        int android_color = Color.parseColor(color);

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(Typeface.DEFAULT)
                .fontSize(fontSize)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(firstletter,android_color);

        return drawable;
    }
}
