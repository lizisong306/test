package com.maidiantech;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.LocationClient;
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

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import entity.BaiduData;
import entity.ColumnData;
import entity.PlaceChannel;
import entity.UpGrade;
import entity.WeatherBaidu;
import entity.Weather_Data;
import entity.XuQiu;
import fragment.DFLinYiFragment;
import fragment.FirstFragment;
import fragment.My_page;
import fragment.NewQuanGuoFragment;
import fragment.Recommend;
import fragment.WelcomePulse;
import fragment.kejiku;
import view.StyleUtils;
import Util.OkHttpUtils;
import Util.NetUtils;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import view.SystemBarTintManager;
import view.UpgradeAlertDialog;

/**
 * Created by Administrator on 2018/10/22.
 */

public class ActivityUITest extends  Myautolayout implements View.OnClickListener, InterpolatedTimeListener {
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
    private static fragment.kejiku shop;
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
    private Receiver receiver = new Receiver();
    public static int mFirstVisiableItem;

    public static List<PlaceChannel> columnButton = new ArrayList<>();
    public static  String wendu= "";
    public static  String Heightwendu= "";
    public static  String Lowwendu = "";
    public static String  tianqi= "";
    boolean natastate = false;
//    public static int height = 0;

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

//    private void getScreenSizeOfDevice2() {
//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getRealSize(point);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        double x = Math.pow(point.x/ dm.xdpi, 2);
//        double y = Math.pow(point.y / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.d("lizisong", "Screen inches : " + screenInches);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("lizisong", keyCode + "");
        return super.onKeyDown(keyCode, event);
    }
//    private float mFirstY;
//    private float mCurrentY;
//    private boolean mIsShow = true;

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//               switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mFirstY = event.getY();
//                        break;
//                   case MotionEvent.ACTION_MOVE:
//                       //称动时的y坐标
//                       mCurrentY = event.getY();
//                       if (mFirstY - mCurrentY > 50) { //手式向上滑动
//                           if( mFirstVisiableItem > 0){ //当head不可见时，才可以隐藏头部，要不会有一段空白
//                               kejiku.showAnimatorfor();
////                               mIsShow = false;
////                        toolbarAnimal(TAB_HIDE); //隐藏
////                        layout_top.setVisibility(View.GONE);
//
//                           }
//                       } else if (mCurrentY - mFirstY > 50) { //手式向下滑动
//                           if(mFirstVisiableItem == 0){//当head可见时，强制显示头部，要不会有一段空白
//                               kejiku.hideAnimatorfor();
////                               if(isaddFrist){
////                                   isaddFrist = false;
////                                   lv_list.addHeaderView(head);
////                                   head.setVisibility(View.VISIBLE);
////                                   toolbarAnimal(TAB_SHOW); //显示
////                               }
//////                        layout_top.setVisibility(View.VISIBLE);
////                               mIsShow = true;
//
//                           }
//                       }
//                       break;
//                   case MotionEvent.ACTION_UP:
//                       break;
//                }
//
//        return false;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        changeStatusBarTextColor(true);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        fullScreen(ActivityUITest.this);


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
//        setStatusBarColor(ActivityUITest.this, R.color.Lightgray);
        setStyle(ActivityUITest.this);
        inchi = getYC();
        //控件
        initView();
        bottmon_title.setVisibility(View.GONE);
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
        shop = new kejiku();
        transaction.add(R.id.homepage_fl, shop);
        kejiku = new Recommend();
        transaction.add(R.id.homepage_fl, kejiku);
