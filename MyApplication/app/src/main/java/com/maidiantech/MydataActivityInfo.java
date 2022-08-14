package com.maidiantech;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Util.BitmapUtil;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import application.MyApplication;
import dao.Service.MainDianIcon;
import entity.Codes;
import entity.Datas;
import entity.SmsRegisData;
import entity.Url;
import entity.Usercode;
import entity.userlogin;
import view.BTAlertDialog;
import view.RoundImageView;
import view.RoundImageView2;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/7.
 */
public class MydataActivityInfo extends AutoLayoutActivity implements View.OnClickListener {
    ImageView mydataback;
    TextView mCity;
    TextView personname;
    TextView city;
    TextView personphone, data_text;
    LinearLayout data_phone, info_name;
    EditText personemail, yzm;
    RoundImageView2 person_tx;
    Button infosubmit, get_yzm;
    String pname, pphone, pemail;
    SharedPreferences qqspf;
    SharedPreferences weixpf;
    String tel, nickname, email, img, yzphone, yzregiststr, registlogin, smsCode = "";
    private int million = 60;
//    // 省数据集合
//    private ArrayList<String> mListProvince = new ArrayList<String>();
//    // 市数据集合
//    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
//    // 区数据集合
//    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();

    //    private OptionsPickerView<String> mOpv;
//    private JSONObject mJsonObj;
//    LinearLayout fromname;
//    TextView from_home;
    AlertDialog dialog;
    //    String[] sexArry = new String[] { "企业用户", "科研人员","其他" };
//    private HashMap<String,String> infomap = new HashMap<>();
//    private String infomid;
    OkHttpUtils utils;
    private String inforjson;
    private PopupWindow mPopBottom;
    private Bitmap bitmap;

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final int PERMISSIONS_FOR_TAKE_PHOTO = 10;
    //拍照对应RequestCode
    public static final int SELECT_PIC_BY_TACK_PHOTO = 11;
    //裁剪图片
    private static final int CROP_PICTURE = 12;

    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photos.jpg";
    /* 输出名称 */
    private static final String PHOTO_FILE_NAME1 = "temp_photos1.jpg";
    private File tempFile, tempFile1;

