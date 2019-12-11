package com.hnhy.ylfz.mvp.contract;

import com.hnhy.ylfz.mvp.ui.BasePresenter;
import com.hnhy.ylfz.mvp.ui.BaseView;
import com.hnhy.framework.system.net.Response;

import java.util.Map;

/**
 * Created by guc on 2019/7/16.
 * 描述：修改密码
 */
public interface ContractModifyPwd {
    interface Presenter extends BasePresenter {
        void modifyPassword(Object tag, Map<String, String> params);
    }

    interface View extends BaseView<Presenter> {
        void onShowLoading(boolean isShow, String msg);

        void onModifySuccess(String msg);

        void onModifyFailure(Response response);
    }
}
