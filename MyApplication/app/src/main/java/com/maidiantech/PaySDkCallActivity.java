package com.maidiantech;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.maidiantech.wxapi.WXPayEntryActivity;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import Util.OkHttpUtils;
import Util.OrderInfoUtil2_0;
import Util.PayResult;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.OrderEntry;
import entity.Ret;
import entity.ZhuanLiEntiry;
import view.StyleUtils1;
import Util.FileHelper;

/**
 * Created by lizisong on 2017/9/8.
 */

public class PaySDkCallActivity extends Activity implements WXPayEntryActivity.OnWeiChatPayListener {
    TextView cenel;
    LinearLayout zhifubaocall, weixinzhifu;
    String aid, id, ranks,mid;
    ProgressBar progressBar;
    String json;
    OrderEntry order;
    ZhuanLiEntiry zhuanli;
    int pay_method=0;
    String orderid, ordername,orderprice,couponid;

    String patent_type, ugid,title,subject,company,linkman,telephone,record_id;
    private OkHttpUtils Okhttp;
    int type;
    HashMap<String, String> map = new HashMap<>();

    final static int RENCAI_PAY_STATE = 0;
    final static int QINGBAO_PAY_STATE = 1;
    final static int ZHUANLI_PAY_STATE = 2;
    final static int ZHUANLIGUANFEI_PAY_STATE = 3;

