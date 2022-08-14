package com.maidiantech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import Util.UIHelper;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianGuanzhu;
import dao.Service.MaiDianYuYue;
import dao.Service.MaiDianZan;
import dao.dbentity.CollectionEntity;
import entity.Area_cate;
import entity.Codes;
import entity.Details;
import entity.ImageState;
import entity.Interation;
import entity.Posts;
import entity.YuYueData;
import entity.ZanCode;
import entity.recode;
import view.CustomWebView;
import view.StyleUtils;
import view.StyleUtils1;

import static application.MyApplication.deviceid;

/**
 * Created by lizisong on 2017/8/4.
 */

public class BejingCeShi extends AutoLayoutActivity {
    UMShareAPI mShareAPI;
    ImageView picback;
    ImageView picoption;
    private ShareAction action;
    private EditText my_editext;
    private Button my_fason;
    private CheckBox my_collect;
    private Button my_tishi;
    private ImageView shares;
    private String aid, json, bodys = "", linyu, title, str, zlstr, rcstr, sbstr, xmstr, replace, newhtml, area1s, xmpic, name, xmbody, area_cate1,
            area_cate2, area_cate3, cate2, cate3, titles, picimg, pic, area1, rank, rcrank, post_title, rcpost_title, reward, rcreward, sbtitle, spec,
            factory, performance, functional, buy_date, price, unit, laboratory_research;
    private OkHttpUtils Okhttp;
    private recode data;
    private CustomWebView webview;
    public String v = "sadhkajs";
    private String charge_standard, stage, cooper, coopers, units, keyword, time1, update, strTime;
    private String pubdate;
    private String study_area;//领域
    private String studyarea;
    private String funciton;
    private MaiDianZan maiDianCollectionzan = null;
    private MaiDianCollection maiDianCollection = null;
    private MaiDianGuanzhu maiDianGuanzhu = null;
    private MaiDianYuYue maiDianYuYue = null;
    private MaiDianGuanzhu maiDian = null;
    private ArrayList<CollectionEntity> listCollections = null;
    private ArrayList<CollectionEntity> listCollectionzan = null;
    private ArrayList<CollectionEntity> listCollection = null;
    private ArrayList<YuYueData> listYuYue;
    private CollectionEntity mCollectionEntity = null;
    private CollectionEntity mCollectionEntityGuanzhu = null;
    private CollectionEntity mCollectionEntityzan = null;
    private String ips;
    LinearLayout dianzan, guanzhu, shoucan, ScrollView2;
    Button yuyue;
    String zchtml, search_project, research_project, username, rc_name, source, rc_souse, zx_souse, zc_souse, xm_souse, sb_souse, sysstr, ment, type, zanjson;
    boolean collect_state = false;
    int moveY = 0;
    Gson g;
    String mids, collectjson, quxiaojson, id, click;
    String loginState, liujson;
    CheckBox check_guanzhu;
    String oldmids;
    String des = "";
    boolean is_state = false;
    boolean is_finish = false;
    Stack stack = new Stack();
    boolean dianjie = false;
    private UMImage image;
    private ProgressBar progressBar;
    public static boolean yuyueState = false;
    Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {
                Toast.makeText(BejingCeShi.this, "请您登录", Toast.LENGTH_SHORT).show();
            }
        }
    };
    Handler
            handler = new Handler() {
        /**
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Gson g = new Gson();
                Codes codes = g.fromJson(liujson, Codes.class);
//                if(codes.code==1){
//                    Toast.makeText(DetailsActivity.this, codes.message, Toast.LENGTH_SHORT).show();
//                }else if(codes.code==-1){
//                    Toast.makeText(DetailsActivity.this, codes.message, Toast.LENGTH_SHORT).show();
//                }
            }
            if (msg.what == 1) {
                Gson g = new Gson();
                try {
                    Details details = g.fromJson(json, Details.class);

                    data = details.getData();

                    des = data.description;
                    bodys = data.getBody();
                    title = data.getTitle();
                    source = data.getSource();
                    pubdate=data.getPubdate();
                    strTime = TimeUtils.getStrTime(pubdate);
                    click = data.click;

                    webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
                    webview.setHapticFeedbackEnabled(false);
                    webview.getSettings().setSupportZoom(true);
                    webview.getSettings().setDomStorageEnabled(true);
                    webview.getSettings().setSupportMultipleWindows(true);
                    webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                    if (data.getTypeid().equals("9")) {
                        replace = str.replace("{{title}}", title);
                        newhtml = replace.replace("{{body}}", bodys);
                        zx_souse = newhtml.replace("{souse}", source);

//                        area1s = newhtml.replace("{p}", area_cate1);
//                        cate2 = area1s.replace("{x}", area_cate2);
//                        cate3 = cate2.replace("{s}", area_cate3);
//                        update = cate3.replace("{time}", strTime);
                        //  Log.i("update",update.toString());
                        int index = 0;
                        if (data.interpretation != null) {
                            if (data.interpretation.size() == 1) {
                                Interation item = data.interpretation.get(0);
                                if (item.title.equals("")) {
                                    zx_souse = zx_souse.replace("{{title_source}}", "");
                                } else {
                                    zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                                }
                            } else {
                                zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                            }

                            for (int i = 0; i < data.interpretation.size(); i++) {
                                Interation item = data.interpretation.get(i);
                                if (i == 0) {
                                    zx_souse = zx_souse.replace("{{title0}}", item.title);
                                } else if (i == 1) {
                                    zx_souse = zx_souse.replace("{{title1}}", item.title);
                                } else if (i == 2) {
                                    zx_souse = zx_souse.replace("{{title2}}", item.title);
                                } else if (i == 3) {
                                    zx_souse = zx_souse.replace("{{title3}}", item.title);
                                }
                                index = i;
                            }
                            switch (data.interpretation.size() - 1) {
                                case 0:
                                    zx_souse = zx_souse.replace("{{title1}}", "");
                                    zx_souse = zx_souse.replace("{{title2}}", "");
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 1:
                                    zx_souse = zx_souse.replace("{{title2}}", "");
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 2:
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 3:

                                    break;
                            }
                        } else {
                            zx_souse = zx_souse.replace("{{title_source}}", "");
                            zx_souse = zx_souse.replace("{{title0}}", "");
                            zx_souse = zx_souse.replace("{{title1}}", "");
                            zx_souse = zx_souse.replace("{{title2}}", "");
                            zx_souse = zx_souse.replace("{{title3}}", "");

                        }
                        String baseUrl = "file:///android_asset/";
                        //  TypefaceUtil.createTypeface(DetailsActivity.this, "fonts/font.ttf");
                        webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.zx_souse, "text/html",
                                "utf-8", null);
                    }
// else if (data.getTypeid().equals("10")) {
//                        replace = str.replace("{{title}}", title);
//                        newhtml = replace.replace("{{body}}", bodys);
//                        zx_souse=newhtml.replace("{souse}",source);
////                        area1s = newhtml.replace("{p}", area_cate1);
////                        cate2 = area1s.replace("{x}", area_cate2);
////                        cate3 = cate2.replace("{s}", area_cate3);
////                        update = cate3.replace("{time}", strTime);
//                        //  Log.i("update",update.toString());
//                        String baseUrl = "file:///android_asset/";
//                        //  TypefaceUtil.createTypeface(DetailsActivity.this, "fonts/font.ttf");
//                        webview.loadDataWithBaseURL(baseUrl, DetailsActivity.this.zx_souse, "text/html",
//                                "utf-8", null);
//                    }
                    else if (data.getTypeid().equals("6")) {
                        replace = zchtml.replace("{{title}}", title);
                        zc_souse = replace.replace("{{body}}", bodys);
                        int index = 0;
                        if (data.interpretation != null) {
                            if (data.interpretation.size() == 1) {
                                Interation item = data.interpretation.get(0);
                                if (item.title.equals("")) {
                                    zc_souse = zc_souse.replace("{{title_source}}", "");
                                } else {
                                    zc_souse = zc_souse.replace("{{title_source}}", "相关解读");
                                }
                            } else {
                                zc_souse = zc_souse.replace("{{title_source}}", "相关解读");
                            }

                            for (int i = 0; i < data.interpretation.size(); i++) {
                                Interation item = data.interpretation.get(i);
                                if (i == 0) {
                                    zc_souse = zc_souse.replace("{{title0}}", item.title);
                                } else if (i == 1) {
                                    zc_souse = zc_souse.replace("{{title1}}", item.title);
                                } else if (i == 2) {
                                    zc_souse = zc_souse.replace("{{title2}}", item.title);
                                } else if (i == 3) {
                                    zc_souse = zc_souse.replace("{{title3}}", item.title);
                                }
                                index = i;
                            }
                            switch (data.interpretation.size() - 1) {
                                case 0:
                                    zc_souse = zc_souse.replace("{{title1}}", "");
                                    zc_souse = zc_souse.replace("{{title2}}", "");
                                    zc_souse = zc_souse.replace("{{title3}}", "");
                                    break;
                                case 1:
                                    zc_souse = zc_souse.replace("{{title2}}", "");
                                    zc_souse = zc_souse.replace("{{title3}}", "");
                                    break;
                                case 2:
                                    zc_souse = zc_souse.replace("{{title3}}", "");
                                    break;
                                case 3:

                                    break;
                            }
                        } else {
                            zc_souse = zc_souse.replace("{{title_source}}", "");
                            zc_souse = zc_souse.replace("{{title0}}", "");
                            zc_souse = zc_souse.replace("{{title1}}", "");
                            zc_souse = zc_souse.replace("{{title2}}", "");
                            zc_souse = zc_souse.replace("{{title3}}", "");

                        }
                        String baseUrl = "file:///android_asset/";
                        webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.zc_souse, "text/html",
                                "utf-8", null);
                    } else if (data.getTypeid().equals("8")) {
                        pic = data.getLitpic();
                        area1 = data.getArea_cate().getArea_cate1();
                        titles = sysstr.replace("{title}", title);

                        type = titles.replace("{type}", data.getLab_type());

                        picimg = type.replace("{listpic}", pic);

                        ment = picimg.replace("{partment}", data.getDepartment());
                        bodys = ment.replace("{body}", bodys);
                        if (area1 != null && !area1.equals("")) {
                            linyu = bodys.replace("{linyu}", area1);
                        } else {
                            linyu = bodys.replace("{linyu}", "");
                        }

                        String baseUrl = "file:///android_asset/";
                        webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.linyu, "text/html",
                                "utf-8", null);
                    } else if (data.getTypeid().equals("1")) {
                        replace = str.replace("{{title}}", title);
                        newhtml = replace.replace("{{body}}", bodys);
                        zx_souse = newhtml.replace("{souse}", strTime);


                        int index = 0;
                        if (data.interpretation != null) {
                            if (data.interpretation.size() == 1) {
                                Interation item = data.interpretation.get(0);
                                if (item.title.equals("")) {
                                    zx_souse = zx_souse.replace("{{title_source}}", "");
                                } else {
                                    zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                                }
                            } else {
                                zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                            }

                            for (int i = 0; i < data.interpretation.size(); i++) {
                                Interation item = data.interpretation.get(i);
                                if (i == 0) {
                                    zx_souse = zx_souse.replace("{{title0}}", item.title);
                                } else if (i == 1) {
                                    zx_souse = zx_souse.replace("{{title1}}", item.title);
                                } else if (i == 2) {
                                    zx_souse = zx_souse.replace("{{title2}}", item.title);
                                } else if (i == 3) {
                                    zx_souse = zx_souse.replace("{{title3}}", item.title);
                                }
                                index = i;
                            }
                            switch (data.interpretation.size() - 1) {
                                case 0:
                                    zx_souse = zx_souse.replace("{{title1}}", "");
                                    zx_souse = zx_souse.replace("{{title2}}", "");
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 1:
                                    zx_souse = zx_souse.replace("{{title2}}", "");
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 2:
                                    zx_souse = zx_souse.replace("{{title3}}", "");
                                    break;
                                case 3:

                                    break;
                            }
                        } else {
                            zx_souse = zx_souse.replace("{{title_source}}", "");
                            zx_souse = zx_souse.replace("{{title0}}", "");
                            zx_souse = zx_souse.replace("{{title1}}", "");
                            zx_souse = zx_souse.replace("{{title2}}", "");
                            zx_souse = zx_souse.replace("{{title3}}", "");

                        }

                        String baseUrl = "file:///android_asset/";
                        zx_souse = zx_souse.replace("{listpic}", data.getLitpic());
                        zx_souse = zx_souse.replace("{leaderette}",data.getLeaderette());
                        webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.zx_souse, "text/html",
                                "utf-8", null);
                    } else if (data.getTypeid().equals("2")) {


                        Area_cate area = data.getArea_cate();
                        area_cate1 = area.getArea_cate1();
                        pic = data.getLitpic();
                        area1 = data.getArea_cate().getArea_cate1();
                        titles = xmstr.replace("{title}", title);
                        xmbody = titles.replace("{body}", bodys);
//                        xm_souse=xmbody.replace("{souse}",source);
                        if (area_cate1 != null && !area_cate1.equals("")) {
                            area1s = xmbody.replace("{p}", area_cate1);
                        } else {
                            area1s = xmbody.replace("{p}", "");
                        }

                        xmpic = area1s.replace("{img}", pic);
                        xmpic = xmpic.replace("{listpic}", data.getLitpic());
                        xmpic=xmpic.replace("{souse}",strTime);
                        String baseUrl1 = "file:///android_asset/";
                        webview.loadDataWithBaseURL(baseUrl1, BejingCeShi.this.xmpic, "text/html",
                                "utf-8", null);
                    } else {
                        if (data.getTypeid().equals("4")) {


                            Area_cate area = data.getArea_cate();
                            area_cate1 = area.getArea_cate1();
                            rank = data.getRank();
                            post_title = data.getPost_title();
                            reward = data.getReward();
                            pubdate = data.getPubdate();
                            study_area = data.getStudy_area();

                            strTime = TimeUtils.getStrTime(pubdate);
                            research_project = data.getResearch_project();
                            username = data.getUsername();


                            pic = data.getLitpic();
                            area1 = data.getArea_cate().getArea_cate1();
                            xmbody = rcstr.replace("{body}", bodys);


                            rcrank = xmbody.replace("{rank}", rank);

                            rcreward = rcrank.replace("{reward}", reward);

                            studyarea = rcreward.replace("{study_area}", study_area);
                            search_project = studyarea.replace("{project}", research_project);

                            if (username != null && !username.equals("")) {
                                search_project = search_project.replace("{username}", username);
                            } else {
                                search_project = search_project.replace("{username}", "");
                            }
                            if (data.getLitpic() != null && !data.getLitpic().equals("")) {
                                search_project = search_project.replace("{img}", data.getLitpic());
                            }

                            if (data.getIs_academician() != null) {
                                if (data.getIs_academician().equals("1")) {
                                    search_project = search_project.replace("{yuanshi}", "院士");
                                } else if (data.getIs_academician().equals("0")) {
                                    search_project = search_project.replace("{yuanshi}", "");
                                }
                            } else {
                                search_project = search_project.replace("{yuanshi}", "");
                            }


                            String baseUrl2 = "file:///android_asset/";
                            webview.loadDataWithBaseURL(baseUrl2, BejingCeShi.this.search_project, "text/html",
                                    "utf-8", null);
                        } else if (data.getTypeid().equals("7")) {
                            pic = data.getLitpic();
                            funciton = data.getFunctional();
                            area1 = data.getArea_cate().getArea_cate1();
                            sbtitle = sbstr.replace("{title}", title);

                            picimg = sbtitle.replace("{listpic}", data.getLitpic());

                            spec = picimg.replace("{spec}", data.getSpec());
                            price = spec.replace("{price}", data.getPrice());
                            functional = price.replace("{functional}", data.getFunctional());
                            performance = functional.replace("{performance}", data.getPerformance());

                            String baseUrl = "file:///android_asset/";
                            webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.performance, "text/html",
                                    "utf-8", null);
                        } else if (data.getTypeid().equals("5")) {
                            area1 = data.getArea_cate().getArea_cate1();
                            titles = zlstr.replace("{title}", title);
//                        sb_souse = titles.replace("{souse}", source);
                            xmbody = titles.replace("{body}", bodys);
                            if (area1 != null && !area1.equals("")) {
                                xmbody = xmbody.replace("{linyu}", area1);
                            } else {
                                xmbody = xmbody.replace("{linyu}", "");
                            }

                            String baseUrl = "file:///android_asset/";
                            webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.xmbody, "text/html",
                                    "utf-8", null);
                        } else {
                            replace = str.replace("{{title}}", title);
                            newhtml = replace.replace("{{body}}", bodys);
                            zx_souse = newhtml.replace("{souse}", source);
                            int index = 0;
                            if (data.interpretation != null) {
                                if (data.interpretation.size() == 1) {
                                    Interation item = data.interpretation.get(0);
                                    if (item.title.equals("")) {
                                        zx_souse = zx_souse.replace("{{title_source}}", "");
                                    } else {
                                        zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                                    }
                                } else {
                                    zx_souse = zx_souse.replace("{{title_source}}", "相关阅读");
                                }

                                for (int i = 0; i < data.interpretation.size(); i++) {
                                    Interation item = data.interpretation.get(i);
                                    if (i == 0) {
                                        zx_souse = zx_souse.replace("{{title0}}", item.title);
                                    } else if (i == 1) {
                                        zx_souse = zx_souse.replace("{{title1}}", item.title);
                                    } else if (i == 2) {
                                        zx_souse = zx_souse.replace("{{title2}}", item.title);
                                    } else if (i == 3) {
                                        zx_souse = zx_souse.replace("{{title3}}", item.title);
                                    }
                                    index = i;
                                }
                                switch (data.interpretation.size() - 1) {
                                    case 0:
                                        zx_souse = zx_souse.replace("{{title1}}", "");
                                        zx_souse = zx_souse.replace("{{title2}}", "");
                                        zx_souse = zx_souse.replace("{{title3}}", "");
                                        break;
                                    case 1:
                                        zx_souse = zx_souse.replace("{{title2}}", "");
                                        zx_souse = zx_souse.replace("{{title3}}", "");
                                        break;
                                    case 2:
                                        zx_souse = zx_souse.replace("{{title3}}", "");
                                        break;
                                    case 3:

                                        break;
                                }
                            } else {
                                zx_souse = zx_souse.replace("{{title_source}}", "");
                                zx_souse = zx_souse.replace("{{title0}}", "");
                                zx_souse = zx_souse.replace("{{title1}}", "");
                                zx_souse = zx_souse.replace("{{title2}}", "");
                                zx_souse = zx_souse.replace("{{title3}}", "");

                            }
                            String baseUrl = "file:///android_asset/";

                            webview.loadDataWithBaseURL(baseUrl, BejingCeShi.this.zx_souse, "text/html",
                                    "utf-8", null);
                        }
                    }
                    pic = data.getLitpic();



                } catch (Exception e) {

                }
            }
            if (msg.what == 2) {
                if (g == null) {
                    g = new Gson();
                }
                ZanCode zanCode = g.fromJson(zanjson, ZanCode.class);
                int code = zanCode.code;

                if (zanCode.code == 1) {
                    String aids = zanCode.data.aid;
                    CollectionEntity value = new CollectionEntity();
                    value.aid = aid;
                    value.title = title;
                    value.upFlag = 0;
                    value.updateTime = System.currentTimeMillis() + "";
                    value.type = name;
                    value.pic = pic;
                    value.iscollect = "0";
                    value.isAdd = 0;

                    maiDianCollectionzan.insert(value);

                    my_tishi.setBackgroundResource(R.mipmap.zan_a);
                    Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                } else if (zanCode.code == -1) {
                    if (zanCode.message.contains("已点过赞")) {
                        Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                        CollectionEntity value = new CollectionEntity();
                        value.aid = aid;
                        value.title = title;
                        value.upFlag = 0;
                        value.updateTime = System.currentTimeMillis() + "";
                        value.type = name;
                        value.pic = pic;
                        value.iscollect = "0";
                        value.isAdd = 0;
                        value.click = data.click;
                        value.image=data.image;
                        value.imageState = data.imageState;
                        value.description = data.description;
                        if(data.getArea_cate() != null){
                            value.area_cate = data.getArea_cate().getArea_cate1();
                        }
                        value.is_academician = data.getIs_academician();

                        maiDianCollectionzan.insert(value);
                        my_tishi.setBackgroundResource(R.mipmap.zan_a);
                        Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                    }else if(zanCode.code==-1){
                        if(zanCode.message.contains("已点过赞")){
                            Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                            CollectionEntity value = new CollectionEntity();
                            value.aid = aid;
                            value.title = title;
                            value.upFlag = 0;
                            value.updateTime = System.currentTimeMillis()+"";
                            value.type = name;
                            value.pic = pic;
                            value.iscollect="0";
                            value.isAdd = 0;
                            value.click = data.click;
                            value.image=data.image;
                            value.imageState = data.imageState;
                            value.description = data.description;
                            if(data.getArea_cate() != null){
                                value.area_cate = data.getArea_cate().getArea_cate1();
                            }
                            value.is_academician = data.getIs_academician();
                            maiDianCollectionzan.insert(value);
                            my_tishi.setBackgroundResource(R.mipmap.zan_a);
                        }

                    }

                }

            }
            if (msg.what == 3) {
                if (g == null) {
                    g = new Gson();
                }
                ZanCode zanCode = g.fromJson(collectjson, ZanCode.class);
                if (zanCode.code == 1) {
                    collect_state = true;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
                    id = zanCode.data.id;
                    CollectionEntity value = new CollectionEntity();
                    value.aid = aid;
                    value.title = title;
                    value.upFlag = 0;
                    value.updateTime = System.currentTimeMillis() + "";
                    value.type = name;
                    value.pic = pic;
                    value.iscollect = "0";
                    value.pid = id;
                    value.isAdd = 0;
                    value.click = data.click;
                    value.image=data.image;
                    value.imageState = data.imageState;
                    value.description = data.description;
                    if(data.getArea_cate() != null){
                        value.area_cate = data.getArea_cate().getArea_cate1();
                    }
                    value.is_academician = data.getIs_academician();
//                    Toast.makeText(DetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    maiDianCollection.insert(value);
                    Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                } else if (zanCode.code == -1) {
                    Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }

            if (msg.what == 4) {
                if (g == null) {
                    g = new Gson();
                }
                ZanCode zanCode = g.fromJson(quxiaojson, ZanCode.class);
                if (zanCode.code == 1) {
                    collect_state = false;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    maiDianCollection.deletebyaid(aid);
                    Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                } else if (zanCode.code == -1) {
                    Toast.makeText(BejingCeShi.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    int x = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d("lizisong", "goog");

//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            x = (int)ev.getX();
//        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
//            if((int)ev.getX()-x < 40){
//                return super.dispatchTouchEvent(ev);
//            }
//        }
//        if (!SlidingActivity.isSliding) {
//            setsetSlidingLayou(ev);
//        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.my_details);
        is_state = true;
        dianjie = false;
        maiDianCollection = MaiDianCollection.getInstance(this);
        maiDianGuanzhu = MaiDianGuanzhu.getInstance(this);
        maiDianCollectionzan = MaiDianZan.getInstance(this);
        maiDianYuYue = MaiDianYuYue.getInstance(this);
        mShareAPI = UMShareAPI.get(this);

        try {
            ips = MyApplication.ip;
            Intent intent = getIntent();
            aid = intent.getStringExtra("id");
            Log.i("aids", aid);
            name = intent.getStringExtra("name");

//            pic = intent.getStringExtra("pic");


            initView();
            int netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                Toast.makeText(BejingCeShi.this, "网络不给力", Toast.LENGTH_SHORT).show();

            } else {
                liulang();
            }

        } catch (Exception e) {
        }


        if (mCollectionEntity == null) {
            mCollectionEntity = new CollectionEntity();
            mCollectionEntity.aid = aid;
            mCollectionEntity.title = title;
            mCollectionEntity.upFlag = 0;
            mCollectionEntity.updateTime = System.currentTimeMillis() + "";
            mCollectionEntity.type = name;
            mCollectionEntity.pic = pic;

        }
        progressBar = (ProgressBar) findViewById(R.id.progress);
        webview = (CustomWebView) findViewById(R.id.wbviw);
        webview.setOnCustomScroolChangeListener(new CustomWebView.ScrollInterface() {

            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                float webcontent = webview.getContentHeight() * webview.getScale();
                float webnow = webview.getHeight() + webview.getScrollY();
//                Log.d("lizisong", "webcontent:"+webcontent+",webnow:"+webnow);
                if (webcontent - webnow < 5) {
                    //已经处于底端
                    yuyue.setBackgroundResource(R.mipmap.yue_tm);
                } else {
                    if (webnow == webview.getHeight()) {
//                        Log.d("lizisong", "已经在顶端");
                    } else {
                        if (yuyueState) {
                            yuyue.setBackgroundResource(R.mipmap.yuejian);
                        } else {
                            yuyue.setBackgroundResource(R.mipmap.yue);
                        }

                    }
                }

            }
        });
        webview.setWebChromeClient(new BejingCeShi.WebChromeClient());


        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        SlidingActivity.isSliding = true;
//                        moveY = webview.getScrollY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                      if(Math.abs(webview.getScrollY()-moveY) >10 ){
//                          SlidingActivity.isSliding = true;
//                      }else{
//                          SlidingActivity.isSliding = false;
//                      }
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        moveY = 0;
//                        SlidingActivity.isSliding = false;
//                        break;
//                }

                return false;


            }
        });


        webview.setWebViewClient(new BejingCeShi.SampleWebViewClient());
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(BejingCeShi.this, "网络不给力", Toast.LENGTH_SHORT).show();

        } else {
//            UIHelper.showDialogForLoading(DetailsActivity.this, "", true);
            progressBar.setVisibility(View.VISIBLE);
            oldmids = aid;
            getjson();
        }
        //html转成string字符串
        try {
            str = new String(toByteArray(getAssets().open("information.html")));
            xmstr = new String(toByteArray(getAssets().open("project.html")));
            zchtml = new String(toByteArray(getAssets().open("policy.html")));
            rcstr = new String(toByteArray(getAssets().open("personnel.html")));
            sbstr = new String(toByteArray(getAssets().open("equipment.html")));
            zlstr = new String(toByteArray(getAssets().open("patent.html")));
            sysstr = new String(toByteArray(getAssets().open("laboratory.html")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void liulang() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis() + "";
                String sign = "";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("aid" + aid);
                sort.add("timestamp" + timestamp);
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
                liujson = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_click.php?aid=" + aid + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private void initView() {
        picback = (ImageView) findViewById(R.id.pic_back);
        TextView deli_text = (TextView) findViewById(R.id.deli_text);
//         ScrollView2=(LinearLayout) findViewById(R.id.ScrollView2);
        dianzan = (LinearLayout) findViewById(R.id.dianzan);
        guanzhu = (LinearLayout) findViewById(R.id.guanzhu);
        shoucan = (LinearLayout) findViewById(R.id.shoucan);
        yuyue = (Button) findViewById(R.id.yuyue);
        shares = (ImageView) findViewById(R.id.shares);
        if (name.equals("推荐轮播图")) {
            deli_text.setText("推荐详情");
        } else {
            deli_text.setText(name + "详情");
        }
        my_collect = (CheckBox) findViewById(R.id.my_collect);
        my_tishi = (Button) findViewById(R.id.my_tishi);
//        check_guanzhu=(CheckBox) findViewById(R.id.check_guanzhu);
        listYuYue = maiDianYuYue.get();
        yuyueState = false;
        for (int i = 0; i < listYuYue.size(); i++) {
            YuYueData item = listYuYue.get(i);
            if (aid.equals(item.aid)) {
                yuyueState = true;
            }
        }

        if (name.equals("人才") || name.equals("专家")) {
            yuyue.setVisibility(View.VISIBLE);
//            ScrollView2.setVisibility(View.VISIBLE);
            dianzan.setVisibility(View.VISIBLE);
            shoucan.setVisibility(View.VISIBLE);
            yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    String company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
                    String adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
                    String types = SharedPreferencesUtil.getString(SharedPreferencesUtil.UNIT_TYPE, "0");
                    if (!loginState.equals("1")) {
                        Intent intent = new Intent(BejingCeShi.this, MyloginActivity.class);
                        startActivity(intent);
                    } else if (company == null || adress == null || types == null || company.equals("") || adress.equals("")
                            || types.equals("")
                            || company.equals("0") || adress.equals("0") || types.equals("0")) {
                        Intent intent = new Intent(BejingCeShi.this, UnitActivity.class);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(BejingCeShi.this, AddXuQiu.class);
                        Posts item= new Posts();
                        item.aid = aid;
                        item.setId(aid);
                        item.setLitpic(pic);
                        if(data != null){
                            item.setDescription(data.description);
                        }

                        item.setTitle(title);
                        item.setUsername(username);
                        if(name.equals("人才")|| name.equals("专家")){
                            Area_cate item1 = new Area_cate();
                            item1.setArea_cate1(area_cate1);
                            item.setArea_cate(item1);
                            item.setTypename("专家");
                        }
                        AddXuQiu.data = item;
                        intent.putExtra("state", "1");
                        startActivity(intent);

//                        Intent intent = new Intent(DetailsActivity.this, Appointment.class);
//                        intent.putExtra("aid", aid);
//                        intent.putExtra("pic", pic);
//                        intent.putExtra("name", username);
//                        intent.putExtra("lingyu", study_area);
//                        intent.putExtra("rand", rank);
//                        intent.putExtra("body", Html.fromHtml(bodys).toString());
//                        if (data != null) {
//                            intent.putExtra("typeid", data.getTypeid());
//                            intent.putExtra("look", data.click);
//                        }
//                        startActivity(intent);
                    }

                }
            });
        }

        if (name.equals("项目") || name.equals("设备") || name.equals("实验室")|| name.equals("研究所")) {
//            ScrollView2.setVisibility(View.VISIBLE);
            yuyue.setVisibility(View.VISIBLE);
            if(name.equals("实验室")|| name.equals("研究所")){
                yuyue.setVisibility(View.GONE);
            }
            dianzan.setVisibility(View.VISIBLE);
            shoucan.setVisibility(View.VISIBLE);
            yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    String company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "0");
                    String adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
                    String types = SharedPreferencesUtil.getString(SharedPreferencesUtil.UNIT_TYPE, "0");
                    if (!loginState.equals("1")) {
                        Intent intent = new Intent(BejingCeShi.this, MyloginActivity.class);
                        startActivity(intent);
                    } else if (company == null || adress == null || types == null || company.equals("") || adress.equals("")
                            || types.equals("")
                            || company.equals("0") || adress.equals("0") || types.equals("0")) {
                        Intent intent = new Intent(BejingCeShi.this, UnitActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(BejingCeShi.this, AddXuQiu.class);
                        Posts item= new Posts();
                        item.aid = aid;
                        item.setId(aid);
                        item.setLitpic(pic);
                        if(data != null){
                            item.setDescription(data.description);
                            item.setLitpic(data.getLitpic());
                            ImageState image = new ImageState();
                            image.image1 = data.getLitpic();
                            item.setImage(image);
                        }
                        item.setTitle(title);
                        if(name.equals("项目")){
                            Area_cate item1 = new Area_cate();
                            item1.setArea_cate1(area1);
                            item.setArea_cate(item1);
                            item.setTypename("项目");
                        }else if(name.equals("设备")){
                            Area_cate item1 = new Area_cate();
                            item1.setArea_cate1(area1);
                            item.setArea_cate(item1);
                            item.setTypename("设备");

                        }
                        AddXuQiu.data = item;
                        intent.putExtra("state", "1");
                        startActivity(intent);
//                        Intent intent = new Intent(DetailsActivity.this, informations.class);
//                        intent.putExtra("pic", pic);
//                        intent.putExtra("aid", aid);
//                        intent.putExtra("click", click);
//                        intent.putExtra("body", des);
//                        intent.putExtra("area", area1);
//
//                        intent.putExtra("funciton", funciton);
//                        if (data != null) {
//                            intent.putExtra("model", data.getSpec());
//                        }
//                        intent.putExtra("title", title);
//                        if (data != null) {
//                            intent.putExtra("typeid", data.getTypeid());
//                        }
//                        startActivity(intent);
                    }

                }
            });
        }
        if (name.equals("资讯") || name.equals("政策") || name.equals("专利") || name.equals("推荐轮播图") || name.equals("推荐") || name.equals("活动") || name.equals("知识")) {
            dianzan.setVisibility(View.VISIBLE);
            shoucan.setVisibility(View.VISIBLE);
            yuyue.setVisibility(View.GONE);
        }

        listCollections = maiDianCollection.get();
        boolean state = false;

        if (listCollections.size() > 0) {
            for (int i = 0; i < listCollections.size(); i++) {
                CollectionEntity item = listCollections.get(i);
                if (item != null && item.aid != null) {
                    if (item.aid.equals(aid)) {

                        collect_state = true;
                        mCollectionEntity = item;
                        break;
                    }
                }
            }
        }
        if (collect_state) {
            my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
        } else {
            my_collect.setBackgroundResource(R.mipmap.zixun_collect);
        }

        my_collect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (dianjie) {
                    return;
                }
                dianjie = true;
                if (!loginState.equals("1")) {
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    Toast.makeText(BejingCeShi.this, "请您登录", Toast.LENGTH_SHORT).show();
//                    lgonjson();
                } else {

                    if (collect_state == false) {
                        listCollections = maiDianCollection.getcollect();
                        for (int i = 0; i < listCollections.size(); i++) {
                            CollectionEntity item = listCollections.get(i);
                            if (item.aid.equals(aid)) {
                                return;
                            }
                        }
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(BejingCeShi.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        } else {
                            collectjson();
                        }

                    } else {
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(BejingCeShi.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        } else {
                            dismisjson();
                        }
//                        Toast.makeText(DetailsActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
//                        maiDianCollection.deletebyaid(aid);
                    }
                }
                dianjie = false;

            }
        });

        listCollection = maiDianGuanzhu.get();
        boolean state1 = false;
        if (listCollection.size() > 0) {
            for (int i = 0; i < listCollection.size(); i++) {
                CollectionEntity item = listCollection.get(i);
                if (item != null && item.aid != null) {
                    if (item.aid.equals(aid)) {
                        collect_state = true;
                        mCollectionEntityGuanzhu = item;
                        break;
                    }
                }
            }
        }

        listCollectionzan = maiDianCollectionzan.get();
        boolean state2 = false;

        if (listCollectionzan.size() > 0) {
            for (int i = 0; i < listCollectionzan.size(); i++) {
                CollectionEntity item = listCollectionzan.get(i);
                if (item != null && item.aid != null) {
                    if (item.aid.equals(aid)) {

                        state2 = true;
                        mCollectionEntityzan = item;
                        break;
                    }
                }
            }
        }
        if (state2) {
            my_tishi.setBackgroundResource(R.mipmap.zan_a);
        }

        my_tishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollectionEntityzan != null) {
                    Toast.makeText(BejingCeShi.this, "已经点过赞", Toast.LENGTH_SHORT).show();

                    return;
                }
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(BejingCeShi.this, "网络不给力", Toast.LENGTH_SHORT).show();

                } else {
                    zanjson();
                }


            }
        });


//        UmengTool.getSignature(this);
        picback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        try {

            shares.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(BejingCeShi.this, ShareActivity.class);
                        intent.putExtra("title", title);
                        if (data.getTypeid().equals("7")){
                            intent.putExtra("txt", data.getFunctional());
                        }else{
                            String type = String.valueOf(Html.fromHtml(bodys));
                            if (type.length() > 512) {
                                type = type.substring(0, 256);
                            }
                            intent.putExtra("txt", type);
                        }
                        intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/api/share.php?aid=" + aid + "");
                        if (pic == null || pic.equals("")) {
                            intent.putExtra("imageurl", "http://"+MyApplication.ip+"/uploads/logo/logo.png");
                        }else {
                            intent.putExtra("imageurl", pic);
                        }
                        startActivity(intent);


//                        UIHelper.showDialogForLoading(BejingCeShi.this, "", true);
//                        if (pic == null || pic.equals("")) {
////                Bitmap    imagecl = BitmapFactory.decodeResource(getResources(), R.mipmap.log);
////
////                    image = new UMImage(DetailsActivity.this, Bitmap2Bytes(imagecl));
////                    http://123.206.8.208/uploads/logo/logo.png
//                            image = new UMImage(BejingCeShi.this, "http://www.zhongkechuangxiang.com/uploads/logo/logo1.png");
//
//                        } else {
//                            image = new UMImage(BejingCeShi.this, pic);
//                        }
//
//                        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                                {
//                                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                                };
//                        action = new ShareAction(BejingCeShi.this);
//                        UIHelper.hideDialogForLoading();
//                        if (data.getTypeid().equals("7")) {
//                            action.setDisplayList(displaylist)
//
//                                    .withText(data.getFunctional())
//                                    .withTitle(title)
//                                    .withTargetUrl("http://www.zhongkechuangxiang.com/api/share.php?aid=" + aid + "")
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .open();
//                        } else {
//                            String type = String.valueOf(Html.fromHtml(bodys));
//                            if (type.length() > 512) {
//                                type = type.substring(0, 256);
//                            }
//                            action.setDisplayList(displaylist)
//
////                    Html.fromHtml(htmlStr)
//                                    .withText(type)
//                                    .withTitle(title)
//                                    .withTargetUrl("http://www.zhongkechuangxiang.com/api/share.php?aid=" + aid + "")
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .open();
//                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }


//        if(yuyueState){
//            yuyue.setClickable(false);
//            yuyue.setBackgroundResource(R.color.text_gray);
//        }else{
        yuyue.setClickable(false);
        yuyue.setBackgroundResource(R.mipmap.yuejian);
//        }


    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void dismisjson() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        new Thread() {
            @Override
            public void run() {
                super.run();
                mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                listCollections = maiDianCollection.get();
                boolean state = false;
                CollectionEntity itemdismis = null;
                if (listCollections.size() > 0) {
                    for (int i = 0; i < listCollections.size(); i++) {
                        CollectionEntity item = listCollections.get(i);
                        if (item != null && item.aid != null) {
                            if (item.aid.equals(aid)) {
                                state = true;
                                itemdismis = item;
                                break;
                            }
                        }
                    }
                }
                if (itemdismis != null) {
                    id = itemdismis.pid;
                }
                String timestamp = System.currentTimeMillis() + "";
                String sign = "";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("id" + id);
                sort.add("mid" + mids);
                sort.add("method" + "cancel");
                sort.add("timestamp" + timestamp);
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
                MyApplication.setAccessid();
                quxiaojson = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_store.php?id=" + id + "&mid=" + mids + "&method=cancel" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void collectjson() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        new Thread() {
            @Override
            public void run() {
                super.run();
                mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                String timestamp = System.currentTimeMillis() + "";
                String sign = "";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("aid" + aid);
                sort.add("mid" + mids);
                sort.add("method" + "add");
                sort.add("timestamp" + timestamp);
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
                MyApplication.setAccessid();
                collectjson = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_store.php?aid=" + aid + "&mid=" + mids + "&method=add" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void lgonjson() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (!loginState.equals("1")) {
//                        Toast.makeText(DetailsActivity.this, "请您登录", Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.what = 3;
                    handlers.sendMessage(msg);
                }
            }
        }.start();
    }


    public void zanjson() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();

                    if (!loginState.equals("1")) {
//                        Toast.makeText(DetailsActivity.this, "请您登录", Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what = 3;
                        handlers.sendMessage(msg);
                    } else {
                        String timestamp = System.currentTimeMillis() + "";
                        String sign = "";
                        ArrayList<String> sort = new ArrayList<String>();
                        sort.add("aid" + aid);
                        sort.add("mid" + mids);
                        sort.add("timestamp" + timestamp);
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
                        MyApplication.setAccessid();
                        zanjson = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_zan.php?aid=" + aid + "&mid=" + mids + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                        if (zanjson != null) {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }


                }
            }.start();

        } catch (Exception e) {
        }

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (my_editext.getText().toString().length() == 0) {
                return;
            } else {
                my_fason.setVisibility(View.VISIBLE);
                shares.setVisibility(View.GONE);
                my_collect.setVisibility(View.GONE);
                my_tishi.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            my_fason.setVisibility(View.GONE);
            shares.setVisibility(View.VISIBLE);
            my_collect.setVisibility(View.VISIBLE);
            my_tishi.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
            } else {
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(BejingCeShi.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getjson() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();

        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
//                    http://www.maidiantech.com/api/arc_detail.php?aid="+aid+"&mid="+mids+"&deviceid="+deviceid+"&typeid="+"1"+"
                    //json=Okhttp.Myokhttpclient("http://172.24.96.216/api/arc_detail.php?aid=1");
                    MyApplication.setAccessid();
                    String timestamp = System.currentTimeMillis() + "";
                    String sign = "";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("aid" + aid);
                    sort.add("mid" + mids);
                    sort.add("deviceid" + deviceid);
                    sort.add("timestamp" + timestamp);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid" + accessid);

                    if (name.equals("政策")) {
                        sort.add("typeid" + "6");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=6" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("项目")) {
                        sort.add("typeid" + "2");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=2" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("人才") || name.equals("专家")) {
                        sort.add("typeid" + "4");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=4" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("设备")) {
                        sort.add("typeid" + "7");
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=7" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("专利")) {
                        sort.add("typeid" + "5");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=5" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("实验室") || name.equals("研究所")) {
                        sort.add("typeid" + "8");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=8" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("资讯")) {
                        sort.add("typeid" + "1");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=1" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    } else if (name.equals("推荐")) {
                        sort.add("typeid" + "9");
                        sort.add("version"+MyApplication.version);
                        sign = KeySort.keyScort(sort);
                        json = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_detail.php?aid=" + aid + "&mid=" + mids + "&deviceid=" + deviceid + "&typeid=9" + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    }

//                    Log.i("json",json);
                    try {
                        Thread.sleep(1000);
//                        UIHelper.hideDialogForLoading();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();
        } catch (Exception e) {
        }
    }

    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
        MobclickAgent.onPageStart("详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        if (yuyueState) {
            yuyue.setClickable(false);
            yuyue.setBackgroundResource(R.mipmap.yuejian);
        }

    }

    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        MobclickAgent.onPageEnd("详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    class SampleWebViewClient extends WebViewClient {


        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            is_state = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Interation item = null;
                if (url != null) {
                    if (url.contains("00")) {
                        item = data.interpretation.get(0);
                    } else if (url.contains("11")) {
                        item = data.interpretation.get(1);
                    } else if (url.contains("22")) {
                        item = data.interpretation.get(2);
                    } else if (url.contains("33")) {
                        item = data.interpretation.get(3);
                    }
                    stack.push(oldmids);
                    aid = item.aid;
                    is_state = true;
//                 UIHelper.showDialogForLoading(DetailsActivity.this, "", true);
                    progressBar.setVisibility(View.VISIBLE);
                    getjson();
                }
            } catch (Exception e) {
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!is_state) {
                boolean state = stack.empty();
                if (!state) {
                    is_state = true;
                    aid = (String) stack.pop();
//                    UIHelper.showDialogForLoading(DetailsActivity.this, "", true);
                    progressBar.setVisibility(View.VISIBLE);
                    is_state = true;
                    getjson();
                } else {
                    finish();
                }
            }
            is_state = false;
            oldmids = aid;
        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
                if (!yuyueState) {
                    yuyue.setClickable(true);
                    yuyue.setBackgroundResource(R.mipmap.yue);
                }
            }
        }
    }

    private void setWebView() {

    }
}
