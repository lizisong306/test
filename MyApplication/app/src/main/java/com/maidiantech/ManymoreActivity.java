package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

import java.util.List;

import Util.PrefUtils;
import adapter.SearchmoreAdapter;
import entity.Renlist;
import entity.Sblist;
import entity.Shiyanshi;
import entity.Tuijian;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Ztlist;
import entity.Zxlist;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/11/8.
 */

public class ManymoreActivity extends AutoLayoutActivity {
    ImageView searchmoreback;
    TextView searchmorename;
    TextView searchmorenum;
    ListView listviewmore;
    List<String> typeList;
    List<Object> dataList;
    int position;
    private String type;
    List<Zxlist> zxList;
    List<Sblist> shebei;
    List<Xmlist> xiangmu;
    List<Zclist> zhengce;
    List<Renlist> rencai;
    List<Zllist> zhuanli;
    List<Ztlist> zhuanti;
    ImageView serch_more_del;
    List<Shiyanshi> shiyanshi;
    List<Tuijian> tuijian;
    private  SearchmoreAdapter  moreadapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_more);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        //控件
        initView();
        Intent intent = getIntent();
        typeList= (List<String>) intent.getSerializableExtra("typeList");
        dataList= (List<Object>) intent.getSerializableExtra("dataList");
        position = intent.getIntExtra("possition", 0);
        type = typeList.get(position);
        Object obj = dataList.get(position);
        if(type.equals("tuijian")) {
            tuijian = (List<Tuijian>) obj;
            searchmorename.setText(tuijian.get(0).getTypename());
            searchmorenum.setText("("+tuijian.size()+"条"+")");
        }
        if(type.equals("zixun")) {
            zxList = (List<Zxlist>) obj;
            searchmorename.setText(zxList.get(0).getTypename());
            searchmorenum.setText("("+zxList.size()+"条"+")");
        }
        if(type.equals("shiyanshi")) {
            shiyanshi = (List<Shiyanshi>) obj;
            searchmorename.setText(shiyanshi.get(0).getTypename());
            searchmorenum.setText("("+shiyanshi.size()+"条"+")");
        }
       if(type.equals("shebei")) {
             shebei = (List<Sblist>) obj;
            searchmorename.setText(shebei.get(0).getTypename());
            searchmorenum.setText("("+shebei.size()+"条"+")");
        }
        if(type.equals("xiangmu")) {
             xiangmu = (List<Xmlist>) obj;
            searchmorename.setText(xiangmu.get(0).getTypename());
            searchmorenum.setText("("+xiangmu.size()+"条"+")");
        }
        if(type.equals("zhengce")) {
             zhengce = (List<Zclist>) obj;
            searchmorename.setText(zhengce.get(0).getTypename());
            searchmorenum.setText("("+zhengce.size()+"条"+")");
        }
        if(type.equals("rencai")) {
             rencai = (List<Renlist>) obj;
            searchmorename.setText(rencai.get(0).getTypename());
            searchmorenum.setText("("+rencai.size()+"条"+")");
        }
        if(type.equals("zhuanli")) {
             zhuanli = (List<Zllist>) obj;
            searchmorename.setText(zhuanli.get(0).getTypename());
            searchmorenum.setText("("+zhuanli.size()+"条"+")");
        }
//        if(type.equals("zhuanti")) {
//             zhuanti = (List<Ztlist>) obj;
//            searchmorename.setText(zhuanti.get(0).getTypename());
//            searchmorenum.setText("("+zhuanti.size()+"条"+")");
//        }

         moreadapter=new SearchmoreAdapter(this,zxList,shiyanshi,shebei,xiangmu,zhengce,rencai,zhuanli,tuijian,typeList,position);
        listviewmore.setAdapter(moreadapter);
        listviewmore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(ManymoreActivity.this,DetailsActivity.class);
                if(type.equals("zhengce")){
                    PrefUtils.setString(ManymoreActivity.this,zhengce.get(position).getId(),zhengce.get(position).getId());
                    intent.putExtra("id",zhengce.get(position).getId());
                    intent.putExtra("name", zhengce.get(position).getTypename());
                    intent.putExtra("pic",zhengce.get(position).getLitpic());
                }
