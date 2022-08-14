package fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AppointmentSpecialist;
import com.maidiantech.DFBaoTouMore;
import com.maidiantech.DFInfoShow;
import com.maidiantech.DetailsActivity;
import com.maidiantech.LocalCity;
import com.maidiantech.LookMoreActivity;
import com.maidiantech.MainActivity;
import com.maidiantech.MyXuqiuActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewJingPinProject;
import com.maidiantech.NewProJect;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewRenCaiDetail;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.NewSearchHistory;
import com.maidiantech.NewZhuanliActivity;
import com.maidiantech.PatentActivity;
import com.maidiantech.PersonActivity;
import com.maidiantech.PolicyActivity;
import com.maidiantech.ProJect;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.QingBaoDeilActivity;
import com.maidiantech.R;
import com.maidiantech.SheBeiActivity;
import com.maidiantech.TianXuQiu;
import com.maidiantech.TopicInformation;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XiangMuDuiJieActivity;
import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.ZhuanLiShenQing;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Port.SollectInterface;
import Util.NetUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import Util.WeatherUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import entity.ColumnData;
import entity.CommHomeData;
import entity.CommonFrgEntity;
import entity.DeviceShow;
import entity.HomeEntity;
import entity.KeySort;
import entity.PlaceChannel;
import entity.PostData;
import entity.PostsData;
import entity.Ret;
import entity.SeqData;
import entity.SeqList;
import entity.XInZhiNow;
import entity.XinzhiTianqi;
import entity.XinzhiTianqiData;
import view.HeaderListView;
import view.MyClickListener;
import view.RoundImageView;
import view.ScrollBanner;
import view.SystemBarTintManager;
import view.ZQImageViewRoundOval;

/**
 * Created by Administrator on 2019/2/27.
 */

public class CommonFragment extends BaseFragment implements SollectInterface {
    CommonFrgEntity data;
    String bgpic="";
    String titles,picUrl,url,img,channelid;
    private View view = null;
    HeaderListView listview;
    LinearLayout title;
    TextView nice_spinner1,sosocontent;
    ImageView nodata;
    ProgressBar progress;
    View HeaderView;
    public String sortid;
    DisplayImageOptions options;
    //头部
    ImageView banner_top,tianqi,linyi;
    RelativeLayout search_lay1;
    TextView low,heihgt,nowwendu;
    int screenWidth;
    TextView tab_zhaofuwu;
    TextView tab_tianxuqiu;
    LinearLayout tab_btn;
    LinearLayout zhaofuwu_bg;
    LinearLayout tianxuqiu_bg;

    //三种布局的情况
    LinearLayout three_cloum1,three_cloum2;
    RelativeLayout three_cloum1_item1,three_cloum1_item2, three_cloum1_item3, three_cloum2_item1,three_cloum2_item2,three_cloum2_item3;
    ImageView three_cloum2_img1,three_cloum2_img2,three_cloum2_img3,three_cloum1_img1,three_cloum1_img2,three_cloum1_img3;
    TextView three_cloum1_text_item1,three_cloum1_text_item2,three_cloum1_text_item3,three_cloum2_text_item1,three_cloum2_text_item2,three_cloum2_text_item3;
    //四种布局的情况
    LinearLayout four_cloum1,four_cloum2;
    RelativeLayout four_cloum1_item1,four_cloum1_item2,four_cloum1_item3,four_cloum1_item4,four_cloum2_item1,four_cloum2_item2, four_cloum2_item3,four_cloum2_item4;
    ImageView four_cloum1_img1,four_cloum1_img2,four_cloum1_img3,four_cloum1_img4,four_cloum2_img1,four_cloum2_img2,four_cloum2_img3,four_cloum2_img4;
    TextView four_cloum1_text_item1,four_cloum1_text_item2,four_cloum1_text_item3,four_cloum1_text_item4,four_cloum2_text_item1,four_cloum2_text_item2,four_cloum2_text_item3,four_cloum2_text_item4;
    //五种布局情况
    LinearLayout five_cloum1,five_cloum2;
    RelativeLayout five_cloum1_item1,five_cloum1_item2,five_cloum1_item3,five_cloum1_item4,five_cloum1_item5,five_cloum2_item1,five_cloum2_item2,five_cloum2_item3,five_cloum2_item4,five_cloum2_item5;
    ImageView five_cloum1_img1,five_cloum1_img2,five_cloum1_img3,five_cloum1_img4,five_cloum1_img5,five_cloum2_img1,five_cloum2_img2,five_cloum2_img3,five_cloum2_img4,five_cloum2_img5;
    TextView five_cloum1_text_item1,five_cloum1_text_item2,five_cloum1_text_item3,five_cloum1_text_item4,five_cloum1_text_item5,five_cloum2_text_item1,five_cloum2_text_item2,five_cloum2_text_item3,five_cloum2_text_item4,five_cloum2_text_item5;

    //第二排单个布局的情况
    LinearLayout oneImage_line;
    ImageView oneImage,sousu;
    EditText text;
    TextView counttxt,commit;
    PlaceChannel currentChannel;
    boolean isherat = true;
    boolean isFirstClick= true,NewPageisOpen =false;
    float lastY;
    SystemBarTintManager tintManager ;
    String city,oldCity="",type;
    String code;
    ColumnData currentColumnData;
    String newtem="", heihttem="",lowtem="",newtxt;
    List<KeySort> showList = new ArrayList<>();
    public HashMap<String, String> zixuanaid = new HashMap<>();
    public List<String> zixuanlist = new ArrayList<>();
    ReceiverBroad receiverBroad;
    ScrollBanner mScrollBanner;
    public List<SeqData> advListData = new ArrayList<>();
    public List<DeviceShow> deviceShows = new ArrayList<>();
    MyDeviceAdapter myDeviceAdapter = new MyDeviceAdapter();
    public List<SeqData> advJingPinData = new ArrayList<>();
    MyXiangMuAdapter xiangMuAdapter = new MyXiangMuAdapter();
    private ArrayList<ImageView> tips;
    int widthPixels;
    int heightPixels;
    MyAdapter advAdapter ;
    Adpater adpater;
    boolean isFristadd =false;
    int current = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
             view = inflater.inflate(R.layout.commonfragment, null);
             listview =(HeaderListView)view.findViewById(R.id.listview);
             progress =  (ProgressBar)view.findViewById(R.id.progress);
             title = (LinearLayout)view.findViewById(R.id.title);
             nice_spinner1 = (TextView)view.findViewById(R.id.nice_spinner1);
             nodata = (ImageView)view.findViewById(R.id.nodata);
             sosocontent = (TextView)view.findViewById(R.id.sosocontent);
             sousu = (ImageView)view.findViewById(R.id.sousu);
             HeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.commonfragmentheart, null);
             options = ImageLoaderUtils.initOptions();
             banner_top = (ImageView)HeaderView.findViewById(R.id.banner_top);
             tianqi = (ImageView)HeaderView.findViewById(R.id.tianqi);
             linyi  = (ImageView)HeaderView.findViewById(R.id.linyi);
             search_lay1 = (RelativeLayout)HeaderView.findViewById(R.id.search_lay1);
             low = (TextView)HeaderView.findViewById(R.id.low);
             heihgt = (TextView)HeaderView.findViewById(R.id.heihgt);
             nowwendu = (TextView)HeaderView.findViewById(R.id.nowwendu);
             WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
             screenWidth = wm.getDefaultDisplay().getWidth();
            tab_zhaofuwu = (TextView)HeaderView.findViewById(R.id.tab_zhaofuwu);
            tab_btn = (LinearLayout)HeaderView.findViewById(R.id.tab_btn);
            tab_tianxuqiu = (TextView)HeaderView.findViewById(R.id.tab_tianxuqiu);
            zhaofuwu_bg = (LinearLayout)HeaderView.findViewById(R.id.zhaofuwu_bg);
            tianxuqiu_bg = (LinearLayout) HeaderView.findViewById(R.id.tianxuqiu_bg);
            //三种情况
            three_cloum1 = (LinearLayout)HeaderView.findViewById(R.id.three_cloum1);
            three_cloum2 = (LinearLayout)HeaderView.findViewById(R.id.three_cloum2);
            three_cloum1_item1 = (RelativeLayout)HeaderView.findViewById(R.id.three_cloum1_item1);
            three_cloum1_item2 = (RelativeLayout)HeaderView.findViewById(R.id.three_cloum1_item2);
            three_cloum1_item3 = (RelativeLayout)HeaderView.findViewById(R.id.three_cloum1_item3);
            three_cloum2_item1 =(RelativeLayout)HeaderView.findViewById(R.id.three_cloum2_item1);
            three_cloum2_item2 =(RelativeLayout)HeaderView.findViewById(R.id.three_cloum2_item2);
            three_cloum2_item3 =(RelativeLayout)HeaderView.findViewById(R.id.three_cloum2_item3);
            three_cloum1_img1 = (ImageView)HeaderView.findViewById(R.id.three_cloum1_img1);
            three_cloum1_img2 = (ImageView)HeaderView.findViewById(R.id.three_cloum1_img2);
            three_cloum1_img3 = (ImageView)HeaderView.findViewById(R.id.three_cloum1_img3);
            three_cloum2_img1 = (ImageView)HeaderView.findViewById(R.id.three_cloum2_img1);
            three_cloum2_img2 = (ImageView)HeaderView.findViewById(R.id.three_cloum2_img2);
            three_cloum2_img3 = (ImageView)HeaderView.findViewById(R.id.three_cloum2_img3);
            three_cloum1_text_item1 = (TextView)HeaderView.findViewById(R.id.three_cloum1_text_item1);
            three_cloum1_text_item2 = (TextView)HeaderView.findViewById(R.id.three_cloum1_text_item2);
            three_cloum1_text_item3 = (TextView)HeaderView.findViewById(R.id.three_cloum1_text_item3);
            three_cloum2_text_item1 = (TextView)HeaderView.findViewById(R.id.three_cloum2_text_item1);
            three_cloum2_text_item2 = (TextView)HeaderView.findViewById(R.id.three_cloum2_text_item2);
            three_cloum2_text_item3 = (TextView)HeaderView.findViewById(R.id.three_cloum2_text_item3);

            //四种情况
            four_cloum1 = (LinearLayout)HeaderView.findViewById(R.id.four_cloum1);
            four_cloum2 = (LinearLayout)HeaderView.findViewById(R.id.four_cloum2);
            four_cloum1_item1 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item1);
            four_cloum1_item2 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item2);
            four_cloum1_item3 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item3);
            four_cloum1_item4 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item4);
            four_cloum2_item1 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum2_item1);
            four_cloum2_item2 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum2_item2);
            four_cloum2_item3 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum2_item3);
            four_cloum2_item4 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum2_item4);
            four_cloum1_img1 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img1);
            four_cloum1_img2 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img2);
            four_cloum1_img3 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img3);
            four_cloum1_img4 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img4);
            four_cloum2_img1 = (ImageView)HeaderView.findViewById(R.id.four_cloum2_img1);
            four_cloum2_img2 = (ImageView)HeaderView.findViewById(R.id.four_cloum2_img2);
            four_cloum2_img3 = (ImageView)HeaderView.findViewById(R.id.four_cloum2_img3);
            four_cloum2_img4 = (ImageView)HeaderView.findViewById(R.id.four_cloum2_img4);
            four_cloum1_text_item1 = (TextView) HeaderView.findViewById(R.id.four_cloum1_text_item1);
            four_cloum1_text_item2 = (TextView) HeaderView.findViewById(R.id.four_cloum1_text_item2);
            four_cloum1_text_item3 = (TextView) HeaderView.findViewById(R.id.four_cloum1_text_item3);
            four_cloum1_text_item4 = (TextView) HeaderView.findViewById(R.id.four_cloum1_text_item4);
            four_cloum2_text_item1 = (TextView) HeaderView.findViewById(R.id.four_cloum2_text_item1);
            four_cloum2_text_item2 = (TextView) HeaderView.findViewById(R.id.four_cloum2_text_item2);
            four_cloum2_text_item3 = (TextView) HeaderView.findViewById(R.id.four_cloum2_text_item3);
            four_cloum2_text_item4 = (TextView) HeaderView.findViewById(R.id.four_cloum2_text_item4);

            //五种情况
            five_cloum1 = (LinearLayout)HeaderView.findViewById(R.id.five_cloum1);
            five_cloum2 = (LinearLayout)HeaderView.findViewById(R.id.five_cloum2);
            five_cloum1_item1 =(RelativeLayout)HeaderView.findViewById(R.id.five_cloum1_item1);
            five_cloum1_item2 =(RelativeLayout)HeaderView.findViewById(R.id.five_cloum1_item2);
            five_cloum1_item3 =(RelativeLayout)HeaderView.findViewById(R.id.five_cloum1_item3);
            five_cloum1_item4 =(RelativeLayout)HeaderView.findViewById(R.id.five_cloum1_item4);
            five_cloum1_item5 =(RelativeLayout)HeaderView.findViewById(R.id.five_cloum1_item5);
            five_cloum2_item1 = (RelativeLayout)HeaderView.findViewById(R.id.five_cloum2_item1);
            five_cloum2_item2 = (RelativeLayout)HeaderView.findViewById(R.id.five_cloum2_item2);
            five_cloum2_item3 = (RelativeLayout)HeaderView.findViewById(R.id.five_cloum2_item3);
            five_cloum2_item4 = (RelativeLayout)HeaderView.findViewById(R.id.five_cloum2_item4);
            five_cloum2_item5 = (RelativeLayout)HeaderView.findViewById(R.id.five_cloum2_item5);
            five_cloum1_img1 = (ImageView)HeaderView.findViewById(R.id.five_cloum1_img1);
            five_cloum1_img2 = (ImageView)HeaderView.findViewById(R.id.five_cloum1_img2);
            five_cloum1_img3 = (ImageView)HeaderView.findViewById(R.id.five_cloum1_img3);
            five_cloum1_img4 = (ImageView)HeaderView.findViewById(R.id.five_cloum1_img4);
            five_cloum1_img5 = (ImageView)HeaderView.findViewById(R.id.five_cloum1_img5);
            five_cloum2_img1 = (ImageView)HeaderView.findViewById(R.id.five_cloum2_img1);
            five_cloum2_img2 = (ImageView)HeaderView.findViewById(R.id.five_cloum2_img2);
            five_cloum2_img3 = (ImageView)HeaderView.findViewById(R.id.five_cloum2_img3);
            five_cloum2_img4 = (ImageView)HeaderView.findViewById(R.id.five_cloum2_img4);
            five_cloum2_img5 = (ImageView)HeaderView.findViewById(R.id.five_cloum2_img5);
            five_cloum1_text_item1 = (TextView)HeaderView.findViewById(R.id.five_cloum1_text_item1);
            five_cloum1_text_item2 = (TextView)HeaderView.findViewById(R.id.five_cloum1_text_item2);
            five_cloum1_text_item3 = (TextView)HeaderView.findViewById(R.id.five_cloum1_text_item3);
            five_cloum1_text_item4 = (TextView)HeaderView.findViewById(R.id.five_cloum1_text_item4);
            five_cloum1_text_item5 = (TextView)HeaderView.findViewById(R.id.five_cloum1_text_item5);

            five_cloum2_text_item1 = (TextView)HeaderView.findViewById(R.id.five_cloum2_text_item1);
            five_cloum2_text_item2 = (TextView)HeaderView.findViewById(R.id.five_cloum2_text_item2);
            five_cloum2_text_item3 = (TextView)HeaderView.findViewById(R.id.five_cloum2_text_item3);
            five_cloum2_text_item4 = (TextView)HeaderView.findViewById(R.id.five_cloum2_text_item4);
            five_cloum2_text_item5 = (TextView)HeaderView.findViewById(R.id.five_cloum2_text_item5);

            //第二排单张图的情况
            oneImage = (ImageView)HeaderView.findViewById(R.id.oneImage);
            oneImage_line = (LinearLayout) HeaderView.findViewById(R.id.oneImage_line);
            text = (EditText) HeaderView.findViewById(R.id.text);
            counttxt = (TextView)HeaderView.findViewById(R.id.count);
            commit = (TextView)HeaderView.findViewById(R.id.commit);
            DisplayMetrics metrics=new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            widthPixels=metrics.widthPixels;
            heightPixels=metrics.heightPixels;
        }
        text.addTextChangedListener(new  TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counttxt.setText(s.toString().length()+"/140字");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sosocontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
        sousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = text.getText().toString();
                if(txt == null || txt.equals("")){
                    Toast.makeText(getActivity(), "请填写需求内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                String login = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(login.equals("1")){
                    progress.setVisibility(View.VISIBLE);
                    postxuqiu();
                }else{
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }

            }
        });
        tab_zhaofuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_btn.setBackgroundResource(R.mipmap.xuanzhong_right);
                zhaofuwu_bg.setVisibility(View.VISIBLE);
                tianxuqiu_bg.setVisibility(View.INVISIBLE);
                tab_zhaofuwu.setTextColor(0xff181818);
                tab_tianxuqiu.setTextColor(0xff8b8b8b);
                try {
                    if(currentChannel.column2.size() == 1){
                        oneImage_line.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){

                }

            }
        });
        nice_spinner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocalCity.class);
                startActivity(intent);
            }
        });
        search_lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });

        nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                } else {
                    ICONJson();
                    getJoson();
                }
            }
        });
        tab_tianxuqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_btn.setBackgroundResource(R.mipmap.xuanzhong_left);
                zhaofuwu_bg.setVisibility(View.INVISIBLE);
                tianxuqiu_bg.setVisibility(View.VISIBLE);
                oneImage_line.setVisibility(View.GONE);
                tab_zhaofuwu.setTextColor(0xff8b8b8b);
                tab_tianxuqiu.setTextColor(0xff181818);
            }
        });

        linyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocalCity.class);
                startActivity(intent);
            }
        });