    //    protected static final int CHOOSE_PICTURE = 0;
//    protected static final int TAKE_PICTURE = 1;
//    private static final int CROP_SMALL_PICTURE = 2;
//    protected static Uri tempUri;
    MainDianIcon mainDianIcon;
    private String mid, state;
    private Datas data;
    private TextView info_weixin, info_qq;
    private LinearLayout bang_weix, bang_qq;
    SHARE_MEDIA platform;
    UMShareAPI mShareAPI;
    HashMap<String, String> hashmap;
    //    HashMap<String,String> hashmapjie;
    public static boolean loginState = false;
    private String qqlogin;
    private String ips;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_data);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        loginState = false;
        if (null == mShareAPI) {
            mShareAPI = UMShareAPI.get(this);
        }
        mainDianIcon = MainDianIcon.getInstance(this);
        ips = MyApplication.ip;
        initView();
        String url1 = SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "0");
        if (url1 != null && !url1.equals("0") && !url1.equals("")) {
            ImageLoader.getInstance().displayImage(url1, person_tx);
        } else {

        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    private void initView() {
        mydataback = (ImageView) findViewById(R.id.mydata_back);
        personname = (TextView) findViewById(R.id.person_name);
        mCity = (TextView) findViewById(R.id.city);
        personphone = (TextView) findViewById(R.id.person_phone);
//        personemail = (EditText) findViewById(R.id.person_email);
        infosubmit = (Button) findViewById(R.id.info_submit);
        person_tx = (RoundImageView2) findViewById(R.id.person_tx);
        data_text = (TextView) findViewById(R.id.data_text);
        data_phone = (LinearLayout) findViewById(R.id.data_phone);
        info_name = (LinearLayout) findViewById(R.id.info_name);
        info_weixin = (TextView) findViewById(R.id.info_weixin);
        info_qq = (TextView) findViewById(R.id.info_qq);
        get_yzm = (Button) findViewById(R.id.get_yzm);
        yzm = (EditText) findViewById(R.id.yzm);
        bang_weix = (LinearLayout) findViewById(R.id.bang_weix);
        bang_qq = (LinearLayout) findViewById(R.id.bang_qq);
        bang_weix.setOnClickListener(this);
        bang_qq.setOnClickListener(this);
        info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MydataActivityInfo.this, PageInfoActivity.class);
                startActivity(intent);
            }
        });
        person_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                showFullPop();

            }
        });
        mydataback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKbTwo();
                finish();

            }
        });

    }

    private UMAuthListener umAuthListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> info) {
            // 获取uid
            if (platform == SHARE_MEDIA.QQ) {
                String uid = info.get("uid");
                if (!TextUtils.isEmpty(uid)) {
                    // uid不为空，获取用户信息
                    getUserInfo(platform);
                } else {
                    Toast.makeText(MydataActivityInfo.this, "授权失败...",
                            Toast.LENGTH_LONG).show();
                }
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                String unionid = info.get("unionid");
                Log.d("lizisong", "unionid:"+unionid);
                if (!TextUtils.isEmpty(unionid)) {
                    getwxinfo(platform);
                }else{
                    UIHelper.hideDialogForLoading();
                    Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UIHelper.hideDialogForLoading();
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UIHelper.hideDialogForLoading();
            Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();

        }
    };

    private void getUserInfo(SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(MydataActivityInfo.this, platform, new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                if (map.get("profile_image_url").toString() != null) {
                    String timestamp = System.currentTimeMillis() + "";
                    String sign = "";
                    ArrayList<String> sort = new ArrayList<String>();
                    qqspf = getSharedPreferences("lognqq", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = qqspf.edit();
                    edt.putString("openid", map.get("openid").toString());//                            edt.putString("uid", map.get("uid").toString());
                    edt.putBoolean("flag", true);
                    edt.commit();
                    String opend = qqspf.getString("openid", "");
                    mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                    hashmap = new HashMap<>();
                    hashmap.put("mid", mid);
                    hashmap.put("openid", opend);
                    hashmap.put("type", "qq");
                    hashmap.put("action", "bangding");
                    sort.add("mid" + mid);
                    sort.add("openid" + opend);
                    sort.add("type" + "qq");
                    sort.add("action" + "bangding");
                    sort.add("timestamp" + timestamp);
                    String accessid = "";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if (loginState.equals("1")) {
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        accessid = mid;
                    } else {
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid" + accessid);
                    sign = KeySort.keyScort(sort);
                    hashmap.put("sign", sign);
                    hashmap.put("timestamp", timestamp);
                    hashmap.put("accessid", accessid);
                    UIHelper.hideDialogForLoading();
                    getjson();
                }
            }


            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    private void getwxinfo(SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(MydataActivityInfo.this, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                if (map.get("profile_image_url").toString() != null) {
                    weixpf = getSharedPreferences("lognwx", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = weixpf.edit();
                    edt.putString("openid", map.get("openid").toString());

                    //                            edt.putString("uid", map.get("uid").toString());
                    edt.putBoolean("flag", true);
                    edt.commit();

                    String timestamp = System.currentTimeMillis() + "";
                    String sign = "";
                    ArrayList<String> sort = new ArrayList<String>();

                    mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                    String opend = weixpf.getString("openid", "");
                    hashmap = new HashMap<>();
                    hashmap.put("mid", mid);
                    hashmap.put("openid", opend);
                    hashmap.put("type", "weixin");
                    hashmap.put("action", "bangding");
                    hashmap.put("version", MyApplication.version);
                    sort.add("mid" + mid);
                    sort.add("openid" + opend);
                    sort.add("type" + "weixin");
                    sort.add("action" + "bangding");
                    sort.add("timestamp" + timestamp);
                    sort.add("version" + MyApplication.version);
                    String accessid = "";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if (loginState.equals("1")) {
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        accessid = mid;
                    } else {
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid" + accessid);
                    sign = KeySort.keyScort(sort);
                    hashmap.put("sign", sign);
                    hashmap.put("timestamp", timestamp);
                    hashmap.put("accessid", accessid);
//                    UIHelper.hideDialogForLoading();
                    Log.d("lizisong", "填写参数");
//                    UIHelper.showDialogForLoading(MyloginActivity.this, "", true);
//                    progressBar.setVisibility(View.VISIBLE);
                    getjson();
                }
                //   }else{
                //   setResult(2, getIntent());
                //  MyloginActivity.this.finish();
                //  }
            }


            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    public void getjson() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //String str="http://123.207.164.210/api/thirdLogin.php";
                    String str = "http://" + ips + "/api/user_third.php";
                    qqlogin = OkHttpUtils.post(str, hashmap);
                    loginState = false;
                    Thread.sleep(2000);
//                    UIHelper.hideDialogForLoading();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void jiebangjson() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //String str="http://123.207.164.210/api/thirdLogin.php";
                    String str = "http://" + ips + "/api/user_third.php";
                    qqlogin = OkHttpUtils.post(str, hashmap);
                    loginState = false;
                    Thread.sleep(2000);
//                    UIHelper.hideDialogForLoading();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showFullPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_crema,
                null);
        LinearLayout layout_all;
        RelativeLayout layout_choose;
        RelativeLayout layout_photo;
        RelativeLayout layout_cancel;
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_cancel = (RelativeLayout) view.findViewById(R.id.layout_cancel);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                // 判断存储卡是否可以用，可用进行存储
//                if (hasSdcard()) {
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(new File(Environment
//                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
//                }
//                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                camera();
                mPopBottom.dismiss();
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                mPopBottom.dismiss();


            }
        });
        layout_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mPopBottom.dismiss();
            }
        });
        mPopBottom = new PopupWindow(view);
        mPopBottom.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        mPopBottom.setTouchable(true);
        mPopBottom.setFocusable(true);
        mPopBottom.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        mPopBottom.setBackgroundDrawable(dw);
        // 动画效果 从底部弹起
//        dialogBootom2UpAnimation
        mPopBottom.setAnimationStyle(R.style.Animations_GrowFromBottom);
        mPopBottom.showAtLocation(person_tx, Gravity.BOTTOM, 0, 0);//parent view随意
    }

    //    private void infojson() {
//
//        pname=  personname.getText().toString().trim();
//        pphone=personphone.getText().toString().trim();
////        pemail=personemail.getText().toString().trim();
//        tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
//        nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "");
//        img=SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG,"");
////            nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "");
//        email = SharedPreferencesUtil.getString(SharedPreferencesUtil.EMAIL, "");
////        if(!pphone.equals(tel)){
////            String codes=yzm.getText().toString();
////            if(codes==null || codes.equals("")){
////                Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
////                return;
////            }
////            if(!codes.equals(smsCode)){
////                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
////                return;
////            }
////        }
//        if(pname.equals(nickname)  && pphone.equals(tel)){
//            Toast.makeText(this, "您没有修改信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
//            utils= OkHttpUtils.getInstancesOkHttp();
//            try {
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        String registstr="http://"+ips+"/api/user_edit.php";
//                        inforjson = OkHttpUtils.postkeyvlauspainr(registstr, infomap);
//
//                        Message msg=new Message();
//                        msg.what=1;
//                        handler.sendMessage(msg);
//
//                    }
//                }.start();
//            }catch (Exception e){}
//
//
//    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            tel = SharedPreferencesUtil.getString(SharedPreferencesUtil.TEL, "");
            String mtype = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_TYPE, "0");
            state = SharedPreferencesUtil.getString(SharedPreferencesUtil.WQ_STATE, "");
            nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "");
