package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Checkindustry;
import entity.LangyaSimple;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


/**
 * Created by 13520 on 2016/11/17.
 */

public class PulsetwoAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private HashMap<Integer, Boolean> date =new HashMap<>();
    private List<LangyaSimple> mLangyaDatas;
    List<LangyaSimple> datalist;
    List<Checkindustry> dexlist;
    public PulsetwoAdapter(){}
    public PulsetwoAdapter(Context context,List<LangyaSimple> mLangyaDatas, List<LangyaSimple> datalist){
        this.context = context;
        this.mLangyaDatas=mLangyaDatas;
        this.datalist=datalist;
        dexlist=new ArrayList<>();
    }
    private List<Object> toSingleDimension(List<Object> twoDim) {
        List<Object> single = new ArrayList<>();
        for(int i=0;i<twoDim.size();i++){
            Object obj = twoDim.get(i);
            List<Object> objList = (List<Object>)obj;
            single.addAll(objList);
        }
        return single;
    }
    @Override
    public int getCount() {
        return mLangyaDatas==null?0:mLangyaDatas.size();
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
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.pulse_listview_item,null);
            holder.pulse_check=(ImageView) convertView.findViewById(R.id.pulse_check);
            holder.pulse_textview=(TextView) convertView.findViewById(R.id.pulse_textview);
            holder.three_gridview=(GridView) convertView.findViewById(R.id.three_gridview);
            holder.linyu_pic=(ImageView) convertView.findViewById(R.id.linyu_pic);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.pulse_textview.setText(mLangyaDatas.get(position).getTitle());
        holder.linyu_pic.setBackgroundResource(mLangyaDatas.get(position).getPicid());
        boolean state = false;
        for(int i=0; i<datalist.size();i++){
            LangyaSimple item = datalist.get(i);
            if(mLangyaDatas.get(position).getTitle().equals(item.getTitle())){
                state = true;
                break;
            }
        }
        if(state){
            holder.pulse_check.setVisibility(View.VISIBLE);
            holder.pulse_textview.setTextColor(context.getResources().getColor(R.color.lansecolor));
        }else {
            holder.pulse_textview.setTextColor(context.getResources().getColor(R.color.text_gray));
            holder.pulse_check.setVisibility(View.INVISIBLE);
        }



        return convertView;
     }
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder=null;
        if(convertView==null){
            holder=new HeaderViewHolder();
            convertView=View.inflate(context,R.layout.head_listview,null);
            holder.dustry_heads=(TextView) convertView.findViewById(R.id.dustry_heads);
            convertView.setTag(holder);
        }else{
            holder= (HeaderViewHolder) convertView.getTag();
        }
      holder.dustry_heads.setText(mLangyaDatas.get(position).getProject_title());
        return convertView;
    }
    @Override
    public long getHeaderId(int position) {
        return Long.parseLong(this.mLangyaDatas.get(position).getProj_id());
    }
    public HashMap<Integer, Boolean> getDate() {
        return date;
    }

    public void setDate(HashMap<Integer, Boolean> date) {
        this.date = date;
    }

    public  List<Checkindustry> getList(){
        return dexlist;
    }

    class ViewHolder{
        ImageView pulse_check;
        TextView pulse_textview;
        GridView three_gridview;
        ImageView linyu_pic;
    }
    class HeaderViewHolder{
        TextView dustry_heads;
    }
}