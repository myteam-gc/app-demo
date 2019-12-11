package com.hnhy.ylfz.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;

/**
 * Created by guc on 2019/12/9.
 * 描述：指标监控
 */
public class ActivityIndexMonitor extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_index_monitor);

    }
}
