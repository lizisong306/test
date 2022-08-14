package com.maidiantech;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.Counts;
import entity.HistoyEntry;
import entity.Renlist;
import entity.Sblist;
import entity.Searchcode;
import entity.Shiyanshi;
import entity.Tuijian;
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
 * Created by lizisong on 2017/7/7.
 */

public class XuQiuSearch extends AutoLayoutActivity {
    AutoCompleteTextView textView;
    TextView sousuo;
    ListView search_listview_info;
    TextView delete;
    String type,redianStr="";
    List<HistoyEntry> arrayList = new ArrayList<>();
    private List<String> redianList = new ArrayList<String>();
    TypeAdatper adatper;
    String trim;
    ProgressBar progress;
    String ips,searchjsons;
    private List<Object> dataList;
    private List<String> typeList;
    List<Zxlist> zixun ;
    public static List<Renlist> rencai ;
    public static List<Sblist> shebei ;
    public static List<Xmlist> xiangmu;
    List<Zclist> zhengce ;
    List<Zllist> zhuanli ;
    List<Ztlist> zhuanti ;
    List<Shiyanshi> shiyanshi ;
    List<Tuijian> tuijian ;
    private String place="";
    private String der="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuqiusearch);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        textView = (AutoCompleteTextView)findViewById(R.id.search);
        sousuo = (TextView)findViewById(R.id.sousuo);
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_listview_info = (ListView)findViewById(R.id.search_listview_info);
        delete = (TextView)findViewById(R.id.delete);
        progress = (ProgressBar)findViewById(R.id.progress);
        type = getIntent().getStringExtra("type");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try {
                   if(type.equals("xiangmu")){
                       SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_XIANGMU,"");
                   }else if(type.equals("rencai")){
                       SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_RENCAI,"");
                   }else if(type.equals("shebei")){
                       SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_SHEBEI,"");
                   }
                   redianList.clear();
                   arrayList.clear();
                   adatper.notifyDataSetChanged();
            }catch (Exception e){

            }
            }
        });
        if(type.equals("xiangmu")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_XIANGMU,"");
        }else if(type.equals("rencai")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_RENCAI,"");
        }else if(type.equals("shebei")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_SHEBEI,"");
        }
        if(redianStr != null && !redianStr.equals("")){
            String[] temp = redianStr.split(";");
            if(temp != null){
                HistoyEntry item =null;
                for (int i=0; i<temp.length;i++){
                    String pos=temp[i];
                    redianList.add(pos);
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        if(i%2 == 0){
                            item = new HistoyEntry();
                            item.left = pos;
                            if(i == temp.length-1){
                                arrayList.add(item);
                            }
                        }else{
                            item.right = pos;
                            arrayList.add(item);
                        }
                    }
                }
            }
        }
        adatper = new TypeAdatper();
        search_listview_info.setAdapter(adatper);
        set_eSearch_TextChanged();
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(XuQiuSearch.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else{
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(textView, 0);
                               }

                           },
                    500);
        }
        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = textView.getText().toString().trim();
                  int  netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(XuQiuSearch.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(XuQiuSearch.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(trim)==false){
                            Toast.makeText(XuQiuSearch.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                hintKbTwo();
                                progress.setVisibility(View.VISIBLE);
                                gethistory(place,der);

                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });

    }
    class TypeAdatper extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(XuQiuSearch.this, R.layout.xuqiuhistory, null);
                holder.left = (TextView) convertView.findViewById(R.id.left);
                holder.right = (TextView)convertView.findViewById(R.id.right);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
           final HistoyEntry item  =arrayList.get(position);
            holder.left.setText(item.left);

            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trim = item.left;
                    progress.setVisibility(View.VISIBLE);
                    gethistory(place,der);
                }
            });
            if(item.right == null){
                holder.right.setVisibility(View.GONE);
            }else {
                holder.right.setText(item.right);
                holder.right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trim = item.right;
                        progress.setVisibility(View.VISIBLE);
                        gethistory(place,der);
                    }
                });
            }


            return convertView;
        }

        class ViewHolder{
            TextView left;
            TextView right;
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

    private void set_eSearch_TextChanged() {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){

                }else {

                }
            }
        });
    }

    public void gethistory(final String place,final String der) {
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String typeid ="";
                        if(type.equals("xiangmu")){
                            typeid ="2";
                        }else if(type.equals("rencai")){
                            typeid ="4";
                        }else if(type.equals("shebei")){
                            typeid ="7";
                        }
                        ips = MyApplication.ip;
                        String timestamp = System.currentTimeMillis()+"";
                        String sign="";
                        ArrayList<String> sort = new ArrayList<String>();
                        sort.add("keyword"+trim);
                        sort.add("typeid"+typeid);
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
                        String searchjson="http://"+ips+"/api/search.php?keyword="+trim+"&typeid="+typeid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+"&nativeplace="+place+"&order="+der+MyApplication.accessid;
                        searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);
                        Message msg=new Message();
                        msg.what=0;
                        handler.sendMessage(msg);

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
            Gson g =new Gson();
            if(msg.what==0){
                try {
                    progress.setVisibility(View.GONE);
                    Searchcode searchcode = g.fromJson(searchjsons, Searchcode.class);
                    Counts data = searchcode.getData();
                    searchcount count = data.getCount();
                    searchresult result = data.getResult();
                    dataList =new ArrayList<>();
                    typeList = new ArrayList<>();
                    zixun = result.getZixun();
                    rencai = result.getRencai();
                    shebei = result.getShebei();
                    xiangmu = result.getXiangmu();
                    zhengce = result.getZhengce();
                    zhuanli = result.getZhuanli();
                    zhuanti = result.getZhuanti();
                    shiyanshi = result.getShiyanshi();
                    tuijian = result.getTuijian();


                    if(searchcode.getMessage().equals("获取信息成功！")) {

                        if(!redianStr.contains(trim)){
                            redianList.add(0,trim);
                            int len = redianList.size();
                            if(len >6){
                                len = 6;
                            }
                            redianStr="";
                            for (int i=0;i<len;i++){
                                String temp=redianList.get(i);
                                if(i==len-1){
                                    redianStr=redianStr+temp;
                                }else{
                                    redianStr=redianStr+temp+";";
                                }
                            }
                            if(type.equals("xiangmu")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_XIANGMU,redianStr);
                            }else if(type.equals("zhengce")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_ZHENGCE,redianStr);
                            }else if(type.equals("rencai")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_RENCAI,redianStr);
                            }else if(type.equals("shebei")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_SHEBEI,redianStr);
                            }else if(type.equals("shiyanshi")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_SHIYANSHI,redianStr);
                            }else if(type.equals("zhuangli")){
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.SOUSUO_ZHUANLI,redianStr);
                            }
                        }
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(XuQiuSearch.this, "没有相关数据", Toast.LENGTH_SHORT).show();

                }
            }

        }
    };
}
