package com.maidiantech;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Flow;
import entity.Ret;
import entity.XuQiuXiangQingEntry;
import view.BTAlertDialog;
import view.RoundImageView;
import view.RoundImageView2;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.TimeUtils;
import Util.*;

/**
 * Created by Administrator on 2018/5/16.
 */

public class NewXuQiuXiangQing extends AutoLayoutActivity implements AbsListView.OnScrollListener, View.OnTouchListener {
    private final int TAB_SHOW = 0; //显示
    private final int TAB_HIDE = 1; //隐藏
    private View layout_top;
    private float mFirstY;
    private float mCurrentY;
    private boolean mIsShow = true;
    private ObjectAnimator mAnimator;
    private int mFirstVisiableItem;
    private boolean isaddFrist = true;
    ListView lv_list;
    View head;
    String json;
    boolean isherat = true;

    XuQiuXiangQingEntry xiangqing;
    private DisplayImageOptions options;

    ProgressBar progress;
    NewXuQiuXiangQingAdapter adapter;
    String user;

    LinearLayout linetitle,content,more_info;
    TextView senddata,xuqiutype,xm_title,xm_rank,xm_linyu,xm_description,bottmon_title,add_cenel,add_kefu;
    ImageView xm_img,about_backs,need_add;
    RoundImageView rc_img;
    String id;

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
//            window.setNavigationBarColor(Color.WHITE);
        }
        setContentView(R.layout.newxuqiuxiangqing);
        options = ImageLoaderUtils.initOptions();
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
            tintManager.setStatusBarAlpha(0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
        MIUISetStatusBarLightMode(getWindow(), true);

        layout_top = findViewById(R.id.layout_top);


         head = getLayoutInflater().inflate(R.layout.head_hide, null);
         head.setVisibility(View.GONE);

         lv_list = (ListView) findViewById(R.id.lv_list);
//        lv_list.addHeaderView(head);
//        lv_list.setAdapter(new MyAdaper());
        lv_list.setOnScrollListener(this);
        lv_list.setOnTouchListener(this);
        lv_list.setBackgroundColor(0xffF6F6F6);
        senddata = (TextView)head.findViewById(R.id.senddata);
        xuqiutype = (TextView)head.findViewById(R.id.xuqiutype);
        xm_title = (TextView)head.findViewById(R.id.xm_title);
        xm_rank = (TextView)head.findViewById(R.id.xm_rank);
        xm_linyu = (TextView)head.findViewById(R.id.xm_linyu);
        content = (LinearLayout) head.findViewById(R.id.content);
        xm_description = (TextView)head.findViewById(R.id.xm_description);
        rc_img = (RoundImageView)head.findViewById(R.id.rc_img);
        xm_img = (ImageView)head.findViewById(R.id.xm_img);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        more_info = (LinearLayout)findViewById(R.id.more_info);
        more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        add_cenel = (TextView)findViewById(R.id.add_cenel);
        add_cenel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_info.setVisibility(View.GONE);
                if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    cenel_yuyue(xiangqing.data.basic.id, mid);
                }
            }
        });
        add_kefu =(TextView)findViewById(R.id.add_kefu);
        add_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_info.setVisibility(View.GONE);
                final BTAlertDialog dialog = new BTAlertDialog(NewXuQiuXiangQing.this);
                dialog.setTitle("客服电话:");
                dialog.setTitle1("400-8530-500");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("呼叫", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + "4008818083");
                            intent.setData(data);
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }});
                dialog.show();

            }
        });
        need_add = (ImageView)findViewById(R.id.need_add);
        need_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(more_info.getVisibility() == View.VISIBLE){
                    more_info.setVisibility(View.GONE);
                }else if(more_info.getVisibility() == View.GONE){
                    more_info.setVisibility(View.VISIBLE);
                }
            }
        });
        about_backs = (ImageView)findViewById(R.id.about_backs);
        about_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        layout_top.setVisibility(View.GONE);
        progress = (ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");


    }

    @Override
    protected void onResume() {
        super.onResume();
        getJsonData();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点下去的起始y坐标
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //称动时的y坐标
                mCurrentY = event.getY();
                if (mFirstY - mCurrentY > 50) { //手式向上滑动
                    if(mIsShow && mFirstVisiableItem > 0){ //当head不可见时，才可以隐藏头部，要不会有一段空白
                        mIsShow = false;
//                        toolbarAnimal(TAB_HIDE); //隐藏
//                        layout_top.setVisibility(View.GONE);
                    }
                } else if (mCurrentY - mFirstY > 50) { //手式向下滑动
                    if(!mIsShow || mFirstVisiableItem == 0){//当head可见时，强制显示头部，要不会有一段空白
                        if(isaddFrist){
                            isaddFrist = false;
                            lv_list.addHeaderView(head);
                            head.setVisibility(View.VISIBLE);
                            toolbarAnimal(TAB_SHOW); //显示
                        }
//                        layout_top.setVisibility(View.VISIBLE);
                        mIsShow = true;

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
    private void toolbarAnimal(int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == TAB_SHOW) {
            mAnimator = ObjectAnimator.ofFloat(layout_top, "translationY", layout_top.getTranslationY(),0).setDuration(1000);
//                        mAnimator = ObjectAnimator.ofFloat(layout_top, "alpha", 1f, 0f);
        } else {
           mAnimator = ObjectAnimator.ofFloat(layout_top, "translationY", layout_top.getTranslationY(), -layout_top.getHeight()).setDuration(1000);

//            mAnimator = ObjectAnimator.ofFloat(layout_top, "alpha", 0f, 1f).setDuration(1000);
        }
        mAnimator.start();
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                 if(!mIsShow){
                     layout_top.setVisibility(View.GONE);
                 }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisiableItem = firstVisibleItem;
    }



    class NewXuQiuXiangQingAdapter extends  BaseAdapter{

        public TextView viewText=null;
        public String current;
        public   View mView;
        public   long oldtime = 0;
        public    int pos;
        public String mobile,tel;

        @Override
        public int getCount() {
            return xiangqing.data.flow.size();
        }

        @Override
        public Object getItem(int position) {
            return xiangqing.data.flow.get(position);
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
                convertView = View.inflate(NewXuQiuXiangQing.this, R.layout.newxuqiuxiangadpater,null);
                holdView.phone = (RelativeLayout)convertView.findViewById(R.id.phone);
                holdView.jingjirenlay = (RelativeLayout)convertView.findViewById(R.id.jingjirenlay);
                holdView.jingji = (ImageView)convertView.findViewById(R.id.jingji);
                holdView.phone_call = (ImageView)convertView.findViewById(R.id.phone_call);
                holdView.jingjiren = (TextView)convertView.findViewById(R.id.jingjiren);
                holdView.state_cenel = (LinearLayout)convertView.findViewById(R.id.state_cenel);
                holdView.state_time = (TextView)convertView.findViewById(R.id.state_time);
                holdView.state_pinglun = (ImageView)convertView.findViewById(R.id.state_pinglun);
                holdView.state_pipei = (TextView)convertView.findViewById(R.id.state_pipei);
                holdView.state_tel = (TextView)convertView.findViewById(R.id.state_tel);

                holdView.state_xinxi = (LinearLayout)convertView.findViewById(R.id.state_xinxi);
                holdView.xinxi_time = (TextView)convertView.findViewById(R.id.xinxi_time);
                holdView.xinxi_context = (TextView)convertView.findViewById(R.id.xinxi_context);

                holdView.fuhe = (LinearLayout)convertView.findViewById(R.id.fuhe);
                holdView.content = (LinearLayout)convertView.findViewById(R.id.content);
                holdView.xangmushebeizhuanli = (TextView)convertView.findViewById(R.id.xangmushebeizhuanli);
                holdView.titie = (TextView)convertView.findViewById(R.id.titie);
                holdView.xm_img = (ImageView)convertView.findViewById(R.id.xm_img);
                holdView.title_lay = (LinearLayout)convertView.findViewById(R.id.title_lay);
                holdView.xm_title = (TextView)convertView.findViewById(R.id.xm_title);
                holdView.xm_rank = (TextView)convertView.findViewById(R.id.xm_rank);
                holdView.xm_linyu = (TextView)convertView.findViewById(R.id.xm_linyu);
                holdView.bottmon = (TextView)convertView.findViewById(R.id.bottmon);

                holdView.rencai = (LinearLayout)convertView.findViewById(R.id.rencai);
                holdView.rencai_content = (LinearLayout)convertView.findViewById(R.id.rencai_content);
                holdView.rencai_title_lay = (LinearLayout)convertView.findViewById(R.id.rencai_title_lay);
                holdView.rencai_time = (TextView)convertView.findViewById(R.id.rencai_time);
                holdView.rencai_titie = (TextView)convertView.findViewById(R.id.rencai_titie);
                holdView.rencai_rank = (TextView)convertView.findViewById(R.id.rencai_rank);
                holdView.rencai_linyu = (TextView)convertView.findViewById(R.id.rencai_linyu);
                holdView.shijijiage = (TextView)convertView.findViewById(R.id.shijijiage);
                holdView.yuanjia = (TextView)convertView.findViewById(R.id.yuanjia);
                holdView.rencai_img = (RelativeLayout)convertView.findViewById(R.id.rencai_img);
                holdView.rencai_img_lay = (RelativeLayout)convertView.findViewById(R.id.rencai_img_lay);
                holdView.rc_img =(RoundImageView)convertView.findViewById(R.id.rc_img);
                holdView.rc_img1 =(RoundImageView2)convertView.findViewById(R.id.rc_img1);
                holdView.adress_lay = (RelativeLayout)convertView.findViewById(R.id.adress_lay);
                holdView.adress_time = (TextView)convertView.findViewById(R.id.adress_time);
                holdView.adress_content = (TextView)convertView.findViewById(R.id.adress_content);
                holdView.bottmon_rencai = (TextView)convertView.findViewById(R.id.bottmon_rencai);
                holdView.biankuang = (LinearLayout)convertView.findViewById(R.id.biankuang);
                holdView.cencel_zhifu_chenggong = (LinearLayout)convertView.findViewById(R.id.cencel_zhifu_chenggong);
                holdView.cencel_zhifu_chenggong_time = (TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_time);
                holdView.cencel_zhifu_chenggong_titie = (TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_titie);
                holdView.cencel_zhifu_chenggong_title =(TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_title);
                holdView.cencel_zhifu_chenggong_rank =(TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_rank);
                holdView.cencel_zhifu_chenggong_img_lay_linyu =(TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_img_lay_linyu);
                holdView.zhenshi_jiage =(TextView)convertView.findViewById(R.id.zhenshi_jiage);
                holdView.yuanshijiage =(TextView)convertView.findViewById(R.id.yuanshijiage);
                holdView.cencel_zhifu_chenggong_img_lay_rencai =(TextView)convertView.findViewById(R.id.cencel_zhifu_chenggong_img_lay_rencai);
                holdView.cencel_zhifu_chenggongimg=(RoundImageView)convertView.findViewById(R.id.cencel_zhifu_chenggongimg);
                holdView.cencel_zhifu_chenggongimg1=(RoundImageView2)convertView.findViewById(R.id.cencel_zhifu_chenggongimg1);
                holdView.jieshou_content = (LinearLayout)convertView.findViewById(R.id.jieshou_content);
                holdView.pay = (LinearLayout)convertView.findViewById(R.id.pay);
                holdView.pay_time = (TextView)convertView.findViewById(R.id.pay_time);
                holdView.pay_titie=(TextView)convertView.findViewById(R.id.pay_titie);
                holdView.pay_title=(TextView)convertView.findViewById(R.id.pay_title);
                holdView.pay_rank = (TextView)convertView.findViewById(R.id.pay_rank);
                holdView.pay_linyu =(TextView)convertView.findViewById(R.id.pay_linyu);
                holdView.pay_zhenshi =(TextView)convertView.findViewById(R.id.pay_zhenshi);
                holdView.pay_yuanshi =(TextView)convertView.findViewById(R.id.pay_yuanshi);
                holdView.pay_adress_time=(TextView)convertView.findViewById(R.id.pay_adress_time);
                holdView.pay_adress_lay = (RelativeLayout)convertView.findViewById(R.id.pay_adress_lay);
                holdView.adress=(TextView)convertView.findViewById(R.id.adress);
                holdView.gopay = (TextView)convertView.findViewById(R.id.gopay);
                holdView.rencai_title = (TextView)convertView.findViewById(R.id.rencai_title);
                holdView.pay_shengyu_time = (TextView)convertView.findViewById(R.id.pay_shengyu_time);
                holdView.pay_rencai = (TextView)convertView.findViewById(R.id.pay_rencai);
                holdView.pay_rc_img = (RoundImageView)convertView.findViewById(R.id.pay_rc_img);
                holdView.pay_rc_img1 = (RoundImageView2)convertView.findViewById(R.id.pay_rc_img1);
                holdView.pay = (LinearLayout)convertView.findViewById(R.id.pay);
                holdView.pay_time = (TextView)convertView.findViewById(R.id.pay_time);
                holdView.pay_titie = (TextView)convertView.findViewById(R.id.pay_titie);
                holdView.pay_title = (TextView)convertView.findViewById(R.id.pay_title);
                holdView.pay_rc_img = (RoundImageView)convertView.findViewById(R.id.pay_rc_img);
                holdView.pay_rc_img1 = (RoundImageView2)convertView.findViewById(R.id.pay_rc_img1);
                holdView.jieshou = (LinearLayout)convertView.findViewById(R.id.jieshou);
                holdView.jieshou_time = (TextView)convertView.findViewById(R.id.jieshou_time);
                holdView.jieshou_titie = (TextView)convertView.findViewById(R.id.jieshou_titie);
                holdView.jieshou_title = (TextView)convertView.findViewById(R.id.jieshou_title);
                holdView.jieshou_rank = (TextView)convertView.findViewById(R.id.jieshou_rank);
                holdView.jieshou_linyu = (TextView)convertView.findViewById(R.id.jieshou_linyu);
                holdView.jieshou_button = (TextView)convertView.findViewById(R.id.jieshou_button);
                holdView.jieshou_adress = (TextView)convertView.findViewById(R.id.jieshou_adress);
                holdView.jieshou_rencai = (TextView)convertView.findViewById(R.id.jieshou_rencai);
                holdView.jieshou_or_img = (RoundImageView)convertView.findViewById(R.id.jieshou_or_img);
                holdView.jieshou_or_img1 = (RoundImageView2)convertView.findViewById(R.id.jieshou_or_img1);
                holdView.pay_content = (LinearLayout)convertView.findViewById(R.id.pay_content);
                holdView.yuyue = (LinearLayout)convertView.findViewById(R.id.yuyue);
                holdView.yuyue_time = (TextView)convertView.findViewById(R.id.yuyue_time);
//                holdView.yuyue_titie = (TextView)convertView.findViewById(R.id.yuyue_titie);
                holdView.yuyue_title = (TextView)convertView.findViewById(R.id.yuyue_title);
                holdView.yuyue_rank = (TextView)convertView.findViewById(R.id.yuyue_rank);
                holdView.yuyue_linyu =(TextView)convertView.findViewById(R.id.yuyue_linyu);
                holdView.yuyuebutton = (TextView)convertView.findViewById(R.id.yuyuebutton);
                holdView.yuyuedes = (TextView)convertView.findViewById(R.id.yuyuedes);
                holdView.qupingjia = (TextView)convertView.findViewById(R.id.qupingjia);
//                holdView.yuyue_rencai== (TextView)findViewById(R.id.yuyue_rencai);
                holdView.qupingjialay = (RelativeLayout)convertView.findViewById(R.id.qupingjialay);
                holdView.yuyue_rencai = (TextView)convertView.findViewById(R.id.yuyue_rencai);
                holdView.yu_img = (RoundImageView)convertView.findViewById(R.id.yu_img);
                holdView.yu_img1 = (RoundImageView2)convertView.findViewById(R.id.yu_img1);
                holdView.jingjireninfo = (LinearLayout)convertView.findViewById(R.id.jingjireninfo);
                holdView.jingjireninfo_time = (TextView)convertView.findViewById(R.id.jingjireninfo_time);
                holdView.jingjireninfo_titie = (TextView)convertView.findViewById(R.id.jingjireninfo_titie);
                holdView.jingjireninfo_title = (TextView)convertView.findViewById(R.id.jingjireninfo_title);
                holdView.jingjireninfo_rank = (TextView)convertView.findViewById(R.id.jingjireninfo_rank);
                holdView.jingjireninfo_linyu = (TextView)convertView.findViewById(R.id.jingjireninfo_linyu);
                holdView.jingjireninfodes = (TextView)convertView.findViewById(R.id.jingjireninfodes);
                holdView.jingjireninfo_rencai = (TextView)convertView.findViewById(R.id.jingjireninfo_rencai);
                holdView.jingjirenin_img = (RoundImageView)convertView.findViewById(R.id.jingjirenin_img);
                holdView.jingjirenin_img1 = (RoundImageView2)convertView.findViewById(R.id.jingjirenin_img1);
                holdView.cencel_zhifu_chenggong_content = (LinearLayout)convertView.findViewById(R.id.cencel_zhifu_chenggong_content);
//                holdView.heart = (LinearLayout)convertView.findViewById(R.id.heart);
//                holdView.senddataheart =(TextView)convertView.findViewById(R.id.senddataheart);
//                holdView.xuqiutypeheart = (TextView)convertView.findViewById(R.id.xuqiutypeheart);
//                holdView.xm_titleheart = (TextView)convertView.findViewById(R.id.xm_titleheart);
//                holdView.xm_rankheart  =(TextView)convertView.findViewById(R.id.xm_rankheart);
//                holdView.xm_linyuheart = (TextView)convertView.findViewById(R.id.xm_linyuheart);
//                holdView.contentheart =(LinearLayout)convertView.findViewById(R.id.contentheart);
//                holdView.xm_descriptionheart = (TextView)convertView.findViewById(R.id.xm_descriptionheart);
//                holdView.xm_imgheart = (ImageView)convertView.findViewById(R.id.xm_imgheart);
//                holdView.rc_imgheart = (RoundImageView)convertView.findViewById(R.id.rc_imgheart);
                convertView.setTag(holdView);
            }else {
                holdView = (HoldView) convertView.getTag();
            }
            final Flow item = xiangqing.data.flow.get(position);
            try {
              /*  if(item.state.equals("-2")){
                    holdView.heart.setVisibility(View.VISIBLE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);


                    holdView.senddataheart.setText("发布时间:"+ TimeUtils.getStrXieXIANTime(item.pubdate));
                    holdView.xuqiutypeheart.setText("需求类型:"+item.typename);
                    if(item.typeid.equals("4")){
                        holdView.contentheart.setVisibility(View.VISIBLE);
                        holdView.contentheart.setBackgroundColor(0xffd8f4ed);
                        holdView.xm_descriptionheart.setVisibility(View.VISIBLE);
                        if(item.litpic != null && !item.litpic.equals("")){
                            holdView.rc_imgheart.setVisibility(View.VISIBLE);
                            holdView.xm_imgheart.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    ,  holdView.rc_imgheart, options);
                        }else{
                            holdView.rc_imgheart.setVisibility(View.VISIBLE);
                            holdView.rc_imgheart.setImageResource(R.mipmap.head_1);
                        }
                        holdView.xm_titleheart.setText(item.title);
                        holdView.xm_rankheart.setText(item.ranks);
                        holdView. xm_linyuheart.setText(item.area_cate);
                        holdView.xm_descriptionheart.setText(item.content);

                    }else if(item.typeid.equals("0")){
                        holdView.contentheart.setVisibility(View.GONE);
                        holdView.xm_descriptionheart.setVisibility(View.VISIBLE);
                        holdView.xm_descriptionheart.setText(item.content);

                    }else if(item.typeid.equals("2")){
                        holdView.contentheart.setVisibility(View.VISIBLE);
                        holdView.contentheart.setBackgroundColor(0xffdde1ed);
                        holdView.xm_descriptionheart.setVisibility(View.VISIBLE);
                        if(item.litpic != null && !item.litpic.equals("")){
                            holdView.rc_imgheart.setVisibility(View.GONE);
                            holdView.xm_imgheart.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    ,  holdView.xm_imgheart, options);
                        }else{
                            holdView.xm_imgheart.setVisibility(View.VISIBLE);
                            holdView.xm_imgheart.setImageResource(R.mipmap.information_placeholder);
                        }
                        holdView.xm_titleheart.setText(item.title);
                        holdView.xm_linyuheart.setText(item.area_cate);
                        holdView.xm_descriptionheart.setText(item.content);
                    }else if(item.typeid.equals("7")){
                        holdView.contentheart.setVisibility(View.VISIBLE);
                        holdView.contentheart.setBackgroundColor(0xfff9efea);
                        holdView. xm_descriptionheart.setVisibility(View.VISIBLE);
                        if(item.litpic != null && !item.litpic.equals("")){
                            holdView.rc_imgheart.setVisibility(View.GONE);
                            holdView.xm_imgheart.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.xm_imgheart, options);
                        }else{
                            holdView.xm_imgheart.setVisibility(View.VISIBLE);
                            holdView.xm_imgheart.setImageResource(R.mipmap.information_placeholder);
                        }
                        holdView.xm_titleheart.setText(item.title);
                        holdView.xm_linyuheart.setText(item.area_cate);
                        holdView.xm_descriptionheart.setText(item.content);

                    }

                    }else*/ if(item.state.equals("-1")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.VISIBLE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    if(item.uname == null){
                        holdView.jingjiren.setText("专属科技经纪人：匹配中...");
                    }else{
                        holdView.jingjiren.setText("专属科技经纪人："+item.uname);
                    }
                    holdView.phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.tel == null || item.tel.equals("")){
//                                Toast.makeText(NewXuQiuXiangQing.this, "暂无经纪人电话", Toast.LENGTH_SHORT).show();
                                try {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + "4008818083");
                                    intent.setData(data);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }else{
                                try {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + item.tel);
                                    intent.setData(data);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        }
                    });
                    if(xiangqing.data.basic.typeid.equals("2")){
                        holdView.jingjiren.setTextColor(Color.parseColor("#8d9ac5"));
                        holdView.jingji.setVisibility(View.VISIBLE);
                        holdView.phone_call.setBackgroundResource(R.mipmap.xiangmu_call);
                        holdView.jingji.setBackgroundResource(R.mipmap.icon_xiangmu_xiangqing);
                    }else if(xiangqing.data.basic.typeid.equals("4")){
                        holdView.jingjiren.setTextColor(Color.parseColor("#5cc2b0"));
                        holdView.phone_call.setBackgroundResource(R.mipmap.rencai_call);
                        holdView.jingji.setBackgroundResource(R.mipmap.icon_rencai_xiangqing);
                        holdView.jingji.setVisibility(View.VISIBLE);
                    }else if(xiangqing.data.basic.typeid.equals("7")){
                        holdView.jingjiren.setTextColor(Color.parseColor("#f2a17e"));
                        holdView.jingji.setVisibility(View.VISIBLE);
                        holdView.phone_call.setBackgroundResource(R.mipmap.shebei_call);
                        holdView.jingji.setBackgroundResource(R.mipmap.icon_shebei_xiangqing);
                    }else if(xiangqing.data.basic.typeid.equals("0")){
                        holdView.jingjiren.setTextColor(Color.parseColor("#3e3e3e"));
                        holdView.jingji.setBackgroundResource(R.mipmap.putongxuqiu);
                        holdView.phone_call.setBackgroundResource(R.mipmap.dianhua_num);

                    }
                    holdView.phone_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if(item.tel == null || item.tel.equals("")){
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        Uri data = Uri.parse("tel:" + "4008818083");
                                        intent.setData(data);
                                        startActivity(intent);
                                    }catch (Exception e){

                                    }
                                }else{
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        Uri data = Uri.parse("tel:" + item.tel);
                                        intent.setData(data);
                                        startActivity(intent);
                                    }catch (Exception e){

                                    }
                                }
                            }catch (Exception e){

                            }
                        }
                    });


                }else if(item.state.equals("10")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.VISIBLE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.xinxi_time.setText( TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.xinxi_context.setText(item.content);
                }else if(item.state.equals("20")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.VISIBLE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.state_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.state_pipei.setText("专属经纪人匹配成功："+item.uname);
                    holdView.state_tel.setText(item.tel);
                    if(xiangqing.data.basic.typeid.equals("2")){
                        holdView.state_tel.setTextColor(Color.parseColor("#8d9ac5"));
                    }else if(xiangqing.data.basic.typeid.equals("4")){
                        holdView.state_tel.setTextColor(Color.parseColor("#5cc2b0"));
                    }else if(xiangqing.data.basic.typeid.equals("7")){
                        holdView.state_tel.setTextColor(Color.parseColor("#f2a17e"));

                    }else if(xiangqing.data.basic.typeid.equals("0")){
                        holdView.state_tel.setTextColor(Color.parseColor("#3e3e3e"));
                    }

                }else if(Integer.parseInt(item.state)>= 1000){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.VISIBLE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.state_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.state_pipei.setText(item.content);
                    holdView.state_tel.setText(item.tel);
                    if(xiangqing.data.basic.typeid.equals("2")){
                        holdView.state_tel.setTextColor(Color.parseColor("#8d9ac5"));
                    }else if(xiangqing.data.basic.typeid.equals("4")){
                        holdView.state_tel.setTextColor(Color.parseColor("#5cc2b0"));
                    }else if(xiangqing.data.basic.typeid.equals("7")){
                        holdView.state_tel.setTextColor(Color.parseColor("#f2a17e"));
                    }else if(xiangqing.data.basic.typeid.equals("0")){
                        holdView.state_tel.setTextColor(Color.parseColor("#3e3e3e"));
                    }
                }else if(item.state.equals("510")  || item.state.equals("610") || item.state.equals("710")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.VISIBLE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.xangmushebeizhuanli.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.titie.setText(item.str);
                    holdView.xm_title.setText(item.title);
                    if(item.litpic != null && !item.litpic.equals("")){
                        ImageLoader.getInstance().displayImage(item.litpic
                                , holdView.xm_img, options);
                    }else{
                        holdView.xm_img.setImageResource(R.mipmap.information_placeholder);
                    }
                    holdView.xm_linyu.setText(item.area_cate);
                    if(item.str == null || item.str.equals("")){
                        holdView.biankuang.setVisibility(View.GONE);
                    }else {
                        holdView.biankuang.setVisibility(View.VISIBLE);
                    }
                    //少一个最下面的文字
                    holdView.bottmon.setText(item.reply);
                    if(item.typeid.equals("2")){
                        holdView.biankuang.setBackgroundResource(R.drawable.shape_top_xiangmu);
                        holdView.bottmon.setTextColor(Color.parseColor("#8d9ac5"));
                    }else if(item.typeid.equals("4")){
                        holdView.bottmon.setTextColor(Color.parseColor("#5cc2b0"));
                        holdView.biankuang.setBackgroundResource(R.drawable.shape_top_rencai);
                    }else if(item.typeid.equals("7")){
                        holdView.bottmon.setTextColor(Color.parseColor("#f2a17e"));
                        holdView.biankuang.setBackgroundResource(R.drawable.shape_top_shebei);
                    }else if(item.typeid.equals("0")){
                        holdView.bottmon.setTextColor(Color.parseColor("#3385ff"));
                        holdView.biankuang.setBackgroundResource(R.drawable.shape_top_zhuanli);
                    }else if(item.typeid.equals("5")){
                        holdView.bottmon.setTextColor(Color.parseColor("#9ac6f7"));
                        holdView.biankuang.setBackgroundResource(R.drawable.shape_top_zhuanli);
                    }
                    holdView.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.typeid.equals("2")){
//                                Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                                intent.putExtra("id", item.aid);
//                                intent.putExtra("name", "项目");
//                                intent.putExtra("pic",item.litpic);
//                                startActivity(intent);

                                Intent intent = new Intent(NewXuQiuXiangQing.this, NewProjectActivity.class);
                                intent.putExtra("aid", item.aid);
                                startActivity(intent);

                            }else if(item.typeid.equals("4")){
//                                Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                                intent.putExtra("id", item.aid);
//                                intent.putExtra("name", "专家");
//                                intent.putExtra("pic",item.litpic);
//                                startActivity(intent);
                                Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                                intent.putExtra("aid", item.aid);
                                startActivity(intent);

                            }else if(item.typeid.equals("7")){
                                Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
                                intent.putExtra("id", item.aid);
                                intent.putExtra("name", "设备");
                                intent.putExtra("pic",item.litpic);
                                startActivity(intent);

                            }else if(item.typeid.equals("5")){
                                Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
                                intent.putExtra("id", item.aid);
                                intent.putExtra("name", "专利");
                                intent.putExtra("pic",item.litpic);
                                startActivity(intent);
                            }
                        }
                    });
                    holdView.bottmon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);

                        }
                    });
                }else if(item.state.equals("150") || item.state.equals("200") || item.state.equals("180") || item.state.equals("160") || item.state.equals("190")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.VISIBLE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.rencai_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.rencai_titie.setText(item.str);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.rc_img.setVisibility(View.GONE);
                        holdView.rc_img1.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.rc_img1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.rc_img1, options);
                        }else{
                            holdView.rc_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.rc_img, options);
                        }

                    }else{
                        holdView.rc_img.setImageResource(R.mipmap.information_placeholder);
                    }
                    holdView.rencai_title.setText(item.title);
                    holdView.rencai_rank.setText(item.ranks);
                    holdView.rencai_linyu.setText(item.area_cate);
                    holdView.shijijiage.setText("￥"+item.price_discounts);
                    holdView.yuanjia.setText("￥"+item.price);
                    holdView.yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                    if(item.price.equals(item.price_discounts)){
                        holdView.yuanjia.setVisibility(View.GONE);
                    }else{
                        holdView.yuanjia.setVisibility(View.VISIBLE);
                    }

                    if(item.meetdate != null){
                        holdView.adress_time.setText(TimeUtils.getStrMonthAndDataTime(item.meetdate));
                    }else{
                        holdView.adress_time.setText("");
                    }
                    holdView.adress_content.setText(item.unit);
                    holdView.bottmon_rencai.setText(item.reply);
                    holdView.bottmon_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);
                        }
                    });
                    holdView.rencai_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                            intent.putExtra("id", item.aid);
