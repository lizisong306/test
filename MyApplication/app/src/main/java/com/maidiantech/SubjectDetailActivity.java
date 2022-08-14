package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.MainActivity;
import com.maidiantech.R;
import com.maidiantech.common.resquest.NetworkCom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import entity.PatentRecord;
import entity.SubjectDetailEntity;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/7/3.
 */

public class SubjectDetailActivity extends AutoLayoutActivity {
    RefreshListView listview;
    ImageView back;
    ProgressBar progress;
    View heart;
    TextView bottmon_title,des,leixing,danweizhuti;
    String id;
    SubjectDetailEntity data;
    List<PatentRecord> dataShow = new ArrayList<>();
    SubjectDetailAdapter adapter;
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
        setContentView(R.layout.subjectdetail);
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
//        listview.setBackgroundColor(0xfff6f6f6);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress = (ProgressBar)findViewById(R.id.progress);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);

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
        heart = View.inflate(this, R.layout.subjectdetailheart, null);
        des = heart.findViewById(R.id.des);
        leixing = heart.findViewById(R.id.leixing);
        danweizhuti = heart.findViewById(R.id.danweizhuti);
        id=getIntent().getStringExtra("id");

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

    @Override
    protected void onResume() {
        super.onResume();
        getJson();
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
    public void getJson(){
        dataShow.clear();
        String url="http://www.zhongkechuangxiang.com/webapp/api.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("c","patent");
        map.put("a","detail");
        map.put("id",id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, SubjectDetailEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.result.patent_record != null){
                                des.setText(data.result.title);
                                leixing.setText(data.result.patent_type_name);
                                danweizhuti.setText(data.result.subject_name);
                                if(data.result.subject.equals("1")){
                                    setDrawableLeft(danweizhuti, R.mipmap.qiye);
                                }else if(data.result.subject.equals("2")){
                                    setDrawableLeft(danweizhuti, R.mipmap.geren);
                                }
                               for(int i=0; i<data.result.patent_record.size();i++){
                                   PatentRecord item = data.result.patent_record.get(i);
                                   dataShow.add(item);
                               }
                            }

                            if(adapter == null){
                                listview.addHeaderView(heart);
                                adapter = new SubjectDetailAdapter();
                                listview.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }


                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };


    /**
     * 设置textview 的drawable属性
     *
     * @param attention
     * @param drawableId
     */
    private void setDrawableLeft(TextView attention, int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        attention.setCompoundDrawables(drawable, null, null, null);
    }
    class SubjectDetailAdapter extends BaseAdapter{

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
                convertView = View.inflate(SubjectDetailActivity.this, R.layout.subjectdetailadapter, null);
                holdView.neirong = (LinearLayout)convertView.findViewById(R.id.neirong);
                holdView.neirongcontent = (TextView)convertView.findViewById(R.id.neirongcontent);
                holdView.neirongtitle = (TextView)convertView.findViewById(R.id.neirongtitle);
                holdView.neirongtime = (TextView)convertView.findViewById(R.id.neirongtime);

                holdView.shouquan = (LinearLayout)convertView.findViewById(R.id.shouquan);
                holdView.shouquantime = (TextView)convertView.findViewById(R.id.shouquantime);
                holdView.shouquanchenggong = (TextView)convertView.findViewById(R.id.shouquanchenggong);
                holdView.shengbao = (TextView)convertView.findViewById(R.id.shengbao);
                holdView.shengbaocailiao = (TextView)convertView.findViewById(R.id.shengbaocailiao);

                holdView.pay = (LinearLayout)convertView.findViewById(R.id.pay);
                holdView.paytime = (TextView)convertView.findViewById(R.id.paytime);
                holdView.jiaoguanfei = (TextView)convertView.findViewById(R.id.jiaoguanfei);
                holdView.jiaoguanfeizhekou = (TextView)convertView.findViewById(R.id.jiaoguanfeizhekou);
                holdView.pay_confirm = (TextView)convertView.findViewById(R.id.pay_confirm);
                convertView.setTag(holdView);

            }else {
                holdView = (HoldView)convertView.getTag();
            }
           final  PatentRecord item = dataShow.get(position);
            if(item.status_value.equals("5")){
                holdView.shouquan.setVisibility(View.VISIBLE);
                holdView.neirong.setVisibility(View.GONE);
                holdView.pay.setVisibility(View.GONE);
                holdView.shouquantime.setText(item.timeaxis);
                holdView.shengbao.setText(item.remark);
                holdView.shouquanchenggong.setText(item.status_name);
                String [] dis = item.filepath.split("/");
                if(dis != null){
                  final  String title =dis[dis.length-1];
                    holdView.shengbaocailiao.setText(title);
                    holdView.shengbaocailiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SubjectDetailActivity.this, PDFDemo.class);
                            intent.putExtra("url", item.filepath);
                            intent.putExtra("title", title);
                            startActivity(intent);
                        }
                    });
                }

            }else if(item.status_value.equals("10")){
                holdView.shouquan.setVisibility(View.GONE);
                holdView.neirong.setVisibility(View.GONE);
                holdView.pay.setVisibility(View.VISIBLE);
                holdView.paytime.setText(item.timeaxis);
                holdView.jiaoguanfei.setText(item.status_name);
                holdView.jiaoguanfeizhekou.setText(item.remark);
                holdView.pay_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         Intent intent = new Intent(SubjectDetailActivity.this, PaySDkCallActivity.class);
                         intent.putExtra("type", 3);
                         intent.putExtra("record_id",item.record_id);
                         startActivity(intent);

                    }
                });

            }else{
                holdView.shouquan.setVisibility(View.GONE);
                holdView.neirong.setVisibility(View.VISIBLE);
                holdView.pay.setVisibility(View.GONE);
                holdView.neirongtime.setText(item.timeaxis);
                holdView.neirongtitle.setText(item.status_name);
                holdView.neirongcontent.setText(item.remark);

            }

            return convertView;
        }
         class HoldView{
            public LinearLayout neirong;
            public TextView neirongtime,neirongtitle,neirongcontent;

            public LinearLayout shouquan;
            public TextView shouquantime,shouquanchenggong,shengbao,shengbaocailiao;

            public LinearLayout pay;
            public TextView paytime,jiaoguanfei,jiaoguanfeizhekou,pay_confirm;

         }
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
