package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;

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
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.NewProjectShow;
import entity.Posts;
import view.HorizontalListView;
import view.MyTextView;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.SharedPreferencesUtil;
/**
 * Created by lizisong on 2018/2/28.
 */

public class NewProjectActivity extends AutoLayoutActivity {
    LinearLayout titleLay;
    ImageView yujian_backs,share,backs1,shares1;
    TextView titlecontent,bottmon_title;
    HorizontalListView horlist;
    RefreshListView listview;
    ProgressBar progress;
    //头部数据
    String id,title,typeid,typename,litpic,description,kewords="";
    private DisplayImageOptions options;
    List<NewProjectShow> projectShowList = new ArrayList<>();
    List<KeyWord> keys = new ArrayList<>();
    String aid = "";
    View heart =null;
    RelativeLayout relayout;
    RelativeLayout youtupian,wutupian;
    TextView titledes;
    ImageView backs,shares;
    ImageView top_heart,zhezhao;
    SystemBarTintManager tintManager ;
    TextView heartdes,titletv,biaoqian;
    List<String> namesList = new ArrayList<>();
    public int current = 0;
    boolean isherat = true;
    String str;
    Adapter adapter;
    boolean isState =false;
    Button yuyue;
    boolean isShow = false;
    Heart heartAdapter = new Heart();

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
        setContentView(R.layout.newprojectactivty);
        options = ImageLoaderUtils.initOptions();
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        isShow = getIntent().getBooleanExtra("isShow", false);
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
        titleLay = (LinearLayout)findViewById(R.id.title);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share = (ImageView)findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path="pages/projectDetails/index?aid="+aid;
                shareweixinxiaochengxuonclick(title, title,path);
            }
        });
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        horlist = (HorizontalListView)findViewById(R.id.horlist);
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        yuyue = (Button)findViewById(R.id.yuyue);
        aid = getIntent().getStringExtra("aid");
        heart = View.inflate(NewProjectActivity.this, R.layout.newprojectheart, null);
        relayout = (RelativeLayout)heart.findViewById(R.id.relayout);
        top_heart = (ImageView)heart.findViewById(R.id.top_heart);
        backs = (ImageView)heart.findViewById(R.id.backs);
        titledes = (TextView)heart.findViewById(R.id.titledes);
        backs1 = (ImageView)heart.findViewById(R.id.backs1);
        shares1 = (ImageView)heart.findViewById(R.id.shares1);
        backs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shares = (ImageView)heart.findViewById(R.id.shares);
        shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path="pages/projectDetails/index?aid="+aid;
                shareweixinxiaochengxuonclick(title, title,path);
            }
        });
        shares1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path="pages/projectDetails/index?aid="+aid;
                shareweixinxiaochengxuonclick(title, title,path);
            }
        });
        zhezhao = (ImageView)heart.findViewById(R.id.zhezhao1);
        youtupian = (RelativeLayout)heart.findViewById(R.id.youtupian);
        wutupian = (RelativeLayout)heart.findViewById(R.id.wutupian);

