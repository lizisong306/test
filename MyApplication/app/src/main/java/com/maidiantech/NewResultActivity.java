package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.NewResultEntity;
import entity.NewResultHeart;
import entity.NewSearchData;
import entity.NewSearchEntity;
import entity.PostData;
import entity.Posts;
import entity.SearchEntry;
import entity.SonCate;
import entity.XiuGaiBaMaiEntity;
import view.AutoLinefeedLayout;
import view.HorizontalListView;
import view.RefreshListView;
import view.RoundImageView;
import Util.NetUtils;
/**
 * Created by Administrator on 2019/9/18.
 */

public class NewResultActivity extends AutoLayoutActivity {
    String evaluetop ;
    String sizepage="3";
    int page=1;
    String typeid="";
    NewSearchEntity newresultentity;
    List<SearchEntry> showList = new ArrayList<>();
    List<TitleKey> titlekeys = new ArrayList();
    ImageView shezhi_backs,bianji,sehe;
    TextView titledes,keyword,hotline,hottitle;
    LinearLayout fiveitemlay;
    RelativeLayout itemlay1,itemlay2,itemlay3,itemlay4,itemlay5;
    TextView item1,item2,item3,item4,item5;
    TextView itemline1,itemline2,itemline3,itemline4,itemline5;
    HorizontalListView horlist;
    RefreshListView listview;
    boolean isHeart = false;
    boolean isShowXiangshi = false;
    TextView commit;
    boolean isClear = false;
    boolean isNodata = false;
    String id,name;
    String key;
    boolean isLoading = false;
    LinearLayout nodate;
    view.AutoLinefeedLayout hotView;
    RelativeLayout donghua;
    ImageView tuijian;
    int tabindex = 0,tabState=0;
    List<String> hots = new ArrayList<>();
    List<String> keywords = new ArrayList<>();
    private DisplayImageOptions options;
    AnimationDrawable animationDrawable;
    Heart heart;
    SearchAdapter adapter;
    PulseCondtion mPulseCondtion;
    ArrayList<PulseData> pulseDatas;
    boolean state = false;
    boolean onestate = false;
    public static boolean isNewClear =false;
    ProgressBar progress;
    public static String namexiugai="";
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
        setContentView(R.layout.newresultactivity);
        options = ImageLoaderUtils.initOptions();
        evaluetop = getIntent().getStringExtra("evaluetop");
        name = getIntent().getStringExtra("title");
        shezhi_backs =(ImageView)findViewById(R.id.shezhi_backs);
        bianji = (ImageView)findViewById(R.id.bianji);
        sehe = (ImageView)findViewById(R.id.sehe);
        id =getIntent().getStringExtra("id");
        titledes = (TextView)findViewById(R.id.titledes);
        keyword = (TextView)findViewById(R.id.keyword);
        commit = (TextView)findViewById(R.id.commit);
        hottitle =(TextView)findViewById(R.id.hottitle);
        hotline = (TextView)findViewById(R.id.hotline);
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);
        fiveitemlay = (LinearLayout)findViewById(R.id.fiveitemlay);
        itemlay1 = (RelativeLayout)findViewById(R.id.itemlay1);
        itemlay2 = (RelativeLayout)findViewById(R.id.itemlay2);
        itemlay3 = (RelativeLayout)findViewById(R.id.itemlay3);
        itemlay4 = (RelativeLayout)findViewById(R.id.itemlay4);
        itemlay5 = (RelativeLayout)findViewById(R.id.itemlay5);
        item1 = (TextView)findViewById(R.id.item1);
        item2 = (TextView)findViewById(R.id.item2);
        item3 = (TextView)findViewById(R.id.item3);
        item4 = (TextView)findViewById(R.id.item4);
        item5 = (TextView)findViewById(R.id.item5);
        itemline1 = (TextView)findViewById(R.id.itemline1);
        itemline2 = (TextView)findViewById(R.id.itemline2);
        itemline3 = (TextView)findViewById(R.id.itemline3);
        itemline4 = (TextView)findViewById(R.id.itemline4);
        itemline5 = (TextView)findViewById(R.id.itemline5);
        progress = (ProgressBar)findViewById(R.id.progress);
        mPulseCondtion = PulseCondtion.getInstance(this);
        horlist = (HorizontalListView)findViewById(R.id.horlist);
        listview = (RefreshListView)findViewById(R.id.listview);
        nodate = (LinearLayout)findViewById(R.id.nodate);
        donghua = (RelativeLayout)findViewById(R.id.donghua);
        tuijian = (ImageView)findViewById(R.id.tuijian);
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewResultActivity.this, NewXuQiu.class);
                intent.putExtra("typeid", "0");
                startActivity(intent);
            }
        });

        Log.d("lizisong","evaluetop:"+evaluetop);
        sehe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewResultActivity.this, XiuGaiBaMaiTiaoJian.class);
                intent.putExtra("name", name);
                intent.putExtra("id",id);
                intent.putExtra("evaluetop",evaluetop);
                startActivity(intent);
            }
        });
        bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewResultActivity.this, XiuGaiMingCheng.class);
                intent.putExtra("name", name);
                intent.putExtra("evaluetop",evaluetop);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        itemlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initline();
                itemline1.setVisibility(View.VISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                tabState=0;
                TextPaint tp ;
                tp=item1.getPaint();
                tp.setFakeBoldText(true);
                item1.setTextColor(0xff33afb6);
                typeid="";
                page=1;
                sizepage="3";
//                donghua.setVisibility(View.VISIBLE);
//                tuijian.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                animationDrawable.start();
//                handler.sendEmptyMessageDelayed(2222, 4500);
                adapter=null;
                getJson();
            }
        });
        itemlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.VISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                tabState=1;
                TextPaint tp ;
                tp=item2.getPaint();
                tp.setFakeBoldText(true);
                item2.setTextColor(0xff33afb6);
                TitleKey heart= titlekeys.get(1);
                typeid=heart.typeid;
                sizepage="10";
                page=1;
                isClear=true;
