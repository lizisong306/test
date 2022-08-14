package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.List;

import entity.hotwords;

/**
 * Created by 13520 on 2016/11/3.
 */
public class HotwordsAdapter extends BaseAdapter {
    private Context context;
    private List<hotwords> hotWordslist;;
    public  HotwordsAdapter(){}
    public HotwordsAdapter(Context context,List<hotwords> hotWordslist ){
        this.context=context;
        this.hotWordslist=hotWordslist;
    }
    @Override
    public int getCount() {
        return 3;
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
        //hot_text_title
        ViewHolder holder=null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.hot_item,null);
            holder.hot_text_title=(TextView) convertView.findViewById(R.id.hot_text_title);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.hot_text_title.setText(hotWordslist.get(position).getKeyword());
        return convertView;
    }
    class ViewHolder{
        TextView hot_text_title;
    }
}
