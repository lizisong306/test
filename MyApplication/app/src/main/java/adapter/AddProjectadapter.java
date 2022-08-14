package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.AddProject;
import com.maidiantech.DetailsActivity;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.R;
import com.maidiantech.XuQiuDeail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.NetUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import entity.Details;
import entity.Posts;


/**
 * Created by lizisong on 2017/7/4.
 */

public class AddProjectadapter extends BaseAdapter{
    private Context context;
    private List<Posts> postsListData;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    int current = -1;


    public AddProjectadapter(Context context, List<Posts> postsListData){
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
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.addprojectadapter, null);
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
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.more = (LinearLayout)convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        try {
                holder.xm_layout.setVisibility(View.VISIBLE);
                holder.xm_zt.setVisibility(View.GONE);
                holder.xm_dianwei.setVisibility(View.VISIBLE);

                holder.xm_look.setText(postsListData.get(position).getClick());
                 boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI) {
                                ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                        , holder.xm_img, options);
                            } else {
                                holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
                                    , holder.xm_img, options);
                        }
                    if(postsListData.get(position).getLitpic()==null || postsListData.get(position).getLitpic().equals("")){
                        holder.xm_img.setVisibility(View.GONE);
                    }else{
                        holder.xm_img.setVisibility(View.VISIBLE);
                    }


//                if (postsListData.get(position).imageState != null) {
//                        holder.xm_layout.setVisibility(View.VISIBLE);
//                        holder.xm_img.setVisibility(View.VISIBLE);
//                        holder.xm_title.setVisibility(View.VISIBLE);
//                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
//                        if (state) {
//                            NetUtils.NetType type = NetUtils.getNetType();
//                            if (type == NetUtils.NetType.NET_WIFI) {
//                                ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
//                                        , holder.xm_img, options);
//                            } else {
//                                holder.xm_img.setBackgroundResource(R.mipmap.information_placeholder);
//                            }
//                        } else {
//                            ImageLoader.getInstance().displayImage(postsListData.get(position).getLitpic()
//                                    , holder.xm_img, options);
//                        }
//                    if(postsListData.get(position).image.image1==null || postsListData.get(position).image.image1.equals("")){
//                        holder.xm_img.setVisibility(View.GONE);
//                    }else{
//                        holder.xm_img.setVisibility(View.VISIBLE);
//                    }
//                }

                holder.xm_title.setText(postsListData.get(position).getTitle());
                if(postsListData.get(position).getArea_cate()!=null){
                    holder.xm_linyu.setText(postsListData.get(position).getArea_cate().getArea_cate1());
                }
                if(postsListData.get(position).getDescription()==null || postsListData.get(position).getDescription().equals("")){
                    holder.xm_description.setVisibility(View.GONE);
                }else{
                    holder.xm_description.setVisibility(View.VISIBLE);
                    holder.xm_description.setText(postsListData.get(position).getDescription().replaceAll("\n",""));
                }

                holder.xm_name.setText(postsListData.get(position).getTypename());
                if(postsListData.get(position).getSource()==null || postsListData.get(position).getSource().equals("")){
                    holder.xm_dianwei.setVisibility(View.GONE);
                }else{
                    holder.xm_dianwei.setVisibility(View.GONE);
                    holder.xm_dianwei.setVisibility(View.VISIBLE);
                    holder.xm_dianwei.setText(postsListData.get(position).getSource());
                    holder.xm_dianwei.setVisibility(View.GONE);
                }
                holder.line.setVisibility(View.VISIBLE);
            if(current == position){
                holder.more.setVisibility(View.VISIBLE);
                holder.xm_layout.setBackgroundColor(0xffF6F6F6);
            }else{
                holder.more.setVisibility(View.GONE);
                holder.xm_layout.setBackgroundColor(0xffFfFfFf);
            }
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情
                    AddProject.retdata = postsListData.get(position);
                    if(postsListData.get(position).typeid.equals("2")){
                        Intent intent = new Intent(context, NewProjectActivity.class);
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
        } catch (Exception e) {

        }
        return convertView;
    }
    class ViewHolder {
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
        //        线
        TextView line;
        LinearLayout more;
    }

    public void setCurrent(int index){
        current = index;
    }
}
