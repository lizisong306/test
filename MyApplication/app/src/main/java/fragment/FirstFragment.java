package fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.chenantao.autolayout.AutoRelativeLayout;
import com.google.gson.Gson;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AppointmentSpecialist;
import com.maidiantech.ClickListInter;
import com.maidiantech.DFBaoTouMore;
import com.maidiantech.DFInfoShow;
import com.maidiantech.Demandrelease;
import com.maidiantech.DetailsActivity;
import com.maidiantech.KongBaiActivity;
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
import com.maidiantech.NoDoubleClickListener;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.QingBaoDeilActivity;
import com.maidiantech.R;
import com.maidiantech.SearchCommentPage;
import com.maidiantech.ShowFristGuideActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.XiangMuDuiJieActivity;
import com.maidiantech.ZhuanLiShenQing;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import db.ChannelDb;
import entity.ADS;
import entity.AdsEntity;
import entity.ColumnData;
import entity.HomeEntity;
import entity.PlaceChannel;
import entity.PostData;
import entity.PostsData;
import entity.Ret;
import view.BTAlertDialog;
import view.HeaderListView;
import view.MyClickListener;
import view.OnDoubleClickListener;
import view.RoundImageView;
import view.ScrollBanner;
import view.SystemBarTintManager;
import view.ViewClick;

import static com.maidiantech.R.id.listview;
/**
 * Created by lizisong on 2017/9/19.
 */

public class FirstFragment extends BaseFragment  {
    LinearLayout title;
    ImageView zhuanjia,xiangmu,zhuanli,sousu;
    private View view;
    int widthPixels;
    int heightPixels;
    private boolean mHasLoadedOnce;
    private int screenWidth;
    private boolean flag = false;
    ReceiverBroad receiverBroad;
    private HeaderListView listView;
    private ProgressBar progress;
    private View HeaderView;
    AutoRelativeLayout bg;
    RelativeLayout bg_heart;
    public static int listheight =50;
    String code;
    int current =0;
    List<PostsData> listData = new ArrayList<>();
    public  List<ADS> adsListData=new ArrayList<>();
    public List<ADS> advListData = new ArrayList<>();
    public List<String> zixuanlist = new ArrayList<>();
    public HashMap<String, String> zixuanaid = new HashMap<>();
    TextView spinner;
    ImageView sousuo;
    FirstFragmentAdapter adapter;
    String ret;
    HomeEntity homedata;
    ImageView nodata;
    private DisplayImageOptions options;
    TextView nice_spinner,nice_spinner1;
    public LinearLayout tab_btn;
    public TextView tab_zhaofuwu;
    public TextView tab_tianxuqiu;

    public LinearLayout zhaofuwu_bg;
    public LinearLayout tianxuqiu_bg;

    public EditText text;
    public TextView counttxt;
    public TextView commit,sosocontent;
    private String city="";
    public String json;
    public String sortid;
    PlaceChannel currentChannel;
    public String adsjson;
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
    ImageView oneImage,banner_top;
    boolean isherat = true;


    String type,channelid;
    ColumnData currentColumnData;
    AdsEntity adsData;
    boolean isFirstClick= true,NewPageisOpen =false;
    float lastY;
    MyAdapter advAdapter ;
    SystemBarTintManager tintManager ;
    int  height = 0;
    ScrollBanner scrollBanner;
    private ArrayList<ImageView> tips;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.firstfragment, null);
            title = (LinearLayout)view.findViewById(R.id.title);
            nice_spinner1 = (TextView)view.findViewById(R.id.nice_spinner1);
            zhuanjia = (ImageView) view.findViewById(R.id.zhuanjia);
            nodata = (ImageView)view.findViewById(R.id.nodata);
            zhuanjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
                    startActivity(intent);
                }
            });
            xiangmu  = (ImageView)view.findViewById(R.id.xiangmu);
            zhuanli  = (ImageView)view. findViewById(R.id.zhuanli);
            zhuanli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
                    intent.putExtra("url", "http://www.zhongkechuangxiang.com/plus/patent/advertisement.html");
                    startActivity(intent);
                }
            });


            sousu  = (ImageView)view.findViewById(R.id.sousu);
            sosocontent = (TextView)view.findViewById(R.id.sosocontent);
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
            DisplayMetrics metrics=new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            widthPixels=metrics.widthPixels;
            heightPixels=metrics.heightPixels;
        }
        city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        if(city.equals("全国") || city.equals("包头") || city.equals("临沂")){
            city = "潍坊";
            channelid="1";
            code="8006";
        }
        options = ImageLoaderUtils.initOptions();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        if (!flag) {
            flag = true;
            listView = (HeaderListView) view.findViewById(listview);
            progress = (ProgressBar) view.findViewById(R.id.progress);
            progress.setVisibility(View.VISIBLE);
            HeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.firstfragmentheart_new, null);


            tab_btn = (LinearLayout)HeaderView.findViewById(R.id.tab_btn);
            banner_top = (ImageView) HeaderView.findViewById(R.id.banner_top);

          if(widthPixels <= 720){
              ImageLoader.getInstance().displayImage("http://www.zhongkechuangxiang.com/uploads/news/columnicon/720.png"
                      , banner_top, options);
          }else if(widthPixels > 720 && widthPixels < 1024){
              ImageLoader.getInstance().displayImage("http://www.zhongkechuangxiang.com/uploads/news/columnicon/1080.png"
                      , banner_top, options);
          }else if(widthPixels > 1024 ){
              ImageLoader.getInstance().displayImage("http://www.zhongkechuangxiang.com/uploads/news/columnicon/1440.png"
                      , banner_top, options);
          }else{
              ImageLoader.getInstance().displayImage("http://www.zhongkechuangxiang.com/uploads/news/columnicon/head.png"
                      , banner_top, options);
          }

            banner_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(getActivity(), ActiveActivity.class);
//                    intent.putExtra("url", "http://www.zhongkechuangxiang.com/webservice/city_zm/index.html");
//                    startActivity(intent);
                }
            });
            nice_spinner = (TextView) HeaderView.findViewById(R.id.nice_spinner);

            nice_spinner1 = (TextView)view.findViewById(R.id.nice_spinner1);

            nice_spinner1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LocalCity.class);
                    startActivity(intent);
                }
            });
            sousuo = (ImageView) HeaderView.findViewById(R.id.sousuo);
//            if(MyApplication.model.equals("ZK-R322")){
//                ViewGroup.LayoutParams params = sousuo.getLayoutParams();
//                params.height= 120;
//                params.width = 120;
//                sousuo.setLayoutParams(params);
//            }
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

            if(city != null && !city.equals("")){
                nice_spinner1.setText(city);
                nice_spinner.setText(city);
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

            sousuo.setOnClickListener(new View.OnClickListener() {
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
                        getjsons();
                        getAdsJson();
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

            nice_spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LocalCity.class);
                    startActivity(intent);
                }
            });

//           if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//               RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
//               layoutParams.bottomMargin=MyApplication.navigationbar+150;
//               listView.setLayoutParams(layoutParams);
//           }

            listView.setOnTouchListener(new View.OnTouchListener() {
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
                                listView.setState(true);
                            }else{
                                if(isherat){
                                    listView.setState(true);
                                }else {
                                    listView.setState(true);
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

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        View firstVisibleItemView = listView.getChildAt(0);
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

//                    return 0;
                }


                class ItemRecod {
                    int height = 0;
                    int top = 0;
                }
            });

        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
