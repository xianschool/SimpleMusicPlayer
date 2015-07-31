package com.wanglin.simplemusicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.os.Handler;

import java.io.IOException;

public class MusicService extends Service {

    MusicServiceReceiver serviceReceiver;

    public static int status = MainActivity.isStopped;
    public static MediaPlayer mPlayer;
    static int current = 0;
    static int count = 0;
    static int flog = 0;
    String firstPath, firstSong, firstSinger;
    public MusicService() {
    }

   static Handler hd = new Handler();
    static Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            MainActivity.seekBar.setProgress(mPlayer.getCurrentPosition());
            MainActivity.playTimer.setText(Utils.MilSecToMin(mPlayer.getCurrentPosition()) + " / " + Utils.MilSecToMin(mPlayer.getDuration()));
            hd.postDelayed(updateThread, 100);
        }
    };

    @Override
    public void onCreate(){

        flog=1;
        serviceReceiver = new MusicServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);
        mPlayer = new MediaPlayer();
        count = current + 1;
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
            sendBroadcast(sendIntent);
        }
        flog=2;

        return Service.START_STICKY;
    }

    public static void PlayMusic(String path){
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

            MainActivity.seekBar.setMax(mPlayer.getDuration());
            hd.post(updateThread);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer arg0)
                {
                    //播放完成一首之后进行下一首
                    current++;
                    if (current >= count) {
                        current = 0;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    status = MainActivity.isPlaying;
                    MainActivity.tv.setText(((MusicInfo) MusicListActivity.musicList.get(current)).getTitle() + " - " + ((MusicInfo) MusicListActivity.musicList.get(current)).getArtist());
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
                        if (MainActivity.path == null){
                            final Cursor c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MusicListActivity.AUDIO_KEYS, null, null, null);
                            c.moveToFirst();
                            firstPath = c.getString(c.getColumnIndexOrThrow("_data"));
                            firstSong = c.getString(c.getColumnIndexOrThrow("title"));
                            firstSinger = c.getString(c.getColumnIndexOrThrow("artist"));
                            PlayMusic(firstPath);
                            status = MainActivity.isPlaying;
                            MainActivity.tv.setText(firstSong + " - " + firstSinger);
                        }
                        else {
                            PlayMusic(MainActivity.path);
                            status = MainActivity.isPlaying;
                        }
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
                    if (current >= count) {
                        current = 0;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    MainActivity.tv.setText(((MusicInfo) MusicListActivity.musicList.get(current)).getTitle() + " - " + ((MusicInfo) MusicListActivity.musicList.get(current)).getArtist());
                    status = MainActivity.isPlaying;
                    break;
                }
                case MainActivity.PREVIOUS_CLICKED:{
                    current--;
                    if (current < 0) {
                        current = count;
                    }
                    PlayMusic(((MusicInfo) MusicListActivity.musicList.get(current)).getPath());
                    MainActivity.tv.setText(((MusicInfo) MusicListActivity.musicList.get(current)).getTitle() + " - " + ((MusicInfo) MusicListActivity.musicList.get(current)).getArtist());
                    status = MainActivity.isPlaying;
                    break;
                }
                case MainActivity.STOP_CLICKED:{
                    if (status == MainActivity.isPlaying || status == MainActivity.isPaused){
                        mPlayer.stop();
                        status = MainActivity.isStopped;
                    }
                    hd.removeCallbacks(updateThread);
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
