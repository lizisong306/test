package com.maidiantech;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.BitmapUtil;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.MainDianIcon;
import dao.Service.PulseCondtion;
import entity.LangyaSimple;
import entity.Result;
import entity.ResultData;
import entity.RetPulseData;
import entity.data;
import fragment.WelcomePulse;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2016/12/26.
 */

public class ResultActivity extends AutoLayoutActivity {
    RefreshListView listView;
    View heardView;
    ImageView imgHeart;
    GridView heart_grid;
    TextView text_date,title_name, title_lingyu;
    HeardGridApater gridApater;
    List<String> heardGuaid = new ArrayList<String>();
    Map<String, String> postData = new HashMap<String, String>();
    List<data> data;
    Button delete;
    String title;
    String from;
    RelativeLayout bg;
    ListViewAdapter mListViewAdapter;
    RelativeLayout mTitle;
    ImageView bamai_back, back;
    private  String   ips;
    public static int evaluetop = 0;
    public static  String evalue = "";
    public static  String typeid = "";
    public static String category = "";
    public static String province = "";
    public static String evalueTitle = "";
    public static String mid ;
    public static String id = "";

    public Result result = new Result();
    public static List<data> more_xiangmu = new ArrayList<>();
    public static List<data> more_rencai = new ArrayList<>();
    public static List<data> more_shebei = new ArrayList<>();
    public static List<data> more_shiyanshi = new ArrayList<>();
    public static List<data> more_zhenci = new ArrayList<>();
    public static List<data> more_zixun = new ArrayList<>();
    public static List<data> more_zhuanli = new ArrayList<>();

    private OkHttpUtils Okhttp;
    private String jsons;
    PulseCondtion mPulseCondtion;
    private TextView login_title;
    private String loginState;
    private String name;
    private TextView login_titles;
    private String mtype;
    private String company;
    private String nickname;
    ProgressBar progressBar;

    MainDianIcon mainDianIcon;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        //设置状态栏是否沉浸
        StyleUtils1.initSystemBar(this);
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        mainDianIcon = MainDianIcon.getInstance(this);
        mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
        login_titles=(TextView) findViewById(R.id.login_title);
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        name=SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "");
        mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
        company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
        nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0");
        progressBar = (ProgressBar)findViewById(R.id.progress);
