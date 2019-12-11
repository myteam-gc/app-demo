package com.hnhy.framework;

import android.os.Environment;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:24
 * Description:
 */
public final class Configuration {
    public static final int MODEL_DEBUG = 0;
    public static final int MODEL_BETA = 1;
    public static final int MODEL_RELEASE = 2;

    private int mCurrentModel;
    private boolean mEnableLog;
    private boolean mEnableCrashReset;
    private boolean mEnableSaveCrashLog;

    private String mUrlApiRelease;
    private String mUrlApiBeta;
    private String mUrlApiDebug;

    private String mUrlUpgradeDebug;
    private String mUrlUpgradeRelease;

    private String mLogNativeDir;
    private String mUpgradeApkDir;

    private Configuration(Builder builder) {
        mCurrentModel = builder.currentModel;
        mEnableLog = builder.enableLog;
        mEnableCrashReset = builder.enableCrashReset;
        mEnableSaveCrashLog = builder.enableSaveCrashLog;
        mLogNativeDir = builder.logNativeDir;
        mUrlApiRelease = builder.urlApiRelease;
        mUrlApiBeta = builder.urlApiBeta;
        mUrlApiDebug = builder.urlApiDebug;
        mUrlUpgradeDebug = builder.urlUpgradeDebug;
        mUrlUpgradeRelease = builder.urlUpgradeRelease;
        mUpgradeApkDir = builder.upgradeApkDir;
    }

    public int getCurrentModel() {
        return mCurrentModel;
    }

    public String getCurrentModelApiUrl() {
        switch (mCurrentModel) {
            case MODEL_RELEASE:
                return mUrlApiRelease;
            case MODEL_BETA:
                return mUrlApiBeta;
            case MODEL_DEBUG:
                return mUrlApiDebug;
            default:
                throw new RuntimeException("unknown the model>>" + mCurrentModel);
        }
    }

    public String getCurrentModelUpgradeUrl() {
        switch (mCurrentModel) {
            case MODEL_RELEASE:
                return mUrlUpgradeRelease;
            case MODEL_BETA:
            case MODEL_DEBUG:
                return mUrlUpgradeDebug;
            default:
                throw new RuntimeException("unknown the model>>" + mCurrentModel);
        }
    }

    public boolean isEnableLog() {
        return mEnableLog;
    }

    public boolean isEnableCrashReset() {
        return mEnableCrashReset;
    }

    public boolean isEnableSaveCrashLog() {
        return mEnableSaveCrashLog;
    }

    public String getLogNativeDir() {
        return mLogNativeDir;
    }

    public String getUpgradeApkDir() {
        return mUpgradeApkDir;
    }

    public boolean isRelease() {
        return mCurrentModel == MODEL_RELEASE;
    }

    public void setModel(int model) {
        if (model < MODEL_DEBUG || model > MODEL_RELEASE) {
            throw new RuntimeException("unknown the model>>" + model);
        } else {
            mCurrentModel = model;
            switch (model) {
                case MODEL_DEBUG:
                case MODEL_BETA:
                    mEnableLog = true;
                    mEnableCrashReset = false;
                    mEnableSaveCrashLog = false;
                    break;
                case MODEL_RELEASE:
                    mEnableLog = false;
                    mEnableCrashReset = false;
                    mEnableSaveCrashLog = true;
                    break;
            }
        }
    }

    public void setEnableLog(boolean enableLog) {
        mEnableLog = enableLog;
    }

    public void setEnableCrashReset(boolean enableCrashReset) {
        mEnableCrashReset = enableCrashReset;
    }

    public void setEnableSaveCrashLog(boolean enableSaveCrashLog) {
        mEnableSaveCrashLog = enableSaveCrashLog;
    }

    public void setUrl(String url) {
        switch (mCurrentModel) {
            case MODEL_RELEASE:
                mUrlApiRelease = url;
                break;
            case MODEL_BETA:
                mUrlApiBeta = url;
                break;
            case MODEL_DEBUG:
                mUrlApiDebug = url;
                break;
            default:
                throw new RuntimeException("unknown the model>>" + mCurrentModel);
        }
    }

    public static class Builder {
        private int currentModel = MODEL_DEBUG;
        private boolean enableLog;
        private boolean enableCrashReset;
        private boolean enableSaveCrashLog;

        private String urlApiRelease;
        private String urlApiBeta;
        private String urlApiDebug;

        private String urlUpgradeDebug;
        private String urlUpgradeRelease;

        private String logNativeDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/";
        private String upgradeApkDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk/";

        public Builder setModel(int model) {
            if (model < MODEL_DEBUG || model > MODEL_RELEASE) {
                throw new RuntimeException("unknown the model>>" + model);
            } else {
                currentModel = model;
                switch (model) {
                    case MODEL_DEBUG:
                    case MODEL_BETA:
                        enableLog = true;
                        enableCrashReset = false;
                        enableSaveCrashLog = true;
                        break;
                    case MODEL_RELEASE:
                        enableLog = false;
                        enableCrashReset = true;
                        enableSaveCrashLog = true;
                        break;
                }
                return this;
            }
        }

        public Builder setUrlApiRelease(String urlRelease) {
            urlApiRelease = urlRelease;
            return this;
        }

        public Builder setUrlApiBeta(String urlBeta) {
            urlApiBeta = urlBeta;
            return this;
        }

        public Builder setUrlApiDebug(String urlDebug) {
            urlApiDebug = urlDebug;
            return this;
        }

        public Builder setUrlUpgradeDebug(String urlUpgradeDebug) {
            this.urlUpgradeDebug = urlUpgradeDebug;
            return this;
        }

        public Builder setUrlUpgradeRelease(String urlUpgradeRelease) {
            this.urlUpgradeRelease = urlUpgradeRelease;
            return this;
        }

        public Builder setLogNativeDir(String path) {
            logNativeDir = path;
            return this;
        }

        public Builder setUpgradeApkDir(String upgradeApkDir) {
            this.upgradeApkDir = upgradeApkDir;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
