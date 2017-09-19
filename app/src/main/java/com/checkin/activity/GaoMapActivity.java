package com.checkin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.checkin.R;
import com.checkin.utils.LocationUtils;

/**
 * Created by Administrator on 2016/2/4.
 */
public class GaoMapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener,View.OnClickListener {
    private View view;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private int locationTool = LocationUtils.LOCATION_GAODE;//默认是 百度定位
    private LocationUtils locationUtils;

    private Button btn_restart;
    private Button btn_stop;
    private Button btn_baidu;
    private Button btn_gaode;
    private TextView address;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 02:
                    String gaodestr = (String)msg.obj;
                    address.setText(gaodestr);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdmap);
        mapView = (MapView)findViewById(R.id.map);

        address = (TextView) findViewById(R.id.tv_address);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_gaode = (Button) findViewById(R.id.btn_gaode);
        btn_baidu = (Button) findViewById(R.id.btn_baidu);
        btn_restart.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_baidu.setOnClickListener(this);
        btn_gaode.setOnClickListener(this);

        mapView.onCreate(savedInstanceState);// 此方法必须重写

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

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
                Intent intent = new Intent(GaoMapActivity.this,BdMapActivity.class);
                startActivity(intent);
//                if(locationUtils != null && locationTool == LocationUtils.LOCATION_GAODE ){
//                    locationTool =LocationUtils.LOCATION_BAIDU;
//                    startLoc(5000,locationTool,1);
//                }
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        locationUtils.stopLocation();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                System.out.println("高德"+aMapLocation.getAddress());
                System.out.println("高德"+aMapLocation.getPoiName());

                Message message =  Message.obtain();
                message.obj = aMapLocation.getPoiName()+"\n"+aMapLocation.getAddress();
                message.what = 02;
                handler.sendMessage(message);

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void startLoc(int time,int tool,int type){
          locationUtils =  new LocationUtils.Builder(GaoMapActivity.this).setGaoDeLocationLister(this)
                .setLocationTime(time).setLocationTools(tool).setLocationType(type).build();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        locationTool = LocationUtils.LOCATION_GAODE;
        startLoc(2000,locationTool,1);
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
         locationUtils.stopLocation();
    }

}
