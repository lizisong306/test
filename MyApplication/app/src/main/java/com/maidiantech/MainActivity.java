package com.maidiantech;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextPaint;
import Util.CloseBarUtil;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import  Util.WeatherUtils;
import android.widget.Toast;
import com.baidu.location.LocationClient;
import com.chenantao.autolayout.AutoCardView;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.maidiantech.Rotate2Animation.InterpolatedTimeListener;
import com.maidiantech.common.resquest.NetworkCom;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import Util.NavigationBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import application.MyApplication;
import entity.BaiduData;
import entity.ColumnData;
import entity.PlaceChannel;
import entity.UpGrade;
import entity.WeatherBaidu;
import entity.WeatherData;
import entity.Weather_Data;
import entity.XuQiu;
import fragment.CommonFragment;
import fragment.DFBaoTaoFragment;
import fragment.DFLinYiFragment;
import fragment.FirstFragment;
import fragment.My_page;
import fragment.NewKeJiKu;
import fragment.NewQuanGuoFragment;
import fragment.Recommend;
import fragment.WelcomePulse;
import fragment.kejiku;
import view.StyleUtils;
import view.SystemBarTintManager;
import view.UpgradeAlertDialog;
import Util.FileHelper;
import view.ViewClick;


public class MainActivity extends Myautolayout implements View.OnClickListener, InterpolatedTimeListener {
    public static FragmentManager manager;
    private RadioButton rb_shop;
    private RadioButton rb_keji;
    private RadioButton rbhome;
    private LinearLayout rbbaimai;
    private RelativeLayout re_baimai;
    private ImageView rbclassfont;
    private ImageView rbclassBack;
    private TextView bottmon_title;
    private ImageView rbClose;
    private TextView rbbaimaiTxt;
    private RadioButton rbclass1;
    private RadioButton rbserach;
    private RadioGroup rg;
    private AutoRelativeLayout layout;
    private LinearLayout rb_bg_class1;
    private static FirstFragment homeFragment;
    private static CommonFragment commonFragment;
    private static My_page mypage;
    private static WelcomePulse mypulse;
    Animation rotate;
    private Rotatable rotat;
    private boolean baimaiState = false;
    private boolean isAddBaimai = false;
    private TextView title = null;
    private String loginState;
    private String name, mtype, company;
    public static int current = 0;
    int netWorkType = 0;
    private String nickname;
    private static fragment.NewKeJiKu shop;
    private static Recommend kejiku;
    static DFLinYiFragment dfLinYiFragment;
    private static NewQuanGuoFragment quanGuoFragment;
//    private static DFBaoTaoFragment dfBaoTaoFragment;
    public static double inchi;
    TextView dian, dian1;
    public static int xuqiucount = 0;
    public static String feixinCode = "";
    private Vibrator mVibrator01 = null;
    String json;
    private LocationClient mLocClient;
    private String ips;
    RelativeLayout donghua;
    private Receiver receiver = new Receiver();
    public static int mFirstVisiableItem;

    public static List<PlaceChannel> columnButton = new ArrayList<>();
    public static  String wendu= "";
    public static  String Heightwendu= "";
    public static  String Lowwendu = "";
    public static String  tianqi= "";
    public ImageView tuijian;
    boolean natastate = false;
//    public static int height = 0;
    public  static  boolean ishideorshowbottombar = false;

    private final  static String UMENG_CHANNEL = "UMENG_CHANNEL";
    String umeng_channel="";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    LinearLayout bottmon;


