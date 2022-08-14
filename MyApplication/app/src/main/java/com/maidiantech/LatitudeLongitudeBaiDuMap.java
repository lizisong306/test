package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;

import view.StyleUtils;
import view.StyleUtils1;

import static com.maidiantech.R.id.map;

/**
 * Created by lizisong on 2017/9/12.
 */

public class LatitudeLongitudeBaiDuMap extends FragmentActivity {
    private BaiduMap mBaiduMap = null;
    private ImageView  back = null;
    private boolean mEnableCustomStyle = true;
    SupportMapFragment map;

    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latitudelongitudebaidumap);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        back = (ImageView)findViewById(R.id.about_backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.map))).getBaiduMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 18.0f; // 默认 11级
        Intent intent = getIntent();
        if (null != intent) {
            mEnableCustomStyle = intent.getBooleanExtra("customStyle", true);
            center = new LatLng(intent.getDoubleExtra("y", 39.915071),
                    intent.getDoubleExtra("x", 116.403907));
            zoom = intent.getFloatExtra("level", 11.0f);
        }
        builder.target(center).zoom(zoom);

////        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
//        mBaiduMap
//                .setMyLocationConfiguration(new MyLocationConfiguration(
//                        mCurrentMode, true, mCurrentMarker));
////        MapStatus.Builder builder1 = new MapStatus.Builder();
////        builder1.overlook(0);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//        Intent intent = getIntent();
//        MapStatus.Builder builder = new MapStatus.Builder();
//        if (intent.hasExtra("x") && intent.hasExtra("y")) {
//            // 当用intent参数时，设置中心点为指定点
//            Bundle b = intent.getExtras();
//            LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
//            builder.target(p);
//        }
//        builder.overlook(-20).zoom(15);
//        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(builder.build())
//                .compassEnabled(false).zoomControlsEnabled(false);
//        map = SupportMapFragment.newInstance(bo);
//        FragmentManager manager = getSupportFragmentManager();
//
//        manager.beginTransaction().add(R.id.map, map, "map_fragment").commit();

    }
}
