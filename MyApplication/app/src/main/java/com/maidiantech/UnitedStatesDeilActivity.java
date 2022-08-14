package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import Util.OkHttpUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.KeyWord;
import entity.Posts;
import entity.ShowUnitedDeilData;
import entity.UnitedStatesData;
import entity.UnitedStatesDeilEntry;
import entity.UnitedStatesEntity;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.NetUtils;
/**
 * Created by lizisong on 2018/1/11.
 */

public class UnitedStatesDeilActivity extends AutoLayoutActivity {
    RefreshListView listview;
    ProgressBar progress;
    View heart;
    ImageView banner,back,share,yujian_backs;
    TextView title,description,jigoudescription,jigoutype;
    String json;
    UnitedStatesDeilEntry data;
    UnitedStatesAdapter adapter = new UnitedStatesAdapter();
    private DisplayImageOptions options;
    List<ShowUnitedDeilData> showUnitedDeilDataList = new ArrayList<>();
    String aid;
    private TextView bottmon_title,titlecontent;
    LinearLayout title_layou;
    List<KeyWord> keyWords = new ArrayList<>();
    SystemBarTintManager tintManager ;
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
        setContentView(R.layout.unitedstatesdeilactivity);
        aid=getIntent().getStringExtra("aid");

        options = ImageLoaderUtils.initOptions();
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        tintManager = new SystemBarTintManager(this);
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
//        hideStatusNavigationBar();//1
        MIUISetStatusBarLightMode(getWindow(), true);

        listview = (RefreshListView)findViewById(R.id.listview);

        progress = (ProgressBar)findViewById(R.id.progress);
        bottmon_title = (TextView) findViewById(R.id.bottmon_title);
        title_layou = (LinearLayout)findViewById(R.id.title);
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress.setVisibility(View.VISIBLE);
        heart = View.inflate(this, R.layout.unitedstatesdeilheart,null);
        listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    back.setVisibility(View.GONE);
                }else if(scrollState == SCROLL_STATE_IDLE){
                    back.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离

                    if(h >(290-25)){
                        title_layou.setVisibility(View.VISIBLE);
                        int cha = h-(290-25);
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            title_layou.setAlpha(alpha);
                        }else{
                            title_layou.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }

                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        title_layou.setVisibility(View.GONE);
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
                    return 400;
                }

