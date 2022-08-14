package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.NetUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import entity.Area_cate;
import entity.spec_post;
import view.RoundImageView;

import static com.maidiantech.R.id.zc_look;


/**
 * Created by 13520 on 2017/2/27.
 */

public class SpecialAdapter extends BaseAdapter {
    private List<spec_post> list;
    private Context context;
    private DisplayImageOptions options;
    private String read_ids;
    private String spe_title;
    private int width= MyApplication.width;
    private int height=MyApplication.height;
    public SpecialAdapter() {
    }

    public SpecialAdapter(Context context, List<spec_post> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        Log.d("lizisong", "position"+position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.specl_item, null);

            holder.notename = (TextView) convertView.findViewById(R.id.notename);

            holder.head_lin = (LinearLayout) convertView.findViewById(R.id.head_lin);
            //资讯
            holder.s_title = (TextView) convertView.findViewById(R.id.zx_title);
            holder.my_jianjie = (LinearLayout) convertView.findViewById(R.id.zx_layout);
            holder.cont_img = (ImageView) convertView.findViewById(R.id.zx_img);
            holder.zx_zan = (TextView) convertView.findViewById(R.id.zx_zan);
            holder.zx_look = (TextView) convertView.findViewById(R.id.zx_look);
            holder.zx_name = (TextView)convertView.findViewById(R.id.zx_name);
            holder.spe_title = (TextView)convertView.findViewById(R.id.spe_title);
            //项目
            holder.xiangmu = (LinearLayout) convertView.findViewById(R.id.xm_layout);
            holder.xm_img = (ImageView) convertView.findViewById(R.id.xm_img);
            holder.xm_title = (TextView) convertView.findViewById(R.id.xm_title);
            holder.xm_look = (TextView) convertView.findViewById(R.id.xm_look);
            holder.xm_zan=(TextView) convertView.findViewById(R.id.xm_dianwei);
            holder.xm_linyu=(TextView) convertView.findViewById(R.id.xm_linyu);
            holder.xm_description=(TextView) convertView.findViewById(R.id.xm_description);
            holder.xm_name = (TextView)convertView.findViewById(R.id.xm_name);

            //人才
            holder.rc = (LinearLayout) convertView.findViewById(R.id.rc_layout);
            holder.cont_imgs = (RoundImageView) convertView.findViewById(R.id.rc_img);
            holder.my_rencainame = (TextView) convertView.findViewById(R.id.rc_uname);
            holder.zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
            holder.my_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
            holder.my_xianqin = (TextView) convertView.findViewById(R.id.rc_title);
            holder.rc_zan = (TextView) convertView.findViewById(R.id.rc_zan);
            holder.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
            //专利
            holder.my_zhuanli = (LinearLayout) convertView.findViewById(R.id.zhuanli_layout);
            holder.my_zhuanli_title = (TextView) convertView.findViewById(R.id.zhunli_title);
            holder.zhuanli_look = (TextView) convertView.findViewById(R.id.zhuanli_look);
            holder.zhuanli_zan=(TextView) convertView.findViewById(R.id.zhuanli_zan);
            holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.zhuanli_linyu);
            holder.zhuanli_description=(TextView) convertView.findViewById(R.id.zhuanli_description);
            holder.zhuanli_name = (TextView)convertView.findViewById(R.id.zhuanli_name);

            //实验室
            holder.laboratory = (LinearLayout) convertView.findViewById(R.id.librarys);
            holder.laboratory_text = (TextView) convertView.findViewById(R.id.librarys_title);
            holder.imageView3 = (ImageView) convertView.findViewById(R.id.librarys_img);
            holder.laboratory_zan = (TextView) convertView.findViewById(R.id.librarys_zan);
            holder.laboratory_look = (TextView) convertView.findViewById(R.id.librarys_look);
            holder.datu_name = (TextView)convertView.findViewById(R.id.datu_name);
            //政策
            holder.zc_line = (LinearLayout) convertView.findViewById(R.id.zc_layout);
            holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
            holder.zc_description = (TextView) convertView.findViewById(R.id.zc_description);
            holder.zc_look = (TextView) convertView.findViewById(zc_look);
            holder.zc_zan=(TextView) convertView.findViewById(R.id.zc_zan);
            holder.zc_lingyu= (TextView)convertView.findViewById(R.id.zc_lingyu);

