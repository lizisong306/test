package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;

import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by lizisong on 2018/4/16.
 */

public class ChengZhangHuoBanActivity extends AutoLayoutActivity {
    ImageView back;
    WebView wbviw;
    TextView bottom;
    @Override
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
        setContentView(R.layout.chengzhanghuobanactivity);
        //        //设置状态栏半透明的状态
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
        back = (ImageView)findViewById(R.id.back);
        wbviw = (WebView)findViewById(R.id.wbviw);
        bottom =(TextView)findViewById(R.id.bottom);
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChengZhangHuoBanActivity.this, ChengZhangHuoBanShenQing.class);
				startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //添加javaScript支持
        wbviw.getSettings().setJavaScriptEnabled(true);
        //取消滚动条
        wbviw.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wbviw.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        wbviw.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        wbviw.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        wbviw.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        wbviw.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        wbviw.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        wbviw.getSettings().setAppCacheEnabled(true);//是否使用缓存
        wbviw.getSettings().setDomStorageEnabled(true);//DOM Storage
        //触摸焦点起作用
        wbviw.requestFocus();
        wbviw.loadUrl("http://www.zhongkechuangxiang.com/webservice/zzhb/Introduction.html");

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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("成长伙伴-主页");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("成长伙伴-主页");
        MobclickAgent.onPause(this);
    }
}
