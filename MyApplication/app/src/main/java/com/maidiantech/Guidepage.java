package com.maidiantech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import adapter.Myleadadapter;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/8/10.
 */
public class Guidepage extends AutoLayoutActivity{
    private SharedPreferences sp;
    private Button bt_into;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.guidepage);
        //设置状态栏半透明的状态
        StyleUtils.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      /*  Intent service = new Intent(context, XGPushService.class);
        context.startService(service);*/
        ViewPager view_lead = (ViewPager) findViewById(R.id.view_lead);
        bt_into = (Button) findViewById(R.id.bt_into);
        bt_into.getBackground().setAlpha(0);
        sp = getSharedPreferences("lead", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        if (sp.getBoolean("isshow", false)) {
            Intent intent = new Intent(Guidepage.this, ShowActivity.class);
            startActivity(intent);
            finish();
        } else {
            // 加载适配器
            view_lead.setAdapter(new Myleadadapter(Guidepage.this));
            ed.putBoolean("isshow", true).commit();
        }
        view_lead.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
               // 当position==2是让他跳转到展示的静态页面
                Log.d("lizisong", "postion:"+position);
                if (position == 3) {

                    bt_into.setVisibility(View.VISIBLE);
                    bt_into.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(Guidepage.this, ShowActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
            }
        });
    }
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
    }
}
