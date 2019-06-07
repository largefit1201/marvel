package com.marvel.jukebox;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    SeekBar seekBar;
    MediaPlayer mp;

    private Messenger mServiceMessenger = null;
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent i = getIntent();
        String music = i.getStringExtra("music");

        seekBar = findViewById(R.id.seekBar);
        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.test);

        TextView title = findViewById(R.id.title);
        title.setText("title");

        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        Button close = findViewById(R.id.close);
        start.setOnClickListener(l);
        stop.setOnClickListener(l);
        close.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.start:
                    //play();
                    Intent start = new Intent(PlayActivity.this, PlayService.class);
                    start.putExtra(PlayService.MESSAGE_KEY, true);
                    startService(start);
                    break;
                case R.id.stop:
                    Intent stop = new Intent(PlayActivity.this, PlayService.class);
                    stop.putExtra(PlayService.MESSAGE_KEY, false);
                    startService(stop);
                    break;
                case R.id.close:
                    onBackPressed();
                    break;
            }
        }
    };

    public void play() {
        seekBar.setMax(mp.getDuration());
        Log.e("marvel", "setMax : " + mp.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("marvel", "progress : " + progress);
                if(fromUser) mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mp.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp.isPlaying()) {
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("marvel", "getCurrentPosition : " + mp.getCurrentPosition());
                    seekBar.setProgress(mp.getCurrentPosition());
                }
            }
        }).start();
    }







    /** 서비스 시작 및 Messenger 전달 */
    private void setStartService() {
        startService(new Intent(PlayActivity.this, PlayService.class));
        bindService(new Intent(this, PlayService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /** 서비스 정지 */
    private void setStopService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(PlayActivity.this, PlayService.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("test","onServiceConnected");
            mServiceMessenger = new Messenger(iBinder);
            try {
                Message msg = Message.obtain(null, PlayService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e) { }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { }
    };

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("test","act : what "+msg.what);
            switch (msg.what) {
                case PlayService.MSG_SEND_TO_ACTIVITY:
                    int value1 = msg.getData().getInt("fromService");
                    String value2 = msg.getData().getString("test");
                    Log.i("test","act : value1 "+value1);
                    Log.i("test","act : value2 "+value2);
                    break;
            }
            return false;
        }
    }));

    /** Service 로 메시지를 보냄 */
    private void sendMessageToService(String str) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, PlayService.MSG_SEND_TO_SERVICE, str);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) { }
            }
        }
    }
}
