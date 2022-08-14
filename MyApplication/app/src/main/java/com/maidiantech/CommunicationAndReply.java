package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Flow;
import entity.PingLun;
import entity.PingLunEntry;
import entity.Ret;
import view.RefreshListView;
import view.RoundImageView;
import view.RoundImageView2;
import view.StyleUtils;
import view.StyleUtils1;
import view.SystemBarTintManager;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
/**
 * Created by Administrator on 2018/5/28.
 */

public class CommunicationAndReply extends AutoLayoutActivity {
    private DisplayImageOptions options;
    ImageView backs;
    RefreshListView listView;
    EditText pinglun;
    TextView send;
    ProgressBar progress;

    //头部数据展示
    View heartView;
    TextView rencai_titie,rencai_title,rencai_rank,rencai_linyu,shijijiage,yuanjia,yuyuebutton,adress_time,adress_content,des,jieshou_button,jieshou_cenel,gopay,pay_shengyu_time,lingyutitle,liuyanline;
    LinearLayout rencai_content,heartbg,pay_lay,jiagelay,des_lay;
    RelativeLayout rencai_img,adress_lay,jieshou0rcencel,daijiantou;
    ImageView xm_img;
    RoundImageView rc_img;
    RoundImageView2 rc_img1;
    public static Flow item;

    private List<PingLun> arrayList = new ArrayList<>();
    HashMap<String, String> hashmap = new HashMap<>();
    public String mobile,tel;

    public final int pageSize = 20;
    String time = "";
    String rid;
    String ret;
    String x,y;
    PingLunEntry pinglundata;
    String user;
    PingLunAdapter adapter = new PingLunAdapter();
    long  oldtime;
    String baseid;
    TextView one,two;
    ImageView sanjiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        setContentView(R.layout.commnicationandreply);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        backs = (ImageView)findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (RefreshListView)findViewById(R.id.listview);
        listView.setBackgroundColor(0xffF6F6F6);

