package com.maidiantech;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Util.SharedPreferencesUtil;
import Util.NetUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.NewProjectShow;
import entity.Posts;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/9/12.
 */

public class NewRenCaiDetail extends AutoLayoutActivity {
    String aid = "",kewords="";
    String id,title,typeid,typename,litpic,litpic_bg,description,isExtra;
    private DisplayImageOptions options;
    List<NewProjectShow> projectShowList = new ArrayList<>();
    LinearLayout titlelay;
    ImageView yujian_backs,right,banner_top,back,share;
    RoundImageView rc_img;
    TextView titletxt,name,biaoqian,bottmon_title;
    ProgressBar progress;
    RefreshListView listview;
    View HeaderView;
    RenCai adapter;
    boolean isState =false;
    boolean isherat = true;
    SystemBarTintManager tintManager ;
    List<KeyWord> keys = new ArrayList<>();
    UMShareAPI mShareAPI;
    Button yuyue;
    boolean isShow = false;

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
        setContentView(R.layout.newrencaidetail);
        mShareAPI = UMShareAPI.get(this);
        tintManager = new SystemBarTintManager(this);
        options = ImageLoaderUtils.initOptions();
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        isShow = getIntent().getBooleanExtra("isShow", false);
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
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);
        HeaderView = View.inflate(this, R.layout.newrencaidetailheart,null);
        back = (ImageView)HeaderView.findViewById(R.id.back);
        banner_top = (ImageView)HeaderView.findViewById(R.id.banner_top);
        share = (ImageView)HeaderView.findViewById(R.id.share);
        rc_img = (RoundImageView)HeaderView.findViewById(R.id.rc_img);
        name = (TextView)HeaderView.findViewById(R.id.name);
        biaoqian = (TextView)HeaderView.findViewById(R.id.biaoqian);
        aid = getIntent().getStringExtra("aid");
        titlelay = (LinearLayout)findViewById(R.id.titlelay);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        titletxt = (TextView)findViewById(R.id.title);
        right = (ImageView)findViewById(R.id.right);
        progress = (ProgressBar)findViewById(R.id.progress);
        listview = (RefreshListView)findViewById(R.id.listview);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        yuyue = (Button)findViewById(R.id.yuyue);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                share();
                String path = "pages/index/index?aid="+aid;
                shareweixinxiaochengxuonclick(description,title,path);
            }
        });


        listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    isState =false;
                    if (listview.getLastVisiblePosition() == (listview.getCount() - 1)) {
                        yuyue.setBackgroundResource(R.mipmap.newrencaiyuyue);
                    }
                    back.setVisibility(View.VISIBLE);
                }else if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    back.setVisibility(View.VISIBLE);
                }else{
                    yuyue.setBackgroundResource(R.mipmap.newrencaiyuyue);
                }
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
                    if(h >290-25){
                        int cha = h-(290-25);
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            titlelay.setAlpha(alpha);
                        }else{
                            titlelay.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }
                        titlelay.setVisibility(View.VISIBLE);

                    }else {
//                            title.getBackground().setAlpha(290-25-h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        titlelay.setVisibility(View.GONE);

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
            }
            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });

        if (MainActivity.hasSoftKeys(getWindowManager()) /*&& isNavigationBarShow()*/) {
            bottmon_title.setVisibility(View.VISIBLE);
        } else {
            bottmon_title.setVisibility(View.GONE);
        }
        if(MyApplication.navigationbar >0){
            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
            params.height=MyApplication.navigationbar;
            bottmon_title.setLayoutParams(params);
        }


        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(NewRenCaiDetail.this, "网络不给力", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);

        } else {
            progress.setVisibility(View.VISIBLE);
            getJson();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
    private void  getJson(){
        String url = "http://"+ MyApplication.ip+"/api/arc_detail_talent.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("aid", aid);
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
                    JSONObject jsonObject = new JSONObject(ret);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        JSONObject jsonData = new JSONObject(data);
                        id = jsonData.getString("id");
                        title = jsonData.getString("title");
                        typeid = jsonData.getString("typeid");
                        typename = jsonData.getString("typename");
                        litpic = jsonData.getString("litpic");
                        litpic_bg = jsonData.getString("litpic_bg");
                        description = jsonData.getString("description");
                        try {
                            isExtra = jsonData.getString("isExtra");
                        }catch (Exception e){

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
                        String project_field = jsonData.getString("project_field");
                        NewProjectShow pos=null;
                        JSONArray jsonprojectfield = new JSONArray(project_field);
                        for( int i=0;i<jsonprojectfield.length();i++){
                            JSONObject item = jsonprojectfield.getJSONObject(i);
                            pos = new NewProjectShow();
                            pos.type = item.getString("type");
                            pos.name = item.getString("name");
                            pos.names = item.getString("names");
                            if(pos.type.equals("0")){
                                pos.content = item.getString("content");
                            }else if(pos.type.equals("1")){
                                pos.content = item.getString("content");
                            }else if(pos.type.equals("2")){
                                pos.project = new ArrayList<>();
                                JSONArray array = item.getJSONArray("content");
                                for(int k=0;k<array.length();k++){
                                    JSONObject object = array.getJSONObject(k);
                                    Posts posts = new Posts();
                                    posts.aid = object.getString("aid");
                                    posts.typeid = object.getString("typeid");
                                    posts.setTitle(object.getString("title"));
                                    posts.setLitpic(object.getString("litpic"));
                                    posts.unit = (object.getString("unit"));
                                    String jscate = object.getString("area_cate");
                                    Area_cate cate = new Area_cate();
                                    cate.setArea_cate1(jscate);
                                    posts.setArea_cate(cate);
                                    posts.setRank(object.getString("ranks"));
                                    posts.setDescription(object.getString("description"));
                                    posts.source = object.getString("str");
                                    pos.project.add(posts);
                                }

                            }else if(pos.type.equals("3")){
                                pos.cooperation = new ArrayList<>();
                                JSONArray object = item.getJSONArray("content");
                                for(int j=0;j<object.length();j++){
                                    String txt = (String) object.get(j);
                                    pos.cooperation.add(txt);
                                }
                            }
                            if(pos.type.equals("4")){
                                projectShowList.add(pos);
                                JSONArray arrystr = item.getJSONArray("content");
                                if(arrystr != null && arrystr.length() > 0){
                                    for(int j=0;j<arrystr.length();j++){

                                        JSONObject temppp = arrystr.getJSONObject(j);
                                        NewProjectShow itemp = new NewProjectShow();
                                        String catestr = temppp.getString("cate");
                                        if(catestr.equals("0")){
                                            itemp.type = "401";
                                        } else if(catestr.equals("1")){
                                            itemp.type = "402";
                                        }
                                        itemp.str = temppp.getString("str");
                                        itemp.name = pos.name;
                                        itemp.names=pos.names;
                                        projectShowList.add(itemp);
                                    }
                                }
                                NewProjectShow lines = new NewProjectShow();
                                lines.type="-2";
                                projectShowList.add(lines);
                            }else {
                                projectShowList.add(pos);
                            }
                        }
                        NewProjectShow dixian = new NewProjectShow();
                        dixian.type="-1";
                        projectShowList.add(dixian);
                        ImageLoader.getInstance().displayImage(litpic_bg
                                , banner_top, options);
                        ImageLoader.getInstance().displayImage(litpic
                                , rc_img, options);
                        name.setText(title);
                        biaoqian.setText(typename);
                        titletxt.setText(title);
                        progress.setVisibility(View.GONE);
                        listview.addHeaderView(HeaderView);
                        if(adapter == null){
                            adapter = new RenCai();
                            listview.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        if(isExtra != null && isExtra.equals("1")){
                            yuyue.setVisibility(View.GONE);
                        }else {
                            yuyue.setVisibility(View.VISIBLE);
                        }
                        if(isShow){
                            yuyue.setVisibility(View.GONE);
                        }


                        yuyue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    Intent intent = new Intent(NewRenCaiDetail.this, TianXuQiu.class);
                                    intent.putExtra("aid", aid);
                                    intent.putExtra("typeid", typeid);
                                    startActivity(intent);

                                } else{
                                    Intent intent = new Intent(NewRenCaiDetail.this, MyloginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });


                    }
                }


            }catch (JSONException e){

            } catch (Exception e){

            }
        }
    };
    Posts rencai11 = null, rencai22= null,rencai33=null;

    class RenCai extends BaseAdapter{

        @Override
        public int getCount() {
            return projectShowList.size();
        }

        @Override
        public Object getItem(int position) {
            return projectShowList.get(position);
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
                convertView = View.inflate(NewRenCaiDetail.this, R.layout.newrencaiadapter,null);
                holdView.lay0_state = convertView.findViewById(R.id.lay0_state);
                holdView.title_show = convertView.findViewById(R.id.title_show);
                holdView.content = convertView.findViewById(R.id.content);
                holdView.lay1_state = convertView.findViewById(R.id.lay1_state);
                holdView.title_show1 = convertView.findViewById(R.id.title_show1);
                holdView.wbviw = convertView.findViewById(R.id.wbviw);

                holdView.lay2_state = convertView.findViewById(R.id.lay2_state);
                holdView.title_show2 = convertView.findViewById(R.id.title_show2);
                holdView.rencai1 = convertView.findViewById(R.id.rencai1);
                holdView.rencai2 = convertView.findViewById(R.id.rencai2);
                holdView.rencai3 = convertView.findViewById(R.id.rencai3);
                holdView.rencai_title1 = convertView.findViewById(R.id.rencai_title1);
                holdView.rencai_title2 = convertView.findViewById(R.id.rencai_title2);
                holdView.rencai_title3 = convertView.findViewById(R.id.rencai_title3);
                holdView.rank1 = convertView.findViewById(R.id.rank1);
                holdView.rank2 = convertView.findViewById(R.id.rank2);
                holdView.rank3 = convertView.findViewById(R.id.rank3);
                holdView.rencai_line1 = convertView.findViewById(R.id.rencai_line1);
                holdView.rencai_line2 = convertView.findViewById(R.id.rencai_line2);
                holdView.rencai_line3 = convertView.findViewById(R.id.rencai_line3);
                holdView.str1 = convertView.findViewById(R.id.str1);
                holdView.str2 = convertView.findViewById(R.id.str2);
                holdView.str3 = convertView.findViewById(R.id.str3);
                holdView.rencai_line1_bottom = convertView.findViewById(R.id.rencai_line1_bottom);
                holdView.rencai_line2_bottom = convertView.findViewById(R.id.rencai_line2_bottom);
                holdView.rencai_line3_bottom = convertView.findViewById(R.id.rencai_line3_bottom);
                holdView.dixian = convertView.findViewById(R.id.dixian);
                holdView.lines = convertView.findViewById(R.id.lines);
                holdView.rencai_img1 = convertView.findViewById(R.id.rencai_img1);
                holdView.rencai_img2 = convertView.findViewById(R.id.rencai_img2);
                holdView.rencai_img3 = convertView.findViewById(R.id.rencai_img3);
                holdView.rencai_lingyu1 = convertView.findViewById(R.id.rencai_lingyu1);
                holdView.rencai_lingyu2 = convertView.findViewById(R.id.rencai_lingyu2);
                holdView.rencai_lingyu3 = convertView.findViewById(R.id.rencai_lingyu3);
                holdView.laycontent = (TextView)convertView.findViewById(R.id.laycontent);
                holdView.imagecontent = (ImageView)convertView.findViewById(R.id.imagecontent);
                holdView.titleshow = (LinearLayout)convertView.findViewById(R.id.titleshow);
                holdView.titlestr = (TextView)convertView.findViewById(R.id.titlestr);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final NewProjectShow item = projectShowList.get(position);
            if(item.type.equals("0")){
                holdView.lay0_state.setVisibility(View.VISIBLE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.title_show.setText(item.name);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
                        setSpecifiedTextsColor(holdView.content,item.content,pos,styledText);
                    }
                    holdView.content.setText(styledText);

                }else{
                    holdView.content.setText(item.content);
                }

            }else if(item.type.equals("1")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.VISIBLE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.title_show1.setText(item.name);
                holdView.wbviw.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
                holdView.wbviw.setHapticFeedbackEnabled(false);
                holdView.wbviw.getSettings().setSupportZoom(false);
                holdView.wbviw.getSettings().setDomStorageEnabled(false);
                holdView.wbviw.getSettings().setSupportMultipleWindows(true);
                holdView.wbviw.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                String str="";
                try {
                    str = new String(ZixunDetailsActivity.toByteArray(getAssets().open("newproject.html")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                str  = str.replace("<---replace-->",item.content);
                holdView.wbviw.loadDataWithBaseURL("file:///android_asset/", str, "text/html",
                        "utf-8", null);
            }else if(item.type.equals("3")){
                holdView.lay0_state.setVisibility(View.VISIBLE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.title_show.setText(item.name);
                if(item.cooperation != null){
                    String content="";
                    for (int i=0; i<item.cooperation.size(); i++){
                        String str = item.cooperation.get(i);
                        if(i==item.cooperation.size()-1){
                            content = content+str;
                        }else{
                            content=content+str+"\n";
                        }
                    }
                    if(keys.size() > 0){
                        SpannableStringBuilder styledText = new SpannableStringBuilder(content);
                        for(int i=0; i<keys.size();i++){
                            KeyWord pos = keys.get(i);
                            setSpecifiedTextsColor(holdView.content,content,pos,styledText);
                        }
                        holdView.content.setText(styledText);

                    }else{
                        holdView.content.setText(content);
                    }
//                    holdView.content.setText(content);
                }

            }else if(item.type.equals("2")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.title_show2.setText(item.name);
                if(item.project != null){
                    Posts rencai1 = null, rencai2= null,rencai3=null;
                    if(item.project.size() == 1){
                        holdView.rencai1.setVisibility(View.VISIBLE);
                        holdView.rencai2.setVisibility(View.GONE);
                        holdView.rencai3.setVisibility(View.GONE);
                        rencai1 = item.project.get(0);
                        rencai11=rencai1;
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.getLitpic() != null && !rencai1.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);

                            } else {
                                holdView.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai1.getLitpic() == null || rencai1.getLitpic().equals("")){
                                holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);
                            }

                        }
                        holdView.rencai_title1.setText(rencai1.getTitle());
                        holdView.rencai_lingyu1.setText(rencai1.unit);
                        holdView.rank1.setText(rencai1.rank);
                        if(rencai1.source != null){
                            holdView.str1.setText(rencai1.source);
                        }else{
                            holdView.str1.setVisibility(View.GONE);
                            holdView.rencai_line1.setVisibility(View.GONE);
                        }

                    }else if(item.project.size() == 2){
                        holdView.rencai1.setVisibility(View.VISIBLE);
                        holdView.rencai2.setVisibility(View.VISIBLE);
                        holdView.rencai3.setVisibility(View.GONE);
                        rencai1 = item.project.get(0);
                        rencai2 = item.project.get(1);
                        rencai11=rencai1;
                        rencai22=rencai2;
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.getLitpic() != null && !rencai1.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);

                            } else {
                                holdView.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai1.getLitpic() == null || rencai1.getLitpic().equals("")){
                                holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);
                            }

                        }
                        holdView.rencai_title1.setText(rencai1.getTitle());
                        holdView.rencai_lingyu1.setText(rencai1.unit);
                        holdView.rank1.setText(rencai1.rank);
                        if(rencai1.source != null){
                            holdView.str1.setText(rencai1.source);
                        }else{
                            holdView.str1.setVisibility(View.GONE);
                            holdView.rencai_line1.setVisibility(View.GONE);
                        }
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai2.getLitpic() != null && !rencai2.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai2.getLitpic()
                                        , holdView.rencai_img2, options);

                            } else {
                                holdView.rencai_img2.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai2.getLitpic() == null || rencai2.getLitpic().equals("")){
                                holdView.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai2.getLitpic()
                                        , holdView.rencai_img2, options);
                            }

                        }
                        holdView.rencai_title2.setText(rencai2.getTitle());
                        holdView.rencai_lingyu2.setText(rencai2.unit);
                        holdView.rank2.setText(rencai2.rank);
                        if(rencai2.source != null){
                            holdView.str2.setText(rencai2.source);
                        }else{
                            holdView.str2.setVisibility(View.GONE);
                            holdView.rencai_line2.setVisibility(View.GONE);
                        }

                    }else if(item.project.size() == 3){
                        holdView.rencai1.setVisibility(View.VISIBLE);
                        holdView.rencai2.setVisibility(View.VISIBLE);
                        holdView.rencai3.setVisibility(View.VISIBLE);
                        rencai1 = item.project.get(0);
                        rencai2 = item.project.get(1);
                        rencai3 = item.project.get(2);
                        rencai11=rencai1;
                        rencai22=rencai2;
                        rencai33=rencai3;
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.getLitpic() != null && !rencai1.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);

                            } else {
                                holdView.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai1.getLitpic() == null || rencai1.getLitpic().equals("")){
                                holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.getLitpic()
                                        , holdView.rencai_img1, options);
                            }

                        }
                        holdView.rencai_title1.setText(rencai1.getTitle());
                        holdView.rencai_lingyu1.setText(rencai1.unit);
                        holdView.rank1.setText(rencai1.rank);
                        if(rencai1.source != null){
                            holdView.str1.setText(rencai1.source);
                        }else{
                            holdView.str1.setVisibility(View.GONE);
                            holdView.rencai_line1.setVisibility(View.GONE);
                        }

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai2.getLitpic() != null && !rencai2.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai2.getLitpic()
                                        , holdView.rencai_img2, options);

                            } else {
                                holdView.rencai_img2.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai2.getLitpic() == null || rencai2.getLitpic().equals("")){
                                holdView.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai2.getLitpic()
                                        , holdView.rencai_img2, options);
                            }

                        }
                        holdView.rencai_title2.setText(rencai2.getTitle());
                        holdView.rencai_lingyu2.setText(rencai2.unit);
                        holdView.rank2.setText(rencai2.rank);
                        if(rencai2.source != null){
                            holdView.str2.setText(rencai2.source);
                        }else{
                            holdView.str2.setVisibility(View.GONE);
                            holdView.rencai_line2.setVisibility(View.GONE);
                        }

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai3.getLitpic() != null && !rencai3.getLitpic().equals("")) {
                                ImageLoader.getInstance().displayImage(rencai3.getLitpic()
                                        , holdView.rencai_img3, options);

                            } else {
                                holdView.rencai_img3.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai3.getLitpic() == null || rencai3.getLitpic().equals("")){
                                holdView.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai3.getLitpic()
                                        , holdView.rencai_img3, options);
                            }

                        }
                        holdView.rencai_title3.setText(rencai3.getTitle());
                        holdView.rencai_lingyu3.setText(rencai3.unit);
                        holdView.rank3.setText(rencai3.rank);
                        if(rencai3.source != null){
                            holdView.str3.setText(rencai3.source);
                        }else{
                            holdView.str3.setVisibility(View.GONE);
                            holdView.rencai_line3.setVisibility(View.GONE);
                        }
                    }
                }
                holdView.rencai1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewRenCaiDetail.this, NewRenCaiDetail.class);
                        intent.putExtra("aid", rencai11.aid);
                        startActivity(intent);
                    }
                });
                holdView.rencai2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewRenCaiDetail.this, NewRenCaiDetail.class);
                        intent.putExtra("aid", rencai22.aid);
                        startActivity(intent);
                    }
                });
                holdView.rencai3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewRenCaiDetail.this, NewRenCaiDetail.class);
                        intent.putExtra("aid", rencai33.aid);
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("-1")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
                holdView.lines.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
            }else if(item.type.equals("4")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.VISIBLE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.titlestr.setText(item.name);
            }else if(item.type.equals("-2")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.VISIBLE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
            }else if(item.type.equals("401")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.VISIBLE);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.str);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
