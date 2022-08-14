package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianYuYue;
import dao.Service.PulseCondtion;
import dao.dbentity.CollectionEntity;
import dao.dbentity.PulseData;
import entity.Category;
import entity.Interest;
import entity.RetBaiMaiData;
import entity.RetYuJianData;
import entity.SmsRegisData;
import entity.Usercode;
import entity.YuJian;
import entity.YuYueData;
import entity.industrydata;
import entity.userlogin;
import fragment.WelcomePulse;
import view.BTAlertDialog;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2017/4/11.
 */

public class RelationPhone extends AutoLayoutActivity {
    private LinearLayout detailRa;
    private ImageView sanfanBack;
    private EditText sanfanName;
    private EditText safan_phone;
    private TextView sanfanCode;
    private Button sanfan_nexts;
    String fromSMSCode;
    String yzphone;
    String qqlogin;
    String smsCode;
    HashMap<String,String> hashmap;
    boolean isState = false;
    String name,openid,img,type,appid,client;
    public static boolean loginState = false;
    String  trim,quxiaojson;
    String ulogin;
    public List<PulseData> listData;
    public PulseCondtion pulseCondtion;

    private ArrayList<CollectionEntity> listCollections = null;
    private MaiDianCollection maiDianCollection = null;
    MaiDianYuYue mYuJian;
    private int million = 60;
    private String registlogin;
    private String sessionid;
    private  String   ips;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                if (million > 0) {
                    sanfanCode.setClickable(false);
                    million--;
                    sanfanCode.setTextSize(15);
                    sanfanCode.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1000);
                }
                if (million == 0) {
                    sanfanCode.setClickable(true);
                    sanfanCode.setText("获取验证码");
                    sanfanCode.setTextColor(Color.parseColor("#ffffff"));
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
                        sessionid = json_data.data.sessionid;
//                        Log.d("lizisong", "smsCode:"+smsCode);
//                        Log.d("lizisong", "sessionid:"+sessionid);
//                        Toast.makeText(RelationPhone.this, smsCode,Toast.LENGTH_SHORT).show();
                        setSendBt();
                    }else{
                        Toast.makeText(RelationPhone.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JsonIOException e){

                }

            }

            try {
                if(msg.what==0){
                    Gson g=new Gson();
                    Usercode qqcode = g.fromJson(qqlogin, Usercode.class);

                    final userlogin data = qqcode.getData();
                    String loginFlag = data.getLoginFlag();
                    MainActivity.xuqiucount = data.num;

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, qqcode.getCode()+"");
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.getUsername());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.getNickname());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, data.getMid());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, data.mtype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, data.linkman);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, data.img);

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE,data.wq_num);
                    SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, data.patent_sq);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.JNINTIME,data.jointime);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.INFOR_NEW,data.infor_new);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_0,data.require_new_0);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_4,data.require_new_4);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_2,data.require_new_2);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_7,data.require_new_7);
                    List<Interest> interestArea = data.interestArea;
                    if(interestArea != null){
//                    Log.d("lizisong", "interestArea.size():"+interestArea.size());
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
                        for (int i=0; i < interestArea.size();i++){
                            String evalue = interestArea.get(i).evalue;
//                        Log.d("lizisong", "evalue:"+evalue);
                            if(evalue.equals("500")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, evalue);
                            }else if(evalue.equals("1000")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, evalue);
                            }else if(evalue.equals("1500")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, evalue);
                            }else if(evalue.equals("3000")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, evalue);
                            }else if(evalue.equals("2000")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, evalue);
                            }else if(evalue.equals("3500")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, evalue);
                            }else if(evalue.equals("4000")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, evalue);
                            }else if(evalue.equals("2500")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, evalue);
                            }else if(evalue.equals("4500")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, evalue);
                            }

                        }
                    }else{
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                    }
                    SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, true);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                    WelcomePulse.LoginState = true;
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

                    RelationPhone.this.finish();
                }
            }catch (Exception e){

            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_phone);
        isState = false;
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Intent intent = getIntent();
         name=intent.getStringExtra("uername");
         openid=intent.getStringExtra("openid");
         img=intent.getStringExtra("uerimg");
         type=intent.getStringExtra("type");
         appid=intent.getStringExtra("appid");
         client=intent.getStringExtra("clienttype");
        ips = MyApplication.ip;
        initView();
        pulseCondtion = PulseCondtion.getInstance(MyApplication.context);
        sanfanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BTAlertDialog dialog = new BTAlertDialog(RelationPhone.this);
                dialog.setTitle("您尚未完成登录，是否继续绑定？");

                dialog.setNegativeButton("退出登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, "0");
                        SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "0");
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, "0");
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE,0);
                        hintKbTwo();
                        finish();
                    }
                });
                dialog.setPositiveButton("继续绑定",null);
                dialog.show();

            }
        });
        sanfanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yzphone = safan_phone.getText().toString();
                if(yzphone != null){
                    yzphone = yzphone.trim();
                }
                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(RelationPhone.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    sanfanCode.setClickable(false);
                    yzregistjson();
                }
            }
        });
        sanfan_nexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!isState){
                    fromSMSCode =  sanfanName.getText().toString();
//                    if(fromSMSCode != null){
//                        if(fromSMSCode.trim().equals(smsCode)){
//                            isState = true;
//                        }
//                    }
//                }
                if(fromSMSCode == null || fromSMSCode.equals("")){
                    Toast.makeText(RelationPhone.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                 yzphone = safan_phone.getText().toString();
                 hashmap=new HashMap<>();
                 hashmap.put("nickname",name);
                 hashmap.put("openid",openid );
                 hashmap.put("img", img);
                 hashmap.put("type", type);
                 hashmap.put("appid",MainActivity.feixinCode);
                 hashmap.put("clienttype","2");
                hashmap.put("tel",yzphone);
                hashmap.put("code", fromSMSCode);
                hashmap.put("sessionid", sessionid);

                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("nickname"+name);
                sort.add("openid"+openid);
                sort.add("img"+img);
                sort.add("type"+ type);
                sort.add("appid"+ MainActivity.feixinCode);
                sort.add("clienttype"+"2");
                sort.add("tel"+yzphone);
                sort.add("code"+fromSMSCode);
                sort.add("sessionid"+sessionid);
                sort.add("timestamp"+timestamp);
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
                hashmap.put("sign", sign);
                hashmap.put("timestamp", timestamp);
                hashmap.put("accessid", accessid);
                hintKbTwo();
                getjson();
            }
        });
    }

    private void initView() {
        detailRa = (LinearLayout) findViewById(R.id.detail_ra);
        sanfanBack = (ImageView) findViewById(R.id.sanfan_back);
        safan_phone = (EditText) findViewById(R.id.safan_phone);
        sanfanName = (EditText) findViewById(R.id.sanfan_name);
        sanfanCode = (TextView) findViewById(R.id.sanfan_code);
        sanfan_nexts = (Button) findViewById(R.id.sanfan_nexts);
    }

    private void yzregistjson() {

        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    String yzregiststr="http://"+ips+"/api/thirdLogin_new.php";
                    HashMap<String ,String> hashMap = new HashMap<String, String>();
                    hashMap.put("tel",yzphone);
                    hashMap.put("accessid", MyApplication.deviceid);
                    hashMap.put("action", "send");
                    hashMap.put("version", MyApplication.version);
                    sort.add("tel"+yzphone);
                    sort.add("action"+"send");

                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    sort.add("accessid"+MyApplication.deviceid);
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
                    hashMap.put("sign", sign);
                    hashMap.put("timestamp", timestamp);
                    hashMap.put("accessid", accessid);
                    registlogin=  OkHttpUtils.postkeyvlauspainr(yzregiststr,hashMap);
//                    registlogin = OkHttpUtils.loaudstringfromurl(yzregiststr);
                    if(registlogin != null){
                        Message msg = Message.obtain();
                        msg.what = 8;
                        handler.sendMessage(msg);

                    }
                }
            }.start();
        }catch (Exception e){}

    }

    private void setSendBt() {
        //设置发送按钮的变化
        sanfanCode.setClickable(false);
        sanfanCode.setText("60秒内输入");
        sanfanCode.setTextSize(13);
        sanfanCode.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    public void getjson() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //String str="http://123.207.164.210/api/thirdLogin.php";
                    String str= "http://"+ips+"/api/thirdLogin_new.php";
//                    Log.d("lizisong", "登录发送的信息3:"+hashmap.toString());
                    qqlogin = OkHttpUtils.post(str, hashmap);
                    loginState = false;
                    Thread.sleep(2000);
//                    UIHelper.hideDialogForLoading();
                    Message msg=new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    /**
     * 同步收藏的信息
     */
    public void collect() {
        try {
            MyApplication.setAccessid();
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


                accessid = mid;
            }else{
                accessid = MyApplication.deviceid;
            }
            sort.add("accessid" + accessid);
            sign = KeySort.keyScort(sort);

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
                                    if(pos.typename.equals("人才")||pos.typename.equals("专家")){
                                        date.title = pos.username;
                                    }else{
                                        date.title = pos.title;
                                    }

                                    date.aid = pos.aid;

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

    /**
     * 同步把信息
     */
    public void synchronizationbaimai(){
        MyApplication.setAccessid();
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
                                newData.updatatime = data.udate;
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
        MyApplication.setAccessid();
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
            accessid = mid;
        }else{
            accessid = MyApplication.deviceid;
        }
        sort.add("accessid" + accessid);
        sign=KeySort.keyScort(sort);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, "0");
            SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);
            SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, "0");
            SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, 0);
            SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, "0");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");

            SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "0");
            SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
            SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, "0");
            SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, 0);
            RelationPhone.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

