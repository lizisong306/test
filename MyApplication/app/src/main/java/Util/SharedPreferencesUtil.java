package Util;

import android.content.Context;
import android.content.SharedPreferences;

import application.MyApplication;

/**
 * Created by lizisong on 2017/1/4.
 */

public class SharedPreferencesUtil {
    /*
     * sharedpreference文件的名字
     */
    public static final String SHARED_NAME = "baimai_shared";

    private static SharedPreferences mPreference = MyApplication.context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);



    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 登录的状态
     */
    public final static String LOGIN_STATE = "LOGIN_STATE";

    /**
     * 登录的名称
     */
    public final static String LOGIN_NAME = "LOGIN_NAME";

    /**
     * 真实姓名
     */
    public final static String NICK_NAME = "NICK_NAME";

    /**
     * 邮箱
     */
    public final static String EMAIL = "EMAIL";

    /**
     * 登录的ID
     *
     */
    public final static String LOGIN_ID = "LOGIN_ID";

    /**
     * 登录类型
     */
    public final static String LOGIN_TYPE = "LOGIN_TYPE";
    /**
     * 隐藏类型
     */
    public final static String LOGIN_HIDE = "LOGIN_HIDE";

    /**
     * 公司名称
     */
    public final static String LOGIN_COM = "LOGIN_COM";

    /**
     * 公司所在地
     *
     */
    public final static String LOGIN_ADRESS = "LOGIN_ADRESS";

    /**
     * 公司电话
     */
    public final static String LOGIN_MOBILE = "LOGIN_MOBILE";

    /**
     * 地区
     */
    public final static String PRODUCT = "PRODUCT";

    /**
     * 手机号
     */
    public final static String TEL = "TEL";

    /**
     * 行业
     */
    public final static String HANGYE = "HANGYE";

    /**
     * 头像
     */
    public final static String IMG = "IMG";
    /**
     * 用户状态
     */
    public final static String WQ_STATE = "WQ_STATE";

    /**
     * 公司负责人
     */
    public final static String LOGIN_BOSS = "LOGIN_BOSS";

    public final static String LOGIN_FLAY = "LOGIN_FLAY";

    /**
     * 推送状态
     */
    public final static String PUSH_STATE = "PUSH_STATE";

    /**
     * WIFI的状态
     */
    public final static String WIFI_DOWN_STATE ="WIFI_DOWN_STATE";

    /**
     * 第三方登录的状态
     *
     */
    public final static String THIRD_LOGIN_STATE = "THIRD_LOGIN_STATE";

    /**
     * 单位类型
     *
     */
    public final static String UNIT_TYPE = "UNIT_TYPE";

    /**
     * 职位
     *
     */
    public final static String ZHIWEI = "ZHIWEI";
    /**
     * 存储当前地方频道
     *
     */
    public final static String CITY = "CURRENT_CITY";

    /**
     * 请求权限
     */
    public final static String RQUEST_PERMISSION = "RQUEST_PERMISSION";
    /**
     * 提示遮罩
     */
    public final static String TIPS_ZHEZHAO = "TIPS_ZHEZHAO";
    /**
     * 加入的天数
     */
    public final static String 	JNINTIME = "JNINTIME";
    /**
     * 消息个数
     */
    public final static String INFOR_NEW = "INFOR_NEW";

    /**
     * 需求消息数
     */
    public final static String REQUIRE_NEW_0 = "REQUIRE_NEW_0";
    /**
     * 专家预约消息数
     */
    public final static String REQUIRE_NEW_4 = "REQUIRE_NEW_4";
    /**
     * 项目预约消息数
     */
    public final static String REQUIRE_NEW_2 = "REQUIRE_NEW_2";
    /**
     * 设备预约消息数
     */
    public final static String REQUIRE_NEW_7 = "REQUIRE_NEW_7";

    /**
     * 头条刷新时间
     */
    public final static String SHUAXIN_SHIJIAN = "SHUAXIN_SHIJIAN";

    /**
     * 会员
     */
    public final static String HUIYUAN_ID = "HUIYUAN_ID";
    public final static String HUIYUAN_ENTERPRISE = "HUIYUAN_ENTERPRISE";
    public final static String HUIYUAN_TELEPHONE = "HUIYUAN_TELEPHONE";
    public final static String HUIYUAN_SIGN_TIME = "HUIYUAN_SIGN_TIME";
    public final static String HUIYUAN_REGION_NAME = "HUIYUAN_REGION_NAME";
    public final static String HUIYUAN_ENAME = "HUIYUAN_ENAME";
    public final static String HUIYUAN_IS_MEMBER = "HUIYUAN_IS_MEMBER";
    /**
     * 感兴趣的领域
     *
     */
    public final static String INTEREST = "INTEREST";
    public final static String XINGQUCOUNT ="XINGQUCOUNT";

    public final static String XINGQU_DINAZIXINXIN = "XINGQU_DIANZIXINXI";
    public final static String XINGQU_SHENGWUJISHU = "XINGQU_SHENGWUJISHU";
    public final static String XINGQU_JIENENGHUANBAO = "XINGQU_JIENENGHUANBAO";
    public final static String XINGQU_XIANJINZHIZHAO = "XINGQU_XIANJINZHIZHAO";
    public final static String XINGQU_XINNENGYUAN  = "XINGQU_XINNENGYUAN";
    public final static String XINQU_HUAHUEHUAGONG = "XINQU_HUAHUEHUAGONG";
    public final static String XINQU_WENHUACHUANYI = "XINQU_WENHUACHUANYI";
    public final static String XINGQU_XINCAILIAO   ="XINGQU_XINCAILIAO";
    public final static String XINGQU_QITA         = "XINGQU_QITA";
    public final static String REDIAN_SOUSUO       = "REDIAN_SOUSUO";

    public final static String SOUSUO_XIANGMU       = "SOUSUO_XIANGMU";
    public final static String SOUSUO_ZHENGCE       = "SOUSUO_ZHENGCE";
    public final static String SOUSUO_RENCAI        = "SOUSUO_RENCAI";
    public final static String SOUSUO_SHEBEI        = "SOUSUO_SHEBEI";
    public final static String SOUSUO_SHIYANSHI        = "SOUSUO_SHIYANSHI";
    public final static String SOUSUO_ZHUANLI        = "SOUSUO_ZHUANLI";


    public final static String LOCAL_CITY_FRIST = "LOCAL_CITY_FRIST";

    public static void putString(String key, String value) {
        if (mPreference != null) {
            mPreference.edit().putString(key, value).commit();
        }
    }

    public static String getString(String key, String defValue) {
        if (mPreference != null) {
            return mPreference.getString(key, defValue);
        } else {
            return defValue;
        }
    }

    public static void putInt(String key, int value) {
        if (mPreference != null) {
            mPreference.edit().putInt(key, value).commit();
        }
    }

    public static int getInt(String key, int defValue) {
        if (mPreference != null) {
            return mPreference.getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    public static void putLong(String key, long value) {
        if (mPreference != null) {
            mPreference.edit().putLong(key, value).commit();
        }
    }

    public static long getLong(String key, long defValue) {
        if (mPreference != null) {
            return mPreference.getLong(key, defValue);
        } else {
            return defValue;
        }
    }

    public static void putBoolean(String key, boolean value) {
        if (mPreference != null) {
            mPreference.edit().putBoolean(key, value).commit();
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (mPreference != null) {
            return mPreference.getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    public static void remove(String key) {
        if (mPreference != null) {
            mPreference.edit().remove(key).commit();
        }
    }

    public static boolean contains(String key) {
        if (mPreference != null) {
            return mPreference.contains(key);
        } else {
            return false;
        }
    }
}
