package com.maidiantech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import Util.UIHelper;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianZan;
import dao.dbentity.CollectionEntity;
import entity.Codes;
import entity.EarlyEntity;
import entity.Posts;
import entity.ZanCode;
import entity.recode;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/6/9.
 */

public class EarlyRef extends SlidingActivity {
    UMShareAPI mShareAPI;
    private MaiDianZan maiDianCollectionzan = null;
    private MaiDianCollection maiDianCollection = null;
    private String aid,ips,name,zanjson;
    ImageView back;
    private ShareAction action;
    private ImageView my_collect;
    private ImageView shares;
    boolean dianjie = false;
    private String pic;
    ImageView check_guanzhu;
    boolean zan_state = false;
    private ArrayList<CollectionEntity> listCollections = null;
    private ArrayList<CollectionEntity> listCollectionzan = null;
    boolean collect_state = false;
    private CollectionEntity mCollectionEntity = null;
    private CollectionEntity mCollectionEntityzan = null;
    private OkHttpUtils Okhttp;
    private UMImage image;
    private recode data;
    String loginState;
    String mids,collectjson,quxiaojson,id;
    private WebView webview;
    private String title;
    private String bodys;
    public  String str;
    public String liujson;
    String oldmids;
    private ProgressBar progressBar;
    String json;
    boolean is_state = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earlyref);
        maiDianCollection = MaiDianCollection.getInstance(this);
        maiDianCollectionzan= MaiDianZan.getInstance(this);
        mShareAPI = UMShareAPI.get(this);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Intent intent = getIntent();
        aid = intent.getStringExtra("id");

        ips = MyApplication.ip;
        name = intent.getStringExtra("name");
        initView();
        if(mCollectionEntity==null){
            mCollectionEntity=new CollectionEntity();
            mCollectionEntity.aid = aid;
            mCollectionEntity.title = title;
            mCollectionEntity.upFlag = 0;
            mCollectionEntity.updateTime = System.currentTimeMillis()+"";
            mCollectionEntity.type = name;
            mCollectionEntity.pic = pic;

        }
        progressBar = (ProgressBar)findViewById(R.id.progress);
        webview = (WebView) findViewById(R.id.wbviw);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new SampleWebViewClient());
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(EarlyRef.this, "网络不给力", Toast.LENGTH_SHORT).show();

        } else {
            progressBar.setVisibility(View.VISIBLE);
            oldmids = aid;
            liulang();
            getjson();
        }
        //html转成string字符串
        try {
            str = new String(toByteArray(getAssets().open("breakfast.html")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shares = (ImageView) findViewById(R.id.shares);

        my_collect = (ImageView) findViewById(R.id.my_collect);

        check_guanzhu=(ImageView) findViewById(R.id.check_guanzhu);
        listCollections = maiDianCollection.get();

        if(listCollections.size() >0){
            for(int i=0; i<listCollections.size(); i++){
                CollectionEntity item = listCollections.get(i);
                if(item != null && item.aid != null){
                    if(item.aid.equals(aid)){
                        collect_state = true;
                        mCollectionEntity = item;
                        break;
                    }
                }
            }
        }
        if(collect_state){
            my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
        }else{
            my_collect.setBackgroundResource(R.mipmap.zixun_collect);
        }

        my_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dianjie){
                    return;
                }
                dianjie = true;
                if(!loginState.equals("1")) {
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    Toast.makeText(EarlyRef.this, "请您登录", Toast.LENGTH_SHORT).show();
                }else{
                    if(collect_state == false){
                        listCollections = maiDianCollection.getcollect();
                        for(int i=0;i<listCollections.size();i++){
                            CollectionEntity  item = listCollections.get(i);
                            if(item.aid.equals(aid)){
                                return;
                            }
                        }
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(EarlyRef.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        }else{
                            collectjson();
                        }

                    }else{
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(EarlyRef.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        }else{
                            dismisjson();
                        }

                    }
                }
                dianjie = false;
            }
        });

        listCollectionzan = maiDianCollectionzan.get();


        if(listCollectionzan.size() >0){
            for(int i=0; i<listCollectionzan.size(); i++){
                CollectionEntity item = listCollectionzan.get(i);
                if(item != null && item.aid != null){
                    if(item.aid.equals(aid)){
                        zan_state = true;
                        mCollectionEntityzan = item;
                        break;
                    }
                }
            }
        }
        if(zan_state){
            check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
        }else{
            check_guanzhu.setBackgroundResource(R.mipmap.zixun_like);
        }

        check_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCollectionEntityzan!=null){
                    Toast.makeText(EarlyRef.this, "已经点过赞", Toast.LENGTH_SHORT).show();
                    return;
                }
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(EarlyRef.this, "网络不给力", Toast.LENGTH_SHORT).show();

                }else{
                    zanjson();
                }
            }
        });

        try {
            shares.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent = new Intent(EarlyRef.this, ShareActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("txt", title);
                        intent.putExtra("Tarurl", "http://"+ips+"/api/share.php?aid="+aid+"");
                        if(pic==null|| pic.equals("")){
                            intent.putExtra("imageurl", "http://"+ips+"/uploads/logo/logo.png");
                        }else {
                            intent.putExtra("imageurl", pic);
                        }
                        startActivity(intent);

//                        UIHelper.showDialogForLoading(EarlyRef.this, "", true);
//                       if(pic==null|| pic.equals("")){
//                            image = new UMImage(EarlyRef.this, "http://"+ips+"/uploads/logo/logo1.png");
//                        }else {
//                            image = new UMImage(EarlyRef.this, pic);
//                        }
//
//                        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                                {
//                                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                                };
//                        action = new ShareAction(EarlyRef.this);
//                        UIHelper.hideDialogForLoading();
//
//                            action.setDisplayList(displaylist)
//                                    .withText(title)
//                                    .withTitle(title)
//                                    .withTargetUrl("http://"+ips+"/api/share.php?aid="+aid+"")
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .open();


                    }catch (Exception e){}
                }
            });
        }catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
        MobclickAgent.onPageStart("科讯早餐");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("科讯早餐");
    }

    public void collectjson(){
        String url ="http://"+ips+"/api/arc_store.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String,String> map = new HashMap<>();
        map.put("aid",aid);
        map.put("mid",mids);
        map.put("method","add");
        networkCom.getJson(url,map,handler,3,0);

    }
    public void dismisjson(){
                mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                listCollections = maiDianCollection.get();
                boolean state = false;
                CollectionEntity itemdismis=null;
                if(listCollections.size() >0){
                    for(int i=0; i<listCollections.size(); i++){
                        CollectionEntity item = listCollections.get(i);
                        if(item != null && item.aid != null){
                            if(item.aid.equals(aid)){
                                state = true;
                                itemdismis = item;
                                break;
                            }
                        }
                    }
                }
                if(itemdismis!=null){
                    id=  itemdismis.pid;
                }
                String url ="http://"+ips+"/api/arc_store.php";
                NetworkCom networkCom = NetworkCom.getNetworkCom();
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                map.put("mid",mids);
                map.put("method","cancel");
                networkCom.getJson(url,map,handler,4,0);

    }

    public void  zanjson(){
        if(!loginState.equals("1")){
            Message msg=new Message();
            msg.what=3;
            handlers.sendMessage(msg);
        }else{
            String url ="http://"+ips+"/api/arc_zan.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            HashMap<String,String> map = new HashMap<>();
            map.put("aid",aid);
            map.put("mid",mids);
            networkCom.getJson(url,map,handler,2,0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Gson g=new Gson();
                liujson = (String)msg.obj;
                Codes codes = g.fromJson(liujson, Codes.class);
            }
            if(msg.what == 1){
                Gson g = new Gson();
                try {
                    webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
                    webview.setHapticFeedbackEnabled(false);
                    webview.getSettings().setSupportZoom(true);
                    webview.getSettings().setDomStorageEnabled(true);
                    webview.getSettings().setSupportMultipleWindows(true);
                    webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                    json = (String)msg.obj;
                    EarlyEntity early = g.fromJson(json, EarlyEntity.class);
                    List<Posts> data = early.data.posts;
                    title = early.data.title;
                    pic = early.data.litpic;
                    str = str.replace("{{title}}", early.data.title);
                    str = str.replace("{{img}}",early.data.litpic);
                    str = str.replace("{{date}}", TimeUtils.getStrTime(early.data.pubdate));
                    String div="";
                    for(int i=0; i<data.size();i++){
                        Posts item = data.get(i);
                        String temp = URLDecoder.decode((item.aid+":"+item.getTypename()), "UTF-8");
                        div=div+"<div class=\"line\">" + "<a href="+temp+">"+item.getTitle()+"</a>"
                        +"</div>"+"<div class=\"word\">"+item.getDescription()+"</div>";
                    }
                    str = str.replace("{{body}}",div);
                    String baseUrl = "file:///android_asset/";

                    webview.loadDataWithBaseURL(baseUrl, str, "text/html",
                            "utf-8", null);

                }catch (Exception e){

                }
            }

            if(msg.what == 2){
                    Gson g=new Gson();
                    zanjson = (String)msg.obj;
                    ZanCode zanCode = g.fromJson(zanjson,ZanCode.class);
                    if(zanCode.code==1){
                        String aids=zanCode.data.aid;
                        CollectionEntity value = new CollectionEntity();
                        value.aid = aid;
                        value.title = title;
                        value.upFlag = 0;
                        value.updateTime = System.currentTimeMillis()+"";
                        value.type = name;
                        value.pic = pic;
                        value.iscollect="0";
                        value.isAdd = 0;
                        if(data != null){
                            if(data.click != null){
                                value.click = data.click;
                            }
                            value.image=data.image;
                            value.imageState = data.imageState;
                            value.description = data.description;
                            if(data.getArea_cate() != null){
                                value.area_cate = data.getArea_cate().getArea_cate1();
                            }
                            value.is_academician = data.getIs_academician();
                        }

                        maiDianCollectionzan.insert(value);

                        check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
                        Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                    }else if(zanCode.code==-1){
                        if(zanCode.message.contains("已点过赞")){
                            Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                            CollectionEntity value = new CollectionEntity();
                            value.aid = aid;
                            value.title = title;
                            value.upFlag = 0;
                            value.updateTime = System.currentTimeMillis()+"";
                            value.type = name;
                            value.pic = pic;
                            value.iscollect="0";
                            value.isAdd = 0;
                            if(data != null){
                                if(data.click != null){
                                    value.click = data.click;
                                }
                                value.image=data.image;
                                value.imageState = data.imageState;
                                value.description = data.description;
                                if(data.getArea_cate() != null){
                                    value.area_cate = data.getArea_cate().getArea_cate1();
                                }
                                value.is_academician = data.getIs_academician();
                            }

                            maiDianCollectionzan.insert(value);
                            check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
                        }
                    }
            }

            if(msg.what==3){
                Gson g=new Gson();
                collectjson = (String) msg.obj;
                ZanCode zanCode = g.fromJson(collectjson,ZanCode.class);
                if(zanCode.code==1){
                    collect_state = true;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
                    id = zanCode.data.id;
                    CollectionEntity value = new CollectionEntity();
                    value.aid = aid;
                    value.title = title;
                    value.upFlag = 0;
                    value.updateTime = System.currentTimeMillis()+"";
                    value.type = name;
                    value.pic = pic;
                    value.iscollect="0";
                    value.pid=id;
                    value.isAdd = 0;
                    if(data != null){
                        value.image=data.image;
                        if( data.click != null){
                            value.click = data.click;
                        }
                        value.imageState = data.imageState;
                        value.description = data.description;
                        if(data.getArea_cate() != null){
                            value.area_cate = data.getArea_cate().getArea_cate1();
                        }
                        value.is_academician = data.getIs_academician();
                    }

                    maiDianCollection.insert(value);
                    Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }else if(zanCode.code==-1){
                    Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }

            if(msg.what==4){
                Gson g = new Gson();
                quxiaojson= (String)msg.obj;
                ZanCode zanCode = g.fromJson(quxiaojson,ZanCode.class);
                if(zanCode.code==1){
                    collect_state = false;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    maiDianCollection.deletebyaid(aid);
                    Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }else if(zanCode.code==-1){
                    Toast.makeText(EarlyRef.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    Handler handlers=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                Toast.makeText(EarlyRef.this, "请您登录", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//            } else {
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(EarlyRef.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
//            if (t != null) {
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
//        }
//    };

    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public void getjson() {
        String url = "http://"+ips+"/api/getReferPlanDetail.php";
        HashMap<String,String> map =new HashMap<>();
        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        map.put("planID",aid);
        networkCom.getJson(url,map,handler,1,0);
    }

    public  byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }

    public void liulang(){
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        String url = "http://"+ips+"/api/arc_click.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("aid", aid);
        networkCom.getJson(url, map,handler,0,0);
    }

    class SampleWebViewClient extends WebViewClient {

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            is_state = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {

                if(url != null){
                    Log.d("lizisong", "url:"+url);
                    String str = URLDecoder.decode((url), "UTF-8");
                    String[] item = str.split(":");
                    name = item[1];
                    if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                        Intent intent = new Intent(EarlyRef.this, ZixunDetailsActivity.class);
                        intent.putExtra("id", item[0]);
                        intent.putExtra("name", item[1]);
                        startActivity(intent);
                    }else{
                        if(name != null && (name.equals("专家") || name.equals("人才"))){
                            Intent intent = new Intent(EarlyRef.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item[0]);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(EarlyRef.this, DetailsActivity.class);
                            intent.putExtra("id", item[0]);
                            intent.putExtra("name", item[1]);
                            startActivity(intent);
                        }

                    }

//                    if(item.typeid.equals("6")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "政策");
//                        startActivity(intent);
//                    }else if(item.typeid.equals("8")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "实验室");
//                        startActivity(intent);
//                    }else if(item.typeid.equals("4")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "人才");
//                        startActivity(intent);
//                    }else if(item.typeid.equals("5")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "专利");
//                        startActivity(intent);
//                    }else if(item.typeid.equals("7")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "设备");
//                        startActivity(intent);
//                    }else if(item.typeid.equals("2")){
//                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "项目");
//                        startActivity(intent);
//                    }else{
//
//                    }
                }
            }catch (Exception e){}

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            is_state = false;
            oldmids = aid;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d("lizisong", "goog");

//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            x = (int)ev.getX();
//        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
//            if((int)ev.getX()-x < 40){
//                return super.dispatchTouchEvent(ev);
//            }
//        }
        if (!SlidingActivity.isSliding) {
            setsetSlidingLayou(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

}
