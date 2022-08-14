package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Util.EditCheckUtils;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.City;
import entity.CityData;
import entity.CityEntry;
import entity.Usercode;
import entity.userlogin;
import view.StyleUtils;
import view.StyleUtils1;

import static com.maidiantech.R.id.unit_finish;


/**
 * Created by 13520 on 2017/3/7.
 */

public class UnitActivity extends AutoLayoutActivity {
    private ImageView unitBack;
    private TextView unitFinish;
    private TextView unitName;
    private TextView unitType;
    private TextView unitLocation;
    private EditText unitPhone;
    private EditText unitTitle;
    private OptionsPickerView<String> mtv;
    private OptionsPickerView<String> mOpv;
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();
    private String tx ,txs;
    private JSONObject mJsonObj;
    private ArrayList<String> linyu=new ArrayList<>();
    private String uname,utype,ulocation,uphone,utitle,infomid,company,adress,mobiles,types,zhiwei,inforjson;
    private HashMap<String,String> compmap = new HashMap<>();
    OkHttpUtils utils;
    private  String   ips;
    HashMap<String,String> hashMap;
    private ArrayList<ArrayList<String>> mListCiryCode = new ArrayList<ArrayList<String>>();
    private ArrayList<String> mListProvinceCode = new ArrayList<String>();
    String json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unti_layout);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
        linyu.add("国有企业");
        linyu.add("国有控股");
        linyu.add("外资企业");
        linyu.add("合资企业");
        linyu.add("私营企业");
        linyu.add("事业单位");
        linyu.add("行政机关");
        linyu.add("其他");

        mtv=new OptionsPickerView<String>(this);
        // 设置标题
        mtv.setTitle("单位类型");
        mtv.setPicker(linyu);
        mtv.setCyclic(false);
        mtv.setSelectOptions(0);
        // 监听确定选择按钮
        mtv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                tx = linyu.get(options1);
                unitType.setText(tx);
                if(tx != null && !tx.equals("")){
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    sort.add("mid"+mid);
                    sort.add("timestamp"+timestamp);
                    sort.add("ctype"+tx);
                    sort.add("version"+MyApplication.version);
                    sort.add("accessid" + mid);
                    sign = KeySort.keyScort(sort);

                    hashMap = new HashMap<String, String>();
                    hashMap.put("mid",mid);
                    hashMap.put("timestamp",timestamp);
                    hashMap.put("sign",sign);
                    hashMap.put("ctype",tx);
                    hashMap.put("version",MyApplication.version);
                    hashMap.put("accessid",mid);
                    xiugai();
                }else{
                    Toast.makeText(UnitActivity.this, "单位信息不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 点击弹出选项选择器
        unitType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mtv.show();
            }
        });
        // 初始化Json对象
        initJsonData();
        // 初始化Json数据
        initJsonDatas();

        // 创建选项选择器对象
        mOpv = new OptionsPickerView<String>(this);

        // 设置标题
        mOpv.setTitle("所在地");


        // 设置三级联动效果
        mOpv.setPicker(mListProvince, mListCiry, null, true);


        // 设置是否循环滚动
        mOpv.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        mOpv.setSelectOptions(0, 0);

        // 监听确定选择按钮
        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                txs = mListProvince.get(options1) +"    "+ mListCiry.get(options1).get(option2) ;
                unitLocation.setText(txs);
                if(txs != null && !txs.equals("")){
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    sort.add("mid"+mid);
                    sort.add("timestamp"+timestamp);
                    sort.add("address"+txs);
                    sort.add("version"+MyApplication.version);
                    sort.add("accessid" + mid);
                    sign = KeySort.keyScort(sort);

                    hashMap = new HashMap<String, String>();
                    hashMap.put("mid",mid);
                    hashMap.put("timestamp",timestamp);
                    hashMap.put("sign",sign);
                    hashMap.put("address",txs);
                    hashMap.put("version",MyApplication.version);
                    hashMap.put("accessid",mid);
                    xiugai();
                }else{
                    Toast.makeText(UnitActivity.this, "单位信息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 点击弹出选项选择器
        unitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mOpv.show();
            }
        });
        unitFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=unitName.getText().toString().trim();
                utype=unitType.getText().toString().trim();
                ulocation=unitLocation.getText().toString().trim();
                uphone=unitPhone.getText().toString().trim();
                utitle=unitTitle.getText().toString().trim();
                String checkResult = EditCheckUtils.unit_info(uname,utype,ulocation,uphone,utitle);
                if (!checkResult.equals("")) {
                    Toast.makeText(UnitActivity.this, checkResult, Toast.LENGTH_SHORT).show();

                }else if( !TelNumMatch.isValidPhoneNumber(uphone)){

                    Toast.makeText(UnitActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();

                }else {
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    infomid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                    compmap.put("mid",infomid);
                    compmap.put("order","2");
                    compmap.put("company",uname);
                    compmap.put("ctype",utype);
                    compmap.put("address",ulocation);
                    compmap.put("mobile",uphone);
                    compmap.put("profession",utitle);
                    compmap.put("version", MyApplication.version);

                    sort.add("mid"+infomid);
                    sort.add("order"+"2");
                    sort.add("company"+uname);
                    sort.add("ctype"+utype);
                    sort.add("address"+ulocation);
                    sort.add("mobile"+uphone);
                    sort.add("profession"+utitle);
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
                    compmap.put("sign", sign);
                    compmap.put("timestamp", timestamp);
                    compmap.put("accessid", accessid);
                    hintKbTwo();
                    int netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(UnitActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                      compjson();
                    }
                }
            }
        });
    }

    private void compjson() {
        uname=unitName.getText().toString().trim();
        utype=unitType.getText().toString().trim();
        ulocation=unitLocation.getText().toString().trim();
        uphone=unitPhone.getText().toString().trim();
        utitle=unitTitle.getText().toString().trim();
        company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "");
        adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "");
        uphone = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "");
        types=SharedPreferencesUtil.getString(SharedPreferencesUtil.UNIT_TYPE, "");
        zhiwei=SharedPreferencesUtil.getString(SharedPreferencesUtil.ZHIWEI, "");

        if(uname.equals(company) && utype.equals(types) && uphone.equals(uphone)&& ulocation.equals(adress) && utitle.equals(zhiwei)){

            Toast.makeText(this, "没有修改的信息", Toast.LENGTH_SHORT).show();
            return;
        }
        utils= OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String registstr="http://"+ips+"/api/user_edit.php";
                        inforjson = OkHttpUtils.postkeyvlauspainr(registstr, compmap);
                        if(inforjson != null){
                            Message msg=new Message();
                            msg.what=1;
                            handler.sendMessage(msg);
                        }

                    }catch ( Exception e){

                    }


                }
            }.start();
        }catch (Exception e){}

    }
    Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson g=new Gson();
                Usercode codes = g.fromJson(inforjson, Usercode.class);
                userlogin data = codes.getData();
                if(codes.getCode()==1){
                    Toast.makeText(UnitActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS,data.address);
                  SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                    finish();

                }else if(codes.getCode()==-1){
                    Toast.makeText(UnitActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    private void initView() {
        unitBack = (ImageView) findViewById(R.id.unit_back);
        unitFinish = (TextView) findViewById(unit_finish);
        unitName = (TextView) findViewById(R.id.unit_name);
        unitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnitActivity.this, UnitXiuActivity.class);
                intent.putExtra("old", unitName.getText().toString());
                startActivity(intent);
            }
        });
        unitType = (TextView) findViewById(R.id.unit_type);
        unitLocation = (TextView) findViewById(R.id.unit_location);
        unitPhone = (EditText) findViewById(R.id.unit_phone);
        unitTitle = (EditText) findViewById(R.id.unit_title);
        unitBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /** 从assert文件夹中读取省市区的json文件，然后转化为json对象 */
    StringBuffer sb = new StringBuffer();
    private void initJsonData() {
//        try {
//            StringBuffer sb = new StringBuffer();
//            InputStream is = getAssets().open("city.json");
//            int len = -1;
//            byte[] buf = new byte[1024];
//            while ((len = is.read(buf)) != -1) {
//                sb.append(new String(buf, 0, len, "UTF-8"));
//            }
//            is.close();
//            mJsonObj = new JSONObject(sb.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        try {

            InputStream is = getAssets().open("citylist.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }
            is.close();
//            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** 初始化Json数据，并释放Json对象 */
    private void initJsonDatas(){
//        try {
//            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonP = jsonArray.getJSONObject(i);// 获取每个省的Json对象
//                String province = jsonP.getString("name");
//
//                ArrayList<String> options2Items_01 = new ArrayList<String>();
//                ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
//                JSONArray jsonCs = jsonP.getJSONArray("city");
//                for (int j = 0; j < jsonCs.length(); j++) {
//                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
//                    String city = jsonC.getString("name");
//                    options2Items_01.add(city);// 添加市数据
//
//                    ArrayList<String> options3Items_01_01 = new ArrayList<String>();
//                    JSONArray jsonAs = jsonC.getJSONArray("area");
//                    for (int k = 0; k < jsonAs.length(); k++) {
//                        options3Items_01_01.add(jsonAs.getString(k));// 添加区数据
//                    }
//                    options3Items_01.add(options3Items_01_01);
//                }
//                mListProvince.add(province);// 添加省数据
//                mListCiry.add(options2Items_01);
//                mListArea.add(options3Items_01);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mJsonObj = null;

        try {
            Gson g = new Gson();
            CityEntry data = g.fromJson(sb.toString(), CityEntry.class);
            if(data != null){
                for (int i = 0; i < data.data.size(); i++) {
                    CityData item = data.data.get(i);
                    String provice = item.province;
                    String proviceCode = item.evalue;
                    mListProvince.add(provice);// 添加省数据
                    mListProvinceCode.add(proviceCode);
                    ArrayList<String> options2Items_01 = new ArrayList<String>();
                    ArrayList<String> options2Items_01Code = new ArrayList<String>();
                    ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
                    for(int j=0; j<item.city.size();j++){
                        City pos = item.city.get(j);
                        options2Items_01.add(pos.ename);
                        options2Items_01Code.add(pos.evalue);
                    }
                    mListCiry.add(options2Items_01);
                    mListCiryCode.add(options2Items_01Code);
                }

            }

//            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonP = jsonArray.getJSONObject(i);// 获取每个省的Json对象
//                String province = jsonP.getString("name");
//
//                ArrayList<String> options2Items_01 = new ArrayList<String>();
//                ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
//                JSONArray jsonCs = jsonP.getJSONArray("city");
//                for (int j = 0; j < jsonCs.length(); j++) {
//                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
//                    String city = jsonC.getString("name");
//                    options2Items_01.add(city);// 添加市数据
//
//                    ArrayList<String> options3Items_01_01 = new ArrayList<String>();
//                    JSONArray jsonAs = jsonC.getJSONArray("area");
//                    for (int k = 0; k < jsonAs.length(); k++) {
//                        options3Items_01_01.add(jsonAs.getString(k));// 添加区数据
//                    }
//                    options3Items_01.add(options3Items_01_01);
//                }
//                mListProvince.add(province);// 添加省数据
//                mListCiry.add(options2Items_01);
//                mListArea.add(options3Items_01);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("完善单位信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("完善单位信息");
        company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "");
        adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "");
        uphone = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "");
        types=SharedPreferencesUtil.getString(SharedPreferencesUtil.UNIT_TYPE, "");
        zhiwei=SharedPreferencesUtil.getString(SharedPreferencesUtil.ZHIWEI, "");
        Log.d("lizisong", "com:"+company+","+"adress:"+adress+",uphone:"+uphone+",types:"+types+",zhiwei:"+zhiwei);
        if(uname!=null){
            unitName.setText(uname);
        }else{
            unitName.setText(company);
        }

        if(utype!=null){
            unitType.setText(utype);
        }else{
            unitType.setText(types);
        }
        if(ulocation!=null){
            unitLocation.setText(ulocation);
        }else{
            unitLocation.setText(adress);
            if(adress != null && !adress.equals("")){
                 String [] ad = adress.split(" ");
                if(ad != null && ad.length == 2){
                     String pro = ad[0];
                     String city = ad[1];
                    int index1=0,index2=0;
                     for(int i=0; i<mListProvince.size(); i++){
                         String province = mListProvince.get(i);
                         if(province.contains(pro)){
                             index1 = i;
                             break;
                         }
                     }

                    ArrayList<String> citylist = mListCiry.get(index1);
                    if(citylist != null){
                        for(int j=0; j<citylist.size(); j++){
                            String cy = citylist.get(j);
                            if(cy.contains(city)){
                                index2= j;
                                break;
                            }

                        }
                    }
                    mOpv.setSelectOptions(index1, index2);
                }
            }
        }
        if(uphone!=null){
            unitPhone.setText(uphone);
        }else{
            unitPhone.setText(uphone);
        }
        if(utitle!=null){
            unitTitle.setText(utitle);
        }else{
            unitTitle.setText(zhiwei);
        }

    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                Usercode codes = gson.fromJson(json,  Usercode.class);
                userlogin data = codes.getData();
                if(codes.getCode()==1){
                    Toast.makeText(UnitActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS,data.address);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);


                }else if(codes.getCode()==-1){
                    Toast.makeText(UnitActivity.this, codes.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    private void xiugai(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://"+ MyApplication.ip+"/api/user_edit_new.php";
                        json = OkHttpUtils.postkeyvlauspainr(url, hashMap);
                        if(json != null){
                            Message msg = Message.obtain();
                            msg.what =1;
                            handler.sendMessage(msg);
                        }
                    }
                }
        ).start();
    }


}
