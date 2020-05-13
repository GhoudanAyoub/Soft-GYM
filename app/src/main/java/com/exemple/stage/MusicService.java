package com.exemple.stage;
/*
 * Created By GHOUADN AYOUB
 */


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(ListOfSongsActivity.absolutePath);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("show", "Error: " + e.toString());
        }
        return START_STICKY;
    }

    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