//        if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listview.getLayoutParams();
//            layoutParams.bottomMargin=MyApplication.navigationbar+150;
//            listview.setLayoutParams(layoutParams);
//        }

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isFirstClick){
                    lastY=event.getY();
                    isFirstClick=false;//初始值是true，此处置为false。
                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        if(moveY < lastY){
                            listview.setState(true);
                        }else{
                            if(isherat){
                                listview.setState(true);
                            }else {
                                listview.setState(true);
                            }
                        }
                        lastY=moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        isFirstClick =true;
                        NewPageisOpen = false;
//                            listView.setState(true);
                        break;
                }
                return false;
            }
        });

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
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isherat = true;
                    }
                }else{
                    isherat = false;
                }
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
                    if(h >290-25){
                        int cha = h-(290-25);
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            title.setAlpha(alpha);
                        }else{
                            title.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }
                        title.setVisibility(View.VISIBLE);

                    }else {
//                            title.getBackground().setAlpha(290-25-h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        title.setVisibility(View.GONE);

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
                    return 400;
                }
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverBroad);
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mScrollBanner != null){
            mScrollBanner.setList(zixuanlist);
        }
        three_cloum1_item1.setClickable(true);
        three_cloum1_item2.setClickable(true);
        three_cloum1_item3.setClickable(true);
        three_cloum2_item1.setClickable(true);
        three_cloum2_item2.setClickable(true);
        three_cloum2_item3.setClickable(true);
        oneImage.setClickable(true);
        banner_top.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                banner_top.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                listview.setHeight(banner_top.getHeight());
            }
        });


        city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "呼和浩特");
        if(city.contains("全国") || city.contains("潍坊") || city.contains("德州") || city.contains("郑州")
                ||  city.contains("聊城") || city.contains("莱西") || city.contains("即墨") || city.contains("包头") || city.contains("临沂市")
                ||  city.contains("江阴") ){
            city = "呼和浩特";
        }
