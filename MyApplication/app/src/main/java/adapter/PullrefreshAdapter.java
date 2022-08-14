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
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import Util.NetUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Posts;
import view.RoundImageView;


/**
 * Created by 13520 on 2016/8/16.
 */
public class PullrefreshAdapter extends BaseAdapter {
    private Context context;
    private List<Posts> postsListData;
    private String channelName;
    private String mytitle;
    private ImageSize size;
    private String read_ids;
    private boolean isDisplayImage = false;
    private String content;
    //    private HashMap<String, String> urlMap = new HashMap<String, String>();
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int width= MyApplication.width;
    private int height=MyApplication.height;

    public PullrefreshAdapter() {
    }

    public PullrefreshAdapter(Context context, List<Posts> postsListData, String channelName, String mytitle) {
        this.context = context;
        this.postsListData = postsListData;
        this.channelName = channelName;
        this.mytitle = mytitle;
        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return postsListData == null ? 0 : postsListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * /* @param position
     *
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
//        Log.d("lizisong", "position"+position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.mylistview_item, null);
            //资讯
            holder.zx_zan = (TextView) convertView.findViewById(R.id.zx_zan);
            holder.zx_look = (TextView) convertView.findViewById(R.id.zx_look);
            holder.zx_update = (TextView) convertView.findViewById(R.id.zx_update);
            holder.pull_zan = (LinearLayout) convertView.findViewById(R.id.zx_line_zan);
            holder.zx_zhuanti = (ImageView) convertView.findViewById(R.id.zx_zt);
            holder.cont_img = (ImageView) convertView.findViewById(R.id.zx_img);
            holder.titles = (TextView) convertView.findViewById(R.id.zx_title);
            holder.my_jianjie = (LinearLayout) convertView.findViewById(R.id.my_jianjie);
            //            holder.zx_description=(TextView) convertView.findViewById(R.id.zx_description);
            //            holder.zx_lanyuan=(TextView) convertView.findViewById(R.id.zx_lanyuan);
            //项目
            holder.xiangmu = (LinearLayout) convertView.findViewById(R.id.xm_layout);
            holder.xm_img = (ImageView) convertView.findViewById(R.id.xm_img);
            holder.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
            holder.xm_tuijian = (ImageView) convertView.findViewById(R.id.xiangmu_tuijian);
            holder.xm_zhiding = (ImageView) convertView.findViewById(R.id.xiangmu_zhiding);
            holder.xm_look = (TextView) convertView.findViewById(R.id.xm_look);
            holder.xm_linyu=(TextView) convertView.findViewById(R.id.xm_linyu);
            holder.xm_zt = (ImageView) convertView.findViewById(R.id.xm_zt);
            holder.xm_zan=(TextView) convertView.findViewById(R.id.xm_zan);
            holder.xm_description=(TextView) convertView.findViewById(R.id.xm_description);
            //人才
            holder.zhicheng = (TextView) convertView.findViewById(R.id.zhicheng);
            holder.rc = (LinearLayout) convertView.findViewById(R.id.rc);
            holder.rc_tuijian = (ImageView) convertView.findViewById(R.id.rencai_tuijian);
            holder.rc_zhiding = (ImageView) convertView.findViewById(R.id.rencai_zhiding);
            holder.rc_zan = (TextView) convertView.findViewById(R.id.rc_zan);
            holder.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
//            holder.line_zan = (LinearLayout) convertView.findViewById(R.id.line_zan);
            holder.rc_zt = (ImageView) convertView.findViewById(R.id.rc_zt);
            holder.cont_imgs = (RoundImageView) convertView.findViewById(R.id.rc_img);
            holder.my_rencainame = (TextView) convertView.findViewById(R.id.rc_uname);
            holder.my_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
            holder.my_xianqin = (TextView) convertView.findViewById(R.id.rc_title);
            holder.my_rencai = (LinearLayout) convertView.findViewById(R.id.rc_layout);
            holder.rc_yuanshi=(ImageView) convertView.findViewById(R.id.rc_yuanshi);
            //设备
//            holder.shebei_description=(TextView) convertView.findViewById(R.id.shebei_description);
            holder.shebei_tuijian = (ImageView) convertView.findViewById(R.id.shenbei_tuijian);
            holder.shebei_zhiding = (ImageView) convertView.findViewById(R.id.shenbei_zhiding);
            holder.shebei_zan = (TextView) convertView.findViewById(R.id.device_zan);
            holder.shebei_look = (TextView) convertView.findViewById(R.id.device_look);
            holder.shebei_line = (LinearLayout) convertView.findViewById(R.id.device_layout);
            holder.shebei_title = (TextView) convertView.findViewById(R.id.device_title);
            holder.device_linyu=(TextView) convertView.findViewById(R.id.device_linyu);
            holder.device_description=(TextView) convertView.findViewById(R.id.device_description);
//            holder.sheb_zan = (LinearLayout) convertView.findViewById(R.id.sheb_zan);
            holder.shebei_zt = (ImageView) convertView.findViewById(R.id.sb_zt);
            //实验室
            holder.imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            holder.library_tuijian = (ImageView) convertView.findViewById(R.id.shiyanshi_tuijian);
            holder.library_zhiding = (ImageView) convertView.findViewById(R.id.shiyanshi_zhiding);
            holder.laboratory_zan = (TextView) convertView.findViewById(R.id.librarys_zan);
            holder.laboratory_look = (TextView) convertView.findViewById(R.id.librarys_look);
            holder.laboratory = (LinearLayout) convertView.findViewById(R.id.librarys);
            holder.laboratory_text = (TextView) convertView.findViewById(R.id.librarys_title);
//            holder.laboratory_linyu=(TextView) convertView.findViewById(R.id.laboratory_linyu);
            holder.sys_zan = (LinearLayout) convertView.findViewById(R.id.sys_dianzan);
            holder.sys_zt = (ImageView) convertView.findViewById(R.id.sys_zt);
//            holder.my_type=(TextView)  convertView.findViewById(R.id.my_type);
            //专利
            holder.my_zhuanli = (LinearLayout) convertView.findViewById(R.id.zhuanli_layout);
            holder.my_zhuanli_title = (TextView) convertView.findViewById(R.id.zhunli_title);
//            holder.my_zhuanli_type=(TextView)  convertView.findViewById(R.id.my_zhuanli_type);
            holder.zhuanli_tuijian = (ImageView) convertView.findViewById(R.id.zhuanli_tuijian);
            holder.zhuanli_zhiding = (ImageView) convertView.findViewById(R.id.zhuanli_zhiding);
//            holder.zhuanli_jieduan=(TextView) convertView.findViewById(R.id.zhuanli_jieduan);
            holder.zhuanli_look = (TextView) convertView.findViewById(R.id.zhuanli_look);
            holder.zl_zt = (ImageView) convertView.findViewById(R.id.zhuanli_zt);
            holder.zhuanli_zan=(TextView) convertView.findViewById(R.id.zhuanli_zan);
            holder.zhuanli_description=(TextView) convertView.findViewById(R.id.zhuanli_description);

            holder.zc_line = (LinearLayout) convertView.findViewById(R.id.zc_layout);
            holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
            holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
            holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.zhuanli_linyu);
            holder.imageView5 = (ImageView) convertView.findViewById(R.id.device_img);
            holder.tuijian = (ImageView) convertView.findViewById(R.id.zixun_tuijian);
            holder.zhiding = (ImageView) convertView.findViewById(R.id.zixue_zhiding);
            holder.zc_tuijian = (ImageView) convertView.findViewById(R.id.zhengci_tuijian);
            holder.zc_zhiding = (ImageView) convertView.findViewById(R.id.zhengci_zhiding);
//            holder.zc_spec = (TextView) convertView.findViewById(R.id.zc_spec);
            holder.zc_look = (TextView) convertView.findViewById(R.id.zc_look);
            holder.zc_zt = (ImageView) convertView.findViewById(R.id.zc_zt);

            holder.zc_zan=(TextView) convertView.findViewById(R.id.zc_zan);
            //三图
            holder.tj_state_line = (LinearLayout) convertView.findViewById(R.id.tj_state_line);
            holder.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
            holder.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
            holder.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
            holder.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);
            holder.tj_state_zan = (TextView) convertView.findViewById(R.id.tj_state_zan);
            holder.tj_state_click = (TextView) convertView.findViewById(R.id.tj_state_click);
            content = postsListData.get(position).getLitpic();
            convertView.setTag(holder);
            //   TypefaceUtil.replaceFont((Activity) context, "fonts/font.ttf");
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cont_img.setTag(content);

//        long times = (System.currentTimeMillis()/1000)-Long.parseLong(strTime);
//
//        TimeUtils.getStrTime(l/1000+"");
//        Log.i("dfdfd",TimeUtils.getStrTime(strTime));
//        String strTime1 = TimeUtils.getPlaytimeWithMsec((int) times);
//        holder.zx_update.setText(strTime1+"前");

//        holder.my_type.setText(postsListData.get(position).getTypename());.
        try {

            if (channelName == "设备" || postsListData.get(position).getTypename().equals("设备")) {
                if (postsListData.get(position).getTags().equals("头条")) {
                    holder.shebei_tuijian.setVisibility(View.GONE);
                    holder.shebei_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.shebei_tuijian.setVisibility(View.GONE);
                    holder.shebei_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.shebei_tuijian.setVisibility(View.GONE);
                    holder.shebei_zhiding.setVisibility(View.GONE);
                }
                holder.cont_img.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.shebei_line.setVisibility(View.VISIBLE);
                holder.pull_zan.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
//                    holder.sheb_zan.setVisibility(View.GONE);
                    holder.shebei_zt.setVisibility(View.VISIBLE);
                } else {
//                    holder.sheb_zan.setVisibility(View.GONE);
                    holder.shebei_zt.setVisibility(View.GONE);
                }
                if (postsListData.get(position).imageState.equals("3")) {
                    holder.tj_state_line.setVisibility(View.VISIBLE);
                    holder.shebei_title.setVisibility(View.GONE);
                    holder.imageView5.setVisibility(View.GONE);
                    holder.shebei_line.setVisibility(View.GONE);
                    holder.laboratory.setVisibility(View.GONE);
                    holder.tj_state_title.setText(postsListData.get(position).getTitle());
                    String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                    if(pid.equals("")){
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

                    holder.tj_state_zan.setText(postsListData.get(position).getZan());
                    holder.tj_state_click.setText(postsListData.get(position).getClick());

                } else if (postsListData.get(position).imageState.equals("1")) {
                    holder.laboratory.setVisibility(View.VISIBLE);
                    if (postsListData.get(position).getTypename().equals("专题")) {
                        holder.sys_zan.setVisibility(View.GONE);
                        holder.sys_zt.setVisibility(View.VISIBLE);
                    } else {
                        holder.sys_zan.setVisibility(View.GONE);
                        holder.sys_zt.setVisibility(View.GONE);
                    }
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.shebei_line.setVisibility(View.GONE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.imageView3, options);
                        } else {
                            holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                        params.height=height;
                        params.width =width;
                        holder.imageView3.setLayoutParams(params);
                        ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                , holder.imageView3, options);
                    }
                    holder.laboratory_text.setText(postsListData.get(position).getTitle());
                    String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                    if(pid.equals("")){
                        holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                    }else {
                        holder.laboratory_text.setTextColor(Color.parseColor("#7777777"));
                    }
                    holder.laboratory_zan.setText(postsListData.get(position).getZan());
                    holder.laboratory_look.setText(postsListData.get(position).getClick());
                } else if (postsListData.get(position).imageState.equals("0")) {
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.imageView5.setVisibility(View.VISIBLE);
                    holder.shebei_title.setVisibility(View.VISIBLE);
                    holder.shebei_line.setVisibility(View.VISIBLE);
                    holder.laboratory.setVisibility(View.GONE);
                    boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                    if (state) {
                        NetUtils.NetType type = NetUtils.getNetType();
                        if (type == NetUtils.NetType.NET_WIFI) {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.imageView5, options);
                        } else {
                            holder.imageView5.setBackgroundResource(R.mipmap.information_placeholder);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                , holder.imageView5, options);
                    }
                    holder.shebei_title.setText(postsListData.get(position).getTitle());
                    if(postsListData.get(position).getArea_cate()!=null){
                        holder.device_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                    }
                    if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                        holder.device_description.setVisibility(View.GONE);
                    }else{
                        holder.device_description.setVisibility(View.VISIBLE);
                        holder.device_description.setText(postsListData.get(position).getDescription());
                    }

                    String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                    if(pid.equals("")){
                        holder.shebei_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.shebei_title.setTextColor(Color.parseColor("#777777"));
                    }
//            holder.shebei_description.setText(postsListData.get(position).getDescription());

                    holder.shebei_zan.setText(postsListData.get(position).getZan());
                    holder.shebei_look.setText(postsListData.get(position).getClick());
                } else if (postsListData.get(position).imageState.equals("-1")) {
                    holder.imageView5.setVisibility(View.GONE);
                    holder.shebei_title.setVisibility(View.VISIBLE);
                    holder.tj_state_line.setVisibility(View.GONE);
                    holder.shebei_line.setVisibility(View.VISIBLE);
                    holder.laboratory.setVisibility(View.GONE);
                    holder.shebei_title.setText(postsListData.get(position).getTitle());
                    String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                    if(pid.equals("")){
                        holder.shebei_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.shebei_title.setTextColor(Color.parseColor("#777777"));
                    }
                    if(postsListData.get(position).getArea_cate()!=null){
                        holder.device_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                    }
                    if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                        holder.device_description.setVisibility(View.GONE);
                    }else{
                        holder.device_description.setVisibility(View.VISIBLE);
                        holder.device_description.setText(postsListData.get(position).getDescription());
                    }
                    holder.shebei_zan.setText(postsListData.get(position).getZan());
                    holder.shebei_look.setText(postsListData.get(position).getClick());
                }

            } else if (channelName == "政策" || postsListData.get(position).getTypename().equals("政策")) {
                if (postsListData.get(position).getTags().equals("头条")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.zc_tuijian.setVisibility(View.GONE);
                    holder.zc_zhiding.setVisibility(View.GONE);
                }
                holder.cont_img.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.zc_line.setVisibility(View.VISIBLE);
                holder.pull_zan.setVisibility(View.GONE);
                if (postsListData.get(position).getTypename().equals("专题")) {
                    holder.zc_zt.setVisibility(View.VISIBLE);
                    holder.zc_zan.setVisibility(View.GONE);
                } else {
                    holder.zc_zt.setVisibility(View.GONE);
                    holder.zc_zan.setVisibility(View.GONE);
                }
                holder.zc_zan.setText(postsListData.get(position).getZan());
                holder.zc_title.setText(postsListData.get(position).getTitle());
                String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                if(pid.equals("")){
                    holder.zc_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description.setText(postsListData.get(position).getDescription());
                holder.zc_look.setText(postsListData.get(position).getClick());
            } else if (channelName == "研究所" || postsListData.get(position).getTypename().equals("实验室") ||postsListData.get(position).getTypename().equals("研究所")) {
                try {
                    if (postsListData.get(position).getTags().equals("头条")) {
                        holder.library_tuijian.setVisibility(View.GONE);
                        holder.library_zhiding.setVisibility(View.VISIBLE);
                    } else if (postsListData.get(position).getTags().equals("特推")) {
                        holder.library_tuijian.setVisibility(View.GONE);
                        holder.library_zhiding.setVisibility(View.GONE);
                    } else if (postsListData.get(position).getTags().equals("普通")) {
                        holder.library_tuijian.setVisibility(View.GONE);
                        holder.library_zhiding.setVisibility(View.GONE);
                    }
                    holder.cont_img.setVisibility(View.GONE);
                    holder.my_jianjie.setVisibility(View.GONE);
                    holder.laboratory.setVisibility(View.VISIBLE);
                    holder.xiangmu.setVisibility(View.GONE);
                    holder.pull_zan.setVisibility(View.GONE);
                    if (postsListData.get(position).getTypename().equals("专题")) {
                        holder.sys_zan.setVisibility(View.GONE);
                        holder.sys_zt.setVisibility(View.VISIBLE);
                    } else {
                        holder.sys_zan.setVisibility(View.GONE);
                        holder.sys_zt.setVisibility(View.GONE);
                    }
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.laboratory_text.setVisibility(View.GONE);
                        holder.imageView3.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.GONE);
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
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

                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.imageView3.setVisibility(View.VISIBLE);
                        holder.laboratory_text.setVisibility(View.VISIBLE);
                        holder.laboratory.setVisibility(View.VISIBLE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.imageView3, options);
                            } else {
                                holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                            params.height=height;
                            params.width =width;
                            holder.imageView3.setLayoutParams(params);
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.imageView3, options);
                        }
                        holder.laboratory_text.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                        }else {
                            holder.laboratory_text.setTextColor(Color.parseColor("#7777777"));
                        }
                        holder.laboratory_zan.setText(postsListData.get(position).getZan());
                        holder.laboratory_look.setText(postsListData.get(position).getClick());
                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.cont_img.setVisibility(View.GONE);
                        holder.titles.setVisibility(View.GONE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.VISIBLE);
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
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.xm_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.xm_title.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.xm_look.setText(postsListData.get(position).getClick());
                        holder.xm_zan.setText(postsListData.get(position).getZan());

                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.imageView3.setVisibility(View.GONE);
                        holder.laboratory_text.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.VISIBLE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.GONE);
                        holder.laboratory_text.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                        }else {
                            holder.laboratory_text.setTextColor(Color.parseColor("#7777777"));
                        }
                        holder.laboratory_zan.setText(postsListData.get(position).getZan());
                        holder.laboratory_look.setText(postsListData.get(position).getClick());
                    }

                } catch (Exception e) {
                }

            } else if (channelName == "项目" || postsListData.get(position).getTypename().equals("项目")) {
                try {
                    if (postsListData.get(position).getTags().equals("头条")) {
                        holder.xm_tuijian.setVisibility(View.GONE);
                        holder.xm_zhiding.setVisibility(View.VISIBLE);
                    } else if (postsListData.get(position).getTags().equals("特推")) {
                        holder.xm_tuijian.setVisibility(View.GONE);
                        holder.xm_zhiding.setVisibility(View.GONE);
                    } else if (postsListData.get(position).getTags().equals("普通")) {
                        holder.xm_tuijian.setVisibility(View.GONE);
                        holder.xm_zhiding.setVisibility(View.GONE);
                    }
                    holder.cont_img.setVisibility(View.GONE);
                    holder.my_jianjie.setVisibility(View.GONE);
                    holder.xiangmu.setVisibility(View.VISIBLE);
                    holder.pull_zan.setVisibility(View.GONE);
                    if (postsListData.get(position).getTypename().equals("专题")) {
                        holder.xm_zt.setVisibility(View.VISIBLE);
                        holder.xm_zan.setVisibility(View.GONE);
                    } else {
                        holder.xm_zt.setVisibility(View.GONE);
                        holder.xm_zan.setVisibility(View.GONE);
                    }
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.xm_title.setVisibility(View.GONE);
                        holder.xm_img.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
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

                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.laboratory.setVisibility(View.VISIBLE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zan.setVisibility(View.GONE);
                            holder.sys_zt.setVisibility(View.VISIBLE);
                        } else {
                            holder.sys_zan.setVisibility(View.GONE);
                            holder.sys_zt.setVisibility(View.GONE);
                        }
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.imageView3, options);
                            } else {
                                holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                            params.height=height;
                            params.width =width;
                            holder.imageView3.setLayoutParams(params);
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.imageView3, options);
                        }
                        holder.laboratory_text.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                        }else {
                            holder.laboratory_text.setTextColor(Color.parseColor("#7777777"));
                        }
                        holder.laboratory_zan.setText(postsListData.get(position).getZan());
                        holder.laboratory_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.xm_img.setVisibility(View.VISIBLE);
                        holder.xm_title.setVisibility(View.VISIBLE);
                        holder.xiangmu.setVisibility(View.VISIBLE);
                        holder.laboratory.setVisibility(View.GONE);
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
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.xm_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.xm_title.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.xm_look.setText(postsListData.get(position).getClick());
                        holder.xm_zan.setText(postsListData.get(position).getZan());
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.xm_img.setVisibility(View.GONE);
                        holder.xm_title.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.xiangmu.setVisibility(View.VISIBLE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.xm_title.setText(postsListData.get(position).getTitle());

                        if(postsListData.get(position).getArea_cate() != null){
                           holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        }
                        holder.xm_description.setText(postsListData.get(position).getDescription());

                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.xm_title.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.xm_title.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.xm_look.setText(postsListData.get(position).getClick());
                        holder.xm_zan.setText(postsListData.get(position).getZan());
                        if(postsListData.get(position).getArea_cate()!=null){
                            holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        }

                        if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                            holder.xm_description.setVisibility(View.GONE);
                        }else{
                            holder.xm_description.setVisibility(View.VISIBLE);
                            holder.xm_description.setText(postsListData.get(position).getDescription());
                        }
                    }
                } catch (Exception e) {

                }

            } else if (channelName == "专家" || postsListData.get(position).getTypename().equals("人才") || postsListData.get(position).getTypename().equals("专家")) {
                if (postsListData.get(position).getTags().equals("头条")) {
                    holder.rc_tuijian.setVisibility(View.GONE);
                    holder.rc_zhiding.setVisibility(View.VISIBLE);
                } else if (postsListData.get(position).getTags().equals("特推")) {
                    holder.rc_tuijian.setVisibility(View.GONE);
                    holder.rc_zhiding.setVisibility(View.GONE);
                } else if (postsListData.get(position).getTags().equals("普通")) {
                    holder.rc_tuijian.setVisibility(View.GONE);
                    holder.rc_zhiding.setVisibility(View.GONE);
                }
                holder.cont_imgs.setVisibility(View.VISIBLE);
                holder.cont_img.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.my_rencai.setVisibility(View.VISIBLE);
                holder.pull_zan.setVisibility(View.GONE);
                holder.rc.setVisibility(View.VISIBLE);
                if (postsListData.get(position).getTypename().equals("专题")) {
//                    holder.line_zan.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.VISIBLE);
                } else {
//                    holder.line_zan.setVisibility(View.GONE);
                    holder.rc_zt.setVisibility(View.GONE);
                }
                holder.cont_imgs.setTag(content);
                try {
                    if (content.equals(holder.cont_imgs.getTag())) {
                        holder.cont_imgs.setTag(content);

                        if (postsListData.get(position).getLitpic().equals("")) {
                            holder.cont_imgs.setVisibility(View.GONE);
                        } else {
                            holder.cont_imgs.setVisibility(View.VISIBLE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI) {
                                    imageLoader.displayImage(postsListData.get(position).getLitpic()
                                            , holder.cont_imgs, options);
                                } else {
                                    holder.cont_imgs.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                imageLoader.displayImage(postsListData.get(position).getLitpic()
                                        , holder.cont_imgs, options);
                            }

                        }

                        holder.my_rencainame.setText(postsListData.get(position).getUsername());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
                        }else {
                            holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
                        }
//                        holder.zhicheng.setText(postsListData.get(position).getRank());
                        holder.rc_zan.setText(postsListData.get(position).getZan());
                        holder.rc_look.setText(postsListData.get(position).getClick());
                        holder.my_xianqin.setText(postsListData.get(position).getDescription());
                        if(postsListData.get(position).getArea_cate()!=null){
                            holder.my_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                        }
                        if(postsListData.get(position).getIs_academician()!=null){
                            if(postsListData.get(position).getIs_academician().equals("1")){
                                holder.rc_yuanshi.setVisibility(View.VISIBLE);
                            }else if(postsListData.get(position).getIs_academician().equals("0")){
                                holder.rc_yuanshi.setVisibility(View.GONE);
                            }
                        }


                    }
                } catch (Exception e) {
                }

            } else if (channelName == "专利" || postsListData.get(position).getTypename().equals("专利")) {
                try {
                    if (postsListData.get(position).getTags() != null && postsListData.get(position).getTags().equals("头条")) {
                        holder.zhuanli_tuijian.setVisibility(View.GONE);
                        holder.zhuanli_zhiding.setVisibility(View.VISIBLE);
                    } else if (postsListData.get(position).getTags() != null && postsListData.get(position).getTags().equals("特推")) {
                        holder.zhuanli_tuijian.setVisibility(View.GONE);
                        holder.zhuanli_zhiding.setVisibility(View.GONE);
                    } else if ( postsListData.get(position).getTags() != null && postsListData.get(position).getTags().equals("普通")) {
                        holder.zhuanli_tuijian.setVisibility(View.GONE);
                        holder.zhuanli_zhiding.setVisibility(View.GONE);
                    }
                    holder.cont_img.setVisibility(View.GONE);
                    holder.my_jianjie.setVisibility(View.GONE);
                    holder.my_zhuanli.setVisibility(View.VISIBLE);
                    holder.pull_zan.setVisibility(View.GONE);
                    if (postsListData.get(position).getTypename()!=null && postsListData.get(position).getTypename().equals("专题")) {
                        holder.zl_zt.setVisibility(View.VISIBLE);
                        holder.zhuanli_zan.setVisibility(View.GONE);
                    } else {
                        holder.zl_zt.setVisibility(View.GONE);
                        holder.zhuanli_zan.setVisibility(View.GONE);
                    }
                    holder.my_zhuanli_title.setText(postsListData.get(position).getTitle());

                    if(postsListData.get(position).getArea_cate()!=null){
                      
                        holder.zhuanli_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                    }
                    if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                        holder.zhuanli_description.setVisibility(View.GONE);
                    }else{
                        holder.zhuanli_description.setVisibility(View.VISIBLE);
                        holder.zhuanli_description.setText(postsListData.get(position).getDescription());
                    }

                    String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                    if(pid.equals("")){
                        holder.my_zhuanli_title.setTextColor(Color.parseColor("#181818"));
                    }else{
                        holder.my_zhuanli_title.setTextColor(Color.parseColor("#777777"));
                    }
                    holder.zhuanli_zan.setText(postsListData.get(position).getZan());
//                holder.zhuanli_jieduan.setText(postsListData.get(position).getState());
                    holder.zhuanli_look.setText(postsListData.get(position).getClick());

                } catch (Exception e) {
                }

//            holder.my_zhuanli_type.setText(postsListData.get(position).getTypename());
            } else if (channelName == "资讯" || postsListData.get(position).getTypename().equals("资讯") || postsListData.get(position).getTypename().equals("活动")) {

                try {
//                if(content.equals(holder.cont_img.getTag())){
//                    holder.cont_img.setTag(content);
//                    if(!urlMap.containsKey(content)){
//                        urlMap.put(content, content);

                    if (postsListData.get(position).getTags().equals("头条")) {
                        holder.tuijian.setVisibility(View.GONE);
                        holder.zhiding.setVisibility(View.VISIBLE);
                    } else if (postsListData.get(position).getTags().equals("特推")) {
                        holder.tuijian.setVisibility(View.GONE);
                        holder.zhiding.setVisibility(View.GONE);
                    } else if (postsListData.get(position).getTags().equals("普通")) {
                        holder.tuijian.setVisibility(View.GONE);
                        holder.zhiding.setVisibility(View.GONE);
                    }

                    if (postsListData.get(position).getTypename().equals("专题")) {
                        holder.pull_zan.setVisibility(View.GONE);
                        holder.zx_zhuanti.setVisibility(View.VISIBLE);
                    } else {
                        holder.pull_zan.setVisibility(View.GONE);
                        holder.zx_zhuanti.setVisibility(View.GONE);
                    }
                    if (postsListData.get(position).imageState.equals("3")) {
                        holder.tj_state_line.setVisibility(View.VISIBLE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        holder.titles.setVisibility(View.GONE);
                        holder.cont_img.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.tj_state_title.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
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
                        holder.tj_state_zan.setText(postsListData.get(position).getZan());
                        holder.tj_state_click.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("1")) {
                        holder.laboratory.setVisibility(View.VISIBLE);
                        if (postsListData.get(position).getTypename().equals("专题")) {
                            holder.sys_zan.setVisibility(View.GONE);
                            holder.sys_zt.setVisibility(View.VISIBLE);
                        } else {
                            holder.sys_zan.setVisibility(View.GONE);
                            holder.sys_zt.setVisibility(View.GONE);
                        }
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.my_jianjie.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.imageView3, options);
                            } else {
                                holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                            params.height=height;
                            params.width =width;
                            holder.imageView3.setLayoutParams(params);
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.imageView3, options);
                        }
                        holder.laboratory_text.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                        }else {
                            holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                        }
                        holder.laboratory_zan.setText(postsListData.get(position).getZan());
                        holder.laboratory_look.setText(postsListData.get(position).getClick());

                    } else if (postsListData.get(position).imageState.equals("0")) {
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.cont_img.setVisibility(View.VISIBLE);
                        holder.titles.setVisibility(View.VISIBLE);
                        holder.my_jianjie.setVisibility(View.VISIBLE);
                        holder.laboratory.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                        , holder.cont_img, options);
                            } else {
                                holder.cont_img.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).image.image1
                                    , holder.cont_img, options);
                        }
                        holder.titles.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.titles.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.titles.setTextColor(Color.parseColor("#777777"));
                        }
//                    holder.zx_description.setText(postsListData.get(position).getDescription());
//                    holder.zx_lanyuan.setText(postsListData.get(position).getSource());
                        holder.zx_zan.setText(postsListData.get(position).getZan());
                        holder.zx_look.setText(postsListData.get(position).getClick());
                    } else if (postsListData.get(position).imageState.equals("-1")) {
                        holder.cont_img.setVisibility(View.GONE);
                        holder.laboratory.setVisibility(View.GONE);
                        holder.titles.setVisibility(View.VISIBLE);
                        holder.tj_state_line.setVisibility(View.GONE);
                        holder.my_jianjie.setVisibility(View.VISIBLE);
                        holder.titles.setText(postsListData.get(position).getTitle());
                        String pid =  PrefUtils.getString(context, postsListData.get(position).getId(),"");
                        if(pid.equals("")){
                            holder.titles.setTextColor(Color.parseColor("#181818"));
                        }else{
                            holder.titles.setTextColor(Color.parseColor("#777777"));
                        }
//                    holder.zx_description.setText(postsListData.get(position).getDescription());
//                    holder.zx_lanyuan.setText(postsListData.get(position).getSource());
                        holder.zx_zan.setText(postsListData.get(position).getZan());
                        holder.zx_look.setText(postsListData.get(position).getClick());
                    }

                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        return convertView;
    }

    public List<Posts> getpostList() {
        return postsListData;
    }

    /**
     *
     */
    class ViewHolder {
        TextView my_type;
        TextView my_zhuanli_type;
        //专利
        TextView zhuanli_linyu;
        ImageView imageView3;
        ImageView imageView5;
        ImageView tuijian;
        ImageView zhiding;
        //项目
        LinearLayout xiangmu;
        ImageView xm_img;
        TextView xm_title;
        ImageView xm_tuijian;
        ImageView xm_zhiding;
        TextView xm_look;
        TextView xm_linyu;
        ImageView xm_zt;
        TextView xm_zan;
        TextView xm_description;
        //人才
        TextView zhicheng;
        LinearLayout rc;
        ImageView rc_tuijian;
        ImageView rc_zhiding;
        TextView rc_zan;
        TextView rc_look;
        LinearLayout line_zan;
        ImageView rc_zt;
        RoundImageView cont_imgs;
        TextView my_rencainame;
        TextView my_linyu;
        TextView my_xianqin;
        LinearLayout my_rencai;
        ImageView rc_yuanshi;

        //专利
        LinearLayout my_zhuanli;
        TextView my_zhuanli_title;
        ImageView zhuanli_tuijian;
        ImageView zhuanli_zhiding;
        TextView zhuanli_jieduan;
        TextView zhuanli_look;
        ImageView zl_zt;
        TextView zhuanli_zan;
        TextView zhuanli_description;
        //实验室
        ImageView library_tuijian;
        ImageView library_zhiding;
        TextView laboratory_zan;
        TextView laboratory_look;
        LinearLayout laboratory;
        TextView laboratory_text;
        TextView laboratory_linyu;
        LinearLayout sys_zan;
        ImageView sys_zt;
        //政策
        LinearLayout zc_line;
        TextView zc_title;
        TextView zc_description;
        ImageView zc_tuijian;
        ImageView zc_zhiding;
        TextView zc_spec;
        TextView zc_look;
        ImageView zc_zt;
        TextView zc_zan;
        //设备
        TextView shebei_description;
        ImageView shebei_tuijian;
        ImageView shebei_zhiding;
        TextView shebei_zan;
        TextView shebei_look;
        LinearLayout shebei_line;
        TextView shebei_title;
        TextView shebei_linyu;
        LinearLayout sheb_zan;
        ImageView shebei_zt;
        TextView device_linyu;
        TextView device_description;
        //资讯
        TextView zx_zan;
        TextView zx_look;
        TextView zx_description;
        TextView zx_lanyuan;
        TextView zx_update;
        LinearLayout pull_zan;
        ImageView zx_zhuanti;
        ImageView cont_img;
        TextView titles;
        LinearLayout my_jianjie;

        //三图
        LinearLayout tj_state_line;
        TextView tj_state_title;
        ImageView tj_state_img1;
        ImageView tj_state_img2;
        ImageView tj_state_img3;
        TextView tj_state_zan;
        TextView tj_state_click;
    }

    /**
     * 快速滑动的时候是否显示图片
     *
     * @param displayState
     */
    public void setDisplayImage(boolean displayState) {
        isDisplayImage = displayState;
    }

}
