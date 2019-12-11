package com.hnhy.framework.system.push;

import android.content.Context;

import com.hnhy.framework.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttClient2 {
    private static final String TAG = "MqttClient";

    private MqttAndroidClient mMqttAndroidClient;

    public MqttClient2(Context context, String brokerUrl, String clientId) {
        mMqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        Logger.e(TAG, "init success,clientId=" + clientId);
    }

    public void connect(MqttConnectOptions options, IMqttActionListener callback) {
        try {
            mMqttAndroidClient.connect(options, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "connect error,msg=" + e.getMessage());
        }
    }

    public void disConnect() {
        try {
            mMqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "disConnect error,msg=" + e.getMessage());
        } catch (NullPointerException e) {
            Logger.e(TAG, "disConnect error,msg=" + e.getMessage());
        }
    }

    public boolean isConnected() {
        try {
            Logger.e(TAG, "isConnected");
            return mMqttAndroidClient != null && mMqttAndroidClient.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void subscribe(String topicFilter, int qos, IMqttActionListener callback) {
        this.subscribe(new String[]{topicFilter}, new int[]{qos}, callback);
    }

    public void subscribe(String[] topicFilters, int[] qos, IMqttActionListener callback) {
        try {
            mMqttAndroidClient.subscribe(topicFilters, qos, null, callback);
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
            mMqttAndroidClient.unsubscribe(topicFilters, null, callback);
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
            mMqttAndroidClient.publish(topic, message, null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
            Logger.e(TAG, "publish error,msg=" + e.getMessage());
            if (callback != null) {
                callback.onFailure(null, e);
            }
        }
    }

    public void setBufferOpts(DisconnectedBufferOptions opts) {
        if (mMqttAndroidClient != null && mMqttAndroidClient.isConnected())
            mMqttAndroidClient.setBufferOpts(opts);
    }

    public void setCallback(MqttCallback callback) {
        mMqttAndroidClient.setCallback(callback);
    }
}
