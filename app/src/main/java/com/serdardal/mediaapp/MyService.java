package com.serdardal.mediaapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyService extends Service {

    AudioManager am;
    AudioFocusRequest afr;
    CountDownTimer timer;
    int minutes;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        minutes = MainActivity.time;
        String NOTIFICATION_CHANNEL_ID = "com.example.mediaapp";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }

        Notification notification = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Media Stopper")
                .setContentText("Media stop process has begun!").build();

        startForeground(1, notification);



        timer = new CountDownTimer(1000*60*minutes,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    afr = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build();
                    am.requestAudioFocus(afr);
                }
                else {
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int focusChange) {

                        }
                    }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                }


                stopSelf();
            }
        };


    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, "Media will stop after "+minutes+" minutes!", Toast.LENGTH_SHORT).show();
        timer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Process stopped!", Toast.LENGTH_SHORT).show();
        timer.cancel();

    }
}