//            getjsons();
            getAdsJson();
        }


        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiverBroad = new ReceiverBroad();
        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("downok");
        oninfilter.addAction("changetitles");
        oninfilter.addAction("changelistviewheihgt");
        getActivity().registerReceiver(receiverBroad, oninfilter);
        tintManager = new SystemBarTintManager(getActivity());
    }


    public void getjsons(){
        city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        if(city.equals("全国") || city.equals("包头") || city.equals("临沂")){
            city = "潍坊";
            channelid="1";
            code="8006";
        }
        if(currentChannel != null){
            if(!currentChannel.nativeplace.equals(city)){
                for(int i=0; i< MainActivity.columnButton.size();i++){
                    PlaceChannel item = MainActivity.columnButton.get(i);
                    if(item.nativeplace.contains(city)){
                        currentChannel = item;
                        channelid = item.channelid;
                        code=item.code;
                        break;
                    }
                }
            }
        }
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("channelid",channelid);
        Log.d("lizisong", "channelid:"+channelid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,10000,0);
    }

    class FirstFragmentAdapter extends BaseAdapter{
        public ViewPager viewPager;

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
            HoldView holdview = null;
            if(convertView == null){
                holdview = new HoldView();
                convertView = View.inflate(getActivity(),R.layout.firstfragmentadapter,null);
                holdview.tianxuqiu = (LinearLayout)convertView.findViewById(R.id.tianxuqiu);

                holdview.scrollBanner = (ScrollBanner)convertView.findViewById(R.id.sb_demographic);
                holdview.xiangmu = (LinearLayout)convertView.findViewById(R.id.xiangmu);
                holdview.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                holdview.xiangmu_lay2 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay2);
                holdview.xiangmu_lay3 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay3);
                holdview.xiangmu_line1 = (TextView)convertView.findViewById(R.id.xiangmu_line1);
                holdview.xiangmu_line2 = (TextView)convertView.findViewById(R.id.xiangmu_line2);
                holdview.xiangmu_line3 = (TextView)convertView.findViewById(R.id.xiangmu_line3);
                holdview.xiangmu_title = (TextView)convertView.findViewById(R.id.xiangmu_title);
                holdview.xiangmu_change = (TextView)convertView.findViewById(R.id.xiangmu_change);
                holdview.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
                holdview.xianmgmu2 = (ImageView)convertView.findViewById(R.id.xianmgmu2);
                holdview.xianmgmu3 = (ImageView)convertView.findViewById(R.id.xianmgmu3);
                holdview.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);
                holdview.xmtitle2 = (TextView)convertView.findViewById(R.id.xmtitle2);
                holdview.xmtitle3 = (TextView)convertView.findViewById(R.id.xmtitle3);
                holdview.lanyuan1 = (TextView)convertView.findViewById(R.id.lanyuan1);
                holdview.lanyuan2 = (TextView)convertView.findViewById(R.id.lanyuan2);
                holdview.lanyuan3 = (TextView)convertView.findViewById(R.id.lanyuan3);
                holdview.xm_more = (TextView)convertView.findViewById(R.id.xm_more);
                holdview.zhuanjia = (LinearLayout)convertView.findViewById(R.id.zhuanjia);
                holdview.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);
                holdview.rencai_lay2 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay2);
                holdview.rencai_lay3 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay3);
                holdview.rencai_lay4 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay4);

                holdview.rencai_line1 = (TextView)convertView.findViewById(R.id.rencai_line1);
                holdview.rencai_line2 = (TextView)convertView.findViewById(R.id.rencai_line2);
                holdview.rencai_line3 = (TextView)convertView.findViewById(R.id.rencai_line3);
                holdview.rencai_line4 = (TextView)convertView.findViewById(R.id.rencai_line4);

                holdview.zhuangjia_change = (TextView)convertView.findViewById(R.id.zhuangjia_change);
                holdview.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);
                holdview.rencai_img2 = (RoundImageView)convertView.findViewById(R.id.rencai_img2);
                holdview.rencai_img3 = (RoundImageView)convertView.findViewById(R.id.rencai_img3);
                holdview.rencai_img4 = (RoundImageView)convertView.findViewById(R.id.rencai_img4);
                holdview.rencai_title1 = (TextView)convertView.findViewById(R.id.rencai_title1);
                holdview.rencai_title2 = (TextView)convertView.findViewById(R.id.rencai_title2);
                holdview.rencai_title3 = (TextView)convertView.findViewById(R.id.rencai_title3);
                holdview.rencai_title4 = (TextView)convertView.findViewById(R.id.rencai_title4);
                holdview.rencai_lingyu1 = (TextView)convertView.findViewById(R.id.rencai_lingyu1);
                holdview.rencai_lingyu2 = (TextView)convertView.findViewById(R.id.rencai_lingyu2);
                holdview.rencai_lingyu3 = (TextView)convertView.findViewById(R.id.rencai_lingyu3);
                holdview.rencai_lingyu4 = (TextView)convertView.findViewById(R.id.rencai_lingyu4);
                holdview.rank1 = (TextView)convertView.findViewById(R.id.rank1);
                holdview.rank2 = (TextView)convertView.findViewById(R.id.rank2);
                holdview.rank3 = (TextView)convertView.findViewById(R.id.rank3);
                holdview.rank4 = (TextView)convertView.findViewById(R.id.rank4);
                holdview.zhuanjia_more = (TextView)convertView.findViewById(R.id.zhuanjia_more);
                holdview.hangye = (LinearLayout)convertView.findViewById(R.id.hangye);
                holdview.hangye_title = (TextView)convertView.findViewById(R.id.hangye_title);
                holdview.hangye_more = (TextView)convertView.findViewById(R.id.hangye_more);
                holdview.qingbaoitem = (RelativeLayout)convertView.findViewById(R.id.qingbaoitem);
                holdview.qingicon = (ImageView)convertView.findViewById(R.id.qingicon);
                holdview.title =(TextView)convertView.findViewById(R.id.title);
                holdview.price = (TextView)convertView.findViewById(R.id.price);
                holdview.description = (TextView)convertView.findViewById(R.id.description);
                holdview.timeupdate = (TextView)convertView.findViewById(R.id.timeupdate);
                holdview.updatadescription=(TextView)convertView.findViewById(R.id.updatadescription);
                holdview.type = (ImageView)convertView.findViewById(R.id.type);
                holdview.zhuanli = (LinearLayout)convertView.findViewById(R.id.zhuanli);
                holdview.zhuanli_title = (TextView)convertView.findViewById(R.id.zhuanli_title);
                holdview.zhuanli_change = (TextView)convertView.findViewById(R.id.zhuanli_change);
                holdview.zhuanli_lay1 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay1);
                holdview.zhuanli_lay2 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay2);
                holdview.zhuanli_lay3 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay3);
                holdview.zhuanli_lay4 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay4);
                holdview.zhuanli_line1 = (TextView)convertView.findViewById(R.id.zhuanli_line1);
                holdview.zhuanli_line2 = (TextView)convertView.findViewById(R.id.zhuanli_line2);
                holdview.zhuanli_line3 = (TextView)convertView.findViewById(R.id.zhuanli_line3);
                holdview.zhuanli_line4 = (TextView)convertView.findViewById(R.id.zhuanli_line4);
                holdview.zhuanli_title1 = (TextView)convertView.findViewById(R.id.zhuanli_title1);
                holdview.zhuanli_title2 = (TextView)convertView.findViewById(R.id.zhuanli_title2);
                holdview.zhuanli_title3 = (TextView)convertView.findViewById(R.id.zhuanli_title3);
                holdview.zhuanli_title4 = (TextView)convertView.findViewById(R.id.zhuanli_title4);
                holdview.zhuanli_lingyu1 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu1);
                holdview.zhuanli_lingyu2 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu2);
                holdview.zhuanli_lingyu3 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu3);
                holdview.zhuanli_lingyu4 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu4);
                holdview.zhuanli_more = (TextView)convertView.findViewById(R.id.zhuanli_more);
                holdview.xianxiafuwu = (LinearLayout)convertView.findViewById(R.id.xianxiafuwu);
                holdview.icon_left = (ImageView) convertView.findViewById(R.id.icon_left);
                holdview.icon_right = (ImageView) convertView.findViewById(R.id.icon_right);
                holdview.source1 = (TextView)convertView.findViewById(R.id.source1);
                holdview.source2 = (TextView)convertView.findViewById(R.id.source2);
                holdview.source3 = (TextView)convertView.findViewById(R.id.source3);
                holdview.fragment = (FrameLayout)convertView.findViewById(R.id.fragment);
                holdview.viewPager = (ViewPager)convertView.findViewById(R.id.viewPager);
                holdview.viewGroup = (LinearLayout) convertView.findViewById(R.id.viewGroup);
                convertView.setTag(holdview);
            }else{
                holdview = (HoldView) convertView.getTag();
            }
            try {
                final PostsData item =listData.get(position);
                holdview.tianxuqiu.setVisibility(View.GONE);
                holdview.fragment.setVisibility(View.GONE);
                holdview.xiangmu.setVisibility(View.GONE);
                holdview.zhuanjia.setVisibility(View.GONE);
                holdview.hangye.setVisibility(View.GONE);
                holdview.zhuanli.setVisibility(View.GONE);
                holdview.xianxiafuwu.setVisibility(View.GONE);
                holdview.qingbaoitem.setVisibility(View.GONE);

                if(item.typename.equals("zixun")){
                    holdview.fragment.setVisibility(View.GONE);
                    holdview.tianxuqiu.setVisibility(View.VISIBLE);
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    scrollBanner=holdview.scrollBanner;
                    holdview.scrollBanner.setList(zixuanlist);
                    holdview.scrollBanner.stopScroll();
                    holdview.scrollBanner.startScroll();
                    holdview.scrollBanner.setOnClickListener(new View.OnClickListener() {
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

//                            Intent intent = new Intent();
//                            intent.setAction("action_toxuqiu");
//                            Recommend.aids =zixuanaid.get(scrollBanner.getCurrent()+"");
//                            getActivity().sendBroadcast(intent);
                        }
                    });
                }else if(item.typename.equals("guanggao")){
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.fragment.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params2 = holdview.fragment.getLayoutParams();
                    params2.height=MyApplication.widths*2/9;
                    holdview.fragment.setLayoutParams(params2);
                    viewPager = holdview.viewPager;

                    holdview.viewPager.setAdapter(advAdapter);
                    for(int i=0; i<tips.size();i++){
                        ImageView img = tips.get(i);
                        if(widthPixels<=480){
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                            params.setMargins(5, 0, 5, 0);
                            holdview.viewGroup.addView(img, params);

                        }else{
                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                            params.setMargins(5, 0, 5,5);
                            holdview.viewGroup.addView(img, params);
                        }
                    }
                    holdview.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            Log.d("lizisong", "position:"+position);
                            current = position;
                        }

                        @Override
                        public void onPageSelected(int position) {
                             int index=position%tips.size();
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


                }else if(item.typename.equals("xiangmu")){
                    holdview.xiangmu.setVisibility(View.VISIBLE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.fragment.setVisibility(View.GONE);

                    int count = item.xiangmu.size();
                   PostData xiangmu1 = null,xiangmu2=null,xiangmu3=null;
                    if(count == 1){
                        holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_lay2.setVisibility(View.GONE);
                        holdview.xiangmu_line2.setVisibility(View.GONE);
                        holdview.xiangmu_lay3.setVisibility(View.GONE);
                        holdview.xiangmu_line3.setVisibility(View.GONE);
                        xiangmu1 = item.xiangmu.get(0);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                        , holdview.xianmgmu1, options);

                            } else {
                                holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdview.xianmgmu1, options);
                        }
                        holdview.xmtitle1.setText(xiangmu1.title);
                        holdview.lanyuan1.setText(xiangmu1.area_cate);


                        if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                            holdview.source1.setVisibility(View.VISIBLE);
                            holdview.source1.setText(xiangmu1.labels);
                            if(xiangmu1.labels.equals("精品项目")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu1.labels.equals("钛领推荐")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu1.labels.equals("国家科学基金")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source1.setVisibility(View.GONE);
                        }



                    }else if(count == 2){
                        xiangmu1 = item.xiangmu.get(0);
                        xiangmu2 = item.xiangmu.get(1);
                        holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_lay2.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line2.setVisibility(View.VISIBLE);
                        holdview.xiangmu_lay3.setVisibility(View.GONE);
                        holdview.xiangmu_line3.setVisibility(View.GONE);

                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                        , holdview.xianmgmu1, options);

                            } else {
                                holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdview.xianmgmu1, options);
                        }
                        holdview.xmtitle1.setText(xiangmu1.title);
                        holdview.lanyuan1.setText(xiangmu1.area_cate);
                        if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                            holdview.source1.setVisibility(View.VISIBLE);
                            holdview.source1.setText(xiangmu1.labels);
                            if(xiangmu1.labels.equals("精品项目")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu1.labels.equals("钛领推荐")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu1.labels.equals("国家科学基金")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source1.setVisibility(View.GONE);
                        }

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                        , holdview.xianmgmu2, options);

                            } else {
                                holdview.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                    , holdview.xianmgmu2, options);
                        }
                        holdview.xmtitle2.setText(xiangmu2.title);
                        holdview.lanyuan2.setText(xiangmu2.area_cate);
                        if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                            holdview.source2.setVisibility(View.VISIBLE);
                            holdview.source2.setText(xiangmu2.labels);
                            if(xiangmu2.labels.equals("精品项目")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu2.labels.equals("钛领推荐")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu2.labels.equals("国家科学基金")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source2.setVisibility(View.GONE);
                        }



                    }else if(count >=3){
                        xiangmu1 = item.xiangmu.get(0);
                        xiangmu2 = item.xiangmu.get(1);
                        xiangmu3 = item.xiangmu.get(2);
                        holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                        holdview.xiangmu_lay2.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line2.setVisibility(View.VISIBLE);
                        holdview.xiangmu_lay3.setVisibility(View.VISIBLE);
                        holdview.xiangmu_line3.setVisibility(View.VISIBLE);

                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                        , holdview.xianmgmu1, options);

                            } else {
                                holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                    , holdview.xianmgmu1, options);
                        }
                        holdview.xmtitle1.setText(xiangmu1.title);
                        holdview.lanyuan1.setText(xiangmu1.area_cate);
                        Log.d("lizisong", "xiangmu1.labels:"+xiangmu1.labels);
                        if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                            holdview.source1.setVisibility(View.VISIBLE);
                            holdview.source1.setText(xiangmu1.labels);
                            if(xiangmu1.labels.equals("精品项目")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu1.labels.equals("钛领推荐")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu1.labels.equals("国家科学基金")){
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source1.setVisibility(View.GONE);
                        }
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                        , holdview.xianmgmu2, options);

                            } else {
                                holdview.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                    , holdview.xianmgmu2, options);
                        }
                        holdview.xmtitle2.setText(xiangmu2.title);
                        holdview.lanyuan2.setText(xiangmu2.area_cate);
                        Log.d("lizisong", "xiangmu2.labels:"+xiangmu2.labels);

                        if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                            holdview.source2.setVisibility(View.VISIBLE);
                            holdview.source2.setText(xiangmu2.labels);
                            if(xiangmu2.labels.equals("精品项目")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu2.labels.equals("钛领推荐")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu2.labels.equals("国家科学基金")){
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source2.setVisibility(View.GONE);
                        }

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && xiangmu3.litpic != null && !xiangmu3.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                        , holdview.xianmgmu3, options);

                            } else {
                                holdview.xianmgmu3.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                    , holdview.xianmgmu3, options);
                        }
                        holdview.xmtitle3.setText(xiangmu3.title);
                        holdview.lanyuan3.setText(xiangmu3.area_cate);
                        if(xiangmu3.labels != null && (!xiangmu3.labels.equals(""))){
                            holdview.source3.setVisibility(View.VISIBLE);
                            holdview.source3.setText(xiangmu3.labels);
                            if(xiangmu3.labels.equals("精品项目")){
                                holdview.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }else if(xiangmu3.labels.equals("钛领推荐")){
                                holdview.source3.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                            }else if(xiangmu3.labels.equals("国家科学基金")){
                                holdview.source3.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                            }else{
                                holdview.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                            }
                        }else{
                            holdview.source3.setVisibility(View.GONE);
                        }


                    }
                    holdview.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String labels = item.xiangmu.get(0).labels;
                                if(labels != null){
                                    if(labels.contains("精品项目") || (item.xiangmu.get(0).typeid.equals("2") && item.xiangmu.get(0).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(0).title);
                                        intent.putExtra("url", item.xiangmu.get(0).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(0).id);
                                        startActivity(intent);
                                    }
                                }else {
                                    if((item.xiangmu.get(0).typeid.equals("2") && item.xiangmu.get(0).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(0).title);
                                        intent.putExtra("url", item.xiangmu.get(0).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(0).id);
                                        startActivity(intent);
                                    }
                                }
                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){

                            }


                        }
                    });
                    holdview.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String labels = item.xiangmu.get(1).labels;
                                if(labels != null){
                                    if(labels.contains("精品项目") || (item.xiangmu.get(1).typeid.equals("2") && item.xiangmu.get(1).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(1).title);
                                        intent.putExtra("url", item.xiangmu.get(1).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(1).id);
                                        startActivity(intent);
                                    }
                                }else {
                                    if((item.xiangmu.get(1).typeid.equals("2") && item.xiangmu.get(1).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(1).title);
                                        intent.putExtra("url", item.xiangmu.get(1).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(1).id);
                                        startActivity(intent);
                                    }
                                }
                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){

                            }



                        }
                    });
                    holdview.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String labels = item.xiangmu.get(2).labels;
                                if(labels != null){
                                    if(labels.contains("精品项目")|| (item.xiangmu.get(2).typeid.equals("2") && item.xiangmu.get(2).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(2).title);
                                        intent.putExtra("url", item.xiangmu.get(2).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(2).id);
                                        startActivity(intent);
                                    }
                                }else {
                                    if((item.xiangmu.get(2).typeid.equals("2") && item.xiangmu.get(2).typename.equals("html"))){
                                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                        intent.putExtra("title", item.xiangmu.get(2).title);
                                        intent.putExtra("url", item.xiangmu.get(2).url);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", item.xiangmu.get(2).id);
                                        startActivity(intent);
                                    }
                                }

                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){

                            }



                        }
                    });
                    holdview.xiangmu_change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progress.setVisibility(View.VISIBLE);
                            sortid = item.xiangmu.get(item.xiangmu.size()-1).id;
                            ChangeXumu();
                        }
                    });
                    holdview.xm_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), NewProJect.class);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), LookMoreActivity.class);
