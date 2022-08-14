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
import com.maidiantech.AddSheBei;
import com.maidiantech.DetailsActivity;
import com.maidiantech.R;
import com.maidiantech.XuQiuDeail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;
import Util.NetUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import entity.Posts;

/**
 * Created by lizisong on 2017/7/4.
 */

public class AddShebeiAdapter extends BaseAdapter {
    private Context context;
    private List<Posts> postsListData;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    int current = -1;

    public AddShebeiAdapter(Context context, List<Posts> postsListData){
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
            convertView = View.inflate(context, R.layout.addshebeiadapter, null);
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
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.more = (LinearLayout)convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }
        try{
                holder.device_layout.setVisibility(View.VISIBLE);
                holder.sb_zt.setVisibility(View.GONE);
                holder.shebei_dianzan.setVisibility(View.GONE);
                holder.device_layout.setVisibility(View.VISIBLE);
                holder.device_img.setVisibility(View.VISIBLE);
                holder.device_title.setVisibility(View.VISIBLE);
                if(postsListData.get(position).image==null  || postsListData.get(position).image.image1==null || postsListData.get(position).image.image1.equals("")){
                    holder.device_img.setVisibility(View.GONE);
                }else{
                    holder.device_img.setVisibility(View.VISIBLE);
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
            holder.device_look.setText(postsListData.get(position).getClick());
            if(current == position){
                holder.more.setVisibility(View.VISIBLE);
                holder.device_layout.setBackgroundColor(0xffF6F6F6);
            }else{
                holder.more.setVisibility(View.GONE);
                holder.device_layout.setBackgroundColor(0xffffffff);
            }
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情
                    AddSheBei.retdata=postsListData.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("id", postsListData.get(position).getId());
                    intent.putExtra("name", postsListData.get(position).getTypename());
                    intent.putExtra("pic", postsListData.get(position ).getLitpic());
                    context.startActivity(intent);
                }
            });

        }catch (Exception e){
        }
        return convertView;
    }
    public void setCurrent(int index){
        current = index;
    }
    class ViewHolder{
        //设备
        ImageView device_img;
        TextView device_title;
        TextView device_linyu;
        TextView device_description;
        TextView device_zan;
        TextView device_look;
        LinearLayout device_layout;
        ImageView sb_zt;
        ImageView shebei_zhiding;
        ImageView shebei_tuijian;
        LinearLayout shebei_dianzan;
        TextView line;
        LinearLayout more;
    }
}
