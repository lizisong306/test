package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import application.ImageLoaderUtils;
import application.MyApplication;
import dao.dbentity.PulseData;
import entity.Area_cate;
import entity.BGFONT;
import entity.Details;
import entity.Posts;
import entity.Ret;
import entity.RetPulseData;
import entity.TipData;
import entity.TipEntry;
import entity.XuQiuRetEntity;
import entity.recode;
import view.AutoLinefeedLayout;
import view.BTAlertDialog;
import view.RoundImageView;
import view.ShapeImageView;
import Util.SharedPreferencesUtil;
import Util.NetUtils;
import static application.MyApplication.deviceid;

/**
 * Created by Administrator on 2019/8/20.
 */

public class TianXuQiu extends AutoLayoutActivity {
    ImageView backs;
    RelativeLayout lay,lay2;
    String aid,typeid="0", id;
    ShapeImageView img;
    TextView xm_title,linyu,wutu_rencai,xm_title2,rank,linyu2,count,tijiao;
    RoundImageView img2;
    EditText text;
    AutoLinefeedLayout hotView;
    Posts item;
    private DisplayImageOptions options;
    List<TipData> tips = new ArrayList<>();
    List<BGFONT> bgfontList = new ArrayList<>();
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
        setContentView(R.layout.tianxuqiu);
        options = ImageLoaderUtils.initOptions();
        backs = (ImageView)findViewById(R.id.backs);
        aid = getIntent().getStringExtra("aid");
        typeid = getIntent().getStringExtra("typeid");
        lay = (RelativeLayout)findViewById(R.id.lay);
        lay2 = (RelativeLayout)findViewById(R.id.lay2);
        img =(ShapeImageView)findViewById(R.id.img);
        xm_title = (TextView)findViewById(R.id.xm_title);
        linyu = (TextView)findViewById(R.id.linyu);
        wutu_rencai = (TextView)findViewById(R.id.wutu_rencai);
        xm_title2 = (TextView)findViewById(R.id.xm_title2);
        rank = (TextView)findViewById(R.id.rank);
        linyu2 = (TextView)findViewById(R.id.linyu2);
        count = (TextView)findViewById(R.id.count);
        img2 = (RoundImageView)findViewById(R.id.img2);
        text = (EditText)findViewById(R.id.text);
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);
        text.setImeOptions(EditorInfo.IME_ACTION_SEND);
        tijiao = (TextView)findViewById(R.id.tijiao);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0 || actionId == 3 || actionId == 5) {
                    int   netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(TianXuQiu.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        hintKbTwo();
                        String loginstate = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                        if(loginstate.equals("1")){
                            String txt = text.getText().toString();

                            if(txt == null || txt.equals("")){
                                Toast.makeText(TianXuQiu.this, "请填写约见的主题及诉求",Toast.LENGTH_SHORT).show();
                            }else{
                                if(event.getAction() == KeyEvent.ACTION_UP){
                                    tijiaopost();
                                }
                            }
                        }else{
                            Intent intent = new Intent(TianXuQiu.this, MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        text.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TianXuQiu.this.count.setText(s.toString().length()+"/200");
                if(s.toString().length() >200){
                    text.setText(s.toString().substring(0,200));
                    TianXuQiu.this.count.setText(200+"/200");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginstate = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(loginstate.equals("1")){
                    String txt = text.getText().toString();
                    if(txt == null || txt.equals("")){
                        Toast.makeText(TianXuQiu.this, "请填写约见的主题及诉求",Toast.LENGTH_SHORT).show();
                    }else{
                        tijiaopost();
                    }
                }else{
                    Intent intent = new Intent(TianXuQiu.this, MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });
        BGFONT item1 = new BGFONT();
        item1.res=R.drawable.shape_hot1;
        item1.color = 0xfff5846d;
        bgfontList.add(item1);

        BGFONT item2 = new BGFONT();
        item2.res=R.drawable.shape_hot2;
        item2.color = 0xffdc7e6e;
        bgfontList.add(item2);

        BGFONT item3 = new BGFONT();
        item3.res=R.drawable.shape_hot3;
        item3.color = 0xffffb178;
        bgfontList.add(item3);

        BGFONT item4 = new BGFONT();
        item4.res=R.drawable.shape_hot4;
        item4.color = 0xfffba281;
        bgfontList.add(item4);

        BGFONT item5 = new BGFONT();
        item5.res=R.drawable.shape_hot5;
        item5.color = 0xfff7a172;
        bgfontList.add(item5);

        if(!typeid.equals("0")){
            getJson();
        }
        getTips();
        hintKbTwo();
    }
    private void getJson(){
        try {
            String url ="http://" + MyApplication.ip + "/api/arc_detail.php";
            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("aid", aid);
            map.put("mid" ,mid);
            map.put("deviceid" ,deviceid);
            if (typeid.equals("2")) {
                lay.setVisibility(View.VISIBLE);
                map.put("typeid" ,"2");
            }else if(typeid.equals("4")){
                lay2.setVisibility(View.VISIBLE);
                map.put("typeid" ,"4");
            }else if(typeid.equals("7")){
                lay.setVisibility(View.VISIBLE);
                map.put("typeid" ,"7");
            }
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,map,handler,2,0);
        }catch (Exception e){

        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 2){
                    Gson g=new Gson();
                    String json1 = (String)msg.obj;
                    Details details = g.fromJson(json1, Details.class);
                    Log.d("lizisong", "json1:"+json1);
                    item = new Posts();
                    if(details != null){
                        if(details.getCode()==1){
                            recode data = details.getData();
                            item.typeid = data.getTypeid();
                            item.aid = aid;
                            item.setId(aid);
                            item.setLitpic(data.getLitpic());
                            item.setDescription(data.description);
                            item.setTitle(data.getTitle());
                            item.setUsername(data.getUsername());
                            rank.setText(data.getRank());
                            if(data.getArea_cate() != null){
                                Area_cate item1 = new Area_cate();
                                item1.setArea_cate1(data.getArea_cate().getArea_cate1());
                                item.setArea_cate(item1);
                            }
                            if(item.typeid.equals("4")){
                                item.setTypename("专家");
                            }else if(item.typeid.equals("2")){
                                item.setTypename("项目");
                            }else if(item.typeid.equals("7")){
                                item.setTypename("设备");
                            }
                            if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI) {
                                        if(typeid.equals("4")){
                                            if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                                img2.setVisibility(View.VISIBLE);
                                                wutu_rencai.setVisibility(View.GONE);
                                                ImageLoader.getInstance().displayImage(data.getLitpic()
                                                        , img2, options);
                                            }else{
                                                img2.setVisibility(View.GONE);
                                                wutu_rencai.setVisibility(View.VISIBLE);
                                                wutu_rencai.setText(data.getTitle().substring(0,1));
                                            }
                                        }else{
                                            img.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(data.getLitpic()
                                                    , img, options);
                                        }

                                    } else {
                                        if(typeid.equals("4")){
                                            img2.setBackgroundResource(R.mipmap.information_placeholder);
                                        }else{
                                            img.setBackgroundResource(R.mipmap.information_placeholder);
                                            img.setVisibility(View.VISIBLE);
                                        }

                                    }
                                } else {
                                    if(typeid.equals("4")){
                                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                                , img2, options);
                                    }else{
                                        img.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(data.getLitpic()
                                                , img, options);
                                    }
                                }
                            }
                            if(typeid.equals("4")){
                                if(data.getLitpic() != null && !data.getLitpic().equals("")){
                                    img2.setVisibility(View.VISIBLE);
                                    wutu_rencai.setVisibility(View.GONE);
                                    ImageLoader.getInstance().displayImage(data.getLitpic()
                                            , img2, options);
                                }else{
                                    img2.setVisibility(View.GONE);
                                    wutu_rencai.setVisibility(View.VISIBLE);
                                    wutu_rencai.setText(data.getTitle().substring(0,1));
                                }
                            }else{
                                if(data.getLitpic() == null || data.getLitpic().equals("")){
                                    img.setVisibility(View.VISIBLE);
                                    img.setImageResource(R.mipmap.information_placeholder);
                                }
                            }

                            xm_title.setText(data.getTitle());
                            xm_title2.setText(data.getTitle());
                            if(data.getArea_cate()!= null){
                                linyu.setText(data.getArea_cate().getArea_cate1());
                            }
                            linyu2.setText(data.getUnit());
                            lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(typeid.equals("2")){
                                        Intent intent=new Intent(TianXuQiu.this, NewProjectActivity.class);
                                        intent.putExtra("aid",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "项目");
                                        startActivity(intent);
                                    }else if(typeid.equals("4")){
//                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
//                                        intent.putExtra("id",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "专家");
//                                        startActivity(intent);
                                        Intent intent = new Intent(TianXuQiu.this, NewRenCaiTail.class);
                                        intent.putExtra("aid", aid);
                                        startActivity(intent);

                                    }else if(typeid.equals("7")){
                                        Intent intent=new Intent(TianXuQiu.this, DetailsActivity.class);
                                        intent.putExtra("id",aid);
                                        intent.putExtra("pic",item.getLitpic());
                                        intent.putExtra("name", "设备");
                                        startActivity(intent);
                                    }
                                }
                            });

                            lay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(typeid.equals("2")){
                                        Intent intent=new Intent(TianXuQiu.this, NewProjectActivity.class);
                                        intent.putExtra("aid",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "项目");
                                        startActivity(intent);
                                    }else if(typeid.equals("4")){
//                                        Intent intent=new Intent(WriteXuQiu.this, DetailsActivity.class);
//                                        intent.putExtra("id",aid);
//                                        intent.putExtra("pic",item.getLitpic());
//                                        intent.putExtra("name", "专家");
//                                        startActivity(intent);
                                        Intent intent = new Intent(TianXuQiu.this, NewRenCaiTail.class);
                                        intent.putExtra("aid", aid);
                                        startActivity(intent);
                                    }else if(typeid.equals("7")){
                                        Intent intent=new Intent(TianXuQiu.this, DetailsActivity.class);
                                        intent.putExtra("id",aid);
                                        intent.putExtra("pic",item.getLitpic());
                                        intent.putExtra("name", "设备");
                                        startActivity(intent);
                                    }
                                }
                            });