//                            intent.putExtra("name", "专家");
//                            intent.putExtra("pic",item.litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);

                        }
                    });
                    holdView.adress_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.longitude >1 && item.latitude >1){
                                Intent intent =new Intent(NewXuQiuXiangQing.this, BaseMapDemo.class);
                                intent.putExtra("y", item.longitude);
                                intent.putExtra("x", item.latitude);
                                startActivity(intent);
                            }
                        }
                    });

                  if(item.state.equals("190")){
                      holdView.qupingjialay.setVisibility(View.VISIBLE);
                      holdView.qupingjia.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              CommentActivity.item = item;
                              Intent intent = new Intent(NewXuQiuXiangQing.this,CommentActivity.class);
                              intent.putExtra("typeid", item.typeid);
                              intent.putExtra("img", item.litpic);
                              intent.putExtra("title", item.title);
                              intent.putExtra("rank", item.ranks);
                              intent.putExtra("y", item.longitude);
                              intent.putExtra("x", item.latitude);
                              intent.putExtra("linyu", item.area_cate);
                              intent.putExtra("time", item.meetdate);
                              intent.putExtra("adress", item.unit);
                              intent.putExtra("resource_id", item.id);
                              startActivity(intent);
                          }
                      });
                  }else{
                      holdView.qupingjialay.setVisibility(View.GONE);
                  }


                }else if(item.state.equals("140") || item.state.equals("120")|| item.state.equals("130")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.VISIBLE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.cencel_zhifu_chenggong_titie.setText(item.str);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.cencel_zhifu_chenggongimg.setVisibility(View.GONE);
                        holdView.cencel_zhifu_chenggongimg1.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.cencel_zhifu_chenggongimg1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.cencel_zhifu_chenggongimg1, options);
                        }else{
                            holdView.cencel_zhifu_chenggongimg.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.cencel_zhifu_chenggongimg, options);
                        }

                    }else{
                        holdView.cencel_zhifu_chenggongimg.setImageResource(R.mipmap.information_placeholder);
                    }

                    holdView.cencel_zhifu_chenggong_title.setText(item.title);
                    holdView.cencel_zhifu_chenggong_rank.setText(item.ranks);
                    holdView.cencel_zhifu_chenggong_img_lay_linyu.setText(item.area_cate);
                    holdView.zhenshi_jiage.setText("￥"+item.price_discounts);
                    holdView.yuanshijiage.setText("￥"+item.price);
                    holdView.yuanshijiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                    if(item.price.equals(item.price_discounts)){
                        holdView.yuanshijiage.setVisibility(View.GONE);
                    }else{
                        holdView.yuanshijiage.setVisibility(View.VISIBLE);
                    }
                    holdView.cencel_zhifu_chenggong_img_lay_rencai.setText(item.reply);
                    holdView.cencel_zhifu_chenggong_img_lay_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }

                            startActivity(intent);
                        }
                    });
                    holdView.cencel_zhifu_chenggong_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                            intent.putExtra("id", item.aid);
