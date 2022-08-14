package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.DetailsActivity;
import com.maidiantech.ManymoreActivity;
import com.maidiantech.R;
import com.maidiantech.SpecialActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

import Util.PrefUtils;
import application.ImageLoaderUtils;
import entity.Renlist;
import entity.Sblist;
import entity.Shiyanshi;
import entity.Tuijian;
import entity.Xmlist;
import entity.Zclist;
import entity.Zllist;
import entity.Zxlist;
import view.RoundImageView;

/**
 * Created by 13520 on 2016/11/3.
 */
public class SearchbiaoAdapter extends BaseAdapter {
    private Context context;
    private List<Object> dataList;
    private List<String> typeList;
    private DisplayImageOptions options;
    private String type;
    private String read_ids;
    List<Zxlist> zxList ;
    List<Zclist> zhengce;
    List<Sblist> shebei;
    List<Zllist> zhuanli;
    List<Renlist> rencai;
    List<Xmlist> xiangmu;
    List<Shiyanshi> shiyanshi;
    List<Tuijian> tuijian;

    public SearchbiaoAdapter(){}
   public SearchbiaoAdapter(Context context, List<Object> dataList,List<String> typeList){
       this.context=context;
       this.dataList=dataList;
       this.typeList=typeList;
   }
    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
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
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        //快速开发 临时重用
        convertView = null;
        //TypefaceUtil.replaceFont((Activity) context, "fonts/font.ttf");
        if (convertView==null) {
            holder=new ViewHolder();
            type = typeList.get(position);
           if(type.equals("zixun") ||type.equals("tuijian")) {
                convertView=View.inflate(context, R.layout.search_item,null);
               holder.zixun_biao=(LinearLayout) convertView.findViewById(R.id.zixun_biao);
               holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
               holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
               holder.search_img1=(ImageView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_img);
               holder.search_text_title1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_title);
               holder.search_img2=(ImageView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_img);
               holder.search_text_title2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_title);
               holder.search_img3=(ImageView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_img);
               holder.search_text_title3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_title);
               holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
               holder.search_hide3=(LinearLayout) convertView.findViewById(R.id.search_hide3);
               holder.search_hide2=(LinearLayout) convertView.findViewById(R.id.search_hide2);
               holder.search_fin=(LinearLayout) convertView.findViewById(R.id.search_fin);
               holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
               holder.zixun_item1=(LinearLayout) convertView.findViewById(R.id.zixun_item1);
//               holder.zx_description1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_description);
//               holder.zx_description2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_description);
//               holder.zx_description3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_description);
//               holder.zx_lanyuan1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_lanyuan);
//               holder.zx_lanyuan2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_lanyuan);
//               holder.zx_lanyuan3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_lanyuan);
//               holder.zx_update1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_update);
//               holder.zx_update2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_update);
//               holder.zx_update3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_update);
               holder.zx_zan1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_zan);
               holder.zx_zan2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_zan);
               holder.zx_zan3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_zan);

               holder.zx_look1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_look);
               holder.zx_look2=(TextView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_look);
               holder.zx_look3=(TextView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_look);
               holder.zxs_zt1=(ImageView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_zt);
               holder.zxs_zt2=(ImageView) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_zt);
               holder.zxs_zt3=(ImageView) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_zt);
               holder.zx_dianzan1=(LinearLayout) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_line_zan);
               holder.zx_dianzan2=(LinearLayout) convertView.findViewById(R.id.search_position2).findViewById(R.id.zx_line_zan);
               holder.zx_dianzan3=(LinearLayout) convertView.findViewById(R.id.search_position3).findViewById(R.id.zx_line_zan);

            }
            if(type.equals("shebei")){
                convertView=View.inflate(context, R.layout.search_device,null);
                holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
                holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
                holder.device_biao=(LinearLayout) convertView.findViewById(R.id.device_biao);
                holder.device_hide3=(LinearLayout) convertView.findViewById(R.id.device_hide3);
                holder.device_hide2=(LinearLayout) convertView.findViewById(R.id.device_hide2);
                holder.device_item1=(LinearLayout) convertView.findViewById(R.id.device_item1);
                holder.shebei_title1=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_title);
                holder.shebei_title2=(TextView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_title);
                holder.shebei_title3=(TextView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_title);
                holder.shebei_description1=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_description);
                holder.shebei_description2=(TextView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_description);
                holder.shebei_description3=(TextView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_description);
                holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
                holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
                holder.search_fin=(LinearLayout) convertView.findViewById(R.id.search_fin);
                holder.imageView1=(ImageView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_img);
                holder.imageView2=(ImageView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_img);
                holder.imageView3=(ImageView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_img);
                holder.shebei_linyu1=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_linyu);
                holder.shebei_linyu2=(TextView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_linyu);
                holder.shebei_linyu3=(TextView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_linyu);
                holder.shebei_zan1=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_zan);
                holder.shebei_zan2=(TextView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_zan);
                holder.shebei_zan3=(TextView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_zan);
                holder.shebei_look1=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_look);
                holder.shebei_look2=(TextView) convertView.findViewById(R.id.device_position2).findViewById(R.id.device_look);
                holder.shebei_look3=(TextView) convertView.findViewById(R.id.device_position3).findViewById(R.id.device_look);

                holder.device_zt1=(ImageView) convertView.findViewById(R.id.device_position1).findViewById(R.id.sb_zt);
                holder.device_zt2=(ImageView) convertView.findViewById(R.id.device_position2).findViewById(R.id.sb_zt);
                holder.device_zt3=(ImageView) convertView.findViewById(R.id.device_position3).findViewById(R.id.sb_zt);
                holder.device_dianzan1=(LinearLayout) convertView.findViewById(R.id.device_position1).findViewById(R.id.shebei_dianzan);
                holder.device_dianzan2=(LinearLayout) convertView.findViewById(R.id.device_position2).findViewById(R.id.shebei_dianzan);
                holder.device_dianzan3=(LinearLayout) convertView.findViewById(R.id.device_position3).findViewById(R.id.shebei_dianzan);

            }
            if(type.equals("zhengce")){
                convertView=View.inflate(context, R.layout.search_zc,null);
                holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
                holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
                holder.zc_biao=(LinearLayout) convertView.findViewById(R.id.zc_biao);
                holder.zc_title1=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_title);
                holder.zc_title2=(TextView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_title);
                holder.zc_title3=(TextView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_title);
                holder.zc_description1=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_description);
                holder.zc_description2=(TextView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_description);
                holder.zc_description3=(TextView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_description);
                holder.zc_zan1=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_zan);
                holder.zc_zan2=(TextView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_zan);
                holder.zc_zan3=(TextView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_zan);

                holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
                holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
                holder.search_fin=(LinearLayout) convertView.findViewById(R.id.search_fin);
                holder.zc_hide3=(LinearLayout) convertView.findViewById(R.id.zc_hide3);
                holder.zc_hide2=(LinearLayout) convertView.findViewById(R.id.zc_hide2);
                holder.zc_item1=(LinearLayout) convertView.findViewById(R.id.zc_item1);
