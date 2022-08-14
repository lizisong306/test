package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.maidiantech.WebQingBaoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.NetUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.PostData;

/**
 * Created by lizisong on 2017/9/29.
 */

public class LookMoreQingBaoAdapter  extends BaseAdapter{
    private Context context;
    private List<PostData> postsListData;
    private DisplayImageOptions options;
    private String read_ids;
    private int width= MyApplication.width;
    private int height=MyApplication.height;
    @Override
    public int getCount() {
        return this.postsListData.size();
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
        HoldView holdView = null;
        if(convertView == null){
             holdView = new HoldView();
             convertView = View.inflate(context, R.layout.lookmoreqingbaoadapter,null);
             holdView.hangyeicon = (ImageView)convertView.findViewById(R.id.hangyeicon);
             holdView.xytitle = (TextView)convertView.findViewById(R.id.xytitle);
             holdView.xiangqing = (TextView)convertView.findViewById(R.id.xiangqing);
             holdView.hangyefenxi = (TextView)convertView.findViewById(R.id.hangyefenxi);
             holdView.chakan = (TextView)convertView.findViewById(R.id.chakan);
             holdView.hangqingbg = (LinearLayout) convertView.findViewById(R.id.hangye);
             convertView.setTag(holdView);
        }else {
            holdView = (HoldView) convertView.getTag();
        }
        try{
            final PostData item = postsListData.get(position);
            ImageLoader.getInstance().displayImage(item.litpic
                        , holdView.hangyeicon, options);
            holdView.xytitle.setText(item.title);
            holdView.xiangqing.setText(item.description);
            float price = Float.parseFloat(item.price);
            if((int)price == 0){
                holdView.hangyefenxi.setVisibility(View.VISIBLE);
                holdView.hangyefenxi.setText("限免");
            }else if(price < 0){
                holdView.hangyefenxi.setVisibility(View.GONE);
            }else {
                holdView.hangyefenxi.setVisibility(View.VISIBLE);
                holdView.hangyefenxi.setText("￥"+item.price);
            }
            holdView.hangqingbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebQingBaoActivity.class);
                    intent.putExtra("url", item.coverUrl);
                    intent.putExtra("contenturl", item.jumpUrl);
                    intent.putExtra("title", item.title);
                    context.startActivity(intent);
                }
            });


        }catch (Exception e){

        }
        return convertView;
    }
    public LookMoreQingBaoAdapter(Context context, List<PostData> postsListData){
        this.context = context;
        this.postsListData = postsListData;
        this.options = ImageLoaderUtils.initOptions();

    }
    class HoldView{
        public LinearLayout hangqingbg;
        public ImageView hangyeicon;
        public TextView  xytitle;
        public TextView xiangqing;
        public TextView hangyefenxi;
        public TextView chakan;
    }


}
