package com.maidiantech;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.NewSearchData;
import entity.NewSearchEntity;
import entity.NewSearchHistoryEntiy;
import entity.PostData;
import entity.Posts;
import entity.SearchEntry;
import Util.NetUtils;
import Util.TelNumMatch;
import Util.SharedPreferencesUtil;
import view.AutoLinefeedLayout;
import view.HorizontalListView;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.SystemBarTintManager;

import static com.maidiantech.MainActivity.isAllScreenDevice;

/**
 * Created by Administrator on 2018/9/21.
 */

public class NewSearchContent extends AutoLayoutActivity {
    RelativeLayout title;
    EditText edit;
    TextView cencel;
    TextView tuijian,zixun,zhuanjia,xiangmu,shebei,zhuanli,zhengce,bottmon_title,keyword,commit,hottitle,hotline;
    RefreshListView listview;
    List<String> poslist = new ArrayList<>();
    String key;
    NewSearchEntity data;
    HorizontalListView horlist;
    LinearLayout nodate;
    AutoLinefeedLayout hotView;
    NewHotAdapter hotAdapter;
    SearchAdapter adapter;
    ListView listguanxi;
    Heart heartAdapter;
    ImageView closesousuo;
    String typeid ="";
    int tabState = 0;
    boolean isLoading = false;
    boolean isClear = false;
    boolean isNodata = false;
    boolean isShowXiangshi = false;
    boolean isHeart = false;
    ProgressBar progress;
    public List<String> keywords = new ArrayList<>();
    public List<TitleKey> titlekeys = new ArrayList();
    List<SearchEntry> showList = new ArrayList<>();
    List<String> xiangshi = new ArrayList<>();
    private DisplayImageOptions options;
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
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
////            window.setNavigationBarColor(Color.BLACK);
//        }
        setContentView(R.layout.newsearchcontent);
        NewSearchHistory.isClose = false;
        options = ImageLoaderUtils.initOptions();
        key = getIntent().getStringExtra("hot");
        typeid = getIntent().getStringExtra("typeid");
//        StyleUtils.initSystemBar(this);
////        //设置状态栏是否沉浸
//        StyleUtils.setStyle(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//            tintManager.setStatusBarAlpha(0);
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1

//        MIUISetStatusBarLightMode(getWindow(), true);
        horlist = (HorizontalListView)findViewById(R.id.horlist);
        tuijian = (TextView)findViewById(R.id.tuijian);
        zixun = (TextView)findViewById(R.id.zixun);
        zhuanjia = (TextView)findViewById(R.id.zhuanjia);
        edit = (EditText)findViewById(R.id.edit);
        listguanxi = (ListView)findViewById(R.id.listguanxi);
        listguanxi.setVisibility(View.GONE);
        keyword = (TextView)findViewById(R.id.keyword);
        hottitle =(TextView)findViewById(R.id.hottitle);
        hotline = (TextView)findViewById(R.id.hotline);
        commit = (TextView)findViewById(R.id.commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteXuQiu.entry_address =14;
                Intent intent = new Intent(NewSearchContent.this, NewXuQiu.class);
                startActivity(intent);
            }
        });
        xiangmu = (TextView)findViewById(R.id.xiangmu);
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);

        shebei = (TextView)findViewById(R.id.shebei);
        zhuanli =(TextView)findViewById(R.id.zhuanli);
        zhengce = (TextView)findViewById(R.id.zhengce);
        nodate = (LinearLayout)findViewById(R.id.nodate);
        closesousuo = (ImageView)findViewById(R.id.closesousuo);
        closesousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText("");
                finish();
                overridePendingTransition(0, 0);
            }
        });

        nodate.setVisibility(View.GONE);
        listview = (RefreshListView)findViewById(R.id.listview);
        cencel = (TextView)findViewById(R.id.cencel);
        progress = (ProgressBar)findViewById(R.id.progress);
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSearchHistory.isClose = true;
                finish();
                overridePendingTransition(0, 0);
            }
        });
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {

                listview.setPullUpToRefreshable(true);
                if(isLoading == true){
                    listview.setPullDownToRefreshFinish();
                    return ;
                }
                isClear = true;
                isShowXiangshi = true;
                isNodata=false;
                isLoading = true;
                getTypeContent(key,typeid,"");

            }
        });
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                if(tabState != 0){
                    SearchEntry item = showList.get(showList.size()-1);
                    if(item != null){
                        if(showList.size() > 1) {
                            isClear = false;
                            item = showList.get(showList.size() - 1);
                            if (item != null) {
                                if (item.aid1 != null || item.aid2 != null || item.aid3 != null) {
                                    if (item.aid3 != null) {
                                        getTypeContent(key, typeid, item.aid3);
                                    } else if (item.aid2 != null) {
                                        getTypeContent(key, typeid, item.aid2);
                                    } else if (item.aid1 != null) {
                                        getTypeContent(key, typeid, item.aid1);
                                    }
                                } else {
                                    getTypeContent(key, typeid, item.id);
                                }
                            }
                        }}

                }
            }
        });

        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
//        Log.d("lizisong", "kkkkkkk");
//        if (MainActivity.ishideorshowbottombar) {
//            bottmon_title.setVisibility(View.GONE);
//        } else {
            bottmon_title.setVisibility(View.GONE);
