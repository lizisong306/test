package com.maidiantech;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
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

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import adapter.SearchmoreAdapter;
import application.MyApplication;
import entity.Counts;
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
import view.ExpandTabView;
import view.StyleUtils;
import view.StyleUtils1;
import view.ViewMiddle;

/**
 * Created by lizisong on 2017/6/15.
 */

public class SearchTypeResult extends AutoLayoutActivity {
    private AutoLinearLayout detailRa;
    private RelativeLayout rlSearchFrameDelete;
    private AutoCompleteTextView search;
    private ImageView ivDeleteText;
    private ImageView shQuxiao;
    private  int netWorkType;
    private String searchjsons;
    private LinearLayout shuju_line;
    private ImageView shuju_img;
    private ListView search_listview_info;
    private  RelativeLayout redianLine;
    private ListView grid;
    private String trim;
    private TextView sousuo;
    redianAdapter redianAdapter;
    String redianStr;
    List<Zxlist> zixun ;
    List<Renlist> rencai ;
    List<Sblist> shebei ;
    List<Xmlist> xiangmu;
    List<Zclist> zhengce ;
    List<Zllist> zhuanli ;
    List<Ztlist> zhuanti ;
    List<Shiyanshi> shiyanshi ;
    List<Tuijian> tuijian ;
    private List<String> redianList = new ArrayList<String>();
    private List<Object> dataList;
    private List<String> typeList;
    Gson g;
    private ProgressBar progress;
    private String type;
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private TextView zc_guanzhu;
    private ViewMiddle viewMiddle;
    private SearchmoreAdapter moreadapter;
    private  String   ips;
    private LinearLayout search_top;
    private TextView zc_order;
    private boolean zc_state=false;
    private String place="";
    private String der="0";
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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


//                    if(xiangmu!=null) {
//                        dataList.add(xiangmu);
//                        typeList.add("xiangmu");
//                    }
//                    if(rencai!=null) {
//                        dataList.add(rencai);
//                        typeList.add("rencai");
//                    }
//                    if(shiyanshi!=null) {
//                        dataList.add(shiyanshi);
//                        typeList.add("shiyanshi");
//                    }
//                    if(shebei!=null) {
//                        dataList.add(shebei);
//                        typeList.add("shebei");
//                    }
//                    if(zhuanli!=null) {
//                        dataList.add(zhuanli);
//                        typeList.add("zhuanli");
//                    }
//
//                    if(zhengce!=null) {
//                        dataList.add(zhengce);
//                        typeList.add("zhengce");
//                    }

