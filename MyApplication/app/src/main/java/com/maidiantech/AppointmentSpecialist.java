package com.maidiantech;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.conn.scheme.HostNameResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import adapter.AppointmentSpecialistAdapter;
import application.MyApplication;
import entity.Posts;
import entity.YuYueRenCai;
import view.NiceSpinner;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/24.
 */

public class AppointmentSpecialist extends AutoLayoutActivity {
    NiceSpinner niceSpinner1;
    NiceSpinner niceSpinner2;
    HashMap<String, String> lingyumap = new HashMap<>();
    HashMap<String,String> rencaimap = new HashMap<>();
    AppointmentSpecialistAdapter adapter;
    ImageView back;
    RefreshListView listView;
    ProgressBar progress;
    int pagesize=10;
    String lastModifiedTime="";
    String area_cate="0";
    String rank ="0";
    String ret;
    YuYueRenCai rencai;
    List<Posts> postsList = new ArrayList<>();
    LinkedList<String> data=new LinkedList<>(Arrays.asList("不限", "电子信息", "新材料", "生物技术","先进制造","新能源","节能环保","文化创意","化工化学","其他"));
    LinkedList<String> data1=new LinkedList<>(Arrays.asList("不限", "院士", "研究员/教授/高工", "副研究员/副教授","博士/工程师","博士/工程师"));
    public static boolean isFinish = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointmentspecialist);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        isFinish = false;
        niceSpinner1 = (NiceSpinner)findViewById(R.id.nice_spinner1);
        niceSpinner2 = (NiceSpinner)findViewById(R.id.nice_spinner2);
        niceSpinner1.setTextColor(Color.BLACK);
        lingyumap.put("不限","0");
        lingyumap.put("电子信息","500");
        lingyumap.put("新材料","1000");
        lingyumap.put("生物技术","1500");
        lingyumap.put("先进制造","2000");
        lingyumap.put("新能源","2500");
        lingyumap.put("节能环保","3000");
        lingyumap.put("文化创意","3500");
        lingyumap.put("化工化学","4000");
        lingyumap.put("其他","4500");

        rencaimap.put("不限","0");
        rencaimap.put("院士","1");
        rencaimap.put("研究员/教授/高工","2");
        rencaimap.put("副研究员/副教授","3");
        rencaimap.put("博士/工程师","4");

        niceSpinner1.attachDataSource(data);
        niceSpinner2.setTextColor(Color.BLACK);
        niceSpinner2.attachDataSource(data1);
        niceSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area_cate = lingyumap.get(data.get(position));
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
                postsList.clear();
                if(adapter != null){
                    adapter.setCurrent(-1);
                }
                getJosn();
                Message msgs=Message.obtain();
                msgs.what=2;
                handler.sendMessageDelayed(msgs,5000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        niceSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rank = rencaimap.get(data1.get(position));
                lastModifiedTime="";
                if(adapter != null){
                   adapter.setCurrent(-1);
                }
                progress.setVisibility(View.VISIBLE);
                postsList.clear();
                getJosn();
                Message msgs=Message.obtain();
                msgs.what=2;
                handler.sendMessageDelayed(msgs,5000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        niceSpinner2.attachDataSource(data1);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        listView.setPullDownToRefreshable(true);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                listView.setPullUpToRefreshFinish();
                int  netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(AppointmentSpecialist.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();
                }else {
//                    lastModifiedTime ="";
//                    area_cate ="0";
//                    rank = "0";
//                    postsList.clear();
//                    getJosn();
                    handler.sendEmptyMessageDelayed(2, 1000);
                }
            }
        });

        listView.setPullUpToRefreshable(true);
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                int  netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(AppointmentSpecialist.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if(postsList.size()==0){
                    }else{
                        if (postsList.get(postsList.size() - 1).getResult() != null && postsList.get(postsList.size() - 1).getResult().equals("no")) {
                            Toast.makeText(AppointmentSpecialist.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                            listView.setPullUpToRefreshFinish();
                        }
                        lastModifiedTime = postsList.get(postsList.size() - 1).getSortTime();
                        area_cate = lingyumap.get(niceSpinner1.getText().toString());
                        rank = rencaimap.get(niceSpinner2.getText().toString());
                        getJosn();
                        Message msgs=Message.obtain();
                        msgs.what=2;
                        handler.sendMessageDelayed(msgs,5000);
                    }
                }
                }

        });

        int  netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(AppointmentSpecialist.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else {
            lastModifiedTime ="";
            area_cate ="0";
            rank = "0";
            progress.setVisibility(View.VISIBLE);
            getJosn();
            handler.sendEmptyMessageDelayed(2, 5000);
        }
    }


    private void getJosn(){
        String url = "http://"+ MyApplication.ip+"/api/service_list.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "20");
        map.put("typeid", "4");
        map.put("LastModifiedTime", lastModifiedTime);
        map.put("tag", "1");
        map.put("area_cate", area_cate);
        map.put("rank",rank);
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                    progress.setVisibility(View.GONE);
                    Gson gs = new Gson();
                    ret = (String)msg.obj;
                    rencai =gs.fromJson(ret, YuYueRenCai.class);
                    if(rencai != null){
                        if(rencai.code.equals("1")){
                            List<Posts> data  = rencai.data;
                            if(data != null){
                                for(int i=0; i<data.size(); i++){
                                    Posts item = data.get(i);
                                    postsList.add(item);
                                }
                            }
                            if(adapter == null){
                                adapter = new AppointmentSpecialistAdapter(AppointmentSpecialist.this, postsList);
                                listView.setAdapter(adapter);
                            }else{
                                adapter.setPostsListData(postsList);
                                adapter.notifyDataSetChanged();
                            }

                            adapter.notifyDataSetChanged();
                            listView.setPullDownToRefreshFinish();
                            listView.setPullUpToRefreshFinish();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    adapter.setCurrent(position-1);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }

                if(msg.what == 2){
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                    progress.setVisibility(View.GONE);
                }
            }catch (Exception e){

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("专家预约");
        MobclickAgent.onResume(this);
        if(isFinish){
            isFinish = false;
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("专家预约");
        MobclickAgent.onResume(this);
    }
}
