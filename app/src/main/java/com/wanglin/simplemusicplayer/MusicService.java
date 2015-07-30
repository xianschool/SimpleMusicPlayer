package com.wanglin.simplemusicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {

    MusicServiceReceiver serviceReceiver;

    int status = MainActivity.isStopped;
    MediaPlayer mPlayer;
    int current = 0;
    int count = 0;
    int flog = 0;

    public MusicService() {
    }

    @Override
    public void onCreate(){

        flog=1;
        count = MusicListActivity.list.size();
        serviceReceiver = new MusicServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current++;
                if (current >= count) {
                    current = 0;
                }
                String filename = (MusicListActivity.musicList.get(current)).getPath();
                PlayMusic(filename);
            }
        });

        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand (Intent intent, int flags,  int startId) {

        if(flog==2){
            Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
            sendIntent.putExtra("update", status);
            //sendIntent.putExtra("current", current);
            sendBroadcast(sendIntent);
        }
        flog=2;

        return Service.START_STICKY;
    }

    private void PlayMusic(String path){
        try
        {
	            /* 重置MediaPlayer */
            mPlayer.reset();
	            /* 设置要播放的文件的路径 */
            mPlayer.setDataSource(path);
	            /* 准备播放 */
            mPlayer.prepare();
	            /* 开始播放 */
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer arg0)
                {
                    //播放完成一首之后进行下一首
                    current--;
                    if (current < 0) {
                        current = count;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    status = MainActivity.isPlaying;
                }
            });
        }catch (IOException e){}
    }

    @Override
    public void onDestroy() {
        flog=0;
        mPlayer.stop();
        mPlayer.release();
        super.onDestroy();
    }

    public class MusicServiceReceiver extends BroadcastReceiver {


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
                case MainActivity.NEXT_CLICKED:{
                    current++;
                    if (current < 0) {
                        current = count;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    status = MainActivity.isPlaying;
                    break;
                }
                case MainActivity.PREVIOUS_CLICKED:{
                    current--;
                    if (current < 0) {
                        current = count;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    status = MainActivity.isPlaying;
                    break;
                }
                case MainActivity.STOP_CLICKED:{
                    if (status == MainActivity.isPlaying || status == MainActivity.isPaused){
                        mPlayer.stop();
                        status = MainActivity.isStopped;
                    }
                    break;
                }
                default:break;
            }
            Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
            sendIntent.putExtra("update", status);
            sendBroadcast(sendIntent);

        }


    }



}
