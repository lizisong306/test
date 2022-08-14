package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import Util.OkHttpUtils;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import Util.NetUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.InstituteData;
import entity.ShowRead;
import entity.ShowUnitedStatesData;
import entity.UnitedData;
import entity.UnitedStatesEntity;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2018/1/10.
 */

public class UnitedStatesActivity extends AutoLayoutActivity {
    ImageView back,search;
    LinearLayout title_lay;
    TextView titlexuanting;
    RefreshListView listView;
    String json;
    UnitedStatesEntity data;
    List<ShowUnitedStatesData> showUnitedStatesDataList = new ArrayList<>();
    UnitedStatesAdapter adapter;
    private DisplayImageOptions options;
    ProgressBar progressBar;
    boolean scrollFlag =false;
    int lastVisibleItemPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unitedstatesactivity);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.shezhi_backs);
        search = (ImageView)findViewById(R.id.search);
        title_lay = (LinearLayout)findViewById(R.id.title_lay);
        titlexuanting = (TextView)findViewById(R.id.titlexuanting);
        listView = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnitedStatesActivity.this, NewSearchHistory.class);
                intent.putExtra("typeid", "8");
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

//                    int index =  listView.getFirstVisiblePosition();
                    if(showUnitedStatesDataList.size() >0){
                        ShowUnitedStatesData item = null;
                            if(firstVisibleItem > 0){
                                item = showUnitedStatesDataList.get(firstVisibleItem-1);
                                titlexuanting.setText(item.institute_name);
                            }else{
                                item = showUnitedStatesDataList.get(0);
                                titlexuanting.setText(item.institute_name);
                            }
                        }

                }catch (Exception e){

                }
            }
        });
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            getJson();
        }

    }
    public void getJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url ="http://"+MyApplication.ip+"/api/Institutions.php?";
                    Log.d("lizisong","url:"+url);
                    json = OkHttpUtils.loaudstringfromurl(url);
                    if(json != null){
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){

                }

            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                progressBar.setVisibility(View.GONE);
                try {
                    Gson gson = new Gson();
                    data= gson.fromJson(json, UnitedStatesEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            List<UnitedData> dataList = data.data;
                            if(dataList != null && dataList.size() >0){
                                for(int i=0; i<dataList.size(); i++){
                                    boolean addstate =false;
                                    String titlename = "";
                                    UnitedData item = dataList.get(i);
                                    ShowUnitedStatesData pos = new ShowUnitedStatesData();
                                    pos.institute_name = item.institute_name;
                                    pos.isShowtitle = false;
                                    pos.type = 0;
                                    titlename = item.institute_name;
                                    showUnitedStatesDataList.add(pos);
                                    ShowUnitedStatesData temp =null;
                                    try {
                                        for(int j=0;j<item.list.size();j++){
                                            int k = j % 3 ;
                                            if(k == 0){
                                                addstate =false;
                                                temp = new ShowUnitedStatesData();
                                                InstituteData instituteData =item.list.get(j);
                                                temp.type = 1;
                                                temp.institute_name = titlename;
                                                temp.title1 = instituteData.title;
                                                temp.aid1 = instituteData.aid;
                                                temp.logoimg1 = instituteData.logoimg;
                                            }else if(k == 1){
                                                InstituteData instituteData =item.list.get(j);
                                                temp.type = 1;
                                                temp.title2 = instituteData.title;
                                                temp.aid2 = instituteData.aid;
                                                temp.institute_name = titlename;
                                                temp.logoimg2 = instituteData.logoimg;
                                            }else if(k ==2){
                                                addstate =true;
                                                InstituteData instituteData =item.list.get(j);
                                                temp.type = 1;
                                                temp.title3 = instituteData.title;
                                                temp.institute_name = titlename;
                                                temp.aid3 = instituteData.aid;
                                                temp.logoimg3 = instituteData.logoimg;
                                                showUnitedStatesDataList.add(temp);
                                            }
                                            if(!addstate && (j == item.list.size()-1)){
                                                showUnitedStatesDataList.add(temp);
                                            }
                                        }
                                    }catch (Exception e){

                                    }


                                }
                            }
                            adapter = new UnitedStatesAdapter();
                            listView.setAdapter(adapter);
                        }
                    }
                }catch (Exception e){

                }

            }

        }
    };
    class UnitedStatesAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showUnitedStatesDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return showUnitedStatesDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdview;
            if(convertView == null){
                holdview = new HoldView();
                convertView = View.inflate(UnitedStatesActivity.this, R.layout.unitedstatesadapter, null);
                holdview.titlelayouy = (LinearLayout)convertView.findViewById(R.id.titlelayouy);
                holdview.title = (TextView)convertView.findViewById(R.id.title);
                holdview.jigoulayout = (LinearLayout)convertView.findViewById(R.id.jigoulayout);
                holdview.jigou1 = (RelativeLayout)convertView.findViewById(R.id.jigou1);
                holdview.jigou2 = (RelativeLayout)convertView.findViewById(R.id.jigou2);
                holdview.jigou3 = (RelativeLayout)convertView.findViewById(R.id.jigou3);
                holdview.img1 = (ImageView) convertView.findViewById(R.id.img1);
                holdview.img2 = (ImageView) convertView.findViewById(R.id.img2);
                holdview.img3 = (ImageView) convertView.findViewById(R.id.img3);
                holdview.tex1 = (TextView) convertView.findViewById(R.id.tex1);
                holdview.tex2 = (TextView) convertView.findViewById(R.id.tex2);
                holdview.tex3 = (TextView) convertView.findViewById(R.id.tex3);
                convertView.setTag(holdview);

            }else{
                holdview = (HoldView) convertView.getTag();
            }

            final ShowUnitedStatesData item = showUnitedStatesDataList.get(position);
            holdview.titlelayouy.setVisibility(View.GONE);
            holdview.jigoulayout.setVisibility(View.GONE);
            if(item.type == 0){
                holdview.titlelayouy.setVisibility(View.VISIBLE);
                holdview.jigoulayout.setVisibility(View.GONE);
                holdview.title.setText(item.institute_name);
                if(item.isShowtitle){
                    holdview.titlelayouy.setVisibility(View.GONE);
                }
            }else if(item.type ==1){
                holdview.jigoulayout.setVisibility(View.VISIBLE);
                holdview.titlelayouy.setVisibility(View.GONE);
                holdview.jigou1.setVisibility(View.INVISIBLE);
                holdview.jigou2.setVisibility(View.INVISIBLE);
                holdview.jigou3.setVisibility(View.INVISIBLE);
                if(item.aid1 != null && !item.aid1.equals("")){
                    holdview.jigou1.setVisibility(View.VISIBLE);
//                    holdview.img1.setImageResource(R.mipmap.keyanjigouloading);
                    ImageLoader.getInstance().displayImage(item.logoimg1
                            , holdview.img1, options);

                    holdview.tex1.setText(item.title1);
                }

                if(item.aid2 != null && !item.aid2.equals("")){
                    holdview.jigou2.setVisibility(View.VISIBLE);
//                    holdview.img2.setImageResource(R.mipmap.keyanjigouloading);
                    ImageLoader.getInstance().displayImage(item.logoimg2
                            , holdview.img2, options);
                    holdview.tex2.setText(item.title2);
                }
                if(item.aid3 != null && !item.aid3.equals("")){
                    holdview.jigou3.setVisibility(View.VISIBLE);
//                    holdview.img3.setImageResource(R.mipmap.keyanjigouloading);
                    ImageLoader.getInstance().displayImage(item.logoimg3
                            , holdview.img3, options);
                    holdview.tex3.setText(item.title3);
                }

            }
            holdview.jigou1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UnitedStatesActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.aid1 );
                    startActivity(intent);
                }
            });
            holdview.jigou2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UnitedStatesActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.aid2);
                    startActivity(intent);
                }
            });
            holdview.jigou3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UnitedStatesActivity.this, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.aid3);
                    startActivity(intent);
                }
            });
            if(position == 0){
                holdview.titlelayouy.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HoldView{
            public LinearLayout titlelayouy;
            public TextView title;

            public LinearLayout jigoulayout;
            public RelativeLayout jigou1,jigou2,jigou3;
            public ImageView img1,img2,img3;
            public TextView tex1,tex2,tex3;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("科研机构");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("科研机构");

    }
}