//                            intent.putExtra("name", "专家");
//                            intent.putExtra("pic",item.litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }
                    });


                }else if(item.state.equals("170")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.VISIBLE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.pay_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.pay_titie.setText(item.str);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.pay_rc_img.setVisibility(View.GONE);
                        holdView.pay_rc_img1.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.pay_rc_img1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.pay_rc_img1, options);
                        }else {
                            holdView.pay_rc_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.pay_rc_img, options);
                        }

                    }else{
                        holdView.pay_rc_img.setImageResource(R.mipmap.information_placeholder);
                    }

                    if(item.meetmobile != null && !item.meetmobile.equals("")){
                        try {
                            String [] mobileAndTel = item.meetmobile.split("/");
                            mobile = mobileAndTel[0];
                            tel    = mobileAndTel[1];
                        }catch (Exception e){

                        }
                    }
                    holdView.pay_title.setText(item.title);
                    holdView.pay_rank.setText(item.ranks);
                    holdView.pay_linyu.setText(item.area_cate);
                    holdView.pay_zhenshi.setText("￥"+item.price_discounts);
                    holdView.pay_yuanshi.setText("￥"+item.price);
                    holdView.pay_yuanshi.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                    holdView.pay_rencai.setText(item.reply);
                    holdView.pay_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);
                        }
                    });
                    if(item.price.equals(item.price_discounts)){
                        holdView.pay_yuanshi.setVisibility(View.GONE);
                    }else{
                        holdView.pay_yuanshi.setVisibility(View.VISIBLE);
                        holdView.pay_yuanshi.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                    }

                    if(item.meetdate != null){
                        holdView.pay_adress_time.setText(TimeUtils.getStrMonthAndDataTime(item.meetdate));
                    }else{
                        holdView.pay_adress_time.setText("");
                    }
                    holdView.adress.setText(item.unit);
                    oldtime =(Long.parseLong(item.time)*1000+172800000)-System.currentTimeMillis()  ;
                    if(oldtime >1000){
                        holdView.pay_shengyu_time.setText(current);
                        handler.removeMessages(10);
                        viewText= holdView.pay_shengyu_time;
                        Message msg = Message.obtain();
                        msg.what =10;
                        handler.sendMessage(msg);
                        pos = position;

                        Log.d("lizisong","send");

                    }else{
                        holdView.pay_shengyu_time.setVisibility(View.VISIBLE);
                        viewText =  holdView.pay_shengyu_time;
                        current = "";
                        pos = 0;
                        handler.removeMessages(10);

                    }
                    holdView.pay_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                            intent.putExtra("id", item.aid);
