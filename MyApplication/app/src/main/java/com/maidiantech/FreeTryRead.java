package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.FreeTryReadEntity;
import entity.ReadData;
import entity.ShowRead;
import view.RefreshListView;
import view.StyleUtils1;
import Util.TimeUtils;

import static com.maidiantech.R.id.listview;

/**
 * Created by lizisong on 2017/10/27.
 */

public class FreeTryRead extends AutoLayoutActivity {
    ImageView yujian_backs;
    TextView biaoti;
    RefreshListView listView;
    ProgressBar progress;
    View view;
    ImageView icon,paixu;
    TextView title,description,shuixu;
    String json,teligId;
    FreeTryReadEntity data;
    private DisplayImageOptions options;
    int paixuState = 0;
    public String journalpubdate = "";
    List<ShowRead> temp = new ArrayList<>();
    List<ShowRead> showReadList = new ArrayList<>();
    FreeTryReadAdapter adapter;
    int netWorkType;
    int unreadcount = 0;
    String isRess = "";
    boolean isClear = false;
    String descrip;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freetryread);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        biaoti = (TextView)findViewById(R.id.biaoti);
        listView = (RefreshListView) findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        view = View.inflate(this, R.layout.freetryreadheart, null);
        icon = (ImageView)view.findViewById(R.id.icon);
        paixu = (ImageView)view.findViewById(R.id.paixu);
        title = (TextView)view.findViewById(R.id.xiangqingbiaoti);
        description = (TextView)view.findViewById(R.id.description);
        shuixu = (TextView)view.findViewById(R.id.shuixu);
        paixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paixuState == 0){
                    paixuState =1;
                }else if(paixuState == 1){
                    paixuState =0;
                }

                if(paixuState == 0){
                    paixu.setImageResource(R.mipmap.daoxu);
                    shuixu.setText("倒序");
                }else if(paixuState == 1){
                    paixu.setImageResource(R.mipmap.zhengxu);
                    shuixu.setText("正序");
                }
//                Collections.reverse(showReadList);
                if(adapter != null){
                   adapter.notifyDataSetChanged();
                }
            }
        });
        shuixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paixuState == 0){
                    paixuState =1;
                }else if(paixuState == 1){
                    paixuState =0;
                }
                if(paixuState == 0){
                    paixu.setImageResource(R.mipmap.daoxu);
                    shuixu.setText("倒序");
                }else if(paixuState == 1){
                    paixu.setImageResource(R.mipmap.zhengxu);
                    shuixu.setText("正序");
                }
                Collections.reverse(showReadList);
                if(adapter != null){
                   adapter.notifyDataSetChanged();
                }
            }
        });
        teligId = getIntent().getStringExtra("teligId");
        isRess = getIntent().getStringExtra("isRess");
        unreadcount =getIntent().getIntExtra("unreadcount", 0);
        descrip = getIntent().getStringExtra("descrip");
        progress.setVisibility(View.VISIBLE);
        listView.setPullDownToRefreshable(true);
        listView.setPullUpToRefreshable(false);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                journalpubdate = "";
                listView.setPullUpToRefreshable(false);
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(FreeTryRead.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    isClear = true;
                    getjson();
                }

            }
        });
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(FreeTryRead.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if(showReadList != null && showReadList.size() > 0){
                        ShowRead item = showReadList.get(showReadList.size()-1);
                        if(item.telig_journal_pubdate3 != null && !item.telig_journal_pubdate3.equals("")){
                            journalpubdate = item.telig_journal_pubdate3;
                        }else if(item.telig_journal_pubdate2 != null && !item.telig_journal_pubdate2.equals("")){
                            journalpubdate = item.telig_journal_pubdate2;
                        }else if(item.telig_journal_pubdate1 != null && !item.telig_journal_pubdate1.equals("")){
                            journalpubdate = item.telig_journal_pubdate1;
                        }
                    }
                    getjson();
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);

                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
                    if(h  > 770){
                        if(isRess.equals("1")){
                            biaoti.setText(data.data.telig_title);
                        }else{
                            biaoti.setText(data.data.telig_title);
                        }
                    }else {
                        if(isRess.equals("1")){
                            biaoti.setText(data.data.telig_title);
                        }else{
                            biaoti.setText(data.data.telig_title);
                        }
                    }

                }
            }

            private int getScrollY() {
                try {
                    int height = 0;
                    for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                        ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                        height += itemRecod.height;
                    }
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                    if (null == itemRecod) {
                        itemRecod = new ItemRecod();
                    }
                    return height - itemRecod.top;
                }catch (Exception e){
                    return 770;
                }

