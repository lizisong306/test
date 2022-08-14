package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.List;

import entity.LangyaSimple;

/**
 * Created by 13520 on 2016/11/24.
 */

public class TwolevelAdapter extends BaseAdapter {
    private Context context;
    List<LangyaSimple> infolist;
    List<LangyaSimple> zhizaolist;
    List<LangyaSimple> cailiaolist;
    List<LangyaSimple> jishulist;
    List<LangyaSimple> huanbaolist;
    List<LangyaSimple> huagonlist;
    List<LangyaSimple> nengyuanlist;
    List<LangyaSimple> chuanyilist;
    List<LangyaSimple> aitalist;
    List<LangyaSimple> dexlist;

    public  TwolevelAdapter(){}
    public  TwolevelAdapter(Context context,List<LangyaSimple> dexlist,List<LangyaSimple> infolist,List<LangyaSimple> zhizaolist,
                            List<LangyaSimple> cailiaolist,List<LangyaSimple> jishulist,List<LangyaSimple> huanbaolist,
                            List<LangyaSimple> huagonlist,List<LangyaSimple> nengyuanlist,List<LangyaSimple> chuanyilist,
                            List<LangyaSimple> aitalist){
        this.context=context;
        this.dexlist=dexlist;
        this.infolist=infolist;
        this.zhizaolist=zhizaolist;
        this.cailiaolist=cailiaolist;
        this.jishulist=jishulist;
        this.huanbaolist=huanbaolist;
        this.huagonlist=huagonlist;
        this.nengyuanlist=nengyuanlist;
        this.chuanyilist=chuanyilist;
        this.aitalist=aitalist;
    }
    @Override
    public int getCount() {
        for (int i = 0; i<dexlist.size(); i++){
            LangyaSimple ds=dexlist.get(i);
            if(ds.getProj_id().equals("2")){
                  infolist.size();
            }
             if(ds.getProj_id().equals("1")){
                 zhizaolist.size();
            }
             if(ds.getProj_id().equals("3")){
               cailiaolist.size();
            }
             if(ds.getProj_id().equals("4")){
               jishulist.size();
            }
             if(ds.getProj_id().equals("5")){
                 huanbaolist.size();
            }
             if(ds.getProj_id().equals("6")){
               chuanyilist.size();
            }
             if(ds.getProj_id().equals("7")){
                 huagonlist.size();
            }
             if(ds.getProj_id().equals("8")){
                nengyuanlist.size();
            }
             if(ds.getProj_id().equals("9")){
                 aitalist.size();
            }
        }
       return infolist.size()+zhizaolist.size();
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
        final LangyaSimple ds=dexlist.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.level_listview_item,null);
            holder.level_text=(TextView) convertView.findViewById(R.id.level_text);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        try {
            if(ds.getProj_id().equals("2")){
                holder.level_text.setText(infolist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("1")){
                holder.level_text.setText(zhizaolist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("3")){
                holder.level_text.setText(cailiaolist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("4")){
                holder.level_text.setText(jishulist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("5")){
                holder.level_text.setText(huanbaolist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("6")){
                holder.level_text.setText(chuanyilist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("7")){
                holder.level_text.setText(huagonlist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("8")){
                holder.level_text.setText(nengyuanlist.get(position).getTitle());
            }
            if(ds.getProj_id().equals("9")){
                holder.level_text.setText(aitalist.get(position).getTitle());
            }
        }catch (Exception e){}
        return convertView;
    }
    class ViewHolder{
        TextView level_text;
    }
}