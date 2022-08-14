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
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;


import Util.SharedPreferencesUtil;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.Details;
import entity.Posts;
import entity.Ret;
import entity.WriteXuQiuData;
import entity.WriteXuQiuDemo;
import entity.recode;
import view.RoundImageView;
import view.StyleUtils;
import  Util.NetUtils;
import view.SystemBarTintManager;
import static application.MyApplication.deviceid;

/**
 * Created by Administrator on 2018/5/3.
 */

public class WriteXuQiu extends AutoLayoutActivity {

    RelativeLayout title,lay,lay2;
    ImageView shezhi_backs,img;
    TextView titledes,xm_title,xm_title2,linyu2,linyu,ShowCount,look,tijiao,bottmon_title;
    EditText text = null;
    String aid,typeid,json1,json;
    Posts item;
    TextView wutu_rencai;
    private DisplayImageOptions options;
    RelativeLayout tishi;
    RoundImageView img2;
    boolean isshow = false;
    public static int entry_address = 0;
    WriteXuQiuDemo data;
    TextView shili1,shili2,demo1,demo2,shili3,demo3;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isshow = false;
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
////            window.setNavigationBarColor(Color.BLACK);
//        }
//
////        setNavigationBarStatusBarTranslucent(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
//            try {
//                setTranslucentStatus(true);
//                SystemBarTintManager tintManager = new SystemBarTintManager(this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//                tintManager.setStatusBarAlpha(0);
//            }catch (Exception e){
//
//            }
//
//        }
        setContentView(R.layout.writexuqiu);
//        StyleUtils.initSystemBar(this);
////        //设置状态栏是否沉浸
//        StyleUtils.setStyle(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
//            try {
//                setTranslucentStatus(true);
//                SystemBarTintManager tintManager = new SystemBarTintManager(this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//                tintManager.setStatusBarAlpha(0);
//            }catch (Exception e){
//
//            }
//
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        hideStatusNavigationBar();//1
        MainActivity.MIUISetStatusBarLightMode(getWindow(), true);
        lay = (RelativeLayout)findViewById(R.id.lay);
        lay2 = (RelativeLayout)findViewById(R.id.lay2);
        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img = (ImageView)findViewById(R.id.img);
        img2 = (RoundImageView)findViewById(R.id.img2);
        titledes = (TextView)findViewById(R.id.titledes);
        xm_title =(TextView)findViewById(R.id.xm_title);
        wutu_rencai = (TextView)findViewById(R.id.wutu_rencai);
        xm_title2 = (TextView) findViewById(R.id.xm_title2);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        shili1 = (TextView)findViewById(R.id.shili1);
        shili2 = (TextView)findViewById(R.id.shili2);
        shili3 = (TextView)findViewById(R.id.shili3);

        demo1 = (TextView)findViewById(R.id.demo1);
        demo2 = (TextView)findViewById(R.id.demo2);
        demo3 = (TextView)findViewById(R.id.demo3);
//        if (MainActivity.hasSoftKeys(getWindowManager())) {
//            bottmon_title.setVisibility(View.VISIBLE);
//        } else {
//            bottmon_title.setVisibility(View.GONE);
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            bottmon_title.setVisibility(View.GONE);
//        }
//        if(MyApplication.navigationbar >0){
//            ViewGroup.LayoutParams params = bottmon_title.getLayoutParams();
//            params.height=MyApplication.navigationbar;
//            bottmon_title.setLayoutParams(params);
//        }
        linyu2=(TextView)findViewById(R.id.linyu2);
        linyu = (TextView)findViewById(R.id.linyu);
        ShowCount = (TextView)findViewById(R.id.count);
        look = (TextView)findViewById(R.id.look);

        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WriteXuQiu.this, ShiLiDemo.class);
