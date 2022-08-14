package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import Util.TimeUtil;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.KeyWord;
import entity.NewRenCaiDataEntity;
import entity.Posts;
import entity.RetRenCai;
import fragment.RenCaiTailFragment;
import view.NoScrollListview;
import view.RefreshListView1;
import view.RoundImageView;
import Util.SharedPreferencesUtil;
import  Util.NoDoubleClick;
import view.ScrollListView;
import view.ShapeImageView;

/**
 * Created by Administrator on 2019/8/6.
 */

public class NewRenCaiTailL extends AutoLayoutActivity {
    ImageView bjtupian,back,zhuanshi,wenhao,back1,wenhao1,back2,wenhaosanjiao;
    TextView wutu_rencai,name,unit,guanzhustate,guanzhu,fensi,hyinzi,content,title,item1,itemline1,item2,itemline2,item3,itemline3,item4,itemline4,item5,itemline5;
    LinearLayout titlelay,hearttab;
    RoundImageView rencai_img;
    RefreshListView1 listview;
    LinearLayout zaixian,xianxia;
    List<KeyWord> keys = new ArrayList<>();
    View heartView;
    TextView heartitem1,heartitemline1,heartitem2,heartitemline2,heartitem3,heartitemline3,heartitem4,heartitemline4,heartitem5,heartitemline5,wenhaotishi;
    String aid,kewords="";
    String id, titles,is_attention,isExtra,typeid,typename,litpic,litpic_zs,litpic_bg,area_cate,rank,units,fensi_num,dongtai_num,factor,factor_info,description,searchkd;
    TextView guanzhustate1;
    List<NewRenCaiDataEntity> showlist = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<ArrayList<NewRenCaiDataEntity>> countList = new ArrayList<>();
    DisplayImageOptions options;
    ImageView sanjiao;
    TextView quxiaoguanzhu;
    Adapter adapter = null;
    boolean isherat = true;
    ProgressBar progress;
    RelativeLayout dblay1;
    RelativeLayout itemlay1,itemlay2,itemlay3,itemlay4,itemlay5,heart1,heart2,heart3,heart4,heart5;
    private List<RenCaiTailFragment> mFragments = new ArrayList<>();
    RenCaiTailFragment rencaitailfragment;
    FragmentAdapte fragmentadapter = new FragmentAdapte(getSupportFragmentManager());
    Adapter1 adapter1 =null;
    List<Posts> adapterlist= new ArrayList<>();
    NoScrollListview custom1,custom2,custom3,custom4,custom5;
    Adapter dataadapter1,dataadapter2,dataadapter3,dataadapter4,dataadapter5;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.newrencaitaill);
        options = ImageLoaderUtils.initOptions();
        aid = getIntent().getStringExtra("aid");
        bjtupian = (ImageView)findViewById(R.id.bjtupian);
        back = (ImageView)findViewById(R.id.back);
        zhuanshi = (ImageView)findViewById(R.id.zhuanshi);
        wenhao = (ImageView)findViewById(R.id.wenhao);
        back1 = (ImageView)findViewById(R.id.back1);
        wutu_rencai = (TextView)findViewById(R.id.wutu_rencai);
        name = (TextView)findViewById(R.id.name);
        unit = (TextView)findViewById(R.id.unit);
        guanzhustate = (TextView)findViewById(R.id.guanzhustate);
        guanzhu = (TextView)findViewById(R.id.guanzhu);
        fensi = (TextView)findViewById(R.id.fensi);
        hyinzi = (TextView)findViewById(R.id.hyinzi);
        content = (TextView)findViewById(R.id.content);
        progress = (ProgressBar)findViewById(R.id.progress);
        title = (TextView)findViewById(R.id.title);
        item1 = (TextView)findViewById(R.id.item1);
        itemline1 = (TextView)findViewById(R.id.itemline1);
        item2 = (TextView)findViewById(R.id.item2);
        itemlay1 = (RelativeLayout)findViewById(R.id.itemlay1);
        itemlay2 = (RelativeLayout)findViewById(R.id.itemlay2);
        itemlay3 = (RelativeLayout)findViewById(R.id.itemlay3);
        itemlay4 = (RelativeLayout)findViewById(R.id.itemlay4);
        itemlay5 = (RelativeLayout)findViewById(R.id.itemlay5);

        itemline2 = (TextView)findViewById(R.id.itemline2);
        item3 = (TextView)findViewById(R.id.item3);
        itemline3 = (TextView)findViewById(R.id.itemline3);
        item4 = (TextView)findViewById(R.id.item4);
        itemline4 = (TextView)findViewById(R.id.itemline4);
        item5 = (TextView)findViewById(R.id.item5);
        itemline5 = (TextView)findViewById(R.id.itemline5);
        dblay1 = (RelativeLayout)findViewById(R.id.dblay1);
        titlelay = (LinearLayout)findViewById(R.id.titlelay);
        rencai_img = (RoundImageView)findViewById(R.id.rencai_img);
        listview = (RefreshListView1)findViewById(R.id.listview);
        back2 = (ImageView)findViewById(R.id.back2);
        wenhaosanjiao = (ImageView)findViewById(R.id.wenhaosanjiao);
        wenhaotishi = (TextView)findViewById(R.id.wenhaotishi);
        zaixian = (LinearLayout)findViewById(R.id.zaixian);
        xianxia = (LinearLayout)findViewById(R.id.xianxia);
        sanjiao = (ImageView)findViewById(R.id.sanjiao);
        quxiaoguanzhu = (TextView)findViewById(R.id.quxiaoguanzhu);
        guanzhustate1 = (TextView)findViewById(R.id.guanzhustate1);
        wenhao1 = (ImageView)findViewById(R.id.wenhao1);
        heartView = View.inflate(this, R.layout.newrencaitailheartl,null);
        heartitem1 = heartView.findViewById(R.id.heartitem1);
        heartitem2 = heartView.findViewById(R.id.heartitem2);
        heartitem3 = heartView.findViewById(R.id.heartitem3);
        heartitem4 = heartView.findViewById(R.id.heartitem4);
        heartitem5 = heartView.findViewById(R.id.heartitem5);
        heart1 =  heartView.findViewById(R.id.heart1);
        heart2 =  heartView.findViewById(R.id.heart2);
        heart3 =  heartView.findViewById(R.id.heart3);
        heart4 =  heartView.findViewById(R.id.heart4);
        heart5 =  heartView.findViewById(R.id.heart5);
        heartitemline1 = heartView.findViewById(R.id.heartitemline1);
        heartitemline2 = heartView.findViewById(R.id.heartitemline2);
        heartitemline3 = heartView.findViewById(R.id.heartitemline3);
        heartitemline4 = heartView.findViewById(R.id.heartitemline4);
        heartitemline5 = heartView.findViewById(R.id.heartitemline5);
        hearttab = heartView.findViewById(R.id.hearttab);
        guanzhustate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_attention.equals("0")) {
                    String login = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                    if(login.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                        commit(aid, mid, "1",1);
                    }else{
                        Intent intent = new Intent(NewRenCaiTailL.this, MyloginActivity.class);
                        startActivity(intent);
                    }
                }else{
                    sanjiao.setVisibility(View.VISIBLE);
                    quxiaoguanzhu.setVisibility(View.VISIBLE);
                }
            }
        });
        quxiaoguanzhu.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                quxiaoguanzhu.setVisibility(View.GONE);
                sanjiao.setVisibility(View.GONE);
                String login = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(login.equals("1")){
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    commit(aid, mid, "-1",-1);
                }else{
                    Intent intent = new Intent(NewRenCaiTailL.this, MyloginActivity.class);
                    startActivity(intent);
                }

            }
        });
        back.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                finish();
            }
        });
        back2.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                finish();
            }
        });
        back1.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                finish();
            }
        });
        wenhao1.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                wenhaosanjiao.setVisibility(View.VISIBLE);
                wenhaotishi.setVisibility(View.VISIBLE);
            }
        });
