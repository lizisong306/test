package com.maidiantech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import application.MyApplication;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/6/21.
 */

public class ActiveActivity extends com.maidiantech.common.ui.BaseActivity {
    public WebView webview;
    String url, content;
    ProgressBar progressBar;
    String mid;
    String shareimg, sharemdtitle,aidtitle;
    UMShareAPI mShareAPI;
    private ShareAction action;
    private UMImage image;
    private String jsMethod;
    private boolean isCallJS = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active);
        setSystemBar();
        setRightIconHide();
        setClose();
        mShareAPI = UMShareAPI.get(this);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);

        url = getIntent().getStringExtra("url");
        content = getIntent().getStringExtra("title");

        setTitle(content);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        webview = (WebView) findViewById(R.id.webview);
        setLeftClickRes(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    hintKbTwo();
                    finish();
                }
            }
        },R.mipmap.back);

        webview = (WebView) findViewById(R.id.webview);
        try {
            if (url.contains("mdtitle")) {
                int postion;
                String title1;
                postion = url.indexOf("mdtitle");
                title1 = url.substring(postion);
                String[] md = title1.split("=");
                if (md != null) {
                    if (md.length > 1) {
                        String dex = null;
                        try {
                            dex = URLDecoder.decode(md[1], "UTF-8");
                        } catch (IllegalArgumentException ex) {

                        } catch (RuntimeException exx) {

                        } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
                        }
                        if(dex != null && !dex.equals("")){
                            setTitle(dex);
                        }

                    }
                }

            }

        }catch (Exception e){

        }

        progressBar.setVisibility(View.VISIBLE);
        if(url != null && !url.contains("mid=")){
            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            if(loginState.equals("1")){
                url=url+"?mid="+mid;
            }
        }
        if(url != null){
            webview.loadUrl(url);
        }
        //添加javaScript支持
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.requestFocus();
        //点击链接继续在当前browser中响应
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                if (url.contains("www.maidiantech.com") || url.contains("123.206.8.208") || url.contains("www.zhongkechuangxiang.com") || url.contains("123.207.164.210")) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    return new WebResourceResponse(null, null, null);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("lizisong", "url:" + url);
                try {
                    if(url.contains("http://")){
                        if (url.contains("mdtitle") && !url.contains("share")) {
                            int postion;
                            String title1;
                            postion = url.indexOf("mdtitle");
                            title1 = url.substring(postion);
                            String[] md = title1.split("=");
                            if (md != null) {
                                if (md.length > 1) {
                                    String dex = URLDecoder.decode(md[1], "UTF-8");
                                    setTitle(dex);
                                }
                            }
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                            String newurl;
                            if (url.contains("?")) {
                                newurl = url + "&mid=" + mid + "&tel=" + tel;
                            } else {
                                newurl = url + "?&mid=" + mid + "&tel=" + tel;
                            }
//                            progressBar.setVisibility(View.VISIBLE);

                            view.loadUrl(newurl);

                        } else if (url.contains("http") && url.contains("is-need-login")) {
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (loginState.equals("1")) {
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                String newurl;
                                String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                                if (url.contains("?")) {
                                    newurl = url + "&mid=" + mid + "&tel=" + tel;
                                } else {
                                    newurl = url + "?&mid=" + mid + "&tel=" + tel;
                                }
//                            progressBar.setVisibility(View.VISIBLE);
                                view.loadUrl(newurl);
                            } else {
                                Intent intent = new Intent(ActiveActivity.this, MyloginActivity.class);
                                startActivity(intent);
                            }
                        } else if (url.contains("is-need-pop")) {
                            finish();
                        } else if (url.contains("share")) {
                            int postion;
                            String title;
                            postion = url.indexOf("mdtitle");
                            title = url.substring(postion);
                            String[] pos = title.split("&");
                            if (pos != null) {
                                if (pos.length == 1) {
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");

                                } else if (pos.length == 2) {
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");
                                    String mdimg = pos[1];
                                    String[] temp1 = mdimg.split("=");
                                    shareimg = temp1[1];
                                }else if(pos.length == 3){
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");
                                    String mdimg = pos[1];
                                    String[] temp1 = mdimg.split("=");
                                    shareimg = temp1[1];
                                    String aid = pos[2];
                                    String[] temp2 = aid.split("=");
                                    aidtitle = temp2[1];
                                }

                            }
                            shareonclick();
                        } else if(url.contains("zhuanjiaOrder")){
                            int postion;
                            String title;
                            String id = "";
                            String typeid="";
                            postion = url.indexOf("id=");
                            title = url.substring(postion);
                            String[] pos = title.split("&");
                            if(pos != null){
                                if(pos[0] != null){
                                    String idStr = pos[0];
                                    String[] temp = idStr.split("=");
                                    id =temp[1];
                                }
                                if(pos.length >1){
                                    if (pos[1] != null) {
                                        String tyepidStr = pos[1];
                                        String[] temp = tyepidStr.split("=");
                                        typeid =temp[1];
                                    }
                                }
                                Intent intent = new Intent(ActiveActivity.this, TianXuQiu.class);
                                intent.putExtra("aid", id);
                                intent.putExtra("typeid", typeid);
                                startActivity(intent);
                            }
                        }else {
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            String newurl;
                            String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");

                            if (url.contains("?")) {
                                newurl = url + "&mid=" + mid + "&tel=" + tel;
                            } else {
                                newurl = url + "?&mid=" + mid + "&tel=" + tel;
                            }
                            progressBar.setVisibility(View.VISIBLE);
                            view.loadUrl(newurl);
                        }
                    }else if(url.contains("maidian://")){
                       if(url.contains("jumpToDetail")){
                           int postion;
                           String content;
                           String  id="",typeid = "",name="";
                           postion = url.indexOf("typeid");
                           content = url.substring(postion);
                           String[] pos = content.split("&");
                           if(pos != null){
                               if(pos[0] != null){
                                   String idStr = pos[0];
                                   String[] temp = idStr.split("=");
                                   typeid =temp[1];
                               }
                               if(pos.length >1){
                                   if (pos[1] != null) {
                                       String tyepidStr = pos[1];
                                       String[] temp = tyepidStr.split("=");
                                       id =temp[1];
                                   }
                               }

                               if(typeid != null){
                                   if(typeid.equals("1")){
                                       name = "资讯";
                                   }else if(typeid.equals("6")){
                                       name = "政策";

                                   }else if(typeid.equals("9")){
                                       name = "推荐";
                                   }else if(typeid.equals("2")){
                                       name = "项目";
                                   }else if(typeid.equals("4")){
                                       name = "专家";
                                   }else if(typeid.equals("7")){
                                       name = "设备";

                                   }else if(typeid.equals("5")){
                                       name = "专利";

                                   }else if(typeid.equals("8")){
                                       name = "研究所";

                                   }else if(typeid.equals("11")){
                                       name = "活动";

                                   }else{
                                       name = "资讯";
                                   }
                               }

                               Intent intent = new Intent(ActiveActivity.this, DetailsActivity.class);
                               intent.putExtra("id", id);
                               intent.putExtra("name", name);
                               startActivity(intent);

                           }

                       } else if(url.contains("jingPinXiangMu")){
                            int postion;
                            String content;
                            String[] arry;
                            postion = url.indexOf("flag");
                            content = url.substring(postion);
                            if (content.contains("=")) {
                                arry = content.split("=");
                                Intent intent = new Intent(ActiveActivity.this, XiangMuDuiJieActivity.class);
                                if(arry != null){
                                    String type = arry[1];
                                    intent.putExtra("channelid",type);
                                }
                                startActivity(intent);
                            }

                        }else if(url.contains("javaScriptLoginComplete")){
                                int postion;
                                String content;
                                postion = url.indexOf("javaScriptLoginComplete");
                                content = url.substring(postion);
                                if(content != null){
                                    String[] jsarray = content.split("/");
                                    if(jsarray != null && jsarray.length >1 ){
                                        jsMethod= jsarray[1];
                                    }
                                }
                                if(jsMethod != null){
                                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                    String phone = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "");
                                    if(phone == null || phone.equals("")){
                                        phone = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
                                    }
                                    if (loginState.equals("1")) {
                                        view.loadUrl("javascript:"+jsMethod+"("+mid+","+phone+")");
                                    }else{
                                        isCallJS = true;
                                        Intent intent = new Intent(ActiveActivity.this, MyloginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }else if (url.contains("mdtitle") && !url.contains("share")) {
                            int postion;
                            String title1;
                            postion = url.indexOf("mdtitle");
                            title1 = url.substring(postion);
                            String[] md = title1.split("=");
                            if (md != null) {
                                if (md.length > 1) {
                                    String dex = URLDecoder.decode(md[1], "UTF-8");
                                    if(dex != null && !dex.equals("")){
                                        setTitle(dex);
                                    }
                                }
                            }
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
                            String newurl;
                            if (url.contains("?")) {
                                newurl = url + "&mid=" + mid + "&tel=" + tel;
                            } else {
                                newurl = url + "?&mid=" + mid + "&tel=" + tel;
                            }
//                            progressBar.setVisibility(View.VISIBLE);

                            view.loadUrl(newurl);

                        } else if (url.contains("is-need-login")) {
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (loginState.equals("1")) {
//                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                                String newurl;
//                                String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");
//                                if (url.contains("?")) {
//                                    newurl = url + "&mid=" + mid + "&tel=" + tel;
//                                } else {
//                                    newurl = url + "?&mid=" + mid + "&tel=" + tel;
//                                }

                            if( url.contains("zhuanjiaOrder")){
                                int postion;
                                String title;
                                String id = "";
                                String typeid="";
                                postion = url.indexOf("id=");
                                title = url.substring(postion);
                                String[] pos = title.split("&");
                                if(pos != null){
                                    if(pos[0] != null){
                                        String idStr = pos[0];
                                        String[] temp = idStr.split("=");
                                        id =temp[1];
                                    }
                                    if(pos.length >1){
                                        if (pos[1] != null) {
                                            String tyepidStr = pos[1];
                                            String[] temp = tyepidStr.split("=");
                                            typeid =temp[1];
                                        }
                                    }
                                    Intent intent = new Intent(ActiveActivity.this, TianXuQiu.class);
                                    intent.putExtra("aid", id);
                                    intent.putExtra("typeid", typeid);
                                    startActivity(intent);
                                }
                            }


                            } else {
                                Intent intent = new Intent(ActiveActivity.this, MyloginActivity.class);
                                startActivity(intent);
                            }
                        } else if (url.contains("is-need-pop")) {
                            finish();
                        }else if( url.contains("zhuanjiaOrder")){
                            int postion;
                            String title;
                            String id = "";
                            String typeid="";
                            postion = url.indexOf("id=");
                            title = url.substring(postion);
                            String[] pos = title.split("&");
                            if(pos != null){
                                if(pos[0] != null){
                                    String idStr = pos[0];
                                    String[] temp = idStr.split("=");
                                    id =temp[1];
                                }
                                if(pos.length >1){
                                    if (pos[1] != null) {
                                        String tyepidStr = pos[1];
                                        String[] temp = tyepidStr.split("=");
                                        typeid =temp[1];
                                    }
                                }
                                Intent intent = new Intent(ActiveActivity.this, TianXuQiu.class);
                                intent.putExtra("aid", id);
                                intent.putExtra("typeid", typeid);
                                startActivity(intent);
                            }
                        }else {
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            String newurl;
                            String tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "0");

                            if (url.contains("?")) {
                                newurl = url + "&mid=" + mid + "&tel=" + tel;
                            } else {
                                newurl = url + "?&mid=" + mid + "&tel=" + tel;
                            }
//                            progressBar.setVisibility(View.VISIBLE);
                            view.loadUrl(newurl);
                        }
                    }else if(url.contains("share://")){
                        if (url.contains("share")) {
                            int postion;
                            String title;
                            postion = url.indexOf("mdtitle");
                            title = url.substring(postion);
                            String[] pos = title.split("&");
                            if (pos != null) {
                                if (pos.length == 1) {
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");

                                } else if (pos.length == 2) {
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");
                                    String mdimg = pos[1];
                                    String[] temp1 = mdimg.split("=");
                                    shareimg = temp1[1];
                                }else if(pos.length == 3){
                                    String mdtitle = pos[0];
                                    String[] temp = mdtitle.split("=");
                                    sharemdtitle = URLDecoder.decode(temp[1], "UTF-8");
                                    String mdimg = pos[1];
                                    String[] temp1 = mdimg.split("=");
                                    shareimg = temp1[1];
                                    String aid = pos[2];
                                    String[] temp2 = aid.split("=");
                                    aidtitle = temp2[1];
                                }

                            }
                            shareonclick();
                        }

                    }


                } catch (Exception e) {
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
               @Override
               public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                   final BTAlertDialog dialog = new BTAlertDialog(ActiveActivity.this);
//                   dialog.setTitle(message);
//                   dialog.setNegativeButton("取消", null);
//                   dialog.setPositiveButton("确定", new View.OnClickListener() {
//                       @Override
//                       public void onClick(View v) {
//                           DataCleanManager.cleanCache();
//                           dialog.dismiss();
//                       }
//                   });
//                   dialog.show();
//                    Log.d("lizisong", "message:"+message+","+result.toString()+","+url);
                    return false;
               }
        });

