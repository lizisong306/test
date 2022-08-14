package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import Util.SharedPreferencesUtil;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import application.MyApplication;
import entity.HistroyShowEntity;
import entity.NewSearchHistoryEntiy;
import view.AutoLinefeedLayout;
import view.MyLinearLayout;
import view.StyleUtils;
import view.SystemBarTintManager;
import Util.NetUtils;
import Util.TelNumMatch;
/**
 * Created by Administrator on 2018/9/21.
 */

public class NewSearchHistory extends AutoLayoutActivity {
    String redianStr;
    List<String> poslist = new ArrayList<>();
    List<String> xiangshi = new ArrayList<>();
    NewSearchHistoryEntiy data;
    String hintStr;
    EditText titledes;
    ImageView closesousuo;
    TextView cencel,bottmon_title;
    ListView listview;
    NewHotAdapter hotAdapter ;
    LinearLayout contentlay;
    AutoLinefeedLayout hotView;
    AutoLinefeedLayout contentView;
    ProgressBar progress;
    String trim=null;
    private TextView hottitle,hotline,titlehistroyline;
    RelativeLayout titlehistroy;
    ImageView close;
    String typeid;
    public static boolean isClose = false;
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
        setContentView(R.layout.newsearchhistory);
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
        progress = (ProgressBar)findViewById(R.id.progress);
        titledes = (EditText) findViewById(R.id.edit) ;
        contentView = (AutoLinefeedLayout) findViewById(R.id.contentView);
        typeid = getIntent().getStringExtra("typeid");
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);
        contentlay =(LinearLayout)findViewById(R.id.contentlay);
        hottitle = (TextView)findViewById(R.id.hottitle);
        isClose = false;
        hotline  = (TextView)findViewById(R.id.hotline);
        closesousuo = (ImageView)findViewById(R.id.closesousuo);
        closesousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titledes.setText("");
            }
        });
        titlehistroy = (RelativeLayout)findViewById(R.id.titlehistroy);
        titlehistroyline = (TextView)findViewById(R.id.titlehistroyline);
        close = (ImageView)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
                poslist.clear();
                contentView.removeAllViews();
                titlehistroy.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                titlehistroyline.setVisibility(View.GONE);
            }
        });
        titledes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp =  s.toString();
                if(temp != null && !temp.equals("")){
                    getReCi(temp);
                    titledes.setSelection(temp.length());
                }else{
                    listview.setVisibility(View.GONE);
                    contentlay.setVisibility(View.VISIBLE);
//                    if(s.toString().length()>0){
//                        titledes.setSelection(s.length());
//                    }

                }
                if(temp == null || (temp != null &&temp.equals(""))){
                    closesousuo.setVisibility(View.GONE);
                }else {
                    closesousuo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        titledes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = titledes.getText().toString().trim();
                    int  netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(NewSearchHistory.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(NewSearchHistory.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(trim)==false){
                            Toast.makeText(NewSearchHistory.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                boolean state =false;
                                for (int i=0; i<poslist.size();i++){
                                    String temp = poslist.get(i);
                                    if(temp.equals(trim)){
                                        state = true;
                                        break;
                                    }
                                }
                               if(!state){
                                   poslist.add(0,trim);
                               }
                                redianStr="";
                                for (int i=0;i<poslist.size();i++){
                                    String temp=poslist.get(i);
                                    if(i==poslist.size()-1){
                                        redianStr=redianStr+";"+temp;
                                    }else{
                                        redianStr=redianStr+";"+temp;
                                    }
                                }
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
                                initView();
                                titledes.setText("");
                                Intent intent = new Intent(NewSearchHistory.this, NewSearchContent.class);
                                intent.putExtra("hot", trim);
                                intent.putExtra("typeid", typeid);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        cencel = (TextView)findViewById(R.id.cencel);
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
            }
        });
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
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
        listview = (ListView)findViewById(R.id.listview);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                         hintKbTwo();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        contentlay.setVisibility(View.VISIBLE);

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

    public boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
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



    @Override
    protected void onResume() {
        super.onResume();
        if(isClose){
            finish();
            return;
        }
        listview.setVisibility(View.GONE);
        contentlay.setVisibility(View.VISIBLE);
        initView();
        getJson("");
    }

    /**
     * 获取热点数据接口
     */
    private void getJson(String key){
        String url="http://"+MyApplication.ip+"/api/hotWords.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",key);
        networkCom.getJson(url,map,handler,1,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取热词的接口
     */
    private void getReCi(String key){
        String url="http://"+MyApplication.ip+"/api/hotWords.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",key);
        networkCom.getJson(url,map,handler,2,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    String ret;
                    ret=(String)msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, NewSearchHistoryEntiy.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            hintStr = data.data.str;
                            titledes.setHint(hintStr);
//                            if(hintStr!= null && hintStr.length()>0){
//                                titledes.setSelection(hintStr.length());
//                            }

                            if(data.data.hotkey != null && data.data.hotkey.size()>0){
                                hottitle.setVisibility(View.VISIBLE);
                                hotline.setVisibility(View.VISIBLE);
                                inithotView();
                            }else{
                                hottitle.setVisibility(View.GONE);
                                hotline.setVisibility(View.GONE);
                            }
                        }
                    }
                }else if(msg.what == 2){
                    String ret;
                    ret=(String)msg.obj;
                    Gson gson = new Gson();
                    xiangshi.clear();
                    data = gson.fromJson(ret, NewSearchHistoryEntiy.class);
                    if(data != null){
                       if(data.data.xiangsi != null && data.data.xiangsi.size()>0){
                           listview.setVisibility(View.VISIBLE);
                           contentlay.setVisibility(View.INVISIBLE);
                           for (int i=0; i<data.data.xiangsi.size();i++){
                                String temp = data.data.xiangsi.get(i);
                               xiangshi.add(temp);
                           }
                       }else{
                           listview.setVisibility(View.GONE);
                           contentlay.setVisibility(View.VISIBLE);
                       }
                    }else{
                        listview.setVisibility(View.GONE);
                        contentlay.setVisibility(View.VISIBLE);
                    }
                    if(hotAdapter == null){
                        hotAdapter = new NewHotAdapter();
                        listview.setAdapter(hotAdapter);
                    }else{
                        hotAdapter.notifyDataSetChanged();
                    }
                }
            }catch (Exception e){

            }

        }
    };

    class NewHotAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return xiangshi.size();
        }

        @Override
        public Object getItem(int position) {
            return xiangshi.get(position);
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
                convertView = View.inflate(NewSearchHistory.this, R.layout.reciapater, null);
                holdView.reci = (TextView)convertView.findViewById(R.id.reci);
                holdView.line = (TextView)convertView.findViewById(R.id.line);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
           final String temp = xiangshi.get(position);
            holdView.reci.setText(temp);
            holdView.reci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addHistory(temp);
                    titledes.setText("");
                    Intent intent = new Intent(NewSearchHistory.this, NewSearchContent.class);
                    intent.putExtra("hot",temp);
                    intent.putExtra("typeid", typeid);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
            try {
                if(xiangshi.size()-1 == position){
                    holdView.line.setVisibility(View.GONE);
                }else{
                    holdView.line.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }

            return convertView;
        }
        class  HoldView {
            public TextView reci;
            public TextView line;
        }
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void initView(){
        redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,null);
        if(redianStr == null || (redianStr != null && redianStr.equals(""))){
            titlehistroy.setVisibility(View.GONE);
            contentView.setVisibility(View.GONE);
            titlehistroyline.setVisibility(View.GONE);
            return;
        }
        String[] temp = redianStr.split(";");
        int size = temp.length; // 添加Button的个数.
        contentView.setIsGou(false);
        contentView.setVisibility(View.VISIBLE);
        titlehistroy.setVisibility(View.VISIBLE);
        titlehistroyline.setVisibility(View.VISIBLE);
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        childBtns.clear();
        contentView.removeAllViews();
        poslist.clear();
        if(size >0){
            for (int i=0; i<size;i++){
                String pos=temp[i];
                if(pos != null && !pos.equals("") && !pos.equals(";")){
                    poslist.add(pos);
                }
            }
        }
           if(poslist.size() >0){
            int countwidht =0;
            for(int i = 0; i < poslist.size(); i++){
                String item = poslist.get(i);
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                if(countwidht > 4*MyApplication.widths){
                    break;
                }
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.historyitem, null);
                TextView tv = childBtn.findViewById(R.id.item);
                tv.setText(item);
                tv.setTag(item);
                childBtns.add(childBtn);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView)v;
                        titledes.setText("");
                        addHistory(tv.getText().toString());
                        Intent intent = new Intent(NewSearchHistory.this, NewSearchContent.class);
                        intent.putExtra("hot",tv.getText().toString());
                        intent.putExtra("typeid", typeid);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });
                contentView.addView(childBtn);
           }
            }
           }


    private void inithotView(){
        int size = data.data.hotkey.size(); // 添加Button的个数.
        if(size == 0){
            return;
        }
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        hotView.removeAllViews();

        if(size > 0){
            int countwidht =0;
            for(int i = 0; i < size; i++){
                    String item = data.data.hotkey.get(i);
                    countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                    if(countwidht > 4*MyApplication.widths){
                       break;
                    }
                    LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hotitem, null);
                    TextView tv = (TextView)childBtn.findViewById(R.id.item);
                    tv.setText(item);
                    tv.setTag(item);
                    childBtns.add(childBtn);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv = (TextView)v;
                            addHistory(tv.getText().toString());
                            titledes.setText("");
                            Intent intent = new Intent(NewSearchHistory.this, NewSearchContent.class);
                            intent.putExtra("hot",tv.getText().toString());
                            intent.putExtra("typeid", typeid);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }
                    });

                    hotView.addView(childBtn);
                }
            }
        }

    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
    }

    private void addHistory(String key){
        String temp;
        String redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,null);
        if(redianStr == null || (redianStr != null && redianStr.equals(""))){
            temp=key+";"+SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
            SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,temp);
        }else{
            String[] tem = redianStr.split(";");
            int size = 0;
            if(tem != null){
                size = tem.length;
            }
            if(size >0){
                poslist.clear();
                for (int i=0; i<size;i++){
                    String pos=tem[i];
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        poslist.add(pos);
                    }
                }
                boolean state =false;
                for (int i=0; i<poslist.size();i++){
                    String pos = poslist.get(i);
                    if(pos.equals(key)){
                        state = false;
                        poslist.remove(pos);
                        break;
                    }
                }
                if(!state){
                    poslist.add(0,key);
                }
                redianStr="";
                for (int i=0;i<poslist.size();i++){
                    String pos=poslist.get(i);
                    if(i==poslist.size()-1){
                        redianStr=redianStr+";"+pos;
                    }else{
                        redianStr=redianStr+";"+pos;
                    }
                }
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
            }

        }
    }

}
