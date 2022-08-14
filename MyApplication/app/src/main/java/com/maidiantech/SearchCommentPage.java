package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Util.NetUtils;
import Util.SharedPreferencesUtil;
import Util.TelNumMatch;
import application.MyApplication;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/10/17.
 */

public class SearchCommentPage extends AutoLayoutActivity {
    AutoCompleteTextView search;
    TextView sousuo;
    ListView listview;
    private String trim;
    private  int netWorkType;
    String redianStr;
    List<String> poslist = new ArrayList<>();
    History adapter = new History();
    ImageView delete_history;
    int tabState = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchcommentpage);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        tabState = getIntent().getIntExtra("tabState", 0);
        search = (AutoCompleteTextView)findViewById(R.id.search);
        sousuo = (TextView)findViewById(R.id.sousuo);
        delete_history = (ImageView)findViewById(R.id.delete_history);
        delete_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
                poslist.clear();
                adapter.notifyDataSetChanged();
            }
        });
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();
            }
        });
        listview = (ListView)findViewById(R.id.listview);
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    trim = search.getText().toString().trim();
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(SearchCommentPage.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }else{
                        if(trim.equals("")){
                            Toast.makeText(SearchCommentPage.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(TelNumMatch.issearch(trim)==false){
                            Toast.makeText(SearchCommentPage.this, "不能输入表情符号", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(event.getAction()==KeyEvent.ACTION_UP){
                                hintKbTwo();

                                boolean state = true;
                                for(int i=0; i<poslist.size();i++){
                                    String temp = poslist.get(i);
                                    if(temp.equals(trim)){
                                        state = false;
                                        break;
                                    }
                                }
                                if(state){
                                    if(trim != null){
                                        poslist.add(trim);
                                    }

                                }
                                String redianStr="";
                                int len = poslist.size();
                                for(int i=0;i<len; i++){
                                    String temp=poslist.get(i);
                                    if(i==len-1){
                                        redianStr=redianStr+temp;
                                    }else{
                                        redianStr=redianStr+temp+";";
                                    }
                                }
                                SharedPreferencesUtil.putString(SharedPreferencesUtil.REDIAN_SOUSUO,redianStr);
                                //跳转到新界面
                                Intent intent = new Intent(SearchCommentPage.this, SearchContentResult.class);
                                intent.putExtra("key", trim);
                                intent.putExtra("tabState",tabState);
                                startActivity(intent);

                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
//        redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
//        if(redianStr != null && !redianStr.equals("")){
//            String[] temp = redianStr.split(";");
//            if(temp != null){
//                for (int i=0; i<temp.length;i++){
//                    String pos=temp[i];
//                    if(pos != null && !pos.equals("") && !pos.equals(";")){
//                        poslist.add(pos);
//                    }
//                }
//            }
//        }
        listview.setAdapter(adapter);
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(SearchCommentPage.this, "网络不给力", Toast.LENGTH_SHORT).show();
        }else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(search, 0);

                               }

                           },
                    500);
        }

    }
    class History extends BaseAdapter{

        @Override
        public int getCount() {
            return poslist.size();
        }

        @Override
        public Object getItem(int position) {
            return poslist.get(position);
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
                convertView = View.inflate(SearchCommentPage.this,R.layout.history, null);
                holdView.txt = (TextView)convertView.findViewById(R.id.text);
                convertView.setTag(holdView);
            }else{
                holdView= (HoldView) convertView.getTag();
            }
            final String text = poslist.get(position);
            holdView.txt.setText(text);
            holdView.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到新界面
                    Intent intent = new Intent(SearchCommentPage.this, SearchContentResult.class);
                    intent.putExtra("key", text);
                    intent.putExtra("tabState",tabState);
                    startActivity(intent);

                }
            });

            return convertView;
        }
    }
    class HoldView {
        public TextView txt;
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

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索");
    }

    @Override
    protected void onResume() {
        super.onResume();
        poslist.clear();
        redianStr = SharedPreferencesUtil.getString(SharedPreferencesUtil.REDIAN_SOUSUO,"");
        if(redianStr != null && !redianStr.equals("")){
            String[] temp = redianStr.split(";");
            if(temp != null){
                for (int i=0; i<temp.length;i++){
                    String pos=temp[i];
                    if(pos != null && !pos.equals("") && !pos.equals(";")){
                        poslist.add(pos);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

    }
}
