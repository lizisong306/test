package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoLinearLayout;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.maidiantech.common.ui.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import entity.ChengZhangHuoBanBasic;
import entity.ChengZhangHuoBanDeilEntity;
import entity.ChengZhangHuoBanDeilEntityData;
import entity.ChengZhangHuoBanShow;
import entity.ColumnData;
import entity.FrameData;
import fragment.FirstFragment;
import view.HorizontalListView;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.SharedPreferencesUtil;
import Util.NetUtils;
/**
 * Created by lizisong on 2018/4/16.
 */

public class ChengZhangHuoBanDeil extends AutoLayoutActivity {
    RefreshListView listview;
    TextView bottmon_title;
    ProgressBar progress;
    View heartView = null;
    ImageView unit_back;
    TextView phone, company,lingyu1,lingyu2,lingyu3,lingyu4,lingyu5,companyadress;
    List<ChengZhangHuoBanShow> showList = new ArrayList<>();
    private DisplayImageOptions options;
    DeilAdapter deilAdapter;

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
        setContentView(R.layout.chengzhanghuobandeil);
        options = ImageLoaderUtils.initOptions();
        //        //设置状态栏半透明的状态
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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MIUISetStatusBarLightMode(getWindow(), true);
        listview = (RefreshListView)findViewById(R.id.listview);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        progress = (ProgressBar)findViewById(R.id.progress);

