package com.maidiantech;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalTipDialog;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Util.EditCheckUtils;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.ProTectByMD5;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.SmsRegisData;
import entity.Usercode;
import entity.userlogin;
import fragment.WelcomePulse;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/9.
 */
public class RegisterCountActivity extends AutoLayoutActivity {

    EditText registerpwd;
    EditText registername;
    EditText code_name;
    Button registernexts;
    CheckBox checkBox;
    ImageView zhuce_back;
    String phoneNumber;
    OkHttpUtils utils;
    TextView maidiankeji;
    String smsCode;
    String fromSMSCode;
    Thread thread;

    boolean isState = false;
    String yzphone;
    TextView getCode;
    private int million = 60;
    private HashMap<String,String> registmap = new HashMap<>();
    private  String   ips;
    private String registlogin;
    public static final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    SMSBroadcastReceiver smsBroadCast;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                if (million > 0) {
                    getCode.setClickable(false);
                    million--;
                    getCode.setTextSize(15);
                    getCode.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1000);
                }
                if (million == 0) {
                    getCode.setClickable(true);
                    getCode.setText("获取验证码");
                    getCode.setTextColor(Color.parseColor("#ffffff"));
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
                        Log.d("lizisong", "smsCode:"+smsCode);
                        setSendBt();
                    }else{
                        Toast.makeText(RegisterCountActivity.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JsonIOException e){

                }

            }

            if(msg.what==1){
                Gson g=new Gson();
                Usercode  registcode = g.fromJson(registlogin, Usercode.class);
                if(registcode.getCode() == 1) {
                    Toast.makeText(RegisterCountActivity.this, registcode.getMessage(), Toast.LENGTH_SHORT).show();
                    final userlogin data = registcode.getData();

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, registcode.getCode() + "");
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
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, data.getLoginFlag());
                    SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);

                    SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);

                    WelcomePulse.LoginState = true;
                    MyloginActivity.isClose = true;
                    Intent intent = new Intent(RegisterCountActivity.this, XingquActivity.class);
                    intent.putExtra("id","0");
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterCountActivity.this, registcode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_register);
        isState = false;
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        phoneNumber = getIntent().getStringExtra("phone");
        initView();
        smsBroadCast = new SMSBroadcastReceiver();
        thread = new Thread();
    }
    private void registjson() {
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                        String registstr="http://"+ips+"/api/register.php";
                        registlogin = OkHttpUtils.postkeyvlauspainr(registstr, registmap);
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);


                }
            }.start();
        }catch (Exception e){}
    }
    private void initView() {
//        registerphone = (EditText) findViewById(R.id.register_phone);
        registerpwd = (EditText) findViewById(R.id.register_pwd);
        registername = (EditText) findViewById(R.id.register_name);
        registernexts = (Button) findViewById(R.id.register_nexts);
        zhuce_back=(ImageView) findViewById(R.id.zhuce_back);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        maidiankeji=(TextView) findViewById(R.id.maidiankeji);
        getCode  = (TextView)findViewById(R.id.get_code);
        code_name = (EditText)findViewById(R.id.code_name);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yzphone = registername.getText().toString();
                if(yzphone != null){
                    yzphone = yzphone.trim();
                }
                // yanzpwd.put("tel",yzphone);
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(RegisterCountActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{
                    yzregistjson();
                }
            }
        });
        checkBox.setChecked(true);
        zhuce_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(R.anim.translate_out,R.anim.translate_in);;
            }
        });
        maidiankeji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RegisterCountActivity.this, ServiceProvisionAcitivty.class);
