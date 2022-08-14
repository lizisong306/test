package com.maidiantech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.BaimaiAdapter;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.bmrank;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2016/12/12.
 */

public class PulseActivity extends Activity {
    private GridView bamai_listview;
    List<bmrank> bmlist;
    BaimaiAdapter bmadapter;
    List<bmrank> lists ;
    ImageView imageView;
    public static int posdata = 0;
    PulseCondtion mPulseCondtion;
    ArrayList<PulseData> pulseDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.from_buttom_in, R.anim.no_alpha);
        setContentView(R.layout.my_industry);
        StyleUtils1.initSystemBar(this);
        StyleUtils1.setResColorStyle(this, R.color.white);
        init();
        TextView pulse_title=(TextView) findViewById(R.id.pulse_title);
        if(Build.MODEL.equals("SM-G9287")){
            pulse_title.setTextSize(20);
        }
        Window window=getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha=0.9f;
        window.setAttributes(wl);
        mPulseCondtion = PulseCondtion.getInstance(PulseActivity.this);
        pulseDatas = mPulseCondtion.get();

    }
    /**
     *
     * 初始化界面
     */
    public void init(){
        bmlist = new ArrayList<>();
        bmlist.add(new bmrank(R.mipmap.img_dianzixinxi,"电子信息",500));
        bmlist.add(new bmrank(R.mipmap.img_xincailiao,"新材料",1000));
        bmlist.add(new bmrank(R.mipmap.img_jienenghuanbao,"节能环保",3000));

        bmlist.add(new bmrank(R.mipmap.img_xinnengyuan,"新能源",2500));
        bmlist.add(new bmrank(R.mipmap.img_xianjinzhizao,"先进制造",2000));
        bmlist.add(new bmrank(R.mipmap.img_shengwujishu,"生物技术",1500));

        bmlist.add(new bmrank(R.mipmap.img_huaxuehuagong,"化学化工",4000));
        bmlist.add(new bmrank(R.mipmap.img_wenhuachuangyi,"文化创意",3500));
        bmlist.add(new bmrank(R.mipmap.img_qita,"其他",4500));

        imageView = (ImageView)findViewById(R.id.bottmon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RotateAnimation animation =new RotateAnimation(360f,0f, Animation.RELATIVE_TO_SELF,
                                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        animation.setDuration(500);
//                    animation.setRepeatCount(1);
                        animation.setFillAfter(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                imageView.setBackgroundResource(R.mipmap.tab_button_add);
                                finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                imageView.startAnimation(animation);
            }
        });
        bamai_listview = (GridView) findViewById(R.id.bamai_listview);
        bamai_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        bmadapter = new BaimaiAdapter(this, bmlist,pulseHandler);
        bamai_listview.setAdapter(bmadapter);

        bamai_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bmrank item = bmlist.get(position);
                lists = new ArrayList<bmrank>();
                lists.add(item);
                String idpos=null;
                String name ="";
                Intent intent =new Intent(PulseActivity.this,NewResultActivity.class);
                ResultActivity.evaluetop = item.getEavule();
                if(pulseDatas != null){
                    for(int i=0;i<pulseDatas.size();i++){
                        PulseData pos = pulseDatas.get(i);
                        if(pos.evaluetop.equals(ResultActivity.evaluetop+"")){
                            idpos = pos.pid;
                            name = pos.name;
                            break;
                        }
                    }
                }
//                Bundle b=new Bundle();
//                b.putString("title", item.getName());
                intent.putExtra("evaluetop",ResultActivity.evaluetop+"");
                if(idpos != null){
                    Log.d("lizisong","chuanid:"+idpos);
                    intent.putExtra("id",idpos);
                    intent.putExtra("title", name);
                }
//                b.putSerializable("newbalist", (Serializable) lists);
//                intent.putExtras(b);

                startActivity(intent);
                finish();
            }
        });
    }

    public void onResume() {
        super.onResume();
        if(posdata == 1){
            posdata = 0;
            finish();
        }
        if(bmadapter != null){
            bmadapter.clernMap();
        }
        MobclickAgent.onPageStart("感兴趣领域"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("感兴趣领域"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
    Handler pulseHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    @Override
    public void finish() {
        overridePendingTransition(R.anim.to_buttom_out, R.anim.no_alpha);
        super.finish();

    }
}
