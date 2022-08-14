package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
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
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Service {

    private static final String CHANNEL_ID_STRING = "Test";
    private  Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Log.d("lizisong", " test is running ");
                Long time = System.currentTimeMillis()-currenttime;
                Message msg = new Message();
                msg.obj= time;
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
    PowerManager.WakeLock wakeLock;
//    private Intent updateIntent;
//    private PendingIntent pendingIntent;
   static long currenttime = System.currentTimeMillis();
   private static final int TIME_INTERVAL = 10000;
    AlarmManager alarmManager;
    private final AlarmManager.OnAlarmListener mLightAlarmListener
            = new AlarmManager.OnAlarmListener() {
        @Override
        public void onAlarm() {
            try{
                notification.contentView.setTextViewText(R.id.notification_title, "Test");
                notification.contentView.setTextViewText(R.id.notification_content, millisecondsConvertToDHMS(System.currentTimeMillis()-currenttime));
//                notificationManager.notify(0,notification);
                startForeground(1, notification);
                startService2();
                Log.d("lizisong", "onAlarm()");
                alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000, "TAGE",mLightAlarmListener,null);
            }catch (Exception e){

            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_notification);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "Test", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING)
                    .setContent(remoteViews)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true)
                    .build();
            //notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_ONGOING_EVENT;

            startForeground(1, notification);
        }
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+TIME_INTERVAL,new );
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000, "TAGE",mLightAlarmListener,null);
        try{
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
            wakeLock.acquire();
        }catch (Exception e){

        }
        Toast.makeText(this, "执行onCreate", Toast.LENGTH_LONG).show();
        Log.d("lizisong", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lizisong", "onStartCommand");
        if (!thread.isAlive()) {
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, MyService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        Toast.makeText(this, "执行startService", Toast.LENGTH_LONG).show();
        super.onDestroy();
        if(wakeLock != null){
            wakeLock.release();
            wakeLock = null;
        }
        if(alarmManager != null){
            alarmManager.cancel(mLightAlarmListener);
        }
        Toast.makeText(this, "执行OnDestory", Toast.LENGTH_LONG).show();
        Log.d("lizisong", "onDestroy");
    }

    public void startService2(){
        try{
            Intent intent = new Intent(MyService.this,MyService2.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //适配8.0机制
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }catch (Exception e){

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try{
                notification.contentView.setTextViewText(R.id.notification_title, "Test");
                notification.contentView.setTextViewText(R.id.notification_content, millisecondsConvertToDHMS((Long) msg.obj));
//                notificationManager.notify(0,notification);
                startForeground(1, notification);
                startService2();
            }catch (Exception e){

            }
        }
    };


    /**
     * 毫秒数转换成天时分秒
     *
     * @param milliseconds
     */

    private static String millisecondsConvertToDHMS(long milliseconds) {
        String daysStr;
        String hoursStr;
        String minutesStr;
        String secondsStr;
        //天
        long day = (milliseconds / 1000) / (24 * 3600);
        if (day < 10) {
            daysStr = "0" + day;
        } else {
            daysStr = day + "";
        }
        //时
        long hour = ((milliseconds / 1000) % (24 * 3600)) / 3600;
        if (hour < 10) {
            hoursStr = "0" + hour;
        } else {
            hoursStr = hour + "";
        }
        //分
        long minute = ((milliseconds / 1000) % 3600) / 60;
        if (minute < 10) {
            minutesStr = "0" + minute;
        } else {
            minutesStr = minute + "";
        }
         //秒
        long second = (milliseconds / 1000) % 60;

        if (second < 10) {
            secondsStr = "0" + second;
        } else {
            secondsStr = second + "";
        }
        return daysStr+"天:"+hoursStr+"时:"+minutesStr+"分:"+secondsStr+"秒";
    }
}
