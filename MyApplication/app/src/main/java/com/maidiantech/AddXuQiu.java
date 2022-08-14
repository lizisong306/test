package com.maidiantech;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Posts;
import entity.Ret;
import entity.RetPulseData;
import view.BTAlertDialog;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/3.
 */

public class AddXuQiu extends AutoLayoutActivity {
    ImageView back;
    EditText text;
    TextView ShowCount;
    TextView addXiamgmu, addrencai, addshebei;
    LinearLayout addxiangmline,addrencailine,addshebeiline;
    LinearLayout xiangmu, rencai, shebei;
    ImageView xm_img,device_img;
    RoundImageView rc_img;
    TextView xm_title,xm_linyu,xm_description,xm_look;
    TextView device_title,device_linyu,device_description,device_look;
    TextView rc_uname,rc_linyu,rc_title;
    TextView close_txt;
    TextView tijiao;
    RelativeLayout close;
    String json;
    OkHttpUtils okHttpUtils;
    private ProgressBar progress;
    public static  Posts data = null;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addxuqiu);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        state = getIntent().getStringExtra("state");
        if(state != null){
            if(state.equals("1")){

            }
        }else {
            data = null;
        }

        back = (ImageView)findViewById(R.id.shezhi_backs);
        ShowCount = (TextView)findViewById(R.id.count);
        addXiamgmu = (TextView)findViewById(R.id.addxiangm);
        addrencai = (TextView)findViewById(R.id.addrencai);
        addshebei = (TextView)findViewById(R.id.addshebei);
        addxiangmline = (LinearLayout)findViewById(R.id.addxiangmline);
        addrencailine = (LinearLayout)findViewById(R.id.addrencailine);
        addshebeiline = (LinearLayout)findViewById(R.id.addshebeiline);

        xiangmu = (LinearLayout)findViewById(R.id.xm_layout);
        xm_img  = (ImageView)findViewById(R.id.xm_img);
        xm_title = (TextView)findViewById(R.id.xm_title);
        xm_linyu = (TextView)findViewById(R.id.xm_linyu);
        xm_description = (TextView)findViewById(R.id.xm_description);
        xm_look = (TextView)findViewById(R.id.xm_look);
        rencai  = (LinearLayout)findViewById(R.id.rc_layout);
        rc_img  = (RoundImageView)findViewById(R.id.rc_img);
        rc_uname = (TextView)findViewById(R.id.rc_uname);
        rc_linyu = (TextView)findViewById(R.id.rc_linyu);
        rc_title = (TextView)findViewById(R.id.rc_title);
        close_txt = (TextView)findViewById(R.id.close_txt);
        close = (RelativeLayout)findViewById(R.id.close);
        close.setVisibility(View.GONE);
        shebei  = (LinearLayout)findViewById(R.id.device_layout);
        device_img = (ImageView)findViewById(R.id.device_img);
        device_title = (TextView)findViewById(R.id.device_title);
        device_linyu = (TextView)findViewById(R.id.device_linyu);
        device_description = (TextView)findViewById(R.id.device_description);
        device_look = (TextView)findViewById(R.id.device_look);
        tijiao = (TextView)findViewById(R.id.tijiao);
        progress = (ProgressBar)findViewById(R.id.progress);
        okHttpUtils = OkHttpUtils.getInstancesOkHttp();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text = (EditText)findViewById(R.id.text);
        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
        text.addTextChangedListener(new  TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ShowCount.setText(s.toString().length()+"/140字");
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BTAlertDialog dialog = new BTAlertDialog(AddXuQiu.this);
                dialog.setTitle("确实要删除选择内容？");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data = null;
                        addxiangmline.setBackgroundColor(0xffF1F0F6);
                        addrencailine.setBackgroundColor(0xffF1F0F6);
                        addshebeiline.setBackgroundColor(0xffF1F0F6);
                        xiangmu.setVisibility(View.GONE);
                        rencai.setVisibility(View.GONE);
                        shebei.setVisibility(View.GONE);
                        close.setVisibility(View.GONE);
                    }});
                dialog.show();



            }
        });

        addXiamgmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shebei.getVisibility() == View.VISIBLE || rencai.getVisibility() == View.VISIBLE || xiangmu.getVisibility() == View.VISIBLE){
                    final BTAlertDialog dialog = new BTAlertDialog(AddXuQiu.this);
                    dialog.setTitle("选择新资源会替换已选择资源\n" +
                            "是否继续选择？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("继续", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        updateFromWeb(grade.data.down_link);
//                            data = null;
                            addxiangmline.setBackgroundColor(0xffF1F0F6);
                            addrencailine.setBackgroundColor(0xffF1F0F6);
                            addshebeiline.setBackgroundColor(0xffF1F0F6);
                            xiangmu.setVisibility(View.GONE);
                            rencai.setVisibility(View.GONE);
                            shebei.setVisibility(View.GONE);
                            close.setVisibility(View.GONE);
                            addxiangmline.setBackgroundColor(Color.WHITE);
                            addrencailine.setBackgroundColor(0xffF1F0F6);
                            addshebeiline.setBackgroundColor(0xffF1F0F6);
                            Intent intent = new Intent(AddXuQiu.this, AddProject.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    addxiangmline.setBackgroundColor(Color.WHITE);
                    addrencailine.setBackgroundColor(0xffF1F0F6);
                    addshebeiline.setBackgroundColor(0xffF1F0F6);
                    Intent intent = new Intent(AddXuQiu.this, AddProject.class);
                    startActivity(intent);
                }



            }
        });

        addrencai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shebei.getVisibility() == View.VISIBLE || rencai.getVisibility() == View.VISIBLE || xiangmu.getVisibility() == View.VISIBLE){
                    final BTAlertDialog dialog = new BTAlertDialog(AddXuQiu.this);
                    dialog.setTitle("选择新资源会替换已选择资源\n" +
                            "是否继续选择？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("继续", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        updateFromWeb(grade.data.down_link);
//                            data = null;
                            addxiangmline.setBackgroundColor(0xffF1F0F6);
                            addrencailine.setBackgroundColor(0xffF1F0F6);
                            addshebeiline.setBackgroundColor(0xffF1F0F6);
                            xiangmu.setVisibility(View.GONE);
                            rencai.setVisibility(View.GONE);
                            shebei.setVisibility(View.GONE);
                            close.setVisibility(View.GONE);
                            addrencailine.setBackgroundColor(Color.WHITE);
                            addshebeiline.setBackgroundColor(0xffF1F0F6);
                            addxiangmline.setBackgroundColor(0xffF1F0F6);
                            Intent intent = new Intent(AddXuQiu.this, AddRencai.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }else{
                    addrencailine.setBackgroundColor(Color.WHITE);
                    addshebeiline.setBackgroundColor(0xffF1F0F6);
                    addxiangmline.setBackgroundColor(0xffF1F0F6);
                    Intent intent = new Intent(AddXuQiu.this, AddRencai.class);
                    startActivity(intent);
                }

            }
        });

        addshebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shebei.getVisibility() == View.VISIBLE || rencai.getVisibility() == View.VISIBLE || xiangmu.getVisibility() == View.VISIBLE){
                    final BTAlertDialog dialog = new BTAlertDialog(AddXuQiu.this);
                    dialog.setTitle("选择新资源会替换已选择资源\n" +
                            "是否继续选择？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("继续", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            data = null;
                            addxiangmline.setBackgroundColor(0xffF1F0F6);
                            addrencailine.setBackgroundColor(0xffF1F0F6);
                            addshebeiline.setBackgroundColor(0xffF1F0F6);
                            xiangmu.setVisibility(View.GONE);
                            rencai.setVisibility(View.GONE);
                            shebei.setVisibility(View.GONE);
                            close.setVisibility(View.GONE);
                            addshebeiline.setBackgroundColor(Color.WHITE);
                            addrencailine.setBackgroundColor(0xffF1F0F6);
                            addxiangmline.setBackgroundColor(0xffF1F0F6);
                            Intent intent = new Intent(AddXuQiu.this, AddSheBei.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }else{
                    addshebeiline.setBackgroundColor(Color.WHITE);
                    addrencailine.setBackgroundColor(0xffF1F0F6);
                    addxiangmline.setBackgroundColor(0xffF1F0F6);
                    Intent intent = new Intent(AddXuQiu.this, AddSheBei.class);
                    startActivity(intent);
                }

            }
        });
        tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = text.getText().toString();

                if(txt == null || txt.equals("")){
                    Toast.makeText(AddXuQiu.this, "请填写约见的主题及诉求",Toast.LENGTH_SHORT).show();
                }else{
                    progress.setVisibility(View.VISIBLE);
                    tijiaopost();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(data != null){
                if(data.typeid.equals("2")){
                    close.setVisibility(View.VISIBLE);
                    xiangmu.setVisibility(View.VISIBLE);
                    rencai.setVisibility(View.GONE);
                    shebei.setVisibility(View.GONE);
                    addxiangmline.setBackgroundColor(Color.WHITE);
                    addrencailine.setBackgroundColor(0xffF1F0F6);
                    addshebeiline.setBackgroundColor(0xffF1F0F6);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(data.getLitpic()
                                    , xm_img, options);
                        } else {
                            xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                , xm_img, options);
                    }
                    if(data.getLitpic()==null || data.getLitpic().equals("")){
                        xm_img.setVisibility(View.GONE);
                    }else{
                        xm_img.setVisibility(View.VISIBLE);
                    }
                    xm_title.setText(data.getTitle());
                    if(data.getArea_cate()!=null){
                        xm_linyu.setText(data.getArea_cate().getArea_cate1());
                    }
                    if(data.getDescription()==null || data.getDescription().equals("")){
                        xm_description.setVisibility(View.GONE);
                    }else{
                        xm_description.setVisibility(View.VISIBLE);
                        xm_description.setText(Html.fromHtml(data.getDescription()));
                    }
                    xm_look.setVisibility(View.GONE);
                }else if(data.typeid.equals("4")){
                    close.setVisibility(View.VISIBLE);
                    xiangmu.setVisibility(View.GONE);
                    rencai.setVisibility(View.VISIBLE);
                    shebei.setVisibility(View.GONE);
                    addrencailine.setBackgroundColor(Color.WHITE);
                    addxiangmline.setBackgroundColor(0xffF1F0F6);
                    addshebeiline.setBackgroundColor(0xffF1F0F6);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(data.getLitpic()
                                    , rc_img, options);
                        } else {
                            rc_img.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                , rc_img, options);
                    }
                    if(data.getLitpic()==null || data.getLitpic().equals("")){
                        rc_img.setVisibility(View.GONE);
                    }else{
                        rc_img.setVisibility(View.VISIBLE);
                    }
                    rc_uname.setText(data.getUsername());
                    rc_title.setText(data.getDescription());
                    try {
                        rc_linyu.setText(data.getArea_cate().getArea_cate1());
                    }catch (Exception e){

                    }
                }else if(data.typeid.equals("7")){
                    close.setVisibility(View.VISIBLE);
                    xiangmu.setVisibility(View.GONE);
                    rencai.setVisibility(View.GONE);
                    shebei.setVisibility(View.VISIBLE);
                    addshebeiline.setBackgroundColor(Color.WHITE);
                    addrencailine.setBackgroundColor(0xffF1F0F6);
                    addxiangmline.setBackgroundColor(0xffF1F0F6);

                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(data.image.image1
                                    , device_img, options);
                        } else {
                            device_img.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(data.image.image1
                                , device_img, options);
                    }
                    if(data.image.image1==null || data.image.image1.equals("")){
                        device_img.setVisibility(View.GONE);
                    }else{
                        device_img.setVisibility(View.VISIBLE);
                    }

                    device_title.setText(data.getTitle());
                    if(data.getArea_cate()!=null){
                        device_linyu.setText(data.getArea_cate().getArea_cate1());
                    }
                    if(data.getDescription()==null || data.getDescription().equals("")){
                        device_description.setVisibility(View.GONE);
                    }else{
                        device_description.setVisibility(View.VISIBLE);
                        device_description.setText(data.getDescription());
                    }
                    device_look.setVisibility(View.GONE);

                }
                if(shebei.getVisibility() == View.VISIBLE || rencai.getVisibility() == View.VISIBLE || xiangmu.getVisibility() == View.VISIBLE){
                    close.setVisibility(View.VISIBLE);
                }else{
                    close.setVisibility(View.GONE);
                }
            }else {
                addshebeiline.setBackgroundColor(0xffF1F0F6);
                addrencailine.setBackgroundColor(0xffF1F0F6);
                addxiangmline.setBackgroundColor(0xffF1F0F6);
            }
        }catch (Exception e){

        }

    }
    private void tijiaopost(){
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        String  mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", mid);
        map.put("method","add");
        if(data != null){
            map.put("aid", data.getId());
            if(data.typeid.equals("4")) {
                map.put("typeid", "4");
                map.put("entryaddress", "7");
            }else if(data.typeid.equals("2")){
                map.put("typeid", "2");
                map.put("entryaddress", "8");
            }else if(data.typeid.equals("7")){
                map.put("typeid", "7");
                map.put("entryaddress", "9");
            }
        }else{
            map.put("aid", "0");
            map.put("typeid", "0");
            map.put("entryaddress", "6");
        }
        map.put("content",text.getText().toString());
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,1,0);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    progress.setVisibility(View.GONE);
                    json = (String)msg.obj;
                    Gson g=new Gson();
                    Ret result =g.fromJson(json, Ret.class);
                    if(result.code.equals("1")){
                        Toast.makeText(AddXuQiu.this, result.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddXuQiu.this, XuQiuSendSuccess.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){

                }

            }
        }
    };
}