            //设备
            holder.shebei_line = (LinearLayout) convertView.findViewById(R.id.device_layout);
            holder.imageView5 = (ImageView) convertView.findViewById(R.id.device_img);
            holder.shebei_title = (TextView) convertView.findViewById(R.id.device_title);
            holder.shebei_zan = (TextView) convertView.findViewById(R.id.device_zan);
            holder.shebei_look = (TextView) convertView.findViewById(R.id.device_look);
            holder.device_linyu = (TextView) convertView.findViewById(R.id.device_linyu);
            holder.device_description = (TextView) convertView.findViewById(R.id.device_description);
            holder.device_name = (TextView)convertView.findViewById(R.id.device_name);

            //三图
            holder.tj_state_line = (LinearLayout) convertView.findViewById(R.id.tj_state_line);
            holder.tj_state_title = (TextView) convertView.findViewById(R.id.tj_state_title);
            holder.tj_state_img1 = (ImageView) convertView.findViewById(R.id.tj_state_img1);
            holder.tj_state_img2 = (ImageView) convertView.findViewById(R.id.tj_state_img2);
            holder.tj_state_img3 = (ImageView) convertView.findViewById(R.id.tj_state_img3);
            holder.tj_state_zan = (TextView) convertView.findViewById(R.id.tj_state_zan);
            holder.tj_state_click = (TextView) convertView.findViewById(R.id.tj_state_click);
            holder.santu_name = (TextView)convertView.findViewById(R.id.santu_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            Log.d("lizisong", "position:"+position);
            holder.tj_state_line.setVisibility(View.GONE);
            if(position == 0){
                holder.spe_title.setVisibility(View.VISIBLE);
                holder.spe_title.setText(spe_title);
            }else{
                holder.spe_title.setVisibility(View.GONE);
            }
        if (list.get(position).getTypename().equals("专题")) {
            holder.head_lin.setVisibility(View.VISIBLE);
          if(list.get(position).state){
              holder.head_lin.setVisibility(View.VISIBLE);
          }else{
              holder.head_lin.setVisibility(View.GONE);
          }
                holder.notename.setText(list.get(position).notename);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);

        } else if (list.get(position).getTypename().equals("资讯")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.VISIBLE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            if (list.get(position).imageState.equals("3")) {
                holder.tj_state_line.setVisibility(View.VISIBLE);
                holder.s_title.setVisibility(View.GONE);
                holder.cont_img.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.tj_state_title.setText(list.get(position).getTitle());
                holder.santu_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                }
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.tj_state_img1, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image2
                                , holder.tj_state_img2, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image3
                                , holder.tj_state_img3, options);
                    } else {
                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.tj_state_img1, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image2
                            , holder.tj_state_img2, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image3
                            , holder.tj_state_img3, options);
                }

                holder.tj_state_zan.setText(list.get(position).getZan());
                holder.tj_state_click.setText(list.get(position).getClick());
                holder.santu_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("1")) {
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.my_jianjie.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView3, options);
                    } else {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                    params.height=height;
                    params.width =width;
                    holder.imageView3.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView3, options);
                }
                holder.laboratory_text.setText(list.get(position).getTitle());
                holder.datu_name.setText(list.get(position).getTypename());

                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());

            } else if (list.get(position).imageState.equals("0")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.cont_img.setVisibility(View.VISIBLE);
                holder.s_title.setVisibility(View.VISIBLE);
                holder.my_jianjie.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.GONE);
