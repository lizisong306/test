package application;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.maidiantech.ClickListInter;
import com.maidiantech.MainActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
//import com.qiniu.android.dns.http.DnspodFree;
//import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
//import com.tencent.android.tpush.XGPushClickedResult;
//import com.tencent.android.tpush.XGPushManager;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import Util.SharedPreferencesUtil;
import entity.city_index;
import entity.citystrcut;

/**
 * Created by 13520 on 2016/8/22.
 */
public class MyApplication extends Application {
    public static MyApplication context;
    public final static String APP_ID = "101348883";

    public static String pkName;
    public static String versionName;
    public static  int versionCode=69;
    public static  String UMENG_CHANNEL = "88022421";
    public MyLocationListenner myListener = new MyLocationListenner();

    public LocationClient mLocationClient = null;
    public Vibrator mVibrator01;
    public static  String currentCity="";
    public static String deviceid = "";

    public static String ip="www.zhongkechuangxiang.com";//123.206.8.208
//    public static String ip="123.207.164.210";
    public static String version="4.4.0";
    public static String model = android.os.Build.MODEL;
    public static  final  int MINHEIGHT = 1980;

    public static String accessid ="";
    public static String [] ips = null;
//    public static String ip="123.207.164.210";
    public static ArrayList<String> cityId = new ArrayList<>();
    public static ArrayList<citystrcut>  citystrcutList = new ArrayList<>();
    public  static  int width;
    public  static  int height,maxHeight;
    public  static  int widths;
    public  static  int heights;
    public static int navigationbar = 0;
    public static String Latitude = "";
    public static String Longitude="";
    public static String adress="";
    public static IWXAPI api =null;
    public static ClickListInter clickListInter;
    public static String ipadress = "http://www.zhongkechuangxiang.com";
    public static View clickView;

