package com.maidiantech.common.resquest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Util.FileHelper;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;

/**
 * Created by lizisong on 2018/2/8.
 * 对以前的请求网络数据在一次封装，将某些公共的字段封装到底层
 */

public class NetworkCom {

    private static NetworkCom networkCom;
    private static ExecutorService cachedThreadExecutor;

    public NetworkCom (){
        cachedThreadExecutor = Executors.newCachedThreadPool();
        MyApplication.setAccessid();
    }

    /**
     * 获取网络数据的单列模式
     * @return
     */
    public static NetworkCom getNetworkCom(){
        if(networkCom == null){
            networkCom = new NetworkCom();

        }
        return networkCom;
    }

    /**
     * get方式获取数据
     * @param url
     * @param parameter
     * @param handler
     * @param code
     */
    public void getJson(final String url, final HashMap<String,String > parameter,
                        final Handler handler, final int code, final int arg){

        final  String version = MyApplication.version;
        final  String accessid = MyApplication.deviceid;
        cachedThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String path ="";
                String geturl="";
                String ret = "";
                ArrayList<String> sort = new ArrayList<String>();
                if (parameter!=null&&!parameter.isEmpty()){
                    int count = 0;
                    for (Map.Entry<String,String> entry:parameter.entrySet()
                            ) {
                        String mid = entry.getKey();
                        if(mid.equals("mid")){
                            continue;
                        }
                        if(count == 0){
                            path = entry.getKey()+"="+entry.getValue();
                        }else{
                            path = path+"&"+entry.getKey()+"="+entry.getValue();
                        }
                        sort.add(entry.getKey()+entry.getValue());
                        count++;
                    }
                }
//                FileHelper.d("lizisong", "start:"+"get方式");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()/1000+"";
                String sign="";
                sort.add("version"+version);
                sort.add("clienttype"+"2");
                sort.add("accessid"+accessid);
                sort.add("timestamp"+timestamp);
                String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                String mid ="";
                if(state.equals("1")){
                    mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                    sort.add("mid"+mid);
                }

                FileHelper.d("lizisong", sort.toString());
                sign = KeySort.keyScort(sort);
                if(state.equals("1")){
                    path = path+"&version="+version+"&accessid="+accessid+"&mid="+mid+"&timestamp="+timestamp+"&clienttype=2"+"&sign="+sign;
                }else{
                    path = path+"&version="+version+"&accessid="+accessid+"&timestamp="+timestamp+"&clienttype=2"+"&sign="+sign;
                }

                geturl = url+"?"+path;

                FileHelper.d("lizisong", "geturl:"+geturl);


                ret=OkHttpUtils.loaudstringfromurl(geturl);
//                FileHelper.d("lizisong", "end:"+"get方式");
                Message msg = Message.obtain();
                msg.what = code;
                msg.arg1 = arg;
                if(ret != null){
                    msg.obj = ret;
//                    FileHelper.d("lizisong", "end:"+ret);
                }else{
                    ret="";
                    msg.obj =ret;
                }
                if(handler != null){
                    handler.sendMessage(msg);
                }
            }
        });

    }

    /**
     * post 方式获取数据
     * @param url
     * @param parameter
     * @param handler
     * @param code
     */
    public void postJson(final String url, final HashMap<String,String > parameter,
                        final Handler handler, final int code, final int arg){
        final  String version = MyApplication.version;
        final  String accessid = MyApplication.deviceid;

        cachedThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String path ="";
                String geturl="";
                String ret = "";
                ArrayList<String> sort = new ArrayList<String>();
                HashMap<String ,String> map = new HashMap<String, String>();
                if (parameter!=null&&!parameter.isEmpty()){

                    for (Map.Entry<String,String> entry:parameter.entrySet()
                            ) {
                        String mid = entry.getKey();
                        if(mid.equals("mid")){
                            continue;
                        }
                        sort.add(entry.getKey()+entry.getValue());
                        map.put(entry.getKey(),entry.getValue());
                    }
                }
//                FileHelper.d("lizisong", "start:"+"post方式");
                String timestamp = System.currentTimeMillis()/1000+"";
                String sign="";
                String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                String mid ="";
                sort.add("version"+version);
                sort.add("accessid"+accessid);
                sort.add("clienttype"+"2");
                sort.add("timestamp"+timestamp);
                if(state.equals("1")){
                    mid=SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                }
                sort.add("mid"+mid);
                sign=KeySort.keyScort(sort);
                map.put("version", version);
                map.put("accessid",accessid);
                map.put("clienttype","2");
                if(state.equals("1")){
                    map.put("mid",mid);
                }
                map.put("timestamp",timestamp);
                map.put("sign", sign);
                Log.d("lizisong", "post:"+url+", mapstr"+map.toString());
//                FileHelper.d("lizisong", map.toString());
//                FileHelper.d("lizisong", sort.toString());
                ret = OkHttpUtils.postkeyvlauspainr(url, map);
//                FileHelper.d("lizisong", "end:"+"post方式");
                Message msg = Message.obtain();
                msg.what = code;
                msg.arg1 = arg;
                if(ret != null){
                    msg.obj = ret;
                }else{
                    ret="";
                    msg.obj =ret;
                }
                if(handler != null){
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
