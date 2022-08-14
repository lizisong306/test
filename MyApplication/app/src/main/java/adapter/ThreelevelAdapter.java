package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.List;

import entity.LangyaSimple;
import entity.LevelCate;
import entity.LevelCount;
import entity.LevelData;

/**
 * Created by 13520 on 2016/11/24.
 */

public class ThreelevelAdapter extends BaseAdapter {
    private Context context;
    List<LevelData> datalist;
    List<LevelCount> count;
    List<LevelCate> sonCate;
    List<LangyaSimple> mLangyaDatas;
    public  ThreelevelAdapter(){}
    public  ThreelevelAdapter(Context context, List<LevelData> datalist,List<LangyaSimple> mLangyaDatas){
        this.context=context;
        this.datalist=datalist;
        this.mLangyaDatas=mLangyaDatas;
    }
    @Override
    public int getCount() {
        for(int i=0;i<datalist.size();i++){
                sonCate = datalist.get(i).getSonCate();
            for(int j=0;j<sonCate.size();j++){
                count = sonCate.get(j).getCount();
            }
        }
        return count.size();
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
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.count_item,null);
            holder.level_text=(TextView) convertView.findViewById(R.id.level_text);
            holder.level_num=(TextView) convertView.findViewById(R.id.level_num);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.level_text.setText(count.get(position).getTypename());
        holder.level_num.setText(count.get(position).getNumber());
       /* if(mLangyaDatas.get(position).getTitle().equals("新材料")){
            Log.i("log",count.toString());
            holder.level_text.setText(count.get(position).getTypename());
        }*/
        return convertView;
    }
    class ViewHolder {
        TextView level_text;
        TextView level_num;
    }
}
