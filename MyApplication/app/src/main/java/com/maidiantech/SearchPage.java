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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import adapter.PageFragmentAdapter;
import adapter.SearchbiaoAdapter;
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
 * Created by 13520 on 2017/3/21.
 */

public class SearchPage extends AutoLayoutActivity {
    private AutoLinearLayout detailRa;
    private RelativeLayout rlSearchFrameDelete;
    private AutoCompleteTextView search;
    private ImageView ivDeleteText;
    private ImageView shQuxiao;
    private TextView sousuo;
    private String trim;
    private  int netWorkType;
    private String searchjsons;
    private List<Object> dataList;
    private List<String> typeList;
    private PageFragmentAdapter adapter = null;
    private LinearLayout shuju_line;
    private ImageView shuju_img;
    private ListView search_listview_info;
    private  RelativeLayout redianLine;
    private ListView grid;
    redianAdapter redianAdapter;
    TypeAdatper adatper;
    String redianStr;
    TextView delete;
    private List<String> redianList = new ArrayList<String>();
    private  SearchbiaoAdapter searchadapter;
    private  String   ips;
    Gson g;
    List<HistoyEntry> arrayList = new ArrayList<>();
    private ProgressBar progress;
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
            hintKbTwo();
            progress.setVisibility(View.GONE);

        }
    };
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
                    if((result==null || result.equals("")) || (result.getZixun()==null &&  result.getRencai()==null && result.getShebei()==null && result.getXiangmu()==null &&  result.getZhengce()==null && result.getZhuanli()==null && result.getZhuanti()==null)){
                        Toast.makeText(SearchPage.this, "没有相关数据", Toast.LENGTH_SHORT).show();
                        shuju_line.setVisibility(View.VISIBLE);
                        shuju_img.setVisibility(View.VISIBLE);
                        return;
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
                    List<Tuijian> tuijian = result.getTuijian();
                    if(tuijian!=null && tuijian.size()>0) {
                        dataList.add(tuijian);
                        typeList.add("tuijian");
                    }
                    if(xiangmu!=null && xiangmu.size()>0) {
                        dataList.add(xiangmu);
                        typeList.add("xiangmu");
                    }
                    if(rencai!=null && rencai.size()>0) {
                        dataList.add(rencai);
                        typeList.add("rencai");
                    }
                    if(shiyanshi!=null && shiyanshi.size()>0) {
                        dataList.add(shiyanshi);
                        typeList.add("shiyanshi");
                    }
                    if(shebei!=null && shebei.size()>0) {
                        dataList.add(shebei);
                        typeList.add("shebei");
                    }
                    if(zhuanli!=null && zhuanli.size()>0) {
                        dataList.add(zhuanli);
                        typeList.add("zhuanli");
                    }
                    if(zixun!=null && zixun.size()>0) {
                        dataList.add(zixun);
                        typeList.add("zixun");
                    }
                    if(zhengce!=null && zhengce.size()>0) {
                        dataList.add(zhengce);
                        typeList.add("zhengce");
                    }
//                    if(zhuanti!=null && zhuanti.size()>0) {
//                        dataList.add(zhuanti);
//                        typeList.add("zhuanti");
//                    }
                    // Log.i("dataList",dataList.size()+"......");
                    if(searchcode.getMessage().equals("获取信息成功！")) {
                        shuju_line.setVisibility(View.GONE);
                        shuju_img.setVisibility(View.GONE);
                         searchadapter=new SearchbiaoAdapter(SearchPage.this,dataList,typeList);
                        search_listview_info.setAdapter(searchadapter);
                        if(!redianList.contains(trim)){
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
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO, redianStr);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(SearchPage.this, "没有相关数据", Toast.LENGTH_SHORT).show();
                    shuju_line.setVisibility(View.VISIBLE);
                    shuju_img.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        initView();
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(SearchPage.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(search, 0);

                               }

                           },
                    500);
        }
        search_listview_info=(ListView) findViewById(R.id.search_listview_info);
        shuju_line=(LinearLayout) findViewById(R.id.shuju_line) ;
        shuju_img=(ImageView) findViewById(R.id.shuju_img);
        progress=(ProgressBar) findViewById(R.id.progress);
        redianLine = (RelativeLayout)findViewById(R.id.redian);
        redianAdapter = new redianAdapter();
        adatper = new TypeAdatper();
        redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
//        if(redianStr != null && !redianStr.equals("")){
//            String[] temp = redianStr.split(";");
//            if(temp != null){
//                for (int i=0; i<temp.length;i++){
//                    String pos=temp[i];
//                    if(pos != null && !pos.equals("") && !pos.equals(";")){
//                        redianList.add(pos);
//                    }
//                }
//            }
//        }

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

        if(redianList.size() >0){
            redianLine.setVisibility(View.VISIBLE);
        }else {
            redianLine.setVisibility(View.GONE);
        }
        grid = (ListView)findViewById(R.id.redian_grid);
        grid.setAdapter(/*redianAdapter*/adatper);


        progress.setVisibility(View.GONE);
        set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器
