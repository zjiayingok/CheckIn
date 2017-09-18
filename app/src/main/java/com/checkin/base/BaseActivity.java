package com.checkin.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.checkin.thridpackage.BusProvider;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by zhangjiaying on 2017/9/18.
 */

public abstract class BaseActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getInflateLayout();
        if (layoutId > 0) {
            setContentView(View.inflate(this, layoutId, null));
        }
        ButterKnife.bind(this);
        initView();
        registerComponent();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterComponent();
    }

    /**
     * 加载布局
     */
    public abstract int getInflateLayout();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * eventBus 注册
     * 子类可实现 该方法
     */
    public void registerComponent() {
        BusProvider.register(this);
    }

    /**
     * eventBus 注销
     * 子类可实现 该方法
     */
    public void unRegisterComponent(){
        BusProvider.unRegister(this);
    }

    @Subscribe
    public void baseMethod(Object object){}

    /**
     * 跳转到设置界面
     * @return
     */
    protected Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }
}
