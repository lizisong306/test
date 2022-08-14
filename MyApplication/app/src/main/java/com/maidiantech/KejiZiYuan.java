package com.maidiantech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import java.util.HashMap;
import application.MyApplication;
import entity.Ret;
import entity.XiuGaiBaMaiEntity;
/**
 * Created by Administrator on 2019/9/25.
 */

public class KejiZiYuan extends AutoLayoutActivity {
    ImageView shezhi_backs;
    LinearLayout lay1,lay2,lay3;
    ImageView icon1,icon2,icon3;
    TextView text1,text2,text3;
    String state="1";
    String id;
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
        setContentView(R.layout.kejiziyuan);
        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);
        lay1 = (LinearLayout)findViewById(R.id.lay1);
        lay2 = (LinearLayout)findViewById(R.id.lay2);
        lay3 = (LinearLayout)findViewById(R.id.lay3);
        icon1 = (ImageView)findViewById(R.id.icon1);
        icon2 = (ImageView)findViewById(R.id.icon2);
        icon3 = (ImageView)findViewById(R.id.icon3);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        state = getIntent().getStringExtra("state");
        if(state == null){
            state="1";
        }
        id = getIntent().getStringExtra("id");
        if(state.equals("1")){
            icon1.setVisibility(View.VISIBLE);
            icon1.setImageResource(R.mipmap.selectsourceseectedmg);
        }else if(state.equals("2")){
            icon2.setVisibility(View.VISIBLE);
            icon2.setImageResource(R.mipmap.selectsourceseectedmg);
        }else if(state.equals("3")){
            icon3.setVisibility(View.VISIBLE);
            icon3.setImageResource(R.mipmap.selectsourceseectedmg);
        }
        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon1.setVisibility(View.VISIBLE);
                icon1.setImageResource(R.mipmap.selectsourceseectedmg);
                icon2.setVisibility(View.VISIBLE);
                icon2.setImageResource(R.mipmap.selectsourceunse);
                icon3.setVisibility(View.VISIBLE);
                icon3.setImageResource(R.mipmap.selectsourceunse);
                state="1";
                getJson();
            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon1.setVisibility(View.VISIBLE);
                icon1.setImageResource(R.mipmap.selectsourceunse);
                icon2.setVisibility(View.VISIBLE);
                icon2.setImageResource(R.mipmap.selectsourceseectedmg);
                icon3.setVisibility(View.VISIBLE);
                icon3.setImageResource(R.mipmap.selectsourceunse);
                state="2";
                getJson();
            }
        });
        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon1.setVisibility(View.VISIBLE);
                icon1.setImageResource(R.mipmap.selectsourceunse);
                icon2.setVisibility(View.VISIBLE);
                icon2.setImageResource(R.mipmap.selectsourceunse);
                icon3.setVisibility(View.VISIBLE);
                icon3.setImageResource(R.mipmap.selectsourceseectedmg);
                state="3";
                getJson();
            }
        });
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void getJson(){
        String url="http://"+ MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","set");
        map.put("id",id);
        map.put("tm", state);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                   try {
                       String ret =(String)msg.obj;
                       Gson gson = new Gson();
                       Ret data = gson.fromJson(ret, Ret.class);
                       if(data!= null){
                           if(data.code.equals("1")){
                               finish();
                           }
                       }
                   }catch(Exception e){

                  }
        }
    }
    };
}
