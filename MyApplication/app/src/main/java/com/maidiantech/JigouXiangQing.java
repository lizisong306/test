package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.KeyWord;
import entity.LabBottom;
import entity.LabMiddle;
import entity.Posts;
import entity.ShowUnitedDeilData;
import entity.ShowUnitedStatesData;
import entity.UnitedStatesData;
import entity.UnitedStatesDeilEntry;
import entity.UnitedStatesEntity;
import view.MyAutoTextView;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.OkHttpUtils;
import Util.NetUtils;
/**
 * Created by lizisong on 2018/1/16.
 */

public class JigouXiangQing extends AutoLayoutActivity {
    private DisplayImageOptions options;
    LinearLayout statelan;
    ImageView back;
    RelativeLayout item1,item2,item3,item4,item5,item6,item7 ;
    TextView state1,state2,state3,state4,state5,state6,state7;
    TextView state1_line,state2_line,state3_line,state4_line,state5_line,state6_line,state7_line;

    TextView title,bottmon_title,title_show,description,jigoutype;
    RefreshListView listview;
    ProgressBar progress;
    View heart;
    ImageView banner,back_heart,share;
    String json;
    UnitedStatesDeilEntry data;
    List<ShowUnitedDeilData> showUnitedDeilDataList = new ArrayList<>();
    List<String> heartData = new ArrayList<>();
    List<Integer> heartIndex = new ArrayList<>();
    JigouXiangAdapter adapter = new JigouXiangAdapter();
    String aid ="";
    public List<KeyWord> keywords = new ArrayList<>();
    int lastVisibleItemPosition,firstindex=-1;