//        zhezhao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zhezhao.setVisibility(View.GONE);
//                heartdes.setVisibility(View.GONE);
//            }
//        });
//        top_heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zhezhao.setVisibility(View.VISIBLE);
//                heartdes.setVisibility(View.GONE);
//            }
//        });
        heartdes = (TextView)heart.findViewById(R.id.heartdes);
        titletv = (TextView)heart.findViewById(R.id.title);
        biaoqian = (TextView)heart.findViewById(R.id.biaoqian);
        progress.setVisibility(View.VISIBLE);
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
        youtupian.setVisibility(View.VISIBLE);
        wutupian.setVisibility(View.GONE);
        listview.setOnScrollListener(new  AbsListView.OnScrollListener(){
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    isState =false;
                    backs.setVisibility(View.VISIBLE);
                    if (listview.getLastVisiblePosition() == (listview.getCount() - 1)) {
                        yuyue.setBackgroundResource(R.mipmap.yue);

                    }
                }else if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    backs.setVisibility(View.VISIBLE);
                }else{
                    yuyue.setBackgroundResource(R.mipmap.yue);
//                    backs.setVisibility(View.GONE);
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
                            titleLay.setAlpha(alpha);
                        }else{
                            titleLay.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }
                        titleLay.setVisibility(View.VISIBLE);

                        if(projectShowList.size() >0 && firstVisibleItem >1){
                            NewProjectShow item = projectShowList.get(firstVisibleItem-2);
                            if(item != null && !isState){
                                if(item.type != null && (item.type.equals("0") || item.type.equals("1") || item.type.equals("2")|| item.type.equals("3")||item.type.equals("4"))){
                                    titleLay.setVisibility(View.VISIBLE);
//                                    backs.setVisibility(View.GONE);
                                    if(current != item.position){
                                        current = item.position;
                                        if(heartAdapter != null){
                                            horlist.setSelection(current);
                                            heartAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }

                    }else {
//                            title.getBackground().setAlpha(290-25-h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        if(!isState){
                           titleLay.setVisibility(View.GONE);
//                            backs.setVisibility(View.VISIBLE);
                           current = 0;
                        }
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
        getJson(aid);
    }

    /**
     * 拉数据
     */
    private void getJson(String aid){
           String url = "http://"+ MyApplication.ip+"/api/arc_detail_project.php";
           HashMap<String,String> map = new HashMap<>();
           map.put("aid",aid);
           NetworkCom networkCom = NetworkCom.getNetworkCom();
           networkCom.getJson(url,map,handler,0,0);
    }
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                progress.setVisibility(View.GONE);
//                Log.d("lizisong", "gggggggggggggggggggggggg");
                String json = (String)msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        JSONObject jsonData = new JSONObject(data);
                        id = jsonData.getString("id");
                        title = jsonData.getString("title");
                        title=title.replaceAll("\n","").replaceAll(" ","").replaceAll("\r\n","");
                        typeid = jsonData.getString("typeid");
                        typename = jsonData.getString("typename");
                        litpic = jsonData.getString("litpic");
                        description = jsonData.getString("description");
                        try {
                            kewords = jsonData.getString("keywords");
                        } catch (JSONException a) {

                        } catch (Exception e) {

                        }
//                        Log.d("lizisong", "kkkkkkkk");

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
//                        Log.d("lizisong", "kkkkkkkk111");

                        String project_field = jsonData.getString("project_field");
                        JSONArray jsonprojectfield = new JSONArray(project_field);
                        titlecontent.setText(title);
                        NewProjectShow jiange = new NewProjectShow();
                        jiange.type = "-1";
                        jiange.names="";
                        projectShowList.add(jiange);
                        int postion=0;
                        for(int i=0;i<jsonprojectfield.length();i++){
                            JSONObject item = jsonprojectfield.getJSONObject(i);
                            NewProjectShow pos = new NewProjectShow();
                            pos.type = item.getString("type");
                            pos.name = item.getString("name");
                            pos.names = item.getString("names");
                            namesList.add(pos.names);
                            if(pos.type.equals("0")){
                                pos.content = item.getString("content");
                                pos.position = postion;
                                postion++;
                            }else if(pos.type.equals("1")){
                                pos.content = item.getString("content");
                                pos.position = postion;
                                postion++;

                            }else if(pos.type.equals("2")){
                                pos.cooperation = new ArrayList<>();
                                pos.position = postion;
                                postion++;
                                JSONArray object = item.getJSONArray("content");
                                for(int j=0;j<object.length();j++){
                                    String txt = (String) object.get(j);
                                    pos.cooperation.add(txt);
                                }
                            }else if(pos.type.equals("3")){
                                projectShowList.add(jiange);
                                pos.position = postion;
                                postion++;
                                pos.project = new ArrayList<>();
                                JSONArray array = item.getJSONArray("project");
                                for(int k=0;k<array.length();k++){
                                    JSONObject object = array.getJSONObject(k);
                                    Posts posts = new Posts();
                                    posts.aid = object.getString("aid");
                                    posts.typeid = object.getString("typeid");
                                    posts.setTypename(object.getString("typename"));
                                    posts.setTitle(object.getString("title"));
                                    posts.setLitpic(object.getString("litpic"));
                                    try {
                                        posts.str =object.getString("str");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }
                                    try {
                                        posts.labels = object.getString("labels");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }

                                    JSONObject jscate = object.getJSONObject("area_cate");

                                    Area_cate cate = new Area_cate();
                                    cate.setArea_cate1(jscate.getString("area_cate1"));
                                    posts.setArea_cate(cate);
                                    posts.setDescription(object.getString("description"));
                                    posts.setClick(object.getString("click"));
                                    pos.project.add(posts);
                                }

                            }
                            if(pos.type.equals("4")){
                                 pos.position = postion;
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
                                         itemp.position = postion;
                                         itemp.names=pos.names;
                                         projectShowList.add(itemp);
                                     }
                                 }
                                NewProjectShow lines = new NewProjectShow();
                                lines.type="-2";
                                projectShowList.add(lines);
                                postion++;

                            }else{
                                 projectShowList.add(pos);
                            }
                        }