//        if(city.equals("包头")){
//            WeatherUtils.xinzhitianqi1(city,handler);
//            WeatherUtils.xinzhitianqi2(city,handler);
//        }else if(city.contains("临沂")){
//            WeatherUtils.xinzhitianqi1(city,handler);
//            WeatherUtils.xinzhitianqi2(city,handler);
//        }else if(city.contains("江阴")){
//            WeatherUtils.xinzhitianqi1("无锡",handler);
//            WeatherUtils.xinzhitianqi2("无锡",handler);
//        }else{
//            WeatherUtils.xinzhitianqi1("包头",handler);
//            WeatherUtils.xinzhitianqi2("包头",handler);
//        }
        for(int i=0; i< MainActivity.columnButton.size();i++){
            PlaceChannel item = MainActivity.columnButton.get(i);
            if(item.nativeplace.contains(city)){
                currentChannel = item;
                code = item.code;
                channelid = item.channelid;
                break;
            }
        }
        if(currentChannel != null){
              WeatherUtils.xinzhitianqi1(currentChannel.englishName,handler);
              WeatherUtils.xinzhitianqi2(currentChannel.englishName,handler);
        }
        //处理三张图的情况
        handlerThreeCase();
        //处理四张图的情况
        handlerFourCase();
        //处理五张图的情况
        handlerFiveCase();
        if(!city.equals(oldCity) && currentChannel != null){
            Log.d("lizisong", "currentChannel.channelid"+currentChannel.channelid);
            oldCity = city;
            progress.setVisibility(View.VISIBLE);
            getJoson();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tintManager = new SystemBarTintManager(getActivity());
        receiverBroad = new ReceiverBroad();
        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("downok");
        oninfilter.addAction("changetitles");
        oninfilter.addAction("changelistviewheihgt");
        getActivity().registerReceiver(receiverBroad, oninfilter);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void lazyLoad() {

    }
    public void getJoson(){
        String url ="http://"+MyApplication.ip+"/api/newchannelrec.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("channelid", currentChannel.channelid);
        networkCom.getJson(url,map,handler,0,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 0){
                    progress.setVisibility(View.GONE);
                    String ret = (String) msg.obj;
                    Gson gson = new Gson();
                    data = gson.fromJson(ret, CommonFrgEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            showList.clear();
                            bgpic = data.data.head.picUrl;
                            titles = data.data.head.title;
                            picUrl = data.data.head.picUrl;
                            url = data.data.head.url;
                            img = data.data.head.img;
                            ImageLoader.getInstance().displayImage(picUrl
                                    , banner_top, options);
                            nice_spinner1.setText(city);
//                            int width = 40
//                            ViewGroup.LayoutParams lp = linyi.getLayoutParams();
//                            lp.width = heiht;
//                            holdView.yi1.setLayoutParams(lp);
                            ImageLoader.getInstance().displayImage(img
                                    , linyi, options);
                            if(data.data.seq != null){
                                for(int i=0; i<data.data.seq.size(); i++){
                                    KeySort item = data.data.seq.get(i);
                                    if(item.list!=null && item.list.size()>0){
                                         showList.add(item);
                                    }
                                    if(item.name.equals("zixun")){
                                        String pos = "";
                                        String aids="";
                                        int p=0;
                                        zixuanaid.clear();
                                        zixuanlist.clear();
                                        for(int j=0; j<item.list.size(); j++){
                                            SeqData posdata = item.list.get(j);
                                            String type="【"+posdata.typename+"】";
                                            if(j % 2 ==0){
                                                pos="";
                                                aids="";
                                                pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                                aids=posdata.id+",";

                                            }else if(j %2 == 1){
                                                pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                                aids=aids+posdata.id;
                                                zixuanaid.put(p+"",aids);
                                                p++;
                                                zixuanlist.add(pos);
                                            }
                                        }

                                        for(int j=0; j<item.list.size(); j++){
                                            SeqData posdata = item.list.get(j);
                                            String type="【"+posdata.typename+"】";
                                            if(j % 2 ==0){
                                                pos="";
                                                aids="";
                                                pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                                aids=posdata.id+",";

                                            }else if(j %2 == 1){
                                                pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                                aids=aids+posdata.id;
                                                zixuanaid.put(p+"",aids);
                                                p++;
                                                zixuanlist.add(pos);
                                            }
                                        }
                                    }else if(item.name.equals("adScroll")){
                                        advListData.clear();
                                        for(int j=0; j<item.list.size();j++){
                                            SeqData posdata = item.list.get(j);
                                            advListData.add(posdata);
                                        }
                                        if(tips == null){
                                            tips = new ArrayList<ImageView>();
                                        }else{
                                            tips.clear();
                                            isFristadd = false;
                                        }
                                        if(advListData.size() > 1){
                                            for (int j = 0; j < advListData.size(); j++) {
                                                //画圆点
                                                ImageView imageView = new ImageView(getActivity());
                                                if (j == 0) {
                                                    imageView.setImageResource(R.drawable.dot_focused);
                                                } else {
                                                    imageView.setImageResource(R.drawable.dot_normal);
                                                }
                                                tips.add(imageView);
                                            }
                                        }
                                        advAdapter = new MyAdapter();
                                    }else if(item.name.equals("device")){
                                        deviceShows.clear();
                                        DeviceShow deviceShow = null;
                                        for (int j=0;j<item.list.size();j++){
                                            int index = j %3;
                                            SeqData pos = item.list.get(j);
                                            if(index == 0){
                                                deviceShow = new DeviceShow();
                                                deviceShow.id1 = pos.id;
                                                deviceShow.title1 = pos.title;
                                                deviceShow.typeid1 = pos.typeid;
                                                deviceShow.litpic1 = pos.litpic;
                                                deviceShow.area_cate1 = pos.area_cate;
                                            }else if(index == 1){
                                                deviceShow.id2 = pos.id;
                                                deviceShow.title2 = pos.title;
                                                deviceShow.typeid2 = pos.typeid;
                                                deviceShow.litpic2 = pos.litpic;
                                                deviceShow.area_cate2 = pos.area_cate;
                                            }else if(index == 2){
                                                deviceShow.id3 = pos.id;
                                                deviceShow.title3 = pos.title;
                                                deviceShow.typeid3 = pos.typeid;
                                                deviceShow.litpic3 = pos.litpic;
                                                deviceShow.area_cate3 = pos.area_cate;
                                                deviceShows.add(deviceShow);
                                                deviceShow = null;
                                            }
                                            if(index == item.list.size() -1){
                                                if(deviceShow != null)
                                                    deviceShows.add(deviceShow);
                                            }
                                        }
                                    }else if(item.name.equals("jpxiangmu")){
                                        advJingPinData.clear();
                                          for(int j=0;j<item.list.size();j++){
                                              SeqData add = item.list.get(j);
                                              advJingPinData.add(add);
                                          }
                                    }
                                }
                            }
                            KeySort item = new KeySort();
                            item.name="dixian";
                            showList.add(item);
                            nodata.setVisibility(View.GONE);
                            if(adpater == null){
                                listview.setHeaderIV(banner_top);
                                listview.addHeaderView(HeaderView);
                                adpater = new Adpater();
                                listview.setAdapter(adpater);
                                adpater.notifyDataSetChanged();
                                if(advListData!=null && advListData.size() >1 ){
                                    Message message = Message.obtain();
                                    msg.what = 100;
                                    handler.sendMessageDelayed(message, 4000);
                                }

                            }else {
                                adpater.notifyDataSetChanged();
                            }

                        }else{
                            nodata.setVisibility(View.VISIBLE);
                        }
                    }else{
                        nodata.setVisibility(View.VISIBLE);
                    }
                }

                if(msg.what == 2){
                    progress.setVisibility(View.GONE);
                    Gson g=new Gson();
                    String json = (String)msg.obj;
                    Ret result =g.fromJson(json, Ret.class);
                    if(result.code.equals("1")){
                        Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                        text.setText("");
                        Intent intent = new Intent();
                        intent.setAction("action_update_conut");
                        getActivity().sendBroadcast(intent);
                        Intent intent1 = new Intent(getActivity(), MyXuqiuActivity.class);
                        intent1.putExtra("type","-1");
                        startActivity(intent1);
                    }
                }

                if(msg.what == 3){
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    HomeEntity homedata = gson.fromJson(json, HomeEntity.class);
                    if(homedata != null && homedata.code.equals("1")){
                        if(homedata.data.posts.xiangmu != null && homedata.data.posts.xiangmu.size() >0){
                            progress.setVisibility(View.GONE);
                            for(int i =0;i<showList.size();i++){
                                KeySort item = showList.get(i);
                                if(item.name.equals("xiangmu")){
                                    item.list.clear();
                                    for(int j=0; j<homedata.data.posts.xiangmu.size();j++){
                                        PostData post = homedata.data.posts.xiangmu.get(j);
                                        SeqData add = new SeqData();
                                        add.litpic = post.litpic;
                                        add.source = post.source;
                                        add.rank = post.rank;
                                        add.url = post.url;
                                        add.typeid = post.typeid;
                                        add.title = post.title;
                                        add.unit = post.unit;
                                        add.id = post.id;
                                        add.username = post.username;
                                        add.is_academician = post.is_academician;
                                        add.tags = post.tags;
                                        add.zan = post.zan;
                                        add.click = post.click;
                                        add.is_special = post.is_special;
                                        add.area_cate = post.area_cate;
                                        add.description = post.description;
                                        add.typename = post.typename;
                                        add.pubdate =post.pubdate;
                                        add.sortTime = post.sortTime;
                                        add.result = post.result;
                                        item.list.add(add);
                                    }
                                    adpater.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }

                if(msg.what == 5){
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    HomeEntity data= gson.fromJson(json, HomeEntity.class);
                    if(data != null){
                        List<PostData> zhuanli = data.data.posts.zhuanli;
                        if(zhuanli != null && zhuanli.size() >0){
                            progress.setVisibility(View.GONE);
                            for(int i =0;i<showList.size();i++){
                                KeySort item = showList.get(i);
                                if(item.name.equals("zhuanli")){
                                    item.list.clear();
                                    for(int j=0; j<zhuanli.size();j++){
                                        PostData post = zhuanli.get(j);
                                        SeqData add = new SeqData();
                                        add.litpic = post.litpic;
                                        add.source = post.source;
                                        add.rank = post.rank;
                                        add.url = post.url;
                                        add.typeid = post.typeid;
                                        add.title = post.title;
                                        add.unit = post.unit;
                                        add.id = post.id;
                                        add.username = post.username;
                                        add.is_academician = post.is_academician;
                                        add.tags = post.tags;
                                        add.zan = post.zan;
                                        add.click = post.click;
                                        add.is_special = post.is_special;
                                        add.area_cate = post.area_cate;
                                        add.description = post.description;
                                        add.typename = post.typename;
                                        add.pubdate =post.pubdate;
                                        add.sortTime = post.sortTime;
                                        add.result = post.result;
                                        item.list.add(add);
                                    }
                                    adpater.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }

                if(msg.what == 4){
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    HomeEntity data = gson.fromJson(json, HomeEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            progress.setVisibility(View.GONE);
                            List<PostData> rencai = data.data.posts.rencai;
                            if(rencai != null && rencai.size() >0){
                                for(int i =0;i<showList.size();i++){
                                    KeySort item = showList.get(i);
                                    if(item.name.equals("rencai")){
                                        item.list.clear();
                                        for(int j=0; j<rencai.size();j++){
                                            PostData post = rencai.get(j);
                                            SeqData add = new SeqData();
                                            add.litpic = post.litpic;
                                            add.source = post.source;
                                            add.rank = post.rank;
                                            add.url = post.url;
                                            add.typeid = post.typeid;
                                            add.title = post.title;
                                            add.unit = post.unit;
                                            add.id = post.id;
                                            add.username = post.username;
                                            add.is_academician = post.is_academician;
                                            add.tags = post.tags;
                                            add.zan = post.zan;
                                            add.click = post.click;
                                            add.is_special = post.is_special;
                                            add.area_cate = post.area_cate;
                                            add.description = post.description;
                                            add.typename = post.typename;
                                            add.pubdate =post.pubdate;
                                            add.sortTime = post.sortTime;
                                            add.result = post.result;
                                            item.list.add(add);
                                        }
                                        adpater.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }


                if(msg.what == 200){
                    try {
                        String json1 = (String)msg.obj;
                        JSONObject jsonObject = new JSONObject(json1);
                        int code = jsonObject.getInt("code");
                        if (code == 1) {
                            String body = jsonObject.getString("data");
                            JSONObject json = new JSONObject(body);
                            JSONArray acolumnButton = json.getJSONArray("columnButton");
                            for (int i = 0; i < acolumnButton.length(); i++) {
                                PlaceChannel item = new PlaceChannel();
                                JSONObject obj = acolumnButton.getJSONObject(i);
                                item.channelid = obj.getString("channelid");
                                item.code = obj.getString("code");
                                item.englishName = obj.getString("englishName");
                                item.nativeplace = obj.getString("nativeplace");
                                item.isOpen = obj.getString("isOpen");
                                item.selectPicUrl = obj.getString("selectPicUrl");
                                MyApplication.cityId.add(item.nativeplace);
                                item.picUrl = obj.getString("picUrl");
                                JSONArray column = obj.getJSONArray("column");
                                item.column1 = new ArrayList<>();
                                item.column2 = new ArrayList<>();
                                for (int j = 0; j < column.length(); j++) {
                                    JSONArray pos = column.getJSONArray(j);

                                    for (int z = 0; z < pos.length(); z++) {
                                        JSONObject object = pos.getJSONObject(z);
                                        ColumnData data = new ColumnData();
                                        data.imgUrl = object.getString("imgUrl");
                                        data.name = object.getString("name");
                                        data.type = object.getString("type");
                                        try {
                                            data.jumpUrl = object.getString("jumpUrl");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.channelid = object.getString("channelid");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }
                                        try {
                                            data.tag = object.getString("tag");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.flag = object.getString("flag");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.cityCode = object.getString("cityCode");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.userName = object.getString("userName");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }

                                        try {
                                            data.path = object.getString("path");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }
                                        try {
                                            data.miniProgramType = object.getString("miniProgramType");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }

                                        try {
                                            data.cityName = object.getString("cityName");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.prvName = object.getString("prvName");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.prvCode = object.getString("prvCode");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.typeid = object.getString("typeid");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.title = object.getString("title");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }


                                        if (j == 0) {
                                            item.column1.add(data);
                                        } else if (j == 1) {
                                            item.column2.add(data);
                                        }

                                    }
                                }
                                MainActivity.columnButton.add(item);

                            }
                            Intent intent = new Intent();
                            intent.setAction("downok");
                            getActivity().sendBroadcast(intent);

                        }
                    } catch (Exception e) {

                    }
                }

                if(msg.what == 500){
                    Gson gson = new Gson();
                    String ret = (String) msg.obj;
                    XinzhiTianqi data = gson.fromJson(ret, XinzhiTianqi.class);
                    if(data != null){
                        try {
                            XinzhiTianqiData now = data.results.get(0);
                            newtem = now.now.temperature;
                            newtxt = now.now.text;
                            nowwendu.setText(newtem);
                            if(newtxt.contains("晴转多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雷阵雨")){
                                tianqi.setImageResource(R.mipmap.leizhenyun);
                            }else if(newtxt.contains("多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雨夹雪")){
                                tianqi.setImageResource(R.mipmap.yujiaxue);
                            }else if(newtxt.contains("浮尘")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("沙")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("晴")){
                                tianqi.setImageResource(R.mipmap.qing);
                            }else if(newtxt.contains("阴")){
                                tianqi.setImageResource(R.mipmap.yin);
                            }else if(newtxt.contains("雪")){
                                tianqi.setImageResource(R.mipmap.xue);
                            }else if(newtxt.contains("雨")){
                                tianqi.setImageResource(R.mipmap.yue1);
                            }else if(newtxt.contains("霾")){
                                tianqi.setImageResource(R.mipmap.li);
                            }

                        }catch (Exception e){

                        }
                    }

                }


                if(msg.what == 100){
                    Message message =Message.obtain();
                    message.what=100;
                    if(adpater != null){
                        if(adpater.viewPager != null){
                            int item = adpater.viewPager.getCurrentItem();
                            item++;

                            adpater.viewPager.setCurrentItem(item);
                        }
                    }
                    handler.sendMessageDelayed(message, 4000);
                }

                if(msg.what == 501){
                    Gson gson = new Gson();
                    String ret = (String) msg.obj;
                    XinzhiTianqi data = gson.fromJson(ret, XinzhiTianqi.class);
                    if(data != null){
                        XinzhiTianqiData now = data.results.get(0);
                        XInZhiNow hlwendu =now.daily.get(0);
                        if(hlwendu != null){
                            heihttem = hlwendu.high;
                            lowtem = hlwendu.low;

                            low.setText(lowtem);
                            heihgt.setText(heihttem);

//                            Log.d("lizisong", )
//                            if(hlwendu.high.length() >hlwendu.low.length()){
//                                low.setWidth(heihgt.getMeasuredWidth());
//                            }
//
//                            if(hlwendu.high.length()<hlwendu.low.length()){
//                                heihgt.setWidth(low.getMeasuredWidth());
//                            }

                        }
                    }
                }

            }catch (Exception e){
                nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void postxuqiu(){
        String  mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
        HashMap<String,String> map = new HashMap<>();
        map.put("mid",mid);
        map.put("entry_cate","2");
        map.put("method","add");
        map.put("content",text.getText().toString());
        map.put("entry_address","2");
        String url = "http://"+ MyApplication.ip+"/api/require_new.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,2,0);
    }

    class ReceiverBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("downok")){
                city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "呼和浩特");
                if(city.contains("全国") || city.contains("潍坊") || city.contains("德州") || city.contains("郑州")
                        ||  city.contains("聊城") || city.contains("莱西") || city.contains("即墨") || city.contains("包头") || city.contains("临沂市")
                        ||  city.contains("江阴") ){
                    city = "呼和浩特";
                }
//                if(city.equals("包头")){
//                    WeatherUtils.xinzhitianqi1(city,handler);
//                    WeatherUtils.xinzhitianqi2(city,handler);
//                }else if(city.contains("临沂")){
//                    WeatherUtils.xinzhitianqi1(city,handler);
//                    WeatherUtils.xinzhitianqi2(city,handler);
//                }else if(city.contains("江阴")){
//                    WeatherUtils.xinzhitianqi1("无锡",handler);
//                    WeatherUtils.xinzhitianqi2("无锡",handler);
//                }else{
//                    WeatherUtils.xinzhitianqi1("包头",handler);
//                    WeatherUtils.xinzhitianqi2("包头",handler);
//                }
                for(int i=0; i< MainActivity.columnButton.size();i++){
                    PlaceChannel item = MainActivity.columnButton.get(i);
                    if(item.nativeplace.contains(city)){
                        currentChannel = item;
                        code = item.code;
                        channelid = item.channelid;
                        break;
                    }
                }
                if(currentChannel != null){
                    WeatherUtils.xinzhitianqi1(currentChannel.englishName,handler);
                    WeatherUtils.xinzhitianqi2(currentChannel.englishName,handler);
                }
                //处理三张图的情况
                handlerThreeCase();
                //处理四张图的情况
                handlerFourCase();
                //处理五张图的情况
                handlerFiveCase();
                if(!city.equals(oldCity)){
                    oldCity = city;
                    progress.setVisibility(View.VISIBLE);
                    getJoson();
                }

            }

        }
    }

    //处理第一排有三个ICON的情况
    private void handlerThreeCase(){
        try {
            ColumnData item;
            int column1 = currentChannel.column1.size();
            int column2 = currentChannel.column2.size();
            int column ;
            if(column1 > column2){
                column = column1;
            }else{
                column = column2;
            }
            if(column == 3){
                if(column == 3 && column2 > 1){
                    four_cloum1.setVisibility(View.GONE);
                    four_cloum2.setVisibility(View.GONE);
                    five_cloum1.setVisibility(View.GONE);
                    five_cloum2.setVisibility(View.GONE);
                    three_cloum1.setVisibility(View.VISIBLE);
                    three_cloum2.setVisibility(View.VISIBLE);
                    oneImage.setVisibility(View.GONE);
                    oneImage_line.setVisibility(View.GONE);
                    if(column1 == 1){
                        three_cloum1_item3.setVisibility(View.INVISIBLE);
                        three_cloum1_item2.setVisibility(View.INVISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);

                    }else if(column1 == 2){
                        three_cloum1_item3.setVisibility(View.INVISIBLE);
                        three_cloum1_item2.setVisibility(View.VISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);

                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);
                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img2, options);
                        three_cloum1_text_item2.setText(item.name);
                    }else if(column1 == 3){
                        three_cloum1_item3.setVisibility(View.VISIBLE);
                        three_cloum1_item2.setVisibility(View.VISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);

                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img2, options);
                        three_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img3, options);
                        three_cloum1_text_item3.setText(item.name);
                    }


                    if(currentChannel.column2.size() == 1){
                        three_cloum2_item3.setVisibility(View.INVISIBLE);
                        three_cloum2_item2.setVisibility(View.INVISIBLE);
                        three_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img1, options);
                        three_cloum2_text_item1.setText(item.name);

                    }else if(currentChannel.column2.size() == 2){
                        three_cloum2_item3.setVisibility(View.INVISIBLE);
                        three_cloum2_item2.setVisibility(View.VISIBLE);
                        three_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img1, options);
                        three_cloum2_text_item1.setText(item.name);
                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img2, options);
                        three_cloum2_text_item2.setText(item.name);
                    }else if(currentChannel.column2.size() == 3){
                        three_cloum2_item3.setVisibility(View.VISIBLE);
                        three_cloum2_item2.setVisibility(View.VISIBLE);
                        three_cloum2_item1.setVisibility(View.VISIBLE);

                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img1, options);
                        three_cloum2_text_item1.setText(item.name);
                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img2, options);
                        three_cloum2_text_item2.setText(item.name);
                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum2_img3, options);
                        three_cloum2_text_item3.setText(item.name);
                    }

                }else{
                    four_cloum1.setVisibility(View.GONE);
                    four_cloum2.setVisibility(View.GONE);
                    five_cloum1.setVisibility(View.GONE);
                    five_cloum2.setVisibility(View.GONE);
                    three_cloum1.setVisibility(View.VISIBLE);
                    three_cloum2.setVisibility(View.GONE);
                    oneImage.setVisibility(View.VISIBLE);
                    oneImage_line.setVisibility(View.VISIBLE);
                    if(column1 == 1){
                        three_cloum1_item3.setVisibility(View.INVISIBLE);
                        three_cloum1_item2.setVisibility(View.INVISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);

                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);
                    }else if(column1 == 2){
                        three_cloum1_item3.setVisibility(View.INVISIBLE);
                        three_cloum1_item2.setVisibility(View.VISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);

                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);
                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img2, options);
                        three_cloum1_text_item2.setText(item.name);
                    }else if(column1 == 3){
                        three_cloum1_item3.setVisibility(View.VISIBLE);
                        three_cloum1_item2.setVisibility(View.VISIBLE);
                        three_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);

                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img1, options);
                        three_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img2, options);
                        three_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,three_cloum1_img3, options);
                        three_cloum1_text_item3.setText(item.name);
                    }
                    item = currentChannel.column2.get(0);
                    ImageLoader.getInstance().displayImage(item.imgUrl
                            ,oneImage, options);