//                            intent.putExtra("name", "专家");
//                            intent.putExtra("pic",item.litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }
                    });

                    holdView.pay_adress_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.longitude >1 && item.latitude >1){
                                Intent intent =new Intent(NewXuQiuXiangQing.this, BaseMapDemo.class);
                                intent.putExtra("y", item.longitude);
                                intent.putExtra("x", item.latitude);
                                startActivity(intent);
                            }
                        }
                    });


                    holdView.gopay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewText = null;
                            handler.removeMessages(10);
                            // 去支付流程
                            tiaozhuan(item.title, item.price, item.ranks, item.area_cate,mobile, tel,
                                    item.meetdate,item.unit, null, item.litpic,item.typeid,item.aid,item.id,item.latitude,item.longitude);
                        }
                    });




                }else if(item.state.equals("110")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.VISIBLE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.jieshou_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.jieshou_titie.setText(item.str);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.jieshou_or_img.setVisibility(View.GONE);
                        holdView.jieshou_or_img1.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.jieshou_or_img1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.jieshou_or_img1, options);
                        }else{
                            holdView.jieshou_or_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.jieshou_or_img, options);
                        }

                    }else{
                        holdView.jieshou_or_img.setImageResource(R.mipmap.information_placeholder);
                    }
                    holdView.jieshou_title.setText(item.title);
                    holdView.jieshou_rank.setText(item.ranks);
                    holdView.jieshou_linyu.setText(item.area_cate);
                    holdView.jieshou_rencai.setText(item.reply);
                    holdView.jieshou_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);
                        }
                    });
                    holdView.jieshou_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