//                        Log.d("lizisong", "kkkkkkkk999999");
                        NewProjectShow dianxian = new NewProjectShow();
                        dianxian.type="-3";
                        projectShowList.add(dianxian);
                        ImageLoader.getInstance().displayImage(litpic
                                , top_heart, options);
                        titletv.setText(title);
                        titledes.setText(title);
                        heartdes.setText(description);
                        if(typeid.equals("2")){
                            biaoqian.setText("项目");
                        }
                        if(litpic == null || (litpic != null && litpic.equals(""))){
                            youtupian.setVisibility(View.GONE);
                            wutupian.setVisibility(View.VISIBLE);
                        }else{
                            youtupian.setVisibility(View.VISIBLE);
                            wutupian.setVisibility(View.GONE);
                        }
//                        Log.d("lizisong", "kkkkkkkk333");
                        listview.addHeaderView(heart);
                        horlist.setAdapter(heartAdapter);
//                        Log.d("lizisong", "kkkkkkkktttttttt");
//                        horlist.setVisibility(View.GONE);
                        if(adapter == null){
//                            Log.d("lizisong", "fdfdfdfdfdfdfdfd");
                            adapter = new Adapter();
                            listview.setAdapter(adapter);
//                            Log.d("lizisong", "ddddddddddddddddddddddddddddddddddddddddd");
                        }else {
                            adapter.notifyDataSetChanged();
                        }
