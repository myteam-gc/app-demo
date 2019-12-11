package com.hnhy.framework.system.download;

public interface CallbackDownload {

    void onWait(Task task);

    void onStart(Task task);

    void onProgress(Task task);

    void onSuccess(Task task);

    void onFailure(Task task);

    void onComplete(Task task);
}