    String firstItem;
    String upkey;
    String currentkey;
    boolean isUp = false;
    boolean isSocll = false;

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
        setContentView(R.layout.jigouxiangqing);
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
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        if (MainActivity.hasSoftKeys(getWindowManager())) {
            bottmon_title.setVisibility(View.VISIBLE);
        } else {
            bottmon_title.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bottmon_title.setVisibility(View.GONE);
        }
        if(MyApplication.navigationbar >0){
            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
            params.height=MyApplication.navigationbar;
            bottmon_title.setLayoutParams(params);
        }
        heart = View.inflate(this, R.layout.jigouxiangqingheart,null);
        statelan = (LinearLayout)findViewById(R.id.statelan);
        statelan.setVisibility(View.GONE);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title =(TextView)findViewById(R.id.title);
        listview = (RefreshListView)findViewById(R.id.listview);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
               if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                   isSocll = false;
                   back_heart.setVisibility(View.VISIBLE);
               }else if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                   back_heart.setVisibility(View.GONE);
               }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
              ShowUnitedDeilData item = null;
              if(!isSocll){

                try {
                    if(showUnitedDeilDataList.size() >0){
                        if (firstVisibleItem > lastVisibleItemPosition) {
                            isUp=true;
                        }
                        if(firstVisibleItem<lastVisibleItemPosition){
                            isUp=false;
                        }
                        if(isUp){
                            item = showUnitedDeilDataList.get(firstVisibleItem-1);
                        }else{
                            item = showUnitedDeilDataList.get(firstVisibleItem);
                        }
                        if(item.type ==3 || item.type == 5){
                            statelan.setVisibility(View.VISIBLE);
                            currentkey=item.names;
                            upkey = getUpKey(currentkey);
                            if(isUp){
                                changeTitle(item.names);
                            }else{
                                changeTitle(upkey);
                            }
                        }
//                            else{
//                                if(!isUp){
//                                    changeTitle(upkey);
//                                }
//                            }

                           if(item.type == 2){
                              statelan.setVisibility(View.GONE);
                              changeTitle(firstItem);
                            }
                          if(firstVisibleItem == 0){
                              statelan.setVisibility(View.GONE);
                              changeTitle(firstItem);
                          }

                    }

                }catch (Exception e){

                }
                if (firstVisibleItem == lastVisibleItemPosition) {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;


            }
            }
        });
        progress =(ProgressBar)findViewById(R.id.progress);
        item1 =(RelativeLayout)findViewById(R.id.item1);
        item2 = (RelativeLayout)findViewById(R.id.item2);
        item3 = (RelativeLayout)findViewById(R.id.item3);
        item4 = (RelativeLayout)findViewById(R.id.item4);
        item5 = (RelativeLayout)findViewById(R.id.item5);
        item6 = (RelativeLayout)findViewById(R.id.item6);
        item7 = (RelativeLayout)findViewById(R.id.item7);
        state1 = (TextView)findViewById(R.id.state1);
        state2 = (TextView)findViewById(R.id.state2);
        state3 = (TextView)findViewById(R.id.state3);
        state4 = (TextView)findViewById(R.id.state4);
        state5 = (TextView)findViewById(R.id.state5);
        state6 = (TextView)findViewById(R.id.state6);
        state7 = (TextView)findViewById(R.id.state7);
        state1_line =(TextView)findViewById(R.id.state1_line);
        state2_line =(TextView)findViewById(R.id.state2_line);
        state3_line =(TextView)findViewById(R.id.state3_line);
        state4_line =(TextView)findViewById(R.id.state4_line);
        state5_line =(TextView)findViewById(R.id.state5_line);
        state6_line =(TextView)findViewById(R.id.state6_line);
        state7_line =(TextView)findViewById(R.id.state7_line);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.VISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
               String txt = state1.getText().toString();
               int current =0;
               for(int i=0; i<showUnitedDeilDataList.size(); i++){
                   ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                   if(item.names != null && !item.names.equals("")){
                       if(item.names.equals(txt)){
                           current = i;
                           break;
                       }
                   }
               }
                listview.setSelection(current+1);

            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.VISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                String txt = state2.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.VISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                String txt = state3.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);

            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.VISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                String txt = state4.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);


            }
        });
        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.VISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                String txt = state5.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);
            }
        });

        item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.VISIBLE);
                state7_line.setVisibility(View.INVISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                String txt = state6.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);
            }
        });

        item7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSocll = true;
                state1_line.setVisibility(View.INVISIBLE);
                state2_line.setVisibility(View.INVISIBLE);
                state3_line.setVisibility(View.INVISIBLE);
                state4_line.setVisibility(View.INVISIBLE);
                state5_line.setVisibility(View.INVISIBLE);
                state6_line.setVisibility(View.INVISIBLE);
                state7_line.setVisibility(View.VISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                String txt = state7.getText().toString();
                int current =0;
                for(int i=0; i<showUnitedDeilDataList.size(); i++){
                    ShowUnitedDeilData item = showUnitedDeilDataList.get(i);
                    if(item.names != null && !item.names.equals("")){
                        if(item.names.equals(txt)){
                            current = i;
                            break;
                        }
                    }
                }
                listview.setSelection(current+1);

            }
        });


        banner = (ImageView) heart.findViewById(R.id.banner);
        back_heart = (ImageView)heart.findViewById(R.id.back_heart);
        back_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share =(ImageView)heart.findViewById(R.id.share);
        title_show = (TextView)heart.findViewById(R.id.title_show);
        description = (TextView)heart.findViewById(R.id.description);
        jigoutype = (TextView)heart.findViewById(R.id.jigoutype);
        aid = getIntent().getStringExtra("aid");
        try {
            int netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                progress.setVisibility(View.GONE);
            } else {
                progress.setVisibility(View.VISIBLE);
                getJson();
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Message obs = Message.obtain();
        obs.what = 1111;
        handler.handleMessage(obs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(1111);
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

    private void getJson(){
        HashMap<String,String> map = new HashMap<>();
        String url = "http://"+MyApplication.ip+"/api/arc_detail_laboratory.php";
        map.put("aid",aid);
        map.put("typeid","8");

        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = "http://"+MyApplication.ip+"/api/arc_detail_laboratory.php?aid="+aid+"&typeid=8&version="+MyApplication.version+"&accessid="+MyApplication.deviceid;
//                json = OkHttpUtils.loaudstringfromurl(url);
//                if(json != null){
//                    Message msg = Message.obtain();
//                    msg.what = 1;
//                    handler.sendMessage(msg);
//                }
//
//            }
//        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    Gson gson = new Gson();
                    json = (String )msg.obj;
                    data = gson.fromJson(json, UnitedStatesDeilEntry.class);
                    progress.setVisibility(View.GONE);
                    if(data != null){
                        if(data.code.equals("1")){
                           if(data.data != null){
                               ImageLoader.getInstance().displayImage(data.data.litpic,banner
                                       ,options);
                               title.setText(data.data.title);
                               title_show.setText(data.data.title);
                               String [] temp = data.data.description.replaceAll("<br />","\n").split("\n");
                               String des="";
                               if(temp != null){
                                   for(int i=0; i<temp.length;i++){
                                       String tem = temp[i];
                                       des = des+"    "+tem+"\n";
                                   }
                               }
                               if(data.data.keywords != null){
                                   if(data.data.keywords.size() >0){
                                       for(int i=0; i<data.data.keywords.size();i++){
                                           KeyWord item = data.data.keywords.get(i);
                                           keywords.add(item);
                                           SpannableStringBuilder styledText = new SpannableStringBuilder(des);
                                           setSpecifiedTextsColor(description,des,item,styledText);
                                           description.setText(styledText);
                                       }
                                   }else{
                                       description.setText(des);
                                   }
                               }else{
                                   description.setText(des);
                               }


                               jigoutype.setText(data.data.typename);
                               if(data.data.lab_middle != null && data.data.lab_middle.size() > 0){
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   showUnitedDeilDataList.add(jiange);
                                   for(int i=0; i < data.data.lab_middle.size();i++){
                                       LabMiddle item =data.data.lab_middle.get(i);
                                       ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                       pos.type =2;
                                       pos.name = item.name;
                                       pos.content = item.content;
                                       showUnitedDeilDataList.add(pos);
                                       if(i<data.data.lab_middle.size()-1){
                                           ShowUnitedDeilData line = new ShowUnitedDeilData();
                                           line.type = 8;
                                           showUnitedDeilDataList.add(line);
                                       }
                                   }
                               }
                               if(data.data.lab_bottom != null && data.data.lab_bottom.size() > 0){
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   showUnitedDeilDataList.add(jiange);
                                   for(int i=0; i<data.data.lab_bottom.size();i++){
                                       LabBottom item = data.data.lab_bottom.get(i);
                                       ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                       pos.type=3;
                                       pos.name=item.name;
                                       pos.names = item.names;
                                       heartData.add(item.names);
                                       heartIndex.add(heartData.size());
                                       showUnitedDeilDataList.add(pos);
                                       ShowUnitedDeilData content = new ShowUnitedDeilData();
                                       content.type = 5;
                                       content.content = item.content;
                                       content.names=item.names;
                                       showUnitedDeilDataList.add(content);
                                       if(i<data.data.lab_bottom.size()-1){
                                           ShowUnitedDeilData jiange1 = new ShowUnitedDeilData();
                                           jiange1.type=1;//间隔
                                           showUnitedDeilDataList.add(jiange1);
                                       }
                                   }
                               }
                               firstItem = heartData.get(0);
                               initheart();
                               if(data.data.talent != null && data.data.talent.size()>0){
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   showUnitedDeilDataList.add(jiange);
                                   ShowUnitedDeilData title = new ShowUnitedDeilData();
                                   title.type = 6;
                                   title.aid=aid;
                                   title.isshowmore=true;
                                   title.typeid="4";
                                   title.title= "机构专家";
                                   showUnitedDeilDataList.add(title);
                                   for(int i=0;i<data.data.talent.size();i++){
                                       Posts item = data.data.talent.get(i);
                                       ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                       pos.type = 7;
                                       pos.title =item.getTitle();
                                       pos.aid = item.aid;
                                       pos.id = item.getId();
                                       pos.unit=item.unit;
                                       pos.area_cate = item.getArea_cate();
                                       pos.litpic = item.getLitpic();
                                       pos.typeid = item.typeid;
                                       pos.url = item.url;
                                       pos.typename=item.getTypename();
                                       pos.rank = item.ranks;
                                       showUnitedDeilDataList.add(pos);

                                       if(i <data.data.talent.size()-1){
                                           ShowUnitedDeilData line = new ShowUnitedDeilData();
                                           line.type = 8;
                                           showUnitedDeilDataList.add(line);
                                       }
                                   }
                               }else {
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   jiange.isshow = true;
                                   showUnitedDeilDataList.add(jiange);
                                   ShowUnitedDeilData title = new ShowUnitedDeilData();
                                   title.type = 6;
                                   title.aid=aid;
                                   title.isshowmore=false;
                                   title.typeid="4";
                                   title.title= "机构专家";
                                   showUnitedDeilDataList.add(title);

                                   ShowUnitedDeilData nodata = new ShowUnitedDeilData();
                                   nodata.type = 9;
                                   nodata.title= "nodata";
                                   showUnitedDeilDataList.add(nodata);

                               }

                               if(data.data.project != null && data.data.project.size() > 0){
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   showUnitedDeilDataList.add(jiange);
                                   ShowUnitedDeilData title = new ShowUnitedDeilData();
                                   title.type = 6;
                                   title.aid=aid;
                                   title.typeid="2";
                                   title.isshowmore=true;
                                   title.title= "明星项目";
                                   showUnitedDeilDataList.add(title);
                                   for(int i=0;i<data.data.project.size();i++){
                                       Posts item = data.data.project.get(i);
                                       ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                       pos.aid = item.aid;
                                       pos.rank = item.getRank();
                                       pos.litpic = item.getLitpic();
                                       pos.area_cate = item.getArea_cate();
                                       pos.title = item.getTitle();
                                       pos.type = 10;
                                       showUnitedDeilDataList.add(pos);

                                       if(i <data.data.project.size()-1){
                                           ShowUnitedDeilData line = new ShowUnitedDeilData();
                                           line.type = 8;
                                           showUnitedDeilDataList.add(line);
                                       }
                                   }

                               }else{
                                   ShowUnitedDeilData jiange = new ShowUnitedDeilData();
                                   jiange.type=1;//间隔
                                   jiange.isshow = true;
                                   showUnitedDeilDataList.add(jiange);
                                   ShowUnitedDeilData title = new ShowUnitedDeilData();
                                   title.type = 6;
                                   title.isshowmore=false;
                                   title.aid=aid;
                                   title.typeid="2";
                                   title.title= "明星项目";
                                   showUnitedDeilDataList.add(title);
                                   ShowUnitedDeilData nodata = new ShowUnitedDeilData();
                                   nodata.type = 11;
                                   nodata.title= "nodata";
                                   showUnitedDeilDataList.add(nodata);
                               }

                           }
                            ShowUnitedDeilData dixian = new ShowUnitedDeilData();
                            dixian.type = -1;
                            showUnitedDeilDataList.add(dixian);
                            listview.addHeaderView(heart);
                            listview.setAdapter(adapter);
                        }

                    }
                }catch (Exception e){

                }
            }
            if(msg.what == 1111){
                if (MainActivity.hasSoftKeys(getWindowManager())) {
                    bottmon_title.setVisibility(View.VISIBLE);
                } else {
                    bottmon_title.setVisibility(View.GONE);
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bottmon_title.setVisibility(View.GONE);
                }

                Message msg1 = Message.obtain();
                msg1.what = 1111;
                handler.sendMessageDelayed(msg1, 1000);
            }
        }
    };
    class JigouXiangAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showUnitedDeilDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return showUnitedDeilDataList.get(position);
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
                convertView = View.inflate(JigouXiangQing.this, R.layout.jigouxiangqingadapter, null);
                holdView.biaoti = (TextView) convertView.findViewById(R.id.biaoti);
                holdView.biaoti1 = (TextView)convertView.findViewById(R.id.biaoti1);
                holdView.biaoti_lay= (RelativeLayout)convertView.findViewById(R.id.biaoti_lay);
                holdView.jiange = (LinearLayout)convertView.findViewById(R.id.jiange);
                holdView.jianjie = (LinearLayout)convertView.findViewById(R.id.jianjie);
                holdView.name = (TextView)convertView.findViewById(R.id.name);
                holdView.more_info = (TextView)convertView.findViewById(R.id.more_info);
                holdView.value = (TextView)convertView.findViewById(R.id.value);
                holdView.line = (LinearLayout)convertView.findViewById(R.id.line);
                holdView.content_lay = (LinearLayout)convertView.findViewById(R.id.content_lay);
                holdView.content = (TextView)convertView.findViewById(R.id.content);
                holdView.title_lay= (RelativeLayout)convertView.findViewById(R.id.title_lay);
                holdView.biaoti_lay1= (RelativeLayout)convertView.findViewById(R.id.biaoti_lay1);
                holdView.biaoti1=(TextView)convertView.findViewById(R.id.biaoti1);
                holdView.xiangmu_lay = (LinearLayout)convertView.findViewById(R.id.xiangmu_lay);
                holdView.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                holdView.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
                holdView.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);
                holdView.lanyuan1 = (TextView)convertView.findViewById(R.id.lanyuan1);
                holdView.noxiangmu = (ImageView)convertView.findViewById(R.id.noxiangmu);
                holdView.rencai_lay = (LinearLayout)convertView.findViewById(R.id.rencai_lay);
                holdView.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);
                holdView.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);
                holdView.rencai_title1 =(TextView)convertView.findViewById(R.id.rencai_title1);
                holdView.rencai_lingyu1 = (TextView)convertView.findViewById(R.id.rencai_lingyu1);
                holdView.rank1 = (TextView)convertView.findViewById(R.id.rank1);
                holdView.norencai = (ImageView)convertView.findViewById(R.id.norencai);
                holdView.hui = (TextView) convertView.findViewById(R.id.hui);
                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);
                holdView.source1 = (TextView)convertView.findViewById(R.id.source1);
                convertView.setTag(holdView);

            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final ShowUnitedDeilData item = showUnitedDeilDataList.get(position);
            if(item.type == 1){
                holdView.jiange.setVisibility(View.VISIBLE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                if(item.isshow){
                    holdView.hui.setVisibility(View.GONE);
                    holdView.line.setVisibility(View.VISIBLE);
                }else{
                    holdView.hui.setVisibility(View.VISIBLE);
                }


            }else if(item.type ==2){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.VISIBLE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.name.setText(item.name);
                if(keywords.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.content);
                    for(int i=0; i<keywords.size();i++){
                        KeyWord pos = keywords.get(i);
                        setSpecifiedTextsColor( holdView.value,item.content,pos,styledText);
                    }
                    holdView.value.setText(styledText);
                }else{
                    holdView.value.setText(item.content);
                }

            }else if(item.type ==3){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.VISIBLE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.biaoti.setText(item.name);
            }else if(item.type == 5){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);

                String [] temp = item.content.replaceAll("<br />","\n").split("\n");
                String des="";
                if(temp != null){
                    for(int i=0; i<temp.length;i++){
                        String tem = temp[i];
                        des = des+tem+"\n";
                    }
                }

                if(keywords.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(des);
                    for(int i=0; i<keywords.size();i++){
                        KeyWord pos = keywords.get(i);
                        setSpecifiedTextsColor(holdView.content,des,pos,styledText);
                    }
                    holdView.content.setText(styledText);
                }else{
                    holdView.content.setText(des);
                }

            }else if(item.type == 6){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.VISIBLE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.biaoti1.setText(item.title);
                holdView.more_info.setVisibility(View.GONE);

              if(item.isshowmore){
                  holdView.more_info.setVisibility(View.VISIBLE);
              }else{
                  holdView.more_info.setVisibility(View.GONE);
              }
                holdView.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.typeid.equals("4")){
                            Intent intent = new Intent(JigouXiangQing.this, MoreJigouItemActivity.class);
                            intent.putExtra("typeid","4");
                            intent.putExtra("aid",item.aid);
                            intent.putExtra("title","更多专家");
                            intent.putExtra("channel","专家");
                            startActivity(intent);

                        }else if(item.typeid.equals("2")){
                            Intent intent = new Intent(JigouXiangQing.this, MoreJigouItemActivity.class);
                            intent.putExtra("typeid","2");
                            intent.putExtra("aid",item.aid);
                            intent.putExtra("title","更多项目");
                            intent.putExtra("channel","项目");
                            startActivity(intent);
                        }
                    }
                });

            }else if(item.type == 10){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.xiangmu_lay1.setVisibility(View.VISIBLE);
                holdView.noxiangmu.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.litpic
                        , holdView.xianmgmu1, options);
                holdView.xmtitle1.setText(item.title);
                if(item.labels != null && (!item.labels.equals(""))){
                    holdView.source1.setVisibility(View.VISIBLE);
                    holdView.source1.setText(item.labels);
                    if(item.labels.equals("精品项目")){
                        holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }else if(item.labels.equals("钛领推荐")){
                        holdView.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                    }else if(item.labels.equals("国家科学基金")){
                        holdView.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                    }else{
                        holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }
                }else{
                    holdView.source1.setVisibility(View.GONE);
                }
                holdView.lanyuan1.setText(item.unit);
                holdView.xiangmu_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(item.typeid.equals("2") && item.typename.equals("html")){
                            Intent intent=new Intent(JigouXiangQing.this, ActiveActivity.class);
                            intent.putExtra("title", item.title);
                            intent.putExtra("url", item.url);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(JigouXiangQing.this, NewProjectActivity.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }
                    }
                });
            }else if(item.type == 7){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.VISIBLE);
                holdView.rencai_lay1.setVisibility(View.VISIBLE);
                holdView.norencai.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.litpic
                        , holdView.rencai_img1, options);
                holdView.rencai_title1.setText(item.title);
                holdView.rencai_lingyu1.setText(item.unit);
                holdView.rank1.setText(item.rank);
                holdView.rencai_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(JigouXiangQing.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "人才");
