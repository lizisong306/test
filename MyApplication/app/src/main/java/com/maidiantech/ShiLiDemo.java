package com.maidiantech;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.util.HashMap;

import application.MyApplication;
import entity.WriteXuQiuData;
import entity.WriteXuQiuDemo;
import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/5/14.
 */

public class ShiLiDemo extends AutoLayoutActivity {
    TextView shili1,shili2,demo1,demo2,shili3,demo3;
    ImageView shezhi_backs;
    String json;
    WriteXuQiuDemo data;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            try {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                tintManager.setStatusBarAlpha(0);
            }catch (Exception e){

            }

        }
        setContentView(R.layout.shilidemoactivity);
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            try {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                tintManager.setStatusBarAlpha(0);
            }catch (Exception e){

            }

        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);
        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);

        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shili1 = (TextView)findViewById(R.id.shili1);
        shili2 = (TextView)findViewById(R.id.shili2);
        shili3 = (TextView)findViewById(R.id.shili3);

        demo1 = (TextView)findViewById(R.id.demo1);
        demo2 = (TextView)findViewById(R.id.demo2);
        demo3 = (TextView)findViewById(R.id.demo3);
        getShiLi();

    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void hideStatusNavigationBar() {
        if (Build.VERSION.SDK_INT < 16) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    private void getShiLi(){
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", "example");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,4,0);

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 4){
                json = (String)msg.obj;
                Gson g=new Gson();
                data  = g.fromJson(json, WriteXuQiuDemo.class);
                if(data != null){
                    if(data.code.equals("1")){
                        if(data.data != null){
                            if(data.data.size() == 1){
                                WriteXuQiuData item1 =data.data.get(0);
                                shili1.setText(item1.name);
                                demo1.setText(item1.content);
                                shili1.setVisibility(View.VISIBLE);
                                demo1.setVisibility(View.VISIBLE);
                            }else if(data.data.size() == 2){
                                WriteXuQiuData item1 =data.data.get(0);
                                shili1.setText(item1.name);
                                demo1.setText(item1.content);
                                WriteXuQiuData item2 =data.data.get(1);
                                shili2.setText(item2.name);
                                demo2.setText(item2.content);
                                shili1.setVisibility(View.VISIBLE);
                                demo1.setVisibility(View.VISIBLE);
                                shili2.setVisibility(View.VISIBLE);
                                demo2.setVisibility(View.VISIBLE);
                            }else if(data.data.size() > 2){
                                WriteXuQiuData item1 =data.data.get(0);
                                shili1.setText(item1.name);
                                demo1.setText(item1.content);
                                WriteXuQiuData item2 =data.data.get(1);
                                shili2.setText(item2.name);
                                demo2.setText(item2.content);
                                WriteXuQiuData item3 =data.data.get(2);
                                shili3.setText(item3.name);
                                demo3.setText(item3.content);
                                shili1.setVisibility(View.VISIBLE);
                                demo1.setVisibility(View.VISIBLE);
                                shili2.setVisibility(View.VISIBLE);
                                demo2.setVisibility(View.VISIBLE);
                                shili3.setVisibility(View.VISIBLE);
                                demo3.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }
        }
    };
}
