package fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.BackHandledFragment;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.NewRenCaiTailL;
import com.maidiantech.NewSearchContent;
import com.maidiantech.R;
import com.maidiantech.TianXuQiu;
import com.maidiantech.UnitedStatesDeilActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XinFanAnCeShi;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Util.NoDoubleClick;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import entity.KeyWord;
import entity.NewRenCaiDataEntity;
import view.RoundImageView;
import view.ScrollListView;
import view.ShapeImageView;

/**
 * Created by Administrator on 2019/8/6.
 */

public class RenCaiTailFragment extends BaseFragment {
    View view;
    ArrayList<NewRenCaiDataEntity> list;
    DisplayImageOptions options;
    List<KeyWord> keys ;
    Adapter  adapter = new Adapter();
    ListView scrollListView;
    public RenCaiTailFragment(ArrayList<NewRenCaiDataEntity> list, ArrayList<KeyWord> keys){
        this.list = list;
        this.keys = keys;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(view == null){
            view = inflater.inflate(R.layout.rencaitailfragment, null);
//        }
        options = ImageLoaderUtils.initOptions();
//        scrollListView = view.findViewById(R.id.custom);
////        scrollListView.setScrollEnable(true);
//        scrollListView.setAdapter(adapter);
        return view;
    }

    public void setSpecifiedTextsColor(TextView txtView, String text, KeyWord specifiedTexts, SpannableStringBuilder styledText){

        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.keyword.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do
        {
            start = temp.indexOf(specifiedTexts.keyword);
            if(start != -1){
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        }while(start != -1);


        for(Integer i : sTextsStartList){
            styledText.setSpan(
                    new ForegroundColorSpan(Color.parseColor(specifiedTexts.color)),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //这个一定要记得设置，不然点击不生效
            txtView.setMovementMethod(LinkMovementMethod.getInstance());
            styledText.setSpan(new TextClick(specifiedTexts),i,i + sTextLength , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    @Override
    protected void lazyLoad() {

    }

    class TextClick extends ClickableSpan {
        KeyWord mKeyWord;
        public TextClick(KeyWord item){
            mKeyWord = item;
        }

        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            if(mKeyWord.aid == null || (mKeyWord.aid != null && mKeyWord.aid.equals(""))){
                Intent intent = new Intent(getActivity(), NewSearchContent.class);
                intent.putExtra("hot",mKeyWord.keyword);
                intent.putExtra("typeid", mKeyWord.typeid);
                if(mKeyWord.typeid.equals("100")){
                    intent.putExtra("typeid", "");
                }else if(mKeyWord.typeid.equals("0")){
                    intent.putExtra("typeid", "");
                }
                startActivity(intent);
            }else {
                if(mKeyWord.typeid.equals("8")){
                    Intent intent=new Intent(getActivity(), UnitedStatesDeilActivity.class);
                    intent.putExtra("aid",mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("4")){
                    Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                    intent.putExtra("aid", mKeyWord.aid);
                    intent.putExtra("typeid", mKeyWord.typeid);
                    startActivity(intent);

                }else if(mKeyWord.typeid.equals("0")){
                    Intent intent = new Intent(getActivity(), NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", "");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), NewSearchContent.class);
                    intent.putExtra("hot",mKeyWord.keyword);
                    intent.putExtra("typeid", mKeyWord.keyword);
                    if(mKeyWord.typeid.equals("100")){
                        intent.putExtra("typeid", "");
                    }else if(mKeyWord.typeid.equals("0")){
                        intent.putExtra("typeid", "");
                    }
                    startActivity(intent);
                }
            }

        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor(mKeyWord.color));
            float size =  ds.getTextSize();
            ds.setTextSize(size);
            ds.setUnderlineText(true);
        }
    }


    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.d("lizisong", "getCount:"+list.size());
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("lizisong", "getCount:"+list.size());
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(getActivity(), R.layout.newrencaitailadapterl, null);
                holdView.title = convertView.findViewById(R.id.title);
                holdView.lines = convertView.findViewById(R.id.lines);
                holdView.tuwen = convertView.findViewById(R.id.tuwen);
                holdView.time  = convertView.findViewById(R.id.time);
                holdView.text  = convertView.findViewById(R.id.text);
                holdView.laycontent = convertView.findViewById(R.id.laycontent);
                holdView.imagecontent = convertView.findViewById(R.id.imagecontent);
                holdView.image = convertView.findViewById(R.id.image);
                holdView.xiangmu_lay = convertView.findViewById(R.id.xiangmu_lay);
                holdView.zhuanjia = convertView.findViewById(R.id.zhuanjia);
                holdView.xianmgmu = convertView.findViewById(R.id.xianmgmu);
                holdView.xmtitle = convertView.findViewById(R.id.xmtitle);
                holdView.lanyuan = convertView.findViewById(R.id.lanyuan);
                holdView.resource = convertView.findViewById(R.id.resource);
                holdView.yuyue = convertView.findViewById(R.id.yuyue);
                holdView.source = convertView.findViewById(R.id.source);
                holdView.chenggao = convertView.findViewById(R.id.chenggao);
                holdView.wutu = convertView.findViewById(R.id.wutu);
                holdView.titlelay = convertView.findViewById(R.id.titlelay);
                holdView.kongbai = convertView.findViewById(R.id.kongbai);
                holdView.rencai_title = convertView.findViewById(R.id.rencai_title);
                holdView.rencai_lingyu = convertView.findViewById(R.id.rencai_lingyu);
                holdView.rank = convertView.findViewById(R.id.rank);
                holdView.rencai_img = convertView.findViewById(R.id.rencai_img);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final NewRenCaiDataEntity item = list.get(position);
            if(item.type.equals("-4")){
                holdView.lines.setVisibility(View.VISIBLE);
                holdView.title.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
            }else if(item.type.equals("-1")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.VISIBLE);
                holdView.image.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
//                long sys = (System.currentTimeMillis()-Long.parseLong(item.createtime)*1000)/1000;
                String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.createtime)*1000), item.createtime);
                holdView.time.setText(time);
                if(item.content != null && !item.content.equals("")){
                    holdView.text.setVisibility(View.VISIBLE);
                    holdView.text.setText(item.content);
                }else{
                    holdView.text.setVisibility(View.GONE);
                }
                if(item.imgurl != null && !item.imgurl.equals("")){
                    holdView.imagecontent.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(item.imgurl
                            , holdView.imagecontent, options);
                }else{
                    holdView.imagecontent.setVisibility(View.GONE);
                }

            }else if(item.type.equals("0") || item.type.equals("4") || item.type.equals("3")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.VISIBLE);
                holdView.titlelay.setVisibility(View.VISIBLE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.title.setText(item.name);
            }else if(item.type.equals("401")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.VISIBLE);
                if(keys.size() > 0){
                    SpannableStringBuilder styledText = new SpannableStringBuilder(item.content);
                    for(int i=0; i<keys.size();i++){
                        KeyWord pos = keys.get(i);
                        setSpecifiedTextsColor(holdView.laycontent,item.content,pos,styledText);
                    }
                    holdView.laycontent.setText(styledText);

                }else{
                    holdView.laycontent.setText(item.content);
                }

            }else if(item.type.equals("402")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.image.setVisibility(View.VISIBLE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.content
                        , holdView.image, options);
            }else if(item.type.equals("-2")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                if(position == 0){
                    holdView.chenggao.setVisibility(View.VISIBLE);
                }else{
                    holdView.chenggao.setVisibility(View.GONE);
                }
                if(item.item.litpic == null || item.item.litpic.equals("")){
                    holdView.rencai_img.setVisibility(View.GONE);
                    holdView.wutu.setVisibility(View.VISIBLE);
                    if(item.item.title != null && !item.item.title.equals("")){
                        String txt = item.item.title.substring(0,1);
                        holdView.wutu.setText(txt);
                    }
                }else{
                    holdView.rencai_img.setVisibility(View.VISIBLE);
                    holdView.wutu.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.item.litpic
                            , holdView.rencai_img, options);
                }
                holdView.rencai_title.setText(item.item.title);
//                                       rencai_title.setTypeface(MyApplication.Medium);
                holdView.rencai_lingyu.setText(item.item.unit);
                holdView.rank.setText(item.item.ranks);
                holdView.zhuanjia.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        Intent intent = new Intent(getActivity(), NewRenCaiTail.class);
                        intent.putExtra("aid", item.item.aid);
                        startActivity(intent);
                    }
                });
                holdView.resource.setText(item.item.str);
                holdView.yuyue.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.item.aid);
                            intent.putExtra("typeid", "4");
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }else if(item.type.equals("-3")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.VISIBLE);
                holdView.zhuanjia.setVisibility(View.GONE);
                holdView.laycontent.setVisibility(View.GONE);
                if(item.item.litpic == null || item.item.litpic.equals("")){
                    holdView.xianmgmu.setImageResource(R.mipmap.information_placeholder);
                }else{
                    ImageLoader.getInstance().displayImage(item.item.litpic
                            , holdView.xianmgmu, options);
                }
                holdView.xmtitle.setText(item.item.title);
                holdView.lanyuan.setText(item.item.areacate);
                holdView.source.setText(item.item.labels);
                holdView.xiangmu_lay.setOnClickListener(new NoDoubleClick() {
                    @Override
                    public void Click(View v) {
                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                        intent.putExtra("aid", item.item.aid);
                        startActivity(intent);
                    }
                });
            }else if(item.type.equals("-5")){
                holdView.lines.setVisibility(View.GONE);
                holdView.title.setVisibility(View.GONE);
                holdView.titlelay.setVisibility(View.GONE);
                holdView.tuwen.setVisibility(View.GONE);
                holdView.image.setVisibility(View.GONE);
                holdView.kongbai.setVisibility(View.VISIBLE);
                holdView.laycontent.setVisibility(View.GONE);
                holdView.xiangmu_lay.setVisibility(View.GONE);
                holdView.zhuanjia.setVisibility(View.GONE);
            }
            return convertView;
        }
        class HoldView{
            TextView title,lines,kongbai,resource,yuyue,chenggao;
            RelativeLayout tuwen;
            TextView time,text,laycontent;
            ShapeImageView imagecontent,image;
            RelativeLayout xiangmu_lay,zhuanjia;
            ShapeImageView xianmgmu;
            TextView xmtitle,lanyuan,source,wutu,rencai_title,rencai_lingyu,rank;
            RoundImageView rencai_img;
            LinearLayout titlelay;
        }
    }

}