//        city = "包头";
        if(city.contains("全国")){
            transaction.show(quanGuoFragment);
            transaction.hide(homeFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(dfLinYiFragment);
//            transaction.hide(homeFragment);
        }/*else if(city.contains("包头")){
//            transaction.show(dfBaoTaoFragment);
            transaction.hide(quanGuoFragment);
            transaction.hide(homeFragment);*
            transaction.hide(dfLinYiFragment);
        }*/else if(city.contains("包头") || city.contains("临沂")||city.contains("江阴")){
            transaction.show(dfLinYiFragment);
            transaction.hide(quanGuoFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(homeFragment);
        }else{
            transaction.show(homeFragment);
            transaction.hide(quanGuoFragment);
//            transaction.hide(dfBaoTaoFragment);
            transaction.hide(dfLinYiFragment);
//            transaction.hide(quanGuoFragment);
        }

        transaction.hide(mypulse);
        transaction.hide(mypage);
        transaction.hide(shop);
        transaction.hide(kejiku);
//        transaction.hide(quanGuoFragment);
        //transaction.hide(industry);
        // transaction.hide(check);
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
//            if(this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
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
//            }
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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
        rbclassBack = (ImageView) findViewById(R.id.img_back);
        rbclassBack.setVisibility(View.INVISIBLE);

        rbbaimaiTxt = (TextView) findViewById(R.id.bamai_txt);
        rbserach = (RadioButton) findViewById(R.id.rb_serach);
        rg = (RadioGroup) findViewById(R.id.rg);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lizisong", "onclick 底部");
            }
        });
        title = (TextView) findViewById(R.id.title);
//        bottmon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
//                OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                bottmon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                height = bottmon.getHeight();
//            }
//        });

        rbhome.setOnClickListener(this);
        rbbaimai.setOnClickListener(this);
        rbserach.setOnClickListener(this);
        rb_shop.setOnClickListener(this);
        rb_keji.setOnClickListener(this);
