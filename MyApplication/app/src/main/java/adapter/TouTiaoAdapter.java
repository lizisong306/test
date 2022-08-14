package adapter;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maidiantech.DetailsActivity;
import com.maidiantech.EarlyRef;
import com.maidiantech.R;
import com.maidiantech.SpecialActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Util.NetUtils;
import Util.PopupWindowUtil;
import Util.PrefUtils;
import Util.ScreenUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.Codes;
import entity.Posts;
import entity.Ret;
import fragment.Recommend;
import view.MyClickListener;
import view.RefreshListView;
import view.RoundImageView;
import view.ScrollBanner;

import static com.maidiantech.R.id.zx_zan;

/**
 * Created by Administrator on 2018/6/12.
 */

public class TouTiaoAdapter extends BaseAdapter {
    private Context context;
    private List<Posts> postsListData;
    private String channelName;
    private String mytitle;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String read_ids;
    private int width= MyApplication.width;
    private PopupWindow mPopupWindow;
    private int height=MyApplication.height;
    private static HashMap<String, String> listMap = new HashMap<String, String>();
    private boolean isFrist = true,isFrist1 = true;
    private int state = 0;
    public boolean biaoji = false,biaoji1 = false;
    public boolean isAnuationStaring = false;
    View view;
    View re;
    RefreshListView listView;

    public TouTiaoAdapter() {
    }

    public TouTiaoAdapter(Context context, List<Posts> postsListData, String channelName, final String mytitle, View view, RefreshListView listView,View re) {
        this.context = context;
        this.postsListData = postsListData;
        this.channelName = channelName;
        this.mytitle = mytitle;
        this.listView = listView;
        this.re = re;
        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
        this.view =view;
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(mPopupWindow != null){
//                   mPopupWindow.dismiss();
//               }
//            }
//        });
    }



    @Override
    public int getCount() {

        return postsListData.size();
    }

    @Override
    public Object getItem(int position) {
        return postsListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position       set
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final  ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.toutiaoadapter, null);
            //资讯
            holder.zx_img = (ImageView) convertView.findViewById(R.id.zx_img);
            holder.zx_title = (TextView) convertView.findViewById(R.id.zx_title);
            holder.zx_zan = (TextView) convertView.findViewById(zx_zan);
            holder.zx_look = (TextView) convertView.findViewById(R.id.zx_look);
            holder.zx_layout = (LinearLayout) convertView.findViewById(R.id.zx_layout);
            holder.zx_zt = (ImageView) convertView.findViewById(R.id.zx_zt);
            holder.zixu_zhiding = (ImageView) convertView.findViewById(R.id.zixue_zhiding);
            holder.zixu_tuijian = (ImageView) convertView.findViewById(R.id.zixun_tuijian);
            holder.zx_line_zan = (LinearLayout) convertView.findViewById(R.id.zx_line_zan);
            holder.zx_lanyuan=(TextView) convertView.findViewById(R.id.zx_lanyuan);


            //项目
            holder.xm_img = (ImageView) convertView.findViewById(R.id.xm_img);
            holder.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
            holder.xm_linyu=(TextView)convertView.findViewById(R.id.xm_linyu);
            holder.xm_dianwei = (TextView) convertView.findViewById(R.id.xm_dianwei);
            holder.xm_look = (TextView) convertView.findViewById(R.id.xm_look);
            holder.xm_layout = (LinearLayout) convertView.findViewById(R.id.xm_layout);
            holder.xm_zt = (ImageView) convertView.findViewById(R.id.xm_zt);
            holder.xiangmu_tuijian = (ImageView) convertView.findViewById(R.id.xiangmu_tuijian);
            holder.xiangmu_zhiding = (ImageView) convertView.findViewById(R.id.xiangmu_zhiding);
            holder.xm_name=(TextView) convertView.findViewById(R.id.xm_name);
            holder.xm_description=(TextView) convertView.findViewById(R.id.xm_description);


            //政策
            holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
            holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
            holder.zc_lingyu = (TextView) convertView.findViewById(R.id.zc_lingyu);
            holder.zc_look = (TextView) convertView.findViewById(R.id.zc_look);
            holder.zc_layout = (LinearLayout) convertView.findViewById(R.id.zc_layout);
            holder.zc_zt = (ImageView) convertView.findViewById(R.id.zc_zt);
            holder.zc_zhiding = (ImageView) convertView.findViewById(R.id.zhengci_zhiding);
            holder.zc_tuijian = (ImageView) convertView.findViewById(R.id.zhengci_tuijian);
            holder.zc_zan = (TextView)convertView.findViewById(R.id.zc_zan);

            //人才
            holder.rc_img = (RoundImageView) convertView.findViewById(R.id.rc_img);
            holder.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
            holder.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
            holder.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
            holder.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
            holder.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);
            holder.rc_zan = (TextView) convertView.findViewById(R.id.rc_zan);
            holder.rc_layout = (LinearLayout) convertView.findViewById(R.id.rc_layout);
            holder.rc_zt = (ImageView) convertView.findViewById(R.id.rc_zt);
            holder.rencai_zhiding = (ImageView) convertView.findViewById(R.id.rencai_zhiding);
            holder.rencai_tuijian = (ImageView) convertView.findViewById(R.id.rencai_tuijian);
            holder.rc_text=(TextView) convertView.findViewById(R.id.rc_text);
            holder.rc_yuanshi=(ImageView) convertView.findViewById(R.id.rc_yuanshi);