//                if(type.equals("tuijian")){
//                    intent.putExtra("id",tuijian.get(position).getId());
//                    intent.putExtra("name", tuijian.get(position).getTypename());
//                    intent.putExtra("pic",tuijian.get(position).getLitpic());
//                }
                if(type.equals("zixun")){
                     intent =new Intent(ManymoreActivity.this,ZixunDetailsActivity.class);
                    PrefUtils.setString(ManymoreActivity.this,zxList.get(position).getId(),zxList.get(position).getId());
                    intent.putExtra("id",zxList.get(position).getId());
                    intent.putExtra("name", zxList.get(position).getTypename());
                    intent.putExtra("pic",zxList.get(position).getLitpic());
                }
                if(type.equals("shebei")){
                    PrefUtils.setString(ManymoreActivity.this,shebei.get(position).getId(),shebei.get(position).getId());
                    intent.putExtra("id",shebei.get(position).getId());
                    intent.putExtra("name", shebei.get(position).getTypename());
                    intent.putExtra("pic",shebei.get(position).getLitpic());
                }
                if(type.equals("rencai")){
                    PrefUtils.setString(ManymoreActivity.this,rencai.get(position).getId(),rencai.get(position).getId());
                    intent.putExtra("id",rencai.get(position).getId());
                    intent.putExtra("name", rencai.get(position).getTypename());
                    intent.putExtra("pic",rencai.get(position).getLitpic());
                }
                if(type.equals("xiangmu")){
                    PrefUtils.setString(ManymoreActivity.this,xiangmu.get(position).getId(),xiangmu.get(position).getId());
                    intent.putExtra("id",xiangmu.get(position).getId());
                    intent.putExtra("name", xiangmu.get(position).getTypename());
                    intent.putExtra("pic",xiangmu.get(position).getLitpic());
                }
                if(type.equals("zhuanli")){
                    PrefUtils.setString(ManymoreActivity.this,zhuanli.get(position).getId(),zhuanli.get(position).getId());
                    intent.putExtra("id",zhuanli.get(position).getId());
                    intent.putExtra("name", zhuanli.get(position).getTypename());
                    intent.putExtra("pic",zhuanli.get(position).getLitpic());
                }
                if(type.equals("shiyanshi")){
                    PrefUtils.setString(ManymoreActivity.this,shiyanshi.get(position).getId(),shiyanshi.get(position).getId());
                    intent.putExtra("id",shiyanshi.get(position).getId());
                    intent.putExtra("name", shiyanshi.get(position).getTypename());
                    intent.putExtra("pic",shiyanshi.get(position).getLitpic());
                }
//                if(type.equals("zhuanti")){
//                    PrefUtils.setString(ManymoreActivity.this,zhuanti.get(position).getId(),zhuanti.get(position).getId());
//                    intent.putExtra("id",zhuanti.get(position).getId());
//                    intent.putExtra("name", zhuanti.get(position).getTypename());
//                    intent.putExtra("pic",zhuanti.get(position).getLitpic());
//                }
                startActivity(intent);
            }
        });
        searchmoreback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serch_more_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                try {
                    SearchInfo.instance.finish();
                }catch (Exception e){

                }

            }
        });
    }
    private void initView() {
        searchmoreback = (ImageView) findViewById(R.id.search_more_back);
        searchmorename = (TextView) findViewById(R.id.search_more_name);
        searchmorenum = (TextView) findViewById(R.id.search_more_num);
        listviewmore = (ListView) findViewById(R.id.listview_more);
        serch_more_del=(ImageView) findViewById(R.id.serch_more_del);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moreadapter!=null){
            moreadapter.notifyDataSetChanged();
        }
    }
}
