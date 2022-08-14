package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import application.ImageLoaderUtils;
import application.MyApplication;
import entity.NewXuQiuData;
import entity.NewXuQiuEntity;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;
import view.SystemBarTintManager;
import view.ZQImageViewRoundOval;

/**
 * Created by Administrator on 2018/5/3.
 */

public class NewXuQiu extends AutoLayoutActivity {
    ImageView shezhi_backs;
    TextView titledes;
    RefreshListView listview;
    ProgressBar progress;
    NewXuQiuEntity quxuqiu;
    List<NewXuQiuData> showList = new ArrayList<>();
    private DisplayImageOptions options;
    int with1,with2;

    Adapter adapter = new Adapter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
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
        with1 = (MyApplication.widths)*136/655;
        with2 = (MyApplication.widths)*136/655;

        setContentView(R.layout.newxuqiu);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);

        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titledes = (TextView)findViewById(R.id.titledes);
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);

        progress.setVisibility(View.VISIBLE);
        getjson();

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

    private void getjson(){
        String url ="http://"+ MyApplication.ip+"/api/require_new.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method","imglist");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,0,0);
    }
   Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(msg.what == 0){
               try {
                   progress.setVisibility(View.GONE);
                   String json = (String)msg.obj;
                   Gson gson = new Gson();
                   quxuqiu =  gson.fromJson(json, NewXuQiuEntity.class);
                   if(quxuqiu != null){
                       if(quxuqiu.code.equals("1")){
                           showList = quxuqiu.data;
                           listview.setAdapter(adapter);
                       }
                   }
               }catch (Exception e){

               }


           }

       }
   };
    class Adapter extends BaseAdapter{

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
            HoldView holdView = null;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(NewXuQiu.this, R.layout.newxuqiuadapter, null);
                holdView.bg = (ZQImageViewRoundOval) convertView.findViewById(R.id.bg);
//                holdView.bg_shape = (ImageView)convertView.findViewById(R.id.bg_shape);
//                Paint paint = new Paint();
//                paint.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
//                holdView.bg.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
                ViewGroup.LayoutParams params  =holdView.bg.getLayoutParams();
                params.height = with2-20;
                holdView.bg.setLayoutParams(params);

//                ViewGroup.LayoutParams params2 =holdView.bg_shape.getLayoutParams();
//                params2.height = with1;
//                holdView.bg_shape.setLayoutParams(params2);
                holdView.texttitle = (TextView)convertView.findViewById(R.id.texttitle);
                holdView.textdes = (TextView)convertView.findViewById(R.id.textdes);
                holdView.lay = (AutoRelativeLayout)convertView.findViewById(R.id.lay);
                convertView.setTag(holdView);

            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final NewXuQiuData item = showList.get(position);
            holdView.texttitle.setText(item.title);
            holdView.textdes.setText(item.description);
            holdView.bg.setType(ZQImageViewRoundOval.TYPE_ROUND);
            holdView.bg.setRoundRadius(15);
            ImageLoader.getInstance().displayImage(item.img
                    , holdView.bg, options);
            holdView.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.typeid.equals("0")){
                        Intent intent = new Intent(NewXuQiu.this, TianXuQiu.class);
                        intent.putExtra("typeid", "0");
                        startActivity(intent);
                    }else if(item.typeid.equals("2")){
                        Intent intent = new Intent(NewXuQiu.this, AddProject.class);
                        startActivity(intent);
                    }else if(item.typeid.equals("4")){
                        Intent intent = new Intent(NewXuQiu.this, AddRencai.class);
                        startActivity(intent);
                    }else if(item.typeid.equals("7")){
                        Intent intent = new Intent(NewXuQiu.this, AddSheBei.class);
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }
        class HoldView{
            public ZQImageViewRoundOval bg;
            public ImageView bg_shape;
            public AutoRelativeLayout lay;
            public TextView texttitle;
            public TextView textdes;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新需求选择");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新需求选择");
        MobclickAgent.onPause(this);
    }
}