            //设备
            holder.device_img = (ImageView) convertView.findViewById(R.id.device_img);
            holder.device_title = (TextView) convertView.findViewById(R.id.device_title);
            holder.device_zan = (TextView) convertView.findViewById(R.id.device_zan);
            holder.device_description = (TextView)convertView.findViewById(R.id.device_description);
            holder.device_linyu =(TextView)convertView.findViewById(R.id.device_linyu);
            holder.device_look = (TextView) convertView.findViewById(R.id.device_look);
            holder.device_layout = (LinearLayout) convertView.findViewById(R.id.device_layout);
            holder.sb_zt = (ImageView) convertView.findViewById(R.id.sb_zt);
            holder.shebei_zhiding = (ImageView) convertView.findViewById(R.id.shenbei_zhiding);
            holder.shebei_tuijian = (ImageView) convertView.findViewById(R.id.shenbei_tuijian);
            holder.shebei_dianzan=(LinearLayout) convertView.findViewById(R.id.shebei_dianzan);

            //专利
            holder.zhunli_title = (TextView) convertView.findViewById(R.id.zhunli_title);
            holder.zhuanli_look = (TextView) convertView.findViewById(R.id.zhuanli_look);
            holder.zhuanli_zan  = (TextView)convertView.findViewById(R.id.zhuanli_zan) ;
            holder.zhuanli_layout = (LinearLayout) convertView.findViewById(R.id.zhuanli_layout);
            holder.zhuanli_zt = (ImageView) convertView.findViewById(R.id.zhuanli_zt);
            holder.zhuanli_tuijian = (ImageView) convertView.findViewById(R.id.zhuanli_tuijian);
            holder.zhuanli_zhiding = (ImageView) convertView.findViewById(R.id.zhuanli_zhiding);
            holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.zhuanli_linyu);
            holder.zhuanli_description=(TextView) convertView.findViewById(R.id.zhuanli_description);
            holder.zhuanli_line=(LinearLayout) convertView.findViewById(R.id.zhuanli_line);


            //实验室
            holder.librarys = (LinearLayout) convertView.findViewById(R.id.librarys);
            holder.librarys_title = (TextView) convertView.findViewById(R.id.librarys_title);
            holder.librarys_img = (ImageView) convertView.findViewById(R.id.librarys_img);