//                        intent.putExtra("pic", item.litpic);
//                        startActivity(intent);
                        Intent intent = new Intent(JigouXiangQing.this, NewRenCaiTail.class);
                        intent.putExtra("aid", item.aid);
                        startActivity(intent);
                    }
                });

            }else if(item.type == 8){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.VISIBLE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
            }else if(item.type == 9){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.VISIBLE);
                holdView.rencai_lay1.setVisibility(View.GONE);
                holdView.norencai.setVisibility(View.VISIBLE);
            }else if(item.type == 11){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.xiangmu_lay1.setVisibility(View.GONE);
                holdView.noxiangmu.setVisibility(View.VISIBLE);
                holdView.rencai_lay.setVisibility(View.GONE);
            }else if(item.type == -1){
                holdView.jiange.setVisibility(View.GONE);
                holdView.jianjie.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
                holdView.biaoti_lay.setVisibility(View.GONE);
                holdView.content_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HoldView {
            //间隔
            public LinearLayout jiange;
            //简介
            public LinearLayout jianjie;
            public TextView name,hui;
            public TextView value;
            //线
            public LinearLayout line;
            //内容标题
            public RelativeLayout biaoti_lay;
            public TextView biaoti;
            //内容
            public LinearLayout content_lay;
            public TextView content;
            //标题
            public RelativeLayout title_lay;
            public RelativeLayout biaoti_lay1;
            public TextView biaoti1;
            public TextView more_info;
            //项目
            public LinearLayout xiangmu_lay;
            public RelativeLayout xiangmu_lay1;
            public ImageView xianmgmu1;
            public TextView xmtitle1;
            public TextView lanyuan1;
            public ImageView noxiangmu;

            //人才
            public LinearLayout rencai_lay;
            public RelativeLayout rencai_lay1;
            public RoundImageView rencai_img1;
            public TextView rencai_title1;
            public TextView rencai_lingyu1;
            public TextView rank1;
            public ImageView norencai;
            public TextView source1;
            //底线
            public RelativeLayout dixian;
        }
    }
    private void initheart(){
        for(int i=0; i<heartData.size();i++){
            String txt= heartData.get(i);
            if(i==0){
                item1.setVisibility(View.VISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                state1.setText(txt);
            }else if(i==1){
                item2.setVisibility(View.VISIBLE);
                state2.setText(txt);
            }else if(i ==2){
                item3.setVisibility(View.VISIBLE);
                state3.setText(txt);
            }else if(i ==3){
                item4.setVisibility(View.VISIBLE);
                state4.setText(txt);
            }else if(i ==4){
                item5.setVisibility(View.VISIBLE);
                state5.setText(txt);
            }else if(i ==5){
                item6.setVisibility(View.VISIBLE);
                state6.setText(txt);
            }else if(i ==6){
                item7.setVisibility(View.VISIBLE);
                state7.setText(txt);
            }
        }
    }
    private void changeTitle(String txt){

        if(txt != null && !txt.equals("")){

            state1_line.setVisibility(View.INVISIBLE);
            state2_line.setVisibility(View.INVISIBLE);
            state3_line.setVisibility(View.INVISIBLE);
            state4_line.setVisibility(View.INVISIBLE);
            state5_line.setVisibility(View.INVISIBLE);
            state6_line.setVisibility(View.INVISIBLE);
            state7_line.setVisibility(View.INVISIBLE);
            state1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            state7.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            if(state1.getText().toString().equals(txt)){
                state1_line.setVisibility(View.VISIBLE);
                state1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state2.getText().toString().equals(txt)){
                state2_line.setVisibility(View.VISIBLE);
                state2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state3.getText().toString().equals(txt)){
                state3_line.setVisibility(View.VISIBLE);
                state3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state4.getText().toString().equals(txt)){
                state4_line.setVisibility(View.VISIBLE);
                state4.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state5.getText().toString().equals(txt)){
                state5_line.setVisibility(View.VISIBLE);
                state5.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state6.getText().toString().equals(txt)){
                state6_line.setVisibility(View.VISIBLE);
                state6.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else if(state7.getText().toString().equals(txt)){
                state7_line.setVisibility(View.VISIBLE);
                state7.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }
    }
    private String getUpKey(String key){
        for(int i=0;i<heartData.size();i++){
            String pos = heartData.get(i);
            if(key.equals(pos)){
                if(i>0){
                    return heartData.get(i-1);
                }else {
                    return heartData.get(i);
                }
            }
        }
        return null;
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
                Intent intent = new Intent(JigouXiangQing.this, NewSearchContent.class);
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
                    Intent intent=new Intent(JigouXiangQing.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(JigouXiangQing.this, NewRenCaiTail.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(JigouXiangQing.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(JigouXiangQing.this, NewSearchContent.class);
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

//                    boolean conuite =false;
//                    for(int i =0; i<counts.size();i++){
//                        QuJian qj = counts.get(i);
//                        if(start >= qj.start && start<= qj.end){
//                            conuite = true;
//                            break;
//                        }
//                    }
//                    if(conuite){
//                        continue;
//                    }
//                    QuJian quJian = new QuJian();
//                    quJian.start = start;
                start = start + lengthFront;
                sTextsStartList.add(start);
//                    quJian.end = start;
//                    counts.add(quJian);
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



}
