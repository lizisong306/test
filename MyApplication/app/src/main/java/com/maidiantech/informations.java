package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.MaiDianYuYue;
import entity.RetPulseData;
import entity.YuYueData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/27.
 */

public class informations extends AutoLayoutActivity implements View.OnClickListener {
    EditText infomen;
    EditText inforiphone;
    EditText inforxiaoxi;
    Button inforfinish;
    Button infornice;
    String info = "";
    String phone = "";
    String xiaoxi = "";
    String aid = "";
    String mid = "";
    String pic = "";
    String click = "";
    String body = "";
    String area = "";
    String LoginFlag = "";
    String funciton="";
    OkHttpUtils okHttpUtils;
    String ret;
    MaiDianYuYue maiDianYuYue;
    HashMap<String, String> map = new HashMap<String, String>();
    String typeid="";
    String title = "";
    String model = "";
    private  String   ips;
    private ImageView  xm_img,yue_back;
    private TextView xm_title,xm_linyu,xm_description,xm_look;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_dialog);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Intent intent = getIntent();
        aid = intent.getStringExtra("aid");
        pic = intent.getStringExtra("pic");
        typeid = intent.getStringExtra("typeid");
        model = intent.getStringExtra("model");
        title = intent.getStringExtra("title");
        click=intent.getStringExtra("click");
        body=intent.getStringExtra("body");
        area=intent.getStringExtra("area");
        funciton=intent.getStringExtra("funciton");
        maiDianYuYue = MaiDianYuYue.getInstance(this);
        initView();
        yue_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
            }
        });
        infomen.setOnClickListener(this);
        inforiphone.setOnClickListener(this);
        inforxiaoxi.setOnClickListener(this);
//        inforfinish.setOnClickListener(this);
        infornice.setOnClickListener(this);
    }

    private void initView() {
        infomen = (EditText) findViewById(R.id.info_men);
        inforiphone = (EditText) findViewById(R.id.infor_iphone);
        inforxiaoxi = (EditText) findViewById(R.id.infor_xiaoxi);
//        inforfinish = (Button) findViewById(R.id.infor_finish);
        yue_back=(ImageView) findViewById(R.id.yue_back);
        infornice = (Button) findViewById(R.id.infor_nice);
        xm_img=(ImageView) findViewById(R.id.xm_img);
        xm_title=(TextView) findViewById(R.id.xm_title);
        xm_linyu=(TextView) findViewById(R.id.xm_linyu);
        xm_description=(TextView) findViewById(R.id.xm_description);
        xm_look=(TextView) findViewById(R.id.xm_look);
        xm_title.setText(title);
        xm_linyu.setText(area);
        xm_look.setText(click);
        options = ImageLoaderUtils.initOptions();
        ImageLoader.getInstance().displayImage(pic, xm_img, options);
        xm_description.setText(body);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info_men:
                break;
            case R.id.infor_iphone:
                break;
            case R.id.infor_xiaoxi:
                break;
            case R.id.infor_finish:
//                finish();
                break;
            case R.id.infor_nice:
                mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                LoginFlag = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_FLAY, "0");
                info = infomen.getText().toString();
                phone = inforiphone.getText().toString();
                xiaoxi = inforxiaoxi.getText().toString();
                if((info == null || info.equals("")) || (phone == null || phone.equals(""))
                        || (xiaoxi == null || xiaoxi.equals(""))){
                    Toast.makeText(informations.this, "提交数据不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }else  if(!TelNumMatch.isValidPhoneNumber(phone)){
                    Toast.makeText(informations.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("method"+"add");
                sort.add("aid"+aid);
                sort.add("mid"+mid);
                sort.add("loginFlag"+LoginFlag);
                sort.add("des"+ xiaoxi);
                sort.add("mobile"+ phone);
                sort.add("meetname"+ info);
                sort.add("typeid"+ typeid);
                sort.add("version"+MyApplication.version);

                sort.add("timestamp"+ timestamp);
                String accessid="";
                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(loginState.equals("1")){
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                    accessid = mid;
                }else{
                    accessid = MyApplication.deviceid;
                }
                sort.add("accessid"+accessid);
                sign = KeySort.keyScort(sort);
                map.put("method","add");
                map.put("aid", aid);
                map.put("mid", mid);
                map.put("loginFlag",LoginFlag);
                map.put("des", xiaoxi);
                map.put("mobile", phone);
                map.put("meetname", info);
                map.put("typeid", typeid);
                map.put("timestamp",timestamp);
                map.put("sign",sign);
                map.put("version", MyApplication.version);
                map.put("accessid",accessid);
                ips = MyApplication.ip;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://"+ips+"/api/appointment.php";
                        try {
                            ret =  okHttpUtils.post(url, map);
                            if(ret != null){
                                Message msg = Message.obtain();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }catch (Exception e){

                        }
                    }
                }).start();
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson g=new Gson();
                RetPulseData result =g.fromJson(ret, RetPulseData.class);
                if(result.code.equals("1")){
                   Toast.makeText(informations.this, result.message, Toast.LENGTH_SHORT).show();
                    YuYueData yueData = new YuYueData();
                    yueData.pic = pic;
                    yueData.typeid = typeid;
                    yueData.upFlag = 1;
                    yueData.meetTel = phone;
                    yueData.aid = aid;
                    yueData.mid = mid;
                    yueData.meetPost = xiaoxi;
                    yueData.meetMen = info;
                    yueData.title = title;
                    yueData.model = model;
                    yueData.meetAdress = result.data.id;
                    yueData.update = System.currentTimeMillis()+"";
                    maiDianYuYue.insert(yueData);
                    DetailsActivity.yuyueState=true;
                    finish();
                }else{
                    Toast.makeText(informations.this, result.message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

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