//                            AddXuQiu2.data = item;
//                            AddXuQiu.data = item;
//                            handlerData();

                        }
                    }
                }else if(msg.what == 1){
                    Gson g=new Gson();
                    String ret = (String)msg.obj;
                    TipEntry tip = g.fromJson(ret, TipEntry.class);
                    if(tip != null){
                        if(tip.code.equals("1")){
                            if(tip.data != null && tip.data.size()>0){
                                for(int i=0;i<tip.data.size();i++){
                                    TipData item  =tip.data.get(i);
                                    tips.add(item);
                                }
                            }
                            inithotView();
                        }
                    }
                }else if(msg.what == 3){
                    Gson g=new Gson();
                   String json = (String)msg.obj;
                    XuQiuRetEntity result =g.fromJson(json, XuQiuRetEntity.class);
                    if(result.code.equals("1")){
                        id = result.data.id;
                        Intent intent = new Intent(TianXuQiu.this, FaBuActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("state", "0");
                        startActivity(intent);
                        finish();
                    }
                }
            }catch (Exception e){

            }

        }
    };
    private void getTips(){
        String url="http://" + MyApplication.ip +"/api/getRequireHotWords.php";
        HashMap<String,String> map = new HashMap<>();
//        if(typeid.equals("7")){
//            map.put("typeid","0");
//        }else{
            map.put("typeid",typeid);
//        }

        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }

    private void inithotView(){
        int size = tips.size(); // 添加Button的个数.
        if(size == 0){
            return;
        }
        ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();
        if(size > 0){
            int countwidht =0;
            for(int i = 0; i < size; i++){
                String item = tips.get(i).require_keywords;
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
//                if(countwidht > 5*MyApplication.widths){
//                    break;
//                }
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setText(item);
                tv.setTag(item);
                int dex =getNum(0,4);
                BGFONT ii =bgfontList.get(dex);
                tv.setBackgroundResource(ii.res);
                tv.setTextColor(ii.color);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       final String tex = ((TextView)v).getText().toString();
                        String txt = text.getText().toString();
                        if(txt== null || (txt!=null &&txt.equals(""))){
                            text.setText(tex);
                        }else{
                            final BTAlertDialog dialog = new BTAlertDialog(TianXuQiu.this);
                            dialog.setTitle("您确认要替换吗？");
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("替换", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    text.setText(tex);
                                    dialog.dismiss();
                                    text.setSelection(tex.length());
                                }
                            });
                            dialog.show();
                        }
                    }
                });
                childBtns.add(childBtn);
                hotView.addView(childBtn);
            }
        }
    }

    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
    }


    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     * @param startNum
     * @param endNum
     * @return
     */
    public  int getNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    private void tijiaopost(){
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        String  mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", mid);
        map.put("method","add");
        map.put("entry_cate","2");
        if(aid != null){
            map.put("aid", aid);
            if(typeid.equals("4")) {
                map.put("typeid", "4");
                map.put("entry_address", "7");
            }else if(typeid.equals("2")){
                map.put("typeid", "2");
                map.put("entry_address", "8");
            }else if(typeid.equals("7")){
                map.put("typeid", "7");
                map.put("entry_address", "9");
            }
        }else{
            map.put("aid", "0");
            map.put("typeid", "0");
            map.put("entry_address", "6");
        }
        if(WriteXuQiu.entry_address != 0){
            map.put("entry_address", WriteXuQiu.entry_address+"");
        }
        map.put("content",text.getText().toString());
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,3,0);

    }

}