//                        counts.clear();
                        setSpecifiedTextsColor(holdView.laycontent,item.str,pos,styledText);
                    }
                    holdView.laycontent.setText(styledText);

                }else{
                    holdView.laycontent.setText(item.str);
                }
            }else if(item.type.equals("402")){
                holdView.lay0_state.setVisibility(View.GONE);
                holdView.lay1_state.setVisibility(View.GONE);
                holdView.lines.setVisibility(View.GONE);
                holdView.lay2_state.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.str
                        , holdView.imagecontent, options);
            }
            return convertView;
        }
        class HoldView{
            public LinearLayout lay0_state;
            public TextView title_show;
            public TextView content;

            public LinearLayout lay1_state;
            public TextView title_show1;
            public WebView wbviw;

            public LinearLayout lay2_state;
            public TextView title_show2;
            public LinearLayout rencai1,rencai2,rencai3;
            public RoundImageView rencai_img1,rencai_img2,rencai_img3;
            public TextView rencai_title1,rencai_title2,rencai_title3;
            public TextView rencai_lingyu1,rencai_lingyu2,rencai_lingyu3;
            public TextView rank1,rank2,rank3;
            public TextView rencai_line1,rencai_line2,rencai_line3;
            public TextView str1,str2,str3,rencai_line1_bottom,rencai_line2_bottom,rencai_line3_bottom;

            public RelativeLayout dixian;
            public TextView lines;

            public TextView laycontent;
            public ImageView imagecontent;
            public LinearLayout titleshow;
            public TextView titlestr;
        }
    }
    private void share(){
        try {
            Intent intent = new Intent(NewRenCaiDetail.this, ShareActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("txt", description);
            intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/api/share.php?aid=" + aid + "");
            if (litpic == null || litpic.equals("")) {
                intent.putExtra("imageurl", "http://"+MyApplication.ip+"/uploads/logo/logo.png");
            }else {
                intent.putExtra("imageurl", litpic);
            }
            startActivity(intent);


        } catch (Exception e) {
        }
    }
    class KeyWord {
        public String keyword;
        public String typeid;
        public String aid;
        public String color;
    }
    public void setSpecifiedTextsColor(TextView txtView, String text, KeyWord specifiedTexts, SpannableStringBuilder styledText){

        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.keyword.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do{

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

    class TextClick extends ClickableSpan {
        KeyWord mKeyWord;
        public TextClick(KeyWord item){
            mKeyWord = item;
        }

        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            if(mKeyWord.aid == null || (mKeyWord.aid != null && mKeyWord.aid.equals(""))){
                Intent intent = new Intent(NewRenCaiDetail.this, NewSearchContent.class);
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
                    Intent intent=new Intent(NewRenCaiDetail.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(NewRenCaiDetail.this, NewRenCaiDetail.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(NewRenCaiDetail.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewRenCaiDetail.this, NewSearchContent.class);
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
    public void shareweixinxiaochengxuonclick(String txt,String title, String Tarurl){
        try {
            ShareAction action;
            UMImage image = null;
            if(litpic != null && !litpic.equals("")){
                image = new UMImage(this, R.mipmap.xiaochengxfenxiang);
            }else{
                image = new UMImage(this, R.mipmap.xiaochengxfenxiang);
            }
            UMMin umMin = new UMMin(Tarurl);

            umMin.setThumb(image);
            umMin.setTitle(title);
            umMin.setDescription(txt);
            umMin.setPath(Tarurl);
            umMin.setUserName("gh_4df836756fd4");
            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(umMin)
                    .setCallback(umShareListener)//回调监听器
                    .share();
        }catch (Exception e){

        }
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
            } else {
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NewRenCaiDetail.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
            finish();
        }
    };
}