//        if(Build.MODEL.equals("SM-G9287")){
//            login_titles.setTextSize(36);
//        }
        if(!loginState.equals("1")){
            login_titles.setText("私人订制");
        }else{
            if(mtype.equals("个人")){
//                login_titles.setText(name+"的私人订制");
                    login_titles.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0")+"的私人订制");
            }else if(mtype.equals("企业")){
                login_titles.setText(company+"的私人订制");
            }

        }
        mPulseCondtion = PulseCondtion.getInstance(this);
        postData.clear();
        more_zhuanli.clear();
        more_xiangmu.clear();
        more_rencai.clear();
        more_shebei.clear();
        more_shiyanshi.clear();
        more_zhenci.clear();
        more_zixun.clear();

        result.showdata = new ArrayList<data>();
        listView = (RefreshListView)findViewById(R.id.listview);
        heardView = View.inflate(this, R.layout.result_heart, null);

        imgHeart = (ImageView) heardView.findViewById(R.id.my_login);

        heart_grid = (GridView)heardView.findViewById(R.id.herat_grid);
        login_title=(TextView) heardView.findViewById(R.id.title_name);
        back = (ImageView)heardView.findViewById(R.id.bamai_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pluse_four.state = true;
                BmpluseThree.close_state=true;
                bmplusetwo.close_state = true;
                PulseActivity.posdata = 1;
                finish();
            }
        });
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        name=SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "");
        mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
        company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
        nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0");

        heart_grid.setVisibility(View.INVISIBLE);
        text_date = (TextView)heardView.findViewById(R.id.title_date);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        text_date.setText("更新日期:"+format.format(new Date()));
        title_name = (TextView)heardView.findViewById(R.id.title_name);
        title_lingyu = (TextView)heardView.findViewById(R.id.title_lingyu);
        bg = (RelativeLayout)heardView.findViewById(R.id.title);
        bamai_back = (ImageView)findViewById(R.id.bamai_back);
        if(Build.MODEL.equals("SM-G9287")){
            login_title.setTextSize(36);
        }
        if(!loginState.equals("1")){
            login_title.setText("私人订制");
        }else{
            if(mtype.equals("个人")){
//                title_name.setText(name+"的私人订制");
                    title_name.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0")+"的私人订制");
            }else if(mtype.equals("企业")){
                title_name.setText(company+"的私人订制");
            }
        }
        bamai_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pluse_four.state = true;
                BmpluseThree.close_state=true;
                bmplusetwo.close_state = true;
                PulseActivity.posdata = 1;
                finish();
            }
        });
        mTitle = (RelativeLayout)findViewById(R.id.title);
        delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =delete.getText().toString();
                if(text.equals("删除该板块")){
                    pluse_four.state = true;
                    BmpluseThree.close_state=true;
                    bmplusetwo.close_state = true;
                    PulseActivity.posdata = 1;
                    finish();
                    Intent intent = new Intent();
                    intent.putExtra("action", "delete");
                    intent.putExtra("evaluetop", evaluetop+"");
                    intent.setAction(WelcomePulse.resultBroaderAction);
                    sendBroadcast(intent);

                }else if(text.equals("修改把脉内容")){
                    pluse_four.state = true;
                    BmpluseThree.close_state=true;
                    finish();
                }
            }
        });
        try {
            evalue = evalue.substring(0,evalue.length()-1);
            if(typeid.equals("")){
                typeid = "2,4,7,8,6,1,5";
            }else{
                typeid = typeid.substring(0, typeid.length()-1);
            }
            Log.d("lizisong", "typeid:"+typeid);

        }catch (Exception e){

        }

//        String timestamp = System.currentTimeMillis()+"";
//        String sign="";
//        ArrayList<String> sort = new ArrayList<String>();

        postData.put("mid",mid);
        postData.put("evaluetop",evaluetop+"");
        postData.put("evalue",evalue);
        postData.put("typeid", typeid);
        postData.put("category", category);
        postData.put("province", province);
//        postData.put("version", MyApplication.version);
//        sort.add("mid"+mid);
//        sort.add("evaluetop"+evaluetop+"");
//        sort.add("evalue"+evalue);
//        sort.add("typeid"+typeid);
//        sort.add("category"+category);
//        sort.add("province"+province);
//        sort.add("timestamp"+timestamp);
//        sort.add("version"+MyApplication.version);
//        String accessid="";
//        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//        if(loginState.equals("1")){
//            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
//
//            accessid = mid;
//        }else{
//            accessid = MyApplication.deviceid;
//        }
//        sort.add("accessid" + accessid);
//        sign = KeySort.keyScort(sort);
//        postData.put("sign", sign);
//        postData.put("timestamp", timestamp);
//        postData.put("accessid", accessid);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        from = intent.getStringExtra("from");
        title_lingyu.setText(title);
        if(title.equals("先进制造")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xianjinzhizao);
            bg.setBackgroundResource(R.mipmap.bg_xianjinzhizao);
        }else if(title.equals("电子信息")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_dianzixinx);
            bg.setBackgroundResource(R.mipmap.bg_dianzixinxi);
        }else if(title.equals("新材料")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xincailiao);
            bg.setBackgroundResource(R.mipmap.bg_xincailiao);
        }else if(title.equals("生物技术")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_shegnwujishu);
            bg.setBackgroundResource(R.mipmap.bg_shengwujishu);
        }else if(title.equals("节能环保")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_jienenghuanbao);
            bg.setBackgroundResource(R.mipmap.bg_jienenghuanbao);
        }else if(title.equals("文化创意")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_wenhuachuangyi);
            bg.setBackgroundResource(R.mipmap.bg_wenhuachuangyi);
        }else if(title.equals("化学化工")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_huaxuehuagong);
            bg.setBackgroundResource(R.mipmap.bg_huaxuehuagong);
        }else if(title.equals("新能源")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xinnengyuan);
            bg.setBackgroundResource(R.mipmap.bg_xinnengyuan);
        }else if(title.equals("其他")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_qita);
            bg.setBackgroundResource(R.mipmap.bg_qita);
        }

        progressBar.setVisibility(View.VISIBLE);

        posData();
