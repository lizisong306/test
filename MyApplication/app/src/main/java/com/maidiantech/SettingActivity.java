package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

import Util.SharedPreferencesUtil;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/17.
 */

public class SettingActivity extends AutoLayoutActivity implements View.OnClickListener {
    ImageView shezhiback;
    TextView personinfo;
    TextView moresetting;
    TextView gai_pwd;
    String mtype;
    LinearLayout gai_pwd_line;
    LinearLayout more_line;
    public static boolean isExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isExit = false;
        initView();
        shezhiback.setOnClickListener(this);
        personinfo.setOnClickListener(this);
        moresetting.setOnClickListener(this);
        gai_pwd.setOnClickListener(this);
    }

    private void initView() {
        shezhiback = (ImageView) findViewById(R.id.shezhi_back);
        personinfo = (TextView) findViewById(R.id.person_info);
        moresetting = (TextView) findViewById(R.id.more_setting);
        gai_pwd=(TextView) findViewById(R.id.gai_pwd);
        gai_pwd_line=(LinearLayout) findViewById(R.id.gai_pwd_line);
        more_line = (LinearLayout)findViewById(R.id.more_line);
        more_line.setVisibility(View.GONE);
        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);
        if(state){
            gai_pwd_line.setVisibility(View.GONE);
            gai_pwd.setVisibility(View.GONE);
        }else{
            gai_pwd_line.setVisibility(View.GONE);
            gai_pwd.setVisibility(View.GONE);
        }
        mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
        if(mtype.equals("个人")){
            personinfo.setText("个人信息");
        }else if(mtype.equals("企业")){
            personinfo.setText("企业信息");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isExit){
            isExit = true;
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shezhi_back:
                finish();
                break;
            case R.id.person_info:
                mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
                if(mtype.equals("个人")){
                    Intent intent =new Intent(SettingActivity.this,MydataActivityInfo.class);
                    startActivity(intent);
                }else if(mtype.equals("企业")){
                    Intent intent =new Intent(SettingActivity.this,Enterpriseinfo.class);
                    startActivity(intent);
                }

                break;
            case R.id.more_setting:
                Intent intent1 =new Intent(SettingActivity.this,My_shezhi.class);
                startActivity(intent1);
                break;
            case  R.id.gai_pwd:
                Intent intent2 =new Intent(SettingActivity.this,RevisePwd.class);
                startActivity(intent2);
                break;
        }

    }
}
