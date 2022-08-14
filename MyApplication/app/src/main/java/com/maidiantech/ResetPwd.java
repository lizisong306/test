package com.maidiantech;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.ProTectByMD5;
import Util.TelNumMatch;
import application.MyApplication;
import entity.RetPulseData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/22.
 */

public class ResetPwd extends AutoLayoutActivity {

    EditText newPassWorld;
    EditText modify;
    Button next;
    String passworld;
    String modifyStr;
    String phone;
    String ret;
    String pwd;
    ImageView forget_back;
    private HashMap<String,String> registmap = new HashMap<>();
    private  String   ips;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpwd);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        phone = getIntent().getStringExtra("phone");
        newPassWorld = (EditText)findViewById(R.id.find_pwd);
        modify = (EditText)findViewById(R.id.new_pwd);
        next = (Button)findViewById(R.id.pwd_finish);
         forget_back=(ImageView) findViewById(R.id.forget_back);
        forget_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passworld = newPassWorld.getText().toString();
                modifyStr = modify.getText().toString();
                if(!TelNumMatch.isMobileNO(passworld)){
                    Toast.makeText(ResetPwd.this, "新密码不合法，请输入8-16位数字与字符的组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(modifyStr == null){
                    Toast.makeText(ResetPwd.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(modifyStr.equals("")){
                    Toast.makeText(ResetPwd.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passworld.equals(modifyStr)){
                    Toast.makeText(ResetPwd.this, "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                modify();

            }
        });
//        Button pwd_finish=(Button) findViewById(R.id.pwd_finish);
//        pwd_finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(ResetPwd.this,FinishActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    public void modify(){
        String timestamp = System.currentTimeMillis()+"";
        String sign="";
        ArrayList<String> sort = new ArrayList<String>();

        registmap.put("tel", phone);
        registmap.put("version", MyApplication.version);
        ProTectByMD5 md5=new ProTectByMD5();
        pwd = md5.encode(passworld);
        registmap.put("pwd", pwd);
        sort.add("tel"+phone);
        sort.add("pwd"+pwd);
        sort.add("timestamp"+timestamp);
        sort.add("version"+MyApplication.version);
        sign = KeySort.keyScort(sort);
        registmap.put("sign", sign);
        registmap.put("timestamp", timestamp);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                http://123.207.164.210/api/PwdModify.php
                String url = "http://"+ips+"/api/forgetPwd.php";
                try {
                    ret =   OkHttpUtils.postkeyvlauspainr(url, registmap);
                    if(ret != null){
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){

                }

            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Gson gs = new Gson();
                RetPulseData data = gs.fromJson(ret, RetPulseData.class);
                if(data.code.equals("1")){
//                    Intent intent=new Intent(ResetPwd.this,FinishActivity.class);
//                    startActivity(intent);
                    Toast.makeText(ResetPwd.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    finish();
//
//
                } else {
                    Toast.makeText(ResetPwd.this,data.message,Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
