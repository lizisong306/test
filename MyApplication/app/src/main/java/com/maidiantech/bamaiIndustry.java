package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import adapter.SearchAdapter;
import application.MyApplication;
import entity.Counts;
import entity.Hotcode;
import entity.Renlist;
import entity.Sblist;
import entity.Searchcode;
import entity.Shiyanshi;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Ztlist;
import entity.Zxlist;
import entity.hotwords;
import entity.searchcount;
import entity.searchdata;
import entity.searchresult;
import view.StyleUtils;
import view.StyleUtils1;

import static dao.Sqlitetions.getInstance;

/**
 * Created by 13520 on 2016/11/15.
 */

public class bamaiIndustry extends AutoLayoutActivity {
    private View view1;
    private ImageView search_back;
    private AutoCompleteTextView editext_search;
    private ImageButton my_img_search;
    private MainActivity main;
    private ListView hot_listview;
    private ListView history_gridview;
    private ImageView ivDeleteText;
    private List<String> findall;
    private ArrayAdapter<String> arrayAdapter;
    private SearchAdapter adapter;
    private String searchjsons;
    private List<searchdata> searchlist;
    private ListView search_listview;
    private LinearLayout search_gone;
    private String hotjsons;
    private  List<hotwords> hotWordslist;
    private   List<String> typeList;
    private  List<Object> dataList;
    List<String> findalls;
    String trim;
    int netWorkType;
    private  String   ips;
    Gson g;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            g =new Gson();
            if(msg.what==0){
                try {
                    searchjsons = (String)msg.obj;
                    Searchcode searchcode = g.fromJson(searchjsons, Searchcode.class);
                    Counts data = searchcode.getData();
                    searchcount count = data.getCount();

                    searchresult result = data.getResult();
                    if(result==null){
                        Toast.makeText(main, "没有相关数据", Toast.LENGTH_SHORT).show();
                    }
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
                    if(xiangmu!=null) {
                        dataList.add(xiangmu);
                        typeList.add("xiangmu");
                    }
                    if(rencai!=null) {
                        dataList.add(rencai);
                        typeList.add("rencai");
                    }
                    if(shiyanshi!=null) {
                        dataList.add(shiyanshi);
                        typeList.add("shiyanshi");
                    }
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
                        Intent intent = new Intent(bamaiIndustry.this, SearchInfo.class);
                        Bundle b = new Bundle();
                        b.putSerializable("datalist", (Serializable) dataList);
                        b.putSerializable("typelist", (Serializable) typeList);
                        intent.putExtras(b);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }catch (Exception e){}
            }
            if(msg.what==1){
                hotjsons = (String)msg.obj;
                Hotcode hotcode = g.fromJson(hotjsons, Hotcode.class);
//                hotData data = hotcode.getData();
//                hotWordslist = data.getHotWords();
//                HotwordsAdapter hotadapter= new HotwordsAdapter(bamaiIndustry.this,hotWordslist);
//                hot_listview.setAdapter(hotadapter);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_search);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);


        search_back=(ImageView) findViewById(R.id.search_back);
        editext_search=(AutoCompleteTextView) findViewById(R.id.editext_search);
        my_img_search=(ImageButton) findViewById(R.id.my_img_search);
        hot_listview=(ListView) findViewById(R.id.hot_listview);
        history_gridview=(ListView) findViewById(R.id.history_gridview);
        ivDeleteText=(ImageView) findViewById(R.id.ivDeleteText);
        search_listview=(ListView) findViewById(R.id.search_listview);
        search_gone=(LinearLayout) findViewById(R.id.search_gone);
        ips = MyApplication.ip;
         netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

        }else{
            gethot();
        }
        set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器
        set_ivDeleteText_OnClick();//设置叉叉的监听器

        try {
            findalls = getInstance(this).findall();
            if(adapter==null){
                adapter=new SearchAdapter(bamaiIndustry.this,findalls);
                history_gridview.setAdapter(adapter);
                history_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        trim  =findalls.get(position);
                        gethistory();
                    }
                });
            }else{
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){}
        my_img_search.setOnClickListener(new NoDoubleClickListener() {

            @Override

            public void onNoDoubleClick(View v) {
                trim = editext_search.getText().toString().trim();
                 netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(bamaiIndustry.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }else{
                    try {

                        if(trim.equals("")){
                            Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            getInstance(bamaiIndustry.this).search_add(trim);
                            gethistory();
                        }
                    }catch (Exception e){}

                }

            }
        });
     /*   editext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = editext_search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(bamaiIndustry.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        try {

                            if(trim.equals("")){
                                Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
                            }else{
                                getInstance(bamaiIndustry.this).search_add(trim);
                                gethistory();
                            }
                        }catch (Exception e){}

                    }
                    return true;
                }
                return false;
            }
        });*/

        //  gethistory();

      /*  search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.hide();
            }
        });*/
    }
    private void set_ivDeleteText_OnClick() {
        editext_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editext_search.setText("");
            }
        });

    }
    private void set_eSearch_TextChanged() {
        editext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                    adapter.notifyDataSetChanged();
                   /* search_listview.setVisibility(View.GONE);
                    search_gone.setVisibility(View.VISIBLE);*/
                }
                else {
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }
            }
        });
    }
    public abstract class NoDoubleClickListener implements View.OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
        private long lastClickTime = 0;
        protected abstract void onNoDoubleClick(View v);
        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
    public void gethistory() {
        try {
            String url="http://"+ips+"/api/search.php";
            HashMap<String,String> map = new HashMap<>();
            map.put("keyword",trim);
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,map,handler,0,0);
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("keyword"+trim);
//                    sort.add("timestamp"+timestamp);
//                    sort.add("version"+MyApplication.version);
//                    sign= KeySort.keyScort(sort);
//                    String searchjson="http://"+ips+"/api/search.php?keyword="+trim+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version;
//                    searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);
//                    Log.i("searchjson",searchjsons);
//                    Message msg=new Message();
//                    msg.what=0;
//                    handler.sendMessage(msg);
//                }
//            }.start();



        }catch (Exception e){
        }
    }
    public void gethot() {
        try {
            String hotjson="http://"+ips+"/api/hotWords.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(hotjson,null,handler,1,0);
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    MyApplication.setAccessid();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("timestamp"+timestamp);
//                    sort.add("version"+MyApplication.version);
//                    String accessid="";
//                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                    if(loginState.equals("1")){
//                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
//                        accessid = mid;
//                    }else{
//                        accessid = MyApplication.deviceid;
//                    }
//                    sort.add("accessid"+accessid);
//                    sign=KeySort.keyScort(sort);
//                    String hotjson="http://"+ips+"/api/hotWords.php"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
//                    hotjsons=OkHttpUtils.loaudstringfromurl(hotjson);
//                    Log.i("hotjsons",hotjsons);
//                    Message msg=new Message();
//                    msg.what=1;
//                    handler.sendMessage(msg);
//                }
//            }.start();
        }catch (Exception e){}

    }

}
