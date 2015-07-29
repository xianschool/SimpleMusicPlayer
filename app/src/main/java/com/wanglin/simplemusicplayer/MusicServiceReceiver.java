package com.wanglin.simplemusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by wanglin on 15-7-29.
 */
public class MusicServiceReceiver extends BroadcastReceiver {

    int status = MainActivity.isStopped;
    MediaPlayer mPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        int control = intent.getIntExtra("control", -1);
        switch (control){
            case MainActivity.PLAY_CLICKED :{
                if (status == MainActivity.isStopped ){
                    PlayMusic(MainActivity.path);
                    status = MainActivity.isPlaying;
                }
                else if (status == MainActivity.isPlaying){
                    mPlayer.pause();
                    status = MainActivity.isPaused;
                }
                else if (status == MainActivity.isPaused){
                    mPlayer.start();
                    status = MainActivity.isPlaying;
                }
                break;
            }
        }
    }

    public static void PlayMusic(String path){

    }
}