//                        Log.d("lizisong", "kkkkkkkkend");
                        yuyue.setVisibility(View.VISIBLE);
                        if(isShow){
                            yuyue.setVisibility(View.GONE);
                        }
                        yuyue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    Intent intent = new Intent(NewProjectActivity.this, TianXuQiu.class);
                                    intent.putExtra("aid", aid);
                                    intent.putExtra("typeid", typeid);
                                    startActivity(intent);

                                } else{
                                    Intent intent = new Intent(NewProjectActivity.this, MyloginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });

                    }

                }catch (JSONException e){

                }catch (Exception ex){

                }
            }else if(msg.what == 10){
                isState = false;
            }

        }
    };

    class Heart extends BaseAdapter{

        @Override
        public int getCount() {
            return namesList.size();
        }

        @Override
        public Object getItem(int position) {
            return namesList.get(position);
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
                convertView = View.inflate(NewProjectActivity.this, R.layout.heartnames, null);
                holdView.txt = (TextView)convertView.findViewById(R.id.txt);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                holdView.sollect = (AutoRelativeLayout)convertView.findViewById(R.id.sollect);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final String tv = namesList.get(position);
            holdView.txt.setText(tv);
            if( current == position){
                holdView.line.setVisibility(View.VISIBLE);
                holdView.txt.getPaint().setFakeBoldText(true);
                holdView.txt.setTextSize(16);
            }else{
                holdView.line.setVisibility(View.GONE);
                holdView.txt.getPaint().setFakeBoldText(false);
                holdView.txt.setTextSize(14);
            }
//            horlist.setSelection(position);

            holdView.sollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isState = true;
                    current = position;
//                    Log.d("lizisong", "position："+position);
                    final String tv = namesList.get(position);
                    for(int i=0;i<projectShowList.size();i++){
                        NewProjectShow item = projectShowList.get(i);
                        if(item.names != null){
                            if(tv.equals(item.names)){
                                listview.setSelection(i);
                                 notifyDataSetChanged();
                                  handler.sendEmptyMessageDelayed(10, 1000);
                                if(i == 0){
                                  listview.setSelection(i);
                                }
                                break;
                            }
                        }
                    }
                }
            });
            return convertView;
        }
        class HoldView{
            public TextView txt,line;
            public AutoRelativeLayout sollect;
        }
    }


     Posts item0,item1,item2;
    class Adapter extends BaseAdapter{

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
            ViewHold viewHold = null;
            if(convertView == null){
                viewHold = new ViewHold();
                convertView = View.inflate(NewProjectActivity.this, R.layout.newprojectadapter,null);
                viewHold.titlelay = (LinearLayout)convertView.findViewById(R.id.titlelay);
                viewHold.title = (TextView)convertView.findViewById(R.id.title);
                viewHold.content = (TextView)convertView.findViewById(R.id.content);
                viewHold.jiange = (LinearLayout)convertView.findViewById(R.id.jiange);
                viewHold.contentlay = (LinearLayout)convertView.findViewById(R.id.contentlay);
                viewHold.line  =(TextView)convertView.findViewById(R.id.line);
                viewHold.contentlay_line = (TextView)convertView.findViewById(R.id.contentlay_line);
                viewHold.wbviw = (WebView)convertView.findViewById(R.id.wbviw);
                viewHold.hezuofangshi = (LinearLayout)convertView.findViewById(R.id.hezuofangshi);
                viewHold.hezuolay1 = (LinearLayout)convertView.findViewById(R.id.hezuolay1);
                viewHold.hezuolay2 = (LinearLayout)convertView.findViewById(R.id.hezuolay2);
                viewHold.txt1  =(TextView)convertView.findViewById(R.id.txt1);
                viewHold.txt2  =(TextView)convertView.findViewById(R.id.txt2);
                viewHold.txt3  =(TextView)convertView.findViewById(R.id.txt3);
                viewHold.txt4  =(TextView)convertView.findViewById(R.id.txt4);
                viewHold.txt5  =(TextView)convertView.findViewById(R.id.txt5);
                viewHold.txt6  =(TextView)convertView.findViewById(R.id.txt6);
                viewHold.txt7  =(TextView)convertView.findViewById(R.id.txt7);
                viewHold.txt8  =(TextView)convertView.findViewById(R.id.txt8);
                viewHold.txt9  =(TextView)convertView.findViewById(R.id.txt9);
                viewHold.txt10  =(TextView)convertView.findViewById(R.id.txt10);
                viewHold.title_hezuo = (TextView)convertView.findViewById(R.id.title_hezuo);
                viewHold.xiangmu_lay = (LinearLayout)convertView.findViewById(R.id.xiangmu_lay);
                viewHold.biaoti = (TextView)convertView.findViewById(R.id.biaoti);
                viewHold.more_info = (TextView)convertView.findViewById(R.id.more_info);
                viewHold.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                viewHold.xiangmu_lay2 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay2);
                viewHold.xiangmu_lay3 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay3);
                viewHold.xianmgmu1 =(ImageView)convertView.findViewById(R.id.xianmgmu1);
                viewHold.xianmgmu2 =(ImageView)convertView.findViewById(R.id.xianmgmu2);
                viewHold.xianmgmu3 =(ImageView)convertView.findViewById(R.id.xianmgmu3);
                viewHold.xmtitle1 =(TextView)convertView.findViewById(R.id.xmtitle1);
                viewHold.xmtitle2 =(TextView)convertView.findViewById(R.id.xmtitle2);
                viewHold.xmtitle3 =(TextView)convertView.findViewById(R.id.xmtitle3);
                viewHold.lanyuan1 =(TextView)convertView.findViewById(R.id.lanyuan1);
                viewHold.lanyuan2 =(TextView)convertView.findViewById(R.id.lanyuan2);
                viewHold.lanyuan3 =(TextView)convertView.findViewById(R.id.lanyuan3);
                viewHold.laycontent = (TextView)convertView.findViewById(R.id.laycontent);
                viewHold.imagecontent = (ImageView)convertView.findViewById(R.id.imagecontent);
                viewHold.titleshow = (LinearLayout)convertView.findViewById(R.id.titleshow);
                viewHold.titlestr = (TextView)convertView.findViewById(R.id.titlestr);
                viewHold.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);
                viewHold.xiangmuline1 = (TextView)convertView.findViewById(R.id.xiangmuline1);
                viewHold.xiangmuline2 = (TextView)convertView.findViewById(R.id.xiangmuline2);
                viewHold.xiangmuline3 = (TextView)convertView.findViewById(R.id.xiangmuline3);
                viewHold.str1 = (TextView)convertView.findViewById(R.id.str1);
                viewHold.str2 = (TextView)convertView.findViewById(R.id.str2);
                viewHold.str3 = (TextView)convertView.findViewById(R.id.str3);
                viewHold.source1 = (TextView)convertView.findViewById(R.id.source1);
                viewHold.source2 = (TextView)convertView.findViewById(R.id.source2);
                viewHold.source3 = (TextView)convertView.findViewById(R.id.source3);
                convertView.setTag(viewHold);
            }else{
                viewHold = (ViewHold)convertView.getTag();
            }
            final NewProjectShow item = projectShowList.get(position);
            if(item.type.equals("-1")){
                viewHold.jiange.setVisibility(View.VISIBLE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
            }else if(item.type.equals("0")){
//                Log.d("lizisong", "0000000000");
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.VISIBLE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.content.setVisibility(View.VISIBLE);
                viewHold.wbviw.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.title.setText(item.name);
                viewHold.contentlay_line.setVisibility(View.VISIBLE);
                String [] temp = item.content.split("\r\n");
//                Log.d("lizisong","item.content:"+item.content);
                String content ="";
                try {
                    for(int i=0; i<temp.length; i++){
                        String line = Html.fromHtml(temp[i]).toString();
                        if(line.equals("")){
                            continue;
                        }
                        content=content+line.replaceAll("\r\n","").replaceAll("\n","")+"\r\n";
                    }
                }catch (Exception e){

                }
//                Log.d("lizisong","item.content2:"+content);

                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
//                        Log.d("lizisong", "pos:"+pos.keyword);
//                        counts.clear();
                        setSpecifiedTextsColor(viewHold.content,content,pos,styledText);
                    }
                    viewHold.content.setText(styledText);

                }else{
                    viewHold.content.setText(content);
                }