//                donghua.setVisibility(View.VISIBLE);
//                tuijian.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                animationDrawable.start();
//                handler.sendEmptyMessageDelayed(2222, 4500);
                adapter=null;
                getJson();
            }
        });
        itemlay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.VISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                tabState=2;
                TextPaint tp ;
                tp=item3.getPaint();
                tp.setFakeBoldText(true);
                item3.setTextColor(0xff33afb6);
                TitleKey heart= titlekeys.get(2);
                typeid=heart.typeid;
                sizepage="10";
                page=1;
                isClear=true;
//                donghua.setVisibility(View.VISIBLE);
//                tuijian.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                animationDrawable.start();
//                handler.sendEmptyMessageDelayed(2222, 4500);
                adapter=null;
                getJson();
            }
        });
        itemlay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.VISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                tabState=3;
                TextPaint tp ;
                tp=item4.getPaint();
                tp.setFakeBoldText(true);
                item4.setTextColor(0xff33afb6);
                TitleKey heart= titlekeys.get(3);
                typeid=heart.typeid;
                sizepage="10";
                page=1;
                isClear=true;
//                donghua.setVisibility(View.VISIBLE);
//                tuijian.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                animationDrawable.start();
//                handler.sendEmptyMessageDelayed(2222, 4500);
                adapter=null;
                getJson();
            }
        });
        itemlay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initline();
                tabState=4;
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.VISIBLE);
                TextPaint tp ;
                tp=item5.getPaint();
                tp.setFakeBoldText(true);
                item5.setTextColor(0xff33afb6);
                TitleKey heart= titlekeys.get(4);
                typeid=heart.typeid;
                sizepage="10";
                page=1;
                isClear=true;
//                donghua.setVisibility(View.VISIBLE);
//                tuijian.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                animationDrawable.start();
//                handler.sendEmptyMessageDelayed(2222, 4500);
                adapter=null;
                getJson();
            }
        });
        horlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabState=position;
                TitleKey heart= titlekeys.get(position);
                if(heart != null){
                    if(tabindex>=6){
                        horlist.setSelection(2,true);
                    }
                    typeid=heart.typeid;
                    if(position==0){
                        sizepage="3";
                        page=1;
                    }else{
                        sizepage="10";
                        page=1;
                    }
                    isClear=true;
//                    donghua.setVisibility(View.VISIBLE);
//                    tuijian.setVisibility(View.VISIBLE);
//                    animationDrawable = (AnimationDrawable)tuijian.getBackground();
//                    animationDrawable.start();
//                    handler.sendEmptyMessageDelayed(2222, 4500);
                    adapter=null;
                    getJson();
                }
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
                getJson();