    /**
     * 防止fragment显示隐藏所造成的界面重叠
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //Log.v("LH", "onSaveInstanceState"+outState);
        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
         int state = intent.getIntExtra("state",0);
         if(state == 3){
             showfirst();
             donghua.setVisibility(View.VISIBLE);
             tuijian.setVisibility(View.VISIBLE);
             AnimationDrawable animationDrawable = (AnimationDrawable)tuijian.getBackground();
             animationDrawable.start();
             handler1.sendEmptyMessageDelayed(2222, 4500);
         }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        columnButton.clear();
        current = 0;
        ICONJson();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
            //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            //如果有面板存在则关闭当前的面板
            finish();
        }
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_main);

        //设置状态栏半透明的状态
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            try {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                tintManager.setStatusBarAlpha(0);
            }catch (Exception e){

            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1

        hideStatusNavigationBar();//1
        MIUISetStatusBarLightMode(getWindow(), true);

        inchi = getYC();
        //控件
        initView();
        //fragment显示
        ishideorshowbottombar =isAllScreenDevice(getApplicationContext());
        if (ishideorshowbottombar) {
            bottmon_title.setVisibility(View.VISIBLE);
        } else {
            bottmon_title.setVisibility(View.GONE);
        }

        MyApplication.navigationbar = getDaoHangHeight(this);
        if(MyApplication.navigationbar >0){
            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
            params.height=MyApplication.navigationbar;
            bottmon_title.setLayoutParams(params);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bottmon_title.setVisibility(View.GONE);
        }
        manager = getSupportFragmentManager();
        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        FragmentTransaction transaction = manager.beginTransaction();

        quanGuoFragment = new NewQuanGuoFragment();
        transaction.add(R.id.homepage_fl, quanGuoFragment);

        homeFragment = new FirstFragment();
        transaction.add(R.id.homepage_fl, homeFragment);

        dfLinYiFragment = new DFLinYiFragment();
        transaction.add(R.id.homepage_fl, dfLinYiFragment);

        mypage = new My_page();
        transaction.add(R.id.homepage_fl, mypage);
        mypulse = new WelcomePulse();
        transaction.add(R.id.homepage_fl, mypulse);
//        dfBaoTaoFragment = new DFBaoTaoFragment();
//        transaction.add(R.id.homepage_fl, dfBaoTaoFragment);
        shop = new NewKeJiKu();
        transaction.add(R.id.homepage_fl, shop);
        kejiku = new Recommend();
        transaction.add(R.id.homepage_fl, kejiku);
        commonFragment = new CommonFragment();
        transaction.add(R.id.homepage_fl, commonFragment);
//        city = "包头";
        if(city.contains("全国")){
            transaction.show(quanGuoFragment);
            quanGuoFragment.setVisible(true);
            transaction.hide(homeFragment);
            homeFragment.setVisible(false);
            transaction.hide(commonFragment);
            commonFragment.setVisible(false);
            transaction.hide(dfLinYiFragment);
            dfLinYiFragment.setVisible(false);
        }else if(city.contains("包头") || city.contains("临沂")||city.contains("江阴")){
            transaction.show(dfLinYiFragment);
            dfLinYiFragment.setVisible(true);
            transaction.hide(quanGuoFragment);
            quanGuoFragment.setVisible(false);
            transaction.hide(commonFragment);
            commonFragment.setVisible(false);
            transaction.hide(homeFragment);
            homeFragment.setVisible(false);
        }else if(city.contains("潍坊") || city.contains("德州") ||
                city.contains("郑州") || city.contains("聊城") || city.contains("莱西") || city.contains("即墨")
                ){
            transaction.show(homeFragment);
            homeFragment.setVisible(true);
            transaction.hide(quanGuoFragment);
            quanGuoFragment.setVisible(false);
            transaction.hide(commonFragment);
            commonFragment.setVisible(false);
            transaction.hide(dfLinYiFragment);
            dfLinYiFragment.setVisible(false);

        } else{
            transaction.show(commonFragment);
            commonFragment.setVisible(true);
            transaction.hide(homeFragment);
            homeFragment.setVisible(false);
            transaction.hide(quanGuoFragment);
            quanGuoFragment.setVisible(false);
            transaction.hide(dfLinYiFragment);
            dfLinYiFragment.setVisible(false);
        }

        transaction.hide(mypulse);
        transaction.hide(mypage);
        transaction.hide(shop);
        transaction.hide(kejiku);
        transaction.commit();
        ips = MyApplication.ip;
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

        } else {
            upgradeVersion();
        }

        mLocClient = ((MyApplication) getApplication()).mLocationClient;

        mVibrator01 = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        ((MyApplication) getApplication()).mVibrator01 = mVibrator01;

        if (Build.VERSION.SDK_INT >= 23) {

            if (SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.RQUEST_PERMISSION, false) == false) {
                SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.RQUEST_PERMISSION, true);
                requestPermissions(new String[]{
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        mLocClient.start();
                        mLocClient.requestLocation();
                    } catch (Exception e) {

                    }
                }
            }).start();

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mLocClient.start();
                        mLocClient.requestLocation();

                    } catch (Exception e) {

                    }
                }
            }).start();
        }

        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("finish");
        oninfilter.addAction("showred");
        oninfilter.addAction("action_toxuqiu");
        registerReceiver(receiver, oninfilter);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public static void setStyle(Activity context){
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.transparent);
        tintManager.setStatusBarAlpha(0);
    }


    //控件
    private void initView() {
        // viewpager = (ViewPager) findViewById(R.id.viewpager);
        rb_bg_class1 = (LinearLayout)findViewById(R.id.rb_bg_class1);
        layout = (AutoRelativeLayout) findViewById(R.id.bg);
        rbhome = (RadioButton) findViewById(R.id.rb_home);
        rb_shop = (RadioButton) findViewById(R.id.rb_shop);
        rb_keji = (RadioButton) findViewById(R.id.rb_keji);
        rbbaimai = (LinearLayout) findViewById(R.id.rb_bg_class);
        bottmon_title = (TextView) findViewById(R.id.bottmon_title);
        bottmon = (LinearLayout)findViewById(R.id.bottmon);
        rbclassfont = (ImageView) findViewById(R.id.img_font);
        re_baimai = (RelativeLayout) findViewById(R.id.re_class);
        dian = (TextView) findViewById(R.id.dian);
        dian1 = (TextView) findViewById(R.id.dian1);
        tuijian = (ImageView)findViewById(R.id.tuijian);
        rbclassBack = (ImageView) findViewById(R.id.img_back);
        rbclassBack.setVisibility(View.INVISIBLE);

        rbbaimaiTxt = (TextView) findViewById(R.id.bamai_txt);
        rbserach = (RadioButton) findViewById(R.id.rb_serach);
        rg = (RadioGroup) findViewById(R.id.rg);
        donghua = (RelativeLayout)findViewById(R.id.donghua);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        title = (TextView) findViewById(R.id.title);

        donghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rbhome.setOnClickListener(this);
        rbbaimai.setOnClickListener(this);
        rbserach.setOnClickListener(this);
        rb_shop.setOnClickListener(this);
        rb_keji.setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    //底部监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                current = 0;
                WelcomePulse.mFirst = false;
                setBackLightGray();
                Drawable drawable = getResources().getDrawable(R.mipmap.tab_home_h);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                rbhome.setCompoundDrawables(null, drawable, null, null);
                rbhome.setTextColor(0xff3385ff);

                Drawable drawable1 = getResources().getDrawable(R.mipmap.keji_normal);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                rb_keji.setCompoundDrawables(null, drawable1, null, null);
                rb_keji.setTextColor(0xff1F1F1F);

                Drawable drawable2 = getResources().getDrawable(R.mipmap.tab_serve);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                rbserach.setCompoundDrawables(null, drawable2, null, null);
                rbserach.setTextColor(0xff1F1F1F);

                Drawable drawable3 = getResources().getDrawable(R.mipmap.shop_normal);
                drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                rb_shop.setCompoundDrawables(null, drawable3, null, null);
                rb_shop.setTextColor(0xff1F1F1F);


                if (baimaiState) {
                    baimaiState = false;
                    rbbaimaiTxt.setTextColor(0xff1F1F1F);
                    Rotate2Animation rotateAnim = null;
                    float cX = re_baimai.getWidth() / 2.0f;
                    float cY = re_baimai.getHeight() / 2.0f;
                    rotateAnim = new Rotate2Animation(360, 270,
                            cX, cY, 310.0f, true);
                    rotateAnim.setDuration(300);
                    rotateAnim.setInterpolatedTimeListener(this);
                    rotateAnim.setFillAfter(true);
                    rotateAnim.setInterpolator(new AccelerateInterpolator());
                    rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            rbclassBack.setVisibility(View.INVISIBLE);
                            rbclassfont.setVisibility(View.VISIBLE);
                            isAddBaimai = false;
                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                            float cX = re_baimai.getWidth() / 2.0f;
                            float cY = re_baimai.getHeight() / 2.0f;
                            Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                                    310.0f, false);
                            // 动画持续时间500毫秒
                            rotation.setDuration(300);
                            // 动画完成后保持完成的状态
                            rotation.setFillAfter(true);
                            rotation.setInterpolator(new AccelerateInterpolator());
                            re_baimai.startAnimation(rotation);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    re_baimai.startAnimation(rotateAnim);
                }

                FragmentTransaction transaction = manager.beginTransaction();
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
//                city="包头";
                if(city.contains("全国")){
                    transaction.show(quanGuoFragment);
                    quanGuoFragment.setVisible(true);
                    transaction.hide(homeFragment);
                    homeFragment.setVisible(false);
//                    transaction.hide(dfBaoTaoFragment);
                    transaction.hide(commonFragment);
                    commonFragment.setVisible(false);
                    transaction.hide(dfLinYiFragment);
                    dfLinYiFragment.setVisible(false);
                }else if(city.contains("包头") || city.contains("临沂")||city.contains("江阴")){
                    transaction.show(dfLinYiFragment);
                    dfLinYiFragment.setVisible(true);
//                    transaction.hide(dfBaoTaoFragment);
                    transaction.hide(homeFragment);
                    homeFragment.setVisible(false);
                    transaction.hide(commonFragment);
                    commonFragment.setVisible(false);
                    transaction.hide(quanGuoFragment);
                    quanGuoFragment.setVisible(false);
                }else if(city.contains("潍坊") || city.contains("德州") ||
                        city.contains("郑州") || city.contains("聊城") || city.contains("莱西") || city.contains("即墨")
                        ){
                    transaction.show(homeFragment);
                    homeFragment.setVisible(true);
                    transaction.hide(quanGuoFragment);
                    quanGuoFragment.setVisible(false);
                    transaction.hide(commonFragment);
                    commonFragment.setVisible(false);
                    transaction.hide(dfLinYiFragment);
                    dfLinYiFragment.setVisible(false);

                } else{
                    transaction.show(commonFragment);
                    commonFragment.setVisible(true);
                    transaction.hide(homeFragment);
                    homeFragment.setVisible(false);
                    transaction.hide(quanGuoFragment);
                    quanGuoFragment.setVisible(false);
//                    transaction.hide(dfBaoTaoFragment);
                    transaction.hide(dfLinYiFragment);
                    dfLinYiFragment.setVisible(false);
                }
                transaction.hide(mypulse);
                //  transaction.hide(industry);
                transaction.hide(mypage);
                transaction.hide(shop);
                transaction.hide(kejiku);
                //  transaction.hide(check);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.rb_keji:
                try{
                    String city1 = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"");
                    for(int i = 0; i< MainActivity.columnButton.size(); i++){
                        PlaceChannel item = MainActivity.columnButton.get(i);
                        if(item.nativeplace.contains(city1)){
                            Recommend.channelid = item.channelid;
                            break;
                        }
                    }
                }catch (Exception e){

                }
                current = 1;
                WelcomePulse.mFirst = false;
                setBackLightGray();
                Drawable drawable4 = getResources().getDrawable(R.mipmap.keji_check);
                drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
                rb_keji.setCompoundDrawables(null, drawable4, null, null);
                rb_keji.setTextColor(0xff3385ff);
                Drawable drawable5 = getResources().getDrawable(R.mipmap.tab_home);
                drawable5.setBounds(0, 0, drawable5.getMinimumWidth(), drawable5.getMinimumHeight());
                rbhome.setCompoundDrawables(null, drawable5, null, null);
                rbhome.setTextColor(0xff1F1F1F);

                Drawable drawable6 = getResources().getDrawable(R.mipmap.tab_serve);
                drawable6.setBounds(0, 0, drawable6.getMinimumWidth(), drawable6.getMinimumHeight());
                rbserach.setCompoundDrawables(null, drawable6, null, null);
                rbserach.setTextColor(0xff1F1F1F);

                Drawable drawable7 = getResources().getDrawable(R.mipmap.shop_normal);
                drawable7.setBounds(0, 0, drawable7.getMinimumWidth(), drawable7.getMinimumHeight());
                rb_shop.setCompoundDrawables(null, drawable7, null, null);
                rb_shop.setTextColor(0xff1F1F1F);
                if (baimaiState) {
                    baimaiState = false;
                    rbbaimaiTxt.setTextColor(0xff1F1F1F);
                    Rotate2Animation rotateAnim = null;
                    float cX = re_baimai.getWidth() / 2.0f;
                    float cY = re_baimai.getHeight() / 2.0f;
                    rotateAnim = new Rotate2Animation(360, 270,
                            cX, cY, 310.0f, true);
                    rotateAnim.setDuration(300);
                    rotateAnim.setInterpolatedTimeListener(this);
                    rotateAnim.setFillAfter(true);
                    rotateAnim.setInterpolator(new AccelerateInterpolator());
                    rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            rbclassBack.setVisibility(View.INVISIBLE);
                            rbclassfont.setVisibility(View.VISIBLE);
                            isAddBaimai = false;
                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                            float cX = re_baimai.getWidth() / 2.0f;
                            float cY = re_baimai.getHeight() / 2.0f;
                            Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                                    310.0f, false);
                            // 动画持续时间500毫秒
                            rotation.setDuration(300);
                            // 动画完成后保持完成的状态
                            rotation.setFillAfter(true);
                            rotation.setInterpolator(new AccelerateInterpolator());
                            re_baimai.startAnimation(rotation);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    re_baimai.startAnimation(rotateAnim);
                }

                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.show(kejiku);

                transaction1.hide(homeFragment);


                transaction1.hide(mypulse);
                //  transaction.hide(industry);
                transaction1.hide(mypage);
                transaction1.hide(shop);
                transaction1.hide(homeFragment);
                transaction1.hide(dfLinYiFragment);
                transaction1.hide(quanGuoFragment);
                transaction1.hide(commonFragment);
                homeFragment.setVisible(false);
                dfLinYiFragment.setVisible(false);
                quanGuoFragment.setVisible(false);
                commonFragment.setVisible(false);
                transaction1.commitAllowingStateLoss();
                kejiku.GunDongDing1();
                break;
            case R.id.rb_bg_class:
                current = 2;
                WelcomePulse.mFirst = true;
//                setBackGround();
                if (baimaiState) {
                    final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAddBaimai = true;
                            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (!loginState.equals("1")) {
                                Intent intent = new Intent(MainActivity.this, MyloginActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, PulseActivity.class);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    re_baimai.startAnimation(animation);

                    return;
                }

                Drawable drawable8 = getResources().getDrawable(R.mipmap.tab_serve);
                drawable8.setBounds(0, 0, drawable8.getMinimumWidth(), drawable8.getMinimumHeight());
                rbserach.setCompoundDrawables(null, drawable8, null, null);
                rbserach.setTextColor(0xff1F1F1F);

                Drawable drawablekeji = getResources().getDrawable(R.mipmap.keji_normal);
                drawablekeji.setBounds(0, 0, drawablekeji.getMinimumWidth(), drawablekeji.getMinimumHeight());
                rb_keji.setCompoundDrawables(null, drawablekeji, null, null);
                rb_keji.setTextColor(0xff1F1F1F);

                Drawable drawable9 = getResources().getDrawable(R.mipmap.tab_home);
                drawable9.setBounds(0, 0, drawable9.getMinimumWidth(), drawable9.getMinimumHeight());
                rbhome.setCompoundDrawables(null, drawable9, null, null);
                rbhome.setTextColor(0xff1F1F1F);

                Drawable drawable10 = getResources().getDrawable(R.mipmap.shop_normal);
                drawable10.setBounds(0, 0, drawable10.getMinimumWidth(), drawable10.getMinimumHeight());
                rb_shop.setCompoundDrawables(null, drawable10, null, null);
                rb_shop.setTextColor(0xff1F1F1F);

                rbbaimaiTxt.setTextColor(0xff3385ff);
                baimaiState = true;
                rbhome.setChecked(false);
                rbserach.setChecked(false);
                rb_shop.setChecked(false);
                rb_keji.setChecked(false);
                Rotate2Animation rotateAnim = null;
                float cX = re_baimai.getWidth() / 2.0f;
                float cY = re_baimai.getHeight() / 2.0f;
                rotateAnim = new Rotate2Animation(0, 90,
                        cX, cY, 310.0f, true);
                rotateAnim.setDuration(300);
                rotateAnim.setFillAfter(true);
                rotateAnim.setInterpolator(new AccelerateInterpolator());
                rotateAnim.setInterpolatedTimeListener(this);
                rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rbclassBack.setVisibility(View.VISIBLE);
                        rbclassfont.setVisibility(View.INVISIBLE);
                        Rotate2Animation rotateAnim = null;
                        float cX = re_baimai.getWidth() / 2.0f;
                        float cY = re_baimai.getHeight() / 2.0f;
                        rotateAnim = new Rotate2Animation(270, 360,
                                cX, cY, 310.0f, false);
                        rotateAnim.setDuration(300);
                        rotateAnim.setFillAfter(true);
                        rotateAnim.setInterpolator(new AccelerateInterpolator());
                        re_baimai.startAnimation(rotateAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                re_baimai.startAnimation(rotateAnim);
                FragmentTransaction transaction5 = manager.beginTransaction();
                transaction5.show(mypulse);
                transaction5.hide(homeFragment);
                transaction5.hide(commonFragment);
                transaction5.hide(mypage);
                transaction5.hide(shop);
                transaction5.hide(kejiku);
//                transaction5.hide(dfBaoTaoFragment);
                transaction5.hide(quanGuoFragment);
                transaction5.hide(dfLinYiFragment);
                homeFragment.setVisible(false);
                dfLinYiFragment.setVisible(false);
                quanGuoFragment.setVisible(false);
                commonFragment.setVisible(false);
                // transaction1.hide(check);
                transaction5.commitAllowingStateLoss();
                Intent intent = new Intent();
                intent.setAction(WelcomePulse.resultBroaderAction);
                intent.putExtra("action", "refresh");
                sendBroadcast(intent);

                break;
            case R.id.rb_shop:
                current = 3;
                WelcomePulse.mFirst = false;
                setBackLightGray();
                Drawable drawable11 = getResources().getDrawable(R.mipmap.shop_ckeck);
                drawable11.setBounds(0, 0, drawable11.getMinimumWidth(), drawable11.getMinimumHeight());
                rb_shop.setCompoundDrawables(null, drawable11, null, null);
                rb_shop.setTextColor(0xff3385ff);

                Drawable drawable12 = getResources().getDrawable(R.mipmap.keji_normal);
                drawable12.setBounds(0, 0, drawable12.getMinimumWidth(), drawable12.getMinimumHeight());
                rb_keji.setCompoundDrawables(null, drawable12, null, null);
                rb_keji.setTextColor(0xff1F1F1F);

                Drawable drawable13 = getResources().getDrawable(R.mipmap.tab_home);
                drawable13.setBounds(0, 0, drawable13.getMinimumWidth(), drawable13.getMinimumHeight());
                rbhome.setCompoundDrawables(null, drawable13, null, null);
                rbhome.setTextColor(0xff1F1F1F);

                Drawable drawable14 = getResources().getDrawable(R.mipmap.tab_serve);
                drawable14.setBounds(0, 0, drawable14.getMinimumWidth(), drawable14.getMinimumHeight());
                rbserach.setCompoundDrawables(null, drawable14, null, null);
                rbserach.setTextColor(0xff1F1F1F);

                if (baimaiState) {
                    baimaiState = false;

                    rbbaimaiTxt.setTextColor(0xff1F1F1F);
                    Rotate2Animation rotateAnim1 = null;
                    float cX1 = re_baimai.getWidth() / 2.0f;
                    float cY1 = re_baimai.getHeight() / 2.0f;
                    rotateAnim1 = new Rotate2Animation(360, 270,
                            cX1, cY1, 310.0f, true);
                    rotateAnim1.setDuration(300);
                    rotateAnim1.setInterpolatedTimeListener(this);
                    rotateAnim1.setFillAfter(true);
                    rotateAnim1.setInterpolator(new AccelerateInterpolator());
                    rotateAnim1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            isAddBaimai = false;
                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                            rbclassBack.setVisibility(View.INVISIBLE);
                            rbclassfont.setVisibility(View.VISIBLE);
                            float cX = re_baimai.getWidth() / 2.0f;
                            float cY = re_baimai.getHeight() / 2.0f;
                            Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                                    310.0f, false);
                            // 动画持续时间500毫秒
                            rotation.setDuration(300);
                            // 动画完成后保持完成的状态
                            rotation.setFillAfter(true);
                            rotation.setInterpolator(new AccelerateInterpolator());
                            re_baimai.startAnimation(rotation);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    re_baimai.startAnimation(rotateAnim1);
                }

                FragmentTransaction transaction3 = manager.beginTransaction();
                transaction3.show(shop);
                transaction3.hide(mypulse);
                transaction3.hide(homeFragment);
                transaction3.hide(mypage);
                transaction3.hide(dfLinYiFragment);
                 transaction3.hide(quanGuoFragment);
                transaction3.hide(commonFragment);
                homeFragment.setVisible(false);
                dfLinYiFragment.setVisible(false);
                quanGuoFragment.setVisible(false);
                commonFragment.setVisible(false);
                transaction3.hide(kejiku);
                transaction3.commitAllowingStateLoss();
                break;

            case R.id.rb_serach:
                current = 4;
                animation();
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.show(mypage);
                transaction2.hide(mypulse);
                transaction2.hide(homeFragment);
                transaction2.hide(quanGuoFragment);
                transaction2.hide(shop);
                transaction2.hide(kejiku);
                transaction2.hide(dfLinYiFragment);
                transaction2.hide(commonFragment);
                homeFragment.setVisible(false);
                dfLinYiFragment.setVisible(false);
                quanGuoFragment.setVisible(false);
                commonFragment.setVisible(false);
                transaction2.commitAllowingStateLoss();
                break;
        }
    }

    private void animation() {
        WelcomePulse.mFirst = false;
        setBackLightGray();
        Drawable drawable2 = getResources().getDrawable(R.mipmap.tab_serve_h);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        rbserach.setCompoundDrawables(null, drawable2, null, null);
        rbserach.setTextColor(0xff3385ff);

        Drawable drawablekeji = getResources().getDrawable(R.mipmap.keji_normal);
        drawablekeji.setBounds(0, 0, drawablekeji.getMinimumWidth(), drawablekeji.getMinimumHeight());
        rb_keji.setCompoundDrawables(null, drawablekeji, null, null);
        rb_keji.setTextColor(0xff1F1F1F);

        Drawable drawable3 = getResources().getDrawable(R.mipmap.tab_home);
        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
        rbhome.setCompoundDrawables(null, drawable3, null, null);
        rbhome.setTextColor(0xff1F1F1F);

        Drawable drawable4 = getResources().getDrawable(R.mipmap.shop_normal);
        drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
        rb_shop.setCompoundDrawables(null, drawable4, null, null);
        rb_shop.setTextColor(0xff1F1F1F);

        if (baimaiState) {
            baimaiState = false;

            rbbaimaiTxt.setTextColor(0xff1F1F1F);
            Rotate2Animation rotateAnim1 = null;
            float cX1 = re_baimai.getWidth() / 2.0f;
            float cY1 = re_baimai.getHeight() / 2.0f;
            rotateAnim1 = new Rotate2Animation(360, 270,
                    cX1, cY1, 310.0f, true);
            rotateAnim1.setDuration(300);
            rotateAnim1.setInterpolatedTimeListener(this);
            rotateAnim1.setFillAfter(true);
            rotateAnim1.setInterpolator(new AccelerateInterpolator());
            rotateAnim1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAddBaimai = false;
                    rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                    rbclassBack.setVisibility(View.INVISIBLE);
                    rbclassfont.setVisibility(View.VISIBLE);
                    float cX = re_baimai.getWidth() / 2.0f;
                    float cY = re_baimai.getHeight() / 2.0f;
                    Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                            310.0f, false);
                    // 动画持续时间500毫秒
                    rotation.setDuration(300);
                    // 动画完成后保持完成的状态
                    rotation.setFillAfter(true);
                    rotation.setInterpolator(new AccelerateInterpolator());
                    re_baimai.startAnimation(rotation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            re_baimai.startAnimation(rotateAnim1);
        }
    }

    public void hide() {
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypage);
        transaction2.hide(homeFragment);
        transaction2.hide(mypulse);
        transaction2.hide(kejiku);
        transaction2.commit();
    }

    public void check() {
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypage);
        transaction2.hide(homeFragment);
        transaction2.hide(mypulse);
        transaction2.commit();

    }

    public void hide1() {
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypulse);
        transaction2.hide(mypage);
        transaction2.show(homeFragment);
        transaction2.commit();
    }

    public void show() {
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypage);
        transaction2.show(homeFragment);
        transaction2.hide(mypulse);
        transaction2.commit();

    }

    public void show1() {
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypage);
        transaction2.hide(homeFragment);
        transaction2.hide(mypulse);
        transaction2.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void interpolatedTime(float interpolatedTime) {
    }

    public void setBackGround(String typeid, boolean state) {
        if (current == 1) {
            return;
        }
        if (typeid == null || (state && typeid.equals("-1"))) {
            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            name = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "");
            mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
            company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
            nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0");
            if (!loginState.equals("1")) {
                title.setVisibility(View.VISIBLE);
                title.setText("把脉");
            } else {
                title.setVisibility(View.VISIBLE);
                if (mtype.equals("个人")) {
                    title.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0") + "的私人订制");
                } else if (mtype.equals("企业")) {
                    title.setText(name + "的私人订制");
                }
            }

            StyleUtils.initSystemBar(this);
            //设置状态栏是否沉浸
            StyleUtils.setStyleTitle(this, R.mipmap.welcome_sb);
            layout.setBackgroundResource(R.mipmap.welcome_pluse);
        } else {
            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            name = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "");
            mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
            company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");

            if (!loginState.equals("1")) {
                title.setVisibility(View.VISIBLE);
                title.setText("私人订制");
            } else {
                title.setVisibility(View.VISIBLE);
                if (mtype.equals("个人")) {
                    title.setText(name + "的私人订制");
                } else if (mtype.equals("企业")) {
                    title.setText(company + "的私人订制");
                }
            }
            title.setVisibility(View.VISIBLE);
            if (typeid.equals("500")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_dianzixinxis);
                layout.setBackgroundResource(R.mipmap.bg_dianzixinxis);
            } else if (typeid.equals("1000")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_xincailiaos);
                layout.setBackgroundResource(R.mipmap.bg_xincailiaos);
            } else if (typeid.equals("3000")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_jienenghuanbaos);
                layout.setBackgroundResource(R.mipmap.bg_jienenghuanbaos);
            } else if (typeid.equals("2500")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_xinenengyuans);
                layout.setBackgroundResource(R.mipmap.bg_xinnengyuans);
            } else if (typeid.equals("2000")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_xianjinzhizaos);
                layout.setBackgroundResource(R.mipmap.bg_xianjinzhizaos);
            } else if (typeid.equals("1500")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_shengwujishus);
                layout.setBackgroundResource(R.mipmap.bg_shengwujishus);
            } else if (typeid.equals("4000")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_huaxuehuagongs);
                layout.setBackgroundResource(R.mipmap.bg_huaxuehuagongs);
            } else if (typeid.equals("3500")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_wenhuachuangyis);
                layout.setBackgroundResource(R.mipmap.bg_wenhuachuangyis);
            } else if (typeid.equals("4500")) {
                StyleUtils.initSystemBar(this);
                StyleUtils.setStyleTitle(this, R.mipmap.sb_qitas);
                layout.setBackgroundResource(R.mipmap.bg_qitas);
            }
        }

    }

    public void setBackLightGray() {
        title.setVisibility(View.GONE);
        StyleUtils.initSystemBar(this);
        StyleUtils.setStyle(this);
        layout.setBackgroundResource(R.color.white);
    }

    public void changeBaimai() {
        WelcomePulse.mFirst = true;
//                setBackGround();
        if (baimaiState) {
            final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(300);

            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_close);
                    isAddBaimai = true;
                    Intent intent = new Intent(MainActivity.this, PulseActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            re_baimai.startAnimation(animation);

            return;
        }

        Drawable drawable4 = getResources().getDrawable(R.mipmap.tab_serve);
        drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
        rbserach.setCompoundDrawables(null, drawable4, null, null);
        rbserach.setTextColor(0xff3e3e3e);


        Drawable drawable5 = getResources().getDrawable(R.mipmap.tab_home);
        drawable5.setBounds(0, 0, drawable5.getMinimumWidth(), drawable5.getMinimumHeight());
        rbhome.setCompoundDrawables(null, drawable5, null, null);
        rbhome.setTextColor(0xff3e3e3e);


        rbbaimaiTxt.setTextColor(0xff3385ff);
        baimaiState = true;
        rbhome.setChecked(false);
        rbserach.setChecked(false);
        Rotate2Animation rotateAnim = null;
        float cX = re_baimai.getWidth() / 2.0f;
        float cY = re_baimai.getHeight() / 2.0f;
        rotateAnim = new Rotate2Animation(0, 90,
                cX, cY, 310.0f, true);
        rotateAnim.setDuration(300);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new AccelerateInterpolator());
        rotateAnim.setInterpolatedTimeListener(this);
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rbclassBack.setVisibility(View.VISIBLE);
                rbclassfont.setVisibility(View.INVISIBLE);
                Rotate2Animation rotateAnim = null;
                float cX = re_baimai.getWidth() / 2.0f;
                float cY = re_baimai.getHeight() / 2.0f;
                rotateAnim = new Rotate2Animation(270, 360,
                        cX, cY, 310.0f, false);
                rotateAnim.setDuration(300);
                rotateAnim.setFillAfter(true);
                rotateAnim.setInterpolator(new AccelerateInterpolator());
                re_baimai.startAnimation(rotateAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        re_baimai.startAnimation(rotateAnim);
        FragmentTransaction transaction1 = manager.beginTransaction();
        transaction1.show(mypulse);
        transaction1.hide(quanGuoFragment);
        transaction1.hide(homeFragment);
        transaction1.hide(mypage);
        transaction1.commit();
        Intent intent = new Intent();
        intent.setAction(WelcomePulse.resultBroaderAction);
        intent.putExtra("action", "refresh");
        sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler1.removeMessages(1111);
        MobclickAgent.onPageEnd("主界面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
      //  NavigationBarShow();
       try {
            if(current == 0){
                FragmentTransaction transaction = manager.beginTransaction();
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
    //            city="包头";
                if(city.contains("全国")){
                    transaction.show(quanGuoFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(commonFragment);
    //                transaction.hide(dfBaoTaoFragment);
                    transaction.hide(dfLinYiFragment);
                    homeFragment.setVisible(false);
                    dfLinYiFragment.setVisible(false);
                    quanGuoFragment.setVisible(true);
                    commonFragment.setVisible(false);
                }else if(city.contains("包头")|| city.contains("临沂")||city.contains("江阴")){
                    transaction.show(dfLinYiFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(quanGuoFragment);
                    transaction.hide(commonFragment);
                    homeFragment.setVisible(false);
                    dfLinYiFragment.setVisible(true);
                    quanGuoFragment.setVisible(false);
                    commonFragment.setVisible(false);
    //                transaction.hide(dfBaoTaoFragment);
                }else if(city.contains("潍坊") || city.contains("德州") ||
                        city.contains("郑州") || city.contains("聊城") || city.contains("莱西") || city.contains("即墨")
                        ){
                    transaction.show(homeFragment);
                    transaction.hide(quanGuoFragment);
                    transaction.hide(commonFragment);
                    transaction.hide(dfLinYiFragment);
                    homeFragment.setVisible(true);
                    dfLinYiFragment.setVisible(false);
                    quanGuoFragment.setVisible(false);
                    commonFragment.setVisible(false);

                }else{
                    transaction.show(commonFragment);
                    transaction.hide(quanGuoFragment);
                    transaction.hide(homeFragment);
    //                transaction.hide(dfBaoTaoFragment);
                    transaction.hide(dfLinYiFragment);
                    homeFragment.setVisible(false);
                    dfLinYiFragment.setVisible(false);
                    quanGuoFragment.setVisible(false);
                    commonFragment.setVisible(true);
                }
                transaction.hide(mypulse);
                //  transaction.hide(industry);
                transaction.hide(mypage);
                transaction.hide(shop);
                transaction.hide(kejiku);
                //  transaction.hide(check);
                transaction.commit();
            }
    //        showTitle();
            netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
            } else {
    //            xuqiuUpdata();
            }
            Message obs = Message.obtain();
            obs.what = 1111;
            handler1.handleMessage(obs);
            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            name = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "");
            mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
            company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
            MobclickAgent.onPageStart("主界面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
            MobclickAgent.onResume(this);
            if (!loginState.equals("1")) {
                dian.setVisibility(View.GONE);
                dian1.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                title.setText("私人订制");
            } else {
                if (xuqiucount > 0) {
    //                dian.setVisibility(View.VISIBLE);
                    dian1.setVisibility(View.GONE);
                } else {
    //                dian.setVisibility(View.GONE);
                    dian1.setVisibility(View.GONE);
                }
                if (mtype.equals("个人")) {
                    title.setVisibility(View.VISIBLE);
                    title.setText(name + "的私人订制");
                } else if (mtype.equals("企业")) {
                    title.setVisibility(View.VISIBLE);
                    title.setText(company + "的私人订制");
                }

            }
       }catch (IllegalStateException e1){

       }catch (Exception e){

       }
    }

    String ret = "";
    String xuqiu = "";
    UpGrade grade;
    XuQiu xuqiuOBJ;

    /**
     * 检查升级
     */
    public void upgradeVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://" + ips + "/uploads/resource/android/" + MyApplication.UMENG_CHANNEL + "/updata.json";
                    ret = OkHttpUtils.loaudstringfromurl(url);
                    if (ret != null) {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handler1.sendMessage(msg);
                    }

                } catch (Exception e) {

                }

            }
        }).start();

    }

    /**
     * 需求接口调用
     */
    public  void xuqiuUpdata() {
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        if (loginState.equals("1")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<String> sorlist = new ArrayList<String>();

                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        String time = System.currentTimeMillis() + "";
                        sorlist.add("mid" + mid);
                        sorlist.add("timestamp" + time);
                        String sign = KeySort.keyScort(sorlist);
                        String url = "http://" + ips + "/api/requireNum.php?mid=" + mid + "&sign=" + sign + "&timestamp=" + time;
                        xuqiu = OkHttpUtils.loaudstringfromurl(url);
                        if (xuqiu != null) {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            handler1.sendMessage(msg);
                        }
                    } catch (Exception e) {

                    }

                }
            }).start();
        }
    }
   File filePath;

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                try {
                    json = (String)msg.obj;
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        String body = jsonObject.getString("data");
                        JSONObject json = new JSONObject(body);
                        JSONArray acolumnButton = json.getJSONArray("columnButton");
                        for (int i = 0; i < acolumnButton.length(); i++) {
                            PlaceChannel item = new PlaceChannel();
                            JSONObject obj = acolumnButton.getJSONObject(i);
                            item.channelid = obj.getString("channelid");
                            item.code = obj.getString("code");
                            item.englishName = obj.getString("englishName");
                            item.nativeplace = obj.getString("nativeplace");
                            item.isOpen = obj.getString("isOpen");
                            item.selectPicUrl = obj.getString("selectPicUrl");
                            MyApplication.cityId.add(item.nativeplace);
                            item.picUrl = obj.getString("picUrl");
                            JSONArray column = obj.getJSONArray("column");
                            item.column1 = new ArrayList<>();
                            item.column2 = new ArrayList<>();
                            for (int j = 0; j < column.length(); j++) {
                                JSONArray pos = column.getJSONArray(j);

                                for (int z = 0; z < pos.length(); z++) {
                                    JSONObject object = pos.getJSONObject(z);
                                    ColumnData data = new ColumnData();
                                    data.imgUrl = object.getString("imgUrl");
                                    data.name = object.getString("name");
                                    data.type = object.getString("type");
                                    try {
                                        data.jumpUrl = object.getString("jumpUrl");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.channelid = object.getString("channelid");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.flag = object.getString("flag");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.cityCode = object.getString("cityCode");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.cityName = object.getString("cityName");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.prvName = object.getString("prvName");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }
                                    try {
                                        data.prvCode = object.getString("prvCode");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.typeid = object.getString("typeid");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    try {
                                        data.tag = object.getString("tag");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }
                                    try {
                                        data.userName = object.getString("userName");
                                    }catch (JSONException a){

                                    }catch (Exception e){

                                    }

                                    try {
                                        data.path = object.getString("path");
                                    }catch (JSONException a){

                                    }catch (Exception e){

                                    }
                                    try {
                                        data.miniProgramType = object.getString("miniProgramType");
                                    }catch (JSONException a){

                                    }catch (Exception e){

                                    }

                                    try {
                                        data.title = object.getString("title");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    if (j == 0) {
                                        item.column1.add(data);
                                    } else if (j == 1) {
                                        item.column2.add(data);
                                    }

                                }
                            }
                            columnButton.add(item);

                        }
                        Intent intent = new Intent();
                        intent.setAction("downok");
                        sendBroadcast(intent);

                    }
                } catch (Exception e) {

                }

            }
            if (msg.what == 0) {
              try {
                  Gson gson = new Gson();
                  grade = gson.fromJson(ret, UpGrade.class);

                  if (grade.code.equals("1")) {
                      int oldVersion = MyApplication.versionCode;
                      int newVersion = grade.data.version_id;
                      String version_name = grade.data.version_name;
                      String txt = grade.data.update_note;

                      if (newVersion > oldVersion) {
                          if (!grade.data.is_required) {
                              final UpgradeAlertDialog dialog = new UpgradeAlertDialog(MainActivity.this);
                              dialog.setTitle("发现新版本,"+version_name+"来啦");
                              dialog.setContext(txt);
                              dialog.setNegativeButton("取消", null);
                              dialog.setPositiveButton("确定", new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
//                                    updateFromWeb(grade.data.down_link);
                                      openBrowserUpdate(grade.data.down_link);
                                      dialog.dismiss();
                                  }
                              });
                              dialog.show();
                          } else {
                              openBrowserUpdate(grade.data.down_link);
//                            updateFromWeb(grade.data.down_link);
                          }
                      }
                  }
              }catch (Exception e){

              }

            }

            if(msg.what == 500){
                Gson gson = new Gson();
                String ret = (String) msg.obj;
                WeatherBaidu data = gson.fromJson(ret, WeatherBaidu.class);
                if(data != null){
                    try {
                        if(data.error.equals("0")){
                            BaiduData item = data.results.get(0);
                            if(item != null){
                                Weather_Data weather =  item.weather_data.get(0);
                                tianqi = weather.weather;
                                String wendu1 = weather.date;
                                String temp  = weather.temperature;
                                String [] tem = wendu1.split("：");
                                wendu = tem[1].substring(0,2);
                                tem = temp.split("~");
                                Heightwendu = tem[0].replace(" ","");
                                Lowwendu  = tem[1].replace(" ","").substring(0,2);

                            }

                        }
                    }catch (Exception e){

                    }

                }
            }

            if (msg.what == 3) {
                try {
                    Gson gson = new Gson();
                    xuqiuOBJ = gson.fromJson(xuqiu, XuQiu.class);
                    if (xuqiuOBJ != null) {
                        xuqiucount = xuqiuOBJ.data.num;
                    }
                } catch (Exception e) {

                }
                if (xuqiucount > 0) {
                    dian.setVisibility(View.VISIBLE);
//                    dian1.setVisibility(View.GONE);
                } else {
                    dian.setVisibility(View.GONE);
//                    dian1.setVisibility(View.GONE);
                }

            }
            if (msg.what == 11) {
               try {
                    UIHelper.hideDialogForLoading();

                    File file = (File) msg.obj;//安装apk
                   filePath = file;


                   if(Build.VERSION.SDK_INT >= 26){
//                       installProcess(file);
                   }else{
                       if(file != null){
//                            FileHelper.d("lizisong", file.toString());
                            Intent intent = new Intent();
                            //执行动作
                            intent.setAction(Intent.ACTION_VIEW);
                            //执行的数据类型
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    }
                } catch (Exception e) {
//                   FileHelper.d("lizisong", e.toString());

                }


            }
            if (msg.what == 1111) {
                try {
                if (/*hasSoftKeys(getWindowManager())*/ isNavigationBarExist(MainActivity.this)) {
                    bottmon_title.setVisibility(View.VISIBLE);
                    Log.d("lizisong", "显示");
//                    homeFragment.setListBottomHeight(200);
                } else {
                    bottmon_title.setVisibility(View.GONE);
//                    homeFragment.setListBottomHeight(100);
                    Log.d("lizisong", "不显示");
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bottmon_title.setVisibility(View.GONE);
//                    homeFragment.setListBottomHeight(100);
                }
                Message msg1 = Message.obtain();
                msg1.what = 1111;
                handler1.sendMessageDelayed(msg1, 1000);
                }catch (Exception e){

                }
            }
            if(msg.what == 2222){
                donghua.setVisibility(View.GONE);
                tuijian.setVisibility(View.GONE);
                if(homeFragment.isVisible){
                    homeFragment.getjsons();
                }else if(dfLinYiFragment.isVisible){
                    dfLinYiFragment.getJoson();
                }else if(quanGuoFragment.isVisible){
                    quanGuoFragment.getJson();
                }else if(commonFragment.isVisible){
                    commonFragment.getJoson();
                }
            }

        }
    };
    private int getDpi()
    {  int dpi = 0;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm =new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try{
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, dm);
            dpi=dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }

    private void updateFromWeb(String url) {
        UIHelper.showDialogForLoading(this, "升级中，请等待...", true);
        final String urlPath = url;
        new Thread(new Runnable() {
            @Override
            public void run() {

                File file = getFileFromServer(urlPath);
                Message msg = Message.obtain();
                msg.what = 11;
                msg.obj = file;
                handler1.sendMessage(msg);
            }
        }).start();

    }

    private File getFileFromServer(String url) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                URL urlpath = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlpath.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                }
                fos.close();
                bis.close();
                is.close();
                return file;
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
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

    class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("finish")) {
                MainActivity.this.finish();
            } else if (intent.getAction().equals("showred")) {
                if (xuqiucount > 0) {
                    dian.setVisibility(View.VISIBLE);
//                    dian1.setVisibility(View.GONE);
                } else {
                    dian.setVisibility(View.GONE);
//                    dian1.setVisibility(View.GONE);
                }
            } else if (intent.getAction().equals("action_toxuqiu")) {
                current = 1;
                WelcomePulse.mFirst = false;
                setBackLightGray();
                Drawable drawable4 = getResources().getDrawable(R.mipmap.keji_check);
                drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
                rb_keji.setCompoundDrawables(null, drawable4, null, null);
                rb_keji.setTextColor(0xff3385ff);
                Drawable drawable5 = getResources().getDrawable(R.mipmap.tab_home);
                drawable5.setBounds(0, 0, drawable5.getMinimumWidth(), drawable5.getMinimumHeight());
                rbhome.setCompoundDrawables(null, drawable5, null, null);
                rbhome.setTextColor(0xff1F1F1F);

                Drawable drawable6 = getResources().getDrawable(R.mipmap.tab_serve);
                drawable6.setBounds(0, 0, drawable6.getMinimumWidth(), drawable6.getMinimumHeight());
                rbserach.setCompoundDrawables(null, drawable6, null, null);
                rbserach.setTextColor(0xff1F1F1F);

                Drawable drawable7 = getResources().getDrawable(R.mipmap.shop_normal);
                drawable7.setBounds(0, 0, drawable7.getMinimumWidth(), drawable7.getMinimumHeight());
                rb_shop.setCompoundDrawables(null, drawable7, null, null);
                rb_shop.setTextColor(0xff1F1F1F);


                if (baimaiState) {
                    baimaiState = false;
                    rbbaimaiTxt.setTextColor(0xff1F1F1F);
                    Rotate2Animation rotateAnim = null;
                    float cX = re_baimai.getWidth() / 2.0f;
                    float cY = re_baimai.getHeight() / 2.0f;
                    rotateAnim = new Rotate2Animation(360, 270,
                            cX, cY, 310.0f, true);
                    rotateAnim.setDuration(300);
                    rotateAnim.setInterpolatedTimeListener(MainActivity.this);
                    rotateAnim.setFillAfter(true);
                    rotateAnim.setInterpolator(new AccelerateInterpolator());
                    rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
//                            rbclassBack.setBackgroundResource();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            rbclassBack.setVisibility(View.INVISIBLE);
                            rbclassfont.setVisibility(View.VISIBLE);
                            isAddBaimai = false;
                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                            float cX = re_baimai.getWidth() / 2.0f;
                            float cY = re_baimai.getHeight() / 2.0f;
                            Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                                    310.0f, false);
                            // 动画持续时间500毫秒
                            rotation.setDuration(300);
                            // 动画完成后保持完成的状态
                            rotation.setFillAfter(true);
                            rotation.setInterpolator(new AccelerateInterpolator());
                            re_baimai.startAnimation(rotation);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    re_baimai.startAnimation(rotateAnim);
                }

                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.show(kejiku);
                transaction1.hide(homeFragment);
                transaction1.hide(dfLinYiFragment);