//            holder.librarys_linyu=(TextView)convertView.findViewById(R.id.librarys_linyu);
            holder.librarys_zan = (TextView) convertView.findViewById(R.id.librarys_zan);
            holder.librarys_look = (TextView) convertView.findViewById(R.id.librarys_look);
            holder.sys_zt = (ImageView) convertView.findViewById(R.id.sys_zt);
            holder.shiyanshi_zhiding = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding);
            holder.shiyanshi_tuijian = (ImageView) convertView.findViewById(R.id.shiyanshi_tuijian);
            holder.datu_name=(TextView) convertView.findViewById(R.id.datu_name);
            holder.sys_dianzan=(LinearLayout) convertView.findViewById(R.id.sys_dianzan);

            //实验室
            holder.librarys1 = (LinearLayout) convertView.findViewById(R.id.librarys1);
            holder.librarys_title1 = (TextView) convertView.findViewById(R.id.librarys_title1);
            holder.librarys_img1 = (ImageView) convertView.findViewById(R.id.librarys_img1);


            holder.shiyanshi_zhiding1 = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding1);

            //三图
            holder.tj_state_line = (LinearLayout) convertView.findViewById(R.id.tj_state_line);
            holder.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
            holder.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
            holder.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
            holder.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);
            holder.tj_state_zan = (TextView) convertView.findViewById(R.id.tj_state_zan);
            holder.tj_state_click = (TextView) convertView.findViewById(R.id.tj_state_click);
            holder.santu_zhiding = (ImageView) convertView.findViewById(R.id.santu_zhiding);
            holder.santu_tuijian = (ImageView) convertView.findViewById(R.id.santu_tuijian);
            holder.santu_name=(TextView) convertView.findViewById(R.id.santu_name);
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.tj_state_zt=(ImageView) convertView.findViewById(R.id.tj_state_zt);
            holder.state_zan = (LinearLayout) convertView.findViewById(R.id.state_zan);

            holder.device_look_lay = (LinearLayout)convertView.findViewById(R.id.device_look_lay) ;
            holder.xm_look_lay = (LinearLayout)convertView.findViewById(R.id.xm_look_lay) ;
            holder.zx_look_lay = (LinearLayout)convertView.findViewById(R.id.zx_look_lay);
            holder.tj_state_click_lay = (LinearLayout)convertView.findViewById(R.id.tj_state_click_lay) ;
            holder.librarys_look_lay = (LinearLayout)convertView.findViewById(R.id.librarys_look_lay);

            holder.shuaxin = (LinearLayout)convertView.findViewById(R.id.shuaxin);
            holder.shuaxintxt = (TextView)convertView.findViewById(R.id.shuaxintxt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {

            holder.librarys1.setVisibility(View.GONE);
            holder.shuaxin.setVisibility(View.GONE);
            holder.librarys1.setClickable(true);
            holder.librarys.setClickable(true);
            holder.tj_state_line.setClickable(true);
            holder.zx_layout.setClickable(true);
            holder.xm_layout.setClickable(true);
            holder.rc_layout.setClickable(true);
            holder.device_layout.setClickable(true);
            holder.zhuanli_layout.setClickable(true);
            if(postsListData.get(position).getTypename().equals("html")){
                holder.shuaxin.setVisibility(View.GONE);

                holder.line.setVisibility(View.VISIBLE);

                holder.librarys1.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.sys_zt.setVisibility(View.GONE);
                holder.sys_dianzan.setVisibility(View.GONE);

                holder.tj_state_line.setVisibility(View.GONE);
                holder.librarys_img1.setVisibility(View.VISIBLE);
                holder.librarys_title1.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.librarys_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.librarys_title1.setText(postsListData.get(position).getTitle());
                holder.librarys_img1.setBackgroundResource(postsListData.get(position).imageid);
                holder.shiyanshi_zhiding1.setVisibility(View.VISIBLE);


            }else if (postsListData.get(position).getTypename().equals("研究所")||postsListData.get(position).getTypename().equals("实验室")|| channelName == "研究所") {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);

                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.sys_zt.setVisibility(View.GONE);
                holder.sys_dianzan.setVisibility(View.GONE);

                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        if (postsListData.get(position).getTags().equals("置顶")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.VISIBLE);
                        } else if (postsListData.get(position).getTags().equals("特推")) {
                            holder.santu_tuijian.setVisibility(View.VISIBLE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        } else if (postsListData.get(position).getTags().equals("普通")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        }
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){
                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {

                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);
                            }
                        }
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.xm_img, options);
                                } else {
                                    holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.xm_img, options);
                            }
                        }
                        holder.xm_title.setText(postsListData.get(position).getTitle());
                        if(postsListData.get(position).getArea_cate()!=null){
                            holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        }
                        if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                            holder.xm_description.setVisibility(View.GONE);
                        }else{
                            holder.xm_description.setVisibility(View.VISIBLE);
                            holder.xm_description.setText(postsListData.get(position).getDescription());
                        }

                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.xm_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.xm_title.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.xm_name.setText(postsListData.get(position).getTypename());
                        holder.xm_dianwei.setVisibility(View.GONE);
                        holder.xm_dianwei.setText(postsListData.get(position).getZan());
                        holder.xm_look.setText("");
                        holder.line.setVisibility(View.VISIBLE);
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.librarys_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                    }
                }
                holder.librarys_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.datu_name.setText(postsListData.get(position).getTypename());
                holder.librarys_zan.setText(postsListData.get(position).getZan());
                holder.librarys_look.setText("");
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.shiyanshi_tuijian.setVisibility(View.GONE);
                    holder.shiyanshi_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.shiyanshi_tuijian.setVisibility(View.VISIBLE);
                    holder.shiyanshi_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.shiyanshi_tuijian.setVisibility(View.GONE);
                    holder.shiyanshi_zhiding.setVisibility(View.GONE);
                }
            } else if(postsListData.get(position).getTypename().equals("活动") || channelName == "活动"){
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);

                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.zx_zt.setVisibility(View.GONE);
                holder.zx_line_zan.setVisibility(View.GONE);

                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);
                            }
                        }
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zt.setVisibility(View.VISIBLE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        } else {
                            holder.sys_zt.setVisibility(View.GONE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            }
                        }
                        holder.librarys_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                        }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        holder.datu_name.setText(postsListData.get(position).getTypename());
                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText("");
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.zx_img, options);
                                } else {
                                    holder.zx_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.zx_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }


                holder.zx_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zx_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zx_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_lanyuan.setText(postsListData.get(position).getTypename());
                holder.zx_zan.setText(postsListData.get(position).getZan());
                holder.zx_look.setText("");

                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zixu_tuijian.setVisibility(View.VISIBLE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                }

            }else if(postsListData.get(position).getTypename().equals("专题")){
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);

                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.sys_zt.setVisibility(View.GONE);
                holder.sys_dianzan.setVisibility(View.GONE);

                if (postsListData.get(position).imageState != null) {
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.librarys_img.setVisibility(View.VISIBLE);
                    holder.librarys_title.setVisibility(View.VISIBLE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.VISIBLE);
                    holder.librarys1.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    if(!isAnuationStaring){
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            } else {
                                holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                            }
                        } else {
                            ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                            params.height=height;
                            params.width =width;
                            holder.librarys_img.setLayoutParams(params);
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.librarys_img, options);
                        }
                    }
                }
                holder.librarys_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.datu_name.setText(postsListData.get(position).getTypename());
                holder.librarys_zan.setText(postsListData.get(position).getZan());
                holder.librarys_look.setText("");
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.shiyanshi_tuijian.setVisibility(View.GONE);
                    holder.shiyanshi_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.shiyanshi_tuijian.setVisibility(View.VISIBLE);
                    holder.shiyanshi_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.shiyanshi_tuijian.setVisibility(View.GONE);
                    holder.shiyanshi_zhiding.setVisibility(View.GONE);
                }
                holder.sys_zt.setVisibility(View.VISIBLE);

            }else if (postsListData.get(position).getTypename().equals("资讯")|| ( postsListData.get(position).typeid!= null && (postsListData.get(position).typeid.equals("10000"))) || postsListData.get(position).getTypename().equals("推荐") || postsListData.get(position).getTypename().equals("活动")  || postsListData.get(position).getTypename().equals("知识")|| channelName == "资讯") {
                holder.librarys1.setVisibility(View.GONE);
                holder.shuaxin.setVisibility(View.GONE);

                holder.line.setVisibility(View.VISIBLE);

//                holder.line.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zx_zt.setVisibility(View.VISIBLE);
                    holder.zx_line_zan.setVisibility(View.GONE);
                } else {
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.zx_line_zan.setVisibility(View.GONE);
                }
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zixu_tuijian.setVisibility(View.VISIBLE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.line.setVisibility(View.VISIBLE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.tj_state_zt.setVisibility(View.VISIBLE);
                        } else {
                            holder.tj_state_zt.setVisibility(View.GONE);
                        }
                        if (postsListData.get(position).getTags().equals("置顶")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.VISIBLE);
                        } else if (postsListData.get(position).getTags().equals("特推")) {
                            holder.santu_tuijian.setVisibility(View.VISIBLE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        } else if (postsListData.get(position).getTags().equals("普通")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        }
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());

                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){
                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);

                            }
                        }
                        if(postsListData.get(position).source != null && !postsListData.get(position).source.equals("")){
                            holder.santu_name.setText(postsListData.get(position).source);
                        }else{
                            holder.santu_name.setText("钛领科技");
                        }