//        }
//        if(MyApplication.navigationbar >0){
//            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
//            params.height=MyApplication.navigationbar;
//            bottmon_title.setLayoutParams(params);
//        }
        isShowXiangshi = true;
        edit.setText(key);

        edit.setSelection(key.length());
        isShowXiangshi = true;
        keyword.setText(key);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp =  s.toString();
                if(temp != null && !temp.equals("")){
                    if(!isShowXiangshi){
                       getReCi(temp);
                    }
                }else{
//                    listview.setVisibility(View.GONE);
                }
                if(temp == null || (temp != null &&temp.equals(""))){
                    closesousuo.setVisibility(View.GONE);
                    hintKbTwo();
                    finish();
                    overridePendingTransition(0, 0);

                }else {
                    closesousuo.setVisibility(View.VISIBLE);
//                    if(s.toString().length()>0){
//                        edit.setSelection(s.toString().length());
//                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    key = edit.getText().toString().trim();
                    int  netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(NewSearchContent.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(key.equals("")){
                            Toast.makeText(NewSearchContent.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(key)==false){
                            Toast.makeText(NewSearchContent.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                if(isLoading){
                                    return false;
                                }
                                isLoading = true;
                                isClear = true;
                                isShowXiangshi = true;
                                isNodata=false;
                                String temp;
                                String redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,null);
                                if(redianStr == null || (redianStr != null && redianStr.equals(""))){
                                    temp=key+";"+SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
                                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,temp);
                                }else{
                                    String[] tem = redianStr.split(";");
                                    int size = 0;
                                    if(tem != null){
                                        size = tem.length;
                                    }
                                    if(size >0){
                                        poslist.clear();
                                        for (int i=0; i<size;i++){
                                            String pos=tem[i];
                                            if(pos != null && !pos.equals("") && !pos.equals(";")){
                                                poslist.add(pos);
                                            }
                                        }
                                        boolean state =false;
                                        for (int i=0; i<poslist.size();i++){
                                            String pos = poslist.get(i);
                                            if(pos.equals(key)){
                                                state = true;
                                                break;
                                            }
                                        }
                                        if(!state){
                                            poslist.add(0,key);
                                        }
                                        redianStr="";
                                        for (int i=0;i<poslist.size();i++){
                                            String pos=poslist.get(i);
                                            if(i==poslist.size()-1){
                                                redianStr=redianStr+";"+pos;
                                            }else{
                                                redianStr=redianStr+";"+pos;
                                            }
                                        }
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
                                    }

                                }
                                isClear = true;
                                isShowXiangshi = true;

                                keyword.setText(key);
                                progress.setVisibility(View.VISIBLE);
                                adapter = null;
                                isHeart = false;
                                isNodata=false;
                                hintKbTwo();
                                listguanxi.setVisibility(View.GONE);
                                getTypeContent(key,typeid,"");
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        progress.setVisibility(View.VISIBLE);
        getTypeContent(key,typeid,"");
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }
    public void getTypeContent(final String key,final String typeid, final String sortid){
        String url = "http://"+ MyApplication.ip+"/api/searchApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",key);
        try {
            if(typeid != null ||  !typeid.equals("")){
                map.put("typeid",typeid);
            }
        }catch (Exception e){

        }
        map.put("pagesize","21");
        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,""));
        map.put("sortid",sortid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    isLoading = false;
                    isShowXiangshi = false;
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, NewSearchEntity.class);
                    if(tabState == 0){
                        listview.setBackgroundColor(0xfff6f6f6);
                    }else{
                        listview.setBackgroundColor(0xffffffff);
                    }
                    if(data != null){
                        if(data.code.equals("1")){
                            listview.setPullUpToRefreshFinish();
                            listview.setPullDownToRefreshFinish();
                            keywords.clear();
                            if(data.data != null){
                                if(data.data.relatedwords != null){
                                    if(data.data.relatedwords.size() >0){
                                        for(int i=0;i<data.data.relatedwords.size();i++){
                                            keywords.add(data.data.relatedwords.get(i));
                                        }
                                    }
                                }

                                if(!isHeart){
                                    titlekeys.clear();
                                }

                                if(data.data.search_list != null){
                                    if(data.data.search_list.size() >0){
                                        if(!isHeart){
                                            titlekeys.clear();
                                        }
                                        if(tabState == 0 && (typeid == null ||( typeid != null &&typeid.equals("")))){
                                            listview.setPullDownToRefreshable(false);
                                            listview.setPullUpToRefreshable(false);
                                            listview.setPullUpToRefreshFinish();
                                            listview.setPullDownToRefreshFinish();
                                            showList.clear();
                                            for(int i=0;i<data.data.search_list.size();i++){
                                                 NewSearchData item = data.data.search_list.get(i);
                                                 if(isHeart == false){
                                                     TitleKey title = new TitleKey();
                                                     title.count = item.count;
                                                     title.typeid = item.typeid;
                                                     title.typename = item.typename;
                                                     titlekeys.add(title);
                                                 }

                                                if(item.result != null && item.result.size() >0){
                                                   SearchEntry heart = new SearchEntry();
                                                   heart.typeid = item.typeid;
                                                   heart.typename=item.typename;
                                                   heart.isheart=true;
                                                   showList.add(heart);

                                                    if(item.typeid.equals("8")){
                                                        SearchEntry search =null;
                                                        boolean addstate =false;
                                                        for(int j=0;j<item.result.size();j++){
                                                            int k = j % 3 ;
                                                            PostData pos = item.result.get(j);
                                                            if(k == 0){
                                                                addstate =false;
                                                                search = new SearchEntry();
                                                                search.title1 = pos.title;
                                                                search.aid1 = pos.id;
                                                                search.typeid=pos.typeid;
                                                                search.typename=pos.typename;
                                                                search.logoimg1 = pos.logoimg;
                                                            }else if(k == 1){
                                                                search.title2 = pos.title;
                                                                search.aid2 = pos.id;
                                                                search.typeid=pos.typeid;
                                                                search.typename=pos.typename;
                                                                search.logoimg2 = pos.logoimg;
                                                            }else if(k == 2){
                                                                addstate=true;
                                                                search.title3 = pos.title;
                                                                search.aid3 = pos.id;
                                                                search.typeid=pos.typeid;
                                                                search.typename=pos.typename;
                                                                search.logoimg3 = pos.logoimg;
                                                                showList.add(search);
                                                            }
                                                            if(!addstate && (j == item.result.size()-1)){
                                                                showList.add(search);
                                                            }
                                                        }
                                                    }else{
                                                        for(int j=0;j<item.result.size();j++){
                                                            PostData pos = item.result.get(j);
                                                            SearchEntry search = new SearchEntry();
                                                            search.typeid = pos.typeid;
                                                            search.typename = pos.typename;
                                                            search.region_text = pos.region_text;
                                                            search.area_cate1 = pos.area_cate;
                                                            search.title = pos.title;
                                                            search.tags = pos.tags;
                                                            search.detail = pos.detail;
                                                            search.labels = pos.labels;
                                                            search.unit = pos.unit;
                                                            search.url = pos.url;
                                                            search.pubdate = pos.pubdate;
                                                            search.largeType = pos.largeType;
                                                            search.count = pos.count;
                                                            search.serviceCount  = pos.serviceCount;
                                                            search.image = pos.image;
                                                            search.imageState = pos.imageState;
                                                            search.rank = pos.rank;
                                                            search.source = pos.source;
                                                            search.sortTime = pos.sortTime;
                                                            search.username = pos.username;
                                                            search.is_academician = pos.is_academician;
                                                            search.litpic = pos.litpic;
                                                            search.id = pos.id;
                                                            search.description = pos.description;
                                                            showList.add(search);
                                                        }
                                                    }
                                                    SearchEntry end = new SearchEntry();
                                                    end.typeid = item.typeid;
                                                    end.typename=item.typename;
                                                    end.num = item.count;
                                                    end.isend=true;
                                                    showList.add(end);
                                                }

                                            }
                                            if(isHeart == false){
                                                TitleKey frist = new TitleKey();
                                                frist.count = "0";
                                                frist.typeid = "";
                                                frist.typename = "推荐";
                                                titlekeys.add(0,frist);
                                            }
                                            if(showList.size() == 0){
                                                nodate.setVisibility(View.VISIBLE);
                                                if(keywords.size() > 0){
                                                    hotline.setVisibility(View.VISIBLE);
                                                    hottitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    hotline.setVisibility(View.GONE);
                                                    hottitle.setVisibility(View.GONE);
                                                }
                                                listview.setVisibility(View.GONE);

                                            }else {
                                                if(keywords.size() >0){
                                                    SearchEntry hot = new SearchEntry();
                                                    hot.typeid="hot";
                                                    hot.typename="热词前";
                                                    showList.add(0,hot);
                                                    nodate.setVisibility(View.GONE);
                                                    listview.setVisibility(View.VISIBLE);
                                                }
                                            }



                                            if(showList.size() == 0){
                                                nodate.setVisibility(View.VISIBLE);
                                                formatfont("推荐");
                                                if(keywords.size() > 0){
                                                    hotline.setVisibility(View.VISIBLE);
                                                    hottitle.setVisibility(View.VISIBLE);
//
                                                }else{
                                                    hotline.setVisibility(View.GONE);
                                                    hottitle.setVisibility(View.GONE);
                                                }
                                                listview.setVisibility(View.GONE);
                                            }else{
                                                nodate.setVisibility(View.GONE);
                                                listview.setVisibility(View.VISIBLE);
                                            }

                                        }else{
                                            listview.setPullDownToRefreshable(true);
                                            listview.setPullUpToRefreshable(true);
                                            listview.setPullUpToRefreshFinish();
                                            listview.setPullDownToRefreshFinish();
                                            if(isClear){
                                                showList.clear();
                                            }

                                            if(data.data.search_list != null){
                                            for(int i=0; i<data.data.search_list.size();i++){
                                                NewSearchData item = data.data.search_list.get(i);
                                                if(isHeart == false){
                                                    TitleKey title = new TitleKey();
                                                    title.count = item.count;
                                                    title.typeid = item.typeid;
                                                    title.typename = item.typename;
                                                    titlekeys.add(title);
                                                }
                                                if(item.typeid.equals("8")){
                                                    SearchEntry search =null;
                                                    boolean addstate =false;
                                                    if(item.result != null && item.result.size()>0){
                                                        for(int j=0;j<item.result.size();j++){
                                                            int k = j % 3 ;
                                                            PostData pos = item.result.get(j);
                                                            if(k == 0){
                                                                addstate =false;
                                                                search = new SearchEntry();
                                                                search.title1 = pos.title;
                                                                search.aid1 = pos.id;
                                                                search.typeid=pos.typeid;
                                                                search.typename = pos.typename;
                                                                search.logoimg1 = pos.logoimg;
                                                            }else if(k == 1){
                                                                search.title2 = pos.title;
                                                                search.aid2 = pos.id;
                                                                search.typename = pos.typename;
                                                                search.typeid=pos.typeid;
                                                                search.logoimg2 = pos.logoimg;
                                                            }else if(k == 2){
                                                                addstate=true;
                                                                search.title3 = pos.title;
                                                                search.aid3 = pos.id;
                                                                search.logoimg3 = pos.logoimg;
                                                                search.typeid=pos.typeid;
                                                                search.typename = pos.typename;
                                                                showList.add(search);
                                                            }
                                                            if(!addstate && (j == item.result.size()-1)){
                                                                showList.add(search);
                                                            }
                                                        }
                                                    }
                                                }else{
                                                    if(item.result != null && item.result.size()>0){
                                                        for(int j=0;j<item.result.size();j++){
                                                            PostData pos = item.result.get(j);
                                                            SearchEntry search = new SearchEntry();
                                                            search.typeid = pos.typeid;
                                                            search.typename = pos.typename;
                                                            search.area_cate1 = pos.area_cate;
                                                            search.title = pos.title;
                                                            search.labels = pos.labels;
                                                            search.detail = pos.detail;
                                                            search.pubdate = pos.pubdate;
                                                            search.rank = pos.rank;
                                                            search.unit = pos.unit;
                                                            search.url = pos.url;
                                                            search.image = pos.image;
                                                            search.tags = pos.tags;
                                                            search.source = pos.source;
                                                            search.region_text = pos.region_text;
                                                            search.imageState = pos.imageState;
                                                            search.largeType = pos.largeType;
                                                            search.count = pos.count;
                                                            search.serviceCount  = pos.serviceCount;
                                                            search.sortTime = pos.sortTime;
                                                            search.username = pos.username;
                                                            search.is_academician = pos.is_academician;
                                                            search.litpic = pos.litpic;
                                                            search.id = pos.id;
                                                            search.description = pos.description;
                                                            showList.add(search);
                                                        }
                                                    }
                                                }
                                            }
                                            }
                                            if(isHeart == false){
                                                TitleKey frist = new TitleKey();
                                                frist.count = "0";
                                                frist.typeid = "";
                                                frist.typename = "推荐";
                                                titlekeys.add(0,frist);
                                            }
                                            if(typeid == null || typeid.equals("")){
                                                tabState=0;
                                            }else{
                                                for (int i=0;i<titlekeys.size();i++){
                                                    TitleKey item = titlekeys.get(i);
                                                    if(typeid.equals(item.typeid)){
                                                        tabState = i;
                                                        break;
                                                    }
                                                }
                                            }
                                            TitleKey word = null;

                                            SearchEntry lastItem = null;
                                            if(showList.size()>0){
                                                lastItem = showList.get(showList.size()-1);
                                            }

                                            if(showList.size() == 0){
                                                nodate.setVisibility(View.VISIBLE);
                                                if(keywords.size() > 0){
                                                    hotline.setVisibility(View.VISIBLE);
                                                    hottitle.setVisibility(View.VISIBLE);
                                                }else{
                                                    hotline.setVisibility(View.GONE);
                                                    hottitle.setVisibility(View.GONE);
                                                }
                                                listview.setVisibility(View.GONE);

                                            }else{
                                                nodate.setVisibility(View.GONE);
                                                listview.setVisibility(View.VISIBLE);
                                            }

                                           if(lastItem != null && lastItem.typeid.equals("8")){
                                               int count =0;
                                               if(lastItem .aid3 == null){
                                                   count=1;
                                               }
                                               if(lastItem.aid2 == null){
                                                   count=2;
                                               }
                                               word = titlekeys.get(tabState);
                                                if(word != null && Integer.parseInt(word.count) <= (showList.size()*3-count)){
                                                    formatfont(word.typename);
                                                    if(keywords.size() >0){
                                                        listview.setPullDownToRefreshable(false);
                                                        listview.setPullUpToRefreshable(false);
                                                        SearchEntry hot = new SearchEntry();
                                                        hot.typeid="hot";
                                                        hot.typename="热词后";
                                                        showList.add(hot);
                                                    }
                                                }
                                           }else{
                                               word = titlekeys.get(tabState);
                                               formatfont(word.typename);
                                                if(word != null && Integer.parseInt(word.count) <= showList.size()){
                                                    if(keywords.size() >0){
                                                        listview.setPullDownToRefreshable(false);
                                                        listview.setPullUpToRefreshable(false);
                                                        SearchEntry hot = new SearchEntry();
                                                        hot.typeid="hot";
                                                        hot.typename="热词后";
                                                        showList.add(hot);
                                                    }
                                                }
                                           }
                                        }
                                    }

                                }
                                }
                                if(typeid == null || typeid.equals("")){
                                    tabState=0;
                                }else{
                                    for (int i=0;i<titlekeys.size();i++){
                                        TitleKey item = titlekeys.get(i);
                                        if(typeid.equals(item.typeid)){
                                            tabState = i;
                                            break;
                                        }
                                    }
                                }
                            horlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(isLoading == true){
                                            return ;
                                        }
                                    try {
                                        isLoading = true;
                                        isClear = true;
                                        isShowXiangshi = true;
                                        TitleKey titlekey =null;

                                        titlekey= titlekeys.get(position);
                                        tabState = position;
                                        typeid=titlekey.typeid;
                                        progress.setVisibility(View.VISIBLE);

//                                        int modele = titlekeys.size()/2 +1;
//                                        if(position>modele){
//                                            if(position < titlekeys.size()-1){
//                                                horlist.setSelection(position+1,true);
//                                            }
//                                        }else{
//                                            if(position >= 1){
//                                                horlist.setSelection(position-1,true);
//                                            }
//                                        }
                                        heartAdapter.notifyDataSetChanged();
                                        listguanxi.setVisibility(View.GONE);
                                        getTypeContent(key,typeid,"");
                                        adapter=null;
                                    }catch (Exception e){

                                    }

                                }
                            });

                                if(heartAdapter == null && isHeart == false){
                                    isHeart = true;
                                    heartAdapter = new Heart();
                                    horlist.setAdapter(heartAdapter);
                                    horlist.setVisibility(View.VISIBLE);
//                                    horlist.setSelection(tabState,true);
                                    heartAdapter.notifyDataSetChanged();
                                }else {
                                    heartAdapter.notifyDataSetChanged();
                                }

                                if(adapter == null){
                                    isLoading = false;
                                    adapter = new SearchAdapter();
                                    listview.setAdapter(adapter);
                                }else {
                                    isLoading = false;
                                    adapter.notifyDataSetChanged();
                                }