//                holder.zc_unit1=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_unit);
//                holder.zc_unit2=(TextView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_unit);
//                holder.zc_unit3=(TextView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_unit);
                holder.zc_look1=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_look);
                holder.zc_look2=(TextView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_look);
                holder.zc_look3=(TextView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_look);
                holder.zc_zt1=(ImageView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_zt);
                holder.zc_zt2=(ImageView) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_zt);
                holder.zc_zt3=(ImageView) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_zt);
                holder.zc_dianzan1=(LinearLayout) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_dianzan);
                holder.zc_dianzan2=(LinearLayout) convertView.findViewById(R.id.search_zc2).findViewById(R.id.zc_dianzan);
                holder.zc_dianzan3=(LinearLayout) convertView.findViewById(R.id.search_zc3).findViewById(R.id.zc_dianzan);
            }
            if(type.equals("xiangmu")){
                convertView=View.inflate(context, R.layout.search_xm,null);
//
                holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
                holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
                holder.xm_biao=(LinearLayout) convertView.findViewById(R.id.xm_biao);
                holder.xm_img1=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_img);
                holder.xm_img2=(ImageView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_img);
                holder.xm_img3=(ImageView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_img);
                holder.xm_title1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_title);
                holder.xm_title2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_title);
                holder.xm_title3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_title);
                holder.xm_linyu1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_linyu);
                holder.xm_linyu2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_linyu);
                holder.xm_linyu3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_linyu);
                holder.xm_description1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_description);
                holder.xm_description2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_description);
                holder.xm_description3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_description);
                holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
                holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
                holder.search_fin=(LinearLayout) convertView.findViewById(R.id.search_fin);
                holder.xm_hide3=(LinearLayout) convertView.findViewById(R.id.xm_hide3);
                holder.xm_hide2=(LinearLayout) convertView.findViewById(R.id.xm_hide2);
                holder.xm_item1=(LinearLayout) convertView.findViewById(R.id.xm_item1);
                holder.xm_zan1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_dianwei);
                holder.xm_zan2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_dianwei);
                holder.xm_zan3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_dianwei);
                holder.xm_look1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_look);
                holder.xm_look2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_look);
                holder.xm_look3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_look);
                holder.xm_zt1=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_zt);
                holder.xm_zt2=(ImageView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_zt);
                holder.xm_zt3=(ImageView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_zt);
            }

           if(type.equals("rencai")){
                convertView=View.inflate(context, R.layout.search_rencai,null);
               holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
               holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
               holder.search_rencai_img1=(RoundImageView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_img);
               holder.search_rencai_uname1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_uname);
               holder.search_rencai_zhicheng1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_zhicheng);
               holder.search_rencai_linyu1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_linyu);
               holder.search_rencai_title1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_title);
              holder.search_rencai_img2=(RoundImageView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_img);
               holder.search_rencai_uname2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_uname);
               holder.search_rencai_zhicheng2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_zhicheng);
               holder.search_rencai_linyu2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_linyu);
               holder.search_rencai_title2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_title);
               holder.search_rencai_img3=(RoundImageView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_img);
               holder.search_rencai_uname3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_uname);
               holder.search_rencai_zhicheng3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_zhicheng);
               holder.search_rencai_linyu3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_linyu);
               holder.search_rencai_title3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_title);
               holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
               holder.search_hide3=(LinearLayout) convertView.findViewById(R.id.search_hide3);
               holder.search_rc_two=(LinearLayout) convertView.findViewById(R.id.search_rc_two);
               holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
               holder.rencai_item1=(LinearLayout) convertView.findViewById(R.id.rencai_item1);

               holder.rc_zan1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_zan);
               holder.rc_zan2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_zan);
               holder.rc_zan3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_zan);

               holder.rc_look1=(TextView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_look);
               holder.rc_look2=(TextView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_look);
               holder.rc_look3=(TextView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_look);

               holder.rc_yuanshi1=(ImageView) convertView.findViewById(R.id.search_rencai1).findViewById(R.id.rc_yuanshi);
               holder.rc_yuanshi2=(ImageView) convertView.findViewById(R.id.search_rencai2).findViewById(R.id.rc_yuanshi);
               holder.rc_yuanshi3=(ImageView) convertView.findViewById(R.id.search_rencai3).findViewById(R.id.rc_yuanshi);


            }
            if(type.equals("zhuanli")){
               convertView=View.inflate(context, R.layout.search_zhuanli,null);
               holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
               holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
               holder.search_zhuanli_two=(LinearLayout) convertView.findViewById(R.id.search_zhuanli_two);
               holder.search_zhuanli_three=(LinearLayout) convertView.findViewById(R.id.search_zhuanli_three);
               holder.search_zhuanli_four=(LinearLayout) convertView.findViewById(R.id.search_zhuanli_four);
               holder.search_zhunli_title1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhunli_title);
               holder.search_zhunli_title2=(TextView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhunli_title);
               holder.search_zhunli_title3=(TextView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhunli_title);
               holder.search_zhunli_title4=(TextView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhunli_title);
               holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
                holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
                holder.zhuanli_item1=(LinearLayout) convertView.findViewById(R.id.zhuanli_item1);
                holder.zhuanli_linyu1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_linyu);
                holder.zhuanli_linyu2=(TextView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhuanli_linyu);
                holder.zhuanli_linyu3=(TextView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhuanli_linyu);
                holder.zhuanli_linyu4=(TextView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhuanli_linyu);
                holder.zhuanli_look1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_look);
                holder.zhuanli_look2=(TextView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhuanli_look);
                holder.zhuanli_look3=(TextView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhuanli_look);
                holder.zhuanli_look4=(TextView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhuanli_look);
                holder.zhuanli_zan1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_zan);
                holder.zhuanli_zan2=(TextView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhuanli_zan);
                holder.zhuanli_zan3=(TextView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhuanli_zan);
                holder.zhuanli_zan4=(TextView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhuanli_zan);
                holder.zhuanli_zt1=(ImageView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_zt);
                holder.zhuanli_zt2=(ImageView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhuanli_zt);
                holder.zhuanli_zt3=(ImageView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhuanli_zt);
                holder.zhuanli_zt4=(ImageView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhuanli_zt);
                holder.zhuanli_description1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_description);
                holder.zhuanli_description2=(TextView) convertView.findViewById(R.id.search_zhuanli2).findViewById(R.id.zhuanli_description);
                holder.zhuanli_description3=(TextView) convertView.findViewById(R.id.search_zhuanli3).findViewById(R.id.zhuanli_description);
                holder.zhuanli_description4=(TextView) convertView.findViewById(R.id.search_zhuanli4).findViewById(R.id.zhuanli_description);
           }
            if(type.equals("shiyanshi")){
                convertView=View.inflate(context, R.layout.shiyanshi,null);

                holder.type_name=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.type_name);
                holder.search_num=(TextView) convertView.findViewById(R.id.search_heads).findViewById(R.id.search_num);
                holder.library_biao=(LinearLayout) convertView.findViewById(R.id.library_biao);
                holder.library_item1=(LinearLayout) convertView.findViewById(R.id.library_item1);
                holder.library_hide2=(LinearLayout) convertView.findViewById(R.id.library_hide2);
                holder.library_hide3=(LinearLayout) convertView.findViewById(R.id.library_hide3);
                holder.search_moreid=(LinearLayout) convertView.findViewById(R.id.search_moreid);
                holder.search_many_more=(LinearLayout) convertView.findViewById(R.id.search_many_more);
                holder.search_fin=(LinearLayout) convertView.findViewById(R.id.search_fin);

                holder.librarys_title1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_title);
                holder.librarys_title2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_title);
                holder.librarys_title3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_title);

                holder.librarys_linyu1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_linyu);
                holder.librarys_linyu2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_linyu);
                holder.librarys_linyu3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_linyu);
                holder.sys_description1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_description);
                holder.sys_description2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_description);
                holder.sys_description3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_description);

                holder.librarys_img1=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_img);
                holder.librarys_img2=(ImageView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_img);
                holder.librarys_img3=(ImageView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_img);

                holder.librarys_zan1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_dianwei);
                holder.librarys_zan2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_dianwei);
                holder.librarys_zan3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_dianwei);

                holder.librarys_look1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_look);
                holder.librarys_look2=(TextView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_look);
                holder.librarys_look3=(TextView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_look);

                holder.sys_zt1=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_zt);
                holder.sys_zt2=(ImageView) convertView.findViewById(R.id.search_xm2).findViewById(R.id.xm_zt);
                holder.sys_zt3=(ImageView) convertView.findViewById(R.id.search_xm3).findViewById(R.id.xm_zt);

            }
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        Object obj = dataList.get(position);
        if(type.equals("shiyanshi")) {
            shiyanshi = (List<Shiyanshi>) obj;
            if(shiyanshi.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(shiyanshi.size()>2){
                holder.library_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.library_hide3.setVisibility(View.GONE);
            }
            if(shiyanshi.size()>1){
                holder.library_hide2.setVisibility(View.VISIBLE);
            }else{
                holder.library_hide2.setVisibility(View.GONE);
            }

            holder.type_name.setText("研究所");
            holder.search_num.setText(shiyanshi.size()+"条");
            try {
                if (shiyanshi.get(0).getTypename().equals("专题")) {
                    holder.sys_zt1.setVisibility(View.VISIBLE);

                } else {
                    holder.sys_zt1.setVisibility(View.GONE);

                }
                options = ImageLoaderUtils.initOptions();
                if(shiyanshi.get(0).getLitpic().equals("")){
                    holder.librarys_img1.setVisibility(View.GONE);
                }else{
                    holder.librarys_img1.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shiyanshi.get(0).getLitpic()
                            , holder.librarys_img1, options);
                }

                holder.librarys_title1.setText(shiyanshi.get(0).getTitle());
                read_ids = PrefUtils.getString(context, shiyanshi.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.librarys_zan1.setText(shiyanshi.get(0).getZan());
                holder.librarys_look1.setText(shiyanshi.get(0).getClick());
                if(shiyanshi.get(0).getArea_cate()!=null){
                    holder.librarys_linyu1.setText(shiyanshi.get(0).getArea_cate().getArea_cate1());
                }
                if(shiyanshi.get(0).getDescription()!=null || !shiyanshi.get(0).getDescription().equals("")){
                    holder.sys_description1.setVisibility(View.VISIBLE);
                    holder.sys_description1.setText(shiyanshi.get(0).getDescription());
                }else {
                    holder.sys_description1.setVisibility(View.GONE);
                }
                holder.library_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shiyanshi.get(0).getId(),shiyanshi.get(0).getId());
                        if(shiyanshi.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shiyanshi.get(0).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);

                            intent.putExtra("id",shiyanshi.get(0).getId());
                            // Log.i("zixunid",zxList.get(0).getId());
                            intent.putExtra("name", shiyanshi.get(0).getTypename());
                            intent.putExtra("pic",shiyanshi.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (shiyanshi.get(1).getTypename().equals("专题")) {
                    holder.sys_zt2.setVisibility(View.VISIBLE);

                } else {
                    holder.sys_zt2.setVisibility(View.GONE);

                }
                if(shiyanshi.get(1).getLitpic().equals("")){
                    holder.librarys_img2.setVisibility(View.GONE);
                }else{
                    holder.librarys_img2.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shiyanshi.get(1).getLitpic()
                            , holder.librarys_img2, options);
                }

                holder.librarys_title2.setText(shiyanshi.get(1).getTitle());
                read_ids = PrefUtils.getString(context, shiyanshi.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title2.setTextColor(Color.parseColor("#777777"));
                }
                holder.librarys_zan2.setText(shiyanshi.get(1).getZan());
                holder.librarys_look2.setText(shiyanshi.get(1).getClick());
                if(shiyanshi.get(1).getArea_cate()!=null){
                    holder.librarys_linyu2.setText(shiyanshi.get(1).getArea_cate().getArea_cate1());
                }
                if(shiyanshi.get(1).getDescription()!=null ||!shiyanshi.get(1).getDescription().equals("")){
                    holder.sys_description2.setVisibility(View.VISIBLE);
                    holder.sys_description2.setText(shiyanshi.get(1).getDescription());
                }else if(shiyanshi.get(1).getDescription().equals("")){
                    holder.sys_description2.setVisibility(View.GONE);
                }
                holder.library_hide2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shiyanshi.get(1).getId(),shiyanshi.get(1).getId());
                        if(shiyanshi.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shiyanshi.get(1).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",shiyanshi.get(1).getId());
                            intent.putExtra("name", shiyanshi.get(1).getTypename());
                            intent.putExtra("pic",shiyanshi.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (shiyanshi.get(2).getTypename().equals("专题")) {
                    holder.sys_zt3.setVisibility(View.VISIBLE);

                } else {
                    holder.sys_zt3.setVisibility(View.GONE);

                }
                if(shiyanshi.get(2).getLitpic().equals("")){
                    holder.librarys_img3.setVisibility(View.GONE);
                }else{
                    holder.librarys_img3.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shiyanshi.get(2).getLitpic()
                            , holder.librarys_img3, options);
                }

                holder.librarys_title3.setText(shiyanshi.get(2).getTitle());
                read_ids = PrefUtils.getString(context, shiyanshi.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title3.setTextColor(Color.parseColor("#777777"));
                }
                holder.librarys_zan3.setText(shiyanshi.get(2).getZan());
                holder.librarys_look3.setText(shiyanshi.get(2).getClick());
                if(shiyanshi.get(2).getArea_cate()!=null){
                    holder.librarys_linyu3.setText(shiyanshi.get(2).getArea_cate().getArea_cate1());
                }
                if(shiyanshi.get(2).getDescription()!=null ||!shiyanshi.get(2).getDescription().equals("")){
                    holder.sys_description3.setVisibility(View.VISIBLE);
                    holder.sys_description3.setText(shiyanshi.get(2).getDescription());
                }else if(shiyanshi.get(2).getDescription().equals("")){
                    holder.sys_description3.setVisibility(View.GONE);
                }
                holder.library_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shiyanshi.get(2).getId(),shiyanshi.get(2).getId());
                        if(shiyanshi.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shiyanshi.get(2).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",shiyanshi.get(2).getId());
                            intent.putExtra("name", shiyanshi.get(2).getTypename());
                            intent.putExtra("pic",shiyanshi.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });

            }catch (Exception e){}
        }
           if(type.equals("zixun")) {
               zxList = (List<Zxlist>) obj;
               if(zxList.size()>3){
                   holder.search_moreid.setVisibility(View.VISIBLE);
               }else{
                   holder.search_moreid.setVisibility(View.GONE);
               }
               if(zxList.size()>2){
                   holder.search_hide3.setVisibility(View.VISIBLE);
               }else{
                   holder.search_hide3.setVisibility(View.GONE);
               }
               if(zxList.size()>1){
                   holder.search_hide2.setVisibility(View.VISIBLE);
               }else{
                   holder.search_hide2.setVisibility(View.GONE);
               }

               holder.type_name.setText("资讯");
               holder.search_num.setText(zxList.size()+"条");
               try {
                   if (zxList.get(0).getTypename().equals("专题")) {
                       holder.zxs_zt1.setVisibility(View.VISIBLE);
                       holder.zx_dianzan1.setVisibility(View.GONE);
                   } else {
                       holder.zxs_zt1.setVisibility(View.GONE);
                       holder.zx_dianzan1.setVisibility(View.GONE);
                   }
                   options = ImageLoaderUtils.initOptions();
                   if(zxList.get(0).getLitpic().equals("")){
                       holder.search_img1.setVisibility(View.GONE);
                   }else{
                       holder.search_img1.setVisibility(View.VISIBLE);

                       ImageLoader.getInstance().displayImage(zxList.get(0).getLitpic()
                               , holder.search_img1, options);
                   }
//                   ImageLoader.getInstance().displayImage(zxList.get(0).getLitpic()
//                           , holder.search_img1, options);
                   holder.search_text_title1.setText(zxList.get(0).getTitle());
                   read_ids = PrefUtils.getString(context, zxList.get(0).getId(), "");
                   if(read_ids.equals("")){

                       holder.search_text_title1.setTextColor(Color.parseColor("#181818"));
                   }else{
                       holder.search_text_title1.setTextColor(Color.parseColor("#777777"));
                   }
//                   holder.zx_description1.setText(zxList.get(0).getDescription());
                   holder.zx_zan1.setText(zxList.get(0).getZan());
                   holder.zx_look1.setText(zxList.get(0).getClick());
                   holder.zixun_item1.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           PrefUtils.setString(context,zxList.get(0).getId(),zxList.get(0).getId());
                           if(zxList.get(0).getTypename().equals("专题")){
                               Intent intent = new Intent(context, SpecialActivity.class);
                               intent.putExtra("id", zxList.get(0).getId());

                               context.startActivity(intent);
                           }else{
                               Intent intent=new Intent(context,ZixunDetailsActivity.class);
                               intent.putExtra("id",zxList.get(0).getId());
                               intent.putExtra("name", zxList.get(0).getTypename());
                               // Log.i("zixunid",zxList.get(0).getId());
                               intent.putExtra("pic",zxList.get(0).getLitpic());
                               context.startActivity(intent);
                           }

                       }
                   });
                   if (zxList.get(1).getTypename().equals("专题")) {
                       holder.zxs_zt2.setVisibility(View.VISIBLE);
                       holder.zx_dianzan2.setVisibility(View.GONE);
                   } else {
                       holder.zxs_zt2.setVisibility(View.GONE);
                       holder.zx_dianzan2.setVisibility(View.GONE);
                   }
                   if(zxList.get(1).getLitpic().equals("")){
                       holder.search_img2.setVisibility(View.GONE);
                   }else{
                       holder.search_img2.setVisibility(View.VISIBLE);

                       ImageLoader.getInstance().displayImage(zxList.get(1).getLitpic()
                               , holder.search_img2, options);
                   }
