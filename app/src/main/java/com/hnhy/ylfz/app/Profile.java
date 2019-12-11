package com.hnhy.ylfz.app;

import com.hnhy.framework.Engine;

/**
 * Created by guc on 2019/7/15.
 * 描述：配置文件
 */
public class Profile {

    public static String itoken;
    public static String mFileSaveImagePath = Engine.getInstance().mContext.getExternalCacheDir().getAbsolutePath() + "/images/";//压缩图片缓存目录
    public static String mFileSaveVideoPath = Engine.getInstance().mContext.getExternalCacheDir().getAbsolutePath() + "/video/";//视频

    public static String URL_FILE_SERVER = "http://yepool.com:5544/filehub/fs";
    public static final String UPLOAD_FILE = "/uploadFile";
    public static final String DOWNLOAD_FILE = "/download?fileId=";

    public static final String URL_API_DEBUG = "http://127.0.0.1:6178/ywk/app/";
    public static final String URL_API_BETA = "http://127.0.0.1:6178/ywk/app/";
    public static final String URL_API_RELEASE = "http://127.0.0.1:6178/ywk/app/";


    public static final String URL_GRADLE_RELEASE = "http://127.0.0.1:6179/OrionProject/checkVersion";
    public static final String URL_GRADLE_DEBUG = "http://127.0.0.1:6179/OrionProject/checkVersion";
}
