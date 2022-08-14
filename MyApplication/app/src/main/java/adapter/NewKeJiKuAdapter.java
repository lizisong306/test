package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AdressBaiduMap;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewProJect;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.PersonActivity;
import com.maidiantech.R;
import com.maidiantech.TianXuQiu;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.UnitedStatesDeilActivity;
import com.maidiantech.WriteXuQiu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.NewKejiKuShow;
import entity.Project_Data;
import view.RoundImageView;
import view.WordCloudView;
import view.ZQImageViewRoundOval;

/**
 * Created by Administrator on 2019/11/13.
 */

public class NewKeJiKuAdapter extends BaseAdapter {
    private Context context;
    private int width= MyApplication.width;
    private int height=MyApplication.height;
    private DisplayImageOptions options;
    private List<NewKejiKuShow> showList;
    private List<Project_Data> project_data = new ArrayList<>();
    private boolean isfirst = false;
    XianguAdapter xianguAdapter;
    WordCloudView quanwcv;

    public NewKeJiKuAdapter(Context context, List<NewKejiKuShow> postsListData) {
        this.context = context;
        this.showList = postsListData;
        this.options = ImageLoaderUtils.initOptions();
        for(int i=0;i<showList.size();i++){
            NewKejiKuShow item = showList.get(i);
            if(item.typename.equals("project_list")){
                if(item.project_data != null && item.project_data.size()>0){
                    for(int j=0;j<item.project_data.size();j++){
                        Project_Data project = item.project_data.get(j);
                        project_data.add(project);
                    }
                }

            }
        }
        xianguAdapter = new XianguAdapter();
    }
    @Override
    public int getCount() {
        return this.showList.size();
    }
    Handler adapterhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String txt = (String) msg.obj;
            int with = msg.what;
            quanwcv.addTextView(txt, with);
        }
    };

    @Override
    public Object getItem(int position) {
        return this.showList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView =null;
        if(convertView == null){
             holdView = new HoldView();
             convertView = View.inflate(context, R.layout.newkejikuadapter,null);
             holdView.title = convertView.findViewById(R.id.title);
//             holdView.kkk = convertView.findViewById(R.id.kkk);
             holdView.wcv = convertView.findViewById(R.id.wcv);
             holdView.zhuanjialay = convertView.findViewById(R.id.zhuanjialay);
             holdView.zhuanjia1 = convertView.findViewById(R.id.zhuanjia1);
             holdView.wutu = convertView.findViewById(R.id.wutu);
             holdView.rencai_title = convertView.findViewById(R.id.rencai_title);
             holdView.rencai_lingyu = convertView.findViewById(R.id.rencai_lingyu);
             holdView.rank = convertView.findViewById(R.id.rank);
             holdView.rencai_img = convertView.findViewById(R.id.rencai_img);
             holdView.yuyue = convertView.findViewById(R.id.yuyue);
             holdView.chakangengduo = convertView.findViewById(R.id.chakangengduo);
             holdView.viewPagerContainer = convertView.findViewById(R.id.viewPagerContainer);
             holdView.viewpager = convertView.findViewById(R.id.viewpager);
             holdView.viewpager.setPageMargin(20);
            holdView.viewpager.setClipChildren(false);
            //父容器一定要设置这个，否则看不出效果
            holdView.viewPagerContainer.setClipChildren(false);
            holdView.viewpager.setPageTransformer(true,new view.ScaleInTransformer());
            //设置预加载数量
            holdView.viewpager.setOffscreenPageLimit(3);
            holdView.viewpager.setCurrentItem(Integer.MAX_VALUE);
             holdView.tuijian = convertView.findViewById(R.id.tuijian);
             holdView.title1 = convertView.findViewById(R.id.title1);
             holdView.title2 = convertView.findViewById(R.id.title2);
             holdView.kexueyuan = convertView.findViewById(R.id.kexueyuan);
             holdView.jigou = convertView.findViewById(R.id.jigou);
             holdView.zhuguanbumen = convertView.findViewById(R.id.zhuguanbumen);
             holdView.waiwenming = convertView.findViewById(R.id.waiwenming);
             holdView.img = convertView.findViewById(R.id.img);
             holdView.kexueyuanname = convertView.findViewById(R.id.kexueyuanname);
             holdView.txt1 = convertView.findViewById(R.id.txt1);
             holdView.txt2 = convertView.findViewById(R.id.txt2);
             holdView.txt3 = convertView.findViewById(R.id.txt3);
             holdView.txt4 = convertView.findViewById(R.id.txt4);
             holdView.txt5 = convertView.findViewById(R.id.txt5);
             holdView.waiwen = convertView.findViewById(R.id.waiwen);
             holdView.waiwencontent = convertView.findViewById(R.id.waiwencontent);
             holdView.adresslay     =convertView.findViewById(R.id.adresslay);
             holdView.buman = convertView.findViewById(R.id.buman);
             holdView.bumancontent = convertView.findViewById(R.id.bumancontent);
             holdView.adress = convertView.findViewById(R.id.adress);
             holdView.adresscontent = convertView.findViewById(R.id.adresscontent);
             holdView.zhuanjia = convertView.findViewById(R.id.zhuanjia);
             holdView.zhuanjiacount = convertView.findViewById(R.id.zhuanjiacount);
             holdView.xiangmu = convertView.findViewById(R.id.xiangmu);
             holdView.xiangmucount = convertView.findViewById(R.id.xiangmucount);
             holdView.shiyanshi = convertView.findViewById(R.id.shiyanshi);
             holdView.shiyanshicount = convertView.findViewById(R.id.shiyanshicount);
             holdView.shebei = convertView.findViewById(R.id.shebei);
             holdView.shebeicount = convertView.findViewById(R.id.shebeicount);
             holdView.chakangengduotext = convertView.findViewById(R.id.chakangengduotext);
             holdView.lianxyansuo = convertView.findViewById(R.id.lianxyansuo);
             holdView.dixian = convertView.findViewById(R.id.dixian);
             quanwcv = holdView.wcv;
             convertView.setTag(holdView);
        }else{
             holdView = (HoldView)convertView.getTag();
        }
        final NewKejiKuShow item = showList.get(position);
        if(item.type==1){
            holdView.title.setVisibility(View.VISIBLE);
            holdView.dixian.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);
            holdView.title.setText(item.nav_name);
        }else if(item.type == -1){
            holdView.title.setVisibility(View.GONE);
            holdView.dixian.setVisibility(View.VISIBLE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);

        }else if(item.type == 2){
            holdView.title.setVisibility(View.GONE);
            holdView.dixian.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.VISIBLE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);
            if(item.typename.equals("talent_list")){
                holdView.chakangengduotext.setText("查看更多专家");
            }else if(item.typename.equals("project_list")){
                holdView.chakangengduotext.setText("查看更多精品项目");
            }else if(item.typename.equals("laboratory_list")){
                holdView.chakangengduotext.setText("查看更多科研机构");
            }
            holdView.chakangengduo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if(item.typename.equals("talent_list")){
                         WriteXuQiu.entry_address = 3;
                         Intent intent = new Intent(context, PersonActivity.class);
                         context.startActivity(intent);
                     }else if(item.typename.equals("project_list")){
                         WriteXuQiu.entry_address = 4;
                         Intent intent = new Intent(context, NewProJect.class);
                         context.startActivity(intent);
                     }else if(item.typename.equals("laboratory_list")){
                         Intent intent = new Intent(context, UnitedStatesActivity.class);
                         context.startActivity(intent);
                     }
                }
            });
        }/*else if(item.typename.equals("hot_search")){
            holdView.title.setVisibility(View.GONE);
            holdView.kkk.setVisibility(View.VISIBLE);
            holdView.dixian.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);
            holdView.wcv.setVisibility(View.VISIBLE);

//            quanwcv = holdView.wcv;
            if(!isfirst){
                isfirst = true;

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                holdView.wcv.removeAllViews();
                        int weight =23;
                        int off = 2;
                        for(int i=0;i<item.hot_search_data.size();i++){
                            String txt = item.hot_search_data.get(i);
                            Message msg = new Message();
                            msg.what = weight;
                            msg.obj = txt;
//                            adapterhandler.sendMessage(msg);
                            holdView.wcv.addTextView(txt, weight);
                            if(--off == 0) {
                                off = 2;
                                if(weight > 3) weight=weight-1;
                            }
                        }
//                    }
//                }).start();
//                holdView.wcv.invalidate();


            }
        }*/else if(item.typename.equals("talent_list")){
            holdView.dixian.setVisibility(View.GONE);
            holdView.title.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.VISIBLE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);
            if(item.talent_data.litpic != null && !item.talent_data.litpic.equals("")){
                holdView.rencai_img.setVisibility(View.VISIBLE);
                holdView.wutu.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.talent_data.litpic
                        ,holdView.rencai_img , options);
            }else{
                holdView.rencai_img.setVisibility(View.GONE);
                holdView.wutu.setVisibility(View.VISIBLE);
                if(item.talent_data.username != null && !item.talent_data.username.equals("")){
                    String txt = item.talent_data.username.substring(0,1);
                    holdView.wutu.setText(txt);
                }

            }
            holdView.rencai_title.setText(item.talent_data.username);
            holdView.rencai_lingyu.setText(item.talent_data.unit);
            holdView.rank.setText(item.talent_data.rank);
            holdView.zhuanjialay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewRenCaiTail.class);
                    intent.putExtra("aid", item.talent_data.id);
                    context.startActivity(intent);
                }
            });
            holdView.yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                    if(loginState.equals("1")){
                        Intent intent = new Intent(context, TianXuQiu.class);
                        intent.putExtra("aid", item.talent_data.id);
                        intent.putExtra("typeid", "4");
                        context.startActivity(intent);
                    } else{
                        Intent intent = new Intent(context, MyloginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });

        }else if(item.typename.equals("project_list")){
            holdView.dixian.setVisibility(View.GONE);
            holdView.title.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.VISIBLE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.GONE);
            holdView.viewpager.setAdapter(xianguAdapter);
        }else if(item.typename.equals("laboratory_list")){
            holdView.dixian.setVisibility(View.GONE);
            holdView.title.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.GONE);
            holdView.kexueyuan.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.lab_data.litpic
                    ,holdView.img , options);
            holdView.kexueyuanname.setText(item.lab_data.title);
            if(item.lab_data.tags_list != null && item.lab_data.tags_list.size()>0){
                for(int i=0;i<item.lab_data.tags_list.size();i++){
                    String p = item.lab_data.tags_list.get(i);
                    if(i==0){
                        holdView.txt1.setVisibility(View.VISIBLE);
                        holdView.txt1.setText(p);
                    }else if(i==1){
                        holdView.txt2.setVisibility(View.VISIBLE);
                        holdView.txt2.setText(p);
                    }else if(i==2){
                        holdView.txt3.setVisibility(View.VISIBLE);
                        holdView.txt3.setText(p);
                    }else if(i==3){
                        holdView.txt4.setVisibility(View.VISIBLE);
                        holdView.txt4.setText(p);
                    }else if(i==4){
                        holdView.txt5.setVisibility(View.VISIBLE);
                        holdView.txt5.setText(p);
                    }
                }
            }
            holdView.waiwencontent.setText(item.lab_data.english_name);
            holdView.bumancontent.setText(item.lab_data.typename);
            holdView.adresscontent.setText(item.lab_data.address);
            holdView.adresslay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AdressBaiduMap.class);
                    intent.putExtra("key", item.lab_data.address);
                    context.startActivity(intent);
                }
            });
            holdView.zhuanjiacount.setText(item.lab_data.talent_count);
            holdView.xiangmucount.setText(item.lab_data.project_count);
            holdView.shiyanshicount.setText(item.lab_data.laboratory_count);
            holdView.lianxyansuo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TianXuQiu.class);
                    intent.putExtra("typeid", "0");
                    context.startActivity(intent);
                }
            });
            holdView.jigou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.lab_data.id);
                    context.startActivity(intent);
                }
            });
            holdView.zhuguanbumen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.lab_data.id);
                    context.startActivity(intent);
                }
            });
            holdView.waiwenming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",item.lab_data.id);
                    context.startActivity(intent);
                }
            });


        }else if(item.typename.equals("quick_link_list")){
            holdView.dixian.setVisibility(View.GONE);
            holdView.title.setVisibility(View.GONE);
//            holdView.kkk.setVisibility(View.GONE);
            holdView.zhuanjialay.setVisibility(View.GONE);
            holdView.chakangengduo.setVisibility(View.GONE);
            holdView.viewPagerContainer.setVisibility(View.GONE);
            holdView.tuijian.setVisibility(View.VISIBLE);
            holdView.kexueyuan.setVisibility(View.GONE);
            holdView.viewpager.setAdapter(xianguAdapter);
            holdView.title1.setText(item.quick_data.quick_title);
            holdView.title2.setText(item.quick_data.quick_content);
        }
        return convertView;
    }
    class HoldView{
        TextView title;
        ConstraintLayout kkk;
        WordCloudView wcv;
        RelativeLayout zhuanjialay,zhuanjia1;
        TextView wutu,rencai_title,rencai_lingyu,rank;
        RoundImageView rencai_img;
        LinearLayout yuyue;
        LinearLayout chakangengduo,adresslay;
        RelativeLayout viewPagerContainer;
        ViewPager viewpager;
        RelativeLayout tuijian;
        TextView title1,title2;
        LinearLayout kexueyuan,zhuguanbumen,waiwenming;
        RelativeLayout jigou;
        ImageView img;
        TextView kexueyuanname,txt1,txt2,txt3,txt4,txt5,waiwen,waiwencontent,buman,bumancontent,adress,adresscontent,zhuanjia,chakangengduotext;
        TextView zhuanjiacount,xiangmu,xiangmucount,shiyanshi,shiyanshicount,shebei,shebeicount;
        RelativeLayout lianxyansuo,dixian;
    }

    class XianguAdapter  extends PagerAdapter {
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();
        @Override
        public int getCount() {
            if(project_data.size() == 1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                HoldView mHolder = null;
                int index =0;
                if(project_data.size() == 0){
                    index = 0;
                }else{
                    index = position % project_data.size();
                }
                final Project_Data item  =project_data.get(index);
                if(mCaches.size() == 0){
                    converView = View.inflate(context,R.layout.newquanguoquanguo, null);
                    mHolder = new HoldView();
                    mHolder.imageView = (ZQImageViewRoundOval) converView.findViewById(R.id.item);
                    mHolder.imageView.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    mHolder.imageView.setRoundRadius(25);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (HoldView)converView.getTag();
                }
                ImageLoader.getInstance().displayImage(item.litpic
                        ,mHolder.imageView , options);
                mHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler(item);
//                        handlernewclick(ads);
                    }

                });


                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        //baotoudevice
        class HoldView{
            public ZQImageViewRoundOval imageView;

        }
        private void handler(Project_Data data){
            Intent intent=new Intent(context, ActiveActivity.class);
            intent.putExtra("title", data.title);
            intent.putExtra("url", data.url);
            context.startActivity(intent);
        }
    }
}
