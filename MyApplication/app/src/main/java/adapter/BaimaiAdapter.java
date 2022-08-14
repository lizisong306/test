package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.bmrank;



/**
 * Created by 13520 on 2016/11/15.
 */

public class BaimaiAdapter extends BaseAdapter  {

    private Context context;
    private List<bmrank> bmlist;
    private HashMap<Integer, Boolean> state =new HashMap<>();
    List<bmrank> lists;
    Handler handler;

    // 记录checkbox的状态

    public  BaimaiAdapter(){}
    public BaimaiAdapter(Context context, List<bmrank> bmlist, Handler handler){
        this.context=context;
        this.bmlist=bmlist;
        lists=new ArrayList<>();
        this.handler = handler;
    }
    public void clernMap(){
        state.clear();
    }
    @Override
    public int getCount() {
        return bmlist==null?0:bmlist.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder  holder=null;

        if(convertView==null){
           holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.baimai_gridview_item,null);
            holder.bm_imgview=(ImageView) convertView.findViewById(R.id.bm_imgview);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.bm_imgview.setBackgroundResource(bmlist.get(position).getImg());

        return convertView;
    }
    public  List<bmrank> getList(){
        return lists;
    }
    class ViewHolder{
        ImageView bm_imgview;
    }
}