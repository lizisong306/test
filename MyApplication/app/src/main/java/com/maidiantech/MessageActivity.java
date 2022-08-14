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
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.MessageData;
import entity.MessageEntity;
import entity.QingBaoDeilEntity;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/11/23.
 */

public class MessageActivity extends AutoLayoutActivity {
    ImageView unit_back,nocontent;
    RefreshListView listview;
    MessageAdapter adapter = new MessageAdapter();
    ProgressBar progress;
    String sortTime="",json;
    MessageEntity data;
    boolean isFinish = false;
    private DisplayImageOptions options;
    List<MessageData> messageDataList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageactivity);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        unit_back = (ImageView)findViewById(R.id.unit_back);
        unit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        nocontent = (ImageView)findViewById(R.id.nocontent);
        progress.setVisibility(View.VISIBLE);
        listview.setPullUpToRefreshable(true);
        listview.setPullDownToRefreshable(true);
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
              if(messageDataList.size() >0){
                  MessageData item = messageDataList.get(messageDataList.size()-1);
                  sortTime = item.sortTime;
                  getJson();
              }
            }
        });
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                 isFinish = true;
                 listview.setPullDownToRefreshable(true);
                 sortTime="";
                 getJson();
            }
        });
        listview.setAdapter(adapter);
        getJson();

    }
    class MessageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return messageDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return messageDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView hold;
            if(convertView == null){
                hold = new HoldView();
                convertView = View.inflate(MessageActivity.this, R.layout.messageadapter, null);
                hold.huodonglay = (LinearLayout)convertView.findViewById(R.id.huodonglay);
                hold.houdong_time = (TextView)convertView.findViewById(R.id.houdong_time);
                hold.houdong_title = (TextView)convertView.findViewById(R.id.houdong_title);
                hold.huodonglay_dian = (ImageView)convertView.findViewById(R.id.huodonglay_dian);
                hold.houdong_img = (ImageView)convertView.findViewById(R.id.houdong_img);
                hold.messagelay_dian = (ImageView)convertView.findViewById(R.id.messagelay_dian);
                hold.messagelay = (LinearLayout)convertView.findViewById(R.id.messagelay);
                hold.messagelay_time = (TextView)convertView.findViewById(R.id.messagelay_time);
                hold.messagelay_title = (TextView)convertView.findViewById(R.id.messagelay_title);
                hold.messagelay_descripse = (TextView)convertView.findViewById(R.id.messagelay_descripse);
                convertView.setTag(hold);
            }else {
                hold = (HoldView) convertView.getTag();
            }
            final MessageData item = messageDataList.get(position);
            hold.huodonglay.setVisibility(View.GONE);
            hold.messagelay.setVisibility(View.GONE);

            if(item.type.equals("html")){
                hold.huodonglay.setVisibility(View.VISIBLE);
                hold.messagelay.setVisibility(View.GONE);
                hold.houdong_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.senddate)*1000), item.senddate));
                hold.houdong_title.setText(item.title);
                if(item.is_click.equals("yes")){
                    hold.huodonglay_dian.setVisibility(View.VISIBLE);
                }else {
                    hold.huodonglay_dian.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(item.img
                        , hold.houdong_img, options);
                hold.huodonglay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MessageActivity.this, ActiveActivity.class);
                        intent.putExtra("url", item.link);
                        startActivity(intent);
                    }
                });

            }else if(item.type.equals("intelligence")){
                hold.huodonglay.setVisibility(View.VISIBLE);
                hold.messagelay.setVisibility(View.GONE);
                hold.houdong_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.senddate)*1000), item.senddate));
                hold.houdong_title.setText(item.title);
                if(item.is_click.equals("yes")){
                    hold.huodonglay_dian.setVisibility(View.VISIBLE);
                }else {
                    hold.huodonglay_dian.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(item.img
                        , hold.houdong_img, options);
                hold.huodonglay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(MessageActivity.this, QingBaoActivity.class);
//                        startActivity(intent);
                        progress.setVisibility(View.VISIBLE);
                        getQingBaoJson(item.telig_id,item);
                    }
                });
            }else if(item.type.equals("infor")){
                hold.huodonglay.setVisibility(View.GONE);
                hold.messagelay.setVisibility(View.VISIBLE);
                hold.messagelay_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.senddate)*1000), item.senddate));
                hold.messagelay_title.setText(item.title);
                hold.messagelay_descripse.setText(item.content);
                if(item.is_click.equals("yes")){
                    hold.messagelay_dian.setVisibility(View.VISIBLE);
                }else {
                    hold.messagelay_dian.setVisibility(View.GONE);
                }
            }else if(item.type.equals("expertAppointment")){
                hold.huodonglay.setVisibility(View.VISIBLE);
                hold.messagelay.setVisibility(View.GONE);
                hold.houdong_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.senddate)*1000), item.senddate));
                hold.houdong_title.setText(item.title);
                if(item.is_click.equals("yes")){
                    hold.huodonglay_dian.setVisibility(View.VISIBLE);
                }else {
                    hold.huodonglay_dian.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(item.img
                        , hold.houdong_img, options);
                hold.huodonglay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MessageActivity.this, AppointmentSpecialist.class);
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("boutiqueProject")){
                hold.huodonglay.setVisibility(View.VISIBLE);
                hold.messagelay.setVisibility(View.GONE);
                hold.houdong_time.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.senddate)*1000), item.senddate));
                hold.houdong_title.setText(item.title);
                if(item.is_click.equals("yes")){
                    hold.huodonglay_dian.setVisibility(View.VISIBLE);
                }else {
                    hold.huodonglay_dian.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(item.img
                        , hold.houdong_img, options);
                hold.huodonglay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MessageActivity.this, XiangMuDuiJieActivity.class);
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }
        class HoldView{
            public LinearLayout huodonglay;
            public TextView houdong_time,houdong_title;
            public ImageView huodonglay_dian,houdong_img;

            public LinearLayout messagelay;
            public TextView messagelay_time,messagelay_title,messagelay_descripse;
            public ImageView messagelay_dian;
        }
    }
    private void getJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("pageSize"+"20");
                sort.add("timestamp"+timestamp);
                sort.add("mid"+mid);
                sort.add("LastModifiedTime"+sortTime);
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sign = KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/information_push.php?pageSize=20"+"&mid="+mid+"&LastModifiedTime="+sortTime+"&sign="+sign+"&timestamp="+timestamp+MyApplication.accessid+"&version="+MyApplication.version;
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
            try {
                if(msg.what == 1){
                    progress.setVisibility(View.GONE);
                    listview.setPullDownToRefreshFinish();
                    listview.setPullUpToRefreshFinish();
                    Gson gson = new Gson();
                    data = gson.fromJson(json,MessageEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(isFinish){
                                isFinish = false;
                                messageDataList.clear();
                            }
                            if(data.data != null && data.data.size() >0){
                                for(int i=0; i<data.data.size();i++){
                                    MessageData item = data.data.get(i);
                                    messageDataList.add(item);
                                }
                                adapter.notifyDataSetChanged();
                                if(messageDataList.size() == 0){
                                    nocontent.setVisibility(View.VISIBLE);
                                }else{
                                    nocontent.setVisibility(View.GONE);
                                }
                            }
                        }
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }else if(msg.what == 2){
                    MessageData item = (MessageData)msg.obj;
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    QingBaoDeilEntity data=gson.fromJson(json, QingBaoDeilEntity.class);
                    if(data.data.isRess.equals("1")){
                        Intent intent = new Intent(MessageActivity.this,LookQingBaoActivity.class);
                        intent.putExtra("teligjournalid",item.telig_journal_id);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MessageActivity.this, QingBaoDeilActivity.class);
                        intent.putExtra("teligId",item.telig_id);
                        intent.putExtra("telig_price",item.telig_price);
                        intent.putExtra("telig_unit",item.telig_unit);
                        intent.putExtra("telig_type",item.telig_type);
                        startActivity(intent);
                    }
                }
            }catch (Exception e){

            }
        }
    };

    private void getQingBaoJson(final String teligId,final  MessageData item){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String mid= SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sort.add("timestamp"+timestamp);
                sort.add("mid"+mid);
                sort.add("teligId"+teligId);
                sign = KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/tellig/telig_list_detail.php?"+"mid="+mid+"&teligId="+teligId+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign"+sign;
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what=2;
                    msg.obj = item;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }
}
