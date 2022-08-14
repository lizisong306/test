package Util;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 13520 on 2016/10/21.
 */
public class TelNumMatch {
    /*
    * 10. * 移动: 2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150, 11. *
    * 3G号段(TD-SCDMA网络)有157,182,183,188,187,181 147是移动TD上网卡专用号段. 联通: 12. *
    * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信: 13. *
    * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180 14.
    */
    static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[12378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1}))[0-9]{8}$";

    String mobPhnNum;

    public TelNumMatch(String mobPhnNum) {
        this.mobPhnNum = mobPhnNum;
        Log.d("tool", mobPhnNum);
    }

    public int matchNum() {
        /**
         * 28. * flag = 1 YD 2 LT 3 DX 29.
         */
        int flag;//存储匹配结果
        // 判断手机号码是否是11位
        if (mobPhnNum.length() == 11) {
            // 判断手机号码是否符合中国移动的号码规则
            if (mobPhnNum.matches(YD)) {
                flag = 1;
            }
            // 判断手机号码是否符合中国联通的号码规则
            else if (mobPhnNum.matches(LT)) {
                flag = 2;
            }
            // 判断手机号码是否符合中国电信的号码规则
            else if (mobPhnNum.matches(DX)) {
                flag = 3;
            }
            // 都不适合，未知֪
            else {
                flag = 4;
            }
        }
        // 不是11位
        else {
            flag = 5;
        }
        Log.i("TelNumMatch", "flag" + flag);
        return flag;
    }
//    ^[1]{1}(([3]{1}[0-9]{1})|([5]{1}[0-9]{1})|([8]{1}[0-9]{1})|([4]{1}[57]{1})|([7]{1}[0-9]{1}))[0-9]{8}$
    //^[1]{1}(([3]{1}[0-9]{1})|([5]{1}[0-9]{1})|([8]{1}[0-9]{1})|([4]{1}[57]{1})|([7]{1}[0-9]{1}))[0-9]{8}$
    //手机号码的有效性验证
    public static boolean isValidPhoneNumber(String number)
    {
        String telRegex = "^0?(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$" ;
        if (TextUtils.isEmpty(number)) return false ;
        else return number.matches( telRegex ) ;
//        boolean flag=false;
//        if(number.length()==11 && (number.matches(YD)||number.matches(LT)||number.matches(DX)))
//        {
//            flag=true;
//        }
//        return flag;
    }

    //判断手机号码是否存在
    public static boolean isExistPhoneNumber(String number)
    {
        return false;
    }

    //判断email地址是否有效
    public static boolean isEmail(String email)
    {
        String patternString="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        return isMatcher(patternString,email);
    }

    //是否是数字和字母
    public static boolean isMatchCharOrNumber(String str)
    {
        String patternString="^[\\d|a-z|A-Z]+$";
        return isMatcher(patternString,str);
    }

    //是否匹配
    public static boolean isMatcher(String patternString,String str)
    {
        boolean isValid=false;
        CharSequence inputStr =str ;
        Pattern pattern =Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(inputStr);
        if(matcher.matches())
        {
            isValid =true;
        }
        return isValid;
    }
    /*正则表达式校验密码
    1、密码必须由数字、字符、特殊字符三种中的两种组成；
            2、密码长度不能少于8个字符；*/
    //(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{8,}
    public static boolean isMobileNO(String mobiles) {
//        /^[\w.]{6,20}$/
//        ^[^\s]{6,20}$
//        ^[\w\W]{6,}$
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^[^\\s]{6,20}$" ;


        if (TextUtils.isEmpty(mobiles)) return false ;
        else return mobiles.matches( telRegex ) ;
    }
   // "^((13[0-9])|(147)|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";

    public static  boolean isMobile(String mobiles) {

        Pattern p = Pattern

                .compile("(1(([35][0-9])|(47)|8[0126789]))\\\\d{8}");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
   // 判断昵称为字母、数字、下划线、汉字且长度不能超过16的正则表达式
    //^[a-zA-Z\d\_\u2E80-\u9FFF]{0,16}$
//    [a-zA-Z]
    public static  boolean isusername(String mobiles) {

        Pattern p = Pattern

                .compile("^[A-Za-z]+$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }

//
//    /^[A-Za-z0-9\u4e00-\u9fa5]+$/
public static boolean issearch(String mobiles) {

    String telRegex = "^[A-Z|a-z|0-9|\\u4e00-\\u9fa5]*$" ;
    if (TextUtils.isEmpty(mobiles.replaceAll(" ", ""))) return false ;
    else return mobiles.replaceAll(" ","").matches(telRegex) ;
}
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
//    "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
//String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //判断email格式是否正确
    public static boolean isEmails(String email) {
        String str ="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

//    ^1\d{10}$|^(0\d{2,3}-?|\(0\d{2,3}\))?[1-9]\d{4,7}(-\d{1,8})?$

    //判断email格式是否正确
    public static boolean ismobil(String email) {
        String str ="^1\\d{10}$|^(0\\d{2,3}-?|\\(0\\d{2,3}\\))?[1-9]\\d{4,7}(-\\d{1,8})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
