package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import Util.SharedPreferencesUtil;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.BmpluseData;
import entity.LangyaSimple;
import entity.four_data;
import view.RefreshListView;
import view.ResQualityChoseListener;
import view.ResQualityDialog;
import view.StyleUtils;
import view.StyleUtils1;
import view.SwipeLayout;

/**
 * Created by lizisong on 2016/12/20.
 */

public class pluse_four extends AutoLayoutActivity {
    Button back, next;
    ImageView close;
    String title;
    RelativeLayout bg;
    TextView mNameTxt;
    RefreshListView listView;
    List<four_data> listData = new ArrayList<>();
    dataDBApter dataAdapter;
    private String zhengci = "全部";
    private String rencai = "全部";
    public static boolean state = false;
    public PulseCondtion pulseCondtion;
    private String mids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluse_four);
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        pulseCondtion = PulseCondtion.getInstance(this);
        init();

        four_data Tile1 = new four_data();
        Tile1.type = 0;
        Tile1.title = "已选择行业:";
        listData.add(Tile1);

         if(bmplusetwo.dexlist != null){
             for(int i=0; i < bmplusetwo.dexlist.size(); i++){
                 LangyaSimple simple = bmplusetwo.dexlist.get(i);
                 four_data itemdata = new four_data();
                 itemdata.des = simple.getTitle();
                 itemdata.type=1;
                 itemdata.title=title;
                 itemdata.resid = simple.getPicid();
                 itemdata.tag = simple.getEvalue()+"";
                 listData.add(itemdata);
             }
         }

        four_data Tile2 = new four_data();
        Tile2.type = 0;
        Tile2.title = "定制的行业的信息:";
        listData.add(Tile2);

        if(BmpluseThree.showList != null){
            for(int i=0; i<BmpluseThree.showList.size(); i++){
                BmpluseData item = BmpluseThree.showList.get(i);
                if(item.checkState){
                    four_data itemdata = new four_data();
                    itemdata.title = item.txt;
                    itemdata.type=2;
                    itemdata.des = item.txt;
                    if(itemdata.des.equals("项目")){
                        itemdata.tag = "2";
                    }else if(itemdata.des.equals("人才")|| itemdata.des.equals("专家")){
                        itemdata.tag = "4";
                    }else if(itemdata.des.equals("设备")){
                        itemdata.tag = "7";
                    }else if(itemdata.des.equals("实验室")||itemdata.des.equals("研究所")){
                        itemdata.tag = "8";
                    }else if(itemdata.des.equals("政策")){
                        itemdata.tag = "6";
                    }else if(itemdata.des.equals("资讯")){
                        itemdata.tag = "1";
                    }else if(itemdata.des.equals("专利")){
                        itemdata.tag = "5";
                    }

                    listData.add(itemdata);
                }
            }
        }
    }



    public void init(){
        bg = (RelativeLayout)findViewById(R.id.title);
        back = (Button)findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        close = (ImageView)findViewById(R.id.img_close);
        next = (Button)findViewById(R.id.bamai_next_two);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity.evalue = "";
                ResultActivity.typeid = "";
                ResultActivity.evalueTitle = "";
                for(int i=0; i<listData.size(); i++){
                    four_data item = listData.get(i);
                    if(item.type == 1){
                        ResultActivity.evalue = ResultActivity.evalue + item.tag+",";
                        ResultActivity.evalueTitle = ResultActivity.evalueTitle + item.des+",";
                    }
                    if(item.type == 2){
                        ResultActivity.typeid = ResultActivity.typeid+item.tag+",";
                    }
                }

                if(rencai.equals("全部")){
                    ResultActivity.category = "0";
                }else if(rencai.equals("专家")){
                    ResultActivity.category = "1";
                }else if(rencai.equals("技术经济人")){
                    ResultActivity.category = "2";
                }

                if(zhengci.equals("全部")){
                    ResultActivity.province="0";
                }else if(zhengci.equals("省级政策")){
                    ResultActivity.province="2";
                }else if(zhengci.equals("全国政策")){
                    ResultActivity.province="1";
                }else if(zhengci.equals("市级政策")){
                    ResultActivity.province="3";
                }

                List<PulseData> list = pulseCondtion.get();
                PulseData item = null;
                mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                if(list.size() > 0){
                    for(int i=0;i<list.size(); i++){
                        PulseData pos = list.get(i);
                        if(pos.evaluetop.equals(ResultActivity.evaluetop+"")){
                            pos.name = title;
                            pos.updatatime = System.currentTimeMillis()+"";
                            pos.category = ResultActivity.category;
                            pos.evaluetop = ResultActivity.evaluetop + "";
                            pos.typeid = ResultActivity.typeid;
                            pos.province = ResultActivity.province;
                            pos.evalue = ResultActivity.evalue;
                            pos.evalueTitle = ResultActivity.evalueTitle;
                            pos.mid = mids+"";
                            item = pos;
                            break;
                        }
                    }
                }
                if(item == null){
                    item = new PulseData();
                    item.name = title;
                    item.updatatime = System.currentTimeMillis()+"";
                    item.category = ResultActivity.category;
                    item.evaluetop = ResultActivity.evaluetop + "";
                    item.typeid = ResultActivity.typeid;
                    item.province = ResultActivity.province;
                    item.evalue = ResultActivity.evalue;
                    item.evalueTitle = ResultActivity.evalueTitle;
                    item.mid = mids+"";
                    pulseCondtion.insert(item);
                }else{
                    item.name = title;
                    item.updatatime = System.currentTimeMillis()+"";
                    item.category = ResultActivity.category;
                    item.evaluetop = ResultActivity.evaluetop + "";
                    item.typeid = ResultActivity.typeid;
                    item.province = ResultActivity.province;
                    item.evalue = ResultActivity.evalue;
                    item.evalueTitle = ResultActivity.evalueTitle;
                    item.mid = mids+"";
                    pulseCondtion.updata(item);
                }

                Intent intent = new Intent(pluse_four.this, ResultActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("from", "pluse_four");
                startActivity(intent);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmpluseThree.close_state = true;
                bmplusetwo.close_state = true;
                PulseActivity.posdata = 1;
                finish();
            }
        });
        listView = (RefreshListView)findViewById(R.id.listview);
        dataAdapter = new dataDBApter();
        title = getIntent().getStringExtra("title");
        if(title.equals("先进制造")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xianjinzhizao);
            bg.setBackgroundResource(R.mipmap.bg_xianjinzhizao);
        }else if(title.equals("电子信息")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_dianzixinx);
            bg.setBackgroundResource(R.mipmap.bg_dianzixinxi);
        }else if(title.equals("新材料")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xincailiao);
            bg.setBackgroundResource(R.mipmap.bg_xincailiao);
        }else if(title.equals("生物技术")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_shegnwujishu);
            bg.setBackgroundResource(R.mipmap.bg_shengwujishu);
        }else if(title.equals("节能环保")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_jienenghuanbao);
            bg.setBackgroundResource(R.mipmap.bg_jienenghuanbao);
        }else if(title.equals("文化创意")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_wenhuachuangyi);
            bg.setBackgroundResource(R.mipmap.bg_wenhuachuangyi);
        }else if(title.equals("化学化工")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_huaxuehuagong);
            bg.setBackgroundResource(R.mipmap.bg_huaxuehuagong);
        }else if(title.equals("新能源")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xinnengyuan);
            bg.setBackgroundResource(R.mipmap.bg_xinnengyuan);
        }else if(title.equals("其他")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_qita);
            bg.setBackgroundResource(R.mipmap.bg_qita);
        }
        listView.setAdapter(dataAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state){
            state = false;
            finish();
        }
        MobclickAgent.onPageStart("确认行业和主题"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("确认行业和主题"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    class dataDBApter extends BaseAdapter{


        Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHold view = null;
            final four_data item = listData.get(position);
            if(convertView == null){
                view = new ViewHold();
                convertView = View.inflate(getApplication(), R.layout.pluse_four_adapter,null);
                view.tips = (TextView) convertView.findViewById(R.id.tips);

                view.swipeLayout = (SwipeLayout)convertView.findViewById(R.id.sl);
                view.icon = (ImageView)convertView.findViewById(R.id.iv);
                view.title = (TextView)convertView.findViewById(R.id.tv);
                view.des = (TextView)convertView.findViewById(R.id.des);
                view.del = (TextView)convertView.findViewById(R.id.del);

                view.bottom = (RelativeLayout)convertView.findViewById(R.id.bottom);
                view.txt = (TextView)convertView.findViewById(R.id.txt);
                view.txt_tip = (TextView)convertView.findViewById(R.id.txt_tip);
                view.arrow = (ImageView)convertView.findViewById(R.id.arrow);
                convertView.setTag(view);
            }else {
                view = (ViewHold) convertView.getTag();
            }
            if(item.type == 0){
                view.swipeLayout.setVisibility(View.GONE);
                view.bottom.setVisibility(View.GONE);
                view.tips.setVisibility(View.VISIBLE);
                view.tips.setText(item.title);
            }else if(item.type == 1){
                view.tips.setVisibility(View.GONE);
                view.swipeLayout.setVisibility(View.VISIBLE);
                view.bottom.setVisibility(View.GONE);
                view.icon.setBackgroundResource(item.resid);

                view.title.setText(item.des);
//                view.des.setText(item.des);
                view.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<bmplusetwo.dexlist.size();i++){
                            LangyaSimple simple = bmplusetwo.dexlist.get(i);
                            if(item.des.equals(simple.getTitle())){
                                bmplusetwo.dexlist.remove(simple);
                                break;
                            }
                        }
                        listData.remove(position);
                        dataAdapter.notifyDataSetChanged();
                    }
                });
            }else if(item.type == 2){
                view.swipeLayout.setVisibility(View.GONE);
                view.bottom.setVisibility(View.VISIBLE);
                view.tips.setVisibility(View.GONE);
                view.txt.setText(item.title);

                if(item.title.equals("人才") || item.title.equals("专家")){
                    if(rencai != null){
                        view.txt_tip .setText(rencai);
                    }else {
                        view.txt_tip .setText("全部");
                    }
                    view.arrow.setVisibility(View.VISIBLE);
                    view.bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ResQualityDialog dialog = new ResQualityDialog(pluse_four.this, "专家", "全部", "技术经济人",null);
                            dialog.setTitleText("专家分类");
                            dialog.setResQualityChoseListener(new ResQualityChoseListener() {
                                @Override
                                public void choseModeAuto(String txt) {
                                    rencai = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseModeHigh(String txt) {

                                    rencai = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseModeLow(String txt) {

                                    rencai = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseTitle(String txt) {
//                                    view.txt_tip.setText(txt);
//                                    rencai = txt;
                                }
                            });
                            dialog.show();
                        }
                    });
                }else if(item.title.equals("政策")){
                    if(zhengci != null){
                       view.txt_tip .setText(zhengci);
                    }else {
                        view.txt_tip .setText("全部");
                    }
                    view.arrow.setVisibility(View.VISIBLE);
                    view.bottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ResQualityDialog dialog = new ResQualityDialog(pluse_four.this, "省级政策", "全国政策", "全部","市级政策");
                            dialog.setTitleText("政策分类");
                            dialog.setResQualityChoseListener(new ResQualityChoseListener() {
                                @Override
                                public void choseModeAuto(String txt) {
                                    zhengci = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseModeHigh(String txt) {
                                    zhengci = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseModeLow(String txt) {
                                    zhengci = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void choseTitle(String txt) {
                                    zhengci = txt;
                                    dataAdapter.notifyDataSetChanged();
                                }
                            });
                            dialog.show();

                        }
                    });
                }else{
                    view.arrow.setVisibility(View.INVISIBLE);
                }

            }

            return convertView;
        }



        class ViewHold{
             TextView tips;//tips

             SwipeLayout swipeLayout;
             ImageView icon;
             TextView title;
             TextView des;
             TextView del;

            RelativeLayout bottom;
            TextView txt;
            TextView txt_tip;
            ImageView arrow;
        }
    }

}