//                            intent.putExtra("typeid",2);
//                            intent.putExtra("title","更多项目");
//                            intent.putExtra("channel","项目");
//                            startActivity(intent);
                        }
                    });
                }else if(item.typename.equals("rencai")){
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.VISIBLE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.fragment.setVisibility(View.GONE);

                    int count = item.rencai.size();
                    PostData rencai1,rencai2,rencai3,rencai4;
                    if(count == 1){
                        rencai1 = item.rencai.get(0);
                        holdview.rencai_lay1.setVisibility(View.VISIBLE);
                        holdview.rencai_line1.setVisibility(View.VISIBLE);
                        holdview.rencai_lay2.setVisibility(View.GONE);
                        holdview.rencai_line2.setVisibility(View.GONE);
                        holdview.rencai_lay3.setVisibility(View.GONE);
                        holdview.rencai_line3.setVisibility(View.GONE);
                        holdview.rencai_lay4.setVisibility(View.GONE);
                        holdview.rencai_line4.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);

                            } else {
                                holdview.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(rencai1.litpic == null || rencai1.litpic.equals("")){
                                holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);
                            }

                        }

                        holdview.rencai_title1.setText(rencai1.title);
                        holdview.rencai_lingyu1.setText(rencai1.unit);
                        holdview.rank1.setText(rencai1.rank);


                    }else if(count == 2){
                        rencai1 = item.rencai.get(0);
                        rencai2 = item.rencai.get(1);
                        holdview.rencai_lay1.setVisibility(View.VISIBLE);
                        holdview.rencai_line1.setVisibility(View.VISIBLE);
                        holdview.rencai_lay2.setVisibility(View.VISIBLE);
                        holdview.rencai_line2.setVisibility(View.VISIBLE);
                        holdview.rencai_lay3.setVisibility(View.GONE);
                        holdview.rencai_line3.setVisibility(View.GONE);
                        holdview.rencai_lay4.setVisibility(View.GONE);
                        holdview.rencai_line4.setVisibility(View.GONE);

                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);

                            } else {
                                holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai1.litpic == null || rencai1.litpic.equals("")){
                                holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);
                            }

                        }
                        holdview.rencai_title1.setText(rencai1.title);
                        holdview.rencai_lingyu1.setText(rencai1.unit);
                        holdview.rank1.setText(rencai1.rank);


                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai2.litpic
                                        , holdview.rencai_img2, options);

                            } else {

                                holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai2.litpic == null || rencai2.litpic.equals("")){
                                holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                               ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdview.rencai_img2, options);
                            }
                        }
                        holdview.rencai_title2.setText(rencai2.title);
                        holdview.rencai_lingyu2.setText(rencai2.unit);
                        holdview.rank2.setText(rencai2.rank);

                    }else if(count == 3){
                        rencai1 = item.rencai.get(0);
                        rencai2 = item.rencai.get(1);
                        rencai3 = item.rencai.get(2);
                        holdview.rencai_lay1.setVisibility(View.VISIBLE);
                        holdview.rencai_line1.setVisibility(View.VISIBLE);
                        holdview.rencai_lay2.setVisibility(View.VISIBLE);
                        holdview.rencai_line2.setVisibility(View.VISIBLE);
                        holdview.rencai_lay3.setVisibility(View.VISIBLE);
                        holdview.rencai_line3.setVisibility(View.VISIBLE);
                        holdview.rencai_lay4.setVisibility(View.GONE);
                        holdview.rencai_line4.setVisibility(View.GONE);

                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);

                            } else {
                                holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai1.litpic == null || rencai1.litpic.equals("")){
                                holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                            ImageLoader.getInstance().displayImage(rencai1.litpic
                                    , holdview.rencai_img1, options);
                            }
                        }
                        holdview.rencai_title1.setText(rencai1.title);
                        holdview.rencai_lingyu1.setText(rencai1.unit);
                        holdview.rank1.setText(rencai1.rank);


                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai2.litpic
                                        , holdview.rencai_img2, options);

                            } else {
                                holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai2.litpic == null || rencai2.litpic.equals("")){
                                holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                            ImageLoader.getInstance().displayImage(rencai2.litpic
                                    , holdview.rencai_img2, options);
                            }
                        }
                        holdview.rencai_title2.setText(rencai2.title);
                        holdview.rencai_lingyu2.setText(rencai2.unit);
                        holdview.rank2.setText(rencai2.rank);

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai3.litpic
                                        , holdview.rencai_img3, options);

                            } else {
                                holdview.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai3.litpic == null || rencai3.litpic.equals("")){
                                holdview.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai3.litpic
                                        , holdview.rencai_img3, options);
                            }
                        }
                        holdview.rencai_title3.setText(rencai3.title);
                        holdview.rencai_lingyu3.setText(rencai3.unit);
                        holdview.rank3.setText(rencai3.rank);


                    }else if(count >= 4){
                        rencai1 = item.rencai.get(0);
                        rencai2 = item.rencai.get(1);
                        rencai3 = item.rencai.get(2);
                        rencai4 = item.rencai.get(3);

                        holdview.rencai_lay1.setVisibility(View.VISIBLE);
                        holdview.rencai_line1.setVisibility(View.VISIBLE);
                        holdview.rencai_lay2.setVisibility(View.VISIBLE);
                        holdview.rencai_line2.setVisibility(View.VISIBLE);
                        holdview.rencai_lay3.setVisibility(View.VISIBLE);
                        holdview.rencai_line3.setVisibility(View.VISIBLE);
                        holdview.rencai_lay4.setVisibility(View.VISIBLE);
                        holdview.rencai_line4.setVisibility(View.VISIBLE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);

                            } else {
                                holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai1.litpic == null || rencai1.litpic.equals("")){
                                holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai1.litpic
                                        , holdview.rencai_img1, options);
                            }
                        }
                        holdview.rencai_title1.setText(rencai1.title);
                        holdview.rencai_lingyu1.setText(rencai1.unit);
                        holdview.rank1.setText(rencai1.rank);


                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai2.litpic
                                        , holdview.rencai_img2, options);

                            } else {
                                holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai2.litpic == null || rencai2.litpic.equals("")){
                                holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai2.litpic
                                        , holdview.rencai_img2, options);
                            }
                        }
                        holdview.rencai_title2.setText(rencai2.title);
                        holdview.rencai_lingyu2.setText(rencai2.unit);
                        holdview.rank2.setText(rencai2.rank);

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai3.litpic
                                        , holdview.rencai_img3, options);

                            } else {
                                holdview.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai3.litpic == null || rencai3.litpic.equals("")){
                                holdview.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                                ImageLoader.getInstance().displayImage(rencai3.litpic
                                        , holdview.rencai_img3, options);
                            }
                        }
                        holdview.rencai_title3.setText(rencai3.title);
                        holdview.rencai_lingyu3.setText(rencai3.unit);
                        holdview.rank3.setText(rencai3.rank);

                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && rencai4.litpic != null && !rencai4.litpic.equals("")) {
                                ImageLoader.getInstance().displayImage(rencai4.litpic
                                        , holdview.rencai_img4, options);

                            } else {
                                holdview.rencai_img4.setBackgroundResource(R.mipmap.touxiangzhanwei);
                            }
                        } else {
                            if(rencai4.litpic == null || rencai4.litpic.equals("")){
                                holdview.rencai_img4.setImageResource(R.mipmap.touxiangzhanwei);
                            }else{
                            ImageLoader.getInstance().displayImage(rencai4.litpic
                                    , holdview.rencai_img4, options);}
                        }
                        holdview.rencai_title4.setText(rencai4.title);
                        holdview.rencai_lingyu4.setText(rencai4.unit);
                        holdview.rank4.setText(rencai4.rank);

                    }
                    holdview.rencai_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.rencai.get(0).id);
