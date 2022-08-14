package com.maidiantech;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.SmsRegisData;

/**
 * Created by lizisong on 2017/5/2.
 */

public class LoginActivity extends AutoLayoutActivity implements View.OnClickListener {
    ImageView phoneback;
    EditText yanzhengphone;
    EditText yzm;
    Button yznext;
    TextView anniu;
    TextView title_name;
    String yzphone;
    String registlogin;
    String yzregiststr;
    String tel;
    String pwd;
    String uname;
    String trim;
    String smsCode;
    String fromSMSCode;
    boolean isState = false;
    HashMap<String, String> registmap;
    private int million = 60;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanzhen_phone);
        phoneback = (ImageView) findViewById(R.id.phone_back);
        yanzhengphone = (EditText) findViewById(R.id.yanzheng_phone);
        yzm = (EditText) findViewById(R.id.yz_m);
        yznext = (Button) findViewById(R.id.yz_next);
        anniu=(TextView) findViewById(R.id.anniu);

        anniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //yanzpwd=new HashMap<String, String>();
                yzphone = yanzhengphone.getText().toString().trim();
                trim = yzm.getText().toString().trim();
                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    yzregistjson();
                }

            }
        });

    }

    private void login(){
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //123.206.8.208/api/sms_interface.php
                    registmap = new HashMap<String, String>();
                    registmap.put("tel",yzphone);
                    registmap.put("code",yzm.getText().toString());
                    registmap.put("appid",MainActivity.feixinCode);
                    registmap.put("clienttype","1");
                    registmap.put("timestamp",System.currentTimeMillis()+"");
                    yzregiststr="http://123.207.164.210/api/user_register_login.php";
                    registlogin = OkHttpUtils.postkeyvlauspainr(yzregiststr,registmap);
                    if(registlogin != null){
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);

                    }
                }
            }.start();
        }catch (Exception e){}
    }

    private void yzregistjson() {

        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //123.206.8.208/api/sms_interface.php
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("tel"+tel);
                    sort.add("action"+"send");
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("timestamp"+timestamp);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid"+accessid);
                    sign = KeySort.keyScort(sort);
                    registmap = new HashMap<String, String>();
                    registmap.put("tel",yzphone);
                    registmap.put("action","send");
                    registmap.put("appid",MainActivity.feixinCode);
                    registmap.put("clienttype","2");
                    registmap.put("timestamp",timestamp+"");
                    registmap.put("sign",sign);
                    registmap.put("accessid",accessid);

                    yzregiststr="http://123.207.164.210/api/user_register_login.php";
                    registlogin = OkHttpUtils.postkeyvlauspainr(yzregiststr,registmap);
                    if(registlogin != null){
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);

                    }
                }
            }.start();
        }catch (Exception e){}

    }

    @Override
    public void onClick(View v) {

    }

    private void setSendBt() {
        //设置发送按钮的变化
        anniu.setClickable(false);
        anniu.setText("60秒内输入");
        anniu.setTextSize(14);
        anniu.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                if (million > 0) {
                    anniu.setClickable(false);
                    million--;
                    anniu.setTextSize(15);
                    anniu.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1000);
                }
                if (million == 0) {
                    anniu.setClickable(true);
                    anniu.setText("获取验证码");
                    anniu.setTextColor(Color.parseColor("#ffffff"));
                    million = 60;
                    handler.removeCallbacksAndMessages(null);

                }
            }
            if(msg.what == 8){
                try {
                    Gson gs = new Gson();
                    SmsRegisData json_data =gs.fromJson(registlogin, SmsRegisData.class);
                    if(json_data.code.equals("1")){
                        smsCode = json_data.data.code;
                        setSendBt();
//                        Toast.makeText(YanzhenActivity.this, smsCode,Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(LoginActivity.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JsonIOException e){

                }
            }
        }
    };
}
