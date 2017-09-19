package com.checkin.utils;

import android.content.Context;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;

/**
 * Created by 坐标转换 on 2016/2/18.
 * 处理 坐标转换 的方案
 * 1.在服务器 和 手机本地 设置 2套坐标
 * 2.设置统一 成 百度坐标，在操作和显示的时候 需要实时 转换。
 *
 * 如果选择 方案1，会遇到  同一个客户 显示 不同地址的 情况。
 * 选择 方案2  会遇到 性能上面的 消耗
 *
 *
 * 3.如果 定位不准  可以通过 坐标转换 来
 */
public class CoordinateSwitch {
    private static  CoordinateSwitch coordinateSwitch;
    private Context mContext;

    private CoordinateSwitch(Context context){
        this.mContext = context.getApplicationContext();
    }
    public CoordinateSwitch getInstance(Context context){
        if(coordinateSwitch == null){
             coordinateSwitch = new CoordinateSwitch(context);
        }
        return  coordinateSwitch;
    }

    //支持GPS/Mapbar/Baidu等多种类型坐标在高德地图上使用
    public void godeSwitch(DPoint sourceLatLng){
        CoordinateConverter converter  = new CoordinateConverter(mContext);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.BAIDU);
        // sourceLatLng待转换坐标点 DPoint类型
        try{
            converter.coord(sourceLatLng);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            desLatLng.getLatitude();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    /// <summary>
/// 中国正常坐标系GCJ02协议的坐标，转到 百度地图对应的 BD09 协议坐标
/// </summary>
/// <param name="lat">维度</param>
/// <param name="lng">经度</param>
    public static void Convert_GCJ02_To_BD09(double lat,double lng)
    {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta) + 0.0065;
        lat = z * Math.sin(theta) + 0.006;
    }
    /// <summary>
/// 百度地图对应的 BD09 协议坐标，转到 中国正常坐标系GCJ02协议的坐标
/// </summary>
/// <param name="lat">维度</param>
/// <param name="lng">经度</param>
    public static void Convert_BD09_To_GCJ02(double lat, double lng)
    {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta);
        lat = z * Math.sin(theta);
    }

}
