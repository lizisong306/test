package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Util.SharedPreferencesUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import entity.ChaXunComEntity;
import entity.ChaXunEntity;
import entity.City;
import entity.CityData;
import entity.CityEntry;
import entity.Ret;
import view.RefreshListView;
import view.StyleUtils;
import view.SystemBarTintManager;

import static com.maidiantech.R.id.unit_finish;

/**
 * Created by lizisong on 2018/4/13.
 */

public class ChengZhangHuoBanShenQing extends AutoLayoutActivity {
    private OptionsPickerView<String> mtv;
    private OptionsPickerView<String> mOpv;
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    private ArrayList<String> mListProvinceCode = new ArrayList<String>();

    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> mListCiryCode = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<String> linyu=new ArrayList<>();
    private ArrayList<String> linyucode = new ArrayList<>();
    private String tx ,txs,code,txcode;
    private ImageView unitBack;
    private TextView arg;
    private TextView unitName;
    private TextView unitType;
    private TextView unitLocation;
    private JSONObject mJsonObj;
    TextView phone;

    private EditText unit_name;
    RefreshListView list_item;
    List<String> names = new ArrayList<>();
    Adapter adapter = null;
    boolean isClick = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.BLACK);
        }
        setContentView(R.layout.chengzhanghuobanshenqing);
        isClick = false;
        linyu.add("国有企业");
        linyu.add("国有控股");
        linyu.add("外资企业");
        linyu.add("合资企业");
        linyu.add("私营企业");
        linyu.add("事业单位");
        linyu.add("行政机关");

        mtv=new OptionsPickerView<String>(this);
        // 设置标题
        mtv.setTitle("单位类型");
        mtv.setPicker(linyu);
        mtv.setCyclic(false);
        mtv.setSelectOptions(0);
        initJsonData();
        initJsonDatas();


        //        //设置状态栏半透明的状态
        StyleUtils.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils.setStyle(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
            try {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                tintManager.setStatusBarAlpha(0);
            }catch (Exception e){

            }

        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//1
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//1
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideStatusNavigationBar();//1
        MIUISetStatusBarLightMode(getWindow(), true);
        initView();
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
                code = mListCiryCode.get(options1).get(option2);
                if(txs != null && !txs.equals("")){

                }else{
                    Toast.makeText(ChengZhangHuoBanShenQing.this, "单位信息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 监听确定选择按钮
        mtv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                tx = linyu.get(options1);
                unitType.setText(tx);
                txcode = (options1+1)+"";
                if(tx != null && !tx.equals("")){
                }else{
                    Toast.makeText(ChengZhangHuoBanShenQing.this, "单位信息不能为空", Toast.LENGTH_SHORT).show();
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



        // 点击弹出选项选择器
        unitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mOpv.show();
            }
        });

        if(MyApplication.navigationbar >0){
            mOpv.setBottom(MyApplication.navigationbar);
            mtv.setBottom(MyApplication.navigationbar);
//            ViewGroup.LayoutParams params = mOpv.getLayoutParams();
//            params.height=MyApplication.navigationbar;
//            bottmon_title.setLayoutParams(params);
        }

    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void hideStatusNavigationBar() {
        if (Build.VERSION.SDK_INT < 16) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
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

    private void initView() {
        unitBack = (ImageView) findViewById(R.id.unit_back);
//        unitFinish = (TextView) findViewById(unit_finish);
//        unitName = (TextView) findViewById(R.id.unit_name);
//        unitName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UnitActivity.this, UnitXiuActivity.class);
//                intent.putExtra("old", unitName.getText().toString());
//                startActivity(intent);
//            }
//        });
        phone = (TextView)findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + "4008818083");
                    intent.setData(data);
                    startActivity(intent);
                }catch (Exception e){

                }
            }
        });
        unitType = (TextView) findViewById(R.id.unit_type);
        unitLocation = (TextView) findViewById(R.id.unit_location);

        unit_name = (EditText) findViewById(R.id.unit_name);
        unitBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        arg = (TextView)findViewById(R.id.arg);
        arg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String company = unit_name.getText().toString();
                if(company == null || company.equals("")){
                    Toast.makeText(ChengZhangHuoBanShenQing.this, "公司名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                postData(company,txcode,code);
            }
        });
        list_item = (RefreshListView) findViewById(R.id.list_item);
//        list_item.setAlpha(0.9f);
        unit_name.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                ChaXunData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /** 从assert文件夹中读取省市区的json文件，然后转化为json对象 */
    StringBuffer sb = new StringBuffer();
    private void initJsonData() {
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

    /**
     * 提交数据
     */
    private void postData(String company, String ctype, String code){
        HashMap<String, String> map = new HashMap<>();
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        map.put("method","add");
        map.put("mid",mid);
        map.put("company",company);
        map.put("ctype",ctype);
        map.put("nativeplace",code);
        String url="http://"+MyApplication.ip+"/api/user_growth_partner.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,0,0);
    }

    /**
     * 查询数据接口
     */
    private void ChaXunData(String txt){
        String logid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        HashMap<String, String> map = new HashMap<>();
        map.put("method", "company");
        map.put("mid", logid);
        map.put("company_name", txt);
        String url ="http://"+MyApplication.ip+"/api/user_growth_partner.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }

    /**
     * 查询公司名称
     */
    private void ChaXunCompany(String txt){
        String logid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        HashMap<String, String> map = new HashMap<>();
        map.put("method", "comp_info");
        map.put("mid", logid);
        map.put("company_name", txt);
        String url ="http://"+MyApplication.ip+"/api/user_growth_partner.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String json = (String)msg.obj;
                Gson g = new Gson();
                Ret ret = g.fromJson(json, Ret.class);
                if(ret != null){
                   if(ret.code.equals("1")){
                        Toast.makeText(ChengZhangHuoBanShenQing.this, ret.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }else if(msg.what == 1){
                String json = (String)msg.obj;
                Gson g = new Gson();
                ChaXunEntity data = g.fromJson(json,ChaXunEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        names = data.data;
                        if(adapter == null){
                            adapter = new Adapter();
                            list_item.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        if(isClick){
                            list_item.setVisibility(View.GONE);
                            isClick = false;
                        }else{
                            list_item.setVisibility(View.VISIBLE);
                        }
                    }else{
                        names.clear();
                        list_item.setVisibility(View.GONE);
                    }
                }

            }else if(msg.what == 2){
                String json = (String)msg.obj;
                Gson g = new Gson();
                ChaXunComEntity data =  g.fromJson(json, ChaXunComEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        txs = data.data.nativeplace_1 +"    "+ data.data.nativeplace_2 ;
                        unitLocation.setText(txs);
                        code = data.data.nativeplace;
                        tx = data.data.ctype;
                        unitType.setText(linyu.get(Integer.parseInt(tx)-1));
                        txcode = tx;
                    }

                }



            }

        }
    };
    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {
            ViewHold hold = null;
            if(convertView == null){
                hold = new ViewHold();
                convertView = View.inflate(ChengZhangHuoBanShenQing.this, R.layout.title, null);
                hold.showName = (TextView)convertView.findViewById(R.id.showname);
                convertView.setTag(hold);
            }else{
                hold = (ViewHold)convertView.getTag();
            }
            hold.showName.setText(names.get(position));
            hold.showName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_item.setVisibility(View.GONE);
                    unit_name.setText(names.get(position));
                    isClick = true;
                    ChaXunCompany(names.get(position));
                }
            });
            return convertView;
        }
        class ViewHold {
            TextView showName;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("成长伙伴-申请");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("成长伙伴-申请");
        MobclickAgent.onPause(this);
    }
    public  boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }
}