//                                intent.putExtra("name", item.rencai.get(0).typename);
//                                intent.putExtra("pic", item.rencai.get(0).litpic);
//                                startActivity(intent);

                            Intent intent = new Intent(getActivity(), NewRenCaiTail.class);
                            intent.putExtra("aid", item.rencai.get(0).id);
                            startActivity(intent);
                        }
                    });
                    holdview.rencai_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.rencai.get(1).id);
//                            intent.putExtra("name", item.rencai.get(1).typename);
//                            intent.putExtra("pic", item.rencai.get(1).litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(getActivity(), NewRenCaiTail.class);
                            intent.putExtra("aid", item.rencai.get(1).id);
                            startActivity(intent);
                        }
                    });
                    holdview.rencai_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.rencai.get(2).id);
//                            intent.putExtra("name", item.rencai.get(2).typename);
//                            intent.putExtra("pic", item.rencai.get(2).litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(getActivity(), NewRenCaiTail.class);
                            intent.putExtra("aid", item.rencai.get(2).id);
                            startActivity(intent);
                        }
                    });
                    holdview.rencai_lay4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.rencai.get(3).id);
//                            intent.putExtra("name", item.rencai.get(3).typename);
//                            intent.putExtra("pic", item.rencai.get(3).litpic);
//                            startActivity(intent);
                            Intent intent = new Intent(getActivity(), NewRenCaiTail.class);
                            intent.putExtra("aid", item.rencai.get(3).id);
                            startActivity(intent);
                        }
                    });

                    holdview.zhuangjia_change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progress.setVisibility(View.VISIBLE);
                            sortid = item.rencai.get(item.rencai.size()-1).id;
                            ChangeZhuanJia();
                        }
                    });
                    holdview.zhuanjia_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                            intent.putExtra("typeid",4);
                            intent.putExtra("title","更多专家");
                            intent.putExtra("channel","专家");
                            startActivity(intent);
                        }
                    });

                }else if(item.typename.equals("qingbao")){
                   final PostData qingbao = item.qingbao.get(0);
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.VISIBLE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.VISIBLE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.fragment.setVisibility(View.GONE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && qingbao.telig_cover != null && !qingbao.telig_cover.equals("")) {
                            ImageLoader.getInstance().displayImage(qingbao.telig_cover
                                    , holdview.qingicon, options);

                        } else {
                            holdview.qingicon.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(qingbao.telig_cover
                                , holdview.qingicon, options);
                    }
                    holdview.title.setText(qingbao.telig_title);
                    holdview.description.setText(qingbao.telig_description);

                    if(qingbao.telig_type.equals("0")){
                        holdview.type.setVisibility(View.GONE);
                    }else if(qingbao.telig_type.equals("2")){
                        holdview.type.setVisibility(View.VISIBLE);
                        holdview.type.setBackgroundResource(R.mipmap.xianmian);
                    }else if(qingbao.telig_type.equals("1")){
                        holdview.type.setVisibility(View.VISIBLE);
                        holdview.type.setBackgroundResource(R.mipmap.youhui);
                    }
                    holdview.updatadescription.setText(qingbao.telig_journal_title);//暂定
                    holdview.price.setText(qingbao.telig_price+"元/"+qingbao.telig_unit);
                    if(Float.parseFloat(qingbao.telig_price) == 0){
                        holdview.price.setText("免费");
                    }
                    if(qingbao.telig_journal_pubdate != null && !qingbao.telig_journal_pubdate.equals("")){
                        holdview.timeupdate.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(qingbao.telig_journal_pubdate)*1000), qingbao.telig_journal_pubdate)+"更新");
                    }

                    holdview.hangye_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
                            startActivity(intent);

                        }
                    });
                    holdview.hangye.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), QingBaoDeilActivity.class);
                            intent.putExtra("teligId",qingbao.telig_id);
                            intent.putExtra("telig_price",qingbao.telig_price);
                            intent.putExtra("telig_unit",qingbao.telig_unit);
                            intent.putExtra("telig_type",qingbao.telig_type);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), WebQingBaoActivity.class);
