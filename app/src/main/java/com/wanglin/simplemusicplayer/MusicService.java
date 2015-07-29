package com.wanglin.simplemusicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

    public MusicService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.e("", "");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand (Intent intent, int flags,  int startId) {



        return Service.START_STICKY;
    }



}
