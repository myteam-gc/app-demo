package com.hnhy.ylfz.mvp.presenter;

import android.text.TextUtils;

import com.hnhy.ylfz.mvp.contract.ContractLogin;
import com.hnhy.ylfz.mvp.model.Api;
import com.hnhy.ylfz.mvp.model.bean.User;
import com.hnhy.ylfz.system.MyRequestCallback;
import com.hnhy.framework.system.net.Response;

/**
 * Created by guc on 2019/7/15.
 * 描述：登录Presenter
 */
public class PresenterLogin implements ContractLogin.Presenter {
    private ContractLogin.View mView;

    public PresenterLogin(ContractLogin.View view) {
        mView = view;
    }

    @Override
    public void login(Object tag,String username, String password) {
        if (!checkIsNull(username, password)) {
            mView.onShowLoading(true);
            Api.getInstance().login(tag, username, password, new MyRequestCallback<User>() {
                @Override
                public void onSuccess(User data, Response resp) {
                    super.onSuccess(data, resp);
                    mView.onLoginCallback(true, data, null);
                }

                @Override
                public void onFailure(Response resp) {
                    super.onFailure(resp);
                    mView.onLoginCallback(false, null, resp.mMessage);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    mView.onShowLoading(false);
                }
            });
        }
    }

    private boolean checkIsNull(String username, String password) {
        boolean isNull = false;
        if (TextUtils.isEmpty(username)) {
            isNull = true;
            mView.onInputIsInvalid("请输入用户名!");
        }
        if (TextUtils.isEmpty(password)) {
            isNull = true;
            mView.onInputIsInvalid("请输入密码!");
        }
        return isNull;
    }
}