//                holder.cont_img.setBackgroundResource(R.mipmap.information_placeholder);
                holder.cont_img.setImageResource(R.mipmap.information_placeholder);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.cont_img, options);
                    } else {
                        holder.cont_img.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.cont_img, options);
                }
                holder.s_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.s_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.s_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_zan.setText(list.get(position).getZan());
                holder.zx_look.setText(list.get(position).getClick());
                holder.zx_name.setText(list.get(position).getTypename());
            } else if (list.get(position).imageState.equals("-1")) {
                holder.cont_img.setVisibility(View.GONE);
                holder.s_title.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.GONE);
                holder.s_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.s_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.s_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_zan.setText(list.get(position).getZan());
                holder.zx_look.setText(list.get(position).getClick());
                holder.zx_name.setText(list.get(position).getTypename());
            }


//                ImageLoader.getInstance().displayImage(list.get(position).getLitpic(),holder.cont_img,options);
        } else if (list.get(position).getTypename().equals("项目")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.VISIBLE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            if (list.get(position).imageState.equals("3")) {
                holder.tj_state_line.setVisibility(View.VISIBLE);
                holder.xm_title.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.GONE);
                holder.xiangmu.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.tj_state_title.setText(list.get(position).getTitle());

                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                }
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.tj_state_img1, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image2
                                , holder.tj_state_img2, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image3
                                , holder.tj_state_img3, options);
                    } else {
                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.tj_state_img1, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image2
                            , holder.tj_state_img2, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image3
                            , holder.tj_state_img3, options);
                }

                holder.tj_state_zan.setText(list.get(position).getZan());
                holder.tj_state_click.setText(list.get(position).getClick());
                holder.xm_name.setText(list.get(position).getTypename());
                holder.santu_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("1")) {
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.xiangmu.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView3, options);
                    } else {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                    params.height=height;
                    params.width =width;
                    holder.imageView3.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView3, options);
                }
                holder.laboratory_text.setText(list.get(position).getTitle());
                holder.datu_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());
            } else if (list.get(position).imageState.equals("0")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.xm_img.setVisibility(View.VISIBLE);
                holder.xm_title.setVisibility(View.VISIBLE);
                holder.xiangmu.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.xm_img, options);
                    } else {
                        holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.xm_img, options);
                }
                if(list.get(position).area_cate!=null){
                    holder.xm_linyu.setText(list.get(position).area_cate.getArea_cate1());
                }
               holder.xm_description.setText(list.get(position).getDescription());
                holder.xm_zan.setText(list.get(position).getZan());
                holder.xm_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_look.setText(list.get(position).getClick());
                holder.xm_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("-1")) {
                holder.xm_img.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.xm_title.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.xiangmu.setVisibility(View.VISIBLE);
                holder.xm_zan.setText(list.get(position).getZan());
                holder.xm_title.setText(list.get(position).getTitle());
                holder.xm_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.xm_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_look.setText(list.get(position).getClick());
            }