//                Log.d("lizisong", "KEYEND");

            }else if(item.type.equals("1")){
//                Log.d("lizisong", "11111111111");
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.VISIBLE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.content.setVisibility(View.GONE);
                viewHold.wbviw.setVisibility(View.VISIBLE);
                viewHold.contentlay_line.setVisibility(View.VISIBLE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.title.setText(item.name);
                viewHold.content.setText(item.content);
                //html转成string字符串
                viewHold.wbviw.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
                viewHold.wbviw.setHapticFeedbackEnabled(false);
                viewHold.wbviw.getSettings().setSupportZoom(false);
                viewHold.wbviw.getSettings().setDomStorageEnabled(false);
                viewHold.wbviw.getSettings().setSupportMultipleWindows(true);
                viewHold.wbviw.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

                try {
                    str = new String(ZixunDetailsActivity.toByteArray(getAssets().open("newproject1.html")));
                    str  = str.replace("<---replace-->",item.content);
                    viewHold.wbviw.loadDataWithBaseURL("file:///android_asset/", str, "text/html",
                            "utf-8", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(item.type.equals("2")){
//                Log.d("lizisong", "222222222222");
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.VISIBLE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.title_hezuo.setText(item.name);
                for(int i=0; i<item.cooperation.size();i++){
                    if(i==0){
                        viewHold.txt1.setVisibility(View.VISIBLE);
                        viewHold.txt1.setText(item.cooperation.get(i));
                    }else if(i== 1){
                        viewHold.txt2.setVisibility(View.VISIBLE);
                        viewHold.txt2.setText(item.cooperation.get(i));
                    }else if(i== 2){
                        viewHold.txt3.setVisibility(View.VISIBLE);
                        viewHold.txt3.setText(item.cooperation.get(i));
                    }else if(i== 3){
                        viewHold.txt4.setVisibility(View.VISIBLE);
                        viewHold.txt4.setText(item.cooperation.get(i));
                    }else if(i== 4){
                        viewHold.txt5.setVisibility(View.VISIBLE);
                        viewHold.txt5.setText(item.cooperation.get(i));
                    }else if(i== 5){
                        viewHold.txt6.setVisibility(View.VISIBLE);
                        viewHold.txt6.setText(item.cooperation.get(i));
                    }else if(i== 6){
                        viewHold.txt7.setVisibility(View.VISIBLE);
                        viewHold.txt7.setText(item.cooperation.get(i));
                    }else if(i== 7){
                        viewHold.txt8.setVisibility(View.VISIBLE);
                        viewHold.txt8.setText(item.cooperation.get(i));
                    }else if(i== 8){
                        viewHold.txt9.setVisibility(View.VISIBLE);
                        viewHold.txt9.setText(item.cooperation.get(i));
                    }else if(i== 9){
                        viewHold.txt10.setVisibility(View.VISIBLE);
                        viewHold.txt10.setText(item.cooperation.get(i));
                    }
                }

            }else if(item.type.equals("3")){
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.VISIBLE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.xiangmuline1.setVisibility(View.GONE);
                viewHold.xiangmuline2.setVisibility(View.GONE);
                viewHold.xiangmuline3.setVisibility(View.GONE);
                viewHold.biaoti.setText(item.name);

                if(item.project.size() == 1){
                    viewHold.xiangmu_lay1.setVisibility(View.VISIBLE);
                    viewHold.xiangmu_lay2.setVisibility(View.GONE);
                    viewHold.xiangmu_lay3.setVisibility(View.GONE);
                    item0 =item.project.get(0);
                    ImageLoader.getInstance().displayImage(item0.getLitpic()
                            , viewHold.xianmgmu1, options);
                    viewHold.xmtitle1.setText(item0.getTitle());
                    viewHold.lanyuan1.setText(item0.getArea_cate().getArea_cate1());

                    if(item0.labels != null && (!item0.labels.equals(""))){
                        viewHold.source1.setVisibility(View.VISIBLE);
                        viewHold.source1.setText(item0.labels);
                        if(item0.labels.equals("精品项目")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item0.labels.equals("钛领推荐")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item0.labels.equals("国家科学基金")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source1.setVisibility(View.GONE);
                    }
                    if(item0.str != null && !item0.str.equals("")){
                        viewHold.str1.setVisibility(View.VISIBLE);
                        viewHold.xiangmuline1.setVisibility(View.VISIBLE);
                        viewHold.str1.setText(item0.str);
                    }else {
                        viewHold.str1.setVisibility(View.GONE);
                        viewHold.xiangmuline1.setVisibility(View.GONE);
                    }
                    viewHold.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item0.getTypename().equals("html")&& item0.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item0.getTitle());
                                intent.putExtra("url",item0.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item0.aid);
                                startActivity(intent);
                            }

                        }
                    });
                }else if(item.project.size() == 2){
                    viewHold.xiangmu_lay1.setVisibility(View.VISIBLE);
                    viewHold.xiangmu_lay2.setVisibility(View.VISIBLE);
                    viewHold.xiangmu_lay3.setVisibility(View.GONE);
                     item0 =item.project.get(0);
                    ImageLoader.getInstance().displayImage(item0.getLitpic()
                            , viewHold.xianmgmu1, options);
                    viewHold.xmtitle1.setText(item0.getTitle());
                    viewHold.lanyuan1.setText(item0.getArea_cate().getArea_cate1());
                    if(item0.labels != null && (!item0.labels.equals(""))){
                        viewHold.source1.setVisibility(View.VISIBLE);
                        viewHold.source1.setText(item0.labels);
                        if(item0.labels.equals("精品项目")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item0.labels.equals("钛领推荐")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item0.labels.equals("国家科学基金")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source1.setVisibility(View.GONE);
                    }
                    if(item0.str != null && !item0.str.equals("")){
                        viewHold.str1.setVisibility(View.VISIBLE);
                        viewHold.str1.setText(item0.str);
                        viewHold.xiangmuline1.setVisibility(View.VISIBLE);
                    }else {
                        viewHold.str1.setVisibility(View.GONE);
                        viewHold.xiangmuline1.setVisibility(View.GONE);
                    }

                     item1 =item.project.get(1);
                    ImageLoader.getInstance().displayImage(item1.getLitpic()
                            , viewHold.xianmgmu2, options);
                    viewHold.xmtitle2.setText(item1.getTitle());
                    viewHold.lanyuan2.setText(item1.getArea_cate().getArea_cate1());
                    if(item1.labels != null && (!item1.labels.equals(""))){
                        viewHold.source2.setVisibility(View.VISIBLE);
                        viewHold.source2.setText(item1.labels);
                        if(item1.labels.equals("精品项目")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item1.labels.equals("钛领推荐")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item1.labels.equals("国家科学基金")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source2.setVisibility(View.GONE);
                    }
                    if(item1.str != null && !item1.str.equals("")){
                        viewHold.str2.setVisibility(View.VISIBLE);
                        viewHold.str2.setText(item1.str);
                        viewHold.xiangmuline2.setVisibility(View.VISIBLE);
                    }else {
                        viewHold.str2.setVisibility(View.GONE);
                        viewHold.xiangmuline2.setVisibility(View.GONE);
                    }
                    viewHold.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item0.getTypename().equals("html")&& item0.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item0.getTitle());
                                intent.putExtra("url",item0.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item0.aid);
                                startActivity(intent);
                            }

                        }
                    });
                    viewHold.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item1.getTypename().equals("html")&& item1.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item1.getTitle());
                                intent.putExtra("url", item1.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item1.aid);
                                startActivity(intent);
                            }
