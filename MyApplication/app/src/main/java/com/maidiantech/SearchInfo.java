package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import adapter.SearchbiaoAdapter;
import application.MyApplication;
import entity.Counts;
import entity.Renlist;
import entity.Sblist;
import entity.Searchcode;
import entity.Shiyanshi;
import entity.Tuijian;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Ztlist;
import entity.Zxlist;
import entity.searchcount;
import entity.searchresult;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/11/5.
 */
public class SearchInfo extends AutoLayoutActivity {
    private List<Object> dataList;
    private List<String> typeList;
    private ListView search_listview_info;
    public static SearchInfo instance = null;
    private String searchjsons;
    private LinearLayout shuju_line;
    private ImageView shuju_img;
    String trim;
    Gson g;
    private  String   ips;
    private ProgressBar progress;
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
            progress.setVisibility(View.GONE);

        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            g =new Gson();
            if(msg.what==0){
                try {
                    Searchcode searchcode = g.fromJson(searchjsons, Searchcode.class);
                    Counts data = searchcode.getData();
                    searchcount count = data.getCount();
                    searchresult result = data.getResult();
                    if(result==null || result.equals("")){
                        Toast.makeText(SearchInfo.this, "没有相关数据", Toast.LENGTH_SHORT).show();
                        shuju_line.setVisibility(View.VISIBLE);
                        shuju_img.setVisibility(View.VISIBLE);
                    }
                    dataList =new ArrayList<>();
                    typeList = new ArrayList<>();
                    List<Zxlist> zixun = result.getZixun();
                    List<Renlist> rencai = result.getRencai();
                    List<Sblist> shebei = result.getShebei();
                    List<Xmlist> xiangmu = result.getXiangmu();
                    List<Zclist> zhengce = result.getZhengce();
                    List<Zllist> zhuanli = result.getZhuanli();
                    List<Ztlist> zhuanti = result.getZhuanti();
                    List<Shiyanshi> shiyanshi = result.getShiyanshi();
                    List<Tuijian> tuijian = result.getTuijian();
                    if(tuijian!=null && tuijian.size()>0) {
                        dataList.add(tuijian);
                        typeList.add("tuijian");
                    }
                    if(xiangmu!=null && xiangmu.size()>0) {
                        dataList.add(xiangmu);
                        typeList.add("xiangmu");
                    }
                    if(rencai!=null && rencai.size()>0) {
                        dataList.add(rencai);
                        typeList.add("rencai");
                    }
                    if(shiyanshi!=null && shiyanshi.size()>0) {
                        dataList.add(shiyanshi);
                        typeList.add("shiyanshi");
                    }
                    if(shebei!=null && shebei.size()>0) {
                        dataList.add(shebei);
                        typeList.add("shebei");
                    }
                    if(zhuanli!=null && zhuanli.size()>0) {
                        dataList.add(zhuanli);
                        typeList.add("zhuanli");
                    }
                    if(zixun!=null && zixun.size()>0) {
                        dataList.add(zixun);
                        typeList.add("zixun");
                    }
                    if(zhengce!=null && zhengce.size()>0) {
                        dataList.add(zhengce);
                        typeList.add("zhengce");
                    }
//                    if(zhuanti!=null) {
//                        dataList.add(zhuanti);
//                        typeList.add("zhuanti");
//                    }
                    // Log.i("dataList",dataList.size()+"......");
                    if(searchcode.getMessage().equals("获取信息成功！")) {
                        SearchbiaoAdapter searchadapter=new SearchbiaoAdapter(SearchInfo.this,dataList,typeList);
                         search_listview_info.setAdapter(searchadapter);
                    }
                }catch (Exception e){
                    Toast.makeText(SearchInfo.this, "没有相关数据", Toast.LENGTH_SHORT).show();
                    shuju_line.setVisibility(View.VISIBLE);
                    shuju_img.setVisibility(View.VISIBLE);
                }
            }

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        instance = this;
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        search_listview_info=(ListView) findViewById(R.id.search_listview_info);
        ImageView search_back=(ImageView) findViewById(R.id.search_back);
         shuju_line=(LinearLayout) findViewById(R.id.shuju_line) ;
         shuju_img=(ImageView) findViewById(R.id.shuju_img);
        progress=(ProgressBar) findViewById(R.id.progress);

        progress.setVisibility(View.VISIBLE);
       Intent intent = getIntent();
//        dataList= (List<Object>) intent.getSerializableExtra("datalist");
//        typeList= (List<String>) intent.getSerializableExtra("typelist");
        trim=intent.getStringExtra("trim");
        progress.setVisibility(View.VISIBLE);
//        UIHelper.showDialogForLoading(SearchInfo.this, "", true);
        gethistory();
//        SearchbiaoAdapter searchadapter=new SearchbiaoAdapter(this,dataList,typeList);
//        search_listview_info.setAdapter(searchadapter);
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void gethistory() {
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String timestamp = System.currentTimeMillis()+"";
                        String sign="";
                        ArrayList<String> sort = new ArrayList<String>();
                        sort.add("keyword"+trim);
                        sort.add("timestamp"+timestamp);
                        sort.add("version"+MyApplication.version);
                        String accessid="";
                        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                            accessid = mid;
                        }else{
                            accessid = MyApplication.deviceid;
                        }
                        sort.add("accessid" + accessid);
                        sign = KeySort.keyScort(sort);
                        MyApplication.setAccessid();
                        String searchjson="http://"+ips+"/api/search.php?keyword="+trim+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                        searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);

                        Message msg=new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                        Message message = Message.obtain();
                        message.what = 1;
                        dismissDialog.sendMessageDelayed(message, 500);
                    }catch (Exception e){

                    }


                }
            }.start();
        }catch (Exception e){
        }
    }
}