package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.ArrayList;
import java.util.List;

import entity.LangyaSimple;
/**
 * Created by 13520 on 2016/11/22.
 */

public class ChooseAdapter extends BaseAdapter {
    private Context context;
    private List<LangyaSimple> dexlist;
    List<LangyaSimple> infolist;
    List<LangyaSimple> zhizaolist;
    List<LangyaSimple> cailiaolist;
    List<LangyaSimple> jishulist;
    List<LangyaSimple> huanbaolist;
    List<LangyaSimple> huagonlist;
    List<LangyaSimple> nengyuanlist;
    List<LangyaSimple> chuanyilist;
    List<LangyaSimple> aitalist;
    List<LangyaSimple> list;
    public ChooseAdapter(Context context,List<LangyaSimple> dexlist ){
        this.context=context;
        this.dexlist=dexlist;
        infolist=new ArrayList<>();
        zhizaolist=new ArrayList<>();
        cailiaolist=new ArrayList<>();
        jishulist=new ArrayList<>();
        huanbaolist=new ArrayList<>();
        huagonlist=new ArrayList<>();
        nengyuanlist=new ArrayList<>();
        chuanyilist=new ArrayList<>();
        aitalist=new ArrayList<>();
        list=new ArrayList<>();
        for(int i=0;i<dexlist.size();i++){
            switch (dexlist.get(i).getProj_id()){
                case "2":
                    infolist.add(dexlist.get(i));
                    break;
                case "1":
                    zhizaolist.add(dexlist.get(i));
                    break;
                case "3":
                    cailiaolist.add(dexlist.get(i));
                    break;
                case "4":
                    jishulist.add(dexlist.get(i));
                    break;
                case "5":
                    huanbaolist.add(dexlist.get(i));
                    break;
                case "6":
                    chuanyilist.add(dexlist.get(i));
                    break;
                case "7":
                    huagonlist.add(dexlist.get(i));
                    break;
                case  "8":
                    nengyuanlist.add(dexlist.get(i));
                    break;
                case "9":
                    aitalist.add(dexlist.get(i));
                    break;
            }
        }
        if(infolist.size()!=0){
            list.add(infolist.get(0));
        }
        if(zhizaolist.size()!=0){
            list.add(zhizaolist.get(0));
        }
        if(cailiaolist.size()!=0){
            list.add(cailiaolist.get(0));
        }
        if(jishulist.size()!=0){
            list.add(jishulist.get(0));
        }
        if(huanbaolist.size()!=0){
            list.add(huanbaolist.get(0));
        }
        if(chuanyilist.size()!=0){
            list.add(chuanyilist.get(0));
        } if(huagonlist.size()!=0){
            list.add(huagonlist.get(0));
        } if(nengyuanlist.size()!=0){
            list.add(nengyuanlist.get(0));
        }
        if(aitalist.size()!=0){
            list.add(aitalist.get(0));
        }
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        final LangyaSimple ds=list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.choose_item,null);
//            holder.choose_text=(TextView) convertView.findViewById(R.id.choose_text);
//            holder.twofenlei_text=(TextView) convertView.findViewById(R.id.twofenlei_text);
//            holder.item_delete=(ImageView) convertView.findViewById(R.id.item_delete);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.choose_text.setText(ds.getProject_title()+"");
        if(ds.getProj_id().equals("2")){
            holder.twofenlei_text.setText("("+infolist.size()+")");
        }
        else if(ds.getProj_id().equals("1")){
            holder.twofenlei_text.setText("("+zhizaolist.size()+")");
        }
        else if(ds.getProj_id().equals("3")){
            holder.twofenlei_text.setText("("+cailiaolist.size()+")");
        }
        else if(ds.getProj_id().equals("4")){
            holder.twofenlei_text.setText("("+jishulist.size()+")");
        }
        else if(ds.getProj_id().equals("5")){
            holder.twofenlei_text.setText("("+huanbaolist.size()+")");
        }
        else if(ds.getProj_id().equals("6")){
            holder.twofenlei_text.setText("("+chuanyilist.size()+")");
        }
        else if(ds.getProj_id().equals("7")){
            holder.twofenlei_text.setText("("+huagonlist.size()+")");
        }
        else if(ds.getProj_id().equals("8")){
            holder.twofenlei_text.setText("("+nengyuanlist.size()+")");
        }
        else if(ds.getProj_id().equals("9")){
            holder.twofenlei_text.setText("("+aitalist.size()+")");
        }
     /*   holder.twofenlei_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,IndexActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("dexlist", (Serializable) dexlist);
                b.putSerializable("zhizaolist", (Serializable) zhizaolist);
                b.putSerializable("infolist", (Serializable) infolist);
                b.putSerializable("cailiaolist", (Serializable) cailiaolist);
                b.putSerializable("jishulist", (Serializable) jishulist);
                b.putSerializable("huanbaolist", (Serializable) huanbaolist);
                b.putSerializable("chuanyilist", (Serializable) chuanyilist);
                b.putSerializable("huagonlist", (Serializable) huagonlist);
                b.putSerializable("nengyuanlist", (Serializable) nengyuanlist);
                b.putSerializable("aitalist", (Serializable) aitalist);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });*/
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(ds);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView choose_text;
        TextView twofenlei_text;
        ImageView item_delete;
    }
}