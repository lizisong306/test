package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.Posts;
import entity.SendEntity;
import entity.XuQiuLiuCengDataBasic;
import entity.XuQiuLiuChengCell;
import entity.XuQiuLiuChengEntity;
import entity.XuQiuLiuChengFlow;
import entity.XuQiuLiuChengOperation;
import entity.XuQiuLiuChengResource;
import entity.XuQiuLiuChengSource;
import entity.XuQiuLiuChengdoc;
import entity.YouHuiJuanEntity;
import view.AutoTextView;
import view.MyAutoTextView;
import view.RefreshListView;
import view.RefreshListView1;
import view.RoundImageView;
import Util.KeyboardUtil;
import Util.*;
import view.ShapeImageView;

/**
 * Created by Administrator on 2019/6/12.
 */

public class XuQiuLiuCheng extends AutoLayoutActivity implements KeyboardUtil.OnSoftKeyboardChangeListener {
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    public boolean isKeyboardVisible;
    ImageView tipp;
    ImageView back;
    TextView send;
    RefreshListView1 listview;
    private int mLastFirstPostion;
    private int mLastFirstTop;
    private int touchSlop;
    TextView xuanzezhuanjia;
    XuQiuLiuChengEntity data;
    String id;
    ProgressBar progress;
    EditText pinglun;
    DisplayImageOptions options;
    View heart;
    LinearLayout daiziyuan;
    AutoTextView ziyuancontent;
    TextView ziyuandate,ziyuantype,wutu,rencai_title,rencai_lingyu,rank,xmtitle,lanyuan,ketizu_title,ketizu_lingyu,ketizurank,ziyuanjindu,unitname,unitadress;
    RelativeLayout zhuanjia,xiangmulay,keyanjigou,ketizu,ziyuanleixing,ziyuandeslay;
    RoundImageView rencai_img;
    ShapeImageView xianmgmu;
    ImageView img,ketizu_img,ziyuandes;
    List<XuQiuLiuChengFlow> showList = new ArrayList();
    Adapter adapter;
    ImageView bgTu,beijing;
    String mid;
    String agent_id;
    String flowid;
    String baseicid;
    String youhuijuannum;
    XuQiuLiuCengDataBasic basicdata;
    LinearLayout zhifulay;
    TextView wutu_rencai,keyong1;
    RoundImageView zhifurencai_img;
    TextView zhifurencai_title;
    TextView zhifurencai_lingyu,zhifurank,jiage,keyong,xiaoji,zongji,yiyouhui,gotopay;
    RelativeLayout xuanzeyouhuijuan;


