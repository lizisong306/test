package com.maidiantech;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.MaiDianYuYue;
import entity.RetPulseData;
import entity.YuYueData;
import view.BTAlertDialog;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/29.
 */

public class My_yuyue extends AutoLayoutActivity {
    RefreshListView listView;
    MaiDianYuYue maiDianYuYue;
    List<YuYueData> listData ;
    YuYueAdapter mYuYueAdapter;
    OkHttpUtils okHttpUtils;
    String ret;
    HashMap<String, String> map = new HashMap<String, String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyue);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        okHttpUtils = OkHttpUtils.getInstancesOkHttp();
        maiDianYuYue = MaiDianYuYue.getInstance(this);

        ImageView yujian_backs=(ImageView) findViewById(R.id.yujian_backs);
        RelativeLayout yuyue_img=(RelativeLayout) findViewById(R.id.yuyue_img);
        ImageView yu_img=(ImageView) findViewById(R.id.yu_img);
        listView = (RefreshListView)findViewById(R.id.listview);
        listData = maiDianYuYue.get();
        if(listData.size()==0){
            yuyue_img.setVisibility(View.VISIBLE);
            yu_img.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            yuyue_img.setVisibility(View.GONE);
            yu_img.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mYuYueAdapter = new YuYueAdapter();

        listView.setAdapter(mYuYueAdapter);

    }

    class YuYueAdapter extends BaseAdapter{

        private DisplayImageOptions options;
        private ImageLoader imageLoader;
        private  String   ips;
        public YuYueAdapter(){
            options = ImageLoaderUtils.initOptions();
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            final YuYueData item = listData.get(position);
            if(convertView == null){
                holder = new HolderView();
                convertView = View.inflate(My_yuyue.this, R.layout.my_yuyue, null);
                holder.rencai = (LinearLayout) convertView.findViewById(R.id.rencai);
                holder.rencai_icon = (ImageView)convertView.findViewById(R.id.rencai_icon);
                holder.rencai_name = (TextView)convertView.findViewById(R.id.rencai_name);
                holder.rencai_zhicheng =(TextView)convertView.findViewById(R.id.rencai_zhicheng);
                holder.lingye = (TextView)convertView.findViewById(R.id.lingye);
                holder.rencai_time = (TextView)convertView.findViewById(R.id.rencai_time);
                holder.rencai_adress = (TextView)convertView.findViewById(R.id.rencai_adress);
                holder.rencai_title = (TextView)convertView.findViewById(R.id.rencai_title);
                holder.rencai_updata = (TextView)convertView.findViewById(R.id.rencai_updata);
                holder.rencai_remind = (Button)convertView.findViewById(R.id.rencai_remind);
                holder.rencai_cencel =(Button)convertView.findViewById(R.id.rencai_cencel);
                holder.yanjiulingyu =(LinearLayout)convertView.findViewById(R.id.yanjiulingyu);

                holder.qita = (LinearLayout)convertView.findViewById(R.id.qita);
                holder.qita_title = (TextView)convertView.findViewById(R.id.qita_title);
                holder.qita_model = (LinearLayout)convertView.findViewById(R.id.qita_model);
                holder.qita_modelinfo = (TextView)convertView.findViewById(R.id.qita_modelinfo);
                holder.qita_lianxiren = (TextView)convertView.findViewById(R.id.qita_lianxiren);
                holder.qita_lianxidianhua = (TextView)convertView.findViewById(R.id.qita_lianxidianhua);
                holder.qita_postdata = (TextView)convertView.findViewById(R.id.qita_postdata);
                holder.qita_update = (TextView)convertView.findViewById(R.id.qita_update);
                holder.qita_remind = (Button)convertView.findViewById(R.id.qita_remind);
                holder.qita_cencel =(Button)convertView.findViewById(R.id.qita_cencel);
                holder.qita_time = (LinearLayout)convertView.findViewById(R.id.qita_time);
                holder.model_line = (LinearLayout)convertView.findViewById(R.id.model_line);
                convertView.setTag(holder);

            }else{
              holder = (HolderView)convertView.getTag();
            }
            if(item.typeid.equals("4")){//人才
                holder.qita.setVisibility(View.GONE);
                holder.rencai.setVisibility(View.VISIBLE);
                imageLoader.displayImage(item.pic
                        , holder.rencai_icon, options);
                holder.rencai_name.setText(item.title);
                holder.rencai_zhicheng.setText(item.rank);
                if(item.lingyu == null || (item.lingyu != null && item.lingyu.equals(""))){
                    holder.yanjiulingyu.setVisibility(View.GONE);
                }
                holder.lingye.setText(item.lingyu);
                holder.rencai_time.setText(item.meetTime);
                holder.rencai_adress.setText(item.meetPost);
                holder.rencai_title.setText(item.meetTitle);
                String data = "";
                data = TimeUtils.getFormatedDateTime("yyyy-MM-dd", Long.parseLong(item.update));
                holder.rencai_updata.setText(data);
                ips = MyApplication.ip;
                holder.rencai_remind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                        map.put("mid", mid);
//                        map.put("method","alter");
//                        map.put("id",item.meetAdress);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyApplication.setAccessid();
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                sort.add("mid"+mid);
                                sort.add("id"+item.meetAdress);
                                sort.add("method"+"alert");
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);

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

                                String url = "http://"+ips+"/api/appointment.php?mid="+mid+"&id="+item.meetAdress+"&method=alert"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                                try {
                                    ret =  okHttpUtils.Myokhttpclient(url);
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
                });
                holder.rencai_cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final BTAlertDialog dialog = new BTAlertDialog(My_yuyue.this);
                        dialog.setTitle("您确认要取消预约吗？");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listData.remove(item);
                                maiDianYuYue.delete(item.id);
                                mYuYueAdapter.notifyDataSetChanged();
                                map.clear();
                                MyApplication.setAccessid();
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                map.put("mid", mid);
                                map.put("method","cancel");
                                map.put("id",item.meetAdress);
                                map.put("version", MyApplication.version);
                                sort.add("mid"+mid);
                                sort.add("method"+"cancel");
                                sort.add("id"+item.meetAdress);
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign=KeySort.keyScort(sort);
                                map.put("sign",sign);
                                map.put("timestamp",timestamp);
                                map.put("accessid",accessid);
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
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });

            }else if(item.typeid.equals("7")){
                holder.rencai.setVisibility(View.GONE);
                holder.qita_time.setVisibility(View.GONE);
                holder.qita.setVisibility(View.VISIBLE);
                holder.qita_model.setVisibility(View.VISIBLE);
                holder.qita_title.setText(item.title);
                holder.qita_postdata.setText(item.meetPost);

                if((item.model == null) || (item.model != null && item.model.equals(""))){
                    holder.model_line.setVisibility(View.GONE);
                }
                holder.qita_modelinfo.setText(item.model);
                holder.qita_lianxiren.setText(item.meetMen);
                holder.qita_lianxidianhua.setText(item.meetTel);
                String data = "";
                data = TimeUtils.getFormatedDateTime("yyyy-MM-dd", Long.parseLong(item.update));
                holder.qita_update.setText(data);
                holder.qita_remind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      final  String  mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                        map.put("mid", mid);
//                        map.put("method","alter");
//                        map.put("id",item.meetAdress);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyApplication.setAccessid();
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                sort.add("mid"+mid);
                                sort.add("id"+item.meetAdress);
                                sort.add("method"+"alert");
                                sort.add("timestamp"+timestamp);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign=KeySort.keyScort(sort);

                                String url = "http://"+ips+"/api/appointment.php?mid="+mid+"&id="+item.meetAdress+"&method=alert"+"&sign="+sign+"&timestamp="+timestamp+MyApplication.accessid;                                try {
                                    ret =  okHttpUtils.Myokhttpclient(url);
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
                });
                holder.qita_cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final BTAlertDialog dialog = new BTAlertDialog(My_yuyue.this);
                        dialog.setTitle("您确认要取消预约吗？");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listData.remove(item);
                                maiDianYuYue.delete(item.id);
                                mYuYueAdapter.notifyDataSetChanged();
                                map.clear();
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                map.put("mid", mid);
                                map.put("method","cancel");
                                map.put("id",item.meetAdress);
                                map.put("version", MyApplication.version);
                                sort.add("mid"+mid);
                                sort.add("method"+"cancel");
                                sort.add("id"+item.meetAdress);
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign = KeySort.keyScort(sort);
                                map.put("timestamp", timestamp);
                                map.put("sign", sign);
                                map.put("accessid", accessid);
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
                                dialog.dismiss();
                            }
                        });
                        dialog.show();


                    }
                });


            }else {
                holder.rencai.setVisibility(View.GONE);
                holder.qita_time.setVisibility(View.GONE);
                holder.qita_model.setVisibility(View.GONE);
                holder.qita.setVisibility(View.VISIBLE);
                holder.qita_model.setVisibility(View.VISIBLE);
                holder.qita_title.setText(item.title);
                holder.model_line.setVisibility(View.GONE);
                holder.qita_modelinfo.setText(item.model);
                holder.qita_lianxiren.setText(item.meetMen);
                holder.qita_lianxidianhua.setText(item.meetTel);
                holder.qita_postdata.setText(item.meetPost);
                String data = "";
                data = TimeUtils.getFormatedDateTime("yyyy-MM-dd", Long.parseLong(item.update));
                holder.qita_update.setText(data);
                holder.qita_remind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        map.clear();
                       final String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                        map.put("mid", mid);
//                        map.put("method","alter");
//                        map.put("id",item.meetAdress);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                sort.add("mid"+mid);
                                sort.add("id"+item.meetAdress);
                                sort.add("method"+"alert");
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign=KeySort.keyScort(sort);
                                MyApplication.setAccessid();
                                String url = "http://"+ips+"/api/appointment.php?mid="+mid+"&id="+item.meetAdress+"&method=alert"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                                try {
                                    ret =  okHttpUtils.Myokhttpclient(url);
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
                });
                holder.qita_cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final BTAlertDialog dialog = new BTAlertDialog(My_yuyue.this);
                        dialog.setTitle("您确认要取消预约吗？");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listData.remove(item);
                                maiDianYuYue.delete(item.id);
                                mYuYueAdapter.notifyDataSetChanged();
                                map.clear();
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                map.put("mid", mid);
                                map.put("method","cancel");
                                map.put("id",item.meetAdress);
                                map.put("version", MyApplication.version);
                                sort.add("mid"+mid);
                                sort.add("method"+"cancel");
                                sort.add("id"+item.meetAdress);
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign = KeySort.keyScort(sort);
                                map.put("sign",sign);
                                map.put("timestamp",timestamp);
                                map.put("accessid",accessid);
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
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });



            }
            return convertView;
        }
        class HolderView{
            //人才
            LinearLayout rencai;
            LinearLayout yanjiulingyu;
            ImageView rencai_icon;
            TextView rencai_name;
            TextView rencai_zhicheng;
            TextView lingye;
            TextView rencai_time;
            TextView rencai_adress;
            TextView rencai_title;
            TextView rencai_updata;
            Button rencai_remind;
            Button rencai_cencel;

            //其他
            LinearLayout qita;
            LinearLayout qita_time;
            TextView qita_title;
            LinearLayout qita_model;
            LinearLayout model_line;
            TextView qita_modelinfo;
            TextView qita_lianxiren;
            TextView qita_lianxidianhua;
            TextView qita_postdata;
            TextView qita_update;
            Button   qita_remind;
            Button qita_cencel;

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
                    Toast.makeText(My_yuyue.this, result.message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(My_yuyue.this, result.message, Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == 2){
                Gson g=new Gson();
                RetPulseData result =g.fromJson(ret, RetPulseData.class);
                if(result.code.equals("1")){
                    Toast.makeText(My_yuyue.this, result.message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(My_yuyue.this, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}
