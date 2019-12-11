// IPushService.aidl
package com.hnhy.framework;
import com.hnhy.framework.IPushServiceCallback;

interface IPushService {
   void registerCallback(IPushServiceCallback callback);
   void unRegisterCallback();

   boolean isConnected();

   void subscribe(String topic,int qos);
}