//                    oneImage.setImageResource(R.mipmap.test);

                }

                three_cloum1_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                three_cloum1_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                three_cloum1_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                three_cloum2_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                three_cloum2_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                three_cloum2_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                oneImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


            }
        }catch (Exception e){

        }
    }

    //处理第一排四个ICON的情况
    private void handlerFourCase(){
        try {
            ColumnData item ;
            int column1 = currentChannel.column1.size();
            int column2 = currentChannel.column2.size();
            int column ;
            if(column1 > column2){
                column = column1;
            }else{
                column = column2;
            }
            if(column == 4){
                if(column == 4 && column2 > 1){
                    four_cloum1.setVisibility(View.VISIBLE);
                    four_cloum2.setVisibility(View.VISIBLE);
                    five_cloum1.setVisibility(View.GONE);
                    five_cloum2.setVisibility(View.GONE);
                    three_cloum1.setVisibility(View.GONE);
                    three_cloum2.setVisibility(View.GONE);
                    oneImage.setVisibility(View.GONE);
                    oneImage_line.setVisibility(View.GONE);
                    if(column1 == 1){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.INVISIBLE);
                        four_cloum1_item2.setVisibility(View.INVISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);
                    }else if(column1 == 2){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.INVISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                    }else if(column1 == 3){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);
                    }else if(column1 == 4){
                        four_cloum1_item4.setVisibility(View.VISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img4,options);
                        four_cloum1_text_item4.setText(item.name);
                    }



                    if(column2 == 1){
                        four_cloum2_item4.setVisibility(View.INVISIBLE);
                        four_cloum2_item3.setVisibility(View.INVISIBLE);
                        four_cloum2_item2.setVisibility(View.INVISIBLE);
                        four_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img1, options);
                        four_cloum2_text_item1.setText(item.name);

                    }else if(column2 == 2){
                        four_cloum2_item4.setVisibility(View.INVISIBLE);
                        four_cloum2_item3.setVisibility(View.INVISIBLE);
                        four_cloum2_item2.setVisibility(View.VISIBLE);
                        four_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img1, options);
                        four_cloum2_text_item1.setText(item.name);
                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img2, options);
                        four_cloum2_text_item2.setText(item.name);

                    }else if(column2 == 3){
                        four_cloum2_item4.setVisibility(View.INVISIBLE);
                        four_cloum2_item3.setVisibility(View.VISIBLE);
                        four_cloum2_item2.setVisibility(View.VISIBLE);
                        four_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img1, options);
                        four_cloum2_text_item1.setText(item.name);
                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img2, options);
                        four_cloum2_text_item2.setText(item.name);
                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img3, options);
                        four_cloum2_text_item3.setText(item.name);
                    }else if(column2 == 4){
                        four_cloum2_item4.setVisibility(View.VISIBLE);
                        four_cloum2_item3.setVisibility(View.VISIBLE);
                        four_cloum2_item2.setVisibility(View.VISIBLE);
                        four_cloum2_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img1, options);
                        four_cloum2_text_item1.setText(item.name);
                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img2, options);
                        four_cloum2_text_item2.setText(item.name);
                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img3, options);
                        four_cloum2_text_item3.setText(item.name);
                        item = currentChannel.column2.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum2_img4, options);
                        four_cloum2_text_item4.setText(item.name);
                    }

                }else{
                    four_cloum1.setVisibility(View.VISIBLE);
                    four_cloum2.setVisibility(View.GONE);
                    five_cloum1.setVisibility(View.GONE);
                    five_cloum2.setVisibility(View.GONE);
                    three_cloum1.setVisibility(View.GONE);
                    three_cloum2.setVisibility(View.GONE);
                    oneImage.setVisibility(View.VISIBLE);
                    oneImage_line.setVisibility(View.VISIBLE);
                    if(column1 == 1){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.INVISIBLE);
                        four_cloum1_item2.setVisibility(View.INVISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);
                    }else if(column1 == 2){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.INVISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                    }else if(column1 == 3){
                        four_cloum1_item4.setVisibility(View.INVISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);
                    }else if(column1 == 4){
                        four_cloum1_item4.setVisibility(View.VISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img4,options);
                        four_cloum1_text_item4.setText(item.name);
                    }



                    item = currentChannel.column2.get(0);
                    ImageLoader.getInstance().displayImage(item.imgUrl
                            ,oneImage, options);
//                    oneImage.setImageResource(R.mipmap.test);

                }
                four_cloum1_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(0);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum1_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(1);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum1_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(2);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum1_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(3);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum2_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum2_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(1);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum2_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(2);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum2_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(3);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                oneImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


            }
        }catch (Exception e){

        }
    }

    //处理第一排五个ICON的情况
    private void handlerFiveCase(){
        try {
            ColumnData item ;
            int column1 = currentChannel.column1.size();
            int column2 = currentChannel.column2.size();
            int column ;
            if(column1 > column2){
                column = column1;
            }else{
                column = column2;
            }
            if(column == 5){
                if(column == 5 && column2 > 1){
                    four_cloum1.setVisibility(View.GONE);
                    four_cloum2.setVisibility(View.GONE);
                    five_cloum1.setVisibility(View.VISIBLE);
                    five_cloum2.setVisibility(View.VISIBLE);
                    three_cloum1.setVisibility(View.GONE);
                    three_cloum2.setVisibility(View.GONE);
                    oneImage.setVisibility(View.GONE);
                    oneImage_line.setVisibility(View.GONE);

                    if(column1 == 1){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.INVISIBLE);
                        five_cloum1_item3.setVisibility(View.INVISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);
                    }else if(column1 == 2){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.INVISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);
                    }else if(column1 == 3){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);
                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);
                    }else if(column1 == 4){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.VISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img4,options);
                        five_cloum1_text_item4.setText(item.name);

                    }else if(column1 == 5){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.VISIBLE);
                        five_cloum1_item5.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img4,options);
                        five_cloum1_text_item4.setText(item.name);

                        item = currentChannel.column1.get(4);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img5,options);
                        five_cloum1_text_item5.setText(item.name);
                    }


                    if(column2 == 1){
                        five_cloum2_item1.setVisibility(View.VISIBLE);
                        five_cloum2_item2.setVisibility(View.INVISIBLE);
                        five_cloum2_item3.setVisibility(View.INVISIBLE);
                        five_cloum2_item4.setVisibility(View.INVISIBLE);
                        five_cloum2_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img1,options);
                        five_cloum2_text_item1.setText(item.name);
                    }else if(column2 == 2){
                        five_cloum2_item1.setVisibility(View.VISIBLE);
                        five_cloum2_item2.setVisibility(View.VISIBLE);
                        five_cloum2_item3.setVisibility(View.INVISIBLE);
                        five_cloum2_item4.setVisibility(View.INVISIBLE);
                        five_cloum2_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img1,options);
                        five_cloum2_text_item1.setText(item.name);

                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img2,options);
                        five_cloum2_text_item2.setText(item.name);
                    }else if(column2 == 3){
                        five_cloum2_item1.setVisibility(View.VISIBLE);
                        five_cloum2_item2.setVisibility(View.VISIBLE);
                        five_cloum2_item3.setVisibility(View.VISIBLE);
                        five_cloum2_item4.setVisibility(View.INVISIBLE);
                        five_cloum2_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img1,options);
                        five_cloum2_text_item1.setText(item.name);

                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img2,options);
                        five_cloum2_text_item2.setText(item.name);

                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img3,options);
                        five_cloum2_text_item3.setText(item.name);

                    }else if(column2 == 4){
                        five_cloum2_item1.setVisibility(View.VISIBLE);
                        five_cloum2_item2.setVisibility(View.VISIBLE);
                        five_cloum2_item3.setVisibility(View.VISIBLE);
                        five_cloum2_item4.setVisibility(View.VISIBLE);
                        five_cloum2_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img1,options);
                        five_cloum2_text_item1.setText(item.name);

                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img2,options);
                        five_cloum2_text_item2.setText(item.name);

                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img3,options);
                        five_cloum2_text_item3.setText(item.name);
                        item = currentChannel.column2.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img4,options);
                        five_cloum2_text_item4.setText(item.name);
                    }else if(column2 == 5){
                        five_cloum2_item1.setVisibility(View.VISIBLE);
                        five_cloum2_item2.setVisibility(View.VISIBLE);
                        five_cloum2_item3.setVisibility(View.VISIBLE);
                        five_cloum2_item4.setVisibility(View.VISIBLE);
                        five_cloum2_item5.setVisibility(View.VISIBLE);
                        item = currentChannel.column2.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img1,options);
                        five_cloum2_text_item1.setText(item.name);

                        item = currentChannel.column2.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img2,options);
                        five_cloum2_text_item2.setText(item.name);

                        item = currentChannel.column2.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img3,options);
                        five_cloum2_text_item3.setText(item.name);
                        item = currentChannel.column2.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img4,options);
                        five_cloum2_text_item4.setText(item.name);
                        item = currentChannel.column2.get(4);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum2_img5,options);
                        five_cloum2_text_item5.setText(item.name);
                    }

                }else{
                    four_cloum1.setVisibility(View.GONE);
                    four_cloum2.setVisibility(View.GONE);
                    five_cloum1.setVisibility(View.VISIBLE);
                    five_cloum2.setVisibility(View.GONE);
                    three_cloum1.setVisibility(View.GONE);
                    three_cloum2.setVisibility(View.GONE);
                    oneImage.setVisibility(View.VISIBLE);
                    oneImage_line.setVisibility(View.VISIBLE);

                    if(column1 == 1){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.INVISIBLE);
                        five_cloum1_item3.setVisibility(View.INVISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);
                    }else if(column1 == 2){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.INVISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);
                    }else if(column1 == 3){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.INVISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);
                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);
                    }else if(column1 == 4){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.VISIBLE);
                        five_cloum1_item5.setVisibility(View.INVISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img4,options);
                        five_cloum1_text_item4.setText(item.name);

                    }else if(column1 == 5){
                        five_cloum1_item1.setVisibility(View.VISIBLE);
                        five_cloum1_item2.setVisibility(View.VISIBLE);
                        five_cloum1_item3.setVisibility(View.VISIBLE);
                        five_cloum1_item4.setVisibility(View.VISIBLE);
                        five_cloum1_item5.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img1,options);
                        five_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img2,options);
                        five_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img3,options);
                        five_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img4,options);
                        five_cloum1_text_item4.setText(item.name);

                        item = currentChannel.column1.get(4);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,five_cloum1_img5,options);
                        five_cloum1_text_item5.setText(item.name);
                    }

                    item = currentChannel.column2.get(0);
                    ImageLoader.getInstance().displayImage(item.imgUrl
                            ,oneImage, options);
//                    oneImage.setImageResource(R.mipmap.test);

                }
                five_cloum1_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(0);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                five_cloum1_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(1);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum1_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(2);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum1_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(3);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum1_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(4);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                five_cloum2_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(1);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                five_cloum2_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(2);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(3);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(4);
                        type=currentColumnData.type;