//		set_ivDeleteText_OnClick();//设置叉叉的监听器
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(SearchPage.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(SearchPage.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(trim)==false){
                            Toast.makeText(SearchPage.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            if(event.getAction()==KeyEvent.ACTION_UP){
                                redianLine.setVisibility(View.GONE);
                                hintKbTwo();
                                shuju_line.setVisibility(View.GONE);
                                shuju_img.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                gethistory();


//                                Intent intent = new Intent(SearchPage.this, SearchInfo.class);
//                                intent.putExtra("trim",trim);
//                                startActivity(intent);
                            }


                        }

//                        try {
//                            if(trim.equals("")){
//                                Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
//                            }else{
//                                getInstance(getActivity()).search_add(trim);
//
//                                gethistory();
//                            }
//                        }catch (Exception e){}

                    }
                    return true;
                }
                return false;
            }
        });
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
//                trim = search.getText().toString().trim();
//                netWorkType = NetUtils.getNetWorkType(MyApplication
//                        .getContext());
//                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//                    Toast.makeText(SearchPage.this, "网络不给力", Toast.LENGTH_SHORT).show();
//                }else{
//                    if(trim.equals("")){
//                        Toast.makeText(SearchPage.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
//                    }
//                    else if(TelNumMatch.issearch(trim)==false){
//                        Toast.makeText(SearchPage.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//
//                        redianLine.setVisibility(View.GONE);
//                        hintKbTwo();
//                        shuju_line.setVisibility(View.GONE);
//                        shuju_img.setVisibility(View.GONE);
//                        progress.setVisibility(View.VISIBLE);
//                        gethistory();
//
//
////                                Intent intent = new Intent(SearchPage.this, SearchInfo.class);
////                                intent.putExtra("trim",trim);
////                                startActivity(intent);
//
//
//                    }

//                        try {
//                            if(trim.equals("")){
//                                Toast.makeText(main, "关键词不能为空", Toast.LENGTH_SHORT).show();
//                            }else{
//                                getInstance(getActivity()).search_add(trim);
//
//                                gethistory();
//                            }
//                        }catch (Exception e){}

//                }
            }
        });

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
    public void gethistory() {
        try {

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String timestamp = System.currentTimeMillis()+"";
                        String sign="";
                        ArrayList<String> sort = new ArrayList<String>();
                        sort.add("keyword"+trim);
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
                        String searchjson="http://"+ips+"/api/search.php?keyword="+trim+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid+"&version=2.3.0";
                        searchjsons= OkHttpUtils.loaudstringfromurl(searchjson);

                        Message msg=new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                        Message message = Message.obtain();
                        message.what = 1;
                        dismissDialog.sendMessageDelayed(message, 500);
                    }catch (Exception e){

                    }


                }
            }.start();
        }catch (Exception e){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
      if(searchadapter!=null){
          searchadapter.notifyDataSetChanged();
      }
        MobclickAgent.onPageStart("搜索");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索");
        MobclickAgent.onPause(this);
    }

    private void set_eSearch_TextChanged() {
        search.addTextChangedListener(new TextWatcher() {
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
//                    adapter.notifyDataSetChanged();
                   /* search_listview.setVisibility(View.GONE);
                    search_gone.setVisibility(View.VISIBLE);*/
                }
                else {
                    ivDeleteText.setVisibility(View.GONE);//当文本框不为空时，出现叉叉
                    ivDeleteText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            search.setText("");
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        detailRa = (AutoLinearLayout) findViewById(R.id.detail_ra);
        rlSearchFrameDelete = (RelativeLayout) findViewById(R.id.rlSearchFrameDelete);
        search = (AutoCompleteTextView) findViewById(R.id.search);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        shQuxiao = (ImageView) findViewById(R.id.sh_quxiao);
         sousuo=(TextView) findViewById(R.id.sousuo);
        delete = (TextView)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
                    redianList.clear();
                    arrayList.clear();
                    adatper.notifyDataSetChanged();
                }catch (Exception e){

                }

            }
        });

        shQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPage.this.finish();
                hintKbTwo();
            }
        });
    }

    class redianAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return redianList.size();
        }

        @Override
        public Object getItem(int position) {
            return redianList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final HolderView  holder;
            if(convertView == null){
                holder = new HolderView();
                convertView = View.inflate(SearchPage.this, R.layout.redian,null);
                holder.txt = (TextView) convertView.findViewById(R.id.txt);
                convertView.setTag(holder);
            }else{
                holder= (HolderView) convertView.getTag();
            }
            holder.txt.setText(redianList.get(position));
            holder.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redianLine.setVisibility(View.GONE);
                    hintKbTwo();
                    shuju_line.setVisibility(View.GONE);
                    shuju_img.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    trim=redianList.get(position);

                    gethistory();
                }
            });
            return convertView;
        }
        class HolderView{
            TextView txt;
        }
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
                convertView = View.inflate(SearchPage.this, R.layout.xuqiuhistory, null);
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
                    redianLine.setVisibility(View.GONE);
                    hintKbTwo();
                    shuju_line.setVisibility(View.GONE);
                    shuju_img.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    gethistory();
                }
            });
            if(item.right == null){
                holder.right.setVisibility(View.GONE);
            }else {
                holder.right.setVisibility(View.VISIBLE);
                holder.right.setText(item.right);
                holder.right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trim = item.right;
                        progress.setVisibility(View.VISIBLE);
                        redianLine.setVisibility(View.GONE);
                        hintKbTwo();
                        shuju_line.setVisibility(View.GONE);
                        shuju_img.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        gethistory();
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


}
