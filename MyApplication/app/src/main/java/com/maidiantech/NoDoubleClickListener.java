package com.maidiantech;

import android.os.Handler;
import android.os.Message;
import android.view.View;



import java.util.Calendar;

import application.MyApplication;

/**
 * Created by 13520 on 2017/3/22.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
    private  long lastClickTime = 0;
    private static Handler handler;
    public NoDoubleClickListener(){
//        if(handler == null){
//            handler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    try {
//                        View v = (View) msg.obj;
//                        v.setClickable(true);
//                        v.setEnabled(true);
//                        Log.d("lizisong", "执行回调");
//                    }catch (Exception e){
//
//                    }
//
//                }
//            };
//        }
    }
    protected abstract void onNoDoubleClick(View v);
    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//            Log.d("lizisong", "onNoDoubleClick");
            lastClickTime = currentTime;
            MyApplication.clickView = v;
            v.setClickable(false);
            v.setClickable(false);
            onNoDoubleClick(v);
//            Message msg = Message.obtain();
//            msg.obj =v;
//            handler.sendMessageDelayed(msg,3000);
        }
    }


}
