package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.List;

/**
 * Created by 13520 on 2016/11/2.
 */
public class SearchAdapter extends BaseAdapter {
    Context context;
    List<String> findalls;
    public SearchAdapter(){}
    public SearchAdapter(Context context,List<String> findalls){
        this.context=context;
        this.findalls=findalls;
    }
    @Override
    public int getCount() {
        return findalls==null?0:findalls.size();/*(zixun!=null && zixun.count>0) + (zixun!=null && zixun.count>0) + (zixun!=null && zixun.count>0);*/
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
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.history_text,null);
            holder.history_text=(TextView) convertView.findViewById(R.id.history_text);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.history_text.setText(findalls.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView history_text;
    }
}