//            holder.xm_title.setText(list.get(position).getTitle());
//            holder.xm_look.setText(list.get(position).getClick());
//            ImageLoader.getInstance().displayImage(list.get(position).getLitpic(),holder.xm_img,options);
        } else if (list.get(position).getTypename().equals("人才")||list.get(position).getTypename().equals("专家")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.VISIBLE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
            if (state) {
                NetUtils.NetType type = NetUtils.getNetType();
                if (type == NetUtils.NetType.NET_WIFI) {
                    ImageLoader.getInstance().displayImage(list.get(position).getLitpic()
                            , holder.cont_imgs, options);
                } else {
                    holder.cont_imgs.setBackgroundResource(R.mipmap.information_placeholder);
                }
            } else {
                ImageLoader.getInstance().displayImage(list.get(position).getLitpic()
                        , holder.cont_imgs, options);
            }
            holder.my_rencainame.setText(list.get(position).username);
            read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
            if(read_ids.equals("")){

                holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
            }else{
                holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
            }
            holder.zhicheng.setText(list.get(position).rank);
            Area_cate area_cate = list.get(position).area_cate;
            if (area_cate != null) {
                holder.my_linyu.setText(area_cate.getArea_cate1());
            }
            holder.my_xianqin.setText(list.get(position).description);
//            if(list.get(position).getSource()==null || list.get(position).getSource().equals("")){
//                holder.rc_zan.setVisibility(View.GONE);
//            }else{
//                holder.rc_zan.setVisibility(View.VISIBLE);
//                holder.rc_zan.setText(list.get(position).getSource());
//            }
//            holder.rc_zan.setText(list.get(position).zan);
            holder.rc_look.setText(list.get(position).click);


        } else if (list.get(position).getTypename().equals("专利")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.VISIBLE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            holder.zhuanli_zan.setText(list.get(position).getZan());
            holder.my_zhuanli_title.setText(list.get(position).getTitle());
            holder.zhuanli_name.setText(list.get(position).getTypename());
            read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
            if(read_ids.equals("")){

                holder.my_zhuanli_title.setTextColor(Color.parseColor("#181818"));
            }else{
                holder.my_zhuanli_title.setTextColor(Color.parseColor("#777777"));
            }
            holder.zhuanli_look.setText(list.get(position).getClick());
        } else if (list.get(position).getTypename().equals("实验室")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.VISIBLE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            if (list.get(position).imageState.equals("3")) {
                holder.tj_state_line.setVisibility(View.VISIBLE);
                holder.laboratory_text.setVisibility(View.GONE);
                holder.imageView3.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.tj_state_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");

                if(read_ids.equals("")){

                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                }
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.tj_state_img1, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image2
                                , holder.tj_state_img2, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image3
                                , holder.tj_state_img3, options);
                    } else {
                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.tj_state_img1, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image2
                            , holder.tj_state_img2, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image3
                            , holder.tj_state_img3, options);
                }

                holder.tj_state_zan.setText(list.get(position).getZan());
                holder.tj_state_click.setText(list.get(position).getClick());
                holder.santu_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("1")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.imageView3.setVisibility(View.VISIBLE);
                holder.laboratory_text.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.my_jianjie.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView3, options);
                    } else {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                    params.height=height;
                    params.width =width;
                    holder.imageView3.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView3, options);
                }
                holder.laboratory_text.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
//            ImageLoader.getInstance().displayImage(list.get(position).getLitpic(), holder.imageView3,options);
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());
                holder.datu_name.setText(list.get(position).getTypename());
            } else if (list.get(position).imageState.equals("0")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.cont_img.setVisibility(View.GONE);
                holder.s_title.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.xiangmu.setVisibility(View.VISIBLE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.xm_img, options);
                    } else {
                        holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.xm_img, options);
                }
                holder.xm_zan.setText(list.get(position).getZan());
                holder.xm_title.setText(list.get(position).getTitle());
                holder.xm_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title.setTextColor(Color.parseColor("#777777"));
                }
                if(list.get(position).area_cate!=null){
                    holder.xm_linyu.setText(list.get(position).area_cate.getArea_cate1());
                }
                holder.xm_description.setText(list.get(position).getDescription());
                holder.xm_look.setText(list.get(position).getClick());
            } else if (list.get(position).imageState.equals("-1")) {
                holder.imageView3.setVisibility(View.GONE);
                holder.laboratory_text.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.laboratory_text.setText(list.get(position).getTitle());
                holder.datu_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
//            ImageLoader.getInstance().displayImage(list.get(position).getLitpic(), holder.imageView3,options);
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());
            }
