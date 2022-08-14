package com.maidiantech;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.XingXuData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/2/28.
 */

public class XingquActivity extends AutoLayoutActivity {
    //电子信息:500  新材料:1000 生物技术:1500 节能环保:3000 先进制造:2000 文化创意:3500 化学化工:4000
    //新能源:2500 其他:4500
    TextView tiaoguo;
    TextView dianzixinxi;
    TextView shengwujishu;
    TextView xianjinzhizao;
    TextView jienenghuanbao;
    TextView xinnengyuan;
    TextView huaxuehuagong;
    TextView xincailiao;
    TextView wenhuachuanyi;
    String dianzixinxiStr="0",shengwujishuStr="0",xianjinzhizaoStr="0",jienenghuanbaoStr="0",xinnengyuanStr="0",huaxuehuagongStr="0",xincailiaoStr="0", wenhuachuanyiStr="0",qitaStr="0";
    TextView qita;
    TextView save;
    String ret;
    String evalue ="";
    TextView xinqu_back;
    private  String   ips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        StyleUtils1.initSystemBar(this);
        StyleUtils1.setResColorStyle(this, R.color.white);
        String id = getIntent().getStringExtra("id");
        ips = MyApplication.ip;
        tiaoguo = (TextView)findViewById(R.id.tiaoguo);
        xinqu_back=(TextView) findViewById(R.id.xinqu_back);
        dianzixinxi = (TextView)findViewById(R.id.dianzixinxi);
        shengwujishu =(TextView)findViewById(R.id.shengwujishu);
        xianjinzhizao = (TextView)findViewById(R.id.xianjinzhizao);
        jienenghuanbao = (TextView)findViewById(R.id.jienenghuanbao);
        xinnengyuan = (TextView)findViewById(R.id.xinnengyuan);
        huaxuehuagong = (TextView)findViewById(R.id.huaxuehuagong);
        xincailiao = (TextView)findViewById(R.id.xincailiao);
        wenhuachuanyi = (TextView)findViewById(R.id.wenhuachuanyi);
        qita =(TextView)findViewById(R.id.qita);
        save = (TextView)findViewById(R.id.save);

