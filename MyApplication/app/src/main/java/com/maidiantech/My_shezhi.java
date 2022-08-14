package com.maidiantech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Util.CacheFileInfo;
import Util.FolderInfo;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import application.DataCleanManager;
import application.MyApplication;
import dao.Service.MaiDianCollection;
import dao.Service.MaiDianYuYue;
import dao.Service.PulseCondtion;
import entity.Codes;
import entity.UpGrade;
import fragment.FirstFragment;
import fragment.WelcomePulse;
import view.BTAlertDialog;
import view.StyleUtils;
import view.StyleUtils1;
import view.SwitchButton;
import view.UpgradeAlertDialog;

/**
 * Created by 13520 on 2016/10/27.
 */
public class My_shezhi extends AutoLayoutActivity {
    private SwitchButton my_togbutton;
    private SwitchButton my_collects;
    private SwitchButton pic_whife;
    LinearLayout help;
    ImageView shezhi_back;
    LinearLayout msg_feedback;
    LinearLayout about,erweima;
    LinearLayout clean_data;
    LinearLayout upGrade;
    LinearLayout xiugaimima;
    TextView xiugaimima_line;
    TextView clean_txt;
    Button exit;
    String loginState = "";
    FolderInfo folderInfo;
    private long dataSize = 0l;
    private OkHttpUtils Okhttp;
    private String flag, mids, jsons;
    private  String   ips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_shezhi);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        ips = MyApplication.ip;
        boolean changeState = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.PUSH_STATE, true);
        my_togbutton = (SwitchButton) findViewById(R.id.my_kaiguan);
        my_togbutton.setChecked(changeState);
        my_togbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean changeState = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.PUSH_STATE, true);
                my_togbutton.setChecked(!changeState);
                SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.PUSH_STATE, !changeState);
            }
        });
        upGrade = (LinearLayout) findViewById(R.id.upgrade);
        upGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int netWorkType =   NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if(netWorkType == NetUtils.NETWORKTYPE_INVALID){
                    Toast.makeText(My_shezhi.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                }else{
                    upgradeVersion();
                }

            }
        });
        xiugaimima = (LinearLayout) findViewById(R.id.xiugai_mima);
        xiugaimima_line = (TextView) findViewById(R.id.xiugaimima_line);
        xiugaimima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(My_shezhi.this, RevisePwd.class);
                startActivity(intent2);
            }
        });

        erweima = (LinearLayout)findViewById(R.id.erweima);
        erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_shezhi.this, TuiJianErWeiMa.class);
                startActivity(intent);
            }
        });

        my_collects = (SwitchButton) findViewById(R.id.my_collects);
        pic_whife = (SwitchButton) findViewById(R.id.pic_whife);
        shezhi_back = (ImageView) findViewById(R.id.shezhi_backs);
        msg_feedback = (LinearLayout) findViewById(R.id.msg_feedback);
        about = (LinearLayout) findViewById(R.id.about);
        clean_data = (LinearLayout) findViewById(R.id.clean_cache);

//        folderInfo = CacheFileInfo.loadFolderInfo(getExternalCacheDir().getAbsolutePath());
//        dataSize+=folderInfo.allFileSize;
//        folderInfo = CacheFileInfo.loadFolderInfo(getFilesDir().getAbsolutePath());
//        dataSize+= folderInfo.allFileSize;
        File file = ImageLoader.getInstance().getDiskCache().getDirectory();
        String path = file.getPath();
        folderInfo = CacheFileInfo.loadFolderInfo(path);
        dataSize += folderInfo.allFileSize;
        clean_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BTAlertDialog dialog = new BTAlertDialog(My_shezhi.this);
                dialog.setTitle("您确认要清理缓存数据吗？");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCleanManager.cleanCache();
                        clean_txt.setText("0.0B");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        clean_txt = (TextView) findViewById(R.id.clean_txt);
        clean_txt.setText(CacheFileInfo.fileSize2String(dataSize));
        shezhi_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        help = (LinearLayout) findViewById(R.id.help_
        );
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        exit = (Button) findViewById(R.id.exit);
        boolean thridState = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);
        if (loginState.equals("1")) {
            if (thridState) {
                xiugaimima.setVisibility(View.GONE);
                xiugaimima_line.setVisibility(View.GONE);
            } else {
                xiugaimima.setVisibility(View.GONE);
                xiugaimima_line.setVisibility(View.GONE);
            }

            exit.setVisibility(View.VISIBLE);
        } else {
            xiugaimima.setVisibility(View.GONE);
            xiugaimima_line.setVisibility(View.GONE);
            exit.setVisibility(View.INVISIBLE);
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BTAlertDialog dialog = new BTAlertDialog(My_shezhi.this);
                dialog.setTitle("您确认要退出当前登录吗?");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingActivity.isExit = true;
                        mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        fintjson();
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_TYPE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_COM, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_NAME, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.NICK_NAME, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ID, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_ADRESS, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.HANGYE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_MOBILE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_BOSS, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.EMAIL, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.PRODUCT, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.TEL, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.LOGIN_FLAY, "0");
                        SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.THIRD_LOGIN_STATE, false);

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.UNIT_TYPE, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.ZHIWEI, "0");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINCAILIAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_WENHUACHUANYI, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG, "0");

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_XINNENGYUAN, "0");
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.LOGIN_HIDE, 0);

                        SharedPreferencesUtil.putString(SharedPreferencesUtil.XINGQU_QITA, "0");
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.WQ_STATE,"0");
                        PulseCondtion.getInstance(My_shezhi.this).deleteData();
                        MaiDianYuYue.getInstance(My_shezhi.this).deleteData();
                        MaiDianCollection.getInstance(My_shezhi.this).deleteData();
                        WelcomePulse.LoginState = true;
                        finish();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(My_shezhi.this, About.class);
