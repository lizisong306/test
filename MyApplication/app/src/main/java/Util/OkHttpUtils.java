package Util;

import android.content.Context;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by gengdi on 2016/6/11.
 */
public class OkHttpUtils {
    private static StringBuffer buffer;
    private String htmlStr;
    private Context context;

    private static OkHttpUtils instancesOKHttp=null;

    /**
     * 创建OkHttpUtils的单例模式
     * @return
     */
    public static OkHttpUtils getInstancesOkHttp(){
        if(instancesOKHttp == null){
            instancesOKHttp = new OkHttpUtils();
        }
        return instancesOKHttp;
    }

    public static final com.squareup.okhttp.OkHttpClient OkHttpClient=new OkHttpClient();
    static {
        OkHttpClient.setConnectTimeout(150, TimeUnit.SECONDS);
        OkHttpClient.setReadTimeout(150, TimeUnit.SECONDS);
        OkHttpClient.setWriteTimeout(150, TimeUnit.SECONDS);
    }
    /**
     *  下面的事get请求
     */

    /**
     * 自定义 返回resqust请求对象
     * @param path
     * @return
     */
    public static Request getrequstfomurl(String path){
        Request request=new Request.Builder().url(path).build();
        return  request;
    }
    /**
     * 自定义 返回Response响应对象
     * @param path
     * @return
     */
    public static Response getresponsefomurl(String path){
        try {

            Request request=getrequstfomurl(path);

            Response response = OkHttpClient.newCall(request).execute();
            return  response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    /**
     * 判断返回值  看是否请求成功
     * @param
     * @return
     */
    public static ResponseBody getresponsbody(String url){
        try{
            Response response = getresponsefomurl(url);
            if (response.isSuccessful()){
                return   response.body();
            }
        }catch (Exception e){

        }
        return  null;
    }
    /**
     * 判断返回的对象
     * 返回string
     */
    public  static  String loaudstringfromurl(String url){
        try {

            ResponseBody getresponsbody = getresponsbody(url);
            if(getresponsbody!=null){
                return  getresponsbody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String Myokhttpclient(String url) {

        //创建okHttpClient对象
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            byte[] b = response.body().bytes();     //获取数据的bytes
            htmlStr = new String(b, "UTF-8");   //然后将其转为gb2312
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlStr;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //以下是post 情求
    //
    ////////////////////


    private static Request builderrequeste(String usr, RequestBody requestBody){
        Request.Builder builder=new Request.Builder();

        builder.url(usr).post(requestBody);
        return  builder.build();
    }

    private static  String postrequestbody(String str,RequestBody requestBody){
        try {
            Request builderrequeste = builderrequeste(str, requestBody);
            Response execute = OkHttpClient.newCall(builderrequeste).execute();
            if (execute.isSuccessful()){
                return execute.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    private static RequestBody builderrequsetboder(Map<String,String> map){
        FormEncodingBuilder builder=new FormEncodingBuilder();
        if (map!=null&&!map.isEmpty()){
            for (Map.Entry<String,String> entry:map.entrySet()
                    ) {
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        return  builder.build();
    }
    //post請求返回的对象
    public static   String postkeyvlauspainr(String str,Map<String,String> map){
        RequestBody builderrequsetboder = builderrequsetboder(map);
        return postrequestbody(str,builderrequsetboder);
    };

    /**
     * 同步post 键值对数据
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> data) throws IOException {
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        for (Map.Entry<String, String> item : data.entrySet()) {
            formBuilder.add(item.getKey(), item.getValue());
        }

        RequestBody body = formBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();

        Response response = excute(request);
        if (response.isSuccessful()) {
            InputStream is = response.body().byteStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            buffer = new StringBuffer();
            while ((str = br.readLine()) != null)  {
                buffer.append(str);
            }
            return buffer.toString();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    private static Response excute(Request request) throws IOException {
        return OkHttpClient.newCall(request).execute();
    }


}