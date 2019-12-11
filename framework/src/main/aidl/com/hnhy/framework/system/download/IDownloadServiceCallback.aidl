package com.hnhy.framework.system.download;

import com.hnhy.framework.system.download.Task;

interface IDownloadServiceCallback {
    void onCallback(in Task task);
}