//                getTypeContent(key,typeid,"");

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
                                        page++;
                                         getJson();
                                    } else if (item.aid2 != null) {
                                        page++;
                                        getJson();
                                    } else if (item.aid1 != null) {
                                        page++;
                                        getJson();
                                    }
                                } else {
                                    page++;
                                    getJson();
                                }
                            }
                        }}

                }
            }
        });
         pulseDatas = mPulseCondtion.get();
         state =false;
         if(pulseDatas != null){
            for (int i=0; i<pulseDatas.size();i++){
                PulseData item = pulseDatas.get(i);
                if(item.evaluetop.equals(evaluetop)){
                    state = true;
                    break;
                }
            }
        }

        donghua.setVisibility(View.VISIBLE);
        tuijian.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable)tuijian.getBackground();
        animationDrawable.start();
        handler.sendEmptyMessageDelayed(2222, 4500);
        namexiugai = name;
        titledes.setText(namexiugai);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNewClear){
            isHeart=false;
            isNewClear=false;
            typeid="";
            page=1;
            tabState=0;
            sizepage="3";
            adapter=null;
            donghua.setVisibility(View.VISIBLE);
            tuijian.setVisibility(View.VISIBLE);
            animationDrawable = (AnimationDrawable)tuijian.getBackground();
            animationDrawable.start();
            handler.sendEmptyMessageDelayed(2222, 4500);
        }
        titledes.setText(namexiugai);
    }

    private void getJson(){
        String url = "http://"+ MyApplication.ip+"/api/getArcByAreaCateApi.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("evaluetop",evaluetop);
        map.put("typeid",typeid);
        if(id != null){
            map.put("id",id);
        }
        map.put("pagesize",sizepage);
        map.put("page",page+"");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
//                    donghua.setVisibility(View.GONE);
//                    tuijian.setVisibility(View.GONE);
                    listview.setPullUpToRefreshFinish();
                    listview.setPullDownToRefreshFinish();
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    newresultentity = gson.fromJson(ret, NewSearchEntity.class);
                    if(tabState == 0){
                        listview.setBackgroundColor(0xfff6f6f6);
                    }else{
                        listview.setBackgroundColor(0xffffffff);
                    }
                    if(newresultentity != null){
                        if(newresultentity.code.equals("1")){
                            listview.setPullUpToRefreshFinish();
                            listview.setPullDownToRefreshFinish();
                            keywords.clear();
                            name = newresultentity.data.name;
                            id = newresultentity.data.id;
                            titledes.setText(name);
                            namexiugai = name;
                            if(newresultentity.data.relatedwords != null){
                                if(newresultentity.data.relatedwords.size() >0){
                                    for(int i=0;i<newresultentity.data.relatedwords.size();i++){
                                        keywords.add(newresultentity.data.relatedwords.get(i));
                                    }
                                    inithotView();
                                }
                            }
                            if(!onestate){
                                onestate=true;
                                getlistJson();
                            }

                            if(newresultentity.data.search_list != null) {
                                if(newresultentity.data.search_list.size() >0){
                                    if(!isHeart){
                                        titlekeys.clear();
                                    }
                                    if(tabState == 0 && (typeid == null ||( typeid != null &&typeid.equals("")))){
                                        listview.setPullDownToRefreshable(false);
                                        listview.setPullUpToRefreshable(false);
                                        listview.setPullUpToRefreshFinish();
                                        listview.setPullDownToRefreshFinish();
                                        showList.clear();
                                        for(int i=0;i<newresultentity.data.search_list.size();i++){
                                            NewSearchData item = newresultentity.data.search_list.get(i);
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

                                        if(newresultentity.data.search_list != null){
                                            for(int i=0; i<newresultentity.data.search_list.size();i++){
                                                NewSearchData item = newresultentity.data.search_list.get(i);
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
                            if(hots.size() == 0){
                                if(newresultentity.data.relatedwords != null){
                                    for(int i=0; i<newresultentity.data.relatedwords.size();i++){
                                        String item = newresultentity.data.relatedwords.get(i);
                                        hots.add(item);
                                    }
                                }
                            }
                            if(titlekeys.size() >0){
                                isHeart = true;
                                if(titlekeys.size() <=5){
                                    fiveitemlay.setVisibility(View.VISIBLE);
                                    horlist.setVisibility(View.GONE);
                                    for(int i=0; i<titlekeys.size();i++){
                                        TitleKey ii = titlekeys.get(i);
                                        if(i==0){
                                            itemlay1.setVisibility(View.VISIBLE);
                                            item1.setText(ii.typename);
                                        }else if(i==1){
                                            itemlay2.setVisibility(View.VISIBLE);
                                            item2.setText(ii.typename);
                                        }else if(i==2){
                                            itemlay3.setVisibility(View.VISIBLE);
                                            item3.setText(ii.typename);
                                        }else if(i==3){
                                            itemlay4.setVisibility(View.VISIBLE);
                                            item4.setText(ii.typename);
                                        }else if(i==4){
                                            itemlay5.setVisibility(View.VISIBLE);
                                            item5.setText(ii.typename);
                                        }
                                    }
                                    if(titlekeys.size() == 1){
                                        itemlay1.setVisibility(View.VISIBLE);
                                        itemlay2.setVisibility(View.GONE);
                                        itemlay3.setVisibility(View.GONE);
                                        itemlay4.setVisibility(View.GONE);
                                        itemlay5.setVisibility(View.GONE);
                                    }else if(titlekeys.size() == 2){
                                        itemlay1.setVisibility(View.VISIBLE);
                                        itemlay2.setVisibility(View.VISIBLE);
                                        itemlay3.setVisibility(View.GONE);
                                        itemlay4.setVisibility(View.GONE);
                                        itemlay5.setVisibility(View.GONE);
                                    }else if(titlekeys.size() == 3){
                                        itemlay1.setVisibility(View.VISIBLE);
                                        itemlay2.setVisibility(View.VISIBLE);
                                        itemlay3.setVisibility(View.VISIBLE);
                                        itemlay4.setVisibility(View.GONE);
                                        itemlay5.setVisibility(View.GONE);
                                    }else if(titlekeys.size() == 4){
                                        itemlay1.setVisibility(View.VISIBLE);
                                        itemlay2.setVisibility(View.VISIBLE);
                                        itemlay3.setVisibility(View.VISIBLE);
                                        itemlay4.setVisibility(View.VISIBLE);
                                        itemlay5.setVisibility(View.GONE);
                                    }else if(titlekeys.size() == 5){
                                        itemlay1.setVisibility(View.VISIBLE);
                                        itemlay2.setVisibility(View.VISIBLE);
                                        itemlay3.setVisibility(View.VISIBLE);
                                        itemlay4.setVisibility(View.VISIBLE);
                                        itemlay5.setVisibility(View.VISIBLE);
                                    }

                                    if(tabState == 0){
                                        initline();
                                        itemline1.setVisibility(View.VISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=0;
                                        TextPaint tp ;
                                        tp=item1.getPaint();
                                        tp.setFakeBoldText(true);
                                        item1.setTextColor(0xff33afb6);
                                        typeid="";
                                        page=1;
                                        sizepage="3";
                                    }else if(tabState == 1){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.VISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=1;
                                        TextPaint tp ;
                                        tp=item2.getPaint();
                                        tp.setFakeBoldText(true);
                                        item2.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(1);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 2){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.VISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=2;
                                        TextPaint tp ;
                                        tp=item3.getPaint();
                                        tp.setFakeBoldText(true);
                                        item3.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(2);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 3){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.VISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=3;
                                        TextPaint tp ;
                                        tp=item4.getPaint();
                                        tp.setFakeBoldText(true);
                                        item4.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(3);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 4){
                                        initline();
                                        tabState=4;
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.VISIBLE);
                                        TextPaint tp ;
                                        tp=item5.getPaint();
                                        tp.setFakeBoldText(true);
                                        item5.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(4);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }
                                }else{
                                    fiveitemlay.setVisibility(View.GONE);
                                    horlist.setVisibility(View.VISIBLE);
                                    if(heart == null){
                                        heart = new Heart();
                                        horlist.setAdapter(heart);
                                    }
                                }
                            }
                            progress.setVisibility(View.GONE);
                            if(adapter == null){
                                isLoading = false;
                                adapter = new SearchAdapter();
                                listview.setAdapter(adapter);
                            }else {
                                isLoading = false;
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                }else if(msg.what == 2){
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    XiuGaiBaMaiEntity data=gson.fromJson(ret, XiuGaiBaMaiEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            String ename="";
                            String evalue="";
                            if(data.data.sonCate != null && data.data.sonCate.size()>0){
                                for(int i=0; i<data.data.sonCate.size();i++){
                                    SonCate item = data.data.sonCate.get(i);
                                    if(item.isChoice.equals("1")){
                                        ename=ename+item.ename+",";
                                        evalue = evalue+item.evalue+",";
                                    }
                                }
                            }
                            if(!state){
                                PulseData entity = new PulseData();
                                entity.mid=SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
                                entity.evaluetop = evaluetop;
                                entity.typeid = "2,4,7,8,6,1,5";
                                entity.pid = id;
                                entity.updatatime = System.currentTimeMillis()+"";
                                entity.evalueTitle = ename;
                                entity.name=name;
                                entity.evalue = evalue;
                                mPulseCondtion.insert(entity);
                            }
                        }
                    }
                }else if(msg.what == 2222){
                    donghua.setVisibility(View.GONE);
                    tuijian.setVisibility(View.GONE);
                    animationDrawable.stop();
                    progress.setVisibility(View.VISIBLE);
                    getJson();
                }
            }catch (Exception e){

            }
        }
    };
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
                    convertView = View.inflate(NewResultActivity.this, R.layout.newsearchadapter, null);
                }else{
                    convertView = View.inflate(NewResultActivity.this, R.layout.newsearchadapterqita, null);
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
                    if(item.typeid.equals("4")){
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

                                if(tabState >= 6){
                                    horlist.setSelection(2,true);
                                }

                                isClear = true;
                                isShowXiangshi = true;
                                adapter = null;
//                            horlist.setSelection(tabState,true);
                                if(heart != null){
                                    heart.notifyDataSetChanged();
                                }
//                                getTypeContent(key,titlekey.typeid,"");
                                sizepage = "10";
                                typeid = titlekey.typeid;
                                if(titlekeys.size() <=5){
                                    if(tabState == 0){
                                        initline();
                                        itemline1.setVisibility(View.VISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=0;
                                        TextPaint tp ;
                                        tp=item1.getPaint();
                                        tp.setFakeBoldText(true);
                                        item1.setTextColor(0xff33afb6);
                                        typeid="";
                                        page=1;
                                        sizepage="3";
                                    }else if(tabState == 1){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.VISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=1;
                                        TextPaint tp ;
                                        tp=item2.getPaint();
                                        tp.setFakeBoldText(true);
                                        item2.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(1);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 2){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.VISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=2;
                                        TextPaint tp ;
                                        tp=item3.getPaint();
                                        tp.setFakeBoldText(true);
                                        item3.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(2);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 3){
                                        initline();
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.VISIBLE);
                                        itemline5.setVisibility(View.INVISIBLE);
                                        tabState=3;
                                        TextPaint tp ;
                                        tp=item4.getPaint();
                                        tp.setFakeBoldText(true);
                                        item4.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(3);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }else if(tabState == 4){
                                        initline();
                                        tabState=4;
                                        itemline1.setVisibility(View.INVISIBLE);
                                        itemline2.setVisibility(View.INVISIBLE);
                                        itemline3.setVisibility(View.INVISIBLE);
                                        itemline4.setVisibility(View.INVISIBLE);
                                        itemline5.setVisibility(View.VISIBLE);
                                        TextPaint tp ;
                                        tp=item5.getPaint();
                                        tp.setFakeBoldText(true);
                                        item5.setTextColor(0xff33afb6);
                                        TitleKey heart= titlekeys.get(4);
                                        typeid=heart.typeid;
                                        sizepage="10";
                                        page=1;
                                        isClear=true;
                                    }
                                }
                                getJson();
                            }
                        }
                    });

                }else if(item.typeid.equals("1") ){
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
                                Intent intent = new Intent(NewResultActivity.this, SpecialActivity.class);
                                intent.putExtra("id", item.id);
                                startActivity(intent);
                            }else if(item.typename.equals("html") || item.typeid.equals("10000")){
                                Intent intent=new Intent(NewResultActivity.this, WebViewActivity.class);
                                intent.putExtra("title", item.title);
                                intent.putExtra("url", item.detail);
                                startActivity(intent);
                            }else if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(NewResultActivity.this, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.id);
                                intent.putExtra("name", item.typename);
                                intent.putExtra("pic", item.litpic);
                                startActivity(intent);
                            }
                        }
                    });
                }else if(item.typeid.equals("5") ||item.typeid.equals("6")){
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
                    if(item.typeid.equals("6")){
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
                            if( item.typeid.equals("6")){
                                Intent intent = new Intent(NewResultActivity.this, NewZhengCeActivity.class);
                                intent.putExtra("aid",  item.id);
                                startActivity(intent);
                            }else if(item.typeid.equals("5")){
                                Intent intent = new Intent(NewResultActivity.this, NewZhuanliActivity.class);
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

                }else if(item.typeid.equals("4")){
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
                            Intent intent = new Intent(NewResultActivity.this, XinFanAnCeShi.class);
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
                            Intent intent = new Intent(NewResultActivity.this, UnitedStatesDeilActivity.class);
                            intent.putExtra("aid",item.aid1 );
                            startActivity(intent);
                        }
                    });
                    holdView.jigou2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewResultActivity.this, UnitedStatesDeilActivity.class);
                            intent.putExtra("aid",item.aid2);
                            startActivity(intent);
                        }
                    });
                    holdView.jigou3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewResultActivity.this, UnitedStatesDeilActivity.class);
                            intent.putExtra("aid",item.aid3);
                            startActivity(intent);
                        }
                    });


                }else if(item.typename.equals("热词前")){
                    holdView.contentView.setVisibility(View.GONE);
                    holdView.contentViewlay.setVisibility(View.GONE);
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
                        LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewResultActivity.this).inflate(R.layout.hotconent, null);
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

                                if(isLoading){
                                    return;
                                }
                                isLoading = true;
                                isClear = true;
                                isNodata = false;
                                donghua.setVisibility(View.VISIBLE);
                                tuijian.setVisibility(View.VISIBLE);
                                animationDrawable = (AnimationDrawable)tuijian.getBackground();
                                animationDrawable.start();
                                handler.sendEmptyMessageDelayed(2222, 4500);
                                adapter = null;
                                TitleKey titlekey =null;
                                key=hot;
                                isShowXiangshi = true;

                                isHeart = false;
                                TitleKey word = titlekeys.get(tabState);
                                formatfont(word.typename);
                                titlekey=titlekeys.get(tabState);
                                typeid = titlekey.typeid;
                                Intent intent = new Intent(NewResultActivity.this, NewSearchContent.class);
                                intent.putExtra("hot", key);
                                intent.putExtra("typeid", typeid);
                                startActivity(intent);


                            }
                        });
                        holdView.contentView.addView(childBtn);
                    }

                }else if(item.typename.equals("热词后")){
                    holdView.contentView.setVisibility(View.GONE);
                    holdView.contentViewlay.setVisibility(View.GONE);
                    holdView.contentlay.setVisibility(View.GONE);
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
                        LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewResultActivity.this).inflate(R.layout.hotitem, null);
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

                                isHeart = false;
                                donghua.setVisibility(View.VISIBLE);
                                tuijian.setVisibility(View.VISIBLE);
                                animationDrawable = (AnimationDrawable)tuijian.getBackground();
                                animationDrawable.start();
                                handler.sendEmptyMessageDelayed(2222, 4500);
                                adapter = null;
                                typeid = titlekey.typeid;
                                Intent intent = new Intent(NewResultActivity.this, NewSearchContent.class);
                                intent.putExtra("hot", key);
                                intent.putExtra("typeid", typeid);
                                startActivity(intent);

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
                            Intent intent = new Intent(NewResultActivity.this, SheBeiDeilActivity.class);
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
                                Intent intent = new Intent(NewResultActivity.this, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.id);
                                intent.putExtra("name", item.typename);
                                intent.putExtra("pic", item.litpic);
                                startActivity(intent);
                            }else if(item.typename.equals("html") || item.typeid.equals("10000")){
                                Intent intent=new Intent(NewResultActivity.this, WebViewActivity.class);
                                intent.putExtra("title", item.title);
                                intent.putExtra("url", item.detail);
                                startActivity(intent);

                            }else{
                                if(item.typeid.equals("4")){
                                    Intent intent = new Intent(NewResultActivity.this, XinFanAnCeShi.class);
                                    intent.putExtra("aid",  item.id);
                                    startActivity(intent);
                                }else if(item.typeid.equals("5")){
                                    Intent intent = new Intent(NewResultActivity.this, NewZhuanliActivity.class);
                                    intent.putExtra("aid",  item.id);
                                    startActivity(intent);
                                }else if(item.typeid.equals("6")){
                                    Intent intent = new Intent(NewResultActivity.this, NewZhengCeActivity.class);
                                    intent.putExtra("aid",  item.id);
                                    startActivity(intent);
                                }else if(item.typeid.equals("2")){
                                    String labels = item.labels;
                                    if(labels != null){
                                        if(labels.contains("精品项目")){
                                            Intent intent=new Intent(NewResultActivity.this, ActiveActivity.class);
                                            intent.putExtra("title", item.title);
                                            intent.putExtra("url", item.url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(NewResultActivity.this, NewProjectActivity.class);
                                            intent.putExtra("aid",  item.id);
                                            startActivity(intent);
                                        }
                                    }else{
                                        Intent intent = new Intent(NewResultActivity.this, NewProjectActivity.class);
                                        intent.putExtra("aid",  item.id);
                                        startActivity(intent);
                                    }

                                }else {
                                    Intent intent = new Intent(NewResultActivity.this, DetailsActivity.class);
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
                convertView = View.inflate(NewResultActivity.this, R.layout.titlekeys,null);
                holdView.sollect = convertView.findViewById(R.id.sollect);
                holdView.txt = convertView.findViewById(R.id.txt);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            final TitleKey title = titlekeys.get(position);
            holdView.txt.setText(title.typename);

            if(tabindex == position){
                holdView.txt.setTextColor(0xff33afb6);
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
    private void initline(){
        itemline1.setVisibility(View.INVISIBLE);
        itemline2.setVisibility(View.INVISIBLE);
        itemline3.setVisibility(View.INVISIBLE);
        itemline4.setVisibility(View.INVISIBLE);
        itemline5.setVisibility(View.INVISIBLE);
        item1.setTextColor(0xff727272);
        TextPaint tp = item1 .getPaint();
        tp.setFakeBoldText(false);

        item2.setTextColor(0xff727272);
        tp = item2.getPaint();
        tp.setFakeBoldText(false);
        item3.setTextColor(0xff727272);
        tp = item3.getPaint();
        tp.setFakeBoldText(false);
        item4.setTextColor(0xff727272);
        tp = item4.getPaint();
        tp.setFakeBoldText(false);
        item5.setTextColor(0xff727272);
        tp = item5.getPaint();
        tp.setFakeBoldText(false);
    }

    class TitleKey{
        public String typeid;
        public String typename;
        public String count;
    }

    private void formatfont(String typename){
        String content = "<font color=\"#181818\">" +"没有找到相关的"+typename+"</font>";
        keyword.setText(Html.fromHtml(content));
    }

    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
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
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(NewResultActivity.this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setText(item);
                tv.setTag(item);
                childBtns.add(childBtn);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView)v;
                        String hot;
                        hot = tv.getText().toString();
                        Intent intent = new Intent(NewResultActivity.this, NewSearchContent.class);
                        intent.putExtra("hot", hot);
                        intent.putExtra("typeid", typeid);
                        startActivity(intent);
                    }
                });

                hotView.addView(childBtn);
            }
        }
    }

    private void getlistJson(){
        String url="http://"+ MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","list");
        map.put("id",id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
    }
}
