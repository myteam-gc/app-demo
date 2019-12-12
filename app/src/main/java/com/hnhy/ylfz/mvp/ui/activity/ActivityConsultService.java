package com.hnhy.ylfz.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.ConsultServiceHistory;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterConsultService;
import com.hnhy.ylfz.mvp.ui.widget.ToolBar;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/12.
 * 描述：咨询服务
 */
public class ActivityConsultService extends BaseActivity {
    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.tv_total_times)
    TextView mTvTotalTimes;
    @BindView(R.id.tv_used_times)
    TextView mTvUsedTimes;
    @BindView(R.id.tv_left_times)
    TextView mTvLeftTimes;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.ll_footer)
    LinearLayout mLlFooter;
    private List<ConsultServiceHistory> mServiceHistory;
    private AdapterConsultService mAdapter;
    private ConsultServiceHistory mBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_service);
        ButterKnife.bind(this);
        setLightStatusBar();
        initRecyclerView();
        mToolbar.postDelayed(() -> {
            mRlHeader.setVisibility(View.VISIBLE);
        }, 500);
        mToolbar.postDelayed(() -> {
            mLlFooter.setVisibility(View.VISIBLE);
            loadData();
        }, 1000);
    }

    private void initRecyclerView() {
        mServiceHistory = new ArrayList<>();
        mAdapter = new AdapterConsultService(mContext, mServiceHistory);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvContent.setAdapter(mAdapter);
    }

    private void loadData() {
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-26";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-26";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-25";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-15";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mAdapter.notifyDataSetChanged();
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}
