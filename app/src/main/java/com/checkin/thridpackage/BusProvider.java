package com.checkin.thridpackage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangjiaying on 2017/8/30.
 * 该类 实现 初始化
 */

public final class BusProvider {
    public static EventBus getInstance() {
        return EventBus.getDefault();
    }

    public static void register(Object subscriber) {
        if (subscriber == null) return;
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unRegister(Object subscriber) {
        if (subscriber == null) return;
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 封装之后 有类型限制，参数必须继承 BaseBean
     * @param busEvent
     */
    public static void postEvent(Object busEvent) {
       if(busEvent == null) return;
        getInstance().post(busEvent);
    }

    /**
     * 发送粘性事件，参数必须继承 BaseBean
     * @param busEvent
     */
    public static void postStickyEvent(Object busEvent) {
        if(busEvent == null) return;
        getInstance().postSticky(busEvent);
    }


    private BusProvider() {
        // No instances.
    }
}
