package com.maidiantech;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.maidiantech.common.ui.*;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/4/5.
 */

public class About extends com.maidiantech.common.ui.BaseActivity {
    String versionName = "4.4.0";
    TextView versionname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_item);
        setSystemBar();
        setRightIconHide();
        setTitle("关于");
//        setStatusBarDarkMode(true,this);
        versionname = (TextView)findViewById(R.id.versionname);
        try {
            versionName  = this.getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
        }catch (Exception e){

        }
        versionname.setText("V"+versionName);

    }
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于");
        MobclickAgent.onPause(this);
    }
}
