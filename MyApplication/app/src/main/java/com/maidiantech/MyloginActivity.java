package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalTipDialog;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.maidiantech.common.resquest.NetworkCom;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import Util.UIHelper;
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
import entity.SmsRegisData;
import entity.Usercode;
import entity.YuJian;
import entity.YuYueData;
import entity.industrydata;
import entity.userlogin;
import fragment.WelcomePulse;
import view.StyleUtils;
import view.StyleUtils1;
import view.T;


/**
 * Created by 13520 on 2016/9/8.
 */
public class MyloginActivity extends AutoLayoutActivity implements View.OnClickListener {
    UMShareAPI mShareAPI;
    SHARE_MEDIA platform;
    EditText myphone;
    EditText mypwd;
    Button mylongbt;
    TextView mypwdtv;
    TextView myzhanhao;
    TextView myweixlogin;
    TextView myqqlogin;
    ImageView login_back;
    OkHttpUtils loginutils;
    String  trim,quxiaojson;
    TextView anniu1;
    private PopupWindow mPopBottom;
    public static String sessionid;
    String pwd;
    String ulogin;
    private int million = 60;
    ProgressBar progresslogin;
    private String encode;
    private  HashMap<String,String> map;
    private int count = 0;
    boolean flag=true;
    SharedPreferences spf;
    Usercode usercode;
    boolean flags ;
    SharedPreferences flage;
    HashMap<String,String> hashmap;
    String qqlogin;
    SharedPreferences sharePreferens;
    public PulseCondtion pulseCondtion;
    public List<PulseData> listData;
    public static boolean loginState = false;
    private ArrayList<CollectionEntity> listCollections = null;
    private MaiDianCollection maiDianCollection = null;
    ProgressBar progressBar;
    MaiDianYuYue mYuJian;
    public  static  boolean isClose = false;
    String smsCode;
    private  String   ips;
    private  Button anniu;
    private TextView xieyi;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
            if(msg.what==0){
                Gson g=new Gson();
                Usercode qqcode = g.fromJson(qqlogin, Usercode.class);
               if(qqcode != null && qqcode.getCode()==1){
                final userlogin data = qqcode.getData();
                String loginFlag = data.getLoginFlag();
                MainActivity.xuqiucount = data.num;

                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, qqcode.getCode()+"");
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.getUsername());
                SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.getNickname());
                SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, data.getMid());
//                   Log.d("lizisong", ""+data.getMid());
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, data.mtype);
                SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, data.patent_sq);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, data.linkman);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, data.img);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, data.wq_num);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.JNINTIME,data.jointime);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.INFOR_NEW,data.infor_new);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_0,data.require_new_0);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_4,data.require_new_4);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_2,data.require_new_2);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REQUIRE_NEW_7,data.require_new_7);
