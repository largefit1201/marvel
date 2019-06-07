package com.marvel.jukebox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayService extends Service {
    public static String MESSAGE_KEY = "is_play";
    MediaPlayer player;

    public static final int MSG_REGISTER_CLIENT = 1;
    //public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SEND_TO_SERVICE = 3;
    public static final int MSG_SEND_TO_ACTIVITY = 4;

    private Messenger mClient = null;

    public PlayService() { }

    @Override
    public void onCreate() {
        super.onCreate();

        // sendMsgToActivity(1234);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean msg = intent.getExtras().getBoolean(PlayService.MESSAGE_KEY);
        if(msg) {
            Log.e("marvel", "start");
            player = MediaPlayer.create(this, R.raw.test);
            player.start();
        } else {
            Log.e("marvel", "stop");
            player.stop();
            player.release();
        }

        return START_NOT_STICKY;
    }






    /** activity로부터 binding 된 Messenger */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.w("test","ControlService - message what : "+msg.what +" , msg.obj "+ msg.obj);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClient = msg.replyTo;
                    break;
            }
            return false;
        }
    }));

    private void sendMsgToActivity(int sendValue) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("fromService", sendValue);
            bundle.putString("test","abcdefg");
            Message msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY);
            msg.setData(bundle);
            mClient.send(msg);
        } catch (RemoteException e) {
            Log.e("marvel", "RemoteException");
        }
    }
}
