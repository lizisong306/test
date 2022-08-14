package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import application.MyApplication;
import entity.Infor;
import entity.TopicInforEntiry;
import entity.TopicInforSubmit;
import view.StyleUtils;
import view.SystemBarTintManager;
import  Util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2018/6/29.
 */

public class TopicInformation extends AutoLayoutActivity {

    ImageView back;
    LinearLayout left_click,right_click,danweiname,jiajilay,yuan_lay;
    ImageView left_img,right_img;
    TextView left_subject,right_subject,danweinameline,yuan;
    TextView zhuanlishengqingjiage,wukeyong,jiage,pay_money,youhui,pay_confirm,bottmon_title,youhuijuan_count;
    AutoCompleteTextView danweimingcheng,lianxiren,lianxifangshi;
    EditText zhuanlimingcheng;

    boolean leftstate = true, rightstate= false;
    boolean isjiaji =false;
    float price=0.0f;
    public static boolean isFinish = false;
    TopicInforEntiry data;
    String typeid,coupon_id="";
    TopicInforSubmit topicInforSubmit;
    ImageView xuanzhongstate1, xuanzhongstate2,xuanzhongstate3,xuanzhongstate4,xuanzhongstate5,xuanzhongstate6,xuanzhongstate7,xuanzhongstate8,xuanzhongstate9,xuanzhongstate10;
    TextView isjiaji1,isjiaji2,isjiaji3,isjiaji4,isjiaji5,isjiaji6,isjiaji7,isjiaji8,isjiaji9,isjiaji10;
    RelativeLayout jiaji1,jiaji2,jiaji3,jiaji4,jiaji5,jiaji6,jiaji7,jiaji8,jiaji9,jiaji10;
    boolean isIsjiajiState1=false,isIsjiajiState2=false,isIsjiajiState3=false,isIsjiajiState4=false,isIsjiajiState5=false,isIsjiajiState6=false,isIsjiajiState7=false,isIsjiajiState8=false,isIsjiajiState9=false,isIsjiajiState10=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.topicinformation);
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
//        setNavigationBarStatusBarTranslucent(this);
        PayConfirmActivity.youhuijuanprice = "";
        PayConfirmActivity.couponid = "";
        isFinish = false;

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
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
            }
        });
        left_click = (LinearLayout)findViewById(R.id.left_click);
        yuan_lay = (LinearLayout)findViewById(R.id.yuan_lay);
        right_click = (LinearLayout)findViewById(R.id.right_click);
        yuan = (TextView)findViewById(R.id.yuan);
        danweiname = (LinearLayout)findViewById(R.id.danweiname);
        danweinameline = (TextView)findViewById(R.id.danweinameline);
        left_img = (ImageView)findViewById(R.id.left_img);
        right_img = (ImageView)findViewById(R.id.right_img);
        left_subject = (TextView)findViewById(R.id.left_subject);
        right_subject = (TextView)findViewById(R.id.right_subject);
        zhuanlishengqingjiage = (TextView)findViewById(R.id.zhuanlishengqingjiage);
        wukeyong = (TextView)findViewById(R.id.wukeyong);
        jiage = (TextView)findViewById(R.id.jiage);
        pay_money = (TextView)findViewById(R.id.pay_money);
        youhui = (TextView)findViewById(R.id.youhui);
        pay_confirm = (TextView)findViewById(R.id.pay_confirm);
        zhuanlimingcheng = (EditText)findViewById(R.id.zhuanlimingcheng);
        danweimingcheng =(AutoCompleteTextView)findViewById(R.id.danweimingcheng);
        lianxiren = (AutoCompleteTextView)findViewById(R.id.lianxiren);
        lianxifangshi = (AutoCompleteTextView)findViewById(R.id.lianxifangshi);
        bottmon_title = (TextView)findViewById(R.id.bottmon_title);
        jiajilay = (LinearLayout)findViewById(R.id.jiajilay);
        youhuijuan_count = (TextView)findViewById(R.id.youhuijuan_count);
        xuanzhongstate1=(ImageView)findViewById(R.id.xuanzhongstate1);
        xuanzhongstate2=(ImageView)findViewById(R.id.xuanzhongstate2);
        xuanzhongstate3=(ImageView)findViewById(R.id.xuanzhongstate3);
        xuanzhongstate4=(ImageView)findViewById(R.id.xuanzhongstate4);
        xuanzhongstate5=(ImageView)findViewById(R.id.xuanzhongstate5);
        xuanzhongstate6=(ImageView)findViewById(R.id.xuanzhongstate6);
        xuanzhongstate7=(ImageView)findViewById(R.id.xuanzhongstate7);
        xuanzhongstate8=(ImageView)findViewById(R.id.xuanzhongstate8);
        xuanzhongstate9=(ImageView)findViewById(R.id.xuanzhongstate9);
        xuanzhongstate10=(ImageView)findViewById(R.id.xuanzhongstate10);
        isjiaji1=(TextView)findViewById(R.id.isjiaji1);
        isjiaji2=(TextView)findViewById(R.id.isjiaji2);
        isjiaji3=(TextView)findViewById(R.id.isjiaji3);
        isjiaji4=(TextView)findViewById(R.id.isjiaji4);
        isjiaji5=(TextView)findViewById(R.id.isjiaji5);
        isjiaji6=(TextView)findViewById(R.id.isjiaji6);
        isjiaji7=(TextView)findViewById(R.id.isjiaji7);
        isjiaji8=(TextView)findViewById(R.id.isjiaji8);
        isjiaji9=(TextView)findViewById(R.id.isjiaji9);
        isjiaji10=(TextView)findViewById(R.id.isjiaji10);
        jiaji1 = (RelativeLayout)findViewById(R.id.jiaji1);
        jiaji2 = (RelativeLayout)findViewById(R.id.jiaji2);
        jiaji3 = (RelativeLayout)findViewById(R.id.jiaji3);
        jiaji4 = (RelativeLayout)findViewById(R.id.jiaji4);
        jiaji5 = (RelativeLayout)findViewById(R.id.jiaji5);
        jiaji6 = (RelativeLayout)findViewById(R.id.jiaji6);
        jiaji7 = (RelativeLayout)findViewById(R.id.jiaji7);
        jiaji8 = (RelativeLayout)findViewById(R.id.jiaji8);
        jiaji9 = (RelativeLayout)findViewById(R.id.jiaji9);
        jiaji10 = (RelativeLayout)findViewById(R.id.jiaji10);
        hideJiaJi();

        typeid = getIntent().getStringExtra("typeid");
        wukeyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicInformation.this, XuanZeYouHuiJuanActivity.class);
                intent.putExtra("cate_id_1","2");
                intent.putExtra("typeid" ,"5");
                if(danweiname.getVisibility() == View.VISIBLE){
                    intent.putExtra("cate_id_2","1");
                }else{
                    intent.putExtra("cate_id_2","2");
                }
                startActivity(intent);
            }
        });

        left_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftstate){
                    return;
                }
                rightstate = false;
                leftstate = true;
                price=0f;