//            holder.laboratory_text.setText(list.get(position).getTitle());
////            ImageLoader.getInstance().displayImage(list.get(position).getLitpic(), holder.imageView3,options);
//            holder.laboratory_zan.setText(list.get(position).getZan());
//            holder.laboratory_look.setText(list.get(position).getClick());
        } else if (list.get(position).getTypename().equals("政策")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.VISIBLE);
            holder.shebei_line.setVisibility(View.GONE);
            holder.zc_title.setText(list.get(position).getTitle());
            read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
            if(read_ids.equals("")){

                holder.zc_title.setTextColor(Color.parseColor("#181818"));
            }else{
                holder.zc_title.setTextColor(Color.parseColor("#777777"));
            }
            holder.zc_description.setText(list.get(position).getDescription())                            ;
            holder.zc_zan.setText(list.get(position).getZan());
            holder.zc_look.setText(list.get(position).getClick());
            holder.zc_lingyu.setText(list.get(position).getTypename());
        } else if (list.get(position).getTypename().equals("设备")) {
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.VISIBLE);
            if (list.get(position).imageState.equals("3")) {
                holder.tj_state_line.setVisibility(View.VISIBLE);
                holder.shebei_title.setVisibility(View.GONE);
                holder.imageView5.setVisibility(View.GONE);
                holder.shebei_line.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.tj_state_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");

                if(read_ids.equals("")){

                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                }
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.tj_state_img1, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image2
                                , holder.tj_state_img2, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image3
                                , holder.tj_state_img3, options);
                    } else {
                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.tj_state_img1, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image2
                            , holder.tj_state_img2, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image3
                            , holder.tj_state_img3, options);
                }

                holder.tj_state_zan.setText(list.get(position).getZan());
                holder.tj_state_click.setText(list.get(position).getClick());
                holder.santu_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("1")) {
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView3, options);
                    } else {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                    params.height=height;
                    params.width =width;
                    holder.imageView3.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView3, options);
                }
                holder.laboratory_text.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                holder.datu_name.setText(list.get(position).getTypename());
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());
                holder.datu_name.setText(list.get(position).getTypename());
            } else if (list.get(position).imageState.equals("0")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.imageView5.setVisibility(View.VISIBLE);
                holder.shebei_title.setVisibility(View.VISIBLE);
                holder.shebei_line.setVisibility(View.VISIBLE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView5, options);
                    } else {
                        holder.imageView5.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView5, options);
                }
                holder.shebei_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.shebei_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.shebei_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.shebei_zan.setText(list.get(position).getZan());
                holder.shebei_look.setText(list.get(position).getClick());
            } else if (list.get(position).imageState.equals("-1")) {
                holder.imageView5.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.shebei_title.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.shebei_line.setVisibility(View.VISIBLE);
                holder.shebei_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.shebei_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.shebei_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.shebei_zan.setText(list.get(position).getZan());
                holder.shebei_look.setText(list.get(position).getClick());
            }
            holder.device_name.setText(list.get(position).getTypename());
