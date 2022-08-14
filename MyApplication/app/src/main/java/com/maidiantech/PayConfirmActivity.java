package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;

import Util.TimeUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.YouHuiJuanEntity;
import view.RoundImageView;

import view.StyleUtils1;
import Util.SharedPreferencesUtil;
import Util.OkHttpUtils;
import Util.KeySort;
/**
 * Created by lizisong on 2017/9/7.
 */

public class PayConfirmActivity extends AutoLayoutActivity {
     ImageView back;
     ImageView xm_img;
     RoundImageView rc_img;
     private DisplayImageOptions options;
     TextView xm_title,xm_rank,xm_linyu;
     TextView connecttxt, phone_num, meettime, adress, youhuijuan, yuan_price,pay_money, youhui, pay_confirm;

     String name, rank, lingyu, conect, tel, datetime, meetadress, price, youhuiprice,litpic,typeid, aid, id;
     public static boolean isFinish = false;
    double x,y;
    TextView youhuijuan_count;
    String json="";
    YouHuiJuanEntity data;
    RelativeLayout youhuijuanlay;
    public static String youhuijuanprice="";
    public static String couponid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payconfirmactivity);
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isFinish = false;
        youhuijuanprice="";
        couponid="";
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.login_backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        xm_img = (ImageView)findViewById(R.id.xm_img);
        rc_img = (RoundImageView)findViewById(R.id.rc_img);
        xm_title = (TextView)findViewById(R.id.xm_title);
        xm_rank = (TextView)findViewById(R.id.xm_rank);
        xm_linyu = (TextView)findViewById(R.id.xm_linyu);
        connecttxt  =(TextView)findViewById(R.id.connect);
        phone_num = (TextView)findViewById(R.id.phone_num);
        meettime = (TextView)findViewById(R.id.meettime);
        adress = (TextView)findViewById(R.id.adress);
        youhuijuan = (TextView)findViewById(R.id.youhuijuan);
        youhuijuan_count = (TextView)findViewById(R.id.youhuijuan_count);
        yuan_price = (TextView)findViewById(R.id.yuan_price);
        pay_money = (TextView)findViewById(R.id.pay_money);
        youhui = (TextView)findViewById(R.id.youhui);
        pay_confirm = (TextView)findViewById(R.id.pay_confirm);
        youhuijuanlay = (RelativeLayout)findViewById(R.id.youhuijuanlay);

        name = getIntent().getStringExtra("name");
        rank = getIntent().getStringExtra("rank");
        lingyu = getIntent().getStringExtra("lingyu");
        conect = getIntent().getStringExtra("conect");
        tel = getIntent().getStringExtra("tel");
        datetime = getIntent().getStringExtra("datetime");
        meetadress = getIntent().getStringExtra("meetadress");
        price = getIntent().getStringExtra("price");
        youhuiprice = getIntent().getStringExtra("youhuiprice");
        litpic = getIntent().getStringExtra("litpic");
        x = getIntent().getDoubleExtra("x", 0.0);
        y = getIntent().getDoubleExtra("y", 0.0);
        typeid = getIntent().getStringExtra("typeid");
        aid   = getIntent().getStringExtra("aid");
        id  = getIntent().getStringExtra("id");
        if(typeid .equals("4")){
            xm_img.setVisibility(View.GONE);
            rc_img.setVisibility(View.VISIBLE);
            if(litpic != null && !litpic.equals("")){
                ImageLoader.getInstance().displayImage(litpic
                        , rc_img, options);
            }else{
                rc_img.setVisibility(View.GONE);
            }

        }else{
            xm_img.setVisibility(View.VISIBLE);
            rc_img.setVisibility(View.GONE);
            if(litpic != null && !litpic.equals("")){
                ImageLoader.getInstance().displayImage(litpic
                        , xm_img, options);
            }else{
                xm_img.setVisibility(View.GONE);
            }
        }
        xm_title.setText(name);
        xm_rank.setText(rank);
        xm_linyu.setText(lingyu);
        connecttxt.setText(conect);
        phone_num.setText(tel);
        meettime.setText(TimeUtils.getStrTime(datetime));
        adress.setText(meetadress+"  ");
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PayConfirmActivity.this, BaseMapDemo.class);
                intent.putExtra("y", y);
                intent.putExtra("x", x);
                startActivity(intent);
            }
        });
        youhuijuan.setText("无可用");
        pay_money.setText("待支付￥"+price);
        yuan_price.setText("￥"+price);
        pay_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(PayConfirmActivity.this, "couponid:"+couponid,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayConfirmActivity.this, PaySDkCallActivity.class);
                intent.putExtra("aid", aid);
                intent.putExtra("id", id);
                intent.putExtra("ranks", rank);
                intent.putExtra("type",0);
                intent.putExtra("couponid", couponid);
//                Toast.makeText(PayConfirmActivity.this,"couponid:"+couponid,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        youhuijuanlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayConfirmActivity.this, XuanZeYouHuiJuanActivity.class);
                intent.putExtra("typeid",typeid);
                intent.putExtra("aid", aid);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付确认");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付确认");
        MobclickAgent.onResume(this);
        if(isFinish){
            isFinish = false;
            finish();
        }
        if(youhuijuanprice != null && !youhuijuanprice.equals("")){
            youhuijuan.setTextColor(0xffff6363);
            youhuijuan.setText("-￥"+youhuijuanprice);
            youhui.setTextColor(0xffff6363);
            youhui.setText("优惠￥"+youhuijuanprice);

            youhuijuan_count.setVisibility(View.GONE);
            float p1 = Float.parseFloat(youhuijuanprice);
            float p2 = Float.parseFloat(price);
            if(p2-p1 >1){
                pay_money.setText("待支付￥"+(p2-p1));
            }else{
//                if(p2>p1){
                    pay_money.setText("待支付￥"+1);
                  youhui.setText("已优惠￥"+(p2-1));
//                }else{
//                    pay_money.setText("已￥"+1);
//                }
            }

        }else{
            getJson();
            couponid="";
            pay_money.setText("待支付￥"+price);
            youhui.setTextColor(0xFF9F9E9E);
            youhuijuan.setTextColor(0xFF9F9E9E);
            youhui.setText("优惠￥"+"0.0");
        }


    }

    private void getJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("mid"+mid);
                sort.add("timestamp"+timestamp);
                sort.add("typeid"+typeid);
                sort.add("aid"+aid);
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sign =KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/discounts_coupon_list.php?mid="+mid+"&typeid="+typeid+"&aid="+aid+"&sign="+sign+"&timestamp="+timestamp+MyApplication.accessid+"&version="+MyApplication.version;
//                Log.d("lizisong", "url:"+url);
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
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
                Gson gson = new Gson();
                data=gson.fromJson(json, YouHuiJuanEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        if(data.data.num.equals("0")){
                            youhuijuan.setVisibility(View.VISIBLE);
                            youhuijuan_count.setVisibility(View.GONE);
                        }else{
                            youhuijuan.setVisibility(View.VISIBLE);
                            youhuijuan.setText("");
                            youhuijuan_count.setVisibility(View.VISIBLE);
                            youhuijuan_count.setText(data.data.num);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XuanZeYouHuiJuanActivity.current = -1;
    }




}