//            if (mtype.equals("企业")) {
//                nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_COM, "");
//            }
//            nickname = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_NAME, "");
            email = SharedPreferencesUtil.getString(SharedPreferencesUtil.EMAIL, "");
            img = SharedPreferencesUtil.getString(SharedPreferencesUtil.IMG, "");
            if (state == null || state.equals("") || state.equals("0")) {
                info_weixin.setText("未绑定");
                info_weixin.setTextColor(0xFF969696);
                info_qq.setText("未绑定");
                info_qq.setTextColor(0xFF969696);
            } else if (state.equals("1")) {
                info_weixin.setText("已绑定");
                info_qq.setText("未绑定");
                info_qq.setTextColor(0xFF969696);
                info_weixin.setTextColor(0xFF666666);
            } else if (state.equals("2")) {
                info_qq.setText("已绑定");
                info_weixin.setText("未绑定");
                info_qq.setTextColor(0xFF666666);
                info_weixin.setTextColor(0xFF969696);
            } else if (state.equals("3")) {
                info_weixin.setText("已绑定");
                info_qq.setText("已绑定");
                info_qq.setTextColor(0xFF666666);
                info_weixin.setTextColor(0xFF666666);
            }
            personname.setText(nickname);
//            personphone.setText(tel);
            if (!TextUtils.isEmpty(tel) && tel.length() > 6) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < tel.length(); i++) {
                    char c = tel.charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }

                personphone.setText(sb.toString());
            }
