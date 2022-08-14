package com.maidiantech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import application.ImageLoaderUtils;
import dao.Collectionss;
import entity.Releasebean;
import view.StyleUtils;
import view.StyleUtils1;
import view.T;

/**
 * Created by 13520 on 2016/9/14.
 */
public class Demandrelease extends AutoLayoutActivity {
    ImageView releaseback;
    ImageView releaseadd;
    ListView myreleaselv;
    private List<Releasebean> list;
    ReleaseAddapter adapter;
    private DisplayImageOptions options;
    private Collectionss c;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_release);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
        list = new ArrayList<>();
      /*  c = new Collectionss(Demandrelease.this);
        list.clear();
        list = c.findall();*/
        if(adapter==null){
            adapter = new ReleaseAddapter();
            myreleaselv.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }
    private void initView() {
        releaseback = (ImageView) findViewById(R.id.release_back);
        releaseadd = (ImageView) findViewById(R.id.release_add);
        myreleaselv = (ListView) findViewById(R.id.my_release_lv);
        releaseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        releaseadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Demandrelease.this,ReleaseAdd.class);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
          /*  list.clear();
            list = c.findall();
            if(adapter==null){
                adapter = new ReleaseAddapter();
                myreleaselv.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }*/

        }
    }
    class ReleaseAddapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (list.size() == 0) {
                return 0;
            } else {
                return list.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder ;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = View.inflate(Demandrelease.this, R.layout.release_lv_item, null);
                holder.xuqiuimg = (ImageView) convertView.findViewById(R.id.xuqiuimg);
                holder.xuqiuname = (TextView) convertView.findViewById(R.id.xuqiuname);
                holder.releasedele = (ImageView) convertView.findViewById(R.id.release_dele);
                holder.suoshulinyu = (TextView) convertView.findViewById(R.id.suoshulinyu);
                holder.xuqiujianjie = (TextView) convertView.findViewById(R.id.xuqiujianjie);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            holder.xuqiuname.setText(list.get(position).getTrim());
            holder.suoshulinyu.setText(list.get(position).getRealease());
            holder.xuqiujianjie.setText(list.get(position).getIntroduction());
            options = ImageLoaderUtils.initOptions();
           /* ImageLoader.getInstance().displayImage(list.get(position).getBitmap()
                    ,  holder.xuqiuimg, options);*/
            Bitmap bitmap=BitmapFactory.decodeFile(list.get(position).getBitmap());
            holder.xuqiuimg.setImageBitmap(bitmap);
            holder.releasedele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NormalDialog dialog = new NormalDialog(Demandrelease.this);
                    dialog.content("是否确定删除此条需求")//
                      /* .showAnim(bas_in)//
                       .dismissAnim(bas_out)//*/
                            .show();
                    dialog.setOnBtnLeftClickL(new OnBtnLeftClickL() {
                        @Override
                        public void onBtnLeftClick() {
                       /*     c.deleteStuById(list.get(position).getId());
                            list.remove(position);
                            adapter.notifyDataSetChanged();*/
                            dialog.dismiss();
                        }
                    });
                    dialog.setOnBtnRightClickL(new OnBtnRightClickL() {
                        @Override
                        public void onBtnRightClick() {
                            T.showShort(Demandrelease.this, "不是");
                            dialog.dismiss();
                        }
                    });
                }
            });
            return convertView;
        }
        public class ViewHolder {
            ImageView xuqiuimg;
            TextView xuqiuname;
            ImageView releasedele;
            TextView suoshulinyu;
            TextView xuqiujianjie;

        }
    }
}
