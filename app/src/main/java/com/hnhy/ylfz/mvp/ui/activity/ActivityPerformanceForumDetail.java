package com.hnhy.ylfz.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ui.ExpandableTextView;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Forum;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绩效论坛详情
 */
public class ActivityPerformanceForumDetail extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.etv_content)
    ExpandableTextView mEtvContent;

    public static void jump(Context mContext, Forum forum) {
        Intent intent = new Intent(mContext, ActivityPerformanceForumDetail.class);
        intent.putExtra("data", forum);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_performance_forum_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Forum forum = getIntent().getParcelableExtra("data");
        if (forum == null) this.finish();
        mTvTitle.setText(forum.title);
        mEtvContent.setText(forum.content);
    }

}
