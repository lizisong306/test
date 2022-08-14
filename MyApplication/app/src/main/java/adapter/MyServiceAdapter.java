package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by 13520 on 2016/8/25.
 */
public class MyServiceAdapter extends BaseAdapter {
    private Context context;
    private DisplayImageOptions options;
//    private int [] str={R.mipmap.a, R.mipmap.b, R.mipmap.e, R.mipmap.c, R.mipmap.d};

    public MyServiceAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return 5;
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
        if(convertView==null){
            convertView=View.inflate(context, R.layout.gridview_item,null);
        }
       ImageView imageView=(ImageView) convertView.findViewById(R.id.imageView);
//        imageView.setImageResource(str[position]);
//      imageView.setImageResource(str[position]);
        return convertView;
    }

}
