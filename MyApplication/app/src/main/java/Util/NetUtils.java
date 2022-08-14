package Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import application.MyApplication;

public class NetUtils {

    private static ConnectivityManager mConnectivityManager;
    // private static Uri PREFERRED_APN_URI =
    // Uri.parse("content://telephony/carriers/preferapn");
    static {
        mConnectivityManager = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public enum NetType {
        /**
         * wifi网络
         */
        NET_WIFI,
        /**
         * 4g网络
         */
        NET_4G,
        /**
         * 3g网络
         */
        NET_3G,
        /**
         * 2g网络
         */
        NET_2G,
        /**
         * 未知网络类型
         */
        NET_UNKNOW,
        /**
         * 无网络
         */
        NET_NONE
    }

    /**
     * 查看是否有网络
     *
     * @return
     */
    public static synchronized boolean isNetConnected() {
        NetworkInfo activeNetInfo = null;
        if (mConnectivityManager != null) {
            // 修改有些ROM getActiveNetworkInfo 报NullPointerException异常的问题
            try {
                activeNetInfo = mConnectivityManager.getActiveNetworkInfo();
            } catch (NullPointerException e) {
            }
        }
        return activeNetInfo == null ? false : activeNetInfo.isConnected();
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static synchronized NetType getNetType() {
        NetType iType = NetType.NET_NONE;
        NetworkInfo networkInfo = null;
        if (mConnectivityManager != null) {
            // 修改有些ROM getActiveNetworkInfo 报NullPointerException异常的问题
            try {
                networkInfo = mConnectivityManager.getActiveNetworkInfo();
            } catch (NullPointerException e) {
            }
        }
        if (networkInfo == null) {
            return NetType.NET_NONE;
        }
        int nType = networkInfo.getType();
        switch (nType) {
            case ConnectivityManager.TYPE_MOBILE:
                int subType = networkInfo.getSubtype();
                if (android.os.Build.VERSION.SDK_INT < 15) {
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            iType = NetType.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            iType = NetType.NET_3G;
                            break;
                        default:
                            iType = NetType.NET_UNKNOW;
                            break;
                    }
                } else {
                    // 4.1开始判断4G网络
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            iType = NetType.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            iType = NetType.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            iType = NetType.NET_4G;
                            break;
                        default:
                            iType = NetType.NET_UNKNOW;
                            break;
                    }
                }
                break;
            case ConnectivityManager.TYPE_WIFI:
                iType = NetType.NET_WIFI;
                break;
            default:
                iType = NetType.NET_UNKNOW;
                break;
        }
        return iType;
    }
    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;
    private static int mNetWorkType;

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
     * {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
     * <p>
     * {@link #NETWORKTYPE_WIFI}
     */
    public static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    public static int getNetWorkType(Context context) {
        String str = null;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
                        : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }
}