//                    return 0;
            }
            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(FreeTryRead.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getjson();
        }
    }
    class FreeTryReadAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showReadList.size();
        }

        @Override
        public Object getItem(int position) {
//            if(paixuState == 0){
                return showReadList.get(position);
//            }else{
//                return temp.get(position);
//            }


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
                convertView = View.inflate(FreeTryRead.this, R.layout.freetryreadadapter, null);
//                holdView.item1 = (RelativeLayout)convertView.findViewById(R.id.item1);
//                holdView.iconitem1 = (ImageView)convertView.findViewById(R.id.iconitem1);
//                holdView.datatime1 = (TextView)convertView.findViewById(R.id.datatime1);
//                holdView.description1 = (TextView)convertView.findViewById(R.id.description1);
//                holdView.item2 = (RelativeLayout)convertView.findViewById(R.id.item2);
//                holdView.iconitem2 = (ImageView)convertView.findViewById(R.id.iconitem2);
//                holdView.datatime2 = (TextView)convertView.findViewById(R.id.datatime2);
//                holdView.description2 = (TextView)convertView.findViewById(R.id.description2);
//                holdView.item3 = (RelativeLayout)convertView.findViewById(R.id.item3);
//                holdView.iconitem3 = (ImageView)convertView.findViewById(R.id.iconitem3);
//                holdView.datatime3 = (TextView)convertView.findViewById(R.id.datatime3);
//                holdView.description3 = (TextView)convertView.findViewById(R.id.description3);
                  holdView.item = (RelativeLayout)convertView.findViewById(R.id.item);
                  holdView.iconitem = (ImageView)convertView.findViewById(R.id.iconitem);
                  holdView.description = (TextView)convertView.findViewById(R.id.description);
                  holdView.datafrom = (TextView)convertView.findViewById(R.id.datafrom);
                  holdView.datatime = (TextView)convertView.findViewById(R.id.datatime);

                  convertView.setTag(holdView);

            }else{
                holdView = (HoldView)convertView.getTag();
            }
//            holdView.item1.setVisibility(View.INVISIBLE);
//            holdView.item2.setVisibility(View.INVISIBLE);
//            holdView.item3.setVisibility(View.INVISIBLE);
//            ShowRead item =null;
//            if(paixuState == 0){
//                item  = showReadList.get(position);
//            }else{
//                item = temp.get(position);
//            }
            final ShowRead item1 = showReadList.get(position);


            if(item1.telig_journal_cover != null && !item1.telig_journal_cover.equals("")){
                holdView.item.setVisibility(View.VISIBLE);
                if (item1.telig_journal_cover != null && !item1.telig_journal_cover.equals("")) {
                    ImageLoader.getInstance().displayImage(item1.telig_journal_cover
                            , holdView.iconitem, options);

                } else {
                    holdView.iconitem.setBackgroundResource(R.mipmap.information_placeholder);
                }
                holdView.datatime.setText(TimeUtils.getTimes(item1.telig_journal_pubdate));
                holdView.description.setText(item1.telig_journal_title);
            }

            holdView.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FreeTryRead.this,LookQingBaoActivity.class);
                    intent.putExtra("teligjournalid",item1.telig_journal_id);
                    startActivity(intent);
                }
            });
