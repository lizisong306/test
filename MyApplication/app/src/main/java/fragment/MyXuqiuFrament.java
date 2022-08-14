package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maidiantech.AddXuQiu;
import com.maidiantech.BackHandledFragment;
import com.maidiantech.MyloginActivity;
import com.maidiantech.R;
import com.maidiantech.XuQiuShow;
import com.maidiantech.XuQiuXiangqQing;
import java.util.List;

import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import adapter.XuqiuAdapter;
import application.MyApplication;
import entity.Codes;
import entity.XuQiudetails;
import entity.XuQiudetailsdata;
import view.ProgressWebView;
import view.RefreshListView;

/**
 * Created by lizisong on 2017/5/17.
 */

public class MyXuqiuFrament extends BackHandledFragment {
    ImageView back;
    TextView title;
    String  titletxt;
    ImageView need_add;
    View view;
    String mid ="";
    RelativeLayout welcome,datalist;
    RefreshListView listview;
    Button add_need;
    XuqiuAdapter adapter;
    private OkHttpUtils Okhttp;
    private String jsons;
    private ProgressBar progress;
    private  String   ips;
    List<XuQiudetailsdata> postsListData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.xuqiuframent, null);
        }
        titletxt = "我的需求";
//        String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");

        back = (ImageView)view.findViewById(R.id.about_backs);
        title = (TextView)view.findViewById(R.id.titlecontent);
        need_add=(ImageView) view.findViewById(R.id.need_add);
        welcome = (RelativeLayout)view.findViewById(R.id.welcome);
        datalist = (RelativeLayout) view.findViewById(R.id.datalist);
        listview = (RefreshListView)view.findViewById(R.id.listview);
        add_need = (Button)view.findViewById(R.id.add_need);
        add_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddXuQiu.class);
                getActivity().startActivity(intent);
            }
        });
        progress = (ProgressBar)view.findViewById(R.id.progress);
        title.setText(titletxt);

        need_add.setVisibility(View.VISIBLE);
        need_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), AddXuQiu.class);
                 getActivity().startActivity(intent);
                }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//            updatedata();
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            getjsons();
        }
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    public void getjsons(){
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mids = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                    jsons = Okhttp.loaudstringfromurl("http://www.maidiantech.com/api/require_new.php?mid="+mids+"&method=list");
                    if(jsons != null){
                        Message msg = new Message();
                        msg.what =1;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){

                }

            }
        }).start();

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    progress.setVisibility(View.GONE);
                    Gson gs = new Gson();
                    XuQiudetails data = gs.fromJson(jsons, XuQiudetails.class);
                    postsListData = data.data;
                    if(postsListData != null){
                        if (adapter == null) {
                            adapter = new XuqiuAdapter(getActivity(), postsListData);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else {
                            adapter.setData(postsListData);
                            adapter.notifyDataSetChanged();
                        }
                        if(postsListData.size() >0){
                            datalist.setVisibility(View.VISIBLE);
                            welcome.setVisibility(View.GONE);
                        }else{
                            datalist.setVisibility(View.GONE);
                            welcome.setVisibility(View.VISIBLE);
                        }
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), XuQiuXiangqQing.class);
                                intent.putExtra("id",postsListData.get(position-1).id );
                                startActivity(intent);
                            }
                        });
                    }else {
                        datalist.setVisibility(View.GONE);
                        welcome.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){

                }

            }

        }
    };

}
