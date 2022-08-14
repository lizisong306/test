package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.AddRencai;
import com.maidiantech.NewRenCaiDetail;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.R;
import com.maidiantech.RencaiActivity;
import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.XuQiuDeail;
import com.maidiantech.YuyueRenCai;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.NetUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import entity.Area_cate;
import entity.Posts;

import view.RoundImageView;

/**
 * Created by lizisong on 2017/7/26.
 */

public class AppointmentSpecialistAdapter extends BaseAdapter {

    Context context;
    List<Posts> postsListData;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    int current = -1;

    public AppointmentSpecialistAdapter(Context cxt, List<Posts> list){
        context = cxt;
        postsListData = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holder;
        if(convertView == null){
            holder = new HolderView();
            convertView = View.inflate(context, R.layout.appointmentspecialistadapter, null);
            holder.rc_img = (RoundImageView) convertView.findViewById(R.id.rc_img);
            holder.rc_linyu = (TextView) convertView.findViewById(R.id.rc_linyu);
            holder.rc_look = (TextView) convertView.findViewById(R.id.rc_look);
            holder.rc_title = (TextView) convertView.findViewById(R.id.rc_title);
            holder.rc_zhicheng = (TextView) convertView.findViewById(R.id.rc_zhicheng);
            holder.rc_uname = (TextView) convertView.findViewById(R.id.rc_uname);
            holder.rc_zan = (TextView) convertView.findViewById(R.id.rc_zan);
            holder.rc_layout = (LinearLayout) convertView.findViewById(R.id.rc_layout);
            holder.rc_danwei = (TextView)convertView.findViewById(R.id.rc_danwei);
            holder.rc_zt = (ImageView) convertView.findViewById(R.id.rc_zt);
            holder.rencai_zhiding = (ImageView) convertView.findViewById(R.id.rencai_zhiding);
            holder.rencai_tuijian = (ImageView) convertView.findViewById(R.id.rencai_tuijian);
            holder.rc_text=(TextView) convertView.findViewById(R.id.rc_text);
            holder.rc_yuanshi=(ImageView) convertView.findViewById(R.id.rc_yuanshi);
            holder.rc_yuyuecount = (TextView)convertView.findViewById(R.id.rc_yuyuecount);
            holder.more = (LinearLayout)convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        }else {
            holder = (HolderView) convertView.getTag();
        }
        try{
            holder.rc_layout.setVisibility(View.VISIBLE);
            holder.rc_zt.setVisibility(View.GONE);
            holder.rc_uname.setText(postsListData.get(position).getUsername());
            holder.rc_title.setText(postsListData.get(position).getDescription());
            holder.rc_zhicheng.setText(postsListData.get(position).getRank());
            holder.rc_yuanshi.setVisibility(View.VISIBLE);
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
            }

            holder.rc_look.setText(postsListData.get(position).getClick());
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
            holder.rc_danwei.setText(postsListData.get(position).unit);
            if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                holder.rc_zan.setVisibility(View.GONE);
            }else{
                holder.rc_zan.setVisibility(View.GONE);
                holder.rc_zan.setText(postsListData.get(position).getSource());

            }
            holder.rc_yuyuecount.setText("预约:"+postsListData.get(position).getClick());

            holder.rc_look.setText(postsListData.get(position).getClick());
//            holder.line.setVisibility(View.VISIBLE);

            if(current == position){
                holder.more.setVisibility(View.VISIBLE);
                holder.rc_title.setVisibility(View.VISIBLE);
                holder.rc_layout.setBackgroundColor(0xffF6F6F6);
            }else{
                holder.more.setVisibility(View.GONE);
                holder.rc_title.setVisibility(View.GONE);
                holder.rc_layout.setBackgroundColor(0xffffffff);
            }

             holder.rc_yuanshi.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(context, YuyueRenCai.class);
                     intent.putExtra("name", postsListData.get(position).getUsername());
                     intent.putExtra("zhicheng", postsListData.get(position).getRank());
                     Area_cate area_cate = postsListData.get(position).getArea_cate();
                     if(area_cate != null){
                         intent.putExtra("linyu", area_cate.getArea_cate1());
                     }
                     intent.putExtra("aid",postsListData.get(position).getId());
                     intent.putExtra("danwei",postsListData.get(position).unit);
                     intent.putExtra("img",postsListData.get(position).getLitpic());
                     intent.putExtra("count",postsListData.get(position).getClick());
                     intent.putExtra("original_price",postsListData.get(position).original_price);
                     intent.putExtra("current_price",postsListData.get(position).current_price);
                     context.startActivity(intent);
                 }
             });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情
//                    AddRencai.retdata = postsListData.get(position);
//                    Intent intent = new Intent(context, RencaiActivity.class);
//                    intent.putExtra("zhicheng", postsListData.get(position).getRank());
//                    intent.putExtra("id", postsListData.get(position).getId());
//                    intent.putExtra("name", postsListData.get(position).getTypename());
//                    intent.putExtra("pic", postsListData.get(position).getLitpic());
//
//                    intent.putExtra("name1", postsListData.get(position).getUsername());
//                    Area_cate area_cate = postsListData.get(position).getArea_cate();
//                    if(area_cate != null){
//                        intent.putExtra("linyu1", area_cate.getArea_cate1());
//                    }
//                    intent.putExtra("danwei1",postsListData.get(position).unit);
//                    intent.putExtra("img1",postsListData.get(position).getLitpic());
//                    intent.putExtra("count1",postsListData.get(position).getClick());
//                    intent.putExtra("original_price",postsListData.get(position).original_price);
//                    intent.putExtra("current_price",postsListData.get(position).current_price);
//
//                    context.startActivity(intent);

                    Intent intent = new Intent(context, XinFanAnCeShi.class);
                    intent.putExtra("aid", postsListData.get(position).getId());
                    context.startActivity(intent);

                }
            });
        }catch (Exception e){

        }
        return convertView;
    }

    class HolderView{
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
        TextView rc_yuyuecount;
        ImageView rc_yuanshi;
        TextView rc_danwei;
        LinearLayout more;
    }
    public void setCurrent(int index){
        current = index;
    }

    public void setPostsListData(List<Posts> list){
        postsListData = list;
    }


}