//        if(bmplusetwo.dexlist != null){
//            for(int i=0; i<bmplusetwo.dexlist.size(); i++){
//                LangyaSimple simple = bmplusetwo.dexlist.get(i);
//                heardGuaid.add(simple.getTitle());
//            }
//        }
//        gridApater = new HeardGridApater();
//        heart_grid.setAdapter(gridApater);
        listView.addHeaderView(heardView, null, true);

        mListViewAdapter = new ListViewAdapter();
        listView.setAdapter(mListViewAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 2) {
                    if(from.equals("pluse_four")){
                        mTitle.setVisibility(View.VISIBLE);
                        delete.setText("修改把脉内容");
                        delete.setBackgroundResource(R.color.updata);
                    }
                } else {
                    mTitle.setVisibility(View.GONE);
                    delete.setText("删除该板块");
                    delete.setBackgroundResource(R.color.red);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mListViewAdapter != null){
            mListViewAdapter.notifyDataSetChanged();
        }
        MobclickAgent.onPageStart("把脉结果"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
//        heart_grid.setVisibility(View.INVISIBLE);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("把脉结果"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    public void posData(){
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        String url = "http://"+ips+"/api/getArcByAreaCate.php";
        networkCom.postJson(url,(HashMap<String, String>) postData,handler,1,0);

    }
    ResultActivity.MyHandler handler = new ResultActivity.MyHandler(this);
    private class MyHandler extends Handler{
        WeakReference<Activity> mActivityReference;
        MyHandler(Activity activity){
            mActivityReference = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity activity = mActivityReference.get();
            if(activity == null){
                return;
            }
            switch (msg.what){
                case  2:
                    progressBar.setVisibility(View.GONE);
                    break;
                case  1:
                    String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
                    if(url != null && !url.equals("0") && !url.equals("")){
                        ImageLoader.getInstance().displayImage(url, imgHeart);
                    } else{
                        BitmapUtil.upUserImageData(imgHeart, mainDianIcon);
                    }
                    try {
                        Gson gs = new Gson();
                        jsons = (String)msg.obj;
                        ResultData json_data =gs.fromJson(jsons, ResultData.class);
                        if(json_data.code == 1){
                            data = json_data.data;
                            for(int i=0; i<data.size(); i++){
                                data item = data.get(i);
                                try {

                                    if(item.typeid.equals("2") ){
                                        more_xiangmu.add(item);
                                    }else if(item.typeid.equals("4") ){
                                        more_rencai.add(item);
                                    }else if(item.typeid.equals("7")){
                                        more_shebei.add(item);
                                    }else if(item.typeid.equals("8")){
                                        more_shiyanshi.add(item);
                                    }else if(item.typeid.equals("6")){
                                        more_zhenci.add(item);
                                    }else if(item.typeid .equals("1") ){
                                        more_zixun.add(item);
                                    }else if(item.typeid .equals("5")){
                                        more_zhuanli.add(item);
                                    }
                                }catch (Exception e){}
                            }
                            result.rencai = more_rencai.size();
                            result.shebei = more_shebei.size();
                            result.shiyanshi = more_shiyanshi.size();
                            result.zhenci = more_zhenci.size();
                            result.xiangmu = more_xiangmu.size();
                            result.zhuanli = more_zhuanli.size();
                            result.zixun = more_zixun.size();

//                        result.showdata = new ArrayList<data>();

                            if(result.xiangmu > 0 ){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "项目";
                                result.showdata.add(mData);
                            }

                            if(result.xiangmu >= 3 ){
                                result.showdata.add(more_xiangmu.get(0));
                                result.showdata.add(more_xiangmu.get(1));
                                result.showdata.add(more_xiangmu.get(2));
                            }else if(result.xiangmu == 2){
                                result.showdata.add(more_xiangmu.get(0));
                                result.showdata.add(more_xiangmu.get(1));
                            }else if(result.xiangmu == 1){
                                result.showdata.add(more_xiangmu.get(0));
                            }

                            if(result.xiangmu > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "项目";
                                result.showdata.add(mData);
                            }

                            if(result.rencai > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "专家";
                                result.showdata.add(mData);
                            }

                            if(result.rencai >= 3){
                                result.showdata.add(more_rencai.get(0));
                                result.showdata.add(more_rencai.get(1));
                                result.showdata.add(more_rencai.get(2));
                            }else if(result.rencai == 2){
                                result.showdata.add(more_rencai.get(0));
                                result.showdata.add(more_rencai.get(1));
                            }else if(result.rencai == 1){
                                result.showdata.add(more_rencai.get(0));
                            }
                            if(result.rencai > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "专家";
                                result.showdata.add(mData);
                            }

                            if(result.shebei > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "设备";
                                result.showdata.add(mData);
                            }
                            if(result.shebei >= 3){
                                result.showdata.add(more_shebei.get(0));
                                result.showdata.add(more_shebei.get(1));
                                result.showdata.add(more_shebei.get(2));
                            }else if(result.shebei == 2){
                                result.showdata.add(more_shebei.get(0));
                                result.showdata.add(more_shebei.get(1));
                            }else if(result.shebei == 1){
                                result.showdata.add(more_shebei.get(0));
                            }

                            if(result.shebei > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "设备";
                                result.showdata.add(mData);
                            }

                            if(result.shiyanshi > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "科研机构";
                                result.showdata.add(mData);
                            }
                            if(result.shiyanshi >= 3){
                                result.showdata.add(more_shiyanshi.get(0));
                                result.showdata.add(more_shiyanshi.get(1));
                                result.showdata.add(more_shiyanshi.get(2));
                            }else if(result.shiyanshi == 2){
                                result.showdata.add(more_shiyanshi.get(0));
                                result.showdata.add(more_shiyanshi.get(1));
                            }else if(result.shiyanshi == 1){
                                result.showdata.add(more_shiyanshi.get(0));
                            }

                            if(result.shiyanshi > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "科研机构";
                                result.showdata.add(mData);
                            }


                            if(result.zhenci > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "政策";
                                result.showdata.add(mData);
                            }
                            if(result.zhenci >= 3){
                                result.showdata.add(more_zhenci.get(0));
                                result.showdata.add(more_zhenci.get(1));
                                result.showdata.add(more_zhenci.get(2));
                            }else if(result.zhenci == 2){
                                result.showdata.add(more_zhenci.get(0));
                                result.showdata.add(more_zhenci.get(1));
                            }else if(result.zhenci == 1){
                                result.showdata.add(more_zhenci.get(0));
                            }

                            if(result.zhenci > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "政策";
                                result.showdata.add(mData);
                            }

                            if(result.zixun > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "资讯";
                                result.showdata.add(mData);
                            }
                            if(result.zixun >= 3){
                                result.showdata.add(more_zixun.get(0));
                                result.showdata.add(more_zixun.get(1));
                                result.showdata.add(more_zixun.get(2));
                            }else if(result.zixun == 2){
                                result.showdata.add(more_zixun.get(0));
                                result.showdata.add(more_zixun.get(1));
                            }else if(result.zixun == 1){
                                result.showdata.add(more_zixun.get(0));
                            }
                            if(result.zixun > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "资讯";
                                result.showdata.add(mData);
                            }

                            if(result.zhuanli > 0){
                                data mData = new data();
                                mData.typeid = "0";
                                mData.title = "专利";
                                result.showdata.add(mData);
                            }
                            if(result.zhuanli >= 3){
                                result.showdata.add(more_zhuanli.get(0));
                                result.showdata.add(more_zhuanli.get(1));
                                result.showdata.add(more_zhuanli.get(2));
                            }else if(result.zhuanli == 2){
                                result.showdata.add(more_zhuanli.get(0));
                                result.showdata.add(more_zhuanli.get(1));
                            }else if(result.zhuanli == 1){
                                result.showdata.add(more_zhuanli.get(0));
                            }
                            if(result.zhuanli > 3){
                                data mData = new data();
                                mData.typeid = "10";
                                mData.title = "更多";
                                mData.description = "专利";
                                result.showdata.add(mData);
                            }
                            mPulseCondtion.updataPid(evaluetop+"", json_data.message);
                            mListViewAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                            id = json_data.message;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新数据
                                    try {
                                        String timestamp = System.currentTimeMillis()+"";
                                        String sign="";
                                        ArrayList<String> sort = new ArrayList<String>();
                                        ips = MyApplication.ip;
                                        postData.remove("mid");
                                        postData.put("id", id);
                                        postData.put("mid", mid);
                                        postData.put("version", MyApplication.version);
                                        sort.add("mid"+mid);
                                        sort.add("id"+id);
                                        sort.add("timestamp"+timestamp);
                                        sort.add("version"+MyApplication.version);
                                        sign = KeySort.keyScort(sort);
                                        postData.put("sign", sign);
                                        postData.put("timestamp", timestamp);
                                        String data = Okhttp.post("http://"+ips+"/api/getAreaCateInfo.php?action=edit&mid="+mid,postData);
                                        Gson gs = new Gson();
                                        RetPulseData item =gs.fromJson(data, RetPulseData.class);
                                        if(item.code.equals("1")){
                                            mPulseCondtion.updataUpFlag(evaluetop+"", 1);
                                        }

                                    }catch (Exception e){

                                    }


                                }
                            }).start();

                        }else{
                            progressBar.setVisibility(View.GONE);
                            if(json_data.code == 0){
                                Toast.makeText(ResultActivity.this, "没有找到您想要的数据！",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ResultActivity.this, json_data.message,Toast.LENGTH_SHORT).show();
                            }

                        }
                    }catch (Exception e){

                    }
                    break;
            }
        }
    }