//            personemail.setText(email);

//            BitmapUtil.upUserImageData(person_tx,mainDianIcon);

        } catch (Exception e) {
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                if (million > 0) {
                    million--;
                    get_yzm.setClickable(false);
                    get_yzm.setTextSize(15);
                    get_yzm.setText(million + "秒内输入");
                    handler.sendEmptyMessageDelayed(2, 1500);
                }
                if (million == 0) {
                    get_yzm.setClickable(true);
                    get_yzm.setText("获取验证码");
                    get_yzm.setTextColor(Color.parseColor("#00aced"));
                    million = 60;
                    handler.removeCallbacksAndMessages(null);
                }
            }
            if (msg.what == 8) {
                try {

                    Gson gs = new Gson();
                    SmsRegisData json_data = gs.fromJson(registlogin, SmsRegisData.class);
                    if (json_data.code.equals("1")) {
                        smsCode = json_data.data.code;
                        setSendBt();
//                        Toast.makeText(YanzhenActivity.this, smsCode,Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MydataActivityInfo.this, json_data.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {

                }
            }
            if (msg.what == 1) {
                Gson g = new Gson();
                Codes codes = g.fromJson(inforjson, Codes.class);
                data = codes.getData();
                if (codes.code == 1) {
                    Toast.makeText(MydataActivityInfo.this, codes.message, Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.nickname);
//                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.uname);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                    finish();

                } else if (codes.code == -1) {
                    Toast.makeText(MydataActivityInfo.this, codes.message, Toast.LENGTH_SHORT).show();
                }

            }
            if (msg.what == 0) {
                Gson g = new Gson();
                Log.d("lizisong", "qqlogin:"+qqlogin);
                UIHelper.hideDialogForLoading();
                Usercode qqcode = g.fromJson(qqlogin, Usercode.class);
                if (qqcode != null && qqcode.getCode() == 1) {
                    final userlogin data = qqcode.getData();
                    String loginFlag = data.getLoginFlag();
                    MainActivity.xuqiucount = data.num;

                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, qqcode.getCode() + "");
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, data.getUsername());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, data.getNickname());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, data.email);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, data.getMid());
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, data.mtype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, data.company);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, data.address);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, data.mobile);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, data.linkman);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, data.product);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, data.tel);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, data.vocation);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, data.img);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, data.wq_num);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, data.ctype);
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, data.profession);
                    state = SharedPreferencesUtil.getString(SharedPreferencesUtil.WQ_STATE, "");
                    if (state == null || state.equals("") || state.equals("0")) {
                        info_weixin.setText("未绑定");
                        info_qq.setText("未绑定");
                    } else if (state.equals("1")) {
                        info_weixin.setText("已绑定");
                        info_qq.setText("未绑定");
                    } else if (state.equals("2")) {
                        info_qq.setText("已绑定");
                        info_weixin.setText("未绑定");
                    } else if (state.equals("3")) {
                        info_weixin.setText("已绑定");
                        info_qq.setText("已绑定");
                    }
                } else if (qqcode.getCode() == -1) {
                    Toast.makeText(MydataActivityInfo.this, qqcode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (msg.what == 3) {
                Gson g = new Gson();
                UIHelper.hideDialogForLoading();
                Usercode qqcode = g.fromJson(qqlogin, Usercode.class);
                if (qqcode != null && qqcode.getCode() == 1) {
                    final userlogin data = qqcode.getData();
                    String loginFlag = data.getLoginFlag();
                    MainActivity.xuqiucount = data.num;
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE, data.wq_num);
                    state = SharedPreferencesUtil.getString(SharedPreferencesUtil.WQ_STATE, "");
                    if (state == null || state.equals("") || state.equals("0")) {
                        info_weixin.setText("未绑定");
                        info_qq.setText("未绑定");
                    } else if (state.equals("1")) {
                        info_weixin.setText("已绑定");
                        info_qq.setText("未绑定");
                    } else if (state.equals("2")) {
                        info_qq.setText("已绑定");
                        info_weixin.setText("未绑定");
                    } else if (state.equals("3")) {
                        info_weixin.setText("已绑定");
                        info_qq.setText("已绑定");
                    }
                } else if (qqcode.getCode() == -1) {
                    Toast.makeText(MydataActivityInfo.this, qqcode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (msg.what == 10) {
                BitmapUtil.upUserImageData(person_tx, mainDianIcon);
            }

        }
    };

    private void setSendBt() {
        //设置发送按钮的变化
        get_yzm.setClickable(false);
        get_yzm.setText("60秒内输入");
        get_yzm.setTextSize(13);
        get_yzm.setTextColor(Color.parseColor("#00aced"));
        handler.sendEmptyMessageDelayed(2, 1500);
    }
    int digree=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
//        if(data != null){
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
           if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
                     String[] pojo = {MediaStore.Images.Media.DATA};
                      Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                      if (cursor != null) {
                           int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                             cursor.moveToFirst();
                             picPath = cursor.getString(columnIndex);
                             if (Build.VERSION.SDK_INT < 14) {
                                   cursor.close();
                              }
                        }
                 if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
                     photoUri = Uri.fromFile(new File(picPath));
                     Log.d("lizisong","photoUri:"+photoUri);
                     digree=readPictureDegree(picPath);

                     Log.d("lizisong", "degree:"+digree);
                     if (Build.VERSION.SDK_INT > 23) {
                           photoUri = FileProvider.getUriForFile(this, "com.maidiantech.fileprovider", new File(picPath));
//                           crop(photoUri, Uri.fromFile(tempFile1));
                           cropForN(picPath, CROP_PICTURE);
                         } else {
//                           crop(photoUri, Uri.fromFile(tempFile1));
                           startPhotoZoom(photoUri, CROP_PICTURE);
                         }
                     } else {
                     //错误提示
                    }
                 }
             if (requestCode == CROP_PICTURE) {
                 Log.d("lizisong", "gogogogogogogogo");
                 if (photoUri != null) {

                     Bitmap bitmap = BitmapFactory.decodeFile(picPath);
//                     Log.d("lizisong", "degree:"+degree);
                     if(digree != 0){
                        bitmap= rotaingImageView(digree,bitmap);
                     }
                     if (bitmap != null) {
                         mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                         ByteArrayOutputStream stream = new ByteArrayOutputStream();
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                         final byte[] b = stream.toByteArray();
//                           mainDianIcon.updata(b, mid);
                         this.person_tx.setImageBitmap(bitmap);
//                           BitmapUtil.upUserImageData(person_tx,mainDianIcon);
                         new Thread() {
                             public void run() {
                                 try {
                                     MyApplication.setAccessid();
                                     String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                     String timestamp = System.currentTimeMillis() + "";
                                     String sign = "";
                                     ArrayList<String> sort = new ArrayList<String>();
                                     sort.add("mid" + mid);
                                     sort.add("timestamp" + timestamp);
                                     sort.add("version" + MyApplication.version);
                                     String accessid = "";
                                     String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                     if (loginState.equals("1")) {

                                         accessid = mid;
                                     } else {
                                         accessid = MyApplication.deviceid;
                                     }
                                     sort.add("accessid" + accessid);
                                     sign = KeySort.keyScort(sort);

                                     String url = "http://" + ips + "/api/uploadUserImg.php?mid=" + mid + "&sign=" + sign + "&timestamp=" + timestamp + "&version=" + MyApplication.version + MyApplication.accessid;
                                     uploadFile(b, url);
//                                     tempFile = new File(Environment.getExternalStorageDirectory(),
//                                             PHOTO_FILE_NAME);
//                                     if (tempFile != null) {
//                                         if (tempFile.exists()) {
//                                             tempFile.delete();
//                                         }
//                                     }
                                     Message msg = Message.obtain();
                                     msg.what = 10;
                                     handler.sendMessage(msg);
                                 } catch (Exception e) {

                                 }
                             }

                         }.start();
                        }
                     }

                 }


            }




        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                tempFile1 = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME1);
                crop(uri, Uri.fromFile(tempFile1));
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                tempFile1 = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME1);

                crop(Uri.fromFile(tempFile), Uri.fromFile(tempFile1));

            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (Uri.fromFile(tempFile1) != null) {
                bitmap = decodeUriAsBitmap(Uri.fromFile(tempFile1));
            }
