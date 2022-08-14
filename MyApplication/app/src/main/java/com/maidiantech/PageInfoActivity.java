package com.maidiantech;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Util.EditCheckUtils;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.Codes;
import entity.Datas;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2017/5/2.
 */

public class PageInfoActivity extends AutoLayoutActivity {
    private int netWorkType;
    private ImageView modifyBack;
    private RelativeLayout rlSearchFrameDelete;
    private AutoCompleteTextView nameSearch;
    private ImageView nameIvDeleteText;
    private String nickname,pname,infomid,inforjson,tel;
    Button infosubmit;
    OkHttpUtils utils;
    private Datas data;
    private HashMap<String,String> infomap = new HashMap<>();
    private  String   ips;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageinfo);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);

        initView();
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(PageInfoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) nameSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(nameSearch, 0);
                               }

                           },
                    200);
        }

        set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器
    }

    private void initView() {
        modifyBack = (ImageView) findViewById(R.id.modify_back);
        rlSearchFrameDelete = (RelativeLayout) findViewById(R.id.rlSearchFrameDelete);
        nameSearch = (AutoCompleteTextView) findViewById(R.id.name_search);
        nameIvDeleteText = (ImageView) findViewById(R.id.name_ivDeleteText);
        infosubmit = (Button) findViewById(R.id.info_submit);

        modifyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                PageInfoActivity.this.finish();
            }
        });
        infosubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pname=  nameSearch.getText().toString().trim();
                String checkResult = EditCheckUtils.submitinfo(pname);
                if (!checkResult.equals("")) {
                    Toast.makeText(PageInfoActivity.this, checkResult, Toast.LENGTH_SHORT).show();

                }
//                else if( !TelNumMatch.isValidPhoneNumber(pphone)){
//
//                        Toast.makeText(MydataActivityInfo.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//
//                }
//                else if(infoyzm==null||infoyzm.equals("")){
//                    Toast.makeText(MydataActivityInfo.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//                }
//                else  if(!TelNumMatch.isEmails(pemail)){
//                    Toast.makeText(MydataActivityInfo.this, "请输入正确的email", Toast.LENGTH_SHORT).show();
//                }
//                else if(!TelNumMatch.isEmail(pemail)){
//                    Toast.makeText(MydataActivityInfo.this, "Emali地址无效", Toast.LENGTH_SHORT).show();
//                }
                else{
                    infomid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                    tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
                    infomap.put("mid",infomid);
                    infomap.put("uname",pname);
                    infomap.put("tel",tel);
                    infomap.put("email","");
                    infomap.put("order","1");
                    infomap.put("version", MyApplication.version);
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("mid"+infomid);
                    sort.add("uname"+pname);
                    sort.add("tel"+tel);
                    sort.add("email"+"");
                    sort.add("order"+"1");
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid" + accessid);
                    sign = KeySort.keyScort(sort);
                    infomap.put("sign",sign);
                    infomap.put("timestamp",timestamp);
                    infomap.put("accessid",accessid);
//                    try {
                    hintKbTwo();
                    infojson();
//                    }catch (Exception e){}

                }
            }
        });
    }
    private void set_eSearch_TextChanged() {
        nameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    nameIvDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
//                    adapter.notifyDataSetChanged();
                   /* search_listview.setVisibility(View.GONE);
                    search_gone.setVisibility(View.VISIBLE);*/
                }
                else {
                    nameIvDeleteText.setVisibility(View.GONE);//当文本框不为空时，出现叉叉
                    if(s.length()>16){
                         String txt =s.toString().substring(0,15);
                         nameSearch.setText(txt);
                    }
                    nameIvDeleteText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nameSearch.setText("");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "");
        nameSearch.setText(nickname);
        nameSearch.setSelection(nickname.length());
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

    private void infojson() {

//        pname=  nameSearch.getText().toString().trim();
//        if(pname.equals(nickname)  ){
//            Toast.makeText(this, "您没有修改信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
        utils= OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String registstr="http://"+ips+"/api/user_edit.php";
                    inforjson = OkHttpUtils.postkeyvlauspainr(registstr, infomap);

                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);

                }
            }.start();
        }catch (Exception e){}


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    Gson g=new Gson();
                    Codes codes = g.fromJson(inforjson, Codes.class);
                    data = codes.getData();
                    if(codes.code==1){
                        Toast.makeText(PageInfoActivity.this, codes.message, Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.nickname);
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.uname);
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);

                        finish();

                    }else if(codes.code==-1){
                        Toast.makeText(PageInfoActivity.this, codes.message, Toast.LENGTH_SHORT).show();
                    }

                }
            }catch (Exception e){

            }

        }
    };
}