//                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");


                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zt.setVisibility(View.VISIBLE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        } else {
                            holder.sys_zt.setVisibility(View.GONE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        }
                        if (postsListData.get(position).getTags().equals("置顶")) {
                            holder.shiyanshi_tuijian.setVisibility(View.GONE);
                            holder.shiyanshi_zhiding.setVisibility(View.VISIBLE);
                        } else if (postsListData.get(position).getTags().equals("特推")) {
                            holder.shiyanshi_tuijian.setVisibility(View.VISIBLE);
                            holder.shiyanshi_zhiding.setVisibility(View.GONE);
                        } else if (postsListData.get(position).getTags().equals("普通")) {
                            holder.shiyanshi_tuijian.setVisibility(View.GONE);
                            holder.shiyanshi_zhiding.setVisibility(View.GONE);
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {


                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            }
                        }
                        holder.librarys_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){
                            holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                        }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        if(postsListData.get(position).source != null && !postsListData.get(position).source.equals("")){
                            holder.datu_name.setText(postsListData.get(position).source);
                        }else{
                            holder.datu_name.setText("钛领科技");
                        }

                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText("");
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.zx_img, options);
                                } else {
                                    holder.zx_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.zx_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }

                if(postsListData.get(position).getTypename().equals("专题")){
                    holder.zx_lanyuan.setVisibility(View.GONE);
                }else{
                    holder.zx_lanyuan.setVisibility(View.VISIBLE);
                    if(postsListData.get(position).source != null && !postsListData.get(position).source.equals("")){
                        holder.zx_lanyuan.setText(postsListData.get(position).source);
                    }else{
                        holder.zx_lanyuan.setText("钛领科技");
                    }
//                   holder.zx_lanyuan.setText(postsListData.get(position).getTypename());
                }
                holder.zx_title.setText(postsListData.get(position).getTitle());

                holder.zx_zan.setText(postsListData.get(position).getZan());
                holder.zx_look.setText("");
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zx_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zx_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.line.setVisibility(View.VISIBLE);

            } else if (postsListData.get(position).getTypename().equals("项目") || channelName == "项目") {
                holder.librarys1.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.VISIBLE);
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.xm_zt.setVisibility(View.GONE);



                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.librarys1.setVisibility(View.GONE);
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.xm_title.setVisibility(View.GONE);
                        holder.xm_img.setVisibility(View.GONE);
                        if (postsListData.get(position).getTags().equals("置顶")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.VISIBLE);
                        } else if (postsListData.get(position).getTags().equals("特推")) {
                            holder.santu_tuijian.setVisibility(View.VISIBLE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        } else if (postsListData.get(position).getTags().equals("普通")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        }
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);
                            }
                        }
                        holder.santu_name.setText(postsListData.get(position).getTypename());

                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.librarys1.setVisibility(View.GONE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zt.setVisibility(View.VISIBLE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        } else {
                            holder.sys_zt.setVisibility(View.GONE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            }
                        }
                        holder.librarys_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                        }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        holder.datu_name.setText(postsListData.get(position).getTypename());
                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText("");
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.librarys1.setVisibility(View.GONE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
                        holder.xm_img.setVisibility(View.VISIBLE);
                        holder.xm_title.setVisibility(View.VISIBLE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.xm_img, options);
                                } else {
    //                                holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.xm_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.librarys1.setVisibility(View.GONE);
                        holder.xm_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
                        holder.xm_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }

                holder.xm_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null){
                    holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                }
                if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                    holder.xm_description.setVisibility(View.GONE);
                }else{
                    holder.xm_description.setVisibility(View.VISIBLE);
                    holder.xm_description.setText(postsListData.get(position).getDescription());
                }

                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_name.setText(postsListData.get(position).getTypename());
                if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                    holder.xm_dianwei.setVisibility(View.GONE);
                }else{
                    holder.xm_dianwei.setVisibility(View.VISIBLE);
                    holder.xm_dianwei.setText(postsListData.get(position).getSource());
                    Log.d("lizisong", "state:"+this.state);
                    if(this.state == 1){
                        holder.xm_dianwei.setVisibility(View.VISIBLE);
                    }else{
                        holder.xm_dianwei.setVisibility(View.GONE);
                    }
                }