        if(MyApplication.navigationbar >0){
            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
            params.height=MyApplication.navigationbar;
            bottmon_title.setLayoutParams(params);
        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(ChengZhangHuoBanDeil.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else{
            progress.setVisibility(View.VISIBLE);
            getJson();
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

    /**
     * 获取数据
     */
    private void getJson(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        HashMap<String, String> map = new HashMap<>();
        String url="http://"+MyApplication.ip+"/api/user_growth_partner.php";
        map.put("method", "detail");
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,0,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                progress.setVisibility(View.GONE);
                String ret = (String)msg.obj;
                Gson gson = new Gson();
              final   ChengZhangHuoBanDeilEntity deilData = gson.fromJson(ret, ChengZhangHuoBanDeilEntity.class);
                if(deilData != null){
                    if(deilData.code.equals("1")){
//                        deilData.data.
                        heartView = View.inflate(ChengZhangHuoBanDeil.this, R.layout.chengzhanghuobandeilheart,null);
                        unit_back = (ImageView)heartView.findViewById(R.id.unit_back);
                        phone = (TextView) heartView.findViewById(R.id.phone);
                        companyadress = (TextView)  heartView.findViewById(R.id.companyadress);
                        company = (TextView)heartView.findViewById(R.id.company);
                        lingyu1 = (TextView)heartView.findViewById(R.id.lingyu1);
                        lingyu2 = (TextView)heartView.findViewById(R.id.lingyu2);
                        lingyu3 = (TextView)heartView.findViewById(R.id.lingyu3);
                        lingyu4 = (TextView)heartView.findViewById(R.id.lingyu4);
                        lingyu5 = (TextView)heartView.findViewById(R.id.lingyu5);

                        unit_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
//                        phone.setText(deilData.data.agent);
                        phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + deilData.data.agent_tel);
                                    intent.setData(data);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        });
                        company.setText(deilData.data.company);

                        if(deilData.data.keyword != null){
                            for (int i=0; i<deilData.data.keyword.size();i++){
                                String key = deilData.data.keyword.get(i);
                                if(i == 0){
                                    lingyu1.setVisibility(View.VISIBLE);
                                    lingyu1.setText(key);
                                }else if(i == 1){
                                    lingyu2.setVisibility(View.VISIBLE);
                                    lingyu2.setText(key);
                                }else if(i == 2){
                                    lingyu3.setVisibility(View.VISIBLE);
                                    lingyu3.setText(key);
                                }else if(i == 3){
                                    lingyu4.setVisibility(View.VISIBLE);
                                    lingyu4.setText(key);
                                }
                                else if(i == 4){
                                    lingyu5.setVisibility(View.VISIBLE);
                                    lingyu5.setText(key);
                                }
                            }
                        }
                        companyadress.setText("企业所在地： " + deilData.data.nativeplace_1+"  "+deilData.data.nativeplace_2);

                        listview.addHeaderView(heartView);

                        for(int i=0; i < deilData.data.basis.size();i++){
                            ChengZhangHuoBanBasic item = deilData.data.basis.get(i);
                            if(item.cate.equals("1")){
                                ChengZhangHuoBanShow pos = new ChengZhangHuoBanShow();
                                pos.data = item.content;
                                pos.cate = item.cate;
                                pos.name = item.name;
                                showList.add(pos);
//                                if(i !=deilData.data.basis.size()-1){
                                    ChengZhangHuoBanShow jiange = new ChengZhangHuoBanShow();
                                    jiange.cate="0";
                                    showList.add(jiange);
//                                }
                            }else if(item.cate.equals("2")){
                                ChengZhangHuoBanShow pos = new ChengZhangHuoBanShow();
                                pos.cate = item.cate;
                                pos.name = item.name;
                                pos.frameData = new ArrayList<>();
                                FrameData frameData = null;
                                for (int k =0; k < item.content.size(); k++){

                                    ColumnData temp  =  item.content.get(k);
                                    int index = k % 6;
                                    if(index == 0){
                                        frameData = new FrameData();
                                        frameData.data1 = temp;
                                    }else if(index == 1){
                                        frameData.data2 = temp;
                                    }else if(index == 2){
                                        frameData.data3 = temp;
                                    }else if(index == 3){
                                        frameData.data4 = temp;
                                    }else if(index == 4){
                                        frameData.data5 = temp;
                                    }else if(index == 5){
                                        frameData.data6 = temp;
                                        pos.frameData.add(frameData);
                                        frameData = null;
                                    }
                                    if(k == item.content.size() -1){
                                        if(frameData != null)
                                           pos.frameData.add(frameData);
                                    }



                                }
                                showList.add(pos);
//                                if(i !=deilData.data.basis.size()-1){
                                    ChengZhangHuoBanShow jiange = new ChengZhangHuoBanShow();
                                    jiange.cate="0";
                                    showList.add(jiange);
//                                }
                            }
                        }
                        deilAdapter = new DeilAdapter();
                        listview.setAdapter(deilAdapter);

                    }
                }

            }
        }
    };

    class DeilAdapter extends BaseAdapter{
        Horizontadapter horizontadapter = new Horizontadapter();


        HashMap<String, ArrayList<ImageView>> tips = new HashMap<>();
        HashMap<String,FrameAdapter> frameAdapter = new HashMap<>();

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
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(ChengZhangHuoBanDeil.this, R.layout.chengzhanghuobandeiladapter, null);
                holder.titlelay = (LinearLayout)convertView.findViewById(R.id.titlelay);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.horizonlist = (HorizontalListView)convertView.findViewById(R.id.horizonlist);
                holder.fragmentlay =(LinearLayout)convertView.findViewById(R.id.fragmentlay);
                holder.fragmenttitle = (TextView)convertView.findViewById(R.id.fragmenttitle);
                holder.fragment = (RelativeLayout)convertView.findViewById(R.id.fragment);
                holder.viewPager = (ViewPager)convertView.findViewById(R.id.viewPager);
                holder.viewGroup = (LinearLayout)convertView.findViewById(R.id.viewGroup);
                holder.jiange = (LinearLayout)convertView.findViewById(R.id.jiange);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();

            }
            final ChengZhangHuoBanShow item = showList.get(position);
            holder.titlelay.setVisibility(View.GONE);
            holder.fragmentlay.setVisibility(View.GONE);
            if(item.cate.equals("0")){
                holder.titlelay.setVisibility(View.GONE);
                holder.fragmentlay.setVisibility(View.GONE);
                holder.jiange.setVisibility(View.VISIBLE);

            }else if(item.cate.equals("1")){
                holder.titlelay.setVisibility(View.VISIBLE);
                holder.fragmentlay.setVisibility(View.GONE);
                holder.jiange.setVisibility(View.GONE);
                holder.title.setText(item.name);
                horizontadapter.setData(item.data);
                holder.horizonlist.setAdapter(horizontadapter);
                horizontadapter.notifyDataSetChanged();
            }else if(item.cate.equals("2")){
                holder.titlelay.setVisibility(View.GONE);
                holder.fragmentlay.setVisibility(View.VISIBLE);
                holder.jiange.setVisibility(View.GONE);
                holder.viewGroup.removeAllViews();
                holder.fragmenttitle.setText(item.name);
//                frameAdapter.setData(item.frameData);
                FrameAdapter positem = frameAdapter.get(item.name);
                if(positem == null){
                    FrameAdapter  Adapter  = new FrameAdapter(item.frameData);
                    holder.viewPager.setAdapter(Adapter);
                    frameAdapter.put(item.name, Adapter);
                }else{
                    holder.viewPager.setAdapter(positem);
                }

                ArrayList<ImageView> pos = tips.get(item.name);
                if(pos == null){
                    ArrayList<ImageView> tip = new ArrayList<>();
                    for(int i =0; i<item.frameData.size();i++){
                        ImageView imageView = new ImageView(ChengZhangHuoBanDeil.this);
                        if (i == 0) {
                            imageView.setImageResource(R.drawable.red_focused);
                        } else {
                            imageView.setImageResource(R.drawable.dot_normal);
                        }
                        tip.add(imageView);
                    }
                    tips.put(item.name, tip);
                    for(int i=0; i<tip.size();i++){
                        ImageView img = tip.get(i);
                        if(MyApplication.width<=480){
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                            params.setMargins(5, 0, 5, 0);
                            holder.viewGroup.addView(img, params);

                        }else{
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                            params.setMargins(5, 0, 5,5);
                            holder.viewGroup.addView(img, params);
                        }
                    }

                }else{
                    for(int i=0; i<pos.size();i++){
                        ImageView img = pos.get(i);
                        if(MyApplication.width<=480){
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                            params.setMargins(5, 0, 5, 0);
                            holder.viewGroup.addView(img, params);

                        }else{
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                            params.setMargins(5, 0, 5,5);
                            holder.viewGroup.addView(img, params);
                        }
                    }
                }

                holder.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        ArrayList<ImageView> pos = tips.get(item.name);
                        int index=position;
                        for(int i=0; i<pos.size(); i++){
                            if(i == index){
                                pos.get(i).setImageResource(R.drawable.red_focused);
                            }else{
                                pos.get(i).setImageResource( R.drawable.dot_normal);
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }

            return convertView;
        }
        class ViewHolder {
            public LinearLayout titlelay,jiange;
            public TextView title;
            public HorizontalListView horizonlist;

            public LinearLayout fragmentlay;
            public TextView fragmenttitle;
            public RelativeLayout fragment;
            public ViewPager viewPager;
            public LinearLayout viewGroup;
        }

        class Horizontadapter extends BaseAdapter{
            private  List<ColumnData> data = new ArrayList<>();


            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHold hold;
                if(convertView == null){
                    hold = new ViewHold();
                    convertView = View.inflate(ChengZhangHuoBanDeil.this, R.layout.horadapter, null);
                    hold.name = (TextView) convertView.findViewById(R.id.name);
                    hold.icon = (ImageView)convertView.findViewById(R.id.icon);
                    hold.bg   = (AutoRelativeLayout) convertView.findViewById(R.id.bg);
                    convertView.setTag(hold);

                }else{
                     hold = (ViewHold)convertView.getTag();
                }
                final  ColumnData item = data.get(position);
                ImageLoader.getInstance().displayImage(item.imgUrl
                        , hold.icon, options);
                hold.name.setText(item.name);
                hold.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleronClick(item.type,item,item.channelid);
                    }
                });

                return convertView;
            }
            public void setData(List<ColumnData> data){
                this.data = data;
//                this.notifyDataSetChanged();

            }
            class ViewHold{
                public ImageView icon;
                public TextView name;
                public AutoRelativeLayout bg;
            }
        }

        class FrameAdapter extends PagerAdapter {
            View view =null;
            LinkedList<View> mCaches = new LinkedList<View>();
            private  List<FrameData>  frameData = null;
            public FrameAdapter(List<FrameData>  frameData){
                this.frameData = frameData;
            }

            @Override
            public int getCount() {
                if(frameData.size() == 1){
                    return 1;
                }
                return frameData.size();
            }

            /**
             * @param container
             * @param position
             * @return
             */
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View converView = null;
                try {
                    int index =0;
                    if(frameData.size() == 0){
                        index = 0;
                    }else{
                        index = position ;
                    }

                    final FrameData item = frameData.get(index);

                    ViewHolder mHolder = null;
                    if(mCaches.size() == 0){
                        converView = View.inflate(ChengZhangHuoBanDeil.this,R.layout.huobanframe, null);
                        mHolder = new ViewHolder();
                        mHolder.one_lay1 = (LinearLayout)converView.findViewById(R.id.one_lay1);
                        mHolder.one_lay2 = (LinearLayout)converView.findViewById(R.id.one_lay2);
                        mHolder.one_lay3 = (LinearLayout)converView.findViewById(R.id.one_lay3);
                        mHolder.two_lay1 = (LinearLayout)converView.findViewById(R.id.two_lay1);
                        mHolder.two_lay2 = (LinearLayout)converView.findViewById(R.id.two_lay2);
                        mHolder.two_lay3 = (LinearLayout)converView.findViewById(R.id.two_lay3);
                        mHolder.icon1 = (ImageView) converView.findViewById(R.id.icon1);
                        mHolder.icon2 = (ImageView) converView.findViewById(R.id.icon2);
                        mHolder.icon3 = (ImageView) converView.findViewById(R.id.icon3);
                        mHolder.icon4 = (ImageView) converView.findViewById(R.id.icon4);
                        mHolder.icon5 = (ImageView) converView.findViewById(R.id.icon5);
                        mHolder.icon6 = (ImageView) converView.findViewById(R.id.icon6);

                        mHolder.name1 = (TextView)  converView.findViewById(R.id.name1);
                        mHolder.name2 = (TextView)  converView.findViewById(R.id.name2);
                        mHolder.name3 = (TextView)  converView.findViewById(R.id.name3);
                        mHolder.name4 = (TextView)  converView.findViewById(R.id.name4);
                        mHolder.name5 = (TextView)  converView.findViewById(R.id.name5);
                        mHolder.name6 = (TextView)  converView.findViewById(R.id.name6);
                        converView.setTag(mHolder);
                    }else{
                        converView = (View)mCaches.removeFirst();
                        mHolder = (ViewHolder)converView.getTag();
                    }

                    if(item.data1 != null){
                        mHolder.icon1.setVisibility(View.VISIBLE);
                        mHolder.name1.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data1.imgUrl
                            ,mHolder.icon1 , options);
                        mHolder.name1.setText(item.data1.name);

                    }else{
                        mHolder.icon1.setVisibility(View.GONE);
                        mHolder.name1.setVisibility(View.GONE);
                    }
                    if(item.data2 != null){
                        mHolder.icon2.setVisibility(View.VISIBLE);
                        mHolder.name2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data2.imgUrl
                                ,mHolder.icon2 , options);
                        mHolder.name2.setText(item.data2.name);

                    }else {
                        mHolder.icon2.setVisibility(View.GONE);
                        mHolder.name2.setVisibility(View.GONE);
                    }

                    if(item.data3 != null){
                        mHolder.icon3.setVisibility(View.VISIBLE);
                        mHolder.name3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data3.imgUrl
                                ,mHolder.icon3 , options);
                        mHolder.name3.setText(item.data3.name);

                    }else{
                        mHolder.icon3.setVisibility(View.GONE);
                        mHolder.name3.setVisibility(View.GONE);
                    }

                    if(item.data4 != null){
                        mHolder.icon4.setVisibility(View.VISIBLE);
                        mHolder.name4.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data4.imgUrl
                                ,mHolder.icon4 , options);
                        mHolder.name4.setText(item.data4.name);

                    }else{
                        mHolder.icon4.setVisibility(View.GONE);
                        mHolder.name4.setVisibility(View.GONE);
                    }

                    if(item.data5 != null){
                        mHolder.icon5.setVisibility(View.VISIBLE);
                        mHolder.name5.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data5.imgUrl
                                ,mHolder.icon5 , options);
                        mHolder.name5.setText(item.data5.name);

                    }else {
                        mHolder.icon5.setVisibility(View.GONE);
                        mHolder.name5.setVisibility(View.GONE);
                    }

                    if(item.data6 != null){
                        mHolder.icon6.setVisibility(View.VISIBLE);
                        mHolder.name6.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.data6.imgUrl
                                ,mHolder.icon6 , options);
                        mHolder.name6.setText(item.data6.name);

                    }else{
                        mHolder.icon6.setVisibility(View.GONE);
                        mHolder.name6.setVisibility(View.GONE);
                    }

                    mHolder.one_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data1.type,item.data1,item.data1.channelid);
                        }
                    });
                    mHolder.one_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data2.type,item.data2,item.data2.channelid);
                        }
                    });
                    mHolder.one_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data3.type,item.data3,item.data3.channelid);
                        }
                    });

                    mHolder.two_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data4.type,item.data4,item.data4.channelid);
                        }
                    });
                    mHolder.two_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data5.type,item.data5,item.data5.channelid);
                        }
                    });

                    mHolder.two_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleronClick(item.data6.type,item.data6,item.data6.channelid);
                        }
                    });

                    container.addView(converView);

                }catch (IllegalStateException exx){}
                catch (NumberFormatException ex){}
                catch ( Exception e){}
                return  converView;

            }

            private class ViewHolder{
                public ImageView icon1,icon2,icon3,icon4,icon5,icon6;
                public TextView  name1,name2,name3,name4,name5,name6;
                public LinearLayout one_lay1,one_lay2,one_lay3,two_lay1,two_lay2,two_lay3;
            }

            public void setData(List<FrameData>  frameData){
                this.frameData = frameData;
            }



            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if(mCaches.size() >0){
                    mCaches.clear();
                }
                container.removeView((View)object);
                mCaches.add((View)object);
