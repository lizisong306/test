package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import Util.NetUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Area_cate;
import entity.Posts;
import view.CustomTextView;
import view.MyAutoTextView;
import view.RoundImageView;

import static com.maidiantech.R.id.zx_zan;


/**
 * Created by 13520 on 2016/12/8.
 */

public class RecommendAdapter extends BaseAdapter {
    private Context context;
    private List<Posts> postsListData;
    private String channelName;
    private String mytitle;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String read_ids;
    private int width= MyApplication.width;
    private int height=MyApplication.height;
    private static HashMap<String, String> listMap = new HashMap<String, String>();
    private int state = 0;

    public RecommendAdapter() {
    }

    public RecommendAdapter(Context context, List<Posts> postsListData, String channelName, String mytitle) {
        this.context = context;
        this.postsListData = postsListData;
        this.channelName = channelName;
        this.mytitle = mytitle;
        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.recommend_item, null);
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
            holder.source = (TextView)convertView.findViewById(R.id.source);

            //政策
            holder.zc_title = (CustomTextView) convertView.findViewById(R.id.zc_title);
            holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
            holder.zc_lingyu = (TextView) convertView.findViewById(R.id.zc_lingyu);
            holder.zc_look = (TextView) convertView.findViewById(R.id.zc_look);
            holder.zc_layout = (LinearLayout) convertView.findViewById(R.id.zc_layout);
            holder.zc_zt = (ImageView) convertView.findViewById(R.id.zc_zt);
            holder.zc_zhiding = (ImageView) convertView.findViewById(R.id.zhengci_zhiding);
            holder.zc_tuijian = (ImageView) convertView.findViewById(R.id.zhengci_tuijian);
            holder.zc_zan = (TextView)convertView.findViewById(R.id.zc_zan);
            holder.zhengcetuijian = (ImageView)convertView.findViewById(R.id.zhengcetuijian);

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
            holder.rank = (TextView)convertView.findViewById(R.id.rank);

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
            holder.sb_dianwei    = (TextView)convertView.findViewById(R.id.sb_dianwei);