//                holder.xm_dianwei.setText(postsListData.get(position).getZan());
                holder.xm_look.setText("");
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.xiangmu_tuijian.setVisibility(View.GONE);
                    holder.xiangmu_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.xiangmu_tuijian.setVisibility(View.VISIBLE);
                    holder.xiangmu_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.xiangmu_tuijian.setVisibility(View.GONE);
                    holder.xiangmu_zhiding.setVisibility(View.GONE);
                }
            } else if (postsListData.get(position).getTypename().equals("政策")|| channelName == "政策") {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.VISIBLE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.zc_zt.setVisibility(View.GONE);
                holder.zc_zan.setVisibility(View.GONE);

                holder.zc_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description.setText(postsListData.get(position).getDescription());
                holder.zc_look.setText("");
                holder.zc_zan.setText(postsListData.get(position).getZan());
                holder.zc_lingyu.setText(postsListData.get(position).getTypename());
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zc_tuijian.setVisibility(View.VISIBLE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                }

            } else if (postsListData.get(position).getTypename().equals("人才") || postsListData.get(position).getTypename().equals("专家")) {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.rc_zt.setVisibility(View.GONE);

                holder.rc_uname.setText(postsListData.get(position).getUsername());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.rc_uname.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.rc_uname.setTextColor(Color.parseColor("#777777"));
                }
                holder.rc_title.setText(postsListData.get(position).getDescription());
                holder.rc_zhicheng.setText(postsListData.get(position).getRank());
                if(postsListData.get(position).getIs_academician()!=null){
                    if(postsListData.get(position).getIs_academician().equals("1")){
                        holder.rc_yuanshi.setVisibility(View.VISIBLE);
                    }else if(postsListData.get(position).getIs_academician().equals("0")){
                        holder.rc_yuanshi.setVisibility(View.GONE);
                    }
                }

                Area_cate area_cate = postsListData.get(position).getArea_cate();
                if (area_cate != null ) {
                    if(area_cate.getArea_cate1().equals("")){
                        holder.rc_text.setVisibility(View.GONE);
                        holder.rc_linyu.setVisibility(View.GONE);
                    }else{
                        holder.rc_text.setVisibility(View.VISIBLE);
                        holder.rc_linyu.setVisibility(View.VISIBLE);
                        holder.rc_linyu.setText(area_cate.getArea_cate1());
                    }
                }else{
                    holder.rc_text.setVisibility(View.GONE);
                    holder.rc_linyu.setVisibility(View.GONE);
                }
                if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                    holder.rc_zan.setVisibility(View.GONE);
                }else{
                    holder.rc_zan.setVisibility(View.VISIBLE);
                    holder.rc_zan.setText(postsListData.get(position).getSource());
                    if(this.state == 1){
                        Log.d("lizisong", "state:"+this.state);
                        holder.rc_zan.setVisibility(View.VISIBLE);
                    }else {
                        holder.rc_zan.setVisibility(View.GONE);
                    }
                }

                holder.rc_look.setText("");
                if (postsListData.get(position).getLitpic().equals("")) {
                    holder.rc_img.setVisibility(View.GONE);
                } else {
                    holder.rc_img.setVisibility(View.VISIBLE);
                    if(!isAnuationStaring){
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                        , holder.rc_img, options);
                            } else {
                                holder.rc_img.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                    , holder.rc_img, options);
                        }
                    }
                }
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.rencai_tuijian.setVisibility(View.GONE);
                    holder.rencai_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.rencai_tuijian.setVisibility(View.VISIBLE);
                    holder.rencai_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.rencai_tuijian.setVisibility(View.GONE);
                    holder.rencai_zhiding.setVisibility(View.GONE);
                }
            } else if (postsListData.get(position).getTypename().equals("设备") || channelName == "设备") {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.shebei_dianzan.setVisibility(View.GONE);

                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.device_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.device_title.setVisibility(View.GONE);
                        holder.device_img.setVisibility(View.GONE);
                        if (postsListData.get(position).getTags().equals("置顶")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.VISIBLE);
                        } else if (postsListData.get(position).getTags().equals("特推")) {
                            holder.santu_tuijian.setVisibility(View.VISIBLE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        } else if (postsListData.get(position).getTags().equals("普通")) {
                            holder.santu_tuijian.setVisibility(View.GONE);
                            holder.santu_zhiding.setVisibility(View.GONE);
                        }
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }
                        if(isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
    //                                holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
    //                                holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
    //                                holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);
                            }
                        }
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zt.setVisibility(View.VISIBLE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        } else {
                            holder.sys_zt.setVisibility(View.GONE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        }
                        if(isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                            }
                        }
                        holder.librarys_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                        }