//                   ImageLoader.getInstance().displayImage(zxList.get(1).getLitpic()
//                           , holder.search_img2, options);
                   holder.search_text_title2.setText(zxList.get(1).getTitle());
                   read_ids = PrefUtils.getString(context, zxList.get(1).getId(), "");
                   if(read_ids.equals("")){

                       holder.search_text_title2.setTextColor(Color.parseColor("#181818"));
                   }else{
                       holder.search_text_title2.setTextColor(Color.parseColor("#777777"));
                   }
//                   holder.zx_description2.setText(zxList.get(1).getDescription());
                   holder.zx_zan2.setText(zxList.get(1).getZan());
                   holder.zx_look2.setText(zxList.get(1).getClick());
                   holder.search_hide2.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           PrefUtils.setString(context,zxList.get(1).getId(),zxList.get(1).getId());
                           if(zxList.get(1).getTypename().equals("专题")){
                               Intent intent = new Intent(context, SpecialActivity.class);
                               intent.putExtra("id", zxList.get(1).getId());

                               context.startActivity(intent);
                           }else{
                               Intent intent=new Intent(context,ZixunDetailsActivity.class);
                               intent.putExtra("id",zxList.get(1).getId());
                               intent.putExtra("name", zxList.get(1).getTypename());
                               intent.putExtra("pic",zxList.get(1).getLitpic());
                               context.startActivity(intent);
                           }

                       }
                   });
                   if (zxList.get(2).getTypename().equals("专题")) {
                       holder.zxs_zt3.setVisibility(View.VISIBLE);
                       holder.zx_dianzan3.setVisibility(View.GONE);
                   } else {
                       holder.zxs_zt3.setVisibility(View.GONE);
                       holder.zx_dianzan3.setVisibility(View.GONE);
                   }
                   if(zxList.get(2).getLitpic().equals("")){
                       holder.search_img3.setVisibility(View.GONE);
                   }else{
                       holder.search_img3.setVisibility(View.VISIBLE);

                       ImageLoader.getInstance().displayImage(zxList.get(2).getLitpic()
                               , holder.search_img3, options);
                   }