//                   if(data != null) {
//                       bitmap = data.getParcelableExtra("data");

            if (bitmap != null) {
                mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final byte[] b = stream.toByteArray();
//                           mainDianIcon.updata(b, mid);
                this.person_tx.setImageBitmap(bitmap);
//                           BitmapUtil.upUserImageData(person_tx,mainDianIcon);
                new Thread() {
                    public void run() {
                        try {
                            MyApplication.setAccessid();
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            String timestamp = System.currentTimeMillis() + "";
                            String sign = "";
                            ArrayList<String> sort = new ArrayList<String>();
                            sort.add("mid" + mid);
                            sort.add("timestamp" + timestamp);
                            sort.add("version" + MyApplication.version);
                            String accessid = "";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (loginState.equals("1")) {

                                accessid = mid;
                            } else {
                                accessid = MyApplication.deviceid;
                            }
                            sort.add("accessid" + accessid);
                            sign = KeySort.keyScort(sort);

                            String url = "http://" + ips + "/api/uploadUserImg.php?mid=" + mid + "&sign=" + sign + "&timestamp=" + timestamp + "&version=" + MyApplication.version + MyApplication.accessid;
                            uploadFile(b, url);
                            tempFile = new File(Environment.getExternalStorageDirectory(),
                                    PHOTO_FILE_NAME);
                            if (tempFile != null) {
                                if (tempFile.exists()) {
                                    tempFile.delete();
                                }
                            }
                            Message msg = Message.obtain();
                            msg.what = 10;
                            handler.sendMessage(msg);
                        } catch (Exception e) {

                        }
                    }

                }.start();
            }
