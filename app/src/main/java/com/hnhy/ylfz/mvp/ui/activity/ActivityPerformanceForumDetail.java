package com.hnhy.ylfz.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;

/**
 * 绩效论坛详情
 */
public class ActivityPerformanceForumDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_forum_detail);
    }

    public static void jump(Context mContext){
        mContext.startActivity(new Intent(mContext,ActivityPerformanceForumDetail.class));
    }
}
