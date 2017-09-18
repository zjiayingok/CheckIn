package com.checkin.busevent;

import android.os.Bundle;

/**
 * Created by zhangjiaying on 2017/8/30.
 * 注意如何区分 action
 *
 * 网络消息
 * 刷新UI
 *
 */

public class BaseBusEvent {
    public Bundle data;


    public BaseBusEvent(String action) {
        this.action = action;
    }

    public BaseBusEvent() {
    }

    private String action;
    private int type;
    private int intValue;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Bundle getBundleData() {
        return data;
    }

    public void setBundleData(Bundle data) {
        this.data = data;
    }
}