//    Handler handler = new Handler(){
//
//    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            pluse_four.state = true;
            BmpluseThree.close_state=true;
            bmplusetwo.close_state = true;
            PulseActivity.posdata = 1;
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

     class HeardGridApater extends BaseAdapter{

         @Override
         public int getCount() {
             return heardGuaid.size();
         }

         @Override
         public Object getItem(int position) {
             return heardGuaid.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             ViewHolder holder = null;
             String item = heardGuaid.get(position);
             if(convertView == null){
                 holder = new ViewHolder();
                 convertView = View.inflate(ResultActivity.this, R.layout.guid_adapter,null);
                 holder.textView = (TextView) convertView.findViewById(R.id.gudi_text);
                 convertView.setTag(holder);
             }else{
                 holder = (ViewHolder)convertView.getTag();
             }
             holder.textView.setText(item);
             return convertView;
         }
         class ViewHolder{
             TextView textView;
         }
     }


    class ListViewAdapter extends BaseAdapter{

        private ImageLoader imageLoader;
        private DisplayImageOptions options;
        public  ListViewAdapter(){
            options = ImageLoaderUtils.initOptions();
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return  result.showdata.size();
        }

        @Override
        public Object getItem(int position) {
            return result.showdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHold holder = null;
            final entity.data item = result.showdata.get(position);
            if(convertView == null){
                holder = new ViewHold();
                convertView = View.inflate(ResultActivity.this, R.layout.result_adapter, null);
                holder.title_txt = (TextView) convertView.findViewById(R.id.title_txt);
                holder.content = (LinearLayout) convertView.findViewById(R.id.rc);
                holder.cont_imgs = (ImageView)convertView.findViewById(R.id.cont_imgs);
                holder.my_rencainame = (TextView)convertView.findViewById(R.id.my_rencainame);
                holder.rencai_imgs = (RoundImageView)convertView.findViewById(R.id.rencai_imgs);
                holder.zhicheng = (TextView)convertView.findViewById(R.id.zhicheng);
                holder.lingyu =(TextView)convertView.findViewById(R.id.lingyu);
                holder.my_linyu = (TextView)convertView.findViewById(R.id.my_linyu);
                holder.my_xianqin = (TextView)convertView.findViewById(R.id.my_xianqin);
                holder.shiyanse_img=(ImageView) convertView.findViewById(R.id.shiyanse_img);
                holder.time = (TextView)convertView.findViewById(R.id.time);
                holder.line = (TextView)convertView.findViewById(R.id.line);

                holder.rc_look =(TextView)convertView.findViewById(R.id.rc_look);
                holder.more =(RelativeLayout)convertView.findViewById(R.id.more);
                holder.lingyu_info = (LinearLayout)convertView.findViewById(R.id.lingyu_info);
                convertView.setTag(holder);
            }else {
                holder = (ViewHold)convertView.getTag();
            }
            try {

                holder.rencai_imgs.setVisibility(View.GONE);
            if(item.typeid.equals("0")){ //title
                holder.content.setVisibility(View.GONE);
                holder.more.setVisibility(View.GONE);
                holder.title_txt.setVisibility(View.VISIBLE);
                holder.title_txt.setText(" "+item.title);
                holder.line.setVisibility(View.GONE);

            }else if(item.typeid.equals("2") ){ //项目
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.VISIBLE);
                holder.shiyanse_img.setVisibility(View.GONE);

                if(item.litpic == null || item.litpic.equals("")){
                    holder.cont_imgs.setVisibility(View.GONE);
                }else{
                    holder.cont_imgs.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(item.litpic
                                , holder.cont_imgs, options);
                }

                holder.lingyu_info.setVisibility(View.VISIBLE);

                holder.my_linyu.setText(item.area_cate.getArea_cate1());
                holder.my_rencainame.setText(item.title);
                holder.my_xianqin.setVisibility(View.VISIBLE);
                holder.my_xianqin.setText(item.description);
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }

                holder.time.setText("项目");
                holder.rc_look.setText(item.click);
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);

            }else if(item.typeid.equals("4") ){    //人才
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);
                holder.rencai_imgs.setVisibility(View.VISIBLE);
