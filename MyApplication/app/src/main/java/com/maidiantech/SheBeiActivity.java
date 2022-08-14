package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import Util.NetUtils;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.DeviceData;
import entity.SheBeiEntity;
import entity.SheBeiPost;
import entity.ShowDevice;
import entity.ShowUnitedStatesData;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by Administrator on 2018/12/11.
 */

public class SheBeiActivity extends AutoLayoutActivity {
    private DisplayImageOptions options;
    ImageView back, search;
    private ProgressBar progress;
    RefreshListView listview;
//    ListView listtype1, listtype2;
    TextView type, lingyu;
//    RelativeLayout typeare1, typeare2;
    ImageView tips;
    SheBeiEntity sheBeiEntity;
//    AreaAdapter areaAdapter;
//    LabelAdapter labelAdapter;
//    private int labelIndex = 0, areaIndex = 0;
//    List<Aree> aree = new ArrayList<>();
//    List<Label> labels = new ArrayList<>();
//    List<SheBeiPost> showDeviceList = new ArrayList<>();
//    List<DeviceData> showAttribute = new ArrayList<>();
    SheBeiAdapter adapter;
    TextView xuanfubiaoti;
    List<ShowDevice> showDevice = new ArrayList<>();
    private ShowDevice biaoti = null;
    private String Size = "20";
    private String pubdate = "";
    private String evalue = "", label = "", sortid = "",stype="";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shebeiactivity);
        options = ImageLoaderUtils.initOptions();
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        back = (ImageView) findViewById(R.id.shezhi_backs);
        search = (ImageView) findViewById(R.id.search);
        progress = (ProgressBar) findViewById(R.id.progress);
        type = (TextView) findViewById(R.id.type);
        lingyu = (TextView) findViewById(R.id.lingyu);
        xuanfubiaoti = (TextView)findViewById(R.id.xuanfubiaoti);
        xuanfubiaoti.setVisibility(View.GONE);
        xuanfubiaoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SheBeiActivity.this, SheBeiDeilActivity.class);
                    intent.putExtra("aid", biaoti.deviceid);
                    startActivity(intent);
                }catch (Exception e){

                }
            }
        });
        listview = (RefreshListView)findViewById(R.id.listview);
//        typeare1 = (RelativeLayout) findViewById(R.id.typeare1);
//        typeare2 = (RelativeLayout) findViewById(R.id.typeare2);
//        tips = (ImageView) findViewById(R.id.tipsbg);
//        tips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tips.setVisibility(View.GONE);
//                typeare1.setVisibility(View.INVISIBLE);
//                typeare2.setVisibility(View.INVISIBLE);
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SheBeiActivity.this, NewSearchHistory.class);
                intent.putExtra("typeid", "14");
                startActivity(intent);
            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setTextColor(0xffffc1a5);
                lingyu.setTextColor(0xff181818);
                if(stype.equals("0")){
                    return;
                }

                xuanfubiaoti.setVisibility(View.GONE);
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(SheBeiActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listview.setPullDownToRefreshFinish();
                } else {
                    sortid = "";
                    stype="0";
                    progress.setVisibility(View.VISIBLE);
                    getJson(Size, stype, sortid, true);
                }
//                if (typeare1.getVisibility() == View.INVISIBLE) {
//                    typeare1.setVisibility(View.VISIBLE);
//                    tips.setVisibility(View.VISIBLE);
//                } else if (typeare1.getVisibility() == View.VISIBLE) {
//                    typeare1.setVisibility(View.INVISIBLE);
//                    tips.setVisibility(View.GONE);
//                }
            }
        });
        lingyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setTextColor(0xff181818);
                lingyu.setTextColor(0xffffc1a5);
                if(stype.equals("1")){
                    return ;
                }
                xuanfubiaoti.setVisibility(View.VISIBLE);

                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(SheBeiActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listview.setPullDownToRefreshFinish();
                } else {
                    sortid = "";
                    stype="1";
                    progress.setVisibility(View.VISIBLE);
                    getJson(Size, stype, sortid, true);
                }