//                        channelid = currentColumnData.channelid;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                oneImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


            }
        }catch (Exception e){

        }
    }
    private void handleronClick(String type, ColumnData data,String channelid){
        Log.d("lizisong", "data.type:"+data.type+"data.typeid:"+data.typeid);
        if(data.type.equals("tagplace")){
            Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
            intent.putExtra("typeid", data.typeid);
            intent.putExtra("cityCode",data.cityCode);
            intent.putExtra("cityName","");
            intent.putExtra("prvName","");
            intent.putExtra("name", "");
            intent.putExtra("type","");
            intent.putExtra("tag",data.tag);
            intent.putExtra("channelid", channelid);
            intent.putExtra("city", city);
            startActivity(intent);
        }else if(data.type.equals("openMiniProgram")){
            openMinPro(data);
        }else if(data.type.equals("QualityProject")){
            Intent intent = new Intent(getActivity(), NewJingPinProject.class);
            startActivity(intent);
        }else if(data.type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), TopicInformation.class);
                intent.putExtra("typeid","2");
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                //getActivity().startActivity(intent);
                //m.animations();
                startActivity(intent);
            }
        }else if(data.type.equals("htmlDetail")){
            Intent intent=new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title",data.title);
            intent.putExtra("url", data.jumpUrl);
            startActivity(intent);
        }else if(data.type.equals("mdDetail")){
            if(data.typeid.equals("2")){
                Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                intent.putExtra("aid", data.aid);
                startActivity(intent);
            }else if(data.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                intent.putExtra("aid", data.aid);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("id", data.aid);
                if(data.typeid.equals("2")){
                    intent.putExtra("name", "项目");
                }else if(data.typeid.equals("4")){
                    intent.putExtra("name", "专家");
                }else if(data.typeid.equals("7")){
                    intent.putExtra("name", "设备");
                }else if(data.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }else if(data.typeid.equals("8")){
                    intent.putExtra("name", "研究院");
                }else if(data.typeid.equals("6")){
                    intent.putExtra("name", "政策");
                }else if(data.typeid.equals("5")){
                    intent.putExtra("name", "专利");
                }else if(data.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }
                intent.putExtra("pic", data.imgUrl);
                startActivity(intent);
            }


        }else if(data.type.equals("techList")){
            if(data.typeid.equals("2")){
                WriteXuQiu.entry_address = 4;
                Intent intent = new Intent(getActivity(), NewProJect.class);
                startActivity(intent);
            }else if(data.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }else if(data.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }else if(data.typeid.equals("7")){
                Intent intent = new Intent(getActivity(), SheBeiActivity.class);
                startActivity(intent);
            }else if(data.typeid.equals("5")){
                Intent intent = new Intent(getActivity(), PatentActivity.class);
                startActivity(intent);
            }else if(data.typeid.equals("8")){
                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
                startActivity(intent);
            }

        }else if(type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            startActivity(intent);
        }else if(type.equals("html")){
            Intent intent=new Intent(getActivity(), ActiveActivity.class);
            intent.putExtra("title", data.title);
            intent.putExtra("url", data.jumpUrl);
            startActivity(intent);
        }else if(type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            startActivity(intent);
        }else if(type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
            intent.putExtra("flag", data.flag);
            intent.putExtra("channelid",channelid);
            startActivity(intent);
        }else if(type.equals("place")){
            if(data.typeid.equals("1")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("prvCode",data.prvCode);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvCode", data.prvCode);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);

                startActivity(intent);
            }else if(data.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("prvCode", data.prvCode);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("11")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("prvCode", data.prvCode);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid",channelid);

//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("xr")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("prvCode", data.prvCode);
                intent.putExtra("channelid",channelid);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        }else{
            Toast.makeText(getActivity(),"功能建设中，请等待升级",Toast.LENGTH_SHORT).show();
        }
    }

    private void openMinPro(ColumnData data){
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = data.title; // 小程序原始id
        req.path = data.jumpUrl;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if(data.prvCode.equals("0")){
            req.miniprogramType=0;
        }else if(data.prvCode.equals("1")){
            req.miniprogramType=1;
        }else if(data.prvCode.equals("2")){
            req.miniprogramType=2;
        }
        MyApplication.api.sendReq(req);
    }

    private void openMinpro(SeqData item){
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = item.title; // 小程序原始id
        req.path = item.jumpUrl;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if(item.prvCode.equals("0")){
            req.miniprogramType=0;
        }else if(item.prvCode.equals("1")){
            req.miniprogramType=1;
        }else if(item.prvCode.equals("2")){
            req.miniprogramType=2;
        }

        MyApplication.api.sendReq(req);
    }
    public void ICONJson() {
        if(MainActivity.columnButton.size() ==0){
            String url = "http://"+MyApplication.ip+"/api/indexChannel_2_5.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,null,handler,200,0);
        }
    }


    class Adpater extends BaseAdapter{
        public ViewPager viewPager;

        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView = null;
            if(convertView == null){
                convertView = View.inflate(getActivity(), R.layout.commonfragmentadapter,null);
                holdView = new HoldView();
                holdView.tianxuqiu = (LinearLayout)convertView.findViewById(R.id.tianxuqiu);
                holdView.sb_demographic = (ScrollBanner)convertView.findViewById(R.id.sb_demographic);

                holdView.santuu = (LinearLayout)convertView.findViewById(R.id.santuu);
                holdView.san = (LinearLayout)convertView.findViewById(R.id.san);
                holdView.er = (LinearLayout)convertView.findViewById(R.id.er);
                holdView.yi = (LinearLayout)convertView.findViewById(R.id.yi);
                holdView.tupian1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian1);
                holdView.tupian1yin = (ImageView)convertView.findViewById(R.id.tupian1yin);
                holdView.tupian2 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian2);
                holdView.tupian3 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian3);
                holdView.er1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er1);
                holdView.er2 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er2);
                holdView.yi1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.yi1);

                holdView.jingping = (LinearLayout)convertView.findViewById(R.id.jingping);
                holdView.jingping_title = (TextView)convertView.findViewById(R.id.jingping_title);
                holdView.hangye_more = (TextView)convertView.findViewById(R.id.hangye_more);
                holdView.viewpager = (ViewPager)convertView.findViewById(R.id.viewpager);
                holdView.viewpager.setPageMargin(20);

                holdView.zhuanjia = (LinearLayout)convertView.findViewById(R.id.zhuanjia);
                holdView.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);
                holdView.rencai_lay2 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay2);
                holdView.rencai_lay3 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay3);
                holdView.rencai_lay4 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay4);
                holdView.rencai_line1 = (TextView)convertView.findViewById(R.id.rencai_line1);
                holdView.rencai_line2 = (TextView)convertView.findViewById(R.id.rencai_line2);
                holdView.rencai_line3 = (TextView)convertView.findViewById(R.id.rencai_line3);
                holdView.rencai_line4 = (TextView)convertView.findViewById(R.id.rencai_line4);
                holdView.zhuangjia_change = (TextView)convertView.findViewById(R.id.zhuangjia_change);
                holdView.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);
                holdView.rencai_img2 = (RoundImageView)convertView.findViewById(R.id.rencai_img2);
                holdView.rencai_img3 = (RoundImageView)convertView.findViewById(R.id.rencai_img3);
                holdView.rencai_img4 = (RoundImageView)convertView.findViewById(R.id.rencai_img4);
                holdView.rencai_title1 = (TextView)convertView.findViewById(R.id.rencai_title1);
                holdView.rencai_title2 = (TextView)convertView.findViewById(R.id.rencai_title2);
                holdView.rencai_title3 = (TextView)convertView.findViewById(R.id.rencai_title3);
                holdView.rencai_title4 = (TextView)convertView.findViewById(R.id.rencai_title4);
                holdView.rencai_lingyu1 = (TextView)convertView.findViewById(R.id.rencai_lingyu1);
                holdView.rencai_lingyu2 = (TextView)convertView.findViewById(R.id.rencai_lingyu2);
                holdView.rencai_lingyu3 = (TextView)convertView.findViewById(R.id.rencai_lingyu3);
                holdView.rencai_lingyu4 = (TextView)convertView.findViewById(R.id.rencai_lingyu4);
                holdView.rank1 = (TextView)convertView.findViewById(R.id.rank1);
                holdView.rank2 = (TextView)convertView.findViewById(R.id.rank2);
                holdView.rank3 = (TextView)convertView.findViewById(R.id.rank3);
                holdView.rank4 = (TextView)convertView.findViewById(R.id.rank4);
                holdView.zhuanjia_more = (TextView)convertView.findViewById(R.id.zhuanjia_more);

                holdView.baotou = (LinearLayout)convertView.findViewById(R.id.baotou);
                holdView.baotou_title = (TextView)convertView.findViewById(R.id.baotou_title);
                holdView.baotou_more = (TextView)convertView.findViewById(R.id.baotou_more);
                holdView.baotouviewpager = (ViewPager)convertView.findViewById(R.id.baotouviewpager);
                holdView.baotouviewpager.setPageMargin(20);

                holdView.hangye = (LinearLayout)convertView.findViewById(R.id.hangye);
                holdView.hangye_title = (TextView)convertView.findViewById(R.id.hangye_title);
                holdView.hangye_more = (TextView)convertView.findViewById(R.id.hangye_more);
                holdView.hangye_more1 = (TextView)convertView.findViewById(R.id.hangye_more1);
                holdView.qingbaoitem = (RelativeLayout)convertView.findViewById(R.id.qingbaoitem);
                holdView.type = (ImageView)convertView.findViewById(R.id.type);
                holdView.qingicon = (ImageView)convertView.findViewById(R.id.qingicon);
                holdView.title =(TextView)convertView.findViewById(R.id.title);
                holdView.price = (TextView)convertView.findViewById(R.id.price);
                holdView.description = (TextView)convertView.findViewById(R.id.description);
                holdView.timeupdate = (TextView)convertView.findViewById(R.id.timeupdate);
                holdView.updatadescription=(TextView)convertView.findViewById(R.id.updatadescription);

                holdView.zhuanli = (LinearLayout)convertView.findViewById(R.id.zhuanli);
                holdView.zhuanli_title = (TextView)convertView.findViewById(R.id.zhuanli_title);
                holdView.zhuanli_change = (TextView)convertView.findViewById(R.id.zhuanli_change);
                holdView.zhuanli_lay1 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay1);
                holdView.zhuanli_lay2 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay2);
                holdView.zhuanli_lay3 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay3);
                holdView.zhuanli_lay4 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay4);
                holdView.zhuanli_line1 = (TextView)convertView.findViewById(R.id.zhuanli_line1);
                holdView.zhuanli_line2 = (TextView)convertView.findViewById(R.id.zhuanli_line2);
                holdView.zhuanli_line3 = (TextView)convertView.findViewById(R.id.zhuanli_line3);
                holdView.zhuanli_line4 = (TextView)convertView.findViewById(R.id.zhuanli_line4);
                holdView.zhuanli_title1 = (TextView)convertView.findViewById(R.id.zhuanli_title1);
                holdView.zhuanli_title2 = (TextView)convertView.findViewById(R.id.zhuanli_title2);
                holdView.zhuanli_title3 = (TextView)convertView.findViewById(R.id.zhuanli_title3);
                holdView.zhuanli_title4 = (TextView)convertView.findViewById(R.id.zhuanli_title4);
                holdView.zhuanli_lingyu1 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu1);
                holdView.zhuanli_lingyu2 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu2);
                holdView.zhuanli_lingyu3 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu3);
                holdView.zhuanli_lingyu4 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu4);
                holdView.zhuanli_more = (TextView)convertView.findViewById(R.id.zhuanli_more);

                holdView.xianxiafuwu = (LinearLayout)convertView.findViewById(R.id.xianxiafuwu);
                holdView.icon_left = (ImageView) convertView.findViewById(R.id.icon_left);
                holdView.icon_right = (ImageView) convertView.findViewById(R.id.icon_right);
                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);

                holdView.fragment = (FrameLayout)convertView.findViewById(R.id.fragment);
                holdView.viewPager = (ViewPager)convertView.findViewById(R.id.viewPager);
                holdView.viewGroup = (LinearLayout) convertView.findViewById(R.id.viewGroup);

                holdView.xiangmu = (LinearLayout)convertView.findViewById(R.id.xiangmu);
                holdView.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                holdView.xiangmu_lay2 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay2);
                holdView.xiangmu_lay3 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay3);
                holdView.xiangmu_line1 = (TextView)convertView.findViewById(R.id.xiangmu_line1);
                holdView.xiangmu_line2 = (TextView)convertView.findViewById(R.id.xiangmu_line2);
                holdView.xiangmu_line3 = (TextView)convertView.findViewById(R.id.xiangmu_line3);
                holdView.xiangmu_title = (TextView)convertView.findViewById(R.id.xiangmu_title);
                holdView.xiangmu_change = (TextView)convertView.findViewById(R.id.xiangmu_change);
                holdView.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
                holdView.xianmgmu2 = (ImageView)convertView.findViewById(R.id.xianmgmu2);
                holdView.xianmgmu3 = (ImageView)convertView.findViewById(R.id.xianmgmu3);
                holdView.source1 = (TextView)convertView.findViewById(R.id.source1);
                holdView.source2 = (TextView)convertView.findViewById(R.id.source2);
                holdView.source3 = (TextView)convertView.findViewById(R.id.source3);
                holdView.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);
                holdView.xmtitle2 = (TextView)convertView.findViewById(R.id.xmtitle2);
                holdView.xmtitle3 = (TextView)convertView.findViewById(R.id.xmtitle3);
                holdView.lanyuan1 = (TextView)convertView.findViewById(R.id.lanyuan1);
                holdView.lanyuan2 = (TextView)convertView.findViewById(R.id.lanyuan2);
                holdView.lanyuan3 = (TextView)convertView.findViewById(R.id.lanyuan3);
                holdView.xm_more = (TextView)convertView.findViewById(R.id.xm_more);

                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);

                holdView.tupian1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian1.setRoundRadius(20);
//                holdView.tupian1yin.setType(ZQImageViewRoundOval.TYPE_ROUND);
//                holdView.tupian1yin.setRoundRadius(20);
                holdView.tupian2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian2.setRoundRadius(20);
                holdView.tupian3.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian3.setRoundRadius(20);
                holdView.er1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.er1.setRoundRadius(20);
                holdView.er2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.er2.setRoundRadius(20);
                holdView.yi1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.yi1.setRoundRadius(20);
                int heiht=0;
                heiht = (screenWidth-60)*461/1326;
                ViewGroup.LayoutParams lp = holdView.yi1.getLayoutParams();
                lp.height = heiht;
                holdView.yi1.setLayoutParams(lp);

                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            final KeySort item = showList.get(position);
            if(item.name.equals("zixun")){
                holdView.tianxuqiu.setVisibility(View.VISIBLE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                holdView.sb_demographic.setIcon(item.img);
                mScrollBanner =  holdView.sb_demographic;
                holdView.sb_demographic.setList(zixuanlist);
                holdView.sb_demographic.stopScroll();
                holdView.sb_demographic.startScroll();
                holdView.sb_demographic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转
                        Intent intent = new Intent(getActivity(), DFInfoShow.class);
                        intent.putExtra("typeid", "1");
                        intent.putExtra("cityCode",code);
                        intent.putExtra("cityName",currentChannel.nativeplace);
                        intent.putExtra("channelid", channelid);
                        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                        intent.putExtra("city", city);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
//                        Intent intent = new Intent();
//                        intent.setAction("action_toxuqiu");
//                        Recommend.aids = zixuanaid.get(mScrollBanner.getCurrent() + "");
//                        getActivity().sendBroadcast(intent);
                    }
                });

            }else if(item.name.equals("adScroll")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.VISIBLE);
                holdView.xiangmu.setVisibility(View.GONE);
                ViewGroup.LayoutParams params2 = holdView.fragment.getLayoutParams();
                params2.height=MyApplication.widths*2/9;
                holdView.fragment.setLayoutParams(params2);
                viewPager = holdView.viewPager;

               if(!isFristadd){
                   isFristadd = true;
                   holdView.viewPager.setAdapter(advAdapter);
                   holdView.viewGroup.removeAllViews();
                    for(int i=0; i<tips.size();i++){
                        ImageView img = tips.get(i);
                        if(widthPixels<=480){
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                            params.setMargins(5, 0, 5, 0);
                            holdView.viewGroup.addView(img, params);

                        }else{
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                            params.setMargins(5, 0, 5,5);
                            holdView.viewGroup.addView(img, params);
                        }
                    }
               }
