package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.ProTectByMD5;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import entity.RetRegisterData;
import view.StyleUtils;
import view.StyleUtils1;


/**
 * Created by 13520 on 2016/12/29.
 */

public class Perfectinformation extends AutoLayoutActivity {

    Button register;
    CheckBox checkBox;
    EditText companyName;
    TextView address;
    EditText lead;
    EditText mobile;
    EditText mima;

    private HashMap<String,String> registmap = new HashMap<>();

    String phonenum;
    String mobileNumber;
    String mimaStr;
    String leadStr;
    String addressStr;
    String comName;
    String retJson;
    private  String   ips;
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();

    private OptionsPickerView<String> mOpv;
    private JSONObject mJsonObj;
    LinearLayout fromname;
    TextView from_home;
    AlertDialog dialog;
    TextView maidianservice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informaiton);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        ImageView information_back =(ImageView) findViewById(R.id.information_back);

        information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register = (Button)findViewById(R.id.register_nexts);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        companyName = (EditText)findViewById(R.id.companyName);
        address = (TextView)findViewById(R.id.address);
        lead = (EditText)findViewById(R.id.lead);
        mobile = (EditText)findViewById(R.id.mobile);
        mima =(EditText)findViewById(R.id.mima);
        maidianservice=(TextView) findViewById(R.id.maidianservice);
        maidianservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Perfectinformation.this, ServiceProvisionAcitivty.class);
//                intent.putExtra("res_id",R.string.html_text );
//                startActivity(intent);
                Intent intent=new Intent(Perfectinformation.this, WebViewActivity.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", "file:///android_asset/clause.html");
                startActivity(intent);
            }
        });
        phonenum = getIntent().getStringExtra("phone");
//        checkBox.setOnCheckedChangeListener();
        checkBox.setChecked(true);
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

                String tx = mListProvince.get(options1) + mListCiry.get(options1).get(option2) + mListArea.get(options1).get(option2).get(options3);
                address.setText(tx);
            }
        });

        // 点击弹出选项选择器
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mOpv.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comName = companyName.getText().toString();
                addressStr = address.getText().toString();
                leadStr = lead.getText().toString();
                mobileNumber = mobile.getText().toString();
                mimaStr = mima.getText().toString();

                if(checkBox.isChecked()){
                    if(comName == null || comName.equals("")){
                        Toast.makeText(Perfectinformation.this, "企业名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(addressStr == null || addressStr.equals("")){
                        Toast.makeText(Perfectinformation.this, "企业所在地不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(leadStr == null || leadStr.equals("")){
                        Toast.makeText(Perfectinformation.this, "企业联系人不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(mobileNumber == null || mobileNumber.equals("")){
                        Toast.makeText(Perfectinformation.this, "企业联系电话不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    } else  if(TelNumMatch.isMobileNO(mimaStr)==false){
                        Toast.makeText(Perfectinformation.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    registmap.put("mtype","2");
                    registmap.put("tel",phonenum);
                    registmap.put("company",comName);
                    registmap.put("address", addressStr);
                    registmap.put("linkman", leadStr);
                    registmap.put("mobile", mobileNumber);
                    registmap.put("version", MyApplication.version);
                    ProTectByMD5 md5 = new ProTectByMD5();
                    String encode = md5.encode(mimaStr);
                    registmap.put("pwd", encode);

                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();

                    sort.add("mtype"+"2");
                    sort.add("tel"+phonenum);
                    sort.add("company"+comName);
                    sort.add("address"+addressStr);
                    sort.add("linkman"+leadStr);
                    sort.add("mobile"+mobileNumber);
                    sort.add("pwd"+encode);
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
                    registmap.put("sign", sign);
                    registmap.put("timestamp", timestamp);
                    registmap.put("accessid", accessid);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String registstr="http://"+ips+"/api/register.php";
                            retJson = OkHttpUtils.postkeyvlauspainr(registstr, registmap);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }else{
                    Toast.makeText(Perfectinformation.this, "没有同意钛领服务条款", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson g=new Gson();
                RetRegisterData registcode = g.fromJson(retJson, RetRegisterData.class);
                if(registcode.code.equals("1")){
                    Toast.makeText(Perfectinformation.this, registcode.message, Toast.LENGTH_SHORT).show();
//                    Intent intentlogin=new Intent(RegisterCountActivity.this, MainActivity.class);
                    Intent intent = new Intent(Perfectinformation.this, XingquActivity.class);
                    intent.putExtra("id", "0");
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(Perfectinformation.this, registcode.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