//            if(item.telig_journal_cover1 != null && !item.telig_journal_cover1.equals("")){
//                holdView.item1.setVisibility(View.VISIBLE);
//                if (item.telig_journal_cover1 != null && !item.telig_journal_cover1.equals("")) {
//                    ImageLoader.getInstance().displayImage(item.telig_journal_cover1
//                            , holdView.iconitem1, options);
//
//                } else {
//                    holdView.iconitem1.setBackgroundResource(R.mipmap.information_placeholder);
//                }
//                holdView.datatime1.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.telig_journal_pubdate1)*1000), item.telig_journal_pubdate1)+"更新");
//                holdView.description1.setText(item.telig_journal_title1);
//            }
//
//            if(item.telig_journal_cover2 != null &&!item.telig_journal_cover2.equals("")){
//                holdView.item2.setVisibility(View.VISIBLE);
//                NetUtils.NetType type = NetUtils.getNetType();
//                if (type == NetUtils.NetType.NET_WIFI && item.telig_journal_cover2 != null && !item.telig_journal_cover2.equals("")) {
//                    ImageLoader.getInstance().displayImage(item.telig_journal_cover2
//                            , holdView.iconitem2, options);
//
//                } else {
//                    holdView.iconitem2.setBackgroundResource(R.mipmap.information_placeholder);
//                }
//                holdView.datatime2.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.telig_journal_pubdate2)*1000), item.telig_journal_pubdate2)+"更新");
//                holdView.description2.setText(item.telig_journal_title2);
//            }
//
//            if(item.telig_journal_cover3 != null && !item.telig_journal_cover3.equals("")){
//                holdView.item3.setVisibility(View.VISIBLE);
//                NetUtils.NetType type = NetUtils.getNetType();
//                if (type == NetUtils.NetType.NET_WIFI && item.telig_journal_cover3 != null && !item.telig_journal_cover3.equals("")) {
//                    ImageLoader.getInstance().displayImage(item.telig_journal_cover3
//                            , holdView.iconitem3, options);
//
//                } else {
//                    holdView.iconitem3.setBackgroundResource(R.mipmap.information_placeholder);
//                }
//                holdView.datatime3.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.telig_journal_pubdate3)*1000), item.telig_journal_pubdate3)+"更新");
//                holdView.description3.setText(item.telig_journal_title3);
//            }

            String read_ids =PrefUtils.getString(FreeTryRead.this, item1.telig_journal_id, "");
            if(read_ids.equals("")){
                holdView.description.setTextColor(Color.parseColor("#3e3e3e"));
            }else{
                holdView.description.setTextColor(Color.parseColor("#777777"));
            }

//            read_ids =PrefUtils.getString(FreeTryRead.this, item.telig_journal_id2, "");
//            if(read_ids.equals("")){
//                holdView.description2.setTextColor(Color.parseColor("#181818"));
//            }else{
//                holdView.description2.setTextColor(Color.parseColor("#777777"));
//            }
//
//            read_ids =PrefUtils.getString(FreeTryRead.this, item.telig_journal_id1, "");
//            if(read_ids.equals("")){
//                holdView.description1.setTextColor(Color.parseColor("#181818"));
//            }else{
//                holdView.description1.setTextColor(Color.parseColor("#777777"));
//            }

