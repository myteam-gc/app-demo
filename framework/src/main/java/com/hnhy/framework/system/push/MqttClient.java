package com.hnhy.framework.system.push;

import com.hnhy.framework.logger.Logger;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClient {
    private static final String TAG = "MqttClient";

    private MqttAsyncClient mMqttAsyncClient;

    public MqttClient(String brokerUrl, String clientId) {
        try {
            mMqttAsyncClient = new MqttAsyncClient(brokerUrl, clientId, new MemoryPersistence());
            Logger.e(TAG, "init success,clientId=" + clientId);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "init error,msg=" + e.getMessage());
        }
    }

    public void connect(MqttConnectOptions options, IMqttActionListener callback) {
        try {
            mMqttAsyncClient.connect(options, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "connect error,msg=" + e.getMessage());
        }
    }

    public void disConnect() {
        try {
            mMqttAsyncClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "disConnect error,msg=" + e.getMessage());
        }
    }

    public boolean isConnected() {
        return mMqttAsyncClient != null && mMqttAsyncClient.isConnected();
    }

    public void subscribe(String topicFilter, int qos, IMqttActionListener callback) {
        this.subscribe(new String[]{topicFilter}, new int[]{qos}, callback);
    }

    public void subscribe(String[] topicFilters, int[] qos, IMqttActionListener callback) {
        try {
            mMqttAsyncClient.subscribe(topicFilters, qos, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "subscribe error,msg=" + e.getMessage());
            if (callback != null) {
                callback.onFailure(null, e);
            }
        }
    }

    public void unsubscribe(String topicFilter, IMqttActionListener callback) {
        this.unsubscribe(new String[]{topicFilter}, callback);
    }

    public void unsubscribe(String[] topicFilters, IMqttActionListener callback) {
        try {
            mMqttAsyncClient.unsubscribe(topicFilters, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "unsubscribe error,msg=" + e.getMessage());
            if (callback != null) {
                callback.onFailure(null, e);
            }
        }
    }

    public void publish(String topic, MqttMessage message, IMqttActionListener callback) {
        try {
            mMqttAsyncClient.publish(topic, message, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "publish error,msg=" + e.getMessage());
            if (callback != null) {
                callback.onFailure(null, e);
            }
        }
    }

    public void setBufferOpts(DisconnectedBufferOptions opts) {
        if (mMqttAsyncClient != null && mMqttAsyncClient.isConnected())
            mMqttAsyncClient.setBufferOpts(opts);
    }
    public void setCallback(MqttCallback callback) {
        mMqttAsyncClient.setCallback(callback);
    }
}
