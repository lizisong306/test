package Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 13520 on 2016/10/20.
 */
public class PrefUtils {
    public  static  final  String PREF_NAME="config";
    public static String getString(Context ctx,String key,String defaultvalue){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_APPEND);
        return sp.getString(key,defaultvalue);
    }
    public static void  setString(Context ctx,String key,String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_APPEND);
      sp.edit().putString(key,value).commit();

    }
}
