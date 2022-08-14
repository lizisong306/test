package com.maidiantech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalTipDialog;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.Registcode;
import entity.Registlog;
import entity.SmsRegisData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/12.
 */
public class YanzhenActivity  extends AutoLayoutActivity {
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
    String type;
    HashMap<String, String> registmap;
    SMSBroadcastReceiver smsBroadCast;
    public static final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    private HashMap<String,String> yanzpwd;
    private int million = 60;
    private  String   ips;
    private Handler handler=new Handler(){
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
                        Toast.makeText(YanzhenActivity.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JsonIOException e){

                }
            }
            if(msg.what==1){
                Gson g=new Gson();
                Registcode registcode = g.fromJson(registlogin, Registcode.class);
                Registlog data =registcode.getData();
                String mid = data.getMid();
                SharedPreferences logid = getSharedPreferences("logid", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = logid.edit();
                edt.putString("mid", mid);
                edt.commit();

                try {
                    if(data.getMid().equals(data.getMid())){
                        Intent intentlogin=new Intent(YanzhenActivity.this,MainActivity.class);
                        startActivity(intentlogin);
                    }
                }catch (Exception e){}
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanzhen_phone);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        pwd = intent.getStringExtra("pwd");
        uname = intent.getStringExtra("uname");
        type =intent.getStringExtra("type");
        ips = MyApplication.ip;
        smsBroadCast = new SMSBroadcastReceiver();

        initView();
    }
    private void initView() {
        phoneback = (ImageView) findViewById(R.id.phone_back);
        yanzhengphone = (EditText) findViewById(R.id.yanzheng_phone);
        yzm = (EditText) findViewById(R.id.yz_m);
        yznext = (Button) findViewById(R.id.yz_next);
        anniu=(TextView) findViewById(R.id.anniu);

        title_name = (TextView)findViewById(R.id.title_name);
        if(type.equals("1")){
            title_name.setText("验证手机号(个人)");
        }else if(type.equals("2")){
            title_name.setText("验证手机号(企业)");
        }
        anniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //yanzpwd=new HashMap<String, String>();
                yzphone = yanzhengphone.getText().toString().trim();
                trim = yzm.getText().toString().trim();
                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(YanzhenActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    yzregistjson();
                }

            }
        });
        yznext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isState){
                    fromSMSCode =  yzm.getText().toString();
                    if(fromSMSCode != null){
                        if(fromSMSCode.equals(smsCode)){
                            isState = true;
                        }
                    }
                }

                if(isState){
                    if(type.equals("1")){
                        Intent intent=new Intent(YanzhenActivity.this,RegisterCountActivity.class);
                        intent.putExtra("phone", yzphone);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                    }else if(type.equals("2")){
                        Intent intent=new Intent(YanzhenActivity.this,Perfectinformation.class);
                        intent.putExtra("phone", yzphone);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(YanzhenActivity.this, "请检查验证码是否正确",Toast.LENGTH_SHORT).show();
                }
                /*registmap=new HashMap<String, String>();
                yzphone = yanzhengphone.getText().toString().trim();
                trim = yzm.getText().toString().trim();
                String checkResult = EditCheckUtils.yanzhen(yzphone, trim);
                if (!checkResult.equals("")) {
                    Toast.makeText(YanzhenActivity.this, checkResult, Toast.LENGTH_SHORT).show();
                }
                else if(!TelNumMatch.isValidPhoneNumber(yzphone)) {
                        NormalTipDialogStyleOne();
                    }
                else {
                        ProTectByMD5 md5 = new ProTectByMD5();
                        String encode = md5.encode(pwd);
                        Log.i("encode", encode);
                        registmap.put("tel", tel);
                        registmap.put("pwd", encode);
                        registmap.put("uname", uname);
                        registjson();
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                    }*/
                }
        });
        phoneback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registjson() {

        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String registstr="http://"+ips+"/api/register.php";
                    registlogin = OkHttpUtils.postkeyvlauspainr(registstr, registmap);


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
                    String sign = "";
                    ArrayList<String> sortKey = new ArrayList<String>();
                    sortKey.add("tel"+yzphone);
                    sortKey.add("flag"+"1");
                    sortKey.add("timestamp"+timestamp);
                    sortKey.add("version"+MyApplication.version);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sortKey.add("accessid" + accessid);
                    sign = KeySort.keyScort(sortKey);
                    MyApplication.setAccessid();
                     yzregiststr="http://"+ips+"/api/sms_interface.php?tel="+yzphone+"&flag="+1+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                     registlogin = OkHttpUtils.loaudstringfromurl(yzregiststr);
                    if(registlogin != null){

                    Message msg = Message.obtain();
                        msg.what = 8;
                        handler.sendMessage(msg);

                    }
                }
            }.start();
        }catch (Exception e){}

    }
    private void NormalTipDialogStyleOne() {
        final NormalTipDialog dialog = new NormalTipDialog(YanzhenActivity.this);
        dialog.content("请输入正确的手机号码")//
                .btnText("确定")//
                //.showAnim(bas_in)//
                //.dismissAnim(bas_out)//
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }
    private void setSendBt() {
        //设置发送按钮的变化
        anniu.setClickable(false);
        anniu.setText("60秒内输入");
        anniu.setTextSize(14);
        anniu.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        million = 0;

    }
    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);

               if(Build.VERSION.SDK_INT < 20){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SMS);
            registerReceiver(smsBroadCast,intentFilter);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
//        million = 0;
//        if(Build.VERSION.SDK_INT >= 23){
//            this.getContentResolver().unregisterContentObserver(content);
//        }else{
          if(Build.VERSION.SDK_INT<20){
            unregisterReceiver(smsBroadCast);
          }
//        }

    }

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


                           if(content.contains("验证码")){
                               Pattern p = Pattern.compile("[^0-9]");
                               Matcher m = p.matcher(content);
                               fromSMSCode =m.replaceAll("");

                               fromSMSCode = fromSMSCode.substring(0,fromSMSCode.length()-1);
                               if(fromSMSCode.equals(smsCode)){
                                                  yzm.setText(fromSMSCode);
                                                  isState = true;
                               }


                                  }
                              }
                       }
                   }
               }
        }


//    /**
//     * 监听短信数据库
//     */
//    SmsContent content = new SmsContent(new Handler());
//    class SmsContent extends ContentObserver {
//
//        private Cursor cursor = null;
//
//        public SmsContent(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//
//            super.onChange(selfChange);
//            try {
//                cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date desc");
//                String body;
//                boolean hasDone =false;
//                if (cursor !=null){
//                    while (cursor.moveToNext()){
//                        body = cursor.getString(cursor.getColumnIndex("body"));
//                        if(body !=null){
//                            hasDone =true;
//                            if(body.contains("验证码")){
//                                Pattern p = Pattern.compile("[^0-9]");
//                                Matcher m = p.matcher(body);
//                                fromSMSCode =m.replaceAll("");
//                                fromSMSCode = fromSMSCode.substring(0,fromSMSCode.length()-1);
//                                if(fromSMSCode.equals(smsCode)){
//                                    yzm.setText(fromSMSCode);
//                                    million =2;
//                                    isState = true;
//
//                                }
//                            }
//                            break;
//                        }
//                        if (hasDone){
//                            break;
//                        }
//                    }
//                }
//            }catch (Exception e){
//
//            }finally {
//                if(cursor!=null){
//                    cursor.close();
//                }
//            }
//        }
//    }


}