//                transaction1.hide(dfBaoTaoFragment);
                transaction1.hide(quanGuoFragment);
                transaction1.hide(mypulse);
                transaction1.hide(commonFragment);
                //  transaction.hide(industry);
                transaction1.hide(mypage);
                transaction1.hide(shop);
                //  transaction.hide(check);
                transaction1.commit();
            }

        }
    }


    public static boolean navigationBarExist(Context context) {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && context.getResources().getBoolean(id);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public double getYC() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        double diagonalPixels = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double screenSize = diagonalPixels / (160 * density);
//        Log.d("lizisong", "screenSize:" + screenSize);
        return screenSize;
    }


    /**
     * 设置屏幕顶部导航栏背景是否透明
     *
     * @param
     */
    public void setStatusBarTransparent(boolean transparent) {
        if (transparent) {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().addFlags(0x04000000);

                // 小米，魅族等手机的通知栏是全透明，白色背景通知栏显示不明显，特殊处理，加一个阴影背景
                Window window = getWindow();
                if (window != null) {
                    ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(AutoCardView.LayoutParams.FILL_PARENT, (int) 0);
                    params.gravity = Gravity.TOP;
                    View statusBarView = new View(this);
                    statusBarView.setLayoutParams(params);
                    statusBarView.setBackgroundResource(R.color.no_color);
//                    statusBarView.getBackground().setAlpha(255);
                    statusBarView.setId(R.id.bg);
                    statusBarView.setAlpha(0);
                    decorViewGroup.addView(statusBarView);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().clearFlags(0x04000000);

                Window window = getWindow();
                if (window != null) {
                    ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                    View statusBarView = decorViewGroup.findViewById(R.id.bg);
                    if (statusBarView != null) {
                        decorViewGroup.removeView(statusBarView);
                    }
                    statusBarView.getBackground().setAlpha(0);
//                    statusBarView.setAlpha(0);
                }
            }
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

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;//设置全屏
                attributes.flags |= flagTranslucentNavigation;//设置是否显示标题栏
                window.setAttributes(attributes);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * 导航栏，状态栏透明
     *
     * @param activity
     */
    public static void setNavigationBarStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        ActionBar actionBar = activity.getActionBar();
        actionBar.hide();
    }



    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public boolean isNavigationBarShow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }
    }


    /**
     * 判断底部navigator是否已经显示
     *
     * @return
     * @paramwindowManager
     */
    static Display d;
    static DisplayMetrics realDisplayMetrics;
    static DisplayMetrics displayMetrics;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        if(d == null)
           d = windowManager.getDefaultDisplay();
         if(realDisplayMetrics == null)
            realDisplayMetrics = new DisplayMetrics();

        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;

        int realWidth = realDisplayMetrics.widthPixels;
        if(displayMetrics == null)
            displayMetrics = new DisplayMetrics();

        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
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

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean hasNavBar(Context context) {

        Point realSize = new Point();

        Point screenSize = new Point();

        boolean hasNavBar = false;

        DisplayMetrics metrics = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        realSize.x = metrics.widthPixels;

        realSize.y = metrics.heightPixels;

        getWindowManager().getDefaultDisplay().getSize(screenSize);

        if (realSize.y != screenSize.y) {

            int difference = realSize.y - screenSize.y;

            int navBarHeight = 0;

            Resources resources = context.getResources();

            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

            if (resourceId > 0) {
                navBarHeight = resources.getDimensionPixelSize(resourceId);
            }

            if (navBarHeight != 0) {
                if (difference == navBarHeight) {
                    hasNavBar = true;
                }
            }
        }

        return hasNavBar;

    }

    public void ICONJson() {
        String url = "http://"+MyApplication.ip+"/api/indexChannel_2_5.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,null,handler1,10,0);
    }
    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar2(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }
    @TargetApi(14)
    public boolean judgeNavigation(){
        if(!ViewConfiguration.get(MainActivity.this).hasPermanentMenuKey()){
              return true;
    }else{
            return false;
        }
    }



    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case  TRIM_MEMORY_COMPLETE:
                System.gc();
                break;
        }
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }


    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, 10086);
    }


    //安装应用
    private void installApk(File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        FileHelper.d("lizisong", "installApk");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        } else {//Android7.0之后获取uri要用contentProvider
            Uri uri  = FileProvider.getUriForFile(this, "com.maidiantech.fileprovider", apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 10086) {
//            installProcess(filePath);//再次执行安装流程，包含权限判等
//        }
    }


    /**
     * 打开浏览器更新下载新版本apk
     * @param apkUrl    apk托管地址
     */
    private void openBrowserUpdate(String apkUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri apk_url = Uri.parse(apkUrl);
        intent.setData(apk_url);
        startActivity(intent);//打开浏览器
    }

    private void openNativie(){
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        _window.setAttributes(params);
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    boolean fullScreen = false;
    private void full(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            fullScreen = false;
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            fullScreen = true;
        }
    }

    public static void  changePindao(){
        if(current == 0){
        FragmentTransaction transaction = manager.beginTransaction();
        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
//                city="包头";
        if(city.contains("全国")){
            transaction.show(quanGuoFragment);
            transaction.hide(homeFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(dfLinYiFragment);
        }else if(city.contains("包头") || city.contains("临沂") || city.contains("江阴")){
            transaction.show(dfLinYiFragment);
            transaction.hide(quanGuoFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(homeFragment);
        }else{
            transaction.show(homeFragment);
            transaction.hide(quanGuoFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(dfLinYiFragment);
        }
        transaction.hide(mypulse);
        //  transaction.hide(industry);
        transaction.hide(mypage);
        transaction.hide(shop);
        transaction.hide(kejiku);
        //  transaction.hide(check);
        transaction.commit();
        }
    }

    public int getNavigationBarHeight() {

        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            Resources resources = getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            //获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }
        else{
            return 0;
        }
    }


    //是否有下方虚拟栏
    private static boolean isNavigationBarAvailable() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        return (!(hasBackKey && hasHomeKey));
    }

   public void  NavigationBarShow(){
           View decorView = getWindow().getDecorView();
           decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @param window  当前窗口
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
        boolean show;
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        return show;
    }


    /**
     * 测试需要的宽度
     * @param text
     * @param Size
     * @return
     */
     public static float GetTextWidth(String text, float Size) {
             TextPaint FontPaint = new TextPaint();
             FontPaint.setTextSize(Size);
             return FontPaint.measureText(text);
  }


/**
 * 新的测试方法
 */

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice(Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }

    /**
     * 判断全面屏是否启用虚拟键盘
     */

    private static final String NAVIGATION = "navigationBarBackground";

    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();

                if (vp.getChildAt(i).getId()!=-1&& NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showfirst(){
        current = 0;
        WelcomePulse.mFirst = false;
        setBackLightGray();
        Drawable drawable = getResources().getDrawable(R.mipmap.tab_home_h);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rbhome.setCompoundDrawables(null, drawable, null, null);
        rbhome.setTextColor(0xff3385ff);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.keji_normal);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        rb_keji.setCompoundDrawables(null, drawable1, null, null);
        rb_keji.setTextColor(0xff1F1F1F);

        Drawable drawable2 = getResources().getDrawable(R.mipmap.tab_serve);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        rbserach.setCompoundDrawables(null, drawable2, null, null);
        rbserach.setTextColor(0xff1F1F1F);

        Drawable drawable3 = getResources().getDrawable(R.mipmap.shop_normal);
        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
        rb_shop.setCompoundDrawables(null, drawable3, null, null);
        rb_shop.setTextColor(0xff1F1F1F);


        if (baimaiState) {
            baimaiState = false;
            rbbaimaiTxt.setTextColor(0xff1F1F1F);
            Rotate2Animation rotateAnim = null;
            float cX = re_baimai.getWidth() / 2.0f;
            float cY = re_baimai.getHeight() / 2.0f;
            rotateAnim = new Rotate2Animation(360, 270,
                    cX, cY, 310.0f, true);
            rotateAnim.setDuration(300);
            rotateAnim.setInterpolatedTimeListener(this);
            rotateAnim.setFillAfter(true);
            rotateAnim.setInterpolator(new AccelerateInterpolator());
            rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
//                            rbclassBack.setBackgroundResource();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    rbclassBack.setVisibility(View.INVISIBLE);
                    rbclassfont.setVisibility(View.VISIBLE);
                    isAddBaimai = false;
                    rbclassBack.setBackgroundResource(R.mipmap.tab_button_add);
                    float cX = re_baimai.getWidth() / 2.0f;
                    float cY = re_baimai.getHeight() / 2.0f;
                    Rotate2Animation rotation = new Rotate2Animation(90, 0, cX, cY,
                            310.0f, false);
                    // 动画持续时间500毫秒
                    rotation.setDuration(300);
                    // 动画完成后保持完成的状态
                    rotation.setFillAfter(true);
                    rotation.setInterpolator(new AccelerateInterpolator());
                    re_baimai.startAnimation(rotation);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            re_baimai.startAnimation(rotateAnim);
        }

        FragmentTransaction transaction = manager.beginTransaction();
        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
//                city="包头";
        if(city.contains("全国")){
            transaction.show(quanGuoFragment);
            transaction.hide(homeFragment);
//                    transaction.hide(dfBaoTaoFragment);
            transaction.hide(commonFragment);
            transaction.hide(dfLinYiFragment);
        }else if(city.contains("包头") || city.contains("临沂")||city.contains("江阴")){
            transaction.show(dfLinYiFragment);
//                    transaction.hide(dfBaoTaoFragment);
            transaction.hide(homeFragment);
            transaction.hide(commonFragment);
            transaction.hide(quanGuoFragment);
        }else if(city.contains("潍坊") || city.contains("德州") ||
                city.contains("郑州") || city.contains("聊城") || city.contains("莱西") || city.contains("即墨")
                ){
            transaction.show(homeFragment);
            transaction.hide(quanGuoFragment);
            transaction.hide(commonFragment);
            transaction.hide(dfLinYiFragment);

        } else{
            transaction.show(commonFragment);
            transaction.hide(homeFragment);
            transaction.hide(quanGuoFragment);
//                    transaction.hide(dfBaoTaoFragment);
            transaction.hide(dfLinYiFragment);
        }
        transaction.hide(mypulse);
        //  transaction.hide(industry);
        transaction.hide(mypage);
        transaction.hide(shop);
        transaction.hide(kejiku);
        //  transaction.hide(check);
        transaction.commitAllowingStateLoss();
    }

}
