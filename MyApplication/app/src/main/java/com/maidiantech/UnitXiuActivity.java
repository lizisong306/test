package com.maidiantech;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.Usercode;
import entity.userlogin;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/11/14.
 */

public class UnitXiuActivity extends AutoLayoutActivity {
    public ImageView about_backs,ivDeleteText;
    public TextView done;
    public AutoCompleteTextView edit;
    String unit;
    String json,commit;
    HashMap<String,String> hashMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unitxiuactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        about_backs = (ImageView)findViewById(R.id.about_backs);
        ivDeleteText = (ImageView)findViewById(R.id.ivDeleteText);
        unit = getIntent().getStringExtra("old");
        done = (TextView)findViewById(R.id.done);
        edit = (AutoCompleteTextView)findViewById(R.id.edit);
        about_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText("");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit = edit.getText().toString();
                if(commit != null && !commit.equals("")){
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    sort.add("mid"+mid);
                    sort.add("timestamp"+timestamp);
                    sort.add("company"+commit);
                    sort.add("version"+MyApplication.version);
                    sort.add("accessid" + mid);
                    sign = KeySort.keyScort(sort);

                    hashMap = new HashMap<String, String>();
                    hashMap.put("mid",mid);
                    hashMap.put("timestamp",timestamp);
                    hashMap.put("sign",sign);
                    hashMap.put("company",commit);
                    hashMap.put("version",MyApplication.version);
                    hashMap.put("accessid",mid);
                    xiugai();
                }else{
                    Toast.makeText(UnitXiuActivity.this, "单位信息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void xiugai(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://"+ MyApplication.ip+"/api/user_edit_new.php";
                        json = OkHttpUtils.postkeyvlauspainr(url, hashMap);
                        if(json != null){
                            Message msg = Message.obtain();
                            msg.what =1;
                            handler.sendMessage(msg);
                        }
                    }
                }
        ).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                Usercode codes = gson.fromJson(json,  Usercode.class);
                userlogin data = codes.getData();
                if(codes.getCode()==1){
                    Toast.makeText(UnitXiuActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS,data.address);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                    finish();

                }else if(codes.getCode()==-1){
                    Toast.makeText(UnitXiuActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(unit != null){
            edit.setText(unit);
            edit.setSelection(unit.length());
        }

    }
}