//        dblay1.setOnClickListener(new v() {
//            @Override
//            public void Click(View v) {
//
//            }
//        });
//        dblay1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wenhaosanjiao.setVisibility(View.GONE);
//                wenhaotishi.setVisibility(View.GONE);
//            }
//        });


        item1.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.VISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.VISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem1.getPaint();
                tp.setFakeBoldText(true);
                heartitem1.setTextColor(0xff33afb6);
                tp=item1.getPaint();
                tp.setFakeBoldText(true);
                item1.setTextColor(0xff33afb6);
                showlist =  countList.get(0);
                custom1.setVisibility(View.VISIBLE);
//                custom1.setSelector(0);
//                dataadapter1.notifyDataSetChanged();
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom1.setSelection(0);
                dataadapter1.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        item2.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.VISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.VISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem2.getPaint();
                tp.setFakeBoldText(true);
                heartitem2.setTextColor(0xff33afb6);
                tp=item2.getPaint();
                tp.setFakeBoldText(true);
                item2.setTextColor(0xff33afb6);
                showlist =  countList.get(1);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.VISIBLE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom2.setSelection(0);
                dataadapter2.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        item3.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.VISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.VISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem3.getPaint();
                tp.setFakeBoldText(true);
                heartitem3.setTextColor(0xff33afb6);
                tp=item3.getPaint();
                tp.setFakeBoldText(true);
                item3.setTextColor(0xff33afb6);
                showlist =  countList.get(2);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.VISIBLE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom3.setSelection(0);
                dataadapter3.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        item4.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.VISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.VISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem4.getPaint();
                tp.setFakeBoldText(true);
                heartitem4.setTextColor(0xff33afb6);
                tp=item4.getPaint();
                tp.setFakeBoldText(true);
                item4.setTextColor(0xff33afb6);
                showlist =  countList.get(3);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.VISIBLE);
                custom5.setVisibility(View.GONE);
                custom4.setSelection(0);
                dataadapter4.notifyDataSetChanged();
//                custom4.setSelector(0);
//                dataadapter4.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        item5.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.VISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.VISIBLE);
                TextPaint tp = heartitem5.getPaint();
                tp.setFakeBoldText(true);
                heartitem5.setTextColor(0xff33afb6);
                tp=item5.getPaint();
                tp.setFakeBoldText(true);
                item5.setTextColor(0xff33afb6);
                showlist =  countList.get(4);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.VISIBLE);
                custom5.setSelection(0);
                dataadapter5.notifyDataSetChanged();
