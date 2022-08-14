package com.maidiantech;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import entity.YouHuiJuanEntity;
import entity.YouHuiJuanXuanze;
import view.RefreshListView;
import Util.KeySort;
import Util.SharedPreferencesUtil;
import Util.OkHttpUtils;
import Util.TimeUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/12/14.
 */

public class XuanZeYouHuiJuanActivity extends AutoLayoutActivity {
    ImageView yujian_backs;
    RefreshListView listView;
    ProgressBar progressBar ;
    String typeid, aid,json;
    YouHuiJuanEntity data;

    View heart = null;
    XuanZeYouHuiJuanAdapter adapter;
    ImageView quxiao;
    public static int current = -1;
    String cate_id_1="",cate_id_2="";

    List<YouHuiJuanXuanze> listData = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuanzeyouhuijuanactivity);
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        typeid = getIntent().getStringExtra("typeid");
        aid   = getIntent().getStringExtra("aid");
        if(typeid.equals("5")){
            cate_id_1 = getIntent().getStringExtra("cate_id_1");
            cate_id_2 = getIntent().getStringExtra("cate_id_2");
        }
        progressBar.setVisibility(View.VISIBLE);
        heart = View.inflate(XuanZeYouHuiJuanActivity.this, R.layout.xuanzeyouhuijuanheart, null);
        quxiao = (ImageView)heart.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = -1;
                PayConfirmActivity.youhuijuanprice = "";
                PayConfirmActivity.couponid = "";
                if(adapter != null){
                   adapter.notifyDataSetChanged();
                }
                finish();
            }
        });
        adapter = new XuanZeYouHuiJuanAdapter();
        getJson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择优惠卷");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择优惠卷");
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
                sign = KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/discounts_coupon_list.php?mid="+mid+"&typeid="+typeid+"&aid="+aid+"&sign="+sign+"&cate_id_1="+cate_id_1+"&cate_id_2="+cate_id_2+"&timestamp="+timestamp+MyApplication.accessid+"&version="+MyApplication.version;
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
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                data=gson.fromJson(json, YouHuiJuanEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                       if(data.data.flow != null){
                           for (int i=0; i<data.data.flow.size(); i++){
                               YouHuiJuanXuanze item = data.data.flow.get(i);
                               listData.add(item);
                           }
                       }
                    }
                }
                listView.addHeaderView(heart);
                listView.setAdapter(adapter);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        current = position;
