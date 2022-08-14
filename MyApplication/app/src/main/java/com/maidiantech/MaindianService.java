package com.maidiantech;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import application.MyApplication;

/**
 * Created by lizisong on 2017/4/12.
 */

public class MaindianService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String url = intent.getStringExtra("url");
        Intent intent1= new Intent();
        intent1.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent1.setData(content_url);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
}