            //专利
            holder.zhunli_title = (CustomTextView) convertView.findViewById(R.id.zhunli_title);
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
            holder.lingyu = (LinearLayout)convertView.findViewById(R.id.lingyu);
            //实验室
            holder.librarys1 = (LinearLayout) convertView.findViewById(R.id.librarys1);
            holder.librarys_title1 = (TextView) convertView.findViewById(R.id.librarys_title1);
            holder.librarys_img1 = (ImageView) convertView.findViewById(R.id.librarys_img1);
            holder.zc_linyu = (TextView)convertView.findViewById(R.id.zc_linyu) ;
            holder.zx_look_icon = (ImageView)convertView.findViewById(R.id.zx_look_icon);
            holder.tj_state_icon = (ImageView)convertView.findViewById(R.id.tj_state_icon);
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
            holder.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {

            holder.librarys1.setVisibility(View.GONE);
            if(postsListData.get(position).getTypename().equals("底线")){
                holder.librarys1.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
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
                holder.dixian.setVisibility(View.VISIBLE);
            }else if(postsListData.get(position).getTypename().equals("html")){
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
                holder.dixian.setVisibility(View.GONE);
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
                holder.librarys1.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
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
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
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
                        holder.xm_look.setText(postsListData.get(position).getClick());
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
                holder.librarys_look.setText(postsListData.get(position).getClick());
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
                holder.librarys1.setVisibility(View.GONE);
                    holder.zx_layout.setVisibility(View.VISIBLE);
                    holder.librarys.setVisibility(View.GONE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.dixian.setVisibility(View.GONE);
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
                            holder.santu_name.setText(postsListData.get(position).getTypename());
                            holder.tj_state_zan.setText(postsListData.get(position).getZan());
                            holder.tj_state_click.setText(postsListData.get(position).getClick());

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
                            holder.librarys_look.setText(postsListData.get(position).getClick());
                        } else if (postsListData.get(position).imageState.equals("0")) {
                            holder.tj_state_line.setVisibility(View.GONE);
                            holder.librarys.setVisibility(View.GONE);
                            holder.zx_img.setVisibility(View.VISIBLE);
                            holder.zx_title.setVisibility(View.VISIBLE);
                            holder.zx_layout.setVisibility(View.VISIBLE);
                            holder.librarys1.setVisibility(View.GONE);
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
                    holder.zx_look.setText(postsListData.get(position).getClick());

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
                    holder.librarys1.setVisibility(View.GONE);
                    holder.librarys.setVisibility(View.VISIBLE);
                    holder.zx_layout.setVisibility(View.GONE);
                    holder.zc_layout.setVisibility(View.GONE);
                    holder.xm_layout.setVisibility(View.GONE);
                    holder.dixian.setVisibility(View.GONE);
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
                    holder.datu_name.setText(postsListData.get(position).getTypename());
                    holder.librarys_zan.setText(postsListData.get(position).getZan());
                    holder.librarys_look.setText(postsListData.get(position).getClick());
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
                holder.line.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_look_icon.setVisibility(View.INVISIBLE);
                holder.tj_state_icon.setVisibility(View.INVISIBLE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                        if(postsListData.get(position).source != null && !postsListData.get(position).source.equals("")){
                            holder.santu_name.setText(postsListData.get(position).source);
                        }else{
                            holder.santu_name.setText("钛领科技");
                        }
                        holder.santu_name.setTextColor(this.context.getResources().getColor(R.color.qianhuangse));
//                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());
                        holder.tj_state_click.setVisibility(View.INVISIBLE);

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
                        holder.datu_name.setTextColor(this.context.getResources().getColor(R.color.qianhuangse));

                        holder.librarys_zan.setText(postsListData.get(position).getZan());
                        holder.librarys_look.setText(postsListData.get(position).getClick());
                        holder.librarys_look.setVisibility(View.INVISIBLE);
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
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
                   holder.zx_lanyuan.setTextColor(this.context.getResources().getColor(R.color.qianhuangse));
//                   holder.zx_lanyuan.setText(postsListData.get(position).getTypename());
               }
                holder.zx_title.setText(postsListData.get(position).getTitle());

                holder.zx_zan.setText(postsListData.get(position).getZan());
                holder.zx_look.setText(postsListData.get(position).getClick());
                holder.zx_look.setVisibility(View.INVISIBLE);
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
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                        holder.santu_name.setText(postsListData.get(position).getTypename());

                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

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
                        holder.librarys_look.setText(postsListData.get(position).getClick());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.librarys1.setVisibility(View.GONE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
                        holder.xm_img.setVisibility(View.VISIBLE);
                        holder.xm_title.setVisibility(View.VISIBLE);
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

                holder.xm_look.setText(postsListData.get(position).getClick());
                if(postsListData.get(position).labels != null && (!postsListData.get(position).labels.equals(""))){
                    holder.source.setVisibility(View.VISIBLE);
                    holder.source.setText(postsListData.get(position).labels);
                    if(postsListData.get(position).labels.equals("精品项目")){
                        holder.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }else if(postsListData.get(position).labels.equals("钛领推荐")){
                        holder.source.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                    }else if(postsListData.get(position).labels.equals("国家科学基金")){
                        holder.source.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                    }else{
                        holder.source.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                    }
                }else{
                    holder.source.setVisibility(View.GONE);
                }
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
                holder.dixian.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.zc_zt.setVisibility(View.GONE);
                holder.zc_zan.setVisibility(View.GONE);
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.zc_title.setTextColor(Color.parseColor("#181818"));
                    holder.zc_title.setMYTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title.setTextColor(Color.parseColor("#777777"));
                    holder.zc_title.setMYTextColor(Color.parseColor("#777777"));
                }

                holder.zc_title.setText(postsListData.get(position).getTitle().replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ","").replaceAll("  ",""));


                holder.zc_description.setText(postsListData.get(position).getDescription());
                holder.zc_look.setText(postsListData.get(position).getClick());
                holder.zc_zan.setText(postsListData.get(position).getZan());
                holder.zc_lingyu.setText(postsListData.get(position).getTypename());
                if(postsListData.get(position).region_text != null){
                    holder.lingyu.setVisibility(View.VISIBLE);
                    holder.zc_linyu.setText(postsListData.get(position).region_text);
                }else{
                    holder.lingyu.setVisibility(View.GONE);
                }
                holder.line.setVisibility(View.GONE);
                holder.zhengcetuijian.setVisibility(View.GONE);
                if (postsListData.get(position).getTags().equals("置顶")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zhengcetuijian.setVisibility(View.VISIBLE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                }

            } else if (postsListData.get(position).getTypename().equals("人才") || postsListData.get(position).getTypename().equals("专家")) {
                holder.librarys1.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                        holder.rc_yuanshi.setVisibility(View.GONE);
                    }else if(postsListData.get(position).getIs_academician().equals("0")){
                        holder.rc_yuanshi.setVisibility(View.GONE);
                    }
                }

                String area_cate = postsListData.get(position).unit;
                if (area_cate != null ) {
                    if(area_cate.equals("")){
                        holder.rc_text.setVisibility(View.GONE);
                        holder.rc_linyu.setVisibility(View.GONE);
                    }else{
                        holder.rc_text.setVisibility(View.GONE);
                        holder.rc_linyu.setVisibility(View.VISIBLE);
                        holder.rc_linyu.setText(area_cate);
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

                holder.rc_look.setText(postsListData.get(position).getClick());
                if (postsListData.get(position).getLitpic().equals("")) {
                    holder.rc_img.setVisibility(View.VISIBLE);
                    holder.rc_img.setImageResource(R.mipmap.img_rencai);
//                    holder.rc_img.setBackgroundResource(R.mipmap.img_rencai);
                } else {
                    holder.rc_img.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                            , holder.rc_img, options);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                    , holder.rc_img, options);
                        } else {
                            holder.rc_img.setBackgroundResource(R.mipmap.img_rencai);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                , holder.rc_img, options);
                    }
                }
                holder.line.setVisibility(View.GONE);
                holder.rank.setVisibility(View.VISIBLE);
                holder.rank.setText(postsListData.get(position).getRank());
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
                holder.librarys1.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

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
                        holder.librarys_look.setText(postsListData.get(position).getClick());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.VISIBLE);
                        holder.device_img.setVisibility(View.VISIBLE);
                        holder.device_title.setVisibility(View.VISIBLE);
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
                holder.device_look.setText(postsListData.get(position).getClick());
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
                if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                    holder.xm_dianwei.setVisibility(View.GONE);
                }else{
                    if(this.state == 1){
                        holder.sb_dianwei.setVisibility(View.VISIBLE);
                        holder.sb_dianwei.setText(postsListData.get(position).getSource());
                    }else{
                        holder.sb_dianwei.setVisibility(View.GONE);
                    }
                }


            } else if (postsListData.get(position).getTypename().equals("专利") || channelName == "专利") {
                holder.librarys1.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.dixian.setVisibility(View.GONE);
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
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.zhunli_title.setTextColor(Color.parseColor("#181818"));
                    holder.zhunli_title.setMYTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zhunli_title.setTextColor(Color.parseColor("#777777"));
                    holder.zhunli_title.setMYTextColor(Color.parseColor("#777777"));
                }

                holder.zhunli_title.setText(postsListData.get(position).getTitle().replaceAll("\r\n","").replaceAll("\n","").replaceAll(" ","").replaceAll("  ",""));
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
                    holder.zhuanli_description.setVisibility(View.GONE);
                    holder.zhuanli_description.setText(postsListData.get(position).getDescription().replaceAll("\r\n", "").replaceAll("\n", ""));
                }

                holder.zhuanli_look.setText(postsListData.get(position).getClick());
                holder.zhuanli_zan.setText(postsListData.get(position).getZan());
                holder.line.setVisibility(View.GONE);
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
            }else {
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
                        holder.santu_name.setText(postsListData.get(position).getTypename());
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

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
                        holder.librarys_look.setText(postsListData.get(position).getClick());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.librarys1.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.VISIBLE);
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
                holder.zx_look.setText(postsListData.get(position).getClick());
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
        } catch (Exception e) {
        }

        return convertView;
    }

    /**
     *
     */
    class ViewHolder {
        //资讯
        ImageView zx_img;
        TextView zx_title;
        TextView zx_description;
        TextView zx_lanyuan;
        TextView zx_update;
        TextView zx_zan;
        TextView zx_look;
        ImageView zx_look_icon;
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
        TextView source;
        TextView xm_description;

        //政策
        CustomTextView zc_title;
        TextView zc_description;
        TextView zc_lingyu;
        TextView zc_look;
        TextView zc_zan;
        ImageView zhengcetuijian;
        LinearLayout zc_layout;
        ImageView zc_zt;
        ImageView zc_zhiding;
        ImageView zc_tuijian;
        TextView zc_linyu;
        LinearLayout lingyu;
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
        TextView rank;
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
        TextView sb_dianwei;
        TextView device_look;
        LinearLayout device_layout;
        ImageView sb_zt;
        ImageView shebei_zhiding;
        ImageView shebei_tuijian;
        LinearLayout shebei_dianzan;

        //专利
        CustomTextView zhunli_title;
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
        ImageView tj_state_icon;
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
        RelativeLayout dixian;
    }

    /**
     * 用来标记是否显示来源
     * @param state
     */
    public void setState(int state){
        this.state = state;
    }
}
