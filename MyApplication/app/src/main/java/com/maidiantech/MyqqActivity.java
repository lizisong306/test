package com.maidiantech;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.tencent.android.tpush.XGPushManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Util.EditCheckUtils;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/13.
 */
public class MyqqActivity extends AutoLayoutActivity {
    LinearLayout check_name;
    AlertDialog dialog;
    TextView name_info;
    EditText name_phone;
    HashMap<String,String> hashmap;
     String ymglogin;
    String name;
    ImageView zhuce_back;
    TextView givema;
    private int million = 60;
    String phones;
    String yzphone;
    String phonelogin;
    EditText find_number;
    String trim;
    private  String   ips;
    String[] sexArry = new String[] { "企业用户", "科研人员","其他" };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                if (million > 0) {
                    million--;
                    givema.setTextSize(15);
                    givema.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1500);
                }
                if (million == 0) {
                    givema.setClickable(true);
                    givema.setText("获取验证码");
                    givema.setTextColor(Color.parseColor("#4FA4Fb"));
                    million = 60;
                }

            }
        }
    };
            @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_qq_info);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        ips = MyApplication.ip;
        check_name =(LinearLayout) findViewById(R.id.check_name);
        name_info=(TextView) findViewById(R.id.name_info);
        Button register_next=(Button) findViewById(R.id.register_next) ;
        ImageView pic_img=(ImageView) findViewById(R.id.pic_img);
        TextView usernames=(TextView) findViewById(R.id.usernames);
         name_phone=(EditText) findViewById(R.id.name_phone);
        zhuce_back=(ImageView) findViewById(R.id.zhuce_back);
         givema=(TextView) findViewById(R.id.givema);
          find_number=(EditText) findViewById(R.id.find_number);
        zhuce_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        usernames.setText(name);
                givema.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phones = name_phone.getText().toString().trim();
                        if(phones.equals("")){
                            Toast.makeText(MyqqActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            setSendBt();
                        }
                        getnamejson();
                    }
                });
        assert register_next != null;
        register_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phones = name_phone.getText().toString().trim();
                trim=find_number.getText().toString().trim();
                String checkResult = EditCheckUtils.yanzhen(phones, trim);
                if (!checkResult.equals("")) {
                    Toast.makeText(MyqqActivity.this, checkResult, Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(MyqqActivity.this,MydataActivityInfo.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.hide, R.anim.show);
                }
            }
        });
        check_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=  new AlertDialog.Builder(MyqqActivity.this)
                        .setTitle("来自")
                        .setSingleChoiceItems(sexArry, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        name_info.setText(sexArry[which]);
                                    }
                                }).show();
            }
        });
    }
    public void getjsons() {
       new Thread(){
           @Override
           public void run() {
               super.run();
               try {

                   String str="http://"+ips+"/api/weixin_qq_login.php";
                   ymglogin = OkHttpUtils.post(str, hashmap);
                   Log.i("ymglogin",ymglogin.toString());
                   Message msg=new Message();
                   msg.what=0;
                   handler.sendMessage(msg);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }.start();
    }
    private void setSendBt() {
        //设置发送按钮的变化
        givema.setClickable(false);
        givema.setText("60秒内输入验证码");
        givema.setTextSize(13);
        givema.setTextColor(Color.parseColor("#4FA4Fb"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    public void getnamejson() {
             new Thread(){
                 @Override
                 public void run() {
                     super.run();
                     String timestamp = System.currentTimeMillis()+"";
                     String sign="";
                     ArrayList<String> sort = new ArrayList<String>();
                     sort.add("tel"+phones);
                     sort.add("timestamp"+timestamp);
                     sort.add("version"+MyApplication.version);
                     String accessid="";
                     String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                     if(loginState.equals("1")){
                         String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                         accessid = mid;
                     }else{
                         accessid = MyApplication.deviceid;
                     }
                     sort.add("accessid" + accessid);
                     sign = KeySort.keyScort(sort);
                     MyApplication.setAccessid();
                     yzphone="http://"+ips+"/api/sms_interface.php?tel="+phones+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                     phonelogin = OkHttpUtils.loaudstringfromurl(yzphone);
                 }
             }.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        million = 0;
        XGPushManager.onActivityStoped(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        million = 0;
    }
}
