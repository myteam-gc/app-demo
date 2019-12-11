package com.hnhy.ylfz.mvp.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hnhy.ylfz.app.Profile;
import com.hnhy.ylfz.mvp.model.bean.User;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemHttp;
import com.hnhy.framework.system.net.Request;
import com.hnhy.framework.system.net.RequestCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guc on 2019/7/15.
 * 描述：服务端接口
 */
@SuppressWarnings("unused")
public class Api {
    private static Api mInstance;

    private Api() {
    }

    public synchronized static Api getInstance() {
        if (mInstance == null) {
            mInstance = new Api();
        }

        return mInstance;
    }
    //region 用户相关

    /**
     *  登录
     * @param tag       TAG
     * @param userName  用户名
     * @param password  密码
     * @param callback  回调
     */
    public void login(Object tag, String userName, String password, RequestCallback<User> callback) {
        Map<String, String> paramsMap = getParamsMap();
        paramsMap.put("username", userName);
        paramsMap.put("password", password);
        Request request = new Request.Builder()
                .setRequestParams(paramsMap)
                .setRelativeUrl("app/login")
                .setRequestType(Request.TYPE_POST)
                .build();

        getHttpSystem().net(tag, request, callback);
    }

    /**
     * 修改密码
     *
     * @param tag      Tag
     * @param params   参数
     * @param callback 回调
     */
    public void modifyPassword(Object tag, @NonNull Map<String, String> params, RequestCallback<String> callback) {
        addItokenToParamsMap(params);
        Request request = new Request.Builder()
                .setRequestParams(params)
                .setRelativeUrl("app/editPassword")
                .setWrapperResponse(false)
                .build();
        getHttpSystem().net(tag, request, callback);
    }
    //endregion 用户相关

    private SystemHttp  getHttpSystem() {
        return SystemManager.getInstance().getSystem(SystemHttp.class);
    }
    private Map<String, String> getParamsMap() {
        return new HashMap<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean addItokenToParamsMap (Map<String, String> params) {
        if (Profile.itoken != null && !TextUtils.isEmpty(Profile.itoken)) {
            params.put("itoken", Profile.itoken);
            return true;
        }
        return false;
    }
}
