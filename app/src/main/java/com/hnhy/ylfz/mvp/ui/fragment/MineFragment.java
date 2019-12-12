package com.hnhy.ylfz.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseFragment;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.activity.ActivityConsultService;
import com.hnhy.ylfz.mvp.ui.activity.ActivityDetailInBrowser;
import com.hnhy.ylfz.mvp.ui.activity.user.ActivityModifyPwd;
import com.hnhy.ylfz.mvp.ui.widget.NiceImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/5.
 * 描述：我的
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_portrait)
    NiceImageView mIvPortrait;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_certified)
    TextView mTvCertified;
    Unbinder unbinder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_consult, R.id.rl_modify_pwd, R.id.rl_feedback, R.id.rl_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_consult:
                startActivity(new Intent(mContext, ActivityConsultService.class));
                break;
            case R.id.rl_modify_pwd:
                ActivityModifyPwd.jump(mContext);
                break;
            case R.id.rl_feedback:
                break;
            case R.id.rl_about:
                ActivityDetailInBrowser.showDetail(mContext, "http://huiyunit.com/about.html#0", "关于");
                break;
        }
    }
}