//                   ImageLoader.getInstance().displayImage(zxList.get(2).getLitpic()
//                           , holder.search_img3, options);
                   holder.search_text_title3.setText(zxList.get(2).getTitle());
                   read_ids = PrefUtils.getString(context, zxList.get(2).getId(), "");
                   if(read_ids.equals("")){

                       holder.search_text_title3.setTextColor(Color.parseColor("#181818"));
                   }else{
                       holder.search_text_title3.setTextColor(Color.parseColor("#777777"));
                   }
//                   holder.zx_description3.setText(zxList.get(2).getDescription());
                   holder.zx_zan3.setText(zxList.get(2).getZan());
                   holder.zx_look3.setText(zxList.get(2).getClick());
                   holder.search_hide3.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           PrefUtils.setString(context,zxList.get(2).getId(),zxList.get(2).getId());
                           if(zxList.get(2).getTypename().equals("专题")){
                               Intent intent = new Intent(context, SpecialActivity.class);
                               intent.putExtra("id", zxList.get(2).getId());

                               context.startActivity(intent);
                           }else{
                               Intent intent=new Intent(context,ZixunDetailsActivity.class);
                               intent.putExtra("id",zxList.get(2).getId());
                               intent.putExtra("name", zxList.get(2).getTypename());
                               intent.putExtra("pic",zxList.get(2).getLitpic());
                               context.startActivity(intent);
                           }

                       }
                   });

               }catch (Exception e){}
        }
        if(type.equals("tuijian")) {
            tuijian = (List<Tuijian>) obj;
            if(tuijian.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(tuijian.size()>2){
                holder.search_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.search_hide3.setVisibility(View.GONE);
            }
            if(tuijian.size()>1){
                holder.search_hide2.setVisibility(View.VISIBLE);
            }else{
                holder.search_hide2.setVisibility(View.GONE);
            }
            holder.type_name.setText("推荐");
            holder.search_num.setText(tuijian.size()+"条");
            try {
                if (tuijian.get(0).getTypename().equals("专题")) {
                    holder.zxs_zt1.setVisibility(View.VISIBLE);
                    holder.zx_dianzan1.setVisibility(View.GONE);
                } else {
                    holder.zxs_zt1.setVisibility(View.GONE);
                    holder.zx_dianzan1.setVisibility(View.GONE);
                }
                options = ImageLoaderUtils.initOptions();
                if(tuijian.get(0).getLitpic().equals("")){
                    holder.search_img1.setVisibility(View.GONE);
                }else{
                    holder.search_img1.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(tuijian.get(0).getLitpic()
                            , holder.search_img1, options);
                }
//                   ImageLoader.getInstance().displayImage(zxList.get(0).getLitpic()
//                           , holder.search_img1, options);
                holder.search_text_title1.setText(tuijian.get(0).getTitle());
                read_ids = PrefUtils.getString(context, tuijian.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.search_text_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_text_title1.setTextColor(Color.parseColor("#777777"));
                }
//                   holder.zx_description1.setText(zxList.get(0).getDescription());
                holder.zx_zan1.setText(tuijian.get(0).getZan());
                holder.zx_look1.setText(tuijian.get(0).getClick());
                holder.zixun_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,tuijian.get(0).getId(),tuijian.get(0).getId());
                        if(tuijian.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", tuijian.get(0).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);

                            intent.putExtra("id",tuijian.get(0).getId());
                            intent.putExtra("name", tuijian.get(0).getTypename());
                            // Log.i("zixunid",zxList.get(0).getId());
                            intent.putExtra("pic",tuijian.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (tuijian.get(1).getTypename().equals("专题")) {
                    holder.zxs_zt2.setVisibility(View.VISIBLE);
                    holder.zx_dianzan2.setVisibility(View.GONE);
                } else {
                    holder.zxs_zt2.setVisibility(View.GONE);
                    holder.zx_dianzan2.setVisibility(View.GONE);
                }
                if(tuijian.get(1).getLitpic().equals("")){
                    holder.search_img2.setVisibility(View.GONE);
                }else{
                    holder.search_img2.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(tuijian.get(1).getLitpic()
                            , holder.search_img2, options);
                }
//                   ImageLoader.getInstance().displayImage(zxList.get(1).getLitpic()
//                           , holder.search_img2, options);
                holder.search_text_title2.setText(tuijian.get(1).getTitle());
                read_ids = PrefUtils.getString(context, tuijian.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.search_text_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_text_title2.setTextColor(Color.parseColor("#777777"));
                }
//                   holder.zx_description2.setText(zxList.get(1).getDescription());
                holder.zx_zan2.setText(tuijian.get(1).getZan());
                holder.zx_look2.setText(tuijian.get(1).getClick());
                holder.search_hide2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,tuijian.get(1).getId(),tuijian.get(1).getId());
                        if(tuijian.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", tuijian.get(1).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",tuijian.get(1).getId());
                            intent.putExtra("name", tuijian.get(1).getTypename());
                            intent.putExtra("pic",tuijian.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (tuijian.get(2).getTypename().equals("专题")) {
                    holder.zxs_zt3.setVisibility(View.VISIBLE);
                    holder.zx_dianzan3.setVisibility(View.GONE);
                } else {
                    holder.zxs_zt3.setVisibility(View.GONE);
                    holder.zx_dianzan3.setVisibility(View.GONE);
                }
                if(tuijian.get(2).getLitpic().equals("")){
                    holder.search_img3.setVisibility(View.GONE);
                }else{
                    holder.search_img3.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(tuijian.get(2).getLitpic()
                            , holder.search_img3, options);
                }
//                   ImageLoader.getInstance().displayImage(zxList.get(2).getLitpic()
//                           , holder.search_img3, options);
                holder.search_text_title3.setText(tuijian.get(2).getTitle());
                read_ids = PrefUtils.getString(context, tuijian.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.search_text_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_text_title3.setTextColor(Color.parseColor("#777777"));
                }
//                   holder.zx_description3.setText(zxList.get(2).getDescription());
                holder.zx_zan3.setText(tuijian.get(2).getZan());
                holder.zx_look3.setText(tuijian.get(2).getClick());
                holder.search_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,tuijian.get(2).getId(),tuijian.get(2).getId());
                        if(tuijian.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", tuijian.get(2).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",tuijian.get(2).getId());
                            intent.putExtra("name", tuijian.get(2).getTypename());
                            intent.putExtra("pic",tuijian.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });

            }catch (Exception e){}
        }

     if(type.equals("shebei")) {
            shebei = (List<Sblist>) obj;
            if(shebei.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(shebei.size()>2){
                holder.device_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.device_hide3.setVisibility(View.GONE);
            }
            if(shebei.size()>1){
                holder.device_hide2.setVisibility(View.VISIBLE);
            }else{
                holder.device_hide2.setVisibility(View.GONE);
            }
            holder.type_name.setText("设备");
            holder.search_num.setText(shebei.size()+"条");
            try {
                if (shebei.get(0).getTypename().equals("专题")) {
                    holder.device_zt1.setVisibility(View.VISIBLE);
                    holder.device_dianzan1.setVisibility(View.GONE);
                } else {
                    holder.device_zt1.setVisibility(View.GONE);
                    holder.device_dianzan1.setVisibility(View.GONE);
                }
                options = ImageLoaderUtils.initOptions();
                if(shebei.get(0).getLitpic().equals("")){
                    holder.imageView1.setVisibility(View.GONE);
                }else{
                    holder.imageView1.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shebei.get(0).getLitpic()
                            , holder.imageView1, options);
                }
//                ImageLoader.getInstance().displayImage(shebei.get(0).getLitpic()
//                        , holder.imageView1, options);
                holder.shebei_title1.setText(shebei.get(0).getTitle());
                read_ids = PrefUtils.getString(context, shebei.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.shebei_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.shebei_title1.setTextColor(Color.parseColor("#777777"));
                }
//                holder.shebei_description1.setText(shebei.get(0).getDescription());
                holder.shebei_zan1.setText(shebei.get(0).getZan());
                holder.shebei_look1.setText(shebei.get(0).getClick());
                if(shebei.get(0).getArea_cate()!=null){
                   holder.shebei_linyu1.setText(shebei.get(0).getArea_cate().getArea_cate1());
                }
                if(shebei.get(0).getDescription()!=null ||!shebei.get(0).getDescription().equals("")){
                    holder.shebei_description1.setVisibility(View.VISIBLE);
                    holder.shebei_description1.setText(shebei.get(0).getDescription());
                }else {
                    holder.shebei_description1.setVisibility(View.GONE);
                }
                holder.device_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shebei.get(0).getId(),shebei.get(0).getId());
                        if(shebei.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shebei.get(0).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",shebei.get(0).getId());
                            intent.putExtra("name", shebei.get(0).getTypename());
                            intent.putExtra("pic",shebei.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (shebei.get(1).getTypename().equals("专题")) {
                    holder.device_zt2.setVisibility(View.VISIBLE);
                    holder.device_dianzan2.setVisibility(View.GONE);
                } else {
                    holder.device_zt2.setVisibility(View.GONE);
                    holder.device_dianzan2.setVisibility(View.GONE);
                }
                if(shebei.get(1).getLitpic().equals("")){
                    holder.imageView2.setVisibility(View.GONE);
                }else{
                    holder.imageView2.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shebei.get(1).getLitpic()
                            , holder.imageView2, options);
                }
//                ImageLoader.getInstance().displayImage(shebei.get(1).getLitpic()
//                        , holder.imageView2, options);
                holder.shebei_title2.setText(shebei.get(1).getTitle());
                read_ids = PrefUtils.getString(context, shebei.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.shebei_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.shebei_title2.setTextColor(Color.parseColor("#777777"));
                }
//                holder.shebei_description2.setText(shebei.get(1).getDescription());
                holder.shebei_zan2.setText(shebei.get(1).getZan());
                holder.shebei_look2.setText(shebei.get(1).getClick());
                if(shebei.get(1).getArea_cate()!=null){
                    holder.shebei_linyu2.setText(shebei.get(1).getArea_cate().getArea_cate1());
                }
                if(shebei.get(1).getDescription()!=null || !shebei.get(1).getDescription().equals("")){
                    holder.shebei_description2.setVisibility(View.VISIBLE);
                    holder.shebei_description2.setText(shebei.get(1).getDescription());
                }else {
                    holder.shebei_description2.setVisibility(View.GONE);
                }
                holder.device_hide2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shebei.get(1).getId(),shebei.get(1).getId());
                        if(shebei.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shebei.get(1).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",shebei.get(1).getId());
                            intent.putExtra("name", shebei.get(1).getTypename());
                            intent.putExtra("pic",shebei.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (shebei.get(2).getTypename().equals("专题")) {
                    holder.device_zt3.setVisibility(View.VISIBLE);
                    holder.device_dianzan3.setVisibility(View.GONE);
                } else {
                    holder.device_zt3.setVisibility(View.GONE);
                    holder.device_dianzan3.setVisibility(View.GONE);
                }
                if(shebei.get(2).getLitpic().equals("")){
                    holder.imageView3.setVisibility(View.GONE);
                }else{
                    holder.imageView3.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(shebei.get(2).getLitpic()
                            , holder.imageView3, options);
                }
//                ImageLoader.getInstance().displayImage(shebei.get(2).getLitpic()
//                        , holder.imageView3, options);
                holder.shebei_title3.setText(shebei.get(2).getTitle());
                read_ids = PrefUtils.getString(context, shebei.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.shebei_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.shebei_title3.setTextColor(Color.parseColor("#777777"));
                }
//                holder.shebei_description3.setText(shebei.get(2).getDescription());
                holder.shebei_zan3.setText(shebei.get(2).getZan());
                holder.shebei_look3.setText(shebei.get(2).getClick());
                if(shebei.get(2).getArea_cate()!=null){
                    holder.shebei_linyu3.setText(shebei.get(2).getArea_cate().getArea_cate1());
                }
                if(shebei.get(2).getDescription()!=null || !shebei.get(2).getDescription().equals("")){
                    holder.shebei_description3.setVisibility(View.VISIBLE);
                    holder.shebei_description3.setText(shebei.get(2).getDescription());
                }else {
                    holder.shebei_description3.setVisibility(View.GONE);
                }
                holder.device_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,shebei.get(2).getId(),shebei.get(2).getId());
                        if(shebei.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", shebei.get(2).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",shebei.get(2).getId());
                            intent.putExtra("name", shebei.get(2).getTypename());
                            intent.putExtra("pic",shebei.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
            }catch (Exception e){}

        }
        if(type.equals("xiangmu")) {
             xiangmu = (List<Xmlist>) obj;
            if(xiangmu.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(xiangmu.size()>2){
                holder.xm_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.xm_hide3.setVisibility(View.GONE);
            }
            if(xiangmu.size()>1){
                holder.xm_hide2.setVisibility(View.VISIBLE);
            }else{
                holder.xm_hide2.setVisibility(View.GONE);
            }
            holder.type_name.setText("项目");
            holder.search_num.setText(xiangmu.size()+"条");
            try {
                if (xiangmu.get(0).getTypename().equals("专题")) {
                    holder.xm_zt1.setVisibility(View.VISIBLE);
                    holder.xm_zan1.setVisibility(View.GONE);
                } else {
                    holder.xm_zt1.setVisibility(View.GONE);
                    holder.xm_zan1.setVisibility(View.GONE);
                }
                options = ImageLoaderUtils.initOptions();
                if(xiangmu.get(0).getLitpic().equals("")){
                    holder.xm_img1.setVisibility(View.GONE);
                }else{
                    holder.xm_img1.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(xiangmu.get(0).getLitpic()
                            , holder.xm_img1, options);
                }
//                ImageLoader.getInstance().displayImage(xiangmu.get(0).getLitpic()
//                        , holder.xm_img1, options);
                holder.xm_title1.setText(xiangmu.get(0).getTitle());
                read_ids = PrefUtils.getString(context, xiangmu.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_zan1.setText(xiangmu.get(0).getZan());
                holder.xm_look1.setText(xiangmu.get(0).getClick());
                if(xiangmu.get(0).getArea_cate()!=null){
                    holder.xm_linyu1.setText(xiangmu.get(0).getArea_cate().getArea_cate1());
                }
                if(xiangmu.get(0).getDescription()!=null || xiangmu.get(0).getDescription().equals("")){
                    holder.xm_description1.setVisibility(View.VISIBLE);
                   holder.xm_description1.setText(xiangmu.get(0).getDescription().replaceAll("\r\n","").replaceAll("\n",""));
                }else{
                    holder.xm_description1.setVisibility(View.GONE);
                }
                holder.xm_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,xiangmu.get(0).getId(),xiangmu.get(0).getId());
                        if(xiangmu.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", xiangmu.get(0).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",xiangmu.get(0).getId());
                            intent.putExtra("name", xiangmu.get(0).getTypename());
                            intent.putExtra("pic",xiangmu.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (xiangmu.get(1).getTypename().equals("专题")) {
                    holder.xm_zt2.setVisibility(View.VISIBLE);
                    holder.xm_zan2.setVisibility(View.GONE);
                } else {
                    holder.xm_zt2.setVisibility(View.GONE);
                    holder.xm_zan2.setVisibility(View.GONE);
                }
                if(xiangmu.get(1).getLitpic().equals("")){
                    holder.xm_img2.setVisibility(View.GONE);
                }else{
                    holder.xm_img2.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(xiangmu.get(1).getLitpic()
                            , holder.xm_img2, options);
                }
//                ImageLoader.getInstance().displayImage(xiangmu.get(1).getLitpic()
//                        , holder.xm_img2, options);
                holder.xm_title2.setText(xiangmu.get(1).getTitle());
                read_ids = PrefUtils.getString(context, xiangmu.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title2.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_zan2.setText(xiangmu.get(1).getZan());
                holder.xm_look2.setText(xiangmu.get(1).getClick());
                if(xiangmu.get(1).getArea_cate()!=null){
                    holder.xm_linyu2.setText(xiangmu.get(1).getArea_cate().getArea_cate1());
                }
                if(xiangmu.get(1).getDescription()!=null || xiangmu.get(1).getDescription().equals("")){
                    holder.xm_description2.setVisibility(View.VISIBLE);
                    holder.xm_description2.setText(xiangmu.get(1).getDescription().replaceAll("\r\n","").replaceAll("\n",""));
                }else{
                    holder.xm_description2.setVisibility(View.GONE);
                }
                holder.xm_hide2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,xiangmu.get(1).getId(),xiangmu.get(1).getId());
                        if(xiangmu.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", xiangmu.get(1).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",xiangmu.get(1).getId());
                            intent.putExtra("name", xiangmu.get(1).getTypename());
                            intent.putExtra("pic",xiangmu.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (xiangmu.get(2).getTypename().equals("专题")) {
                    holder.xm_zt3.setVisibility(View.VISIBLE);
                    holder.xm_zan3.setVisibility(View.GONE);
                } else {
                    holder.xm_zt3.setVisibility(View.GONE);
                    holder.xm_zan3.setVisibility(View.GONE);
                }
                if(xiangmu.get(2).getLitpic().equals("")){
                    holder.xm_img3.setVisibility(View.GONE);
                }else{
                    holder.xm_img3.setVisibility(View.VISIBLE);

                    ImageLoader.getInstance().displayImage(xiangmu.get(2).getLitpic()
                            , holder.xm_img3, options);
                }
//                ImageLoader.getInstance().displayImage(xiangmu.get(2).getLitpic()
//                        , holder.xm_img3, options);
                holder.xm_title3.setText(xiangmu.get(2).getTitle());
                read_ids = PrefUtils.getString(context, xiangmu.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.xm_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.xm_title3.setTextColor(Color.parseColor("#777777"));
                }
                holder.xm_zan3.setText(xiangmu.get(2).getZan());
                holder.xm_look3.setText(xiangmu.get(2).getClick());
                if(xiangmu.get(2).getArea_cate()!=null){
                    holder.xm_linyu3.setText(xiangmu.get(2).getArea_cate().getArea_cate1());
                }
                if(xiangmu.get(2).getDescription()!=null || xiangmu.get(2).getDescription().equals("")){
                    holder.xm_description3.setVisibility(View.VISIBLE);
                    holder.xm_description3.setText(xiangmu.get(2).getDescription().replaceAll("\r\n","").replaceAll("\n",""));
                }else{
                    holder.xm_description3.setVisibility(View.GONE);
                }
                holder.xm_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,xiangmu.get(2).getId(),xiangmu.get(2).getId());
                        if(xiangmu.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", xiangmu.get(2).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",xiangmu.get(2).getId());
                            intent.putExtra("name", xiangmu.get(2).getTypename());
                            intent.putExtra("pic",xiangmu.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
            }catch (Exception e){}
        }
        if(type.equals("zhengce")) {
            zhengce = (List<Zclist>) obj;
           if(zhengce.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(zhengce.size()>2){
                holder.zc_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.zc_hide3.setVisibility(View.GONE);
            }
            if(zhengce.size()>1){
                holder.zc_hide2.setVisibility(View.VISIBLE);
            }else{
                holder.zc_hide2.setVisibility(View.GONE);
            }
            holder.type_name.setText("政策");
            holder.search_num.setText(zhengce.size()+"条");
            //holder.search_num.setText(zhengce.get(0).getTitle());

            try {
                if (zhengce.get(0).getTypename().equals("专题")) {
                    holder.zc_zt1.setVisibility(View.VISIBLE);
                    holder.zc_dianzan1.setVisibility(View.GONE);
                } else {
                    holder.zc_zt1.setVisibility(View.GONE);
                    holder.zc_dianzan1.setVisibility(View.GONE);
                }
                holder.zc_title1.setText(zhengce.get(0).getTitle());
                read_ids = PrefUtils.getString(context, zhengce.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description1.setText(zhengce.get(0).getDescription());
                holder.zc_look1.setText(zhengce.get(0).getClick());
                holder.zc_zan1.setText(zhengce.get(0).zan);
                holder.zc_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhengce.get(0).getId(),zhengce.get(0).getId());
                        if(zhengce.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhengce.get(0).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhengce.get(0).getId());
                            intent.putExtra("name", zhengce.get(0).getTypename());
                            intent.putExtra("pic",zhengce.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (zhengce.get(1).getTypename().equals("专题")) {
                    holder.zc_zt2.setVisibility(View.VISIBLE);
                    holder.zc_dianzan2.setVisibility(View.GONE);
                } else {
                    holder.zc_zt2.setVisibility(View.GONE);
                    holder.zc_dianzan2.setVisibility(View.GONE);
                }
                holder.zc_title2.setText(zhengce.get(1).getTitle());
                read_ids = PrefUtils.getString(context, zhengce.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title2.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description2.setText(zhengce.get(1).getDescription());
                holder.zc_look2.setText(zhengce.get(1).getClick());
                holder.zc_zan2.setText(zhengce.get(1).zan);
                holder.zc_hide2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhengce.get(1).getId(),zhengce.get(1).getId());
                        if(zhengce.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhengce.get(1).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhengce.get(1).getId());
                            intent.putExtra("name", zhengce.get(1).getTypename());
                            intent.putExtra("pic",zhengce.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (zhengce.get(2).getTypename().equals("专题")) {
                    holder.zc_zt3.setVisibility(View.VISIBLE);
                    holder.zc_dianzan3.setVisibility(View.GONE);
                } else {
                    holder.zc_zt3.setVisibility(View.GONE);
                    holder.zc_dianzan3.setVisibility(View.GONE);
                }
                holder.zc_title3.setText(zhengce.get(2).getTitle());
                read_ids = PrefUtils.getString(context, zhengce.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title3.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description3.setText(zhengce.get(2).getDescription());
                holder.zc_look3.setText(zhengce.get(2).getClick());
                holder.zc_zan3.setText(zhengce.get(2).zan);
                holder.zc_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhengce.get(2).getId(),zhengce.get(2).getId());
                        if(zhengce.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhengce.get(2).getId());

                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhengce.get(2).getId());
                            intent.putExtra("name", zhengce.get(2).getTypename());
                            intent.putExtra("pic",zhengce.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
            }catch (Exception e){}
        }
        if(type.equals("rencai")) {
             rencai = (List<Renlist>) obj;
            if(rencai.size()>3){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(rencai.size()>2){
                holder.search_hide3.setVisibility(View.VISIBLE);
            }else{
                holder.search_hide3.setVisibility(View.GONE);
            }
            if(rencai.size()>1){
                holder.search_rc_two.setVisibility(View.VISIBLE);
            }else{
                holder.search_rc_two.setVisibility(View.GONE);
            }
            holder.type_name.setText("专家");
            holder.search_num.setText(rencai.size()+"条");
            try {
                options = ImageLoaderUtils.initOptions();
                ImageLoader.getInstance().displayImage(rencai.get(0).getLitpic()
                        , holder.search_rencai_img1, options);
                holder.search_rencai_title1.setText(rencai.get(0).getDescription());
                holder.search_rencai_uname1.setText(rencai.get(0).getUsername());
                read_ids = PrefUtils.getString(context, rencai.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.search_rencai_uname1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_rencai_uname1.setTextColor(Color.parseColor("#777777"));
                }
                holder.search_rencai_zhicheng1.setText(rencai.get(0).getRank());
                holder.search_rencai_linyu1.setText(rencai.get(0).getArea_cate().getArea_cate1());
//                if(rencai.get(0).getSource()==null || rencai.get(0).getSource().equals("")){
//                    holder.rc_zan1.setVisibility(View.GONE);
//                }else{
//                    holder.rc_zan1.setVisibility(View.VISIBLE);
//                    holder.rc_zan1.setText(rencai.get(0).getSource());
//                }
                holder.rc_look1.setText(rencai.get(0).getClick());
                if(rencai.get(0).is_academician.equals("1")){
                    holder.rc_yuanshi1.setVisibility(View.VISIBLE);
                }else{
                    holder.rc_yuanshi1.setVisibility(View.GONE);
                }
                holder.rencai_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,rencai.get(0).getId(),rencai.get(0).getId());
                        Intent intent=new Intent(context,DetailsActivity.class);
                        intent.putExtra("id",rencai.get(0).getId());
                        intent.putExtra("name", rencai.get(0).getTypename());
                        intent.putExtra("pic",rencai.get(0).getLitpic());
                        context.startActivity(intent);
                    }
                });
                ImageLoader.getInstance().displayImage(rencai.get(1).getLitpic()
                        , holder.search_rencai_img2, options);
                holder.search_rencai_title2.setText(rencai.get(1).getDescription());
                holder.search_rencai_uname2.setText(rencai.get(1).getUsername());
                read_ids = PrefUtils.getString(context, rencai.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.search_rencai_uname2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_rencai_uname2.setTextColor(Color.parseColor("#777777"));
                }
                holder.search_rencai_zhicheng2.setText(rencai.get(1).getRank());
                holder.search_rencai_linyu2.setText(rencai.get(1).getArea_cate().getArea_cate1());
//                if(rencai.get(1).getSource()==null || rencai.get(1).getSource().equals("")){
//                    holder.rc_zan2.setVisibility(View.GONE);
//                }else{
//                    holder.rc_zan2.setVisibility(View.VISIBLE);
//                    holder.rc_zan2.setText(rencai.get(1).getSource());
//                }

                holder.rc_look2.setText(rencai.get(1).getClick());
                if(rencai.get(1).is_academician.equals("1")){
                    holder.rc_yuanshi2.setVisibility(View.VISIBLE);
                }else{
                    holder.rc_yuanshi2.setVisibility(View.GONE);
                }
                holder.search_rc_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,rencai.get(1).getId(),rencai.get(1).getId());
                        Intent intent=new Intent(context,DetailsActivity.class);
                        intent.putExtra("id",rencai.get(1).getId());
                        intent.putExtra("name", rencai.get(1).getTypename());
                        intent.putExtra("pic",rencai.get(1).getLitpic());
                        context.startActivity(intent);
                    }
                });
                ImageLoader.getInstance().displayImage(rencai.get(2).getLitpic()
                        , holder.search_rencai_img3, options);
                holder.search_rencai_title3.setText(rencai.get(2).getDescription());
                holder.search_rencai_uname3.setText(rencai.get(2).getUsername());
                read_ids = PrefUtils.getString(context, rencai.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.search_rencai_uname3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_rencai_uname3.setTextColor(Color.parseColor("#777777"));
                }
                holder.search_rencai_zhicheng3.setText(rencai.get(2).getRank());
                holder.search_rencai_linyu3.setText(rencai.get(2).getArea_cate().getArea_cate1());
//                if(rencai.get(2).getSource()==null || rencai.get(2).getSource().equals("")){
//                    holder.rc_zan3.setVisibility(View.GONE);
//                }else{
//                    holder.rc_zan3.setVisibility(View.VISIBLE);
//                    holder.rc_zan3.setText(rencai.get(2).getSource());
//                }

                holder.rc_look3.setText(rencai.get(2).getClick());
                if(rencai.get(2).is_academician.equals("1")){
                    holder.rc_yuanshi3.setVisibility(View.VISIBLE);
                }else{
                    holder.rc_yuanshi3.setVisibility(View.GONE);
                }
                holder.search_hide3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,rencai.get(2).getId(),rencai.get(2).getId());
                        Intent intent=new Intent(context,DetailsActivity.class);
                        intent.putExtra("id",rencai.get(2).getId());
                        intent.putExtra("name", rencai.get(2).getTypename());
                        intent.putExtra("pic",rencai.get(2).getLitpic());
                        context.startActivity(intent);
                    }
                });
            }catch (Exception e){}
        }
        if(type.equals("zhuanli")) {
            zhuanli = (List<Zllist>) obj;

           if(zhuanli.size()>4){
                holder.search_moreid.setVisibility(View.VISIBLE);
            }else{
                holder.search_moreid.setVisibility(View.GONE);
            }
            if(zhuanli.size()>3){
                holder.search_zhuanli_four.setVisibility(View.VISIBLE);
            }else{
                holder.search_zhuanli_four.setVisibility(View.GONE);
            }
            if(zhuanli.size()>2){
                holder.search_zhuanli_three.setVisibility(View.VISIBLE);
            }else{
                holder.search_zhuanli_three.setVisibility(View.GONE);
            }
            if(zhuanli.size()>1){
                holder.search_zhuanli_two.setVisibility(View.VISIBLE);
            }else{
                holder.search_zhuanli_two.setVisibility(View.GONE);
            }
            holder.type_name.setText("专利");
            holder.search_num.setText(zhuanli.size()+"条");
            try {
                if (zhuanli.get(0).getTypename().equals("专题")) {
                    holder.zhuanli_zt1.setVisibility(View.VISIBLE);
//                    holder.zhuanli_dianzan1.setVisibility(View.GONE);
                } else {
                    holder.zhuanli_zt1.setVisibility(View.GONE);
//                    holder.zhuanli_dianzan1.setVisibility(View.GONE);
                }
                holder.search_zhunli_title1.setText(zhuanli.get(0).getTitle());
                read_ids = PrefUtils.getString(context, zhuanli.get(0).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#777777"));
                }
                if(zhuanli.get(0).getArea_cate()!=null){
                    holder.zhuanli_linyu1.setText(zhuanli.get(0).getArea_cate().getArea_cate1());
                }
                if(zhuanli.get(0).getDescription()!=null || !zhuanli.get(0).getDescription().equals("")){
                    holder.zhuanli_description1.setVisibility(View.VISIBLE);
                    holder.zhuanli_description1.setText(zhuanli.get(0).getDescription());
                }else {
                    holder.zhuanli_description1.setVisibility(View.GONE);
                }
                holder.zhuanli_look1.setText(zhuanli.get(0).getClick());
                holder.zhuanli_zan1.setText(zhuanli.get(0).zan);
                holder.zhuanli_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhuanli.get(0).getId(),zhuanli.get(0).getId());
                        if(zhuanli.get(0).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhuanli.get(0).getId());
                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhuanli.get(0).getId());
                            intent.putExtra("name", zhuanli.get(0).getTypename());
                            intent.putExtra("pic",zhuanli.get(0).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (zhuanli.get(1).getTypename().equals("专题")) {
                    holder.zhuanli_zt2.setVisibility(View.VISIBLE);
//                    holder.zhuanli_dianzan2.setVisibility(View.GONE);
                } else {
                    holder.zhuanli_zt2.setVisibility(View.GONE);
//                    holder.zhuanli_dianzan2.setVisibility(View.GONE);
                }
                holder.search_zhunli_title2.setText(zhuanli.get(1).getTitle());
                read_ids = PrefUtils.getString(context, zhuanli.get(1).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title2.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title2.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_look2.setText(zhuanli.get(1).getClick());
                holder.zhuanli_zan2.setText(zhuanli.get(1).zan);
                if(zhuanli.get(1).getArea_cate()!=null){
                    holder.zhuanli_linyu2.setText(zhuanli.get(1).getArea_cate().getArea_cate1());
                }
                if(zhuanli.get(1).getDescription()!=null || !zhuanli.get(1).getDescription().equals("")){
                    holder.zhuanli_description2.setVisibility(View.VISIBLE);
                    holder.zhuanli_description2.setText(zhuanli.get(1).getDescription());
                }else if(zhuanli.get(1).getDescription().equals("")){
                    holder.zhuanli_description2.setVisibility(View.GONE);
                }
                holder.search_zhuanli_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhuanli.get(1).getId(),zhuanli.get(1).getId());
                        if(zhuanli.get(1).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhuanli.get(1).getId());
                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhuanli.get(1).getId());
                            intent.putExtra("name", zhuanli.get(1).getTypename());
                            intent.putExtra("pic",zhuanli.get(1).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (zhuanli.get(2).getTypename().equals("专题")) {
                    holder.zhuanli_zt3.setVisibility(View.VISIBLE);
//                    holder.zhuanli_dianzan3.setVisibility(View.GONE);
                } else {
                    holder.zhuanli_zt3.setVisibility(View.GONE);
//                    holder.zhuanli_dianzan3.setVisibility(View.GONE);
                }
                holder.search_zhunli_title3.setText(zhuanli.get(2).getTitle());
                read_ids = PrefUtils.getString(context, zhuanli.get(2).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title3.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title3.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_look3.setText(zhuanli.get(2).getClick());
                holder.zhuanli_zan3.setText(zhuanli.get(2).zan);
                if(zhuanli.get(2).getArea_cate()!=null){
                    holder.zhuanli_linyu3.setText(zhuanli.get(2).getArea_cate().getArea_cate1());
                }
                if(zhuanli.get(2).getDescription()!=null || !zhuanli.get(2).getDescription().equals("")){
                    holder.zhuanli_description3.setVisibility(View.VISIBLE);
                    holder.zhuanli_description3.setText(zhuanli.get(2).getDescription());
                }else {
                    holder.zhuanli_description3.setVisibility(View.GONE);
                }
                holder.search_zhuanli_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhuanli.get(2).getId(),zhuanli.get(2).getId());
                        if(zhuanli.get(2).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhuanli.get(2).getId());
                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhuanli.get(2).getId());
                            intent.putExtra("name", zhuanli.get(2).getTypename());
                            intent.putExtra("pic",zhuanli.get(2).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
                if (zhuanli.get(3).getTypename().equals("专题")) {
                    holder.zhuanli_zt4.setVisibility(View.VISIBLE);
//                    holder.zhuanli_dianzan4.setVisibility(View.GONE);
                } else {
                    holder.zhuanli_zt4.setVisibility(View.GONE);
//                    holder.zhuanli_dianzan4.setVisibility(View.GONE);
                }
                holder.search_zhunli_title4.setText(zhuanli.get(3).getTitle());
                read_ids = PrefUtils.getString(context, zhuanli.get(3).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title4.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title4.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_look4.setText(zhuanli.get(3).getClick());
                holder.zhuanli_zan4.setText(zhuanli.get(3).zan);
                if(zhuanli.get(3).getArea_cate()!=null){
                    holder.zhuanli_linyu4.setText(zhuanli.get(3).getArea_cate().getArea_cate1());
                }
                if(zhuanli.get(3).getDescription()!=null && !zhuanli.get(3).getDescription().equals("")){
                    holder.zhuanli_description4.setText(zhuanli.get(3).getDescription());
                }else {
                    holder.zhuanli_description4.setVisibility(View.GONE);
                }
                holder.search_zhuanli_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.setString(context,zhuanli.get(3).getId(),zhuanli.get(3).getId());
                        if(zhuanli.get(3).getTypename().equals("专题")){
                            Intent intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", zhuanli.get(3).getId());
                            context.startActivity(intent);
                        }else{
                            Intent intent=new Intent(context,DetailsActivity.class);
                            intent.putExtra("id",zhuanli.get(3).getId());
                            intent.putExtra("name", zhuanli.get(3).getTypename());
                            intent.putExtra("pic",zhuanli.get(3).getLitpic());
                            context.startActivity(intent);
                        }

                    }
                });
            }catch (Exception e){}
        }
