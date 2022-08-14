package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import entity.Ztlist;
import entity.Zxlist;
import view.RoundImageView;

/**
 * Created by 13520 on 2016/11/8.
 */

public class SearchmoreAdapter extends BaseAdapter {
    private Context context;
    List<String> typeList;
    List<Object> dataList;
    private String type;
    List<Zxlist> zxList;
    List<Sblist> shebei;
    List<Xmlist> xiangmu;
    List<Zclist> zhengce;
    List<Renlist> rencai;
    List<Zllist> zhuanli;
    List<Ztlist> zhuanti;
    List<Tuijian> tuijian;
    List<Shiyanshi> shiyanshi;
    private DisplayImageOptions options;
    int size;
    int position;
    private String read_ids;

    public  SearchmoreAdapter(){}
    public  SearchmoreAdapter(Context context, List<Zxlist> zxList, List<Shiyanshi> shiyanshi, List<Sblist> shebei,List<Xmlist> xiangmu,List<Zclist> zhengce,List<Renlist> rencai, List<Zllist> zhuanli, List<Tuijian> tuijian, List<String> typeList,int position){
        this.context=context;
        this.zxList=zxList;
        this.shiyanshi=shiyanshi;
        this.shebei=shebei;
        this.xiangmu=xiangmu;
        this.zhengce=zhengce;
        this.rencai=rencai;
        this.zhuanli=zhuanli;
        this.zhuanti=zhuanti;
        this.typeList=typeList;
        this.tuijian=tuijian;
        this.position=position;
    }
    @Override
    public int getCount() {
        type = typeList.get(position);
        if(type.equals("tuijian")){
            size = tuijian.size();
        }
        if(type.equals("zixun")){
             size = zxList.size();
        }
        if(type.equals("shiyanshi")){
            size = shiyanshi.size();
        }
        if(type.equals("zhengce")){
            size = zhengce.size();
        }
        if(type.equals("shebei")){
            size = shebei.size();
        }
        if(type.equals("xiangmu")){
            size = xiangmu.size();
        }
        if(type.equals("rencai")){
            size = rencai.size();
        }
        if(type.equals("zhuanli")){
            size = zhuanli.size();
        }
//        if(type.equals("zhuanti")){
//            size = zhuanti.size();
//        }
        return size;
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
        ViewHolder holder=null;
        convertView=null;
    if(convertView==null){
        holder=new ViewHolder();
        if(type.equals("zixun") || type.equals("tuijian")){

            convertView=View.inflate(context, R.layout.listview_more_item,null);
            holder.search_img1=(ImageView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_img);
            holder.search_text_title1=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_title);
//            holder.zx_description=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_description);
            holder.zx_lanyuan=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_lanyuan);
            holder.zx_zan=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_zan);
            holder.zx_look=(TextView) convertView.findViewById(R.id.search_position1).findViewById(R.id.zx_look);

        }
        if(type.equals("shiyanshi")){
            convertView=View.inflate(context, R.layout.library_more,null);

            holder.librarys_title1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_title);
            holder.librarys_img1=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_img);
            holder.librarys_zan1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_dianwei);
            holder.librarys_look1=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_look);
            holder.librarys_linyu=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_linyu);
            holder.librarys_des=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_description);

        }
        if(type.equals("zhengce")){

            convertView=View.inflate(context, R.layout.search_zc_more,null);
            holder.zc_title=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_title);
            holder.zc_description=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_description);
            holder.zc_unit=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_unit);
            holder.zc_look=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_look);
            holder.zc_zan=(TextView) convertView.findViewById(R.id.search_zc1).findViewById(R.id.zc_zan);

        }
        if(type.equals("xiangmu")){

            convertView=View.inflate(context, R.layout.search_xm_more,null);
            holder.xm_img=(ImageView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_img);
            holder.xm_title=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_title);
            holder.xm_linyu=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_linyu);
            holder.xm_zan=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_dianwei);
            holder.xm_look=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_look);
            holder.xm_description=(TextView) convertView.findViewById(R.id.search_xm1).findViewById(R.id.xm_description);

        }
        if(type.equals("shebei")){
            convertView=View.inflate(context, R.layout.search_device_more,null);
            holder.shebei_title=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_title);
            holder.shebei_description=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_description);
            holder.shebei_linyu=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_linyu);
            holder.shebei_zan=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_zan);
            holder.shebei_look=(TextView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_look);
            holder.imageView5=(ImageView) convertView.findViewById(R.id.device_position1).findViewById(R.id.device_img);
        }
        if(type.equals("rencai")){

            convertView=View.inflate(context, R.layout.search_rencai_more,null);
            holder.search_rencai_img1=(RoundImageView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_img);
            holder.search_rencai_uname1=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_uname);
            holder.search_rencai_zhicheng1=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_zhicheng);
            holder.search_rencai_linyu1=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_linyu);
            holder.search_rencai_title1=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_title);
            holder.rc_zan=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_zan);
            holder.rc_look=(TextView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_look);
            holder.rc_yuanshi=(ImageView) convertView.findViewById(R.id.search_rencai).findViewById(R.id.rc_yuanshi);

        }
        if(type.equals("zhuanli")){

            convertView=View.inflate(context, R.layout.search_zhuanli_more,null);
            holder.search_zhunli_title1=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhunli_title);
            holder.zhuanli_linyu=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_linyu);