//                ViewGroup.LayoutParams layoutParams = holder.cont_imgs.getLayoutParams();
//                if(Build.MODEL.equals("SM-G9287")){
//                    layoutParams.width=200;
//                    layoutParams.height=240;
//                }else{
//                    layoutParams.width=190;
//                    layoutParams.height=220;
//                }
//
//
//                holder.cont_imgs.setLayoutParams(layoutParams);
                if(item.litpic == null || item.litpic.equals("")){
                    holder.rencai_imgs.setVisibility(View.GONE);
                }else{
                    holder.rencai_imgs.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(item.litpic
                                , holder.rencai_imgs, options);
                }
                holder.lingyu_info.setVisibility(View.VISIBLE);
                holder.my_linyu.setText(item.area_cate.getArea_cate1());
                holder.my_rencainame.setText(item.username );
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }

                holder.my_xianqin.setText(item.description);
                holder.time.setText("专家");
                holder.rc_look.setText(item.click);
                holder.zhicheng.setVisibility(View.VISIBLE);
                holder.zhicheng.setText(item.rank);
                holder.line.setVisibility(View.VISIBLE);
            }else if(item.typeid.equals("7")){ //设备
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.lingyu_info.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.VISIBLE);


                if(item.litpic == null || item.litpic.equals("")){
                    holder.cont_imgs.setVisibility(View.GONE);
                }else{
                    holder.cont_imgs.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(item.litpic
                            , holder.cont_imgs, options);
                }

                holder.my_rencainame.setText(item.title);
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }
//                holder.my_xianqin.setText(item.description);
                holder.my_xianqin.setVisibility(View.VISIBLE);
                holder.my_xianqin.setText(item.description);
                holder.time.setText("设备");
                holder.rc_look.setText(item.click);
                holder.lingyu_info.setVisibility(View.VISIBLE);
                holder.my_linyu.setText(item.area_cate.getArea_cate1());
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);

            }else if(item.typeid.equals("8")){ //实验室
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.VISIBLE);
                holder.shiyanse_img.setVisibility(View.GONE);

                if(item.litpic == null || item.litpic.equals("")){
                    holder.cont_imgs.setVisibility(View.GONE);
                }else{
                    holder.cont_imgs.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(item.litpic
                            , holder.cont_imgs, options);
                }

                holder.lingyu_info.setVisibility(View.VISIBLE);

                holder.my_linyu.setText(item.area_cate.getArea_cate1());
                holder.my_rencainame.setText(item.title);
                holder.my_xianqin.setVisibility(View.VISIBLE);
                holder.my_xianqin.setText(item.description);
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }

                holder.time.setText("科研机构");
                holder.rc_look.setText(item.click);
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
//                holder.more.setVisibility(View.GONE);
//                holder.content.setVisibility(View.VISIBLE);
//                holder.title_txt.setVisibility(View.GONE);
//                holder.lingyu_info.setVisibility(View.GONE);
//                holder.cont_imgs.setVisibility(View.GONE);
//                holder.shiyanse_img.setVisibility(View.VISIBLE);
//
//                holder.my_rencainame.setText(item.title);
//                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
//                if(pid.equals("")){
//                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
//                }else{
//                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
//                }
//                if(item.litpic == null || item.litpic.equals("")){
//                    holder.shiyanse_img.setVisibility(View.GONE);
//                }else{
//                    holder.shiyanse_img.setVisibility(View.VISIBLE);
//                    imageLoader.displayImage(item.litpic
//                            , holder.shiyanse_img, options);
//                }
////                holder.my_xianqin.setText(item.description);
//                holder.time.setText("实验室");
//                holder.my_xianqin.setVisibility(View.GONE);
//
//                holder.rc_look.setText(item.click);
//                holder.zhicheng.setVisibility(View.GONE);
            }else if(item.typeid.equals("6")){ //政策
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.lingyu_info.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);
                holder.my_rencainame.setText(item.title);

                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }
                holder.my_xianqin.setVisibility(View.VISIBLE);
                holder.my_xianqin.setText(item.description);
                holder.lingyu_info.setVisibility(View.GONE);

