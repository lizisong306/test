package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import adapter.RecommendAdapter;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Counts;
import entity.Renlist;
import entity.Sblist;
import entity.SearchEntry;
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
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/10/18.
 */

public class SearchContentResult extends AutoLayoutActivity {
    AutoCompleteTextView search;
    TextView sousuo;
    TextView tuijian,zixun,zhuanjia,xiangmu,zhengce,shebei,zhuanli;
    private String trim;
    private  int netWorkType;
    String redianStr,searchjsons;
    List<String> poslist = new ArrayList<>();
    private DisplayImageOptions options;
    RefreshListView listview;
    ImageView nocontent;
    ProgressBar progress;
    Searchcode searchcode;

    List<Zxlist> zixunlist;
    List<Renlist> rencailist;
    List<Sblist> shebeilist;
    List<Xmlist> xiangmulist;
    List<Zclist> zhengcelist;
    List<Zllist> zhuanlilist;
    List<Ztlist> zhuantilist;
    List<Shiyanshi> shiyanshilist;
    List<Tuijian> tuijianlist;
    List<SearchEntry> showList = new ArrayList<>();
    SearchAdapter adapter;
    int tabState =0;
    String typeid, lastModifiedTime="", key;
    boolean isLoading = false;
    boolean isClear = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchcontentresult);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        search = (AutoCompleteTextView)findViewById(R.id.search);
        sousuo = (TextView)findViewById(R.id.sousuo);
        tuijian = (TextView)findViewById(R.id.tuijian);
        zixun = (TextView)findViewById(R.id.zixun);
        zhuanjia = (TextView)findViewById(R.id.zhuanjia);
        zhengce =(TextView)findViewById(R.id.zhengce);
        xiangmu = (TextView)findViewById(R.id.xiangmu);
        shebei = (TextView)findViewById(R.id.shebei);
        zhuanli = (TextView)findViewById(R.id.zhuanli);
        nocontent = (ImageView)findViewById(R.id.nocontent);
        progress  = (ProgressBar)findViewById(R.id.progress);
        listview = (RefreshListView)findViewById(R.id.listview);
        options = ImageLoaderUtils.initOptions();
        trim = getIntent().getStringExtra("key");
        tabState = getIntent().getIntExtra("tabState", 0);
        setTab(tabState);

        search.setText(trim);
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
            }
        });
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(SearchContentResult.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(SearchContentResult.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }else if(TelNumMatch.issearch(trim) == false){
                            Toast.makeText(SearchContentResult.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                hintKbTwo();
                                boolean state = true;
                                for(int i=0; i<poslist.size();i++){
                                    String temp = poslist.get(i);
                                    if(temp.equals(trim)){
                                        state = false;
                                        break;
                                    }
                                }
                                if(state){
                                    if(trim != null){
                                        poslist.add(trim);
                                    }
                                }
                                String redianStr="";
                                int len = poslist.size();
                                for(int i=0;i<len; i++){
                                    String temp=poslist.get(i);
                                    if(i==len-1){
                                        redianStr=redianStr+temp;
                                    }else{
                                        redianStr=redianStr+temp+";";
                                    }
                                }
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
                                //调用接口
                                progress.setVisibility(View.VISIBLE);
                                isClear = true;
                                if(isLoading == true){
                                    return false;
                                }
                                isLoading = true;

                                if(tabState == 0){
//                                    showList.clear();
                                    adapter = null;
                                    getJson(trim);
                                }else if(tabState == 1){
                                    key=trim;
                                    typeid="1";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }else if(tabState == 2){
                                    key=trim;
                                    typeid="4";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }else if(tabState == 3){
                                    key=trim;
                                    typeid="2";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }else if(tabState == 4){
                                    key=trim;
                                    typeid="6";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }else if(tabState == 5){
                                    key=trim;
                                    typeid="7";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }else if(tabState == 6){
                                    key=trim;
                                    typeid="5";
                                    lastModifiedTime="";
//                                    showList.clear();
                                    adapter = null;
                                    getJsonType(key,typeid,lastModifiedTime);
                                }

                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
        if(redianStr != null && !redianStr.equals("")){
            String[] temp = redianStr.split(";");
            if(temp != null){
                for (int i=0; i<temp.length;i++){
                    String pos=temp[i];
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        poslist.add(pos);
                    }
                }
            }
        }
        nocontent.setVisibility(View.GONE);
        if(tabState == 0){
            tuijian.setTextColor(getResources().getColor(R.color.lansecolor));
            zixun.setTextColor(getResources().getColor(R.color.title));
            zhuanjia.setTextColor(getResources().getColor(R.color.title));
            xiangmu.setTextColor(getResources().getColor(R.color.title));
            zhengce.setTextColor(getResources().getColor(R.color.title));
            shebei.setTextColor(getResources().getColor(R.color.title));
            zhuanli.setTextColor(getResources().getColor(R.color.title));
        }
        tuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 0){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                tabState = 0;
                tuijian.setTextColor(getResources().getColor(R.color.lansecolor));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                adapter = null;
//                showList.clear();
                progress.setVisibility(View.VISIBLE);
                getJson(trim);
            }
        });

        zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 1){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                tabState = 1;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.lansecolor));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                key=trim;
                typeid="1";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);
            }
        });
        zhuanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 2){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                tabState = 2;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.lansecolor));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                key=trim;
                typeid="4";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);
            }
        });
        xiangmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 3){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                tabState = 3;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.lansecolor));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                key=trim;
                typeid="2";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);
            }
        });
        zhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 4){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isClear = true;
                isLoading = true;
                tabState = 4;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.lansecolor));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                key=trim;
                typeid="6";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);

            }
        });
        shebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 5){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                tabState = 5;
                isClear = true;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.lansecolor));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                key=trim;
                typeid="7";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);
            }
        });
        zhuanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabState == 6){
                    return;
                }
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                tabState = 6;
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.lansecolor));
                key=trim;
                typeid="5";
                lastModifiedTime="";
                progress.setVisibility(View.VISIBLE);
