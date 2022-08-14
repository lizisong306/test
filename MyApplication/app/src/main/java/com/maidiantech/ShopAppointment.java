package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import Util.OkHttpUtils;
import application.MyApplication;
import entity.Price;
import entity.RencaiPrice;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/20.
 */

public class ShopAppointment extends AutoLayoutActivity {
    ImageView back;
    TextView next;

    TextView price1,price2,price3,price4;
    public static boolean isFinish = false;
    String ret;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopappointment);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isFinish = false;
        back = (ImageView)findViewById(R.id.back);
        next = (TextView)findViewById(R.id.next);
        price1 = (TextView)findViewById(R.id.price1);
        price2 = (TextView)findViewById(R.id.price2);
        price3 = (TextView)findViewById(R.id.price3);
        price4 = (TextView)findViewById(R.id.price4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到下个界面
                Intent intent = new Intent(ShopAppointment.this, AppointmentSpecialist.class);
                startActivity(intent);
            }
        });
        getPrice();
    }
    public void getPrice(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://"+ MyApplication.ip+"/api/service_price.php";
                ret= OkHttpUtils.loaudstringfromurl(url);
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
            try {
                if(msg.what == 1){
                    Gson gson = new Gson();
                    RencaiPrice data = gson.fromJson(ret, RencaiPrice.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            for(int i=0; i<data.data.size();i++){
                                Price item =data.data.get(i);
                                if(item.id.equals("1")){
                                    price1.setText(item.original_price+"元");
                                }else if(item.id.equals("2")){
                                    price2.setText(item.original_price+"元");
                                }else if(item.id.equals("3")){
                                    price3.setText(item.original_price+"元");
                                }else if(item.id.equals("4")){
                                    price4.setText(item.original_price+"元");
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isFinish){
            isFinish = false;
            finish();
        }
    }
}