//        rb_bg_class1.setOnClickListener(this);

    }

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
//            CMLog.show("高度："+resourceId);
//            CMLog.show("高度："+context.getResources().getDimensionPixelSize(resourceId) +"");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
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

    public  void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }

    public void ICONJson() {
        String url = "http://"+ MyApplication.ip+"/api/indexChannel_2_5.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,null,handler1,10,0);
    }

    public static void setStyle(Activity context){
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.transparent);
        tintManager.setStatusBarAlpha(0);
    }


    private void changeStatusBarTextColor(boolean isBlack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            }else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
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
                    transaction.hide(dfLinYiFragment);
                }/*else if(city.contains("包头")){
//                    transaction.show(dfBaoTaoFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(quanGuoFragment);
                    transaction.hide(dfLinYiFragment);

                }*/else if(city.contains("包头") || city.contains("临沂")||city.contains("江阴")){
                    transaction.show(dfLinYiFragment);
//                    transaction.hide(dfBaoTaoFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(quanGuoFragment);
                }else{
                    transaction.show(homeFragment);
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
                transaction.commit();
                break;
            case R.id.rb_keji:
//                String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                if(LoginState.equals("0")){
//                    Intent intent = new Intent(MainActivity.this, MyloginActivity.class);
//                    startActivity(intent);
//                    Drawable drawable4= getResources().getDrawable(R.mipmap.keji_normal);
//                    drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
//                    rb_keji.setCompoundDrawables(null, drawable4, null, null);
//                    rb_keji.setTextColor(0xff1F1F1F);
//                    return;
//                }
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


                transaction1.hide(mypulse);
                //  transaction.hide(industry);
                transaction1.hide(mypage);
                transaction1.hide(shop);
                transaction1.hide(dfLinYiFragment);
                transaction1.hide(quanGuoFragment);
//                transaction1.hide(dfBaoTaoFragment);


                //  transaction.hide(check);
                transaction1.commit();
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
//                            rbclassBack.setBackgroundResource(R.mipmap.tab_button_close);
                            isAddBaimai = true;
                            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (!loginState.equals("1")) {
                                Intent intent = new Intent(ActivityUITest.this, MyloginActivity.class);
                                //getActivity().startActivity(intent);
                                //m.animations();
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ActivityUITest.this, PulseActivity.class);
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

//                rotat.rotate(Rotatable.ROTATE_Y, 180, 1500);

                FragmentTransaction transaction5 = manager.beginTransaction();
                // preferences = getSharedPreferences("name", MODE_PRIVATE);
                // FragmentTransaction transaction1 = manager.beginTransaction();
                //  preferences = getSharedPreferences("name", MODE_PRIVATE);
                //   if(preferences.getBoolean("hanye",false)){
                transaction5.show(mypulse);

                //  transaction1.hide(industry);
                //  }else{
                //    transaction1.show(industry);
                //     transaction1.hide(mypulse);
                //    }

                transaction5.hide(homeFragment);


                transaction5.hide(mypage);
                transaction5.hide(shop);
                transaction5.hide(kejiku);
//                transaction5.hide(dfBaoTaoFragment);
                transaction5.hide(quanGuoFragment);
                transaction5.hide(dfLinYiFragment);
                // transaction1.hide(check);
                transaction5.commit();
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
                //* transaction2.add(R.id.homepage_fl,mypage);*//*
                transaction3.show(shop);
                transaction3.hide(mypulse);

                transaction3.hide(homeFragment);

                transaction3.hide(mypage);
                transaction3.hide(dfLinYiFragment);
                transaction3.hide(quanGuoFragment);
//                transaction3.hide(dfBaoTaoFragment);
                transaction3.hide(kejiku);
                // transaction2.hide(industry);
                // transaction2.hide(check);
                transaction3.commit();
                break;

            case R.id.rb_serach:
                current = 4;
                animation();

                FragmentTransaction transaction2 = manager.beginTransaction();
                //* transaction2.add(R.id.homepage_fl,mypage);*//*
                transaction2.show(mypage);
                transaction2.hide(mypulse);
                transaction2.hide(homeFragment);
                transaction2.hide(quanGuoFragment);
//                transaction2.hide(dfBaoTaoFragment);
                transaction2.hide(shop);
                transaction2.hide(kejiku);
                transaction2.hide(dfLinYiFragment);
                // transaction2.hide(industry);
                // transaction2.hide(check);
                transaction2.commit();
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
                    //                transaction.hide(dfBaoTaoFragment);
                    transaction.hide(dfLinYiFragment);
                }/*else if(city.contains("包头")){
    //                transaction.show(dfBaoTaoFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(quanGuoFragment);
                    transaction.hide(dfLinYiFragment);

                }*/else if(city.contains("包头")|| city.contains("临沂")||city.contains("江阴")){
                    transaction.show(dfLinYiFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(quanGuoFragment);
                    //                transaction.hide(dfBaoTaoFragment);
                }else{
                    transaction.show(homeFragment);
                    transaction.hide(quanGuoFragment);
                    //                transaction.hide(dfBaoTaoFragment);
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
            //        showTitle();
            netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                Toast.makeText(ActivityUITest.this, "网络不给力", Toast.LENGTH_SHORT).show();
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
//        String isShownav = Settings.System.getString(getContentResolver(), "switch_navigation_bar");
//        Log.d("lizisong", "isShownav:"+isShownav);


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
        // preferences.edit().putString("frament","").commit();
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.hide(mypage);
        //transaction2.show(industry);
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
            }/*else if(city.contains("包头")){
//            transaction.show(dfBaoTaoFragment);
            transaction.hide(homeFragment);
            transaction.hide(quanGuoFragment);
            transaction.hide(dfLinYiFragment);

        }*/else if(city.contains("包头") || city.contains("临沂") || city.contains("江阴")){
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


    class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("finish")) {
                ActivityUITest.this.finish();
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
                    rotateAnim.setInterpolatedTimeListener(ActivityUITest.this);
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
                //  transaction.hide(industry);
                transaction1.hide(mypage);
                transaction1.hide(shop);
                //  transaction.hide(check);
                transaction1.commit();
            }

        }
    }

    public void setBackLightGray() {
        title.setVisibility(View.GONE);
//        StyleUtils.initSystemBar(this);
//        StyleUtils.setStyle(this);
        layout.setBackgroundResource(R.color.white);
    }
    File filePath;
    String ret = "";
    String xuqiu = "";
    UpGrade grade;
    XuQiu xuqiuOBJ;

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
                                final UpgradeAlertDialog dialog = new UpgradeAlertDialog(ActivityUITest.this);
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
                            ActivityUITest.this.finish();
                        }
                    }
                } catch (Exception e) {
//                   FileHelper.d("lizisong", e.toString());

                }


            }
            if (msg.what == 1111) {
                try {
                    if (hasSoftKeys(getWindowManager()) /*&& isNavigationBarShow()*/) {
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

        }
    };

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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            d.getRealMetrics(realDisplayMetrics);
//        }


        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;

        int realWidth = realDisplayMetrics.widthPixels;
        if(displayMetrics == null)
            displayMetrics = new DisplayMetrics();

        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;

        int displayWidth = displayMetrics.widthPixels;
//        Log.d("lizisong","realWidth:"+realWidth+","+"realHeight:"+realHeight+","+"displayWidth:"+displayWidth+","+"displayHeight:"+displayHeight);

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;

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
    class myserver extends  Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}
