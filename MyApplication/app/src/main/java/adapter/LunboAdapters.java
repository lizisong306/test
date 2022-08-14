package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.ActiveActivity;
import com.maidiantech.LocalCity;
import com.maidiantech.R;
import com.maidiantech.WebViewActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Util.NetUtils;
import Util.SharedPreferencesUtil;
import Util.WeatherUtils;
import application.ImageLoaderUtils;
import entity.ADS;
import fragment.FirstFragment;

import static application.MyApplication.heights;
import static application.MyApplication.widths;

/**
 * Created by 13520 on 2016/9/23.
 */
public class LunboAdapters extends PagerAdapter {
    private Context context;
    private Handler handler;
    private  List<ADS> adsListData = new ArrayList<>();
    private DisplayImageOptions options;
    WeakReference<View> weakReference;
    private int width= widths;
    private int height= heights;
    private  int imagePoint;
    private  LinearLayout layout;
    LinkedList<View> mCaches = new LinkedList<View>();
//    private static HashMap<String, View> listMap = new HashMap<String, View>();
    public LunboAdapters(){}
    public LunboAdapters(Context context, Handler handler, List<ADS> adsListData){
        this.context=context;
        this.handler=handler;
        this.adsListData.clear();
        for(int i=0; i<adsListData.size();i++){
            this.adsListData.add(adsListData.get(i));
        }


    }
    public void setLunBoAdapterList(List<ADS> adsListData){
        this.adsListData=adsListData;
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // 无限个数
        try {
            if(adsListData.size()==1){
                return 1;
            }else{
                return Integer.MAX_VALUE;
            }
        }catch (Exception e){

        }
        return Integer.MAX_VALUE;

    }

