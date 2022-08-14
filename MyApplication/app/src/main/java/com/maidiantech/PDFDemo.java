package com.maidiantech;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import Util.DownloadUtil;
import Util.BASE64Encoder;
import view.StyleUtils;
import view.StyleUtils1;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/7/10.
 */

public class PDFDemo extends AppCompatActivity {

    WebView mWebView;
    ImageView back;
    ProgressBar progressBar;
    String url ="",title="";
    TextView titlecontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfdemo);
//        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        MIUISetStatusBarLightMode(getWindow(), true);
        initView();
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        titlecontent.setText(title);
        //加载本地文件
//        preView("file:///android_asset/demo.pdf");
        //加载允许跨域访问的文件
//        download(url);
        preView(url);

        //跨域加载文件 先将pdf下载到本地在加载
//        download("http://p5grppofr.bkt.clouddn.com/pdf-js-demo.pdf");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView)findViewById(R.id.webView);
        back = (ImageView)findViewById(R.id.back);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        titlecontent = (TextView)findViewById(R.id.titlecontent);

        progressBar.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        //设置渲染效果优先级，高
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 下载pdf文件到本地
     *
     * @param url 文件url
     */
    private void download(String url) {
        DownloadUtil.download(url, getCacheDir() + "/temp.pdf",
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(final String path) {
                        progressBar.setVisibility(View.GONE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preView(path);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        progressBar.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onDownloadFailed(String msg) {

                    }
                });
    }

    /**
     * 预览pdf
     *
     * @param pdfUrl url或者本地文件路径
     */
    private void preView(String pdfUrl) {
        //1.只使用pdf.js渲染功能，自定义预览UI界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api >= 19

//            mWebView.loadUrl("file:///android_asset/index.html?" + pdfUrl);
            if (!TextUtils.isEmpty(pdfUrl)) {

                byte[] bytes = null;

                try {// 获取以字符编码为utf-8的字符

                    bytes = pdfUrl.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }

                if (bytes != null) {

                    pdfUrl = new BASE64Encoder().encode(bytes);// BASE64转码

                }

            }
            mWebView.loadUrl( "file:///android_asset/pdf/web/viewer.html?file=" + pdfUrl);

        } else {
            if (!TextUtils.isEmpty(pdfUrl)) {

                byte[] bytes = null;

                try {// 获取以字符编码为utf-8的字符

                    bytes = pdfUrl.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }

                if (bytes != null) {

                    pdfUrl = new BASE64Encoder().encode(bytes);// BASE64转码

                }

            }

            mWebView.loadUrl("file:///android_asset/pdf/web/viewer.html?file=" + pdfUrl);
//            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdfUrl);


        }
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

    public  boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
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

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {

//                Log.d("lizisong", "newProgress:"+newProgress);
//                progressBar.setVisibility(View.GONE);
//                mWebView.reload();

            }
        }
    }

    private void hideStatusNavigationBar() {
        if (Build.VERSION.SDK_INT < 16) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

}
