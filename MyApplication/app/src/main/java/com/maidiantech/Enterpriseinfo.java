package com.maidiantech;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Util.BitmapUtil;
import Util.EditCheckUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import Util.XWEditText;
import application.MyApplication;
import dao.Service.MainDianIcon;
import entity.Codes;
import entity.Datas;
import entity.SmsRegisData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2017/1/7.
 */

public class Enterpriseinfo extends AutoLayoutActivity {
    TextView  qiye_diqu;
    ImageView enterpriseback;
    EditText compname;
    TextView qiyediqu;
    EditText comptel;
    EditText compperson;
    EditText compmobil;
    EditText compemail;
    XWEditText compyewo;
    Button mylongbt;
    TextView comp_linyu;
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();

    private ArrayList<String> linyu=new ArrayList<>();
    private OptionsPickerView<String> mOpv;
    private OptionsPickerView<String> mtv;
    private JSONObject mJsonObj;
    LinearLayout fromname;
    TextView from_home;
    AlertDialog dialog;
    String txs,tx,name,person,mobil,mail,yewo,tel,infomid,inforjson;
    OkHttpUtils utils;
    String company,adress,hangye,mobiles,boss,email,product;
    private HashMap<String,String> compmap = new HashMap<>();
    LinearLayout qiye_icon;

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private PopupWindow mPopBottom;
    ImageView icon;
    private Bitmap bitmap;
    private EditText qy_txt;
    private Button qiye_yzm;
    private int million = 60;
    private String yzphone,yzregiststr,registlogin,smsCode;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photos.jpg";
    private File tempFile;
    MainDianIcon mainDianIcon;
    private  String   ips;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        mainDianIcon = MainDianIcon.getInstance(this);
        initView();
        linyu.add("电子信息");
        linyu.add("新材料");
        linyu.add("节能环保");
        linyu.add("新能源");
        linyu.add("先进制造");
        linyu.add("生物技术");
        linyu.add("化学化工");
        linyu.add("文化创意");
        linyu.add("其他");
        // 初始化Json对象
        initJsonData();
        // 初始化Json数据
        initJsonDatas();

        // 创建选项选择器对象
        mOpv = new OptionsPickerView<String>(this);

        // 设置标题
        mOpv.setTitle("选择城市");


        // 设置三级联动效果
        mOpv.setPicker(mListProvince, mListCiry, mListArea, true);


        // 设置是否循环滚动
        mOpv.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        mOpv.setSelectOptions(0, 0, 0);