    @Override
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
        setContentView(R.layout.xuqiuliucheng);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        options = ImageLoaderUtils.initOptions();
        PayConfirmActivity.youhuijuanprice="";
        PayConfirmActivity.couponid="";
        heart = View.inflate(XuQiuLiuCheng.this,R.layout.xuqiuliuchengheart,null);
        daiziyuan = heart.findViewById(R.id.daiziyuan);
        ziyuandeslay = heart.findViewById(R.id.ziyuandeslay);
        ziyuandate = heart.findViewById(R.id.ziyuandate);
        ziyuantype = heart.findViewById(R.id.ziyuantype);
        beijing =  heart.findViewById(R.id.beijing);
        ziyuancontent = heart.findViewById(R.id.ziyuancontent);
        rencai_title =  heart.findViewById(R.id.rencai_title);
        rencai_lingyu = heart.findViewById(R.id.rencai_lingyu);
        rank = heart.findViewById(R.id.rank);
        xmtitle = heart.findViewById(R.id.xmtitle);
        lanyuan = heart.findViewById(R.id.lanyuan);
        wutu = heart.findViewById(R.id.wutu);
        ketizu_title = heart.findViewById(R.id.ketizu_title);
        ketizu_lingyu = heart.findViewById(R.id.ketizu_lingyu);
        ketizurank = heart.findViewById(R.id.ketizurank);
        ziyuanjindu = heart.findViewById(R.id.ziyuanjindu);
        ziyuandes = heart.findViewById(R.id.ziyuandes);
        zhuanjia = heart.findViewById(R.id.zhuanjia);
        xiangmulay = heart.findViewById(R.id.xiangmulay);
        keyanjigou = heart.findViewById(R.id.keyanjigou);
        ketizu = heart.findViewById(R.id.ketizu);
        rencai_img = heart.findViewById(R.id.rencai_img);
        xianmgmu = heart.findViewById(R.id.xianmgmu);
        img = heart.findViewById(R.id.img);
        unitname = heart.findViewById(R.id.unitname);
        unitadress = heart.findViewById(R.id.unitadress);
        ketizu_img = heart.findViewById(R.id.ketizu_img);
        ziyuanleixing = heart.findViewById(R.id.ziyuanleixing);
        tipp = (ImageView)findViewById(R.id.tipp);
        back = (ImageView)findViewById(R.id.back);
        send = (TextView) findViewById(R.id.send);
        bgTu = (ImageView)findViewById(R.id.bgtuu);
        zhifulay = (LinearLayout)findViewById(R.id.zhifulay);
        wutu_rencai = (TextView)findViewById(R.id.wutu_rencai);
        zhifurencai_img = (RoundImageView)findViewById(R.id.rencai_img);
        zhifurencai_lingyu = (TextView)findViewById(R.id.rencai_lingyu);
        zhifurank = (TextView)findViewById(R.id.rank);
        zhifurencai_title = (TextView)findViewById(R.id.rencai_title);
        keyong1 = (TextView)findViewById(R.id.keyong1);
        jiage = (TextView)findViewById(R.id.jiage);
        keyong = (TextView)findViewById(R.id.keyong);
        xiaoji = (TextView)findViewById(R.id.xiaoji);
        zongji = (TextView)findViewById(R.id.zongji);
        yiyouhui = (TextView)findViewById(R.id.yiyouhui);
        gotopay = (TextView)findViewById(R.id.gotopay);
        xuanzeyouhuijuan = (RelativeLayout)findViewById(R.id.xuanzeyouhuijuan);
        bgTu.setVisibility(View.VISIBLE);
        listview = (RefreshListView1)findViewById(R.id.listview);
        listview.setPullDownToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                bgTu.setVisibility(View.VISIBLE);
                beijing.setVisibility(View.GONE);
                getJson(id);
                listview.setPullDownToRefreshFinish();
            }
        });
        xuanzezhuanjia = (TextView)findViewById(R.id.xuanzezhuanjia);
        progress = (ProgressBar)findViewById(R.id.progress);
        pinglun = (EditText)findViewById(R.id.pinglun);
        pinglun.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String txt = pinglun.getText().toString();
                    if(txt == null || txt.equals("")){
                        Toast.makeText(XuQiuLiuCheng.this, "发送能容不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(event.getAction()==KeyEvent.ACTION_UP){
                        commit(txt, mid, agent_id);
                    }
                }
                return false;
            }
        });
        pinglun.setFilters(new InputFilter[]{new MaxTextLengthFilter(140)});

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                tipp.setVisibility(View.GONE);
                hintKbTwo();
                String content = pinglun.getText().toString().replace(" ","");
                if(content != null && !content.equals("")){
                    commit(content, mid, agent_id);
                }
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentTop;
                View firstChildView = view.getChildAt(0);
                if (firstChildView != null) {
                    currentTop = view.getChildAt(0).getTop();
                }else {
                    //ListView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
                    return;
                }
                if(currentTop == 0 && firstVisibleItem ==0){
                    bgTu.setVisibility(View.VISIBLE);
                    beijing.setVisibility(View.GONE);
                }
                //判断上次可见的第一个位置和这次可见的第一个位置
                if (firstVisibleItem != mLastFirstPostion) {
                    //不是同一个位置
                    if (firstVisibleItem > mLastFirstPostion) {
                        //TODO do down
                        bgTu.setVisibility(View.GONE);
                        beijing.setVisibility(View.VISIBLE);
                    } else {
                        //TODO do up
                    }
                    mLastFirstTop = currentTop;
                } else {
                    //是同一个位置
                    if(Math.abs(currentTop - mLastFirstTop) > touchSlop){
                        //避免动作执行太频繁或误触，加入touchSlop判断，具体值可进行调整
                        if (currentTop > mLastFirstTop) {
                            //TODO do up

                        } else if (currentTop < mLastFirstTop) {
                            //TODO do down
                            bgTu.setVisibility(View.GONE);
                            beijing.setVisibility(View.VISIBLE);

                        }
                        mLastFirstTop = currentTop;
                    }
                }
                mLastFirstPostion = firstVisibleItem;
            }
        });
        //获取屏幕高度
