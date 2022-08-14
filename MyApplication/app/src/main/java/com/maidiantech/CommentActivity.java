package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import Util.TimeUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Flow;
import entity.Ret;
import view.RefreshListView;
import view.RoundImageView;
import view.RoundImageView2;
import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by lizisong on 2017/9/13.
 */

public class CommentActivity extends AutoLayoutActivity {
    ImageView backs;
    ProgressBar progress;
    ImageView xm_img;
    RoundImageView rc_img;
    RoundImageView2 rc_img1;
    TextView xm_title,xm_rank,xm_linyu,commit,count1,adress_content,adress_time,shijijiage,yuanjia;
    ImageView bumanyi,yiban,manyi,feichangmanyi;
    EditText pinglun;
    Intent intent;
    String typeid,img,title,rank,linyu,time,adress, resource_id,symbol_id="0";
    DisplayImageOptions options;
    RelativeLayout adress_lay;

    AnimationDrawable animationDrawable1,animationDrawable2,animationDrawable3,animationDrawable4;
    double x,y;
    HashMap<String,String> map =new HashMap<>();
    String ret;
    public static Flow item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//                getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
////            window.setNavigationBarColor(Color.WHITE);
//
//            options = ImageLoaderUtils.initOptions();
//            StyleUtils.initSystemBar(this);
////        //设置状态栏是否沉浸
//            StyleUtils.setStyle(this);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
//                setTranslucentStatus(true);
//                SystemBarTintManager tintManager = new SystemBarTintManager(this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//                tintManager.setStatusBarAlpha(0);
//            }
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
//            MIUISetStatusBarLightMode(getWindow(), true);
//        }

//        getWindow().setSoftInputMode
//                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
//                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.comment_heart);
                    StyleUtils.initSystemBar(this);
////        //设置状态栏是否沉浸
            StyleUtils.setStyle(this);
        intent = getIntent();
        options = ImageLoaderUtils.initOptions();
        backs = (ImageView)findViewById(R.id.shezhi_backs);
        progress = (ProgressBar)findViewById(R.id.progress);
        xm_img = (ImageView)findViewById(R.id.xm_img);
        rc_img = (RoundImageView)findViewById(R.id.rc_img);
        xm_title = (TextView) findViewById(R.id.xm_title);
        xm_rank = (TextView)findViewById(R.id.xm_rank) ;
        xm_linyu = (TextView)findViewById(R.id.xm_linyu);
        adress_time = (TextView)findViewById(R.id.adress_time);
        adress_lay = (RelativeLayout)findViewById(R.id.adress_lay);
        adress_content = (TextView)findViewById(R.id.adress_content);
        count1 = (TextView)findViewById(R.id.count);
        shijijiage = (TextView)findViewById(R.id.shijijiage);
        yuanjia = (TextView)findViewById(R.id.yuanjia);
        rc_img1 = (RoundImageView2)findViewById(R.id.rc_img1);
        adress_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(CommentActivity.this, BaseMapDemo.class);
                intent.putExtra("y", y);
                intent.putExtra("x", x);
                startActivity(intent);
            }
        });
        bumanyi = (ImageView) findViewById(R.id.bumanyi);
        bumanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animationDrawable1 != null){
                    if(animationDrawable1.isRunning()){
                        animationDrawable1.stop();
                        animationDrawable1 = null;
                        bumanyi.setImageResource(R.mipmap.bumanyi);
                        symbol_id = "0";
                        return;
                    }
                }
                yiban.setImageResource(R.mipmap.yiban);
                manyi.setImageResource(R.mipmap.manyi);
                feichangmanyi.setImageResource(R.mipmap.feichangmanyi);
                bumanyi.setImageResource(R.drawable.bumanyi);
                animationDrawable1 = (AnimationDrawable)bumanyi.getDrawable();
                animationDrawable1.start();
                symbol_id = "4";
                if(animationDrawable2 != null){
                    if(animationDrawable2.isRunning()){
                        animationDrawable2.stop();
                    }
                    animationDrawable2=null;
                }
                if(animationDrawable3 != null){
                    if(animationDrawable3.isRunning()){
                        animationDrawable3.stop();
                    }
                    animationDrawable3=null;
                }
                if(animationDrawable4 != null){
                    if(animationDrawable4.isRunning()){
                        animationDrawable4.stop();
                    }
                    animationDrawable4=null;
                }


            }
        });
        yiban = (ImageView) findViewById(R.id.yiban);
        yiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animationDrawable2 != null){
                    if(animationDrawable2.isRunning()){
                        animationDrawable2.stop();
                        yiban.setImageResource(R.mipmap.yiban);
                        animationDrawable2 = null;

                        yiban.setImageResource(R.mipmap.yiban);
                        symbol_id = "0";
                        return;
                    }
                }
                bumanyi.setImageResource(R.mipmap.bumanyi);
                manyi.setImageResource(R.mipmap.manyi);
                feichangmanyi.setImageResource(R.mipmap.feichangmanyi);
                yiban.setImageResource(R.drawable.yiban);
                animationDrawable2 = (AnimationDrawable)yiban.getDrawable();
                animationDrawable2.start();
                symbol_id = "3";
                if(animationDrawable1 != null){
                    if(animationDrawable1.isRunning()){
                        animationDrawable1.stop();
                    }
                    animationDrawable1=null;
                }
                if(animationDrawable3 != null){
                    if(animationDrawable3.isRunning()){
                        animationDrawable3.stop();
                    }
                    animationDrawable3=null;
                }
                if(animationDrawable4 != null){
                    if(animationDrawable4.isRunning()){
                        animationDrawable4.stop();
                    }
                    animationDrawable4=null;
                }
            }
        });
        manyi = (ImageView) findViewById(R.id.manyi);
        manyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animationDrawable3 != null){
                    if(animationDrawable3.isRunning()){
                        animationDrawable3.stop();
                        manyi.setImageResource(R.mipmap.manyi);
                        animationDrawable3 = null;
                        symbol_id = "0";
                        return;
                    }
                }
                bumanyi.setImageResource(R.mipmap.bumanyi);
                yiban.setImageResource(R.mipmap.yiban);

                feichangmanyi.setImageResource(R.mipmap.feichangmanyi);
                manyi.setImageResource(R.drawable.menyi);
                animationDrawable3 = (AnimationDrawable)manyi.getDrawable();
                animationDrawable3.start();
                symbol_id = "2";
                if(animationDrawable1 != null){
                    if(animationDrawable1.isRunning()){
                        animationDrawable1.stop();
                    }
                    animationDrawable1=null;
                }
                if(animationDrawable2 != null){
                    if(animationDrawable2.isRunning()){
                        animationDrawable2.stop();
                    }
                    animationDrawable2=null;
                }
                if(animationDrawable4 != null){
                    if(animationDrawable4.isRunning()){
                        animationDrawable4.stop();
                    }
                    animationDrawable4=null;
                }
            }
        });

        feichangmanyi= (ImageView) findViewById(R.id.feichangmanyi);
        feichangmanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(animationDrawable4 != null){
                   if(animationDrawable4.isRunning()){
                       animationDrawable4.stop();
                       feichangmanyi.setImageResource(R.mipmap.feichangmanyi);
                       animationDrawable4 = null;
                       symbol_id = "0";
                       feichangmanyi.setImageResource(R.mipmap.feichangmanyi);
                       return;
                   }
                }
                bumanyi.setImageResource(R.mipmap.bumanyi);
                yiban.setImageResource(R.mipmap.yiban);
                manyi.setImageResource(R.mipmap.manyi);
                feichangmanyi.setImageResource(R.drawable.feichangmenyi);
                animationDrawable4 = (AnimationDrawable)feichangmanyi.getDrawable();
                animationDrawable4.start();
                symbol_id = "1";
                if(animationDrawable1 != null){
                    if(animationDrawable1.isRunning()){
                        animationDrawable1.stop();
                    }
                    animationDrawable1=null;
                }
                if(animationDrawable2 != null){
                    if(animationDrawable2.isRunning()){
                        animationDrawable2.stop();
                    }
                    animationDrawable2=null;
                }
                if(animationDrawable3 != null){
                    if(animationDrawable3.isRunning()){
                        animationDrawable3.stop();
                    }
                    animationDrawable3=null;
                }

            }
        });

        pinglun = (EditText)  findViewById(R.id.pinglun);
        commit =(TextView) findViewById(R.id.commit);
        typeid = intent.getStringExtra("typeid");
        img = intent.getStringExtra("img");
        title = intent.getStringExtra("title");
        rank = intent.getStringExtra("rank");
        linyu = intent.getStringExtra("linyu");
        time = intent.getStringExtra("time");
        adress = intent.getStringExtra("adress");
        resource_id = intent.getStringExtra("resource_id");
        x = intent.getDoubleExtra("x", 0.0);
        y = intent.getDoubleExtra("y", 0.0);


        if(typeid.equals("4")){
            xm_img.setVisibility(View.GONE);
            rc_img.setVisibility(View.VISIBLE);
            if(img != null && !img.equals("")){
                if(item.litpic.contains("maidianzhanweitu.png")){
                    rc_img1.setVisibility(View.VISIBLE);
                    rc_img.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img1, options);
                }else{
                    rc_img1.setVisibility(View.GONE);
                    rc_img.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img, options);
                }
            }else{
                xm_img.setVisibility(View.GONE);
                rc_img.setVisibility(View.GONE);
            }
            shijijiage.setText("￥"+item.price_discounts);
            yuanjia.setText("￥"+item.price);
            yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if(item.price.equals(item.price_discounts)){
                yuanjia.setVisibility(View.GONE);
            }else{
                yuanjia.setVisibility(View.VISIBLE);
            }
        }else{
            xm_img.setVisibility(View.GONE);
            shijijiage.setVisibility(View.GONE);
            rc_img.setVisibility(View.GONE);
            yuanjia.setVisibility(View.GONE);
            if(img != null && !img.equals("")){
                ImageLoader.getInstance().displayImage(img
                        , xm_img, options);
            }else{
                xm_img.setVisibility(View.GONE);
                rc_img.setVisibility(View.GONE);
            }
        }
        xm_title.setText(title);
        xm_rank.setText(rank);
        xm_linyu.setText(linyu);
        adress_time.setText(TimeUtils.getStrMonthAndDataTime(time));
        adress_content.setText(adress+"  ");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String pingjia = pinglun.getText().toString();
                if(symbol_id.equals("0")){
                    Toast.makeText(CommentActivity.this, "请选择评论表情",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(pingjia != null && !pingjia.equals("")){
                        progress.setVisibility(View.VISIBLE);
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
                        map.put("mid", mid);
                        map.put("method", "evaluate");
                        map.put("resource_id", resource_id);
                        map.put("content", pingjia);
                        map.put("symbol_id", symbol_id);
                        map.put("id", item.id);
                        map.put("rid", item.rid);
                        map.put("typeid", item.typeid);
                        map.put("aid", item.aid);
                        map.put("aid", item.aid);
//                    MyApplication.setAccessid();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("version"+MyApplication.version);
//                    sort.add("accessid"+MyApplication.deviceid);
//                    sort.add("timestamp"+timestamp);
//                    sort.add("mid"+mid);
//                    sort.add("action"+"save");
//                    sort.add("resource_id"+resource_id);
//                    sort.add("content"+ pingjia);
//                    sort.add("symbol_id"+symbol_id);
//                    sign = KeySort.keyScort(sort);
//                    map.put("sign", sign);
//                    map.put("version",MyApplication.version);
//                    map.put("accessid",MyApplication.deviceid);
//                    map.put("timestamp",timestamp);
                        commitPinglun();

                }else{
                    Toast.makeText(CommentActivity.this, "请填写意见信息",Toast.LENGTH_SHORT).show();
                }
            }
        });


        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pinglun.addTextChangedListener(new  TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count1.setText(s.toString().length()+"/140字");
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }
    private void commitPinglun(){
        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,1,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    ret = OkHttpUtils.post(url,map);
//                    if(ret != null){
//                        Message msg = Message.obtain();
//                        msg.what =1;
//                        handler.sendMessage(msg);
//                    }
//
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
            if(msg.what == 1){
                progress.setVisibility(View.GONE);
                Gson gson =new Gson();
                ret = (String) msg.obj;
                Ret ret1 =  gson.fromJson(ret, Ret.class);
                if(ret1 != null){
                    if(ret1.code.equals("1")){
                        Toast.makeText(CommentActivity.this, ret1.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(CommentActivity.this, ret1.message, Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("需求-评价");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("需求-评价");
        MobclickAgent.onPause(this);
    }
}