//                            listview.setVisibility(View.VISIBLE);
                            listguanxi.setVisibility(View.GONE);
                            if(isNodata == false){
                                isNodata = true;
                                inithotView();
                            }
                            progress.setVisibility(View.GONE);

                            }
                        }
                    }
                if(msg.what == 2){
                    String ret;
                    ret=(String)msg.obj;
                    Gson gson = new Gson();
                    NewSearchHistoryEntiy data = gson.fromJson(ret, NewSearchHistoryEntiy.class);
                    xiangshi.clear();
                    if(data != null){
                        if(data.data.xiangsi != null && data.data.xiangsi.size()>0){
                            xiangshi.clear();
                            listview.setVisibility(View.INVISIBLE);
                            listguanxi.setVisibility(View.VISIBLE);
                            for (int i=0; i<data.data.xiangsi.size();i++){
                                String temp = data.data.xiangsi.get(i);
                                xiangshi.add(temp);
                            }
                        }else{
                            listguanxi.setVisibility(View.GONE);
                            listview.setVisibility(View.VISIBLE);
                        }
                    }else{
                        listguanxi.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                    }
                    if(hotAdapter == null){
                       hotAdapter = new NewHotAdapter();
                        listguanxi.setAdapter(hotAdapter);
                    }else{
                        hotAdapter.notifyDataSetChanged();
                    }
                }

            }catch (Exception e){

            }
        }
    };
    class TitleKey{
        public String typeid;
        public String typename;
        public String count;
    }

    class Heart extends BaseAdapter{
        private int height=MyApplication.height;
        private int width= MyApplication.width;
        @Override
        public int getCount() {
            return titlekeys.size();
        }

        @Override
        public Object getItem(int position) {
            return titlekeys.get(position);
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
                convertView = View.inflate(NewSearchContent.this, R.layout.titlekeys,null);
                holdView.sollect = convertView.findViewById(R.id.sollect);
                holdView.txt = convertView.findViewById(R.id.txt);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
           final TitleKey title = titlekeys.get(position);
            holdView.txt.setText(title.typename);

            if(tabState == position){
                holdView.txt.setTextColor(0xfffecb5f);
            }else{
                holdView.txt.setTextColor(0xffffffff);
            }
            return convertView;
        }
        class HoldView{
            public AutoRelativeLayout sollect;
            public TextView txt;
        }
    }

    class SearchAdapter extends BaseAdapter{
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();

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
                if(tabState == 0){
                    convertView = View.inflate(NewSearchContent.this, R.layout.newsearchadapter, null);
                }else{
                    convertView = View.inflate(NewSearchContent.this, R.layout.newsearchadapterqita, null);
                }
                holdView = new HoldView();
                holdView.contentViewlay = (LinearLayout)convertView.findViewById(R.id.contentViewlay);
                holdView.contentView = (AutoLinefeedLayout)convertView.findViewById(R.id.contentView);
                holdView.heart = (LinearLayout) convertView.findViewById(R.id.heart);
                holdView.type_name = (TextView)convertView.findViewById(R.id.type_name);
                holdView.count = (TextView)convertView.findViewById(R.id.count);

                holdView.end = (LinearLayout) convertView.findViewById(R.id.end);
                holdView.lookmore = (TextView)convertView.findViewById(R.id.lookmore);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                holdView.zx_layout = (LinearLayout) convertView.findViewById(R.id.zx_layout);
                holdView.zx_img = (ImageView)convertView.findViewById(R.id.zx_img);
                holdView.zx_zt = (ImageView)convertView.findViewById(R.id.zx_zt);
                holdView.zx_title = (TextView)convertView.findViewById(R.id.zx_title);
                holdView.zx_look =(TextView)convertView.findViewById(R.id.zx_look);

                holdView.zc_layout = (LinearLayout) convertView.findViewById(R.id.zc_layout);
                holdView.zhualilingyu = (LinearLayout)convertView.findViewById(R.id.zhualilingyu);
                holdView.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
                holdView.zc_linyu = (TextView) convertView.findViewById(R.id.zc_linyu);
                holdView.zc_lingyu = (TextView)convertView.findViewById(R.id.zc_lingyu) ;
                holdView.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
                holdView.zc_look = (TextView) convertView.findViewById(R.id.zc_look);
                holdView.zx_lanyuan = (TextView)convertView.findViewById(R.id.zx_lanyuan);

                holdView.rc_layout = (RelativeLayout) convertView.findViewById(R.id.rc_layout);
                holdView.rc_img = (RoundImageView)convertView.findViewById(R.id.rc_img);
                holdView.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);
                holdView.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
                holdView.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
                holdView.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
                holdView.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
                holdView.rc_yuanshi = (TextView)convertView.findViewById(R.id.rc_yuanshi);
                holdView.xm_layout = (LinearLayout) convertView.findViewById(R.id.xm_layout);
                holdView.xm_img = (ImageView)convertView.findViewById(R.id.xm_img);
                holdView.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
                holdView.xm_linyu = (TextView) convertView.findViewById(R.id.xm_linyu);
                holdView.xm_description = (TextView) convertView.findViewById(R.id.xm_description);
                holdView.xm_name = (TextView) convertView.findViewById(R.id.xm_name);
                holdView.xm_look = (TextView) convertView.findViewById(R.id.xm_look);

                holdView.jigoulayout = (LinearLayout)convertView.findViewById(R.id.jigoulayout);
                holdView.jigou1 = (RelativeLayout)convertView.findViewById(R.id.jigou1);
                holdView.jigou2 = (RelativeLayout)convertView.findViewById(R.id.jigou2);
                holdView.jigou3 = (RelativeLayout)convertView.findViewById(R.id.jigou3);
                holdView.img1 = (ImageView) convertView.findViewById(R.id.img1);
                holdView.img2 = (ImageView) convertView.findViewById(R.id.img2);
                holdView.img3 = (ImageView) convertView.findViewById(R.id.img3);
                holdView.tex1 = (TextView) convertView.findViewById(R.id.tex1);
                holdView.tex2 = (TextView) convertView.findViewById(R.id.tex2);
                holdView.tex3 = (TextView) convertView.findViewById(R.id.tex3);
                holdView.contentlay = (LinearLayout)convertView.findViewById(R.id.contentlay);
                holdView.hotView = (AutoLinefeedLayout)convertView.findViewById(R.id.hotView);

                holdView.onelay = (LinearLayout)convertView.findViewById(R.id.onelay);
                holdView.tj_state_lay = (LinearLayout) convertView.findViewById(R.id.tj_state_lay);
                holdView.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
                holdView.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
                holdView.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
                holdView.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);
                holdView.tj_state_zan = (TextView) convertView.findViewById(R.id.tj_state_zan);
                holdView.tj_state_click = (TextView) convertView.findViewById(R.id.tj_state_click);
                holdView.santu_zhiding = (ImageView) convertView.findViewById(R.id.santu_zhiding);
                holdView.santu_tuijian = (ImageView) convertView.findViewById(R.id.santu_tuijian);
                holdView.santu_name=(TextView) convertView.findViewById(R.id.santu_name);
                holdView.line = (TextView) convertView.findViewById(R.id.line);
                holdView.tj_state_zt=(ImageView) convertView.findViewById(R.id.tj_state_zt);
                holdView.state_zan = (LinearLayout) convertView.findViewById(R.id.state_zan);

                holdView.librarys_lay = (LinearLayout) convertView.findViewById(R.id.librarys_lay);
                holdView.librarys_title = (TextView) convertView.findViewById(R.id.librarys_title);
                holdView.librarys_img = (ImageView) convertView.findViewById(R.id.librarys_img);
