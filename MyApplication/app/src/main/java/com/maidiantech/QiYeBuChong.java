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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.tencent.mm.opensdk.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import application.MyApplication;
import entity.City;
import entity.CityData;
import entity.CityEntry;
import entity.QiYeBuChongDeil;
import entity.QiYeCommapyEntiy;
import entity.Ret;
import view.AutoLinefeedLayout;

/**
 * Created by Administrator on 2019/8/27.
 */

public class QiYeBuChong extends AutoLayoutActivity {
    private OptionsPickerView<String> mOpv;
    private ArrayList<ArrayList<String>> mListCiryCode = new ArrayList<ArrayList<String>>();
    private ArrayList<String> mListProvinceCode = new ArrayList<String>();
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    ImageView backs;
    EditText edit;
    AutoLinefeedLayout hotView;
    TextView shengfen,shicity,xuanzesuozaidi,queding;
    ListView search_listview_info;
    LinearLayout listlay;
    List<String> companys = new ArrayList<>();
    Adapter adapter ;
    QiYeBuChongDeil data;
    String [] qiyetype = new String[]{"国有企业","国有控股","外资企业", "合资企业","行政机关","私营企业","事业单位","其他"};
    HashMap<String,String> codeType = new HashMap<>();
    String adresscode,qicode;
    String id;
    boolean stateshow = false;
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
        setContentView(R.layout.qiyebuchong);
        id = getIntent().getStringExtra("id");
        codeType.put("国有企业", "1");
        codeType.put("国有控股", "2");
        codeType.put("外资企业", "3");
        codeType.put("合资企业", "4");
        codeType.put("行政机关", "5");
        codeType.put("私营企业", "6");
        codeType.put("事业单位", "7");
        codeType.put("其他", "8");
        backs = (ImageView)findViewById(R.id.backs);
        edit = (EditText)findViewById(R.id.edit);
        hotView = (AutoLinefeedLayout)findViewById(R.id.hotView);
        inithotView();
        shengfen = (TextView)findViewById(R.id.shengfen);
        shicity =(TextView)findViewById(R.id.shicity);
        xuanzesuozaidi = (TextView)findViewById(R.id.xuanzesuozaidi);
        search_listview_info = (ListView)findViewById(R.id.search_listview_info);
        listlay = (LinearLayout)findViewById(R.id.listlay);
        queding = (TextView)findViewById(R.id.queding);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        xuanzesuozaidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                mOpv.show();
            }
        });
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!stateshow){
                    stateshow=false;
                    if(s.toString() != null && !s.toString().equals("")){
                         changetext(s.toString());
                    }else{
                        listlay.setVisibility(View.GONE);
                    }
                }
                stateshow=false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt =edit.getText().toString();
                if(txt == null || (txt!=null && txt.equals(""))){
                    Toast.makeText(QiYeBuChong.this, "企业名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(qicode == null || (qicode!=null && qicode.equals(""))){
                    Toast.makeText(QiYeBuChong.this, "企业类型不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(adresscode == null ||(adresscode != null && adresscode.equals(""))){
                    Toast.makeText(QiYeBuChong.this, "企业地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                commit(txt);
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
//                String txt=mListProvince.get(options1) +"    "+ mListCiry.get(options1).get(option2);
                ArrayList<String> prov = mListCiryCode.get(options1);
                adresscode= prov.get(option2);
                shengfen.setText(mListProvince.get(options1));
                shicity.setText(mListCiry.get(options1).get(option2));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void changetext(String company){
        String url ="http://"+ MyApplication.ip+"/api/wx_investigate.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("method", "company");
        map.put("company_name", company);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                 if(msg.what == 1){
                     String ret = (String)msg.obj;
                     Log.d("lizisong", "ret:"+ret);
                     Gson gson = new Gson();
                     QiYeCommapyEntiy qicom = gson.fromJson(ret, QiYeCommapyEntiy.class);
                     if(qicom != null){
                         if(qicom.code.equals("1")){
                             companys = qicom.data;

                             if(companys != null && companys.size()>0){
                                 listlay.setVisibility(View.VISIBLE);
                                 if(adapter == null){
                                     adapter = new Adapter();
                                     search_listview_info.setAdapter(adapter);
                                 }else{
                                     adapter.notifyDataSetChanged();
                                 }
                             }else{
                                 listlay.setVisibility(View.GONE);
                             }
                         }
                     }
                 }else if(msg.what == 2){
                     String ret = (String)msg.obj;
                     Log.d("lizisong","ret:"+ret);
                     Gson gson = new Gson();
                     data = gson.fromJson(ret, QiYeBuChongDeil.class);
                     if(data != null){
                         if(data.code.equals("1")){
                             edit.setText(data.data.company);
                             edit.setSelection(data.data.company.length());
                             shengfen.setText(data.data.nativeplace_2);
                             shicity.setText(data.data.nativeplace_1);
                             adresscode=data.data.nativeplace;
                             int ctype = data.data.ctypes;
                             for(int i=0;i<childBtns.size();i++){
                                 LinearLayout item = childBtns.get(i);
                                 TextView tv = (TextView)item.findViewById(R.id.item);
                                 tv.setBackgroundResource(R.drawable.shape_round_xx);
                                 tv.setTextColor(0xff808080);
                                 if(i == ctype-1){
                                     tv.setBackgroundResource(R.drawable.shape_round_xxx);
                                     tv.setTextColor(0xfffefefe);
                                     qicode=ctype+"";
                                 }
                             }
                         }
                     }
                 }else if(msg.what==3){
                     String ret = (String)msg.obj;
                     Gson gson = new Gson();
                     Ret ert = gson.fromJson(ret, Ret.class);
                     if(ert != null){
                         if(ert.code.equals("1")){
                             Intent intent = new Intent(QiYeBuChong.this, MainActivity.class);
                             intent.putExtra("state",3);
                             startActivity(intent);
                             finish();
                         }
                     }
                 }
            }catch (Exception e){

            }
        }
    };
    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            Log.d("lizisong", "companys.size():"+companys.size());
            return companys.size();
        }

        @Override
        public Object getItem(int position) {
            return companys.get(position);
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
                convertView = View.inflate(QiYeBuChong.this, R.layout.qiyebuchongadapter, null);
                holdView.companyname = convertView.findViewById(R.id.companyname);
                holdView.line = convertView.findViewById(R.id.line);
                holdView.item = convertView.findViewById(R.id.item);
                convertView.setTag(holdView);
            }else {
                holdView = (HoldView) convertView.getTag();
            }
            final String item = companys.get(position);
            holdView.companyname.setText(item);
            holdView.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listlay.setVisibility(View.GONE);
                    stateshow=true;
                    compandetil(item);
                }
            });
