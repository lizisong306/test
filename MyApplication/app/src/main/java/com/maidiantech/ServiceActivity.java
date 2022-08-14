package com.maidiantech;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.umeng.analytics.MobclickAgent;

import adapter.MyServiceAdapter;
import view.StyleUtils;
import view.StyleUtils1;
import view.T;

/**
 * Created by 13520 on 2016/8/25.
 */
public class ServiceActivity extends AutoLayoutActivity {
    ImageView idback;
    GridView servicegi;
    BaseAnimatorSet bas_in = new FlipVerticalSwingEnter();
    BaseAnimatorSet bas_out = new FadeExit();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity_service);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
    }
   private void initView() {
        idback = (ImageView) findViewById(R.id._back);
        servicegi = (GridView) findViewById(R.id.service_gi);
        idback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MyServiceAdapter serviceadapter=new MyServiceAdapter(this);
        servicegi.setAdapter(serviceadapter);
       servicegi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               final NormalDialog dialog = new NormalDialog(ServiceActivity.this);
               dialog.content("是否购买会员?")//
                      /* .showAnim(bas_in)//
                       .dismissAnim(bas_out)//*/
                       .show();
               dialog.setOnBtnLeftClickL(new OnBtnLeftClickL() {
                   @Override
                   public void onBtnLeftClick() {
                       T.showShort(ServiceActivity.this, "是");
                       dialog.dismiss();
                   }
               });
               dialog.setOnBtnRightClickL(new OnBtnRightClickL() {
                   @Override
                   public void onBtnRightClick() {
                       T.showShort(ServiceActivity.this, "不是");
                       dialog.dismiss();
                   }
               });
           }
       });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
