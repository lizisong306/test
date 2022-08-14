package com.maidiantech;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
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
import java.util.HashMap;
import java.util.Stack;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import Util.UIHelper;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianZan;
import dao.dbentity.CollectionEntity;
import entity.Codes;
import entity.Details;
import entity.Interation;
import entity.ZanCode;
import entity.recode;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/4/6.
 */

public class ZixunDetailsActivity extends AutoLayoutActivity {
    UMShareAPI mShareAPI;
    private ShareAction action;

    private ImageView my_collect;

    private ImageView shares;
    private String aid;
    private OkHttpUtils Okhttp;
    private String json;
    private recode data;
    private String bodys;

    private WebView webview;
    private String title;
    private String str;

    public String v = "sadhkajs";
    private String replace;
    private String newhtml;

    private String name;
    private String deviceid;

    private String pic;

    private MaiDianZan maiDianCollectionzan = null;
    private MaiDianCollection maiDianCollection = null;



    private ArrayList<CollectionEntity> listCollections = null;
    private ArrayList<CollectionEntity> listCollectionzan = null;
    private ArrayList<CollectionEntity> listCollection = null;

    private CollectionEntity mCollectionEntity = null;

    private CollectionEntity mCollectionEntityzan = null;
    String source;
    String zx_souse;
    String type;
    String zanjson;
    Gson g;
    String mids,collectjson,quxiaojson,id;
    String loginState,liujson;
    ImageView check_guanzhu;
    String oldmids;
    boolean is_state = false;
    boolean collect_state = false;
    boolean zan_state = false;

