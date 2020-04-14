package com.hnhy.ylfz.mvp.ui.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.system.SystemImagePick;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.widget.CornerImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善信息
 */
public class ActivityImprovingInfo extends BaseActivity {

    @BindView(R.id.iv_portrait)
    CornerImageView mIvPortrait;
    @BindView(R.id.iv_camera)
    ImageView mIvCamera;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_password_confirm)
    EditText mEtPasswordConfirm;
    @BindView(R.id.btn_finish)
    Button mBtnFinish;
    private boolean hasInputUsername = false;
    private boolean hasInputPassword = false;
    private boolean hasInputConfirmPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improving_info);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.iv_portrait, R.id.iv_camera, R.id.btn_finish, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_portrait:
                getSystem(SystemImagePick.class).pickImage(SystemImagePick.TYPE_TAKE_PHOTO, (String[] imagePaths) -> {
                    Logger.e("pickImage:path:", imagePaths[0]);
                    Glide.with(mContext).load(imagePaths[0]).into(mIvPortrait);
                }, true);
                break;
            case R.id.iv_camera:
                break;
            case R.id.btn_finish:
                break;
            case R.id.iv_delete:
                mEtUsername.setText("");
                break;
        }
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
                hasInputUsername = !TextUtils.isEmpty(s.toString());
                mIvDelete.setVisibility(hasInputUsername ? View.VISIBLE : View.INVISIBLE);
                setFinishBtnClickable();
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasInputPassword = !TextUtils.isEmpty(s.toString());
                setFinishBtnClickable();
            }
        });
        mEtPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasInputConfirmPwd = !TextUtils.isEmpty(s.toString());
                setFinishBtnClickable();
            }
        });
    }

    //完成按钮是否可点击
    private void setFinishBtnClickable() {
        mBtnFinish.setEnabled(hasInputConfirmPwd && hasInputPassword && hasInputUsername);
    }
}
