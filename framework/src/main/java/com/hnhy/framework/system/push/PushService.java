package com.hnhy.framework.system.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.hnhy.framework.Configuration;
import com.hnhy.framework.Engine;
import com.hnhy.framework.IPushService;
import com.hnhy.framework.IPushServiceCallback;
import com.hnhy.framework.R;
import com.hnhy.framework.datastructure.LinkQueue;
import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.util.DeviceUtils;
import com.hnhy.framework.util.FrameworkUtils;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by guc on 2018/9/10.
 * 描述：
 */
public class PushService extends Service {
    public static final String ACTION_NOTIFICATION = "com.hnhy.petition.notification";
    public static final String ACTION_SERVICE_DESTROY = "com.hnhy.petition.service_destroy";
    public static final String RECEIVER_NOTIFICATION_CLICK = "com.hnhy.petition.app.NotificationClickReceiver";
    public static final String EXTRA_DATA = "data";
    private static final int GRAY_SERVICE_ID = Integer.MAX_VALUE - 2;
    private static final String TAG = "PushService";
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    //    private static final String HOST = "tcp://192.168.20.100:6210";
    private static String HOST = "tcp://192.168.30.153:1883";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private IPushService.Stub mBinder;
    private IPushServiceCallback mPushServiceCallback;

    private MqttClient2 mMqttClient;
    private MqttConnectOptions mMqttConnectOptions;
    private NotificationManager mNotificationManager;
    private boolean mIsReconnecting;
    private boolean mIsRunningTimer;

    private Gson mGson;
    private List<TopicWrapper> mSubscribedTopics;
    private LinkQueue<TopicWrapper> mSubscribeQueue;
    private int mNotificationId;
    //region 系统广播注册
    private SystemBroadcastReceiver mSysReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate");
        init();
        registerSystemReceiver();
        //如果为release版，则修改 HOST
        if (Engine.getInstance().mConfiguration.getCurrentModel() == Configuration.MODEL_RELEASE) {
            HOST = "tcp://192.168.20.100:6210";
        }
        Log.d(TAG, "HOST" + HOST);
        mBinder = new IPushService.Stub() {
            @Override
            public void registerCallback(IPushServiceCallback callback) {
                mPushServiceCallback = callback;
            }

            @Override
            public void unRegisterCallback() {
                mPushServiceCallback = null;
            }

            @Override
            public boolean isConnected() {
                return mMqttClient != null && mMqttClient.isConnected();
            }

            @Override
            public void subscribe(String topic, int qos) {
                Logger.d(TAG, String.format("subscribe topic=%s,qos=%s", topic, qos));
                subscribeTopic(new TopicWrapper(topic, qos));
            }
        };

        mMqttClient = new MqttClient2(getApplicationContext(), HOST, DeviceUtils.getDeviceId(this));
        mMqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                if (throwable != null) {
                    Logger.d(TAG, "connectionLost" + throwable.getMessage() +
                            "\nnet is active:" + FrameworkUtils.isNetworkAvailable(PushService.this) +
                            "\n throwable toString:" + throwable.toString() +
                            "\n mMqttClient.isConnected():" + mMqttClient.isConnected()
                    );
                } else {
                    Logger.d(TAG, "connectionLost" + "throwable is null");
                }

