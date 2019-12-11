package com.hnhy.ylfz.mvp.presenter;

import com.hnhy.ylfz.mvp.contract.ContractModifyPwd;
import com.hnhy.ylfz.mvp.model.Api;
import com.hnhy.ylfz.system.MyRequestCallback;
import com.hnhy.framework.system.net.Response;

import java.util.Map;

/**
 * Created by guc on 2019/7/16.
 * 描述：
 */
public class PresenterModifyPwd implements ContractModifyPwd.Presenter {
    private ContractModifyPwd.View mView;

    public PresenterModifyPwd(ContractModifyPwd.View view) {
        this.mView = view;
    }

    @Override
    public void modifyPassword(Object tag, Map<String, String> params) {
        mView.onShowLoading(true, "正在修改密码，请稍后...");
        Api.getInstance().modifyPassword(tag, params, new MyRequestCallback<String>() {
            @Override
            public void onSuccess(String data, Response resp) {
                super.onSuccess(data, resp);
                mView.onModifySuccess(resp.mMessage);
                mView.onShowLoading(false, null);
            }

            @Override
            public void onFailure(Response resp) {
                super.onFailure(resp);
                mView.onModifyFailure(resp);
                mView.onShowLoading(false, null);
            }
        });
    }
}