//                holder.my_linyu.setText(item.area_cate.getArea_cate1());

                holder.rc_look.setText(item.click);
                holder.time.setText("政策");
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
            }else if(item.typeid .equals("1") ){ // 资讯
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.VISIBLE);
                holder.lingyu_info.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);


                if(item.litpic == null || item.litpic.equals("")){
                    holder.cont_imgs.setVisibility(View.GONE);
                }else{
                    holder.cont_imgs.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(item.litpic
                            , holder.cont_imgs, options);
                }

                holder.my_rencainame.setText(item.title);
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }
//                holder.my_xianqin.setText(item.description);
                holder.time.setText("资讯");
                holder.my_xianqin.setVisibility(View.GONE);
                holder.lingyu_info.setVisibility(View.GONE);
                holder.my_xianqin.setText(item.description);
                holder.my_linyu.setText(item.area_cate.getArea_cate1());

                holder.rc_look.setText(item.click);
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
            }else if(item.typeid .equals("5")){ //专利
                holder.more.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
                holder.title_txt.setVisibility(View.GONE);
                holder.lingyu_info.setVisibility(View.GONE);
                holder.cont_imgs.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);
                holder.my_rencainame.setText(item.title);
                String pid=PrefUtils.getString(ResultActivity.this,item.id,"");
                if(pid.equals("")){
                    holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                }