//           holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        holder.datu_name.setText(postsListData.get(position).getTypename());
                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText("");
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.VISIBLE);
                        holder.device_img.setVisibility(View.VISIBLE);
                        holder.device_title.setVisibility(View.VISIBLE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.device_img, options);
                                } else {
                                    holder.device_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.device_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.device_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.VISIBLE);
                        holder.device_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }
                holder.device_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null){
                    holder.device_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                }
                if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                    holder.device_description.setVisibility(View.GONE);
                }else{
                    holder.device_description.setVisibility(View.VISIBLE);
                    holder.device_description.setText(postsListData.get(position).getDescription());
                }

                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.device_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.device_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.device_zan.setText(postsListData.get(position).getZan());
                holder.device_look.setText("");
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.shebei_tuijian.setVisibility(View.GONE);
                    holder.shebei_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.shebei_tuijian.setVisibility(View.VISIBLE);
                    holder.shebei_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.shebei_tuijian.setVisibility(View.GONE);
                    holder.shebei_zhiding.setVisibility(View.GONE);
                }

            } else if (postsListData.get(position).getTypename().equals("专利") || channelName == "专利") {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.zhuanli_zt.setVisibility(View.GONE);

                holder.zhunli_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null ){
                    if(postsListData.get(position).getArea_cate().getArea_cate1()==null || postsListData.get(position).getArea_cate().getArea_cate1().equals("")){
                        holder.zhuanli_line.setVisibility(View.GONE);
                    }else{
                        holder.zhuanli_line.setVisibility(View.VISIBLE);
                        holder.zhuanli_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                    }
                }
                if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                    holder.zhuanli_description.setVisibility(View.GONE);
                }else{
                    holder.zhuanli_description.setVisibility(View.VISIBLE);
                    holder.zhuanli_description.setText(postsListData.get(position).getDescription());
                }
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zhunli_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zhunli_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_look.setText("");
                holder.zhuanli_zan.setText(postsListData.get(position).getZan());
                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zhuanli_tuijian.setVisibility(View.GONE);
                    holder.zhuanli_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zhuanli_tuijian.setVisibility(View.VISIBLE);
                    holder.zhuanli_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zhuanli_tuijian.setVisibility(View.GONE);
                    holder.zhuanli_zhiding.setVisibility(View.GONE);
                }
            } else if(postsListData.get(position).getTypename().equals("刷新")){
                holder.shuaxin.setVisibility(View.VISIBLE);
                holder.librarys1.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
//                if( position < postsListData.size() - 1){
//                    postsListData.get(position+1).xianstate = 1;
//                }
                long sys = (System.currentTimeMillis()-SharedPreferencesUtil.getLong(SharedPreferencesUtil.SHUAXIN_SHIJIAN,0))/1000;
                String time = TimeUtil.getTimeFormatText(new Date(SharedPreferencesUtil.getLong(SharedPreferencesUtil.SHUAXIN_SHIJIAN,0)), sys+"");
                holder.shuaxintxt.setText(time+"看到这里，点击刷新");
                holder.shuaxin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        listView.smoothScrollToPosition(0);
                        re.setVisibility(View.GONE);
                        listView.setSelection(0);
                        biaoji = true;
                        biaoji1 = true;
                        if(biaoji1){
                            biaoji1 = false;
                            if(isFrist1){
                                handler.sendEmptyMessageDelayed(11,50);
                            }else{
                                handler.sendEmptyMessageDelayed(11,50);
                            }
                        }
                        listView.setOnScrollListener(listener);

                    }
                });


            }else {
                holder.shuaxin.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys1.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys1.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zx_zt.setVisibility(View.VISIBLE);
                    holder.zx_line_zan.setVisibility(View.GONE);
                } else {
                    holder.zx_zt.setVisibility(View.GONE);
                    holder.zx_line_zan.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.tj_state_zt.setVisibility(View.VISIBLE);
                            holder.state_zan.setVisibility(View.GONE);
                        } else {
                            holder.tj_state_zt.setVisibility(View.GONE);
                            holder.state_zan.setVisibility(View.GONE);
                        }
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                        }

                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.tj_state_img1, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                            , holder.tj_state_img2, options);
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                            , holder.tj_state_img3, options);
                                } else {
                                    holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                                    holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.tj_state_img1, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image2
                                        , holder.tj_state_img2, options);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image3
                                        , holder.tj_state_img3, options);
                            }
                        }
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText("");

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zt.setVisibility(View.VISIBLE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        } else {
                            holder.sys_zt.setVisibility(View.GONE);
                            holder.sys_dianzan.setVisibility(View.GONE);
                        }
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.librarys_img, options);
                                } else {
                                    holder.librarys_img.setBackgroundResource(R.mipmap.datu);
                                }
                            } else {
                                ViewGroup.LayoutParams params = holder.librarys_img.getLayoutParams();
                                params.height=height;
                                params.width =width;
                                holder.librarys_img.setLayoutParams(params);
                                if(!isAnuationStaring){
                                  ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.librarys_img, options);
                                }
                            }
                        }
                        holder.librarys_title.setText(postsListData.get(position).getTitle());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.librarys_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.librarys_title.setTextColor(Color.parseColor("#777777"));
                        }