//                zhuanlimingcheng.setText("");
//                danweimingcheng.setText("");
//                lianxiren.setText("");
                PayConfirmActivity.youhuijuanprice="";
                PayConfirmActivity.couponid="";
                XuanZeYouHuiJuanActivity.current=-1;
//                lianxifangshi.setText("");
                youhui.setText("优惠￥"+"0");
                youhui.setTextColor(0xFF9F9E9E);
                clickleft();
                clickright();

            }
        });
        yuan_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicInformation.this, XuanZeYouHuiJuanActivity.class);
                intent.putExtra("cate_id_1","2");
                intent.putExtra("typeid" ,"5");
                if(danweiname.getVisibility() == View.VISIBLE){
                    intent.putExtra("cate_id_2","1");
                }else{
                    intent.putExtra("cate_id_2","2");
                }
                startActivity(intent);
            }
        });

        right_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightstate){
                    return;
                }
//                zhuanlimingcheng.setText("");
//                danweimingcheng.setText("");
//                lianxiren.setText("");
//                lianxifangshi.setText("");
                PayConfirmActivity.youhuijuanprice="";
                PayConfirmActivity.couponid="";
                XuanZeYouHuiJuanActivity.current=-1;
                youhui.setText("优惠￥"+"0");
                youhui.setTextColor(0xFF9F9E9E);
                rightstate = true;
                leftstate= false;
                price=0f;
                clickleft();
                clickright();

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
        lianxifangshi.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, ""));

        pay_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCommit();
            }
        });

