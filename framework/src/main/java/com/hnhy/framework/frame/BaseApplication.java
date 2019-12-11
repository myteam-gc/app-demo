package com.hnhy.framework.frame;

import android.app.Application;

import tech.linjiang.pandora.Pandora;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:25
 * Description:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Pandora.init(this);
    }
}