//                custom5.setSelector(0);
//                dataadapter5.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        heartitem1.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.VISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.VISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem1.getPaint();
                tp.setFakeBoldText(true);
                heartitem1.setTextColor(0xff33afb6);
                tp=item1.getPaint();
                tp.setFakeBoldText(true);
                item1.setTextColor(0xff33afb6);
                showlist =  countList.get(0);
                custom1.setVisibility(View.VISIBLE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom1.setSelection(0);
                dataadapter1.notifyDataSetChanged();
//                custom1.setSelector(0);
//                dataadapter1.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        heartitem2.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.VISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.VISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem2.getPaint();
                tp.setFakeBoldText(true);
                heartitem2.setTextColor(0xff33afb6);
                tp=item2.getPaint();
                tp.setFakeBoldText(true);
                item2.setTextColor(0xff33afb6);
                showlist =  countList.get(1);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.VISIBLE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom2.setSelection(0);
                dataadapter2.notifyDataSetChanged();
//                custom2.setSelector(0);
//                dataadapter2.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        heartitem3.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.VISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.VISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem3.getPaint();
                tp.setFakeBoldText(true);
                heartitem3.setTextColor(0xff33afb6);
                tp=item3.getPaint();
                tp.setFakeBoldText(true);
                item3.setTextColor(0xff33afb6);
                showlist =  countList.get(2);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.VISIBLE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.GONE);
                custom3.setSelection(0);
                dataadapter3.notifyDataSetChanged();
//                custom3.setSelector(0);
//                dataadapter3.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        heartitem4.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.VISIBLE);
                itemline5.setVisibility(View.INVISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.VISIBLE);
                heartitemline5.setVisibility(View.INVISIBLE);
                TextPaint tp = heartitem4.getPaint();
                tp.setFakeBoldText(true);
                heartitem4.setTextColor(0xff33afb6);
                tp=item4.getPaint();
                tp.setFakeBoldText(true);
                item4.setTextColor(0xff33afb6);
                showlist =  countList.get(3);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.VISIBLE);
                custom5.setVisibility(View.GONE);
                custom4.setSelection(0);
                dataadapter4.notifyDataSetChanged();
//                custom4.setSelector(0);
//                dataadapter4.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        heartitem5.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                initline();
                itemline1.setVisibility(View.INVISIBLE);
                itemline2.setVisibility(View.INVISIBLE);
                itemline3.setVisibility(View.INVISIBLE);
                itemline4.setVisibility(View.INVISIBLE);
                itemline5.setVisibility(View.VISIBLE);
                heartitemline1.setVisibility(View.INVISIBLE);
                heartitemline2.setVisibility(View.INVISIBLE);
                heartitemline3.setVisibility(View.INVISIBLE);
                heartitemline4.setVisibility(View.INVISIBLE);
                heartitemline5.setVisibility(View.VISIBLE);
                TextPaint tp = heartitem5.getPaint();
                tp.setFakeBoldText(true);
                heartitem5.setTextColor(0xff33afb6);
                tp=item5.getPaint();
                tp.setFakeBoldText(true);
                item5.setTextColor(0xff33afb6);
                showlist =  countList.get(4);
                custom1.setVisibility(View.GONE);
                custom2.setVisibility(View.GONE);
                custom3.setVisibility(View.GONE);
                custom4.setVisibility(View.GONE);
                custom5.setVisibility(View.VISIBLE);
                custom5.setSelection(0);
                dataadapter5.notifyDataSetChanged();
//                custom5.setSelector(0);
//                dataadapter5.notifyDataSetChanged();
                listview.setSelection(0);
                if(adapter1 != null){
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                wenhaosanjiao.setVisibility(View.GONE);
                wenhaotishi.setVisibility(View.GONE);
                sanjiao.setVisibility(View.GONE);
                quxiaoguanzhu.setVisibility(View.GONE);
                return false;
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                wenhaosanjiao.setVisibility(View.GONE);
                wenhaotishi.setVisibility(View.GONE);
                sanjiao.setVisibility(View.GONE);
                quxiaoguanzhu.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isherat = true;
                    }
                }else{
                    isherat = false;
                }
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
                    Log.d("lizisong","h:"+h);
                    if(h >250){
                        wenhao1.setVisibility(View.INVISIBLE);
                    }else{
                        wenhao1.setVisibility(View.VISIBLE);
                    }
                    if(h>500){
                        guanzhustate1.setVisibility(View.INVISIBLE);
                    }else{
                        guanzhustate1.setVisibility(View.VISIBLE);
                    }
                    if(h >=600){
//                        int cha = h-(530-25);
//                        float alpha = cha/100f;
//                        if(alpha < 1.0f){
//                            titlelay.setAlpha(alpha);
//                        }else{
//                            titlelay.setAlpha(1.0f);
//                        }
                        titlelay.setVisibility(View.VISIBLE);
                        guanzhustate1.setVisibility(View.INVISIBLE);
                        hearttab.setVisibility(View.INVISIBLE);

                    }else {
                        guanzhustate1.setVisibility(View.VISIBLE);
                        titlelay.setVisibility(View.GONE);
                        hearttab.setVisibility(View.VISIBLE);
                    }

                }
            }

            private int getScrollY() {
                try {
                    int height = 0;
                    for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                        ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                        height += itemRecod.height;
                    }
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                    if (null == itemRecod) {
                        itemRecod = new ItemRecod();
                    }
                    return height - itemRecod.top;
                }catch (Exception e){
                    return 600;
                }
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
//        aid = "11390";
        progress.setVisibility(View.VISIBLE);
        zaixian.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                Toast.makeText(NewRenCaiTailL.this, "暂未开通此功能",Toast.LENGTH_SHORT).show();
            }
        });
        xianxia.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(loginState.equals("1")){
                    Intent intent = new Intent(NewRenCaiTailL.this, TianXuQiu.class);
                    intent.putExtra("aid", aid);
                    intent.putExtra("typeid", "4");
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(NewRenCaiTailL.this, MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });
        getJson();
    }

    private void commit(String aid, String mid,String state,int arg){
        String url ="http://www.zhongkechuangxiang.com/api/arc_detail_talent.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method", "guanzhu");
        map.put("aid", aid);
        map.put("mid", mid);
        map.put("state", state);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,arg);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progress.setVisibility(View.GONE);
                    String ret = (String) msg.obj;
