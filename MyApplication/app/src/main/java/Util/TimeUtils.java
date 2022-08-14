package Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 13520 on 2016/10/24.
 */
public class TimeUtils {
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;

    }
    public static String getStrfengeTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;

    }

    /**
     * 斜线格式
     * @param timeStamp
     * @return
     */
    public static String getStrXieXIANTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;

    }

    public static String getStrMonthAndDataTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;

    }

    public static String getStrMonthAndData(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;

    }
    public static String getStrTimeHHMM(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;

    }
    public  static  String getTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;
    }

    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    public  static  String getTimes(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000L));//单位秒
        return timeString;
    }
    public static String getPlaytimeWithMsec(long millisecond) {
        String ret =null;
        String formate = "%1$02d:%2$02d";
        final long time = millisecond;
        long min = time / 60000;
        long second = (time - min * 60000) / 1000;
        String temp= String.format(formate, min, second);
        String[] timeTemp = temp.split(":");
        String result = timeTemp[0];
        int resultTemp = Integer.parseInt(result);
        if(resultTemp >= 24){
            ret = (resultTemp/24)+"天";
        }else if(resultTemp >0 && resultTemp < 24){
            ret = resultTemp +"小时";
        }else {
            ret = timeTemp[1]+"分";
        }
        return ret;
    }


    public static String getPlaytimeWithSec(int second) {
        String formate = "%1$02d:%2$02d";
        final long time = second;
        long min = time / 60;
        long aSecond = time - min * 60;
        return String.format(formate, min, aSecond);
    }

    public static String daysDifference(String day1, String day2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            Date d1 = df.parse(day1);
            Date d2 = df.parse(day2);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            if(days==2){
                return ("前天");
            }
            else  if(days==1){
                return ("昨天");
            }else if(days==0&&hours>0){
                return (hours+"小时前");
            }else if(days==0&&hours==0){
                return ("刚刚");
            }else{
                return ("1");
            }

        }catch (Exception e){

        }
        return null;
    }
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t*1000);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

}
