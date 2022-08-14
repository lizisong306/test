package com.maidiantech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.SmsRegisData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/8.
 */
public class ForgetActivity extends AutoLayoutActivity {
    EditText findpwd;
    EditText findnumber;
    Button findnext;
    TextView getCode;
    ImageView forget_back;
    String smsCode ;
    String parseCode = "";
    String yzregiststr = "";
    String yzphone = "";
    String registlogin ="";
    private int million = 60;
    boolean isState = false;
    private  String   ips;
    public static final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    String fromSMSCode;
    SMSBroadcastReceiver smsBroadCast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_forget_pwd);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isState = false;
        smsBroadCast = new SMSBroadcastReceiver();
        findnumber = (EditText) findViewById(R.id.yanzheng_phone);
        findpwd = (EditText)findViewById(R.id.yz_m);
        findnext = (Button) findViewById(R.id.phone_next);
        forget_back = (ImageView)findViewById(R.id.forget_back);
        getCode = (TextView)findViewById(R.id.anniu);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yzphone = findnumber.getText().toString();
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(ForgetActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                setSendBt();
                yzregistjson();
            }
        });
        forget_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isState){
                    fromSMSCode =  findpwd.getText().toString();
                    if(fromSMSCode != null){
                        if(fromSMSCode.equals(smsCode)){
                            isState = true;
                        }
                    }
                }
                if(isState){
                    Intent intent =new Intent(ForgetActivity.this,ResetPwd.class);
                    intent.putExtra("phone", yzphone);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ForgetActivity.this, "请检查验证码是否正确",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SMS);
        registerReceiver(smsBroadCast, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        million = 0;
        unregisterReceiver(smsBroadCast);
    }

    private void yzregistjson() {
        try {
            ips = MyApplication.ip;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //123.206.8.208/api/sms_interface.php
                    MyApplication.setAccessid();
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("tel"+yzphone);
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
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

                    yzregiststr="http://"+ips+"/api/sms_interface.php?tel="+yzphone+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                    registlogin = OkHttpUtils.loaudstringfromurl(yzregiststr);
                    Message msg = Message.obtain();
                    msg.what = 8;
                    handler.sendMessage(msg);
                    Log.i("registlogin",registlogin);
                }
            }.start();
        }catch (Exception e){}

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==2){
                if (million > 0) {
                    million--;
                    getCode.setClickable(false);
                    getCode.setTextSize(15);
                    getCode.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1500);
                }
                if (million == 0) {
                    getCode.setClickable(true);
                    getCode.setText("获取验证码");
                    getCode.setTextColor(Color.parseColor("#ffffff"));
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
//                       Toast.makeText(ForgetActivity.this, smsCode,Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(ForgetActivity.this, json_data.message, Toast.LENGTH_SHORT).show();
                   }
               }catch (Exception e){

               }

           }
        }
    };

    class SMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(ACTION_SMS)){
                    Bundle bundle = intent.getExtras();
                    SmsMessage msg = null;
                    String content = null;
                    if (null != bundle) {
                        Object[] smsObj = (Object[]) bundle.get("pdus");
                        for (Object object : smsObj) {
                            msg = SmsMessage.createFromPdu((byte[]) object);
                            String num = msg.getOriginatingAddress();
                            content =  msg.getMessageBody();

                            String [] str = content.split(",");
                            if(str != null){
                                if(str.length >0){
                                    for(int i=0; i<str.length;i++){
                                        String item = str[i];
                                        if(item.contains("验证码")){
                                            Pattern p = Pattern.compile("[^0-9]");
                                            Matcher m = p.matcher(item);
                                            fromSMSCode =m.replaceAll("");

                                            fromSMSCode = fromSMSCode.substring(0,fromSMSCode.length()-1);
                                            if(fromSMSCode.equals(smsCode)){
                                                findpwd.setText(fromSMSCode);
                                                isState = true;
                                            }
                                            break;
                                        }
                                    }
                                }
                        }
                    }
                }
            }

        }
    }

    private void setSendBt() {
        //设置发送按钮的变化
        getCode.setClickable(false);
        getCode.setText("60秒内输入");
        getCode.setTextSize(13);
        getCode.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }

}