    //支付宝的参数
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017071707788255";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088721537009068";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
//    public static final String RSA2_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJu9wXmpm9ARg4sb+ZpCeQ9LtKXwaftZnqHlA5tWVgGs7Xr5M6Kus/gJLknrzz9gUGpCItU/vM9ko6WryM1a9hfXk0CqBB8rc9CZZid1UxTNf81cr2bzC19rBq4cula7bN06jNEn7ff3WdQwpxl2wsRU1yf/Nc2BErAqFSbGUzyfAgMBAAECgYBtRWAgGo6GGCOxULl6MDV8M+PVoa7aefDBKkuzi4ngtxIQfBRsiiMfXmGp39eTYeJKC4khEg1JfhULwlGrM60ByTwcDd95ovGI66Xlgc64ZRjlkzJ3cz6oLVYS7mPIG2sx/GASQ8cUuGO4xbKI3FWse9KIYbZnlSisoAb8FOqVMQJBAMmZzqFuLDqmj7RopZVD963bNcWLyrNDNCuminiDc5ZkU6nvGACkSkWQAct/7bK1+W7qO6ZvopjiatHmE5Gq3nUCQQDFxA7d/259bXvhSfmCUzlifTGKHHVNHuxekua4bmQ2ZXi+HM/sygx6DmfcVSHctec6VSp/8/rlbPqdbu601nRDAkBc+FhJK/Srq4pUCXprBqge/hujGV+GIfTaOHhdMUnJzkSi9occLKevwsSBI2Lr6m24T77od00FskzOtYrKv4DNAkEAngq46tXW4WteBUMNnwWNJVTxAvMNFq1wrX9t29N0SDg9DoiN7SfmYqAiheSOpAfGGkq3JPu/9BpsCOX+4wtCawJBALY4oeoFl9JW1quB0ytpadqI+9UyUNH3hyt2sgTG5zjsw25fx/hxrtOgDfJnjeiOuJ8724vOsQ9fkpqhrIekyEY=";
//    public static final String RSA2_PRIVATE ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCT8TvicBQxOuUoyMN/YUjsczZ0GolGLLQKJkB2ktRyp4ED+gUvjCzxb1qP0pq0LYSjmSYIn1JddmsSxZAsANBdsoKMX4pf2idnSbaeM/e6H3g0TM0/ST7NXj3oOi3HWx7JMV9Tv89bIKTmvNbIREEXtwwt8P8fJVxHIuLEoYp8Xoc96BxGhtfiH+GFQ2qmvEcXs5DjK9Qk9y+0YhSCYkvIchO/oGTFALtKk9gkMARY4a5td3Yhj0M6mfPm5GSciTaL1SBQdJiwUq/Oo05R98GVnhJG5h8w8kLYu9DPZOQCuQZS0MqoBa0EkdL8ofULX+BkLac/ENnASKJzYoqU23P9AgMBAAECggEBAI9rI/+/YYPcMX2ZKxSYjiZTBq4Fb6CtA5h3PrT0YKoJwmPFhDN087jWl3en1jvniJdEh3+CXEmo2+Ilzj8qRtUekbpdgHqttNdHuZaikHxJt7ET50F70zsqKl+vzuNdbXnjuCMrZmxUOChLVvhsWqIP3OnIoLpj8JIxup6eb+9h+VllRr+jABKbvJLx0aMDRrMs6jkNl37d2+DjujLuOiWHpiHdfnVZIugSe7yPs9XnkuoYk4gvLznIzjOi4BE+7KRK/3Li2VYQSo0818qeyz9066kHs8NbBKay6c9h7NYPibezjg22d1VLX7TJCVXspb/1YufmyFlh+/AqynJa0W0CgYEA/kgj0ixqIHu7LxIMvUxT6tPLyL4zuk7vXHBQF+l8xPqGPdRNEs70IbZghzOHgSPMaOc3i789ou5ebwcz2deA8J41FYw0amdayV2FGYFzVtR/qXxwa8zI/LUSNP2IVWMlETkyJHOE3OQBS8EL4m8c8Js18fyK+u6Mqj37bkOBMzsCgYEAlPElg6qYb7POlsocLnbEVjH1whm2NLXXn19LY6N0uBjXOgHsgjfHSP1yZbD5zuKYdiom1HsveKcSOtTtQRdwCezu/b1xn+wuSFN0usMWw/K9fVpkgAQiJmSNxHmpvMe7xNCV8VQD2VXZvhOI9eWkF0RGWr4BVZPBtc2u2w2PkicCgYEArgBTcsitvpismCMz0H1glzpjInT1FLJbHNhGFnbRyEDeh4S1UP+JE2CnYR0jxnzmrYo6+kfdN5cBy9wT4SeUthKspJgSbhVXjJ+QKsnoUSyMR0A99aZminalhNlQ5402mjiXVVYvPrBPKrVpGoOKPCMZoQN9XwTKANz1JpjO2m0CgYAdwgiCxATs3Hn8Oqlixyv3JMg7XbO/2E0adIm1gKUDW0M1PckpQ0e315uRochng3J+uXFEptAXRRopUv2MMcia0xH09HLNRv0ASlxaLDxSLh+Z+gN2aF0CWrjQdpZpN2bWre4nZ2fVdoeoqHKG3rjoRSXhX3EYzgq37j5vWchDpQKBgQDIlFtxtO2ZGcg7RacmQPFS835lpDgviYxz5EKmGEnebSXViJl83jKv7TarU40hq5KVomyB6QqQsFBX67g5BgdZOVZBAknZCaXPpA7QRq5c599hMZ0KhedLzKWZwxM3obX5hk8lS4af732iEbyldehP4otPSR1B+ctS38worBUyaA==";

//    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQCbvcF5qZvQEYOLG/maQnkPS7Sl8Gn7WZ6h5QObVlYBrO16+TOirrP4CS5J688/YFBqQiLVP7zPZKOlq8jNWvYX15NAqgQfK3PQmWYndVMUzX/NXK9m8wtfawauHLpWu2zdOozRJ+3391nUMKcZdsLEVNcn/zXNgRKwKhUmxlM8nwIDAQABAoGAbUVgIBqOhhgjsVC5ejA1fDPj1aGu2nnwwSpLs4uJ4LcSEHwUbIojH15hqd/Xk2HiSguJIRINSX4VC8JRqzOtAck8HA3feaLxiOul5YHOuGUY5ZMyd3M+qC1WEu5jyBtrMfxgEkPHFLhjuMWyiNxVrHvSiGG2Z5UorKAG/BTqlTECQQDJmc6hbiw6po+0aKWVQ/et2zXFi8qzQzQrpop4g3OWZFOp7xgApEpFkAHLf+2ytflu6jumb6KY4mrR5hORqt51AkEAxcQO3f9ufW174Un5glM5Yn0xihx1TR7sXpLmuG5kNmV4vhzP7MoMeg5n3FUh3LXnOlUqf/P65Wz6nW7utNZ0QwJAXPhYSSv0q6uKVAl6awaoHv4boxlfhiH02jh4XTFJyc5EovaHHCynr8LEgSNi6+ptuE++6HdNBbJMzrWKyr+AzQJBAJ4KuOrV1uFrXgVDDZ8FjSVU8QLzDRatcK1/bdvTdEg4PQ6Ije0n5mKgIoXkjqQHxhpKtyT7v/QabAjl/uMLQmsCQQC2OKHqBZfSVtargdMraWnaiPvVMlDR94crdrIExuc47MNuX8f4ca7ToA3yZ43ojrifO9uLzrEPX5KaoayHpMhG";
    public static final String RSA_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCT8TvicBQxOuUoyMN/YUjsczZ0GolGLLQKJkB2ktRyp4ED+gUvjCzxb1qP0pq0LYSjmSYIn1JddmsSxZAsANBdsoKMX4pf2idnSbaeM/e6H3g0TM0/ST7NXj3oOi3HWx7JMV9Tv89bIKTmvNbIREEXtwwt8P8fJVxHIuLEoYp8Xoc96BxGhtfiH+GFQ2qmvEcXs5DjK9Qk9y+0YhSCYkvIchO/oGTFALtKk9gkMARY4a5td3Yhj0M6mfPm5GSciTaL1SBQdJiwUq/Oo05R98GVnhJG5h8w8kLYu9DPZOQCuQZS0MqoBa0EkdL8ofULX+BkLac/ENnASKJzYoqU23P9AgMBAAECggEBAI9rI/+/YYPcMX2ZKxSYjiZTBq4Fb6CtA5h3PrT0YKoJwmPFhDN087jWl3en1jvniJdEh3+CXEmo2+Ilzj8qRtUekbpdgHqttNdHuZaikHxJt7ET50F70zsqKl+vzuNdbXnjuCMrZmxUOChLVvhsWqIP3OnIoLpj8JIxup6eb+9h+VllRr+jABKbvJLx0aMDRrMs6jkNl37d2+DjujLuOiWHpiHdfnVZIugSe7yPs9XnkuoYk4gvLznIzjOi4BE+7KRK/3Li2VYQSo0818qeyz9066kHs8NbBKay6c9h7NYPibezjg22d1VLX7TJCVXspb/1YufmyFlh+/AqynJa0W0CgYEA/kgj0ixqIHu7LxIMvUxT6tPLyL4zuk7vXHBQF+l8xPqGPdRNEs70IbZghzOHgSPMaOc3i789ou5ebwcz2deA8J41FYw0amdayV2FGYFzVtR/qXxwa8zI/LUSNP2IVWMlETkyJHOE3OQBS8EL4m8c8Js18fyK+u6Mqj37bkOBMzsCgYEAlPElg6qYb7POlsocLnbEVjH1whm2NLXXn19LY6N0uBjXOgHsgjfHSP1yZbD5zuKYdiom1HsveKcSOtTtQRdwCezu/b1xn+wuSFN0usMWw/K9fVpkgAQiJmSNxHmpvMe7xNCV8VQD2VXZvhOI9eWkF0RGWr4BVZPBtc2u2w2PkicCgYEArgBTcsitvpismCMz0H1glzpjInT1FLJbHNhGFnbRyEDeh4S1UP+JE2CnYR0jxnzmrYo6+kfdN5cBy9wT4SeUthKspJgSbhVXjJ+QKsnoUSyMR0A99aZminalhNlQ5402mjiXVVYvPrBPKrVpGoOKPCMZoQN9XwTKANz1JpjO2m0CgYAdwgiCxATs3Hn8Oqlixyv3JMg7XbO/2E0adIm1gKUDW0M1PckpQ0e315uRochng3J+uXFEptAXRRopUv2MMcia0xH09HLNRv0ASlxaLDxSLh+Z+gN2aF0CWrjQdpZpN2bWre4nZ2fVdoeoqHKG3rjoRSXhX3EYzgq37j5vWchDpQKBgQDIlFtxtO2ZGcg7RacmQPFS835lpDgviYxz5EKmGEnebSXViJl83jKv7TarU40hq5KVomyB6QqQsFBX67g5BgdZOVZBAknZCaXPpA7QRq5c599hMZ0KhedLzKWZwxM3obX5hk8lS4af732iEbyldehP4otPSR1B+ctS38worBUyaA==";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    public String state;
    private IWXAPI api;
    private AutoRelativeLayout bg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paysdkcall);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        cenel = (TextView)findViewById(R.id.cencel);
        zhifubaocall = (LinearLayout)findViewById(R.id.zhifubaocall);
        weixinzhifu  = (LinearLayout)findViewById(R.id.weixinzhifu);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        bg = (AutoRelativeLayout)findViewById(R.id.bg);