//                if(data.tel == null || data.tel.equals("")){
//
//                    Intent intent=new Intent(MyloginActivity.this,RelationPhone.class);
//                    intent.putExtra("uername",hashmap.get("nickname"));
//                    intent.putExtra("openid",hashmap.get("openid"));
//                    intent.putExtra("uerimg",hashmap.get("img"));
//                    intent.putExtra("type",hashmap.get("type"));
//                    intent.putExtra("appid",MainActivity.feixinCode);
//                    intent.putExtra("clienttype","2");
//                    startActivity(intent);
//                    MyloginActivity.this.finish();
//                    return;
//                }
                List<Interest> interestArea = data.interestArea;
                if(interestArea != null){
                    if(interestArea.size() == 1){
                        try {
                            if(interestArea.get(0).evalue == null || interestArea.get(0).evalue.equals("")){
                                SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                            }else{
                                SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 1);
                            }
                        }catch (Exception e){
                            SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                        }

                    }else{
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, interestArea.size());
                    }
                    for (int i=0; i < interestArea.size();i++){
                        String evalue = interestArea.get(i).evalue;
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
                    progressBar.setVisibility(View.GONE);
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

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                    WelcomePulse.LoginState = true;
                    hintKbTwo();
                    MyloginActivity.this.finish();
                  }else {
                       final userlogin data = qqcode.getData();
                       if(data.state == 1){
                           Intent intent=new Intent(MyloginActivity.this,RelationPhone.class);
                           intent.putExtra("uername",hashmap.get("nickname"));
                           intent.putExtra("openid",hashmap.get("openid"));
                           intent.putExtra("uerimg",hashmap.get("img"));
                           intent.putExtra("type",hashmap.get("type"));
                           intent.putExtra("appid",MainActivity.feixinCode);
                           intent.putExtra("clienttype","2");
                           startActivity(intent);
                           MyloginActivity.this.finish();
                           return;
                       }
                  }
               }


                if(msg.what==1){
                    Gson g=new Gson();
                    usercode =g.fromJson(ulogin, Usercode.class);
                    final userlogin data = usercode.getData();
                    String loginFlag = data.getLoginFlag();
                    progressBar.setVisibility(View.GONE);
                    if(usercode.getCode()==1) {
                        Toast.makeText(MyloginActivity.this, usercode.getMessage(), Toast.LENGTH_SHORT).show();

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
                                  if(interestArea.size() == 1){
                                      try {
                                          if(interestArea.get(0).evalue == null || interestArea.get(0).evalue.equals("")){
                                              SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                                          }else{
                                              SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 1);
                                          }
                                      }catch (Exception e){
                                          SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                                      }

                                  }else{
                                      SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, interestArea.size());
                                  }

                                    for (int i = 0; i < interestArea.size(); i++) {
                                        String evalue = interestArea.get(i).evalue;
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
                                MyloginActivity.this.finish();
                                // overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
                            }


                    }else {
                        Toast.makeText(MyloginActivity.this, usercode.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(msg.what==2){
                    if (million > 0) {
                        anniu1.setClickable(false);
                        million--;
                        anniu1.setTextSize(15);
                        anniu1.setText(million + "秒内输入");
                        handler.sendEmptyMessageDelayed(2, 1000);
                    }
                    if (million == 0) {
                        anniu1.setClickable(true);
                        anniu1.setText("获取验证码");
                        anniu1.setTextColor(Color.parseColor("#ffffff"));
                        million = 60;
                        handler.removeCallbacksAndMessages(null);

                    }
                }
                if(msg.what == 10){

                        try {
                            progressBar.setVisibility(View.GONE);
                            Gson gs = new Gson();
                            SmsRegisData json_data =gs.fromJson(registlogin, SmsRegisData.class);
                            if(json_data.code.equals("1")){
                                smsCode = json_data.data.code;
                                sessionid = json_data.data.sessionid;

                                Intent intent = new Intent(MyloginActivity.this, YanZhengMa.class);
                                intent.putExtra("phone", trim);
                                intent.putExtra("sessionid",sessionid);
                                startActivity(intent);
//                                setSendBt();
//                                Toast.makeText(MyloginActivity.this, smsCode,Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MyloginActivity.this, json_data.message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (JsonIOException e){

                        }

                }
                if(msg.what == 13){
                    String ret =(String) msg.obj;
                    Gson gs = new Gson();
                    QiYeEntiry  qiye = gs.fromJson(ret, QiYeEntiry.class);
                    if(qiye != null){
                        if(qiye.code.equals("1")){
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ID,qiye.data.id);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENTERPRISE,qiye.data.enterprise_name);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_TELEPHONE, qiye.data.telephone);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_SIGN_TIME, qiye.data.sign_time);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_REGION_NAME, qiye.data.region_name);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_ENAME, qiye.data.ename);
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.HUIYUAN_IS_MEMBER, qiye.data.is_member);
                            Intent intent = new Intent();
                            intent.setAction("action_show_chuangxin");
                            MyApplication.context.sendBroadcast(intent);
                        }
                    }
                }
            } catch (Exception e) {}
        }
    };

    private void setSendBt() {
        //设置发送按钮的变化
        anniu1.setClickable(false);
        anniu1.setText("60秒内输入");
        anniu1.setTextSize(14);
        anniu1.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_login);
        loginState = false;
        isClose = false;
        mYuJian = MaiDianYuYue.getInstance(this);
        maiDianCollection=MaiDianCollection.getInstance(this);
        ips = MyApplication.ip;
        if(null==mShareAPI){
            mShareAPI = UMShareAPI.get(this);
        }
        sharePreferens = SharedPreferencesUtil.getSharedPreferences(this);
        pulseCondtion = PulseCondtion.getInstance(MyApplication.context);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
        myphone.setOnClickListener(this);
        mypwd.setOnClickListener(this);
        mylongbt.setOnClickListener(this);
        mypwdtv.setOnClickListener(this);
        myzhanhao.setOnClickListener(this);
        myweixlogin.setOnClickListener(this);
        myqqlogin.setOnClickListener(this);

    }

    private void loginjson() {
         loginutils= OkHttpUtils.getInstancesOkHttp();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String str="http://"+ips+"/api/user_register_login.php";
//                    String str="http://www.maidiantech.com/api/login.php";
                     ulogin = OkHttpUtils.postkeyvlauspainr(str, map);
                     Thread.sleep(2000);
//                    UIHelper.hideDialogForLoading();
                   // progresslogin.setVisibility(View.GONE);
                     Message msg=new Message();
                     msg.what=1;
                     handler.sendMessage(msg);
               }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }



    private void initView() {
        login_back=(ImageView) findViewById(R.id.login_backs);
        myphone = (EditText) findViewById(R.id.my_phone);
        mypwd = (EditText) findViewById(R.id.my_pwd);
        mylongbt = (Button) findViewById(R.id.my_long_bt);
        mypwdtv = (TextView) findViewById(R.id.my_pwd_tv);
        myzhanhao = (TextView) findViewById(R.id.my_zhanhao);
        myweixlogin = (TextView) findViewById(R.id.my_weix_login);
        myqqlogin = (TextView) findViewById(R.id.my_qq_login);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        anniu = (Button)findViewById(R.id.anniu);
        xieyi=(TextView) findViewById(R.id.xieyi);
        xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyloginActivity.this, WebViewNew.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", "file:///android_asset/clause.html");
                startActivity(intent);
            }
        });
        anniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                trim = myphone.getText().toString().trim();

                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(trim)){

                    Toast.makeText(MyloginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    //anniu1.setClickable(false);
                    anniu.setClickable(false);
//                    progressBar.setVisibility(View.VISIBLE);
                    yzregistjson();


                }
            }
        });

        myphone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0 || actionId == 3 || actionId == 5) {
                    trim = myphone.getText().toString().trim();
                 int   netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(MyloginActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        hintKbTwo();
                        trim = myphone.getText().toString().trim();
                        if(!TelNumMatch.isValidPhoneNumber(trim)){
                                    Toast.makeText(MyloginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.VISIBLE);
                            yzregistjson();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        anniu1 = (TextView)findViewById(R.id.anniu1);
        anniu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = myphone.getText().toString().trim();

                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(trim)){
                    Toast.makeText(MyloginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    anniu1.setClickable(false);
                    yzregistjson();

                }
            }
        });
