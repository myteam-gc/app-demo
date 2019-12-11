package com.hnhy.framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnhy.framework.frame.SystemManager;

public class Engine {
    private static final String FILE_NAME_CONFIGURATION = "file_name_configuration";
    private static final String KEY_ORIGINAL_CONFIGURATION = "key_original_configuration";
    private static final String KEY_MIRROR_CONFIGURATION = "key_mirror_configuration";
    private static Engine mInstance;
    public Context mContext;
    public Configuration mConfiguration;
    private SharedPreferences mSharedPreferences;

    private Engine(Context context, Configuration configuration) {
        mContext = context.getApplicationContext();
        mSharedPreferences = context.getSharedPreferences(FILE_NAME_CONFIGURATION, Context.MODE_PRIVATE);

        Configuration mirrorConfiguration = getConfiguration(KEY_MIRROR_CONFIGURATION);
        if (mirrorConfiguration == null) {
            if (configuration != null) {
                mConfiguration = configuration;
                saveConfiguration(KEY_ORIGINAL_CONFIGURATION, mConfiguration);
//                saveConfiguration(KEY_MIRROR_CONFIGURATION, mConfiguration);
            } else {
                throw new RuntimeException("the configuration is null!");
            }
        } else {
            mConfiguration = mirrorConfiguration;
        }
    }

    public static void createInstance(Context context, Configuration configuration) {
        if (mInstance == null) {
            mInstance = new Engine(context, configuration);
        } else {
            mInstance.mContext = context;
            mInstance.mConfiguration = configuration;
        }
    }

    public static Engine getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("The instance is null,should call create instance");
        } else {
            return mInstance;
        }
    }

    public void exitApp() {
        SystemManager.getInstance().destroyAllSystem();
        System.exit(0);
        mSharedPreferences = null;
        mContext = null;
        mConfiguration = null;
    }

    public void updateConfiguration() {
        saveConfiguration(KEY_MIRROR_CONFIGURATION, mConfiguration);
    }

    public void restoreConfiguration() {
        Configuration configuration = getConfiguration(KEY_ORIGINAL_CONFIGURATION);
        if (configuration != null) {
            mConfiguration = configuration;
            saveConfiguration(KEY_MIRROR_CONFIGURATION, configuration);
        }
    }

    private void saveConfiguration(String key, Configuration configuration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(configuration);
        edit.putString(key, s).apply();
    }

    private Configuration getConfiguration(String key) {
        String configurationStr = mSharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(configurationStr)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(configurationStr, new TypeToken<Configuration>() {
            }.getType());
        }
    }

}
