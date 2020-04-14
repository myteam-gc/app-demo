package com.hnhy.ylfz.mvp.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ylfz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class ActivityRegister extends BaseActivity {


    private static final long MILLIS_IN_FUTURE = 60 * 1000;
    private static final long INTERVAL = 1000;
    /**
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     */
    private static final String REG_PHONE = "^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}$";
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.et_username)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtCode;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    private CountDownTimer mTimeCounter;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initTimeCounter();
    }

    @OnClick({R.id.iv_delete, R.id.btn_next, R.id.tv_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                clearPhone();
                break;
            case R.id.btn_next:
                startActivity(new Intent(this, ActivityImprovingInfo.class));
                break;
            case R.id.tv_get_code:
                startGetCode();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimeCounter != null) {
            mTimeCounter.cancel();
        }
        super.onDestroy();
    }

    private void initView() {
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIvDelete.setVisibility(TextUtils.isEmpty(s.toString()) ? View.INVISIBLE : View.VISIBLE);
                mBtnNext.setEnabled(!TextUtils.isEmpty(s.toString()));
            }
        });
    }

    private void startGetCode() {
        mPhone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            FrameworkUtils.Toast.showShort("请输入手机号");
        } else if (FrameworkUtils.isMatch(REG_PHONE, mPhone)) {
            mTimeCounter.start();
        } else {
            FrameworkUtils.Toast.showShort("请输入正确的手机号");
        }

    }

    /**
     * 获取验证码
     */
    private void initTimeCounter() {
        mTimeCounter = new CountDownTimer(MILLIS_IN_FUTURE, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvGetCode.setTextColor(Color.parseColor("#C9CDD9"));
                mTvGetCode.setClickable(false);
                mTvGetCode.setText(getString(R.string.re_get_code_timer, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mTvGetCode.setText(getString(R.string.re_get_code));
                mTvGetCode.setClickable(true);
                mTvGetCode.setTextColor(Color.parseColor("#365AFF"));
            }
        };
    }

    private void clearPhone() {
        mEtPhone.setText("");
    }

}