        try {
            if(id.equals("1")){
                tiaoguo.setVisibility(View.GONE);
                xinqu_back.setVisibility(View.VISIBLE);
                xinqu_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XingquActivity.this.finish();
                    }
                });
            }else{
                tiaoguo.setVisibility(View.VISIBLE);
                xinqu_back.setVisibility(View.GONE);

                tiaoguo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XingquActivity.this.finish();
                    }
                });
            }
        }catch (Exception e){

        }


        String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, "0");
        if(!code.equals("0")){
            dianzixinxi.setBackgroundResource(R.mipmap.dianzixingqu_h);
            dianzixinxiStr = code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");
        if(!code.equals("0")){
            shengwujishu.setBackgroundResource(R.mipmap.shengwuxingqu_h);
            shengwujishuStr = code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");
        if(!code.equals("0")){
            jienenghuanbao.setBackgroundResource(R.mipmap.jienengxingqu_h);
            jienenghuanbaoStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");
        if(!code.equals("0")){
            xianjinzhizao.setBackgroundResource(R.mipmap.xianjinxignqu_h);
            xianjinzhizaoStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");
        if(!code.equals("0")){
            xinnengyuan.setBackgroundResource(R.mipmap.xinnengyuanxingqu_h);
            xinnengyuanStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");
        if(!code.equals("0")){
            huaxuehuagong.setBackgroundResource(R.mipmap.huaxuexingqu_h);
            huaxuehuagongStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");
        if(!code.equals("0")){
            wenhuachuanyi.setBackgroundResource(R.mipmap.wenhuaxingqu_h);
            wenhuachuanyiStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");
        if(!code.equals("0")){
            xincailiao.setBackgroundResource(R.mipmap.xincailiaoxingqu_h);
            xincailiaoStr=code;
        }

        code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_QITA, "0");
        if(!code.equals("0")){
            qita.setBackgroundResource(R.mipmap.qitaxinqu_h);
            qitaStr=code;
        }





        dianzixinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,"500");
                    dianzixinxiStr="500";
                    dianzixinxi.setBackgroundResource(R.mipmap.dianzixingqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,"0");
                    dianzixinxiStr="0";
                    dianzixinxi.setBackgroundResource(R.mipmap.dianzixingqu);
                }
            }
        });

        shengwujishu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "1500");
                    shengwujishuStr="1500";
                    shengwujishu.setBackgroundResource(R.mipmap.shengwuxingqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");
                    shengwujishuStr="0";
                    shengwujishu.setBackgroundResource(R.mipmap.shengwuxingqu);
                }
            }
        });


        xianjinzhizao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "2000");
                    xianjinzhizaoStr = "2000";
                    xianjinzhizao.setBackgroundResource(R.mipmap.xianjinxignqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");
                    xianjinzhizaoStr = "0";
                    xianjinzhizao.setBackgroundResource(R.mipmap.xianjinxingqu);
                }
            }
        });

        jienenghuanbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "3000");
                    jienenghuanbaoStr = "3000";
                    jienenghuanbao.setBackgroundResource(R.mipmap.jienengxingqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");
                    jienenghuanbaoStr = "0";
                    jienenghuanbao.setBackgroundResource(R.mipmap.jienengxingqu);
                }
            }
        });

        xinnengyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "2500");
                    xinnengyuanStr="2500";
                    xinnengyuan.setBackgroundResource(R.mipmap.xinnengyuanxingqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");
                    xinnengyuanStr="0";
                    xinnengyuan.setBackgroundResource(R.mipmap.xinnengyuanxingqu);
                }
            }
        });
        huaxuehuagong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =  SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");
               if(code.equals("0")){
//                   SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "4000");
                   huaxuehuagongStr="4000";
                   huaxuehuagong.setBackgroundResource(R.mipmap.huaxuexingqu_h);
               } else{
//                   SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");
                   huaxuehuagongStr="0";
                   huaxuehuagong.setBackgroundResource(R.mipmap.huaxuexingqu);
               }
            }
        });

        xincailiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "1000");
                    xincailiaoStr="1000";
                    xincailiao.setBackgroundResource(R.mipmap.xincailiaoxingqu_h);
                }else {
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");
                    xincailiaoStr="0";
                    xincailiao.setBackgroundResource(R.mipmap.xincailiaoxingqu);
                }
            }
        });

        wenhuachuanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "3500");
                    wenhuachuanyiStr="3500";
                    wenhuachuanyi.setBackgroundResource(R.mipmap.wenhuaxingqu_h);
                }else {
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");
                    wenhuachuanyiStr="0";
                    wenhuachuanyi.setBackgroundResource(R.mipmap.wenhuaxingqu);
                }
            }
        });
        qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_QITA, "0");
                if(code.equals("0")){
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "4500");
                    qitaStr="4500";
                    qita.setBackgroundResource(R.mipmap.qitaxinqu_h);
                }else{
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "0");
                    qitaStr="0";
                    qita.setBackgroundResource(R.mipmap.qitaxingqu);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = dianzixinxiStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,"0")*/;
                if(!value.equals("0")){
                    evalue = value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,"0");
                }
                value = shengwujishuStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU,"0");
                }
                value = jienenghuanbaoStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO,"0");
                }

                value = xianjinzhizaoStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO,"0");
                }

                value = xinnengyuanStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINNENGYUAN,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");
                }

                value = huaxuehuagongStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, value);
                }else {
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");
                }

                value = wenhuachuanyiStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_WENHUACHUANYI,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI,"0");
                }

                value = xincailiaoStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINCAILIAO,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO,value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO,"0");
                }

                value = qitaStr/*SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_QITA,"0")*/;
                if(!value.equals("0")){
                    evalue += ","+value;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, value);
                }else{
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "0");
                }
                if(evalue == null || evalue.equals("0") || evalue.equals("")){
                    Toast.makeText(XingquActivity.this, "请选择兴趣领域", Toast.LENGTH_SHORT).show();
                    return ;
                }
                SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 1);
                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             String timestamp = System.currentTimeMillis()+"";
                             String sign = "";
                             ArrayList<String> sort = new ArrayList<String>();
                             String url = "http://"+ips+"/api/interestArea.php";
                             HashMap<String, String> hashMap = new HashMap<String, String>();
                             String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");


                             hashMap.put("interest", "save");
                             hashMap.put("mid", mid);
                             hashMap.put("evalue",evalue);
                             hashMap.put("version", MyApplication.version);
                             sort.add("interest"+save);
                             sort.add("mid"+mid);
                             sort.add("evalue"+evalue);
                             sort.add("timestamp"+timestamp);
                             sort.add("version"+MyApplication.version);
                             String accessid="";
                             String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                             if(loginState.equals("1")){
//                                 String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                                 accessid = mid;
                             }else{
                                 accessid = MyApplication.deviceid;
                             }
                             sort.add("accessid" + accessid);
                             sign = KeySort.keyScort(sort);
                             hashMap.put("sign",sign);
                             hashMap.put("timestamp", timestamp);
                             hashMap.put("accessid", accessid);
                             ret = OkHttpUtils.postkeyvlauspainr(url, hashMap);
                             if(ret != null){
                                 Message msg = Message.obtain();
                                 msg.what =1;
                                 myHandler.sendMessage(msg);
                             }
                         }catch (Exception e){
                             Message msg = Message.obtain();
                             msg.what =2;
                             myHandler.sendMessage(msg);
                         }

                     }
                 }).start();
            }
        });



    }
    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                XingXuData reslut =   gson.fromJson(ret, XingXuData.class);
                if(reslut.code.equals("1")){
                    Toast.makeText(XingquActivity.this, reslut.message,Toast.LENGTH_SHORT).show();
                    XingquActivity.this.finish();
                }
            }else if(msg.what == 2){
                Toast.makeText(XingquActivity.this, "提交数据失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