//        bg.setAlpha(0.5f);

        api = MyApplication.api;
        api.registerApp("wx5b39abf9cbfcf5e7");
        WXPayEntryActivity.WeiChatPay(this);
        mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        aid = getIntent().getStringExtra("aid");
        id = getIntent().getStringExtra("id");
        ranks = getIntent().getStringExtra("ranks");
        couponid = getIntent().getStringExtra("couponid");
//        Toast.makeText(PaySDkCallActivity.this, "couponid:"+couponid, Toast.LENGTH_SHORT).show();
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        type = getIntent().getIntExtra("type",-1);
        if(type == ZHUANLI_PAY_STATE){
            patent_type = getIntent().getStringExtra("patent_type");
            ugid = getIntent().getStringExtra("ugid");
            if(ugid == null || ugid.equals("")){
                ugid=" ";
            }
            title = getIntent().getStringExtra("title");
            subject = getIntent().getStringExtra("subject");
            company = getIntent().getStringExtra("company");
            linkman = getIntent().getStringExtra("linkman");
            telephone = getIntent().getStringExtra("phone");
        }
        if(type == ZHUANLIGUANFEI_PAY_STATE){
            record_id = getIntent().getStringExtra("record_id");
        }
        cenel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayConfirmActivity.isFinish = true;
                finish();
            }
        });
        zhifubaocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付宝支付
                progressBar.setVisibility(View.VISIBLE);
                if(type == RENCAI_PAY_STATE){
                    map.put("action", "save");
                    map.put("mid", mid);
                    map.put("goods_id", aid);
                    map.put("resource_id", id);
                    map.put("ranks", ranks);
                    map.put("pay_method", "1");
                    map.put("couponid", couponid);
                    map.put("version",MyApplication.version);
                    pay_method = 1;
                    PayOrderRencai();
                }else if(type == QINGBAO_PAY_STATE){
                    map.put("action", "save");
                    map.put("mid", mid);
                    map.put("goods_id", aid);
                    map.put("pay_method", "1");
                    map.put("version",MyApplication.version);
                    pay_method = 1;
                    PayOrderforQingBao();
                }else if(type == ZHUANLI_PAY_STATE){
                    map.put("c", "patent");
                    map.put("mid", mid);
                    map.put("a", "submit");
                    map.put("pay_method", "1");
                    map.put("patent_type",patent_type);
                    map.put("ugid",ugid);
                    map.put("title",title);
                    map.put("subject",subject);
                    map.put("company",company);
                    map.put("linkman",linkman);
                    map.put("telephone",telephone);
                    map.put("pay_method","1");
                    map.put("coupon_id",couponid);
                    pay_method = 1;
                    PayOrderZhuanLi(pay_method);
                }else if(type == ZHUANLIGUANFEI_PAY_STATE){
                    map.put("c", "patent");
                    map.put("a", "costOrder");
                    map.put("record_id", record_id);
                    map.put("pay_method","1");
                    pay_method = 1;
                    PayOrderZhuanLiGuanFei(1);
                }
            }
        });
        weixinzhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信支付
                //支付宝支付