//                startActivity(intent);
                tishi.setVisibility(View.VISIBLE);
                hintKbTwo();
            }
        });
        tijiao = (TextView)findViewById(R.id.tijiao);
        text = (EditText)findViewById(R.id.text);
        text.setImeOptions(EditorInfo.IME_ACTION_SEND);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0 || actionId == 3 || actionId == 5) {

                    int   netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(WriteXuQiu.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{

                        hintKbTwo();
                        String loginstate = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                        if(loginstate.equals("1")){
                            String txt = text.getText().toString();

                            if(txt == null || txt.equals("")){
                                Toast.makeText(WriteXuQiu.this, "请填写约见的主题及诉求",Toast.LENGTH_SHORT).show();
                            }else{
                                if(event.getAction() == KeyEvent.ACTION_UP){
                                    tijiaopost();
                                }
                            }
                        }else{
                            Intent intent = new Intent(WriteXuQiu.this, MyloginActivity.class);
                            startActivity(intent);
                        }
                    }

                    return true;
                }
                return false;
            }
        });
        tishi = (RelativeLayout)findViewById(R.id.tishi);
        tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tishi.setVisibility(View.GONE);
            }
        });
        options = ImageLoaderUtils.initOptions();
        aid = getIntent().getStringExtra("aid");
        typeid = getIntent().getStringExtra("typeid");
        text.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ShowCount.setText(s.toString().length()+"/200字");
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        if(!typeid.equals("0")){
            getJson();
        }else{
            lay.setVisibility(View.GONE);
        }
        getShiLi();
        tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginstate = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(loginstate.equals("1")){
                    String txt = text.getText().toString();
                    if(txt == null || txt.equals("")){
                        Toast.makeText(WriteXuQiu.this, "请填写约见的主题及诉求",Toast.LENGTH_SHORT).show();
                    }else{
                        tijiaopost();
                    }
                }else{
                    Intent intent = new Intent(WriteXuQiu.this, MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//                           public void run() {
//                               InputMethodManager inputManager =
//                                       (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                               inputManager.showSoftInput(text, 0);
//
//                           }
//
//                       },
//                500);

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
    private void getJson(){
        try {
            String url ="http://" + MyApplication.ip + "/api/arc_detail.php";
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("aid", aid);
            map.put("mid" ,mid);
            map.put("deviceid" ,deviceid);
            if (typeid.equals("2")) {
                lay.setVisibility(View.VISIBLE);
                map.put("typeid" ,"2");
            }else if(typeid.equals("4")){
                lay2.setVisibility(View.VISIBLE);
                map.put("typeid" ,"4");
            }else if(typeid.equals("7")){
                lay.setVisibility(View.VISIBLE);
                map.put("typeid" ,"7");
            }
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,map,handler,2,0);
        }catch (Exception e){

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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 2){
                    Gson g=new Gson();
                    json1 = (String)msg.obj;
                    Details details = g.fromJson(json1, Details.class);
                    item = new Posts();
                    if(details != null){
                        if(details.getCode()==1){
                            recode data = details.getData();
                            item.typeid = data.getTypeid();
                            item.aid = aid;
                            item.setId(aid);
                            item.setLitpic(data.getLitpic());
                            item.setDescription(data.description);
                            item.setTitle(data.getTitle());
                            item.setUsername(data.getUsername());
                            if(data.getArea_cate() != null){
                                Area_cate item1 = new Area_cate();
                                item1.setArea_cate1(data.getArea_cate().getArea_cate1());
                                item.setArea_cate(item1);
                            }
                            if(item.typeid.equals("4")){
                                item.setTypename("专家");
                            }else if(item.typeid.equals("2")){
                                item.setTypename("项目");
                            }else if(item.typeid.equals("7")){
                                item.setTypename("设备");
                            }
                            if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI) {
                                        if(typeid.equals("4")){
                                          if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                                img2.setVisibility(View.VISIBLE);
                                                wutu_rencai.setVisibility(View.GONE);
                                                ImageLoader.getInstance().displayImage(data.getLitpic()
                                                        , img2, options);
                                          }else{
                                              img2.setVisibility(View.GONE);
                                              wutu_rencai.setVisibility(View.VISIBLE);
                                              wutu_rencai.setText(data.getTitle().substring(0,1));
                                          }
                                        }else{
                                            img.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(data.getLitpic()
                                                    , img, options);
                                        }

                                    } else {
                                        if(typeid.equals("4")){
                                            img2.setBackgroundResource(R.mipmap.information_placeholder);
                                        }else{
                                            img.setBackgroundResource(R.mipmap.information_placeholder);
                                            img.setVisibility(View.VISIBLE);
                                        }

                                    }
                                } else {
                                    if(typeid.equals("4")){
                                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                                , img2, options);
                                    }else{
                                        img.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                                , img, options);
                                    }
                                }
                            }
                            if(typeid.equals("4")){
                                if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                    img2.setVisibility(View.VISIBLE);
                                    wutu_rencai.setVisibility(View.GONE);
                                    ImageLoader.getInstance().displayImage(data.getLitpic()
                                            , img2, options);
                                }else{
                                    img2.setVisibility(View.GONE);
                                    wutu_rencai.setVisibility(View.VISIBLE);
                                    wutu_rencai.setText(data.getTitle().substring(0,1));
                                }
                            }

                            xm_title.setText(data.getTitle());
                            xm_title2.setText(data.getTitle());
                            if(data.getArea_cate()!= null){
                                linyu.setText("所属领域："+data.getArea_cate().getArea_cate1());
                                linyu2.setText("所属领域："+data.getArea_cate().getArea_cate1());
                            }
                            lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(typeid.equals("2")){
                                        Intent intent=new Intent(WriteXuQiu.this, NewProjectActivity.class);
                                        intent.putExtra("aid",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "项目");
                                        startActivity(intent);
                                    }else if(typeid.equals("4")){
//                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
//                                        intent.putExtra("id",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "专家");
//                                        startActivity(intent);
                                        Intent intent = new Intent(WriteXuQiu.this, NewRenCaiTail.class);
                                        intent.putExtra("aid", aid);
                                        startActivity(intent);

                                    }else if(typeid.equals("7")){
                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
                                        intent.putExtra("id",aid);
                                        intent.putExtra("pic",item.getLitpic());
                                        intent.putExtra("name", "设备");
                                        startActivity(intent);
                                    }
                                }
                            });

                            lay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(typeid.equals("2")){
                                        Intent intent=new Intent(WriteXuQiu.this, NewProjectActivity.class);
                                        intent.putExtra("aid",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "项目");
                                        startActivity(intent);
                                    }else if(typeid.equals("4")){
//                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
//                                        intent.putExtra("id",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "专家");
//                                        startActivity(intent);
                                        Intent intent = new Intent(WriteXuQiu.this, NewRenCaiTail.class);
                                        intent.putExtra("aid", aid);
                                        startActivity(intent);
                                    }else if(typeid.equals("7")){
                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
                                        intent.putExtra("id",aid);
                                        intent.putExtra("pic",item.getLitpic());
                                        intent.putExtra("name", "设备");
                                        startActivity(intent);
                                    }
                                }
                            });