//            super.destroyItem(container, position, object);
//            container.removeView(view);
//            try {
//                if(weakReference != null){
//                    view = weakReference.get();
//                }
//                if(view != null){
//                    view = null;
//                    weakReference = null;
//                }
//            }catch (Exception e){
//
//            }
            }
            //        @Override
//        public void onPageSelected(int arg0) {
//            setImageBackground(arg0 % imgIdArray.length);
//        }





        }
    }

    private void handleronClick(String type, ColumnData data,String channelid){
        if(type.equals("expertAppointment")){
            Intent intent = new Intent(ChengZhangHuoBanDeil.this, AppointmentSpecialist.class);
            startActivity(intent);
        }else if(type.equals("html")){
            Intent intent=new Intent(ChengZhangHuoBanDeil.this, ActiveActivity.class);
            intent.putExtra("title", data.title);
            intent.putExtra("url", data.jumpUrl);
            startActivity(intent);
        }else if(type.equals("intelligence")){
            Intent intent = new Intent(ChengZhangHuoBanDeil.this, QingBaoActivity.class);
            startActivity(intent);
        }else if(type.equals("boutiqueProject")){
            Intent intent = new Intent(ChengZhangHuoBanDeil.this, XiangMuDuiJieActivity.class);
            startActivity(intent);
        }else if(type.equals("place")){
            if(data.typeid.equals("1")){
                Intent intent = new Intent(ChengZhangHuoBanDeil.this, DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",data.cityCode);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);

                startActivity(intent);
            }else if(data.typeid.equals("6")){
                Intent intent = new Intent(ChengZhangHuoBanDeil.this, DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",data.cityCode);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("11")){
                Intent intent = new Intent(ChengZhangHuoBanDeil.this, DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",data.cityCode);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid",channelid);

                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("xr")){
                Intent intent = new Intent(ChengZhangHuoBanDeil.this, DFInfoShow.class);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",data.cityCode);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid",channelid);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        }else{

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("成长伙伴-详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("成长伙伴-详情");
        MobclickAgent.onPause(this);
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
}
