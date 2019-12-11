package com.hnhy.ylfz.mvp.contract;


import com.hnhy.ylfz.mvp.model.bean.User;
import com.hnhy.ylfz.mvp.ui.BasePresenter;
import com.hnhy.ylfz.mvp.ui.BaseView;

/**
 * Created by guc on 2019/7/15.
 * 描述：
 */
public interface ContractLogin {
    interface Presenter extends BasePresenter {
        void login(Object tag,String username, String password);
    }

    interface View extends BaseView<Presenter> {
        void onShowLoading(boolean show);

        void onInputIsInvalid(String msg);

        void onLoginCallback(boolean bool, User user, String msg);
    }
}
