package com.wanglin.simplemusicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    public static Button play, stop, next, previous,mList;
    public static TextView tv;
    ActivityReceiver activityReceiver;
    public static final String CTL_ACTION = "com.wanglin.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "com.wanglin.action.UPDATE_ACTION";
    public static final int isPlaying = 10;
    public static final int isPaused = 11;
    public static final int isStopped = 12;
    public static final int PLAY_CLICKED = 1;
    public static final int NEXT_CLICKED = 2;
    public static final int STOP_CLICKED = 3;
    public static final int PREVIOUS_CLICKED = 4;
    public static final int LIST_CLICKED = 5;
    public static String path;
    public static Cursor c;
    Intent intentService;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickListener();

        activityReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        registerReceiver(activityReceiver, filter);
        intentService = new Intent(this, MusicService.class);
        startService(intentService);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickListener(){

        play = (Button) findViewById(R.id.Btn_Play);
        play.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CTL_ACTION);
                intent.putExtra("control",PLAY_CLICKED);
                sendBroadcast(intent);
            }
        });

        stop = (Button) findViewById(R.id.Btn_Stop);
        stop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CTL_ACTION);
                intent.putExtra("control", STOP_CLICKED);
                sendBroadcast(intent);
            }
        });

        next = (Button) findViewById(R.id.Btn_Next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CTL_ACTION);
                intent.putExtra("control",NEXT_CLICKED);
                sendBroadcast(intent);
            }
        });

        previous = (Button) findViewById(R.id.Btn_Previous);
        previous.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CTL_ACTION);
                intent.putExtra("control",PREVIOUS_CLICKED);
                sendBroadcast(intent);
            }
        });

        mList = (Button)  findViewById(R.id.Btn_List);
        mList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("MusicListActivity://"));
                startActivityForResult(intent, LIST_CLICKED);
            }
        });

        tv = (TextView) findViewById(R.id.SongInfomation);
    }


    public class ActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int update = intent.getIntExtra("update", -1);

            switch (update){
                case isPaused:{
                    play.setText(R.string.play);
                    status = isPlaying;
                    break;
                }

                case isPlaying:{
                    play.setText(R.string.pause);
                    status = isPaused;
                    break;
            }

                case isStopped:{
                    play.setText(R.string.play);
                    status = isPlaying;
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){

    super.onActivityResult(reqCode, resCode, data);
    switch (reqCode){
        case (LIST_CLICKED) : {
            if(resCode == Activity.RESULT_OK){

                Uri musicData = data.getData();
                c = getContentResolver().query(musicData, null, null, null, null);
                c.moveToFirst();
                String song = c.getString(c.getColumnIndexOrThrow("title"));
                String singer = c.getString(c.getColumnIndexOrThrow("artist"));
                path = c.getString(c.getColumnIndexOrThrow("_data"));

                MusicService.PlayMusic(path);
                MusicService.status = isPlaying;

                tv.setText(song + " - " + singer);
            }
        }
    }

    }
}
