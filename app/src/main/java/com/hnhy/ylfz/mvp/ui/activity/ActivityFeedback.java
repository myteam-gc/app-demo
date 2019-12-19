package com.hnhy.ylfz.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/18.
 * 描述：我的反馈
 */
public class ActivityFeedback extends BaseActivity {
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_my_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBtnCommit.setEnabled(!TextUtils.isEmpty(s.toString()));
            }
        });
    }
}
