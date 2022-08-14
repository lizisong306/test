package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.maidiantech.BackHandledFragment;
import com.maidiantech.MainActivity;
import com.maidiantech.R;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by 13520 on 2016/8/19.
 */
public class My_industry extends BackHandledFragment {
    private View view;
    private MainActivity show;
    private MainActivity checks;
    private GridView bamai_listview;
    List<Integer> bmlist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_industry, null);
       // show= (MainActivity) getActivity();
       // checks= (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      /*  bmlist=new ArrayList<>();
        bmlist.add(R.mipmap.dianzixinxi);
        bmlist.add(R.mipmap.xincailiao);
        bmlist.add(R.mipmap.shengwujishu);
        bmlist.add(R.mipmap.jienenghuanbao);
        bmlist.add(R.mipmap.xianjinzhizao);
        bmlist.add(R.mipmap.wenhuachuangyi);
        bmlist.add(R.mipmap.huaxuehuagong);
        bmlist.add(R.mipmap.xinnengyuan);
        bmlist.add(R.mipmap.qita);
        bamai_listview =(GridView) view.findViewById(R.id.bamai_listview);
        BaimaiAdapter bmadapter=new BaimaiAdapter(getActivity(),bmlist);
        bamai_listview.setAdapter(bmadapter);*/
        /* ImageView industry_back=(ImageView) view.findViewById(R.id.industry_back);
        ImageView img=(ImageView) view.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checks.check();
            }
        });
        industry_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.show();
            }
        });*/
    }
    @Override
    protected boolean onBackPressed() {
        return false;
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
}