//                    Log.d("lizisong", "ret:"+ret);
                    JSONObject jsonObject = new JSONObject(ret);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        String data = jsonObject.getString("data");
                        if(data != null){
                            JSONObject dataObject = new JSONObject(data);
                            JSONObject talent_basic = dataObject.getJSONObject("talent_basic");
                            id = talent_basic.getString("id");
                            titles = talent_basic.getString("title");
                            title.setText(titles);
                            try {
                                isExtra = talent_basic.getString("isExtra");
                            }catch (JSONException a) {

                            }catch (Exception e){

                            }
                            typeid = talent_basic.getString("typeid");
                            typename = talent_basic.getString("typename");
                            litpic = talent_basic.getString("litpic");
                            litpic_zs = talent_basic.getString("litpic_zs");
                            litpic_bg = talent_basic.getString("litpic_bg");
                            area_cate = talent_basic.getString("area_cate");
                            rank = talent_basic.getString("rank");
                            units = talent_basic.getString("unit");
                            fensi_num = talent_basic.getString("fensi_num");
                            dongtai_num = talent_basic.getString("dongtai_num");
                            factor = talent_basic.getString("factor");
                            factor_info = talent_basic.getString("factor_info");
                            description = talent_basic.getString("description");
                            searchkd = talent_basic.getString("searchkd");
                            is_attention = talent_basic.getString("is_attention");

                            try {
                                kewords = dataObject.getString("keywords");
                            } catch (JSONException a) {

                            } catch (Exception e) {

                            }
                            Log.d("lizisong", "is_attention:"+is_attention);
                            if(is_attention.equals("0")){
                                guanzhustate.setText("关注");
                                guanzhustate.setTextColor(0xffffffff);
                                guanzhustate.setBackgroundResource(R.drawable.shap_re);
                            }else if(is_attention.equals("1")){
                                guanzhustate.setText("已关注");
                                guanzhustate.setTextColor(0xffa3a3a3);
                                guanzhustate.setBackgroundResource(R.drawable.shape_ling);
                            }
                            ImageLoader.getInstance().displayImage(litpic_bg
                                    , bjtupian, options);

                            if(kewords != null && !kewords.equals("")){
                                JSONArray words = new JSONArray(kewords);
                                if(words != null){
                                    if(words.length() >0){
                                        for(int i=0; i<words.length();i++){
                                            JSONObject item = words.getJSONObject(i);
                                            KeyWord add = new KeyWord();
                                            add.color = item.getString("color");
                                            add.aid = item.getString("aid");
                                            add.keyword=item.getString("keyword");
                                            add.typeid = item.getString("typeid");
                                            keys.add(add);
                                        }
                                    }
                                }
                            }
                            JSONArray talent_data  =  dataObject.getJSONArray("talent_data");
                            if(talent_data != null){
                                for (int i=0; i<talent_data.length();i++){
                                    JSONObject item = talent_data.getJSONObject(i);
                                    ArrayList<NewRenCaiDataEntity> pos = new ArrayList<>();
                                    String name = item.getString("name");
                                    String cate = item.getString("cate");
                                    JSONArray cells = item.getJSONArray("cells");
                                    if(cells != null){
                                        for(int j=0;j<cells.length();j++){
                                            JSONObject cell = cells.getJSONObject(j);
                                            NewRenCaiDataEntity positem = new NewRenCaiDataEntity();
                                            if(cate.equals("dongtai")){
                                                try {
                                                    positem.createtime = cell.getString("createtime");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    positem.imgurl = cell.getString("imgurl");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    positem.content = cell.getString("content");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                positem.type = "-1";
                                                pos.add(positem);
//                                                    showDatas.put(name, pos);
                                            }else if(cate.equals("keyanlvli") || cate.equals("keyanchengguo")){
                                                try {
                                                    positem.type = cell.getString("type");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    positem.name = cell.getString("name");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    positem.names = cell.getString("names");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }

                                                try {
                                                    if(positem.type.equals("0")){
                                                        pos.add(positem);
                                                        NewRenCaiDataEntity po = new NewRenCaiDataEntity();
                                                        po.content = cell.getString("content");
                                                        po.type="401";
                                                        pos.add(po);
                                                    }else if(positem.type.equals("4")){
                                                        pos.add(positem);
                                                        JSONArray arrystr = cell.getJSONArray("content");
                                                        if(arrystr != null && arrystr.length() > 0){
                                                            for(int z=0;z<arrystr.length();z++){
                                                                NewRenCaiDataEntity posit = new NewRenCaiDataEntity();
                                                                JSONObject temppp = arrystr.getJSONObject(z);
                                                                String catestr = temppp.getString("cate");
                                                                if(catestr.equals("0")){
                                                                    posit.type = "401";
                                                                } else if(catestr.equals("1")){
                                                                    posit.type = "402";
                                                                }
                                                                posit.content = temppp.getString("str");
                                                                pos.add(posit);
                                                            }
                                                        }
                                                    }else if(positem.type.equals("3")){
                                                        pos.add(positem);
                                                        JSONArray object = cell.getJSONArray("content");
                                                        NewRenCaiDataEntity p = new NewRenCaiDataEntity();
                                                        p.type="401";
                                                        for(int z=0;z<object.length();z++){
                                                            String txt = (String) object.get(z);
                                                            p.content = p.content+txt+"\n";
                                                        }
                                                        pos.add(p);
                                                    }
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                NewRenCaiDataEntity line = new NewRenCaiDataEntity();
                                                line.type="-4";
                                                pos.add(line);
//                                                    showDatas.put(name, pos);
                                            }else if(cate.equals("relation_talent") || cate.equals("relation_project")){
                                                if(cate.equals("relation_talent")){
                                                    positem.type ="-2";
                                                }else if(cate.equals("relation_project")){
                                                    positem.type ="-3";
                                                }
                                                Posts posts = new Posts();
                                                try {
                                                    posts.aid =cell.getString("aid");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.areacate = cell.getString("area_cate");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }

                                                try {
                                                    posts.typeid = cell.getString("typeid");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.title = cell.getString("title");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.litpic = cell.getString("litpic");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.ranks = cell.getString("ranks");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.description = cell.getString("description");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }

                                                try {
                                                    posts.str = cell.getString("str");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }

                                                try {
                                                    posts.unit = cell.getString("unit");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                try {
                                                    posts.typename = cell.getString("typename");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }

                                                try {
                                                    posts.labels = cell.getString("labels");
                                                } catch (JSONException a) {

                                                } catch (Exception e) {

                                                }
                                                positem.item = posts;
                                                pos.add(positem);

                                            }
                                        }
                                    }
                                    if(pos.size() <5){
                                        for(int k=0;k<4;k++){
                                            NewRenCaiDataEntity KONG = new NewRenCaiDataEntity();
                                            KONG.type="-5";
                                            pos.add(KONG);
                                        }
                                    }
                                    NewRenCaiDataEntity KONG = new NewRenCaiDataEntity();
                                    KONG.type="-5";
                                    pos.add(KONG);
                                    countList.add(pos);
                                    names.add(name);
//                                    showDatas.put(name, pos);
                                }
                            }

                        }
                        if(litpic != null && !litpic.equals("")){
                            wutu_rencai.setVisibility(View.GONE);
                            rencai_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(litpic
                                    , rencai_img, options);
                        }else{
                            wutu_rencai.setVisibility(View.VISIBLE);
                            rencai_img.setVisibility(View.GONE);
                            wutu_rencai.setText(titles.substring(0,1));
                        }
                        if(litpic_zs != null && !litpic_zs.equals("")){
                            zhuanshi.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(litpic_zs
                                    , zhuanshi, options);
                        }else{
                            zhuanshi.setVisibility(View.GONE);
                        }
                        name.setText(titles);
                        unit.setText(units);
                        content.setText(description);
                        fensi.setText(dongtai_num);
                        guanzhu.setText(fensi_num);
                        hyinzi.setText(factor);
                        wenhaotishi.setText(factor_info);
                        for (int i=0; i<countList.size();i++) {
                            if(i==0){
                                item1.setText(names.get(i));
                                heartitem1.setText(names.get(i));
//                                showlist = countList.get(i);
                                item1.setVisibility(View.VISIBLE);
                                itemline1.setVisibility(View.VISIBLE);
                                heartitem1.setVisibility(View.VISIBLE);
                                heartitemline1.setVisibility(View.VISIBLE);
                                itemlay1.setVisibility(View.VISIBLE);
                                heart1.setVisibility(View.VISIBLE);
                                TextPaint tp = heartitem1.getPaint();
                                tp.setFakeBoldText(true);
                                heartitem1.setTextColor(0xff33afb6);
                                tp=item1.getPaint();
                                tp.setFakeBoldText(true);
                                item1.setTextColor(0xff33afb6);
                                dataadapter1 = new Adapter(countList.get(i));

                            }else if(i == 1){
                                item2.setText(names.get(i));
                                heartitem2.setText(names.get(i));
                                itemlay2.setVisibility(View.VISIBLE);
                                heart2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                heartitem2.setVisibility(View.VISIBLE);
                                dataadapter2 = new Adapter(countList.get(i));
                            }else if(i == 2){
                                item3.setText(names.get(i));
                                heartitem3.setText(names.get(i));
                                item3.setVisibility(View.VISIBLE);
                                itemlay3.setVisibility(View.VISIBLE);
                                heart3.setVisibility(View.VISIBLE);
                                heartitem3.setVisibility(View.VISIBLE);
                                dataadapter3 = new Adapter(countList.get(i));
                            }else if(i == 3){
                                item4.setText(names.get(i));
                                heartitem4.setText(names.get(i));
                                item4.setVisibility(View.VISIBLE);
                                itemlay4.setVisibility(View.VISIBLE);
                                heart4.setVisibility(View.VISIBLE);
                                heartitem4.setVisibility(View.VISIBLE);
                                dataadapter4 = new Adapter(countList.get(i));
                            }else if(i == 4){
                                item5.setText(names.get(i));
                                heartitem5.setText(names.get(i));
                                item5.setVisibility(View.VISIBLE);
                                itemlay5.setVisibility(View.VISIBLE);
                                heart5.setVisibility(View.VISIBLE);
                                heartitem5.setVisibility(View.VISIBLE);
                                dataadapter5 = new Adapter(countList.get(i));
                            }
                        }
                        if(countList.size() ==0){
                            itemlay1.setVisibility(View.VISIBLE);
                            heart1.setVisibility(View.VISIBLE);
                            itemlay2.setVisibility(View.VISIBLE);
                            heart2.setVisibility(View.VISIBLE);
                            itemlay3.setVisibility(View.VISIBLE);
                            heart3.setVisibility(View.VISIBLE);
                            itemlay4.setVisibility(View.VISIBLE);
                            heart4.setVisibility(View.VISIBLE);
                            itemlay5.setVisibility(View.VISIBLE);
                            heart5.setVisibility(View.VISIBLE);
                        }
                        for (int i=0;i<countList.size();i++){
                            ArrayList<NewRenCaiDataEntity> item = countList.get(i);
                            rencaitailfragment = new RenCaiTailFragment(item,(ArrayList<KeyWord>) keys);
                            mFragments.add(rencaitailfragment);
                        }
                        adapterlist.add(new Posts());
                        if(adapter1 == null){
                            adapter1 = new Adapter1();
                            listview.addHeaderView(heartView);
                            listview.setAdapter(adapter1);
                        }else{
                            adapter1.notifyDataSetChanged();
                        }
                    }
                }else if(msg.what == 2){
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    RetRenCai data =gson.fromJson(ret, RetRenCai.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            Toast.makeText(NewRenCaiTailL.this, data.message, Toast.LENGTH_SHORT).show();
                            if(msg.arg1 == 1){
                                guanzhustate.setText("已关注");
                                sanjiao.setVisibility(View.GONE);
                                is_attention="1";
                                guanzhustate.setTextColor(0xffa3a3a3);
                                guanzhustate.setBackgroundResource(R.drawable.shape_ling);
                            }else if(msg.arg1 == -1){
                                is_attention="0";
                                guanzhustate.setText("关注");
                                guanzhustate.setTextColor(0xffffffff);
                                guanzhustate.setBackgroundResource(R.drawable.shap_re);
                            }
                            fensi_num = data.data.fensi_num;
                            guanzhu.setText(fensi_num);
                        }else{
                            Toast.makeText(NewRenCaiTailL.this, data.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };

    private void initline(){
        itemline1.setVisibility(View.INVISIBLE);
        itemline2.setVisibility(View.INVISIBLE);
        itemline3.setVisibility(View.INVISIBLE);
        itemline4.setVisibility(View.INVISIBLE);
        itemline5.setVisibility(View.INVISIBLE);
        heartitemline1.setVisibility(View.INVISIBLE);
        heartitemline2.setVisibility(View.INVISIBLE);
        heartitemline3.setVisibility(View.INVISIBLE);
        heartitemline4.setVisibility(View.INVISIBLE);
        heartitemline5.setVisibility(View.INVISIBLE);
        item1.setTextColor(0xff727272);
        TextPaint tp = item1 .getPaint();
        tp.setFakeBoldText(false);

        item2.setTextColor(0xff727272);
        tp = item2.getPaint();
        tp.setFakeBoldText(false);
        item3.setTextColor(0xff727272);
        tp = item3.getPaint();
        tp.setFakeBoldText(false);
        item4.setTextColor(0xff727272);
        tp = item4.getPaint();
        tp.setFakeBoldText(false);
        item5.setTextColor(0xff727272);
        tp = item5.getPaint();
        tp.setFakeBoldText(false);
        heartitem1.setTextColor(0xff727272);
        tp = heartitem1.getPaint();
        tp.setFakeBoldText(false);
        heartitem2.setTextColor(0xff727272);
        tp = heartitem2.getPaint();
        tp.setFakeBoldText(false);
        heartitem3.setTextColor(0xff727272);
        tp = heartitem3.getPaint();
        tp.setFakeBoldText(false);
        heartitem4.setTextColor(0xff727272);
        tp = heartitem4.getPaint();
        tp.setFakeBoldText(false);
        heartitem5.setTextColor(0xff727272);
        tp = heartitem5.getPaint();
        tp.setFakeBoldText(false);
    }

    private void getJson(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        String url = "http://"+ MyApplication.ip+"/api/arc_detail_talent.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("aid",aid);
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,1);
    }
    class Adapter1 extends BaseAdapter{
        private  boolean isinit = false;

        @Override
        public int getCount() {
            return adapterlist.size();
        }

        @Override
        public Object getItem(int position) {
            return adapterlist.get(position);
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
                convertView = View.inflate(NewRenCaiTailL.this, R.layout.newrencaitailladapter, null);
//                holdView.viewPager = convertView.findViewById(R.id.viewpager);
                holdView.custom1 = convertView.findViewById(R.id.custom1);
                holdView.custom2 = convertView.findViewById(R.id.custom2);
                holdView.custom3 = convertView.findViewById(R.id.custom3);
                holdView.custom4 = convertView.findViewById(R.id.custom4);
                holdView.custom5 = convertView.findViewById(R.id.custom5);
//                holdView.custom1.setScrollEnable(true);
//                holdView.custom2.setScrollEnable(true);
//                holdView.custom3.setScrollEnable(true);
//                holdView.custom4.setScrollEnable(true);
//                holdView.custom5.setScrollEnable(true);
                custom1 = holdView.custom1;
                custom2 = holdView.custom2;
                custom3 = holdView.custom3;
                custom4 = holdView.custom4;
                custom5 = holdView.custom5;
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            if(!isinit){
                isinit= true;
                Log.d("lizisong", "isinit");
//                holdView.viewPager.setAdapter(fragmentadapter);

                if(dataadapter1 != null){
                    holdView.custom1.setAdapter(dataadapter1);
                }
                if(dataadapter2 != null){
                    holdView.custom2.setAdapter(dataadapter2);
                }
                if(dataadapter3 != null){
                    holdView.custom3.setAdapter(dataadapter3);
                }
                if(dataadapter4 != null){
                    holdView.custom4.setAdapter(dataadapter4);
                }
                if(dataadapter5 != null){
                    holdView.custom5.setAdapter(dataadapter5);
                }
            }

            return convertView;
        }
        class HoldView {
            NoScrollListview custom1,custom2,custom3,custom4,custom5;
            ViewPager viewPager;
        }
    }


    class FragmentAdapte extends FragmentStatePagerAdapter {


        public FragmentAdapte(FragmentManager fm) {
            super(fm);
        }

        @Override

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            Log.d("lizisong", "FragmentAdaptegetCount():"+mFragments.size());
            return mFragments.size();
        }
    }




    class Adapter extends BaseAdapter {
        List<NewRenCaiDataEntity> showlist=null;
        public Adapter(List<NewRenCaiDataEntity> list){
            showlist = list;
        }

        @Override
        public int getCount() {
            return showlist.size();
        }

        @Override
        public Object getItem(int position) {
            return showlist.get(position);
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
                convertView = View.inflate(NewRenCaiTailL.this, R.layout.newrencaitailadapterl, null);
                holdView.title = convertView.findViewById(R.id.title);
                holdView.lines = convertView.findViewById(R.id.lines);
                holdView.tuwen = convertView.findViewById(R.id.tuwen);
                holdView.time  = convertView.findViewById(R.id.time);
                holdView.text  = convertView.findViewById(R.id.text);
                holdView.laycontent = convertView.findViewById(R.id.laycontent);
                holdView.imagecontent = convertView.findViewById(R.id.imagecontent);
                holdView.image = convertView.findViewById(R.id.image);
                holdView.xiangmu_lay = convertView.findViewById(R.id.xiangmu_lay);
                holdView.zhuanjia = convertView.findViewById(R.id.zhuanjia);
                holdView.xianmgmu = convertView.findViewById(R.id.xianmgmu);
                holdView.xmtitle = convertView.findViewById(R.id.xmtitle);
                holdView.lanyuan = convertView.findViewById(R.id.lanyuan);
                holdView.resource = convertView.findViewById(R.id.resource);
                holdView.yuyue = convertView.findViewById(R.id.yuyue);
                holdView.source = convertView.findViewById(R.id.source);
                holdView.chenggao = convertView.findViewById(R.id.chenggao);
                holdView.wutu = convertView.findViewById(R.id.wutu);
                holdView.titlelay = convertView.findViewById(R.id.titlelay);
                holdView.kongbai = convertView.findViewById(R.id.kongbai);
                holdView.rencai_title = convertView.findViewById(R.id.rencai_title);
                holdView.rencai_lingyu = convertView.findViewById(R.id.rencai_lingyu);
                holdView.rank = convertView.findViewById(R.id.rank);
                holdView.rencai_img = convertView.findViewById(R.id.rencai_img);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final NewRenCaiDataEntity item = showlist.get(position);
            if(item.type.equals("-4")){
                holdView.lines.setVisibility(View.VISIBLE);
                holdView.title.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
            }else if(item.type.equals("-1")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.VISIBLE);
                holdView.image.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
//                long sys = (System.currentTimeMillis()-Long.parseLong(item.createtime)*1000)/1000;
                String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.createtime)*1000), item.createtime);
                holdView.time.setText(time);
                if(item.content != null && !item.content.equals("")){
                    holdView.text.setVisibility(View.VISIBLE);
                    holdView.text.setText(item.content);
                }else{
                    holdView.text.setVisibility(View.GONE);
                }
                if(item.imgurl != null && !item.imgurl.equals("")){
                    holdView.imagecontent.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.imgurl
                            , holdView.imagecontent, options);
                }else{
                    holdView.imagecontent.setVisibility(View.GONE);
                }

            }else if(item.type.equals("0") || item.type.equals("4") || item.type.equals("3")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.VISIBLE);
                holdView.titlelay.setVisibility(View.VISIBLE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.title.setText(item.name);
            }else if(item.type.equals("401")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.VISIBLE);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
                        setSpecifiedTextsColor(holdView.laycontent,item.content,pos,styledText);
                    }
                    holdView.laycontent.setText(styledText);

                }else{
                    holdView.laycontent.setText(item.content);
                }

            }else if(item.type.equals("402")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.image.setVisibility(View.VISIBLE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.content
                        , holdView.image, options);
            }else if(item.type.equals("-2")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                if(position == 0){
                    holdView.chenggao.setVisibility(View.VISIBLE);
                }else{
                    holdView.chenggao.setVisibility(View.GONE);
                }
                if(item.item.litpic == null || item.item.litpic.equals("")){
                    holdView.rencai_img.setVisibility(View.GONE);
                    holdView.wutu.setVisibility(View.VISIBLE);
                    if(item.item.title != null && !item.item.title.equals("")){
                        String txt = item.item.title.substring(0,1);
                        holdView.wutu.setText(txt);
                    }
                }else{
                    holdView.rencai_img.setVisibility(View.VISIBLE);
                    holdView.wutu.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.item.litpic
                            , holdView.rencai_img, options);
                }
                holdView.rencai_title.setText(item.item.title);
