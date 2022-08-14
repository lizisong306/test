package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import Util.XWEditText;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.MaiDianYuYue;
import entity.RetPulseData;
import entity.YuYueData;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

import static com.maidiantech.R.id.meet_time;
import static com.maidiantech.R.id.submit_btn;
import static com.maidiantech.R.id.yuejian_back;


/**
 * Created by 13520 on 2016/12/24.
 */

public class Appointment extends AutoLayoutActivity implements View.OnClickListener {
    ImageView yuejianback;
    TextView delitext;
    ImageView share;
    XWEditText meettime;
    XWEditText meetpost;
    XWEditText meetmen;
    XWEditText meettel;
    XWEditText meettitle;

    String meetTiem = "";
    String meetPost = "";
    String meetMen = "";
    String meetTel = "";
    String meetTitle = "";
    String body="";
    String name = "";
    String lingyu = "";
    String Rand = "";
    String ret;
    String mid = "";
    String aid = "";
    String LoginFlag = "";
    String pic = "";
    String look = "";
    Button submitbtn;
    String typeid="";
    OkHttpUtils okHttpUtils;
    HashMap<String, String> map = new HashMap<String, String>();
    MaiDianYuYue maiDianYuYue;
    private  String   ips,sion;
    private TextView rc_uname,rc_linyu,rc_title,rc_look;
    RoundImageView roundImageView;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        maiDianYuYue = MaiDianYuYue.getInstance(this);
        aid = getIntent().getStringExtra("aid");
        pic = getIntent().getStringExtra("pic");
        name = getIntent().getStringExtra("name");
        lingyu = getIntent().getStringExtra("lingyu");
        Rand = getIntent().getStringExtra("rand");
        typeid = getIntent().getStringExtra("typeid");
        body = getIntent().getStringExtra("body");
        look = getIntent().getStringExtra("look");
        okHttpUtils = OkHttpUtils.getInstancesOkHttp();
        options = ImageLoaderUtils.initOptions();
        initView();
        rc_uname.setText(name);
        rc_linyu.setText(lingyu);
        rc_title.setText(body);
        rc_look.setText(look);
        ImageLoader.getInstance().displayImage(pic
                , roundImageView, options);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetTiem = meettime.getText().toString();
                meetPost = meetpost.getText().toString();
                meetMen  = meetmen.getText().toString();
                meetTel =  meettel.getText().toString();
                meetTitle = meettitle.getText().toString();
                mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                LoginFlag = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_FLAY, "0");
                if((meetTiem == null || meetTiem.equals(""))  || (meetPost == null || meetPost.equals(""))
                        || (meetMen == null || meetMen.equals("")) || (meetTel == null || meetTel.equals(""))
                        || (meetTitle == null || meetTitle.equals(""))){
                    Toast.makeText(Appointment.this, "提交数据不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }else  if(!TelNumMatch.ismobil(meetTel)){
                    Toast.makeText(Appointment.this, "请输入正确的电话号", Toast.LENGTH_SHORT).show();
                    return;
                }

                map.put("method", "add");
                map.put("aid", aid);
                map.put("mid",mid);
                map.put("meetdate", meetTiem);
                map.put("meetname", meetMen);
                map.put("mobile",meetTel);
                map.put("meettheme",meetTitle);
                map.put("loginFlag",LoginFlag);
                map.put("meetaddress", meetPost);
                map.put("typeid", typeid);
                ips = MyApplication.ip;
                String url = "http://"+ips+"/api/appointment.php";
                NetworkCom networkCom = NetworkCom.getNetworkCom();
                networkCom.postJson(url,map,handler,1,0);
            }
        });
        yuejianback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        meettime.setOnClickListener(this);
        meetpost.setOnClickListener(this);
        meetmen.setOnClickListener(this);
        meettel.setOnClickListener(this);
        meettitle.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("约见");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("约见");
        MobclickAgent.onPause(this);
    }

    private void initView() {
        yuejianback = (ImageView) findViewById(yuejian_back);
        delitext = (TextView) findViewById(R.id.deli_text);
        share = (ImageView) findViewById(R.id.share);
        meettime = (XWEditText) findViewById(meet_time);
        meetpost = (XWEditText) findViewById(R.id.meet_post);
        meetmen = (XWEditText) findViewById(R.id.meet_men);
        meettel = (XWEditText) findViewById(R.id.meet_tel);
        meettitle = (XWEditText) findViewById(R.id.meet_title);
        submitbtn = (Button) findViewById(submit_btn);
        rc_uname = (TextView)findViewById(R.id.rc_uname);
        rc_linyu = (TextView)findViewById(R.id.rc_linyu);
        rc_title = (TextView)findViewById(R.id.rc_title);
        rc_look  = (TextView)findViewById(R.id.rc_look);
        roundImageView = (RoundImageView)findViewById(R.id.rc_img);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case meet_time:
                break;
            case R.id.meet_post:
                break;
            case R.id.meet_men:
                break;
            case R.id.meet_tel:
                break;
            case R.id.meet_title:
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson g=new Gson();
                ret = (String)msg.obj;
                RetPulseData result =g.fromJson(ret, RetPulseData.class);
                if(result.code.equals("1")){
                    YuYueData yueData = new YuYueData();
                    yueData.meetTel = meetTel;
                    yueData.meetTitle = meetTitle;
                    yueData.meetPost = meetPost;
                    yueData.loginFlag = LoginFlag;
                    yueData.aid = aid;
                    yueData.mid = mid;
                    yueData.meetTime = meetTiem;
                    yueData.meetMen = meetMen;
                    yueData.typeid="4";
                    yueData.pic=pic;
                    yueData.upFlag=1;
                    yueData.lingyu = lingyu;
                    yueData.title = name;
                    yueData.lingyu = lingyu;
                    yueData.rank = Rand;
                    yueData.meetAdress = result.data.id;
                    yueData.update = System.currentTimeMillis()+"";
                    maiDianYuYue.insert(yueData);
                    DetailsActivity.yuyueState = true;
                    Intent intent=new Intent(Appointment.this,Dialogclass.class);
                    startActivityForResult(intent,1);
                }
                else if(result.code.equals("-1")){
                    Toast.makeText(Appointment.this, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 3){
            finish();
        }
    }



    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
