package com.hnhy.ylfz.mvp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2019/7/15.
 * 描述：登录
 */
public class ActivityLogin extends BaseActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_show_password)
    ImageView mIvShowPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setLightStatusBar();
        initView();
    }

    private void initView() {
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIvDelete.setVisibility(TextUtils.isEmpty(s.toString()) ? View.INVISIBLE : View.VISIBLE);
                mBtnLogin.setEnabled(!TextUtils.isEmpty(s.toString()));
            }
        });
    }

    @OnClick({R.id.iv_delete, R.id.tv_forget_password, R.id.btn_login, R.id.tv_register_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                clearUsername();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_login:
                break;
            case R.id.tv_register_now:
                startActivity(new Intent(this, ActivityRegister.class));
                break;
        }
    }

    private void clearUsername() {
        mEtUsername.setText("");
    }

}