//                    return 0;
            }


            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        initHeart();
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
        try {
           int netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                progress.setVisibility(View.GONE);
            } else {
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

    private void initHeart(){
        banner = (ImageView) heart.findViewById(R.id.banner);
        back = (ImageView)heart.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share = (ImageView)heart.findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        title = (TextView)heart.findViewById(R.id.title);
        description = (TextView)heart.findViewById(R.id.description);
        jigoudescription = (TextView)heart.findViewById(R.id.jigoudescription);
        jigoutype = (TextView)heart.findViewById(R.id.jigoutype);
        jigoudescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnitedStatesDeilActivity.this, JigouXiangQing.class);
                intent.putExtra("aid", aid);
                startActivity(intent);
            }
        });
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://"+MyApplication.ip+"/api/arc_detail_laboratory.php?aid="+aid+"&typeid=8&version="+MyApplication.version+"&accessid="+MyApplication.deviceid;
                Log.d("lizisong", "url:"+url);
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what =1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                progress.setVisibility(View.GONE);
                Gson gson = new Gson();
                data = gson.fromJson(json, UnitedStatesDeilEntry.class);
                try {
                    if(data.code.equals("1")){
                        ImageLoader.getInstance().displayImage(data.data.litpic,banner
                                ,options);
                        title.setText(data.data.title);
                        titlecontent.setText(data.data.title);
                        String [] temp = data.data.description.replaceAll("<br />","\n").split("\n");
                        String des="";
                        if(temp != null){
                            for(int i=0; i<temp.length;i++){
                                String tem = temp[i];
                                des = des+"    "+tem+"\n";
                            }
                        }
                        description.setText(des);
                        jigoutype.setText(data.data.typename);
                        ShowUnitedDeilData title = new ShowUnitedDeilData();
                        title.type = 0;
                        title.title= "机构专家";
                        title.typeid="4";
                        title.aid =aid;

                        if(data.data.keywords != null){
                            if(data.data.keywords.size() >0){
                                SpannableStringBuilder styledText = new SpannableStringBuilder(des);
                                for(int i=0; i<data.data.keywords.size();i++){
                                    KeyWord item = data.data.keywords.get(i);
                                    setSpecifiedTextsColor(description,des,item,styledText);
                                    description.setText(styledText);
                                }
                            }else{
                                description.setText(des);
                            }
                        }else{
                                description.setText(des);
                        }

                        if(data.data.talent != null && data.data.talent.size() > 0){
                            title.isshowmore=true;
                            showUnitedDeilDataList.add(title);
                            for(int i=0;i<data.data.talent.size();i++){
                                Posts item = data.data.talent.get(i);
                                ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                pos.type = 1;
                                pos.title =item.getTitle();
                                pos.aid = item.aid;
                                pos.id = item.getId();
                                pos.unit = item.unit;
                                pos.area_cate = item.getArea_cate();
                                pos.litpic = item.getLitpic();
                                pos.rank = item.ranks;
                                showUnitedDeilDataList.add(pos);

                                if(i <data.data.talent.size()-1){
                                    ShowUnitedDeilData line = new ShowUnitedDeilData();
                                    line.type = 5;
                                    showUnitedDeilDataList.add(line);
                                }
                            }

                        }else{
//                        ShowUnitedDeilData title = new ShowUnitedDeilData();
//                        title.type = 0;
//                        title.title= "机构专家";
//                        showUnitedDeilDataList.add(title);
                            title.isshowmore=false;
                            showUnitedDeilDataList.add(title);
                            ShowUnitedDeilData nodata = new ShowUnitedDeilData();
                            nodata.type = 3;
                            nodata.title= "nodata";
                            showUnitedDeilDataList.add(nodata);

                        }
                        ShowUnitedDeilData title1 = new ShowUnitedDeilData();
                        title1.type = 0;
                        title1.title= "明星项目";
                        title1.typeid="2";
                        title1.aid =aid;



                        if(data.data.project != null && data.data.project.size() > 0){
                            title1.isshowmore=true;
                            showUnitedDeilDataList.add(title1);
                            for(int i=0;i<data.data.project.size();i++){
                                Posts item = data.data.project.get(i);
                                ShowUnitedDeilData pos = new ShowUnitedDeilData();
                                pos.aid = item.aid;
                                pos.rank = item.getRank();
                                pos.litpic = item.getLitpic();
                                pos.area_cate = item.getArea_cate();
                                pos.typeid = item.typeid;
                                pos.url = item.url;
                                pos.typename=item.getTypename();
                                pos.title = item.getTitle();
                                pos.unit = item.unit;
                                pos.labels = item.labels;
                                pos.type = 2;
                                showUnitedDeilDataList.add(pos);

                                if(i <data.data.project.size()-1){
                                    ShowUnitedDeilData line = new ShowUnitedDeilData();
                                    line.type = 5;
                                    showUnitedDeilDataList.add(line);
                                }

                            }
                        }else{
//                        ShowUnitedDeilData title = new ShowUnitedDeilData();
//                        title.type = 0;
//                        title.title= "明星项目";
//                        showUnitedDeilDataList.add(title);
                            title1.isshowmore=false;
                            showUnitedDeilDataList.add(title1);
                            ShowUnitedDeilData nodata = new ShowUnitedDeilData();
                            nodata.type = 4;
                            nodata.title= "nodata";
                            showUnitedDeilDataList.add(nodata);
                        }
                        ShowUnitedDeilData dixian = new ShowUnitedDeilData();
                        dixian.type = -1;
                        showUnitedDeilDataList.add(dixian);
                        listview.addHeaderView(heart);
                        listview.setAdapter(adapter);

                    }
                }catch (Exception e){

                }



            }else if(msg.what ==1111){
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
    class UnitedStatesAdapter extends BaseAdapter{

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
                convertView = View.inflate(UnitedStatesDeilActivity.this, R.layout.unitedstatesdeilheartadapter,null);
                holdView = new HoldView();
                holdView.title_lay = (RelativeLayout)convertView.findViewById(R.id.title_lay);
                holdView.biaoti = (TextView)convertView.findViewById(R.id.biaoti);
                holdView.xiangmu_lay = (LinearLayout)convertView.findViewById(R.id.xiangmu_lay);
                holdView.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                holdView.more_info = (TextView)convertView.findViewById(R.id.more_info);
                holdView.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
                holdView.jianxi  = (TextView)convertView.findViewById(R.id.jianxi);
                holdView.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);

                holdView.lanyuan1 = (TextView)convertView.findViewById(R.id.lanyuan1);

                holdView.xiangmu_line1 = (TextView)convertView.findViewById(R.id.xiangmu_line1);

                holdView.noxiangmu =  (ImageView)convertView.findViewById(R.id.noxiangmu);

                holdView.rencai_lay = (LinearLayout)convertView.findViewById(R.id.rencai_lay);
                holdView.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);

                holdView.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);

                holdView.rencai_title1 =(TextView)convertView.findViewById(R.id.rencai_title1);

                holdView.rencai_lingyu1 =(TextView)convertView.findViewById(R.id.rencai_lingyu1);

                holdView.rank1 =(TextView)convertView.findViewById(R.id.rank1);
                holdView.source1 = (TextView)convertView.findViewById(R.id.source1);

                holdView.rencai_line1 =(TextView)convertView.findViewById(R.id.rencai_line1);
                holdView.norencai = (ImageView)convertView.findViewById(R.id.norencai);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final ShowUnitedDeilData item =  showUnitedDeilDataList.get(position);
            if(item.type == 0){
                holdView.line.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.VISIBLE);
                holdView.biaoti.setText(item.title);
                holdView.more_info.setVisibility(View.GONE);
                if(item.isshowmore){
                    holdView.more_info.setVisibility(View.VISIBLE);
                    holdView.jianxi.setVisibility(View.VISIBLE);

                }else{
                    holdView.more_info.setVisibility(View.GONE);
                    holdView.jianxi.setVisibility(View.GONE);
                }

                holdView.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(item.typeid.equals("4")){
                            Intent intent = new Intent(UnitedStatesDeilActivity.this, MoreJigouItemActivity.class);
                            intent.putExtra("typeid","4");
                            intent.putExtra("aid",item.aid);
                            intent.putExtra("title","更多专家");
                            intent.putExtra("channel","专家");
                            startActivity(intent);

                        }else if(item.typeid.equals("2")){
                            Intent intent = new Intent(UnitedStatesDeilActivity.this, MoreJigouItemActivity.class);
                            intent.putExtra("typeid","2");
                            intent.putExtra("aid",item.aid);
                            intent.putExtra("title","更多项目");
                            intent.putExtra("channel","项目");
                            startActivity(intent);
                        }
                    }
                });
            }else if(item.type == 3){
                holdView.line.setVisibility(View.GONE);
                holdView.rencai_lay1.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.norencai.setVisibility(View.VISIBLE);
            }else if(item.type == 1){
                holdView.line.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.VISIBLE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.rencai_lay1.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);

                ImageLoader.getInstance().displayImage(item.litpic
                        , holdView.rencai_img1, options);
                holdView.rencai_title1.setText(item.title);
                holdView.rencai_lingyu1.setText(item.unit);
                holdView.rencai_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(UnitedStatesDeilActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "专家");
