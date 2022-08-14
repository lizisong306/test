package com.maidiantech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.List;

import adapter.TwolevelAdapter;
import entity.LangyaSimple;

/**
 * Created by 13520 on 2016/11/23.
 */

public class IndexActivity extends Activity {
    ListView indexlistview;
    List<LangyaSimple> infolist;
    List<LangyaSimple> zhizaolist;
    List<LangyaSimple> cailiaolist;
    List<LangyaSimple> jishulist;
    List<LangyaSimple> huanbaolist;
    List<LangyaSimple> huagonlist;
    List<LangyaSimple> nengyuanlist;
    List<LangyaSimple> chuanyilist;
    List<LangyaSimple> aitalist;
    List<LangyaSimple> dexlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);
        initView();
        Intent intent = getIntent();
        dexlist= (List<LangyaSimple>) intent.getSerializableExtra("dexlist");
        infolist= (List<LangyaSimple>) intent.getSerializableExtra("infolist");
        zhizaolist= (List<LangyaSimple>) intent.getSerializableExtra("zhizaolist");
        cailiaolist= (List<LangyaSimple>) intent.getSerializableExtra("cailiaolist");
        jishulist= (List<LangyaSimple>) intent.getSerializableExtra("jishulist");
        huanbaolist= (List<LangyaSimple>) intent.getSerializableExtra("huanbaolist");
        huagonlist= (List<LangyaSimple>) intent.getSerializableExtra("huagonlist");
        nengyuanlist= (List<LangyaSimple>) intent.getSerializableExtra("nengyuanlist");
        chuanyilist= (List<LangyaSimple>) intent.getSerializableExtra("chuanyilist");
        aitalist= (List<LangyaSimple>) intent.getSerializableExtra("aitalist");
        TwolevelAdapter leveladapter=new TwolevelAdapter(this,dexlist,infolist,zhizaolist,cailiaolist,jishulist,huanbaolist,huagonlist,nengyuanlist,chuanyilist,aitalist);
        indexlistview.setAdapter(leveladapter);
    }
    private void initView() {
        indexlistview = (ListView) findViewById(R.id.index_listview);
    }
}