    /**
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View converView = null;
        HoldView mHolder = null;
        try {
            final ADS ads = adsListData.get(position%adsListData.size());

            if(mCaches.size() == 0){
                converView   = View.inflate(context, R.layout.lunbo, null);
                mHolder = new HoldView();
                mHolder.bgtianqi = (RelativeLayout)converView.findViewById(R.id.tianqi);
                mHolder.tianqi=(ImageView)converView.findViewById(R.id.tianqi_bg);//天气背景
                mHolder.wendu = (TextView)converView.findViewById(R.id.tianqi_wendu);//温度
                mHolder.height = (TextView)converView.findViewById(R.id.height_wendu);//高温
                mHolder.low = (TextView)converView.findViewById(R.id.low_wendu);//低温
                mHolder.des = (TextView)converView.findViewById(R.id.tianqi_show);//天气描述
                mHolder.kongqi  = (TextView)converView.findViewById(R.id.tianqi_kongqi);//空气质量
                mHolder.difang  = (TextView)converView.findViewById(R.id.tianqi_difang);//地方
                mHolder.localcity = (ImageView)converView.findViewById(R.id.localcity);
                mHolder.tianqi_icon = (ImageView)converView.findViewById(R.id.tianqi_icon);//icon
                mHolder.textviewtianqi = (TextView) converView.findViewById(R.id.tianqi_text);
                mHolder.bglunbo = (RelativeLayout)converView.findViewById(R.id.lunbo);
                mHolder.img = (ImageView) converView.findViewById(R.id.img);
                mHolder. textview = (TextView)converView.findViewById(R.id.textview);
                mHolder. show=(RelativeLayout)converView.findViewById(R.id.show);
                converView.setTag(mHolder);
            }else {
                converView = (View)mCaches.removeFirst();
                mHolder = (HoldView)converView.getTag();
            }
            if(ads != null && ads.getTypename() != null && ads.getTypename().equals("天气")){
                mHolder.bgtianqi.setVisibility(View.VISIBLE);
                mHolder.bglunbo.setVisibility(View.INVISIBLE);
                mHolder.difang.setText(WeatherUtils.weatherData.city);
                mHolder.height.setText(WeatherUtils.weatherData.temperature1);
                mHolder.low.setText(WeatherUtils.weatherData.temperature2);
                if(Integer.parseInt(WeatherUtils.weatherData.tgd1)>Integer.parseInt(WeatherUtils.weatherData.temperature1)){
                   mHolder.wendu.setText(WeatherUtils.weatherData.temperature1);
                }else{
                    mHolder.wendu.setText(WeatherUtils.weatherData.tgd1);
                }

                mHolder.des.setText(WeatherUtils.weatherData.status1+","+WeatherUtils.weatherData.direction1+" "+WeatherUtils.weatherData.power1);
                ViewGroup.LayoutParams params = mHolder.tianqi.getLayoutParams();
                params.height=heights;
                params.width =widths;
                mHolder.tianqi.setLayoutParams(params);
                if(WeatherUtils.weatherData.status1.contains("晴")){
                    mHolder. tianqi.setBackgroundResource(R.mipmap.qing_bg);
                }else if(WeatherUtils.weatherData.status1.contains("多云")){
                    mHolder.tianqi.setBackgroundResource(R.mipmap.qing_bg);
                }else if(WeatherUtils.weatherData.status1.contains("雪")){
                    mHolder.tianqi.setBackgroundResource(R.mipmap.xue_bg);
                }else if(WeatherUtils.weatherData.status1.contains("雨")) {
                    mHolder. tianqi.setBackgroundResource(R.mipmap.yu_bg);
                }else if(WeatherUtils.weatherData.status1.contains("阴")){
                    mHolder.tianqi.setBackgroundResource(R.mipmap.yin_bg);
                }
                if(WeatherUtils.weatherData.pollution_l.length() > 4){
                    mHolder.kongqi.setText("良");
                }else{
                    mHolder.kongqi.setText(WeatherUtils.weatherData.pollution_l);
                }


                mHolder.textviewtianqi.setText("");
                if(WeatherUtils.weatherData.status1.contains("大暴雪")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.baoxue);
                }else if(WeatherUtils.weatherData.status1.contains("大雪")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.daxue);
                }else if(WeatherUtils.weatherData.status1.contains("大暴雨")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.dabaoyu);
                }else if(WeatherUtils.weatherData.status1.contains("暴雨")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.baoyu);
                }else if(WeatherUtils.weatherData.status1.contains("大雨")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.dayu);
                }else if(WeatherUtils.weatherData.status1.contains("阵雨")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.leizhenyu);
                }else if(WeatherUtils.weatherData.status1.contains("雷阵雨")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.leizhenyu);
                }else if(WeatherUtils.weatherData.status1.contains("阵雨")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.zhenyu);
                }else if(WeatherUtils.weatherData.status1.contains("中雪")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.zhongxue);
                }else if(WeatherUtils.weatherData.status1.contains("中雨")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.zhongyu);
                }else if(WeatherUtils.weatherData.status1.contains("阵雨")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.zhenyu);
                }else if(WeatherUtils.weatherData.status1.contains("阵雪")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.zhenxue);
                }else if(WeatherUtils.weatherData.status1.contains("雨夹雪")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.yujiaxue);
                }else if(WeatherUtils.weatherData.status1.contains("小雨")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.xiayu);
                }else if(WeatherUtils.weatherData.status1.contains("小雪")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.xiaxue);
                }else if(WeatherUtils.weatherData.status1.contains("扬沙")){
                    mHolder. tianqi_icon.setBackgroundResource(R.mipmap.yanchen);
                }else if(WeatherUtils.weatherData.status1.contains("烟尘")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.yanchen);
                }else if(WeatherUtils.weatherData.status1.contains("雾霾")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.wumai);
                }else if(WeatherUtils.weatherData.status1.contains("雾")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.wu);
                }else if(WeatherUtils.weatherData.status1.contains("晴")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.qing);
                }else if(WeatherUtils.weatherData.status1.contains("多云")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.duoyun);
                }else if(WeatherUtils.weatherData.status1.contains("阴天")){
                    mHolder.tianqi_icon.setBackgroundResource(R.mipmap.yintian);
                }
                mHolder.localcity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LocalCity.class);
                        context.startActivity(intent);

                    }
                });

            }else{
                String url = ads.getPicUrl();
                String title=ads.getTitle();
                mHolder.bglunbo.setVisibility(View.VISIBLE);
                mHolder.bgtianqi.setVisibility(View.INVISIBLE);
                options = ImageLoaderUtils.initOptions();
                    if(adsListData.size()!=0){
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if(state){
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(url
                                        ,mHolder.img , options);
                            }else{
                                mHolder.img.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        }else{
                            ViewGroup.LayoutParams params = mHolder.img.getLayoutParams();
                            params.height=heights;
                            params.width =widths;
                            mHolder.img.setLayoutParams(params);
                            ImageLoader.getInstance().displayImage(url
                                    ,mHolder.img , options);
                        }
                    }

                mHolder.textview.setText(title);

                mHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ads != null && ads.getTypename() != null && ads.getTypename().equals("html")
                                && ads.url != null && !ads.url.equals("")){
                            Intent intent=new Intent(context, ActiveActivity.class);
                            intent.putExtra("url", ads.url);
                            intent.putExtra("title", ads.getTitle());
                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,ZixunDetailsActivity.class);
                            intent.putExtra("id",ads.getAid());
                            intent.putExtra("name",ads.getTypename());
                            intent.putExtra("pic",ads.getPicUrl());
                            context.startActivity(intent);
                        }

                    }
                });
            }

            container.addView(converView);
        }catch (IllegalStateException exx){}
        catch (NumberFormatException ex){}
        catch ( Exception e){}


        return converView;
    }
    private class HoldView {
        public RelativeLayout bgtianqi;
        public ImageView tianqi;
        public TextView   wendu;
        TextView  height;
        TextView  low;
        TextView des;
        TextView kongqi;
        ImageView tianqi_icon;
        ImageView localcity;
        TextView textviewtianqi;
        TextView difang;

        RelativeLayout bglunbo;
        public ImageView img;
        TextView textview;
        RelativeLayout show;
    }
    public  List<ADS> getList(){
        return adsListData;
    }
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        try{
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }catch (Exception e){

        }


//        Log.d("lizisong", "destory Item");
//        container.removeView((View) object);
    }
    public LinearLayout getLayout(){
        return layout;
    }
    /**
     * 用来设置原点的宽度
     * @param imageWidth
     */
    public void setWidth(int imageWidth){
        this.imagePoint = imageWidth;


    }
    /**
     * 清理缓存的数据
     */
    public synchronized void clearCache(){
//        if(listMap != null && listMap.size() > 0){
//            listMap.clear();
//        }
    }
    public void releaseImageViewResouce(ImageView imageView) {
        try{
            if (imageView == null) return;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap=null;
                }
            }
            System.gc();
        }catch (Exception e){

        }

    }
}