//                if (typeare2.getVisibility() == View.INVISIBLE) {
//                    typeare2.setVisibility(View.VISIBLE);
//                    tips.setVisibility(View.VISIBLE);
//                } else if (typeare2.getVisibility() == View.VISIBLE) {
//                    typeare2.setVisibility(View.INVISIBLE);
//                    tips.setVisibility(View.GONE);
//                }
            }
        });

        listview.setPullDownToRefreshable(true);
        listview.setPullUpToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                listview.setPullUpToRefreshFinish();
                listview.setPullUpToRefreshable(true);
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(SheBeiActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    pubdate = "";
                    sortid = "";
                    listview.setPullUpToRefreshable(true);
                    getJson(Size, stype, sortid, true);
                }
            }
        });

        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(SheBeiActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listview.setPullDownToRefreshFinish();
                } else {
                    if(showDevice.size() > 1){
                        if(stype.equals("0")){
                            sortid = showDevice.get(showDevice.size()-2).id;
                        }else{
                            for(int i=showDevice.size()-1; i>=0;i--){
                                ShowDevice item = showDevice.get(i);
                                if(item.typeid.equals("20")){
                                    sortid = item.id;
                                    break;
                                }
                            }
                        }
                        getJson(Size, stype, sortid, false);
                    }
                }
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    scrollFlag = true;
//                } else {
//                    scrollFlag = false;
//                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {

                    int index =  listview.getFirstVisiblePosition();
                    if(showDevice.size() >0){
                        ShowDevice item = null;
                        if(firstVisibleItem > 0){
                            item = showDevice.get(firstVisibleItem-1);
                            biaoti = item;
                            String name = item.deviceName;
                            String devcie = xuanfubiaoti.getText().toString();
                            if(devcie != null && !devcie.equals("")){
                                if(!devcie.equals(name) && !item.typeid.equals("20")){
                                    xuanfubiaoti.setText(item.deviceName);
                                }
                            }else{
                                xuanfubiaoti.setText(item.deviceName);
                            }

                        }else{
                            item = showDevice.get(0);
                            biaoti = item;

                            String name = item.deviceName;
                            String devcie = xuanfubiaoti.getText().toString();
                            if(devcie != null && !devcie.equals("")){
                                if(!devcie.equals(name) && !item.typeid.equals("20")){
                                    xuanfubiaoti.setText(item.deviceName);
                                }
                            }else{
                                xuanfubiaoti.setText(item.deviceName);
                            }
                        }
                    }

                }catch (Exception e){

                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(SheBeiActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
            listview.setPullDownToRefreshFinish();
        } else {
            sortid = "";
            stype="0";
            progress.setVisibility(View.VISIBLE);
            getJson(Size, stype, sortid, true);
        }
    }

    private void getJson(String size, String stype, String sortid,boolean state) {
        String url = "http://" + MyApplication.ip + "/api/indexDevicesApi.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pagesize", size);
        map.put("stype", stype);
        map.put("sortid", sortid);
        if(state){
            networkCom.getJson(url, map, handler, 1, 1);
        } else {
            networkCom.getJson(url, map, handler, 1, 0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    progress.setVisibility(View.GONE);
                    listview.setPullDownToRefreshFinish();
                    listview.setPullUpToRefreshFinish();
                    if(msg.arg1 == 1){
//                        listview.setSelection(0);
//                        if(adapter != null){
//                            adapter.notifyDataSetChanged();
//                        }
                        showDevice.clear();
                    }
                    String res = (String) msg.obj;
                    Gson gson = new Gson();
                    sheBeiEntity = gson.fromJson(res, SheBeiEntity.class);
                    if (sheBeiEntity != null) {
                        if (sheBeiEntity.code != null) {
                            if (sheBeiEntity.code.equals("1")) {
                                if (sheBeiEntity.data != null) {
//                                    if (aree.size() == 0) {
//                                        if (sheBeiEntity.data.area != null) {
//                                            if (sheBeiEntity.data.area != null) {
//                                                for (int i = 0; i < sheBeiEntity.data.area.size(); i++) {
//                                                    Aree item = sheBeiEntity.data.area.get(i);
//                                                    aree.add(item);
//                                                }
//                                            }
//                                            if (areaAdapter == null) {
//                                                areaAdapter = new AreaAdapter();
//                                                listtype1.setAdapter(areaAdapter);
//                                            }
//                                        }
//                                    }

//                                    if (labels.size() == 0) {
//                                        if (sheBeiEntity.data.label != null) {
//                                            for (int i = 0; i < sheBeiEntity.data.label.size(); i++) {
//                                                Label item = sheBeiEntity.data.label.get(i);
//                                                labels.add(item);
//                                            }
//                                            if (labelAdapter == null) {
//                                                labelAdapter = new LabelAdapter();
//                                                listtype2.setAdapter(labelAdapter);
//                                            }
//                                        }
//                                    }

                                    if(sheBeiEntity.data.posts != null){
                                        for(int i=0; i<sheBeiEntity.data.posts.size();i++){
                                            SheBeiPost item = sheBeiEntity.data.posts.get(i);
                                            ShowDevice pos = new ShowDevice();
                                            pos.id = item.id;
                                            pos.image = item.image;
                                            pos.imageState = item.imageState;
                                            pos.largeType = item.largeType;
                                            pos.litpic = item.litpic;
                                            pos.count = item.count;
                                            pos.pubdate = item.pubdate;
                                            pos.serviceCount = item.serviceCount;
                                            pos.title = item.title;
                                            pos.typeid = item.typeid;
                                            pos.typename = item.typename;
                                            pos.url = item.url;
                                            if(stype.equals("0")){
                                               showDevice.add(pos);
                                                ShowDevice line = new ShowDevice();
                                                line.typeid="0";
                                                line.deviceid=item.id;
                                                showDevice.add(line);
                                            }
                                            if(item.deviceServiceList != null){
                                                ShowDevice title = new ShowDevice();
                                                title.deviceName=item.title;
                                                title.id=item.id;
                                                title.deviceid = item.id;
                                                title.typeid = "20";
                                                if(stype.equals("1")){
                                                   showDevice.add(title);
                                                }
                                                for(int j=0; j<item.deviceServiceList.size();j++){
                                                    DeviceData oo = item.deviceServiceList.get(j);
                                                    ShowDevice tit = new ShowDevice();
                                                    tit.deviceName = item.title;
                                                    tit.deviceid = item.id;
                                                    tit.id = oo.id;
                                                    tit.typeid ="21";
                                                    tit.sampleType = oo.sampleType;
                                                    tit.serviceName = oo.serviceName;
                                                    if(stype.equals("1")){
                                                       showDevice.add(tit);
                                                        if(j == item.deviceServiceList.size()-1){
                                                            ShowDevice jiange = new ShowDevice();
                                                            jiange.typeid="-1";
                                                            jiange.deviceid=item.id;
                                                            jiange.deviceName=item.title;
                                                            showDevice.add(jiange);
                                                        }else{
                                                            ShowDevice line = new ShowDevice();
                                                            line.typeid="0";
                                                            line.deviceid=item.id;
                                                            line.deviceName=item.title;
                                                            showDevice.add(line);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if(sheBeiEntity.data.posts.size()<20){
                                            listview.setPullUpToRefreshable(false);
                                            ShowDevice dixian = new ShowDevice();
                                            dixian.typeid="-2";
                                            showDevice.add(dixian);
                                        }

                                    }else{
                                        listview.setPullUpToRefreshable(false);
                                        ShowDevice dixian1 = new ShowDevice();
                                        dixian1.typeid="-2";
                                        showDevice.add(dixian1);
                                    }

                                }
                                if(adapter == null){
                                    adapter = new SheBeiAdapter();
                                    listview.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                    if(msg.arg1 == 1){
                                        listview.setSelection(0);
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {

            }

        }
    };

    class SheBeiAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            return showDevice.size();
        }

        @Override
        public Object getItem(int position) {
            return showDevice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if(convertView == null){
                convertView = View.inflate(SheBeiActivity.this, R.layout.shebeiadapter, null);
                holdView = new HoldView();
                holdView.device_lay = convertView.findViewById(R.id.device_lay);
                holdView.device = convertView.findViewById(R.id.device);
                holdView.count = convertView.findViewById(R.id.count);
                holdView.right = convertView.findViewById(R.id.right);
                holdView.right1 = convertView.findViewById(R.id.right1);
                holdView.source1=convertView.findViewById(R.id.source1);
                holdView.count1 = convertView.findViewById(R.id.count1);

                holdView.lanyuan = convertView.findViewById(R.id.lanyuan);
                holdView.source  = convertView.findViewById(R.id.source);
                holdView.title = convertView.findViewById(R.id.title);

                holdView.fenge = convertView.findViewById(R.id.fenge);

                holdView.biaoti = convertView.findViewById(R.id.biaoti);

                holdView.service = convertView.findViewById(R.id.service);
                holdView.service_title = convertView.findViewById(R.id.service_title);
                holdView.servicelingyu = convertView.findViewById(R.id.servicelingyu);
                holdView.yuyue = convertView.findViewById(R.id.yuyue);
                holdView.dixian = convertView.findViewById(R.id.dixian);
                holdView.line = convertView.findViewById(R.id.line);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final ShowDevice item = showDevice.get(position);
            if(item.typeid.equals("0")){
                holdView.dixian.setVisibility(View.GONE);
                holdView.line.setVisibility(View.VISIBLE);
                holdView.service.setVisibility(View.GONE);
                holdView.biaoti.setVisibility(View.GONE);
                holdView.fenge.setVisibility(View.GONE);
                holdView.device_lay.setVisibility(View.GONE);
            }else if(item.typeid.equals("-1")){
                holdView.dixian.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.biaoti.setVisibility(View.GONE);
                holdView.fenge.setVisibility(View.VISIBLE);
                holdView.device_lay.setVisibility(View.GONE);
            }else if(item.typeid.equals("14")){
                holdView.dixian.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.biaoti.setVisibility(View.GONE);
                holdView.fenge.setVisibility(View.GONE);
                holdView.device_lay.setVisibility(View.VISIBLE);
                if(item.litpic != null && !item.litpic.equals("")){
                    holdView.device.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.litpic
                            , holdView.device, options);
                }else{
                    holdView.device.setVisibility(View.GONE);
                }
                holdView.title.setText(item.title);
                holdView.lanyuan.setText(item.largeType);
                holdView.source.setText(item.serviceCount);
                holdView.count.setText(item.count);
                holdView.source1.setText(item.serviceCount);
                holdView.count1.setText(item.count);

                if(item.count == null || (item.count!= null && item.count.equals("0"))){
                       holdView.right1.setVisibility(View.GONE);
                       holdView.right.setVisibility(View.VISIBLE);
                       holdView.count.setVisibility(View.GONE);
                       holdView.source.setText(item.serviceCount);
                }else{
                    holdView.right1.setVisibility(View.VISIBLE);
                    holdView.right.setVisibility(View.GONE);
//                        holdView.count.setVisibility(View.VISIBLE);
//                        holdView.source.setText("      "+item.serviceCount);
                 }
                holdView.device_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SheBeiActivity.this, SheBeiDeilActivity.class);
                        intent.putExtra("aid", item.id);
                        startActivity(intent);
                    }
                });


            }else if(item.typeid.equals("20")){
                holdView.line.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.biaoti.setVisibility(View.VISIBLE);
                holdView.fenge.setVisibility(View.GONE);
                holdView.device_lay.setVisibility(View.GONE);
                if(position == 0){
                    biaoti = item;
                    holdView.biaoti.setVisibility(View.GONE);
                }else{
                    holdView.biaoti.setVisibility(View.VISIBLE);
                }
                holdView.biaoti.setText(item.deviceName);
                holdView.biaoti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SheBeiActivity.this, SheBeiDeilActivity.class);
                        intent.putExtra("aid", item.id);
                        startActivity(intent);
                    }
                });
            }else if(item.typeid.equals("21")){
                holdView.dixian.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.service.setVisibility(View.VISIBLE);
                holdView.biaoti.setVisibility(View.GONE);
                holdView.fenge.setVisibility(View.GONE);
                holdView.device_lay.setVisibility(View.GONE);
                holdView.service_title.setText(item.serviceName);
                holdView.servicelingyu.setText(item.sampleType);
                holdView.yuyue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SheBeiActivity.this, "此功能暂未开放", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(item.typeid.equals("-2")){
                holdView.line.setVisibility(View.GONE);
                holdView.service.setVisibility(View.GONE);
                holdView.biaoti.setVisibility(View.GONE);
                holdView.fenge.setVisibility(View.GONE);
                holdView.device_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        class HoldView{
            //设备
            public RelativeLayout device_lay,right,right1;
            public ImageView device;
            public TextView title,lanyuan,source,source1,count,count1;

            //标题
            public TextView biaoti;

            //服务
            public RelativeLayout service;
            public TextView service_title, servicelingyu;
            public ImageView yuyue;
            //线
            public TextView line;
            //分割
            public LinearLayout fenge;
            //底线
            public RelativeLayout dixian;

        }
    };




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SheBei Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


//    class AreaAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return aree.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return aree.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final HoldView holdView;
//            if (convertView == null) {
//                convertView = View.inflate(SheBeiActivity.this, R.layout.arealabeladapter, null);
//                holdView = new HoldView();
//                holdView.txt = convertView.findViewById(R.id.txt);
//                holdView.lines = convertView.findViewById(R.id.lines);
//                convertView.setTag(holdView);
//
//            } else {
//                holdView = (HoldView) convertView.getTag();
//            }
//            final Aree area = aree.get(position);
//            holdView.txt.setText(area.ename);
//            holdView.txt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (area.ename.equals("不限")) {
//                        type.setText("项目类型");
//                        evalue = "";
//                    } else {
//                        type.setText(area.ename);
//                        evalue = area.evalue;
//                    }
//                    areaIndex = position;
//                    typeare1.setVisibility(View.INVISIBLE);
//                    tips.setVisibility(View.GONE);
//                    progress.setVisibility(View.VISIBLE);
//                    pubdate = "";
//                    sortid = "";
//                    areaAdapter.notifyDataSetChanged();
////                    getJson(Size, pubdate, sortid, evalue, label, true);
//
//                }
//            });
//            Log.d("lizisong", "areaIndex:"+areaIndex);
//            if(areaIndex == position){
//                holdView.txt.setTextColor(0xff3385ff);
//            }else{
//                holdView.txt.setTextColor(0xff181818);
//            }
//
//            if(position == aree.size()-1){
//                holdView.lines.setVisibility(View.GONE);
//            }else{
//                holdView.lines.setVisibility(View.VISIBLE);
//            }
//            return convertView;
//        }
//
//        class HoldView {
//            public TextView txt;
//            public TextView lines;
//        }
//    }
//
//    class LabelAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return labels.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return labels.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//           HoldView holdView;
//            if (convertView == null) {
//                convertView = View.inflate(SheBeiActivity.this, R.layout.arealabeladapter, null);
//                holdView = new HoldView();
//                holdView.txt = convertView.findViewById(R.id.txt);
//                holdView.lines = convertView.findViewById(R.id.lines);
//                convertView.setTag(holdView);
//
//            } else {
//                holdView = (HoldView) convertView.getTag();
//            }
//            final Label label = labels.get(position);
//            holdView.txt.setText(label.name);
//            holdView.txt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (label.name.equals("不限")) {
//                        lingyu.setText("所属领域");
////                        NewProJect.this.label = "";
//                    } else {
//                        lingyu.setText(label.name);
////                        NewProJect.this.label = label.label;
//                    }
//                    labelIndex = position;
//                    typeare2.setVisibility(View.INVISIBLE);
//                    tips.setVisibility(View.GONE);
//                    labelAdapter.notifyDataSetChanged();
//                    progress.setVisibility(View.VISIBLE);
//                    pubdate = "";
//                    sortid = "";
////                    getJson(Size, pubdate, sortid, evalue,  NewProJect.this.label, true);
//                }
//            });
//            if(position == labels.size()-1){
//                holdView.lines.setVisibility(View.GONE);
//            }else{
//                holdView.lines.setVisibility(View.VISIBLE);
//            }
//
//            if(labelIndex == position){
//                holdView.txt.setTextColor(0xff3385ff);
//            }else{
//                holdView.txt.setTextColor(0xFF181818);
//            }
//
//            return convertView;
//        }
//
//        class HoldView {
//            public TextView txt,top,bottom;
//            public TextView lines;
//        }
//    }
}
