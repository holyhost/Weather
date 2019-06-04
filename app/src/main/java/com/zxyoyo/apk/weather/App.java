package com.zxyoyo.apk.weather;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhouyou.http.EasyHttp;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图
        SDKInitializer.initialize(this);
        EasyHttp.init(this);//默认初始化
    }
}
