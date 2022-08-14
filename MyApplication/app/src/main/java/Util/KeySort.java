package Util;

import com.sina.weibo.sdk.utils.MD5;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lizisong on 2017/4/28.
 */

public class KeySort {

    public static String keyScort(ArrayList list){
       try{
           String ret="";
           Collections.sort(list);
           for(int i=0; i<list.size();i++){
               String item = (String)list.get(i);
               ret=ret+item;
           }
           String md5Str="";
           ProTectByMD5 md5=new ProTectByMD5();
           md5Str = md5.encode("secret");
           ret= md5Str+ret;
//           FileHelper.d("lizisong","ret:"+ret);
           ret= md5.encode(ret);
//           FileHelper.d("lizisong","最终:"+ret);
           return ret.toUpperCase();
       } catch (Exception e){

       }
        return null;

    }
}
