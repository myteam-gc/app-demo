package com.hnhy.framework.system.download;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.hnhy.framework.logger.Logger;

public class ServiceDownload extends Service {
    private static final String TAG = "ServiceDownload";
    private IDownloadService.Stub mBinder;
    private IDownloadServiceCallback mServiceCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate");
        mBinder = new IDownloadService.Stub() {
            @Override
            public void registerCallback(IDownloadServiceCallback callback) {
                Logger.d(TAG, "registerCallback");
                mServiceCallback = callback;
            }

            @Override
            public void unRegisterCallback() {
                Logger.d(TAG, "unRegisterCallback");
                mServiceCallback = null;
            }

            @Override
            public void download(Task task) {
                Logger.d(TAG, String.format("download %s", task.toString()));
                Downloader.getInstance().download(task);
            }

            @Override
            public void pause(int tag) {
                Logger.d(TAG, String.format("pause,tag=%s", tag));
                Downloader.getInstance().pause(tag);
            }

            @Override
            public void cancel(int tag) {
                Logger.d(TAG, String.format("cancel,tag=%s", tag));
                Downloader.getInstance().cancel(tag);
            }
        };

        Downloader.getInstance().setCallback(new Downloader.Callback() {
            @Override
            public void onCallback(Task task) {
                try {
                    mServiceCallback.onCallback(task);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Logger.e(TAG, "callback error,msg=" + e.getMessage());
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Logger.d(TAG, "bindService");
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Logger.d(TAG, "unbindService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
}
