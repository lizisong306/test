package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import entity.ZhuanLiShenData;
import entity.ZhuanLiShenEntiry;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.NetUtils;
import Util.SharedPreferencesUtil;
/**
 * Created by Administrator on 2018/6/29.
 */

public class ZhuanLiShenQing extends AutoLayoutActivity {
    ImageView back,share;
    RefreshListView listview;
    TextView bottmon_title;
    List<ZhuanLiShenData> dataShow = new ArrayList<>();
    ZhuanLiShen adapter ;
    ProgressBar progress;
    LinearLayout welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.BLACK);
        }
        setContentView(R.layout.zhuanlishenqing);
        //        //设置状态栏半透明的状态
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
//        setNavigationBarStatusBarTranslucent(this);


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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MIUISetStatusBarLightMode(getWindow(), true);
        back = (ImageView)findViewById(R.id.back);
        listview = (RefreshListView)findViewById(R.id.listview);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        progress = (ProgressBar)findViewById(R.id.progress);
        welcome = (LinearLayout)findViewById(R.id.welcome);
        share   = (ImageView)findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZhuanLiShenQing.this, TopicInformation.class);
                intent.putExtra("typeid","2");
                startActivity(intent);
            }
        });
        if (MainActivity.hasSoftKeys(getWindowManager())) {
            bottmon_title.setVisibility(View.VISIBLE);
            if(MyApplication.navigationbar >0){
                ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
                params.height=MyApplication.navigationbar;
                bottmon_title.setLayoutParams(params);
            }
        } else {
            bottmon_title.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(ZhuanLiShenQing.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            dataShow.clear();
            getJson();
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

    private void getJson(){
         String url = "http://www.zhongkechuangxiang.com/webapp/api.php";
         HashMap<String,String> map = new HashMap<>();
         map.put("c","patent");
         map.put("a","list");
         map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);

    }

    class ZhuanLiShen extends BaseAdapter{

        @Override
        public int getCount() {
            return dataShow.size();
        }

        @Override
        public Object getItem(int position) {
            return dataShow.get(position);
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
                convertView = View.inflate(ZhuanLiShenQing.this, R.layout.zhuanlishenqingadapter, null);
                holdView.senddata = (TextView)convertView.findViewById(R.id.senddata);
                holdView.xuqiutype = (TextView)convertView.findViewById(R.id.xuqiutype);
                holdView.zhuangtai = (TextView)convertView.findViewById(R.id.zhuangtai);
                convertView.setTag(holdView);
            }else{
              holdView = (HoldView)convertView.getTag();
            }
            ZhuanLiShenData item = dataShow.get(position);
            holdView.senddata.setText("发布时间:"+item.updatetime);
            holdView.xuqiutype.setText(item.status_name);
            holdView.zhuangtai.setText(item.title);

            return convertView;
        }
        class HoldView{
           public TextView senddata;
           public TextView xuqiutype;
           public TextView zhuangtai;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.setVisibility(View.GONE);
            if(msg.what == 1){
                try {
                    String ret ;
                    ret =(String) msg.obj;
                    Gson gson = new Gson();
                    ZhuanLiShenEntiry data = gson.fromJson(ret,ZhuanLiShenEntiry.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.result != null){
                                for (int i=0; i<data.result.size(); i++){
                                    ZhuanLiShenData item = data.result.get(i);
                                    dataShow.add(item);
                                }
                            }
                        }
                        if(data.result == null || (data.result != null && data.result.size() == 0)){
                            welcome.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }else {
                            listview.setVisibility(View.VISIBLE);
                            welcome.setVisibility(View.GONE);
                        }
                        adapter = new ZhuanLiShen();
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ZhuanLiShenData item = dataShow.get(position-1);
                                Intent intent = new Intent(ZhuanLiShenQing.this, SubjectDetailActivity.class );
                                intent.putExtra("id", item.id);
                                startActivity(intent);
                            }
                        });
                    }

                }catch (Exception e){

                }
            }
        }
    };
}