    Stack stack = new Stack();
    boolean dianjie = false;
    private UMImage image;
    private ProgressBar progressBar;
    private String pubdate;
    ImageView back;
    private String strTime;
    private String leaderette;
    private String lead;
    private  String   ips;
    Handler handlers=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                Toast.makeText(ZixunDetailsActivity.this, "请您登录", Toast.LENGTH_SHORT).show();
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
            if(msg.what==0){
                Gson g=new Gson();
                liujson = (String)msg.obj;
                Codes codes = g.fromJson(liujson, Codes.class);

            }
            if (msg.what == 1) {
                Gson g = new Gson();
                try {
                    json = (String)msg.obj;
                    Details details = g.fromJson(json, Details.class);

                    data = details.getData();


                    bodys = data.getBody();
                    title = data.getTitle();
                    source = data.getSource();
                    pubdate=data.getPubdate();
                    leaderette = data.leaderette;
                    strTime = TimeUtils.getStrTime(pubdate);
                    webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
                    webview.setHapticFeedbackEnabled(false);
                    webview.getSettings().setSupportZoom(false);
                    webview.getSettings().setDomStorageEnabled(false);
                    webview.getSettings().setSupportMultipleWindows(true);
                    webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


                      if(data.getTypeid().equals("1") ) {
                          zx_yuedu();
                    }else{
                          zx_yuedu();
                    }
                    pic=data.getLitpic();


                } catch (Exception e) {

                }
            }
            if(msg.what==2){
                if (g==null){
                    g=new Gson();
                }
                zanjson = (String)msg.obj;
                ZanCode zanCode = g.fromJson(zanjson,ZanCode.class);
                int code = zanCode.code;

                if(zanCode.code==1){
                    String aids=zanCode.data.aid;
                    CollectionEntity value = new CollectionEntity();
                    value.aid = aid;
                    value.title = title;
                    value.upFlag = 0;
                    value.updateTime = System.currentTimeMillis()+"";
                    value.type = name;
                    value.pic = pic;
                    value.iscollect="0";
                    value.isAdd = 0;
//                    try {
//                        if(data!=null){
//                            value.click = data.click;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    value.image=data.image;
//                    value.imageState = data.imageState;
//                    value.description = data.description;
//                    if(data.getArea_cate() != null){
//                        value.area_cate = data.getArea_cate().getArea_cate1();
//                    }
//                    value.is_academician = data.getIs_academician();
                    maiDianCollectionzan.insert(value);

                    check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
                    Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }else if(zanCode.code==-1){
                    if(zanCode.message.contains("已点过赞")){
                        Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                        CollectionEntity value = new CollectionEntity();
                        value.aid = aid;
                        value.title = title;
                        value.upFlag = 0;
                        value.updateTime = System.currentTimeMillis()+"";
                        value.type = name;
                        value.pic = pic;
                        value.iscollect="0";
                        value.isAdd = 0;
//                        value.click = data.click;
//                        value.image=data.image;
//                        value.imageState = data.imageState;
//                        value.description = data.description;
//                        if(data.getArea_cate() != null){
//                            value.area_cate = data.getArea_cate().getArea_cate1();
//                        }
//                        value.is_academician = data.getIs_academician();
                        maiDianCollectionzan.insert(value);
                        check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
                    }

                }

            }
            if(msg.what==3){
                if (g==null){
                    g=new Gson();
                }
                collectjson = (String)msg.obj;
                ZanCode zanCode = g.fromJson(collectjson,ZanCode.class);
                if(zanCode.code==1){
                    collect_state = true;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
                    id = zanCode.data.id;
                    CollectionEntity value = new CollectionEntity();
                    value.aid = aid;
                    value.title = title;
                    value.upFlag = 0;
                    value.updateTime = System.currentTimeMillis()+"";
                    value.type = name;
                    value.pic = pic;
                    value.iscollect="0";
                    value.pid=id;
                    value.isAdd = 0;
                    try{
                        value.image=data.image;
                        value.click = data.click;
                        value.imageState = data.imageState;
                        value.description = data.description;
                        if(data.getArea_cate() != null){
                            value.area_cate = data.getArea_cate().getArea_cate1();
                        }
                        value.is_academician = data.getIs_academician();
                    }catch (Exception e) {

                    }

                    maiDianCollection.insert(value);
                    Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }else if(zanCode.code==-1){
                    Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }

            if(msg.what==4){
                if (g==null){
                    g=new Gson();
                }
                quxiaojson = (String)msg.obj;
                ZanCode zanCode = g.fromJson(quxiaojson,ZanCode.class);
                if(zanCode.code==1){
                    collect_state = false;
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    maiDianCollection.deletebyaid(aid);
                    Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }else if(zanCode.code==-1){
                    Toast.makeText(ZixunDetailsActivity.this, zanCode.message, Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    private void zx_yuedu() {
        replace = str.replace("{{title}}", title);
        newhtml = replace.replace("{{body}}", bodys);
        zx_souse=newhtml.replace("{souse}",strTime);
        if(leaderette!=null && !leaderette.equals("")){
            zx_souse=zx_souse.replace("{leaderette}",leaderette);
        }else{
            zx_souse=zx_souse.replace("{leaderette}","");
        }
        zx_souse=zx_souse.replace("{listpic}",data.getLitpic());

        int index = 0;
        if(data.interpretation != null){
            if(data.interpretation.size()==1){
                Interation item = data.interpretation.get(0);
                if(item.title.equals("")){
                    zx_souse= zx_souse.replace("{{title_source}}","");
                }else{
                    zx_souse= zx_souse.replace("{{title_source}}","相关阅读");
                }
            }else{
                zx_souse= zx_souse.replace("{{title_source}}","相关阅读");
            }

            for(int i=0; i<data.interpretation.size(); i++){
                Interation item = data.interpretation.get(i);
                if(i == 0){
                    zx_souse =zx_souse.replace("{{title0}}",item.title);
                    zx_souse=zx_souse.replace("{url}",item.litpic);
                    zx_souse=zx_souse.replace("{click1}",item.click);
                    if(item.typeid.equals("1")){
                        zx_souse=zx_souse.replace("{typeid1}","资讯");
                    }else if(item.typeid.equals("6")){
                        zx_souse=zx_souse.replace("{typeid1}","政策");
                    }else if(item.typeid.equals("9")){
                        zx_souse=zx_souse.replace("{typeid1}","推荐");
                    }else if(item.typeid.equals("2")){
                        zx_souse=zx_souse.replace("{typeid1}","项目");
                    }else if(item.typeid.equals("4")){
                        zx_souse=zx_souse.replace("{typeid1}","专家");
                    }else if(item.typeid.equals("7")){
                        zx_souse=zx_souse.replace("{typeid1}","设备");
                    }else if(item.typeid.equals("5")){
                        zx_souse=zx_souse.replace("{typeid1}","专利");
                    }else if(item.typeid.equals("8")){
                        zx_souse=zx_souse.replace("{typeid1}","研究所");
                    }else if(item.typeid.equals("11")){
                        zx_souse=zx_souse.replace("{typeid1}","活动");
                    }else{
                        zx_souse=zx_souse.replace("{typeid1}","资讯");
                    }

                }else if(i ==1 ){
                    zx_souse = zx_souse.replace("{{title1}}", item.title);
                    zx_souse=zx_souse.replace("{ur2}",item.litpic);
                    zx_souse=zx_souse.replace("{click2}",item.click);
                    if(item.typeid.equals("1")){
                        zx_souse=zx_souse.replace("{typeid2}","资讯");
                    }else if(item.typeid.equals("6")){
                        zx_souse=zx_souse.replace("{typeid2}","政策");
                    }else if(item.typeid.equals("9")){
                        zx_souse=zx_souse.replace("{typeid2}","推荐");
                    }else if(item.typeid.equals("2")){
                        zx_souse=zx_souse.replace("{typeid2}","项目");
                    }else if(item.typeid.equals("4")){
                        zx_souse=zx_souse.replace("{typeid2}","专家");
                    }else if(item.typeid.equals("7")){
                        zx_souse=zx_souse.replace("{typeid2}","设备");
                    }else if(item.typeid.equals("5")){
                        zx_souse=zx_souse.replace("{typeid2}","专利");
                    }else if(item.typeid.equals("8")){
                        zx_souse=zx_souse.replace("{typeid2}","研究所");
                    }else if(item.typeid.equals("11")){
                        zx_souse=zx_souse.replace("{typeid2}","活动");
                    }else{
                        zx_souse=zx_souse.replace("{typeid2}","资讯");
                    }
                }else if(i == 2){
                    zx_souse = zx_souse.replace("{{title2}}", item.title);
                    zx_souse=zx_souse.replace("{ur3}",item.litpic);
                    zx_souse=zx_souse.replace("{click3}",item.click);
                    if(item.typeid.equals("1")){
                        zx_souse=zx_souse.replace("{typeid3}","资讯");
                    }else if(item.typeid.equals("6")){
                        zx_souse=zx_souse.replace("{typeid3}","政策");
                    }else if(item.typeid.equals("9")){
                        zx_souse=zx_souse.replace("{typeid3}","推荐");
                    }else if(item.typeid.equals("2")){
                        zx_souse=zx_souse.replace("{typeid3}","项目");
                    }else if(item.typeid.equals("4")){
                        zx_souse=zx_souse.replace("{typeid3}","专家");
                    }else if(item.typeid.equals("7")){
                        zx_souse=zx_souse.replace("{typeid3}","设备");
                    }else if(item.typeid.equals("5")){
                        zx_souse=zx_souse.replace("{typeid3}","专利");
                    }else if(item.typeid.equals("8")){
                        zx_souse=zx_souse.replace("{typeid3}","研究所");
                    }else if(item.typeid.equals("11")){
                        zx_souse=zx_souse.replace("{typeid3}","活动");
                    }else{
                        zx_souse=zx_souse.replace("{typeid3}","资讯");
                    }
                }else if(i==3){
                    zx_souse = zx_souse.replace("{{title3}}", item.title);
                    zx_souse=zx_souse.replace("{ur4}",item.litpic);
                    zx_souse=zx_souse.replace("{click4}",item.click);
                    if(item.typeid.equals("1")){
                        zx_souse=zx_souse.replace("{typeid4}","资讯");
                    }else if(item.typeid.equals("6")){
                        zx_souse=zx_souse.replace("{typeid4}","政策");
                    }else if(item.typeid.equals("9")){
                        zx_souse=zx_souse.replace("{typeid4}","推荐");
                    }else if(item.typeid.equals("2")){
                        zx_souse=zx_souse.replace("{typeid4}","项目");
                    }else if(item.typeid.equals("4")){
                        zx_souse=zx_souse.replace("{typeid4}","专家");
                    }else if(item.typeid.equals("7")){
                        zx_souse=zx_souse.replace("{typeid4}","设备");
                    }else if(item.typeid.equals("5")){
                        zx_souse=zx_souse.replace("{typeid4}","专利");
                    }else if(item.typeid.equals("8")){
                        zx_souse=zx_souse.replace("{typeid4}","研究所");
                    }else if(item.typeid.equals("11")){
                        zx_souse=zx_souse.replace("{typeid4}","活动");
                    }else{
                        zx_souse=zx_souse.replace("{typeid4}","资讯");
                    }
                }
                index = i;
            }
            switch (data.interpretation.size()-1){
                case 0:
                    zx_souse =  zx_souse.replace("{{title1}}","");
                    zx_souse=zx_souse.replace("{ur2}","");
                    zx_souse=zx_souse.replace("{click2}","");
                    zx_souse=zx_souse.replace("{typeid2}","");
                    zx_souse =  zx_souse.replace("{{title2}}","");
                    zx_souse=zx_souse.replace("{ur3}","");
                    zx_souse=zx_souse.replace("{click3}","");
                    zx_souse=zx_souse.replace("{typeid3}","");
                    zx_souse =  zx_souse.replace("{{title3}}","");
                    zx_souse=zx_souse.replace("{ur4}","");
                    zx_souse=zx_souse.replace("{click4}","");
                    zx_souse=zx_souse.replace("{typeid4}","");
                    break;
                case 1:
                    zx_souse =  zx_souse.replace("{{title2}}","");
                    zx_souse=zx_souse.replace("{ur3}","");
                    zx_souse=zx_souse.replace("{click3}","");
                    zx_souse=zx_souse.replace("{typeid3}","");
                    zx_souse =  zx_souse.replace("{{title3}}","");
                    zx_souse=zx_souse.replace("{ur4}","");
                    zx_souse=zx_souse.replace("{click4}","");
                    zx_souse=zx_souse.replace("{typeid4}","");
                    break;
                case 2:
                    zx_souse =  zx_souse.replace("{{title3}}","");
                    zx_souse=zx_souse.replace("{ur4}","");
                    break;
                case 3:

                    break;
            }
        }else{
            zx_souse =  zx_souse.replace("{{title_source}}","");
            zx_souse =  zx_souse.replace("{{title0}}","");
            zx_souse=zx_souse.replace("{url}","");
            zx_souse=zx_souse.replace("{click1}","");
            zx_souse=zx_souse.replace("{typeid1}","");
            zx_souse =  zx_souse.replace("{{title1}}","");
            zx_souse=zx_souse.replace("{ur2}","");
            zx_souse=zx_souse.replace("{click2}","");
            zx_souse=zx_souse.replace("{typeid2}","");
            zx_souse =  zx_souse.replace("{{title2}}","");
            zx_souse=zx_souse.replace("{ur3}","");
            zx_souse=zx_souse.replace("{click3}","");
            zx_souse=zx_souse.replace("{typeid3}","");
            zx_souse =  zx_souse.replace("{{title3}}","");
            zx_souse=zx_souse.replace("{ur4}","");
            zx_souse=zx_souse.replace("{click4}","");
            zx_souse=zx_souse.replace("{typeid4}","");

        }

        if(data.tech_resources != null){
            String kejiziyuan =
                    "\t\t\t<div class=\"introduction\">\n" +
                    "\t\t\t\t<div class=\"choice\">\n" +
                    "\t\t\t\t\t<p>科技资源</p>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t</div><div class=\"list\">";
            if(data.tech_resources.size() > 0){

                for(int i=0; i<data.tech_resources.size();i++){
                    String pos ;
                    Interation item = data.tech_resources.get(i);
                    String type="";
                    if(item.typeid.equals("4")){
                        type = "专家";
                    }else if(item.typeid.equals("6")){
                        type = "政策";
                    }else if(item.typeid.equals("8")){
                        type = "研究所";
                    }if(item.typeid.equals("5")){
                        type = "专利";
                    }if(item.typeid.equals("7")){
                        type = "设备";
                    }if(item.typeid.equals("2")){
                        type = "项目";
                    }

                    if(item.typeid.equals("4")){
                        pos = "\t<a href=\""+item.aid+"\"><div class=\"release\" id=\"yuan\"><div  class=\"personnel_list\">\t<div class=\"personnel_images\">\t\t<img id=\"images\" src=\""+item.litpic+"\" alt=\"\">\t</div>"
                        +"<div class=\"personnel_name\"><span>"+item.title+"</span><br><span id=\"lingyu1\">所属领域："+item.area_cate.getArea_cate1()+"</span></div></div>"+
                        "<div  class=\"content\">"+item.description+"</div><div class=\"classification\">人才</div>"+"<div class=\"browse\"><img src=\"./images/chakan@2x.png\" alt=\"\"><span>"+item.click+"</span></div></div>\n" +
                                "\t\t\t\t</a>";
                        kejiziyuan+= pos;
                    }else {
                        if(item.litpic != null && !item.litpic.equals("")){

                            pos = "<a href=\""+item.aid+"\">"+"\t<div class=\"release\" id=\"fang\">\n" +
                                    "\t\t\t\t\t<div  class=\"personnel_list\">\n" +
                                    "\t\t\t\t\t\t<div class=\"personnel_images_fang\">\n" +
                                    "\t\t\t\t\t\t\t<img  src=\""+item.litpic+"\" alt=\"\">\n" +
                                    "\t\t\t\t\t\t</div>"+"<div class=\"personnel_name\"><span>"+item.title+"</span><br><span id=\"lingyu2\">所属领域："+item.area_cate.getArea_cate1()+"</span></div>\n" +
                                    "\t\t\t\t\t</div><div  class=\"content\">"+item.description+"</div><div class=\"classification\">"+type+"</div><div class=\"browse\"><img src=\"./images/chakan@2x.png\" alt=\"\"><span>"+item.click+"</span></div></div>\n" +
                                    "\t\t\t\t</a>";
                            kejiziyuan+= pos;
                        }else{
                            pos = "<a href=\""+item.aid+"\">\n" +
                                    "\t\t\t\t<div class=\"release\" id=\"wutu\">\n" +
                                    "\t\t\t\t\t<div  class=\"personnel_list\"><div class=\"personnel_name_null\"><span>"+item.title+"</span><br><span id=\"lingyu3\">所属领域："+item.area_cate.getArea_cate1()+"</span></div>\n" +
                                    "\t\t\t\t\t</div>\t<div  class=\"content\">"+item.description+"</div><div class=\"classification\">"+type+"</div><div class=\"browse\"><img src=\"./images/chakan@2x.png\" alt=\"\"><span>"+item.click+"</span></div>\n" +
                                    "\t\t\t\t</div>\n" +
                                    "\t\t\t\t</a>";
                            kejiziyuan+= pos;

                        }
                    }

                }
                kejiziyuan= kejiziyuan+"\t</div>\n"
                        ;
            }
//            this.zx_souse =this.zx_souse+ kejiziyuan;
            zx_souse=zx_souse.replace("{kejiziyuan}",kejiziyuan);

        }

        String baseUrl = "file:///android_asset/";

        webview.loadDataWithBaseURL(baseUrl, ZixunDetailsActivity.this.zx_souse, "text/html",
                "utf-8", null);
    }

    int x =0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        setsetSlidingLayou(ev);
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
        setContentView(R.layout.zixun_details);
        is_state = true;
        dianjie = false;
        maiDianCollection = MaiDianCollection.getInstance(this);
        maiDianCollectionzan=MaiDianZan.getInstance(this);
        mShareAPI = UMShareAPI.get(this);
        try {
            Intent intent = getIntent();
            aid = intent.getStringExtra("id");
            Log.i("xuecheng",aid);
            ips = MyApplication.ip;
            name = intent.getStringExtra("name");
            initView();
            int netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                Toast.makeText(ZixunDetailsActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();

            } else {
                liulang();
            }

        }catch (Exception e){}


        if(mCollectionEntity==null){
            mCollectionEntity=new CollectionEntity();
            mCollectionEntity.aid = aid;
            mCollectionEntity.title = title;
            mCollectionEntity.upFlag = 0;
            mCollectionEntity.updateTime = System.currentTimeMillis()+"";
            mCollectionEntity.type = name;
            mCollectionEntity.pic = pic;

        }
        progressBar = (ProgressBar)findViewById(R.id.progress);
        webview = (WebView) findViewById(R.id.wbviw);
        webview.setWebChromeClient(new ZixunDetailsActivity.WebChromeClient());

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        webview.setWebViewClient(new ZixunDetailsActivity.SampleWebViewClient());
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(ZixunDetailsActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();

        } else {
            progressBar.setVisibility(View.VISIBLE);
            oldmids = aid;
            getjson();
        }
        //html转成string字符串
        try {
            str = new String(toByteArray(getAssets().open("information.html")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void liulang() {
        HashMap<String,String> map = new HashMap<>();
        map.put("aid",aid);
        String url= "http://" + MyApplication.ip + "/api/arc_click.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,0,0);
//        Okhttp = OkHttpUtils.getInstancesOkHttp();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                String timestamp = System.currentTimeMillis() + "";
//                String sign = "";
//                ArrayList<String> sort = new ArrayList<String>();
//                sort.add("aid" + aid);
//                sort.add("timestamp" + timestamp);
//                sort.add("version"+MyApplication.version);
//                sign = KeySort.keyScort(sort);
//                liujson = Okhttp.Myokhttpclient("http://" + ips + "/api/arc_click.php?aid=" + aid + "&sign=" + sign + "&timestamp=" + timestamp+"&version="+MyApplication.version);
//                Message msg = new Message();
//                msg.what = 0;
//                handler.sendMessage(msg);
//            }
//        }.start();
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
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webview.canGoBack()){
                    webview.goBack();
                }else{
                    finish();
                }
            }
        });

        shares = (ImageView) findViewById(R.id.shares);

        my_collect = (ImageView) findViewById(R.id.my_collect);

        check_guanzhu=(ImageView) findViewById(R.id.check_guanzhu);


//
//        if(name.equals("资讯")||name.equals("政策")||name.equals("专利")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识")){
//
//        }

        listCollections = maiDianCollection.get();

        if(listCollections.size() >0){
            for(int i=0; i<listCollections.size(); i++){
                CollectionEntity item = listCollections.get(i);
                if(item != null && item.aid != null){
                    if(item.aid.equals(aid)){
                        collect_state = true;
                        mCollectionEntity = item;
                        break;
                    }
                }
            }
        }
        if(collect_state){
            my_collect.setBackgroundResource(R.mipmap.zixun_collect_h);
        }else{
            my_collect.setBackgroundResource(R.mipmap.zixun_collect);
        }

        my_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dianjie){
                    return;
                }
                dianjie = true;
                if(!loginState.equals("1")) {
                    my_collect.setBackgroundResource(R.mipmap.zixun_collect);
                    Toast.makeText(ZixunDetailsActivity.this, "请您登录", Toast.LENGTH_SHORT).show();
                }else{
                    if(collect_state == false){
                        listCollections = maiDianCollection.getcollect();
                        for(int i=0;i<listCollections.size();i++){
                            CollectionEntity  item = listCollections.get(i);
                            if(item.aid.equals(aid)){
                                return;
                            }
                        }
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(ZixunDetailsActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        }else{
                            collectjson();
                        }

                    }else{
                        int netWorkType = NetUtils.getNetWorkType(MyApplication
                                .getContext());
                        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                            Toast.makeText(ZixunDetailsActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();

                        }else{
                            dismisjson();
                        }

                    }
                }
                dianjie = false;
                }
        });