//                            AddXuQiu2.data = item;
//                            AddXuQiu.data = item;
//                            handlerData();

                        }
                    }
                }else if(msg.what == 1){
                        Gson g=new Gson();
                        json = (String)msg.obj;
//                    Log.d("lizisong","xuqiu:"+json);
                        Ret result =g.fromJson(json, Ret.class);
                        if(result.code.equals("1")){
                            if(isshow == false){
                                isshow = true;
//                                Toast.makeText(WriteXuQiu.this, result.message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteXuQiu.this, MyXuqiuActivity.class);
                                if(typeid.equals("0")){
                                    intent.putExtra("type", "-1");
                                }else{
                                    intent.putExtra("type", typeid);
                                }
                                startActivity(intent);
                                finish();
                            }
                        }


                }else if(msg.what == 4){
                    json = (String)msg.obj;
                    Gson g=new Gson();
                    data  = g.fromJson(json, WriteXuQiuDemo.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.data != null){
                                if(data.data.size() == 1){
                                    WriteXuQiuData item1 =data.data.get(0);
                                    shili1.setText(item1.name);
                                    demo1.setText(item1.content);
                                    shili1.setVisibility(View.VISIBLE);
                                    demo1.setVisibility(View.VISIBLE);
                                }else if(data.data.size() == 2){
                                    WriteXuQiuData item1 =data.data.get(0);
                                    shili1.setText(item1.name);
                                    demo1.setText(item1.content);
                                    WriteXuQiuData item2 =data.data.get(1);
                                    shili2.setText(item2.name);
                                    demo2.setText(item2.content);
                                    shili1.setVisibility(View.VISIBLE);
                                    demo1.setVisibility(View.VISIBLE);
                                    shili2.setVisibility(View.VISIBLE);
                                    demo2.setVisibility(View.VISIBLE);
                                }else if(data.data.size() > 2){
                                    WriteXuQiuData item1 =data.data.get(0);
                                    shili1.setText(item1.name);
                                    demo1.setText(item1.content);
                                    WriteXuQiuData item2 =data.data.get(1);
                                    shili2.setText(item2.name);
                                    demo2.setText(item2.content);
                                    WriteXuQiuData item3 =data.data.get(2);
                                    shili3.setText(item3.name);
                                    demo3.setText(item3.content);
                                    shili1.setVisibility(View.VISIBLE);
                                    demo1.setVisibility(View.VISIBLE);
                                    shili2.setVisibility(View.VISIBLE);
                                    demo2.setVisibility(View.VISIBLE);
                                    shili3.setVisibility(View.VISIBLE);
                                    demo3.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }

            }catch (Exception e){

            }

        }
    };

    private void tijiaopost(){
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        String  mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", mid);
        map.put("method","add");
        map.put("entry_cate","2");
        if(aid != null){
            map.put("aid", aid);
            if(typeid.equals("4")) {
                map.put("typeid", "4");
                map.put("entry_address", "7");
            }else if(typeid.equals("2")){
                map.put("typeid", "2");
                map.put("entry_address", "8");
            }else if(typeid.equals("7")){
                map.put("typeid", "7");
                map.put("entry_address", "9");
            }
        }else{
            map.put("aid", "0");
            map.put("typeid", "0");
            map.put("entry_address", "6");
        }

        if(entry_address != 0){
            map.put("entry_address", entry_address+"");
        }

        map.put("content",text.getText().toString());
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,1,0);

    }

    private void getShiLi(){
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", "example");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,4,0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("写需求");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("写需求");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entry_address = 0;
    }
}