//                   }

        }
//        }
    }

    /**
     * 剪切图片
     *
     * @param uri
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     */
    private void crop(Uri uri, Uri uri1) {
        // 裁剪图片意图
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("output", uri1);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    //图片文件路径
    private String picPath;
    //图片对应Uri
    private Uri photoUri;


    /**
     * 拍照获取图片
     */
    private void takePictures() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "手机未插入内存卡", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 安卓6.0以上版本权限处理
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_FOR_TAKE_PHOTO);
        } else {
            takePictures();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_FOR_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictures();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 图片裁剪，参数根据自己需要设置
     *
     * @param uri
     * @param REQUE_CODE_CROP
     */
    private void startPhotoZoom(Uri uri,
                                int REQUE_CODE_CROP) {
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * 7.0以上版本图片裁剪操作
     *
     * @param imagePath
     * @param REQUE_CODE_CROP
     */
    private void cropForN(String imagePath, int REQUE_CODE_CROP) {
        Uri cropUri = getImageContentUri(new File(imagePath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");
        intent.putExtra("crop", "true");
        //输出是X方向的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUE_CODE_CROP);
    }



    /**
     * 从相机获取
     */
    public void camera() {
        //小于6.0版本直接操作
        if (Build.VERSION.SDK_INT < 23) {
            takePictures();
        } else {
            //6.0以后权限处理
            permissionForM();
        }
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//        Uri fileUri = getOutputMediaFileUri();
//        imagePath = fileUri.getPath().substring(15);
//        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        ((Activity) this.getApplicationContext()).startActivityForResult(i, FROM_CAMERA);

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            getGLESTextureLimitEqualAboveLollipop();
//        }


//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},222);
//                return;
//            }else{

//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            // 判断存储卡是否可以用，可用进行存储
//            tempFile = new File(Environment.getExternalStorageDirectory(),
//                    PHOTO_FILE_NAME);
//            if (tempFile != null) {
//                if (tempFile.exists()) {
//                    tempFile.delete();
//                }
//            }
//            if (hasSdcard()) {
//
//                Uri imageUri = getOutputMediaFileUri();
//                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            }
//            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
////            }
//        } else {
//
////            openCamra();//调用具体方法
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            // 判断存储卡是否可以用，可用进行存储
//            tempFile = new File(Environment.getExternalStorageDirectory(),
//                    PHOTO_FILE_NAME);
//            if (tempFile != null) {
//                if (tempFile.exists()) {
//                    tempFile.delete();
//                }
//            }
//            if (hasSdcard()) {
//                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME));
//                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            }
//            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
//        }

    }

    private static final String TAG = "lizisong";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static String laboratory;

    public static int uploadFile(byte[] file, String RequestURL) {
        int res = 0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + PHOTO_FILE_NAME + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while ((len = is.read(bytes)) != -1) {
                dos.write(file, 0, file.length);
//                }
//                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                res = conn.getResponseCode();
                //  Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    //    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    JSONObject jo;
                    try {
                        jo = new JSONObject(result);
                        laboratory = jo.getString("code");
                        String data = jo.getString("data");
                        if (laboratory.equals("1")) {
                            JSONObject item = new JSONObject(data);
                            String imgurl = item.getString("img");
                            SharedPreferencesUtil.putString(SharedPreferencesUtil.IMG, imgurl);
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    // Log.e(TAG, "result : " + result);
                } else {
                    // Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bang_weix:
                state = SharedPreferencesUtil.getString(SharedPreferencesUtil.WQ_STATE, "");
                if (state == null || state.equals("") || state.equals("0") || state.equals("2")) {
                    final BTAlertDialog dialog = new BTAlertDialog(MydataActivityInfo.this);
                    dialog.setTitle("您确定要绑定微信么？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            UIHelper.showDialogForLoading(MydataActivityInfo.this, "", true);
                            platform = SHARE_MEDIA.WEIXIN;
                            Log.d("lizisong", "bang_weix");
                            boolean isauth=UMShareAPI.get(MydataActivityInfo.this).isAuthorize(MydataActivityInfo.this, SHARE_MEDIA.WEIXIN);
                            if(isauth){
                                UMShareAPI.get(MydataActivityInfo.this).deleteOauth(MydataActivityInfo.this,SHARE_MEDIA.WEIXIN,authListener);
                            }else{
//                        progressBar.setVisibility(View.VISIBLE);
                                mShareAPI.doOauthVerify(MydataActivityInfo.this, platform, umAuthListener);
                            }
                        }
                    });
                    dialog.show();

                } else if (state.equals("1") || state.equals("3")) {
                    final BTAlertDialog dialog = new BTAlertDialog(MydataActivityInfo.this);
                    dialog.setTitle("确定要解除帐号与微信的绑定么？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("解除绑定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            hashmap = new HashMap<>();
                            hashmap.put("mid", mid);
                            hashmap.put("type", "weixin");
                            hashmap.put("action", "jiebang");
                            hashmap.put("version", MyApplication.version);
                            ArrayList<String> sort = new ArrayList<String>();
                            String timestamp = System.currentTimeMillis() + "";
                            String sign = "";
                            sort.add("mid" + mid);
                            sort.add("type" + "weixin");
                            sort.add("action" + "jiebang");
                            sort.add("timestamp" + timestamp);
                            sort.add("version" + MyApplication.version);
                            String accessid = "";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (loginState.equals("1")) {
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");

                                accessid = mid;
                            } else {
                                accessid = MyApplication.deviceid;
                            }
                            sort.add("accessid" + accessid);
                            sign = KeySort.keyScort(sort);
                            hashmap.put("sign", sign);
                            hashmap.put("timestamp", timestamp);
                            hashmap.put("accessid", accessid);
                            jiebangjson();

                        }
                    });

                    dialog.show();
                }
                break;

            case R.id.bang_qq:

                state = SharedPreferencesUtil.getString(SharedPreferencesUtil.WQ_STATE, "");
                if (state == null || state.equals("") || state.equals("0") || state.equals("1")) {

//                    UIHelper.showDialogForLoading(MydataActivityInfo.this, "", true);
                    mShareAPI = UMShareAPI.get(this);
                    platform = SHARE_MEDIA.QQ;
                    mShareAPI.doOauthVerify(this, platform, umAuthListener);


                } else if (state.equals("2") || state.equals("3")) {
                    final BTAlertDialog dialog = new BTAlertDialog(MydataActivityInfo.this);
                    dialog.setTitle("确定要解除帐号与QQ的绑定么？");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("解除绑定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            ArrayList<String> sort = new ArrayList<String>();
                            String timestamp = System.currentTimeMillis() + "";
                            String sign = "";
                            hashmap = new HashMap<>();
                            hashmap.put("mid", mid);
                            hashmap.put("type", "qq");
                            hashmap.put("action", "jiebang");
                            sort.add("mid" + mid);
                            sort.add("type" + "qq");
                            sort.add("action" + "jiebang");
                            sort.add("timestamp" + timestamp);
                            String accessid = "";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if (loginState.equals("1")) {
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");

                                accessid = mid;
                            } else {
                                accessid = MyApplication.deviceid;
                            }
                            sort.add("accessid" + accessid);
                            sign = KeySort.keyScort(sort);
                            hashmap.put("sign", sign);
                            hashmap.put("timestamp", timestamp);
                            hashmap.put("accessid", accessid);
                            jiebangjson();
                        }
                    });

                    dialog.show();
                }
                break;
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            // 先通过getContentResolver方法获得一个ContentResolver实例，
            // 调用openInputStream(Uri)方法获得uri关联的数据流stream
            // 把上一步获得的数据流解析成为bitmap
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider
                    .getUriForFile(context.getApplicationContext(), "com.maidiantech.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private Uri getOutputMediaFileUri() {
//        return Uri.fromFile(getOutputMediaFile());
        return getUriForFile(this.getApplicationContext(), tempFile);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MydataActivityInfo Page") // TODO: Define a title for the content shown.
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

    public static Intent invokeSystemCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        intent.putExtra("scale", true);

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);

        File out = new File(getPath());
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        return intent;
    }
    private static String mFile;

    public static String getPath() {
        //resize image to thumb
        if (mFile == null) {
            mFile = Environment.getExternalStorageDirectory() + "/"   + "temp.jpg";
        }
        return mFile;
    }
    ////////////////////////////////////////////////////////////////////
    File file;
    String imgPathOri;
    Uri imgUriOri;
    String imgName="/"   + "temp.jpg";

    final int CAMERA_RESULT_CODE = 10;
    /**
     * 7.0以上获取裁剪 Uri
     *
     * @param imageFile
     * @return
     */
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 创建原图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "HomePic_" + new SimpleDateFormat(
                "yyyyMMdd_HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirOri       /* directory */
        );
        imgPathOri = image.getAbsolutePath();
        return image;
    }

    /**
     * 打开系统相机
     */
    private void openSysCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
