package com.maidiantech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.util.ArrayList;
import java.util.HashMap;

import application.MyApplication;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.Ret;
import entity.XiuGaiBaMaiEntity;

/**
 * Created by Administrator on 2019/9/18.
 */

public class XiuGaiMingCheng extends AutoLayoutActivity {
    String name, id;
    ImageView back;
    AutoCompleteTextView name_search;
    Button info_submit;
    PulseCondtion mPulseCondtion;
    ArrayList<PulseData> pulseDatas;
    String evaluetop;
    PulseData postitem;
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
        setContentView(R.layout.xiugaimingcheng);
        mPulseCondtion = PulseCondtion.getInstance(XiuGaiMingCheng.this);
        pulseDatas = mPulseCondtion.get();
        id=getIntent().getStringExtra("id");
        evaluetop = getIntent().getStringExtra("evaluetop");
        name = getIntent().getStringExtra("name");
        name_search = (AutoCompleteTextView)findViewById(R.id.name_search);
        back = (ImageView)findViewById(R.id.modify_back);
        info_submit = (Button)findViewById(R.id.info_submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name_search.setText(name);
        if(pulseDatas != null){
            for (int i=0; i<pulseDatas.size();i++){
                PulseData item = pulseDatas.get(i);
                if(item.evaluetop.equals(evaluetop)){
                    postitem = item;
                    break;
                }
            }
        }
        info_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJson();
            }
        });
        if(name != null && !name.equals("")){
            name_search.setSelection(name.length());
        }
    }
    private void getJson(){
        String txt = name_search.getText().toString();
        if(txt == null || (txt!=null && txt.equals(""))){
            Toast.makeText(XiuGaiMingCheng.this, "修改名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String url="http://"+ MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","set");
        map.put("name", txt);
        map.put("id",id);
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
                    Gson gson = new Gson();
                    Ret data = gson.fromJson(ret, Ret.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            Toast.makeText(XiuGaiMingCheng.this, "修改名称成功",Toast.LENGTH_SHORT).show();
                            NewResultActivity.namexiugai=name_search.getText().toString();
                            if(postitem != null){
                                postitem.name=name_search.getText().toString();
                                mPulseCondtion.updata(postitem);
                            }
                            finish();
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };
}