//            holder.zhuanli_state=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_state);
            holder.zhuanli_look=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_look);
            holder.zhuanli_zan=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_zan);
            holder.zhuanli_description=(TextView) convertView.findViewById(R.id.search_zhuanli1).findViewById(R.id.zhuanli_description);

        }
        convertView.setTag(holder);

    }
    else{
            holder= (ViewHolder) convertView.getTag();
    }
        if(type.equals("zixun")){
            try {
                options = ImageLoaderUtils.initOptions();
                if(zxList.get(position).getLitpic().equals("")){
                    holder.search_img1.setVisibility(View.GONE);
                }else{
                    holder.search_img1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(zxList.get(position).getLitpic()
                            , holder.search_img1, options);
                }
                holder.search_text_title1.setText(zxList.get(position).getTitle());
                read_ids = PrefUtils.getString(context, zxList.get(position).getId(), "");
                if(read_ids.equals("")){
                    holder.search_text_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_text_title1.setTextColor(Color.parseColor("#777777"));
                }
//                holder.zx_description.setText(zxList.get(position).getDescription());
                holder.zx_zan.setText(zxList.get(position).getZan());
                holder.zx_look.setText(zxList.get(position).getClick());
            }catch (Exception e){}


        }
        if(type.equals("tuijian")){
            try {
                options = ImageLoaderUtils.initOptions();
                if(tuijian.get(position).getLitpic().equals("")){
                    holder.search_img1.setVisibility(View.GONE);
                }else{
                    holder.search_img1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(tuijian.get(position).getLitpic()
                            , holder.search_img1, options);
                }
                holder.search_text_title1.setText(tuijian.get(position).getTitle());
                read_ids = PrefUtils.getString(context, tuijian.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.search_text_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_text_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.zx_description.setText(tuijian.get(position).getDescription());
                holder.zx_zan.setText(tuijian.get(position).getZan());
                holder.zx_look.setText(tuijian.get(position).getClick());
            }catch (Exception e){}


        }
        if(type.equals("shiyanshi")){
            try {
                options = ImageLoaderUtils.initOptions();
                if(shiyanshi.get(position).getLitpic().equals("")){
                    holder.librarys_img1.setVisibility(View.GONE);
                }else{
                    holder.librarys_img1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(shiyanshi.get(position).getLitpic()
                            , holder.librarys_img1, options);
                }

                holder.librarys_title1.setText(shiyanshi.get(position).getTitle());
                read_ids = PrefUtils.getString(context, shiyanshi.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.librarys_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.librarys_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.librarys_zan1.setText(shiyanshi.get(position).getZan());
                holder.librarys_look1.setText(shiyanshi.get(position).getClick());
                if(shiyanshi.get(position).getArea_cate()!=null){
                    holder.librarys_linyu.setText(shiyanshi.get(position).getArea_cate().getArea_cate1());
                }
                if(shiyanshi.get(position).getDescription()!=null){
                    holder.librarys_des.setText(shiyanshi.get(position).getDescription());
                }else{
                    holder.librarys_des.setVisibility(View.GONE);
                }

            }catch (Exception e){}

        }
        if(type.equals("zhengce")){
            try {
                holder.zc_title.setText(zhengce.get(position).getTitle());
                read_ids = PrefUtils.getString(context, zhengce.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.zc_title.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.zc_title.setTextColor(Color.parseColor("#777777"));
                }
                holder.zc_description.setText(zhengce.get(position).getDescription());
                holder.zc_look.setText(zhengce.get(position).getClick());
                holder.zc_zan.setText(zhengce.get(position).zan);
            }catch (Exception e){}

        }
        if(type.equals("shebei")){
          options = ImageLoaderUtils.initOptions();
        try {
            if(shebei.get(position).getLitpic().equals("")){
                holder.imageView5.setVisibility(View.GONE);
            }else{
                holder.imageView5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(shebei.get(position).getLitpic()
                        , holder.imageView5, options);
            }
            holder.shebei_title.setText(shebei.get(position).getTitle());
            read_ids = PrefUtils.getString(context, shebei.get(position).getId(), "");
            if(read_ids.equals("")){

                holder.shebei_title.setTextColor(Color.parseColor("#181818"));
            }else{
                holder.shebei_title.setTextColor(Color.parseColor("#777777"));
            }
            holder.shebei_description.setText(shebei.get(position).getDescription());
            if(shebei.get(position).getArea_cate()!=null){
                holder.shebei_linyu.setText(shebei.get(position).getArea_cate().getArea_cate1());
            }
            holder.shebei_zan.setText(shebei.get(position).getZan());
            holder.shebei_look.setText(shebei.get(position).getClick());
          }catch (Exception e){}

        }
        if(type.equals("xiangmu")){
       try {
            options = ImageLoaderUtils.initOptions();
            if(xiangmu.get(position).getLitpic().equals("")|| xiangmu.get(position).getLitpic()==null){
                holder.xm_img.setVisibility(View.GONE);
            }else{
                holder.xm_img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(xiangmu.get(position).getLitpic()
                        , holder.xm_img, options);
            }

            holder.xm_title.setText(xiangmu.get(position).getTitle());
           if(xiangmu.get(position).getArea_cate()!=null){
               holder.xm_linyu.setText(xiangmu.get(position).getArea_cate().getArea_cate1());
           }
           if(xiangmu.get(position).getDescription()==null || xiangmu.get(position).getDescription().equals("")){
               holder.xm_description.setVisibility(View.GONE);
           }else{
               holder.xm_description.setVisibility(View.VISIBLE);
               holder.xm_description.setText(xiangmu.get(position).getDescription().replaceAll("\r\n","").replaceAll("\n",""));
           }

           read_ids = PrefUtils.getString(context, xiangmu.get(position).getId(), "");
           if(read_ids.equals("")){

               holder.xm_title.setTextColor(Color.parseColor("#181818"));
           }else{
               holder.xm_title.setTextColor(Color.parseColor("#777777"));
           }
            holder.xm_zan.setText(xiangmu.get(position).getZan());
            holder.xm_look.setText(xiangmu.get(position).getClick());
}catch (Exception e){}
        }
        if(type.equals("rencai")){
            try {
                options = ImageLoaderUtils.initOptions();
                ImageLoader.getInstance().displayImage(rencai.get(position).getLitpic()
                        , holder.search_rencai_img1, options);
                holder.search_rencai_title1.setText(rencai.get(position).getDescription());
                holder.search_rencai_uname1.setText(rencai.get(position).getUsername());
                read_ids = PrefUtils.getString(context, rencai.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.search_rencai_uname1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_rencai_uname1.setTextColor(Color.parseColor("#777777"));
                }
                holder.search_rencai_zhicheng1.setText(rencai.get(position).getRank());
                holder.search_rencai_linyu1.setText(rencai.get(position).getArea_cate().getArea_cate1());
//                if(rencai.get(position).getSource()==null || rencai.get(position).getSource().equals("")){
//                    holder.rc_zan.setVisibility(View.GONE);
//                }else{
//                    holder.rc_zan.setVisibility(View.VISIBLE);
//                    holder.rc_zan.setText(rencai.get(position).getSource());
//                }
//                holder.rc_zan.setText(rencai.get(position).getZan());
                holder.rc_look.setText(rencai.get(position).getClick());
                if(rencai.get(position).is_academician.equals("1")){
                    holder.rc_yuanshi.setVisibility(View.VISIBLE);
                }else{
                    holder.rc_yuanshi.setVisibility(View.GONE);
                }
            }catch (Exception e){}


        }
        if(type.equals("zhuanli")){
            try {
                holder.search_zhunli_title1.setText(zhuanli.get(position).getTitle());
                read_ids = PrefUtils.getString(context, zhuanli.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#777777"));
                }
                holder.zhuanli_look.setText(zhuanli.get(position).getClick());
                holder.zhuanli_zan.setText(zhuanli.get(position).zan);
                if(zhuanli.get(position).getDescription()!=null){
                    holder.zhuanli_description.setText(zhuanli.get(position).getDescription());
                }
                if(zhuanli.get(position).getArea_cate().getArea_cate1()!=null){
                    holder.zhuanli_linyu.setText(zhuanli.get(position).getArea_cate().getArea_cate1());
                }
            }catch (Exception e){}

        }
        if(type.equals("zhuanti")){
            try {
                holder.search_zhunli_title1.setText(zhuanti.get(position).getTitle());
                read_ids = PrefUtils.getString(context, zhuanti.get(position).getId(), "");
                if(read_ids.equals("")){

                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#181818"));
                }else{
                    holder.search_zhunli_title1.setTextColor(Color.parseColor("#777777"));
                }
            }catch ( Exception e){}

        }
        return convertView;
    }
    class ViewHolder{
        ImageView search_img1;
        TextView search_text_title1;
        RoundImageView search_rencai_img1;
        TextView search_rencai_uname1;
        TextView search_rencai_zhicheng1;
        TextView search_rencai_linyu1;
        TextView search_rencai_title1;
        TextView search_zhunli_title1;

        //人才
        TextView rc_zan;
        TextView rc_look;
        ImageView rc_yuanshi;

       //专利
        TextView zhuanli_linyu;
        TextView zhuanli_state;
        TextView zhuanli_look;
        TextView zhuanli_zan;
        TextView zhuanli_description;


        TextView librarys_title1;
        TextView librarys_linyu1;
        ImageView librarys_img1;
        TextView librarys_zan1;
        TextView librarys_look1;
        TextView librarys_linyu;
        TextView librarys_des;
        //资讯
        TextView zx_description;
        TextView zx_lanyuan;
        TextView zx_zan;
        TextView zx_look;
        //政策
        TextView zc_title;
        TextView zc_description;
        TextView zc_unit;
        TextView zc_look;
        TextView zc_zan;
        //项目
        ImageView xm_img;
        TextView xm_title;
        TextView xm_linyu;
        TextView xm_zan;
        TextView xm_look;
        TextView xm_description;

        //设备
        ImageView imageView5;
        TextView shebei_title;
        TextView shebei_description;
        TextView shebei_linyu;
        TextView shebei_zan;
        TextView shebei_look;



    }
}
