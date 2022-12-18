package com.example.tema4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
public class ForegroundImageService extends Service {
    public static final String STARTFOREGROUND_ACTION = "myaction.startforeground";
    public static final String STOPFOREGROUND_ACTION = "myaction.stopforeground";
    public static final int FOREGROUND_SERVICE_ID = 1234;

    public ForegroundImageService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
            Intent notificationIntent = new Intent(this, ImageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final String channelID = "my channel id";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelID, "my channel name", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(false);
                channel.setSound(null, null);
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, channelID)
                    .setContentTitle("Image download")
                    .setTicker("Ticker")
                    .setContentText("Please wait a few seconds ...")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(FOREGROUND_SERVICE_ID, notification);

            final String param = intent.getStringExtra(WebSearchActivity.EXTRA_URL);
            new DownloadImageTask(this).execute(param);

        } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
            stopForeground(true);
            stopSelf();

            // start second activity to show result
            Intent anotherIntent = new Intent(getApplicationContext(), ImageActivity.class);
            startActivity(anotherIntent);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}