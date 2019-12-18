package com.hnhy.ylfz.mvp.ui.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Message;
import com.hnhy.ylfz.mvp.ui.widget.CornerImageView;
import com.hnhy.ylfz.mvp.ui.widget.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/17.
 * 描述：通知详情
 */
public class ActivityMessageDetail extends BaseActivity {
    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_img)
    CornerImageView mIvImg;
    @BindView(R.id.tv_message)
    TextView mTvMessage;

    public static void jump(Context context, Message message) {
        Intent intent = new Intent(context, ActivityMessageDetail.class);
        intent.putExtra("data", message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        loadData();
    }

    private void loadData() {
        Message message = getIntent().getParcelableExtra("data");
        if (message == null) {
            this.finish();
        }
        mTvTitle.setText(message.title);
        mTvMessage.setText(message.content);
        getSystem(SystemImageLoader.class).displayImage(this, mIvImg, message.picUrl);

    }
}