//                        adapter.notifyDataSetChanged();
//                        finish();
//                    }
//                });
            }
        }
    };

    class XuanZeYouHuiJuanAdapter extends BaseAdapter{

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(XuanZeYouHuiJuanActivity.this, R.layout.xuanzeyouhuijuanadapter, null);
                holdView.youhuijuan = (RelativeLayout) convertView.findViewById(R.id.youhuijuan);
                holdView.youhuijuan_title = (TextView)convertView.findViewById(R.id.youhuijuan_title);
                holdView.youhuijuan_date = (TextView)convertView.findViewById(R.id.youhuijuan_date);
                holdView.youhuijuan_price = (TextView)convertView.findViewById(R.id.youhuijuan_price);
                holdView.youhuijuan_fanwei = (TextView)convertView.findViewById(R.id.youhuijuan_fanwei);
                holdView.youhuijuan_overdue = (TextView)convertView.findViewById(R.id.youhuijuan_overdue);
                holdView.youhuijuan_danwei = (TextView)convertView.findViewById(R.id.youhuijuan_danwei);
                holdView.xuanzeyouhuijuan = (ImageView)convertView.findViewById(R.id.xuanzeyouhuijuan);
                holdView.end = (RelativeLayout)convertView.findViewById(R.id.end);
                holdView.heart = (RelativeLayout)convertView.findViewById(R.id.heart);
                holdView.yuan = (ImageView)convertView.findViewById(R.id.yuan);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            holdView.xuanzeyouhuijuan.setVisibility(View.GONE);
            final YouHuiJuanXuanze item = listData.get(position);
            if(item.keyong.equals("1")){
                holdView.youhuijuan_title.setText(item.title);
                holdView.youhuijuan_price.setText(item.moneys);
                String date ="";
                date = TimeUtils.getTimes(item.collectdate);
                date = date+"至"+ TimeUtils.getTimes(item.enddate);
                holdView.youhuijuan_date.setText(date);
                holdView.youhuijuan_date.setTextColor(0xff3e3e3e);
                holdView.youhuijuan_price.setTextColor(0xffff6363);
                holdView.youhuijuan_fanwei.setTextColor(0xff3e3e3e);
                holdView.youhuijuan_danwei.setTextColor(0xffff6363);
                holdView.youhuijuan_title.setTextColor(0xff3e3e3e);
                holdView.youhuijuan_fanwei.setText("使用范围:"+item.no_scope);
                if(item.color != null && item.color.equals("1") ){
                    holdView.heart.setBackgroundResource(R.mipmap.youhuiquan_heart_1);
                    holdView.end.setBackgroundResource(R.mipmap.youhuiquan_end_1);
                    holdView.yuan.setBackgroundResource(R.mipmap.youhuiquan_yuan_1);
                    holdView.youhuijuan_title.setTextColor(0xffffc169);
                    holdView.youhuijuan_date.setTextColor(0xffff9d9d);
                    holdView.youhuijuan_price.setTextColor(0xffffc169);
                    holdView.youhuijuan_danwei.setTextColor(0xffffc169);
                    holdView.youhuijuan_fanwei.setTextColor(0xffff9d9d);

                }else{
                    holdView.heart.setBackgroundResource(R.mipmap.youhuiquan_heart);
                    holdView.end.setBackgroundResource(R.mipmap.youhuiquan_end);
                    holdView.yuan.setBackgroundResource(R.mipmap.youhuiquan_yuan);
                    holdView.youhuijuan_title.setTextColor(0xff3e3e3e);
                    holdView.youhuijuan_date.setTextColor(0xff3e3e3e);
                    holdView.youhuijuan_price.setTextColor(0xffff6363);
                    holdView.youhuijuan_danwei.setTextColor(0xffff6363);
                    holdView.youhuijuan_fanwei.setTextColor(0xff3e3e3e);

                }

            }else{
                holdView.heart.setBackgroundResource(R.mipmap.youhuiquan_heart);
                holdView.end.setBackgroundResource(R.mipmap.youhuiquan_end);
                holdView.yuan.setBackgroundResource(R.mipmap.youhuiquan_yuan);
                holdView.youhuijuan_title.setText(item.title);
                holdView.youhuijuan_price.setText(item.moneys);
                String date ="";
                date = TimeUtils.getTimes(item.collectdate);
                date = date+"至"+ TimeUtils.getTimes(item.enddate);
                holdView.youhuijuan_date.setText(date);
                holdView.youhuijuan_date.setTextColor(0xffbbbbbb);
                holdView.youhuijuan_price.setTextColor(0xffbbbbbb);
                holdView.youhuijuan_fanwei.setTextColor(0xffbbbbbb);
                holdView.youhuijuan_title.setTextColor(0xffbbbbbb);
                holdView.youhuijuan_danwei.setTextColor(0xffbbbbbb);
                holdView.youhuijuan_fanwei.setText("使用范围:"+item.no_scope);
                holdView.youhuijuan_fanwei.setTextColor(0xffff6363);
            }

            if(item.keyong.equals("1")){
                if(current == position){
                    holdView.xuanzeyouhuijuan.setVisibility(View.VISIBLE);
                    PayConfirmActivity.youhuijuanprice = item.moneys;
                    PayConfirmActivity.couponid = item.id;
                }else{
                    holdView.xuanzeyouhuijuan.setVisibility(View.GONE);
                }
            }
            holdView.youhuijuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.keyong.equals("1")){
                        current = position;
                        PayConfirmActivity.couponid = item.id;
//                        Toast.makeText(XuanZeYouHuiJuanActivity.this,"item.id:"+item.id,Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        XuanZeYouHuiJuanActivity.this.finish();
                    }else{
                        Toast.makeText(XuanZeYouHuiJuanActivity.this, "不可用", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return convertView;
        }
        class HoldView{
            //优惠卷
            public RelativeLayout youhuijuan;
            public TextView youhuijuan_title,youhuijuan_date,youhuijuan_price,youhuijuan_fanwei,youhuijuan_overdue,youhuijuan_danwei;
            public ImageView xuanzeyouhuijuan;
            public RelativeLayout heart,end;
            public ImageView yuan;
        }
    }


}