        // 监听确定选择按钮
        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                 txs = mListProvince.get(options1) + mListCiry.get(options1).get(option2) + mListArea.get(options1).get(option2).get(options3);
                qiye_diqu.setText(txs);
            }
        });

        // 点击弹出选项选择器
        qiye_diqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mOpv.show();
            }
        });

        mtv=new OptionsPickerView<String>(this);
        mtv.setPicker(linyu);
        mtv.setCyclic(false);
        mtv.setSelectOptions(0);
        // 监听确定选择按钮
        mtv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                 tx = linyu.get(options1);
                comp_linyu.setText(tx);
            }
        });
        // 点击弹出选项选择器
        comp_linyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mtv.show();
            }
        });
        mylongbt.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v
             */
            @Override
            public void onClick(View v) {
//                try {
                    name=compname.getText().toString();
                    person=compperson.getText().toString();
                    mobil=compmobil.getText().toString();
                    mail=compemail.getText().toString();
                    yewo=compyewo.getText().toString();
                    tel=comptel.getText().toString();
                   txs = qiye_diqu.getText().toString();
//                    qiye_diqu.setText(txs);
                tx=comp_linyu.getText().toString();
//                    comp_linyu.setText(tx);
                    String checkResult = EditCheckUtils.comp_info(name,txs,tx,tel,person,mobil,mail,yewo);
                    if (!checkResult.equals("")) {
                        Toast.makeText(Enterpriseinfo.this, checkResult, Toast.LENGTH_SHORT).show();

                    }else if( !TelNumMatch.isValidPhoneNumber(tel)){

                        Toast.makeText(Enterpriseinfo.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();

                    }else  if(!TelNumMatch.isEmails(mail)){
                        Toast.makeText(Enterpriseinfo.this, "请输入正确的email", Toast.LENGTH_SHORT).show();
                    }else if(!TelNumMatch.isEmail(mail)){
                        Toast.makeText(Enterpriseinfo.this, "Emali地址无效", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        infomid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                        compmap.put("mid",infomid);
                        compmap.put("uname",name);
                        compmap.put("address",txs);
                        compmap.put("vocation",tx);
                        compmap.put("tel",tel);
                        compmap.put("linkman",person);
                        compmap.put("mobile",mobil);
                        compmap.put("email",mail);
                        compmap.put("product",yewo);
                        String accessid="";
                        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            accessid = mid;
                        }else{
                            accessid = MyApplication.deviceid;
                        }
                        compmap.put("accessid",accessid);
                        compjson();
                    }
//                }catch (Exception e){}

            }
        });

    }

    private void compjson() {
        name=compname.getText().toString();
        person=compperson.getText().toString();
        mobil=compmobil.getText().toString();
        mail=compemail.getText().toString();
        yewo=compyewo.getText().toString();
        tel=comptel.getText().toString();
        txs = qiye_diqu.getText().toString();
//                    qiye_diqu.setText(txs);
        tx=comp_linyu.getText().toString();

        company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "");
        adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "");
        hangye=SharedPreferencesUtil.getString(SharedPreferencesUtil.HANGYE, "");
        mobiles = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "");
        boss = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_BOSS,"");
        email = SharedPreferencesUtil.getString(SharedPreferencesUtil.EMAIL, "");
        product=SharedPreferencesUtil.getString(SharedPreferencesUtil.PRODUCT, "");
        tel=SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
        if(!tel.equals(tel)){
            String codes=qy_txt.getText().toString();
            if(!smsCode.equals(codes)){
                return;
            }
        }
        if(name.equals(company) && person.equals(boss) && mobil.equals(mobiles)&& mail.equals(email) && yewo.equals(product) && tel.equals(tel) && txs.equals(adress) && tx.equals(hangye)){

            Toast.makeText(this, "没有修改的信息", Toast.LENGTH_SHORT).show();
            return;
        }
        utils= OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        String url="http://"+ips+"/api/user_edit.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,compmap,handler,1,0);
