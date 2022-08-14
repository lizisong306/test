package com.maidiantech;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.sql.Time;
import java.util.HashMap;

import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Ret;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/26.
 */

public class YuyueRenCai extends AutoLayoutActivity {

    ImageView back;
    RoundImageView rc_img;
    TextView rc_uname;
    TextView rc_linyu,rc_danwei,rc_yuyuecount,adress,meettime,item1,item2,item3;
    EditText edit;
    TextView ok, cenel;
    String name;
    String linyu;
    String danwei;
    String yuyuecount;
    String imgURL;

    LinearLayout time;
    String ret ="";
    private DisplayImageOptions options;
    long start, end, now, date1, date2, date3;
    TextView confirm;
    String aid, meetAdress, meetTime, price="0", context;
    String original_price, current_price,zhicheng;
    TextView original, current,rc_zhicheng;
    ProgressBar progress;
    LinearLayout onclick1,onclick2,onclick3;
    public static boolean isFinish = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyuerencai);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isFinish = false;
        original = (TextView)findViewById(R.id.original);
        current = (TextView)findViewById(R.id.current);
        back = (ImageView)findViewById(R.id.back);
        rc_img = (RoundImageView)findViewById(R.id.rc_img);
        rc_uname = (TextView)findViewById(R.id.rc_uname);
        rc_linyu = (TextView)findViewById(R.id.rc_linyu);
        rc_danwei = (TextView)findViewById(R.id.rc_danwei);
        rc_yuyuecount = (TextView)findViewById(R.id.rc_yuyuecount);
        adress = (TextView)findViewById(R.id.adress);
        meettime = (TextView)findViewById(R.id.meettime);
        item1 = (TextView)findViewById(R.id.item1);
        item2 = (TextView)findViewById(R.id.item2);
        item3 = (TextView)findViewById(R.id.item3);
        time = (LinearLayout)findViewById(R.id.time);
        ok = (TextView)findViewById(R.id.ok);
        cenel = (TextView)findViewById(R.id.cenel);
        onclick1 = (LinearLayout)findViewById(R.id.onclick1);
        onclick2 = (LinearLayout)findViewById(R.id.onclick2);
        onclick3 = (LinearLayout)findViewById(R.id.onclick3);
        time.setVisibility(View.GONE);
        edit = (EditText)findViewById(R.id.edit);
        confirm = (TextView)findViewById(R.id.confirm);
        progress = (ProgressBar)findViewById(R.id.progress);
        rc_zhicheng = (TextView)findViewById(R.id.rc_zhicheng);
        name = getIntent().getStringExtra("name");
        linyu = getIntent().getStringExtra("linyu");
        danwei = getIntent().getStringExtra("danwei");
        yuyuecount = getIntent().getStringExtra("count");
        imgURL = getIntent().getStringExtra("img");
        aid = getIntent().getStringExtra("aid");
        original_price = getIntent().getStringExtra("original_price");
        current_price  = getIntent().getStringExtra("current_price");
        zhicheng  = getIntent().getStringExtra("zhicheng");
        options = ImageLoaderUtils.initOptions();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rc_uname.setText(name);
        rc_linyu.setText(linyu);
        rc_danwei.setText(danwei);
        current.setText(current_price);
        original.setText(original_price);
        original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
        rc_yuyuecount.setText("预约:"+yuyuecount);
        rc_zhicheng.setText(zhicheng);
        adress.setText(danwei+ "  ");
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(YuyueRenCai.this, AdressBaiduMap.class);
                intent.putExtra("key", danwei);
                startActivity(intent);
            }
        });

        meettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setVisibility(View.VISIBLE);
                now = System.currentTimeMillis()/1000;

                 date1 = now+30*86400;//TimeUtils.getStrTimeHHMM(time);
                 date2 = now+30*86400*2;
                 date3 = now+30*86400*3;

                item1.setText("一个月内:"+TimeUtils.getStrTimeHHMM(now+"")+"-"+TimeUtils.getStrTimeHHMM(date1+""));
                item2.setText("二个月内:"+TimeUtils.getStrTimeHHMM(now+"")+"-"+TimeUtils.getStrTimeHHMM(date2+""));
                item3.setText("三个月内:"+TimeUtils.getStrTimeHHMM(now+"")+"-"+TimeUtils.getStrTimeHHMM(date3+""));
            }
        });

        int  netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(YuyueRenCai.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else {
            ImageLoader.getInstance().displayImage(imgURL
                    , rc_img, options);
        }

        onclick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = now;
                end = date1;
                item1.setTextColor(0xff000000);
                item2.setTextColor(0xff9F9E9E);
                item3.setTextColor(0xff9F9E9E);
            }
        });
        onclick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = now;
                end = date2;
                item1.setTextColor(0xff9F9E9E);
                item2.setTextColor(0xff000000);
                item3.setTextColor(0xff9F9E9E);
            }
        });
        onclick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = now;
                end = date3;
                item1.setTextColor(0xff9F9E9E);
                item2.setTextColor(0xff9F9E9E);
                item3.setTextColor(0xff000000);
            }
        });

        cenel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setVisibility(View.GONE);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setVisibility(View.GONE);
                if(end >0){
                    meettime.setText(TimeUtils.getStrTimeHHMM(now+"")+"-"+TimeUtils.getStrTimeHHMM(end+""));
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(LoginState.equals("0")){
                    Intent intent = new Intent(YuyueRenCai.this, MyloginActivity.class);
                    startActivity(intent);
                }else{
                    meetAdress = adress.getText().toString();
                    meetTime = start+"-"+end;
                    context  = edit.getText().toString();
                    if(meetAdress == null || meetAdress.equals("")){
                        Toast.makeText(YuyueRenCai.this, "约见地址不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(meetTime.equals("0-0")){
                        Toast.makeText(YuyueRenCai.this, "约见时间不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(context == null || context.equals("")){
                        Toast.makeText(YuyueRenCai.this, "约见内容不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progress.setVisibility(View.VISIBLE);
                    confirm();
                }
            }
        });

    }

    private void confirm(){
        String url = "http://"+MyApplication.ip+"/api/require_new.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("aid",aid);
        map.put("method","add");
        map.put("typeid","4");
        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,""));
        map.put("meetaddress", meetAdress);
        map.put("meetdate", meetTime);
        map.put("meetprice", price);
        map.put("content", context);
        map.put("entry_address","1");
        if(WriteXuQiu.entry_address != 0){
            map.put("entry_address",WriteXuQiu.entry_address+"");
        }
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,1,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String url = "http://"+MyApplication.ip+"/api/require_new.php";
//                    String accessid="";
//                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                    if(loginState.equals("1")){
//                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
//
//                        accessid = mid;
//                    }else{
//                        accessid = MyApplication.deviceid;
//                    }
//
//                    HashMap<String,String> map = new HashMap<String, String>();
//                    map.put("aid",aid);
//                    map.put("method","add");
//                    map.put("typeid","4");
//                    map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,""));
//                    map.put("meetaddress", meetAdress);
//                    map.put("meetdate", meetTime);
//                    map.put("meetprice", price);
//                    map.put("content", context);
//                    map.put("accessid", accessid);
//                    ret =  OkHttpUtils.post(url, map);
//                    if(ret != null){
//                        Message msg = Message.obtain();
//                        msg.what=1;
//                        handler.sendMessage(msg);
//                    }
//                }catch (Exception e){
//
//                }
//
//            }
//        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progress.setVisibility(View.GONE);
                    Gson gs = new Gson();
                    ret = (String)msg.obj;
                    Ret data =  gs.fromJson(ret, Ret.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            Toast.makeText(YuyueRenCai.this, data.message, Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(YuyueRenCai.this, XuQiuSendSuccess.class);
//                            startActivity(intent);
                            Intent intent = new Intent(YuyueRenCai.this, MyXuqiuActivity.class);
                            intent.putExtra("type","4");
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("预约人才");
        if(isFinish){
            isFinish = false;
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("预约人才");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WriteXuQiu.entry_address=0;
    }
}