//        webview.setOnLoadFinishListener(new ProgressWebView.LoadFinishListener() {
//
//            @Override
//            public void OnLoadfinish() {
//                // TODO Auto-generated method stub
//                swipeLayout.setRefreshing(false);
//            }
//        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
//            webview.goBack();// 返回前一个页面
//            return true;
//        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("活动");
        MobclickAgent.onResume(this);
        if(isCallJS){
            isCallJS = false;
            Message msg = Message.obtain();
            msg.what = 0;
            handler.sendMessageDelayed(msg, 2000);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("活动");
        MobclickAgent.onPause(this);
    }

    private void shareonclick() {
        try {
            UIHelper.showDialogForLoading(ActiveActivity.this, "", true);
            if (shareimg == null || shareimg.equals("")) {
                image = new UMImage(ActiveActivity.this, "http://www.zhongkechuangxiang.com/uploads/logo/logo1.png");
            } else {
                image = new UMImage(ActiveActivity.this, "http://www.zhongkechuangxiang.com/plus/activity/images/" + shareimg + ".jpg");
            }

            final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                    {
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                    };
            action = new ShareAction(ActiveActivity.this);
            UIHelper.hideDialogForLoading();
//            WXMiniProgramObject miniProgramObj=;
//            WXMiniProgramObject miniProgramObj;
//            WXMiniProgramObject
            UMWeb web = new UMWeb("http://www.zhongkechuangxiang.com/plus/activity/hd/" + shareimg + ".html?"+"&teligId="+aidtitle);
            web.setTitle(sharemdtitle);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(sharemdtitle);//描述

            action.setDisplayList(displaylist).withSubject(sharemdtitle).withText(sharemdtitle).withMedia(web).setCallback(umShareListener).open();

//            new ShareAction(MainActivity.this).withText(sharemdtitle).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
//                    .setCallback(umShareListener).open();
//
//            action.setDisplayList(displaylist)
//                    .withText(sharemdtitle)
//                    .withTitle(sharemdtitle)
//
//                    .withTargetUrl("http://www.zhongkechuangxiang.com/plus/activity/hd/" + shareimg + ".html?"+"&teligId="+aidtitle)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .open();

        } catch (Exception e) {
        }
    }

    //
//    public class WebChromeClient extends android.webkit.WebChromeClient {
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            if (newProgress == 100) {
////                progressBar.setVisibility(View.GONE);
//            }
//        }
//    }
//
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
            } else {
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ActiveActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    void setCookie(String value) {
        String StringCookie = "mid=" + value + ";path=/";
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, StringCookie);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Active Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                webview.loadUrl("javascript:"+jsMethod+"("+mid+")");
            }
        }
    };


    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
