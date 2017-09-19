package com.checkin.utils;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.checkin.dialog.iAlertDialog;

/**
 * 用build模式 给 location  设置参数  起到控制作用
 *TODO  设置超时，设置定位模式，坐标转换  和后台数据库统一
 *
 */
public class LocationUtils {
    private Context mContext;  //设置上下文
    private int locationTool; //设置 定位的方式  2高德 和 1百度
    private int locationType;  //定位模式
    private int scanTime; //如果是 连续定位  需要设定 时间间隔
    private BDLocationListener bdlocationListener;//百度定位监听
    private AMapLocationListener gdlocationListener;//高德定位监听


    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    /**
     * 定位模式：定位一次，无论成功与否，都会停止定位，最长耗时为TIME_OUT毫秒
     */
    public static final int MODE_ONCE_STOP = -1;
    /**
     * 定位模式：定位一次直到成功才停止定位，否则不会自动停止，除非自己手动停止
     */
    public static final int MODE_ONCE_SUCCESS = 1;
    /**
     * 定位模式：开启定位后，一直定位，不会自动停止定位
     */
    public static final int MODE_MORE_SUCCESS = 0;
    private static LocationUtils locationUtils;
    public static final int ACTION_STOP_BdLOCATE = 1;
    public static final int ACTION_STOP_GdLOCATE = 2;
    public static final int ACTION_STOP_TENXUN  = 3;
    public static final int LOCATION_BAIDU = 1;
    public static final int LOCATION_GAODE = 2;
    public static final int LOCATION_TENXUN = 3;


    public LocationUtils(Builder builder){
        this.mContext =builder.mContext;
        this.locationTool = builder.locationTool;
        this.locationType = builder.locationType;
        this.scanTime = builder.scanTime;
        this.bdlocationListener = builder.bdlocationListener;
        this.gdlocationListener = builder.gdlocationListener;
        startLoc();
    }

    /**
     * 开始定位s
     */
    public void startLoc(){
         if (locationTool == LOCATION_BAIDU) {//百度
             startBdLoaction(bdlocationListener);
          }else if(locationTool == LOCATION_GAODE){//高德
             startGdLocation(gdlocationListener);
          }
    }

    private void startBdLoaction(final BDLocationListener locationListener) {
        if (!ConnectionDetector.isConnectingToInternet(mContext)) {
            Toast.makeText(mContext,"网络不通", Toast.LENGTH_SHORT).show();
        }
        if (!isOPen(mContext)) {
            showAlertDialog(mContext);
        } else {
            // 定位初始化
            mLocClient = new LocationClient(mContext.getApplicationContext());
            mLocClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    locationListener.onReceiveLocation(bdLocation);
                }
            });
            LocationClientOption option = new LocationClientOption();
            option.disableCache(true);//禁止启用缓存定位
            option.setOpenGps(true);// 打开GPS
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setPriority(LocationClientOption.GpsFirst);
            // 设置网络优先(不设置，默认是gps优先)
            option.setIsNeedLocationPoiList(true);
            option.setIsNeedAddress(true);
// 返回的定位结果包含地址信息
            option.setAddrType("all");
            option.setScanSpan(scanTime);// 设置发起定位请求的间隔时间为5s(小于1秒则一次定位)
            mLocClient.setLocOption(option);
            mLocClient.start();
        }
    }

    private void startGdLocation(final AMapLocationListener locationListener){
        locationClient = new AMapLocationClient(mContext);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                locationListener.onLocationChanged(aMapLocation);
            }
        });
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(Long.valueOf(scanTime));
        //只有持续定位设置定位间隔才有效，单次定位无效
        locationOption.setOnceLocation(false);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 重新定位
     */
    public void restartLocation(){
        switch (locationTool){
            case LOCATION_BAIDU:
                if(mLocClient != null && !mLocClient.isStarted()){
                    mLocClient.start();
                    Log.e("百度重新定位","重新定位");
                }else{
                    startBdLoaction(bdlocationListener);
                }
                break;
            case LOCATION_GAODE:
                if(locationClient != null && !locationClient.isStarted()){
                    locationClient.startLocation();
                    Log.e("高德重新定位","重新定位");
                }else{
                    startGdLocation(gdlocationListener);
                }
                break;
        }
    }

    public void stopLocation(){
        switch (locationTool){
            case LOCATION_BAIDU:
                if(mLocClient  != null && mLocClient.isStarted()){
                    Log.e("百度停止定位","停止定位");
                    stopAndDestroyLocate();
                }
                break;
            case LOCATION_GAODE:
                if(locationClient != null && locationClient.isStarted()){
                    Log.e("高德停止定位","停止定位");
                    /**
                     * 如果AMapLocationClient是在当前Activity实例化的，
                     * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
                     */
                    locationClient.stopLocation();
                    locationClient.onDestroy();
                    locationClient = null;
                    locationOption = null;
                }
                break;
        }
    }

    public  void stopAndDestroyLocate() {
        if (mLocClient != null) {
            if (bdlocationListener != null) {
                mLocClient
                        .unRegisterLocationListener(bdlocationListener);
            }
            mLocClient.stop();
        }
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 展示定位弹出框
     *
     * @param context
     * @return
     */
    public static final void showAlertDialog(final Context context) {
        {
            iAlertDialog.showAlertDialog(context, "提示", "请在设置中允许确定您的位置。", "设置", "取消", new iAlertDialog.OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent checkInternetIntent = new Intent();
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        checkInternetIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    } else {
                        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.Settings");
                        checkInternetIntent.setComponent(cm);
                        checkInternetIntent.setAction("android.intent.action.VIEW");
                    }
                    context.startActivity(checkInternetIntent);
                }
            }, new iAlertDialog.OnClickNoListener() {
                @Override
                public void onClickNo() {
                    iAlertDialog.dismissDialog();
                }
            }, true);
        }
    }

    public static class Builder {
        private Context mContext;  //设置上下文
        private int locationTool; //设置 定位的方式  2高德 和 1百度
        private int locationType;  //定位模式
        private int scanTime; //如果是 连续定位  需要设定 时间间隔
        private BDLocationListener bdlocationListener;//百度定位监听
        private AMapLocationListener gdlocationListener;//高德定位监听

        public Builder(Context mContext) {
            this.mContext = mContext;
        }
        public Builder  setLocationTools(int tool){
            this.locationTool = tool;
            return this;
        }

        public Builder  setLocationType(int type){
            this.locationType = type;
            return this;
        }

        public Builder  setLocationTime(int time){
             this.scanTime = time;
            return this;
        }

        public Builder setBaiDuLocationLister(BDLocationListener locationListener){
            this.bdlocationListener = locationListener;
            return this;
        }
        public Builder setGaoDeLocationLister(AMapLocationListener locationListener){
            this.gdlocationListener = locationListener;
            return this;
        }

        public LocationUtils build(){
            return new LocationUtils(this);
        }
    }

}