//        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        mOnGlobalLayoutListener = KeyboardUtil.observeSoftKeyboard(this, this);
        tipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipp.setVisibility(View.GONE);
                zhifulay.setVisibility(View.GONE);
                hintKbTwo();
            }
        });
        progress.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
    }
    @Override
    protected void onResume() {
        super.onResume();
        getJson(id);
        Log.d("lizisong", "优惠价格:"+PayConfirmActivity.youhuijuanprice);
        Log.d("lizisong", "优惠卷id:"+PayConfirmActivity.couponid);
        if(PayConfirmActivity.youhuijuanprice != null && !PayConfirmActivity.youhuijuanprice.equals("")){
            Log.d("lizisong", "1");
            yiyouhui.setText("已优惠：￥"+PayConfirmActivity.youhuijuanprice);
            int p1 = Integer.parseInt(PayConfirmActivity.youhuijuanprice);
            int p2 = Integer.parseInt(price);
            keyong1.setText("￥：-"+PayConfirmActivity.youhuijuanprice);
            keyong1.setVisibility(View.VISIBLE);
            keyong.setVisibility(View.GONE);
            Log.d("lizisong", "2");
            if(p2-p1 >1){
                zongji.setText("￥"+(p2-p1));
                xiaoji.setText("￥"+(p2-p1));
            }else{
                zongji.setText("￥"+1);
                xiaoji.setText("￥"+1);
            }
            Log.d("lizisong", "3");

        }else{
            Log.d("lizisong", "4");
            keyong1.setVisibility(View.GONE);
            PayConfirmActivity.couponid="";
            zongji.setText("￥"+price);
            yiyouhui.setText("优惠￥"+"0");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtil.removeSoftKeyboardObserver(XuQiuLiuCheng.this,mOnGlobalLayoutListener);
    }

    @Override
    public void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible) {
        isKeyboardVisible = visible;
        if(isKeyboardVisible){
            tipp.setVisibility(View.VISIBLE);
        }else{
            tipp.setVisibility(View.GONE);
        }
    }
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void getJson(String id){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("method", "detail");
        map.put("identity","0");
        map.put("id",id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,1);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if(msg.what == 1){
//                    bgTu.setVisibility(View.GONE);
//                    beijing.setVisibility(View.VISIBLE);
                    listview.setPullDownToRefreshFinish();
                    progress.setVisibility(View.GONE);
                    String ret = (String)msg.obj;
                    if(ret != null){
                        JSONObject jsonObject = new JSONObject(ret);
                        int code = jsonObject.getInt("code");
                        if(code == 1){
                            String message = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            if(data != null){
                                JSONObject jsonData = new JSONObject(data);
                                if(jsonData != null){
                                    JSONObject basic = jsonData.getJSONObject("basic");
                                    if(basic != null){
                                        basicdata = new XuQiuLiuCengDataBasic();
                                        basicdata.typeid = basic.getString("typeid");
                                        basicdata.agent_mid = basic.getString("agent_mid");
                                        basicdata.id = basic.getString("id");
                                        baseicid = basicdata.id;
                                        basicdata.mid = basic.getString("mid");
                                        basicdata.aid = basic.getString("aid");
                                        basicdata.state = basic.getString("state");
                                        basicdata.typename = basic.getString("typename");
                                        basicdata.litpic = basic.getString("litpic");
                                        basicdata.title = basic.getString("title");
                                        basicdata.unit = basic.getString("unit");
                                        basicdata.area_cate = basic.getString("area_cate");
                                        basicdata.description = basic.getString("description");
                                        basicdata.pubdate = basic.getString("pubdate");
                                        basicdata.ranks = basic.getString("ranks");
                                        basicdata.content = basic.getString("content");
                                        basicdata.uname = basic.getString("uname");
                                        basicdata.tel = basic.getString("tel");
                                    }
                                    JSONArray flow = null;
                                    try {
                                        flow = jsonData.getJSONArray("flow");
                                    } catch (JSONException a) {

                                    } catch (Exception e) {

                                    }
                                    if(flow != null){
                                        if(flow.length() >0){
                                            showList.clear();
                                            for(int i=0; i<flow.length();i++){
                                                JSONObject item = flow.getJSONObject(i);
                                                XuQiuLiuChengFlow pos = new XuQiuLiuChengFlow();
                                                pos.id = item.getString("id");
                                                pos.state = item.getString("state");
                                                pos.time = item.getString("time");
                                                String cell = item.getString("cell");
                                                if(cell != null && !cell.equals("")){
                                                    pos.cell = new XuQiuLiuChengCell();
                                                    JSONObject cellObject = new JSONObject(cell);
                                                    if(cellObject != null){
                                                        pos.cell.content = cellObject.getString("content");
                                                        String document  = cellObject.getString("document");
                                                        if(document != null && !document.equals("")){
                                                            JSONObject documentObject = new JSONObject(document);
                                                            pos.cell.document = new XuQiuLiuChengdoc();
                                                            pos.cell.document.document_img = documentObject.getString("document_img");
                                                            pos.cell.document.document_time = documentObject.getString("document_time");
                                                            pos.cell.document.document_url  = documentObject.getString("document_url");
                                                            pos.cell.document.document_title = documentObject.getString("document_title");
                                                        }
                                                        String source = cellObject.getString("source");
                                                        if(source != null && !source.equals("")){
                                                            JSONObject sourceObject = new JSONObject(source);
                                                            pos.cell.source = new XuQiuLiuChengSource();
                                                            pos.cell.source.agent_mid = sourceObject.getString("agent_mid");
                                                            pos.cell.source.uname = sourceObject.getString("uname");
                                                            try {
                                                                pos.cell.source.tel = sourceObject.getString("tel");
                                                            } catch (JSONException a) {

                                                            } catch (Exception e) {

                                                            }
                                                            pos.cell.source.agent_img = sourceObject.getString("agent_img");
                                                            pos.cell.source.str = sourceObject.getString("str");
                                                        }
                                                        String resource = cellObject.getString("resource");
                                                        if(resource != null && !resource.equals("")){
                                                            JSONObject resourceObject = new JSONObject(resource);
                                                            pos.cell.resource = new XuQiuLiuChengResource();
                                                            pos.cell.resource.aid = resourceObject.getString("aid");
                                                            pos.cell.resource.title = resourceObject.getString("title");
                                                            pos.cell.resource.unit  = resourceObject.getString("unit");
                                                            pos.cell.resource.area_cate = resourceObject.getString("area_cate");
                                                            pos.cell.resource.litpic = resourceObject.getString("litpic");
                                                            pos.cell.resource.ranks  = resourceObject.getString("ranks");
                                                            pos.cell.resource.price  = resourceObject.getString("price");
                                                        }
                                                        String operation = cellObject.getString("operation");
                                                        if(operation != null && !operation.equals("")){
                                                            JSONArray operationObject = new JSONArray(operation);
                                                            if(operationObject != null && operationObject.length() >0){
                                                                  pos.cell.operation = new ArrayList<>();
                                                                  for (int j=0; j<operationObject.length();j++){
                                                                        JSONObject obj = operationObject.getJSONObject(j);
                                                                        XuQiuLiuChengOperation opt = new XuQiuLiuChengOperation();
                                                                        opt.type = obj.getString("type");
                                                                        opt.str = obj.getString("str");
                                                                        pos.cell.operation.add(opt);
                                                                  }
                                                            }
                                                        }
                                                    }
                                                }
                                                showList.add(pos);
                                            }
                                            XuQiuLiuChengFlow item = new XuQiuLiuChengFlow();
                                            showList.add(item);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(basicdata != null){
                               long sys = (System.currentTimeMillis()-Long.parseLong(basicdata.pubdate)*1000)/1000;
                               String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(basicdata.pubdate)*1000), sys+"");
                               ziyuandate.setText(time);
                               ziyuantype.setText("需求类型："+basicdata.typename);
                               ziyuancontent.setText(basicdata.content.replaceAll("\r\n", "").replaceAll("\n", ""));
                               mid=basicdata.mid;
                               agent_id = basicdata.agent_mid;
                               if(basicdata.typeid != null){
                                   if(basicdata.typeid.equals("4")){
                                       ziyuanleixing.setVisibility(View.VISIBLE);
                                       zhuanjia.setVisibility(View.VISIBLE);
                                       if(basicdata.litpic == null || basicdata.litpic.equals("")){
                                           rencai_img.setVisibility(View.GONE);
                                           wutu.setVisibility(View.VISIBLE);
                                           if(basicdata.title != null && !basicdata.title.equals("")){
                                               String txt = basicdata.title.substring(0,1);
                                               wutu.setText(txt);
                                           }
                                       }else{
                                           rencai_img.setVisibility(View.VISIBLE);
                                           wutu.setVisibility(View.GONE);
                                           ImageLoader.getInstance().displayImage(basicdata.litpic
                                                   , rencai_img, options);
                                       }
                                       rencai_title.setText(basicdata.title);
//                                       rencai_title.setTypeface(MyApplication.Medium);
                                       rencai_lingyu.setText(basicdata.area_cate);
                                       rank.setText(basicdata.ranks);
//                                       zhuanjia.setOnClickListener(new View.OnClickListener() {
//                                           @Override
//                                           public void onClick(View v) {
//
//                                           }
//                                       });
                                       zhuanjia.setOnClickListener(new NoDoubleClick() {
                                           @Override
                                           public void Click(View v) {
                                               Intent intent = new Intent(XuQiuLiuCheng.this,XinFanAnCeShi.class);
                                               intent.putExtra("aid",basicdata.aid);
                                               startActivity(intent);
                                           }
                                       });
                                   }else if(basicdata.typeid.equals("2")){
                                       ziyuanleixing.setVisibility(View.VISIBLE);
                                       xiangmulay.setVisibility(View.VISIBLE);
                                       if(basicdata.litpic == null || basicdata.litpic.equals("")){
                                           xianmgmu.setImageResource(R.mipmap.information_placeholder);
                                       }else{
                                           ImageLoader.getInstance().displayImage(basicdata.litpic
                                                   , xianmgmu, options);
                                       }
                                       xmtitle.setText(basicdata.title);
                                       lanyuan.setText(basicdata.area_cate);

                                       xiangmulay.setOnClickListener(new NoDoubleClick() {
                                           @Override
                                           public void Click(View v) {
                                               Intent intent = new Intent(XuQiuLiuCheng.this,NewProjectActivity.class);
                                               intent.putExtra("aid",basicdata.aid);
                                               startActivity(intent);
                                           }
                                       });
                                   }else if(basicdata.typeid.equals("7")){
                                       ziyuanleixing.setVisibility(View.VISIBLE);
                                       xiangmulay.setVisibility(View.VISIBLE);
                                       if(basicdata.litpic == null || basicdata.litpic.equals("")){
                                           xianmgmu.setImageResource(R.mipmap.information_placeholder);
                                       }else{
                                           ImageLoader.getInstance().displayImage(basicdata.litpic
                                                   , xianmgmu, options);
                                       }
                                       xmtitle.setText(basicdata.title);
                                       lanyuan.setText(basicdata.area_cate);
                                       xiangmulay.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(XuQiuLiuCheng.this, SheBeiDeilActivity.class);
                                               intent.putExtra("aid", basicdata.aid);
                                               startActivity(intent);
//                                               Toast.makeText(XuQiuLiuCheng.this, "暂不支持该类型的详情", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }else if(basicdata.typeid.equals("8") || basicdata.typeid.equals("16")){
                                       ziyuanleixing.setVisibility(View.VISIBLE);
                                       ketizu.setVisibility(View.VISIBLE);
                                       keyanjigou.setVisibility(View.VISIBLE);
                                       ImageLoader.getInstance().displayImage(basicdata.litpic
                                               ,  img, options);
                                       unitname.setText(basicdata.title);
                                       unitadress.setText(basicdata.area_cate);
                                       keyanjigou.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Toast.makeText(XuQiuLiuCheng.this, "暂不支持该类型的详情", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                               }else{
                                   ziyuanleixing.setVisibility(View.GONE);
                               }
                               if(basicdata.uname != null && !basicdata.uname.equals("")){
                                   ziyuanjindu.setText("专属经济人:"+basicdata.uname);
                                   ziyuanjindu.setTextColor(0xff3e3e3e);
                               }else{
                                   ziyuanjindu.setText("全力为您匹配...");
                                   ziyuanjindu.setTextColor(0xfff0a066);
                               }
                        ziyuandeslay.setOnClickListener(new NoDoubleClick() {
                            @Override
                            public void Click(View v) {
                                if(basicdata.tel != null && !basicdata.tel.equals("")){
                                    Uri uri = Uri.parse("tel:"+basicdata.tel);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                    startActivity(intent);
                                }
                            }
                        });
                 }
                  listview.removeHeaderView(heart);
                  listview.addHeaderView(heart);
                  xuanzezhuanjia.setVisibility(View.GONE);
                  if(adapter == null){
                     adapter = new Adapter();
                     listview.setAdapter(adapter);
                  }else{
                      adapter.notifyDataSetChanged();
                  }

                }else if(msg.what == 2){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    SendEntity send = gson.fromJson(ret, SendEntity.class);
                    if(send != null){
                        if(send.code == 1){
                            listview.setSelection(0);
                            pinglun.setText("");
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                            getJson(id);
                        }else{
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(msg.what == 3){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    SendEntity send = gson.fromJson(ret, SendEntity.class);
                    if(send != null){
                        if(send.code == 1){
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                            getJson(id);
                        }else{
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(msg.what == 4){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    SendEntity send = gson.fromJson(ret, SendEntity.class);
                    if(send != null){
                        if(send.code == 1){
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                            getJson(id);
                        }else{
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(msg.what == 5){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    SendEntity send = gson.fromJson(ret, SendEntity.class);
                    if(send != null){
                        if(send.code == 1){
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                            getJson(id);
                        }else{
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(msg.what == 6){
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    SendEntity send = gson.fromJson(ret, SendEntity.class);
                    if(send != null){
                        if(send.code == 1){
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                            getJson(id);
                        }else{
                            Toast.makeText(XuQiuLiuCheng.this,send.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(msg.what == 7){
                    String ret = (String)msg.obj;
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    YouHuiJuanEntity data = gson.fromJson(ret, YouHuiJuanEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            youhuijuannum = data.data.num;
                        }
                    }
                    zhifulay.setVisibility(View.VISIBLE);
                    keyong1.setVisibility(View.GONE);
                    tipp.setVisibility(View.VISIBLE);
                    if(zhifuimgurl == null || zhifuimgurl.equals("")){
                        zhifurencai_img.setVisibility(View.GONE);
                        wutu_rencai.setVisibility(View.VISIBLE);
                        if(zhiftitle != null && !zhiftitle.equals("")){
                            String txt = zhiftitle.substring(0,1);
                            wutu_rencai.setText(txt);
                        }
                    }else{
                        zhifurencai_img.setVisibility(View.VISIBLE);
                        wutu_rencai.setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(zhifuimgurl
                                , zhifurencai_img, options);
                    }
                    zhifurencai_title.setText(zhiftitle);
                    zhifurencai_lingyu.setText(zhiflingye);
                    zhifurank.setText(zhifuranks);

                    jiage.setText("￥"+price);
                    zongji.setText("￥"+price);
                    xiaoji.setText("￥"+price);
                    keyong.setText(youhuijuannum);
                    if(youhuijuannum != null && !youhuijuannum.equals("")){
                        if(Integer.parseInt(youhuijuannum)>0){
                            keyong.setVisibility(View.VISIBLE);
                        }else{
                            keyong.setVisibility(View.GONE);
                        }
                    }
                    xuanzeyouhuijuan.setOnClickListener(new NoDoubleClick() {
                        @Override
                        public void Click(View v) {
                            if(youhuijuannum != null && !youhuijuannum.equals("")){
                                if(Integer.parseInt(youhuijuannum)>0){
                                    Intent intent = new Intent(XuQiuLiuCheng.this, XuanZeYouHuiJuanActivity.class);
                                    intent.putExtra("typeid","4");
                                    intent.putExtra("aid", aid);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    gotopay.setOnClickListener(new NoDoubleClick() {
                        @Override
                        public void Click(View v) {
                            zhifulay.setVisibility(View.GONE);
                            tipp.setVisibility(View.GONE);
                            Intent intent = new Intent(XuQiuLiuCheng.this, PaySDkCallActivity.class);
                            intent.putExtra("aid", aid);
                            intent.putExtra("id", resid);
                            intent.putExtra("ranks", zhifuranks);
                            intent.putExtra("type",0);
                            intent.putExtra("couponid", PayConfirmActivity.couponid);
                            startActivity(intent);
                        }
                    });
                }
            }catch (Exception e){

            }
        }
    };
    String zhiftitle,zhifuranks,zhiflingye,zhifuimgurl,aid,resid,price;

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
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
                convertView = View.inflate(XuQiuLiuCheng.this, R.layout.xuqiuliuchengadapter, null);
                holdView.date = convertView.findViewById(R.id.date);
                holdView.daiziyuan = convertView.findViewById(R.id.daiziyuan);
                holdView.wutu = convertView.findViewById(R.id.wutu);
                holdView.jingjiren_title = convertView.findViewById(R.id.jingjiren_title);
                holdView.jingjiren_lingyu = convertView.findViewById(R.id.jingjiren_lingyu);
                holdView.jingjirenrank = convertView.findViewById(R.id.jingjirenrank);
                holdView.ziyuancontent = convertView.findViewById(R.id.ziyuancontent);
                holdView.rencai_title = convertView.findViewById(R.id.rencai_title);
                holdView.rencai_lingyu = convertView.findViewById(R.id.rencai_lingyu);
                holdView.rank = convertView.findViewById(R.id.rank);
                holdView.xmtitle = convertView.findViewById(R.id.xmtitle);
                holdView.lanyuan = convertView.findViewById(R.id.lanyuan);
                holdView.unitname = convertView.findViewById(R.id.unitname);
                holdView.unitadress = convertView.findViewById(R.id.unitadress);
                holdView.ketizu_title = convertView.findViewById(R.id.ketizu_title);
                holdView.ketizu_lingyu = convertView.findViewById(R.id.ketizu_lingyu);
                holdView.ziyuanjindu = convertView.findViewById(R.id.ziyuanjindu);
                holdView.ziyuandes = convertView.findViewById(R.id.ziyuandes);
                holdView.cencel = convertView.findViewById(R.id.cencel);
                holdView.tuijianzhuanja = convertView.findViewById(R.id.tuijianzhuanja);
                holdView.yijieshou = convertView.findViewById(R.id.yijieshou);
                holdView.ziyuandateandtype = convertView.findViewById(R.id.ziyuandateandtype);
                holdView.jingjiren = convertView.findViewById(R.id.jingjiren);
                holdView.ziyuanleixing = convertView.findViewById(R.id.ziyuanleixing);
                holdView.zhuanjia = convertView.findViewById(R.id.zhuanjia);
                holdView.xiangmulay = convertView.findViewById(R.id.xiangmulay);
                holdView.keyanjigou = convertView.findViewById(R.id.keyanjigou);
                holdView.ketizu = convertView.findViewById(R.id.ketizu);
                holdView.jingjiren_img = convertView.findViewById(R.id.jingjiren_img);
                holdView.rencai_img = convertView.findViewById(R.id.rencai_img);
                holdView.wutu_rencai = convertView.findViewById(R.id.wutu_rencai);
                holdView.xianmgmu = convertView.findViewById(R.id.xianmgmu);
                holdView.img = convertView.findViewById(R.id.img);
                holdView.ketizu_img = convertView.findViewById(R.id.ketizu_img);
                holdView.ziyuanhandler = convertView.findViewById(R.id.ziyuanhandler);
                holdView.pdf = convertView.findViewById(R.id.pdf);
                holdView.pingjia = convertView.findViewById(R.id.pingjia);
                holdView.feichangmanyi = convertView.findViewById(R.id.feichangmanyi);
                holdView.manyi = convertView.findViewById(R.id.manyi);
                holdView.bumanymanyi = convertView.findViewById(R.id.bumanymanyi);
                holdView.pdf_img = convertView.findViewById(R.id.pdf_img);
                holdView.pdf_title = convertView.findViewById(R.id.pdf_title);
                holdView.pdf_lingyu = convertView.findViewById(R.id.pdf_lingyu);
                holdView.dituan = convertView.findViewById(R.id.dituan);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            final XuQiuLiuChengFlow item = showList.get(position);
            try {
                holdView.ziyuanhandler.setVisibility(View.GONE);
                holdView.cencel.setVisibility(View.GONE);
                holdView.tuijianzhuanja.setVisibility(View.GONE);
                holdView.yijieshou.setVisibility(View.GONE);
                holdView.pingjia.setVisibility(View.GONE);
                holdView.yijieshou.setVisibility(View.GONE);
                holdView.pdf.setVisibility(View.GONE);
                if(item.time != null){
                    long sys = (System.currentTimeMillis()-Long.parseLong(item.time)*1000)/1000;
                    String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), sys+"");
                    holdView.date.setText(time);
                }
                if(item.cell != null){
                    holdView.daiziyuan.setVisibility(View.VISIBLE);
                    holdView.dituan.setVisibility(View.GONE);
                    if(item.cell.source != null){
                        holdView.ziyuandateandtype.setVisibility(View.VISIBLE);
                        if(item.cell.source.agent_img == null || ((item.cell.source.agent_img != null ) && item.cell.source.agent_img.equals(""))){
                            holdView.jingjiren_img.setImageResource(R.mipmap.bigrencaibg);
                            holdView.wutu.setVisibility(View.VISIBLE);
                            holdView.jingjiren_img.setVisibility(View.GONE);
                            if(item.cell.source.uname != null && !item.cell.source.uname.equals("")){
                                 String str = item.cell.source.uname.substring(0,1);
                                 holdView.wutu.setText(str);
                            }
                        }else {
                            holdView.wutu.setVisibility(View.GONE);
                            holdView.jingjiren_img.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(item.cell.source.agent_img
                                    , holdView.jingjiren_img, options);
                        }
                        holdView.jingjiren_title.setText(item.cell.source.uname);
                        holdView.jingjiren_lingyu.setText(item.cell.source.str);
                    }else{
                        holdView.ziyuandateandtype.setVisibility(View.GONE);
                    }

                    if(item.cell.content == null || ((item.cell.content != null ) && item.cell.content.equals(""))){
                        holdView.ziyuancontent.setVisibility(View.GONE);
                    }else {
                        holdView.ziyuancontent.setVisibility(View.VISIBLE);
                        holdView.ziyuancontent.setText(item.cell.content);
                    }

                    if(item.cell.resource == null){
                        holdView.ziyuanleixing.setVisibility(View.GONE);
                    }else{
                        holdView.ziyuanleixing.setVisibility(View.VISIBLE);
                        holdView.zhuanjia.setVisibility(View.VISIBLE);
                        if(item.cell.resource.litpic == null || ((item.cell.resource.litpic != null ) && item.cell.resource.litpic.equals(""))){
                            holdView.rencai_img.setImageResource(R.mipmap.bigrencaibg);
                            holdView.rencai_img.setVisibility(View.GONE);
                            holdView.wutu_rencai.setVisibility(View.VISIBLE);
                            if(item.cell.resource.title != null && !item.cell.resource.title.equals("")){
                                String txt = item.cell.resource.title.substring(0,1);
                                holdView.wutu_rencai.setText(txt);
                            }
                        }else {
                            holdView.rencai_img.setVisibility(View.VISIBLE);
                            holdView.wutu_rencai.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(item.cell.resource.litpic
                                    , holdView.rencai_img, options);
                        }
                        holdView.rencai_title.setText(item.cell.resource.title);
                        holdView.rank.setText(item.cell.resource.ranks);
                        holdView.rencai_lingyu.setText(item.cell.resource.unit);
                        holdView.zhuanjia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(XuQiuLiuCheng.this,XinFanAnCeShi.class);
                                intent.putExtra("aid",item.cell.resource.aid);
                                startActivity(intent);
                            }
                        });
                    }

                    if(item.cell.document == null){
                        holdView.pdf.setVisibility(View.GONE);
                    }else{
                        holdView.pdf.setVisibility(View.VISIBLE);
                        if(item.cell.document.document_img == null || ((item.cell.document.document_img != null ) && item.cell.document.document_img.equals(""))){
                            holdView.pdf_img.setImageResource(R.mipmap.bigrencaibg);
                        }else {
                            ImageLoader.getInstance().displayImage(item.cell.document.document_img
                                    , holdView.pdf_img, options);
                        }
                        holdView.pdf_title.setText(item.cell.document.document_title);
                        if(item.cell.document.document_time != null && !item.cell.document.document_time.equals("")){
                                 holdView.pdf_lingyu.setText(TimeUtils.getStrMonthAndDataTime(item.cell.document.document_time));
                        }else{
                            holdView.pdf_lingyu.setText(" ");
                        }
                        holdView.pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(XuQiuLiuCheng.this, PDFDemo.class);
                                intent.putExtra("url", item.cell.document.document_url);
                                intent.putExtra("title", item.cell.document.document_title);
                                startActivity(intent);
                            }
                        });
                    }

                    if(item.cell.operation != null){
                        if(item.cell.operation.size() == 3){
                            XuQiuLiuChengOperation item0 =item.cell.operation.get(0);
                            XuQiuLiuChengOperation item1 =item.cell.operation.get(1);
                            XuQiuLiuChengOperation item2 =item.cell.operation.get(2);
                            if(item0 != null && item1 != null && item2 != null){
                                    holdView.pingjia.setVisibility(View.VISIBLE);
                                    holdView.feichangmanyi.setText(item0.str);
                                    holdView.manyi.setText(item1.str);
                                    holdView.bumanymanyi.setText(item2.str);
                                    holdView.feichangmanyi.setOnClickListener(new NoDoubleClick() {
                                        @Override
                                        public void Click(View v) {
                                            menyidu(item.id, "1");
                                        }
                                    });
                                    holdView.manyi.setOnClickListener(new NoDoubleClick() {
                                        @Override
                                        public void Click(View v) {
                                            menyidu(item.id, "2");
                                        }
                                    });
                                    holdView.bumanymanyi.setOnClickListener(new NoDoubleClick() {
                                        @Override
                                        public void Click(View v) {
                                            menyidu(item.id, "4");
                                        }
                                    });

                            }else{
                                holdView.pingjia.setVisibility(View.GONE);
                            }

                        }else if(item.cell.operation.size() ==2){
                            XuQiuLiuChengOperation item0 =item.cell.operation.get(0);
                            XuQiuLiuChengOperation item1 =item.cell.operation.get(1);
                            holdView.ziyuanjindu.setText(item0.str);
                            holdView.ziyuandes.setText(item1.str);
                            if(item0 != null && item1 != null){
                                if(item0.type.equals("jieShouZhuanJia") && item1.type.equals("juJueZhuanJia")){//这个是支付
                                    holdView.ziyuanhandler.setVisibility(View.VISIBLE);
                                }else{
                                    holdView.ziyuanhandler.setVisibility(View.GONE);
                                }

                                holdView.ziyuanjindu.setOnClickListener(new NoDoubleClick() {
                                    @Override
                                    public void Click(View v) {
                                        progress.setVisibility(View.VISIBLE);
                                        zhiftitle = item.cell.resource.title;
                                        zhiflingye = item.cell.resource.unit;
                                        zhifuimgurl = item.cell.resource.litpic;
                                        zhifuranks = item.cell.resource.ranks;
                                        price = item.cell.resource.price;
                                        aid = item.cell.resource.aid;
                                        resid = item.id;
                                        youhuijuan(item.cell.resource.aid, "4");
                                    }
                                });

                                holdView.ziyuandes.setOnClickListener(new NoDoubleClick() {
                                    @Override
                                    public void Click(View v) {
                                        jujue(item.id);
                                    }
                                });
                            }
                        }else{
                            if(item.cell.operation.size() ==1){
                                XuQiuLiuChengOperation item0 =item.cell.operation.get(0);
                                if(item0 != null){
                                    if(item0.type.equals("caoZuoHuiZhi")){
                                        holdView.yijieshou.setVisibility(View.VISIBLE);
                                        holdView.yijieshou.setText(item0.str);
                                    }else{
                                        holdView.yijieshou.setVisibility(View.GONE);
                                    }

                                    if(item0.type.equals("quXiaoYaoQing")){
                                        holdView.cencel.setVisibility(View.VISIBLE);
                                        holdView.cencel.setText(item0.str);
                                        holdView.cencel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                flowid = item.id;
                                                cenel();
                                            }
                                        });
                                    }else{
                                        holdView.cencel.setVisibility(View.GONE);
                                    }

                                    if(item0.type.equals("faSongYueJianBaoGao")){
                                        holdView.tuijianzhuanja.setVisibility(View.VISIBLE);
                                        holdView.tuijianzhuanja.setText(item0.str);
                                        holdView.tuijianzhuanja.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  flowid = item.id;
                                                  fasongbaogao(data.data.basic.id, flowid);
                                               }
                                            }
                                        );
                                    }else if(item0.type.equals("xiangQiYeTuiJian")){
                                        holdView.tuijianzhuanja.setVisibility(View.VISIBLE);
                                        holdView.tuijianzhuanja.setText(item0.str);
                                        holdView.tuijianzhuanja.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                flowid = item.id;
                                                tuijianzhuanjia();
                                            }
                                        });
                                    }else{
                                        holdView.tuijianzhuanja.setVisibility(View.GONE);
                                    }
                                }

                            }
                        }
                    }else{
                        holdView.ziyuanhandler.setVisibility(View.GONE);
                        holdView.cencel.setVisibility(View.GONE);
                        holdView.tuijianzhuanja.setVisibility(View.GONE);
                        holdView.yijieshou.setVisibility(View.GONE);
                    }

                }else{
                    holdView.daiziyuan.setVisibility(View.GONE);
                    holdView.dituan.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            return convertView;
        }
        class HoldView{
            MyAutoTextView ziyuancontent;
            TextView date,jingjiren_title,jingjiren_lingyu,jingjirenrank,rencai_title,rencai_lingyu,rank,xmtitle,lanyuan,dituan,wutu,wutu_rencai;
            TextView unitname,unitadress,ketizu_title,ketizu_lingyu,ziyuanjindu,ziyuandes,cencel,tuijianzhuanja,yijieshou,pdf_title,pdf_lingyu;
            RelativeLayout ziyuandateandtype,jingjiren,ziyuanleixing,zhuanjia,xiangmulay,keyanjigou,ketizu,pdf;
            RoundImageView jingjiren_img,rencai_img;
            ImageView img,ketizu_img,pdf_img;
            ShapeImageView xianmgmu;
            LinearLayout ziyuanhandler,daiziyuan;
            RelativeLayout pingjia;
            TextView feichangmanyi,manyi,bumanymanyi;
        }
    }

     class MaxTextLengthFilter implements InputFilter {

        private int mMaxLength;
        //构造方法中传入最多能输入的字数
        public MaxTextLengthFilter(int max) {
            mMaxLength = max;
        }

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int keep = mMaxLength - (dest.length() - (dend - dstart));
            if (keep < (end - start)) {
                Toast.makeText(XuQiuLiuCheng.this,"最多只能输入" + mMaxLength + "个字",Toast.LENGTH_SHORT).show();
            }
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null;
            } else {
                return source.subSequence(start, start + keep);
            }
        }
    }

    private void commit(String content,String mid, String agent_id){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("method","dialogue");
        map.put("id",id);
        map.put("send_id",agent_id);
        map.put("send_identity","0");//receive_id
        map.put("receive_id",mid);
        map.put("receive_identity","1");
        map.put("content",content);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,2,1);
    }


    private void tuijianzhuanjia(){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("method","companyRecommend");
        map.put("identity","1");
        map.put("id",id);
        map.put("flow_id",flowid);
        map.put("agent_mid",agent_id);
        map.put("company_id", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,3,1);
    }

    private void cenel(){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("method","cancelRecommend");
        map.put("identity","1");
        map.put("id",id);
        map.put("flow_id",flowid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,4,1);
    }


   private void tuijianzhuanjia2(String aid,String typeid){
       String url= MyApplication.ipadress+"/api/new_require.php";
       HashMap<String, String> map = new HashMap<>();
       map.put("method","recommendTalent");
       map.put("identity","1");
       map.put("id",id);
       map.put("agent_mid",agent_id);
       map.put("aid",aid);
       map.put("typeid",typeid);
       NetworkCom networkCom = NetworkCom.getNetworkCom();
       networkCom.postJson(url,map,handler,5,1);
   }
    private void fasongbaogao(String id, String flow_id){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method","faSongYueJianBaoGao");
        map.put("identity","1");
        map.put("id",id);
        map.put("flow_id",flow_id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,5,1);
    }

    private void menyidu(String flow_id, String state){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method", "evaluate");
        map.put("identity", "0");
        map.put("id", baseicid);
        map.put("flow_id",flow_id);
        map.put("state",state);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,6,1);
    }

    private void jujue(String flow_id){
        String url= MyApplication.ipadress+"/api/new_require.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method", "juJueZhuanJia");
        map.put("identity", "0");
        map.put("id", baseicid);
        map.put("flow_id", flow_id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,6,1);
    }

    private void youhuijuan(String aid, String typeid){
       String url= "http://www.zhongkechuangxiang.com/api/discounts_coupon_list.php";
       String mid =SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
       HashMap<String ,String> map = new HashMap<>();
       map.put("mid",mid);
       map.put("aid", aid);
       map.put("typeid", typeid);
       NetworkCom networkCom = NetworkCom.getNetworkCom();
       networkCom.getJson(url,map,handler,7,1);

    }

}