                    if(searchcode.getMessage().equals("获取信息成功！")) {
                        shuju_line.setVisibility(View.GONE);
                        shuju_img.setVisibility(View.GONE);
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
                        if(type.equals("xiangmu")){
                            typeList.add("xiangmu");
                            if(xiangmu==null || xiangmu.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else if(type.equals("zhengce")){
                            typeList.add("zhengce");
                            if(zhengce==null || zhengce.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else if(type.equals("rencai")){
                            typeList.add("rencai");
                            if(rencai==null || rencai.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else if(type.equals("shebei")){
                            typeList.add("shebei");
                            if(shebei==null || shebei.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else if(type.equals("shiyanshi")){
                            typeList.add("shiyanshi");
                            if(shiyanshi==null || shiyanshi.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else if(type.equals("zhuangli")){
                            typeList.add("zhuanli");
                            if(zhuanli==null || zhuanli.size()==0){
                                shuju_line.setVisibility(View.VISIBLE);
                                shuju_img.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                        moreadapter=new SearchmoreAdapter(SearchTypeResult.this,zixun,shiyanshi,shebei,xiangmu,zhengce,rencai,zhuanli,tuijian,typeList,0);
                        search_listview_info.setAdapter(moreadapter);
                        search_listview_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(type.equals("rencai")){
                                    PrefUtils.setString(SearchTypeResult.this, rencai.get(position).getId(), rencai.get(position).getId());
                                    Intent intent = new Intent(SearchTypeResult.this, XinFanAnCeShi.class);
                                    intent.putExtra("aid", rencai.get(position).getId());
                                    startActivity(intent);
                                }else{
                                    Intent intent=new Intent(SearchTypeResult.this,DetailsActivity.class);
                                    if(type.equals("zhuangli")){
                                        PrefUtils.setString(SearchTypeResult.this, zhuanli.get(position).getId(), zhuanli.get(position).getId());
                                        intent.putExtra("id",zhuanli.get(position).getId());
                                        intent.putExtra("name", zhuanli.get(position).getTypename());
                                        intent.putExtra("pic",zhuanli.get(position).getLitpic());
                                    }else if(type.equals("shebei")){
                                        PrefUtils.setString(SearchTypeResult.this, shebei.get(position).getId(), shebei.get(position).getId());
                                        intent.putExtra("id",shebei.get(position).getId());
                                        intent.putExtra("name", shebei.get(position).getTypename());
                                        intent.putExtra("pic",shebei.get(position).getLitpic());
                                    }else if(type.equals("rencai")){
                                        PrefUtils.setString(SearchTypeResult.this, rencai.get(position).getId(), rencai.get(position).getId());
                                        intent.putExtra("id",rencai.get(position).getId());
                                        intent.putExtra("name", rencai.get(position).getTypename());
                                        intent.putExtra("pic",rencai.get(position).getLitpic());
                                    }else if(type.equals("xiangmu")){
                                        PrefUtils.setString(SearchTypeResult.this, xiangmu.get(position).getId(), xiangmu.get(position).getId());
                                        intent.putExtra("id",xiangmu.get(position).getId());
                                        intent.putExtra("name", xiangmu.get(position).getTypename());
                                        intent.putExtra("pic",xiangmu.get(position).getLitpic());
                                    }else if(type.equals("shiyanshi")){
                                        PrefUtils.setString(SearchTypeResult.this, shiyanshi.get(position).getId(), shiyanshi.get(position).getId());
                                        intent.putExtra("id",shiyanshi.get(position).getId());
                                        intent.putExtra("name", shiyanshi.get(position).getTypename());
                                        intent.putExtra("pic",shiyanshi.get(position).getLitpic());
                                    }else if(type.equals("zhengce")){
                                        PrefUtils.setString(SearchTypeResult.this, zhengce.get(position).getId(), zhengce.get(position).getId());
                                        intent.putExtra("id",zhengce.get(position).getId());
                                        intent.putExtra("name", zhengce.get(position).getTypename());
                                        intent.putExtra("pic",zhengce.get(position).getLitpic());
                                    }
                                    startActivity(intent);
                                }

                            }
                        });
                    }


                }catch (Exception e){
                    Toast.makeText(SearchTypeResult.this, "没有相关数据", Toast.LENGTH_SHORT).show();
                    shuju_line.setVisibility(View.VISIBLE);
                    shuju_img.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        StyleUtils1.initSystemBar(this);
        StyleUtils1.setStyle(this);
        type = getIntent().getStringExtra("type");
        trim = getIntent().getStringExtra("trim");
        ips = MyApplication.ip;
        initView();
        try {
            if(trim!=null || trim.equals("")){
                search.setText(trim);
                search.setSelection(trim.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        netWorkType = NetUtils.getNetWorkType(MyApplication
//                .getContext());
//        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//            Toast.makeText(SearchTypeResult.this, "网络不给力", Toast.LENGTH_SHORT).show();
//        }else{
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//
//                               public void run() {
//                                   InputMethodManager inputManager =
//                                           (InputMethodManager) search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                   inputManager.showSoftInput(search, 0);
//                               }
//
//                           },
//                    500);
//        }

        search_listview_info=(ListView) findViewById(R.id.search_listview_info);
        shuju_line=(LinearLayout) findViewById(R.id.shuju_line) ;
        shuju_img=(ImageView) findViewById(R.id.shuju_img);
        progress=(ProgressBar) findViewById(R.id.progress);
        redianLine = (RelativeLayout)findViewById(R.id.redian);
        redianAdapter = new redianAdapter();
        if(type.equals("xiangmu")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_XIANGMU,"");
        }else if(type.equals("zhengce")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_ZHENGCE,"");
        }else if(type.equals("rencai")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_RENCAI,"");
        }else if(type.equals("shebei")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_SHEBEI,"");
        }else if(type.equals("shiyanshi")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_SHIYANSHI,"");
        }else if(type.equals("zhuangli")){
            redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.SOUSUO_ZHUANLI,"");
        }

        if(redianStr != null && !redianStr.equals("")){
            String[] temp = redianStr.split(";");
            if(temp != null){
                for (int i=0; i<temp.length;i++){
                    String pos=temp[i];
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        redianList.add(pos);
                    }
                }
            }
        }

        if(redianList.size() >0){
            redianLine.setVisibility(View.GONE);
        }else {
            redianLine.setVisibility(View.GONE);
        }
        grid = (ListView)findViewById(R.id.redian_grid);
        grid.setAdapter(redianAdapter);

        progress.setVisibility(View.GONE);
        set_eSearch_TextChanged();
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(SearchTypeResult.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(SearchTypeResult.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(trim)==false){
                            Toast.makeText(SearchTypeResult.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                redianLine.setVisibility(View.GONE);
                                hintKbTwo();
                                shuju_line.setVisibility(View.GONE);
                                shuju_img.setVisibility(View.GONE);
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
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
//                trim = search.getText().toString().trim();
//                netWorkType = NetUtils.getNetWorkType(MyApplication
//                        .getContext());
//                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//                    Toast.makeText(SearchTypeResult.this, "网络不给力", Toast.LENGTH_SHORT).show();
//                }else{
//                    if(trim.equals("")){
//                        Toast.makeText(SearchTypeResult.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
//                    }
//                    else if(TelNumMatch.issearch(trim)==false){
//                        Toast.makeText(SearchTypeResult.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//
//
//                        redianLine.setVisibility(View.GONE);
                     //   hintKbTwo();
//                        shuju_line.setVisibility(View.GONE);
//                        shuju_img.setVisibility(View.GONE);
//                        progress.setVisibility(View.VISIBLE);
//                        gethistory(place,der);


//                                Intent intent = new Intent(SearchPage.this, SearchInfo.class);
//                                intent.putExtra("trim",trim);
//                                startActivity(intent);


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

        redianLine.setVisibility(View.GONE);
        hintKbTwo();
        progress.setVisibility(View.VISIBLE);
        gethistory(place,der);

        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        zc_guanzhu=(TextView) findViewById(R.id.zc_guanzhu);
        search_top=(LinearLayout) findViewById(R.id.search_top);
        zc_order=(TextView) findViewById(R.id.zc_order);
        if(type.equals("zhengce")){
            search_top.setVisibility(View.VISIBLE);
        }
        viewMiddle = new ViewMiddle(this);
        initVaule();
        initListener();
        zc_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(zc_state==false){
                        if(zhengce==null){
                            return;
                        }
                        zc_order.setTextColor(getResources().getColor(R.color.lansecolor));
                        progress.setVisibility(View.VISIBLE);
                        if(zhengce!=null){
                            zhengce.clear();
                        }
                        moreadapter.notifyDataSetChanged();
                        zc_state=true;

                        if(zc_state==false){
                            der="0";
                        }else{
                            der="1";
                        }
                        gethistory(place,der);
                    }else{
                        if(zhengce==null){
                            return;
                        }
                        zc_state=false;
                        zc_order.setTextColor(getResources().getColor(R.color.title));
                        progress.setVisibility(View.VISIBLE);
                        if(zhengce!=null){
                            zhengce.clear();
                        }
                        moreadapter.notifyDataSetChanged();
                        if(zc_state==false){
                            der="0";

                        }else{
                            der="1";

                        }
                        gethistory(place,der);
                    }
                }catch (Exception e){}

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
        shQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTypeResult.this.finish();
                hintKbTwo();
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

    class redianAdapter extends BaseAdapter {
        private  String   ips;
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
            final HolderView holder;
            if(convertView == null){
                holder = new HolderView();

                convertView = View.inflate(SearchTypeResult.this, R.layout.redian,null);
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

                    gethistory(place,der);
                }
            });
            return convertView;
        }
        class HolderView{
            TextView txt;
        }
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
                        }else if(type.equals("zhengce")){
                            typeid ="6";
                        }else if(type.equals("rencai")){
                            typeid ="4";
                        }else if(type.equals("shebei")){
                            typeid ="7";
                        }else if(type.equals("shiyanshi")){
                            typeid ="8";
                        }else if(type.equals("zhuangli")){
                            typeid ="5";
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
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
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

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索");
        MobclickAgent.onResume(this);
        if(moreadapter!=null){
            moreadapter.notifyDataSetChanged();
        }
    }

    private void initVaule() {

//		mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);


        ArrayList<String> mTextArray = new ArrayList<String>();
//		mTextArray.add("距离");
        mTextArray.add("区域筛选");

//        mTextArray.add("升级");
//		mTextArray.add("距离");
        expandTabView.setValue(mTextArray, mViewArray);
        expandTabView.text_color(0xff3e3e3e);
//		expandTabView.setTitle(viewLeft.getShowText(), 0);
//        expandTabView.setTitle(viewMiddle.getShowText(), 0);
//		expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    private void initListener() {

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText, String value) {
                onRefresh(viewMiddle,showText,value);
            }
        });

    }

    private void onRefresh(View view, String showText, String value) {

        expandTabView.onPressBack();
        int position = getPositon(view);

        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
            expandTabView.setColor(getResources().getColor(R.color.lansecolor),position);
            expandTabView.setDrawableLeft(R.mipmap.arrows_h,position);


        }
        progress.setVisibility(View.VISIBLE);
        if(zhengce!=null){
            zhengce.clear();
        }
        if(moreadapter!=null){
            moreadapter.notifyDataSetChanged();
        }
        place=value;
        gethistory(place,der);

    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {

        if (!expandTabView.onPressBack()) {
            finish();
        }

    }
}
