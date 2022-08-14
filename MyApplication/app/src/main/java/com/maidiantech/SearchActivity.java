package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import Util.UIHelper;
import application.MyApplication;
import entity.Counts;
import entity.Renlist;
import entity.Sblist;
import entity.Searchcode;
import entity.Shiyanshi;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Ztlist;
import entity.Zxlist;
import entity.searchcount;
import entity.searchresult;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/8/11.
 */
public class SearchActivity extends AutoLayoutActivity {
    ImageView searchback;
    ImageButton my_img_search;
    AutoCompleteTextView autoCompleteTextView;
    private  int netWorkType;
    String trim;
    Gson g;
    private List<Object> dataList;
    private List<String> typeList;
    private String searchjsons;
    private  String   ips;
    private TextView redian1, redian2,redian3,redian4,redian5,redian6,redian7,redian8;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_search);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
         ips = MyApplication.ip;
        initView();
         netWorkType = NetUtils.getNetWorkType(MyApplication
                 .getContext());
        searchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         my_img_search = (ImageButton)findViewById(R.id.my_img_search);
         autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.editext_search);
         redian1 = (TextView)findViewById(R.id.redian1);
         redian2 = (TextView)findViewById(R.id.redian2);
         redian3 = (TextView)findViewById(R.id.redian3);
         redian4 = (TextView)findViewById(R.id.redian4);
         redian5 = (TextView)findViewById(R.id.redian5);
         redian6 = (TextView)findViewById(R.id.redian6);

         redian1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian1.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{

                         gethistory();
                     }

                 }
             }
         });

         redian2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian2.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });

         redian3.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian3.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });

         redian4.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian4.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });

         redian5.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian5.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });

         redian6.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = redian6.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });

         my_img_search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 trim = autoCompleteTextView.getText().toString();
                 if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                     Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                 }else{
                     if(trim == null || trim.equals("")){
                         Toast.makeText(SearchActivity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(TelNumMatch.issearch(trim)==false){
                         Toast.makeText(SearchActivity.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                         return;
                     }else{
                         gethistory();
                     }

                 }
             }
         });
    }
    private void initView() {
        searchback = (ImageView) findViewById(R.id.search_back);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }


    public void gethistory() {
        try {
            UIHelper.showDialogForLoading(SearchActivity.this, "",true);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String timestamp = System.currentTimeMillis()+"";
                        String sign="";
                        ArrayList<String> sort = new ArrayList<String>();
                        sort.add("keyword"+trim);
                        sort.add("typeid"+"7");
                        sort.add("timestamp"+timestamp);
                        sort.add("version"+MyApplication.version);
                        String accessid="";
                        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                            accessid = mid;
                        }else{
                            accessid = MyApplication.deviceid;
                        }
                        sort.add("accessid" + accessid);
                        sign = KeySort.keyScort(sort);
                        MyApplication.setAccessid();
                        String searchjson="http://"+ips+"/api/search.php?keyword="+trim+"&typeid=7"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                        searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);
                        Message msg=new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                        Message message = Message.obtain();
                        message.what = 1;

                    }catch (Exception e){

                    }

                }
            }.start();
        }catch (Exception e){
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            g =new Gson();
            if(msg.what==0){
                try {
                    Searchcode searchcode = g.fromJson(searchjsons, Searchcode.class);
                    Counts data = searchcode.getData();
                    searchcount count = data.getCount();

                    searchresult result = data.getResult();


                    dataList =new ArrayList<>();
                    typeList = new ArrayList<>();

                    List<Zxlist> zixun = result.getZixun();
                    List<Renlist> rencai = result.getRencai();
                    List<Sblist> shebei = result.getShebei();
                    List<Xmlist> xiangmu = result.getXiangmu();
                    List<Zclist> zhengce = result.getZhengce();
                    List<Zllist> zhuanli = result.getZhuanli();
                    List<Ztlist> zhuanti = result.getZhuanti();
                    List<Shiyanshi> shiyanshi = result.getShiyanshi();


                    if(shebei!=null) {
                        dataList.add(shebei);
                        typeList.add("shebei");
                    }
                    if(zhuanli!=null) {
                        dataList.add(zhuanli);
                        typeList.add("zhuanli");
                    }
                    if(zixun!=null) {
                        dataList.add(zixun);
                        typeList.add("zixun");
                   }
                    if(zhengce!=null) {
                        dataList.add(zhengce);
                        typeList.add("zhengce");
                    }
                    if(zhuanti!=null) {
                        dataList.add(zhuanti);
                        typeList.add("zhuanti");
                    }

                    if(searchcode.getMessage().equals("获取信息成功！")) {
                        UIHelper.hideDialogForLoading();
                        Intent intent=new Intent(SearchActivity.this,ManymoreActivity.class);
                        Bundle b=new Bundle();
                        b.putSerializable("dataList", (Serializable) dataList);
                        b.putSerializable("typeList", (Serializable) typeList);

                        intent.putExtras(b);
                        intent.putExtra("possition",0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        SearchActivity.this.startActivity(intent);
                    }


                }catch (Exception e){
                    UIHelper.hideDialogForLoading();
                    Toast.makeText(SearchActivity.this, "没有相关数据",Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
