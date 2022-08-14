package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.DuiJieData;
import entity.XiangMuDuiJieEntity;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/9/27.
 */

public class XiangMuDuiJieActivity extends AutoLayoutActivity {
    ImageView back;
    RefreshListView listview;
    ProgressBar progressBar;
    XiangMuDuiJieEntity data;
    String ret;
    List<DuiJieData> listData = new ArrayList<>();
    XiangMuAdapter adapter;
    String channelid,flag="";
    DisplayImageOptions options;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangmuduijieactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        channelid = getIntent().getStringExtra("channelid");
        flag = getIntent().getStringExtra("flag");
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.information_back);
        listview = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getJson();
    }

    class XiangMuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
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
                convertView = View.inflate(XiangMuDuiJieActivity.this,R.layout.xiangmuduijieadapter,null);
                holdView.title_bg = (RelativeLayout)convertView.findViewById(R.id.title_bg);
                holdView.title = (TextView)convertView.findViewById(R.id.title);
                holdView.count = (TextView)convertView.findViewById(R.id.count);
                holdView.bg = (ImageView)convertView.findViewById(R.id.bg);
                convertView.setTag(holdView);
            }else {
                holdView = (HoldView) convertView.getTag();
            }
            DuiJieData item = listData.get(position);
            holdView.count.setText(item.count+"个项目");
            holdView.title.setText(item.bagName);


            ImageLoader.getInstance().displayImage(item.picUrl
                        , holdView.bg, options);

            return convertView;
        }
        class HoldView{
            public RelativeLayout title_bg;
            public TextView title;
            public TextView count;
            public ImageView bg;
        }
    }

    private void getJson(){
        String url = "http://"+ MyApplication.ip+"/api/getProjectDocking.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "cate");

        if(channelid != null){
            map.put("channelid", channelid);
        }

        if(flag != null){
          map.put("flag", flag);
        }

        networkCom.getJson(url,map,handler,1,0);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    MyApplication.setAccessid();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("version"+MyApplication.version);
//                    sort.add("accessid"+MyApplication.deviceid);
//                    sort.add("timestamp"+timestamp);
//                    sort.add("action"+"cate");
//                    sign = KeySort.keyScort(sort);
//                    String url = "http://"+ MyApplication.ip+"/api/getProjectDocking.php?action=cate"+"&version="+MyApplication.version+MyApplication.deviceid+"&timestamp="+timestamp+"&sign="+sign;
//                    ret = OkHttpUtils.loaudstringfromurl(url);
//                    if(ret != null){
//                        Message msg = Message.obtain();
//                        msg.what =1;
//                        handler.sendMessage(msg);
//                    }
//                }catch (Exception e){
//
//                }
//
//            }
//        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progressBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    ret = (String)msg.obj;
                    data = gson.fromJson(ret, XiangMuDuiJieEntity.class);
                    if(data != null && data.code==1){
                        if(data.data != null && data.data.size() >0 ){
                            for(int i=0; i<data.data.size();i++){
                                DuiJieData item = data.data.get(i);
                                listData.add(item);
                            }
                        }
                        if(adapter == null){
                            adapter = new XiangMuAdapter();
                            listview.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DuiJieData item = listData.get(position-1);
                            Intent intent = new Intent(XiangMuDuiJieActivity.this, XiangMuListActivity.class);
                            intent.putExtra("id", item.id);
                            startActivity(intent);
                        }
                    });

                }
            }catch (Exception e){

            }

        }
    };


}
