package com.maidiantech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizisong on 2019/12/20.
 */

public class TestRecyclerViewActivity extends AutoLayoutActivity {
    RecyclerView recyclerView;
    RecycAdapter mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    private List mList;
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
        setContentView(R.layout.testrecyclerviewactivity);
        recyclerView = (RecyclerView)findViewById(R.id.rv_list);
        mList = new ArrayList();
        initData(mList);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new RecycAdapter(mList);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }
    class RecycAdapter extends RecyclerView.Adapter<RecycAdapter.MyHolder>{
        private List mList;//数据源
        RecycAdapter(List list) {
            mList = list;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("lizisong", "onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycleritem, parent, false);
            //将view传递给我们自定义的ViewHolder
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecycAdapter.MyHolder holder, int position) {
            Log.d("lizisong", "onBindViewHolder");
            holder.tv.setText(mList.get(position).toString());

        }

        @Override
        public int getItemCount() {
            Log.d("lizisong", "getItemCount");
            return mList.size();
        }
        class MyHolder extends RecyclerView.ViewHolder{
            TextView tv;

            public MyHolder(View itemView) {
                super(itemView);
                Log.d("lizisong", "MyHolder");
                tv = itemView.findViewById(R.id.tv_content);
            }
        }
    }
    public void initData(List list) {
        for (int i = 1; i <= 40; i++) {
            list.add("第" + i + "条数据");
        }
    }

}