//                            intent.putExtra("id", item.aid);
//                            intent.putExtra("name", "专家");
//                            intent.putExtra("pic",item.litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }
                    });
                    holdView.jieshou_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user ="1";
                            AgreeOrCenel(item.id,user,item.aid,item.rid, item.typeid);
                        }
                    });

                    holdView.jieshou_adress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user ="0";
                            AgreeOrCenel(item.id,user,item.aid,item.rid, item.typeid);
                        }
                    });


                }else if(item.state.equals("820")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.VISIBLE);
                    holdView.jingjireninfo.setVisibility(View.GONE);
                    holdView.yuyue_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
//                holdView.yuyue_titie.setText(item.str);
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.yu_img.setVisibility(View.GONE);
                        holdView.yu_img1.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.yu_img1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.yu_img1, options);
                        }else{
                            holdView.yu_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.yu_img, options);
                        }

                    }else{
                        holdView.yu_img.setImageResource(R.mipmap.information_placeholder);
                    }
                    holdView.yuyue_title.setText(item.title);
                    holdView.yuyue_linyu.setText(item.area_cate);
                    holdView.yuyuedes.setText(item.content);
                    holdView.yuyue_rencai.setText(item.reply);
                    holdView.yuyue_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);
                        }
                    });

                    holdView.yuyuebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //预约接口
                            yuyueJson(item.id,item.rid,item.aid,item.typeid);
                        }
                    });

                }else if(item.state.equals("810")){
//                    holdView.heart.setVisibility(View.GONE);
                    holdView.phone.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuhe.setVisibility(View.GONE);
                    holdView.rencai.setVisibility(View.GONE);
                    holdView.cencel_zhifu_chenggong.setVisibility(View.GONE);
                    holdView.pay.setVisibility(View.GONE);
                    holdView.jieshou.setVisibility(View.GONE);
                    holdView.yuyue.setVisibility(View.GONE);
                    holdView.jingjireninfo.setVisibility(View.VISIBLE);
                    holdView.jingjireninfo_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    if(item.litpic != null && !item.litpic.equals("")){
                        holdView.jingjirenin_img1.setVisibility(View.GONE);
                        holdView.jingjirenin_img.setVisibility(View.GONE);
                        if(item.litpic.contains("maidianzhanweitu.png")){
                            holdView.jingjirenin_img1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.jingjirenin_img1, options);
                        }else{
                            holdView.jingjirenin_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.jingjirenin_img, options);
                        }

                    }else{
                        holdView.jingjirenin_img.setImageResource(R.mipmap.information_placeholder);
                    }
                    holdView.jingjireninfo_title.setText(item.title);
                    holdView.jingjireninfo_linyu.setText(item.area_cate);
                    holdView.jingjireninfodes.setText(item.content);
                    holdView.jingjireninfo_rencai.setText(item.reply);
                    holdView.jingjireninfo_rencai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunicationAndReply.item = item;
                            Intent intent = new Intent(NewXuQiuXiangQing.this, CommunicationAndReply.class);
                            if(xiangqing != null && xiangqing.data != null && xiangqing.data.basic != null){
                                intent.putExtra("baseid",xiangqing.data.basic.id);
                            }
                            startActivity(intent);
                        }
                    });

                }
            }catch (Exception e){

            }

            return convertView;
        }
        class HoldView {
           public RelativeLayout phone,jingjirenlay,qupingjialay;
           public ImageView jingji,phone_call;
           public TextView  jingjiren;

          public LinearLayout state_cenel;
          public TextView state_time;
          public ImageView state_pinglun;
          public TextView state_pipei,state_tel;

          public LinearLayout state_xinxi;
          public TextView xinxi_time,xinxi_context;

          public LinearLayout fuhe,content,biankuang;
          public TextView xangmushebeizhuanli;
          public TextView titie;
          public ImageView xm_img;
          public LinearLayout title_lay;
          public TextView xm_title,xm_rank,xm_linyu,bottmon;

          public LinearLayout rencai,rencai_content,rencai_title_lay;
          public TextView rencai_time,rencai_titie,rencai_title,rencai_rank,rencai_linyu,shijijiage,yuanjia,qupingjia;
          public RelativeLayout rencai_img,rencai_img_lay;
          public RoundImageView rc_img;
          public RoundImageView2 rc_img1;
          public RelativeLayout adress_lay;
          public TextView adress_time,adress_content,bottmon_rencai;

          public LinearLayout cencel_zhifu_chenggong,cencel_zhifu_chenggong_content,jieshou_content;

          public TextView cencel_zhifu_chenggong_time,cencel_zhifu_chenggong_titie,cencel_zhifu_chenggong_title,cencel_zhifu_chenggong_rank,cencel_zhifu_chenggong_img_lay_linyu,zhenshi_jiage,yuanshijiage,cencel_zhifu_chenggong_img_lay_rencai;
          public RoundImageView cencel_zhifu_chenggongimg;
          public RoundImageView2 cencel_zhifu_chenggongimg1;

          public LinearLayout pay,pay_content;
          public RelativeLayout pay_adress_lay;
          public TextView pay_time,pay_titie,pay_title,pay_rank,pay_linyu,pay_zhenshi,pay_yuanshi,pay_adress_time,adress,gopay,pay_shengyu_time,pay_rencai;
          public RoundImageView pay_rc_img;
          public RoundImageView2 pay_rc_img1;

         public LinearLayout jieshou;
         public TextView jieshou_time,jieshou_titie,jieshou_title,jieshou_rank,jieshou_linyu,jieshou_button,jieshou_adress,jieshou_rencai;
         public RoundImageView jieshou_or_img;
         public RoundImageView2 jieshou_or_img1;
        public LinearLayout yuyue;
        public TextView yuyue_time,yuyue_title,yuyue_rank,yuyue_linyu,yuyuebutton,yuyuedes,yuyue_rencai;
        public RoundImageView yu_img;
        public RoundImageView2 yu_img1;
        public LinearLayout jingjireninfo;
        public TextView jingjireninfo_time,jingjireninfo_titie,jingjireninfo_title,jingjireninfo_rank,jingjireninfo_linyu,jingjireninfodes,jingjireninfo_rencai;
        public RoundImageView jingjirenin_img;
        public RoundImageView2 jingjirenin_img1;

        //头部展示
//        public  LinearLayout heart;
//        public TextView    senddataheart ;//= (TextView)head.findViewById(R.id.senddata);
//        public TextView     xuqiutypeheart;// = (TextView)head.findViewById(R.id.xuqiutype);
//        public TextView      xm_titleheart;// = (TextView)head.findViewById(R.id.xm_title);
//        public TextView   xm_rankheart ;//= //(TextView)head.findViewById(R.id.xm_rank);
//        public TextView    xm_linyuheart ;//= (TextView)head.findViewById(R.id.xm_linyu);
//        public LinearLayout   contentheart;// = (LinearLayout) head.findViewById(R.id.content);
//        public TextView    xm_descriptionheart;// = (TextView)head.findViewById(R.id.xm_description);
//        public RoundImageView   rc_imgheart ;//= (RoundImageView)head.findViewById(R.id.rc_img);
//        public ImageView    xm_imgheart;// = (ImageView)head.findViewById(R.id.xm_img);

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

    private void getJsonData(){
        String url="http://"+MyApplication.ip+"/api/require_new.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("method", "detail");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }

    private void cenel_yuyue(String id, String mid){
        String url ="http://"+MyApplication.ip+"/api/require_user_feedback.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("method", "cancel");
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,4,0);

    }


    private void yuyueJson(String id, String rid,String aid, String typeid){
        String url ="http://"+MyApplication.ip+"/api/require_user_feedback.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("method", "appointment");
        map.put("rid", rid);
        map.put("aid", aid);
        map.put("typeid", typeid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,3,0);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    json = (String)msg.obj;
                    xiangqing = gson.fromJson(json, XuQiuXiangQingEntry.class);
                    if(xiangqing != null && xiangqing.data != null){
                        senddata.setText("发布时间:"+ TimeUtils.getStrXieXIANTime(xiangqing.data.basic.pubdate));
                        xuqiutype.setText("需求类型:"+xiangqing.data.basic.typename);

                        if(xiangqing.data.basic.typeid.equals("4")){
                            content.setVisibility(View.VISIBLE);
                            content.setBackgroundColor(0xffd8f4ed);
                            xm_description.setVisibility(View.VISIBLE);
                            if(xiangqing.data.basic.litpic != null && !xiangqing.data.basic.litpic.equals("")){
                                rc_img.setVisibility(View.VISIBLE);
                                xm_img.setVisibility(View.GONE);
                                ImageLoader.getInstance().displayImage(xiangqing.data.basic.litpic
                                        , rc_img, options);
                            }else{
                                rc_img.setVisibility(View.VISIBLE);
                                rc_img.setImageResource(R.mipmap.head_1);
                            }
                            xm_title.setText(xiangqing.data.basic.title);
                            xm_rank.setText(xiangqing.data.basic.ranks);
                            xm_linyu.setText(xiangqing.data.basic.area_cate);
                            xm_description.setText(xiangqing.data.basic.content);
                            content.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(NewXuQiuXiangQing.this, NewRenCaiTail.class);
                                    intent.putExtra("aid", xiangqing.data.basic.aid);
                                    intent.putExtra("isShow", true);
                                    startActivity(intent);
                                }
                            });


                        }else if(xiangqing.data.basic.typeid.equals("0")){
                            content.setVisibility(View.GONE);
                            xm_description.setVisibility(View.VISIBLE);
                            xm_description.setText(xiangqing.data.basic.content);

                        }else if(xiangqing.data.basic.typeid.equals("2")){
                            content.setVisibility(View.VISIBLE);
                            content.setBackgroundColor(0xffdde1ed);
                            xm_description.setVisibility(View.VISIBLE);
                            if(xiangqing.data.basic.litpic != null && !xiangqing.data.basic.litpic.equals("")){
                                rc_img.setVisibility(View.GONE);
                                xm_img.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(xiangqing.data.basic.litpic
                                        , xm_img, options);
                            }else{
                                xm_img.setVisibility(View.VISIBLE);
                                xm_img.setImageResource(R.mipmap.information_placeholder);
                            }
                            xm_title.setText(xiangqing.data.basic.title);
                            xm_linyu.setText(xiangqing.data.basic.area_cate);
                            xm_description.setText(xiangqing.data.basic.content);

                            content.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(NewXuQiuXiangQing.this, NewProjectActivity.class);
                                    intent.putExtra("aid", xiangqing.data.basic.aid);
                                    intent.putExtra("isShow", true);
                                    startActivity(intent);
                                }
                            });

                        }else if(xiangqing.data.basic.typeid.equals("7")){
                            content.setVisibility(View.VISIBLE);
                            content.setBackgroundColor(0xfff9efea);
                            xm_description.setVisibility(View.VISIBLE);
                            if(xiangqing.data.basic.litpic != null && !xiangqing.data.basic.litpic.equals("")){
                                rc_img.setVisibility(View.GONE);
                                xm_img.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(xiangqing.data.basic.litpic
                                        , xm_img, options);
                            }else{
                                xm_img.setVisibility(View.VISIBLE);
                                xm_img.setImageResource(R.mipmap.information_placeholder);
                            }
                            xm_title.setText(xiangqing.data.basic.title);
                            xm_linyu.setText(xiangqing.data.basic.area_cate);
                            xm_description.setText(xiangqing.data.basic.content);
                            content.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(NewXuQiuXiangQing.this, DetailsActivity.class);
                                    intent.putExtra("id", xiangqing.data.basic.aid);
                                    intent.putExtra("isShow",true);
                                    intent.putExtra("name", "设备");
                                    startActivity(intent);
//                                    Intent intent = new Intent(NewXuQiuXiangQing.this, NewProjectActivity.class);
//                                    intent.putExtra("aid", xiangqing.data.basic.aid);
//                                    intent.putExtra("isShow", true);
//                                    startActivity(intent);
                                }
                            });

                        }

                        Flow jingjiren = new Flow();
                        jingjiren.state = "-1";
                        jingjiren.uname = xiangqing.data.basic.uname;
                        jingjiren.tel = xiangqing.data.basic.tel;
                        xiangqing.data.flow.add(0,jingjiren);


                        Flow heart = new Flow();
                        heart.state = "-2";
                        heart.pubdate= xiangqing.data.basic.pubdate;
                        heart.typename =xiangqing.data.basic.typename;
                        heart.litpic=xiangqing.data.basic.litpic;
                        heart.content =xiangqing.data.basic.content;
                        heart.ranks=xiangqing.data.basic.ranks;
                        heart.area_cate=xiangqing.data.basic.area_cate;
                        heart.title=xiangqing.data.basic.title;