//            holder.librarys_linyu=(TextView)convertView.findViewById(R.id.librarys_linyu);
                holdView.librarys_zan = (TextView) convertView.findViewById(R.id.librarys_zan);
                holdView.librarys_look = (TextView) convertView.findViewById(R.id.librarys_look);
                holdView.sys_zt = (ImageView) convertView.findViewById(R.id.sys_zt);
                holdView.shiyanshi_zhiding = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding);
                holdView.shiyanshi_tuijian = (ImageView) convertView.findViewById(R.id.shiyanshi_tuijian);
                holdView.datu_name=(TextView) convertView.findViewById(R.id.datu_name);
                holdView.sys_dianzan=(LinearLayout) convertView.findViewById(R.id.sys_dianzan);
                holdView.source = (TextView)convertView.findViewById(R.id.source);

                holdView.shebie_layout = (LinearLayout)convertView.findViewById(R.id.shebie_layout);
                holdView.rightshebie = (RelativeLayout)convertView.findViewById(R.id.rightshebie);
                holdView.rightshebie1 = (RelativeLayout)convertView.findViewById(R.id.rightshebie1);
                holdView.device_lay = (RelativeLayout)convertView.findViewById(R.id.device_lay);
                holdView.device = (ImageView)convertView.findViewById(R.id.device);
                holdView.title = (TextView)convertView.findViewById(R.id.title);
                holdView.lanyuan = (TextView)convertView.findViewById(R.id.lanyuan);
                holdView.sourceshebie = (TextView)convertView.findViewById(R.id.sourceshebie);
                holdView.countshebie = (TextView)convertView.findViewById(R.id.countshebie);
                holdView.sourceshebie1 = (TextView)convertView.findViewById(R.id.sourceshebie1);
                holdView.countshebie1 = (TextView)convertView.findViewById(R.id.countshebie1);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final SearchEntry item = showList.get(position);


            holdView.line.setVisibility(View.VISIBLE);
            try {
            if(item.isheart && tabState == 0){
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.VISIBLE);
                holdView.shebie_layout.setVisibility(View.GONE);
                if(item.typename.equals("专家")){
                    holdView.type_name.setText("专家");
                }else{
                    holdView.type_name.setText(item.typename);
                }

            }else if(item.isend && tabState == 0){
                holdView.contentlay.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.VISIBLE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.GONE);
                String danwei="" ;
                String typename="";
                if(item.typeid.equals("1")){
                    danwei="条";
                }else if(item.typeid.equals("4")){
                    danwei="位";
                }else if(item.typeid.equals("2")){
                    danwei="项";
                }else if(item.equals("8")){
                    danwei="所";
                }else if(item.typeid.equals("5")){
                    danwei="项";
                }else{
                    danwei="条";
                }

                holdView.lookmore.setText("查看更多"+item.typename+"( "+item.num+danwei+" )");
                holdView.lookmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.isend){
                            if(isLoading == true){
                                return ;
                            }
                            isLoading = true;
                            String typename =item.typename;
                            TitleKey titlekey =null;
                            for(int i=0;i<titlekeys.size();i++){
                                TitleKey title = titlekeys.get(i);
                                if(typename.equals(title.typename)){
                                    tabState = i;
                                    titlekey = title;
                                    typeid = titlekey.typeid;
                                    break;
                                }
                            }
//                            titlekey= titlekeys.get(tabState);
//                            int modele = titlekeys.size()/2 +1;
//                            if(tabState>modele){
//                                if(tabState < titlekeys.size()-1){
//                                    horlist.setSelection(tabState+1,true);
//                                }
//                            }else{
//                                if(tabState >= 1){
//                                    horlist.setSelection(tabState-1,true);
//                                }
//                            }
                            if(tabState >= 6){
                                horlist.setSelection(2,true);
                            }

                            isClear = true;
                            isShowXiangshi = true;
                            adapter = null;
//                            horlist.setSelection(tabState,true);
                            heartAdapter.notifyDataSetChanged();
                            getTypeContent(key,titlekey.typeid,"");
                        }
                    }
                });

            }else if(item.typename.equals("资讯") || item.typename.equals("专题")){
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.VISIBLE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.onelay.setVisibility(View.GONE);
                holdView.librarys_lay.setVisibility(View.GONE);
                holdView.tj_state_lay.setVisibility(View.GONE);
                if(item.imageState != null){
                       if(item.imageState.equals("3")){//三图
                           holdView.tj_state_lay.setVisibility(View.VISIBLE);
                           holdView.librarys_lay.setVisibility(View.GONE);
                           holdView.onelay.setVisibility(View.GONE);

                           holdView.tj_state_title.setText(item.title.replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ",""));
                           boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                           if (state) {
                               NetUtils.NetType type = NetUtils.getNetType();
                               if (type == NetUtils.NetType.NET_WIFI) {

                                   ImageLoader.getInstance().displayImage(item.image.image1
                                           , holdView.tj_state_img1, options);
                                   ImageLoader.getInstance().displayImage(item.image.image2
                                           , holdView.tj_state_img2, options);
                                   ImageLoader.getInstance().displayImage(item.image.image3
                                           , holdView.tj_state_img3, options);
                               } else {
                                   ImageLoader.getInstance().displayImage(item.image.image1
                                           , holdView.tj_state_img1, options);
                                   ImageLoader.getInstance().displayImage(item.image.image2
                                           , holdView.tj_state_img2, options);
                                   ImageLoader.getInstance().displayImage(item.image.image3
                                           , holdView.tj_state_img3, options);
                               }
                           } else {
                               ImageLoader.getInstance().displayImage(item.image.image1
                                       , holdView.tj_state_img1, options);
                               ImageLoader.getInstance().displayImage(item.image.image2
                                       , holdView.tj_state_img2, options);
                               ImageLoader.getInstance().displayImage(item.image.image3
                                       , holdView.tj_state_img3, options);
                           }
                           if(item.source != null && !item.source.replaceAll(" ", "").equals("")){
                               holdView.santu_name.setText(item.source);
                           }else{
                               holdView.santu_name.setText("钛领科技");
                           }


                       }else if(item.imageState.equals("1")){//大图
                           holdView.librarys_lay.setVisibility(View.VISIBLE);
                           holdView.onelay.setVisibility(View.GONE);
                           holdView.tj_state_lay.setVisibility(View.GONE);
                           holdView.librarys_title.setText(item.title.replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ",""));
                           boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                           if (state) {
                               NetUtils.NetType type = NetUtils.getNetType();
                               if (type == NetUtils.NetType.NET_WIFI) {
                                   ImageLoader.getInstance().displayImage(item.image.image1
                                           , holdView.librarys_img, options);
                               } else {
                                   holdView.librarys_img.setBackgroundResource(R.mipmap.datu);
                               }
                           } else {

                               ImageLoader.getInstance().displayImage(item.image.image1
                                       , holdView.librarys_img, options);
                           }

                           if(item.source != null && !item.source.replaceAll(" ", "").equals("")){
                               holdView.datu_name.setText(item.source);
                           }else{
                               holdView.datu_name.setText("钛领科技");
                           }

                       }else if(item.imageState.equals("0") || item.imageState.equals("-1")){//单图
                           holdView.librarys_lay.setVisibility(View.GONE);
                           holdView.onelay.setVisibility(View.VISIBLE);
                           holdView.tj_state_lay.setVisibility(View.GONE);
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
                           if(item.source != null && !item.source.replaceAll(" ", "").equals("")){
                               holdView.zx_lanyuan.setText(item.source);
                           }else{
                               holdView.zx_lanyuan.setText("钛领科技");
                           }


                       }

                }else {
                    holdView.librarys_lay.setVisibility(View.GONE);
                    holdView.onelay.setVisibility(View.VISIBLE);
                    holdView.tj_state_lay.setVisibility(View.GONE);
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
                    if(item.source != null && !item.source.replaceAll(" ", "").equals("")){
                        holdView.zx_lanyuan.setText(item.source);
                    }else{
                        holdView.zx_lanyuan.setText("钛领科技");
                    }
                }



                holdView.zx_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = item.typename;
                        if(name != null && name.equals("专题")){
                            Intent intent = new Intent(NewSearchContent.this, SpecialActivity.class);
                            intent.putExtra("id", item.id);
                            startActivity(intent);
                        }else if(item.typename.equals("html") || item.typeid.equals("10000")){
                            Intent intent=new Intent(NewSearchContent.this, WebViewActivity.class);
                            intent.putExtra("title", item.title);
                            intent.putExtra("url", item.detail);
                            startActivity(intent);
                        }else if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                            Intent intent = new Intent(NewSearchContent.this, ZixunDetailsActivity.class);
                            intent.putExtra("id", item.id);
                            intent.putExtra("name", item.typename);
                            intent.putExtra("pic", item.litpic);
                            startActivity(intent);
                        }
                    }
                });
            }else if(item.typename.equals("政策") ||item.typename.equals("专利")){
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.VISIBLE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.zc_title.setText(item.title.replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ","").replaceAll("  ",""));
                holdView.zc_lingyu.setText(item.typename);
                holdView.zc_linyu.setText(item.area_cate1);
                if(item.area_cate1 != null && !item.area_cate1.equals("")){
                    holdView.zhualilingyu.setVisibility(View.VISIBLE);
                    holdView.zc_linyu.setText(item.area_cate1);
                }else{
                    holdView.zhualilingyu.setVisibility(View.GONE);
                }
                if(item.typename.equals("政策")){
                    Log.d("lizisong", "item.region_text:"+item.region_text);
                    if(item.region_text != null){
                        holdView.zhualilingyu.setVisibility(View.VISIBLE);
                        holdView.zc_linyu.setText(item.region_text);
                    }else{
                        holdView.zhualilingyu.setVisibility(View.GONE);
                    }

                }
//                if(item.description != null){
//                   holdView.zc_description.setText(item.description.replaceAll("\r\n", "").replaceAll("\n",""));
//                }
//                holdView.zc_look.setText(item.click);
                holdView.zc_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if( item.typename.equals("政策")){
                           Intent intent = new Intent(NewSearchContent.this, NewZhengCeActivity.class);
                           intent.putExtra("aid",  item.id);
                           startActivity(intent);
                       }else if(item.typename.equals("专利")){
                            Intent intent = new Intent(NewSearchContent.this, NewZhuanliActivity.class);
                            intent.putExtra("aid",  item.id);
                            startActivity(intent);
                       }
//                        Intent intent = new Intent(NewSearchContent.this, DetailsActivity.class);
//                        intent.putExtra("id",  item.id);
//                        intent.putExtra("name",item.typename);
//                        intent.putExtra("pic", item.litpic);
//                        startActivity(intent);
                    }
                });

            }else if(item.typename.equals("专家")){
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.VISIBLE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                if(item.litpic != null && !item.litpic.equals("")){
                    holdView.rc_img.setVisibility(View.VISIBLE);
                    holdView.rc_img.setBackgroundResource(R.mipmap.img_rencai);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , holdView.rc_img, options);
                }else{
                    holdView.rc_img.setVisibility(View.VISIBLE);
                    holdView.rc_img.setBackgroundResource(R.mipmap.img_rencai);
                }
                holdView.rc_uname.setText(item.username);
                holdView.rc_linyu.setText(item.unit);
                holdView.rc_title.setText(item.description);
                holdView.rc_yuanshi.setText(item.rank);

                holdView.rc_look.setText(item.click);
                holdView.rc_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewSearchContent.this, XinFanAnCeShi.class);
                        intent.putExtra("aid",  item.id);
                        startActivity(intent);
                    }
                });

            }else if( item.typeid != null &&item.typeid.equals("8")){
                if(tabState == 0){
                    holdView.line.setVisibility(View.VISIBLE);
                }else{
                    holdView.line.setVisibility(View.GONE);
                }
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.VISIBLE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.jigou1.setVisibility(View.INVISIBLE);
                holdView.jigou2.setVisibility(View.INVISIBLE);
                holdView.jigou3.setVisibility(View.INVISIBLE);
                if(item.aid1 != null && !item.aid1.equals("")){
                    holdView.jigou1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.logoimg1
                            , holdView.img1, options);

                    holdView.tex1.setText(item.title1);
                }

                if(item.aid2 != null && !item.aid2.equals("")){
                    holdView.jigou2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.logoimg2
                            , holdView.img2, options);
                    holdView.tex2.setText(item.title2);
                }
                if(item.aid3 != null && !item.aid3.equals("")){
                    holdView.jigou3.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.logoimg3
                            , holdView.img3, options);
                    holdView.tex3.setText(item.title3);
                }

                holdView.jigou1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewSearchContent.this, UnitedStatesDeilActivity.class);
                        intent.putExtra("aid",item.aid1 );
                        startActivity(intent);
                    }
                });
                holdView.jigou2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewSearchContent.this, UnitedStatesDeilActivity.class);
                        intent.putExtra("aid",item.aid2);
                        startActivity(intent);
                    }
                });
                holdView.jigou3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewSearchContent.this, UnitedStatesDeilActivity.class);
                        intent.putExtra("aid",item.aid3);
                        startActivity(intent);
                    }
                });


            }else if(item.typename.equals("热词前")){
                holdView.contentView.setVisibility(View.VISIBLE);
                holdView.contentViewlay.setVisibility(View.VISIBLE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.contentView.removeAllViews();
                holdView.line.setVisibility(View.GONE);
                childBtns.clear();
                int countwidht =0;
                for(int i=0; i<keywords.size();i++){
                    String pos = keywords.get(i);
                    countwidht += getTextWidth(getApplicationContext(),pos,28)+60;
                    if(countwidht > 4*MyApplication.widths){
                        break;
                    }
                    LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewSearchContent.this).inflate(R.layout.hotconent, null);
                    TextView tv = (TextView)childBtn.findViewById(R.id.item);
                    tv.setText(pos);
                    childBtns.add(childBtn);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv = (TextView)v;
                            String hot;
                            hot = tv.getText().toString();
                            isShowXiangshi = true;
                            edit.setText(hot);
                            edit.setSelection(hot.length());
                            if(isLoading){
                                return;
                            }
                            isLoading = true;
                            isClear = true;

                            isNodata = false;
                            progress.setVisibility(View.VISIBLE);
                            adapter = null;
                            TitleKey titlekey =null;
                            key=hot;
                            isShowXiangshi = true;
                            edit.setText(hot);
                            edit.setSelection(hot.length());
                            isHeart = false;
                            TitleKey word = titlekeys.get(tabState);
                            formatfont(word.typename);
                            addHistory();
                            titlekey=titlekeys.get(tabState);
                            typeid = titlekey.typeid;
                            getTypeContent(hot,titlekey.typeid,"");

                        }
                    });
                    holdView.contentView.addView(childBtn);
                }

            }else if(item.typename.equals("热词后")){
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.VISIBLE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.hotView.removeAllViews();
                childBtns.clear();
                int countwidht =0;
                for(int i=0;i<keywords.size();i++){
                    String pos = keywords.get(i);
                    countwidht += getTextWidth(getApplicationContext(),pos,28)+60;
                    if(countwidht > 4*MyApplication.widths){
                        break;
                    }
                    LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewSearchContent.this).inflate(R.layout.hotitem, null);
                    TextView tv = (TextView)childBtn.findViewById(R.id.item);
                    tv.setText(pos);
                    childBtns.add(childBtn);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv = (TextView)v;
                            String hot;
                            hot = tv.getText().toString();
                            if(isLoading){
                                return;
                            }
                            isLoading = true;
                            isClear = true;
                            isShowXiangshi = true;
                            isNodata = false;
                            TitleKey titlekey =null;

                            titlekey=  titlekeys.get(tabState);
                            key=hot;
                            isShowXiangshi = true;
                            edit.setText(hot);
                            edit.setSelection(hot.length());
                            isHeart = false;
                            addHistory();
                            progress.setVisibility(View.VISIBLE);
                            adapter = null;
                            getTypeContent(hot,titlekey.typeid,"");

                        }
                    });
                    holdView.hotView.addView(childBtn);
                }

            }else if(item.typeid != null && item.typeid.equals("14")){
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.VISIBLE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.xm_layout.setVisibility(View.GONE);
                holdView.heart.setVisibility(View.GONE);
                if(item.litpic != null && !item.litpic.equals("")){
                    holdView.device.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , holdView.device, options);
                }else{
                    holdView.device.setVisibility(View.GONE);
                }
                holdView.title.setText(item.title);
                holdView.lanyuan.setText(item.largeType);
                holdView.sourceshebie.setText(item.serviceCount);
                holdView.countshebie.setText(item.count);
                holdView.sourceshebie1.setText(item.serviceCount);
                holdView.countshebie1.setText(item.count);
                if(item.count == null || (item.count!= null && item.count.equals("0"))){
                    holdView.countshebie.setVisibility(View.GONE);
                    holdView.rightshebie1.setVisibility(View.GONE);
                    holdView.rightshebie.setVisibility(View.VISIBLE);
                    holdView.sourceshebie.setText(item.serviceCount);
                }else{
                    holdView.rightshebie1.setVisibility(View.VISIBLE);
                    holdView.rightshebie.setVisibility(View.GONE);
//                    holdView.countshebie.setVisibility(View.VISIBLE);
//                    holdView.sourceshebie.setText("      "+item.serviceCount);
                }
                holdView.device_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewSearchContent.this, SheBeiDeilActivity.class);
                        intent.putExtra("aid", item.id);
                        startActivity(intent);
                    }
                });
            }else {
                holdView.zx_layout.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.shebie_layout.setVisibility(View.GONE);
                holdView.zc_layout.setVisibility(View.GONE);
                holdView.rc_layout.setVisibility(View.GONE);
                holdView.contentView.setVisibility(View.GONE);
                holdView.contentViewlay.setVisibility(View.GONE);
                holdView.jigoulayout.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);

                holdView.xm_layout.setVisibility(View.VISIBLE);
                holdView.heart.setVisibility(View.GONE);
                if(item.litpic != null && !item.litpic.equals("")){
                    holdView.xm_img.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , holdView.xm_img, options);
                }else{
                    holdView.xm_img.setVisibility(View.GONE);
                }
                holdView.xm_title.setText(item.title.replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ","").replaceAll("  ",""));
                if(item.area_cate1 != null){
                    holdView.xm_linyu.setText(item.area_cate1.replaceAll(" ",""));
                }
                if(item.description != null){
                    holdView.xm_description.setText(item.description.replaceAll("\r\n","").replaceAll("\n",""));
                }
                if (item.labels != null && (!item.labels.equals(""))) {
                    holdView.source.setVisibility(View.VISIBLE);
                    holdView.source.setText(item.labels);
                    if (item.labels.equals("精品项目")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    } else if (item.labels.equals("钛领推荐")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                    } else if (item.labels.equals("国家科学基金")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                    } else {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }
                } else {
                    holdView.source.setVisibility(View.GONE);
                }
                holdView.xm_name.setText(item.typename);
                holdView.xm_look.setText(item.click);
                holdView.xm_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = item.typename;
                        if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                            Intent intent = new Intent(NewSearchContent.this, ZixunDetailsActivity.class);
                            intent.putExtra("id", item.id);
                            intent.putExtra("name", item.typename);
                            intent.putExtra("pic", item.litpic);
                            startActivity(intent);
                        }else if(item.typename.equals("html") || item.typeid.equals("10000")){
                            Log.d("lizisong", "精品项目");
//                            Log.d("lizisong", "url:"+item.detail);
                            Intent intent=new Intent(NewSearchContent.this, WebViewActivity.class);
                            intent.putExtra("title", item.title);
                            if(item.typeid.equals("2")){
                               intent.putExtra("url", item.url);
                            }else{
                                intent.putExtra("url", item.detail);
                            }
                            startActivity(intent);

                        }else{
                            if(item.typeid.equals("4")){
                                Intent intent = new Intent(NewSearchContent.this, XinFanAnCeShi.class);
                                intent.putExtra("aid",  item.id);
                                startActivity(intent);
                            }else if(item.typeid.equals("5")){
                                Intent intent = new Intent(NewSearchContent.this, NewZhuanliActivity.class);
                                intent.putExtra("aid",  item.id);
                                startActivity(intent);
                            }else if(item.typeid.equals("6")){
                                Intent intent = new Intent(NewSearchContent.this, NewZhengCeActivity.class);
                                intent.putExtra("aid",  item.id);
                                startActivity(intent);
                            }else if(item.typeid.equals("2")){
                                String labels = item.labels;
                                if(labels != null){
                                    if(labels.contains("精品项目")){
                                        Intent intent=new Intent(NewSearchContent.this, ActiveActivity.class);
                                        intent.putExtra("title", item.title);
                                        intent.putExtra("url", item.url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(NewSearchContent.this, NewProjectActivity.class);
                                        intent.putExtra("aid",  item.id);
                                        startActivity(intent);
                                    }
                                }else{
                                    Intent intent = new Intent(NewSearchContent.this, NewProjectActivity.class);
                                    intent.putExtra("aid",  item.id);
                                    startActivity(intent);
                                }

                            }else {
                                Intent intent = new Intent(NewSearchContent.this, DetailsActivity.class);
                                intent.putExtra("id",  item.id);
                                intent.putExtra("name",item.typename);
                                intent.putExtra("pic", item.litpic);
                                startActivity(intent);
                            }

                        }
                    }

                });

            }

        }catch (Exception e){

        }
         return convertView;
        }


        class HoldView{
            //热词
            public LinearLayout  contentViewlay;
            public AutoLinefeedLayout contentView;
            //热词后
            public LinearLayout contentlay;
            public AutoLinefeedLayout hotView;
            //头
            public LinearLayout heart;
            public TextView type_name;
            public TextView count;
            //尾
            public LinearLayout end;
            public TextView lookmore;
            // 资讯
            public LinearLayout onelay;
            public LinearLayout zx_layout;
            public ImageView zx_img,zx_zt;
            public TextView zx_title,zx_look,zx_lanyuan;


            //政策
            public LinearLayout zc_layout,zhualilingyu;
            public TextView zc_title,zc_linyu,zc_description,zc_look,zc_lingyu;

            //人才
            public RelativeLayout rc_layout;
            public RoundImageView rc_img;
            public TextView rc_uname;
            public TextView rc_zhicheng;
            public TextView rc_yuanshi;
            public TextView rc_title;
            public TextView rc_look,rc_linyu;
            //项目
            public LinearLayout xm_layout;
            public ImageView xm_img;
            public TextView xm_title,xm_linyu,xm_description,xm_name,xm_look;
            //线
            public TextView line;
            public TextView source;

            public LinearLayout jigoulayout;
            public RelativeLayout jigou1,jigou2,jigou3;
            public ImageView img1,img2,img3;
            public TextView tex1,tex2,tex3;


            //三图
            public LinearLayout tj_state_lay;
            public  TextView tj_state_title;
            public ImageView tj_state_img1;
            public ImageView tj_state_img2;
            public ImageView tj_state_img3;
            public  TextView tj_state_zan;
            public TextView tj_state_click;
            public ImageView santu_zhiding;
            public ImageView santu_tuijian;
            public  TextView santu_name;
            public ImageView tj_state_zt;
            public LinearLayout state_zan;

            //实验室
            public LinearLayout librarys_lay;
            public TextView librarys_title;
            public TextView librarys_linyu;
            public ImageView librarys_img;
            public TextView librarys_zan;
            public   TextView librarys_look;

            public   ImageView sys_zt;
            public ImageView shiyanshi_zhiding;
            public ImageView shiyanshi_tuijian;
            public  TextView datu_name;
            public  LinearLayout sys_dianzan;


            //设备
            public LinearLayout shebie_layout;
            public RelativeLayout device_lay,rightshebie,rightshebie1;
            public ImageView device;
            public TextView title,lanyuan,sourceshebie,sourceshebie1,countshebie,countshebie1;

        }
    }

    private void inithotView(){
        int size = keywords.size(); // 添加Button的个数.
        if(size == 0){
            hotView.removeAllViews();
            return;
        }
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        hotView.removeAllViews();

        if(size > 0){
            int countwidht =0;

            for(int i = 0; i < size; i++){
                String item = keywords.get(i);
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                if(countwidht > 4*MyApplication.widths){
                    break;
                }
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewSearchContent.this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setText(item);
                tv.setTag(item);
                childBtns.add(childBtn);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView)v;
                        if(isLoading){
                            return;
                        }
                        isLoading = true;
                        isClear = true;
                        isHeart = false;
                        isNodata = false;
                        isShowXiangshi = true;
                        listview.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        showList.clear();
                        adapter = null;
                        addHistory();
                        TitleKey title =titlekeys.get(tabState);
                        typeid =title.typeid;
                        key = tv.getText().toString();
                        isShowXiangshi = true;
                        edit.setText(key);
                        edit.setSelection(key.length());
                        getTypeContent(tv.getText().toString(),typeid,"");

                    }
                });

                hotView.addView(childBtn);
            }
        }
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
    private void addHistory(){
        String temp;
        String redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,null);
        if(redianStr == null || (redianStr != null && redianStr.equals(""))){
            temp=key+";"+SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,temp);
        }else{
            String[] tem = redianStr.split(";");
            int size = 0;
            if(tem != null){
                size = tem.length;
            }
            if(size >0){
                poslist.clear();
                for (int i=0; i<size;i++){
                    String pos=tem[i];
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        poslist.add(pos);
                    }
                }
                boolean state =false;
                for (int i=0; i<poslist.size();i++){
                    String pos = poslist.get(i);
                    if(pos.equals(key)){
                        state = false;
                        poslist.remove(pos);
                        break;
                    }
                }
                if(!state){
                    poslist.add(0,key);
                }
                redianStr="";
                for (int i=0;i<poslist.size();i++){
                    String pos=poslist.get(i);
                    if(i==poslist.size()-1){
                        redianStr=redianStr+";"+pos;
                    }else{
                        redianStr=redianStr+";"+pos;
                    }
                }
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
            }

        }
    }
    private void formatfont(String typename){
       String content = "<font color=\"#181818\">" + "没有找到“ " + "</font>"+"<font color=\"#7383f8\">"+key+"</font>"+"<font color=\"#181818\">"+" ”相关的"+typename+"</font>";
       keyword.setText(Html.fromHtml(content));
    }

    /**
     * 获取热词的接口
     */
    private void getReCi(String key){
        String url="http://"+MyApplication.ip+"/api/hotWords.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",key);
        networkCom.getJson(url,map,handler,2,0);
    }


    class NewHotAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return xiangshi.size();
        }

        @Override
        public Object getItem(int position) {
            return xiangshi.get(position);
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
                convertView = View.inflate(NewSearchContent.this, R.layout.reciapater, null);
                holdView.reci = (TextView)convertView.findViewById(R.id.reci);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final String temp = xiangshi.get(position);
            holdView.reci.setText(temp);
            holdView.reci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLoading){
                        return;
                    }
                    isLoading = true;
                    isClear = true;
                    isHeart = false;
                    isNodata = false;
                    key = temp;
                    addHistory();
                    isShowXiangshi = true;
                    edit.setText(key);
                    edit.setSelection(key.length());
                    progress.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    listguanxi.setVisibility(View.GONE);
                    getTypeContent(key,typeid,key);

                }
            });
            try {
                if(xiangshi.size()-1 == position){
                    holdView.line.setVisibility(View.GONE);
                }else{
                    holdView.line.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }

            return convertView;
        }
        class  HoldView {
            public TextView reci;
            public TextView line;
        }
    }


    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
    }


}
