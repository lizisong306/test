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
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.Codes;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/29.
 */

public class Feedback extends AutoLayoutActivity {
    ImageView feedbacks;
    EditText mobiltxt;
    EditText questiontxt;
    Button btsubmit;
    String mobil,questions,mid,qqlogin;
    HashMap<String,String> hashmap=new HashMap<>();
    private  String   ips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
        feedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                mobil=mobiltxt.getText().toString();
                questions=questiontxt.getText().toString();

//                String timestamp = System.currentTimeMillis()+"";
//                String sign="";
//                ArrayList<String> sort = new ArrayList<String>();

                hashmap.put("mobile",mobil);
                hashmap.put("mid",mid);
                hashmap.put("question",questions);
//                hashmap.put("version", MyApplication.version);

//                sort.add("mobile"+mobil);
//                sort.add("mid"+mid);
//                sort.add("question"+questions);
//                sort.add("timestamp"+timestamp);
//                sort.add("version"+MyApplication.version);
//                String accessid="";
//                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                if(loginState.equals("1")){
//                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                    accessid = mid;
//                }else{
//                    accessid = MyApplication.deviceid;
//                }
//                sort.add("accessid"+accessid);
//                sign = KeySort.keyScort(sort);
//                hashmap.put("sign",sign);
//                hashmap.put("accessid",accessid);
//                hashmap.put("timestamp",timestamp);
                jsons();
            }
        });

    }




    private void initView() {
        feedbacks = (ImageView) findViewById(R.id.feed_backs);
        mobiltxt = (EditText) findViewById(R.id.mobil_txt);
        questiontxt = (EditText) findViewById(R.id.question_txt);
        btsubmit = (Button) findViewById(R.id.bt_submit);
    }
    public void jsons() {
        ips = MyApplication.ip;
        String url="http://"+ips+"/api/userFeedback.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,hashmap,handler,1,0);
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                try {
//                    String str="http://"+ips+"/api/userFeedback.php";
//                    qqlogin = OkHttpUtils.post(str, hashmap);
//                    Message msg=new Message();
//                    msg.what=1;
//                    handler.sendMessage(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }
    Handler handler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Gson g=new Gson();
                qqlogin = (String)msg.obj;
                Codes codes = g.fromJson(qqlogin, Codes.class);
                if(codes.code==1){
                    Toast.makeText(Feedback.this, codes.message, Toast.LENGTH_SHORT).show();
                    finish();
                }else if(codes.code==-1){
                    Toast.makeText(Feedback.this, codes.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("问题反馈");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("问题反馈");

    }
}
