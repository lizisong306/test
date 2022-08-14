package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianYuYue;
import dao.Service.PulseCondtion;
import dao.dbentity.CollectionEntity;
import dao.dbentity.PulseData;
import entity.Category;
import entity.Interest;
import entity.QiYeEntiry;
import entity.RetBaiMaiData;
import entity.RetYuJianData;
import entity.Usercode;
import entity.YuJian;
import entity.YuYueData;
import entity.industrydata;
import entity.userlogin;
import fragment.WelcomePulse;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/5/24.
 */

public class YanZhengMa extends AutoLayoutActivity {
    ImageView back;
    TextView send;
    EditText exit1,exit2,exit3,exit4;
    private  String  ips;
    private String phone;
    private  HashMap<String,String> map;
    private String ulogin;
    private int million = 60;
    private String sessionid;
    public PulseCondtion pulseCondtion;
    MaiDianYuYue mYuJian;
    Usercode usercode;
    ProgressBar progress;
    private ArrayList<CollectionEntity> listCollections = null;
    private MaiDianCollection maiDianCollection = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanzhengma);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        phone = getIntent().getStringExtra("phone");
        sessionid = getIntent().getStringExtra("sessionid");
        back = (ImageView)findViewById(R.id.login_backs);
        send = (TextView)findViewById(R.id.send);
        exit1 = (EditText)findViewById(R.id.edite1);
        exit2 = (EditText)findViewById(R.id.edite2);
        exit3 = (EditText)findViewById(R.id.edite3);
        exit4 = (EditText)findViewById(R.id.edite4);
        progress = (ProgressBar)findViewById(R.id.progress);
        pulseCondtion = PulseCondtion.getInstance(MyApplication.context);
        maiDianCollection=MaiDianCollection.getInstance(this);
        mYuJian = MaiDianYuYue.getInstance(this);
        exit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("lizisong", actionId+"");
                return false;
            }
        });
        exit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(count ==1){
                     exit2.setFocusable(true);
                     exit2.requestFocus();
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        exit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count ==1){
                    exit3.setFocusable(true);
                    exit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        exit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count ==1){
                    exit4.setFocusable(true);
                    exit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        exit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count ==1){
                    loginjson();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        exit2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("lizisong", actionId+"");
                return false;
            }
        });
        exit3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("lizisong", actionId+"");
                return false;
            }
        });
        exit4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String txt1,txt2,txt3,txt4;
                txt1 = exit1.getText().toString();
                txt2 = exit2.getText().toString();
                txt3 = exit3.getText().toString();
                txt4 = exit4.getText().toString();
                if(txt1 == null || txt2 == null || txt3 == null || txt4 == null
                        || txt1.equals("") || txt2.equals("") || txt3.equals("") || txt4.equals("")){
                    Toast.makeText(YanZhengMa.this, "请检查验证码是否正确",Toast.LENGTH_SHORT).show();
                    return false;
                }
               loginjson();
                return false;
            }
        });

        ips = MyApplication.ip;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yzregistjson();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("验证码");
        setSendBt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("验证码");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(exit4.hasFocus()){
                exit4.setText("");
                exit3.requestFocus();
            }else if(exit3.hasFocus()){
                exit3.setText("");
                exit2.requestFocus();
            }else if(exit2.hasFocus()){
                exit2.setText("");
                exit1.requestFocus();
            }else if(exit1.hasFocus()){
                exit1.setText("");
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("lizisong",event.getKeyCode()+"lizisong");
        return super.dispatchKeyEvent(event);


    }

    HashMap<String,String> registmap;
    String registlogin;
    private void yzregistjson() {
        progress.setVisibility(View.VISIBLE);
//        registmap = new HashMap<String, String>();
//        registmap.put("tel",phone);
//        registmap.put("action","send");
//        registmap.put("appid",MainActivity.feixinCode);
//        registmap.put("clienttype","2");
////        registmap.put("accessid", MyApplication.deviceid);
////        registmap.put("timestamp",timestamp+"");
////        registmap.put("version", MyApplication.version);
//        String yzregiststr="http://"+MyApplication.ip+"/api/user_register_login.php";
//        NetworkCom networkCom = NetworkCom.getNetworkCom();
//        networkCom.postJson(yzregiststr,registmap,handler,10,0);
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //123.206.8.208/api/sms_interface.php

                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();

                    registmap = new HashMap<String, String>();
                    registmap.put("tel",phone);
                    registmap.put("action","send");
                    registmap.put("appid",MainActivity.feixinCode);
                    registmap.put("clienttype","2");
                    registmap.put("accessid", MyApplication.deviceid);
                    registmap.put("timestamp",timestamp+"");
                    registmap.put("version", MyApplication.version);
                    sort.add("tel"+phone);
                    sort.add("action"+"send");
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("accessid"+ MyApplication.deviceid);
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
                    registmap.put("sign", sign);
                    registmap.put("accessid", accessid);
                    String yzregiststr="http://"+ips+"/api/user_register_login.php";
                    registlogin = OkHttpUtils.postkeyvlauspainr(yzregiststr,registmap);
                    if(registlogin != null){
                        Message msg = Message.obtain();
                        msg.what=10;
                        msg.obj=registlogin;
                        handler.sendMessage(msg);

                    }
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
                    send.setClickable(false);
                    million--;
                    send.setTextSize(15);
                    send.setText("再次发送 ("+million+")");
                    handler.sendEmptyMessageDelayed(2, 1000);
                }
                if (million == 0) {
                    send.setClickable(true);
                    send.setText("获取验证码");
                    million = 60;
                    handler.removeCallbacksAndMessages(null);

                }
            }

            if(msg.what == 10){
                progress.setVisibility(View.GONE);
                setSendBt();
            }

            if(msg.what == 13){
                    String ret =(String) msg.obj;
                    Gson gs = new Gson();
                    QiYeEntiry qiye = gs.fromJson(ret, QiYeEntiry.class);
                    if(qiye != null){
                        if(qiye.code.equals("1")){
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ID,qiye.data.id);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENTERPRISE,qiye.data.enterprise_name);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_TELEPHONE, qiye.data.telephone);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_SIGN_TIME, qiye.data.sign_time);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_REGION_NAME, qiye.data.region_name);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENAME, qiye.data.ename);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_IS_MEMBER, qiye.data.is_member);
                            Log.d("lizisong", "SharedPreferencesUtil.HUIYUAN_ENTERPRISE:"+qiye.data.enterprise_name);
                            Intent intent = new Intent();
                            intent.setAction("action_show_chuangxin");
                            MyApplication.context.sendBroadcast(intent);
                        }
                    }
            }


            if(msg.what == 1){
                {
                    try {
                    Gson g=new Gson();
                    ulogin = (String)msg.obj;
                    usercode =g.fromJson(ulogin, Usercode.class);
                    final userlogin data = usercode.getData();
                    String loginFlag = data.getLoginFlag();
                    progress.setVisibility(View.GONE);
                    if(usercode.getCode()==1) {
                        Toast.makeText(YanZhengMa.this, usercode.getMessage(), Toast.LENGTH_SHORT).show();

                        if (loginFlag.equals(data.getLoginFlag())) {
                            MainActivity.xuqiucount = data.num;
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, usercode.getCode() + "");
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.getUsername());
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.getNickname());
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, data.getMid());
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, data.mtype);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, data.linkman);
                            SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, data.patent_sq);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, data.img);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, data.wq_num);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                            SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.JNINTIME,data.jointime);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.INFOR_NEW,data.infor_new);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_0,data.require_new_0);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_4,data.require_new_4);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_2,data.require_new_2);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_7,data.require_new_7);
                            List<Interest> interestArea = data.interestArea;
                            if(interestArea != null){
//                                    Log.d("lizisong", "interestArea.size():"+interestArea.size());
                                if(interestArea.size() == 1){
                                    try {
                                        if(interestArea.get(0).evalue == null || interestArea.get(0).evalue.equals("")){
                                            Log.d("lizisong", "1");
                                            SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                                        }else{
                                            Log.d("lizisong", "2");
                                            SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 1);
                                        }
                                    }catch (Exception e){
                                        Log.d("lizisong", "3");
                                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                                    }

                                }else{
                                    Log.d("lizisong", "4");
                                    SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, interestArea.size());
                                }

                                for (int i = 0; i < interestArea.size(); i++) {
                                    String evalue = interestArea.get(i).evalue;
                                    Log.d("lizisong", "evalue:"+evalue);
                                    if (evalue.equals("500")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, evalue);
                                    } else if (evalue.equals("1000")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, evalue);
                                    } else if (evalue.equals("1500")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, evalue);
                                    } else if (evalue.equals("3000")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, evalue);
                                    } else if (evalue.equals("2000")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, evalue);
                                    } else if (evalue.equals("3500")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, evalue);
                                    } else if (evalue.equals("4000")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, evalue);
                                    } else if (evalue.equals("2500")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, evalue);
                                    } else if (evalue.equals("4500")) {
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, evalue);
                                    }

                                }
                            }else{
                                SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                            }
                            WelcomePulse.LoginState = true;
                            qiyejson();
                            /**
                             * 同步把脉数据,同步预约信息
                             */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronizationbaimai();
                                    synchronizationyujian();
                                    collect();

                                }
                            }).start();
                            //                            Intent intentlogin = new Intent(MyloginActivity.this, MainActivity.class);
                            //                            startActivity(intentlogin);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                            WelcomePulse.LoginState = true;
                            MyloginActivity.isClose =true;
                            YanZhengMa.this.finish();
                            // overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
                        }


                    }else {
                        Toast.makeText(YanZhengMa.this, usercode.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }catch (Exception e){

                    }
                }

            }
        }
    };

    private void setSendBt() {
        //设置发送按钮的变化
        send.setClickable(false);
        send.setText("再次发送 ("+million+")");
        send.setTextSize(15);

        handler.sendEmptyMessageDelayed(2, 1500);
    }

    private void loginjson() {
        hintKbTwo();
        progress.setVisibility(View.VISIBLE);
//        String code = exit1.getText().toString()+ exit2.getText().toString()+ exit3.getText().toString()+ exit4.getText().toString();
//        String str="http://"+ips+"/api/user_register_login.php";
//        HashMap<String,String> map = new HashMap<String, String>();
//        map.put("tel",phone);
//        map.put("code",code);
//        map.put("appid",MainActivity.feixinCode);
//        map.put("clienttype","2");
//        NetworkCom networkCom = NetworkCom.getNetworkCom();
//        networkCom.postJson(str,map,handler,1,0);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    map=new HashMap<>();

                    String code = exit1.getText().toString()+ exit2.getText().toString()+ exit3.getText().toString()+ exit4.getText().toString();
                    String timestamp = System.currentTimeMillis()+"";

                    map.put("tel",phone);
                    map.put("code",code);
                    map.put("appid",MainActivity.feixinCode);
                    map.put("clienttype","2");
                    map.put("sessionid", sessionid);
                    map.put("version", MyApplication.version);

                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("tel"+phone);
                    sort.add("code"+code);
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("sessionid"+sessionid);
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
                    map.put("sign",sign);
                    map.put("timestamp",timestamp);
                    map.put("accessid",accessid);
                    String str="http://"+ips+"/api/user_register_login.php";
//                    String str="http://www.maidiantech.com/api/login.php";
                    Log.d("lizisong",map.toString());
                    ulogin = OkHttpUtils.postkeyvlauspainr(str, map);
                    Log.d("lizisong","login:"+ulogin);
                    Thread.sleep(2000);
//                    UIHelper.hideDialogForLoading();
                    // progresslogin.setVisibility(View.GONE);
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj = ulogin;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    public List<PulseData> listData;
    /**
     * 同步把信息
     */
    public void synchronizationbaimai(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        String timestamp = System.currentTimeMillis()+"";
        String sign="";
        ArrayList<String> sort = new ArrayList<String>();
        sort.add("action"+"select");
        sort.add("mid"+mid);
        sort.add("timestamp"+timestamp);
        sort.add("version"+MyApplication.version);
        String accessid="";
        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        if(loginState.equals("1")){
            accessid = mid;
        }else{
            accessid = MyApplication.deviceid;
        }
        sort.add("accessid" + accessid);
        sign=KeySort.keyScort(sort);
        MyApplication.setAccessid();
        String url = "http://"+ips+"/api/getAreaCateInfo.php?action=select&mid="+mid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
        try {
            ulogin = OkHttpUtils.loaudstringfromurl(url);
            if(ulogin!= null){
                Gson g=new Gson();
                RetBaiMaiData item =g.fromJson(ulogin, RetBaiMaiData.class);
                if(item != null){
                    if(item.code.equals("1")){
                        listData = pulseCondtion.get();
                        for(int i = 0; i<item.data.size(); i++){
                            boolean state = false;
                            industrydata data = item.data.get(i);
                            for(int j =0; j<listData.size(); j++){
                                PulseData pos = listData.get(j);
                                if(pos.evaluetop.equals(data.getEvalue()+"")){
                                    state = true;
                                    break;
                                }
                            }
                            if(!state){
                                PulseData newData = new PulseData();
                                newData.name = data.getEname();
                                newData.pid = data.id+"";
                                newData.mid = mid;
                                newData.typeid = data.typeid;
                                newData.evaluetop =data.getEvalue()+"";
                                newData.updatatime = data.udate+"";
                                newData.evalueTitle="";
                                newData.evalue="";
                                if(data.province.equals("")){
                                    newData.province = "0";
                                }else{
                                    newData.province = data.province;
                                }
                                newData.category =data.category;
                                for(int z=0;z<data.getSonCate().size();z++){
                                    Category category = data.getSonCate().get(z);
                                    if(category.isChoice.equals("1")){
                                        newData.evalueTitle = newData.evalueTitle+category.getEname()+",";
                                        newData.evalue = newData.evalue+category.getEvalue()+",";
                                    }
                                }
                                pulseCondtion.insert(newData);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
    }


    /**
     * 同步预见信息
     */
    public void  synchronizationyujian(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");

        String timestamp = System.currentTimeMillis()+"";
        String sign="";
        ArrayList<String> sort = new ArrayList<String>();
        sort.add("method"+"select");
        sort.add("mid"+mid);
        sort.add("timestamp"+timestamp);
        sort.add("version"+MyApplication.version);
        String accessid="";
        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        if(loginState.equals("1")){
//            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

            accessid = mid;
        }else{
            accessid = MyApplication.deviceid;
        }
        sort.add("accessid" + accessid);
        sign = KeySort.keyScort(sort);
        MyApplication.setAccessid();
        String url = "http://"+ips+"/api/appointment.php?method=select&mid="+mid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;

        try {
            ulogin = OkHttpUtils.loaudstringfromurl(url);

            if(ulogin!= null){

                Gson g=new Gson();

                RetYuJianData item =g.fromJson(ulogin, RetYuJianData.class);

                if(item.code.equals("1")){
                    List<YuJian> list =  item.data.posts;
                    if(list != null){
                        List<YuYueData> arrayList = mYuJian.get();
                        if(list.size() > 0){
                            for(int i=0; i<list.size();i++){
                                boolean state = false;
                                YuJian pos = list.get(i);
                                for(int j=0; j < arrayList.size();j++){
                                    YuYueData tt = arrayList.get(j);
                                    if(tt.meetAdress.equals(pos.id)){
                                        state = true;
                                        break;
                                    }
                                }

                                if(!state){
                                    YuYueData date = new YuYueData();
                                    date.typeid = pos.typeid;
                                    date.meetAdress = pos.id;
                                    date.meetTel = pos.mobile;
                                    date.rank = pos.rank;
                                    date.update=pos.pubdate+"000";
                                    date.meetTime = pos.meetdate;
                                    date.pic = pos.litpic;
                                    date.meetMen = pos.meetname;
                                    date.meetTitle = pos.meettheme;
                                    if(pos.typeid.equals("4")){
                                        date.title = pos.username;
                                        date.meetPost = pos.meetaddress;
                                    }else{
                                        date.title = pos.title;
                                        date.meetPost = pos.des;
                                    }
                                    if(date.typeid == null){
                                        date.typeid = "";
                                    }
                                    if(date.meetAdress == null){
                                        date.meetAdress = "";
                                    }
                                    if(date.meetTel == null){
                                        date.meetTel = "";
                                    }
                                    if(date.rank == null){
                                        date.rank = "";
                                    }
                                    if(date.update == null){
                                        date.update = "";
                                    }
                                    if(date.meetTime == null){
                                        date.meetTime = "";
                                    }

                                    if(date.pic == null){
                                        date.pic = "";
                                    }
                                    if(date.meetMen == null){
                                        date.meetMen = "";
                                    }

                                    if(date.meetTitle == null){
                                        date.meetTitle = "";
                                    }
                                    if(date.title == null){
                                        date.title = "";
                                    }
                                    if(date.meetPost == null){
                                        date.meetPost = "";
                                    }

                                    mYuJian.insert(date);
                                }


                            }

                        }

                    }
                }

            }
        }catch (Exception e){

        }
    }
    String quxiaojson="";

    /**
     * 同步收藏的信息
     */
    public void collect() {
        try {
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            String timestamp = System.currentTimeMillis()+"";
            String sign="";
            ArrayList<String> sort = new ArrayList<String>();
            sort.add("mid"+mid);
            sort.add("method"+"list");
            sort.add("timestamp"+timestamp);
            sort.add("version"+MyApplication.version);
            String accessid="";
            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(loginState.equals("1")){
//                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                accessid = mid;
            }else{
                accessid = MyApplication.deviceid;
            }
            sort.add("accessid" + accessid);
            sign = KeySort.keyScort(sort);
            MyApplication.setAccessid();
            quxiaojson = OkHttpUtils.loaudstringfromurl("http://"+ips+"/api/arc_store.php?mid=" + mid + "&method=list&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid);
            if (quxiaojson != null) {
                Gson g = new Gson();
                RetYuJianData retYuJianData = g.fromJson(quxiaojson, RetYuJianData.class);

//                                       List<YuJian> posts = data.posts;
                if (retYuJianData.code.equals("1")) {
                    List<YuJian> posts = retYuJianData.data.posts;
                    if (posts != null) {
                        listCollections = maiDianCollection.get();
                        if (posts.size() > 0) {
                            for (int i = 0; i < posts.size(); i++) {
                                boolean state = false;
                                YuJian pos = posts.get(i);
                                for (int j = 0; j < listCollections.size(); j++) {
                                    CollectionEntity tt = listCollections.get(j);
                                    if (tt.aid.equals(pos.aid)) {
                                        state = true;
                                        break;
                                    }
                                }
                                if(!state){
                                    CollectionEntity date = new CollectionEntity();
                                    date.type = pos.typename;
                                    date.pid = pos.id;
                                    date.pic = pos.litpic;
                                    if(pos.typename.equals("人才")){
                                        date.title = pos.username;
                                    }else{
                                        date.title = pos.title;
                                    }
                                    date.aid = pos.aid;
                                    date.iscollect="0";
                                    date.isAdd = 0;
                                    date.image=pos.image;
                                    date.click = pos.click;
                                    date.imageState = pos.imageState;
                                    date.description = pos.description;
                                    if(pos.area_cate!= null){
                                        date.area_cate = pos.area_cate.getArea_cate1();
                                    }
                                    date.is_academician = pos.is_academician;

                                    maiDianCollection.insert(date);
                                }

                            }
                        }

                    }
                }
            }
        }catch (Exception e){

        }

    }
    private void qiyejson(){
        String url = "http://erp.zhongkechuangxiang.com/api/Product/MPList";
        HashMap<String, String > map = new HashMap<>();
        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,13,0);
    }

}
