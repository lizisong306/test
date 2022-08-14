package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.Ret;
import entity.RetPulseData;
import entity.SelectType;
import entity.SonCate;
import entity.TimeLimit;
import entity.XiuGaiBaMaiEntity;
import view.BTAlertDialog;

/**
 * Created by Administrator on 2019/9/18.
 */

public class XiuGaiBaMaiTiaoJian extends AutoLayoutActivity {
    ImageView shezhi_backs;
    TextView title;
    RelativeLayout xifenlay,kejilay,zixunlay,shanchebamai;
    TextView xifentxt,kejitxt,zixuntxt;
    String id,evaluetop;
    XiuGaiBaMaiEntity data;
    List<SelectType> selectArray = new ArrayList<>();
    List<SonCate>   sonCateArray = new ArrayList<>();
    int count1=0,count2=0;
    PulseCondtion mPulseCondtion;
    String state;
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
        setContentView(R.layout.xiugaibamaitiaojian);
        mPulseCondtion  = PulseCondtion.getInstance(this);
        id = getIntent().getStringExtra("id");
        evaluetop = getIntent().getStringExtra("evaluetop");
        shezhi_backs = (ImageView)findViewById(R.id.shezhi_backs);
        title = (TextView)findViewById(R.id.title);
        xifenlay = (RelativeLayout)findViewById(R.id.xifenlay);
        kejilay = (RelativeLayout)findViewById(R.id.kejilay);
        zixunlay = (RelativeLayout)findViewById(R.id.zixunlay);
        shanchebamai = (RelativeLayout)findViewById(R.id.shanchebamai);
        xifentxt = (TextView)findViewById(R.id.xifentxt);
        kejitxt = (TextView)findViewById(R.id.kejitxt);
        zixuntxt = (TextView)findViewById(R.id.zixuntxt);
        xifenlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XiuGaiBaMaiTiaoJian.this, XiuGaiListActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("list", (Serializable) sonCateArray);
                b.putString("name","细分领域");
                intent.putExtras(b);
                intent.putExtra("id", id);
                intent.putExtra("evaluetop",evaluetop);
                startActivity(intent);
            }
        });
        kejilay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XiuGaiBaMaiTiaoJian.this, XiuGaiListActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("list", (Serializable) selectArray);
                b.putString("name","科技资源");
                intent.putExtras(b);
                intent.putExtra("id", id);
                intent.putExtra("evaluetop",evaluetop);
                startActivity(intent);
            }
        });
        zixunlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XiuGaiBaMaiTiaoJian.this, KejiZiYuan.class);
                intent.putExtra("state", state);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        shanchebamai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BTAlertDialog dialog = new BTAlertDialog(XiuGaiBaMaiTiaoJian.this);
                dialog.setTitle("您确认要删除吗？");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delJson();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        shezhi_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getJson();
    }
    private void delJson(){
        String url = "http://"+MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","del");
        map.put("id",id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
    }

    private void getJson(){
        String url="http://"+ MyApplication.ip+"/api/setBaMaiApi.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("action","list");
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
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    data=gson.fromJson(ret, XiuGaiBaMaiEntity.class);
                    count1=0;
                    count2 =0;
                    selectArray.clear();
                    sonCateArray.clear();
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.data.selectType != null && data.data.selectType.size()>0){
                                for(int i=0; i<data.data.selectType.size();i++){
                                    SelectType item = data.data.selectType.get(i);
                                    selectArray.add(item);
                                    if(item.isChoice.equals("1")){
                                        count1++;
                                    }
                                }
                            }
                            kejitxt.setText("已关注"+count1+"类科技资源");

                            if(data.data.sonCate != null && data.data.sonCate.size()>0){
                                for(int i=0; i<data.data.sonCate.size();i++){
                                    SonCate item = data.data.sonCate.get(i);
                                    sonCateArray.add(item);
                                    if(item.isChoice.equals("1")){
                                        count2++;
                                    }
                                }
                            }
                            xifentxt.setText("已选 "+count2+" 个细分领域");
                            if(data.data.zxTimeLimit != null && data.data.zxTimeLimit.size()>0){
                                for (int i=0; i<data.data.zxTimeLimit.size();i++){
                                    TimeLimit item = data.data.zxTimeLimit.get(i);
                                    if(item.isChoice.equals("1")){
                                        state = item.tvalue;
                                        break;
                                    }
                                }
                            }
                            if(state.equals("1")){
                                zixuntxt.setText("全部资讯");
                            }else if(state.equals("2")){
                                zixuntxt.setText("一个月内");
                            }else if(state.equals("3")){
                                zixuntxt.setText("六个月内");
                            }

                        }
                    }

                }else if(msg.what == 2){
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    Ret r  =gson.fromJson(ret, Ret.class);
                    if(r != null){
                        if(r.code.equals("1")){
                            Toast.makeText(XiuGaiBaMaiTiaoJian.this, "删除成功",Toast.LENGTH_SHORT).show();
                            mPulseCondtion.deletebyevaluetop(evaluetop);
                            Intent intent = new Intent(XiuGaiBaMaiTiaoJian.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };
}
