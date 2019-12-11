package com.hnhy.framework.frame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by guc on 2019/7/30.
 * 描述：懒加载Fragment
 */

public abstract class LazyBaseFragment extends Fragment {
    protected Context mcontext;
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false; //是否与view建立映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    private View mView;//要导入的布局view

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        mcontext = this.getContext();
        initView(mView);
        isInitView = true;
        loadData();
        return mView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            loadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void loadData() {
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    /**
     * 加載页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 加载控件
     */
    protected abstract void initView(View view);

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

}