//        try {
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    String registstr="http://"+ips+"/api/user_edit.php";
//                    inforjson = OkHttpUtils.postkeyvlauspainr(registstr, compmap);
//                    Message msg=new Message();
//                    msg.what=1;
//                    handler.sendMessage(msg);
//
//                }
//            }.start();
//        }catch (Exception e){}
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                if (million > 0) {
                    million--;
                    qiye_yzm.setTextSize(15);
                    qiye_yzm.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1500);
                }
                if (million == 0) {
                    qiye_yzm.setClickable(true);
                    qiye_yzm.setText("获取验证码");
                    qiye_yzm.setTextColor(Color.parseColor("#00aced"));
                    million = 60;
                }
            }
            if(msg.what == 8){
                try {

                    Gson gs = new Gson();
                    registlogin = (String)msg.obj;
                    SmsRegisData json_data =gs.fromJson(registlogin, SmsRegisData.class);
                    if(json_data.code.equals("1")){
                        smsCode = json_data.data.code;
                        setSendBt();
//                        Toast.makeText(YanzhenActivity.this, smsCode,Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Enterpriseinfo.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JsonIOException e){

                }
            }
            if(msg.what == 1){
                Gson g=new Gson();
                inforjson = (String)msg.obj;
                Codes codes = g.fromJson(inforjson, Codes.class);
                Datas data = codes.getData();
                if(codes.code==1){
                    Toast.makeText(Enterpriseinfo.this, codes.message, Toast.LENGTH_SHORT).show();

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                  SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
                   SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
                   SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS,data.linkman);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                    finish();

                }else if(codes.code==-1){
                    Toast.makeText(Enterpriseinfo.this, codes.message, Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    private void setSendBt() {
        //设置发送按钮的变化
        qiye_yzm.setClickable(false);
        qiye_yzm.setText("60秒内输入");
        qiye_yzm.setTextSize(13);
        qiye_yzm.setTextColor(Color.parseColor("#00aced"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final byte[] b = stream.toByteArray();
                mainDianIcon.updata(b, mid);
                this.icon.setImageBitmap(bitmap);
                new Thread(){
                    public void run() {
                        try {
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
                            String url="http://"+ips+"/api/uploadUserImg.php?mid="+mid;
                            uploadFile(b, url);
                        }catch (Exception e ){
                            Log.d("lizisong", e.toString());
                        }
                    }

                }.start();
//                boolean delete = tempFile.delete();
//                System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("企业信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("企业信息");
        try {
//            String url =SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
//            if(url != null && !url.equals("0") && !url.equals("")){
//                ImageLoader.getInstance().displayImage(url, this.icon);
//            } else{
//                BitmapUtil.upUserImageData(this.icon, mainDianIcon);
//            }
            company = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "");
            adress = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ADRESS, "");
            hangye=SharedPreferencesUtil.getString(SharedPreferencesUtil.HANGYE, "");
            mobiles = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_MOBILE, "");
            boss = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_BOSS,"");
            email = SharedPreferencesUtil.getString(SharedPreferencesUtil.EMAIL, "");
            product=SharedPreferencesUtil.getString(SharedPreferencesUtil.PRODUCT, "");
            tel=SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
           if(name != null){
             compname.setText(name);
           }else{
               compname.setText(company);

           }
            if(person!=null){
                compperson.setText(person);
            }else{
                compperson.setText(boss);
            }

            if(mobil!=null){
                compmobil.setText(mobil);
            }else{
                compmobil.setText(mobiles);
            }

            if(mail!=null){
                compemail.setText(mail);
            }else{
                compemail.setText(email);
            }

           if(yewo!=null){
               compyewo.setText(yewo);
           }else{
               compyewo.setText(product);
           }

            if(tel!=null){
                comptel.setText(tel);
            }else{
                comptel.setText(tel);
            }
            if(txs!=null){
                qiye_diqu.setText(txs);
            }else{
                qiye_diqu.setText(adress);
            }
           if(tx!=null){
               comp_linyu.setText(tx);
           }else{
               comp_linyu.setText(hangye);
           }


        }catch (Exception e){}


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

    /** 从assert文件夹中读取省市区的json文件，然后转化为json对象 */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** 初始化Json数据，并释放Json对象 */
    private void initJsonDatas(){
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 获取每个省的Json对象
                String province = jsonP.getString("name");

                ArrayList<String> options2Items_01 = new ArrayList<String>();
                ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
                JSONArray jsonCs = jsonP.getJSONArray("city");
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
                    String city = jsonC.getString("name");
                    options2Items_01.add(city);// 添加市数据

                    ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                    JSONArray jsonAs = jsonC.getJSONArray("area");
                    for (int k = 0; k < jsonAs.length(); k++) {
                        options3Items_01_01.add(jsonAs.getString(k));// 添加区数据
                    }
                    options3Items_01.add(options3Items_01_01);
                }
                mListProvince.add(province);// 添加省数据
                mListCiry.add(options2Items_01);
                mListArea.add(options3Items_01);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


    /**
     *
     */
    private void initView() {
        enterpriseback = (ImageView) findViewById(R.id.enterprise_back);
        compname = (EditText) findViewById(R.id.comp_name);
        icon = (ImageView)findViewById(R.id.icon);
        qy_txt=(EditText) findViewById(R.id.qy_txt);
        qiye_yzm=(Button) findViewById(R.id.qiye_yzm);
        qiye_yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yzphone=  qiye_yzm.getText().toString();
                if(!TelNumMatch.isValidPhoneNumber(yzphone)){
                    Toast.makeText(Enterpriseinfo.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else{

                    yzregistjson();
                }
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                showFullPop();
            }
        });
        compname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                name=s.toString();
            }
        });
        qiye_diqu = (TextView) findViewById(R.id.qiye_diqu);
        qiye_diqu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txs=s.toString();
            }
        });
        comptel = (EditText) findViewById(R.id.comp_tel);
        comptel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tel=s.toString();
            }
        });
        compperson = (EditText) findViewById(R.id.comp_person);
        compperson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                person=s.toString();
            }
        });
        compmobil = (EditText) findViewById(R.id.comp_mobil);
        compmobil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobil=s.toString();
            }
        });
        compemail = (EditText) findViewById(R.id.comp_email);
        compemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mail=s.toString();
            }
        });
        compyewo = (XWEditText) findViewById(R.id.comp_yewo);
        compyewo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                yewo=s.toString();
            }
        });
        mylongbt = (Button) findViewById(R.id.comp_bt);
        comp_linyu=(TextView) findViewById(R.id.comp_linyu);
        enterpriseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void yzregistjson() {
        String url ="http://"+ips+"/api/sms_interface.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String,String> map = new HashMap<>();
        map.put("tel",yzphone);
        map.put("flag","1");
        networkCom.getJson(url,map,handler,8,0);

//        try {
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    //123.206.8.208/api/sms_interface.php
//                    MyApplication.setAccessid();
//                    yzregiststr="http://"+ips+"/api/sms_interface.php?tel="+yzphone+"&flag="+1+MyApplication.accessid;
//                    registlogin = OkHttpUtils.loaudstringfromurl(yzregiststr);
//                    if(registlogin != null){
//
//                        Message msg = Message.obtain();
//                        msg.what = 8;
//                        handler.sendMessage(msg);
//                        Log.i("registlogin",registlogin);
//                    }
//                }
//            }.start();
//        }catch (Exception e){}

    }
    /**
     * 剪切图片
     *
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从相机获取
     */
    public void camera() {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME));
                    //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                }
                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
            }
        } else {

//            openCamra();//调用具体方法
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断存储卡是否可以用，可用进行存储
            if (hasSdcard()) {
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        }

    }
    private void showFullPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_crema,
                null);
        LinearLayout layout_all;
        RelativeLayout layout_choose;
        RelativeLayout layout_photo;
        RelativeLayout layout_cancel;
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_cancel=(RelativeLayout) view.findViewById(R.id.layout_cancel);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                // 判断存储卡是否可以用，可用进行存储
//                if (hasSdcard()) {
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(new File(Environment
//                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
//                }
//                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                camera();
                mPopBottom.dismiss();
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                mPopBottom.dismiss();


            }
        });
        layout_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                mPopBottom.dismiss();
            }
        });
        mPopBottom = new PopupWindow(view);
        mPopBottom.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setTouchable(true);
        mPopBottom.setFocusable(true);
        mPopBottom.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        mPopBottom.setBackgroundDrawable(dw);
        // 动画效果 从底部弹起
//        dialogBootom2UpAnimation
        mPopBottom.setAnimationStyle(R.style.Animations_GrowFromBottom);
        mPopBottom.showAtLocation(icon, Gravity.BOTTOM, 0, 0);//parent view随意
    }

    private static final String TAG = "lizisong";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static String laboratory;
    public static int uploadFile(byte[] file, String RequestURL) {
        int res=0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);

            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + PHOTO_FILE_NAME + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while ((len = is.read(bytes)) != -1) {
                dos.write(file, 0, file.length);
//                }
//                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                res = conn.getResponseCode();
                //  Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    //    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    JSONObject jo;
                    try {
                        jo = new JSONObject(result);
                        laboratory= jo.getString("code");
                        String data = jo.getString("data");
                        if(laboratory.equals("1")){
                            JSONObject item = new JSONObject(data);
                            String imgurl = item.getString("img");
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, imgurl);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    // Log.e(TAG, "result : " + result);
                } else {
                    // Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