//        if(type.equals("zhuanti")) {
//            List<Ztlist> zhuanti = (List<Ztlist>) obj;
//            if(zhuanti.size()>4){
//                holder.search_moreid.setVisibility(View.VISIBLE);
//            }else{
//                holder.search_moreid.setVisibility(View.GONE);
//            }
//            if(zhuanti.size()>3){
//                holder.search_zhuanli_four.setVisibility(View.VISIBLE);
//            }else{
//                holder.search_zhuanli_four.setVisibility(View.GONE);
//            }
//            if(zhuanti.size()>2){
//                holder.search_zhuanli_three.setVisibility(View.VISIBLE);
//            }else{
//                holder.search_zhuanli_three.setVisibility(View.GONE);
//            }
//            if(zhuanti.size()>1){
//                holder.search_zhuanli_two.setVisibility(View.VISIBLE);
//            }else{
//                holder.search_zhuanli_two.setVisibility(View.GONE);
//            }
//            holder.type_name.setText("专题");
//            holder.search_num.setText(zhuanti.size()+"条");
//            try {
//                holder.search_zhunli_title1.setText(zhuanti.get(0).getTitle());
//                holder.search_zhunli_title2.setText(zhuanti.get(1).getTitle());
//                holder.search_zhunli_title3.setText(zhuanti.get(2).getTitle());
//                holder.search_zhunli_title4.setText(zhuanti.get(3).getTitle());
//            }catch (Exception e){}
//        }
       holder.search_many_more.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                    Intent intent=new Intent(context,ManymoreActivity.class);
                           Bundle b=new Bundle();
                           b.putSerializable("dataList", (Serializable) dataList);
                           b.putSerializable("typeList", (Serializable) typeList);
                          // b.putSerializable("zxlist", (Serializable) zxList);
                          // b.putSerializable("zhengce", (Serializable) zhengce);
                           intent.putExtras(b);
                           intent.putExtra("possition",position);
                           intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                           context.startActivity(intent);
                       }
                   });

        return convertView;
    }

    class ViewHolder{
        TextView sc_text_title;
        LinearLayout zixun_biao;
        TextView type_name;
        TextView search_num;
        ImageView search_img1;
        TextView search_text_title1;
        ImageView search_img2;
        TextView search_text_title2;
        ImageView search_img3;
        TextView search_text_title3;
        TextView search_more;
        LinearLayout search_moreid;
        LinearLayout search_hide3;
        RoundImageView search_rencai_img1;
        TextView search_rencai_uname1;
        TextView search_rencai_zhicheng1;
        TextView search_rencai_linyu1;
        TextView search_rencai_title1;
        RoundImageView search_rencai_img2;
        TextView search_rencai_uname2;
        TextView search_rencai_zhicheng2;
        TextView search_rencai_linyu2;
        TextView search_rencai_title2;
        RoundImageView search_rencai_img3;
        TextView search_rencai_uname3;
        TextView search_rencai_zhicheng3;
        TextView search_rencai_linyu3;
        TextView search_rencai_title3;
        LinearLayout search_rc_two;
        LinearLayout search_hide2;
        LinearLayout search_fin;
        LinearLayout search_zhuanli_two;
        LinearLayout search_zhuanli_three;
        LinearLayout search_zhuanli_four;
        TextView search_zhunli_title1;
        TextView search_zhunli_title2;
        TextView search_zhunli_title3;
        TextView search_zhunli_title4;
        LinearLayout search_many_more;
        LinearLayout zixun_item1;
        LinearLayout rencai_item1;
        LinearLayout zhuanli_item1;
        TextView zx_description1;
        TextView zx_description2;
        TextView zx_description3;
        //项目
        LinearLayout xm_biao;
        ImageView xm_img1;
        ImageView xm_img2;
        ImageView xm_img3;
        TextView xm_title1;
        TextView xm_title2;
        TextView xm_title3;
        TextView xm_description1;
        TextView xm_description2;
        TextView xm_description3;
        TextView xm_linyu1;
        TextView xm_linyu2;
        TextView xm_linyu3;
        LinearLayout xm_hide3;
        LinearLayout xm_hide2;
        LinearLayout xm_item1;
        TextView xm_zan1;
        TextView xm_zan2;
        TextView xm_zan3;
        TextView xm_look1;
        TextView xm_look2;
        TextView xm_look3;
        ImageView xm_zt1;
        ImageView xm_zt2;
        ImageView xm_zt3;
        //政策
        LinearLayout zc_biao;
        TextView zc_title1;
        TextView zc_title2;
        TextView zc_title3;
        TextView zc_description1;
        TextView zc_description2;
        TextView zc_description3;
        LinearLayout zc_hide3;
        LinearLayout zc_hide2;
        LinearLayout zc_item1;
        TextView zc_unit1;
        TextView zc_unit2;
        TextView zc_unit3;
        TextView zc_look1;
        TextView zc_look2;
        TextView zc_look3;
        TextView zc_zan1;
        TextView zc_zan2;
        TextView zc_zan3;
        ImageView zc_zt1;
        ImageView zc_zt2;
        ImageView zc_zt3;
        LinearLayout zc_dianzan1;
        LinearLayout zc_dianzan2;
        LinearLayout zc_dianzan3;
        //设备
        LinearLayout device_biao;
        LinearLayout device_hide3;
        LinearLayout device_hide2;
        LinearLayout device_item1;
        TextView shebei_title1;
        TextView shebei_title2;
        TextView shebei_title3;
        TextView shebei_description1;
        TextView shebei_description2;
        TextView shebei_description3;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        TextView shebei_linyu1;
        TextView shebei_linyu2;
        TextView shebei_linyu3;
        TextView shebei_zan1;
        TextView shebei_zan2;
        TextView shebei_zan3;
        TextView shebei_look1;
        TextView shebei_look2;
        TextView shebei_look3;
        ImageView device_zt1;
        ImageView device_zt2;
        ImageView device_zt3;
        LinearLayout device_dianzan1;
        LinearLayout device_dianzan2;
        LinearLayout device_dianzan3;


        //资讯
        TextView zx_lanyuan1;
        TextView zx_lanyuan2;
        TextView zx_lanyuan3;
        TextView zx_update1;
        TextView zx_update2;
        TextView zx_update3;
        TextView zx_zan1;
        TextView zx_zan2;
        TextView zx_zan3;
        TextView zx_look1;
        TextView zx_look2;
        TextView zx_look3;
        ImageView zxs_zt1;
        ImageView zxs_zt2;
        ImageView zxs_zt3;
        LinearLayout zx_dianzan1;
        LinearLayout zx_dianzan2;
        LinearLayout zx_dianzan3;
        //人才
        TextView rc_zan1;
        TextView rc_zan2;
        TextView rc_zan3;
        TextView rc_look1;
        TextView rc_look2;
        TextView rc_look3;
        ImageView rc_yuanshi1;
        ImageView rc_yuanshi2;
        ImageView rc_yuanshi3;
        //z专利
        TextView zhuanli_linyu1;
        TextView zhuanli_linyu2;
        TextView zhuanli_linyu3;
        TextView zhuanli_linyu4;
        TextView zhuanli_look1;
        TextView zhuanli_look2;
        TextView zhuanli_look3;
        TextView zhuanli_look4;
        TextView zhuanli_zan1;
        TextView zhuanli_zan2;
        TextView zhuanli_zan3;
        TextView zhuanli_zan4;
        ImageView zhuanli_zt1;
        ImageView zhuanli_zt2;
        ImageView zhuanli_zt3;
        ImageView zhuanli_zt4;
        TextView zhuanli_description1;
        TextView zhuanli_description2;
        TextView zhuanli_description3;
        TextView zhuanli_description4;

        //实验室
        LinearLayout librarys;
        TextView librarys_title1;
        TextView librarys_title2;
        TextView librarys_title3;
        TextView librarys_linyu1;
        TextView librarys_linyu2;
        TextView librarys_linyu3;
        ImageView sys_zt1;
        ImageView sys_zt2;
        ImageView sys_zt3;
        TextView sys_description1;
        TextView sys_description2;
        TextView sys_description3;

        ImageView librarys_img1;
        ImageView librarys_img2;
        ImageView librarys_img3;
        TextView librarys_zan1;
        TextView librarys_zan2;
        TextView librarys_zan3;
        TextView librarys_look1;
        TextView librarys_look2;
        TextView librarys_look3;

        LinearLayout library_hide3;
        LinearLayout library_hide2;
        LinearLayout library_item1;
        LinearLayout library_biao;



    }
}
