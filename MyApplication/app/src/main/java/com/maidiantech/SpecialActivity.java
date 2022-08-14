package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import adapter.SpecialAdapter;
import application.MyApplication;
import entity.Codes;
import entity.SpeCode;
import entity.SpeData;
import entity.Spec_list;
import entity.spec_post;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2017/2/27.
 */

public class SpecialActivity extends AutoLayoutActivity {
    private ImageView speBack, back;
    private TextView speTitle, title1;
    private RefreshListView speListview;
    private View mHomePageHeaderView;
    private RelativeLayout relayout;
    private ViewPager pager;
    private LinearLayout layout;
    private ImageView headImg;
    private TextView headTitle;
    int netWorkType = 0;
    private OkHttpUtils Okhttp;
    private String jsons;
    private String aid;
    private DisplayImageOptions options;
    private SpecialAdapter sadapter;
    private  List<Spec_list> special_list;
    private List<spec_post> list=new ArrayList<>();
    private String liujson;
    private ProgressBar progress;
    private View bg = null;
    private  String   ips;
    RelativeLayout spe_line;
    LinearLayout spe_linelayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_layout);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
         aid=getIntent().getStringExtra("id");
        ips = MyApplication.ip;

        initView();
        mHomePageHeaderView = getLayoutInflater().inflate(R.layout.head_banner, null);
        headImg = (ImageView) mHomePageHeaderView.findViewById(R.id.head_img);
        speBack = (ImageView) mHomePageHeaderView.findViewById(R.id.spe_back);
        speTitle = (TextView) mHomePageHeaderView.findViewById(R.id.spe_title);
        speTitle.setVisibility(View.VISIBLE);
        bg = mHomePageHeaderView.findViewById(R.id.bg);
        bg.getBackground().setAlpha(150);
        speBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewGroup.LayoutParams params = headImg.getLayoutParams();
        params.width =MyApplication.widths;
        params.height=MyApplication.widths*2/3;
        headImg.setLayoutParams(params);
        bg.setLayoutParams(params);
        headTitle = (TextView) mHomePageHeaderView.findViewById(R.id.head_title);
        speListview.addHeaderView(mHomePageHeaderView, null, false);
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

        } else {
//            UIHelper.showDialogForLoading(this,"", true);
            getspec();
            liulang();
        }
        speListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        ImageLoader.getInstance().pause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        ImageLoader.getInstance().resume();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >=2 ) {
                    spe_linelayout.setVisibility(View.VISIBLE);
                    spe_line.setVisibility(View.VISIBLE);
                } else {
                    spe_linelayout.setVisibility(View.GONE);
                    spe_line.setVisibility(View.GONE);
                }
