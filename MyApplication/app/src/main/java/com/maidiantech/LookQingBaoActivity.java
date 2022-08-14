package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtils;
import Util.UIHelper;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.CellData;
import entity.DataList;
import entity.LookQingBaoEntity;
import entity.ShowLookQingData;
import view.RefreshListView;
import view.StyleUtils1;

import static com.tencent.android.tpush.NotificationAction.intent;

/**
 * Created by lizisong on 2017/10/31.
 */

public class LookQingBaoActivity extends AutoLayoutActivity {
    ImageView back;
    TextView biaoti;
    RefreshListView listview;
    ProgressBar progress;
    LookQingBaoEntity data;
    String teligjournalid="";
    String json;
    View view;
    private DisplayImageOptions options;
    ImageView icon,shouqiicon,share;
    TextView title,qingbaotitle,left,right,shouqi,layout3;
    List<ShowLookQingData> listData = new ArrayList<>();
    LookQingBaoAdapter adapter;
    LinearLayout layout2;
//    List<ShowLookQingData> listHeart = new ArrayList<>();
    final static int BIG_TITLE_SHOW = 1;
    final static int SMALL_TITLE_SHOW = 2;
    final static int SHOUQI_TITLE_SHOW = 3;
    final static int BIG_TITLE = 4;
    final static int SMALL_TITLE =5;
    final static int CONTENT = 6;
    final static int CONTENT_IMG = 7;
    final static int FROM = 8;
    final static int LINE = 9;
    final static int LINE1 = 10;
    int state = 0;
    UMShareAPI mShareAPI;
    private ShareAction action;
    private UMImage image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookqingbaoactivity);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        mShareAPI = UMShareAPI.get(this);
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.yujian_backs);
        biaoti = (TextView)findViewById(R.id.biaoti);
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        share = (ImageView)findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareonclick();
            }
        });
        view = View.inflate(LookQingBaoActivity.this, R.layout.lookqingbaoheart,null);
        icon = (ImageView)view.findViewById(R.id.icon);
        qingbaotitle = (TextView)view.findViewById(R.id.qingbaotitle);
        left = (TextView)view.findViewById(R.id.left);
        right = (TextView)view.findViewById(R.id.right);
        shouqi = (TextView)view.findViewById(R.id.shouqi);
        layout2 = (LinearLayout)view.findViewById(R.id.layout2);
        layout3 = (TextView)view.findViewById(R.id.layout3);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == 1){
                    state=0;
                    shouqi.setText("展开");
                    shouqiicon.setBackgroundResource(R.mipmap.down);
                    layout3.setVisibility(View.GONE);
                }else if(state == 0){
                    state=1;
                    shouqi.setText("收起");
                    layout3.setVisibility(View.VISIBLE);
                    shouqiicon.setBackgroundResource(R.mipmap.up);
                }
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
        shouqiicon = (ImageView)view.findViewById(R.id.shouqiicon);
        progress.setVisibility(View.VISIBLE);
        teligjournalid = getIntent().getStringExtra("teligjournalid");
        PrefUtils.setString(LookQingBaoActivity.this, teligjournalid, teligjournalid);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);

                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
//                    Log.d("lizisong","hhhhh:"+h);
                    if(h > 450){
                        biaoti.setText(data.data.telig_journal_title);
                    }else {
                        biaoti.setText("情报详情");
                    }

                }
            }

            private int getScrollY() {
                try {
                    int height = 0;
                    for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                        ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                        height += itemRecod.height;
                    }
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                    if (null == itemRecod) {
                        itemRecod = new ItemRecod();
                    }
                    return height - itemRecod.top;
                }catch (Exception e){
                    return 451;
                }