    static {


        //初始化政策的选择条件
        citystrcut quanguo = new citystrcut();
        quanguo.key = "全国";
        city_index value0 = new city_index("全国", "0");
        quanguo.item.add(value0);
        citystrcutList.add(quanguo);

        citystrcut huabeidiqu = new citystrcut();
        quanguo.key = "华北地区";

        city_index huabeidiquitem0 = new city_index("不限", "1000,1500,2000,2500,3000");
        huabeidiqu.item.add(huabeidiquitem0);
        city_index huabeidiquitem1 = new city_index("北京市", "1000");
        huabeidiqu.item.add(huabeidiquitem1);
        city_index huabeidiquitem2 = new city_index("天津市", "1500");
        huabeidiqu.item.add(huabeidiquitem2);
        city_index huabeidiquitem3 = new city_index("河北省", "2000");
        huabeidiqu.item.add(huabeidiquitem3);
        city_index huabeidiquitem4= new city_index("山西省", "2500");
        huabeidiqu.item.add(huabeidiquitem4);
        city_index huabeidiquitem5= new city_index("内蒙古自治区", "3000");
        huabeidiqu.item.add(huabeidiquitem5);
        citystrcutList.add(huabeidiqu);

        citystrcut dongbeidiqu = new citystrcut();
        quanguo.key = "东北地区";
        city_index dongbeiitem0= new city_index("不限", "3500,4000,4500");
        dongbeidiqu.item.add(dongbeiitem0);
        city_index dongbeiitem1= new city_index("辽宁省", "3500");
        dongbeidiqu.item.add(dongbeiitem1);
        city_index dongbeiitem2= new city_index("吉林省", "4000");
        dongbeidiqu.item.add(dongbeiitem2);
        city_index dongbeiitem3= new city_index("黑龙江省", "4500");
        dongbeidiqu.item.add(dongbeiitem3);
        citystrcutList.add(dongbeidiqu);

        citystrcut huadongdiqu = new citystrcut();
        huadongdiqu.key="华东地区";
        city_index huadongitem0 = new city_index("不限","8000,5500,6000,6500,7000,7500,5000");
        huadongdiqu.item.add(huadongitem0);
        city_index huadongitem1 = new city_index("山东省","8000");
        huadongdiqu.item.add(huadongitem1);
        city_index huadongitem2 = new city_index("江苏省","5500");
        huadongdiqu.item.add(huadongitem2);
        city_index huadongitem3 = new city_index("浙江省","6000");
        huadongdiqu.item.add(huadongitem3);
        city_index huadongitem4 = new city_index("安徽省","6500");
        huadongdiqu.item.add(huadongitem4);
        city_index huadongitem5 = new city_index("福建省","7000");
        huadongdiqu.item.add(huadongitem5);
        city_index huadongitem6 = new city_index("江西省","7500");
        huadongdiqu.item.add(huadongitem6);
        city_index huadongitem7 = new city_index("上海市","5000");
        huadongdiqu.item.add(huadongitem7);
        citystrcutList.add(huadongdiqu);

        citystrcut zhongnandiqu = new citystrcut();
        zhongnandiqu.key="中南地区";
        city_index zhongnanitem0 = new city_index("不限","8500,10000,9000,9500,11000,10500");
        zhongnandiqu.item.add(zhongnanitem0);
        city_index zhongnanitem1 = new city_index("河南省","8500");
        zhongnandiqu.item.add(zhongnanitem1);
        city_index zhongnanitem2 = new city_index("广东省","10000");
        zhongnandiqu.item.add(zhongnanitem2);
        city_index zhongnanitem3 = new city_index("湖北省","9000");
        zhongnandiqu.item.add(zhongnanitem3);
        city_index zhongnanitem4 = new city_index("湖南省","9500");
        zhongnandiqu.item.add(zhongnanitem4);
        city_index zhongnanitem5 = new city_index("海南省","11000");
        zhongnandiqu.item.add(zhongnanitem5);
        city_index zhongnanitem6 = new city_index("广西省","10500");
        zhongnandiqu.item.add(zhongnanitem6);
        citystrcutList.add(zhongnandiqu);

        citystrcut xibeidiqu = new citystrcut();
        xibeidiqu.key="西北地区";
        city_index xibeiitem0 = new city_index("不限","14000,14500,15500,15000,16000");
        xibeidiqu.item.add(xibeiitem0);
        city_index xibeiitem1 = new city_index("陕西省","14000");
        xibeidiqu.item.add(xibeiitem1);
        city_index xibeiitem2 = new city_index("甘肃省","14500");
        xibeidiqu.item.add(xibeiitem2);
        city_index xibeiitem3 = new city_index("宁夏回族自治区","15500");
        xibeidiqu.item.add(xibeiitem3);
        city_index xibeiitem4 = new city_index("青海省","15000");
        xibeidiqu.item.add(xibeiitem4);
        city_index xibeiitem5 = new city_index("新疆维吾尔自治区","16000");
        xibeidiqu.item.add(xibeiitem5);
        citystrcutList.add(xibeidiqu);
        citystrcut xinandiqu = new citystrcut();
        xibeidiqu.key="西南地区";
        city_index xinanitem0 = new city_index("不限","11500,12500,12000,13000,13500");
        xinandiqu.item.add(xinanitem0);
        city_index xinanitem1 = new city_index("重庆市","11500");
        xinandiqu.item.add(xinanitem1);
        city_index xinanitem2 = new city_index("贵州省","12500");
        xinandiqu.item.add(xinanitem2);
        city_index xinanitem3 = new city_index("四川省","12000");
        xinandiqu.item.add(xinanitem3);
        city_index xinanitem4 = new city_index("云南省","13000");
        xinandiqu.item.add(xinanitem4);
        city_index xinanitem5 = new city_index("西藏自治区","13500");
        xinandiqu.item.add(xinanitem5);
        citystrcutList.add(xinandiqu);

    }



    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        MainActivity.current = 0;
        context = this;
        UMENG_CHANNEL = getAppKey(context, "UMENG_CHANNEL");
//        Toast.makeText(context, UMENG_CHANNEL, Toast.LENGTH_SHORT).show();
//        getYC();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

         width = wm.getDefaultDisplay().getWidth()-22;
         maxHeight = wm.getDefaultDisplay().getHeight();
//        Toast.makeText(this, "maxHeight："+maxHeight, Toast.LENGTH_SHORT).show();
         height = (width*7)/15;
        widths = wm.getDefaultDisplay().getWidth();
        heights = (widths*7)/15;
      //  state = new HashMap<Integer, Boolean>();

//       Typeface fromAsset = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
//        try
//        {
//            Field field = Typeface.class.getDeclaredField("MONOSPACE");
//            field.setAccessible(true);
//            field.set(null, fromAsset);
//        }
//        catch (NoSuchFieldException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IllegalAccessException e)
//        {
//            e.printStackTrace();
//        }
        ImageLoaderUtils.initConfiguration(getApplicationContext());
        UMConfigure.init(this,"58c60bf59f06fd4c5f000ef3"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin("wx5b39abf9cbfcf5e7", "827c684d4c77cd49ee9c73f96dd42c2e");
        api = WXAPIFactory.createWXAPI(this, "wx5b39abf9cbfcf5e7");
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        // 微信 appid appsecret
//        PlatformConfig.setSinaWeibo("3686103814",
//                "b3687bc66634c80bfa80e9d15ca97308");
        // 新浪微博 appkey appsecret
       // PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
       // PlatformConfig.setQQZone("1105114681", "qSYFtMpGEqCitQS0");
        PlatformConfig.setQQZone("1105617885", "NVWKQAQknDHvf9jo");
        // QQ和Qzone appid appkey
        PlatformConfig.setAlipay("2015111700822536");
        // 支付宝 appid
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        // 易信 appkey
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi",
                "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        // Twitter appid appkey
        PlatformConfig.setPinterest("1439206");
        // Pinterest appid
        PlatformConfig.setLaiwang("laiwangd497e70d4",
                "d497e70d4c3e4efeab1381476bac4c5e");
        getAppInfo();
        mLocationClient = new LocationClient(this);
        initLocation();
        mLocationClient.registerLocationListener(myListener);
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            deviceid = TelephonyMgr.getDeviceId();
//            Toast.makeText(this, "deviceid:"+deviceid, Toast.LENGTH_SHORT).show();
            if(deviceid == null || deviceid.equals("")){
                deviceid="99999999";
            }
            Log.d("lizisong", "deviceid:"+deviceid);
//            deviceid="python";
        }catch (Exception e){

        }
        SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, false);

        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        if(city.equals("北京")){
            SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY, "全国");
        }