//         progresslogin=(ProgressBar) findViewById(R.id.progresslogin);
        LinearLayout release_layout=(LinearLayout) findViewById(R.id.release_layout);
        release_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();

                //overridePendingTransition(0,R.anim.translate_out);
            }
        });
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

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//            /*隐藏软键盘*/
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if(inputMethodManager.isActive()){
//                inputMethodManager.hideSoftInputFromWindow(MyloginActivity.this.getCurrentFocus().getWindowToken(), 0);
//            }
//
//            trim = myphone.getText().toString().trim();
//
//            // yanzpwd.put("tel",yzphone);
//            if(!TelNumMatch.isValidPhoneNumber(trim)){
//                Toast.makeText(MyloginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//            }else{
//                //anniu1.setClickable(false);
//                yzregistjson();
//                Intent intent = new Intent(MyloginActivity.this, YanZhengMa.class);
//                startActivity(intent);
//
//            }
//
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.my_phone:

                break;
            case  R.id.my_pwd:
                break;
            case  R.id.my_long_bt:
                map=new HashMap<>();
                trim = myphone.getText().toString().trim();
                pwd = mypwd.getText().toString().trim();
                String timestamp = System.currentTimeMillis()+"";
               /* String regEx = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(pwd);
                boolean b = matcher.matches();*/

//                String checkResult = EditCheckUtils.checkEt(trim, pwd);
//                if (!checkResult.equals("")) {
//                    Toast.makeText(this, checkResult, Toast.LENGTH_LONG).show();
//                }
//                /*else if (!TelNumMatch.isValidPhoneNumber(trim)) {
//                    NormalTipDialogStyleOne();
//                }*/else  if(TelNumMatch.isMobileNO(pwd)==false){
//                    Toast.makeText(this, "密码格式不正确", Toast.LENGTH_SHORT).show();
//                }else{
//                    ProTectByMD5 md5=new ProTectByMD5();
//                    encode = md5.encode(pwd);
                    // 如果返回来的字符串为空,则进行网络请求
                        map.put("tel",trim);
                        map.put("code",pwd);
                        map.put("appid",MainActivity.feixinCode);
                        map.put("clienttype","2");
                        map.put("sessionid", sessionid);
                        map.put("accessid",MyApplication.deviceid);
                        map.put("timestamp",timestamp);
                        map.put("version", MyApplication.version);
//                    UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
                    // dialog=ProgressDialog.show(this,"Doing something","Please wait...",true);
                    // dialog = createLoadingDialog(this, "正在登录");
                    hintKbTwo();
//                    progressBar.setVisibility(View.VISIBLE);

                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("tel"+trim);
                    sort.add("code"+pwd);
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("sessionid"+sessionid);
                    sort.add("accessid"+MyApplication.deviceid);
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    sign = KeySort.keyScort(sort);
                    map.put("sign",sign);
                    map.put("timestamp",timestamp);
                    loginjson();

//                }
                break;
            case  R.id.my_pwd_tv:
                Intent intent=new Intent(MyloginActivity.this,ForgetActivity.class);
                startActivity(intent);
                break;
            case  R.id.my_zhanhao:
               /* Intent intent1=new Intent(MyloginActivity.this,RegisterCountActivity.class);
                startActivity(intent1);*/
                hintKbTwo();
                Intent intent1=new Intent(MyloginActivity.this,RegisterCountActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);
//                showFullPop();
               // overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
                break;
            case  R.id.my_weix_login:
                if(loginState == false){
                    loginState = true;
                    UIHelper.hideDialogForLoading();
                    UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
                    platform = SHARE_MEDIA.WEIXIN;
                    boolean isauth=UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN);
                    if(isauth){
                        UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.WEIXIN,authListener);
                    }else{
//                        progressBar.setVisibility(View.VISIBLE);
                        mShareAPI.doOauthVerify(this, platform, umAuthListener);
                    }

                }
                break;
            case  R.id.my_qq_login:
                if(loginState == false){
                    loginState = true;
                    UIHelper.hideDialogForLoading();
                    UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
//                    progressBar.setVisibility(View.VISIBLE);
                    platform = SHARE_MEDIA.QQ;

                    mShareAPI.doOauthVerify(this, platform, umAuthListener);
                }



              /*  if(flage.getBoolean("flags",true)){

                    edit.putBoolean("flags",false).commit();
                    Intent intent2=new Intent(MyloginActivity.this,MyqqActivity.class);
                    startActivity(intent2);
                }else {

                    Toast.makeText(this, "第二次", Toast.LENGTH_SHORT).show();
                    Intent intent2=new Intent(MyloginActivity.this,MainActivity.class);
                    startActivity(intent2);

                }*/

                break;
        }
    }
    private void showFullPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_full,
                null);
        LinearLayout layout_all;
        RelativeLayout layout_choose;
        RelativeLayout layout_photo;
        RelativeLayout layout_cancel;
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_cancel=(RelativeLayout) view.findViewById(R.id.layout_cancel);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent1=new Intent(MyloginActivity.this,YanzhenActivity.class);
                intent1.putExtra("type", "2");
                startActivity(intent1);
                mPopBottom.dismiss();
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent1=new Intent(MyloginActivity.this,YanzhenActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);

                mPopBottom.dismiss();
            }
        });
        layout_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mPopBottom.dismiss();
            }
        });
        mPopBottom = new PopupWindow(view);
        mPopBottom.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setTouchable(true);
        mPopBottom.setFocusable(true);
        mPopBottom.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        mPopBottom.setBackgroundDrawable(dw);
        // 动画效果 从底部弹起
