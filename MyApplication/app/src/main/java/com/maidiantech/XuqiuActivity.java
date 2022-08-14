package com.maidiantech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;


import Util.SharedPreferencesUtil;
import view.ProgressWebView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2017/3/30.
 */

public class XuqiuActivity extends AutoLayoutActivity {
    public ProgressWebView webview;
    private SwipeRefreshLayout swipeLayout;
    ImageView back;
    TextView title;
    String url , titletxt;
    ImageView need_add;
    @SuppressLint("SetJavaScriptEnabled") @SuppressWarnings("deprecation")
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
        title.setText(titletxt);
        webview = (ProgressWebView)findViewById(R.id.webview);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webview.canGoBack()){
                    webview.goBack();
                }else{
                    finish();
                }
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

        webview.loadUrl(url);
        //添加javaScript支持
        webview.getSettings().setJavaScriptEnabled(true);
        //取消滚动条
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //触摸焦点起作用
        webview.requestFocus();
        //点击链接继续在当前browser中响应
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                if (url.contains("www.maidiantech.com") || url.contains("123.206.8.208")) {
                    return super.shouldInterceptRequest(view, url);
                }else{
                    return new WebResourceResponse(null,null,null);
                }

            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("xuqiu")){
                    XuqiuActivity.this.finish();
                }else if(url.contains("returnshop")){
                    XuqiuActivity.this.finish();
                }else if(url.contains("returnmyneeds")){
                    XuqiuActivity.this.finish();
                }else if (url.contains("http://www.maidiantech.com/plus/patent/select.html")){
                    finish();
                }else if(url.contains("is-need-login")){
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                        String newurl =url+"&mid="+mid+"&tel="+tel;
                        view.loadUrl(newurl);

                    }else{
                        Intent intent = new Intent(XuqiuActivity.this, MyloginActivity.class);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("需求浏览器");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("需求浏览器");
    }
}
