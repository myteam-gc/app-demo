package com.hnhy.ylfz.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.IndexMonitor;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterIndexMonitor;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/9.
 * 描述：指标监控
 */
public class ActivityIndexMonitor extends BaseActivity {
    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;

    private List<IndexMonitor> mDatas;
    private AdapterIndexMonitor mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_index_monitor);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        mDatas = new ArrayList<>();
        mDatas.add(new IndexMonitor());
        mDatas.add(new IndexMonitor());
        mAdapter.update(mDatas);
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void initView() {
        mAdapter = new AdapterIndexMonitor(mContext, mDatas);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvContent.setAdapter(mAdapter);
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}