//            ImageLoader.getInstance().displayImage(list.get(position).getLitpic(), holder.imageView5,options);

        }else{
            holder.head_lin.setVisibility(View.GONE);
            holder.my_jianjie.setVisibility(View.VISIBLE);
            holder.xiangmu.setVisibility(View.GONE);
            holder.rc.setVisibility(View.GONE);
            holder.my_zhuanli.setVisibility(View.GONE);
            holder.laboratory.setVisibility(View.GONE);
            holder.zc_line.setVisibility(View.GONE);
            holder.shebei_line.setVisibility(View.GONE);
            if (list.get(position).imageState.equals("3")) {
                holder.tj_state_line.setVisibility(View.VISIBLE);
                holder.s_title.setVisibility(View.GONE);
                holder.cont_img.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.GONE);
                holder.laboratory.setVisibility(View.GONE);
                holder.tj_state_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");

                if(read_ids.equals("")){

                    holder.tj_state_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.tj_state_title.setTextColor(Color.parseColor("#777777"));
                }
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.tj_state_img1, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image2
                                , holder.tj_state_img2, options);
                        ImageLoader.getInstance().displayImage(list.get(position).image.image3
                                , holder.tj_state_img3, options);
                    } else {
                        holder.tj_state_img1.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img2.setBackgroundResource(R.mipmap.information_placeholder);
                        holder.tj_state_img3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.tj_state_img1, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image2
                            , holder.tj_state_img2, options);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image3
                            , holder.tj_state_img3, options);
                }

                holder.tj_state_zan.setText(list.get(position).getZan());
                holder.tj_state_click.setText(list.get(position).getClick());
                holder.santu_name.setText(list.get(position).getTypename());

            } else if (list.get(position).imageState.equals("1")) {
                holder.laboratory.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);

                holder.my_jianjie.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.imageView3, options);
                    } else {
                        holder.imageView3.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ViewGroup.LayoutParams params = holder.imageView3.getLayoutParams();
                    params.height=height;
                    params.width =width;
                    holder.imageView3.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.imageView3, options);
                }
                holder.laboratory_text.setText(list.get(position).getTitle());
                holder.datu_name.setText(list.get(position).getTypename());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.laboratory_text.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.laboratory_text.setTextColor(Color.parseColor("#777777"));
                }
                holder.laboratory_zan.setText(list.get(position).getZan());
                holder.laboratory_look.setText(list.get(position).getClick());
                holder.datu_name.setText(list.get(position).getTypename());
            } else if (list.get(position).imageState.equals("0")) {
                holder.tj_state_line.setVisibility(View.GONE);
                holder.cont_img.setVisibility(View.VISIBLE);
                holder.s_title.setVisibility(View.VISIBLE);
                holder.my_jianjie.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.GONE);
                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                if (state) {
                    NetUtils.NetType type = NetUtils.getNetType();
                    if (type == NetUtils.NetType.NET_WIFI) {
                        ImageLoader.getInstance().displayImage(list.get(position).image.image1
                                , holder.cont_img, options);
                    } else {
                        holder.cont_img.setBackgroundResource(R.mipmap.information_placeholder);
                    }
                } else {
                    ImageLoader.getInstance().displayImage(list.get(position).image.image1
                            , holder.cont_img, options);
                }
                holder.s_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.s_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.s_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_zan.setText(list.get(position).getZan());
                holder.zx_look.setText(list.get(position).getClick());
                holder.zx_name.setText(list.get(position).getTypename());
            } else if (list.get(position).imageState.equals("-1")) {
                holder.cont_img.setVisibility(View.GONE);
                holder.s_title.setVisibility(View.VISIBLE);
                holder.tj_state_line.setVisibility(View.GONE);
                holder.my_jianjie.setVisibility(View.VISIBLE);
                holder.laboratory.setVisibility(View.GONE);
                holder.s_title.setText(list.get(position).getTitle());
                read_ids = PrefUtils.getString(context, list.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.s_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.s_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_zan.setText(list.get(position).getZan());
                holder.zx_look.setText(list.get(position).getClick());
                holder.zx_name.setText(list.get(position).getTypename());
            }
        }
        }catch (Exception e){}
        return convertView;
    }

    public void setSpeTitle(String title){
        spe_title = title;
    }

    class ViewHolder {
        TextView spe_title;
        TextView notename;
        TextView s_title;
        LinearLayout my_jianjie;
        LinearLayout head_lin;
        //资讯
        ImageView cont_img;
        TextView zx_zan;
        TextView zx_look;
        TextView zx_name;
        //项目
        LinearLayout xiangmu;
        ImageView xm_img;
        TextView xm_title;
        TextView xm_look;
        TextView xm_zan;
        TextView xm_linyu;
        TextView xm_description;
        TextView xm_name;
        //人才
        LinearLayout rc;
        RoundImageView cont_imgs;
        TextView my_rencainame;
        TextView zhicheng;
        TextView my_linyu;
        TextView my_xianqin;
        TextView rc_zan;
        TextView rc_look;
        //专利
        LinearLayout my_zhuanli;
        TextView my_zhuanli_title;
        TextView zhuanli_look;
        TextView zhuanli_zan;
        TextView zhuanli_linyu;
        TextView zhuanli_description;
        TextView zhuanli_name;
        //实验室
        LinearLayout laboratory;
        TextView laboratory_text;
        ImageView imageView3;
        TextView laboratory_zan;
        TextView laboratory_look;
        TextView datu_name;
        //政策
        LinearLayout zc_line;
        TextView zc_title;
        TextView zc_spec;
        TextView zc_look;
        TextView zc_zan;
        TextView zc_description;
        TextView zc_lingyu;
        //设备
        LinearLayout shebei_line;
        ImageView imageView5;
        TextView shebei_title;
        TextView shebei_zan;
        TextView shebei_look;
        TextView device_linyu;
        TextView device_description;
        TextView device_name;

        //三图
        LinearLayout tj_state_line;
        TextView tj_state_title;
        ImageView tj_state_img1;
        ImageView tj_state_img2;
        ImageView tj_state_img3;
        TextView tj_state_zan;
        TextView tj_state_click;
        TextView santu_name;
    }
}