//                        intent.putExtra("pic", item.litpic);
//                        startActivity(intent);

                        Intent intent = new Intent(UnitedStatesDeilActivity.this, NewRenCaiTail.class);
                        intent.putExtra("aid", item.aid);
                        startActivity(intent);

                    }
                });
                holdView.rank1.setText(item.rank);
            }else if(item.type == 5){
                holdView.line.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.rencai_lay1.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
            }else if(item.type == 2){
                holdView.line.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay1.setVisibility(View.VISIBLE);

                ImageLoader.getInstance().displayImage(item.litpic
                        , holdView.xianmgmu1, options);
                holdView.xmtitle1.setText(item.title);
                holdView.lanyuan1.setText(item.area_cate.getArea_cate1());
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
                holdView.source1.setText(item.labels);
                holdView.xiangmu_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(item.typeid.equals("2") && item.typename.equals("html")){
                            Intent intent=new Intent(UnitedStatesDeilActivity.this, ActiveActivity.class);
                            intent.putExtra("title", item.title);
                            intent.putExtra("url", item.url);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(UnitedStatesDeilActivity.this, NewProjectActivity.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }


//                        Intent intent = new Intent(UnitedStatesDeilActivity.this, DetailsActivity.class);
//                        intent.putExtra("id", item.aid);
//                        intent.putExtra("name", "项目");
//                        intent.putExtra("pic", item.litpic);
//                        startActivity(intent);
                    }
                });

            }else if(item.type == 4){
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.title_lay.setVisibility(View.GONE);
                holdView.xiangmu_lay1.setVisibility(View.GONE);
                holdView.noxiangmu.setVisibility(View.VISIBLE);
            }else if(item.type == -1){
                holdView.line.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.rencai_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
                holdView.title_lay.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HoldView {
            //标题
             public RelativeLayout title_lay;
             public TextView biaoti;
             public TextView more_info;
             public TextView jianxi;

            //项目
            public LinearLayout xiangmu_lay;
            public RelativeLayout xiangmu_lay1;
            public ImageView xianmgmu1;
            public TextView xmtitle1;
            public TextView lanyuan1;
            public TextView rencai_line1;
            public ImageView norencai;
            public TextView source1;

            //人才
            public LinearLayout rencai_lay;
            public RelativeLayout rencai_lay1;
            public RoundImageView rencai_img1;
            public TextView rencai_title1;
            public TextView rencai_lingyu1;
            public TextView rank1;
            public TextView xiangmu_line1;
            public ImageView noxiangmu;
            //线
            public TextView line;
            //底线
            public RelativeLayout dixian;

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(1111);
//        MobclickAgent.onPageEnd("MainActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
    }
    public void setHeartHeight(int height){

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
                Intent intent = new Intent(UnitedStatesDeilActivity.this, NewSearchContent.class);
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
                    Intent intent=new Intent(UnitedStatesDeilActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(UnitedStatesDeilActivity.this, NewRenCaiTail.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(UnitedStatesDeilActivity.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(UnitedStatesDeilActivity.this, NewSearchContent.class);
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