//                    return 0;
            }
            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        getjson();
    }


    private void getjson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sort.add("timestamp"+timestamp);
                sort.add("teligjournalid"+teligjournalid);
                sign = KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/tellig/telig_journal_detail.php?teligjournalid="+teligjournalid+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign="+sign;
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                data = gson.fromJson(json, LookQingBaoEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        ImageLoader.getInstance().displayImage(data.data.telig_journal_banner
                                , icon, options);
                        qingbaotitle.setText(data.data.telig_journal_title);
                        left.setText(data.data.telig_journal_author);
                        right.setText(TimeUtils.getTimes(data.data.telig_journal_pubdate));
                        shouqi.setText("展开");
                        layout3.setVisibility(View.GONE);
                        shouqiicon.setBackgroundResource(R.mipmap.down);
                        /***
                         * 提取Title
                         */
                        if(data.data.list != null && data.data.list.size() >0){
                            for(int i=0; i<data.data.list.size(); i++){
                                DataList item = data.data.list.get(i);
                                ShowLookQingData data = new ShowLookQingData();
                                data.theme = item.theme;
                                data.type = BIG_TITLE_SHOW;
                                listData.add(data);
                                for(int j=0;j<item.datas.size();j++){
                                    CellData cell = item.datas.get(j);
                                    ShowLookQingData celldata = new ShowLookQingData();
                                    celldata.telig_journal_theme = cell.telig_journal_title;
                                    celldata.type = SMALL_TITLE_SHOW;
                                    listData.add(celldata);
                                }
                            }
                            ShowLookQingData shouqi = new ShowLookQingData();
                            shouqi.type = SHOUQI_TITLE_SHOW;
                            listData.add(shouqi);
                                for(int i=0; i<data.data.list.size(); i++){
                                    DataList item = data.data.list.get(i);
                                    ShowLookQingData line = new ShowLookQingData();
                                    line.type =  LINE;
                                    listData.add(line);
                                    ShowLookQingData data = new ShowLookQingData();
                                    data.theme = item.theme;
                                    data.type = BIG_TITLE;
                                    listData.add(data);
                                    for(int j=0;j<item.datas.size();j++){
                                        CellData cell = item.datas.get(j);
                                        if(cell.telig_journal_title != null && !cell.telig_journal_title.equals("")){
                                            ShowLookQingData data1 = new ShowLookQingData();
                                            data1.telig_journal_title= cell.telig_journal_title;
                                            data1.type= SMALL_TITLE;
                                            listData.add(data1);
                                        }

                                        if(cell.telig_journal_pic != null && !cell.telig_journal_pic.equals("")){
                                            ShowLookQingData data2 = new ShowLookQingData();
                                            data2.telig_journal_pic = cell.telig_journal_pic;
                                            data2.type=CONTENT_IMG;
                                            data2.telig_journal_pic_description = cell.telig_journal_pic_description;
                                            listData.add(data2);
                                        }
                                        if(cell.telig_journal_content != null && !cell.telig_journal_content.equals("")){
                                            ShowLookQingData data3 = new ShowLookQingData();
                                            data3.telig_journal_content = cell.telig_journal_content;
                                            data3.type=CONTENT;
                                            listData.add(data3);
                                        }

                                        if(cell.telig_journal_from != null && !cell.telig_journal_from.equals("")){
                                            ShowLookQingData data4 = new ShowLookQingData();
                                            data4.telig_journal_from = cell.telig_journal_from;
                                            data4.telig_journal_ref_link = cell.telig_journal_ref_link;
                                            Log.d("lizisong", "cell.telig_journal_ref_link:"+cell.telig_journal_ref_link);
                                            data4.type=FROM;
                                            listData.add(data4);
                                            if(j != item.datas.size()-1){
                                                ShowLookQingData line1 = new ShowLookQingData();
                                                line1.type =  LINE1;
                                                listData.add(line1);
                                            }
                                        }
                                    }
                                }
                        }

                            if(adapter == null){
                                listview.addHeaderView(view);
                                adapter = new LookQingBaoAdapter();
                                listview.setAdapter(adapter);
                            }

                    }


                }
                progress.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("查看情报");
        MobclickAgent.onResume(this);
        tongji();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看情报");
        MobclickAgent.onPause(this);
    }

    class LookQingBaoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
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
                convertView = View.inflate(LookQingBaoActivity.this, R.layout.lookqingbaoadapter,null);
                holdView.big_title = (LinearLayout)convertView.findViewById(R.id.big_title);
                holdView.small_title = (LinearLayout)convertView.findViewById(R.id.small_title);
                holdView.shouqi = (LinearLayout)convertView.findViewById(R.id.shouqi);
                holdView.content_big_title_lay = (LinearLayout)convertView.findViewById(R.id.content_big_title_lay);
                holdView.content_small_title_lay = (LinearLayout)convertView.findViewById(R.id.content_small_title_lay);
                holdView.img_lay = (LinearLayout)convertView.findViewById(R.id.img_lay);
                holdView.chakan  = (TextView)convertView.findViewById(R.id.chakan);
                holdView.content = (TextView)convertView.findViewById(R.id.content);
                holdView.laiyuan = (TextView)convertView.findViewById(R.id.laiyuan);
                holdView.content_img = (TextView)convertView.findViewById(R.id.content_img);
                holdView.content_small_title = (TextView)convertView.findViewById(R.id.content_small_title);
                holdView.content_big_title = (TextView)convertView.findViewById(R.id.content_big_title);
                holdView.small = (TextView)convertView.findViewById(R.id.small);
                holdView.line = (LinearLayout) convertView.findViewById(R.id.line);
                holdView.line1 = (TextView)convertView.findViewById(R.id.line1);
                holdView.lanyuanlay =(RelativeLayout)convertView.findViewById(R.id.lanyuanlay);

                holdView.big = (TextView)convertView.findViewById(R.id.big);
                holdView.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final ShowLookQingData item = listData.get(position);
            if(item.type == BIG_TITLE_SHOW){
                if(state == 1){
                    holdView.big_title.setVisibility(View.VISIBLE);
                }else{
                    holdView.big_title.setVisibility(View.GONE);
                }

                holdView.small_title.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.big.setText(item.theme);
                holdView.big_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         String title = item.theme;
                         for(int i=0; i<listData.size();i++){
                            ShowLookQingData item = listData.get(i);
                            if(item.type ==BIG_TITLE){
                                if(item.theme.equals(title)){
                                    listview.setSelection(i);
//                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                });
            }else if(item.type == SMALL_TITLE_SHOW){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                if(state == 1){
                    holdView.small_title.setVisibility(View.VISIBLE);
                }else{
                    holdView.small_title.setVisibility(View.GONE);
                }

                holdView.shouqi.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                holdView.small.setText(item.telig_journal_theme);
                holdView.small_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = item.telig_journal_theme;
                        for(int i=0; i<listData.size();i++){
                            ShowLookQingData item = listData.get(i);
                            if(item.type ==SMALL_TITLE){
                                if(item.telig_journal_title.equals(title)){
                                    listview.setSelection(i);
//                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                });
            }else if(item.type ==SHOUQI_TITLE_SHOW){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                if(state ==1){
                  holdView.shouqi.setVisibility(View.VISIBLE);
                }else{
                    holdView.shouqi.setVisibility(View.GONE);
                }
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                holdView.shouqi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state=0;
                        shouqi.setText("展开");
                        shouqiicon.setBackgroundResource(R.mipmap.down);
                        adapter.notifyDataSetChanged();
                    }
                });
            }else if(item.type == BIG_TITLE){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.VISIBLE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                holdView.content_big_title.setText(item.theme);
            }else if(item.type == SMALL_TITLE){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.VISIBLE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                holdView.content_small_title.setText(item.telig_journal_title);
            }else if(item.type == CONTENT_IMG){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.VISIBLE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.telig_journal_pic
                        , holdView.img, options);
                holdView.content_img.setText(item.telig_journal_pic_description);

            }else if(item.type == CONTENT){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.VISIBLE);
                holdView.laiyuan.setVisibility(View.GONE);
                String [] temp = item.telig_journal_content.split("\r\n");
                String content ="";
                try {
                    for(int i=0; i<temp.length; i++){
                        String line = Html.fromHtml(temp[i]).toString();
                        if(line.equals("")){
                            continue;
                        }
                        content=content+"    "+line.replaceAll("\r\n","").replaceAll("\n","")+"\r\n";
                    }
                }catch (Exception e){

                }

                holdView.content.setText(content+"\r\n");
            }else if(item.type == FROM){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.VISIBLE);
                holdView.lanyuanlay.setVisibility(View.VISIBLE);
                holdView.laiyuan.setText(item.telig_journal_from);
                if(item.telig_journal_ref_link != null && !item.telig_journal_ref_link.equals("")){
                    holdView.chakan.setVisibility(View.VISIBLE);
                }else{
                    holdView.chakan.setVisibility(View.GONE);
                }
                holdView.chakan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.telig_journal_ref_link != null && !item.telig_journal_ref_link.equals("")){
                            Intent intent=new Intent(LookQingBaoActivity.this, WebViewActivity.class);
                            intent.putExtra("title", "");
                            intent.putExtra("url", item.telig_journal_ref_link);
                            startActivity(intent);
                        }
                    }
                });
            }else if(item.type == LINE){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.VISIBLE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.GONE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
            }else if(item.type == LINE1){
                holdView.big_title.setVisibility(View.GONE);
                holdView.line.setVisibility(View.GONE);
                holdView.small_title.setVisibility(View.GONE);
                holdView.lanyuanlay.setVisibility(View.GONE);
                holdView.shouqi.setVisibility(View.GONE);
                holdView.line1.setVisibility(View.VISIBLE);
                holdView.content_big_title_lay.setVisibility(View.GONE);
                holdView.content_small_title_lay.setVisibility(View.GONE);
                holdView.img_lay.setVisibility(View.GONE);
                holdView.content.setVisibility(View.GONE);
                holdView.laiyuan.setVisibility(View.GONE);
            }

            return convertView;
        }
        class HoldView{
            public LinearLayout big_title,small_title,shouqi,content_big_title_lay,content_small_title_lay,img_lay,line;
            public TextView content,laiyuan,content_img,content_small_title,content_big_title,small,big,line1;
            public ImageView img;
            public TextView chakan;
            public RelativeLayout lanyuanlay;
        }

    }
    private void tongji(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String logid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                String url;
                if(logid.equals("0")){
                     logid=MyApplication.deviceid;
                     url = "http://"+MyApplication.ip+"/api/tellig/telig_action_record.php?"+"&tempId="+logid+"&teligjournalid="+teligjournalid;
                }else{
                     url = "http://"+MyApplication.ip+"/api/tellig/telig_action_record.php?"+"&mid="+logid+"&teligjournalid="+teligjournalid;
                }
                OkHttpUtils.loaudstringfromurl(url);
            }
        }).start();
    }

    private void shareonclick() {
        try {

            Intent intent = new Intent(LookQingBaoActivity.this, ShareActivity.class);
            intent.putExtra("title", data.data.telig_journal_title);
            intent.putExtra("txt", data.data.telig_journal_title);
            intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/plus/activity/hd/intelligence_deatil_share.html?telig_journal_id="+teligjournalid);
            intent.putExtra("imageurl", "http://www.zhongkechuangxiang.com/plus/activity/images/Introduction_page.jpg");
            startActivity(intent);

//            UIHelper.showDialogForLoading(LookQingBaoActivity.this, "", true);
//
//            image = new UMImage(LookQingBaoActivity.this, "http://www.zhongkechuangxiang.com/plus/activity/images/Introduction_page.jpg");
//
//
//            final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                    {
//                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                    };
//            action = new ShareAction(LookQingBaoActivity.this);
//            UIHelper.hideDialogForLoading();
//
//            action.setDisplayList(displaylist)
//                    .withText(data.data.telig_journal_title)
//                    .withTitle(data.data.telig_journal_title)
//                    .withTargetUrl("http://www.zhongkechuangxiang.com/plus/activity/hd/intelligence_deatil_share.html?telig_journal_id="+teligjournalid)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .open();

        } catch (Exception e) {
        }
    }
//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//            } else {
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(LookQingBaoActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
//            if (t != null) {
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
