package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.tencent.mm.opensdk.utils.Log;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import entity.BGFONT;
import entity.Ret;
import entity.XuQiuBuChongData;
import entity.XuQiuBuChongEntity;
import entity.XuQiuBuChongValue;
import view.AutoLinefeedLayout;
import Util.NoDoubleClick;
/**
 * Created by Administrator on 2019/8/22.
 */

public class XuQiuBuChong extends AutoLayoutActivity {
    IndicatorSeekBar seekBar,seekbarjinji;
    ImageView backs;
    AutoLinefeedLayout hotView,hotView1;
    TextView queren,count;
    XuQiuBuChongEntity data;
    List<XuQiuBuChongValue> listhot1 = new ArrayList<>();
    List<XuQiuBuChongValue> listhot2 = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<String> code1 = new ArrayList<>();
    ArrayList<String> code2 = new ArrayList<>();
    String chengbenyusuan="";
    String jinjichengdu="";
    String id;
    int jinjichengxu=0;
    int progress=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.xuqiubuchong);
        seekBar = (IndicatorSeekBar)findViewById(R.id.seekbar);
        seekbarjinji = (IndicatorSeekBar)findViewById(R.id.seekbarjinji);
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);
        hotView1 = (AutoLinefeedLayout)findViewById(R.id.hotView1);
        queren = (TextView) findViewById(R.id.queren);
        count = (TextView)findViewById(R.id.count);
        id = getIntent().getStringExtra("id");
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                Log.d("lizisong", "onSeeking:"+seekParams.progress);
                int mod=seekParams.progress % 5;
                if(seekParams.progress % 10 ==0){
                    progress=seekParams.progress;
                    count.setText(seekParams.progress+"");
                    seekBar.setProgress(progress);
                }else if(mod ==0){
                    progress=seekParams.progress;
                    count.setText(seekParams.progress+"");
                    seekBar.setProgress(progress);
                }else if(mod >=0 && mod<3){
                    progress=(seekParams.progress-mod);
                    count.setText(progress+"");
                    seekBar.setProgress(progress);
                }else if(mod>=3 && mod <5){
                    progress=seekParams.progress+(5-mod);
                    count.setText(progress+"");
                    seekBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
//              seekBar.setProgress(progress);
//                Log.d("lizisong", "onStopTrackingTouch:"+seekBar.getProgress());
                if(progress % 10 ==0){
                    seekBar.setProgress(progress);
                }else if(progress % 5 ==0){
                    seekBar.setProgress(progress);
                }
            }
        });
        seekbarjinji.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                jinjichengxu = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        backs = (ImageView)findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queren.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                String url="http://" + MyApplication.ip +"/api/new_require.php";
                HashMap<String, String > map = new HashMap<>();
                map.put("method","xuqiubuchong");
                map.put("id",id);
                map.put("identity","0");
                if(code1.size() >0){
                    String  area_cate="";
                    for(int i=0; i<code1.size();i++){
                        String code = code1.get(i);
                        if(i==code1.size()-1){
                            area_cate=area_cate+code;
                        }else{
                            area_cate=area_cate+code+"，";
                        }
                    }
                    map.put("area_cate",area_cate);
                }
                if(code2.size()>0){
                    String hezuomoshi="";
                    for(int i=0;i<code2.size();i++){
                        String code =code2.get(i);
                        if(i==code2.size()-1){
                            hezuomoshi = hezuomoshi+code;
                        }else{
                            hezuomoshi=hezuomoshi+code+"，";
                        }                    }
                    map.put("hezuomoshi",hezuomoshi);
                }
                chengbenyusuan = count.getText().toString();
                map.put("chengbenyusuan",chengbenyusuan);
                if(jinjichengxu<=33){
                    map.put("jinjichengdu","1");
                }else if(jinjichengxu >33 && jinjichengxu<=66){
                    map.put("jinjichengdu","2");
                }else if(jinjichengxu>66 && jinjichengxu<100){
                    map.put("jinjichengdu","3");
                }else if(jinjichengxu == 100){
                    map.put("jinjichengdu","4");
                }else{
                    map.put("jinjichengdu","1");
                }
                NetworkCom networkCom = NetworkCom.getNetworkCom();
                networkCom.getJson(url,map,handler,2,0);

            }
        });
        getHot();

    }
    private void getHot(){
        String url="http://" + MyApplication.ip +"/api/new_require.php";
        HashMap<String, String > map = new HashMap<>();
        map.put("method","buchongxuanxiang");
        map.put("identity","0");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                 if(msg.what == 1){
                     String ret = (String) msg.obj;
                     Gson gson = new Gson();
                     data=gson.fromJson(ret, XuQiuBuChongEntity.class);
                     if(data != null){
                         if(data.code.equals("1")){
                             if(data.data != null &&data.data.size()>0){
                                 for(int i=0;i<data.data.size();i++){
                                     XuQiuBuChongData item = data.data.get(i);
                                     if(item.area_cate != null){
                                         listhot1= item.area_cate;
                                     }
                                     if(item.hezuomoshi != null){
                                         listhot2= item.hezuomoshi;
                                     }
                                 }

                             }
                             if(listhot1 != null && listhot1.size() >0){
                                 inithotView();
                             }
                             if(listhot2 != null && listhot2.size() >0){
                                 inithotView2();
                             }
                         }
                     }
                 }else if(msg.what == 2){
                     String ret = (String) msg.obj;
                     Gson gson = new Gson();
                     Log.d("lizisong", "ret:"+ret);
                     Ret ret1 = gson.fromJson(ret, Ret.class);
                     if(ret1.code.equals("1")){
                         FaBuActivity.states=1;
                         Intent intent = new Intent(XuQiuBuChong.this, FaBuActivity.class);
                         startActivity(intent);

                     }
                 }
            }catch (Exception e){

            }
        }
    };

    private void inithotView(){
        int size = listhot1.size(); // 添加Button的个数.
        if(size == 0){
            return;
        }
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        if(size > 0){
            int countwidht =0;
            for(int i = 0; i < size; i++){
                String item = listhot1.get(i).name;
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                if(countwidht > 4*MyApplication.widths){
                    break;
                }
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setBackgroundResource(R.drawable.shape_round_xx);
                tv.setText(item);
                tv.setTag(item);
                tv.setTextColor(0xff808080);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = ((TextView)v).getText().toString();
                        boolean state = false;
                        for(int i=0;i<list1.size();i++){
                            String item = list1.get(i);
                            if(txt.equals(item)){
                                state=true;
                                break;
                            }
                        }
                       String code = findCode(txt);
                        if(!state){
                            list1.add(txt);
                            code1.add(code);
                            ((TextView)v).setBackgroundResource(R.drawable.shape_round_xxx);
                            ((TextView)v).setTextColor(0xfffefefe);
                        }else{
                            ((TextView)v).setBackgroundResource(R.drawable.shape_round_xx);
                            ((TextView)v).setTextColor(0xff808080);
                            list1.remove(txt);
                            code1.remove(code);
                        }
                    }
                });
                hotView.addView(childBtn);
            }
        }
    }


    private void inithotView2(){
        int size = listhot2.size(); // 添加Button的个数.
        if(size == 0){
            return;
        }
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        if(size > 0){
            int countwidht =0;
            for(int i = 0; i < size; i++){
                String item = listhot2.get(i).name;
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                if(countwidht > 4*MyApplication.widths){
                    break;
                }
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setBackgroundResource(R.drawable.shape_round_xx);
                tv.setText(item);
                tv.setTag(item);
                tv.setTextColor(0xff808080);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = ((TextView)v).getText().toString();
                        boolean state = false;
                        for(int i=0;i<list2.size();i++){
                            String item = list2.get(i);
                            if(txt.equals(item)){
                                state=true;
                                break;
                            }
                        }
                       String code = findCode2(txt);
                        if(!state){
                            list2.add(txt);
                            code2.add(code);
                            ((TextView)v).setBackgroundResource(R.drawable.shape_round_xxx);
                            ((TextView)v).setTextColor(0xfffefefe);
                        }else{
                            ((TextView)v).setBackgroundResource(R.drawable.shape_round_xx);
                            ((TextView)v).setTextColor(0xff808080);
                            list2.remove(txt);
                            code2.remove(code);
                        }
                    }
                });
                hotView1.addView(childBtn);
            }
        }
    }
    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
    }


    private String findCode(String key){
        String code="";
        for (int i=0; i<listhot1.size();i++){
            XuQiuBuChongValue ii = listhot1.get(i);
            if(ii.name.equals(key)){
                code = ii.value;
                break;
            }
        }
        return code;
    }

    private String findCode2(String key){
        String code="";
        for (int i=0; i<listhot2.size();i++){
            XuQiuBuChongValue ii = listhot2.get(i);
            if(ii.name.equals(key)){
                code = ii.value;
                break;
            }
        }
        return code;
    }
}
