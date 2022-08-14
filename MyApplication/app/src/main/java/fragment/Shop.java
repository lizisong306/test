package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maidiantech.R;
import com.maidiantech.SearchActivity;
import com.maidiantech.ShopAppointment;
import com.maidiantech.WebViewActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by 13520 on 2017/3/20.
 */

public class Shop extends BaseFragment  {
    private View view;
    LinearLayout rencai_fuwu,jishu_fuwu,shebei_fuwu,zhishi_fuwu,ziyuan_fuwu,zhuanli_fuwu;
    ImageView back,banner_top,banner_bottom;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.shop_activity, null);
            //TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
        }
        rencai_fuwu = (LinearLayout)view.findViewById(R.id.rencai_fuwu);
        jishu_fuwu  = (LinearLayout)view.findViewById(R.id.jishu_fuwu);
        shebei_fuwu = (LinearLayout)view.findViewById(R.id.shebei_fuwu);
        zhishi_fuwu = (LinearLayout)view.findViewById(R.id.zhishi_fuwu);
        ziyuan_fuwu = (LinearLayout)view.findViewById(R.id.ziyuan_fuwu);
        zhuanli_fuwu = (LinearLayout)view.findViewById(R.id.zhuanli_fuwu);

        back = (ImageView)view.findViewById(R.id.back);
        banner_top = (ImageView)view.findViewById(R.id.banner_top);
        banner_bottom = (ImageView)view.findViewById(R.id.banner_bottom);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });

        banner_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "企业成长伙伴");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/banner.html");
                startActivity(intent);
            }
        });
        banner_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/an-service-mall/coupon.html
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "创新劵");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/coupon.html");
                startActivity(intent);
            }
        });

        rencai_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/service-mall/talent-service.html
//                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra("title", "人才服务");
//                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/talent-service.html");
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), ShopAppointment.class);
                startActivity(intent);

            }
        });

        jishu_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/an-service-mall/jishu-service.html
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "技术服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/jishu-service.html");
                startActivity(intent);
            }
        });
        shebei_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("title", "设备服务");
//                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/shebei-service.html");
                startActivity(intent);
            }
        });

        zhishi_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "知识服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/zhishi-service.html");
                startActivity(intent);
            }
        });
        ziyuan_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "资源服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/ziyuan-service.html");
                startActivity(intent);
            }
        });
        zhuanli_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "专利服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/zhuanli-service.html");
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    protected void lazyLoad() {

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("商城"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("商城");
    }
}
