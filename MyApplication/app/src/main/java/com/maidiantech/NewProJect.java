package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import Util.NetUtils;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Aree;
import entity.Code;
import entity.Codes;
import entity.Datas;
import entity.Label;
import entity.Posts;
import view.CustomTextView;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by Administrator on 2018/11/20.
 */

public class NewProJect extends AutoLayoutActivity {
    Code code;
    List<Aree> areas;
    List<Label> labels;
    ImageView back, search;
    private ProgressBar progress;
    AreaAdapter areaAdapter;
    LabelAdapter labelAdapter;
    RefreshListView listview;
    ListView listtype1, listtype2;
    TextView type, lingyu;
    RelativeLayout typeare1,typeare2;
    private int Size = 10;
    private String pubdate = "";
    private String evalue = "", label = "", sortid = "";
    private List<Posts> postsListData = new ArrayList<>();
    private String jsons;
    private Datas data;
    String time_limit;
    ImageView tips;
    private int labelIndex = 0, areaIndex = 0;
    Adapter adapter;
    private DisplayImageOptions options;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newproject);
        options = ImageLoaderUtils.initOptions();
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        back = (ImageView) findViewById(R.id.shezhi_backs);
        search = (ImageView) findViewById(R.id.search);
        listtype1 = (ListView) findViewById(R.id.listtype1);
        listtype2 = (ListView) findViewById(R.id.listtype2);
        listview = (RefreshListView) findViewById(R.id.listview);
        progress = (ProgressBar) findViewById(R.id.progress);
        type = (TextView) findViewById(R.id.type);
        lingyu = (TextView) findViewById(R.id.lingyu);
        typeare1 = (RelativeLayout)findViewById(R.id.typeare1);
        typeare2 = (RelativeLayout)findViewById(R.id.typeare2);
        tips = (ImageView) findViewById(R.id.tipsbg);
        time_limit = getIntent().getStringExtra("time_limit");
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tips.setVisibility(View.GONE);
                typeare1.setVisibility(View.INVISIBLE);
                typeare2.setVisibility(View.INVISIBLE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProJect.this, NewSearchHistory.class);
                intent.putExtra("typeid", "2");
                startActivity(intent);
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeare1.getVisibility() == View.INVISIBLE) {
                    typeare1.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.VISIBLE);
                } else if (typeare1.getVisibility() == View.VISIBLE) {
                    typeare1.setVisibility(View.INVISIBLE);
                    tips.setVisibility(View.GONE);
                }
            }
        });
        lingyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeare2.getVisibility() == View.INVISIBLE) {
                    typeare2.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.VISIBLE);
                } else if (typeare2.getVisibility() == View.VISIBLE) {
                    typeare2.setVisibility(View.INVISIBLE);
                    tips.setVisibility(View.GONE);
                }
            }
        });
        listview.setPullDownToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {

            @Override
            public void pullDownToRefresh() {
                listview.setPullUpToRefreshFinish();
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(NewProJect.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listview.setPullDownToRefreshFinish();
                } else {
                    pubdate = "";
                    sortid = "";
                    listview.setPullUpToRefreshable(true);
                    getJson(Size, pubdate, sortid, evalue, label, true);
                }

            }
        });
        listview.setPullUpToRefreshable(true);
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {

                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(NewProJect.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if (postsListData.size() == 0) {

                    } else {
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                        getJson(Size, pubdate, sortid, evalue, label, false);
                    }
                }
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        typeare1.setVisibility(View.INVISIBLE);
                        typeare2.setVisibility(View.INVISIBLE);
                        tips.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(NewProJect.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            getJson(Size, pubdate, sortid, evalue, label, false);
            getProject();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getJson(final int Size, final String pubdate, final String sortid, final String evalue, final String labels, final boolean state) {
        String url = "http://" + MyApplication.ip + "/api/arc_index.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", Size + "");
        map.put("LastModifiedTime", pubdate + "");
        map.put("typeid", "2");
        map.put("rand", "1");
        map.put("sortid", sortid);
        map.put("evalue", evalue);
        map.put("label", labels);
        if(time_limit != null && !time_limit.equals("")){
            String[] litp = time_limit.split("-");
            map.put("st",litp[0]);
            map.put("et", litp[1]);
        }
        if (state) {
            networkCom.getJson(url, map, handler, 3, 1);
        } else {
            networkCom.getJson(url, map, handler, 3, 0);
        }

    }

    public void getProject() {
        String url = "http://" + MyApplication.ip + "/api/getSearchTermsApi.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "project");
        networkCom.getJson(url, map, handler, 10, 0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 10) {
                    Gson gson = new Gson();
                    String ret = (String) msg.obj;
                    code = gson.fromJson(ret, Code.class);
                    if (code != null) {
                        if (code.code.equals("1")) {
                            if (code.data != null) {
                                if (code.data.area != null) {
                                    areas = code.data.area;
                                    labels = code.data.label;
                                }
                                if (areaAdapter == null) {
                                    areaAdapter = new AreaAdapter();
                                    listtype1.setAdapter(areaAdapter);
                                }
                                if (labelAdapter == null) {
                                    labelAdapter = new LabelAdapter();
                                    listtype2.setAdapter(labelAdapter);
                                }
                            }
                        }
                    }
                }
                if (msg.what == 3) {
                    listview.setPullDownToRefreshFinish();
                    listview.setPullUpToRefreshFinish();
//                    listview.setBackgroundColor(0xfff6f6f6);
                    progress.setVisibility(View.GONE);
                    if (msg.arg1 == 1) {
                        postsListData.clear();
                    }
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
//                    if(postsListData != null){
                    List<Posts> post = data.getPosts();
                    if (post != null) {
                        if (post.size() > 0) {
                            for (int i = 0; i < post.size(); i++) {
                                Posts item = post.get(i);
                                postsListData.add(item);
                            }
                        } else {
                            listview.setPullUpToRefreshable(false);
                            Posts item = new Posts();
                            item.setTypename("底线");
                            postsListData.add(item);

                        }
                    } else {
                        listview.setPullUpToRefreshable(false);
                        Posts item = new Posts();
                        item.setTypename("底线");
                        postsListData.add(item);
                    }
//                    }
                    if (adapter == null) {
                        adapter = new Adapter();
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
//                                    PrefUtils.setString(NewProJect.this,postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                    if (postsListData.get(position - 1).getTypename().equals("专题")) {
                                        Intent intent = new Intent(NewProJect.this, SpecialActivity.class);
                                        intent.putExtra("id", postsListData.get(position -1).getId());
                                        startActivity(intent);
                                    } else {
                                        String labels = postsListData.get(position - 1).labels;
                                        if(labels != null){
                                            if(labels.contains("精品项目")){
                                                Intent intent=new Intent(NewProJect.this, ActiveActivity.class);
                                                intent.putExtra("title", postsListData.get(position - 1).getTitle());
                                                intent.putExtra("url", postsListData.get(position - 1).url);
                                                startActivity(intent);
                                            }else{
                                                Intent intent = new Intent(NewProJect.this, NewProjectActivity.class);
                                                intent.putExtra("aid", postsListData.get(position - 1).getId());
                                                startActivity(intent);
                                            }
                                        }else{
                                            Intent intent = new Intent(NewProJect.this, NewProjectActivity.class);
                                            intent.putExtra("aid", postsListData.get(position - 1).getId());
                                            startActivity(intent);
                                        }

                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        adapter.notifyDataSetChanged();
                    }


                }
            } catch (Exception e) {

            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NewProJect Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    class AreaAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return areas.size();
        }

        @Override
        public Object getItem(int position) {
            return areas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final HoldView holdView;
            if (convertView == null) {
                convertView = View.inflate(NewProJect.this, R.layout.arealabeladapter, null);
                holdView = new HoldView();
                holdView.txt = convertView.findViewById(R.id.txt);
                holdView.lines = convertView.findViewById(R.id.lines);
                convertView.setTag(holdView);

            } else {
                holdView = (HoldView) convertView.getTag();
            }
            final Aree area = areas.get(position);
            holdView.txt.setText(area.ename);
            holdView.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (area.ename.equals("不限")) {
                        type.setText("项目类型");
                        evalue = "";
                    } else {
                        type.setText(area.ename);
                        evalue = area.evalue;
                    }
                    areaIndex = position;
                    typeare1.setVisibility(View.INVISIBLE);
                    tips.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    pubdate = "";
                    sortid = "";
                    areaAdapter.notifyDataSetChanged();
                    getJson(Size, pubdate, sortid, evalue, label, true);

                }
            });
            Log.d("lizisong", "areaIndex:"+areaIndex);
            if(areaIndex == position){
                holdView.txt.setTextColor(0xff3385ff);
            }else{
                holdView.txt.setTextColor(0xff181818);
            }

            if(position == areas.size()-1){
                holdView.lines.setVisibility(View.GONE);
            }else{
                holdView.lines.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        class HoldView {
            public TextView txt;
            public TextView lines;
        }
    }

    class LabelAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return labels.size();
        }

        @Override
        public Object getItem(int position) {
            return labels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if (convertView == null) {
                convertView = View.inflate(NewProJect.this, R.layout.arealabeladapter, null);
                holdView = new HoldView();
                holdView.txt = convertView.findViewById(R.id.txt);
                holdView.lines = convertView.findViewById(R.id.lines);
                convertView.setTag(holdView);

            } else {
                holdView = (HoldView) convertView.getTag();
            }
            final Label label = labels.get(position);
            holdView.txt.setText(label.name);
            holdView.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (label.name.equals("不限")) {
                        lingyu.setText("所属领域");
                        NewProJect.this.label = "";
                    } else {
                        lingyu.setText(label.name);
                        NewProJect.this.label = label.label;
                    }
                    labelIndex = position;
                    typeare2.setVisibility(View.INVISIBLE);
                    tips.setVisibility(View.GONE);
                    labelAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.VISIBLE);
                    pubdate = "";
                    sortid = "";
                    getJson(Size, pubdate, sortid, evalue,  NewProJect.this.label, true);
                }
            });
            if(position == labels.size()-1){
                holdView.lines.setVisibility(View.GONE);
            }else{
                holdView.lines.setVisibility(View.VISIBLE);
            }
            Log.d("lizisong", "labelIndex:"+labelIndex);
            if(labelIndex == position){
                holdView.txt.setTextColor(0xff3385ff);
            }else{
                holdView.txt.setTextColor(0xFF181818);
            }

            return convertView;
        }

        class HoldView {
            public TextView txt,top,bottom;
            public TextView lines;
        }
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return postsListData.size();
        }

        @Override
        public Object getItem(int position) {
            return postsListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if (convertView == null) {
                convertView = View.inflate(NewProJect.this, R.layout.newprojectada, null);
                holdView = new HoldView();
                holdView.xiangmu_lay = (RelativeLayout) convertView.findViewById(R.id.xiangmu_lay);
                holdView.xianmgmu = (ImageView) convertView.findViewById(R.id.xianmgmu);
                holdView.xmtitle = (CustomTextView) convertView.findViewById(R.id.xmtitle);
                holdView.lanyuan = (TextView) convertView.findViewById(R.id.lanyuan);
                holdView.source = (TextView) convertView.findViewById(R.id.source);
                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);
                convertView.setTag(holdView);

            } else {
                holdView = (HoldView) convertView.getTag();
            }
            final Posts item = postsListData.get(position);
            if(item.getTypename().equals("底线")){
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
            }else{
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                if (item.getLitpic() != null && (!item.getLitpic().equals(""))) {
                    holdView.xianmgmu.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.getLitpic()
                            , holdView.xianmgmu, options);
                } else {
                    holdView.xianmgmu.setVisibility(View.GONE);
                }
                holdView.xmtitle.setMYTextColor(Color.parseColor("#181818"));
                holdView.xmtitle.setText(item.getTitle().replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ",""));

                try {
                    holdView.lanyuan.setText(item.getArea_cate().getArea_cate1());
                } catch (Exception e) {

                }
                if (item.labels != null && (!item.labels.equals(""))) {
                    holdView.source.setVisibility(View.VISIBLE);
                    holdView.source.setText(item.labels);
                    if (item.labels.equals("精品项目")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    } else if (item.labels.equals("钛领推荐")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                    } else if (item.labels.equals("国家科学基金")) {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                    } else {
                        holdView.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }
                } else {
                    holdView.source.setVisibility(View.GONE);
                }
            }


            return convertView;
        }

        class HoldView {
            public RelativeLayout xiangmu_lay;
            public ImageView xianmgmu;
            public CustomTextView xmtitle;
            public TextView  lanyuan, source;
            public RelativeLayout dixian;
        }
    }
}