//                                       rencai_title.setTypeface(MyApplication.Medium);
                holdView.rencai_lingyu.setText(item.item.unit);
                holdView.rank.setText(item.item.ranks);
                holdView.zhuanjia.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        Intent intent = new Intent(NewRenCaiTailL.this, NewRenCaiTail.class);
                        intent.putExtra("aid", item.item.aid);
                        startActivity(intent);
                    }
                });
                holdView.resource.setText(item.item.str);
                holdView.yuyue.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(NewRenCaiTailL.this, TianXuQiu.class);
                            intent.putExtra("aid", item.item.aid);
                            intent.putExtra("typeid", "4");
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(NewRenCaiTailL.this, MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }else if(item.type.equals("-3")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                if(item.item.litpic == null || item.item.litpic.equals("")){
                    holdView.xianmgmu.setImageResource(R.mipmap.information_placeholder);
                }else{
                    ImageLoader.getInstance().displayImage(item.item.litpic
                            , holdView.xianmgmu, options);
                }
                holdView.xmtitle.setText(item.item.title);
                holdView.lanyuan.setText(item.item.areacate);
                holdView.source.setText(item.item.labels);
                holdView.xiangmu_lay.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        Intent intent = new Intent(NewRenCaiTailL.this, NewProjectActivity.class);
                        intent.putExtra("aid", item.item.aid);
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("-5")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HoldView{
            TextView title,lines,kongbai,resource,yuyue,chenggao;
            RelativeLayout tuwen;
            TextView time,text,laycontent;
            ShapeImageView imagecontent,image;
            RelativeLayout xiangmu_lay,zhuanjia;
            ShapeImageView xianmgmu;
            TextView xmtitle,lanyuan,source,wutu,rencai_title,rencai_lingyu,rank;
            RoundImageView rencai_img;
            LinearLayout  titlelay;
        }
    }
    public void setSpecifiedTextsColor(TextView txtView, String text, KeyWord specifiedTexts, SpannableStringBuilder styledText){

        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.keyword.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do
        {
            start = temp.indexOf(specifiedTexts.keyword);
            if(start != -1){
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        }while(start != -1);


        for(Integer i : sTextsStartList){
            styledText.setSpan(
                    new ForegroundColorSpan(Color.parseColor(specifiedTexts.color)),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //这个一定要记得设置，不然点击不生效
            txtView.setMovementMethod(LinkMovementMethod.getInstance());
            styledText.setSpan(new TextClick(specifiedTexts),i,i + sTextLength , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }


    class TextClick extends ClickableSpan {
        KeyWord mKeyWord;
        public TextClick(KeyWord item){
            mKeyWord = item;
        }

        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            if(mKeyWord.aid == null || (mKeyWord.aid != null && mKeyWord.aid.equals(""))){
                Intent intent = new Intent(NewRenCaiTailL.this, NewSearchContent.class);
                intent.putExtra("hot",mKeyWord.keyword);
                intent.putExtra("typeid", mKeyWord.typeid);
                if(mKeyWord.typeid.equals("100")){
                    intent.putExtra("typeid", "");
                }else if(mKeyWord.typeid.equals("0")){
                    intent.putExtra("typeid", "");
                }
                startActivity(intent);
            }else {
                if(mKeyWord.typeid.equals("8")){
                    Intent intent=new Intent(NewRenCaiTailL.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(NewRenCaiTailL.this, NewRenCaiTailL.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(NewRenCaiTailL.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewRenCaiTailL.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", mKeyWord.keyword);
                    if(mKeyWord.typeid.equals("100")){
                        intent.putExtra("typeid", "");
                    }else if(mKeyWord.typeid.equals("0")){
                        intent.putExtra("typeid", "");
                    }
                    startActivity(intent);
                }
            }

        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor(mKeyWord.color));
            float size =  ds.getTextSize();
            ds.setTextSize(size);
            ds.setUnderlineText(true);
        }
    }
}
