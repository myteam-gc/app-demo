package com.hnhy.ylfz.mvp.ui.activity.msg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Message;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterMessage;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/17.
 * 描述：我的消息
 */
public class ActivityMyMessage extends BaseActivity {
    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<Message> mDatas;
    private AdapterMessage mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_my_message);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        Message message = new Message();
        message.date = "2019-11-05";
        message.title = "您提交的认证申请已通过";
        message.content = "您提交的认证申请已通过";
        mDatas.add(message);
        mAdapter.notifyDataSetChanged();
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void initView() {
        mDatas = new ArrayList<>();
        mAdapter = new AdapterMessage(mContext, mDatas);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvContent.setAdapter(mAdapter);
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}