//                showList.clear();
                adapter = null;
                getJsonType(key,typeid,lastModifiedTime);
            }
        });
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {

                listview.setPullUpToRefreshable(true);
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                isClear = true;
                if(tabState == 1){
                    key=trim;
                    typeid="1";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 2){
                    key=trim;
                    typeid="4";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 3){
                    key=trim;
                    typeid="2";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 4){
                    key=trim;
                    typeid="6";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 5){
                    key=trim;
                    typeid="7";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 6){
                    key=trim;
                    typeid="5";
                    lastModifiedTime="";
                    progress.setVisibility(View.VISIBLE);
//                    showList.clear();
                    adapter = null;
                    getJsonType(key,typeid,lastModifiedTime);
                }
            }
        });

        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                if(isLoading == true){
                    return ;
                }
                isLoading = true;
                if(tabState == 1){
                    key=trim;
                    typeid="1";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 2){
                    key=trim;
                    typeid="4";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 3){
                    key=trim;
                    typeid="2";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 4){
                    key=trim;
                    typeid="6";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 5){
                    key=trim;
                    typeid="7";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }else if(tabState == 6){
                    key=trim;
                    typeid="5";
                    if(showList.size() > 1){
                        lastModifiedTime  = showList.get(showList.size()-1).id;
                    }else{
                        lastModifiedTime="";
                    }
                    getJsonType(key,typeid,lastModifiedTime);
                }
            }

        });

       progress.setVisibility(View.VISIBLE);
        if(tabState == 0){
            getJson(trim);
        }else{
            getJsonType(trim,typeid,lastModifiedTime);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoading = false;
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    /**
     *
     */
    private void getJson(final String key){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://"+MyApplication.ip+"/api/search.php?keyword="+key+"&version="+MyApplication.version;
                searchjsons = OkHttpUtils.loaudstringfromurl(url);
                if(searchjsons != null && !searchjsons.equals("")){
                      Message msg = Message.obtain();
                      msg.what =1;
                      handler.sendMessage(msg);
                    Message msg1 = Message.obtain();
                    msg1.what=2;
                    shandler2.sendMessageDelayed(msg1, 3000);

                }else{
                    isLoading = false;
                    isClear = false;
                }
            }
        }).start();
    }

    private void getJsonType(final String key,final String typeid, final String LastModifiedTime){

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sort.add("timestamp"+timestamp);
                sort.add("keyword"+timestamp);
                sort.add("pageSize"+"20");
                sort.add("typeid"+typeid);
                sort.add("sortid"+LastModifiedTime);
                sign= KeySort.keyScort(sort);
                String url = "http://"+MyApplication.ip+"/api/search.php?keyword="+key+"&pageSize=20&typeid="+typeid+"&sortid="+LastModifiedTime+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign"+sign;
                searchjsons = OkHttpUtils.loaudstringfromurl(url);
                if(searchjsons != null && !searchjsons.equals("")){
                    Message msg = Message.obtain();
                    msg.what =1;
                    handler.sendMessage(msg);

                    Message msg1 = Message.obtain();
                    msg1.what=2;
                    shandler2.sendMessageDelayed(msg1, 3000);

                }else{
                    isLoading = false;
                    isClear = false;
                }
            }
        }).start();
    }

    Handler shandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2){
                isLoading = false;
            }
        }
    };


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {

                    if(tabState == 0){
                        listview.setBackgroundColor(getResources().getColor(R.color.bg_sou));
                        listview.setPullDownToRefreshable(false);
                        listview.setPullUpToRefreshable(false);
                    }else{
//                        listview.setBackgroundColor(0xfff6f6f6);
                        listview.setBackgroundColor(getResources().getColor(R.color.hui));
                        listview.setPullDownToRefreshable(true);
                        listview.setPullUpToRefreshable(true);
                    }
                    listview.setPullDownToRefreshFinish();
                    listview.setPullUpToRefreshFinish();
                    progress.setVisibility(View.GONE);
                    if(isClear){
                        isClear = false;
                        showList.clear();
                    }
                    Gson gson = new Gson();
                    searchcode = gson.fromJson(searchjsons, Searchcode.class);
                    Counts data = searchcode.getData();
                    searchcount count = data.getCount();
                    searchresult result = data.getResult();
                    zixunlist = result.getZixun();
                    rencailist = result.getRencai();
                    xiangmulist = result.getXiangmu();
                    shebeilist = result.getShebei();
                    zhengcelist = result.getZhengce();
                    zhuanlilist = result.getZhuanli();
                    zhuantilist = result.getZhuanti();
                    shiyanshilist = result.getShiyanshi();
                    tuijianlist = result.getTuijian();
                    if(zixunlist != null){
                        if(zixunlist.size() >0){
                            if(tabState == 0){
                                SearchEntry zixuanheart = new SearchEntry();
                                zixuanheart.typename = "资讯";
                                zixuanheart.num =  count.getZixun().getNum();
                                zixuanheart.isheart = true;
                                showList.add(zixuanheart);
                            }

                            for(int i=0; i<zixunlist.size();i++){
                                SearchEntry zixuan = new SearchEntry();
                                Zxlist pos = zixunlist.get(i);
                                zixuan.typename = pos.getTypename();
                                zixuan.description = pos.getDescription();
                                zixuan.id = pos.getId();
                                zixuan.keywords = pos.getKeywords();
                                zixuan.litpic = pos.getLitpic();
                                zixuan.pubdate = pos.getPubdate();
                                zixuan.title = pos.getTitle();
                                zixuan.typeid = pos.getTypeid();
                                zixuan.zan = pos.getZan();
                                zixuan.click = pos.getClick();
                                showList.add(zixuan);
                            }
                            if(tabState == 0){
                                SearchEntry zixuanend = new SearchEntry();
                                zixuanend.typename = "资讯";
                                zixuanend.isend = true;
                                showList.add(zixuanend);
                            }
                        }
                    }
                    if(rencailist != null){
                        if(rencailist.size() > 0){
                            if(tabState == 0){
                                SearchEntry heart = new SearchEntry();
                                heart.typename = "专家";
                                heart.num =  count.getRencai().getNum();
                                heart.isheart = true;
                                showList.add(heart);
                            }
                            for(int i=0; i<rencailist.size();i++){
                                SearchEntry rencai = new SearchEntry();
                                Renlist item = rencailist.get(i);
                                rencai.description = item.getDescription();
                                rencai.id = item.getId();
                                rencai.keywords = item.getKeywords();
                                rencai.litpic = item.getLitpic();
                                rencai.pubdate = item.getPubdate();
                                rencai.title = item.getTitle();
                                rencai.typeid = item.getTypeid();
                                rencai.body = item.getBody();
                                rencai.rank = item.getRank();
                                rencai.typename = item.getTypename();
                                rencai.username = item.getUsername();
                                rencai.study_area = item.getStudy_area();
                                rencai.zan = item.getZan();
                                rencai.click = item.getClick();
                                rencai.area_cate = item.getArea_cate();
                                rencai.is_academician = item.getIs_academician();
                                showList.add(rencai);
                            }
                            if(tabState == 0){
                                SearchEntry rencaiend = new SearchEntry();
                                rencaiend.typename = "专家";
                                rencaiend.isend = true;
                                showList.add(rencaiend);
                            }
                        }
                    }

                    if(xiangmulist != null){
                        if(xiangmulist.size() >0){
                            if(tabState == 0){
                                SearchEntry heart = new SearchEntry();
                                heart.typename = "项目";
                                heart.num =  count.getXiangmu().getNum();
                                heart.isheart = true;
                                showList.add(heart);
                            }
                            for(int i=0;i<xiangmulist.size();i++){
                                SearchEntry xiangmu = new SearchEntry();
                                Xmlist item = xiangmulist.get(i);
                                xiangmu.description = item.getDescription();
                                xiangmu.id = item.getId();
                                xiangmu.keywords = item.getKeywords();
                                xiangmu.litpic = item.getLitpic();
                                xiangmu.pubdate = item.getPubdate();
                                xiangmu.title = item.getTitle();
                                xiangmu.typeid = item.getTypeid();
                                xiangmu.typename = item.getTypename();
                                xiangmu.zan = item.getZan();
                                xiangmu.click = item.getClick();
                                xiangmu.area_cate = item.getArea_cate();
                                showList.add(xiangmu);
                            }
                            if(tabState == 0){
                                SearchEntry xiangmuend = new SearchEntry();
                                xiangmuend.typename = "项目";
                                xiangmuend.isend = true;
                                showList.add(xiangmuend);
                            }
                        }

                    }
                    if(shebeilist != null){
                        if(shebeilist.size() >0){
                            if(tabState == 0){
                                SearchEntry heart = new SearchEntry();
                                heart.typename = "设备";
                                heart.num =  count.getShebei().getNum();
                                heart.isheart = true;
                                showList.add(heart);
                            }
                            for(int i=0;i<shebeilist.size();i++){
                                SearchEntry shebei = new SearchEntry();
                                Sblist item = shebeilist.get(i);
                                shebei.description = item.getDescription();
                                shebei.id = item.getId();
                                shebei.keywords = item.getKeywords();
                                shebei.litpic = item.getLitpic();
                                shebei.pubdate = item.getPubdate();
                                shebei.title=item.getTitle();
                                shebei.typeid = item.getTypeid();
                                shebei.typename = item.getTypename();
                                shebei.zan = item.getZan();
                                shebei.click =item.getClick();
                                shebei.area_cate = item.getArea_cate();
                                showList.add(shebei);
                            }
                            if(tabState == 0){
                                SearchEntry end = new SearchEntry();
                                end.typename = "设备";
                                end.isend = true;
                                showList.add(end);
                            }
                        }
                    }
                    if(zhengcelist != null){
                        if(zhengcelist.size() >0){
                            if(tabState == 0){
                                SearchEntry heart = new SearchEntry();
                                heart.typename = "政策";
                                heart.num =  count.getZhengce().getNum();
                                heart.isheart = true;
                                showList.add(heart);
                            }
                            for(int i=0;i<zhengcelist.size();i++){
                                SearchEntry zhengce = new SearchEntry();
                                Zclist item = zhengcelist.get(i);
                                zhengce.description = item.getDescription();
                                zhengce.id = item.getId();
                                zhengce.keywords = item.getKeywords();
                                zhengce.litpic = item.getLitpic();
                                zhengce.pubdate = item.getPubdate();
                                zhengce.title = item.getTitle();
                                zhengce.typeid = item.getTypeid();
                                zhengce.typename = item.getTypename();
                                zhengce.click = item.getClick();
                                zhengce.zan = item.zan;
                                showList.add(zhengce);
                            }
                            if(tabState == 0){
                                SearchEntry end = new SearchEntry();
                                end.typename = "政策";
                                end.isend = true;
                                showList.add(end);
                            }
                        }
                    }

                    if(zhuanlilist != null){
                        if(zhuanlilist.size() >0){
                            if(tabState == 0){
                                SearchEntry heart = new SearchEntry();
                                heart.typename = "专利";
                                heart.num =  count.getZhuanli().getNum();
                                heart.isheart = true;
                                showList.add(heart);
                            }
                            for(int i=0;i<zhuanlilist.size();i++){
                                SearchEntry zhuanli = new SearchEntry();
                                Zllist item = zhuanlilist.get(i);
                                zhuanli.description = item.getDescription();
                                zhuanli.id = item.getId();
                                zhuanli.keywords = item.getKeywords();
                                zhuanli.litpic = item.getLitpic();
                                zhuanli.pubdate = item.getPubdate();
                                zhuanli.title = item.getTitle();
                                zhuanli.typename = item.getTypename();
                                zhuanli.click = item.getClick();
                                zhuanli.typeid = item.getTypeid();
                                zhuanli.zan = item.getZan();
                                zhuanli.area_cate = item.getArea_cate();
                                showList.add(zhuanli);
                            }
                            if(tabState == 0){
                                SearchEntry end = new SearchEntry();
                                end.typename = "专利";
                                end.isend = true;
                                showList.add(end);
                            }
                        }
                    }
//                    if(shiyanshilist != null){
//                        if(shiyanshilist.size() > 0){
//                            if(tabState == 0){
//                                SearchEntry heart = new SearchEntry();
//                                heart.typename = "实验室";
//                                heart.num =  count.shiyanshi.num;
//                                heart.isheart = true;
//                                showList.add(heart);
//                            }
//                            for (int i=0;i<shiyanshilist.size();i++){
//                                SearchEntry shiyanshi = new SearchEntry();
//                                Shiyanshi item = shiyanshilist.get(i);
//                                shiyanshi.area_cate = item.getArea_cate();
//                                shiyanshi.click = item.getClick();
//                                shiyanshi.description = item.getDescription();
//                                shiyanshi.id = item.getId();
//                                shiyanshi.litpic = item.getLitpic();
//                                shiyanshi.pubdate = item.getPubdate();
//                                shiyanshi.title = item.getTitle();
//                                shiyanshi.typename = item.getTypename();
//                                shiyanshi.zan = item.getZan();
//                                showList.add(shiyanshi);
//                            }
//                            if(tabState == 0){
//                                SearchEntry end = new SearchEntry();
//                                end.typename = "实验室";
//                                end.isend = true;
//                                showList.add(end);
//                            }
//                        }
//                    }
                    if(showList.size() < 20){
                        listview.setPullUpToRefreshable(false);
                    }
                    if(adapter == null){
                        adapter = new SearchAdapter();
                        listview.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                    if(showList.size() > 0){
                        nocontent.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                    }else{
                        nocontent.setVisibility(View.VISIBLE);
                    }

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                SearchEntry item   =showList.get(position-1);
                                if(item.isend){
                                    if(isLoading == true){
                                        return ;
                                    }
                                    isLoading = true;
                                    if(item.typename.equals("资讯")){
                                       tabState = 1;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.lansecolor));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.title));
                                       xiangmu.setTextColor(getResources().getColor(R.color.title));
                                       zhengce.setTextColor(getResources().getColor(R.color.title));
                                       shebei.setTextColor(getResources().getColor(R.color.title));
                                       zhuanli.setTextColor(getResources().getColor(R.color.title));
                                       key=trim;
                                       typeid="1";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                    }else if(item.typename.equals("专家")){
                                       tabState = 2;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.title));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.lansecolor));
                                       xiangmu.setTextColor(getResources().getColor(R.color.title));
                                       zhengce.setTextColor(getResources().getColor(R.color.title));
                                       shebei.setTextColor(getResources().getColor(R.color.title));
                                       zhuanli.setTextColor(getResources().getColor(R.color.title));
                                       key=trim;
                                       typeid="4";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                   }else if(item.typename.equals("项目")){
                                       tabState = 3;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.title));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.title));
                                       xiangmu.setTextColor(getResources().getColor(R.color.lansecolor));
                                       zhengce.setTextColor(getResources().getColor(R.color.title));
                                       shebei.setTextColor(getResources().getColor(R.color.title));
                                       zhuanli.setTextColor(getResources().getColor(R.color.title));
                                       key=trim;
                                       typeid="2";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                   }else if(item.typename.equals("政策")){
                                       tabState = 4;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.title));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.title));
                                       xiangmu.setTextColor(getResources().getColor(R.color.title));
                                       zhengce.setTextColor(getResources().getColor(R.color.lansecolor));
                                       shebei.setTextColor(getResources().getColor(R.color.title));
                                       zhuanli.setTextColor(getResources().getColor(R.color.title));
                                       key=trim;
                                       typeid="6";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                   }else if(item.typename.equals("设备")){
                                       tabState = 5;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.title));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.title));
                                       xiangmu.setTextColor(getResources().getColor(R.color.title));
                                       zhengce.setTextColor(getResources().getColor(R.color.title));
                                       shebei.setTextColor(getResources().getColor(R.color.lansecolor));
                                       zhuanli.setTextColor(getResources().getColor(R.color.title));
                                       key=trim;
                                       typeid="7";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                   }else if(item.typename.equals("专利")){
                                       tabState = 6;
                                       tuijian.setTextColor(getResources().getColor(R.color.title));
                                       zixun.setTextColor(getResources().getColor(R.color.title));
                                       zhuanjia.setTextColor(getResources().getColor(R.color.title));
                                       xiangmu.setTextColor(getResources().getColor(R.color.title));
                                       zhengce.setTextColor(getResources().getColor(R.color.title));
                                       shebei.setTextColor(getResources().getColor(R.color.title));
                                       zhuanli.setTextColor(getResources().getColor(R.color.lansecolor));
                                       key=trim;
                                       typeid="5";
                                       lastModifiedTime="";
                                       progress.setVisibility(View.VISIBLE);
                                       showList.clear();
                                       adapter = null;
                                       getJsonType(key,typeid,lastModifiedTime);
                                   }
                                }else{
                                    if(item.typename.equals("专题")){
                                        Intent intent = new Intent(SearchContentResult.this, SpecialActivity.class);
                                        intent.putExtra("id", item.id);
                                        startActivity(intent);
                                    }else{
                                        String name = item.typename;
                                        if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                            Intent intent = new Intent(SearchContentResult.this, ZixunDetailsActivity.class);
                                            intent.putExtra("id", item.id);
                                            intent.putExtra("name", item.typename);
                                            intent.putExtra("pic", item.litpic);
                                            startActivity(intent);
                                        }else if(item.typename.equals("html") || item.typeid.equals("10000")){
                                            Intent intent=new Intent(SearchContentResult.this, WebViewActivity.class);
                                            intent.putExtra("title", item.title);
                                            intent.putExtra("url", item.detail);
                                            startActivity(intent);
                                        }else{
                                            if(item.typeid.equals("4")){
                                                Intent intent = new Intent(SearchContentResult.this, XinFanAnCeShi.class);
                                                intent.putExtra("aid",  item.id);
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(SearchContentResult.this, DetailsActivity.class);
                                                intent.putExtra("id",  item.id);
                                                intent.putExtra("name",item.typename);
                                                intent.putExtra("pic", item.litpic);
                                                startActivity(intent);
                                            }

                                        }
                                    }

                                }

                            }catch (Exception e){

                            }
                        }
                    });

                }catch (Exception e){
                    isLoading = false;
                }
            }

        }
    };
    class SearchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                if(tabState == 0){
                     convertView = View.inflate(SearchContentResult.this, R.layout.searchadapter, null);
                }else{
                    convertView = View.inflate(SearchContentResult.this, R.layout.searchadapterqita, null);
                }
                holdView.heart = (LinearLayout) convertView.findViewById(R.id.heart);
                holdView.type_name = (TextView)convertView.findViewById(R.id.type_name);
                holdView.count = (TextView)convertView.findViewById(R.id.count);

                holdView.end = (LinearLayout) convertView.findViewById(R.id.end);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                holdView.zx_layout = (LinearLayout) convertView.findViewById(R.id.zx_layout);
                holdView.zx_img = (ImageView)convertView.findViewById(R.id.zx_img);
                holdView.zx_zt = (ImageView)convertView.findViewById(R.id.zx_zt);
                holdView.zx_title = (TextView)convertView.findViewById(R.id.zx_title);
                holdView.zx_look =(TextView)convertView.findViewById(R.id.zx_look);

                holdView.zc_layout = (LinearLayout) convertView.findViewById(R.id.zc_layout);
                holdView.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
                holdView.zc_linyu = (TextView) convertView.findViewById(R.id.zc_linyu);
                holdView.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
                holdView.zc_look = (TextView) convertView.findViewById(R.id.zc_look);

                holdView.rc_layout = (LinearLayout) convertView.findViewById(R.id.rc_layout);
                holdView.rc_img = (RoundImageView)convertView.findViewById(R.id.rc_img);
                holdView.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);
                holdView.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
                holdView.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
                holdView.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
                holdView.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);

                holdView.xm_layout = (LinearLayout) convertView.findViewById(R.id.xm_layout);
                holdView.xm_img = (ImageView)convertView.findViewById(R.id.xm_img);
                holdView.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
                holdView.xm_linyu = (TextView) convertView.findViewById(R.id.xm_linyu);
                holdView.xm_description = (TextView) convertView.findViewById(R.id.xm_description);
                holdView.xm_name = (TextView) convertView.findViewById(R.id.xm_name);
                holdView.xm_look = (TextView) convertView.findViewById(R.id.xm_look);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            try {
                final SearchEntry item = showList.get(position);
                holdView.line.setVisibility(View.VISIBLE);
                if(item.isheart && tabState == 0){
                    holdView.zx_layout.setVisibility(View.GONE);
                    holdView.end.setVisibility(View.GONE);
                    holdView.zc_layout.setVisibility(View.GONE);
                    holdView.rc_layout.setVisibility(View.GONE);
                    holdView.xm_layout.setVisibility(View.GONE);
                    holdView.heart.setVisibility(View.VISIBLE);
                    if(item.typename.equals("专家")){
                        holdView.type_name.setText("专家");
                    }else{
                        holdView.type_name.setText(item.typename);
                    }
                    holdView.count.setText(item.num+"条");
                }else if(item.isend && tabState == 0){
                    holdView.zx_layout.setVisibility(View.GONE);
                    holdView.end.setVisibility(View.VISIBLE);
                    holdView.zc_layout.setVisibility(View.GONE);
                    holdView.rc_layout.setVisibility(View.GONE);
                    holdView.xm_layout.setVisibility(View.GONE);
                    holdView.heart.setVisibility(View.GONE);
                    holdView.line.setVisibility(View.GONE);
                }else if(item.typename.equals("资讯") || item.typename.equals("专题")){
                    holdView.zx_layout.setVisibility(View.VISIBLE);
                    holdView.end.setVisibility(View.GONE);
                    holdView.zc_layout.setVisibility(View.GONE);
                    holdView.rc_layout.setVisibility(View.GONE);
                    holdView.xm_layout.setVisibility(View.GONE);
                    holdView.heart.setVisibility(View.GONE);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.zx_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                , holdView.zx_img, options);
                    }else{
                        holdView.zx_img.setVisibility(View.GONE);
                    }
                    if(item.typename.equals("专题")){
                        holdView.zx_zt.setVisibility(View.VISIBLE);
                    }else {
                        holdView.zx_zt.setVisibility(View.GONE);
                    }
                    holdView.zx_title.setText(item.title);
                    holdView.zx_look.setText(item.click);
                }else if(item.typename.equals("政策")){
                    holdView.zx_layout.setVisibility(View.GONE);
                    holdView.end.setVisibility(View.GONE);
                    holdView.zc_layout.setVisibility(View.VISIBLE);
                    holdView.rc_layout.setVisibility(View.GONE);
                    holdView.xm_layout.setVisibility(View.GONE);
                    holdView.heart.setVisibility(View.GONE);
                    holdView.zc_title.setText(item.title);
                    if(item.area_cate != null){
                        holdView.zc_linyu.setText(item.area_cate.getArea_cate1());
                    }
                    holdView.zc_description.setText(item.description.replaceAll("\r\n", "").replaceAll("\n",""));
                    holdView.zc_look.setText(item.click);

                }else if(item.typename.equals("专家")){
                    holdView.zx_layout.setVisibility(View.GONE);
                    holdView.end.setVisibility(View.GONE);
                    holdView.zc_layout.setVisibility(View.GONE);
                    holdView.rc_layout.setVisibility(View.VISIBLE);
                    holdView.xm_layout.setVisibility(View.GONE);
                    holdView.heart.setVisibility(View.GONE);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.rc_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                , holdView.rc_img, options);
                    }else{
                        holdView.rc_img.setVisibility(View.GONE);
                    }
                    holdView.rc_uname.setText(item.title);
                    holdView.rc_zhicheng.setText(item.rank);
                    holdView.rc_title.setText(item.description);
                    if(item.area_cate != null){
                        holdView.rc_linyu.setText(item.area_cate.getArea_cate1());
                    }
                    holdView.rc_look.setText(item.click);

                }else {
                    holdView.zx_layout.setVisibility(View.GONE);
                    holdView.end.setVisibility(View.GONE);
                    holdView.zc_layout.setVisibility(View.GONE);
                    holdView.rc_layout.setVisibility(View.GONE);
                    holdView.xm_layout.setVisibility(View.VISIBLE);
                    holdView.heart.setVisibility(View.GONE);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.xm_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                , holdView.xm_img, options);
                    }else{
                        holdView.xm_img.setVisibility(View.GONE);
                    }
                    holdView.xm_title.setText(item.title);
                    if(item.area_cate != null){
                        holdView.xm_linyu.setText(item.area_cate.getArea_cate1());
                    }
                    holdView.xm_description.setText(item.description.replaceAll("\r\n","").replaceAll("\n",""));
                    holdView.xm_name.setText(item.typename);
                    holdView.xm_look.setText(item.click);

                }

            }catch (Exception e){

            }
            return convertView;
        }
        class HoldView{
            //头
            public LinearLayout heart;
            public TextView type_name;
            public TextView count;
            //尾
            public LinearLayout end;
            // 资讯
            public LinearLayout zx_layout;
            public ImageView zx_img,zx_zt;
            public TextView zx_title,zx_look;

            //政策
            public LinearLayout zc_layout;
            public TextView zc_title,zc_linyu,zc_description,zc_look;

            //人才
            public LinearLayout rc_layout;
            public RoundImageView rc_img;
            public TextView rc_uname;
            public TextView rc_zhicheng;
            public TextView rc_title;
            public TextView rc_look,rc_linyu;
            //项目
            public LinearLayout xm_layout;
            public ImageView xm_img;
            public TextView xm_title,xm_linyu,xm_description,xm_name,xm_look;
            //线
            public TextView line;

        }
    }

    public void setTab(int table){
        switch (table){
            case 0:
                tuijian.setTextColor(getResources().getColor(R.color.lansecolor));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 1:
                typeid="1";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.lansecolor));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 2:
                typeid="4";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.lansecolor));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 3:
                typeid="2";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.lansecolor));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 4:
                typeid="6";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.lansecolor));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 5:
                typeid="7";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.lansecolor));
                zhuanli.setTextColor(getResources().getColor(R.color.title));
                break;
            case 6:
                typeid="5";
                tuijian.setTextColor(getResources().getColor(R.color.title));
                zixun.setTextColor(getResources().getColor(R.color.title));
                zhuanjia.setTextColor(getResources().getColor(R.color.title));
                xiangmu.setTextColor(getResources().getColor(R.color.title));
                zhengce.setTextColor(getResources().getColor(R.color.title));
                shebei.setTextColor(getResources().getColor(R.color.title));
                zhuanli.setTextColor(getResources().getColor(R.color.lansecolor));
                break;
        }
    }

}