//                            intent.putExtra("url", qingbao.coverUrl);
//                            intent.putExtra("contenturl", qingbao.jumpUrl);
//                            intent.putExtra("title", qingbao.title);
//                            startActivity(intent);
                        }
                    });

                }else if(item.typename.equals("zhuanli")){
                    PostData zhuanli1,zhuanli2,zhuanli3,zhuanli4;
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.VISIBLE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    holdview.fragment.setVisibility(View.GONE);
                    int count = item.zhuanli.size();
                    if(count == 1){
                        zhuanli1 = item.zhuanli.get(0);
                        holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay2.setVisibility(View.GONE);
                        holdview.zhuanli_line2.setVisibility(View.GONE);
                        holdview.zhuanli_lay3.setVisibility(View.GONE);
                        holdview.zhuanli_line3.setVisibility(View.GONE);
                        holdview.zhuanli_lay4.setVisibility(View.GONE);
                        holdview.zhuanli_line4.setVisibility(View.GONE);
                        holdview.zhuanli_title1.setText(zhuanli1.title);
                        holdview.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);

                    }else if(count == 2){
                        zhuanli1 = item.zhuanli.get(0);
                        zhuanli2 = item.zhuanli.get(1);
                        holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay3.setVisibility(View.GONE);
                        holdview.zhuanli_line3.setVisibility(View.GONE);
                        holdview.zhuanli_lay4.setVisibility(View.GONE);
                        holdview.zhuanli_line4.setVisibility(View.GONE);
                        holdview.zhuanli_title1.setText(zhuanli1.title);
                        holdview.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                        holdview.zhuanli_title2.setText(zhuanli2.title);
                        holdview.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);

                    }else if(count == 3){
                        zhuanli1 = item.zhuanli.get(0);
                        zhuanli2 = item.zhuanli.get(1);
                        zhuanli3 = item.zhuanli.get(2);
                        holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay3.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line3.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay4.setVisibility(View.GONE);
                        holdview.zhuanli_line4.setVisibility(View.GONE);
                        holdview.zhuanli_title1.setText(zhuanli1.title);
                        holdview.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                        holdview.zhuanli_title2.setText(zhuanli2.title);
                        holdview.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                        holdview.zhuanli_title3.setText(zhuanli3.title);
                        holdview.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);

                    }else if(count >= 4){
                        zhuanli1 = item.zhuanli.get(0);
                        zhuanli2 = item.zhuanli.get(1);
                        zhuanli3 = item.zhuanli.get(2);
                        zhuanli4 = item.zhuanli.get(3);

                        holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay3.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line3.setVisibility(View.VISIBLE);
                        holdview.zhuanli_lay4.setVisibility(View.VISIBLE);
                        holdview.zhuanli_line4.setVisibility(View.VISIBLE);
                        holdview.zhuanli_title1.setText(zhuanli1.title);
                        holdview.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                        holdview.zhuanli_title2.setText(zhuanli2.title);
                        holdview.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                        holdview.zhuanli_title3.setText(zhuanli3.title);
                        holdview.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);
                        holdview.zhuanli_title4.setText(zhuanli4.title);
                        holdview.zhuanli_lingyu4.setText("所属领域:"+zhuanli4.area_cate);
                    }
                    holdview.zhuanli_lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                            intent.putExtra("aid", item.zhuanli.get(0).id);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.zhuanli.get(0).id);
//                            intent.putExtra("name", item.zhuanli.get(0).typename);
//                            intent.putExtra("pic", item.zhuanli.get(0).litpic);
//                            startActivity(intent);
                        }
                    });
                    holdview.zhuanli_lay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                            intent.putExtra("aid", item.zhuanli.get(1).id);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.zhuanli.get(1).id);
//                            intent.putExtra("name", item.zhuanli.get(1).typename);
//                            intent.putExtra("pic", item.zhuanli.get(1).litpic);
//                            startActivity(intent);
                        }
                    });
                    holdview.zhuanli_lay3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                            intent.putExtra("aid", item.zhuanli.get(2).id);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.zhuanli.get(2).id);
//                            intent.putExtra("name", item.zhuanli.get(2).typename);
//                            intent.putExtra("pic", item.zhuanli.get(2).litpic);
//                            startActivity(intent);
                        }
                    });
                    holdview.zhuanli_lay4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                            intent.putExtra("aid", item.zhuanli.get(3).id);
                            startActivity(intent);
