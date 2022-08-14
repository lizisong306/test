package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.QingBaoEntity;
import entity.Qingbaodata;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/10/25.
 */

public class QingBaoActivity extends AutoLayoutActivity {
    ImageView yujian_backs;
    TextView quanbu,dingyue;
    RefreshListView listview;
    ProgressBar progress;
    List<Qingbaodata> listdata = new ArrayList<>();
    private DisplayImageOptions options;
    String json;
    int tabState = 0;
    QingBaoEntity data;
    QingBaoAdapter adapter;
    String teligId="";
    LinearLayout noicon;
    boolean isClear = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qingbaoactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        quanbu = (TextView)findViewById(R.id.quanbu);
        dingyue = (TextView)findViewById(R.id.dingyue);
        noicon = (LinearLayout)findViewById(R.id.noicon);
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar) findViewById(R.id.progress);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress.setVisibility(View.VISIBLE);
        if(tabState == 0){
            quanbu.setTextColor(0xff3385ff);
            dingyue.setTextColor(0xff8e8e8e);
        }
        quanbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabState =0;
                listview.setPullDownToRefreshable(true);
                listview.setPullUpToRefreshable(true);
                if(tabState == 0){
                    quanbu.setTextColor(0xff3385ff);
                    dingyue.setTextColor(0xff8e8e8e);
                }else{
                    quanbu.setTextColor(0xff8e8e8e);
                    dingyue.setTextColor(0xff3385ff);
                }
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(QingBaoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    isClear = true;
                    teligId="";
                    getJson();

                }

            }
        });

        dingyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(state.equals("0")){
                    Intent intent = new Intent(QingBaoActivity.this, MyloginActivity.class);
                    startActivity(intent);
                    return;
                }
                listview.setPullDownToRefreshable(true);
                listview.setPullUpToRefreshable(true);
                tabState =1;
                if(tabState == 0){
                    quanbu.setTextColor(0xff3385ff);
                    dingyue.setTextColor(0xff8e8e8e);
                }else{
                    quanbu.setTextColor(0xff8e8e8e);
                    dingyue.setTextColor(0xff3385ff);
                }
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(QingBaoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {

                    isClear = true;
                    teligId="";
                    getDingYuJson();

                }

            }
        });

        listview.setPullDownToRefreshable(true);
        listview.setPullUpToRefreshable(true);
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(QingBaoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    listview.setPullUpToRefreshable(true);
                    if(tabState == 0){
                        isClear = false;
                        if(listdata != null && listdata.size() >0){
                            teligId=listdata.get(listdata.size()-1).telig_id;
                        }
                        getJson();
                    }else {
                        if(listdata != null && listdata.size() >0){
                            teligId=listdata.get(listdata.size()-1).telig_ress_date;
                        }
                        isClear = false;
                        getDingYuJson();
                    }

                }
            }
        });
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(QingBaoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    teligId="";
                    if(tabState == 0){
                        isClear = true;
                        getJson();
                    }else {
                        isClear = true;
                        getDingYuJson();
                    }

                }

            }
        });
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getJson();
        }

    }
    class QingBaoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public Object getItem(int position) {
            return listdata.get(position);
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
                convertView = View.inflate(QingBaoActivity.this, R.layout.qingbaoadapter, null);
                holdView.qingbaoitem = (RelativeLayout)convertView.findViewById(R.id.qingbaoitem);
                holdView.icon = (ImageView)convertView.findViewById(R.id.icon);
                holdView.title =(TextView)convertView.findViewById(R.id.title);
                holdView.price = (TextView)convertView.findViewById(R.id.price);
                holdView.description = (TextView)convertView.findViewById(R.id.description);
                holdView.timeupdate = (TextView)convertView.findViewById(R.id.timeupdate);
                holdView.updatadescription=(TextView)convertView.findViewById(R.id.updatadescription);
                holdView.type = (ImageView)convertView.findViewById(R.id.type);


                holdView.dingyueitem = (RelativeLayout)convertView.findViewById(R.id.dingyueitem);
                holdView.banner = (ImageView)convertView.findViewById(R.id.banner);
                holdView.weidu = (TextView) convertView.findViewById(R.id.weidu);
                holdView.update = (TextView) convertView.findViewById(R.id.update);
                holdView.genxin = (TextView) convertView.findViewById(R.id.genxin);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            if(tabState ==0){
                holdView.qingbaoitem.setVisibility(View.VISIBLE);
                holdView.dingyueitem.setVisibility(View.GONE);
            }else {
                holdView.qingbaoitem.setVisibility(View.GONE);
                holdView.dingyueitem.setVisibility(View.VISIBLE);
            }

            try {
               final Qingbaodata item = listdata.get(position);
                if(tabState ==0) {
                    ImageLoader.getInstance().displayImage(item.telig_cover
                            , holdView.icon, options);
                    holdView.title.setText(item.telig_title);
                    holdView.description.setText(item.telig_slogan);
                    if(Float.parseFloat(item.telig_price) != 0){
                        holdView.price.setText(item.telig_price + "元/" + item.telig_unit);
                    }else{
                        holdView.price.setText("免费");
                    }

                    if(item.telig_journal_pubdate != null && !item.telig_journal_pubdate.equals("")){
                        holdView.timeupdate.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.telig_journal_pubdate)*1000), item.telig_journal_pubdate)+"更新");
                    }
                    if(item.telig_type.equals("0")){
                        holdView.type.setVisibility(View.GONE);
                    }else if(item.telig_type.equals("2")){
                        holdView.type.setVisibility(View.VISIBLE);
                        holdView.type.setBackgroundResource(R.mipmap.xianmian);
                    }else if(item.telig_type.equals("1")){
                        holdView.type.setVisibility(View.VISIBLE);
                        holdView.type.setBackgroundResource(R.mipmap.youhui);
                    }
                    holdView.updatadescription.setText(item.telig_journal_title);//暂定
                    holdView.genxin.setText(item.telig_journal_title);
                    holdView.qingbaoitem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(QingBaoActivity.this, QingBaoDeilActivity.class);
                            intent.putExtra("teligId",item.telig_id);
                            intent.putExtra("telig_price",item.telig_price);
                            intent.putExtra("telig_unit",item.telig_unit);
                            intent.putExtra("telig_type",item.telig_type);
                            startActivity(intent);
                        }
                    });
                }else{
                    ImageLoader.getInstance().displayImage(item.telig_banner
                            , holdView.banner, options);
                    holdView.weidu.setText(item.unreadcount+"篇未读");
                    if(item.telig_journal_pubdate != null && !item.telig_journal_pubdate.equals("")){
                        holdView.update.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.telig_journal_pubdate)*1000), item.telig_journal_pubdate)+"更新");
                    }
                    holdView.genxin.setText(item.telig_journal_title);
                    holdView.dingyueitem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(QingBaoActivity.this, QingBaoDeilActivity.class);
                            intent.putExtra("teligId",item.telig_id);
                            intent.putExtra("telig_price",item.telig_price);
                            intent.putExtra("telig_unit",item.telig_unit);
                            intent.putExtra("telig_type",item.telig_type);
                            startActivity(intent);