        pinglun = (EditText)findViewById(R.id.pinglun);
        send = (TextView)findViewById(R.id.send);
        baseid = getIntent().getStringExtra("baseid");
        if(baseid != null){
            if(Integer.parseInt(baseid) > 1000){
                send.setClickable(false);
            }
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = pinglun.getText().toString();
                if(txt != null && !txt.equals("")){
//                    progress.setVisibility(View.VISIBLE);
                    final String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    hashmap.put("mid", mid);
                    hashmap.put("rid", rid);
                    hashmap.put("comment", txt);
                    commitContent();
                }else {
                    Toast.makeText(CommunicationAndReply.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        progress = (ProgressBar)findViewById(R.id.progress);
        heartView = View.inflate(CommunicationAndReply.this, R.layout.communicationandreplyheart, null);
        rencai_titie = (TextView)heartView.findViewById(R.id.rencai_titie);
        rencai_title =(TextView)heartView.findViewById(R.id.rencai_title);
        rencai_rank = (TextView)heartView.findViewById(R.id.rencai_rank);
        rencai_linyu = (TextView)heartView.findViewById(R.id.rencai_linyu);
        shijijiage =(TextView)heartView.findViewById(R.id.shijijiage);
        yuanjia = (TextView)heartView.findViewById(R.id.yuanjia);
        yuyuebutton = (TextView)heartView.findViewById(R.id.yuyuebutton);
        adress_time = (TextView)heartView.findViewById(R.id.adress_time);
        adress_content = (TextView)heartView.findViewById(R.id.adress_content);
        des = (TextView)heartView.findViewById(R.id.des);
        rencai_content = (LinearLayout)heartView.findViewById(R.id.rencai_content);
        rencai_img = (RelativeLayout)heartView.findViewById(R.id.rencai_img);
        adress_lay = (RelativeLayout)heartView.findViewById(R.id.adress_lay);
        xm_img = (ImageView)heartView.findViewById(R.id.xm_img);
        rc_img = (RoundImageView)heartView.findViewById(R.id.rc_img);
        rc_img1 = (RoundImageView2)heartView.findViewById(R.id.rc_img1);
        heartbg = (LinearLayout)heartView.findViewById(R.id.heartbg);
        jieshou0rcencel = (RelativeLayout)heartView.findViewById(R.id.jieshou0rcencel);
        jieshou_button = (TextView)heartView.findViewById(R.id.jieshou_button);
        jieshou_cenel = (TextView)heartView.findViewById(R.id.jieshou_cenel);
        pay_lay = (LinearLayout)heartView.findViewById(R.id.pay_lay);
        jiagelay = (LinearLayout)heartView.findViewById(R.id.jiagelay);
        des_lay = (LinearLayout)heartView.findViewById(R.id.des_lay);
        one = (TextView)heartView.findViewById(R.id.one);
        two = (TextView)heartView.findViewById(R.id.two);

        liuyanline = (TextView)heartView.findViewById(R.id.liuyanline);
        gopay = (TextView)heartView.findViewById(R.id.gopay);
        lingyutitle = (TextView)heartView.findViewById(R.id.lingyutitle);
        pay_shengyu_time = (TextView)heartView.findViewById(R.id.pay_shengyu_time);
        daijiantou = (RelativeLayout)heartView.findViewById(R.id.daijiantou);
        sanjiao = (ImageView)heartView.findViewById(R.id.sanjiao);
        if(item != null){
            if(item.state.equals("510") || item.state.equals("610")|| item.state.equals("710")){
                des_lay.setVisibility(View.GONE);
                daijiantou.setVisibility(View.GONE);
                if(item.str != null && !item.str.equals("")){
                    rencai_titie.setText(item.str);
                    rencai_titie.setVisibility(View.VISIBLE);
                }else{
                    rencai_titie.setVisibility(View.GONE);
                }
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);
                if(item.typeid.equals("2")){
                    heartbg.setBackgroundColor(0xff8d9ac5);
                    if(item.litpic != null && !item.litpic.equals("")){
                        rc_img.setVisibility(View.GONE);
                        xm_img.setVisibility(View.VISIBLE);

                        ImageLoader.getInstance().displayImage(item.litpic
                                , xm_img, options);
                    }else{
                        xm_img.setVisibility(View.VISIBLE);
                        xm_img.setImageResource(R.mipmap.information_placeholder);
                    }

                }else if(item.typeid.equals("4")){
                    rencai_title.setText(item.uname);
                    heartbg.setBackgroundColor(0xff5cc2b0);
                    if(item.litpic != null && !item.litpic.equals("")){
                        rc_img.setVisibility(View.VISIBLE);
                        xm_img.setVisibility(View.GONE);
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
                        rc_img.setVisibility(View.VISIBLE);
                        rc_img.setImageResource(R.mipmap.head_1);
                    }
                }else if(item.typeid.equals("7")){

                    heartbg.setBackgroundColor(0xfff2a17e);
                    if(item.litpic != null && !item.litpic.equals("")){
                        rc_img.setVisibility(View.GONE);
                        xm_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                , xm_img, options);
                    }else{
                        xm_img.setVisibility(View.VISIBLE);
                        xm_img.setImageResource(R.mipmap.information_placeholder);
                    }
                }else if(item.typeid.equals("0")){
                    heartbg.setBackgroundColor(0xff9ac6f7);
                }else if(item.typeid.equals("5")){
                    heartbg.setBackgroundColor(0xff9ac6f7);
                    if(item.litpic != null && !item.litpic.equals("")){
                        rc_img.setVisibility(View.GONE);
                        xm_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                , xm_img, options);
                    }else{
                        xm_img.setVisibility(View.VISIBLE);
                        xm_img.setImageResource(R.mipmap.information_placeholder);
                    }
                }
                yuyuebutton.setVisibility(View.GONE);
                adress_lay.setVisibility(View.GONE);
            }else if(item.state.equals("110")|| item.state.equals("120")|| item.state.equals("130")|| item.state.equals("140")){
                des_lay.setVisibility(View.GONE);
                if(item.str != null && !item.str.equals("")){
                    rencai_titie.setText(item.str);
                    rencai_titie.setVisibility(View.VISIBLE);
                }else{
                    rencai_titie.setVisibility(View.GONE);
                }
                adress_lay.setVisibility(View.GONE);
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);
                yuyuebutton.setVisibility(View.GONE);
                heartbg.setBackgroundColor(0xff5cc2b0);
                if(item.litpic != null && !item.litpic.equals("")){
                    rc_img.setVisibility(View.VISIBLE);
                    xm_img.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img, options);
                }else{
                    rc_img.setVisibility(View.VISIBLE);
                    rc_img.setImageResource(R.mipmap.head_1);
                }
                shijijiage.setText("￥"+item.price);
                yuanjia.setText("￥"+item.price_discounts);
                if(item.price.equals(item.price_discounts)){
                    yuanjia.setVisibility(View.GONE);
                }else{
                    yuanjia.setVisibility(View.VISIBLE);
                    yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                jiagelay.setVisibility(View.VISIBLE);
                if(item.state.equals("130")){
                    daijiantou.setVisibility(View.GONE);
                }

                if(item.state.equals("110")){
                    jieshou0rcencel.setVisibility(View.VISIBLE);
                    jieshou_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user ="1";
                            AgreeOrCenel(item.id);
                            jieshou0rcencel.setVisibility(View.GONE);
                        }
                    });
                    jieshou_cenel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user ="1";
                            AgreeOrCenel(item.id);
                            jieshou0rcencel.setVisibility(View.GONE);

                        }
                    });


                }else{
                    jieshou0rcencel.setVisibility(View.GONE);
                }

            }else if(item.state.equals("150")|| item.state.equals("160")|| item.state.equals("170")|| item.state.equals("180")|| item.state.equals("190")|| item.state.equals("200")){
                des_lay.setVisibility(View.GONE);
                if(item.str != null && !item.str.equals("")){
                    rencai_titie.setText(item.str);
                    rencai_titie.setVisibility(View.VISIBLE);
                }else{
                    rencai_titie.setVisibility(View.GONE);
                }
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);

                adress_lay.setVisibility(View.VISIBLE);
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);

                yuyuebutton.setVisibility(View.GONE);
                heartbg.setBackgroundColor(0xff5cc2b0);
                if(item.litpic != null && !item.litpic.equals("")){
                    rc_img.setVisibility(View.VISIBLE);
                    xm_img.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img, options);
                }else{
                    rc_img.setVisibility(View.VISIBLE);
                    rc_img.setImageResource(R.mipmap.head_1);
                }
                shijijiage.setText("￥"+item.price);
                yuanjia.setText("￥"+item.price_discounts);
                if(item.price.equals(item.price_discounts)){
                    yuanjia.setVisibility(View.GONE);
                }else{
                    yuanjia.setVisibility(View.VISIBLE);
                    yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                if(item.meetdate != null){
                    adress_time.setText(TimeUtils.getStrMonthAndDataTime(item.meetdate));
                }else{
                    adress_time.setText("");
                }
                adress_content.setText(item.unit);
                if(item.state.equals("170")){
                    pay_lay.setVisibility(View.VISIBLE);
                    oldtime =(Long.parseLong(item.time)*1000+172800000)-System.currentTimeMillis();
                    if(oldtime >1000){
                        String current =  formatTime(oldtime);
                        pay_shengyu_time.setText("("+current+")");
                        Message msg = Message.obtain();
                        msg.what =100;
                        handler.sendMessage(msg);

                    }else{
                        handler.removeMessages(100);
                        pay_lay.setVisibility(View.GONE);

                    }
                    gopay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handler.removeMessages(100);
                            if(item.meetmobile != null && !item.meetmobile.equals("")){
                                try {
                                    String [] mobileAndTel = item.meetmobile.split("/");
                                    mobile = mobileAndTel[0];
                                    tel    = mobileAndTel[1];
                                }catch (Exception e){

                                }
                            }
                            // 去支付流程
                            tiaozhuan(item.title, item.price, item.ranks, item.area_cate,mobile, tel,
                                    item.meetdate,item.unit, null, item.litpic,item.typeid,item.aid,item.id,item.latitude,item.longitude);
                        }
                    });

                }else {
                    pay_lay.setVisibility(View.GONE);
                }

            }else if(item.state.equals("820")){
                des_lay.setVisibility(View.VISIBLE);
                rencai_titie.setVisibility(View.GONE);
                adress_lay.setVisibility(View.GONE);
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);

                yuyuebutton.setVisibility(View.VISIBLE);
                if(item.litpic != null && !item.litpic.equals("")){
                    rc_img.setVisibility(View.VISIBLE);
                    xm_img.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img, options);
                }else{
                    rc_img.setVisibility(View.VISIBLE);
                    rc_img.setImageResource(R.mipmap.head_1);
                }
                des.setText(item.content);

            }else if(item.state.equals("810")){
                rencai_titie.setVisibility(View.GONE);
                adress_lay.setVisibility(View.GONE);
                rencai_title.setText(item.title);
                rencai_rank.setText(item.ranks);
                rencai_linyu.setText(item.area_cate);
                des_lay.setVisibility(View.VISIBLE);
                liuyanline.setVisibility(View.GONE);
                one.setBackgroundColor(0xffdddddd);
                two.setBackgroundColor(0xffdddddd);
                sanjiao.setBackgroundResource(R.mipmap.huijianjian);
                yuyuebutton.setVisibility(View.GONE);
                if(item.litpic != null && !item.litpic.equals("")){
                    rc_img.setVisibility(View.VISIBLE);
                    xm_img.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , rc_img, options);
                }else{
                    xm_img.setVisibility(View.GONE);
                    rc_img.setVisibility(View.VISIBLE);
                    rc_img.setImageResource(R.mipmap.information_placeholder);
                }
                lingyutitle.setText("专属经济技术经济人");
                des.setText(item.content);

            }
        }
        listView.addHeaderView(heartView);
        getData();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }
    class PingLunAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           HolderView holder;
            if(convertView == null){
                holder = new HolderView();
                convertView = View.inflate(CommunicationAndReply.this, R.layout.pinglunadapter, null);
                holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
                holder.heart = (RoundImageView)convertView.findViewById(R.id.rc_img);
                holder.name  = (TextView)convertView.findViewById(R.id.name);
                holder.content = (TextView)convertView.findViewById(R.id.content);
                holder.lastitem = (TextView)convertView.findViewById(R.id.lastitem);
                convertView.setTag(holder);
            }else{
                holder = (HolderView) convertView.getTag();
            }
            PingLun item =arrayList.get(position);

            try {
                if(item.state == 0){
                    holder.layout.setVisibility(View.VISIBLE);
                    holder.lastitem.setVisibility(View.GONE);
                    holder.name.setText(item.username);
                    holder.content.setText(item.comments);
                    ImageLoader.getInstance().displayImage(item.headurl
                            , holder.heart, options);
                }else{
                    holder.layout.setVisibility(View.GONE);
                    holder.lastitem.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            return convertView;
        }

        class HolderView{
            public RoundImageView heart;
            public TextView name;
            public TextView content;
            public TextView lastitem;
            public LinearLayout layout;
        }
    }
    private void getData(){
       final String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        rid=item.id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://"+ MyApplication.ip+"/api/getRequireComment.php?rid="+rid+"&mid="+mid+"&lastTime="+time+"&pageSize="+pageSize+"&version="+MyApplication.version;
                ret = OkHttpUtils.loaudstringfromurl(url);
                if(ret != null){
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    progress.setVisibility(View.GONE);
                    listView.setPullUpToRefreshFinish();
                    Gson gson = new Gson();
                    pinglundata = gson.fromJson(ret, PingLunEntry.class);
                    if(pinglundata != null){
                        if(pinglundata.code.equals("1")){
                            List<PingLun> posList = pinglundata.data;
                            if(posList != null){
                                if(posList.size() > 0){
                                    for (int i=0; i < posList.size();i++){
                                        PingLun item = posList.get(i);
                                        item.state = 0;
                                        arrayList.add(item);
                                    }
                                }
                                if(posList.size() < 20){
                                    listView.setPullUpToRefreshable(false);
                                }
                            }
                        }
                        PingLun item = new PingLun();
                        item.state = -1;
                        arrayList.add(item);
                    }


                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }catch (Exception e){

                }
            }
            if(msg.what == 2){
                try {
                    pinglun.setText("");
                    Gson gson = new Gson();
                    Ret ret1= gson.fromJson(ret, Ret.class);
                    if(ret1.code.equals("1")){
                        Toast.makeText(CommunicationAndReply.this, ret1.message, Toast.LENGTH_SHORT).show();
                        time="";
                        arrayList.clear();
                        getData();
                    }
                }catch (Exception e){

                }
            }
            if(msg.what == 3){
                try {
                Gson gson = new Gson();
                String str = (String)msg.obj;
                Ret ret = gson.fromJson(str, Ret.class);
                if(ret != null){
                    if(ret.code.equals("1")){

                    }
                    Toast.makeText(CommunicationAndReply.this, ret.message.replaceAll("<br>","\n"), Toast.LENGTH_SHORT).show();
                }
                }catch (Exception e){

                }
            }
            if(msg.what == 100){
                try {
                    if(oldtime > 1000){
                        oldtime=oldtime-1000;
                        String current = formatTime(oldtime);
                        pay_shengyu_time.setText("("+current+")");
                        Message msg1 = Message.obtain();
                        msg1.what =100;
                        handler.sendMessageDelayed(msg1, 1000);


                    }else{
                        handler.removeMessages(100);
                        pay_lay.setVisibility(View.GONE);
                    }
                }catch (Exception E){

                }
            }
        }
    };
    private void commitContent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url= "http://"+MyApplication.ip+"/api/require_addcomment.php";
                try {
                    ret = OkHttpUtils.post(url, hashmap);
                    if(ret != null){
                        Message msg = Message.obtain();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){

                }

            }
        }).start();
    }


    /**
     * 同意或者拒绝的接口
     **/
    public void AgreeOrCenel(final String id1){
        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id", id1);
        map.put("method", "feedback");
        map.put("userfk",user );
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,3,0);
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php?id="+id1+"&method=feedback&userfk="+user;
//                        str = OkHttpUtils.loaudstringfromurl(url);
//                        if(str != null){
//                            Message msg = Message.obtain();
//                            msg.what = 2;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                }
//        ).start();
    }
    public void tiaozhuan(String name, String price, String rank, String lingyu,String conect,String tel,
                          String datetime, String meetadress, String youhuiprice, String litpic,String typeid,String aid,String id,double x,double y){
        Intent intent = new Intent(this, PayConfirmActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("rank", rank);
        intent.putExtra("lingyu", lingyu);
        intent.putExtra("conect", conect);
        intent.putExtra("tel", tel);
        intent.putExtra("datetime", datetime);
        intent.putExtra("meetadress", meetadress);
        intent.putExtra("youhuiprice", youhuiprice);
        intent.putExtra("litpic", litpic);
        intent.putExtra("typeid", typeid);
        intent.putExtra("aid", aid);
        intent.putExtra("id", id);
        intent.putExtra("y", y);
        intent.putExtra("x", x);
        startActivity(intent);
    }
    /*
* 毫秒转化时分秒毫秒
*/
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        //  StringBuffer sb = new StringBuffer();
        String ret="";

        if(day > 0) {
//            sb.append(day+"天");
            hour =hour+day*24;
        }
        if(hour > 0) {
            //sb.append(hour+":");
            ret = hour+"：";
        }else{
            ret ="00：";
        }
        if(minute > 0) {
            // sb.append(minute+":");
            ret=ret+minute+"：";
        }else{
            ret=ret+"00：";
        }
        if(second > 0) {
            //sb.append(second+":");
            ret = ret+second+"";
        }else{
            ret = ret+"00";
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);
    }
}
