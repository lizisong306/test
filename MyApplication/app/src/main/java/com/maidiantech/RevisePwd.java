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
import java.util.Map;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.ProTectByMD5;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.RetPulseData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/22.
 */

public class RevisePwd extends AutoLayoutActivity {
    EditText old_pwd;
    EditText new_pwd;
    EditText confirm;
    Button modify;
    String oldStr;
    String newdStr;
    String modifyStr;
    String ret="";
    String oldpwd;
    String newpwd;
    ImageView xiugai_back;
    String mid;
    Map<String, String> postData = new HashMap<String, String>();
    private OkHttpUtils Okhttp;
    private  String   ips;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revisepwd);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        old_pwd = (EditText)findViewById(R.id.find_pwd);
        new_pwd = (EditText)findViewById(R.id.new_pwd);
        confirm = (EditText)findViewById(R.id.right_pwd);
        modify = (Button)findViewById(R.id.find_next);
        xiugai_back=(ImageView) findViewById(R.id.xiugai_back);
        xiugai_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldStr = old_pwd.getText().toString();
                newdStr = new_pwd.getText().toString();
                modifyStr = confirm.getText().toString();
                if(!TelNumMatch.isMobileNO(oldStr)){
                    Toast.makeText(RevisePwd.this, "原始密码不合法,请输入长度6-20位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TelNumMatch.isMobileNO(newdStr)){
                    Toast.makeText(RevisePwd.this, "新密码不合法，请输入长度6-20位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(modifyStr == null){
                    Toast.makeText(RevisePwd.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(modifyStr.equals("")){
                    Toast.makeText(RevisePwd.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newdStr.equals(modifyStr)){
                    Toast.makeText(RevisePwd.this, "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                modify();

            }
        });
    }

    public void modify(){

        String timestamp = System.currentTimeMillis()+"";
        String sign="";
        ArrayList<String> sort = new ArrayList<String>();
        mid =  SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
       postData.put("mid", mid);
        ProTectByMD5 md5=new ProTectByMD5();
         oldpwd = md5.encode(oldStr);
         newpwd = md5.encode(newdStr);
        postData.put("mid",mid);
       postData.put("oldpwd", oldpwd);
       postData.put("newpwd", newpwd);
        postData.put("version", MyApplication.version);

        sort.add("mid"+mid);
        sort.add("oldpwd"+ oldpwd);
        sort.add("newpwd"+newpwd);
        sort.add("timestamp"+timestamp);
        sort.add("version"+MyApplication.version);
        sign = KeySort.keyScort(sort);
        postData.put("sign", sign);
        postData.put("timestamp", timestamp);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url =  "http://"+ips+"/api/PwdModify.php";
                try {
                    ret = Okhttp.post(url,postData);
                    if(ret != null){
                        Message msg = Message.obtain();
                        msg.what = 8;
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
            if(msg.what == 8){
                Gson gs = new Gson();
                RetPulseData data = gs.fromJson(ret, RetPulseData.class);
                if(data.code.equals("1")){
                    Toast.makeText(RevisePwd.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RevisePwd.this,data.message,Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
