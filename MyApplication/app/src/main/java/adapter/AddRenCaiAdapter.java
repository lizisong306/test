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
import com.maidiantech.DetailsActivity;
import com.maidiantech.NewRenCaiDetail;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.R;
import com.maidiantech.XuQiuDeail;
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
 * Created by lizisong on 2017/7/4.
 */

public class AddRenCaiAdapter extends BaseAdapter {
    private Context context;
    private List<Posts> postsListData;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    int current = -1;

    public AddRenCaiAdapter(Context context, List<Posts> postsListData){
        this.context = context;
        this.postsListData = postsListData;
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
        ViewHolder holder;
        if(convertView == null){
            //人才
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.addrencaiadapter, null);
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
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.more = (LinearLayout)convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        try{
                holder.rc_layout.setVisibility(View.VISIBLE);
                holder.rc_zt.setVisibility(View.GONE);
                holder.rc_uname.setText(postsListData.get(position).getUsername());
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
            if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                holder.rc_zan.setVisibility(View.GONE);
            }else{
                holder.rc_zan.setVisibility(View.GONE);
                holder.rc_zan.setText(postsListData.get(position).getSource());

            }

            holder.rc_look.setText(postsListData.get(position).getClick());
                holder.line.setVisibility(View.VISIBLE);

            if(current == position){
                holder.more.setVisibility(View.VISIBLE);
                holder.rc_layout.setBackgroundColor(0xffF6F6F6);
            }else{
                holder.more.setVisibility(View.GONE);
                holder.rc_layout.setBackgroundColor(0xffffffff);
            }
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情
                    AddRencai.retdata=postsListData.get(position);
                    if(postsListData.get(position).typeid.equals("4")){
                        Intent intent = new Intent(context, NewRenCaiTail.class);
                        intent.putExtra("aid", postsListData.get(position).getId());
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("id", postsListData.get(position).getId());
                        intent.putExtra("name", postsListData.get(position).getTypename());
                        intent.putExtra("pic", postsListData.get(position ).getLitpic());
                        context.startActivity(intent);
                    }

                }
            });


        }catch (Exception e){

        }
        return convertView;
    }
    public void setCurrent(int index){
        current = index;
    }
    class ViewHolder {
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
        //        线
        TextView line;
        LinearLayout more;
    }
}
