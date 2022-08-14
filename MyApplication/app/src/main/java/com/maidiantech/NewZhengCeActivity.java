package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.widget.Button;
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
import entity.NewProjectShow;
import entity.Posts;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;

/**
 * Created by Administrator on 2018/12/26.
 */

public class NewZhengCeActivity extends AutoLayoutActivity {
    private DisplayImageOptions options;
    boolean isShow = false;
    SystemBarTintManager tintManager;
    View heart;
    LinearLayout titleLay;
    TextView titlecontent,bottmon_title,titledes;
    ImageView yujian_backs,backs;
    Button yuyue;
    Adapter adapter;
    ProgressBar progress;
    RefreshListView listview;
    List<KeyWord> keys = new ArrayList<>();
    List<NewProjectShow> projectShowList = new ArrayList<>();
    String aid;
    String id,title,typeid,typename,litpic,description,kewords="";
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
        setContentView(R.layout.newzhengceactivity);
        options = ImageLoaderUtils.initOptions();
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        isShow = getIntent().getBooleanExtra("isShow", false);
        tintManager = new SystemBarTintManager(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);

        heart =  View.inflate(NewZhengCeActivity.this, R.layout.newzhengceheart, null);
        titleLay = (LinearLayout)findViewById(R.id.title);
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        yuyue = (Button)findViewById(R.id.yuyue);
        progress = (ProgressBar)findViewById(R.id.progress);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        backs = heart.findViewById(R.id.backs);
        titledes = heart.findViewById(R.id.titledes);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (RefreshListView)findViewById(R.id.listview);
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
        aid = getIntent().getStringExtra("aid");
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
                      //  isherat = true;
                    }
                }else{
                   // isherat = false;
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

                    }else {
//                            title.getBackground().setAlpha(290-25-h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        titleLay.setVisibility(View.GONE);
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
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(NewZhengCeActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            getJson(aid,true);
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
    private void getJson(String aid, boolean state){
        String url= "http://"+ MyApplication.ip+"/api/arc_detail_policy.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("typeid","6");
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
            try {
                progress.setVisibility(View.GONE);
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
                        title=title.replaceAll("\n","").replaceAll(" ","").replaceAll("\r\n","");
                        typeid = jsonData.getString("typeid");
                        typename = jsonData.getString("typename");
                        litpic = jsonData.getString("litpic");
                        description = jsonData.getString("description");
                        titledes.setText(title);
                        titlecontent.setText(title);
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
                        JSONArray jsonprojectfield = new JSONArray(project_field);
                        NewProjectShow jiange = new NewProjectShow();
                        jiange.type = "-1";
                        jiange.names="";
                        projectShowList.add(jiange);
                        for(int i=0;i<jsonprojectfield.length();i++){

                            JSONObject item = jsonprojectfield.getJSONObject(i);
                            NewProjectShow pos = new NewProjectShow();
                            pos.type = item.getString("type");
                            pos.name = item.getString("name");
                            pos.names = item.getString("names");
                            if(pos.type.equals("0")){
                                pos.content = item.getString("content");
                                projectShowList.add(pos);
                            }else if(pos.type.equals("4")){
                                projectShowList.add(pos);
                                JSONArray arrystr = item.getJSONArray("content");
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
                                    itemp.name=pos.name;
                                    itemp.names=pos.names;
                                    projectShowList.add(itemp);
                                }
                            }else if(pos.type.equals("2")){
                                NewProjectShow jiange1 = new NewProjectShow();
                                jiange1.type = "-1";
                                jiange1.names="";
                                projectShowList.add(jiange1);
                                projectShowList.add(pos);
                                JSONArray array = item.getJSONArray("content");
                                for(int j=0;j<array.length();j++){
                                    JSONObject object = array.getJSONObject(j);
                                    NewProjectShow zhuanli = new NewProjectShow();
                                    zhuanli.type="5";
                                    zhuanli.item = new Posts();
                                    zhuanli.item.aid = object.getString("aid");
                                    zhuanli.item.typeid = object.getString("typeid");
                                    zhuanli.item.setTitle(object.getString("title"));
                                    try {
                                        zhuanli.item.areacate =  object.getString("area_cate");
                                    }catch (JSONException EX){

                                    }catch (Exception e){

                                    }

                                    zhuanli.item.setDescription(object.getString("description"));
                                    try {
                                        zhuanli.item.str =object.getString("str");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }
                                    projectShowList.add(zhuanli);

                                }
                            }else if(pos.type.equals("3")){
                                pos.cooperation = new ArrayList<>();
                                JSONArray object = item.getJSONArray("content");
                                for(int j=0;j<object.length();j++){
                                    String txt = (String) object.get(j);
                                    pos.cooperation.add(txt);
                                }
                                projectShowList.add(pos);
                            }
                        }

                        NewProjectShow dianxian = new NewProjectShow();
                        dianxian.type="-3";
                        projectShowList.add(dianxian);

                    }

                    if(adapter == null){
                        adapter = new Adapter();
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
            HodView holdView;
            if(convertView == null){
                convertView = View.inflate(NewZhengCeActivity.this, R.layout.newzhengceadapter, null);
                holdView = new HodView();
                holdView.contentlay = convertView.findViewById(R.id.contentlay);
                holdView.title = convertView.findViewById(R.id.title);
                holdView.content = convertView.findViewById(R.id.content);
                holdView.contentlay_line = convertView.findViewById(R.id.contentlay_line);
                holdView.jiange = convertView.findViewById(R.id.jiange);
                holdView.zhuanli_lay = convertView.findViewById(R.id.zhuanli_lay);
                holdView.zhuanli_layout = convertView.findViewById(R.id.zhuanli_layout);
                holdView.zhunli_title = convertView.findViewById(R.id.zhunli_title);
                holdView.zhuanli_linyu = convertView.findViewById(R.id.zhuanli_linyu);
                holdView.str = convertView.findViewById(R.id.str);
                holdView.dixian = convertView.findViewById(R.id.dixian);
                holdView.biaoti = convertView.findViewById(R.id.biaoti);
                holdView.titleshow = convertView.findViewById(R.id.titleshow);
                holdView.titlestr = convertView.findViewById(R.id.titlestr);
                holdView.more_info = convertView.findViewById(R.id.more_info);
                holdView.laycontent = convertView.findViewById(R.id.laycontent);
                holdView.imagecontent = convertView.findViewById(R.id.imagecontent);
                convertView.setTag(holdView);

            }else {
                holdView = (HodView) convertView.getTag();
            }
            final NewProjectShow item =projectShowList.get(position);
            if(item.type.equals("-1")){
                holdView.jiange.setVisibility(View.VISIBLE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);

            }else if(item.type.equals("0")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.VISIBLE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
                holdView.title.setText(item.name);
//                holdView.content.setText(item.content);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
//                        counts.clear();
                        setSpecifiedTextsColor(holdView.content,item.content,pos,styledText);
                    }
                    holdView.content.setText(styledText);

                }else{
                    holdView.content.setText(item.content);
                }
            }else if(item.type.equals("3")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.VISIBLE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
                holdView.title.setText(item.name);
                String content="";
                if(item.cooperation != null){
                    for (int i=0; i<item.cooperation.size(); i++){
                        String str = item.cooperation.get(i);
                        if(i==item.cooperation.size()-1){
                            content = content+str;
                        }else{
                            content=content+str+"\n";
                        }
                    }
                }
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
//                        counts.clear();
                        setSpecifiedTextsColor(holdView.content,content,pos,styledText);
                    }
                    holdView.content.setText(styledText);

                }else{
                    holdView.content.setText(content);
                }
            }else if(item.type.equals("4")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
                holdView.titlestr.setText(item.name);
            }else if(item.type.equals("401")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.VISIBLE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
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
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.VISIBLE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.str
                        , holdView.imagecontent, options);
            }else if(item.type.equals("2")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.VISIBLE);
                holdView.biaoti.setText(item.name);
                holdView.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else if(item.type.equals("5")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
                holdView.zhunli_title.setText(item.item.getTitle());
                holdView.zhuanli_linyu.setText(item.item.areacate);
                holdView.str.setText(item.item.str);
                holdView.zhuanli_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewZhengCeActivity.this, NewZhengCeActivity.class);
                        intent.putExtra("aid", item.item.aid);
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("-3")){
                holdView.jiange.setVisibility(View.GONE);
                holdView.contentlay_line.setVisibility(View.GONE);
                holdView.contentlay.setVisibility(View.GONE);
                holdView.zhuanli_layout.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
                holdView.titleshow.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.imagecontent.setVisibility(View.GONE);
                holdView.zhuanli_lay.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HodView {
            public LinearLayout contentlay;
            public TextView title;
            public TextView content;

            public TextView contentlay_line;

            public LinearLayout jiange;

            public LinearLayout zhuanli_lay;
            public TextView biaoti,more_info;

            public LinearLayout zhuanli_layout;
            public TextView zhunli_title,zhuanli_linyu,str;

            public RelativeLayout dixian;

            public LinearLayout titleshow;
            public TextView titlestr,laycontent;
            public ImageView imagecontent;
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
    class TextClick extends ClickableSpan {
        KeyWord mKeyWord;
        public TextClick(KeyWord item){
            mKeyWord = item;
        }

        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            if(mKeyWord.aid == null || (mKeyWord.aid != null && mKeyWord.aid.equals(""))){
                Intent intent = new Intent(NewZhengCeActivity.this, NewSearchContent.class);
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
                    Intent intent=new Intent(NewZhengCeActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(NewZhengCeActivity.this, NewRenCaiTail.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(NewZhengCeActivity.this, NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewZhengCeActivity.this, NewSearchContent.class);
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
