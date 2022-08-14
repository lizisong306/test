package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Util.OkHttpUtils;
import adapter.PulsetwoAdapter;
import entity.Category;
import entity.LangyaSimple;
import entity.LevelCate;
import entity.LevelData;
import entity.bmrank;
import entity.industryCode;
import entity.industrydata;
import view.HorizontalListView;
import view.StyleUtils;
import view.StyleUtils1;

import static com.maidiantech.DetailsActivity.toByteArray;

/**
 * Created by 13520 on 2016/11/16.
 */

public class bmplusetwo extends AutoLayoutActivity {
    ImageView bamaiback;
    ListView bamaiinfo;
    HorizontalListView horListView;
    Button bamainexttwo,goback;
    List<industrydata> data;
    List<LangyaSimple> mLangyaDatas;
    List<bmrank> newbalist;
    TextView text_show;
    public static List<LangyaSimple> dexlist;
    List<LangyaSimple> checkList = new ArrayList<>();
    OkHttpUtils Okhttp;
    TextView checkAll;
    String json;
    PulsetwoAdapter pluseadapter;
    public static List<LevelCate> sonCate;
    List<LevelData> datalist;
    List<LangyaSimple> choiceData;
    RelativeLayout bg;
    TextView mNameTxt;
    ImageView mImageView;
    public static  boolean close_state = false;
    HorizontalListViewAdapter choiceDataAdapter;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what==1){
//                    Gson g=new Gson();
//                    LevleCode levleCode = g.fromJson(json, LevleCode.class);
//                    datalist = levleCode.getData();
//                    sonCate = datalist.get(0).getSonCate();
                    pluseadapter=new PulsetwoAdapter(bmplusetwo.this,mLangyaDatas,dexlist);
                    bamaiinfo.setAdapter(pluseadapter);
                }else if(msg.what == 10001){
                    choiceDataAdapter.notifyDataSetChanged();
                    pluseadapter.notifyDataSetChanged();
                    text_show.setText(dexlist.size()+"/"+mLangyaDatas.size());
                }
            }catch (Exception e){}
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluse_two);
        StyleUtils1.initSystemBar(this);

        //设置状态栏半透明的状态
        Intent intent = getIntent();
        newbalist= (List<bmrank>) intent.getSerializableExtra("newbalist");
        bg = (RelativeLayout)findViewById(R.id.title);
        mNameTxt = (TextView)findViewById(R.id.name);
        text_show = (TextView)findViewById(R.id.text_show);
        String type = intent.getStringExtra("title");
        if(type.equals("先进制造")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xianjinzhizao);
            bg.setBackgroundResource(R.mipmap.bg_xianjinzhizao);
            mNameTxt.setText("先进制造");
        }else if(type.equals("电子信息")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_dianzixinx);
            bg.setBackgroundResource(R.mipmap.bg_dianzixinxi);
            mNameTxt.setText("电子信息");
        }else if(type.equals("新材料")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xincailiao);
            bg.setBackgroundResource(R.mipmap.bg_xincailiao);
            mNameTxt.setText("新材料");
        }else if(type.equals("生物技术")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_shegnwujishu);
            bg.setBackgroundResource(R.mipmap.bg_shengwujishu);
            mNameTxt.setText("生物技术");
        }else if(type.equals("节能环保")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_jienenghuanbao);
            bg.setBackgroundResource(R.mipmap.bg_jienenghuanbao);
            mNameTxt.setText("节能环保");
        }else if(type.equals("文化创意")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_wenhuachuangyi);
            bg.setBackgroundResource(R.mipmap.bg_wenhuachuangyi);
            mNameTxt.setText("文化创意");

        }else if(type.equals("化学化工")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_huaxuehuagong);
            bg.setBackgroundResource(R.mipmap.bg_huaxuehuagong);
            mNameTxt.setText("化学化工");
        }else if(type.equals("新能源")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xinnengyuan);
            bg.setBackgroundResource(R.mipmap.bg_xinnengyuan);
            mNameTxt.setText("新能源");
        }else if(type.equals("其他")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_qita);
            bg.setBackgroundResource(R.mipmap.bg_qita);
            mNameTxt.setText("其他");
        }

        //设置状态栏是否沉浸

        mLangyaDatas = new ArrayList<LangyaSimple>();
        dexlist=new ArrayList<>();
        choiceData = new ArrayList<>();
        choiceDataAdapter = new HorizontalListViewAdapter(this, dexlist,handler);

        initView();

        try {
            String  str = new String(toByteArray(getAssets().open("industry.json")));
            Gson g=new Gson();
            industryCode industryCode = g.fromJson(str, entity.industryCode.class);
            data = industryCode.getData();
            initDatas();
            text_show.setText("0"+"/"+mLangyaDatas.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bamaiback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //数据请求
        getleveljson();

        bamaiinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(!dexlist.contains(mLangyaDatas.get(position))){
                     dexlist.add(mLangyaDatas.get(position));
                }else{
                    dexlist.remove(mLangyaDatas.get(position));
                }
                pluseadapter.notifyDataSetChanged();

                text_show.setText(dexlist.size()+"/"+mLangyaDatas.size());
               choiceDataAdapter.notifyDataSetChanged();
            }
        });
        bamainexttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dexlist.size()==0){
                    Toast.makeText(bmplusetwo.this, "请选择领域", Toast.LENGTH_SHORT).show();
                }else{
                    industrydata item = data.get(0);
                    List<Category> list =item.getSonCate();
                    for(int j =0; j<dexlist.size();j++){
                        LangyaSimple value = dexlist.get(j);
                        for(int i=0;i<list.size(); i++){
                            Category category = list.get(i);
                            if(value.getTitle().equals(category.getEname())){
                                value.setEvalue(Integer.parseInt(category.getEvalue()));
                                break;
                            }
                        }
                    }

                    Intent intent =new Intent(bmplusetwo.this,BmpluseThree.class);
                    Bundle b=new Bundle();
                    b.putSerializable("dexlist", (Serializable) dexlist);
                    intent.putExtra("title", mNameTxt.getText());
                    intent.putExtras(b);
                    startActivity(intent);

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(close_state){
            close_state = false;
            finish();
        }
        MobclickAgent.onPageStart("选择行业"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择行业"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void initView() {
        bamaiback = (ImageView) findViewById(R.id.bamai_back);
        bamaiinfo = (ListView) findViewById(R.id.bamai_info);
        bamainexttwo = (Button) findViewById(R.id.bamai_next_two);
        horListView = (HorizontalListView)findViewById(R.id.horlist);
        horListView.setAdapter(choiceDataAdapter);
        checkAll = (TextView)findViewById(R.id.check_all);
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<mLangyaDatas.size();i++){
                    if(!dexlist.contains(mLangyaDatas.get(i))){
                        dexlist.add(mLangyaDatas.get(i));
                    }
                }
                pluseadapter.notifyDataSetChanged();
                text_show.setText(dexlist.size()+"/"+mLangyaDatas.size());
                choiceDataAdapter.notifyDataSetChanged();
            }
        });
        mImageView = (ImageView)findViewById(R.id.img_close);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PulseActivity.posdata = 1;
                finish();
            }
        });
        goback = (Button)findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas() {
         for(int i=0;i<newbalist.size();i++){
             String  type = newbalist.get(i).getName();
        if(type.equals("先进制造")){
            mLangyaDatas.add(new LangyaSimple("1", "1", data.get(4).getSonCate().get(0).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(0).getEvalue()),R.mipmap.process));
            mLangyaDatas.add(new LangyaSimple("2", "1", data.get(4).getSonCate().get(1).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(1).getEvalue()),R.mipmap.marineengineering));
            mLangyaDatas.add(new LangyaSimple("3", "1", data.get(4).getSonCate().get(2).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(2).getEvalue()),R.mipmap.satelliteindustry));
            mLangyaDatas.add(new LangyaSimple("4", "1", data.get(4).getSonCate().get(3).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(3).getEvalue()),R.mipmap.aviation));
            mLangyaDatas.add(new LangyaSimple("5", "1", data.get(4).getSonCate().get(4).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(4).getEvalue()),R.mipmap.ntelligent));
            mLangyaDatas.add(new LangyaSimple("5", "1", data.get(4).getSonCate().get(5).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(5).getEvalue()),R.mipmap.transportation));
            mLangyaDatas.add(new LangyaSimple("5", "1", data.get(4).getSonCate().get(6).getEname(),  "先进制造",Integer.parseInt(data.get(4).getSonCate().get(6).getEvalue()),R.mipmap.key));
        }
        if(type.equals("电子信息")){
            mLangyaDatas.add(new LangyaSimple("1", "2", data.get(0).getSonCate().get(0).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(0).getEvalue()),R.mipmap.noveldisplaydevice));
            mLangyaDatas.add(new LangyaSimple("2", "2", data.get(0).getSonCate().get(1).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(1).getEvalue()),R.mipmap.optical));
            mLangyaDatas.add(new LangyaSimple("3", "2", data.get(0).getSonCate().get(2).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(2).getEvalue()),R.mipmap.transportations));
            mLangyaDatas.add(new LangyaSimple("4", "2", data.get(0).getSonCate().get(3).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(3).getEvalue()),R.mipmap.informations));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(4).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(4).getEvalue()),R.mipmap.newtype));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(5).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(5).getEvalue()),R.mipmap.radios));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(6).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(6).getEvalue()),R.mipmap.communication));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(7).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(7).getEvalue()),R.mipmap.computers));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(8).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(8).getEvalue()),R.mipmap.microelectronic));
            mLangyaDatas.add(new LangyaSimple("5", "2", data.get(0).getSonCate().get(9).getEname(),  "电子信息",Integer.parseInt(data.get(0).getSonCate().get(9).getEvalue()),R.mipmap.software));
        }
             if(type.equals("新材料")) {
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(0).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(0).getEvalue()),R.mipmap.xinxicailiao));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(1).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(1).getEvalue()),R.mipmap.nycailiao));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(2).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(2).getEvalue()),R.mipmap.nmcailiao));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(3).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(3).getEvalue()),R.mipmap.fuhecailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(4).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(4).getEvalue()),R.mipmap.taocicailiao));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(5).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(5).getEvalue()),R.mipmap.hjcailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(6).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(6).getEvalue()),R.mipmap.gncailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(7).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(7).getEvalue()),R.mipmap.yycailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(8).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(8).getEvalue()),R.mipmap.xncailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(9).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(9).getEvalue()),R.mipmap.zncailaio));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(10).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(10).getEvalue()),R.mipmap.jzcailiao));
                 mLangyaDatas.add(new LangyaSimple("1", "3", data.get(1).getSonCate().get(11).getEname(), "新材料",Integer.parseInt(data.get(1).getSonCate().get(11).getEvalue()),R.mipmap.hgcailaio));
             }
             if(type.equals("生物技术")) {
                 mLangyaDatas.add(new LangyaSimple("1", "4", data.get(2).getSonCate().get(0).getEname(), "生物技术",Integer.parseInt(data.get(2).getSonCate().get(0).getEvalue()),R.mipmap.microorganism));
                 mLangyaDatas.add(new LangyaSimple("1", "4", data.get(2).getSonCate().get(1).getEname(), "生物技术",Integer.parseInt(data.get(2).getSonCate().get(1).getEvalue()),R.mipmap.manufacturing));
                 mLangyaDatas.add(new LangyaSimple("1", "4", data.get(2).getSonCate().get(2).getEname(), "生物技术",Integer.parseInt(data.get(2).getSonCate().get(2).getEvalue()),R.mipmap.agriculture));
                 mLangyaDatas.add(new LangyaSimple("1", "4", data.get(2).getSonCate().get(3).getEname(), "生物技术",Integer.parseInt(data.get(2).getSonCate().get(3).getEvalue()),R.mipmap.energy));
                 mLangyaDatas.add(new LangyaSimple("1", "4", data.get(2).getSonCate().get(4).getEname(), "生物技术",Integer.parseInt(data.get(2).getSonCate().get(4).getEvalue()),R.mipmap.medicine));
             }
             if(type.equals("节能环保")) {
                 mLangyaDatas.add(new LangyaSimple("1", "5", data.get(3).getSonCate().get(0).getEname(), "节能环保",Integer.parseInt(data.get(3).getSonCate().get(0).getEvalue()),R.mipmap.energyservice));
                 mLangyaDatas.add(new LangyaSimple("1", "5", data.get(3).getSonCate().get(1).getEname(), "节能环保",Integer.parseInt(data.get(3).getSonCate().get(1).getEvalue()),R.mipmap.resourcerecycling));
                 mLangyaDatas.add(new LangyaSimple("1", "5", data.get(3).getSonCate().get(2).getEname(), "节能环保",Integer.parseInt(data.get(3).getSonCate().get(2).getEvalue()),R.mipmap.advanced));
                 mLangyaDatas.add(new LangyaSimple("1", "5", data.get(3).getSonCate().get(3).getEname(), "节能环保",Integer.parseInt(data.get(3).getSonCate().get(3).getEvalue()),R.mipmap.highefficiency));
             }
             if(type.equals("文化创意")) {
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(0).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(0).getEvalue()),R.mipmap.softwares));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(1).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(1).getEvalue()),R.mipmap.television));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(2).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(2).getEvalue()),R.mipmap.press));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(3).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(3).getEvalue()),R.mipmap.cultures));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(4).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(4).getEvalue()),R.mipmap.advertising));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(5).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(5).getEvalue()),R.mipmap.business));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(6).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(6).getEvalue()),R.mipmap.design));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(7).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(7).getEvalue()),R.mipmap.leisure));
                 mLangyaDatas.add(new LangyaSimple("1", "6", data.get(5).getSonCate().get(8).getEname(), "文化创意",Integer.parseInt(data.get(5).getSonCate().get(8).getEvalue()),R.mipmap.ancillary));
             }
             if(type.equals("化学化工")) {
                 mLangyaDatas.add(new LangyaSimple("1", "7", data.get(6).getSonCate().get(0).getEname(), "化学化工",Integer.parseInt(data.get(6).getSonCate().get(0).getEvalue()),R.mipmap.chemical));
             }
             if(type.equals("新能源")) {
                 mLangyaDatas.add(new LangyaSimple("1", "8", data.get(7).getSonCate().get(0).getEname(), "新能源",Integer.parseInt(data.get(7).getSonCate().get(0).getEvalue()),R.mipmap.ocean));
                 mLangyaDatas.add(new LangyaSimple("1", "8", data.get(7).getSonCate().get(1).getEname(), "新能源",Integer.parseInt(data.get(7).getSonCate().get(1).getEvalue()),R.mipmap.geothermal));
                 mLangyaDatas.add(new LangyaSimple("1", "8", data.get(7).getSonCate().get(2).getEname(), "新能源",Integer.parseInt(data.get(7).getSonCate().get(2).getEvalue()),R.mipmap.biomass));
                 mLangyaDatas.add(new LangyaSimple("1", "8", data.get(7).getSonCate().get(3).getEname(), "新能源",Integer.parseInt(data.get(7).getSonCate().get(3).getEvalue()),R.mipmap.wind));
                 mLangyaDatas.add(new LangyaSimple("1", "8", data.get(7).getSonCate().get(4).getEname(), "新能源",Integer.parseInt(data.get(7).getSonCate().get(4).getEvalue()),R.mipmap.solarenergy));
             }
             if(type.equals("其他")) {
                 mLangyaDatas.add(new LangyaSimple("1", "9", data.get(8).getSonCate().get(0).getEname(), "其他",Integer.parseInt(data.get(8).getSonCate().get(0).getEvalue()),R.mipmap.highefficiency));
             }
         }
    }

    public void getleveljson() {
        Okhttp= OkHttpUtils.getInstancesOkHttp();
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
//                    for(int i=0;i<newbalist.size();i++) {
//                        int date = newbalist.get(i).getEavule();
//                        json=Okhttp.Myokhttpclient("http://123.206.8.208/api/getAreaCount.php?evalue="+date+ "");
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
//                    }
                }
            }.start();
        }catch (Exception e){}
    }

}