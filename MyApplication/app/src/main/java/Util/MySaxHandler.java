package Util;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import entity.WeatherData;

/**
 * Created by lizisong on 2017/2/27.
 */

public class MySaxHandler extends DefaultHandler {
    private String content;


    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        content = new String(ch, start, length);
        Log.d("lizisong:",content);

    }

    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
//        Log.d("lizisong", "startElement localName:"+localName + ",qName:"+qName);


    }

    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
//        Log.d("lizisong", "endElement localName:"+localName + ",qName:"+qName);
         if(localName.equals("city")){
             WeatherUtils.weatherData.city = content;
         }else if(localName.equals("status1")){
             WeatherUtils.weatherData.status1 = content;
         }else if(localName.equals("status2")){
             WeatherUtils.weatherData.status2 = content;
         }else if(localName.equals("figure1")){
             WeatherUtils.weatherData.figure1 = content;
         }else if(localName.equals("figure2")){
             WeatherUtils.weatherData.figure2 = content;
         }else if(localName.equals("direction1")){
             WeatherUtils.weatherData.direction1 = content;
         }else if(localName.equals("direction2")){
             WeatherUtils.weatherData.direction2 = content;
         }else if(localName.equals("power1")){
             WeatherUtils.weatherData.power1 = content;
         }else if(localName.equals("power2")){
             WeatherUtils.weatherData.power2 = content;
         }else if(localName.equals("temperature1")){
             WeatherUtils.weatherData.temperature1 = content;
         }else if(localName.equals("temperature2")){
             WeatherUtils.weatherData.temperature2 = content;
         }else if(localName.equals("ssd")){
             WeatherUtils.weatherData.ssd = content;
         }else if(localName.equals("tgd1")){
             WeatherUtils.weatherData.tgd1 = content;
         }else if(localName.equals("tgd2")){
             WeatherUtils.weatherData.tgd2 = content;
         }else if(localName.equals("zwx")){
             WeatherUtils.weatherData.zwx = content;
         }else if(localName.equals("ktk")){
             WeatherUtils.weatherData.ktk = content;
         }else if(localName.equals("pollution")){
             WeatherUtils.weatherData.pollution = content;
         }else if(localName.equals("xcz")){
             WeatherUtils.weatherData.xcz = content;
         }else if(localName.equals("zho")){
             WeatherUtils.weatherData.zho = content;
         }else if(localName.equals("diy")){
             WeatherUtils.weatherData.diy = content;
         }else if(localName.equals("fas")){
             WeatherUtils.weatherData.fas = content;
         }else if(localName.equals("chy")){
             WeatherUtils.weatherData.chy = content;
         }else if(localName.equals("zho_shuoming")){
             WeatherUtils.weatherData.zho_shuoming = content;
         }else if(localName.equals("diy_shuoming")){
             WeatherUtils.weatherData.diy_shuoming = content;
         }else if(localName.equals("fas_shuoming")){
             WeatherUtils.weatherData.fas_shuoming = content;
         }else if(localName.equals("chy_shuoming")){
             WeatherUtils.weatherData.chy_shuoming = content;
         }else if(localName.equals("pollution_l")){
             WeatherUtils.weatherData.pollution_l = content;
         }else if(localName.equals("zwx_l")){
             WeatherUtils.weatherData.zwx_l = content;
         }else if(localName.equals("ssd_l")){
             WeatherUtils.weatherData.ssd_l = content;
         }else if(localName.equals("fas_l")){
             WeatherUtils.weatherData.fas_l = content;
         }else if(localName.equals("zho_l")){
             WeatherUtils.weatherData.zho_l = content;
         }else if(localName.equals("chy_l")){
             WeatherUtils.weatherData.chy_l = content;
         }else if(localName.equals("ktk_l")){
             WeatherUtils.weatherData.ktk_l = content;
         }else if(localName.equals("xcz_l")){
             WeatherUtils.weatherData.xcz_l = content;
         }else if(localName.equals("diy_l")){
             WeatherUtils.weatherData.diy_l = content;
         }else if(localName.equals("pollution_s")){
             WeatherUtils.weatherData.pollution_s = content;
         }else if(localName.equals("zwx_s")){
             WeatherUtils.weatherData.zwx_s = content;
         }else if(localName.equals("ssd_s")){
             WeatherUtils.weatherData.ssd_s = content;
         }else if(localName.equals("ktk_s")){
             WeatherUtils.weatherData.ktk_s = content;
         }else if(localName.equals("xcz_s")){
             WeatherUtils.weatherData.xcz_s = content;
         }else if(localName.equals("gm")){
             WeatherUtils.weatherData.gm = content;
         }else if(localName.equals("gm_l")){
             WeatherUtils.weatherData.gm_l = content;
         }else if(localName.equals("gm_s")){
             WeatherUtils.weatherData.gm_s = content;
         }else if(localName.equals("yd")){
             WeatherUtils.weatherData.yd = content;
         }else if(localName.equals("yd_l")){
             WeatherUtils.weatherData.yd_l = content;
         }else if(localName.equals("yd_s")){
             WeatherUtils.weatherData.yd_s = content;
         }else if(localName.equals("savedate_weather")){
             WeatherUtils.weatherData.savedate_weather = content;
         }else if(localName.equals("savedate_life")){
             WeatherUtils.weatherData.savedate_life = content;
         }else if(localName.equals("savedate_zhishu")){
             WeatherUtils.weatherData.savedate_zhishu = content;
         }else if(localName.equals("udatetime")){
             WeatherUtils.weatherData.udatetime = content;
         }


    }
}