//                for(int i=0; i<tips.size(); i++){
//                    if(i == current){
//                        tips.get(i).setImageResource(R.drawable.dot_focused);
//                    }else{
//                        tips.get(i).setImageResource( R.drawable.dot_normal);
//                    }
//                }
                holdView.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            Log.d("lizisong", "position:"+position);
//                        current = position;
                    }

                    @Override
                    public void onPageSelected(int position) {
                        int index=position%tips.size();
                        current = index;
                        for(int i=0; i<tips.size(); i++){
                            if(i == index){
                                tips.get(i).setImageResource(R.drawable.dot_focused);
                            }else{
                                tips.get(i).setImageResource( R.drawable.dot_normal);
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }else if(item.name.equals("adNormal")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                final SeqData item1, item2, item3;
                if(item.list.size() >= 3){
                    holdView.san.setVisibility(View.VISIBLE);
                    holdView.yi.setVisibility(View.GONE);
                    holdView.er.setVisibility(View.GONE);
                    item1 = item.list.get(0);
                    item2 = item.list.get(1);
                    item3 = item.list.get(2);
                    ImageLoader.getInstance().displayImage(item1.imgUrl
                            , holdView.tupian1, options);
                    ImageLoader.getInstance().displayImage(item2.imgUrl
                            , holdView.tupian2, options);
                    ImageLoader.getInstance().displayImage(item3.imgUrl
                            , holdView.tupian3, options);
                    holdView.tupian1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item1);
                    }
                    });

                    holdView.tupian2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item2);
                        }
                    });
                    holdView.tupian3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item3);
                        }
                    });

                }else if(item.list.size() == 2){
                    holdView.san.setVisibility(View.GONE);
                    holdView.yi.setVisibility(View.GONE);
                    holdView.er.setVisibility(View.VISIBLE);
                    item1 = item.list.get(0);
                    item2 = item.list.get(1);
                    ImageLoader.getInstance().displayImage(item1.imgUrl
                            , holdView.er1, options);
                    ImageLoader.getInstance().displayImage(item2.imgUrl
                            , holdView.er2, options);
                    holdView.er1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item1);

                        }
                    });
                    holdView.er2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item2);

                        }
                    });
                }else if(item.list.size() == 1){
                    holdView.san.setVisibility(View.GONE);
                    holdView.yi.setVisibility(View.VISIBLE);
                    holdView.er.setVisibility(View.GONE);
                    item1 = item.list.get(0);
                    ImageLoader.getInstance().displayImage(item1.imgUrl
                            , holdView.yi1, options);
                    holdView.yi1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlernewclick(item1);

                        }
                    });
                }

            }else if(item.name.equals("rencai")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.VISIBLE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                int count = item.list.size();
                SeqData rencai1,rencai2,rencai3,rencai4;
                if(count == 1){
                    rencai1 = item.list.get(0);
                    holdView.rencai_lay1.setVisibility(View.VISIBLE);
                    holdView.rencai_line1.setVisibility(View.VISIBLE);
                    holdView.rencai_lay2.setVisibility(View.GONE);
                    holdView.rencai_line2.setVisibility(View.GONE);
                    holdView.rencai_lay3.setVisibility(View.GONE);
                    holdView.rencai_line3.setVisibility(View.GONE);
                    holdView.rencai_lay4.setVisibility(View.GONE);
                    holdView.rencai_line4.setVisibility(View.GONE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);

                        } else {
                            holdView.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        if(rencai1.litpic == null || rencai1.litpic.equals("")){
                            holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);
                        }
                    }

                    holdView.rencai_title1.setText(rencai1.title);
                    holdView.rencai_lingyu1.setText(rencai1.unit);
                    holdView.rank1.setText(rencai1.rank);


                }else if(count == 2){
                    rencai1 = item.list.get(0);
                    rencai2 = item.list.get(1);
                    holdView.rencai_lay1.setVisibility(View.VISIBLE);
                    holdView.rencai_line1.setVisibility(View.VISIBLE);
                    holdView.rencai_lay2.setVisibility(View.VISIBLE);
                    holdView.rencai_line2.setVisibility(View.VISIBLE);
                    holdView.rencai_lay3.setVisibility(View.GONE);
                    holdView.rencai_line3.setVisibility(View.GONE);
                    holdView.rencai_lay4.setVisibility(View.GONE);
                    holdView.rencai_line4.setVisibility(View.GONE);

                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);

                        } else {
                            holdView.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai1.litpic == null || rencai1.litpic.equals("")){
                            holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);
                        }

                    }
                    holdView.rencai_title1.setText(rencai1.title);
                    holdView.rencai_lingyu1.setText(rencai1.unit);
                    holdView.rank1.setText(rencai1.rank);


                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);

                        } else {

                            holdView.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai2.litpic == null || rencai2.litpic.equals("")){
                            holdView.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);
                        }
                    }
                    holdView.rencai_title2.setText(rencai2.title);
                    holdView.rencai_lingyu2.setText(rencai2.unit);
                    holdView.rank2.setText(rencai2.rank);

                }else if(count == 3){
                    rencai1 = item.list.get(0);
                    rencai2 = item.list.get(1);
                    rencai3 = item.list.get(2);
                    holdView.rencai_lay1.setVisibility(View.VISIBLE);
                    holdView.rencai_line1.setVisibility(View.VISIBLE);
                    holdView.rencai_lay2.setVisibility(View.VISIBLE);
                    holdView.rencai_line2.setVisibility(View.VISIBLE);
                    holdView.rencai_lay3.setVisibility(View.VISIBLE);
                    holdView.rencai_line3.setVisibility(View.VISIBLE);
                    holdView.rencai_lay4.setVisibility(View.GONE);
                    holdView.rencai_line4.setVisibility(View.GONE);

                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);

                        } else {
                            holdView.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai1.litpic == null || rencai1.litpic.equals("")){
                            holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);
                        }
                    }
                    holdView.rencai_title1.setText(rencai1.title);
                    holdView.rencai_lingyu1.setText(rencai1.unit);
                    holdView.rank1.setText(rencai1.rank);


                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);

                        } else {
                            holdView.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai2.litpic == null || rencai2.litpic.equals("")){
                            holdView.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);
                        }
                    }
                    holdView.rencai_title2.setText(rencai2.title);
                    holdView.rencai_lingyu2.setText(rencai2.unit);
                    holdView.rank2.setText(rencai2.rank);

                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai3.litpic
                                    , holdView.rencai_img3, options);

                        } else {
                            holdView.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai3.litpic == null || rencai3.litpic.equals("")){
                            holdView.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai3.litpic
                                    , holdView.rencai_img3, options);
                        }
                    }
                    holdView.rencai_title3.setText(rencai3.title);
                    holdView.rencai_lingyu3.setText(rencai3.unit);
                    holdView.rank3.setText(rencai3.rank);


                }else if(count >= 4){
                    rencai1 = item.list.get(0);
                    rencai2 = item.list.get(1);
                    rencai3 = item.list.get(2);
                    rencai4 = item.list.get(3);

                    holdView.rencai_lay1.setVisibility(View.VISIBLE);
                    holdView.rencai_line1.setVisibility(View.VISIBLE);
                    holdView.rencai_lay2.setVisibility(View.VISIBLE);
                    holdView.rencai_line2.setVisibility(View.VISIBLE);
                    holdView.rencai_lay3.setVisibility(View.VISIBLE);
                    holdView.rencai_line3.setVisibility(View.VISIBLE);
                    holdView.rencai_lay4.setVisibility(View.VISIBLE);
                    holdView.rencai_line4.setVisibility(View.VISIBLE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);

                        } else {
                            holdView.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai1.litpic == null || rencai1.litpic.equals("")){
                            holdView.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdView.rencai_img1, options);
                        }
                    }
                    holdView.rencai_title1.setText(rencai1.title);
                    holdView.rencai_lingyu1.setText(rencai1.unit);
                    holdView.rank1.setText(rencai1.rank);


                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);

                        } else {
                            holdView.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai2.litpic == null || rencai2.litpic.equals("")){
                            holdView.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdView.rencai_img2, options);
                        }
                    }
                    holdView.rencai_title2.setText(rencai2.title);
                    holdView.rencai_lingyu2.setText(rencai2.unit);
                    holdView.rank2.setText(rencai2.rank);

                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai3.litpic
                                    , holdView.rencai_img3, options);

                        } else {
                            holdView.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai3.litpic == null || rencai3.litpic.equals("")){
                            holdView.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai3.litpic
                                    , holdView.rencai_img3, options);
                        }
                    }
                    holdView.rencai_title3.setText(rencai3.title);
                    holdView.rencai_lingyu3.setText(rencai3.unit);
                    holdView.rank3.setText(rencai3.rank);

                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && rencai4.litpic != null && !rencai4.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(rencai4.litpic
                                    , holdView.rencai_img4, options);

                        } else {
                            holdView.rencai_img4.setBackgroundResource(R.mipmap.touxiangzhanwei);
                        }
                    } else {
                        if(rencai4.litpic == null || rencai4.litpic.equals("")){
                            holdView.rencai_img4.setImageResource(R.mipmap.touxiangzhanwei);
                        }else{
                            ImageLoader.getInstance().displayImage(rencai4.litpic
                                    , holdView.rencai_img4, options);}
                    }
                    holdView.rencai_title4.setText(rencai4.title);
                    holdView.rencai_lingyu4.setText(rencai4.unit);
                    holdView.rank4.setText(rencai4.rank);

                }
                holdView.rencai_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                        intent.putExtra("aid", item.list.get(0).id);
                        startActivity(intent);
                    }
                });
                holdView.rencai_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                        intent.putExtra("aid", item.list.get(1).id);
                        startActivity(intent);
                    }
                });
                holdView.rencai_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                        intent.putExtra("aid", item.list.get(2).id);
                        startActivity(intent);
                    }
                });
                holdView.rencai_lay4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                        intent.putExtra("aid", item.list.get(3).id);
                        startActivity(intent);
                    }
                });

                holdView.zhuangjia_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.setVisibility(View.VISIBLE);
                        sortid = item.list.get(item.list.size()-1).id;
                        ChangeZhuanJia();
                    }
                });
                holdView.zhuanjia_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), LookMoreActivity.class);
