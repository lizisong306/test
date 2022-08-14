package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import db.ChannelDb;
import entity.PlaceChannel;
import view.StyleUtils;
import view.StyleUtils1;
import view.SystemBarTintManager;

/**
 * Created by lizisong on 2017/4/10.
 */

public class LocalCity extends AutoLayoutActivity  {
    ImageView information_back;
    TextView city;
    private GridView gridView;
    private LocalCityAdapter adapter;
    private DisplayImageOptions options;
    private boolean isClick =false;
    int current = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.localcity);
        //        //设置状态栏半透明的状态
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        MainActivity.MIUISetStatusBarLightMode(getWindow(),true);
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
        options = ImageLoaderUtils.initOptions();
        information_back = (ImageView) findViewById(R.id.information_back);
        information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalCity.this.finish();
            }
        });

        city = (TextView) findViewById(R.id.city);
        city.setText(MyApplication.currentCity);
        gridView = (GridView)findViewById(R.id.gradview);
        adapter = new LocalCityAdapter();
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isClick = true;
            adapter.notifyDataSetChanged();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            isClick = false;
            adapter.notifyDataSetChanged();
        }
        return super.onTouchEvent(event);
    }

    class LocalCityAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return MainActivity.columnButton.size();
        }

        @Override
        public Object getItem(int position) {
            return MainActivity.columnButton.get(position);
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
                convertView = View.inflate(LocalCity.this, R.layout.localcityitem, null);
                holdView.click_down = (ImageView) convertView.findViewById(R.id.click_down);
                holdView.click_up = (ImageView)convertView.findViewById(R.id.click_up);
                holdView.localitem = (RelativeLayout)convertView.findViewById(R.id.localitem);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }

            final PlaceChannel item = MainActivity.columnButton.get(position);
            String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"");
            if(city.equals(item.nativeplace)){
                ImageLoader.getInstance().displayImage(item.selectPicUrl
                        , holdView.click_down, options);
            }else{
                ImageLoader.getInstance().displayImage(item.picUrl
                        , holdView.click_down, options);
            }

           holdView.click_down.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"");
                   if(city.equals(item.nativeplace)){
                       Toast.makeText(LocalCity.this,"当前已是"+city+"频道",Toast.LENGTH_SHORT).show();
                       return;
                   }
                   if(item.isOpen.equals("1")){
                       Toast.makeText(LocalCity.this,"正在切换到"+item.nativeplace+"频道",Toast.LENGTH_SHORT).show();
                       ChannelDb.changeChannel(item.nativeplace);
                       Intent intent = new Intent();
                       intent.setAction("changetitles");
                       sendBroadcast(intent);
                       SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,item.nativeplace);
                       finish();

                   }else {
                       Toast.makeText(LocalCity.this,"频道正在开发,敬请期待",Toast.LENGTH_SHORT).show();
                   }
               }
           });
            return convertView;
        }
    class HoldView {
        public RelativeLayout localitem;
        public ImageView click_down;
        public ImageView click_up;
    }
}

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择城市");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择城市");
        MobclickAgent.onPause(this);
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
}
