package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by lizisong on 2017/3/22.
 */

public class kejikuAdapter extends BaseAdapter {
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

    public kejikuAdapter() {
    }

    public kejikuAdapter(Context context, List<Posts> postsListData, String channelName, String mytitle) {
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
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.kejiku_item, null);
            //资讯
            holder.zx_img = (ImageView) convertView.findViewById(R.id.zx_img);
            holder.zx_title = (TextView) convertView.findViewById(R.id.zx_title);
            holder.zx_type = (TextView) convertView.findViewById(R.id.zx_lanyuan);
            holder.zx_layout = (LinearLayout) convertView.findViewById(R.id.zx_layout);
            holder.zx_zt = (ImageView) convertView.findViewById(R.id.zx_zt);
            holder.zixu_zhiding = (ImageView) convertView.findViewById(R.id.zixue_zhiding);
            holder.zixu_tuijian = (ImageView) convertView.findViewById(R.id.zixun_tuijian);
            holder.zx_look = (TextView)convertView.findViewById(R.id.zx_look);
            holder.zx_zan  = (TextView)convertView.findViewById(R.id.zx_zan);
//            holder.zx_line_zan = (LinearLayout) convertView.findViewById(R.id.zx_line_zan);


            //项目
            holder.xm_img = (ImageView) convertView.findViewById(R.id.xm_img);
            holder.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
            holder.xm_linyu=(TextView)convertView.findViewById(R.id.xm_linyu);
            holder.xm_description=(TextView) convertView.findViewById(R.id.xm_description);
            holder.xm_type = (TextView) convertView.findViewById(R.id.xm_type);
            holder.xm_layout = (LinearLayout) convertView.findViewById(R.id.xm_layout);
            holder.xm_zt = (ImageView) convertView.findViewById(R.id.xm_zt);
            holder.xiangmu_tuijian = (ImageView) convertView.findViewById(R.id.xiangmu_tuijian);
            holder.xiangmu_zhiding = (ImageView) convertView.findViewById(R.id.xiangmu_zhiding);
            holder.xm_zan = (TextView)convertView.findViewById(R.id.xm_zan);
            holder.xm_look = (TextView)convertView.findViewById(R.id.xm_look);
            holder.xm_img1 = (ImageView)convertView.findViewById(R.id.xm_img1);