//                        intent.putExtra("typeid",4);
//                        intent.putExtra("title","更多专家");
//                        intent.putExtra("channel",currentChannel.channelid);
//                        intent.putExtra("tag","bt4");
//                        startActivity(intent);
                        Intent intent = new Intent(getActivity(), PersonActivity.class);
                        startActivity(intent);
                    }
                });
            }else if(item.name.equals("qingbao")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.VISIBLE);
                holdView.qingbaoitem.setVisibility(View.VISIBLE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                final SeqData qingbao = item.list.get(0);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI && qingbao.telig_cover != null && !qingbao.telig_cover.equals("")) {
                        ImageLoader.getInstance().displayImage(qingbao.telig_cover
                                , holdView.qingicon, options);
                    } else {
                        holdView.qingicon.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(qingbao.telig_cover
                            , holdView.qingicon, options);
                }
                holdView.title.setText(qingbao.telig_title);
                holdView.description.setText(qingbao.telig_description);

                if(qingbao.telig_type.equals("0")){
                    holdView.type.setVisibility(View.GONE);
                }else if(qingbao.telig_type.equals("2")){
                    holdView.type.setVisibility(View.VISIBLE);
                    holdView.type.setBackgroundResource(R.mipmap.xianmian);
                }else if(qingbao.telig_type.equals("1")){
                    holdView.type.setVisibility(View.VISIBLE);
                    holdView.type.setBackgroundResource(R.mipmap.youhui);
                }
                holdView.updatadescription.setText(qingbao.telig_journal_title);//暂定
                holdView.price.setText(qingbao.telig_price+"元/"+qingbao.telig_unit);
                if(Float.parseFloat(qingbao.telig_price) == 0){
                    holdView.price.setText("免费");
                }
                if(qingbao.telig_journal_pubdate != null && !qingbao.telig_journal_pubdate.equals("")){
                    holdView.timeupdate.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(qingbao.telig_journal_pubdate)*1000), qingbao.telig_journal_pubdate)+"更新");
                }

                holdView.hangye_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), QingBaoActivity.class);
                        startActivity(intent);

                    }
                });
                holdView.hangye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), QingBaoDeilActivity.class);
                        intent.putExtra("teligId",qingbao.telig_id);
                        intent.putExtra("telig_price",qingbao.telig_price);
                        intent.putExtra("telig_unit",qingbao.telig_unit);
                        intent.putExtra("telig_type",qingbao.telig_type);
                        startActivity(intent);
                    }
                });

            }else if(item.name.equals("zhuanli")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.VISIBLE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                SeqData zhuanli1,zhuanli2,zhuanli3,zhuanli4;
                int count = item.list.size();
                if(count == 0){
                    zhuanli1 = item.list.get(0);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.GONE);
                    holdView.zhuanli_line2.setVisibility(View.GONE);
                    holdView.zhuanli_lay3.setVisibility(View.GONE);
                    holdView.zhuanli_line3.setVisibility(View.GONE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                }else if(count == 2){
                    zhuanli1 = item.list.get(0);
                    zhuanli2 = item.list.get(1);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.GONE);
                    holdView.zhuanli_line3.setVisibility(View.GONE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                }else if(count == 3){
                    zhuanli1 = item.list.get(0);
                    zhuanli2 = item.list.get(1);
                    zhuanli3 = item.list.get(2);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                    holdView.zhuanli_title3.setText(zhuanli3.title);
                    holdView.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);
                }else if(count >= 4){
                    zhuanli1 = item.list.get(0);
                    zhuanli2 = item.list.get(1);
                    zhuanli3 = item.list.get(2);
                    zhuanli4 = item.list.get(3);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay4.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line4.setVisibility(View.VISIBLE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                    holdView.zhuanli_title3.setText(zhuanli3.title);
                    holdView.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);
                    holdView.zhuanli_title4.setText(zhuanli4.title);
                    holdView.zhuanli_lingyu4.setText("所属领域:"+zhuanli4.area_cate);
                }

                holdView.zhuanli_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                        intent.putExtra("aid", item.list.get(0).id);
                        startActivity(intent);

                    }
                });
                holdView.zhuanli_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                        intent.putExtra("aid", item.list.get(1).id);
                        startActivity(intent);

                    }
                });
                holdView.zhuanli_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                        intent.putExtra("aid", item.list.get(2).id);
                        startActivity(intent);

                    }
                });
                holdView.zhuanli_lay4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                        intent.putExtra("aid", item.list.get(3).id);
                        startActivity(intent);

                    }
                });
                holdView.zhuanli_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.setVisibility(View.VISIBLE);
                        sortid = item.list.get(item.list.size()-1).id;
                        ChangeZhuanli();
                    }
                });
                holdView.zhuanli_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                        intent.putExtra("typeid",5);
                        intent.putExtra("title","更多专利");
                        intent.putExtra("channel","专利");
                        startActivity(intent);
                    }
                });
            }else if(item.name.equals("offline")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.VISIBLE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                final SeqData offline1,offline2;
                offline1 = item.list.get(0);
                offline2 = item.list.get(1);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI && offline1.litpic != null && !offline1.litpic.equals("")) {
                        ImageLoader.getInstance().displayImage(offline1.litpic
                                , holdView.icon_left, options);
                        ImageLoader.getInstance().displayImage(offline2.litpic
                                , holdView.icon_right, options);

                    } else {
                        holdView.icon_left.setBackgroundResource(R.mipmap.information_placeholder);
                        holdView.icon_right.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(offline1.litpic
                            , holdView.icon_left, options);
                    ImageLoader.getInstance().displayImage(offline2.litpic
                            , holdView.icon_right, options);
                }
                holdView.icon_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActiveActivity.class);
                        intent.putExtra("url", offline1.url);
                        startActivity(intent);
                    }
                });

                holdView.icon_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActiveActivity.class);
                        intent.putExtra("url", offline2.url);
                        startActivity(intent);
                    }
                });
            }else if(item.name.equals("device")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.VISIBLE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                holdView.baotou_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
                        intent.putExtra("typeid", "7");
                        intent.putExtra("cityCode","");
                        intent.putExtra("cityName","");
                        intent.putExtra("prvName","");
                        intent.putExtra("name", "");
                        intent.putExtra("type","");
                        intent.putExtra("channelid", currentChannel.channelid);
                        intent.putExtra("city", city);
                        startActivity(intent);
                    }
                });
                holdView.baotouviewpager.setAdapter(myDeviceAdapter);
            }else if(item.name.equals("xiangmu")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.VISIBLE);
                int count = item.list.size();
                SeqData xiangmu1 = null,xiangmu2=null,xiangmu3=null;
                if(count == 1){
                    holdView.xiangmu_lay1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_lay2.setVisibility(View.GONE);
                    holdView.xiangmu_line2.setVisibility(View.GONE);
                    holdView.xiangmu_lay3.setVisibility(View.GONE);
                    holdView.xiangmu_line3.setVisibility(View.GONE);
                    xiangmu1 = item.list.get(0);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdView.xianmgmu1, options);

                        } else {
                            holdView.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                , holdView.xianmgmu1, options);
                    }
                    holdView.xmtitle1.setText(xiangmu1.title);
                    holdView.lanyuan1.setText(xiangmu1.area_cate);


                    if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                        holdView.source1.setVisibility(View.VISIBLE);
                        holdView.source1.setText(xiangmu1.labels);
                        if(xiangmu1.labels.equals("精品项目")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu1.labels.equals("钛领推荐")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu1.labels.equals("国家科学基金")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source1.setVisibility(View.GONE);
                    }
                }else if(count == 2){
                    xiangmu1 = item.list.get(0);
                    xiangmu2 = item.list.get(1);
                    holdView.xiangmu_lay1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_lay2.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line2.setVisibility(View.VISIBLE);
                    holdView.xiangmu_lay3.setVisibility(View.GONE);
                    holdView.xiangmu_line3.setVisibility(View.GONE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdView.xianmgmu1, options);

                        } else {
                            holdView.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                , holdView.xianmgmu1, options);
                    }
                    holdView.xmtitle1.setText(xiangmu1.title);
                    holdView.lanyuan1.setText(xiangmu1.area_cate);
                    if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                        holdView.source1.setVisibility(View.VISIBLE);
                        holdView.source1.setText(xiangmu1.labels);
                        if(xiangmu1.labels.equals("精品项目")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu1.labels.equals("钛领推荐")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu1.labels.equals("国家科学基金")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source1.setVisibility(View.GONE);
                    }

                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                    , holdView.xianmgmu2, options);

                        } else {
                            holdView.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                , holdView.xianmgmu2, options);
                    }
                    holdView.xmtitle2.setText(xiangmu2.title);
                    holdView.lanyuan2.setText(xiangmu2.area_cate);
                    if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                        holdView.source2.setVisibility(View.VISIBLE);
                        holdView.source2.setText(xiangmu2.labels);
                        if(xiangmu2.labels.equals("精品项目")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu2.labels.equals("钛领推荐")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu2.labels.equals("国家科学基金")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source2.setVisibility(View.GONE);
                    }
                }else if(count >= 3){
                    xiangmu1 = item.list.get(0);
                    xiangmu2 = item.list.get(1);
                    xiangmu3 = item.list.get(2);
                    holdView.xiangmu_lay1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line1.setVisibility(View.VISIBLE);
                    holdView.xiangmu_lay2.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line2.setVisibility(View.VISIBLE);
                    holdView.xiangmu_lay3.setVisibility(View.VISIBLE);
                    holdView.xiangmu_line3.setVisibility(View.VISIBLE);

                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdView.xianmgmu1, options);

                        } else {
                            holdView.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                , holdView.xianmgmu1, options);
                    }
                    holdView.xmtitle1.setText(xiangmu1.title);
                    holdView.lanyuan1.setText(xiangmu1.area_cate);
                    Log.d("lizisong", "xiangmu1.labels:"+xiangmu1.labels);
                    if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                        holdView.source1.setVisibility(View.VISIBLE);
                        holdView.source1.setText(xiangmu1.labels);
                        if(xiangmu1.labels.equals("精品项目")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu1.labels.equals("钛领推荐")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu1.labels.equals("国家科学基金")){
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source1.setVisibility(View.GONE);
                    }
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                    , holdView.xianmgmu2, options);

                        } else {
                            holdView.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                , holdView.xianmgmu2, options);
                    }
                    holdView.xmtitle2.setText(xiangmu2.title);
                    holdView.lanyuan2.setText(xiangmu2.area_cate);
                    Log.d("lizisong", "xiangmu2.labels:"+xiangmu2.labels);

                    if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                        holdView.source2.setVisibility(View.VISIBLE);
                        holdView.source2.setText(xiangmu2.labels);
                        if(xiangmu2.labels.equals("精品项目")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu2.labels.equals("钛领推荐")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu2.labels.equals("国家科学基金")){
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source2.setVisibility(View.GONE);
                    }

                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && xiangmu3.litpic != null && !xiangmu3.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                    , holdView.xianmgmu3, options);

                        } else {
                            holdView.xianmgmu3.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                , holdView.xianmgmu3, options);
                    }
                    holdView.xmtitle3.setText(xiangmu3.title);
                    holdView.lanyuan3.setText(xiangmu3.area_cate);
                    if(xiangmu3.labels != null && (!xiangmu3.labels.equals(""))){
                        holdView.source3.setVisibility(View.VISIBLE);
                        holdView.source3.setText(xiangmu3.labels);
                        if(xiangmu3.labels.equals("精品项目")){
                            holdView.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }else if(xiangmu3.labels.equals("钛领推荐")){
                            holdView.source3.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                        }else if(xiangmu3.labels.equals("国家科学基金")){
                            holdView.source3.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                        }else{
                            holdView.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                        }
                    }else{
                        holdView.source3.setVisibility(View.GONE);
                    }
                }

                holdView.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            String labels = item.list.get(0).labels;
                            if(labels != null){
                                if(labels.contains("精品项目") || (item.list.get(0).typeid.equals("2") && item.list.get(0).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(0).title);
                                    intent.putExtra("url", item.list.get(0).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(0).id);
                                    startActivity(intent);
                                }
                            }else {
                                if((item.list.get(0).typeid.equals("2") && item.list.get(0).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(0).title);
                                    intent.putExtra("url", item.list.get(0).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(0).id);
                                    startActivity(intent);
                                }
                            }
                        } catch (IndexOutOfBoundsException ex) {

                        } catch (Exception e) {

                        }

                    }
                });
                holdView.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            String labels = item.list.get(1).labels;
                            if(labels != null){
                                if(labels.contains("精品项目")|| (item.list.get(1).typeid.equals("2") && item.list.get(1).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(1).title);
                                    intent.putExtra("url", item.list.get(1).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(1).id);
                                    startActivity(intent);
                                }
                            }else {
                                if((item.list.get(1).typeid.equals("2") && item.list.get(1).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(1).title);
                                    intent.putExtra("url", item.list.get(1).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(1).id);
                                    startActivity(intent);
                                }
                            }

                        } catch (IndexOutOfBoundsException ex) {

                        } catch (Exception e) {

                        }

                    }
                });
                holdView.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String labels = item.list.get(2).labels;
                            if(labels != null){
                                if(labels.contains("精品项目") || (item.list.get(2).typeid.equals("2") && item.list.get(2).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(2).title);
                                    intent.putExtra("url", item.list.get(2).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(2).id);
                                    startActivity(intent);
                                }
                            }else {
                                if((item.list.get(2).typeid.equals("2") && item.list.get(2).typename.equals("html"))){
                                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                    intent.putExtra("title", item.list.get(2).title);
                                    intent.putExtra("url", item.list.get(2).url);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                    intent.putExtra("aid", item.list.get(2).id);
                                    startActivity(intent);
                                }
                            }
                        } catch (IndexOutOfBoundsException ex) {

                        } catch (Exception e) {

                        }



                    }
                });
                holdView.xiangmu_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.setVisibility(View.VISIBLE);
                        sortid = item.list.get(item.list.size() - 1).id;
                        ChangeXumu();
                    }
                });
                holdView.xm_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewProJect.class);
                        startActivity(intent);
