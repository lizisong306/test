package com.maidiantech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.Ret;
import entity.SelectType;
import entity.SonCate;
import view.RefreshListView;

/**
 * Created by Administrator on 2019/9/24.
 */

public class XiuGaiListActivity extends AutoLayoutActivity {
    ImageView shezhi_backs;
    TextView title;
    RefreshListView listview;
    Button info_submit;
    String name,id;
    String evalue="",typeid="",evaluename="";
    List<SelectType> selectList;
    List<SonCate> sonCateList;
    AdapterSonCate adapterSonCate;
    AdapterSelectType adapterSelectType;
    Ret data;
    PulseCondtion mPulseCondtion;
    ArrayList<PulseData> pulseDatas;
    String evaluetop="";
    boolean state = false;
    PulseData positem;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.xiugailistactivity);
        mPulseCondtion = PulseCondtion.getInstance(this);
        pulseDatas = mPulseCondtion.get();
        evaluetop = getIntent().getStringExtra("evaluetop");
        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);
        title =(TextView)findViewById(R.id.title);
        listview = (RefreshListView)findViewById(R.id.listview);
        info_submit = (Button)findViewById(R.id.info_submit);
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        title.setText(name);
        if(name.equals("细分领域")){
            sonCateList = (List<SonCate>)getIntent().getSerializableExtra("list");
            adapterSonCate = new AdapterSonCate();
            listview.setAdapter(adapterSonCate);
        }else if(name.equals("科技资源")){
            selectList  = (List<SelectType>)getIntent().getSerializableExtra("list");
            adapterSelectType = new AdapterSelectType();
            listview.setAdapter(adapterSelectType);
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(name.equals("细分领域")){
                    int count1 =0;
                    for (int i=0; i<sonCateList.size();i++){
                        SonCate item = sonCateList.get(i);
                        if(item.isChoice.equals("1")){
                            count1++;
                        }
                    }
                    SonCate item = sonCateList.get(position-1);
                    if(item.isChoice.equals("1")){
                        if(count1>1){
                          item.isChoice="0";
                        }
                    }else if(item.isChoice.equals("0")){
                        item.isChoice="1";
                    }
                    if(adapterSonCate != null){
                        adapterSonCate.notifyDataSetChanged();
                    }
                }else if(name.equals("科技资源")){
                    int count2 =0;
                    for (int i=0; i<selectList.size();i++){
                        SelectType item = selectList.get(i);
                        if(item.isChoice.equals("1")){
                            count2++;
                        }
                    }

                    SelectType item = selectList.get(position-1);
                    if(item.isChoice.equals("1")){
                        if(count2>1){
                            item.isChoice="0";
                        }
                    }else if(item.isChoice.equals("0")){
                        item.isChoice="1";
                    }
                    if(adapterSelectType != null){
                        adapterSelectType.notifyDataSetChanged();
                    }
                }
            }
        });
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        info_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJson();
            }
        });
        if(pulseDatas != null){
            for (int i=0; i<pulseDatas.size();i++){
                PulseData item = pulseDatas.get(i);
                if(item.evaluetop.equals(evaluetop)){
                    state = true;
                    positem = item;
                    break;
                }
            }
        }

    }
    class AdapterSonCate extends BaseAdapter{

        @Override
        public int getCount() {
             return sonCateList.size();
        }

        @Override
        public Object getItem(int position) {
             return sonCateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView=null;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(XiuGaiListActivity.this, R.layout.xiugailistadapter, null);
                holdView.icon = convertView.findViewById(R.id.icon);
                holdView.text = convertView.findViewById(R.id.text);
                holdView.item = convertView.findViewById(R.id.item);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            SonCate item = sonCateList.get(position);
            holdView.text.setText(item.ename);
            if(item.isChoice.equals("1")){
                holdView.icon.setVisibility(View.VISIBLE);
                holdView.icon.setImageResource(R.mipmap.selectsourceseectedmg);
            }else{
                holdView.icon.setVisibility(View.VISIBLE);
                holdView.icon.setImageResource(R.mipmap.selectsourceunse);
            }

            return convertView;
        }
        class HoldView{
            ImageView icon;
            TextView text;
            AutoRelativeLayout item;
        }
    }
    class AdapterSelectType extends BaseAdapter{

        @Override
        public int getCount() {
            return selectList.size();
        }

        @Override
        public Object getItem(int position) {
            return selectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView=null;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(XiuGaiListActivity.this, R.layout.xiugailistadapter, null);
                holdView.icon = convertView.findViewById(R.id.icon);
                holdView.text = convertView.findViewById(R.id.text);
                holdView.item = convertView.findViewById(R.id.item);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            SelectType item = selectList.get(position);
            holdView.text.setText(item.typename);
            if(item.isChoice.equals("1")){
                holdView.icon.setVisibility(View.VISIBLE);
                holdView.icon.setImageResource(R.mipmap.selectsourceseectedmg);
            }else{
                holdView.icon.setVisibility(View.VISIBLE);
                holdView.icon.setImageResource(R.mipmap.selectsourceunse);
            }
            return convertView;
        }
        class HoldView{
            ImageView icon;
            TextView text;
            AutoRelativeLayout item;
        }
    }
    private void getJson(){
        String url = "http://"+ MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","set");
        map.put("id",id);
        evalue="";
        evaluename="";
        typeid="";
        if(name.equals("细分领域")){
            for(int i=0; i<sonCateList.size();i++){
                SonCate item = sonCateList.get(i);
                if(item.isChoice.equals("1")){
                 evalue=evalue+item.evalue+",";
                 evaluename=evaluename+item.ename+",";
                }
            }
           map.put("evalue",evalue);
        }else if(name.equals("科技资源")){
            for(int i=0; i<selectList.size();i++){
                SelectType item = selectList.get(i);
                if(item.isChoice.equals("1")){
                    typeid=typeid+item.typeid+",";
                }
            }
            map.put("typeid",typeid);
        }
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, Ret.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            Toast.makeText(XiuGaiListActivity.this, "修改成功",Toast.LENGTH_SHORT).show();
                            if(name.equals("细分领域")){
                                positem.evalueTitle=evaluename;
                                positem.evalue = evalue;
                                mPulseCondtion.updata(positem);
                            }else if(name.equals("科技资源")){
                                positem.typeid = typeid;
                                mPulseCondtion.updata(positem);
                            }
                            NewResultActivity.isNewClear=true;
                            finish();
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };
}