//                holder.my_xianqin.setText(item.description);
                holder.my_xianqin.setVisibility(View.VISIBLE);
                holder.my_xianqin.setText(item.description);
                holder.lingyu_info.setVisibility(View.VISIBLE);

                holder.my_linyu.setText(item.area_cate.getArea_cate1());

                holder.rc_look.setText(item.click);
                holder.time.setText("专利");
                holder.zhicheng.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
            }else if(item.typeid .equals("10")){ //更多
                holder.more.setVisibility(View.VISIBLE);
                holder.content.setVisibility(View.GONE);
                holder.title_txt.setVisibility(View.GONE);
                holder.shiyanse_img.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                holder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ResultActivity.this, MoreInfoActivity.class);
                        intent.putExtra("title", item.description);
                        Bundle bundle=new Bundle();
                        if(item.typeid.equals("2")){
                            bundle.putSerializable("list", (Serializable) more_xiangmu);
                        }else if(item.typeid.equals("4")){
                            bundle.putSerializable("list", (Serializable) more_rencai);
                        }else if(item.typeid.equals("7")){
                            bundle.putSerializable("list", (Serializable) more_shebei);
                        }else if(item.typeid.equals("8")){
                            bundle.putSerializable("list", (Serializable) more_shiyanshi);
                        }else if(item.typeid.equals("6")){
                            bundle.putSerializable("list", (Serializable) more_zhenci);
                        }else if(item.typeid.equals("1")){
                            bundle.putSerializable("list", (Serializable) more_zixun);
                        }else if(item.typeid .equals("5")){
                            bundle.putSerializable("list", (Serializable) more_zhuanli);
                        }
                        intent.putExtras(bundle);//发送数据
                        startActivity(intent);
                    }
                });


            }
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.typeid.equals("2")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
//                        Intent intent=new Intent(ResultActivity.this, DetailsActivity.class);
//                        intent.putExtra("id",item.id);
//                        intent.putExtra("pic",item.litpic);
//                        intent.putExtra("name", "项目");
//                        startActivity(intent);
                        Intent intent = new Intent(ResultActivity.this, NewProjectActivity.class);
                        intent.putExtra("aid", item.id);
                        startActivity(intent);
                    }else if(item.typeid.equals("4")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
//                        Intent intent=new Intent(ResultActivity.this, DetailsActivity.class);
//                        intent.putExtra("id",item.id);
//                        intent.putExtra("pic",item.litpic);
//                        intent.putExtra("name", "专家");
//                        startActivity(intent);
                        Intent intent = new Intent(ResultActivity.this, XinFanAnCeShi.class);
                        intent.putExtra("aid", item.id);
                        startActivity(intent);
                    }else if(item.typeid.equals("6")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
                        Intent intent=new Intent(ResultActivity.this, NewZhengCeActivity.class);
                        intent.putExtra("aid",item.id);
//                        intent.putExtra("pic",item.litpic);
//                        intent.putExtra("name", "政策");
                        startActivity(intent);
                    }else if(item.typeid.equals("7")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
                        Intent intent=new Intent(ResultActivity.this, DetailsActivity.class);
                        intent.putExtra("id",item.id);
                        intent.putExtra("pic",item.litpic);
                        intent.putExtra("name", "设备");
                        startActivity(intent);
                    }else if(item.typeid.equals("8")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
                        Intent intent=new Intent(ResultActivity.this, UnitedStatesDeilActivity.class);
                        intent.putExtra("aid",item.id);
                        startActivity(intent);
                    }else if(item.typeid.equals("1")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
                        Intent intent=new Intent(ResultActivity.this, ZixunDetailsActivity.class);
                        intent.putExtra("id",item.id);
                        intent.putExtra("pic",item.litpic);
                        intent.putExtra("name", "资讯");
                        startActivity(intent);
                    }else if(item.typeid.equals("5")){
                        PrefUtils.setString(ResultActivity.this,item.id,item.id);
                        Intent intent=new Intent(ResultActivity.this, NewZhuanliActivity.class);
                        intent.putExtra("aid",item.id);
//                        intent.putExtra("pic",item.litpic);
//                        intent.putExtra("name", "专利");
                        startActivity(intent);
                    }
                }
            });

            }catch (Exception e){}
            return convertView;
        }

        class ViewHold{
            TextView title_txt,line;

            LinearLayout content;
            RoundImageView rencai_imgs;
            ImageView cont_imgs;
            TextView my_rencainame;
            TextView zhicheng;
            TextView lingyu;
            TextView my_linyu;
            LinearLayout lingyu_info;
            TextView my_xianqin;

            TextView time;

            TextView rc_look;

            RelativeLayout more;
            ImageView shiyanse_img;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