//            holdView.item1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  Intent intent = new Intent(FreeTryRead.this,LookQingBaoActivity.class);
//                  intent.putExtra("teligjournalid",temp.telig_journal_id1);
//                  startActivity(intent);
//                }
//            });
//            holdView.item2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(FreeTryRead.this,LookQingBaoActivity.class);
//                    intent.putExtra("teligjournalid",temp.telig_journal_id2);
//                    startActivity(intent);
//                }
//            });
//            holdView.item3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(FreeTryRead.this,LookQingBaoActivity.class);
//                    intent.putExtra("teligjournalid",temp.telig_journal_id3);
//                    startActivity(intent);
//                }
//            });

            return convertView;
        }
        class HoldView{
//            public RelativeLayout item1,item2,item3;
//            public ImageView iconitem1,iconitem2,iconitem3;
//            public TextView datatime1,datatime2,datatime3;
//            public TextView description1,description2,description3;
            public RelativeLayout item;
            public ImageView iconitem;
            public TextView description;
            public TextView datafrom;
            public TextView datatime;
        }
    }

    private void getjson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sort.add("timestamp"+timestamp);
                sort.add("journalpubdate"+journalpubdate);
                sort.add("mid"+mid);
                sort.add("pageSize"+"100");
                sort.add("teligId"+teligId);
                sign = KeySort.keyScort(sort);
                String url="http://"+ MyApplication.ip+"/api/tellig/telig_journal_list.php?"+"teligId="+teligId+"&pageSize=100"+"&mid="+mid+"&journalpubdate="+journalpubdate+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign="+sign;
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                   Message msg = Message.obtain();
                   msg.what = 1;
                   handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                progress.setVisibility(View.GONE);
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
              Gson gson = new Gson();
            try {
                data = gson.fromJson(json, FreeTryReadEntity.class);
                if(isClear){
                    isClear = false;
                    showReadList.clear();
                }
                if(data != null){
                    if(data.code.equals("1")){
                        biaoti.setText(data.data.telig_title);
                        ImageLoader.getInstance().displayImage(data.data.telig_cover
                                , icon, options);
                        title.setText(data.data.telig_title);
                        description.setText(descrip);
                        if(paixuState == 0){
                            paixu.setImageResource(R.mipmap.daoxu);
                            shuixu.setText("倒序");
                        }else if(paixuState == 1){
                            paixu.setImageResource(R.mipmap.zhengxu);
                            shuixu.setText("正序");
                        }

                        if(data.data.journallist != null && data.data.journallist.size() > 0){
                            ShowRead newitem = null;
                            boolean addstate =false;
                            for(int i=0; i<data.data.journallist.size();i++){
//                                int mod = i % 3 ;

                                ReadData item;
//                                if(mod == 0){
//                                    addstate = false;
                                    newitem = new ShowRead();
                                    item = data.data.journallist.get(i);
                                    newitem.telig_journal_cover = item.telig_journal_cover;
                                    newitem.telig_journal_pubdate = item.telig_journal_pubdate;
                                    newitem.telig_journal_title = item.telig_journal_title;
                                    newitem.telig_journal_id = item.telig_journal_id;

//                                }else if(mod == 1){
//                                    item = data.data.journallist.get(i);
//                                    newitem.telig_journal_cover2 = item.telig_journal_cover;
//                                    newitem.telig_journal_pubdate2 = item.telig_journal_pubdate;
//                                    newitem.telig_journal_title2 = item.telig_journal_title;
//                                    newitem.telig_journal_id2 = item.telig_journal_id;

//                                }else if(mod == 2){
//                                    item = data.data.journallist.get(i);
//                                    newitem.telig_journal_cover3 = item.telig_journal_cover;
//                                    newitem.telig_journal_pubdate3 = item.telig_journal_pubdate;
//                                    newitem.telig_journal_title3 = item.telig_journal_title;
//                                    newitem.telig_journal_id3 = item.telig_journal_id;
//                                    addstate = true;
//                                    showReadList.add(newitem);
//
//                                }
//                                if(!addstate && (i == data.data.journallist.size()-1)){
                                    showReadList.add(newitem);
//                                }

                            }

//                            Collections.reverse(data.data.journallist);
//                            addstate =false;
//                            for(int i=0; i<data.data.journallist.size();i++){
//                                int mod = i % 3 ;
//
//                                ReadData item;
//                                if(mod == 0){
//                                    addstate = false;
//                                    newitem = new ShowRead();
//                                    item = data.data.journallist.get(i);
//                                    newitem.telig_journal_cover1 = item.telig_journal_cover;
//                                    newitem.telig_journal_pubdate1 = item.telig_journal_pubdate;
//                                    newitem.telig_journal_title1 = item.telig_journal_title;
//                                    newitem.telig_journal_id1 = item.telig_journal_id;
//
//                                }else if(mod == 1){
//                                    item = data.data.journallist.get(i);
//                                    newitem.telig_journal_cover2 = item.telig_journal_cover;
//                                    newitem.telig_journal_pubdate2 = item.telig_journal_pubdate;
//                                    newitem.telig_journal_title2 = item.telig_journal_title;
//                                    newitem.telig_journal_id2 = item.telig_journal_id;
//
//                                }else if(mod == 2){
//                                    item = data.data.journallist.get(i);
//                                    newitem.telig_journal_cover3 = item.telig_journal_cover;
//                                    newitem.telig_journal_pubdate3 = item.telig_journal_pubdate;
//                                    newitem.telig_journal_title3 = item.telig_journal_title;
//                                    newitem.telig_journal_id3 = item.telig_journal_id;
//                                    addstate = true;
//                                    temp.add(newitem);
//
//                                }
//                                if(!addstate && (i == data.data.journallist.size()-1)){
//                                    temp.add(newitem);
//                                }
//
//                            }


                            if(data.data.journallist.size()<20){
                                listView.setPullUpToRefreshable(false);
                            }
                        }
                        if(adapter == null){
                            if(isRess.equals("1")){
                                biaoti.setText(data.data.telig_title);
                                listView.addHeaderView(view);
                            }else{
                                biaoti.setText(data.data.telig_title);
                            }
                            adapter = new FreeTryReadAdapter();
                            listView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }catch (Exception e){

            }

        }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }


}