//                payTestWeiXin();
                progressBar.setVisibility(View.VISIBLE);
                if(type == RENCAI_PAY_STATE){
                    map.put("action", "save");
                    map.put("mid", mid);
                    map.put("goods_id", aid);
                    map.put("resource_id", id);
                    map.put("ranks", ranks);
                    map.put("pay_method", "2");
                    map.put("couponid", couponid);
                    map.put("version", MyApplication.version);
                    pay_method = 2;
                    PayOrderRencai();
                }else if(type == QINGBAO_PAY_STATE){
                    map.put("action", "save");
                    map.put("mid", mid);
                    map.put("goods_id", aid);
                    map.put("pay_method", "2");
                    map.put("version", MyApplication.version);
                    pay_method = 2;
                    PayOrderforQingBao();
                }else if(type == ZHUANLI_PAY_STATE){
                    map.put("c", "patent");
                    map.put("mid", mid);
                    map.put("a", "submit");
                    map.put("pay_method", "2");
                    map.put("patent_type",patent_type);
                    map.put("ugid",ugid);
                    map.put("title",title);
                    map.put("subject",subject);
                    map.put("company",company);
                    map.put("linkman",linkman);
                    map.put("telephone",telephone);
                    map.put("coupon_id",couponid);
                    pay_method = 2;
                    PayOrderZhuanLi(pay_method);
                }else if(type == ZHUANLIGUANFEI_PAY_STATE){
                    map.put("c", "patent");
                    map.put("a", "costOrder");
                    map.put("record_id", record_id);
                    map.put("pay_method","2");
                    pay_method = 2;
                    PayOrderZhuanLiGuanFei(2);
                }
            }
        });
    }

    /**
     * 情报订购接口
     */
    public void PayOrderforQingBao(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://"+ MyApplication.ip+"/api/getIndustryTeligOrder.php";
                json = OkHttpUtils.postkeyvlauspainr(url, map);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.arg1 =pay_method;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 专利订购接口
     */
    public void PayOrderZhuanLi(int arg1){
        String url="http://"+ MyApplication.ip+"/webapp/api.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,11,arg1);
    }


    /**
     * 专利官方支付
     */
    public void PayOrderZhuanLiGuanFei(int arg1){
        String url="http://"+ MyApplication.ip+"/webapp/api.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,11,arg1);
    }


    /**
     * 人才订购接口
     */
    public void PayOrderRencai(){

        Log.d("lizisong", "map.tostring():"+map.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://"+ MyApplication.ip+"/api/userServiceOrdervManage.php";
                Log.d("lizisong", "url:"+url);
                json = OkHttpUtils.postkeyvlauspainr(url, map);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.arg1 =pay_method;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 3){
                    progressBar.setVisibility(View.GONE);
                    if(msg.arg1 == 1){
                        Gson g=new Gson();
                        Log.d("lizisong", "json:"+json);
                        order = g.fromJson(json, OrderEntry.class);
                        if(order != null && order.code.equals("1")){
                            orderid = order.data.order_id;
                            ordername = order.data.order_name;
                            orderprice = order.data.order_price;
                                //传入支付宝支付
                            if(type == RENCAI_PAY_STATE){
                                payV2("钛领科技:人才预约服务");
                            }else if(type == QINGBAO_PAY_STATE){
                                payV2("钛领科技:订阅行业情报");
                            }else if(type == ZHUANLI_PAY_STATE){

                            }
                       }
                    }else if(msg.arg1 == 2){
                            //微信支付
                        Log.d("lizisong", "json:"+json);
                        JSONObject json1 = new JSONObject(json);
                        try {
                            if(null != json1 && !json1.has("retcode") ){
                                PayReq req = new PayReq();
                                req.appId			= json1.getString("appid");
                                req.partnerId		= json1.getString("mch_id");
                                req.prepayId		= json1.getString("prepay_id");
                                req.nonceStr		= json1.getString("nonce_str");
                                req.timeStamp		= json1.getString("timestamp");
                                orderid = json1.getString("out_trade_no");
                                req.packageValue	= "Sign=WXPay";
                                req.sign			= json1.getString("sign");
                                req.extData			= "app data"; // optional
//                                Toast.makeText(PaySDkCallActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

                                api.sendReq(req);
                            }else{
                                Toast.makeText(PaySDkCallActivity.this, "返回错误"+json1.getString("retmsg"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }

                    }
//                    Intent intent = new Intent(PaySDkCallActivity.this, PayDemoActivity.class);
//                    intent.putExtra("order_id", order.data.order_id);
//                    intent.putExtra("order_name", order.data.order_name);
//                    intent.putExtra("order_price", order.data.order_price);
//                    intent.putExtra("make_order_time", order.data.make_order_time);
//                    startActivity(intent);


                }else if(msg.what == SDK_PAY_FLAG){
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.d("lizisong", "resultInfo:"+resultInfo);
                    Log.d("lizisong", "resultStatus:"+resultStatus);
                    if(resultStatus.equals("9000")){
                        state = "1";
                        if(type == RENCAI_PAY_STATE){
                            payRetForRencai();
                        }else if(type == QINGBAO_PAY_STATE){
                            payRetQingBao();
                        }else if(type == ZHUANLI_PAY_STATE){
                            //专利的回调放在这里
                            payRetZhuanli();
                        }else if(type == ZHUANLIGUANFEI_PAY_STATE){
                            payRetZhuanli();
                        }

                    }else{
                        state = "0";
                        if(type == RENCAI_PAY_STATE){
                            payRetForRencai();
                        }else if(type == QINGBAO_PAY_STATE){
                            payRetQingBao();
                        }else if(type == ZHUANLI_PAY_STATE){

                        }
                    }
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        Toast.makeText(PaySDkCallActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PaySDkCallActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                }else if(msg.what == 10){
                    Gson gson=new Gson();
                    if(type == ZHUANLI_PAY_STATE || type == ZHUANLIGUANFEI_PAY_STATE){
                        ret = (String)msg.obj;
                    }
                    Ret ret1 =  gson.fromJson(ret, Ret.class);
                    FileHelper.d("lizisong", "ret:"+ret);
                    if(ret1 != null){
                        if(state.equals("1")){
                            QingBaoDeilActivity.isShowSucessfull = true;
                        }else{
                            QingBaoDeilActivity.isShowSucessfull = false;
                        }
                        PayConfirmActivity.isFinish = true;
                        TopicInformation.isFinish = true;
                        finish();
                        return;
                    }
                }else if(msg.what == 11){
                    if(msg.arg1 == 1){
                        String ret = (String)msg.obj;
                        Gson gson = new Gson();
                        zhuanli= gson.fromJson(ret, ZhuanLiEntiry.class);
                        if(zhuanli != null){
                            if(zhuanli.code.equals("1")){
                                orderid = zhuanli.result.order_id;
                                ordername = zhuanli.result.order_name;
                                orderprice = zhuanli.result.order_price;
//                                Log.d("lizisong", "orderid:"+orderid+",ordername:"+ordername+",orderproce:"+orderprice);
//                              payV2("钛领科技:专利申请服务");
                                Log.d("lizisong","专利申请:"+zhuanli.result.sign );
                              payV(zhuanli.result.sign);

                            }
                        }
                    }else if(msg.arg1 == 2){
                        String ret = (String) msg.obj;
                            //微信支付
                            JSONObject json1 = new JSONObject(ret);
                            try {
                                if(null != json1 && !json1.has("retcode") ){
                                    PayReq req = new PayReq();
                                    req.appId			= json1.getString("appid");
                                    req.partnerId		= json1.getString("mch_id");
                                    req.prepayId		= json1.getString("prepay_id");
                                    req.nonceStr		= json1.getString("nonce_str");
                                    req.timeStamp		= json1.getString("timestamp");
                                    orderid = json1.getString("out_trade_no");
                                    req.packageValue	= "Sign=WXPay";
                                    req.sign			= json1.getString("sign");
                                    req.extData			= "app data"; // optional
//                                Toast.makeText(PaySDkCallActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

                                    api.sendReq(req);
                                }else{
                                    Toast.makeText(PaySDkCallActivity.this, "返回错误"+json1.getString("retmsg"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){

                            }


                    }

                }
            }catch (Exception e){

            }

        }
    };


    /**
     * 支付宝支付业务
     */
    public void payV(final String orderInfo){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaySDkCallActivity.this);
//                Log.d("lizisong", "orderInfo:"+orderInfo);
                Map<String, String> result = alipay.payV2(orderInfo, false);
//                Log.d("lizisong", "PAY:"+result.toString());


                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2(String title) {


        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = false;
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, orderid,ordername,orderprice,title);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey =  RSA_PRIVATE ;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;
        Log.d("lizisong", "orderInfo:"+orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaySDkCallActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.d("lizisong", "PAY:"+result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    String ret = null;
    public void payRetForRencai(){
//           Toast.makeText(PaySDkCallActivity.this, "couponid:"+couponid,Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "http://"+MyApplication.ip+"/api/userServiceOrdervManage.php?paystate="+state+"&resource_id="+id+"&order_id="+orderid+"&couponid="+couponid+"&mid="+mid+"&version="+MyApplication.version;
                    Log.d("lizisong", "payretForRencai:"+url);
                    ret = OkHttpUtils.loaudstringfromurl(url);
                    if(ret != null){
                        Message msg = Message.obtain();
                        msg.what = 10;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
    }

    public void payRetQingBao(){
//        Toast.makeText(PaySDkCallActivity.this, "couponid:"+couponid,Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                http://www.zhongkechuangxiang.com/api/getIndustryTeligOrder.php?paystate=1&goods_id=1&mid=2&order_id=201711022297769044
                String url = "http://"+MyApplication.ip+"/api/getIndustryTeligOrder.php?paystate="+state+"&order_id="+orderid+"&mid="+mid+"&couponid="+couponid+"&version="+MyApplication.version;
                Log.d("lizisong", "url:"+url);
                ret = OkHttpUtils.loaudstringfromurl(url);
                if(ret != null){
                    Message msg = Message.obtain();
                    msg.what = 10;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void payRetZhuanli(){
        String url="http://"+MyApplication.ip+"/webapp/api.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("c","patent");
        map.put("a","notify");
        map.put("order_id", orderid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,10,0);

    }

    public void payTestWeiXin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://www.zhongkechuangxiang.com/api/getIndustryTeligOrder.php?action=save&goods_id="+orderid+"&mid="+mid+"&pay_method=2";
                ret = OkHttpUtils.loaudstringfromurl(url);
                if(ret != null){
                    Message msg = Message.obtain();
                    msg.what = 11;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    @Override
    public void onSuccess() {
        Toast.makeText(PaySDkCallActivity.this, "微信支付成功了.....",Toast.LENGTH_LONG).show();
        state = "1";
        if(type == RENCAI_PAY_STATE){
            payRetForRencai();
        }else if(type == QINGBAO_PAY_STATE){
            payRetQingBao();
        }else if(type == ZHUANLI_PAY_STATE){
           //微信的支付回调放在这里
            payRetZhuanli();
        }else if(type == ZHUANLIGUANFEI_PAY_STATE){
            payRetZhuanli();
        }

    }

    @Override
    public void onFailed(String errMessage) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(PaySDkCallActivity.this, "微信支付失败了.....",Toast.LENGTH_LONG).show();
        state = "0";
        if(type == RENCAI_PAY_STATE){
            payRetForRencai();
        }else if(type == QINGBAO_PAY_STATE){
            payRetQingBao();
        }else if(type == ZHUANLI_PAY_STATE){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WXPayEntryActivity.WeiChatPay(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付");
        MobclickAgent.onPause(this);
    }
}
