package com.hnhy.ylfz.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hnhy.framework.Engine;

public class SharedPreferencesManager {
    private final static String SP_NAME_DEFAULT = "sp_cache";
    private static final String KEY_FIRST_OPEN = "key_first_open";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    private static SharedPreferencesManager mInstance;
    private SharedPreferences mSharedPreferences;

    private static final String KEY_USER = "key_user";
    private static final String KEY_IS_FIRST_LOGIN = "is_first_login";
    private static final String KEY_IS_REMIND_ASSISTANT = "is_remind_assistant";
    private static final String KEY_IS_COPY_PEOPLE = "key_is_copy_people";
    private String TAG = "PreferenceUser";

    private SharedPreferencesManager() {
        mSharedPreferences = Engine.getInstance().mContext.getSharedPreferences(SP_NAME_DEFAULT, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferencesManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isFirstOpen() {
        return mSharedPreferences.getBoolean(KEY_FIRST_OPEN, true);
    }

    public void setFirstOpenOver() {
        Editor edit = mSharedPreferences.edit();
        edit.putBoolean(KEY_FIRST_OPEN, false);
        edit.apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setUserName(String userName) {
        Editor edit = mSharedPreferences.edit();
        edit.putString(KEY_USERNAME, userName);
        edit.apply();
    }

    public String getPassword() {
        return mSharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        Editor edit = mSharedPreferences.edit();
        edit.putString(KEY_PASSWORD, password);
        edit.apply();
    }

    public boolean isRemindAssistant() {
        return mSharedPreferences.getBoolean(KEY_IS_REMIND_ASSISTANT, true);
    }

    public void setRemindAssistant(boolean remindAssistant) {
        Editor edit = mSharedPreferences.edit();
        edit.putBoolean(KEY_IS_REMIND_ASSISTANT, remindAssistant);
        edit.apply();
    }

    public void clearRemindAssistant() {
        Editor edit = mSharedPreferences.edit();
        edit.remove(KEY_IS_REMIND_ASSISTANT);
        edit.apply();
    }

    public void setCopyPeople(String peopleJson) {
        Editor edit = mSharedPreferences.edit();
        edit.putString(KEY_IS_COPY_PEOPLE, peopleJson);
        edit.apply();
    }

    public String getCopyPeople() {
        return mSharedPreferences.getString(KEY_IS_COPY_PEOPLE, "");
    }
}