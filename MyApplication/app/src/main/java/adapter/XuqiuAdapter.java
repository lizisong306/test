package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.TimeUtils;
import application.ImageLoaderUtils;
import entity.Posts;
import entity.XuQiudetailsdata;
import view.RoundImageView;

/**
 * Created by lizisong on 2017/6/30.
 */

public class XuqiuAdapter extends BaseAdapter {
    private Context context;
    private List<XuQiudetailsdata> postsListData;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public XuqiuAdapter(Context context, List<XuQiudetailsdata> Data){
           this.context = context;
           this.postsListData = Data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.xuqiudetailsadapter, null);
            holder.senddata = (TextView)convertView.findViewById(R.id.senddata);
            holder.xuqiutype = (TextView)convertView.findViewById(R.id.xuqiutype);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.rc_img     = (RoundImageView)convertView.findViewById(R.id.rc_img);
            holder.xm_img  =(ImageView)convertView.findViewById(R.id.xm_img);
            holder.xm_title = (TextView)convertView.findViewById(R.id.xm_title);
            holder.xm_rank  = (TextView)convertView.findViewById(R.id.xm_rank);
            holder.xm_linyu = (TextView)convertView.findViewById(R.id.xm_linyu);
            holder.showdian = (ImageView)convertView.findViewById(R.id.showdian);
            holder.zhuangtai = (TextView)convertView.findViewById(R.id.zhuangtai);
            holder.line1 = (TextView)convertView.findViewById(R.id.line1);
            holder.show = (RelativeLayout)convertView.findViewById(R.id.show);
            holder.xm_description = (TextView)convertView.findViewById(R.id.xm_description);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        XuQiudetailsdata item = postsListData.get(position);
        if(item.typeid.equals("4")){
            holder.rc_img.setVisibility(View.VISIBLE);
            holder.xm_img.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(item.litpic
                    , holder.rc_img, options);
            holder.show.setBackgroundColor(0xffd8f4ed);
        }else {
            holder.rc_img.setVisibility(View.GONE);
            holder.xm_img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.litpic
                    , holder.xm_img, options);
        }



        holder.zhuangtai.setText(item.str);
        holder.xm_title.setText(item.title);
        holder.xm_rank.setText(item.ranks);
        holder.xm_linyu.setText(item.area_cate);

      /*  if(item.state == 12){
            holder.zhuangtai.setTextColor(0xffffbd32);
            holder.showdian.setBackgroundResource(R.mipmap.chenggong);
        }else if(item.state==13){
            holder.zhuangtai.setTextColor(0xff696969);
            holder.showdian.setBackgroundResource(R.mipmap.shibai);

        }else {
            holder.zhuangtai.setTextColor(0xff3385ff);
            holder.showdian.setBackgroundResource(R.mipmap.liucheng1);
        }else if(item.state == 2){
            holder.zhuangtai.setTextColor(0xffffbd32);
            holder.showdian.setBackgroundResource(R.mipmap.chenggong);
        }else if(item.state == 3){
            holder.zhuangtai.setTextColor(0xff696969);
            holder.showdian.setBackgroundResource(R.mipmap.shibai);
        }else if(item.state == 4){
            holder.zhuangtai.setTextColor(0xff696969);
            holder.showdian.setBackgroundResource(R.mipmap.shibai);
        }else if(item.state == 5){
            holder.zhuangtai.setTextColor(0xff3385ff);
            holder.showdian.setBackgroundResource(R.mipmap.liucheng1);
        }*/
        holder.description.setText(item.content);
//        if(item.is_click.equals("yes") && (item.state != 12  || item.state != 13)){
//            holder.showdian.setVisibility(View.VISIBLE);
//            holder.showdian.setBackgroundResource(R.mipmap.tab_my_message);
//            holder.zhuangtai.setTextColor(0xff3385ff);
//        }else{
//            holder.showdian.setVisibility(View.VISIBLE);
//
//        }
        if(item.typename == null || item.typename.equals("")){
            holder.xuqiutype.setText("需求类型:不限");
        }else{
            holder.xuqiutype.setText("需求类型:"+item.typename);
        }
        if(item.pubdate != null){
            if(item.pubdate != null){
               holder.senddata.setText("发布时间:"+ TimeUtils.getStrXieXIANTime(item.pubdate));
            }
        }
        if(item.typeid.equals("0")){
            holder.show.setVisibility(View.GONE);
            holder.line1.setVisibility(View.VISIBLE);
        }else{
            holder.show.setVisibility(View.VISIBLE);
            holder.line1.setVisibility(View.VISIBLE);
        }

        if(item.is_click.equals("0")){
            holder.showdian.setVisibility(View.GONE);
            holder.showdian.setBackgroundResource(R.mipmap.dian1);
            if(item.typeid.equals("7")){
                holder.zhuangtai.setTextColor(0xfff2a17e);

            }else if(item.typeid.equals("2")){
                holder.zhuangtai.setTextColor(0xff8d9ac5);

            }else if(item.typeid.equals("4")){
                holder.zhuangtai.setTextColor(0xff5cc2b0);

            }else if(item.typeid.equals("0")){
                holder.zhuangtai.setTextColor(0xff3385ff);

            }


        } else if(item.is_click.equals("1")){
            holder.showdian.setVisibility(View.VISIBLE);
            holder.showdian.setBackgroundResource(R.mipmap.dian1);
            if(item.typeid.equals("7")){
                holder.zhuangtai.setTextColor(0xfff2a17e);
                holder.showdian.setBackgroundResource(R.mipmap.dian4);
            }else if(item.typeid.equals("2")){
                holder.zhuangtai.setTextColor(0xff8d9ac5);
                holder.showdian.setBackgroundResource(R.mipmap.dian2);
            }else if(item.typeid.equals("4")){
                holder.zhuangtai.setTextColor(0xff5cc2b0);
                holder.showdian.setBackgroundResource(R.mipmap.dian3);
            }else if(item.typeid.equals("0")){
                holder.zhuangtai.setTextColor(0xff3385ff);
                holder.showdian.setBackgroundResource(R.mipmap.dian1);
            }

        }else if(item.is_click.equals("2")){
            holder.showdian.setVisibility(View.VISIBLE);
            holder.showdian.setBackgroundResource(R.mipmap.shibai);
            holder.zhuangtai.setTextColor(0xff696969);

        }else if(item.is_click.equals("3")){
            holder.showdian.setVisibility(View.VISIBLE);
            holder.showdian.setBackgroundResource(R.mipmap.chenggong);
            holder.zhuangtai.setTextColor(0xffffbd32);
        }



        if(item.typeid.equals("7")){
            holder.show.setBackgroundColor(0xfff9efea);
//            holder.zhuangtai.setTextColor(0xfff2a17e);

        }
        if(item.typeid.equals("2")){
            holder.show.setBackgroundColor(0xffdde1ed);
        }
        if(item.typeid.equals("4")){
            holder.show.setBackgroundColor(0xffd8f4ed);
        }
        return convertView;
    }

    public void setData(List<XuQiudetailsdata> Data){
            this.postsListData= Data;
    }
    class ViewHolder {
        TextView senddata;
        TextView xuqiutype;
        TextView description;
        RoundImageView rc_img;
        TextView line1;
        ImageView xm_img;
        TextView xm_title;
        TextView xm_rank;
        TextView xm_linyu;
        ImageView showdian;
        TextView zhuangtai;
        TextView xm_description;
        RelativeLayout show;
    }
}
