package com.maidiantech;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

public class SlidingActivity extends AutoLayoutActivity {
    SlidingLayout rootView;
    public static boolean  isSliding = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = new SlidingLayout(this);
        rootView.bindActivity(this);

        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
//        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
    public void setsetSlidingLayou(MotionEvent event){
        rootView.setSlidingLayout(event);

    }

}
