package com.hnhy.framework.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;

import com.hnhy.framework.Engine;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.system.download.CallbackDownload;
import com.hnhy.framework.system.download.IDownloadService;
import com.hnhy.framework.system.download.IDownloadServiceCallback;
import com.hnhy.framework.system.download.ServiceDownload;
import com.hnhy.framework.system.download.Task;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SystemDownload extends BaseSystem {
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_CANCEL = 3;
    public static final int STATUS_FAILURE = 4;
    public static final int STATUS_SUCCESS = 5;
    public static final int TYPE_CALLBACK_WAIT = 10;
    public static final int TYPE_CALLBACK_START = 11;
    public static final int TYPE_CALLBACK_PROGRESS = 12;
    public static final int TYPE_CALLBACK_SUCCESS = 13;
    public static final int TYPE_CALLBACK_FAILURE = 14;
    public static final int TYPE_CALLBACK_COMPLETE = 15;
    private static final String TAG = "SystemDownload";
    private static final int WAIT_TIME = 10;
    private static final String MSG1 = "下载服务无法启动";

    private SparseArray<CallbackDownload> mCallbackDownloads;
    private IDownloadService mIService;
    private IDownloadServiceCallback mServiceCallback;
    private ServiceConnection mServiceConnection;
    private boolean mIsBind;

    @Override
    protected void init() {
        Logger.d(TAG, "init");
        mCallbackDownloads = new SparseArray<>();
        mServiceCallback = new IDownloadServiceCallback.Stub() {
            @Override
            public void onCallback(Task task) {
                Logger.d(TAG, String.format("onCallback,%s", task.toString()));
                switch (task.type_callback) {
                    case TYPE_CALLBACK_WAIT:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onWait(task);
                        }
                        break;
                    case TYPE_CALLBACK_START:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onStart(task);
                        }
                        break;
                    case TYPE_CALLBACK_PROGRESS:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onProgress(task);
                        }
                        break;
                    case TYPE_CALLBACK_SUCCESS:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onSuccess(task);
                        }

                        break;
                    case TYPE_CALLBACK_FAILURE:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onFailure(task);
                        }
                        break;
                    case TYPE_CALLBACK_COMPLETE:
                        if (mCallbackDownloads.get(task.tag) != null) {
                            mCallbackDownloads.get(task.tag).onComplete(task);
                        }
                        break;
                }
            }
        };

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.d(TAG, "onServiceConnected");
                mIsBind = true;
                mIService = IDownloadService.Stub.asInterface(service);
                try {
                    mIService.registerCallback(mServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Logger.e(TAG, "registerCallback error,msg=" + e.getMessage());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsBind = false;
                Logger.d(TAG, "onServiceConnected");
            }
        };

        bindService();
    }

    @Override
    protected void destroy() {
        Logger.d(TAG, "destroy");
        if (mIService != null) {
            try {
                mIService.unRegisterCallback();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mServiceCallback = null;
        mCallbackDownloads.clear();
        mCallbackDownloads = null;
        unBindService();
    }

    public void download(final Task task) {
        if (mIsBind) {
            down(task);
        } else {
            Logger.w(TAG, "download the service is not bind,delay 6 second down");
            Observable.interval(0, 1, TimeUnit.SECONDS)
                    .take(WAIT_TIME).subscribe(new Observer<Long>() {
                Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(Long aLong) {
                    if (mIsBind) {
                        mDisposable.dispose();
                        down(task);
                    } else {
                        if (aLong == WAIT_TIME - 1) {
                            Logger.e(TAG, MSG1);
                            task.status = STATUS_FAILURE;
                            task.msg = MSG1;
                            mCallbackDownloads.get(task.tag).onFailure(task);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Logger.e(TAG, MSG1);
                    task.status = STATUS_FAILURE;
                    task.msg = MSG1;
                    mCallbackDownloads.get(task.tag).onFailure(task);
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    public void pause(int tag) {
        Logger.d(TAG, String.format("pause tag=%s", tag));
        try {
            mIService.pause(tag);
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.e(TAG, String.format("cancel pause,tag=%s,msg=%s", tag, e.getMessage()));
        }
    }

    public void cancel(int tag) {
        Logger.d(TAG, String.format("cancel tag=%s", tag));
        try {
            mIService.cancel(tag);
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.e(TAG, String.format("cancel error,tag=%s,msg=%s", tag, e.getMessage()));
        }
    }

    public void setCallbackDownload(int tag, CallbackDownload callbackDownload) {
        mCallbackDownloads.put(tag, callbackDownload);
    }

    public void removeCallbackDownload(int tag) {
        mCallbackDownloads.remove(tag);
    }

    private void down(Task task) {
        Logger.d(TAG, String.format("down %s", task.toString()));
        try {
            mIService.download(task);
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.e(TAG, String.format("down error,tag=%s,msg=%s", task.tag, e.getMessage()));
        }
    }

    private void bindService() {
        Logger.d(TAG, "bindService");
        Intent intent = new Intent(Engine.getInstance().mContext, ServiceDownload.class);
        Engine.getInstance().mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        Logger.d(TAG, "unBindService");
        Engine.getInstance().mContext.unbindService(mServiceConnection);
    }
}