//        xuanzhongstate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               isjiaji = !isjiaji;
//                if(isjiaji){
//                    xuanzhongstate.setImageResource(R.mipmap.xuanzhong_h);
//                }else{
//                    xuanzhongstate.setImageResource(R.mipmap.xuanzhong);
//                }
//            }
//        });

        getJson(typeid);

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

    private void getJson(String subject){
        String url ="http://www.zhongkechuangxiang.com/webapp/api.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("c", "patent");
        map.put("a", "urgent");
        map.put("subject",subject);
        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
        networkCom.getJson(url,map,handler,1,0);
    }

    private void getCommit(){
        String name = zhuanlimingcheng.getText().toString();
        String danwei= danweimingcheng.getText().toString();
        String connect = lianxiren.getText().toString();
        String phone = lianxifangshi.getText().toString();
        String ugid="";

        if(name == null || name.equals("")){
            Toast.makeText(this, "专利名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(danweiname.getVisibility() == View.VISIBLE){
            if(danwei == null || danwei.equals("")){
                Toast.makeText(this, "单位名称不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(connect == null || connect.equals("")){
            Toast.makeText(this, "联系人不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone == null || phone.equals("")){
            Toast.makeText(this, "联系方式不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(danweiname.getVisibility() == View.VISIBLE){
            if(isIsjiajiState1){
                ugid=data.result.subject_one.urgent_list.get(0).id;
            }
            if(isIsjiajiState2){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(1).id;
            }
            if(isIsjiajiState3){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(2).id;
            }
            if(isIsjiajiState4){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(3).id;
            }
            if(isIsjiajiState5){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(4).id;
            }
            if(isIsjiajiState6){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(5).id;
            }
            if(isIsjiajiState7){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(6).id;
            }
            if(isIsjiajiState8){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(7).id;
            }
            if(isIsjiajiState9){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(8).id;
            }
            if(isIsjiajiState10){
                ugid=ugid+","+data.result.subject_one.urgent_list.get(9).id;
            }
        }else{
            if(isIsjiajiState1){
                ugid=data.result.subject_two.urgent_list.get(0).id;
            }
            if(isIsjiajiState2){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(1).id;
            }
            if(isIsjiajiState3){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(2).id;
            }
            if(isIsjiajiState4){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(3).id;
            }
            if(isIsjiajiState5){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(4).id;
            }
            if(isIsjiajiState6){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(5).id;
            }
            if(isIsjiajiState7){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(6).id;
            }
            if(isIsjiajiState8){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(7).id;
            }
            if(isIsjiajiState9){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(8).id;
            }
            if(isIsjiajiState10){
                ugid=ugid+","+data.result.subject_two.urgent_list.get(9).id;
            }
        }

        Intent intent = new Intent(TopicInformation.this, PaySDkCallActivity.class);
        intent.putExtra("type",2);
        intent.putExtra("ugid", ugid);
        intent.putExtra("couponid",PayConfirmActivity.couponid );
        intent.putExtra("patent_type", "2");
        if(danweiname.getVisibility() == View.VISIBLE){
            intent.putExtra("subject","1");
        }else{
            intent.putExtra("subject","2");
        }
        intent.putExtra("title", name);
        intent.putExtra("company", danwei);
        intent.putExtra("linkman", connect);
        intent.putExtra("phone", phone);
        startActivity(intent);

//        String url = "http://www.zhongkechuangxiang.com/webapp/api.php";
//        HashMap<String, String> map = new HashMap<>();
//        map.put("c","patent");
//        map.put("a","submit");
//        map.put("mid", SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, ""));
//        String key="";
//        for (Map.Entry<String,String> entry:xuanzezhuangtai.entrySet()
//                ) {
//            String item,value;
//            item=entry.getKey();
//            value=entry.getValue();
//            if(value.equals("true")){
//                key=item+",";
//            }
//
//        }
//        map.put("ugid", key);
//        map.put("title", name);
//        if(danweiname.getVisibility() == View.VISIBLE){
//            map.put("subject","1");
//            map.put("company", danwei);
//        }else{
//            map.put("subject","2");
//        }
//        map.put("patent_type",typeid);
//        map.put("linkman", connect);
//        map.put("linkman", phone);
//        map.put("coupon_id", coupon_id);
//        NetworkCom networkCom = NetworkCom.getNetworkCom();
//        networkCom.postJson(url,map,handler,2,0);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, TopicInforEntiry.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.result != null){
                                if(data.result.subject_one != null){
                                    hideJiaJi();
                                    if(data.result.subject_one.urgent_list != null){
                                        for(int i=0; i<data.result.subject_one.urgent_list.size();i++){
                                            Infor item = data.result.subject_one.urgent_list.get(i);
                                            showJiaJi(i,item);

                                        }
                                    }
                                    zhuanlishengqingjiage.setText("￥"+data.result.subject_one.patent_price);
                                    pay_money.setText("待支付￥"+data.result.subject_one.patent_price);
                                    jiage.setText("￥"+data.result.subject_one.patent_price);
                                   if(data.result.subject_one.coupon_list.num != null && !data.result.subject_one.coupon_list.num.equals("")) {
                                        if(Integer.parseInt(data.result.subject_one.coupon_list.num) > 0){
                                            yuan.setVisibility(View.VISIBLE);
                                            yuan_lay.setVisibility(View.VISIBLE);
                                            yuan_lay.setVisibility(View.VISIBLE);
                                            yuan.setText(data.result.subject_one.coupon_list.num);
                                            wukeyong.setVisibility(View.GONE);
                                        }else{
//                                            jiage.setVisibility(View.GONE);
                                        }
                                }
                                }
                            }
                        }
                    }

                }
                if(msg.what == 2){
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    topicInforSubmit = gson.fromJson(ret, TopicInforSubmit.class);
                    if(topicInforSubmit != null){
                        if(topicInforSubmit.code.equals("1")){

                        }
                    }
                }
            }catch (Exception e){

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isFinish){
            finish();
            return;
        }

        if(PayConfirmActivity.youhuijuanprice != null && !PayConfirmActivity.youhuijuanprice.equals("")){
            youhuijuan_count.setVisibility(View.GONE);
            youhuijuan_count.setTextColor(0xffff6363);
            youhuijuan_count.setText("-￥"+PayConfirmActivity.youhuijuanprice);
            youhui.setTextColor(0xffff6363);
            yuan.setVisibility(View.GONE);
            yuan_lay.setVisibility(View.GONE);
            wukeyong.setVisibility(View.VISIBLE);
            wukeyong.setText("-￥"+PayConfirmActivity.youhuijuanprice);
            wukeyong.setTextColor(0xffff5a4d);
            youhui.setText("优惠￥"+PayConfirmActivity.youhuijuanprice);
            Count();
//            float p1 = Float.parseFloat(PayConfirmActivity.youhuijuanprice);
//            float p2=0f;
//            if(danweiname.getVisibility() == View.VISIBLE){
//                p2 = Float.parseFloat(data.result.subject_one.patent_price);
//            }else{
//                p2 = Float.parseFloat(data.result.subject_two.patent_price);
//            }
//
//            if(p2-p1 >1){
//                price = p2-p1;
//                pay_money.setText("待支付￥"+price);
//                jiage.setText("￥"+price);
//            }else{
////                if(p2>p1){
//                price = 1f;
//                pay_money.setText("待支付￥"+price);
//                jiage.setText("￥"+1);
//                youhui.setText("已优惠￥"+(p2-1));
////                }else{
////                    pay_money.setText("已￥"+1);
////                }
//            }

        }else {
            try {
                youhuijuan_count.setVisibility(View.GONE);
                youhui.setText("优惠￥"+"0");
                youhui.setTextColor(0xFF9F9E9E);
                Count();
                if(danweiname.getVisibility() == View.VISIBLE){
                    if(data.result.subject_one.coupon_list.num != null && !data.result.subject_one.coupon_list.num.equals("")){
                        if(Integer.parseInt(data.result.subject_one.coupon_list.num) <=0){
                            wukeyong.setText("无可用");
                            wukeyong.setTextColor(0xffa8a8a8);
                            wukeyong.setVisibility(View.VISIBLE);
                            yuan.setVisibility(View.GONE);
                            yuan_lay.setVisibility(View.GONE);
                        }else{
                            yuan.setText(data.result.subject_one.coupon_list.num);
                            wukeyong.setVisibility(View.GONE);
                            yuan.setVisibility(View.VISIBLE);
                            yuan_lay.setVisibility(View.VISIBLE);

                        }
                    }
                }else{
                    if(Integer.parseInt(data.result.subject_two.coupon_list.num) <=0){
                        wukeyong.setText("无可用");
                        wukeyong.setTextColor(0xffa8a8a8);
                        wukeyong.setVisibility(View.VISIBLE);
                        yuan.setVisibility(View.GONE);
                        yuan_lay.setVisibility(View.GONE);
                    }else{
                        yuan.setText(data.result.subject_two.coupon_list.num);
                        yuan_lay.setVisibility(View.VISIBLE);
                        wukeyong.setVisibility(View.GONE);
                        yuan.setVisibility(View.VISIBLE);

                    }
                }
            }catch (Exception e){

            }


        }

    }
    public void clickleft(){
        if(leftstate){
            left_click.setBackgroundResource(R.drawable.left_draw_down);
            left_img.setImageResource(R.mipmap.qiye_h);
            left_subject.setTextColor(Color.parseColor("#ffffff"));
            danweiname.setVisibility(View.VISIBLE);
            danweinameline.setVisibility(View.VISIBLE);
            try {
                if(data.result != null){
                    if(data.result.subject_one != null){
                        hideJiaJi();
                        try {
                            for(int i=0; i<data.result.subject_one.urgent_list.size();i++){
                                Infor item = data.result.subject_one.urgent_list.get(i);
                                showJiaJi(i,item);
                            }
                        }catch (Exception e){

                        }

                        zhuanlishengqingjiage.setText("￥"+data.result.subject_one.patent_price);
                        pay_money.setText("待支付￥"+data.result.subject_one.patent_price);
                        jiage.setText("￥"+data.result.subject_one.patent_price);
                        if(data.result.subject_one.coupon_list.num != null && !data.result.subject_one.coupon_list.num.equals("")) {
                            if(Integer.parseInt(data.result.subject_one.coupon_list.num) > 0){
                                yuan.setVisibility(View.VISIBLE);
                                yuan_lay.setVisibility(View.VISIBLE);
                                yuan.setText(data.result.subject_one.coupon_list.num);
                                wukeyong.setVisibility(View.GONE);
                            }else{
                                wukeyong.setText("无可用");
                                wukeyong.setTextColor(Color.parseColor("#a8a8a8"));
                                wukeyong.setVisibility(View.VISIBLE);
                                yuan.setVisibility(View.GONE);
                                yuan_lay.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }catch (Exception e){

            }
        }else {
            left_click.setBackgroundResource(R.drawable.left_draw_up);
            left_img.setImageResource(R.mipmap.qiye);
            left_subject.setTextColor(Color.parseColor("#5b9dff"));

        }

    }
    public void clickright(){
        if(rightstate){
            right_click.setBackgroundResource(R.drawable.right_draw_down);
            right_img.setImageResource(R.mipmap.geren_h);
            right_subject.setTextColor(Color.parseColor("#ffffff"));
            danweiname.setVisibility(View.GONE);
            danweinameline.setVisibility(View.GONE);

            try {
                if(data.result != null){
                    if(data.result.subject_one != null){
                        hideJiaJi();
                        try {
                            for(int i=0; i<data.result.subject_two.urgent_list.size();i++){
                                Infor item = data.result.subject_two.urgent_list.get(i);
                                showJiaJi(i,item);
                            }
                        }catch (Exception e){

                        }

                        zhuanlishengqingjiage.setText("￥"+data.result.subject_two.patent_price);
                        pay_money.setText("待支付￥"+data.result.subject_two.patent_price);
                        jiage.setText("￥"+data.result.subject_two.patent_price);
                        if(data.result.subject_two.coupon_list.num != null && !data.result.subject_two.coupon_list.num.equals("")) {
                            if(Integer.parseInt(data.result.subject_two.coupon_list.num) > 0){
                                yuan.setVisibility(View.VISIBLE);
                                yuan_lay.setVisibility(View.VISIBLE);
                                yuan.setText(data.result.subject_two.coupon_list.num);
                                wukeyong.setVisibility(View.GONE);
                            }else{
                                wukeyong.setText("无可用");
                                wukeyong.setTextColor(Color.parseColor("#a8a8a8"));
                                wukeyong.setVisibility(View.VISIBLE);
                                yuan.setVisibility(View.GONE);
                                yuan_lay.setVisibility(View.GONE);
                            }
                        }

                    }
                }
            }catch (Exception e){

            }

        }else{
            right_click.setBackgroundResource(R.drawable.right_draw_up);
            right_img.setImageResource(R.mipmap.geren);
            right_subject.setTextColor(Color.parseColor("#5b9dff"));
        }
    }
    private void hideJiaJi(){
        jiaji1.setVisibility(View.GONE);
        jiaji2.setVisibility(View.GONE);
        jiaji3.setVisibility(View.GONE);
        jiaji4.setVisibility(View.GONE);
        jiaji5.setVisibility(View.GONE);
        jiaji6.setVisibility(View.GONE);
        jiaji7.setVisibility(View.GONE);
        jiaji8.setVisibility(View.GONE);
        jiaji9.setVisibility(View.GONE);
        jiaji10.setVisibility(View.GONE);
        isIsjiajiState1= false;
        isIsjiajiState2= false;
        isIsjiajiState3= false;
        isIsjiajiState4= false;
        isIsjiajiState5= false;
        isIsjiajiState6= false;
        isIsjiajiState7= false;
        isIsjiajiState8= false;
        isIsjiajiState9= false;
        isIsjiajiState10= false;
        xuanzhongstate1.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate2.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate3.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate4.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate5.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate6.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate7.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate8.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate9.setBackgroundResource(R.mipmap.xuanzhong);
        xuanzhongstate10.setBackgroundResource(R.mipmap.xuanzhong);
    }
    private void showJiaJi(int state, final Infor item){
        switch (state){
            case 0:
                jiaji1.setVisibility(View.VISIBLE);
                isjiaji1.setText(item.name+"（￥"+item.price+"）");
                jiaji1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState1=!isIsjiajiState1;
                         if(isIsjiajiState1){
                             xuanzhongstate1.setBackgroundResource(R.mipmap.xuanzhong_h);

                         }else {
                             xuanzhongstate1.setBackgroundResource(R.mipmap.xuanzhong);

                         }
                        Count();

                    }
                });
                break;
            case 1:
                jiaji2.setVisibility(View.VISIBLE);
                isjiaji2.setText(item.name+"（￥"+item.price+"）");
                jiaji2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState2=!isIsjiajiState2;
                        if(isIsjiajiState2){
                            xuanzhongstate2.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {
                            xuanzhongstate2.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }

                });
                break;
            case 2:
                jiaji3.setVisibility(View.VISIBLE);
                isjiaji3.setText(item.name+"（￥"+item.price+"）");
                jiaji3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState3=!isIsjiajiState3;
                        if(isIsjiajiState3){
                            xuanzhongstate3.setBackgroundResource(R.mipmap.xuanzhong_h);

                        }else {
                            xuanzhongstate3.setBackgroundResource(R.mipmap.xuanzhong);

                        }
                        Count();

                    }
                });
                break;
            case 3:
                jiaji4.setVisibility(View.VISIBLE);
                isjiaji4.setText(item.name+"（￥"+item.price+"）");
                jiaji4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState4=!isIsjiajiState4;
                        if(isIsjiajiState4){
                            xuanzhongstate4.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {
                            xuanzhongstate4.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 4:
                jiaji5.setVisibility(View.VISIBLE);
                isjiaji5.setText(item.name+"（￥"+item.price+"）");
                jiaji5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState5=!isIsjiajiState5;
                        if(isIsjiajiState5){

                            xuanzhongstate5.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {

                            xuanzhongstate5.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 5:
                jiaji6.setVisibility(View.VISIBLE);
                isjiaji6.setText(item.name+"（￥"+item.price+"）");
                jiaji6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState6=!isIsjiajiState6;
                        if(isIsjiajiState6){

                            xuanzhongstate6.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {

                            xuanzhongstate6.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 6:
                jiaji7.setVisibility(View.VISIBLE);
                isjiaji7.setText(item.name+"（￥"+item.price+"）");
                jiaji7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState7=!isIsjiajiState7;
                        if(isIsjiajiState7){

                            xuanzhongstate7.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {

                            xuanzhongstate7.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 7:
                jiaji8.setVisibility(View.VISIBLE);
                isjiaji8.setText(item.name+"（￥"+item.price+"）");
                jiaji8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState8=!isIsjiajiState8;
                        if(isIsjiajiState8){

                            xuanzhongstate8.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {

                            xuanzhongstate8.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 8:
                jiaji9.setVisibility(View.VISIBLE);
                isjiaji9.setText(item.name+"（￥"+item.price+"）");
                jiaji9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState9=!isIsjiajiState9;
                        if(isIsjiajiState9){

                            xuanzhongstate9.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {

                            xuanzhongstate9.setBackgroundResource(R.mipmap.xuanzhong);
                        }
                        Count();

                    }
                });
                break;
            case 9:
                jiaji10.setVisibility(View.VISIBLE);
                isjiaji10.setText(item.name+"（￥"+item.price+"）");
                jiaji10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIsjiajiState10=!isIsjiajiState10;
                        if(isIsjiajiState10){

                            xuanzhongstate10.setBackgroundResource(R.mipmap.xuanzhong_h);
                        }else {
                            xuanzhongstate10.setBackgroundResource(R.mipmap.xuanzhong);

                        }
                        Count();

                    }
                });
                break;

        }
    }
    private void Count(){
           float price1=0f,price2=0f,price3=0f;
        try {
            if(danweiname.getVisibility() == View.VISIBLE){
                if(isIsjiajiState1){
                    price3=Float.parseFloat(data.result.subject_one.urgent_list.get(0).price);
                }
                if(isIsjiajiState2){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(1).price);
                }
                if(isIsjiajiState3){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(2).price);
                }
                if(isIsjiajiState4){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(3).price);
                }
                if(isIsjiajiState5){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(4).price);
                }
                if(isIsjiajiState6){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(5).price);
                }
                if(isIsjiajiState7){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(6).price);
                }
                if(isIsjiajiState8){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(7).price);
                }
                if(isIsjiajiState9){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(8).price);
                }
                if(isIsjiajiState10){
                    price3=price3+Float.parseFloat(data.result.subject_one.urgent_list.get(9).price);
                }
            }else{
                if(isIsjiajiState1){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(0).price);
                }
                if(isIsjiajiState2){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(1).price);
                }
                if(isIsjiajiState3){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(2).price);
                }
                if(isIsjiajiState4){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(3).price);
                }
                if(isIsjiajiState5){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(4).price);
                }
                if(isIsjiajiState6){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(5).price);
                }
                if(isIsjiajiState7){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(6).price);
                }
                if(isIsjiajiState8){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(7).price);
                }
                if(isIsjiajiState9){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(8).price);
                }
                if(isIsjiajiState10){
                    price3=price3+Float.parseFloat(data.result.subject_two.urgent_list.get(9).price);
                }
            }
            if(danweiname.getVisibility() == View.VISIBLE){
                price1 = Float.parseFloat(data.result.subject_one.patent_price);
            }else{
                price1 = Float.parseFloat(data.result.subject_two.patent_price);
            }
            if(PayConfirmActivity.youhuijuanprice != null && !PayConfirmActivity.youhuijuanprice.equals("")){
                price2 = Float.parseFloat(PayConfirmActivity.youhuijuanprice);
            }


            price = price1- price2 + price3;

            youhui.setText("已优惠￥"+(int)price2);
            if(price >0){
                pay_money.setText("待支付￥"+(int)price);
                jiage.setText("￥"+(int)price);
            }else{
                pay_money.setText("待支付￥"+1);
                jiage.setText("￥"+1);
            }

//            if((price1- price2 )>1){
//                price =price1- price2+price3;
//                if(price >0){
//                    pay_money.setText("待支付￥"+price);
//                    jiage.setText("￥"+price);
//                }else{
//                    pay_money.setText("待支付￥"+1);
//                    jiage.setText("￥"+1);
//                }
//            }else{
//    //                if(p2>p1){
//                price = 1f+price3;
//                pay_money.setText("待支付￥"+price);
//                if(price>0){
//                    pay_money.setText("待支付￥"+price);
//                    jiage.setText("￥"+price);
//                }else{
//                    pay_money.setText("待支付￥"+1);
//                    jiage.setText("￥"+1);
//                }
//
//
//                youhui.setText("已优惠￥"+(price1-1));
    //                }else{
    //                    pay_money.setText("已￥"+1);
    //                }
//            }
        }catch (Exception e){

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PayConfirmActivity.youhuijuanprice="";
        PayConfirmActivity.couponid="";
        XuanZeYouHuiJuanActivity.current=-1;
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
}