            //政策
            holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
            holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);

            holder.zc_type = (TextView) convertView.findViewById(R.id.zc_type);
            holder.zc_layout = (LinearLayout) convertView.findViewById(R.id.zc_layout);
            holder.zc_zt = (ImageView) convertView.findViewById(R.id.zc_zt);
            holder.zc_zhiding = (ImageView) convertView.findViewById(R.id.zhengci_zhiding);
            holder.zc_tuijian = (ImageView) convertView.findViewById(R.id.zhengci_tuijian);
            holder.zc_zan    = (TextView)convertView.findViewById(R.id.zc_zan);
            holder.zc_look   =(TextView)convertView.findViewById(R.id.zc_look);


            //人才
            holder.rc_img = (ImageView) convertView.findViewById(R.id.rc_img);
            holder.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
            holder.rc_type = (TextView) convertView.findViewById(R.id.rc_type);
            holder.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
            holder.rc_zan  = (TextView)convertView.findViewById(R.id.rc_zan);
            holder.rc_look = (TextView)convertView.findViewById(R.id.rc_look);
            holder.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
            holder.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);

            holder.rc_layout = (LinearLayout) convertView.findViewById(R.id.rc_layout);
            holder.rc_zt = (ImageView) convertView.findViewById(R.id.rc_zt);
            holder.rencai_zhiding = (ImageView) convertView.findViewById(R.id.rencai_zhiding);
            holder.rencai_tuijian = (ImageView) convertView.findViewById(R.id.rencai_tuijian);
            holder.rc_yuanshi = (ImageView) convertView.findViewById(R.id.rc_yuanshi);

            //设备
            holder.device_img = (ImageView) convertView.findViewById(R.id.device_img);
            holder.device_title = (TextView) convertView.findViewById(R.id.device_title);
            holder.sb_look = (TextView)convertView.findViewById(R.id.device_look);
            holder.sb_zan = (TextView)convertView.findViewById(R.id.device_zan);
            holder.device_type = (TextView) convertView.findViewById(R.id.device_type);
            holder.device_layout = (LinearLayout) convertView.findViewById(R.id.device_layout);
            holder.sb_zt = (ImageView) convertView.findViewById(R.id.sb_zt);
            holder.shebei_zhiding = (ImageView) convertView.findViewById(R.id.shenbei_zhiding);
            holder.shebei_tuijian = (ImageView) convertView.findViewById(R.id.shenbei_tuijian);
            holder.device_description=(TextView) convertView.findViewById(R.id.device_description);
            holder.device_linyu=(TextView) convertView.findViewById(R.id.device_linyu);

            //专利
            holder.zhunli_title = (TextView) convertView.findViewById(R.id.zhunli_title);
            holder.zhuanli_type = (TextView) convertView.findViewById(R.id.zhuanli_type);
            holder.zl_look = (TextView)convertView.findViewById(R.id.zhuanli_look);
            holder.zc_zan = (TextView)convertView.findViewById(R.id.zhuanli_zan);
            holder.zhuanli_layout = (LinearLayout) convertView.findViewById(R.id.zhuanli_layout);
            holder.zhuanli_zt = (ImageView) convertView.findViewById(R.id.zhuanli_zt);
            holder.zhuanli_tuijian = (ImageView) convertView.findViewById(R.id.zhuanli_tuijian);
            holder.zhuanli_zhiding = (ImageView) convertView.findViewById(R.id.zhuanli_zhiding);
            holder.zhuanli_description=(TextView) convertView.findViewById(R.id.zhuanli_description);
            holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.zhuanli_linyu);

            //实验室
            holder.librarys = (LinearLayout) convertView.findViewById(R.id.librarys);
            holder.librarys_title = (TextView) convertView.findViewById(R.id.librarys_title);
            holder.librarys_img = (ImageView) convertView.findViewById(R.id.librarys_img);
            holder.librarys_type = (TextView) convertView.findViewById(R.id.librarys_type);
            holder.sys_look = (TextView)convertView.findViewById(R.id.librarys_look);
            holder.sys_zan = (TextView)convertView.findViewById(R.id.librarys_zan);
            holder.sys_zt = (ImageView) convertView.findViewById(R.id.sys_zt);
            holder.shiyanshi_zhiding = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding);
            holder.shiyanshi_tuijian = (ImageView) convertView.findViewById(R.id.shiyanshi_tuijian);
            //三图
            holder.tj_state_line = (LinearLayout) convertView.findViewById(R.id.tj_state_line);
            holder.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
            holder.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
            holder.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
            holder.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);

            holder.tj_state_type = (TextView) convertView.findViewById(R.id.tj_state_type);
            holder.santu_zhiding = (ImageView) convertView.findViewById(R.id.santu_zhiding);
            holder.santu_tuijian = (ImageView) convertView.findViewById(R.id.santu_tuijian);
            holder.san_look = (TextView)convertView.findViewById(R.id.tj_state_zan);
            holder.san_zan = (TextView)convertView.findViewById(R.id.tj_state_zan);
            holder.line = (TextView) convertView.findViewById(R.id.line);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            if (postsListData.get(position).getTypename().equals("实验室")||postsListData.get(position).getTypename().equals("研究所")||postsListData.get(position).typeid.equals("8")) {
                holder.librarys.setVisibility(View.VISIBLE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.VISIBLE);
                holder.xm_img.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.sys_zt.setVisibility(View.VISIBLE);
                } else {
                    holder.sys_zt.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.GONE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
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


                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.VISIBLE);
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
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.VISIBLE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                holder.xm_img1.setImageResource(R.mipmap.zhengfangxing);
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.xm_img1, options);
                            } else {
                                holder.xm_img1.setBackgroundResource(R.mipmap.zhengfangxing);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.xm_img1, options);
                        }
                        holder.xm_title.setText(postsListData.get(position).getTitle());
                        if(postsListData.get(position).getArea_cate()!=null){
                            holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        }
                        holder.xm_description.setText(postsListData.get(position).getDescription());
                        read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                        if(read_ids.equals("")){

                            holder.xm_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.xm_title.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.xm_type.setText(postsListData.get(position).getTypename());
                        holder.xm_look.setText(postsListData.get(position).getClick());
                        holder.xm_zan.setText(postsListData.get(position).getZan());
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

                holder.librarys_type.setText(postsListData.get(position).getTypename());
                holder.sys_look.setText(postsListData.get(position).getClick());
                holder.sys_zan.setText(postsListData.get(position).getZan());
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
            } else if(postsListData.get(position).getTypename().equals("活动")){

                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
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
//                    holder.zx_line_zan.setVisibility(View.GONE);
                } else {
                    holder.zx_zt.setVisibility(View.GONE);
//                    holder.zx_line_zan.setVisibility(View.VISIBLE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
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


                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
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
                        holder.librarys_type.setText(postsListData.get(position).getTypename());
                        holder.sys_look.setText(postsListData.get(position).getClick());
                        holder.sys_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
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

                holder.zx_type.setText(postsListData.get(position).getTypename());
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

            }else if (postsListData.get(position).getTypename().equals("资讯") || postsListData.get(position).getTypename().equals("推荐") || postsListData.get(position).getTypename().equals("活动") || postsListData.get(position).getTypename().equals("专题")) {
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zx_zt.setVisibility(View.VISIBLE);
//                    holder.zx_line_zan.setVisibility(View.GONE);
                } else {
                    holder.zx_zt.setVisibility(View.GONE);
//                    holder.zx_line_zan.setVisibility(View.VISIBLE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
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


                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
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
                        holder.librarys_type.setText(postsListData.get(position).getTypename());
                        holder.sys_look.setText(postsListData.get(position).getClick());
                        holder.sys_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
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

                holder.zx_type.setText(postsListData.get(position).getTypename());
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
            } else if (postsListData.get(position).getTypename().equals("项目")) {
                holder.xm_layout.setVisibility(View.VISIBLE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.rc_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.xm_zt.setVisibility(View.VISIBLE);
                    holder.xm_dianwei.setVisibility(View.GONE);
                } else {
                    holder.xm_zt.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.xm_title.setVisibility(View.GONE);
                        holder.xm_img.setVisibility(View.GONE);
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

                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.xm_layout.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);

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
                        holder.librarys_type.setText(postsListData.get(position).getTypename());
                        holder.sys_look.setText(postsListData.get(position).getClick());
                        holder.sys_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("0")) {
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
                holder.xm_description.setText(postsListData.get(position).getDescription());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_type.setText(postsListData.get(position).getTypename());
                holder.xm_look.setText(postsListData.get(position).getClick());
                holder.xm_zan.setText(postsListData.get(position).getZan());
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
            } else if (postsListData.get(position).getTypename().equals("政策")) {
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
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zc_zt.setVisibility(View.VISIBLE);

                } else {
                    holder.zc_zt.setVisibility(View.GONE);
                }
                holder.zc_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description.setText(postsListData.get(position).getDescription());
                holder.zc_type.setText(postsListData.get(position).getTypename());
                holder.zc_look.setText(postsListData.get(position).getClick());
                holder.zc_zan.setText(postsListData.get(position).getZan());

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

            } else if (postsListData.get(position).getTypename().equals("人才")||postsListData.get(position).getTypename().equals("专家")) {
                holder.rc_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_layout.setVisibility(View.GONE);
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.GONE);
                holder.zhuanli_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.zx_zt.setVisibility(View.GONE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.zc_zt.setVisibility(View.GONE);
                holder.zhuanli_zt.setVisibility(View.GONE);
                holder.sys_zt.setVisibility(View.GONE);
                holder.tj_state_line.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.rc_zt.setVisibility(View.VISIBLE);
                } else {
                    holder.rc_zt.setVisibility(View.GONE);
                }
                holder.rc_uname.setText(postsListData.get(position).getUsername());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.rc_uname.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.rc_uname.setTextColor(Color.parseColor("#777777"));
                }
                holder.rc_title.setText(postsListData.get(position).getDescription());
                holder.rc_zhicheng.setText(postsListData.get(position).getRank());
                Area_cate area_cate = postsListData.get(position).getArea_cate();
                if (area_cate != null) {
                    holder.rc_linyu.setText(area_cate.getArea_cate1());
                }
                if(postsListData.get(position).getIs_academician()!=null){
                    if(postsListData.get(position).getIs_academician().equals("1")){
                        holder.rc_yuanshi.setVisibility(View.VISIBLE);
                    }else{
                        holder.rc_yuanshi.setVisibility(View.GONE);
                    }
                }


                holder.rc_type.setText(postsListData.get(position).getTypename());
                holder.rc_look.setText(postsListData.get(position).getClick());
                holder.rc_zan.setText(postsListData.get(position).getZan());

                if (postsListData.get(position).getLitpic().equals("")) {
                    holder.rc_img.setVisibility(View.GONE);
                } else {
                    holder.rc_img.setVisibility(View.VISIBLE);
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
            } else if (postsListData.get(position).getTypename().equals("设备")) {
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
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.sb_zt.setVisibility(View.VISIBLE);
                } else {
                    holder.sb_zt.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.device_layout.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.device_title.setVisibility(View.GONE);
                        holder.device_img.setVisibility(View.GONE);
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


                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.device_layout.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);

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
                        holder.librarys_type.setText(postsListData.get(position).getTypename());
                        holder.sys_look.setText(postsListData.get(position).getClick());
                        holder.sys_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
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
                        holder.device_layout.setVisibility(View.VISIBLE);
                        holder.device_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }
                holder.device_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null){
                    holder.device_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                }
                holder.device_description.setText(postsListData.get(position).getDescription());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.device_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.device_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.device_type.setText(postsListData.get(position).getTypename());
                holder.sb_look.setText(postsListData.get(position).getClick());
                holder.sb_zan.setText(postsListData.get(position).getZan());
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

            } else if (postsListData.get(position).getTypename().equals("专利")) {
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
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zhuanli_zt.setVisibility(View.VISIBLE);

                } else {
                    holder.zhuanli_zt.setVisibility(View.GONE);
                }
                holder.zhunli_title.setText(postsListData.get(position).getTitle());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zhunli_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zhunli_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_type.setText(postsListData.get(position).getTypename());
                holder.zl_look.setText(postsListData.get(position).getClick());
                holder.zl_zan.setText(postsListData.get(position).getZan());
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
            } else {
                holder.librarys.setVisibility(View.GONE);
                holder.zx_layout.setVisibility(View.VISIBLE);
                holder.zc_layout.setVisibility(View.GONE);
                holder.xm_img1.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
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
//                    holder.zx_line_zan.setVisibility(View.GONE);
                } else {
                    holder.zx_zt.setVisibility(View.GONE);
//                    holder.zx_line_zan.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState != null ) {
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
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


                        holder.tj_state_type.setText(postsListData.get(position).getTypename());
                        holder.san_zan.setText(postsListData.get(position).getZan());
                        holder.san_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys_img.setVisibility(View.VISIBLE);
                        holder.librarys_title.setVisibility(View.VISIBLE);
                        holder.librarys.setVisibility(View.VISIBLE);
                        holder.zx_layout.setVisibility(View.GONE);
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
                        holder.librarys_type.setText(postsListData.get(position).getTypename());
                        holder.sys_look.setText(postsListData.get(position).getClick());
                        holder.sys_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.librarys.setVisibility(View.GONE);
                        holder.zx_img.setVisibility(View.VISIBLE);
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
                        holder.zx_layout.setVisibility(View.VISIBLE);
                        holder.zx_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                    }
                }

                holder.zx_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null){
                    holder.zhuanli_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                }
                holder.zhuanli_description.setText(postsListData.get(position).getDescription());
                read_ids = PrefUtils.getString(context, postsListData.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zx_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zx_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_type.setText("资讯");
                holder.zx_zan.setText(postsListData.get(position).getZan());
                holder.zx_look.setText(postsListData.get(position).getClick());
                postsListData.get(position).setTypename("资讯");
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
        TextView zx_type;
        LinearLayout zx_layout;
        ImageView zx_zt;
        ImageView zixu_zhiding;
        ImageView zixu_tuijian;
        TextView zx_zan;
        TextView zx_look;
        LinearLayout zx_line_zan;

        //项目
        ImageView xm_img;
        ImageView xm_img1;
        TextView xm_title;
        TextView xm_dianwei;
        TextView xm_type;
        LinearLayout xm_layout;
        ImageView xm_zt;
        ImageView xiangmu_zhiding;
        ImageView xiangmu_tuijian;
        TextView xm_zan;
        TextView xm_look;
        TextView xm_linyu;
        TextView xm_description;

        //政策
        TextView zc_title;
        TextView zc_description;
//        TextView zc_lingyu;
        TextView zc_type;
        TextView zc_zan;
        TextView zc_look;
        LinearLayout zc_layout;
        ImageView zc_zt;
        ImageView zc_zhiding;
        ImageView zc_tuijian;

        //人才
        TextView rc_uname;
        ImageView rc_img;
        TextView rc_zhicheng;
        TextView rc_linyu;
        TextView rc_title;
        TextView rc_zan;
        TextView rc_look;
        TextView rc_type;
        LinearLayout rc_layout;
        ImageView rc_zt;
        ImageView rencai_zhiding;
        ImageView rencai_tuijian;
        ImageView rc_yuanshi;

        //设备
        ImageView device_img;
        TextView device_title;
        TextView device_type;
        TextView sb_zan;
        TextView sb_look;
        LinearLayout device_layout;
        ImageView sb_zt;
        ImageView shebei_zhiding;
        ImageView shebei_tuijian;
        TextView device_linyu;
        TextView device_description;

        //专利
        TextView zhunli_title;

        TextView zhuanli_type;
        TextView zl_look;
        TextView zl_zan;
        LinearLayout zhuanli_layout;
        ImageView zhuanli_zt;
        ImageView zhuanli_zhiding;
        ImageView zhuanli_tuijian;
        TextView zhuanli_linyu;
        TextView zhuanli_description;

        //实验室
        LinearLayout librarys;
        TextView librarys_title;

        ImageView librarys_img;

        TextView librarys_type;
        TextView sys_look;
        TextView sys_zan;
        ImageView sys_zt;
        ImageView shiyanshi_zhiding;
        ImageView shiyanshi_tuijian;
        //三图
        LinearLayout tj_state_line;
        TextView tj_state_title;
        ImageView tj_state_img1;
        ImageView tj_state_img2;
        ImageView tj_state_img3;
        TextView san_look;
        TextView san_zan;
        TextView tj_state_type;
        ImageView santu_zhiding;
        ImageView santu_tuijian;

        //        线
        TextView line;
    }
}
