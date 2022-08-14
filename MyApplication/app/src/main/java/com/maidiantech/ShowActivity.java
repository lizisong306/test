package com.maidiantech;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;


import com.tencent.android.tpush.service.XGPushServiceV4;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.SharedPreferencesUtil;

import application.MyApplication;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/8/10.
 */
public class ShowActivity extends AutoLayoutActivity {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 5){

            }else{
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 判断是否从推送通知栏打开的
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
            //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            //如果有面板存在则关闭当前的面板
            finish();
        }
        Log.d("lizisong", "ShowActivity");
        setContentView(R.layout.activity_show);
        Context context = getApplicationContext();
        boolean changeState = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.PUSH_STATE, true);
        if(changeState){
            XGPushManager.registerPush(context,"maidian", new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    Log.d("lizisong", "注册成功，设备token为：" + data);
                    MainActivity.feixinCode = data+"";
                    Background_login();
                    Log.d("lizisong", "MainActivity.feixinCode：" + MainActivity.feixinCode);
                }
                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.d("lizisong", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
            Intent service = new Intent(context, XGPushServiceV4.class);
            context.startService(service);
        }
       // XGPushConfig.enableDebug(this, false);

        //设置状态栏半透明的状态
//        StyleUtils1.initSystemBar(this);
//       StyleUtils1.setStyleTitle(this,R.mipmap.start_bg_sb);
       handler.sendEmptyMessageDelayed(0,2500);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }
    private boolean isAppRunning(Context context) {
        String packageName = context.getPackageName();
        String topActivityClassName=getTopActivityName(context);
        if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }
    public  String getTopActivityName(Context context){
        String topActivityClassName=null;
        ActivityManager activityManager =
                (ActivityManager)(context.getSystemService(Context.ACTIVITY_SERVICE )) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            ComponentName f=runningTaskInfos.get(0).topActivity;
            topActivityClassName=f.getClassName();
        }
        return topActivityClassName;
    }
    @Override
    protected void onStart() {
        super.onStart();
        XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(this);
        if(clickedResult != null){
            String title = clickedResult.getTitle();
            String id = clickedResult.getMsgId() + "";
            String content = clickedResult.getContent();
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("闪屏界面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("闪屏界面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void Background_login(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
//                String timestamp = System.currentTimeMillis()+"";
//                String sign="";
//                ArrayList<String> sort = new ArrayList<String>();
//                sort.add("appid"+MainActivity.feixinCode);
//                sort.add("clienttype"+"2");
//                sort.add("timestamp"+timestamp);
//                sort.add("accessid"+ MyApplication.deviceid);
//                sort.add("version"+MyApplication.version);
//                sort.add("mid"+mid);
//                sign = Util.KeySort.keyScort(sort);
//                String url = "http://"+ips+"/api/quiesce_login.php?mid="+mid+"&appid="+MainActivity.feixinCode+"&clienttype=2"+"&timestamp="+timestamp+"&version="+MyApplication.version+"&sign="+sign+"&accessid="+MyApplication.accessid;
//                loginstr = Util.OkHttpUtils.loaudstringfromurl(url);
//                if(loginstr != null){
//                    Message msg = Message.obtain();
//                    msg.what = 4;
//                    handler1.sendMessage(msg);
//                }
//            }
//        }).start();

        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        HashMap<String, String> map = new HashMap<>();
        String url="http://"+MyApplication.ip+"/api/quiesce_login.php";
        map.put("appid",MainActivity.feixinCode);
        map.put("clienttype","2");
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,null,5,0);
    }
}