//                new File(Environment.getExternalStorageDirectory(), imgName)));
//        File file = new File(Environment.getExternalStorageDirectory(), imgName);
        try {
            file = createOriImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imgUriOri = Uri.fromFile(file);
            } else {
                imgUriOri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
            startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
        }
    }

    /**
     * 打开系统相机6.0
     */
    private void openSysCamera6() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), imgName)));
        startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
    }


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException ex) {
            Log.d(TAG, "cannot read exif" + ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        Log.d(TAG, "degree:" + degree);
        return degree;

    }
    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Log.e("TAG","angle==="+angle);
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


    /**
     * 保存bitmap到sd卡filePath文件中 如果有，则删除
     * @param bitmap
     * @param filePath :图片绝对路径
     * @return boolean :是否成功
     */
    public static boolean saveBitmap2file(Bitmap bitmap,String filePath){
        if (bitmap==null){
            return false;
        }
        //压缩格式
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality =100;
        OutputStream stream=null;
        File file=new File(filePath);
        File dir=file.getParentFile();
        if (!dir.exists()){
            dir.mkdirs();//创建父目录
        }
        if (file.exists()){
            file.delete();
        }

        try {
            stream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  bitmap.compress(format,quality,stream);
    }


    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            progressBar.setVisibility(View.VISIBLE);
            mShareAPI.doOauthVerify(MydataActivityInfo.this, platform, umAuthListener);
//            notifyDataSetChanged();
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UIHelper.hideDialogForLoading();

            Toast.makeText(MydataActivityInfo.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UIHelper.hideDialogForLoading();
            Toast.makeText(MydataActivityInfo.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };
}