//                                Intent intent = new Intent(getActivity(), LookMoreActivity.class);
//                                intent.putExtra("typeid", 2);
//                                intent.putExtra("title", "更多项目");
//                                intent.putExtra("channel", "项目");
//                                startActivity(intent);
                    }
                });

            }else if(item.name.equals("jpxiangmu")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.VISIBLE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                holdView.hangye_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewProJect.class);
                        startActivity(intent);
                    }
                });
                holdView.viewpager.setAdapter(xiangMuAdapter);
            }else if(item.name.equals("dixian")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.hangye.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.xianxiafuwu.setVisibility(View.GONE);
                holdView.fragment.setVisibility(View.GONE);
                holdView.xiangmu.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }
    class HoldView{
        //资讯
        public LinearLayout tianxuqiu;
        public ScrollBanner sb_demographic;

        //三张图
        public LinearLayout santuu;
        public LinearLayout san,er,yi;
        public ZQImageViewRoundOval tupian1,tupian2,tupian3,er1,er2,yi1;
        public ImageView tupian1yin;

        //精品项目
        public LinearLayout jingping;
        public TextView jingping_title,hangye_more;
        public ViewPager viewpager;

        //专家推荐
        public LinearLayout zhuanjia;
        public RelativeLayout rencai_lay1,rencai_lay2,rencai_lay3,rencai_lay4;
        public TextView rencai_line1,rencai_line2,rencai_line3,rencai_line4;
        public TextView zhuangjia_change;
        public RoundImageView rencai_img1,rencai_img2,rencai_img3,rencai_img4;
        public TextView rencai_title1,rencai_title2,rencai_title3,rencai_title4;
        public TextView rencai_lingyu1,rencai_lingyu2,rencai_lingyu3,rencai_lingyu4;
        public TextView rank1,rank2,rank3,rank4;
        public TextView zhuanjia_more;

        //行业解析
        public LinearLayout hangye;
        public RelativeLayout qingbaoitem;
        public TextView hangye_title;
        public TextView hangye_more1;
        public ImageView qingicon,type;
        public TextView title;
        public TextView description,timeupdate,price,updatadescription;

        //专利推荐
        public LinearLayout zhuanli;
        public RelativeLayout zhuanli_lay1,zhuanli_lay2,zhuanli_lay3,zhuanli_lay4;
        public TextView zhuanli_line1,zhuanli_line2,zhuanli_line3,zhuanli_line4;
        public TextView zhuanli_title,zhuanli_change;
        public TextView zhuanli_title1,zhuanli_title2,zhuanli_title3,zhuanli_title4;
        public TextView zhuanli_lingyu1,zhuanli_lingyu2,zhuanli_lingyu3,zhuanli_lingyu4,zhuanli_more;

        //线下服务
        public LinearLayout xianxiafuwu;
        public ImageView icon_left,icon_right;

        //设备共享
        public LinearLayout baotou;
        public TextView baotou_title;
        public TextView baotou_more;
        public ViewPager baotouviewpager;

        //滚动广告
        public FrameLayout fragment;
        public ViewPager viewPager;
        public LinearLayout viewGroup;

        //项目推荐
        public LinearLayout xiangmu;
        public RelativeLayout xiangmu_lay1,xiangmu_lay2,xiangmu_lay3;
        public TextView xiangmu_line1,xiangmu_line2,xiangmu_line3;
        public TextView xiangmu_title;
        public TextView xiangmu_change;
        public ImageView xianmgmu1,xianmgmu2,xianmgmu3;
        public TextView xmtitle1,xmtitle2,xmtitle3;
        public TextView lanyuan1,lanyuan2,lanyuan3;
        public TextView xm_more;
        public TextView source1,source2,source3;
        //底线
        public RelativeLayout dixian;

    }

    class MyAdapter extends PagerAdapter {
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();
        @Override
        public int getCount() {
            if(advListData.size() == 1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        /**
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View converView = null;
            try {
                int index =0;
                if(advListData.size() == 0){
                    index = 0;
                }else{
                    index = position % advListData.size();
                }

                final SeqData ads = advListData.get(index);

                ViewHolder mHolder = null;
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.showimage, null);
                    mHolder = new ViewHolder();
                    mHolder.imageView = (ImageView) converView.findViewById(R.id.item);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (ViewHolder)converView.getTag();
                }
//                Log.d("lizisong","ads.img:"+ads.img);
                ImageLoader.getInstance().displayImage(ads.img
                        ,mHolder.imageView , options);



                mHolder.imageView.setClickable(true);
                mHolder.imageView.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                    @Override
                    public void oneClick() {
                        if(ads != null && ads.type != null && ads.type.equals("html")
                                && ads.url != null && !ads.url.equals("")){
                            //增加mid参数 todoby wyy
                            tongji(ads.id);
                            Log.d("lizisong", "ads.id:"+ads.id);
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            if (!mid.equals("0")){
                                Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                intent.putExtra("title",ads.title);
                                intent.putExtra("url", ads.url+"?mid="+mid);
                                startActivity(intent);

                            } else {
                                Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                intent.putExtra("url", ads.url);
                                startActivity(intent);
                            }

                        }else{
                            Log.d("lizisong", "ads.id:"+ads.id);
                            tongji(ads.id);
                            Intent intent=new Intent(getActivity(),ZixunDetailsActivity.class);
                            intent.putExtra("id",ads.aid);
                            intent.putExtra("name",ads.typename);
                            intent.putExtra("pic",ads.litpic);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void doubleClick() {

                    }
                }));
                container.addView(converView);
            }catch (IllegalStateException exx){}
            catch (NumberFormatException ex){}
            catch ( Exception e){}
            return  converView;

        }

        private class ViewHolder{
            public ImageView imageView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }

    }

    private void tongji(final String id){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        HashMap<String, String> map = new HashMap<>();
        String url="http://"+MyApplication.ip+"/api/banner_behavior.php";
        map.put("id", id);
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,-1,0);

    }

    private void handlernewclick(SeqData item){
        tongji(item.id);
        Log.d("lizisong", "item.type:"+item.type+"data.typeid:"+item.typeid);
        if(item.type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), ZhuanLiShenQing.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                //getActivity().startActivity(intent);
                //m.animations();
                startActivity(intent);
            }

        }if(item.type.equals("openMiniProgram")){
            openMinpro(item);
        }else if(item.type.equals("htmlDetail")){
            Intent intent=new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title",item.title);
            intent.putExtra("url", item.jumpUrl);
            startActivity(intent);
        }else if(item.type.equals("mdDetail")){
            if(item.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                intent.putExtra("aid", item.aid);
                startActivity(intent);

            }else if(item.typeid.equals("2")){
                Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                intent.putExtra("aid", item.aid);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("id", item.aid);
                if(item.typeid.equals("2")){
                    intent.putExtra("name", "项目");
                }else if(item.typeid.equals("4")){
                    intent.putExtra("name", "专家");
                }else if(item.typeid.equals("7")){
                    intent.putExtra("name", "设备");
                }else if(item.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }else if(item.typeid.equals("8")){
                    intent.putExtra("name", "研究院");
                }else if(item.typeid.equals("6")){
                    intent.putExtra("name", "政策");
                }else if(item.typeid.equals("5")){
                    intent.putExtra("name", "专利");
                }else if(item.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }
                intent.putExtra("pic", item.litpic);
                startActivity(intent);
            }


        }else if(item.type.equals("techList")){
            if(item.typeid.equals("2")){
                WriteXuQiu.entry_address = 4;
                Intent intent = new Intent(getActivity(), NewProJect.class);
                startActivity(intent);
            }else if(item.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("7")){
                Intent intent = new Intent(getActivity(), SheBeiActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("5")){
                Intent intent = new Intent(getActivity(), PatentActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("8")){
                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
                startActivity(intent);
            }

        }else if(item.type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            startActivity(intent);
        }else if(item.type.equals("html")){
            Intent intent=new Intent(getActivity(), ActiveActivity.class);
            intent.putExtra("title", item.title);
            intent.putExtra("url", item.jumpUrl);
            startActivity(intent);
        }else if(item.type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            startActivity(intent);
        }else if(item.type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
            intent.putExtra("channelid","9");
            startActivity(intent);
        }else if(item.type.equals("place")){
            if(item.typeid.equals("1")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid", channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",  channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("11")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("xr")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                String city =item.nativeplace;
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",channelid);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        }else{

        }
    }

    private void ChangeZhuanJia(){
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "4");
        map.put("typeid", "4");
        map.put("tag", "1");
        map.put("sortid", sortid);
        map.put("channelid", channelid);
        networkCom.getJson(url,map,handler,4,0);
    }

    private void ChangeZhuanli(){
        String url = "http://"+ MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize","4");
        map.put("typeid","5");
        map.put("channelid", channelid);
        map.put("tag","1");
        map.put("sortid",sortid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,5,0);
    }


    class MyDeviceAdapter  extends PagerAdapter{
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();
        @Override
        public int getCount() {
            return deviceShows.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                HoldView mHolder = null;
                final DeviceShow item  =deviceShows.get(position);
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.baotoudevice, null);
                    mHolder = new HoldView();
                    mHolder.xiangmu_lay1 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay1);
                    mHolder.xiangmu_lay2 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay2);
                    mHolder.xiangmu_lay3 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay3);
                    mHolder.xianmgmu1 = (ImageView)converView.findViewById(R.id.xianmgmu1);
                    mHolder.xianmgmu2 = (ImageView)converView.findViewById(R.id.xianmgmu2);
                    mHolder.xianmgmu3 = (ImageView)converView.findViewById(R.id.xianmgmu3);
                    mHolder.xmtitle1  =(TextView)converView.findViewById(R.id.xmtitle1);
                    mHolder.xmtitle2  =(TextView)converView.findViewById(R.id.xmtitle2);
                    mHolder.xmtitle3  =(TextView)converView.findViewById(R.id.xmtitle3);
                    mHolder.lanyuan1 = (TextView)converView.findViewById(R.id.lanyuan1);
                    mHolder.lanyuan2 = (TextView)converView.findViewById(R.id.lanyuan2);
                    mHolder.lanyuan3 = (TextView)converView.findViewById(R.id.lanyuan3);
                    mHolder.yuyue1 = (ImageView)converView.findViewById(R.id.yuyue1);
                    mHolder.yuyue2 = (ImageView)converView.findViewById(R.id.yuyue2);
                    mHolder.yuyue3 = (ImageView)converView.findViewById(R.id.yuyue3);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (HoldView)converView.getTag();
                }
                mHolder.xiangmu_lay1.setVisibility(View.GONE);
                mHolder.xiangmu_lay2.setVisibility(View.GONE);
                mHolder.xiangmu_lay3.setVisibility(View.GONE);
                if(item.id1 != null){
                    mHolder.xiangmu_lay1.setVisibility(View.VISIBLE);
//                    if(!isAnuationStaring){
                    ImageLoader.getInstance().displayImage(item.litpic1
                            ,mHolder.xianmgmu1 , options);
//                    }
                    mHolder.xmtitle1.setText(item.title1);
                    if(item.area_cate1 == null){
                        mHolder.lanyuan1.setVisibility(View.INVISIBLE);
                    }else{
                        mHolder.lanyuan1.setVisibility(View.VISIBLE);
                    }
                    mHolder.lanyuan1.setText("所属领域："+item.area_cate1);

                }
                if(item.id2 != null){
                    mHolder.xiangmu_lay2.setVisibility(View.VISIBLE);
//                    if(!isAnuationStaring){
                    ImageLoader.getInstance().displayImage(item.litpic2
                            ,mHolder.xianmgmu2 , options);
//                    }
                    mHolder.xmtitle2.setText(item.title2);
                    if(item.area_cate2 == null){
                        mHolder.lanyuan2.setVisibility(View.INVISIBLE);
                    }else{
                        mHolder.lanyuan2.setVisibility(View.VISIBLE);
                    }
                    mHolder.lanyuan2.setText("所属领域："+item.area_cate2);
                }

                if(item.id3 != null){
                    mHolder.xiangmu_lay3.setVisibility(View.VISIBLE);
//                    if(!isAnuationStaring){
                    ImageLoader.getInstance().displayImage(item.litpic3
                            ,mHolder.xianmgmu3 , options);
//                    }
                    mHolder.xmtitle3.setText(item.title3);
                    if(item.area_cate3 == null){
                        mHolder.lanyuan3.setVisibility(View.INVISIBLE);
                    }else{
                        mHolder.lanyuan3.setVisibility(View.VISIBLE);
                    }
                    mHolder.lanyuan3.setText("所属领域："+item.area_cate3);
                }
                mHolder.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id1);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid1);
                        intent.putExtra("pic", item.litpic1);
                        startActivity(intent);
                    }
                });
                mHolder.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id2);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid2);
                        intent.putExtra("pic", item.litpic2);
                        startActivity(intent);
                    }
                });
                mHolder.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id3);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid3);
                        intent.putExtra("pic", item.litpic3);
                        startActivity(intent);
                    }
                });


                mHolder.yuyue1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.id1);
                            intent.putExtra("typeid", item.typeid1);
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mHolder.yuyue2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.id2);
                            intent.putExtra("typeid", item.typeid2);
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mHolder.yuyue3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.id3);
                            intent.putExtra("typeid", item.typeid3);
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });


                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        //baotoudevice
        class HoldView{
            public RelativeLayout xiangmu_lay1,xiangmu_lay2,xiangmu_lay3;
            public ImageView xianmgmu1,xianmgmu2,xianmgmu3;
            public TextView xmtitle1,lanyuan1,xmtitle2,lanyuan2,xmtitle3,lanyuan3;
            public ImageView yuyue1,yuyue2,yuyue3;

        }
    }


    class MyXiangMuAdapter extends PagerAdapter {
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();

        @Override
        public int getCount() {
            return advJingPinData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                HoldView mHolder = null;
                final SeqData ads = advJingPinData.get(position);
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.baotouviewpageradapter, null);
                    mHolder = new HoldView();
                    mHolder.iv = (ZQImageViewRoundOval)converView.findViewById(R.id.iv);
                    mHolder.iv.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    mHolder.iv.setRoundRadius(20);
                    mHolder.iv.setRoundJiao(false,false,true,true);
                    mHolder.rencai_lay1 = (RelativeLayout)converView.findViewById(R.id.rencai_lay1);
                    mHolder.rencai_im = (RoundImageView)converView.findViewById(R.id.rencai_im);
                    mHolder.rencai_title1 = (TextView) converView.findViewById(R.id.rencai_title1);
                    mHolder.rank = (TextView) converView.findViewById(R.id.rank);
                    mHolder.rencai_lingyu1 = (TextView) converView.findViewById(R.id.rencai_lingyu1);
                    mHolder.rank1 = (ImageView) converView.findViewById(R.id.rank1);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (HoldView)converView.getTag();
                }

                ImageLoader.getInstance().displayImage(ads.imgUrl
                        ,mHolder.iv , options);
                ImageLoader.getInstance().displayImage(ads.litpic
                        ,mHolder.rencai_im , options);
                mHolder.rencai_title1.setText(ads.arc_title);
                mHolder.rank.setText(ads.ranks);
                mHolder.rencai_lingyu1.setText(ads.area_cate);
                mHolder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlernewclick(ads);
                    }
                });
                mHolder.rank1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", ads.aid);
                            intent.putExtra("typeid", ads.typeid);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mHolder.rencai_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                        intent.putExtra("id", ads.aid);
//                        intent.putExtra("name", "专家");
//                        intent.putExtra("typeid", ads.typeid);
//                        intent.putExtra("pic", ads.litpic);
//                        startActivity(intent);
                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                        intent.putExtra("aid", ads.aid);
                        startActivity(intent);
                    }
                });
//                if(!isAnuationStaring){


//                }

                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        class HoldView {
            public ZQImageViewRoundOval iv;
            public RelativeLayout rencai_lay1;
            public RoundImageView rencai_im;
            public TextView rencai_title1,rank,rencai_lingyu1;
            public ImageView rank1;
        }
    }

    private void ChangeXumu(){
        String url ="http://"+ MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "3");
        map.put("typeid", "2");
        map.put("tag", "1");
        map.put("sortid", sortid);
        map.put("channelid", channelid);
        networkCom.getJson(url,map,handler,3,0);
    }

    public boolean isVisible= false;
    public void setVisible(boolean state){
        isVisible=state;
    }

}
