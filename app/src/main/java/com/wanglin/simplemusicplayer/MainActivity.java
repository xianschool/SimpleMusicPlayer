package com.wanglin.simplemusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {

    List<Object> musicList = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();
    Button play, stop, next, previous,mList;
    ActivityReceiver activityReceiver;
    public static final String CTL_ACTION = "com.wanglin.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "com.wanglin.action.UPDATE_ACTION";
    public static final int isPlaying = 10;
    public static final int isPaused = 11;
    public static final int isStoped = 12;
    public static final int PLAY_CLICKED = 1;
    public static final int NEXT_CLICKED = 2;
    public static final int STOP_CLICKED = 3;
    public static final int PREVIOUS_CLICKED = 4;
    public static final int LIST_CLICKED = 5;
    Intent intentService;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickListener();
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
                intent.putExtra("control",STOP_CLICKED);
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
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/"));
                startActivityForResult(intent, LIST_CLICKED);
            }
        });
    }


    public class ActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
