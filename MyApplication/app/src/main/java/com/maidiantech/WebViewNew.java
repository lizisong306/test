package com.maidiantech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import Util.SharedPreferencesUtil;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;

import application.MyApplication;
import view.ProgressWebView;
import view.StyleUtils1;

/**
 * Created by Administrator on 2019/3/20.
 */

public class WebViewNew extends AutoLayoutActivity {
    public ProgressWebView webview;
    private SwipeRefreshLayout swipeLayout;
    ImageView back;
    TextView title;
    String url , titletxt;
    ImageView need_add;
    String mid;
    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        titletxt = getIntent().getStringExtra("title");
        url  = getIntent().getStringExtra("url");
        back = (ImageView)findViewById(R.id.about_backs);
        title = (TextView)findViewById(R.id.titlecontent);
        need_add=(ImageView) findViewById(R.id.need_add);

        need_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebViewNew.this, ShareActivity.class);
                intent.putExtra("title", titletxt);
                intent.putExtra("txt", titletxt);
                intent.putExtra("imageurl", "http://"+ MyApplication.ip+"/uploads/logo/logo.png");
                intent.putExtra("Tarurl", url);
                startActivity(intent);
            }
        });
//        Log.d("lizisong", "dkfdkfjkdjfkdfkdkfdfdfjkdfjkdjfkdjfkdjfkdjfkdj");
        title.setText(titletxt);
        if(titletxt != null &&titletxt.equals("我的需求")){
            need_add.setVisibility(View.VISIBLE);
            need_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WebViewNew.this, XuqiuActivity.class);
                    intent.putExtra("title", "我的需求");
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    intent.putExtra("url", "http://www.zhongkechuangxiang.com/plus/an-demand1/demand-submit.html?mid="+mid);
                    startActivity(intent);

                }
            });

        }else{
            need_add.setVisibility(View.GONE);
        }

//        if(url == null || (url != null && !url.contains("http"))){
//            need_add.setVisibility(View.GONE);
//        }else{
//            need_add.setVisibility(View.VISIBLE);
//        }

        webview = (ProgressWebView)findViewById(R.id.webview);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(webview.canGoBack()){
//                    webview.goBack();
//                }else{
                finish();
//                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                webview.loadUrl(webview.getUrl());
                webview.isRefresh(true);
            }
        });
        swipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        webview = (ProgressWebView)findViewById(R.id.webview);
        if(url != null){
            if(url.contains("?")){
                url = url+"&accessid="+MyApplication.deviceid;
            }else{
                url = url+"?accessid="+MyApplication.deviceid;
            }
            webview.loadUrl(url);
        }
        //添加javaScript支持
        webview.getSettings().setJavaScriptEnabled(true);
        //取消滚动条
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);//DOM Storage
        //触摸焦点起作用
        webview.requestFocus();
        //点击链接继续在当前browser中响应
        webview.setWebViewClient(new WebViewClient(){

//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//
////                if (url.contains("http://www.zhongkechuangxiang.com") || url.contains("123.206.8.208")) {
////                    return super.shouldInterceptRequest(view, url);
////                }else{
////                    return new WebResourceResponse(null,null,null);
////                }
//
//            }


//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//
//
//                return super.shouldInterceptRequest(view, request);
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("lizisong","shouldOverrideUrlLoading url"+url);
                if(url.contains("returnshop")){
                    WebViewNew.this.finish();
                }else if(url.contains("http://www.zhongkechuangxiang.com/plus/patent/detail.html?")){
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                    String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                    String newurl =url+"&mid="+mid+"&tel="+tel;
                    Intent  intent = new Intent(WebViewNew.this, XuqiuActivity.class);
                    intent.putExtra("url", newurl);
                    intent.putExtra("title", " ");
                    startActivity(intent);
//                    view.loadUrl(newurl);
                }else if (url.contains("back")){
                    finish();

                }else if(url.contains("is-need-login")){
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                        String newurl =url+"&mid="+mid+"&tel="+tel;
                        view.loadUrl(newurl);

                    }else{
                        Intent intent = new Intent(WebViewNew.this, MyloginActivity.class);
                        startActivity(intent);
                    }
                }else if(url.contains("tel:")){
                    if(url != null){
                        String [] phone = url.split(":");
                        if(phone != null && phone.length ==2){
                            String phoneNum = phone[1];
                            if(phoneNum != null){
                                try {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + phoneNum);
                                    intent.setData(data);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        }
                    }
                }
                else{
                    view.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("lizisong","onPageStarted url"+url);
                if(titletxt != null && titletxt.equals("我的需求")){
                    if(url.contains("aid=") || url.contains("demand-submit.html?mid=")||url.contains("appointment-success.html")){
                        need_add.setVisibility(View.GONE);
                    }else{
                        need_add.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webview.setOnLoadFinishListener(new ProgressWebView.LoadFinishListener() {

            @Override
            public void OnLoadfinish() {
                // TODO Auto-generated method stub
                swipeLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
//            webview.goBack();// 返回前一个页面
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新资讯");
        MobclickAgent.onResume(this);
        if(titletxt != null &&titletxt.equals("我的需求")){
            if(webview != null){
                webview.reload();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新资讯");
        MobclickAgent.onPause(this);
    }
}
