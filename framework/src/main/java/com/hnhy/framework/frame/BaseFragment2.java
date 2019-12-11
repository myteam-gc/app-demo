package com.hnhy.framework.frame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hnhy.framework.system.SystemHttp;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:43
 * Description:
 */

public abstract class BaseFragment2 extends Fragment {

    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSystem(SystemHttp.class).cancelRequest(this);
    }

    protected <T extends BaseSystem> T getSystem(Class<T> system) {
        return SystemManager.getInstance().getSystem(system);
    }


    public void showLoadingDialog(boolean show) {
        this.showLoadingDialog(show, "");
    }

    public void showLoadingDialog(boolean show, String msg) {
        // 宿主activity 必须基础BaseActivity
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog(show, msg);
        }
    }
}