//                        tel = data.basic.tel;
//                        if(data.basic.uname==null){
//                            jingjiren.setText("专属经济人:"+"匹配中");
//                        }else{
//                            jingjiren.setText("专属经济人:"+data.basic.uname);
//                        }

//                        lv_list.setSelection(4);
                        adapter = new NewXuQiuXiangQingAdapter();
                        lv_list.setAdapter(adapter);
//                        xiangqing.data.flow.add(0,heart);

//                        lv_list.addHeaderView(head);
//                        head.setVisibility(View.VISIBLE);

                    }
                }else if(msg.what == 10){
                    if(adapter.oldtime > 1000){
                        adapter.oldtime=adapter.oldtime-1000;
                    }else{
                        handler.removeMessages(10);
                    }
                    adapter.current = formatTime(adapter.oldtime);
                    Log.d("lizisong",adapter.current);
                    try {
                        if(adapter.viewText == null){
                            Log.d("lizisong","1");
                            adapter.viewText =  (TextView)lv_list.getChildAt(adapter.pos).findViewById(R.id.fuza_paytime);
                        }else{
                            Log.d("lizisong","2");
                            adapter.viewText.setText("("+adapter.current+")");
                        }

                    }catch (Exception e){

                    }


                    Message message = Message.obtain();
                    message.what =10;
                    handler.sendMessageDelayed(message, 1000);
                }
                if(msg.what == 2){
                    Gson gson = new Gson();
                    String str = (String)msg.obj;
                    Ret ret = gson.fromJson(str, Ret.class);
                    if(ret != null){
                        getJsonData();
                        Toast.makeText(NewXuQiuXiangQing.this, ret.message, Toast.LENGTH_SHORT).show();
                    }
                }
                if(msg.what == 3){
                    Gson gson = new Gson();
                    String str = (String)msg.obj;
                    Ret ret = gson.fromJson(str, Ret.class);
                    if(ret != null){
                        if(ret.code.equals("1")){
                            getJsonData();
                        }else{
                            Toast.makeText(NewXuQiuXiangQing.this, ret.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(msg.what == 4){
                    Gson gson = new Gson();
                    String str = (String)msg.obj;
                    Ret ret = gson.fromJson(str, Ret.class);
                    if(ret != null){
                        if(ret.code.equals("1")){
                            getJsonData();
                            Toast.makeText(NewXuQiuXiangQing.this, ret.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NewXuQiuXiangQing.this, ret.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }catch (Exception e){

            }

        }
    };
    /*
* 毫秒转化时分秒毫秒
*/
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        //  StringBuffer sb = new StringBuffer();
        String ret="";

        if(day > 0) {
//            sb.append(day+"天");
            hour =hour+day*24;
        }
        if(hour > 0) {
            //sb.append(hour+":");
            ret = hour+"：";
        }else{
            ret ="00：";
        }
        if(minute > 0) {
            // sb.append(minute+":");
            ret=ret+minute+"：";
        }else{
            ret=ret+"00：";
        }
        if(second > 0) {
            //sb.append(second+":");
            ret = ret+second+"";
        }else{
            ret = ret+"00";
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(10);
    }

    public void tiaozhuan(String name, String price, String rank, String lingyu,String conect,String tel,
                          String datetime, String meetadress, String youhuiprice, String litpic,String typeid,String aid,String id,double x,double y){
        Intent intent = new Intent(this, PayConfirmActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("rank", rank);
        intent.putExtra("lingyu", lingyu);
        intent.putExtra("conect", conect);
        intent.putExtra("tel", tel);
        intent.putExtra("datetime", datetime);
        intent.putExtra("meetadress", meetadress);
        intent.putExtra("youhuiprice", youhuiprice);
        intent.putExtra("litpic", litpic);
        intent.putExtra("typeid", typeid);
        intent.putExtra("aid", aid);
        intent.putExtra("id", id);
        intent.putExtra("y", y);
        intent.putExtra("x", x);
        startActivity(intent);
    }

    /**
     * 同意或者拒绝的接口
     **/
    public void AgreeOrCenel(final String id,final String userfk,String aid, String rid,String typeid){
        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("method", "feedback");
        map.put("userfk",userfk );
        map.put("aid",aid );
        map.put("rid",rid );
        map.put("typeid",typeid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php?id="+id1+"&method=feedback&userfk="+user;
//                        str = OkHttpUtils.loaudstringfromurl(url);
//                        if(str != null){
//                            Message msg = Message.obtain();
//                            msg.what = 2;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                }
//        ).start();
    }
}
