package com.hnhy.framework.logger;

import android.util.Log;

import com.hnhy.framework.Engine;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:47
 * Description:
 */

public class Logger {

    public static void i(String tag, String msg) {
        if (Engine.getInstance().mConfiguration.isEnableLog()) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Engine.getInstance().mConfiguration.isEnableLog()) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Engine.getInstance().mConfiguration.isEnableLog()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Engine.getInstance().mConfiguration.isEnableLog()) {
            Log.e(tag, msg);
        }
    }
}