//                        Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
//                        intent.putExtra("aid", item.project.get(1).aid);
//                        startActivity(intent);
                        }
                    });

                }else if(item.project.size() >2){
                    viewHold.xiangmu_lay1.setVisibility(View.VISIBLE);
                    viewHold.xiangmu_lay2.setVisibility(View.VISIBLE);
                    viewHold.xiangmu_lay3.setVisibility(View.VISIBLE);
                    item0 =item.project.get(0);
                    ImageLoader.getInstance().displayImage(item0.getLitpic()
                            , viewHold.xianmgmu1, options);
                    viewHold.xmtitle1.setText(item0.getTitle());
                    viewHold.lanyuan1.setText(item0.getArea_cate().getArea_cate1());
                    if(item0.labels != null && (!item0.labels.equals(""))){
                        viewHold.source1.setVisibility(View.VISIBLE);
                        viewHold.source1.setText(item0.labels);
                        if(item0.labels.equals("精品项目")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item0.labels.equals("钛领推荐")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item0.labels.equals("国家科学基金")){
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source1.setVisibility(View.GONE);
                    }
                    if(item0.str != null && !item0.str.equals("")){
                        viewHold.str1.setVisibility(View.VISIBLE);
                        viewHold.str1.setText(item0.str);
                        viewHold.xiangmuline1.setVisibility(View.VISIBLE);
                    }else {
                        viewHold.str1.setVisibility(View.GONE);
                        viewHold.xiangmuline1.setVisibility(View.GONE);
                    }

                    item1 =item.project.get(1);
                    ImageLoader.getInstance().displayImage(item1.getLitpic()
                            , viewHold.xianmgmu2, options);
                    viewHold.xmtitle2.setText(item1.getTitle());
                    viewHold.lanyuan2.setText(item1.getArea_cate().getArea_cate1());
                    if(item1.labels != null && (!item1.labels.equals(""))){
                        viewHold.source2.setVisibility(View.VISIBLE);
                        viewHold.source2.setText(item1.labels);
                        if(item1.labels.equals("精品项目")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item1.labels.equals("钛领推荐")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item1.labels.equals("国家科学基金")){
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source2.setVisibility(View.GONE);
                    }
                    if(item1.str != null && !item1.str.equals("")){
                        viewHold.str2.setVisibility(View.VISIBLE);
                        viewHold.str2.setText(item1.str);
                        viewHold.xiangmuline2.setVisibility(View.VISIBLE);
                    }else {
                        viewHold.str2.setVisibility(View.GONE);
                        viewHold.xiangmuline2.setVisibility(View.GONE);
                    }
                    item2 =item.project.get(2);
                    ImageLoader.getInstance().displayImage(item2.getLitpic()
                            , viewHold.xianmgmu3, options);
                    viewHold.xmtitle3.setText(item2.getTitle());
                    viewHold.lanyuan3.setText(item2.getArea_cate().getArea_cate1());
                    if(item2.labels != null && (!item2.labels.equals(""))){
                        viewHold.source3.setVisibility(View.VISIBLE);
                        viewHold.source3.setText(item1.labels);
                        if(item2.labels.equals("精品项目")){
                            viewHold.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(item2.labels.equals("钛领推荐")){
                            viewHold.source3.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(item2.labels.equals("国家科学基金")){
                            viewHold.source3.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            viewHold.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        viewHold.source3.setVisibility(View.GONE);
                    }
                    if(item2.str != null && !item2.str.equals("")){
                        viewHold.str3.setVisibility(View.VISIBLE);
                        viewHold.str3.setText(item1.str);
                        viewHold.xiangmuline3.setVisibility(View.VISIBLE);
                    }else {
                        viewHold.str3.setVisibility(View.GONE);
                        viewHold.xiangmuline3.setVisibility(View.GONE);
                    }

                    viewHold.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item0.getTypename().equals("html")&& item0.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item0.getTitle());
                                intent.putExtra("url",item0.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item0.aid);
                                startActivity(intent);
                            }

                        }
                    });
                    viewHold.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item1.getTypename().equals("html")&& item1.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item1.getTitle());
                                intent.putExtra("url", item1.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item1.aid);
                                startActivity(intent);
                            }
