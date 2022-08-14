package Util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.maidiantech.common.resquest.NetworkCom;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import entity.WeatherData;

import static com.flyco.animation.R.id.time;

/**
 * Created by lizisong on 2017/2/27.
 */

public class WeatherUtils {
    public static WeatherData weatherData = new WeatherData();
    public static void parseWeather(String city, String day, final Handler handler) throws  Exception{
        String citycode = "";

        try {
            citycode = URLEncoder.encode(city, "GB2312");
            final String url = "http://php.weather.sina.com.cn/xml.php?city="+citycode+"&password=DJOYnieT8234jlsK&day="+day;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL parese = null;
                    HttpURLConnection urlConnection = null;
                    try {
                        parese = new URL(url);
                        urlConnection = (HttpURLConnection) parese.openConnection();
                        urlConnection.setDoInput(true);
                        urlConnection.setReadTimeout(10 * 1000);
                        urlConnection.setConnectTimeout(10 * 1000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setUseCaches(false);
                        urlConnection.setRequestProperty("connection", "close");
                        urlConnection.connect();
                        int code = urlConnection.getResponseCode();
                        if(code == 200){
                            StringBuffer sb = new StringBuffer();
                            BufferedReader buffer = null;
                            buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String line;
                            while((line = buffer.readLine()) != null)
                            {
                                sb.append(line);
                            }

                            Log.d("lizisong", sb.toString());
                            ByteArrayInputStream tInputStringStream = null;
                            tInputStringStream = new ByteArrayInputStream(sb.toString().getBytes());
                            InputSource is2 = new InputSource(tInputStringStream);

                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            //通过SAXParserFactory得到SAXParser的实例
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            MySaxHandler msh = new MySaxHandler();
                            //将对象msh传递给xr
                            xr.setContentHandler(msh);
                            //调用xr的parse方法解析输入流
                            xr.parse(is2);
                            tInputStringStream.close();
                            if(handler != null){
                                Message msg = Message.obtain();
                                msg.what = 1000;
                                handler.sendMessage(msg);
                            }
                        }
                    } catch (MalformedURLException e) {

                    }catch (Exception e){

                    }finally {

                    }


                }
            }).start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
    private void parsexml(String ret){
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例

        try {


        }catch (Exception e){

        }

    }
    public static void baiduParse(String city, Handler handler){
        String url="http://api.map.baidu.com/telematics/v3/weather";
        HashMap<String, String> map = new HashMap<>();
        map.put("location",city);
        map.put("output", "json");
        map.put("ak", "vAbA2AdimVLzs4KBjfjQIROP");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,500,0);
    }


    /**
     * 新知的天气接口
     */
    public static void xinzhitianqi1(String city, Handler handler){
        String url="https://api.thinkpage.cn/v3/weather/now.json";
        HashMap<String, String> map = new HashMap<>();
        map.put("key","u1ugudtxnqtvftyh");
        map.put("location",city);
        map.put("language", "zh-Hans");
        map.put("unit", "c");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,500,0);
    }

    public static void xinzhitianqi2(String city, Handler handler){
        String url="https://api.thinkpage.cn/v3/weather/daily.json";
        HashMap<String, String> map = new HashMap<>();
        map.put("key","u1ugudtxnqtvftyh");
        map.put("location",city);
        map.put("language", "zh-Hans");
        map.put("unit", "c");
        map.put("start", "0");
        map.put("days","1");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,501,0);
    }




}