//            if(position == companys.size()-1){
//                holdView.line.setVisibility(View.GONE);
//            }else{
                holdView.line.setVisibility(View.VISIBLE);
//            }
            return convertView;
        }
        class HoldView{
            TextView companyname,line;
            AutoRelativeLayout item;
        }
    }
    private void compandetil(String companname){
        String url ="http://"+ MyApplication.ip +"/api/wx_investigate.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method","comp_info");
        map.put("company_name",companname);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
    }

    private void commit(String company){
        String url ="http://"+ MyApplication.ip+"/api/new_require.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("method","qiyexinxiwanshan");
        map.put("identity", "0");
        map.put("id",id);
        map.put("company",company);
        map.put("ctype",qicode);
        map.put("address",adresscode);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,3,0);
    }
    ArrayList<LinearLayout> childBtns = new ArrayList<LinearLayout>();

    private void inithotView(){
        int size = qiyetype.length; // 添加Button的个数.
        if(size == 0){
            return;
        }

        if(size > 0){
            int countwidht =0;
            for(int i = 0; i < size; i++){
                String item = qiyetype[i];
                countwidht += getTextWidth(getApplicationContext(),item,28)+60;
                LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hotitem, null);
                TextView tv = (TextView)childBtn.findViewById(R.id.item);
                tv.setBackgroundResource(R.drawable.shape_round_xx);
                tv.setText(item);
                tv.setTag(item);
                tv.setTextColor(0xff808080);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = ((TextView)v).getText().toString();
                        qicode = codeType.get(txt);
                        for(int i=0;i<childBtns.size();i++){
                            LinearLayout item = childBtns.get(i);
                            TextView tv = (TextView)item.findViewById(R.id.item);
                            tv.setBackgroundResource(R.drawable.shape_round_xx);
                            tv.setTextColor(0xff808080);
                        }
                        ((TextView)v).setBackgroundResource(R.drawable.shape_round_xxx);
                        ((TextView)v).setTextColor(0xfffefefe);

                    }
                });
                hotView.addView(childBtn);
                childBtns.add(childBtn);
            }
        }
    }

    public int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
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

}