//                      holder.librarys_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        holder.datu_name.setText(postsListData.get(position).getTypename());
                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText("");
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        if(!isAnuationStaring){
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                            , holder.zx_img, options);
                                } else {
                                    holder.zx_img.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.zx_img, options);
                            }
                        }
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }

                holder.zx_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.zx_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zx_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_lanyuan.setText(postsListData.get(position).getTypename());
                holder.zx_zan.setText(postsListData.get(position).getZan());
                holder.zx_look.setText("");
                if(postsListData.get(position).getTypename().equals("科讯早参")){
                    postsListData.get(position).setTypename("科讯早参");
                }else{
                    postsListData.get(position).setTypename("资讯");
                }

                holder.line.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zixu_tuijian.setVisibility(View.VISIBLE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zixu_tuijian.setVisibility(View.GONE);
                    holder.zixu_zhiding.setVisibility(View.GONE);
                }
            }
            holder.zx_look_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow( holder.zx_look,postsListData.get(position) );

                }
            });
            holder.xm_look_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow( holder.xm_look,postsListData.get(position));
                }
            });

            holder.device_look_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow( holder.device_look,postsListData.get(position));
                }
            });

            holder.librarys_look_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow( holder.librarys_look,postsListData.get(position));
                }
            });
            holder.tj_state_click_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.VISIBLE);
                    showPopupWindow( holder.tj_state_click,postsListData.get(position));
                }
            });

            holder.tj_state_line.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));


            holder.librarys1.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));
            holder.librarys.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));

            holder.zx_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));
            holder.xm_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));

            holder.zc_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));


            holder.rc_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));

            holder.device_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));

            holder.zhuanli_layout.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }
                        Posts item = postsListData.get(position);
                        PrefUtils.setString(context,item.getId(),item.getId());
                        if(item.getTypename().equals("科讯早参")){
                            Intent intent = new Intent(context, EarlyRef.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);

                        }else if(item.getTypename().equals("html")|| item.typeid.equals("10000")){
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("url", item.detail);
                            context.startActivity(intent);
                        }else if (item.getTypename().equals("专题")) {
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", item.getId());
                            context.startActivity(intent);
                        } else {

                            String name = item.getTypename();
                            if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                Intent intent = new Intent(context, ZixunDetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("name", item.getTypename());
                                intent.putExtra("pic", item.getLitpic());
                                context.startActivity(intent);
                            }

                        }
                    } catch (IndexOutOfBoundsException ex) {

                    } catch (Exception e) {
                    }
                }

                @Override
                public void doubleClick() {

                }
            }));


        } catch (Exception e) {
        }

        return convertView;
    }

    /**
     *
     */
    class ViewHolder {
        LinearLayout device_look_lay,xm_look_lay,zx_look_lay,tj_state_click_lay,librarys_look_lay;
        //资讯
        ImageView zx_img;
        TextView zx_title;
        TextView zx_description;
        TextView zx_lanyuan;
        TextView zx_update;
        TextView zx_zan;
        TextView zx_look;
        LinearLayout zx_layout;
        ImageView zx_zt;
        ImageView zixu_zhiding;
        ImageView zixu_tuijian;
        LinearLayout zx_line_zan;

        //项目
        ImageView xm_img;
        TextView xm_title;
        TextView xm_linyu;
        TextView xm_dianwei;
        TextView xm_look;
        TextView xm_zan;
        LinearLayout xm_layout;
        ImageView xm_zt;
        ImageView xiangmu_zhiding;
        ImageView xiangmu_tuijian;
        TextView xm_name;
        TextView xm_description;

        //政策
        TextView zc_title;
        TextView zc_description;
        TextView zc_lingyu;
        TextView zc_look;
        TextView zc_zan;
        LinearLayout zc_layout;
        ImageView zc_zt;
        ImageView zc_zhiding;
        ImageView zc_tuijian;

        //人才
        TextView rc_uname;
        RoundImageView rc_img;
        TextView rc_zhicheng;
        TextView rc_linyu;
        TextView rc_title;
        TextView rc_zan;
        TextView rc_look;
        LinearLayout rc_layout;
        ImageView rc_zt;
        ImageView rencai_zhiding;
        ImageView rencai_tuijian;
        TextView rc_text;
        ImageView rc_yuanshi;

        //设备
        ImageView device_img;
        TextView device_title;
        TextView device_linyu;
        TextView device_description;
        TextView device_zan;
        TextView device_look;
        LinearLayout device_layout;
        ImageView sb_zt;
        ImageView shebei_zhiding;
        ImageView shebei_tuijian;
        LinearLayout shebei_dianzan;

        //专利
        TextView zhunli_title;
        TextView zhuanli_linyu;
        TextView zhuanli_jiduan;
        TextView zhuanli_look;
        TextView zhuanli_zan;
        LinearLayout zhuanli_layout;
        ImageView zhuanli_zt;
        ImageView zhuanli_zhiding;
        ImageView zhuanli_tuijian;
        TextView zhuanli_description;
        LinearLayout zhuanli_line;

        //实验室
        LinearLayout librarys;
        TextView librarys_title;
        TextView librarys_linyu;
        ImageView librarys_img;
        TextView librarys_zan;
        TextView librarys_look;
        ImageView sys_zt;
        ImageView shiyanshi_zhiding;
        ImageView shiyanshi_tuijian;
        TextView datu_name;
        LinearLayout sys_dianzan;
        //三图
        LinearLayout tj_state_line;
        TextView tj_state_title;
        ImageView tj_state_img1;
        ImageView tj_state_img2;
        ImageView tj_state_img3;
        TextView tj_state_zan;
        TextView tj_state_click;
        ImageView santu_zhiding;
        ImageView santu_tuijian;
        TextView santu_name;
        ImageView tj_state_zt;
        LinearLayout state_zan;
        //test
        LinearLayout librarys1;
        TextView librarys_title1;
        TextView librarys_linyu1;
        ImageView librarys_img1;
        TextView librarys_zan1;
        TextView librarys_look1;
        ImageView sys_zt1;
        ImageView shiyanshi_zhiding1;
        ImageView shiyanshi_tuijian1;
        TextView datu_name1;
        LinearLayout sys_dianzan1;

        //        线
        TextView line;
        // 刷新
        LinearLayout shuaxin;
        TextView shuaxintxt;
    }

    /**
     * 用来标记是否显示来源
     * @param state
     */
    public void setState(int state){
        this.state = state;
    }


    private void showPopupWindow(View anchorView,Posts item) {
        View contentView = getPopupWindowContentView(item);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view.setVisibility(View.GONE);
            }
        });
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = calculatePopWindowPos(anchorView, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;

        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    private View getPopupWindowContentView(final  Posts item) {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(context).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                if(v.getId() == R.id.menu_item1){
                    dislikesocure("source",item.getId());
                    postsListData.remove(item);
                    notifyDataSetChanged();

                }else if(v.getId() == R.id.menu_item3){
                    dislikesocure("post",item.getId());
                    postsListData.remove(item);
                    notifyDataSetChanged();
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    view.setVisibility(View.GONE);
                }
            }
        };
