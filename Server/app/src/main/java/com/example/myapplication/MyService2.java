package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService2 extends Service {

    private static final String CHANNEL_ID_STRING = "Test2";
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Log.d("lizisong", " test2 is running");
                Message msg = Message.obtain();
                handler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    static NotificationManager notificationManager;
    static NotificationChannel mChannel = null;
    static Notification notification;

//    private Intent updateIntent;
//    private PendingIntent pendingIntent;
//    static long currenttime = System.currentTimeMillis();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lizisong", "onCreate2");
        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "Test2", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true)
                    .setContentTitle("Test2")
                    .setContentText("Test2")
                    .build();
            //notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            startForeground(2, notification);
        }
        Toast.makeText(this, "执行onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lizisong", "onStartCommand2");
        if (!thread.isAlive()) {
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, MyService2.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        Log.d("lizisong", "onDestroy2");
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
   private  Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            startService2();
        }
    };

    public void startService2(){
        try{
            Intent intent = new Intent(MyService2.this,MyService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //适配8.0机制
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }catch (Exception e){

        }
    }
}
