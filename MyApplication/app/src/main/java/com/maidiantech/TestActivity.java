package com.maidiantech;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class TestActivity extends AppCompatActivity /*implements AppBarLayout.OnOffsetChangedListener */{

//    private AppBarLayout abl_bar;
//    private View tl_expand, tl_collapse;
//    private View v_expand_mask, v_collapse_mask, v_pay_mask;
//    private int mMaskColor;
//    private RecyclerView rv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.testactivity);
        // 隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        View root = LayoutInflater.from(this).inflate(R.layout.my_shezhi, null);
        // 或者 在界面的根层加入 android:fitsSystemWindows=”true” 这个属性，这样就可以让内容界面从 状态栏 下方开始。
        ViewCompat.setFitsSystemWindows(root, true);
        setContentView(root);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0 以上 全透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
            window.setStatusBarColor(Color.TRANSPARENT);
            // 虚拟导航键
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 以上 半透明
            Window window = getWindow();
            // 状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 虚拟导航键
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

//        abl_bar =(AppBarLayout) findViewById(R.id.abl_bar);
//        tl_expand = (View) findViewById(R.id.tl_expand);
//        tl_collapse = (View) findViewById(R.id.tl_collapse);
//        abl_bar.addOnOffsetChangedListener(this);
//         rv_content = (RecyclerView) findViewById(R.id.rv_content);//获取对象
//        rv_content.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器，这里选择用竖直的列表
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            list.add("" + i);
//        }
//        ItemAdapter itemAdapter=new ItemAdapter(list, this);//添加适配器，这里适配器刚刚装入了数据
//        rv_content.setAdapter(itemAdapter);
    }
//        @Override
//    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//    }

//    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
//
//        List<String> list;//存放数据
//        Context context;
//
//        public ItemAdapter(List<String> list, Context context) {
//            this.list = list;
//            this.context = context;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
//            return holder;
//        }
//
//        //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
//// 在这里对获取对象进行操作
//        //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
//        //position是点击位置
//        @Override
//        public void onBindViewHolder(MyViewHolder holder,final  int position) {
//            //设置textView显示内容为list里的对应项
//            holder.textView.setText(list.get(position));
//            //子项的点击事件监听
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "点击子项"+position, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        //要显示的子项数量
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        //这里定义的是子项的类，不要在这里直接对获取对象进行操作
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//
//            TextView textView;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                textView = (TextView) itemView.findViewById(R.id.textview);
//            }
//        }
//
//    /*之下的方法都是为了方便操作，并不是必须的*/
//
//        //在指定位置插入，原位置的向后移动一格
//        public boolean addItem(int position, String msg) {
//            if (position < list.size() && position >= 0) {
//                list.add(position, msg);
//                notifyItemInserted(position);
//                return true;
//            }
//            return false;
//        }
//
//        //去除指定位置的子项
//        public boolean removeItem(int position) {
//            if (position < list.size() && position >= 0) {
//                list.remove(position);
//                notifyItemRemoved(position);
//                return true;
//            }
//            return false;
//        }
//
//        //清空显示数据
//        public void clearAll() {
//            list.clear();
//            notifyDataSetChanged();
//        }}