//                        Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
//                        intent.putExtra("aid", item.project.get(1).aid);
//                        startActivity(intent);
                        }
                    });
                    viewHold.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(item2.getTypename().equals("html")&& item2.typeid.equals("2")){
                                Intent intent=new Intent(NewProjectActivity.this, ActiveActivity.class);
                                intent.putExtra("title", item2.getTitle());
                                intent.putExtra("url", item2.url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", item2.aid);
                                startActivity(intent);
                            }
//                        Intent intent = new Intent(NewProjectActivity.this, NewProjectActivity.class);
//                        intent.putExtra("aid", item.project.get(2).aid);
//                        startActivity(intent);
                        }
                    });
                }


                viewHold.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewProjectActivity.this, LookMoreActivity.class);
                        intent.putExtra("typeid",2);
                        intent.putExtra("title","更多项目");
                        intent.putExtra("channel","项目");
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("4")){
//                Log.d("lizisong", "444444444444444");
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.VISIBLE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                viewHold.titlestr.setText(item.name);
            }else if(item.type.equals("401")){
//                Log.d("lizisong", "401401401401401401401401401");
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.VISIBLE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.str);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
//                        counts.clear();
                        setSpecifiedTextsColor(viewHold.laycontent,item.str,pos,styledText);
                    }
                    viewHold.laycontent.setText(styledText);

                }else{
                    viewHold.laycontent.setText(item.str);
                }
