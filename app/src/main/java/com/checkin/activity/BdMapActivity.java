package com.checkin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.checkin.R;
import com.checkin.base.BaseActivity;
import com.checkin.utils.LocationUtils;

import java.util.List;

/**
 * Created by zhangjiaying on 2017/9/19.
 */

public class BdMapActivity extends BaseActivity implements BDLocationListener,AMapLocationListener,View.OnClickListener{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    private TextView address;
    public String nameAddr = "";
    public LocationUtils locationUtils;
    private Button btn_restart;
    private Button btn_stop;
    private Button btn_baidu;
    private Button btn_gaode;
    private int locationTool = LocationUtils.LOCATION_BAIDU;//默认是 百度定位
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 01:
                    String str = (String)msg.obj;
                    address.setText(str);
                    break;
                case 02:
                    String gaodestr = (String)msg.obj;
                    address.setText(gaodestr);
                    break;
            }
        }
    };

    @Override
    public int getInflateLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        address = (TextView) findViewById(R.id.tv_address);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_gaode = (Button) findViewById(R.id.btn_gaode);
        btn_baidu = (Button) findViewById(R.id.btn_baidu);
        btn_restart.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_baidu.setOnClickListener(this);
        btn_gaode.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        startLoc(5000,locationTool,1);

    }

    private void startLoc(int time, int tool, int type){
        locationUtils =  new LocationUtils.Builder(BdMapActivity.this).setBaiDuLocationLister(this)
                .setLocationTime(time).setLocationTools(tool).setLocationType(type).build();
    }

    /**
     * 定位SDK监听函数
     */

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        List<Poi> poiList =  location.getPoiList();
        if(poiList != null && poiList.size() > 0){
            nameAddr =  poiList.get(0).getName();
        }
        Message message =  Message.obtain();
        message.obj = nameAddr+"\n"+location.getAddrStr();
        message.what = 01;
        handler.sendMessage(message);

        System.out.println(location.getAddrStr());

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        System.out.println("高德"+aMapLocation.getAddress());
        System.out.println("高德"+aMapLocation.getPoiName());

        Message message =  Message.obtain();
        message.obj = aMapLocation.getPoiName()+"\n"+aMapLocation.getAddress();
        message.what = 02;
        handler.sendMessage(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_restart:
                locationUtils.restartLocation();
                break;
            case R.id.btn_stop:
                locationUtils.stopLocation();
                break;
            case R.id.btn_baidu:
                locationUtils.stopLocation();
                if(locationUtils != null && locationTool == LocationUtils.LOCATION_BAIDU ){
                    locationUtils.restartLocation();
                }else if(locationUtils != null && locationTool == LocationUtils.LOCATION_GAODE ){
                    locationTool =LocationUtils.LOCATION_BAIDU;
                    startLoc(5000,locationTool,1);
                }
                break;
            case R.id.btn_gaode:
                locationUtils.stopLocation();
                Intent intent = new Intent(BdMapActivity.this,GaoMapActivity.class);
                startActivity(intent);
//                if(locationUtils != null && locationTool == LocationUtils.LOCATION_BAIDU ){
//                    locationTool = LocationUtils.LOCATION_GAODE;
//                    startLoc(5000,locationTool,1);
//                }else if(locationUtils != null && locationTool == LocationUtils.LOCATION_GAODE ){
//                    locationUtils.restartLocation();
//                }
                break;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        // 退出时销毁定位
        locationUtils.stopLocation();
        // 关闭定位图层
        switch (locationTool){
            case 1:
                mBaiduMap.setMyLocationEnabled(false);
                mMapView.onDestroy();
                mMapView = null;
                break;
            case 2:
                //TODO  处理高德地图 view
                break;
        }

        super.onDestroy();
    }



}

