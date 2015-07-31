package com.wanglin.simplemusicplayer;

/**
 * Created by wanglin on 15-7-31.
 */
public class Utils {

    public static String MilSecToMin (int MilSec){
        int Min, Sec;
        Min = (MilSec / 1000) / 60;
        Sec = (MilSec - (Min * 60 * 1000)) / 1000;

        if (Min < 10 && Sec < 10){
            return "0" + Min + ":" + "0" + Sec;
        }
        else if (Min < 10 && Sec >= 10){
            return "0" + Min + ":" + Sec;
        }
        else if (Min >= 10 && Sec < 10){
            return Min + ":"  + "0" + Sec;
        }
        else {
            return Min + ":" + Sec;
        }
    }

}
