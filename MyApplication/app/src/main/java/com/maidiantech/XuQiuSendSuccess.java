package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

/**
 * Created by lizisong on 2017/7/11.
 */

public class XuQiuSendSuccess extends AutoLayoutActivity {
    ImageView back;
    TextView xixin, goback;
    String typeid = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuqiusendsuccess);
        back = (ImageView)findViewById(R.id.shezhi_backs);
        xixin = (TextView)findViewById(R.id.xixin);
        goback = (TextView)findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        xixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XuQiuSendSuccess.this, UnitActivity.class);
                startActivity(intent);
            }
        });
        typeid = getIntent().getStringExtra("typeid");
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                YuyueRenCai.isFinish = true;
//                AppointmentSpecialist.isFinish = true;
//                ShopAppointment.isFinish = true;
//                DetailsActivity.is_finish = true;
//                ProJect.is_finish = true;
//                EquipmentActivity.is_finish =true;
//                PersonActivity.is_finish = true;
//                Intent intent = new Intent();
//                intent.setAction("action_toxuqiu");
//                sendBroadcast(intent);

//                if(typeid.equals("0")){
//                    Intent intent = new Intent(XuQiuSendSuccess.this, MyXuqiuActivity.class);
//                    intent.putExtra("type","-1");
//                    startActivity(intent);
//                }else if(typeid.equals("2")){
//                    Intent intent = new Intent(XuQiuSendSuccess.this, MyXuqiuActivity.class);
//                    intent.putExtra("type",typeid);
//                    startActivity(intent);
//                }else if(typeid.equals("4")){
//                    Intent intent = new Intent(XuQiuSendSuccess.this, MyXuqiuActivity.class);
//                    intent.putExtra("type",typeid);
//                    startActivity(intent);
//                }else if(typeid.equals("7")){
//                    Intent intent = new Intent(XuQiuSendSuccess.this, MyXuqiuActivity.class);
//                    intent.putExtra("type",typeid);
//                    startActivity(intent);
//                }

                finish();
            }
        });

    }
}
