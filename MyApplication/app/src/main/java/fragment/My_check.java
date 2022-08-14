package fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maidiantech.BackHandledFragment;
import com.maidiantech.MainActivity;
import com.maidiantech.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import entity.checks;


/**
 * Created by 13520 on 2016/8/22.
 */
public class My_check extends BackHandledFragment {
    private View view;
    private MainActivity mac;
    private MainActivity shows;
    private MyitemAdapter adapeter;
    private List<checks> list=new ArrayList<>();
    private String [] ElectronicArray={"软件产品","微电子技术","计算机及网络产品","广播电视技术产品","新型电子元器件","信息安全产品","智能交通产品","光机电一体化"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.item, null);
        mac = (MainActivity) getActivity();
        shows = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button submit = (Button) view.findViewById(R.id.submit);
        ImageView backs = (ImageView) view.findViewById(R.id.backs);
         ImageView my_check=(ImageView) view.findViewById(R.id.my_check);
        my_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTitleDialog();
            }
        });
        ListView item_view = (ListView) view.findViewById(R.id.item_view);
        list.add(new checks(R.mipmap.jia,"软件产品"));
        list.add(new checks(R.mipmap.jia,"微电子技术"));
        list.add(new checks(R.mipmap.jia,"计算机及网络产品"));
        list.add(new checks(R.mipmap.jia,"广播电视技术产品"));
        list.add(new checks(R.mipmap.jia,"新型电子元器件"));
        list.add(new checks(R.mipmap.jia,"信息安全产品"));
        list.add(new checks(R.mipmap.jia,"智能交通产品"));
        list.add(new checks(R.mipmap.jia,"光机电一体化"));


        adapeter = new MyitemAdapter();
        item_view.setAdapter(adapeter);

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shows.show1();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mac.hide1();
            }
        });


    }

    class MyitemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.myitem, null);
            }
            TextView my_tv=(TextView) convertView.findViewById(R.id.my_tv);
            my_tv.setText(list.get(position).getName());

            CheckBox my_cb=(CheckBox) convertView.findViewById(R.id.my_cb);


            return convertView;
        }
    }
    private void inputTitleDialog() {

        final EditText inputServer = new EditText(getActivity());



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("输入框").setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        list.add(new checks(R.mipmap.jia,inputName));
                        adapeter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), inputName, Toast.LENGTH_SHORT).show();

                    }
                });
        builder.show();
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