//        dialogBootom2UpAnimation
        mPopBottom.setAnimationStyle(R.style.Animations_GrowFromBottom);
        mPopBottom.showAtLocation(myzhanhao, Gravity.BOTTOM, 0, 0);//parent view随意
    }

    private void NormalTipDialogStyleOne() {
        final NormalTipDialog dialog = new NormalTipDialog(MyloginActivity.this);
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


    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            progressBar.setVisibility(View.VISIBLE);
            UIHelper.hideDialogForLoading();
            UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
            Toast.makeText(MyloginActivity.this, "登录中...", Toast.LENGTH_LONG).show();
            mShareAPI.doOauthVerify(MyloginActivity.this, platform, umAuthListener);
//            notifyDataSetChanged();
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UIHelper.hideDialogForLoading();

            Toast.makeText(MyloginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UIHelper.hideDialogForLoading();
            Toast.makeText(MyloginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    private UMAuthListener umAuthListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> info) {
            // 获取uid

            if(platform == SHARE_MEDIA.QQ){
                String uid = info.get("uid");
                if (!TextUtils.isEmpty(uid)) {
                    // uid不为空，获取用户信息
                        getUserInfo(platform);
                } else {
                    Toast.makeText(MyloginActivity.this, "授权失败...",
                            Toast.LENGTH_LONG).show();
                }
            }else if(platform == SHARE_MEDIA.WEIXIN){
//                Log.d("lizisong","info："+info.toString());
                 String unionid = info.get("unionid");
//                 Log.d("lizisong", "unionid:--------"+unionid);
                 if(!TextUtils.isEmpty(unionid)){
                    getwxinfo(platform);
                 }
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UIHelper.hideDialogForLoading();
            Toast.makeText( getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            loginState = false;
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UIHelper.hideDialogForLoading();
            Toast.makeText( getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
            loginState = false;
        }
    };

    private void getUserInfo(SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(MyloginActivity.this, platform, new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if(share_media == SHARE_MEDIA.QQ){
                    if(i == 2 && map != null){
                       // flage = getSharedPreferences("flags", Context.MODE_PRIVATE);
                      //  SharedPreferences.Editor edit = flage.edit();
                       // if (flage.getBoolean("flags", true)) {
                            //  edit.putBoolean("flags", false).commit();
                            if (map.get("profile_image_url").toString() != null) {
                                spf = getSharedPreferences("logn", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = spf.edit();
                                edt.putString("uername", map.get("screen_name").toString());
                                edt.putString("uerimg", map.get("profile_image_url").toString());
                                edt.putString("openid", map.get("openid").toString());
                                edt.putString("gender", map.get("gender").toString());
    //                            edt.putString("uid", map.get("uid").toString());
                                edt.putBoolean("flag", true);
                                edt.commit();
                                String uername = spf.getString("uername", "");
                                String opend=spf.getString("openid","");
                                String uid = spf.getString("uid", "");
                                String imgUrl =spf.getString("uerimg","");
                                String sex = spf.getString("gender","");
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();

                                hashmap=new HashMap<>();
                                hashmap.put("nickname",uername);
                                hashmap.put("openid",opend );
                                hashmap.put("img", imgUrl);
                                hashmap.put("type", "qq");
                                hashmap.put("appid",MainActivity.feixinCode);
                                hashmap.put("clienttype","2");
//                                Log.d("lizisong", "登录发送的信息:"+hashmap.toString());
//                                UIHelper.hideDialogForLoading();
//                                hashmap.put("sex",sex);
//                                progressBar.setVisibility(View.GONE);
                                UIHelper.hideDialogForLoading();
                                UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
//                                progressBar.setVisibility();
//                                progressBar.setVisibility(View.VISIBLE);
                                sort.add("nickname"+uername);
                                sort.add("openid"+opend);
                                sort.add("img"+imgUrl);
                                sort.add("type"+"qq");
                                sort.add("appid"+MainActivity.feixinCode);
                                sort.add("clienttype"+"2");
                                sort.add("timestamp"+timestamp);
                                sign = KeySort.keyScort(sort);
                                hashmap.put("sign",sign);
                                hashmap.put("timestamp",timestamp);
                                getjson();
                            }
                }
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                UIHelper.hideDialogForLoading();
            }
        });
    }
    private void getwxinfo(SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(MyloginActivity.this, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                if (map.get("profile_image_url").toString() != null) {
                    Log.d("lizisong", "profile_image_url:--------"+map.get("profile_image_url").toString());
                    spf = getSharedPreferences("logn", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = spf.edit();
                    edt.putString("nickname", map.get("screen_name").toString());
                    edt.putString("uerimg", map.get("profile_image_url").toString());
                    edt.putString("openid", map.get("openid").toString());

                    //                            edt.putString("uid", map.get("uid").toString());
                    edt.putBoolean("flag", true);
                    edt.commit();
                    String uername = spf.getString("nickname", "");
                    String opend=spf.getString("openid","");
                    String imgUrl =spf.getString("uerimg","");
                    String sex = spf.getString("gender","");
//                    Intent intent=new Intent(MyloginActivity.this,RelationPhone.class);
//                    intent.putExtra("uername",uername);
//                    intent.putExtra("openid",opend);
//                    intent.putExtra("uerimg",imgUrl);
//                    intent.putExtra("type","weixin");
//                    intent.putExtra("appid",MainActivity.feixinCode);
//                    intent.putExtra("clienttype","2");
//                    startActivity(intent);
//                    MyloginActivity.this.finish();

                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    hashmap=new HashMap<>();
                    hashmap.put("nickname",uername);

                    hashmap.put("openid",opend );
                    hashmap.put("img", imgUrl);
                    hashmap.put("type", "weixin");
                    hashmap.put("appid",MainActivity.feixinCode);
                    hashmap.put("clienttype","2");
                    hashmap.put("version", MyApplication.version);
//                    Log.d("lizisong", "登录发送的信息:"+hashmap.toString());
                    UIHelper.hideDialogForLoading();
                    UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
//                    progressBar.setVisibility(View.VISIBLE);
                    sort.add("nickname"+uername);
                    sort.add("openid"+opend);
                    sort.add("img"+imgUrl);
                    sort.add("type"+"weixin");
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    sign = KeySort.keyScort(sort);
                    hashmap.put("sign",sign);
                    hashmap.put("timestamp",timestamp);
                    getjson();
                }
                    //   }else{
                    //   setResult(2, getIntent());
                    //  MyloginActivity.this.finish();
                    //  }
                }


            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);

       // UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);

    }
    public void getjson() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //String str="http://123.207.164.210/api/thirdLogin.php";
                   String str= "http://"+ips+"/api/thirdLogin_new.php";
//                    Log.d("lizisong", "hashmap:"+hashmap.toString());
                    qqlogin = OkHttpUtils.post(str, hashmap);
//                    Log.d("lizisong", "returnlogin:"+qqlogin);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录");
        MobclickAgent.onResume(this);
        if(isClose){
            finish();
        }
        anniu.setClickable(true);
        myphone.setText("");
        mypwd.setText("");
        XGPushManager.onActivityStarted(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录");
        MobclickAgent.onPause(this);
        XGPushManager.onActivityStoped(this);
        UIHelper.hideDialogForLoading();
    }


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

            sign = KeySort.keyScort(sort);

            quxiaojson = OkHttpUtils.loaudstringfromurl("http://"+ips+"/api/arc_store.php?mid=" + mid + "&method=list&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version);
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
                                    if(pos.typename.equals("人才") || pos.typename.equals("专家")){
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
            sign=KeySort.keyScort(sort);
            String url = "http://"+ips+"/api/getAreaCateInfo.php?action=select&mid="+mid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version;
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
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            String timestamp = System.currentTimeMillis()+"";
            String sign="";
            ArrayList<String> sort = new ArrayList<String>();
            sort.add("method"+"select");
            sort.add("mid"+mid);
            sort.add("timestamp"+timestamp);
            sort.add("version"+MyApplication.version);
            sign = KeySort.keyScort(sort);
            String url = "http://"+ips+"/api/appointment.php?method=select&mid="+mid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version;

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

    HashMap<String,String> registmap;
    String registlogin;
    private void yzregistjson() {

        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //123.206.8.208/api/sms_interface.php
                    String timestamp = System.currentTimeMillis()/1000+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();

                    registmap = new HashMap<String, String>();
                    registmap.put("tel",trim);
                    registmap.put("action","send");
                    registmap.put("appid",MainActivity.feixinCode);
                    registmap.put("clienttype","2");
                    registmap.put("timestamp",timestamp+"");
                    registmap.put("version", MyApplication.version);
                    sort.add("tel"+trim);
                    sort.add("action"+"send");
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    sign = KeySort.keyScort(sort);
                    registmap.put("sign", sign);
                   String yzregiststr="http://"+ips+"/api/user_register_login.php";
                    registlogin = OkHttpUtils.postkeyvlauspainr(yzregiststr,registmap);
                    if(registlogin != null){
                        Message msg = Message.obtain();
                        msg.what = 10;
                        handler.sendMessage(msg);

                    }
                }
            }.start();
        }catch (Exception e){}

    }
    private void qiyejson(){
        String url = "http://erp.zhongkechuangxiang.com/api/Product/MPList";
        HashMap<String, String > map = new HashMap<>();
        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,13,0);
    }

}

