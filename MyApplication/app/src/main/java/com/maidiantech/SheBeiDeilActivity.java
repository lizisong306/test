package com.maidiantech;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import Util.NetUtils;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.KeyWord;
import entity.NewSheBeiDeilEntry;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/12/14.
 */

public class SheBeiDeilActivity extends AutoLayoutActivity {
    public DisplayImageOptions options;
    public RefreshListView listview;
    LinearLayout title,dangan;
    ImageView yujian_backs,share;
    TextView titlecontent,bottmon_title;
    ProgressBar progress;
    SystemBarTintManager tintManager ;
    RelativeLayout youtupian,wutupian;
    ImageView top_heart,shares1,backs1;
    boolean isState =false;
    boolean isherat = true;
    List<KeyWord> keys = new ArrayList<>();
    List<NewSheBeiDeilEntry> danganka = new ArrayList<>();
    List<NewSheBeiDeilEntry> showList = new ArrayList<>();
    ImageView backs,shares;
    TextView titledes,shebeititle;
    String aid,kewords="";
    LinearLayout item0,item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12,item13,item14,item15,item16,item17,item18,item19;
    TextView name0,name1,name2,name3,name4,name5,name6,name7,name8,name9,name10,name11,name12,name13,name14,name15,name16,name17,name18,name19;
    TextView namecontent0,namecontent1,namecontent2,namecontent3,namecontent4,namecontent5,namecontent6,namecontent7,namecontent8,namecontent9,namecontent10,namecontent11,namecontent12,namecontent13,namecontent14,namecontent15,namecontent16,namecontent17,namecontent18,namecontent19;
    View heart;
    int width;
    SheBeiDeilAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.shebeideildeilactivity);
        tintManager = new SystemBarTintManager(this);
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
//        isShow = getIntent().getBooleanExtra("isShow", false);
        tintManager = new SystemBarTintManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            try {
                setTranslucentStatus(true);
                tintManager = new SystemBarTintManager(this);
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
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);
        options = ImageLoaderUtils.initOptions();


        aid = getIntent().getStringExtra("aid");
        listview = (RefreshListView)findViewById(R.id.listview);
        title = (LinearLayout)findViewById(R.id.title);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        title.setVisibility(View.GONE);
        share = (ImageView)findViewById(R.id.share);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        progress = (ProgressBar)findViewById(R.id.progress);
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
        heart = View.inflate(SheBeiDeilActivity.this,R.layout.shebeideilheart,null);
        youtupian = heart.findViewById(R.id.youtupian);
        wutupian  = heart.findViewById(R.id.wutupian);
        top_heart = heart.findViewById(R.id.top_heart);
        backs1 = heart.findViewById(R.id.backs1);
        shares1 = heart.findViewById(R.id.shares1);
        shebeititle = heart.findViewById(R.id.shebeititle);
        titledes = heart.findViewById(R.id.titledes);
        backs = heart.findViewById(R.id.backs);
        shares = heart.findViewById(R.id.shares);
        init();
        dangan = heart.findViewById(R.id.dangan);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shares1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

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
                    if(h >100){
                        int cha = h-100;
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            title.setAlpha(alpha);
                        }else{
                            title.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }
                        title.setVisibility(View.VISIBLE);

                    }else {
//                            title.getBackground().setAlpha(290-25-h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        title.setVisibility(View.GONE);

                    }

                }

            }
            class ItemRecod {
                int height = 0;
                int top = 0;
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
            }

        });
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(SheBeiDeilActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            getJson(aid,true);
        }

    }
    private void getJson(String aid, boolean state){
        String url= "http://"+MyApplication.ip+"/api/arc_detail_devices.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("typeid","14");
        map.put("aid", aid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        if(state){
            networkCom.getJson(url, map, handler, 1, 1);
        } else {
            networkCom.getJson(url, map, handler, 1, 0);
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.setVisibility(View.GONE);
            String json = (String)msg.obj;
            try {
                if(msg.what == 1){
                    JSONObject jsonObject = new JSONObject(json);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        if(data != null){
                            Log.d("lizisong", "开始解析");
                            JSONObject jsonData = new JSONObject(data);
                            String id = jsonData.getString("id");
                            String title = jsonData.getString("title");
                            String typeid = jsonData.getString("typeid");
                            String typename = jsonData.getString("typename");
                            String litpic = jsonData.getString("litpic");
                            String description = jsonData.getString("description");
                            titlecontent.setText(title);
                            shebeititle.setText(title);
                            titledes.setText(title);

                            ImageLoader.getInstance().displayImage(litpic
                                    , top_heart, options);
                            if(litpic == null || (litpic != null && litpic.equals(""))){
                                youtupian.setVisibility(View.GONE);
                                wutupian.setVisibility(View.VISIBLE);
                            }else{
                                youtupian.setVisibility(View.VISIBLE);
                                wutupian.setVisibility(View.GONE);
                            }

                            try {
                                kewords = jsonData.getString("keywords");
                            } catch (JSONException a) {

                            } catch (Exception e) {

                            }

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

                            String service_field = jsonData.getString("service_field");
                            JSONArray jsonservicefield = new JSONArray(service_field);
                            if(jsonservicefield != null){
                                for(int i=0; i< jsonservicefield.length();i++){
                                    JSONObject item = jsonservicefield.getJSONObject(i);
                                    NewSheBeiDeilEntry pos = new NewSheBeiDeilEntry();
                                    pos.type = item.getString("type");
                                    pos.name = item.getString("name");
                                    pos.names = item.getString("names");
//                                    Log.d("lizisong", "开始解析3");
                                    if(pos.type.equals("5")){
                                        JSONArray object = item.getJSONArray("content");
                                         if(object != null){
                                             for(int j=0;j<object.length();j++){
                                                 NewSheBeiDeilEntry ka = new NewSheBeiDeilEntry();
                                                 JSONObject a = object.getJSONObject(j);
                                                 ka.servicename = a.getString("servicename");
                                                 ka.contents = a.getString("contents");
                                                 danganka.add(ka);
                                             }
                                         }
//                                        Log.d("lizisong", "开始解析4");
                                    }else if(pos.type.equals("0")){
                                        NewSheBeiDeilEntry title1 = new NewSheBeiDeilEntry();
                                        title1.type = "0";
                                        title1.name = pos.name;
                                        showList.add(title1);
                                        NewSheBeiDeilEntry content = new NewSheBeiDeilEntry();
                                        content.type="1";
                                        content.content = item.getString("content");
                                        pos.content = item.getString("content");

                                        showList.add(content);
                                        NewSheBeiDeilEntry jiege = new NewSheBeiDeilEntry();
                                        jiege.type = "-1";
                                        showList.add(jiege);
//                                        Log.d("lizisong", "开始解析5");
                                    }else if(pos.type.equals("2")){
                                        JSONArray object = item.getJSONArray("content");
                                        pos.type="0";
                                        showList.add(pos);

                                        if(object != null){
                                            for(int j =0;j<object.length();j++){
                                                JSONObject a = object.getJSONObject(j);
                                                NewSheBeiDeilEntry ka = new NewSheBeiDeilEntry();
                                                ka.type ="2";
                                                ka.aid = a.getString("aid");
                                                ka.typeid = a.getString("typeid");
                                                ka.typename = a.getString("typename");
                                                ka.title = a.getString("title");
                                                ka.sampleType = a.getString("sampleType");
                                                ka.price = a.getString("price");
                                                Log.d("lizisong", "ka.sampleType:"+ka.sampleType+"ka.title:"+ka.title);
                                                showList.add(ka);
                                                NewSheBeiDeilEntry k = new NewSheBeiDeilEntry();
                                                k.type="-2";
                                                showList.add(k);
                                            }
                                        }

                                    }
                                }
                            }
                            NewSheBeiDeilEntry dixian = new NewSheBeiDeilEntry();
                            dixian.type ="-3";
                            showList.add(dixian);
                            if(danganka.size()>0){
                                for(int i =0; i<danganka.size();i++){
                                    NewSheBeiDeilEntry item = danganka.get(i);
                                    fuzhi(i,item);
//                                    Log.d("lizisong", "进来了1");
//                                    View viewitem = View.inflate(SheBeiDeilActivity.this, R.layout.shebeideilitem, null);
//                                    Log.d("lizisong", "进来了2");
////                                    ViewGroup.LayoutParams params2 = viewitem.getLayoutParams();
////                                    params2.width = width;
////                                    Log.d("lizisong", "进来了3");
////                                    viewitem.setLayoutParams(params2);
//                                    Log.d("lizisong", "进来了4");
//                                    Log.d("lizisong", "执行："+width);
//                                    TextView name = viewitem.findViewById(R.id.name);
//                                    TextView namecontent = viewitem.findViewById(R.id.namecontent);
//                                    TextView dixianline = viewitem.findViewById(R.id.dixianline);
//                                    name.setText(item.servicename);
//                                    namecontent.setText(item.contents);
//                                    dangan.addView(viewitem);
//                                    Log.d("lizisong", "执行：add");
//                                    if(danganka.size()-1 == i){
//                                        dixianline.setVisibility(View.GONE);
//                                    }
//                                    Log.d("lizisong", "开始解析8");
                                }
                            }
                        }

                    }
                    if(adapter == null){
                        adapter = new SheBeiDeilAdapter();
                        listview.addHeaderView(heart);
                        listview.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                }
            }catch (JSONException e){

        }
        }
    };
    class SheBeiDeilAdapter extends BaseAdapter{

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
            HoldView holdView;
            if(convertView == null){
                convertView = View.inflate(SheBeiDeilActivity.this, R.layout.shebeideiladapter, null);
                holdView = new HoldView();
                holdView.titlelay = convertView.findViewById(R.id.titlelay);
                holdView.title = convertView.findViewById(R.id.title);
                holdView.content = convertView.findViewById(R.id.content);
                holdView.jiange = convertView.findViewById(R.id.jiange);
                holdView.service = convertView.findViewById(R.id.service);
                holdView.service_title = convertView.findViewById(R.id.service_title);
                holdView.servicelingyu = convertView.findViewById(R.id.servicelingyu);
                holdView.yuyue = convertView.findViewById(R.id.yuyue);
                holdView.line = convertView.findViewById(R.id.line);
                holdView.dixian = convertView.findViewById(R.id.dixian);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final NewSheBeiDeilEntry item = showList.get(position);
            if(item.type.equals("0")){
                holdView.titlelay.setVisibility(View.VISIBLE);
                holdView.content.setVisibility(View.GONE);
                holdView.jiange.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.title.setText(item.name);
            }else if(item.type.equals("1")){
                holdView.titlelay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.VISIBLE);
                holdView.jiange.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.content.setText(item.content);
            }else if(item.type.equals("-1")){
                holdView.titlelay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.jiange.setVisibility(View.VISIBLE);
                holdView.service.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
            }else if(item.type.equals("2")){
                holdView.titlelay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.jiange.setVisibility(View.GONE);
                holdView.service.setVisibility(View.VISIBLE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.service_title.setText(item.title);
                holdView.servicelingyu.setText(item.sampleType);
                holdView.yuyue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SheBeiDeilActivity.this, "此功能暂未开放", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(item.type.equals("-2")){
                holdView.titlelay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.jiange.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.line.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
            }else if(item.type.equals("-3")){
                holdView.titlelay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.jiange.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
        class HoldView{
            public LinearLayout titlelay;
            public TextView title;

            public TextView content;

            public LinearLayout jiange;

            public RelativeLayout service;
            public TextView service_title;
            public TextView servicelingyu;
            public ImageView yuyue;

            public TextView line;

            public RelativeLayout dixian;
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
    void init(){
        item0 = (LinearLayout)heart.findViewById(R.id.item0);
        item1 =(LinearLayout) heart.findViewById(R.id.item1);
        item2 = (LinearLayout)heart.findViewById(R.id.item2);
        item3 =(LinearLayout) heart.findViewById(R.id.item3);
        item4 = (LinearLayout)heart.findViewById(R.id.item4);
        item5 = (LinearLayout)heart.findViewById(R.id.item5);
        item6 =(LinearLayout) heart.findViewById(R.id.item6);
        item7 = (LinearLayout)heart.findViewById(R.id.item7);
        item8 = (LinearLayout)heart.findViewById(R.id.item8);
        item9 = (LinearLayout)heart.findViewById(R.id.item9);
        item10 =(LinearLayout) heart.findViewById(R.id.item10);
        item11 =(LinearLayout) heart.findViewById(R.id.item11);
        item12 =(LinearLayout) heart.findViewById(R.id.item12);
        item13 =(LinearLayout) heart.findViewById(R.id.item13);
        item14 =(LinearLayout) heart.findViewById(R.id.item14);
        item15 = (LinearLayout)heart.findViewById(R.id.item15);
        item16 =(LinearLayout) heart.findViewById(R.id.item16);
        item17 = (LinearLayout)heart.findViewById(R.id.item17);
        item18 = (LinearLayout)heart.findViewById(R.id.item18);
        item19 =(LinearLayout) heart.findViewById(R.id.item19);


        name0 =(TextView) heart.findViewById(R.id.name0);
        name1 = (TextView)heart.findViewById(R.id.name1);
        name2 =(TextView) heart.findViewById(R.id.name2);
        name3 =(TextView) heart.findViewById(R.id.name3);
        name4 = (TextView)heart.findViewById(R.id.name4);
        name5 = (TextView)heart.findViewById(R.id.name5);
        name6 = (TextView)heart.findViewById(R.id.name6);
        name7 =(TextView) heart.findViewById(R.id.name7);
        name8 = (TextView)heart.findViewById(R.id.name8);
        name9 = (TextView)heart.findViewById(R.id.name9);
        name10 =(TextView) heart.findViewById(R.id.name10);
        name11 = (TextView)heart.findViewById(R.id.name11);
        name12 =(TextView) heart.findViewById(R.id.name12);
        name13 =(TextView) heart.findViewById(R.id.name13);
        name14 =(TextView) heart.findViewById(R.id.name14);
        name15 = (TextView)heart.findViewById(R.id.name15);
        name16 = (TextView)heart.findViewById(R.id.name16);
        name17 = (TextView)heart.findViewById(R.id.name17);
        name18 = (TextView)heart.findViewById(R.id.name18);
        name19 =(TextView) heart.findViewById(R.id.name19);

        namecontent0=(TextView)heart.findViewById(R.id.namecontent0);
        namecontent1=(TextView)heart.findViewById(R.id.namecontent1);
        namecontent2=(TextView)heart.findViewById(R.id.namecontent2);
        namecontent3=(TextView)heart.findViewById(R.id.namecontent3);
        namecontent4=(TextView)heart.findViewById(R.id.namecontent4);
        namecontent5=(TextView)heart.findViewById(R.id.namecontent5);
        namecontent6=(TextView)heart.findViewById(R.id.namecontent6);
        namecontent7=(TextView)heart.findViewById(R.id.namecontent7);
        namecontent8=(TextView)heart.findViewById(R.id.namecontent8);
        namecontent9=(TextView)heart.findViewById(R.id.namecontent9);
        namecontent10=(TextView)heart.findViewById(R.id.namecontent10);
        namecontent11=(TextView)heart.findViewById(R.id.namecontent11);
        namecontent12=(TextView)heart.findViewById(R.id.namecontent12);
        namecontent13=(TextView)heart.findViewById(R.id.namecontent13);
        namecontent14=(TextView)heart.findViewById(R.id.namecontent14);
        namecontent15=(TextView)heart.findViewById(R.id.namecontent15);
        namecontent16=(TextView)heart.findViewById(R.id.namecontent16);
        namecontent17=(TextView)heart.findViewById(R.id.namecontent17);
        namecontent18=(TextView)heart.findViewById(R.id.namecontent18);
        namecontent19=(TextView)heart.findViewById(R.id.namecontent19);
    }

    void fuzhi(int i,NewSheBeiDeilEntry item){
        switch (i){
            case 0:
                item0.setVisibility(View.VISIBLE);
                name0.setText(item.servicename);
                namecontent0.setText(item.contents);
                break;
            case 1:
                item1.setVisibility(View.VISIBLE);
                name1.setText(item.servicename);
                namecontent1.setText(item.contents);
                break;
            case 2:
                item2.setVisibility(View.VISIBLE);
                name2.setText(item.servicename);
                namecontent2.setText(item.contents);
                break;
            case 3:
                item3.setVisibility(View.VISIBLE);
                name3.setText(item.servicename);
                namecontent3.setText(item.contents);
                break;
            case 4:
                item4.setVisibility(View.VISIBLE);
                name4.setText(item.servicename);
                namecontent4.setText(item.contents);
                break;
            case 5:
                item5.setVisibility(View.VISIBLE);
                name5.setText(item.servicename);
                namecontent5.setText(item.contents);
                break;
            case 6:
                item6.setVisibility(View.VISIBLE);
                name6.setText(item.servicename);
                namecontent6.setText(item.contents);
                break;
            case 7:
                item7.setVisibility(View.VISIBLE);
                name7.setText(item.servicename);
                namecontent7.setText(item.contents);
                break;
            case 8:
                item8.setVisibility(View.VISIBLE);
                name8.setText(item.servicename);
                namecontent8.setText(item.contents);
                break;
            case 9:
                item9.setVisibility(View.VISIBLE);
                name9.setText(item.servicename);
                namecontent9.setText(item.contents);
                break;
            case 10:
                item10.setVisibility(View.VISIBLE);
                name10.setText(item.servicename);
                namecontent10.setText(item.contents);
                break;
            case 11:
                item11.setVisibility(View.VISIBLE);
                name11.setText(item.servicename);
                namecontent11.setText(item.contents);
                break;
            case 12:
                item12.setVisibility(View.VISIBLE);
                name12.setText(item.servicename);
                namecontent12.setText(item.contents);
                break;
            case 13:
                item13.setVisibility(View.VISIBLE);
                name13.setText(item.servicename);
                namecontent13.setText(item.contents);
                break;
            case 14:
                item14.setVisibility(View.VISIBLE);
                name14.setText(item.servicename);
                namecontent14.setText(item.contents);
                break;
            case 15:
                item15.setVisibility(View.VISIBLE);
                name15.setText(item.servicename);
                namecontent15.setText(item.contents);
                break;
            case 16:
                item16.setVisibility(View.VISIBLE);
                name16.setText(item.servicename);
                namecontent16.setText(item.contents);
                break;
            case 17:
                item17.setVisibility(View.VISIBLE);
                name17.setText(item.servicename);
                namecontent17.setText(item.contents);
                break;
            case 18:
                item18.setVisibility(View.VISIBLE);
                name18.setText(item.servicename);
                namecontent18.setText(item.contents);
                break;
            case 19:
                item19.setVisibility(View.VISIBLE);
                name19.setText(item.servicename);
                namecontent19.setText(item.contents);
                break;
        }
    }

}