//                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                            intent.putExtra("id", item.zhuanli.get(3).id);
//                            intent.putExtra("name", item.zhuanli.get(3).typename);
//                            intent.putExtra("pic", item.zhuanli.get(3).litpic);
//                            startActivity(intent);
                        }
                    });
                    holdview.zhuanli_change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progress.setVisibility(View.VISIBLE);
                            sortid = item.zhuanli.get(item.zhuanli.size()-1).id;
                            ChangeZhuanli();
                        }
                    });
                    holdview.zhuanli_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                            intent.putExtra("typeid",5);
                            intent.putExtra("title","更多专利");
                            intent.putExtra("channel","专利");
                            startActivity(intent);
                        }
                    });



                }else if(item.typename.equals("offline")){
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.VISIBLE);
                    holdview.fragment.setVisibility(View.GONE);
                    final PostData offline1,offline2;
                    offline1 = item.offline.get(0);
                    offline2 = item.offline.get(1);

                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI && offline1.litpic != null && !offline1.litpic.equals("")) {
                            ImageLoader.getInstance().displayImage(offline1.litpic
                                    , holdview.icon_left, options);
                            ImageLoader.getInstance().displayImage(offline2.litpic
                                    , holdview.icon_right, options);

                        } else {
                            holdview.icon_left.setBackgroundResource(R.mipmap.information_placeholder);
                            holdview.icon_right.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(offline1.litpic
                                , holdview.icon_left, options);
                        ImageLoader.getInstance().displayImage(offline2.litpic
                                , holdview.icon_right, options);
                    }
                    holdview.icon_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ActiveActivity.class);
                            intent.putExtra("url", offline1.url);
                            startActivity(intent);
                        }
                    });

                    holdview.icon_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ActiveActivity.class);
                            intent.putExtra("url", offline2.url);
                            startActivity(intent);
                        }
                    });

                }

            }catch (Exception e){

            }

            return convertView;
        }
        class HoldView{
             //<!-- 新的首页-->
            public LinearLayout tianxuqiu;

            //滚动
            public ScrollBanner scrollBanner;
            //项目
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
            public TextView hangye_title,hangye_more;
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
            //滚动广告
            public FrameLayout fragment;
            public ViewPager viewPager;
            public LinearLayout viewGroup;

        }
    }
    private class  MyHandler extends Handler{
        WeakReference<FirstFragment> mActivityReference;
        MyHandler(FirstFragment activity){
            mActivityReference = new WeakReference<FirstFragment>(activity);}
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                FirstFragment fragment = mActivityReference.get();
                if(fragment == null){
                    return;
                }
                try {
                    if(msg.what == 10000){
                        progress.setVisibility(View.GONE);
//                    listView.setPullDownToRefreshFinish();
                        Gson gson = new Gson();
                        ret = (String) msg.obj;
                        homedata = gson.fromJson(ret, HomeEntity.class);

                        if(homedata != null && homedata.code.equals("1")){
                            listData.clear();
                            if(homedata.data != null){
                                if(homedata.data.ads!=null){
                                    adsListData.clear();
                                    for (int i=0;i<homedata.data.ads.size();i++){
                                        ADS ads = homedata.data.ads.get(i);
                                        adsListData.add(ads);
                                    }
                                }
                                ADS ads0 = null;
                                if(adsListData != null && adsListData.size() >0){
                                     ads0 = adsListData.get(0);
                                }

                                if(ads0 != null && ads0.image != null){
                                    ImageLoader.getInstance().displayImage(ads0.picUrl
                                            , banner_top, options);
                                    if(widthPixels <= 720){
                                        ImageLoader.getInstance().displayImage(ads0.image.image3
                                                , banner_top, options);
                                    }else if(widthPixels > 720 && widthPixels < 1024){
                                        ImageLoader.getInstance().displayImage(ads0.image.image2
                                                , banner_top, options);
                                    }else if(widthPixels > 1024 ){
                                        ImageLoader.getInstance().displayImage(ads0.image.image1
                                                , banner_top, options);
                                    }else{
                                        ImageLoader.getInstance().displayImage(ads0.image.image2
                                                , banner_top, options);
                                    }
                                }

                                if(homedata.data.adv!=null){
                                    advListData.clear();
                                    for (int i=0;i<homedata.data.adv.size();i++){
                                        ADS ads = homedata.data.adv.get(i);
                                        advListData.add(ads);
                                    }
                                    if(tips == null){
                                       tips = new ArrayList<ImageView>();
                                    }else{
                                        tips.clear();
                                    }
                                    if(advListData.size() > 1){
                                        for (int i = 0; i < advListData.size(); i++) {
                                            //画圆点
                                            ImageView imageView = new ImageView(getActivity());
                                            if (i == 0) {
                                                imageView.setImageResource(R.drawable.dot_focused);
                                            } else {
                                                imageView.setImageResource(R.drawable.dot_normal);
                                            }
                                            tips.add(imageView);
                                        }
                                    }
                                    advAdapter = new MyAdapter();
                                }
                                if(homedata.data.posts != null){

                                    PostsData zixuan = new PostsData();
                                    zixuan.typename ="zixun";
                                    zixuan.zixun = homedata.data.posts.zixun;
                                    listData.add(zixuan);
                                    zixuanaid.clear();
                                    zixuanlist.clear();
                                    String pos = "";
                                    String aids="";
                                    int p=0;

                                    for(int i=0; i<zixuan.zixun.size(); i++){
                                        PostData posdata = zixuan.zixun.get(i);
                                        String type="【"+posdata.typename+"】";

                                        if(i % 2 ==0){
                                            pos="";
                                            aids="";
                                            pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                            aids=posdata.id+",";
//                                        if(pos.length() > 20){
//                                            pos = pos.substring(0,20);
//                                        }
                                        }else if(i %2 == 1){
                                            pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                            aids=aids+posdata.id;

                                            zixuanaid.put(p+"",aids);
                                            p++;
                                            zixuanlist.add(pos);
                                        }
                                    }

                                    for(int i=0; i<zixuan.zixun.size(); i++){
                                        PostData posdata = zixuan.zixun.get(i);
                                        String type="【"+posdata.typename+"】";

                                        if(i % 2 ==0){
                                            pos="";
                                            aids="";
                                            pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                            aids=posdata.id+",";
//                                        if(pos.length() > 20){
//                                            pos = pos.substring(0,20);
//                                        }
                                        }else if(i %2 == 1){
                                            pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                            aids=aids+posdata.id;

                                            zixuanaid.put(p+"",aids);
                                            p++;
                                            Log.d("lizisong", pos);
                                            zixuanlist.add(pos);
                                        }
                                    }

                                    if(homedata.data.adv!=null && homedata.data.adv.size() >0){
                                        PostsData guanggao = new PostsData();
                                        guanggao.typename ="guanggao";
                                        listData.add(guanggao);

                                    }

                                   if(homedata.data.posts.xiangmu != null && homedata.data.posts.xiangmu.size()>0){
                                        PostsData xiangmu = new PostsData();
                                        xiangmu.typename ="xiangmu";
                                        xiangmu.xiangmu = homedata.data.posts.xiangmu;
                                        listData.add(xiangmu);
                                   }
                                   if(homedata.data.posts.rencai != null && homedata.data.posts.rencai.size()>0){
                                        PostsData rencai = new PostsData();
                                        rencai.typename ="rencai";
                                        rencai.rencai = homedata.data.posts.rencai;
                                        listData.add(rencai);
                                   }
                                  if(homedata.data.posts.qingbao != null && homedata.data.posts.qingbao.size()>0){
                                        PostsData qingbao = new PostsData();
                                        qingbao.typename ="qingbao";
                                        qingbao.qingbao = homedata.data.posts.qingbao;
                                        listData.add(qingbao);
                                  }
                                   if(homedata.data.posts.zhuanli != null && homedata.data.posts.zhuanli.size()>0){
                                        PostsData zhuanli = new PostsData();
                                        zhuanli.typename ="zhuanli";
                                        zhuanli.zhuanli = homedata.data.posts.zhuanli;
                                        listData.add(zhuanli);
                                   }
                                   if(homedata.data.posts.offline != null && homedata.data.posts.offline.size()>0){
                                        PostsData offline = new PostsData();
                                        offline.typename ="offline";
                                        offline.offline = homedata.data.posts.offline;
                                        listData.add(offline);
                                   }

                                    if(adapter == null){
                                        listView.setHeaderIV(banner_top);
                                        listView.addHeaderView(HeaderView);
                                        adapter = new FirstFragmentAdapter();
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        if(homedata.data.adv!=null && homedata.data.adv.size() >1 ){
                                            Message message = Message.obtain();
                                            msg.what = 100;
                                            handler.sendMessageDelayed(message, 4000);
                                        }

                                    }else {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        if(listData.size() == 0){
                            nodata.setVisibility(View.VISIBLE);
                        }else{
                            nodata.setVisibility(View.GONE);
                        }
                    }

                    if(msg.what == 2){
                        progress.setVisibility(View.GONE);
                        Gson g=new Gson();
                        json = (String)msg.obj;
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
//                        Intent intent = new Intent(getActivity(), XuQiuSendSuccess.class);
//                        startActivity(intent);
//                        xuqiuUpdata();
//                            Intent intent = new Intent();
//                            intent.setAction("action_update_conut");
//                            getActivity().sendBroadcast(intent);
                        }
                    }
                    if(msg.what == 3){
                        progress.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        json = (String)msg.obj;
                        homedata = gson.fromJson(json, HomeEntity.class);
                        if(homedata != null && homedata.code.equals("1")){
                            if(homedata.data.posts.xiangmu != null && homedata.data.posts.xiangmu.size() >0){

                                for(int i =0;i<listData.size();i++){
                                    PostsData item = listData.get(i);
                                    if(item.typename.equals("xiangmu")){
                                        item.xiangmu = homedata.data.posts.xiangmu;
//                                        for(int j=0; j<homedata.data.posts.xiangmu.size();j++){
//                                            Log.d("lizisong",homedata.data.posts.xiangmu.get(j).title);
//                                        }
                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }

                        }

                    }

                    if(msg.what == 4){
                        progress.setVisibility(View.GONE);
                        Gson gson = new Gson();
                       String json = (String)msg.obj;
                        homedata = gson.fromJson(json, HomeEntity.class);
                        if(homedata != null && homedata.code.equals("1")){
                            if(homedata.data.posts.rencai != null && homedata.data.posts.rencai.size() >0){
                                for(int i =0;i<listData.size();i++){
                                    PostsData item = listData.get(i);
                                    if(item.typename.equals("rencai")){
                                        item.rencai = homedata.data.posts.rencai;
                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }

                        }
                    }

                    if(msg.what == 5){
                        progress.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        json = (String)msg.obj;
                        homedata = gson.fromJson(json, HomeEntity.class);
                        if(homedata != null && homedata.code.equals("1")){
                            if(homedata.data.posts.zhuanli != null && homedata.data.posts.zhuanli.size() >0){
                                for(int i =0;i<listData.size();i++){
                                    PostsData item = listData.get(i);
                                    if(item.typename.equals("zhuanli")){
                                        item.zhuanli = homedata.data.posts.zhuanli;
                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(msg.what == 8){
                        Gson gson = new Gson();
                        adsjson = (String)msg.obj;
                        adsData = gson.fromJson(adsjson, AdsEntity.class);
                        if(adsData.code.equals("1")){
                            if(adsData.data != null){
                                if(adsData.data.size()>0){
                                    ShowFristGuideActivity.data.clear();
                                    for(int i=0;i<adsData.data.size();i++){
                                        ADS item = adsData.data.get(i);
                                        ShowFristGuideActivity.data.add(item);
                                    }
                                    Message msg0 = Message.obtain();
                                    msg0.what = 15;
                                    handler.sendMessageDelayed(msg0, 1000);

//                                Intent intent = new Intent(getActivity(), ShowFristGuideActivity.class);
//                                startActivity(intent);

                                }else{
                                    Message msg0 = Message.obtain();
                                    msg0.what = 16;
                                    handler.sendMessageDelayed(msg0, 1000);

                                }
                            }
                        }
                    }
                    if(msg.what == 100){
                        Message message =Message.obtain();
                        message.what=100;


//                    if(current>=advListData.size()){
//                        current = 0;
//                    }else{
//                        current++;
//                    }
                        if(adapter != null){
                            if(adapter.viewPager != null){
                                int item = adapter.viewPager.getCurrentItem();
                                item++;

                                adapter.viewPager.setCurrentItem(item);
                            }
                        }
                        handler.sendMessageDelayed(message, 4000);
                    }
                    if(msg.what == 15){
                        Intent intent = new Intent(getActivity(), ShowFristGuideActivity.class);
                        startActivity(intent);
                    }
                    if(msg.what == 16){
                        Intent intent = new Intent(getActivity(), KongBaiActivity.class);
                        startActivity(intent);
                    }

                    if(msg.what == 20){

                    }

                    if(msg.what == 200){
                        try {
                            json = (String)msg.obj;
                            JSONObject jsonObject = new JSONObject(json);
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
                                                data.prvCode = object.getString("prvCode");
                                            } catch (JSONException a) {

                                            } catch (Exception e) {

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

                }catch (Exception e){
                    nodata.setVisibility(View.VISIBLE);
                }
            }
        }

    FirstFragment.MyHandler handler = new FirstFragment.MyHandler(this);

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(scrollBanner != null){
            scrollBanner.setList(zixuanlist);
        }

        MobclickAgent.onPageStart("首页");
        if(height == 0){
            banner_top.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    banner_top.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    height = banner_top.getHeight();
                }
            });
        }
        city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        if(city.equals("全国") || city.equals("包头") || city.equals("临沂")){
            city = "潍坊";
            channelid = "1";
            code="8006";
        }
        if(city != null && !city.equals("")){
            nice_spinner.setText(city);
            nice_spinner1.setText(city);
            if(currentChannel != null){
                if(!currentChannel.nativeplace.equals(city)){
                    for(int i=0; i< MainActivity.columnButton.size();i++){
                        PlaceChannel item = MainActivity.columnButton.get(i);
                        if(item.nativeplace.contains(city)){
                            currentChannel = item;
                            channelid = item.channelid;
                            code=item.code;
                            break;
                        }
                    }
                    //处理三张图的情况
                    handlerThreeCase();
                    //处理四张图的情况
                    handlerFourCase();
                    //处理五张图的情况
                    handlerFiveCase();
                    if(tianxuqiu_bg.getVisibility() == View.VISIBLE){
                        oneImage_line.setVisibility(View.GONE);
                    }
                    getjsons();
                }
            }

        }
        if(!SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, false)){

            if(MyApplication.currentCity != null && !MyApplication.currentCity.equals("")){
                for(int i=0; i<MyApplication.cityId.size();i++){
                    final String city1 = MyApplication.cityId.get(i);
                    if(MyApplication.currentCity.contains(city1)){
                        String current = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "北京");
                        if(!MyApplication.currentCity.contains(current)){
                            SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, true);
                            //弹出切换提示框
                            final BTAlertDialog dialog = new BTAlertDialog(getActivity());
                            dialog.setTitle("系统定位到"+MyApplication.currentCity+"，是否切回"+MyApplication.currentCity+"频道?");
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //切换频道刷新数据
                                    SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,city1);
                                    city=city1;
                                    ChannelDb.changeChannel(city1);
                                    Intent intent1 = new Intent();
                                    intent1.setAction("changetitles");
                                    getActivity().sendBroadcast(intent1);
                                    city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                                    if(city != null && !city.equals("")){
                                        nice_spinner.setText(city);
                                        if(MyApplication.model.equals("ZK-R322")){
                                            nice_spinner.setText("        "+city);
                                        }
                                        nice_spinner1.setText(city);
                                        if(currentChannel != null){
                                            if(!currentChannel.nativeplace.equals(city)){
                                                for(int i=0; i< MainActivity.columnButton.size();i++){
                                                    PlaceChannel item = MainActivity.columnButton.get(i);
                                                    if(item.nativeplace.contains(city)){
                                                        currentChannel = item;
                                                        channelid = item.channelid;
                                                        code=item.code;
                                                        break;
                                                    }
                                                }
                                                //处理三张图的情况
                                                handlerThreeCase();
                                                //处理四张图的情况
                                                handlerFourCase();
                                                //处理五张图的情况
                                                handlerFiveCase();
                                            }
                                        }

                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        break;
                    }
                }
            }
        }
        banner_top.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                banner_top.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = banner_top.getHeight();
                listView.setHeight(height);
            }
        });

    }

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

    private void ChangeXumu(){
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "3");
        map.put("typeid", "2");
        map.put("tag", "1");
        map.put("sortid", sortid);
        networkCom.getJson(url,map,handler,3,0);

    }

    private void ChangeZhuanJia(){
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "4");
        map.put("typeid", "4");
        map.put("tag", "1");
        map.put("sortid", sortid);
        networkCom.getJson(url,map,handler,4,0);
    }
    private void ChangeZhuanli(){
        String url = "http://"+ MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize","4");
        map.put("typeid","5");
        map.put("tag","1");
        map.put("sortid",sortid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,5,0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverBroad);
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

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
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum1_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                five_cloum1_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum1_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(3);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                five_cloum1_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(4);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(3);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                five_cloum2_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(4);
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
                        handleronClick(type, currentColumnData,channelid);
                    }
                });
                four_cloum1_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });

                four_cloum1_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                four_cloum1_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column1.get(3);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                four_cloum2_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(0);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                four_cloum2_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(1);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });


                four_cloum2_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(2);
                        type=currentColumnData.type;
                        handleronClick(type, currentColumnData,channelid);
                    }
                });



                four_cloum2_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColumnData= currentChannel.column2.get(3);
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
//                        channelid = currentColumnData.channelid;
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

    class ReceiverBroad extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("downok")){
                for(int i=0; i< MainActivity.columnButton.size();i++){
                    PlaceChannel item = MainActivity.columnButton.get(i);
                    if(item.nativeplace.contains(city)){
                        currentChannel = item;
                        channelid = item.channelid;
                        code=item.code;
                        break;
                    }
                }
                //处理三张图的情况
            handlerThreeCase();
                //处理四张图的情况
            handlerFourCase();
                //处理五张图的情况
            handlerFiveCase();
            getjsons();

            }

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
    private void handleronClick(String type, ColumnData data,String channelid){
        if(data.type.equals("tagplace")){
            Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("typeid", data.typeid);
            intent.putExtra("cityCode",data.cityCode);
            intent.putExtra("cityName","");
            intent.putExtra("prvName","");
            intent.putExtra("name", "");
            intent.putExtra("type","");
            intent.putExtra("tag",data.tag);
            intent.putExtra("channelid", data.channelid);
            intent.putExtra("city", city);
            startActivity(intent);
        }else if(data.type.equals("openMiniProgram")){
            openMinPro(data);
        }else if(type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), ZhuanLiShenQing.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                startActivity(intent);
            }

        }else if(type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if(type.equals("html")){
             Intent intent=new Intent(getActivity(), ActiveActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
             intent.putExtra("title", data.title);
             intent.putExtra("url", data.jumpUrl);
             startActivity(intent);
        }else if(type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if(type.equals("QualityProject")){
            Intent intent = new Intent(getActivity(), NewJingPinProject.class);
            startActivity(intent);
        }else if(type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if(type.equals("place")){
             if(data.typeid.equals("1")){
                    Intent intent = new Intent(getActivity(), DFInfoShow.class);
                    intent.putExtra("typeid", data.typeid);
                    intent.putExtra("cityCode",code);
                    intent.putExtra("cityName",data.cityName);
                    intent.putExtra("prvName",data.prvName);
                    intent.putExtra("name", data.name);
                    intent.putExtra("type",data.type);
                    intent.putExtra("channelid", channelid);
                    String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                    intent.putExtra("city", city);
                    intent.putExtra("prvCode", data.prvCode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                    intent.putExtra("city", city);
                    startActivity(intent);
             }else if(data.typeid.equals("11")){
                    Intent intent = new Intent(getActivity(), DFInfoShow.class);
                    intent.putExtra("typeid", data.typeid);
                    intent.putExtra("cityCode",code);
                    intent.putExtra("cityName",data.cityName);
                    intent.putExtra("prvName",data.prvName);
                    intent.putExtra("name", data.name);
                    intent.putExtra("type",data.type);
                    intent.putExtra("prvCode", data.prvCode);
                    intent.putExtra("channelid",channelid);
                 intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                    intent.putExtra("city", city);
                    startActivity(intent);
             }else if(data.typeid.equals("xr")){
                 Intent intent = new Intent(getActivity(), DFInfoShow.class);
                 String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                 intent.putExtra("typeid", data.typeid);
                 intent.putExtra("cityCode",code);
                 intent.putExtra("cityName",data.cityName);
                 intent.putExtra("prvName",data.prvName);
                 intent.putExtra("name", data.name);
                 intent.putExtra("type",data.type);
                 intent.putExtra("prvCode", data.prvCode);
                 intent.putExtra("channelid",channelid);
                 intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                 intent.putExtra("city", city);
                 startActivity(intent);
             }
        }else{
            Toast.makeText(getActivity(),"功能建设中，请等待升级",Toast.LENGTH_SHORT).show();
        }
    }

    private void getAdsJson(){
        String url="http://"+MyApplication.ip+"/api/arc_advertising_banner.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,null,handler,8,0);
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

//    public void setListBottomHeight(int height){
//        try {
//            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams)listView.getLayoutParams();
//            params.bottomMargin=height;
//        }catch (Exception e){
//        }
//
//
//    }

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

                final ADS ads = advListData.get(index);

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
                                intent.putExtra("title",ads.getTitle());
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
                            intent.putExtra("id",ads.getAid());
                            intent.putExtra("name",ads.getTypename());
                            intent.putExtra("pic",ads.getPicUrl());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void doubleClick() {

                    }
                }));

//                mHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(ads != null && ads.type != null && ads.type.equals("html")
//                                && ads.url != null && !ads.url.equals("")){
//                            //增加mid参数 todoby wyy
//                            tongji(ads.id);
//                            Log.d("lizisong", "ads.id:"+ads.id);
//                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
//                            if (!mid.equals("0")){
//                                Intent intent=new Intent(getActivity(), ActiveActivity.class);
//                                intent.putExtra("title",ads.getTitle());
//                                intent.putExtra("url", ads.url+"?mid="+mid);
//                                startActivity(intent);
//
//                            } else {
//
//                                Intent intent=new Intent(getActivity(), ActiveActivity.class);
//                                intent.putExtra("url", ads.url);
//                                startActivity(intent);
//                            }
//
//                        }else{
//                            Log.d("lizisong", "ads.id:"+ads.id);
//                            tongji(ads.id);
//                            Intent intent=new Intent(getActivity(),ZixunDetailsActivity.class);
//                            intent.putExtra("id",ads.getAid());
//                            intent.putExtra("name",ads.getTypename());
//                            intent.putExtra("pic",ads.getPicUrl());
//                            startActivity(intent);
//                        }
//                    }
//
//                });

//                mHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if(event.getAction() == MotionEvent.ACTION_DOWN){
//                            handler.removeMessages(100);
////                            Log.d("lizisong", "ACTION_DOWN");
//                        }else if(event.getAction() == MotionEvent.ACTION_UP){
////                            Log.d("lizisong", "ACTION_UP");
//                            handler.removeMessages(100);
//                            handler.sendEmptyMessageDelayed(100, 4000);
//                        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
////                            Log.d("lizisong", "ACTION_MOVE");
//                            handler.removeMessages(100);
//                            handler.sendEmptyMessageDelayed(100, 4000);
//                        }
//                        return false;
//                    }
//                });
                container.addView(converView);

            }catch (IllegalStateException exx){}
            catch (NumberFormatException ex){}
            catch ( Exception e){}
            return  converView;

        }

        private class ViewHolder{
            public ImageView imageView;
        }
//        public void releaseImageViewResouce(ImageView imageView) {
//            try {
//                if (imageView == null) return;
//                Drawable drawable = imageView.getDrawable();
//                if (drawable != null && drawable instanceof BitmapDrawable) {
//                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    if (bitmap != null && !bitmap.isRecycled()) {
//                        bitmap.recycle();
//                        bitmap=null;
//                    }
//                }
//                System.gc();
//            }catch (Exception e){
//
//            }
//
//        }


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


    public void ICONJson() {
       if( MainActivity.columnButton.size() ==0){
        String url = "http://"+MyApplication.ip+"/api/indexChannel_2_5.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,null,handler,200,0);
       }
    }
//    View.OnClickListener listener1 = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(ViewClick.isFastClick()){
//                return;
//            }
//            Log.d("lizisong", "执行0000");
//            currentColumnData= currentChannel.column1.get(0);
//            type=currentColumnData.type;
////                        channelid = currentColumnData.channelid;
//            handleronClick(type, currentColumnData,channelid);
//        }
//    };

    public boolean isVisible= false;

    public void setVisible(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
    }
}
