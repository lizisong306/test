package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoLinearLayout;
import com.chenantao.autolayout.utils.AutoUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianGuanzhu;
import dao.dbentity.CollectionEntity;
import entity.RetYuJianData;
import entity.YuJian;
import entity.ZanCode;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;
import view.SwipeLayout;

import static com.maidiantech.R.id.zx_zan;


/**
 * Created by 13520 on 2016/9/20.
 */
public class MycollectActivity extends AutoLayoutActivity {
    private ArrayList<CollectionEntity> listCollections = null;
    private MaiDianCollection maiDianCollection = null;
    private MaiDianGuanzhu maiDianGuanzhu = null;
    private  LayoutInflater inFlater;
    private RefreshListView mListView;
    CollectionAdpter mListAdapter;
    private OkHttpUtils Okhttp;
    private String mids,quxiaojson;
    private ProgressBar progress;
    RelativeLayout  shoucan_img;
    ImageView sc_img;
    ImageView gz_img;
    String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collection);

        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        Intent intent = getIntent();
        progress = (ProgressBar)findViewById(R.id.progress);
        type =intent.getStringExtra("type");
        maiDianCollection = MaiDianCollection.getInstance(this);
        maiDianGuanzhu = MaiDianGuanzhu.getInstance(this);
        TextView mine_head=(TextView) findViewById(R.id.mine_head);
        mListView = (RefreshListView)findViewById(R.id.my_collect_lv);
         sc_img=(ImageView) findViewById(R.id.sc_img);
         gz_img=(ImageView) findViewById(R.id.gz_img) ;
          shoucan_img=(RelativeLayout) findViewById(R.id.shoucan_img);
        progress.setVisibility(View.VISIBLE);
        maiDianCollection.deleteData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                collect();
            }
        }).start();

        if(type.equals("1")){
            mine_head.setText("我的收藏");

            listCollections = maiDianCollection.get();
//            if(listCollections.size()==0){
//                shoucan_img.setVisibility(View.VISIBLE);
//                sc_img.setVisibility(View.VISIBLE);
//                gz_img.setVisibility(View.GONE);
//                mListView.setVisibility(View.GONE);
//            }else{
//                shoucan_img.setVisibility(View.GONE);
//                sc_img.setVisibility(View.GONE);
//                gz_img.setVisibility(View.GONE);
//                mListView.setVisibility(View.VISIBLE);
//            }
        }else if(type.equals("2")){
            mine_head.setText("我的关注");
            listCollections = maiDianGuanzhu.get();
            if(listCollections.size()==0){
                shoucan_img.setVisibility(View.VISIBLE);
                sc_img.setVisibility(View.GONE);
                gz_img.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }else{
                shoucan_img.setVisibility(View.GONE);
                sc_img.setVisibility(View.GONE);
                gz_img.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
        }

        ImageView collext_back=(ImageView) findViewById(R.id.collext_back);


        collext_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListAdapter = new CollectionAdpter();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 CollectionEntity item= listCollections.get(position);
                 String name = item.type;
                if(name != null && (name.equals("科讯早参"))){
                    Intent intent = new Intent(MycollectActivity.this, EarlyRef.class);
                    intent.putExtra("id", item.aid);
                    startActivity(intent);

                }else if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                    Intent intent = new Intent(MycollectActivity.this, ZixunDetailsActivity.class);
                    intent.putExtra("id", item.aid);
                    intent.putExtra("name", item.type);
                    intent.putExtra("pic", item.pic);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MycollectActivity.this, DetailsActivity.class);
                    intent.putExtra("id", item.aid);
                    intent.putExtra("name", item.type);
                    intent.putExtra("pic", item.pic);
                    startActivity(intent);
                }
            }
        });
        mListView.setAdapter(mListAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(type.equals("1")){
            listCollections = maiDianCollection.get();
            Collections.reverse(listCollections);
        }else if(type.equals("2")){

            listCollections = maiDianGuanzhu.get();
            Collections.reverse(listCollections);
        }
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * 适配器类
     */
    class  CollectionAdpter extends BaseAdapter{
        private DisplayImageOptions options;
        private  String   ips;
        private String read_ids;

        @Override
        public int getCount() {
            return listCollections.size();
        }

        @Override
        public Object getItem(int position) {
            return listCollections.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final CollectionEntity item = listCollections.get(position);
            if(convertView == null){
                holder = new ViewHolder();
                inFlater =(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inFlater.inflate(R.layout.collection_adapter, null);

                //资讯
                holder.s1 = (SwipeLayout)convertView.findViewById(R.id.s1);
                holder.cene1 = (TextView)convertView.findViewById(R.id.cenel);
                holder.zx_img = (ImageView) convertView.findViewById(R.id.zx_img);
                holder.zx_title = (TextView) convertView.findViewById(R.id.zx_title);
                holder.zx_zan = (TextView) convertView.findViewById(zx_zan);
                holder.zx_look = (TextView) convertView.findViewById(R.id.zx_look);
                holder.zx_layout = (AutoLinearLayout) convertView.findViewById(R.id.zx_layout);
                holder.zx_zt = (ImageView) convertView.findViewById(R.id.zx_zt);
                holder.zixu_zhiding = (ImageView) convertView.findViewById(R.id.zixue_zhiding);
                holder.zixu_tuijian = (ImageView) convertView.findViewById(R.id.zixun_tuijian);
                holder.zx_line_zan = (LinearLayout) convertView.findViewById(R.id.zx_line_zan);
                holder.zx_lanyuan=(TextView) convertView.findViewById(R.id.zx_lanyuan);

                //项目
                holder.s2 = (SwipeLayout)convertView.findViewById(R.id.s2);
                holder.cene2 = (TextView)convertView.findViewById(R.id.cene2);
                holder.xm_img = (ImageView) convertView.findViewById(R.id.xm_img);
                holder.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
                holder.xm_linyu=(TextView)convertView.findViewById(R.id.xm_linyu);
                holder.xm_dianwei = (TextView) convertView.findViewById(R.id.xm_dianwei);
                holder.xm_look = (TextView) convertView.findViewById(R.id.xm_look);
                holder.xm_layout = (AutoLinearLayout) convertView.findViewById(R.id.xm_layout);
                holder.xm_zt = (ImageView) convertView.findViewById(R.id.xm_zt);
                holder.xiangmu_tuijian = (ImageView) convertView.findViewById(R.id.xiangmu_tuijian);
                holder.xiangmu_zhiding = (ImageView) convertView.findViewById(R.id.xiangmu_zhiding);
                holder.xm_name=(TextView) convertView.findViewById(R.id.xm_name);
                holder.xm_description=(TextView) convertView.findViewById(R.id.xm_description);

                //政策
                holder.s3 = (SwipeLayout)convertView.findViewById(R.id.s3);
                holder.cene3 = (TextView)convertView.findViewById(R.id.cene3);
                holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
                holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
                holder.zc_lingyu = (TextView) convertView.findViewById(R.id.zc_lingyu);
                holder.zc_look = (TextView) convertView.findViewById(R.id.zc_look);
                holder.zc_layout = (AutoLinearLayout) convertView.findViewById(R.id.zc_layout);
                holder.zc_zt = (ImageView) convertView.findViewById(R.id.zc_zt);
                holder.zc_zhiding = (ImageView) convertView.findViewById(R.id.zhengci_zhiding);
                holder.zc_tuijian = (ImageView) convertView.findViewById(R.id.zhengci_tuijian);
                holder.zc_zan = (TextView)convertView.findViewById(R.id.zc_zan);

                //人才
                holder.s4 = (SwipeLayout)convertView.findViewById(R.id.s4);
                holder.cene4 = (TextView)convertView.findViewById(R.id.cene4);
                holder.rc_img = (RoundImageView) convertView.findViewById(R.id.rc_img);
                holder.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
                holder.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
                holder.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
                holder.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
                holder.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);
                holder.rc_zan = (TextView) convertView.findViewById(R.id.rc_zan);
                holder.rc_layout = (AutoLinearLayout) convertView.findViewById(R.id.rc_layout);
                holder.rc_zt = (ImageView) convertView.findViewById(R.id.rc_zt);
                holder.rencai_zhiding = (ImageView) convertView.findViewById(R.id.rencai_zhiding);
                holder.rencai_tuijian = (ImageView) convertView.findViewById(R.id.rencai_tuijian);
                holder.rc_text=(TextView) convertView.findViewById(R.id.rc_text);
                holder.rc_yuanshi=(ImageView) convertView.findViewById(R.id.rc_yuanshi);

                //设备
                holder.s5 = (SwipeLayout)convertView.findViewById(R.id.s5);
                holder.cene5 = (TextView)convertView.findViewById(R.id.cene5);
                holder.device_img = (ImageView) convertView.findViewById(R.id.device_img);
                holder.device_title = (TextView) convertView.findViewById(R.id.device_title);
                holder.device_zan = (TextView) convertView.findViewById(R.id.device_zan);
                holder.device_description = (TextView)convertView.findViewById(R.id.device_description);
                holder.device_linyu =(TextView)convertView.findViewById(R.id.device_linyu);
                holder.device_look = (TextView) convertView.findViewById(R.id.device_look);
                holder.device_layout = (AutoLinearLayout) convertView.findViewById(R.id.device_layout);
                holder.sb_zt = (ImageView) convertView.findViewById(R.id.sb_zt);
                holder.shebei_zhiding = (ImageView) convertView.findViewById(R.id.shenbei_zhiding);
                holder.shebei_tuijian = (ImageView) convertView.findViewById(R.id.shenbei_tuijian);
                holder.shebei_dianzan=(LinearLayout) convertView.findViewById(R.id.shebei_dianzan);

                //专利
                holder.s6 = (SwipeLayout)convertView.findViewById(R.id.s6);
                holder.cene6 = (TextView)convertView.findViewById(R.id.cene6);
                holder.zhunli_title = (TextView) convertView.findViewById(R.id.zhunli_title);
                holder.zhuanli_look = (TextView) convertView.findViewById(R.id.zhuanli_look);
                holder.zhuanli_zan  = (TextView)convertView.findViewById(R.id.zhuanli_zan) ;
                holder.zhuanli_layout = (AutoLinearLayout) convertView.findViewById(R.id.zhuanli_layout);
                holder.zhuanli_zt = (ImageView) convertView.findViewById(R.id.zhuanli_zt);
                holder.zhuanli_tuijian = (ImageView) convertView.findViewById(R.id.zhuanli_tuijian);
                holder.zhuanli_zhiding = (ImageView) convertView.findViewById(R.id.zhuanli_zhiding);
                holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.zhuanli_linyu);
                holder.zhuanli_description=(TextView) convertView.findViewById(R.id.zhuanli_description);
                holder.zhuanli_line=(LinearLayout) convertView.findViewById(R.id.zhuanli_line);

                //实验室
                holder.s7 = (SwipeLayout)convertView.findViewById(R.id.s7);
                holder.cene7 = (TextView)convertView.findViewById(R.id.cene7);
                holder.librarys = (AutoLinearLayout) convertView.findViewById(R.id.librarys);
                holder.librarys_title = (TextView) convertView.findViewById(R.id.librarys_title);
                holder.librarys_img = (ImageView) convertView.findViewById(R.id.librarys_img);
//            holder.librarys_linyu=(TextView)convertView.findViewById(R.id.librarys_linyu);
                holder.librarys_zan = (TextView) convertView.findViewById(R.id.librarys_zan);
                holder.librarys_look = (TextView) convertView.findViewById(R.id.librarys_look);
                holder.sys_zt = (ImageView) convertView.findViewById(R.id.sys_zt);
                holder.shiyanshi_zhiding = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding);
                holder.shiyanshi_tuijian = (ImageView) convertView.findViewById(R.id.shiyanshi_tuijian);
                holder.datu_name=(TextView) convertView.findViewById(R.id.datu_name);
                holder.sys_dianzan=(LinearLayout) convertView.findViewById(R.id.sys_dianzan);

                //实验室
                holder.s8 = (SwipeLayout) convertView.findViewById(R.id.s8);
                holder.cene8 = (TextView)convertView.findViewById(R.id.cene8);
                holder.librarys1 = (AutoLinearLayout) convertView.findViewById(R.id.librarys1);
                holder.librarys_title1 = (TextView) convertView.findViewById(R.id.librarys_title1);
                holder.librarys_img1 = (ImageView) convertView.findViewById(R.id.librarys_img1);


                holder.shiyanshi_zhiding1 = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding1);

                //三图
                holder.s9 = (SwipeLayout)convertView.findViewById(R.id.s9);
                holder.cene9 = (TextView)convertView.findViewById(R.id.cene9);
                holder.tj_state_line = (AutoLinearLayout) convertView.findViewById(R.id.tj_state_line);
                holder.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
                holder.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
                holder.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
                holder.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);
                holder.tj_state_zan = (TextView) convertView.findViewById(R.id.tj_state_zan);
                holder.tj_state_click = (TextView) convertView.findViewById(R.id.tj_state_click);
                holder.santu_zhiding = (ImageView) convertView.findViewById(R.id.santu_zhiding);
                holder.santu_tuijian = (ImageView) convertView.findViewById(R.id.santu_tuijian);
                holder.santu_name=(TextView) convertView.findViewById(R.id.santu_name);
                holder.tj_state_zt=(ImageView) convertView.findViewById(R.id.tj_state_zt);
                holder.state_zan = (LinearLayout) convertView.findViewById(R.id.state_zan);
                AutoUtils.autoSize(convertView);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            options = ImageLoaderUtils.initOptions();
            ips = MyApplication.ip;

            try {
                holder.librarys1.setVisibility(View.GONE);
                holder.s8.setVisibility(View.GONE);

            if(item.type.equals("资讯") || item.type.equals("推荐") || item.type.equals("活动") || item.type.equals("专题") || item.type.equals("知识")){
                        holder.librarys1.setVisibility(View.GONE);
                        holder.s8.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.s1.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.s7.setVisibility(View.GONE);
                        holder.zc_layout.setVisibility(View.GONE);
                        holder.s3.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.s2.setVisibility(View.GONE);
                        holder.rc_layout.setVisibility(View.GONE);
                        holder.s4.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.GONE);
                        holder.s5.setVisibility(View.GONE);
                        holder.zhuanli_layout.setVisibility(View.GONE);
                        holder.s6.setVisibility(View.GONE);
                        holder.xm_zt.setVisibility(View.GONE);
                        holder.zc_zt.setVisibility(View.GONE);
                        holder.rc_zt.setVisibility(View.GONE);
                        holder.sb_zt.setVisibility(View.GONE);
                        holder.sys_zt.setVisibility(View.GONE);
                        holder.zhuanli_zt.setVisibility(View.GONE);

                        if (item.imageState != null) {
                            if (item.imageState.equals("3")) {
                                holder.tj_state_line.setVisibility(View.VISIBLE);
                                holder.s9.setVisibility(View.VISIBLE);
                                holder.zx_title.setVisibility(View.GONE);
                                holder.zx_img.setVisibility(View.GONE);
                                holder.librarys.setVisibility(View.GONE);
                                holder.s7.setVisibility(View.GONE);
                                holder.librarys1.setVisibility(View.GONE);
                                holder.s8.setVisibility(View.GONE);
                                holder.zx_layout.setVisibility(View.GONE);
                                holder.s1.setVisibility(View.GONE);


                                holder.tj_state_title.setText(item.title);

                                read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                                if(read_ids.equals("")){
                                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                                }else{
                                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                                }
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI) {
                                        ImageLoader.getInstance().displayImage(item.image.image1
                                                , holder.tj_state_img1, options);
                                        ImageLoader.getInstance().displayImage(item.image.image2
                                                , holder.tj_state_img2, options);
                                        ImageLoader.getInstance().displayImage(item.image.image3
                                                , holder.tj_state_img3, options);
                                    } else {
                                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                    }
                                } else {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(item.image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(item.image.image3
                                            , holder.tj_state_img3, options);
                                }
                                holder.santu_name.setText(item.type);
                                holder.tj_state_zan.setText(item.zan);
                                holder.tj_state_click.setText(item.click);


                            } else if (item.imageState.equals("1")) {
                                holder.tj_state_line.setVisibility(View.GONE);
                                holder.s9.setVisibility(View.GONE);
                                holder.librarys_img.setVisibility(View.VISIBLE);
                                holder.librarys_title.setVisibility(View.VISIBLE);
                                holder.librarys1.setVisibility(View.GONE);
                                holder.s8.setVisibility(View.GONE);
                                holder.librarys.setVisibility(View.VISIBLE);
                                holder.s7.setVisibility(View.VISIBLE);
                                holder.zx_layout.setVisibility(View.GONE);
                                holder.s1.setVisibility(View.GONE);
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI) {
                                        ImageLoader.getInstance().displayImage(item.image.image1
                                                , holder.librarys_img, options);
                                    } else {
                                        holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                    }
                                } else {


                                    ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                    params.height=MyApplication.height;
                                    params.width =MyApplication.width;
                                    holder.librarys_img.setLayoutParams(params);
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.librarys_img, options);
                                }
                                holder.librarys_title.setText(item.title);
                                read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                                if(read_ids.equals("")){
                                    holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                                }else{
                                    holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                                }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                                holder.datu_name.setText(item.type);
                                holder.librarys_zan.setText(item.zan);
                                holder.librarys_look.setText(item.click);
                            } else if (item.imageState.equals("0")) {
                                holder.tj_state_line.setVisibility(View.GONE);
                                holder.s9.setVisibility(View.GONE);
                                holder.librarys.setVisibility(View.GONE);
                                holder.s7.setVisibility(View.GONE);
                                holder.zx_img.setVisibility(View.VISIBLE);
                                holder.librarys1.setVisibility(View.GONE);
                                holder.s8.setVisibility(View.GONE);
                                holder.zx_title.setVisibility(View.VISIBLE);
                                holder.zx_layout.setVisibility(View.VISIBLE);
                                holder.s1.setVisibility(View.VISIBLE);
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI) {
                                        ImageLoader.getInstance().displayImage(item.pic
                                                , holder.zx_img, options);
                                    } else {
                                        holder.zx_img.setBackgroundResource(R.mipmap.information_placeholder);
                                    }
                                } else {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.zx_img, options);
                                }
                            } else if (item.imageState.equals("-1")) {
                                holder.zx_img.setVisibility(View.GONE);
                                holder.librarys.setVisibility(View.GONE);
                                holder.s7.setVisibility(View.GONE);
                                holder.librarys1.setVisibility(View.GONE);
                                holder.s8.setVisibility(View.GONE);
                                holder.zx_layout.setVisibility(View.VISIBLE);
                                holder.s1.setVisibility(View.VISIBLE);
                                holder.zx_title.setVisibility(View.VISIBLE);
                                holder.tj_state_line.setVisibility(View.GONE);
                                holder.s9.setVisibility(View.GONE);
                            }
                        }

                        holder.zx_title.setText(item.title);
                        holder.zx_zan.setText(item.zan);
                        holder.zx_look.setText(item.click);
                        read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                        if(read_ids.equals("")){
                            holder.zx_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.zx_title.setTextColor(Color.parseColor("#777777"));
                        }
                      holder.zx_lanyuan.setVisibility(View.VISIBLE);
                      holder.zx_lanyuan.setText(item.type);
                }else if (item.type.equals("项目") || item.type.equals("实验室")||item.type.equals("研究所")){
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.VISIBLE);
                    holder.s2.setVisibility(View.VISIBLE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.s1.setVisibility(View.GONE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.s3.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.GONE);
                    holder.s4.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.GONE);
                    holder.s5.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.GONE);
                    holder.s6.setVisibility(View.GONE);
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.zhuanli_zt.setVisibility(View.GONE);

                    holder.xm_zt.setVisibility(View.GONE);
                    holder.xm_dianwei.setVisibility(View.GONE);

                    if (item.imageState != null) {
                        if (item.imageState.equals("3")) {
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.tj_state_line.setVisibility(View.VISIBLE);
                            holder.s9.setVisibility(View.VISIBLE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.xm_layout.setVisibility(View.GONE);
                            holder.s2.setVisibility(View.GONE);
                            holder.xm_title.setVisibility(View.GONE);
                            holder.xm_img.setVisibility(View.GONE);

                            holder.tj_state_title.setText(item.title);
                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){
                                holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                            }
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(item.image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(item.image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(item.image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(item.image.image3
                                        , holder.tj_state_img3, options);
                            }
                            holder.santu_name.setText(item.type);
                            holder.tj_state_zan.setText(item.zan);
                            holder.tj_state_click.setText(item.click);

                        } else if (item.imageState.equals("1")) {
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.xm_layout.setVisibility(View.GONE);
                            holder.s2.setVisibility(View.GONE);
                            holder.librarys_img.setVisibility(View.VISIBLE);
                            holder.librarys_title.setVisibility(View.VISIBLE);
                            holder.librarys.setVisibility(View.VISIBLE);
                            holder.s7.setVisibility(View.VISIBLE);

                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=MyApplication.height;
                                params.width =MyApplication.width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.librarys_img, options);
                            }
                            holder.librarys_title.setText(item.title);
                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){
                                holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                            }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                            holder.datu_name.setText(item.type);
                            holder.librarys_zan.setText(item.zan);
                            holder.librarys_look.setText(item.click);
                        } else if (item.imageState.equals("0")) {
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.xm_layout.setVisibility(View.VISIBLE);
                            holder.s2.setVisibility(View.VISIBLE);
                            holder.xm_img.setVisibility(View.VISIBLE);
                            holder.xm_title.setVisibility(View.VISIBLE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.pic
                                            , holder.xm_img, options);
                                } else {
                                    holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.xm_img, options);
                            }
                        } else if (item.imageState.equals("-1")) {
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.xm_img.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.xm_layout.setVisibility(View.VISIBLE);
                            holder.s2.setVisibility(View.VISIBLE);
                            holder.xm_title.setVisibility(View.VISIBLE);
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                        }
                    }

                    holder.xm_title.setText(item.title);
                    if(item.area_cate != null){
                        holder.xm_linyu.setText(item.area_cate);
                    }
                    if(item.description == null || item.description.equals("")){
                        holder.xm_description.setVisibility(View.GONE);
                    }else{
                        holder.xm_description.setVisibility(View.VISIBLE);
                        holder.xm_description.setText(item.description);
                    }

                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){
                        holder.xm_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.xm_title.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.xm_name.setVisibility(View.VISIBLE);
                    holder.xm_name.setText(item.type);
                    holder.xm_dianwei.setText(item.zan);
                    holder.xm_look.setText(item.click);

                }else if(item.type.equals("政策")){
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.zc_layout.setVisibility(View.VISIBLE);
                    holder.s3.setVisibility(View.VISIBLE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.s2.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.s1.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.GONE);
                    holder.s4.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.GONE);
                    holder.s5.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.GONE);
                    holder.s6.setVisibility(View.GONE);
                    holder.xm_zt.setVisibility(View.GONE);
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.zhuanli_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.s9.setVisibility(View.GONE);

                    holder.zc_zt.setVisibility(View.GONE);
                    holder.zc_zan.setVisibility(View.GONE);

                    holder.zc_title.setText(item.title);
                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){

                        holder.zc_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.zc_title.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.zc_description.setText(item.description.replaceAll("\r\n","").replaceAll("\n", ""));
                    holder.zc_look.setText(item.click);
                    holder.zc_zan.setText(item.zan);
                    holder.zc_lingyu.setVisibility(View.VISIBLE);
                    holder.zc_lingyu.setText(item.type);

                }else if(item.type.equals("人才") ||item.type.equals("专家")){
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.VISIBLE);
                    holder.s4.setVisibility(View.VISIBLE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.s3.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.s2.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.s1.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.GONE);
                    holder.s5.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.GONE);
                    holder.s6.setVisibility(View.GONE);
                    holder.xm_zt.setVisibility(View.GONE);
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.zhuanli_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.s9.setVisibility(View.GONE);

                    holder.rc_zt.setVisibility(View.GONE);

                    holder.rc_uname.setText(item.title);
                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){
                        holder.rc_uname.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.rc_uname.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.rc_title.setText(item.description);

                    if(item.is_academician != null){
                        if(item.is_academician.equals("1")){
                            holder.rc_yuanshi.setVisibility(View.VISIBLE);
                        }else if(item.is_academician.equals("0")){
                            holder.rc_yuanshi.setVisibility(View.GONE);
                        }
                    }


                    if (item.area_cate != null ) {
                        if(item.area_cate.equals("")){
                            holder.rc_text.setVisibility(View.GONE);
                            holder.rc_linyu.setVisibility(View.GONE);
                        }else{
                            holder.rc_text.setVisibility(View.VISIBLE);
                            holder.rc_linyu.setVisibility(View.VISIBLE);
                            holder.rc_linyu.setText(item.area_cate);
                        }
                    }else{
                        holder.rc_text.setVisibility(View.GONE);
                        holder.rc_linyu.setVisibility(View.GONE);
                    }
                    holder.rc_zan.setText(item.zan);
                    holder.rc_look.setText(item.click);
                    if (item.pic.equals("")) {
                        holder.rc_img.setVisibility(View.GONE);
                    } else {
                        holder.rc_img.setVisibility(View.VISIBLE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(item.pic
                                        , holder.rc_img, options);
                            } else {
                                holder.rc_img.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(item.pic
                                    , holder.rc_img, options);
                        }
                    }

                }else if (item.type.equals("设备")){
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.VISIBLE);
                    holder.s5.setVisibility(View.VISIBLE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.s3.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.s2.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.s1.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.GONE);
                    holder.s4.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.GONE);
                    holder.s6.setVisibility(View.GONE);
                    holder.xm_zt.setVisibility(View.GONE);
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                    holder.zhuanli_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.shebei_dianzan.setVisibility(View.GONE);

                    if (item.imageState != null) {
                        if (item.imageState.equals("3")) {
                            holder.tj_state_line.setVisibility(View.VISIBLE);
                            holder.s9.setVisibility(View.VISIBLE);
                            holder.device_layout.setVisibility(View.GONE);
                            holder.s5.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.device_title.setVisibility(View.GONE);
                            holder.device_img.setVisibility(View.GONE);

                            holder.tj_state_title.setText(item.title);
                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){
                                holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                            }
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(item.image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(item.image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(item.image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(item.image.image3
                                        , holder.tj_state_img3, options);
                            }
                            holder.santu_name.setText(item.type);
                            holder.tj_state_zan.setText(item.zan);
                            holder.tj_state_click.setText(item.click);

                        } else if (item.imageState.equals("1")) {
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.device_layout.setVisibility(View.GONE);
                            holder.s5.setVisibility(View.GONE);
                            holder.librarys_img.setVisibility(View.VISIBLE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.librarys_title.setVisibility(View.VISIBLE);
                            holder.librarys.setVisibility(View.VISIBLE);
                            holder.s7.setVisibility(View.VISIBLE);

                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=MyApplication.height;
                                params.width =MyApplication.width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.librarys_img, options);
                            }
                            holder.librarys_title.setText(item.title);
                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){

                                holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                            }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                            holder.datu_name.setText(item.type);
                            holder.librarys_zan.setText(item.zan);
                            holder.librarys_look.setText(item.click);
                        } else if (item.imageState.equals("0")) {
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.device_layout.setVisibility(View.VISIBLE);
                            holder.s5.setVisibility(View.VISIBLE);
                            holder.device_img.setVisibility(View.VISIBLE);
                            holder.device_title.setVisibility(View.VISIBLE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.pic
                                            , holder.device_img, options);
                                } else {
                                    holder.device_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.device_img, options);
                            }
                        } else if (item.imageState.equals("-1")) {
                            holder.device_img.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.device_layout.setVisibility(View.VISIBLE);
                            holder.s5.setVisibility(View.VISIBLE);
                            holder.device_title.setVisibility(View.VISIBLE);
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                        }
                    }
                    holder.device_title.setText(item.title);
                    if(item.area_cate != null){
                        holder.device_linyu.setText(item.area_cate);
                    }
                    if(item.description == null || item.description.equals("")){
                        holder.device_description.setVisibility(View.GONE);
                    }else{
                        holder.device_description.setVisibility(View.VISIBLE);
                        holder.device_description.setText(item.description);
                    }

                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){

                        holder.device_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.device_title.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.device_zan.setText(item.zan);
                    holder.device_look.setText(item.click);
                }else if (item.type.equals("专利")){
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.VISIBLE);
                    holder.s6.setVisibility(View.VISIBLE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.s3.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.s2.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.s1.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.GONE);
                    holder.s4.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.GONE);
                    holder.s5.setVisibility(View.GONE);
                    holder.xm_zt.setVisibility(View.GONE);
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.s9.setVisibility(View.GONE);

                    holder.zhuanli_zt.setVisibility(View.GONE);

                    holder.zhunli_title.setText(item.title);
                    if(item.area_cate != null ){
                        if(item.area_cate == null || item.area_cate.equals("")){
                            holder.zhuanli_line.setVisibility(View.GONE);
                        }else{
                            holder.zhuanli_line.setVisibility(View.VISIBLE);
                            holder.zhuanli_linyu.setText(item.area_cate);
                        }
                    }
                    if(item.description == null || item.description.equals("")){
                        holder.zhuanli_description.setVisibility(View.GONE);
                    }else{
                        holder.zhuanli_description.setVisibility(View.VISIBLE);
                        holder.zhuanli_description.setText(item.description.replaceAll("\r\n","").replaceAll("\n", ""));
                    }
                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){

                        holder.zhunli_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.zhunli_title.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.zhuanli_look.setText(item.click);
                    holder.zhuanli_zan.setText(item.zan);
                }else {
                    holder.librarys1.setVisibility(View.GONE);
                    holder.s8.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.VISIBLE);
                    holder.s1.setVisibility(View.VISIBLE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.s7.setVisibility(View.GONE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.s3.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.s2.setVisibility(View.GONE);
                    holder.rc_layout.setVisibility(View.GONE);
                    holder.s4.setVisibility(View.GONE);
                    holder.device_layout.setVisibility(View.GONE);
                    holder.s5.setVisibility(View.GONE);
                    holder.zhuanli_layout.setVisibility(View.GONE);
                    holder.s6.setVisibility(View.GONE);
                    holder.xm_zt.setVisibility(View.GONE);
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                    holder.sb_zt.setVisibility(View.GONE);
                    holder.sys_zt.setVisibility(View.GONE);
                    holder.zhuanli_zt.setVisibility(View.GONE);

                    if (item.imageState != null) {
                        if (item.imageState.equals("3")) {
                            holder.tj_state_line.setVisibility(View.VISIBLE);
                            holder.s9.setVisibility(View.VISIBLE);
                            holder.zx_title.setVisibility(View.GONE);
                            holder.zx_img.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.zx_layout.setVisibility(View.GONE);
                            holder.s1.setVisibility(View.GONE);


                            holder.tj_state_title.setText(item.title);

                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){
                                holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                            }
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(item.image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(item.image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(item.image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(item.image.image3
                                        , holder.tj_state_img3, options);
                            }
                            holder.santu_name.setText(item.type);
                            holder.tj_state_zan.setText(item.zan);
                            holder.tj_state_click.setText(item.click);


                        } else if (item.imageState.equals("1")) {
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.librarys_img.setVisibility(View.VISIBLE);
                            holder.librarys_title.setVisibility(View.VISIBLE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.VISIBLE);
                            holder.s7.setVisibility(View.VISIBLE);
                            holder.zx_layout.setVisibility(View.GONE);
                            holder.s1.setVisibility(View.GONE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {


                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=MyApplication.height;
                                params.width =MyApplication.width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.librarys_img, options);
                            }
                            holder.librarys_title.setText(item.title);
                            read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                            if(read_ids.equals("")){
                                holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                            }else{
                                holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                            }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                            holder.datu_name.setText(item.type);
                            holder.librarys_zan.setText(item.zan);
                            holder.librarys_look.setText(item.click);
                        } else if (item.imageState.equals("0")) {
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.zx_img.setVisibility(View.VISIBLE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.zx_title.setVisibility(View.VISIBLE);
                            holder.zx_layout.setVisibility(View.VISIBLE);
                            holder.s1.setVisibility(View.VISIBLE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(item.pic
                                            , holder.zx_img, options);
                                } else {
                                    holder.zx_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(item.image.image1
                                        , holder.zx_img, options);
                            }
                        } else if (item.imageState.equals("-1")) {
                            holder.zx_img.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.s7.setVisibility(View.GONE);
                            holder.librarys1.setVisibility(View.GONE);
                            holder.s8.setVisibility(View.GONE);
                            holder.zx_layout.setVisibility(View.VISIBLE);
                            holder.s1.setVisibility(View.VISIBLE);
                            holder.zx_title.setVisibility(View.VISIBLE);
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.s9.setVisibility(View.GONE);
                        }
                    }

                    holder.zx_title.setText(item.title);
                    holder.zx_zan.setText(item.zan);
                    holder.zx_look.setText(item.click);
                    read_ids = PrefUtils.getString(MycollectActivity.this, item.aid, "");
                    if(read_ids.equals("")){
                        holder.zx_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.zx_title.setTextColor(Color.parseColor("#777777"));
                    }

                 holder.zx_lanyuan.setVisibility(View.VISIBLE);
                 holder.zx_lanyuan.setText(item.type);
                }

            }catch (Exception e){

            }
            holder.zx_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.xm_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.zc_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.rc_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.device_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.zhuanli_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.librarys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.tj_state_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.librarys1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDet(item);
                }
            });
            holder.cene1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });
            holder.cene9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delclick(item);
                }
            });

            return convertView;
        }

        public void delclick(final CollectionEntity item){
            Okhttp =  OkHttpUtils.getInstancesOkHttp();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                                MyApplication.setAccessid();
                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                sort.add("id"+item.pid);
                                sort.add("mid"+mids);
                                sort.add("method"+"cancel");
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                String accessid="";
                                String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                if(loginState.equals("1")){
                                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                    accessid = mid;
                                }else{
                                    accessid = MyApplication.deviceid;
                                }
                                sort.add("accessid"+accessid);
                                sign = KeySort.keyScort(sort);

                                quxiaojson = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_store.php?id="+item.pid+"&mid="+mids+"&method=cancel"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                                Message msg=new Message();
                                msg.what=4;
                                msg.obj = item;
                                handler.sendMessage(msg);
                            }
                        }.start();
        }

        public void clickDet(CollectionEntity item){
                    String name =  item.type;
                    if(name != null &&(name.equals("科讯早参"))){
                        Intent intent = new Intent(MycollectActivity.this, EarlyRef.class);
                        intent.putExtra("id", item.aid);
                        startActivity(intent);
                    }else if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                        Intent intent = new Intent(MycollectActivity.this, ZixunDetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", item.type);
                        intent.putExtra("pic", item.pic);
                        startActivity(intent);

                    }else{
                        if(name != null && (name.equals("人才")||name.equals("专家"))){
                            Intent intent = new Intent(MycollectActivity.this, XinFanAnCeShi.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MycollectActivity.this, DetailsActivity.class);
                            intent.putExtra("id", item.aid);
                            intent.putExtra("name", item.type);
                            intent.putExtra("pic", item.pic);
                            startActivity(intent);
                        }

                    }
        }
        private class ViewHolder{
            //资讯
            SwipeLayout s1;
            ImageView zx_img;
            TextView zx_title;
            TextView zx_description;
            TextView zx_lanyuan;
            TextView zx_update;
            TextView zx_zan;
            TextView zx_look;
            TextView cene1;
            AutoLinearLayout zx_layout;
            ImageView zx_zt;
            ImageView zixu_zhiding;
            ImageView zixu_tuijian;
            LinearLayout zx_line_zan;

            //项目
            SwipeLayout s2;
            TextView cene2;
            ImageView xm_img;
            TextView xm_title;
            TextView xm_linyu;
            TextView xm_dianwei;
            TextView xm_look;
            TextView xm_zan;
            AutoLinearLayout xm_layout;
            ImageView xm_zt;
            ImageView xiangmu_zhiding;
            ImageView xiangmu_tuijian;
            TextView xm_name;
            TextView xm_description;

            //政策
            SwipeLayout s3;
            TextView cene3;
            TextView zc_title;
            TextView zc_description;
            TextView zc_lingyu;
            TextView zc_look;
            TextView zc_zan;
            AutoLinearLayout zc_layout;
            ImageView zc_zt;
            ImageView zc_zhiding;
            ImageView zc_tuijian;

            //人才
            SwipeLayout s4;
            TextView cene4;
            TextView rc_uname;
            RoundImageView rc_img;
            TextView rc_zhicheng;
            TextView rc_linyu;
            TextView rc_title;
            TextView rc_zan;
            TextView rc_look;
            AutoLinearLayout rc_layout;
            ImageView rc_zt;
            ImageView rencai_zhiding;
            ImageView rencai_tuijian;
            TextView rc_text;
            ImageView rc_yuanshi;

            //设备
            SwipeLayout s5;
            TextView cene5;
            ImageView device_img;
            TextView device_title;
            TextView device_linyu;
            TextView device_description;
            TextView device_zan;
            TextView device_look;
            AutoLinearLayout device_layout;
            ImageView sb_zt;
            ImageView shebei_zhiding;
            ImageView shebei_tuijian;
            LinearLayout shebei_dianzan;

            //专利
            SwipeLayout s6;
            TextView cene6;
            TextView zhunli_title;
            TextView zhuanli_linyu;
            TextView zhuanli_jiduan;
            TextView zhuanli_look;
            TextView zhuanli_zan;
            AutoLinearLayout zhuanli_layout;
            ImageView zhuanli_zt;
            ImageView zhuanli_zhiding;
            ImageView zhuanli_tuijian;
            TextView zhuanli_description;
            LinearLayout zhuanli_line;

            //实验室
            SwipeLayout s7;
            TextView cene7;
            AutoLinearLayout librarys;
            TextView librarys_title;
            TextView librarys_linyu;
            ImageView librarys_img;
            TextView librarys_zan;
            TextView librarys_look;
            ImageView sys_zt;
            ImageView shiyanshi_zhiding;
            ImageView shiyanshi_tuijian;
            TextView datu_name;
            LinearLayout sys_dianzan;
            //三图
            SwipeLayout s9;
            TextView cene9;
            AutoLinearLayout tj_state_line;
            TextView tj_state_title;
            ImageView tj_state_img1;
            ImageView tj_state_img2;
            ImageView tj_state_img3;
            TextView tj_state_zan;
            TextView tj_state_click;
            ImageView santu_zhiding;
            ImageView santu_tuijian;
            TextView santu_name;
            ImageView tj_state_zt;
            LinearLayout state_zan;
            //test
            SwipeLayout s8;
            TextView cene8;
            AutoLinearLayout librarys1;
            TextView librarys_title1;
            TextView librarys_linyu1;
            ImageView librarys_img1;
            TextView librarys_zan1;
            TextView librarys_look1;
            ImageView sys_zt1;
            ImageView shiyanshi_zhiding1;
            ImageView shiyanshi_tuijian1;
            TextView datu_name1;
            LinearLayout sys_dianzan1;



        }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==4){
                    Gson g=new Gson();
                    ZanCode zanCode = g.fromJson(quxiaojson,ZanCode.class);
                    if(zanCode.code==1){
                        CollectionEntity item = (CollectionEntity)msg.obj;
                        maiDianCollection.deletebyaid(item.aid);
                        listCollections.remove(item);
                        mListAdapter.notifyDataSetChanged();
                        Toast.makeText(MycollectActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                    }else if(zanCode.code==-1){
                        Toast.makeText(MycollectActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }


    public void collect() {
        try {
            MyApplication.setAccessid();
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            String timestamp = System.currentTimeMillis()+"";
            String sign="";
            ArrayList<String> sort = new ArrayList<String>();
            sort.add("mid"+mid);
            sort.add("method"+"list");
            sort.add("timestamp"+timestamp);
            sort.add("version"+MyApplication.version);
            String accessid="";
            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(loginState.equals("1")){
                accessid = mid;
            }else{
                accessid = MyApplication.deviceid;
            }
            sort.add("accessid"+accessid);
            sign = KeySort.keyScort(sort);
            String url = "http://"+MyApplication.ip+"/api/arc_store.php?mid=" + mid + "&method=list&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
            Log.d("lizisong", url);
            quxiaojson = Okhttp.Myokhttpclient(url);
            Log.d("lizisong", "quxiaojson:"+quxiaojson);
            if (quxiaojson != null) {
                Gson g = new Gson();
                RetYuJianData retYuJianData = g.fromJson(quxiaojson, RetYuJianData.class);

//                                       List<YuJian> posts = data.posts;
                if (retYuJianData.code.equals("1")) {

                    List<YuJian> posts = retYuJianData.data.posts;


                    if (posts != null) {
                        listCollections = maiDianCollection.get();
                        if (posts.size() > 0) {
                            for (int i = 0; i < posts.size(); i++) {
                                boolean state = false;
                                YuJian pos = posts.get(i);
                                for (int j = 0; j < listCollections.size(); j++) {
                                    CollectionEntity tt = listCollections.get(j);
                                    if (tt.aid.equals(pos.aid)) {
                                        state = true;
                                        break;
                                    }
                                }
                                if(!state){
                                    CollectionEntity date = new CollectionEntity();
                                    date.type = pos.typename;
                                    date.pid = pos.id;
                                    date.pic = pos.litpic;
                                    if(pos.typename.equals("人才") || pos.typename.equals("专家")){
                                        date.title = pos.username;
                                    }else{
                                        date.title = pos.title;
                                    }
                                    date.aid = pos.aid;
                                    date.iscollect="0";
                                    date.isAdd = 0;
                                    date.image=pos.image;
                                    date.click = pos.click;
                                    date.imageState = pos.imageState;
                                    date.description = pos.description;
                                    if(pos.area_cate!= null){
                                        date.area_cate = pos.area_cate.getArea_cate1();
                                    }
                                    date.is_academician = pos.is_academician;

                                    maiDianCollection.insert(date);
                                }

                            }
                        }

                    }
                }

            }
            Message msg = Message.obtain();
            msg.what =0;
            handler.sendMessage(msg);
        }catch (Exception e){

        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                listCollections = maiDianCollection.get();
                Collections.reverse(listCollections);
                progress.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mListAdapter = new CollectionAdpter();
                mListView.setAdapter(mListAdapter);
                if(listCollections.size()==0){
                    shoucan_img.setVisibility(View.VISIBLE);
                    sc_img.setVisibility(View.VISIBLE);
                    gz_img.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                }else{
                    shoucan_img.setVisibility(View.GONE);
                    sc_img.setVisibility(View.GONE);
                    gz_img.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
//                if(mListAdapter != null){
//                    mListAdapter.notifyDataSetChanged();
//                }
            }
        }
    };
}