        listCollectionzan = maiDianCollectionzan.get();


        if(listCollectionzan.size() >0){
            for(int i=0; i<listCollectionzan.size(); i++){
                CollectionEntity item = listCollectionzan.get(i);
                if(item != null && item.aid != null){
                    if(item.aid.equals(aid)){

                        zan_state = true;
                        mCollectionEntityzan = item;
                        break;
                    }
                }
            }
        }
        if(zan_state){
            check_guanzhu.setBackgroundResource(R.mipmap.zixun_like_h);
        }else{
            check_guanzhu.setBackgroundResource(R.mipmap.zixun_like);
        }

        check_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCollectionEntityzan!=null){
                    Toast.makeText(ZixunDetailsActivity.this, "已经点过赞", Toast.LENGTH_SHORT).show();
                    return;
                }
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(ZixunDetailsActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();

                }else{
                    zanjson();
                }
            }
        });

        try {
            shares.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(ZixunDetailsActivity.this, ShareActivity.class);
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
//                        UIHelper.showDialogForLoading(ZixunDetailsActivity.this, "", true);
//                        if(pic==null|| pic.equals("")){
//
//                            image = new UMImage(ZixunDetailsActivity.this, "http://"+ips+"/uploads/logo/logo1.png");
//
//                        }else {
//                            image = new UMImage(ZixunDetailsActivity.this, pic);
//                        }
//
//                        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                                {
//                                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                                };
//                        action = new ShareAction(ZixunDetailsActivity.this);
//                        UIHelper.hideDialogForLoading();
//                        if(data.getTypeid().equals("7")){
//                            action.setDisplayList(displaylist)
//
//                                    .withText(data.getFunctional())
//                                    .withTitle(title)
//                                    .withTargetUrl("http://"+ips+"/api/share.php?aid="+aid+"")
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .open();
//                        }
//                        else {
//                            String type= String.valueOf(Html.fromHtml(bodys));
//                            if(type.length()>512){
//                                type=type.substring(0,256);
//                            }
//                            action.setDisplayList(displaylist)
//                                    .withText(type)
//                                    .withTitle(title)
//                                    .withTargetUrl("http://"+ips+"/api/share.php?aid="+aid+"")
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .open();
//                        }
                    }catch (Exception e){}
                }
            });
        }catch (Exception e){}




    }
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public void dismisjson() {

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

        String url ="http://" + MyApplication.ip + "/api/arc_store.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();

        map.put("id", id);
        map.put("mid", mids);
        map.put("method","cancel");
        networkCom.getJson(url,map,handler,4,0);


    }
    public void collectjson(){
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        String url ="http://" + MyApplication.ip + "/api/arc_store.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("aid",aid);
        map.put("mid" , mids);
        map.put("method" , "add");
        networkCom.getJson(url, map, handler,3,0);

    }

    public void zanjson() {

        String url ="http://" + MyApplication.ip + "/api/arc_zan.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();


        Okhttp = OkHttpUtils.getInstancesOkHttp();
        try {

            if (!loginState.equals("1")) {
                Message msg = new Message();
                msg.what = 3;
                handlers.sendMessage(msg);
            } else {
                map.put("aid" , aid);
                map.put("mid" , mids);
                map.put("tag", "1");
                networkCom.getJson(url,map,handler,2,0);

            }



        } catch (Exception e) {
        }

    }


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
            Toast.makeText(ZixunDetailsActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
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
        Okhttp =  OkHttpUtils.getInstancesOkHttp();
        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        deviceid = MyApplication.deviceid;
        String url ="http://"+MyApplication.ip+"/api/arc_detail.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("aid", aid);
            map.put("mid", mids);
            map.put("deviceid", deviceid);
            map.put("typeid","1");
            networkCom.getJson(url,map,handler,1,0);
        } catch (Exception e) {
        }
    }

    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
        MobclickAgent.onPageStart("资讯详情");
        MobclickAgent.onResume(this);
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

    }

    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        MobclickAgent.onPageEnd("资讯详情");
        MobclickAgent.onPause(this);
    }

    class SampleWebViewClient extends WebViewClient {


        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            is_state = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            try {
                Interation item =null;
                if(url != null){
                    if(url.contains("a00")){
                        item = data.interpretation.get(0);
                    }else if(url.contains("a11")){
                        item = data.interpretation.get(1);
                    }else if(url.contains("a22")){
                        item = data.interpretation.get(2);
                    }/*else if(url.contains("33")){
                        item = data.interpretation.get(3);
                    }*/else{
                        if(data != null){
                            if(data.tech_resources != null){
                                for(int i =0; i<data.tech_resources.size();i++){
                                    Interation pos = data.tech_resources.get(i);
                                    if(url.contains(pos.aid)){
                                        item = pos;
                                        break;
                                    }
                                }
                            }
                        }

                    }
                    if(item.typeid.equals("6")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "政策");
                        startActivity(intent);
                    }else if(item.typeid.equals("8")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "研究所");
                        startActivity(intent);
                    }else if(item.typeid.equals("4")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "专家");
                        startActivity(intent);
                    }else if(item.typeid.equals("5")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "专利");
                        startActivity(intent);
                    }else if(item.typeid.equals("7")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "设备");
                        startActivity(intent);
                    }else if(item.typeid.equals("2")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "项目");
                        startActivity(intent);
                    }else if(item.typeid.equals("1")){
                        Intent intent = new Intent(ZixunDetailsActivity.this, DetailsActivity.class);
                        intent.putExtra("id", item.aid);
                        intent.putExtra("name", "资讯");
                        startActivity(intent);
                    }else{
                        stack.push(oldmids);
                        aid = item.aid;
                        is_state = true;
                        progressBar.setVisibility(View.VISIBLE);
                        getjson();
                    }


                }
//            }catch (Exception e){}

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setScrollY(0);
            view.setScrollX(0);
            view.scrollTo(0, 0);
            super.onPageFinished(view, url);
            if(!is_state){
                boolean state = stack.empty();
                if(!state){
                    is_state = true;
                    aid = (String) stack.pop();
                    progressBar.setVisibility(View.VISIBLE);
                    is_state = true;
                    getjson();
                }else{
                    finish();
                }
            }
            is_state = false;
            oldmids = aid;
        }
    }
    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                view.setScrollY(0);
                view.setScrollX(0);
                view.scrollTo(0, 0);
                progressBar.setVisibility(View.GONE);
            }

        }
    }

    private void Connection_technology(){

    }

}