//                if(visibleItemCount >5){
//                    spe_linelayout.setVisibility(View.VISIBLE);
//                    spe_line.setVisibility(View.VISIBLE);
//                }
                Log.d("lizisong", "visibleItemCount:"+visibleItemCount+","+"firstVisibleItem:"+firstVisibleItem);
            }
        });
    }

    private void initView() {
        speListview = (RefreshListView) findViewById(R.id.spe_listview);
        progress=(ProgressBar) findViewById(R.id.progress);
        spe_line = (RelativeLayout)findViewById(R.id.spe_line);
        spe_linelayout = (LinearLayout)findViewById(R.id.spe_linelayout);
        progress.setVisibility(View.VISIBLE);
        back = (ImageView)findViewById(R.id.spe_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title1 = (TextView)findViewById(R.id.spe_title);
    }

    public void getspec() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("aid"+aid);
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");

                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid" + accessid);
                    sign = KeySort.keyScort(sort);
                    MyApplication.setAccessid();
                    jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/special.php?aid="+aid+"&sign="+sign+"&timestamp"+timestamp+"&version="+MyApplication.version+MyApplication.accessid);

                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                    Message msg = Message.obtain();
                    message.what = 0;
                    dismissDialog.sendMessageDelayed(msg, 500);
                }catch (Exception e){}
            }
        }.start();
    }

    public void liulang(){
        Okhttp =  OkHttpUtils.getInstancesOkHttp();
        new Thread(){
            @Override
            public void run() {
                super.run();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("aid"+aid);
                sort.add("timestamp"+timestamp);
                sort.add("version"+MyApplication.version);
                sign = KeySort.keyScort(sort);
                liujson = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_click.php?aid="+aid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version);
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        }.start();
    }
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
            progress.setVisibility(View.GONE);

        }
    };
    private Handler handler= new Handler(){
        /**
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==1){
                Gson g=new Gson();
                Codes codes = g.fromJson(liujson, Codes.class);
//                if(codes.code==1){
//                    Toast.makeText(DetailsActivity.this, codes.message, Toast.LENGTH_SHORT).show();
//                }else if(codes.code==-1){
//                    Toast.makeText(DetailsActivity.this, codes.message, Toast.LENGTH_SHORT).show();
//                }
            }
            if (msg.what==0){
                try {
                    Gson g=new Gson();
                    SpeCode speCode = g.fromJson(jsons, SpeCode.class);
                    SpeData data = speCode.getData();
                    String title = data.getTitle();
                    speTitle.setText(title);
                    title1.setText(title);
                    headTitle.setText(data.getDescription());
                    ImageLoader.getInstance().displayImage(data.getLitpic()
                            , headImg, options);
                    special_list = data.getSpecial_list();
                    for (int i=0;i<special_list.size();i++){
                        Spec_list item = special_list.get(i);
                        String notename=  item.getNotename();
                        spec_post imte1 = new spec_post();
                        imte1.notename = notename;
                        imte1.setTypename("专题");
                        if(item.posts.size()==0){
                            imte1.state=false;
                        }else{
                            imte1.state=true ;
                        }
                        list.add(imte1);
                        for(int j =0; j<item.posts.size();j++){
                            spec_post spec_post = item.posts.get(j);
                            list.add(spec_post);
                        }
                    }
                    if(sadapter==null){
                        sadapter=new SpecialAdapter(SpecialActivity.this,list);
                        sadapter.setSpeTitle(title);
                        speListview.setAdapter(sadapter);
                    }else{
                        sadapter.notifyDataSetChanged();
                    }
                    speListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PrefUtils.setString(SpecialActivity.this,list.get(position - 2).getId(),list.get(position - 2).getId());
                            String name = list.get(position - 2).getTypename();
                            if(name.equals("专题")){
                                return;
                            }

                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(SpecialActivity.this, ZixunDetailsActivity.class);
                                intent.putExtra("id", list.get(position - 2).getId());
                                intent.putExtra("name", list.get(position - 2).getTypename());
                                intent.putExtra("pic", list.get(position - 2).getLitpic());
                                startActivity(intent);
                            }else{
                                if(name.equals("专家")){
                                    Intent intent = new Intent(SpecialActivity.this, NewRenCaiTail.class);
                                    intent.putExtra("aid", list.get(position - 2).getId());
                                    startActivity(intent);
                                }else if(name.equals("项目")){
                                    Intent intent = new Intent(SpecialActivity.this, NewProjectActivity.class);
                                    intent.putExtra("aid", list.get(position - 2).getId());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(SpecialActivity.this, DetailsActivity.class);
                                    intent.putExtra("id", list.get(position - 2).getId());
                                    intent.putExtra("name", list.get(position - 2).getTypename());
                                    intent.putExtra("pic", list.get(position - 2).getLitpic());
                                    startActivity(intent);
                                }


                            }


                        }
                    });
                }catch (Exception e){}

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("专题");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("专题");
        MobclickAgent.onResume(this);
        if(sadapter!=null){
            sadapter.notifyDataSetChanged();
        }
    }
    public int getScrollY() {
        View c = speListview.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = speListview.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }
}