//                startActivity(intent);
                Intent intent = new Intent(My_shezhi.this, TestRecyclerViewActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(My_shezhi.this, TestCIYun.class);
//                startActivity(intent);
//                Intent intent = new Intent(My_shezhi.this, ActivityUITest.class);
//                startActivity(intent);

            }
        });
        msg_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_shezhi.this, Feedback.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(My_shezhi.this, WebViewActivity.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", "file:///android_asset/clause.html");
                startActivity(intent);
            }
        });

        my_collects.setOnChangeListener(new SwitchButton.OnChangeListener() {

            @Override
            public void onChange(SwitchButton sb, boolean state) {
                if (state) {
                    //选中
                    Toast.makeText(My_shezhi.this, "选中", Toast.LENGTH_SHORT).show();
                } else {
                    //未选中
                    Toast.makeText(My_shezhi.this, "未选中", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        my_collects.setOnChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });

        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
        pic_whife.setChecked(state);

        pic_whife.setOnChangeListener(new SwitchButton.OnChangeListener() {

            @Override
            public void onChange(SwitchButton sb, boolean state) {
                SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, state);

            }
        });
//        pic_whife.setOnChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
    }

    //    http://123.206.8.208:80/api/logout.php?mid=1
    public void fintjson() {
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    MyApplication.setAccessid();
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("mid"+mids);
                    sort.add("timestamp"+timestamp);
                    sort.add("version"+MyApplication.version);
                    String accessid="";
                    String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        accessid = mid;
                    }else{
                        accessid = MyApplication.deviceid;
                    }
                    sort.add("accessid"+accessid);
                    sign = KeySort.keyScort(sort);

                    jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/loginout.php?mid=" + mids + "&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                    if (jsons != null) {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {

                }
//                http://123.206.8.208:80/api/logout.php?mid=1&flag=

            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Gson g = new Gson();
                Codes codes = g.fromJson(jsons, Codes.class);
                if (codes.code == 1) {
                    Toast.makeText(My_shezhi.this, codes.message, Toast.LENGTH_SHORT).show();
                } else if (codes.code == -1) {
                    Toast.makeText(My_shezhi.this, codes.message, Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    String ret = "";
    UpGrade grade;

    /**
     * 检查升级
     */
    public void upgradeVersion() {
//        UIHelper.showDialogForLoading(this, "", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://"+ips+"/uploads/resource/android/" + MyApplication.UMENG_CHANNEL + "/updata.json";

                    ret = OkHttpUtils.loaudstringfromurl(url);

                    if (ret != null) {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handlergrade.sendMessage(msg);
                    }
                } catch (Exception e) {

                }

            }

        }).start();
    }

    Handler handlergrade = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                UIHelper.hideDialogForLoading();
                Gson gson = new Gson();
                grade = gson.fromJson(ret, UpGrade.class);
                if (grade.code.equals("1")) {
                    int oldVersion = MyApplication.versionCode;
                    int newVersion = grade.data.version_id;
                    String version_name = grade.data.version_name;
                    String txt = grade.data.update_note;
                    if (newVersion > oldVersion) {
                        if (!grade.data.is_required) {
                            final UpgradeAlertDialog dialog = new UpgradeAlertDialog(My_shezhi.this);
                            dialog.setTitle("发现新版本,"+version_name+"来啦");
                            dialog.setContext(txt);
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openBrowserUpdate(grade.data.down_link);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else {
                            openBrowserUpdate(grade.data.down_link);
                        }

                    } else {
                        Toast.makeText(My_shezhi.this, "已经是最新版本！", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (msg.what == 11) {
                try {
                    UIHelper.hideDialogForLoading();
                    File file = (File)msg.obj;//安装apk
                    if(file != null){
                        Intent intent = new Intent();
                        //执行动作
                        intent.setAction(Intent.ACTION_VIEW);
                        //执行的数据类型
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        startActivity(intent);

                        Intent intent1 = new Intent();
                        intent1.setAction("finish");
                        My_shezhi.this.sendBroadcast(intent1);
                        My_shezhi.this.finish();
                    }
                }catch (Exception e){

                }

            }

        }
    };

    private void updateFromWeb(String url) {
        //        Log.d("lizisong", "url:"+url);
//        Intent intentdata = new Intent(Intent.ACTION_VIEW);
////        intentdata.setType("application/vnd.android.package-archive");
////        startActivity(intentdata);
//        intentdata.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intentdata.setAction(android.content.Intent.ACTION_VIEW);
//        intentdata.setDataAndType(Uri.parse(url),
//                "application/vnd.android.package-archive");
//        startActivity(intentdata);
        UIHelper.showDialogForLoading(this, "升级中，请等待...", true);
        final String urlPath = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = getFileFromServer(urlPath);
                Message msg = Message.obtain();
                msg.what = 11;
                msg.obj = file;
                handlergrade.sendMessage(msg);
            }
        }).start();
    }

    private File getFileFromServer(String url) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                URL urlpath = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlpath.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                }
                fos.close();
                bis.close();
                is.close();
                return file;
//                Message msg = Message.obtain();
//                msg.what = 11;
//                handler1.sendMessage(msg);
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }
    /**
     * 打开浏览器更新下载新版本apk
     * @param apkUrl    apk托管地址
     */
    private void openBrowserUpdate(String apkUrl) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri apk_url = Uri.parse(apkUrl);
        intent.setData(apk_url);
        startActivity(intent);//打开浏览器

    }
}
