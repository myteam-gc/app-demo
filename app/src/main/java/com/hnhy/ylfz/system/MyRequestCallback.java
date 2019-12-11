package com.hnhy.ylfz.system;

import android.content.Intent;

import com.hnhy.ylfz.mvp.ui.activity.user.ActivityLogin;
import com.hnhy.framework.Engine;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemPager;
import com.hnhy.framework.system.net.RequestCallback;
import com.hnhy.framework.system.net.Response;

/**
 * Created by guc on 2019/7/15.
 * 描述：
 */
public abstract class MyRequestCallback<T> extends RequestCallback<T> {
    protected MyRequestCallback() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T data, Response resp) {
        super.onSuccess(data, resp);
    }

    @Override
    public void onFailure(Response resp) {
        if (resp.mHttpCode == 401) {
            resp.mMessage = "登录已过期，请重新登录";
            SystemManager.getInstance().getSystem(SystemPager.class).finishAllActivity();
            Engine.getInstance().mContext.startActivity(new Intent(Engine.getInstance().mContext, ActivityLogin.class));
        } else {
            super.onFailure(resp);
        }

    }

    @Override
    public void onComplete() {
        super.onComplete();
    }
}
