package com.hnhy.framework.frame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected View mRootView;//要导入的布局view

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initView(mRootView);
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View rootView);

    protected void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSystem(SystemHttp.class).cancelRequest(this);
    }

    protected <T extends BaseSystem> T getSystem(Class<T> system) {
        return SystemManager.getInstance().getSystem(system);
    }
}
