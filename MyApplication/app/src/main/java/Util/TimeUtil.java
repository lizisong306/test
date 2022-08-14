package Util;

import java.util.Date;

/**
 * Created by lizisong on 2017/9/15.
 */

public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date,String sec) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
             return TimeUtils.getStrMonthAndData(sec);
        }
        if (diff > month) {
            r = (diff / month);
             return TimeUtils.getStrMonthAndData(sec);
        }
        if (diff > day) {
            r = (diff / day);
            if(r<= 3){
               return r + "天前";
            }else {
                return TimeUtils.getStrMonthAndData(sec);
            }

        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        if(diff < minute){
            return "刚刚";
        }
        return TimeUtils.getStrMonthAndData(sec);

    }
}
