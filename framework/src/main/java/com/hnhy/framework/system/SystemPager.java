package com.hnhy.framework.system;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class SystemPager extends BaseSystem {
    private static final String TAG = "SystemPager";

    private List<Activity> mList;
    private Activity mCurrActivity;

    @Override
    protected void init() {
        mList = new ArrayList<>();
    }

    @Override
    protected void destroy() {
        finishAllActivity();
        mCurrActivity = null;
        mList = null;
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            Logger.d(TAG, "addActivity name=" + activity.getClass().getSimpleName());
            mList.add(activity);
            mCurrActivity = activity;
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            Logger.d(TAG, "finishActivity name=" + activity.getClass().getSimpleName());
            mList.remove(activity);
            if (mList != null && mList.size() > 0) {
                mCurrActivity = mList.get(mList.size() - 1);//定位到最后一个存活的act
            } else {
                mCurrActivity = null;
            }
        }
    }

    public Activity getCurrActivity() {
        return mCurrActivity;
    }

    public FragmentActivity getCurrFragmentActivity() {
        return (FragmentActivity) mCurrActivity;
    }

    public void finishAllActivity() {
        if (mList == null || mList.size() < 1) return;
        for (Activity activity : mList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        mList.clear();
    }

    public boolean hasActivity(String localClassName) {
        for (Activity activity : mList) {
            if (localClassName.equals(activity.getClass().getName())) {
                return true;
            }
        }
        return false;
    }


}
