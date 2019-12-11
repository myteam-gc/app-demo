package com.hnhy.framework.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hnhy.framework.Engine;
import com.hnhy.framework.IPushService;
import com.hnhy.framework.IPushServiceCallback;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.system.push.PushService;
import com.hnhy.framework.util.FrameworkUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SystemPush extends BaseSystem {
    private static final String TAG = "SystemPush";

    private IPushService mPushService;
    private IPushServiceCallback mPushServiceCallback;
    private ServiceConnection mServiceConnection;

    private boolean mIsBind;
    private boolean mServiceIsRunning;

    @Override
    protected void init() {
        mServiceIsRunning = FrameworkUtils.isServiceRunning(mContext, PushService.class.getName());
        Logger.d(TAG, "init mServiceIsRunning=" + mServiceIsRunning);

        mPushServiceCallback = new IPushServiceCallback.Stub() {
            @Override
            public void onSubscribeCallback(boolean success, String msg) {
                Logger.d(TAG, "onSubscribeCallback = " + success + ",msg=" + msg);
            }
        };

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.d(TAG, "onServiceConnected");
                mIsBind = true;
                mPushService = IPushService.Stub.asInterface(service);
                try {
                    mPushService.registerCallback(mPushServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Logger.d(TAG, "onServiceConnected");
                mIsBind = false;
            }
        };

        if (!mServiceIsRunning) {
            startService();
        }
        bindService();
    }

    @Override
    protected void destroy() {
        try {
            mPushService.unRegisterCallback();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unBindService();
        Logger.e(TAG, "SystemPush is destroy");
    }

    public void subscribe(final String topic, final int qos) {
        Logger.d(TAG, "subscribe,topic=" + topic + ",qos=" + qos);
        if (mIsBind) {
            sub(topic, qos);
        } else {
            Logger.w(TAG, "subscribe,the service not bind or mqtt not connect,retry...");
            Observable.interval(0, 1, TimeUnit.SECONDS)
                    .take(10).subscribe(new Observer<Long>() {
                Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(Long aLong) {
                    if (mIsBind) {
                        mDisposable.dispose();
                        sub(topic, qos);
                    } else {
                        if (aLong == 10 - 1) {
                            Logger.e(TAG, "can't subcribe,mIsBind=" + mIsBind);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }

    private void sub(String topic, int qos) {
        try {
            mPushService.subscribe(topic, qos);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startService() {
        Logger.d(TAG, "startService");
        Intent intent = new Intent(Engine.getInstance().mContext, PushService.class);
        Engine.getInstance().mContext.startService(intent);
        mServiceIsRunning = true;
    }

    private void bindService() {
        Logger.d(TAG, "bindService");
        Intent intent = new Intent(Engine.getInstance().mContext, PushService.class);
        Engine.getInstance().mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        Logger.d(TAG, "unBindService");
        Engine.getInstance().mContext.unbindService(mServiceConnection);
    }
}