//                            Intent intent = new Intent(QingBaoActivity.this, FreeTryRead.class);
//                            intent.putExtra("teligId", item.telig_id);
//                            intent.putExtra("isRess", "1");
//                            intent.putExtra("unreadcount", item.unreadcount);
//                            intent.putExtra("telig_price",item.telig_price);
//                            intent.putExtra("telig_unit",item.telig_unit);
//                            intent.putExtra("telig_type",item.telig_type);

//                            startActivity(intent);

                        }
                    });
                }
            }catch (Exception e){

            }
            return convertView;
        }
        class HoldView{
            public RelativeLayout qingbaoitem;
            public ImageView icon;
            public TextView title;
            public TextView price,description,timeupdate,updatadescription;

            public RelativeLayout dingyueitem;
            public ImageView banner,type;
            public TextView weidu,update,genxin;


        }
    }
    private void getJson(){
        String url ="http://"+ MyApplication.ip+"/api/tellig/telig_list.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "20");
        map.put("teligId", teligId);
        networkCom.getJson(url,map,myhandler,1,0);
    }

    private void getDingYuJson(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        String url ="http://"+ MyApplication.ip+"/api/tellig/telig_myress.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "20");
        map.put("teligressdate", teligId);
        map.put("mid", mid);
        networkCom.getJson(url,map,myhandler,1,0);
    }
    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
            if(msg.what == 1){
                progress.setVisibility(View.GONE);
                listview.setPullDownToRefreshFinish();
                listview.setPullUpToRefreshFinish();
                if(isClear){
                    isClear = false;
                    listdata.clear();
                }
                Gson gson = new Gson();
                json = (String)msg.obj;
                data = gson.fromJson(json, QingBaoEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        if(data.data != null && data.data.size() > 0){
                            for (int i=0; i<data.data.size(); i++){
                                Qingbaodata item = data.data.get(i);
                                listdata.add(item);
                            }
                        }
                    }
                }
                if(data.data != null && data.data.size() < 20){
                    listview.setPullUpToRefreshable(false);
//                    listview.setPullDownToRefreshable(false);
                }
                if(adapter == null){
                    listview.setBackgroundColor(0xffF6F6F6);
                    adapter = new QingBaoAdapter();
                    listview.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
                if(tabState == 1){
                    if(data == null){
                        noicon.setVisibility(View.VISIBLE);
                    }else{
                       if(data.data != null && data.data.size() == 0){
                           noicon.setVisibility(View.VISIBLE);
                       }else{
                           noicon.setVisibility(View.GONE);
                       }
                    }
                }else{
                    noicon.setVisibility(View.GONE);
                }
            }
            }catch (Exception e){

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("情报");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("情报");
        MobclickAgent.onPause(this);
    }
}
