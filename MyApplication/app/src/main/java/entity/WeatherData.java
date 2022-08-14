package entity;

/**
 * Created by lizisong on 2017/2/27.
 */

public class WeatherData {
    public String city;       //对应的查询城市
    public String status1;   //白天天气情况
    public String status2;   //夜间天气情况
    public String figure1;   //白天天气情况拼音
    public String figure2;   //夜间天气情况拼音
    public String direction1; //白天风向
    public String direction2; //夜晚风向
    public String power1;     // 白天风力
    public String power2;     // 夜间风力
    public String temperature1; //白天温度
    public String temperature2; //夜间温度
    public String ssd;          //体感指数
    public String tgd1;         //白天体感温度
    public String tgd2;         //夜间体感温度
    public String zwx;          //紫外线强度
    public String ktk;          //空调指数
    public String pollution;    //污染指数
    public String xcz;          //洗车指数
    public String zho;          //综合指数？
    public String diy;          //
    public String fas;
    public String chy;          //穿衣指数
    public String zho_shuoming;	//zho的说明，然而zho是什么指数我也不确定
    public String diy_shuoming;
    public String fas_shuoming;
    public String chy_shuoming;//穿衣指数说明
    public String pollution_l; //污染程度
    public String zwx_l;       //紫外线指数概述
    public String ssd_l;       //体感指数概述
    public String fas_l;       //这个不知道
    public String zho_l;       //这个也不清楚
    public String chy_l;       //穿衣指数概述（可理解为穿衣建议）
    public String ktk_l;      //空调指数概述
    public String xcz_l;      //洗车指数概述
    public String diy_l;      //这个不知道
    public String pollution_s; //污染指数详细说明
    public String zwx_s;       //紫外线详细说明
    public String ssd_s;       //体感详细说明
    public String ktk_s;       //空调指数详细说明
    public String xcz_s;      //洗车详细说明
    public String gm;         //感冒指数
    public String gm_l;       //感冒指数概述
    public String gm_s;       //感冒指数详细说明
    public String yd;         //运动指数
    public String yd_l;       //运动指数概述
    public String yd_s;       //运动指数详细说明
    public String savedate_weather; //天气数据日期
    public String savedate_life;    //生活数据日期
    public String savedate_zhishu;   //指数数据日期
    public String udatetime;      //更新时间

}
