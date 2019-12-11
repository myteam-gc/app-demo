package com.hnhy.framework.system.download;
import com.hnhy.framework.system.download.IDownloadServiceCallback;
import com.hnhy.framework.system.download.Task;

interface IDownloadService {
   void registerCallback(IDownloadServiceCallback callback);
   void unRegisterCallback();
   void download(in Task task);
   void pause(int tag);
   void cancel(int tag);
}
