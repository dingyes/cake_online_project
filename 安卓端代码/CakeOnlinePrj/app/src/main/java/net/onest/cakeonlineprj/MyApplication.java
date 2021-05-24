package net.onest.cakeonlineprj;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化推送服务
        JPushInterface.init(this);
        // 设置允许调试模式
        JPushInterface.setDebugMode(true);
    }
}