//                viewHold.laycontent.setText(item.str);
            }else if(item.type.equals("402")){
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.str
                        , viewHold.imagecontent, options);
            }else if(item.type.equals("-2")){
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.GONE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.VISIBLE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
            }else if(item.type.equals("-3")){
                viewHold.jiange.setVisibility(View.GONE);
                viewHold.contentlay.setVisibility(View.GONE);
                viewHold.hezuofangshi.setVisibility(View.GONE);
                viewHold.dixian.setVisibility(View.VISIBLE);
                viewHold.xiangmu_lay.setVisibility(View.GONE);
                viewHold.contentlay_line.setVisibility(View.GONE);
                viewHold.titleshow.setVisibility(View.GONE);
                viewHold.laycontent.setVisibility(View.GONE);
                viewHold.imagecontent.setVisibility(View.GONE);
            }

            return convertView;
        }
//        List<QuJian> counts = new ArrayList<>();

        public void setSpecifiedTextsColor(TextView txtView,String text, KeyWord specifiedTexts, SpannableStringBuilder styledText){

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
        class QuJian{
            int start;
            int end;
        }
        class ViewHold{
            //title
             public LinearLayout titlelay,contentlay;
             public TextView title,contentlay_line;
            //内容
            public TextView content;
            //间隔
            public LinearLayout jiange;
            // 线
            public TextView line;
            //WebView
            public WebView wbviw;
            //合作方式
            public LinearLayout hezuofangshi,hezuolay1,hezuolay2;
            public TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,title_hezuo;
            //
            public LinearLayout xiangmu_lay;
            public TextView biaoti,more_info;
            public RelativeLayout xiangmu_lay1,xiangmu_lay2,xiangmu_lay3;
            public ImageView xianmgmu1,xianmgmu2,xianmgmu3;
            public TextView xmtitle1,lanyuan1,xmtitle2,lanyuan2,xmtitle3,lanyuan3;

            public TextView laycontent;
            public ImageView imagecontent;
            public LinearLayout titleshow;
            public TextView titlestr;
            public RelativeLayout dixian;

            public TextView xiangmuline1,str1,source1;
            public TextView xiangmuline2,str2,source2;
            public TextView xiangmuline3,str3,source3;


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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    class KeyWord {
        public String keyword;
        public String typeid;
        public String aid;
        public String color;
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
                Intent intent = new Intent(NewProjectActivity.this, NewSearchContent.class);
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
                    Intent intent=new Intent(NewProjectActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(NewProjectActivity.this, XinFanAnCeShi.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(NewProjectActivity.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewProjectActivity.this, NewSearchContent.class);
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
                image = new UMImage(this,litpic);
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
            Toast.makeText(NewProjectActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
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