//                intent.putExtra("res_id",R.string.html_text );
//                startActivity(intent);
                Intent intent=new Intent(RegisterCountActivity.this, WebViewActivity.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", "file:///android_asset/clause.html");
                startActivity(intent);
            }
        });
        registernexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String registphone = registerphone.getText().toString().trim();
                boolean state = checkBox.isChecked();
                if(!checkBox.isChecked()){
                    Toast.makeText(RegisterCountActivity.this, "没有同意钛领服务条款", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isState){
                    fromSMSCode =  code_name.getText().toString();
                    if(fromSMSCode != null){
                        if(fromSMSCode.trim().equals(smsCode)){
                            isState = true;
                        }
                    }
                }
                if(!isState){
                    Toast.makeText(RegisterCountActivity.this, "请核对验证码是否一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                String registpwd = registerpwd.getText().toString().trim();
                String registname = registername.getText().toString().trim();
                String checkResult = EditCheckUtils.regist(registpwd,registname);
                if (!checkResult.equals("")) {
                    Toast.makeText(RegisterCountActivity.this, checkResult, Toast.LENGTH_SHORT).show();
                }else if(TelNumMatch.isMobileNO(registpwd)==false){
                    Toast.makeText(RegisterCountActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                }else{
                    ProTectByMD5 md5 = new ProTectByMD5();
                    String encode = md5.encode(registpwd);

                    registmap.put("pwd", encode);
                    registmap.put("userid", registname);
                    registmap.put("mtype", "1");
                    registmap.put("tel", yzphone);
                    registmap.put("appid",MainActivity.feixinCode);
                    registmap.put("clienttype","2");
                    registmap.put("version", MyApplication.version);

                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("pwd"+encode);
                    sort.add("userid"+registname);
                    sort.add("mtype"+"1");
                    sort.add("tel"+yzphone);
                    sort.add("appid"+MainActivity.feixinCode);
                    sort.add("clienttype"+"2");
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
                    registmap.put("sign",sign);
                    registmap.put("timestamp",timestamp);
                    registmap.put("accessid",accessid);
                    registjson();

                }
            }
        });
    }
    private void NormalTipDialogStyleOne() {
        final NormalTipDialog dialog = new NormalTipDialog(RegisterCountActivity.this);
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
    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
        if(Build.VERSION.SDK_INT < 20){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SMS);
            registerReceiver(smsBroadCast,intentFilter);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        if(Build.VERSION.SDK_INT < 20){
            unregisterReceiver(smsBroadCast);
        }
    }

    /**
     * 规则1：至少包含大小写字母及数字中的一种
     * 是否包含
     *
     * @param str
     * @return
     */
    public  boolean isLetterOrDigit(String str) {
        boolean isLetterOrDigit = false;//定义一个boolean值，用来表示是否包含字母或数字
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetterOrDigit = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isLetterOrDigit && str.matches(regex);
        return isRight;
    }


    private void yzregistjson() {

        try {

             new Thread(){
                @Override
                public void run() {
                    super.run();
                    MyApplication.setAccessid();
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("tel"+yzphone);
                    sort.add("flag"+"1");
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

                   String yzregiststr="http://"+ips+"/api/sms_interface.php?tel="+yzphone+"&flag="+1+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                    registlogin=  OkHttpUtils.getInstancesOkHttp().Myokhttpclient(yzregiststr);
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
        getCode.setClickable(false);
        getCode.setText("60秒内输入");
        getCode.setTextSize(13);
        getCode.setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }


    class SMSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_SMS)){
                Bundle bundle = intent.getExtras();
                SmsMessage msg = null;
                String content = null;
                if (null != bundle) {
                    Object[] smsObj = (Object[]) bundle.get("pdus");
                    for (Object object : smsObj) {
                        msg = SmsMessage.createFromPdu((byte[]) object);
                        String num = msg.getOriginatingAddress();
                        content =  msg.getMessageBody();
                        if(content.contains("验证码")){
                            Pattern p = Pattern.compile("[^0-9]");
                            Matcher m = p.matcher(content);
                            fromSMSCode =m.replaceAll("");

                            fromSMSCode = fromSMSCode.substring(0,fromSMSCode.length()-1);
                            if(fromSMSCode.equals(smsCode)){
                                code_name.setText(fromSMSCode);
                                isState = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