//        LinearLayout.LayoutParams paramTest = (LinearLayout.LayoutParams) contentView.getLayoutParams();
//        paramTest.leftMargin = 50;
//        paramTest.rightMargin = 50;
//        contentView.setLayoutParams(paramTest);
        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);
//        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item3).setOnClickListener(menuItemOnClickListener);
//        contentView.findViewById(R.id.menu_item4).setOnClickListener(menuItemOnClickListener);
//        contentView.findViewById(R.id.menu_item5).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }


    public  int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        LinearLayout tankuang = (LinearLayout)contentView.findViewById(R.id.tankuang);
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight-230 < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight-80;
            tankuang.setBackgroundResource(R.mipmap.down_iconbg);
            TextView menu1 = (TextView)contentView.findViewById(R.id.menu_item1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)menu1.getLayoutParams();
            layoutParams.topMargin=0;
            menu1.setLayoutParams(layoutParams);

        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
            tankuang.setBackgroundResource(R.mipmap.up_iconbg);
        }
        return windowPos;
    }

    private void dislikesocure(String dislike,String aid){
        String url="http://"+MyApplication.ip+"/api/setLinkRedirect.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("aid", aid);
        map.put("disliketype",dislike);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,0,0);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String ret = (String)msg.obj;
                Gson gs = new Gson();
                Ret g = gs.fromJson(ret, Ret.class);
                if(g != null){
                    if(g.code.equals("1")){

                    }
                }
            }
            if(msg.what == 10){
//                listView.getSc
                Log.d("lizisong", "listView:ddddddddd");
                listView.pulldow();

                if(listView.getMessage() == 0){
                    if(isFrist){
                        listView.pulldow();
                        listView.pulldow();
                        handler.sendEmptyMessage(10);
                    }else{
                        handler.sendEmptyMessageDelayed(10,50);
                    }
                }else if(listView.getMessage() == 1){
                    isFrist = false;
                    handler.removeMessages(10);
                    listView.pullup();
                    Intent intent = new Intent();
                    intent.setAction("action_gogogo");
                    context.sendBroadcast(intent);
                }
            }
            if(msg.what == 11){
                Log.d("lizisong", "listView:ddddddddd");
                listView.pulldow();
                if(listView.getMessage() == 0){
                    if(isFrist1){
                        listView.pulldow();
                        listView.pulldow();
                        handler.sendEmptyMessage(11);
                    }else{
                        handler.sendEmptyMessageDelayed(11,50);
                    }
                }else if(listView.getMessage() == 1){
                    isFrist1 = false;
                    handler.removeMessages(11);
                    listView.pullup();
                    Intent intent = new Intent();
                    intent.setAction("action_gogogo");
                    context.sendBroadcast(intent);
                }
            }
        }
    };

    private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            handler.removeMessages(11);
            if(firstVisibleItem  >= 1){
//                Log.d("lizisong", "kkkkkkkkk");
//                                        showAnimator();
//                                        showAlpha();

            }else{
                Log.d("lizisong", "gogogogogogogogogogogogogogogogogo");

                if(biaoji){
                    biaoji = false;
                    if(isFrist){
                        handler.sendEmptyMessage(10);
                    }else{
                        handler.sendEmptyMessageDelayed(10,50);
                    }
                }
            }
        }
    } ;

    public void shuaxin(){
//        listView.smoothScrollToPosition(0);
        listView.setSelection(0);
        biaoji = true;
        biaoji1 = true;
        listView.setOnScrollListener(listener);
    }

    public void shuaxin1(){
//        listView.smoothScrollToPosition(0);
        listView.setSelection(0);
        biaoji = true;
        biaoji1 = true;
        if(biaoji1){
            biaoji1 = false;
            if(isFrist1){
                handler.sendEmptyMessageDelayed(11,50);
            }else{
                handler.sendEmptyMessageDelayed(11,50);
            }
        }
        listView.setOnScrollListener(listener);
    }
    public void setFrist(boolean isFrist){
           this.isFrist = isFrist;
    }
    public void setIsAnuation(boolean isAnuation){
        this.isAnuationStaring = isAnuation;
    }


}
