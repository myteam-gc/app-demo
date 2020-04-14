package com.hnhy.framework.frame;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hnhy.framework.R;
import com.hnhy.framework.system.SystemHttp;
import com.hnhy.framework.system.SystemPager;
import com.hnhy.framework.system.SystemWaterMark;
import com.hnhy.ui.dialog.LoadingDialog;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:35
 * Description:
 */

public class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;
    protected Context mContext;
    private int mLoadingDialogCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setLightStatusBar();
        getSystem(SystemPager.class).addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSystem(SystemWaterMark.class).onActivityStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mLoadingDialog != null) {
            mLoadingDialog.onBackPressed();
        }
        getSystem(SystemHttp.class).cancelRequest(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSystem(SystemPager.class).removeActivity(this);
        getSystem(SystemHttp.class).cancelRequest(this);
    }

    public void showLoadingDialog(boolean show) {
        this.showLoadingDialog(show, "");
    }

    public void showLoadingDialog(boolean show, String msg) {
        if (show) {
            mLoadingDialogCount++;
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.showLoading(msg, true);
        } else {
            mLoadingDialogCount--;
            if (mLoadingDialog != null && mLoadingDialogCount <= 0) {
                mLoadingDialog.dismissImmediately();
            }
        }
    }

    public void setStatusBar() {
        // 设置图片沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置白底黑字状态栏
     */
    public void setLightStatusBar(){
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected <T extends BaseSystem> T getSystem(Class<T> system) {
        return SystemManager.getInstance().getSystem(system);
    }
}