                mSubscribeQueue.enQueue(mSubscribedTopics);
                mSubscribedTopics.clear();
                if (!mMqttClient.isConnected())//未连接时再重连
                    tryReconnect();
//                    connect();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msg = new String(message.getPayload());
                Logger.d(TAG, "messageArrived,topic=" + topic + ",msg=" + msg);
                sendNotice(getPushMsg(msg));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Logger.d(TAG, "deliveryComplete");
            }
        });

        connect();
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
        mPushServiceCallback = null;
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        startForeground(GRAY_SERVICE_ID, new Notification());
        Intent innerIntent = new Intent(this, GrayInnerService.class);
        startService(innerIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(ACTION_SERVICE_DESTROY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setClassName(this, RECEIVER_NOTIFICATION_CLICK);
        }
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
        Logger.d(TAG, "onDestroy");
        unRegisterSystemReceiver();
        mBinder = null;
        mMqttClient.disConnect();
        mNotificationManager = null;
        mPushServiceCallback = null;
        mMqttConnectOptions = null;
    }

    private void subscribeTopic(final TopicWrapper topicWrapper) {
        if (mMqttClient != null && mMqttClient.isConnected()) {
            Logger.d(TAG, "subscribeTopic ed size=" + mSubscribedTopics.size());
            if (mSubscribedTopics.contains(topicWrapper)) {
                return;
            }
            mMqttClient.subscribe(topicWrapper.mTopic, topicWrapper.mQos, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Logger.d(TAG, String.format("subscribeTopic callback topic=%s:%s", topicWrapper.mTopic, true));
                    if (mPushServiceCallback != null) {
                        try {
                            mPushServiceCallback.onSubscribeCallback(true, "");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    mSubscribedTopics.add(topicWrapper);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Logger.w(TAG, String.format("subscribeTopic callback topic=%s:%s,msg=%s", topicWrapper.mTopic, false, throwable.getMessage()));
                    if (mPushServiceCallback != null) {
                        try {
                            mPushServiceCallback.onSubscribeCallback(false, throwable.getMessage());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Logger.d(TAG, "subscribeTopic,mqtt is not connect, into queue first.");
            mSubscribeQueue.enQueue(topicWrapper);
        }
    }

    private synchronized void connect() {
        Logger.d(TAG, "connect");
        if (mMqttClient != null && mMqttClient.isConnected()) {
            Logger.d(TAG, "connect return");
            return;
        }
        mMqttClient.connect(mMqttConnectOptions, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Logger.d(TAG, "connect success");
                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(10);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(false);
                mMqttClient.setBufferOpts(disconnectedBufferOptions);

                mIsReconnecting = false;
                subscribeFromQueue();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Logger.w(TAG, "connect error,msg=" + throwable.getMessage());
                mIsReconnecting = false;
                tryReconnect();
            }
        });
    }

    private void init() {
        mGson = new Gson();
        mSubscribedTopics = new ArrayList<>();
        mSubscribeQueue = new LinkQueue<>();

        initNotification();

        mMqttConnectOptions = new MqttConnectOptions();
        mMqttConnectOptions.setAutomaticReconnect(true);
        mMqttConnectOptions.setKeepAliveInterval(10);
        mMqttConnectOptions.setConnectionTimeout(50);
        mMqttConnectOptions.setCleanSession(false);//  离线消息也能接收11.06修改
        mMqttConnectOptions.setUserName(USERNAME);
        mMqttConnectOptions.setPassword(PASSWORD.toCharArray());
    }

    private void subscribeFromQueue() {
        while (!mSubscribeQueue.isEmpty()) {
            subscribeTopic(mSubscribeQueue.deQueue());
        }
    }

    private void tryReconnect() {
        Logger.d(TAG, "mIsReconnecting=" + mIsReconnecting + "\n + mIsRunningTimer" + mIsRunningTimer);
        if (!mIsReconnecting && !mIsRunningTimer) {
            mIsReconnecting = true;
            mIsRunningTimer = true;
            Observable.interval(0, 60, TimeUnit.SECONDS)
                    .take(60).subscribe(new Observer<Long>() {
                Disposable mDisposable;

                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(Long aLong) {
                    if (mMqttClient != null && mMqttClient.isConnected()) {
                        Logger.e(TAG, "Observable mMqttClient.isConnected() =" + mMqttClient.isConnected());
                        subscribeFromQueue();
                        mDisposable.dispose();
                        mIsRunningTimer = false;
                    } else {
                        connect();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    mIsRunningTimer = false;
                }
            });
        }
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
            Logger.d(TAG, "create notice channel success");
        }
    }

    private void sendNotice(PushMessage pushMsg) {
        if (pushMsg == null) return;
        mNotificationId++;
        if (mNotificationId >= 10) {
            mNotificationId = 0;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(pushMsg.title)
                .setContentText(pushMsg.content);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, mNotificationId, getClickIntent(pushMsg), PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setCustomBigContentView(getCustomBigContentView(pushMsg));
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(mNotificationId, builder.build());
    }

    /**
     * 获取通知点击Intent
     *
     * @param pushMsg 推送消息
     * @return intent
     */
    private Intent getClickIntent(PushMessage pushMsg) {
        Intent clickIntent = new Intent(ACTION_NOTIFICATION);
        clickIntent.putExtra(EXTRA_DATA, pushMsg);
        ComponentName componentName = new ComponentName(this, RECEIVER_NOTIFICATION_CLICK);
        clickIntent.setComponent(componentName);
        return clickIntent;
    }

    /**
     * 获取自定义通知样式
     *
     * @param msg
     * @return
     */
    private RemoteViews getCustomBigContentView(PushMessage msg) {
        RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.layout_notices);//自定义通知栏

        rv.setTextViewText(R.id.tv_title_notice, msg.title);
        rv.setTextViewText(R.id.tv_sfry, String.format("涉访人员：%s", msg.sfry));
        rv.setTextViewText(R.id.tv_content, String.format("预警内容：%s", msg.content));
        rv.setTextViewText(R.id.tv_create_time, FrameworkUtils.DateTime.dateFormat(System.currentTimeMillis(), "HH:mm"));
        //通知类型（1：公告， 4：稳控状态变更， 7：下发通知，
        // 11：红色预警， 12：橙色预警， 13：黄色预警， 14：蓝色预警， 15：群体预警）
        switch (msg.noticeType) {
            case 1:
            case 7:
                rv.setTextViewText(R.id.tv_title, "通知公告");
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.card_inform2);
                rv.setViewVisibility(R.id.tv_sfry, View.GONE);
                break;
            case 4:
                rv.setTextViewText(R.id.tv_title, "消息提醒");
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_warn2);
                break;
            case 11:
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_red);
                break;
            case 12:
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_orange);
                break;
            case 13:
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_yellow);
                break;
            case 14:
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_blue);
                break;
            case 15:
                rv.setImageViewResource(R.id.iv_icon_notice, R.drawable.icon_group);
                break;
        }
        return rv;
    }

    private static class TopicWrapper {
        private String mTopic;
        private int mQos;

        private TopicWrapper(String topic, int qos) {
            this.mTopic = topic;
            this.mQos = qos;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TopicWrapper) {
                TopicWrapper t = (TopicWrapper) obj;
                return t.mTopic.equals(mTopic);
            }
            return false;
        }
    }

    /**
     * 推送接收到的消息处理
     *
     * @param msg mqtt接收到的消息
     * @return 消息处理对象
     */
    private PushMessage getPushMsg(String msg) {
        try {
            PushMessage pushMsg;
            if (!TextUtils.isEmpty(msg)) {
                int index = msg.lastIndexOf("@");
                if (index != -1) {
                    msg = msg.substring(0, index);
                }
                pushMsg = mGson.fromJson(msg, PushMessage.class);
                if (!TextUtils.isEmpty(pushMsg.titleNew)){
                    pushMsg.title = pushMsg.titleNew;
                }
                return pushMsg;
            }
            return null;
        } catch (Exception e) {
            Logger.e(TAG, "send notice failure,reason:" + e.getMessage());
            return null;
        }

    }

    /**
     * 监听网络变化
     */
    private void registerSystemReceiver() {
        mSysReceiver = new SystemBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mSysReceiver, intentFilter);
    }

    private void unRegisterSystemReceiver() {
        if (mSysReceiver != null) {
            unregisterReceiver(mSysReceiver);
            mSysReceiver = null;
        }
    }
    //endregion

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

    }

    /**
     * 系统广播监听
     */
    private class SystemBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            switch (action) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    if (FrameworkUtils.isNetworkAvailable(context)) {
                        Logger.e(TAG, "网络连接上了");
                        connect();
                    } else {
                        Logger.e(TAG, "网络断了");
//                        mMqttClient.disConnect();
                    }
                    break;
                case Intent.ACTION_SCREEN_ON://屏幕点亮
                    Logger.e(TAG, "screen on");
                    if (!FrameworkUtils.isNetworkAvailable(context)) return;
                    if (mMqttClient != null && !mMqttClient.isConnected()) {
                        Logger.e(TAG, "screen on connect");
                        connect();
                    }
                    break;
            }
        }
    }

}
