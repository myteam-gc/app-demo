package com.hnhy.framework.system;

import android.os.Build;

import com.hnhy.framework.Engine;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.util.FrameworkUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序错误处理系统
 *
 * @author FXTV-Android
 */
public class SystemCrash extends BaseSystem {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private String mBaseMessage;

    @Override
    protected void init() {
        mBaseMessage = getBaseMessage();

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                if (Engine.getInstance().mConfiguration.isEnableSaveCrashLog()) {
                    String writeStr = getWriteStr(mBaseMessage, ex);
                    String logFilePath = Engine.getInstance().mConfiguration.getLogNativeDir() + "crash.log";
                    FrameworkUtils.Files.writeFile(logFilePath, writeStr, true, true);
                }

                if (Engine.getInstance().mConfiguration.isEnableCrashReset()) {
                    // TODO: 2018/5/17 崩溃重启
                } else {
                    mDefaultHandler.uncaughtException(thread, ex);
                }
            }
        });
    }

    @Override
    protected void destroy() {
        mBaseMessage = null;
        if (mDefaultHandler != null) {
            mDefaultHandler = null;
        }
    }

    private String getWriteStr(String baseMessage, Throwable ex) {
        return baseMessage + getExceptionInfo(ex);
    }

    private String getBaseMessage() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : getBaseMessageMap().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key).append(" = ").append(value).append("\n");
        }
        return builder.toString();
    }

    private HashMap<String, String> getBaseMessageMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("versionName", FrameworkUtils.App.getVersionName());
        map.put("versionCode", "" + FrameworkUtils.App.getVersionCode());
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        return map;
    }

    private String getExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        return mStringWriter.toString();
    }
}