//        DNSClinet();

    }
    /**
     * 全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取程序信息
     * @return
     */
    private void getAppInfo() {
        try {
             pkName = this.getPackageName();
             versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
//            Toast.makeText(context, "versionCode:"+versionCode, Toast.LENGTH_SHORT).show();
//            Log.d("new", "versionCode:"+versionCode);
        } catch (Exception e) {
        }
    }


    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null)
                return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bdLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bdLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bdLocation.getLatitude());
            MyApplication.Latitude = bdLocation.getLatitude()+"";
            sb.append("\nlontitude : ");
            sb.append(bdLocation.getLongitude());
            MyApplication.Longitude = bdLocation.getLongitude()+"";
            sb.append("\nradius : ");
            sb.append(bdLocation.getRadius());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(bdLocation.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(bdLocation.getSatelliteNumber());
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\n省：");
//				sb.append(bdLocation.getProvince());
//				sb.append("\n市：");
//				sb.append(bdLocation.getCity());
//				sb.append("\n区/县：");
//				sb.append(bdLocation.getDistrict());
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
//                Log.d("lizisong", sb.toString());
            }
            MyApplication.adress = bdLocation.getAddrStr();
            sb.append("\nsdk version : ");
            sb.append(mLocationClient.getVersion());
            sb.append("\nisCellChangeFlag : ");
            sb.append(bdLocation.isCellChangeFlag());
            //logMsg(sb.toString());

//            Log.d("lizisong", "adress:"+bdLocation.getCity());
            currentCity = bdLocation.getCity();
            String city = sb.toString();
            if(city != null){
                if(city.contains("即墨")){
                    currentCity = "即墨";
                }
                if(city.contains("莱西")){
                    currentCity = "莱西";
                }
            }
            if(currentCity != null && !currentCity.equals("")){
                UpLoadAdress(MyApplication.Longitude,MyApplication.Latitude,MyApplication.adress);
                mLocationClient.stop();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public static void setAccessid(){
        accessid = "&accessid="+deviceid;
    }

    public static void DNSClinet(){

        try{
            String ip="";
            IResolver[] resolvers = new IResolver[1];
            resolvers[0] = new Resolver(InetAddress.getByName("123.206.8.208"));
            //resolvers[1] = AndroidDnsServer.defaultResolver();
            DnsManager dns = new DnsManager(NetworkInfo.normal, resolvers);
            ips = dns.query("www.maidiantech.com");


            if(ips != null){
                if(ips.length >0){
                    MyApplication.ip = ips[0];
                }
            }
            Log.d("lizisong", "ip:"+MyApplication.ip);

        }catch (Exception e){

        }

    }

    public static String getAppKey(Context c,String appKey) {
        try {
            ApplicationInfo ai = c.getPackageManager().getApplicationInfo(
                    c.getPackageName(), PackageManager.GET_META_DATA);
            Object EP_APPKEY = ai.metaData.get(appKey);
            if (EP_APPKEY instanceof Integer) {
                long longValue = ((Integer) EP_APPKEY).longValue();
                String value = String.valueOf(longValue);
                return value;
            } else if (EP_APPKEY instanceof String) {
                String value = String.valueOf(EP_APPKEY);
                return value;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return null;

    }

    private void UpLoadAdress(String longitude,String latitude,String reg_site){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        if( !mid.equals("")){
            String url = "http://"+MyApplication.ip+"/api/user_addinfo.php";
            HashMap<String,String> map = new HashMap<>();
            map.put("longitude", longitude);
            map.put("latitude", latitude);
            map.put("reg_site", reg_site);
            map.put("mid", mid);
            Log.d("lizisong", "adress:"+reg_site);
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,map,null,1,0);
        }
    }

}

