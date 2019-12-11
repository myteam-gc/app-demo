package com.hnhy.ylfz.mvp.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.contract.ContractModifyPwd;
import com.hnhy.ylfz.mvp.presenter.PresenterModifyPwd;
import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.system.SystemPager;
import com.hnhy.framework.system.net.Response;
import com.hnhy.framework.util.FrameworkUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2019/7/16.
 * 描述：修改密码界面
 */
public class ActivityModifyPwd extends BaseActivity implements ContractModifyPwd.View {

    @BindView(R.id.et_pwd_old)
    EditText mEtOld;
    @BindView(R.id.et_pwd_new)
    EditText mEtNew;
    @BindView(R.id.et_pwd_confirm)
    EditText mEtConfirm;
    private PresenterModifyPwd mPresenter;
    private String mOldPwd;
    private String mNewPwd;
    public static void jump(Context context){
        Intent intent = new Intent(context,ActivityModifyPwd.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        mPresenter = new PresenterModifyPwd(this);
    }

    @OnClick(R.id.btn_pwd_save)
    public void onViewClicked() {
        if (checkValid()) {
            mPresenter.modifyPassword(this, getCommitParams());
        }
    }

    @Override
    public void onShowLoading(boolean isShow, String msg) {
        showLoadingDialog(isShow, TextUtils.isEmpty(msg) ? getString(R.string.tips_is_committing) : msg);
    }

    @Override
    public void onModifySuccess(String msg) {
        FrameworkUtils.Toast.showShort("修改密码成功，请重新登录");
        getSystem(SystemPager.class).finishAllActivity();
        startActivity(new Intent(this, ActivityLogin.class));
        this.finish();
    }

    @Override
    public void onModifyFailure(Response response) {
        FrameworkUtils.Toast.showShort(response.mMessage);
    }

    /**
     * 检查参数是否合格
     *
     * @return true ：通过 false:不通过
     */
    private boolean checkValid() {
        String mNewPwdConfirm;
        mOldPwd = mEtOld.getText().toString().trim();
        if (TextUtils.isEmpty(mOldPwd)) {
            FrameworkUtils.Toast.showShort(R.string.str_pwd_hint_old);
            return false;
        }
        mNewPwd = mEtNew.getText().toString().trim();
        if (TextUtils.isEmpty(mNewPwd)) {
            FrameworkUtils.Toast.showShort(R.string.str_pwd_hint_new);
            return false;
        }
        mNewPwdConfirm = mEtConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(mNewPwdConfirm)) {
            FrameworkUtils.Toast.showShort(R.string.str_pwd_hint_confirm);
            return false;
        }
        if (!mNewPwd.equals(mNewPwdConfirm)) {
            FrameworkUtils.Toast.showShort("请填写相同的新密码");
            return false;
        }
        if (mNewPwd.equals(mOldPwd)) {
            FrameworkUtils.Toast.showShort("新密码不能与原密码相同");
            return false;
        }
        // TODO: 2018/9/11 delay handle
        if (!FrameworkUtils.isMatch("^\\w{6,20}$", mNewPwd)) {
            FrameworkUtils.Toast.showShort("密码格式不对，请输入6-20位字母或数字组合");
            return false;
        }
        return true;
    }

    /**
     * "oldPass": "旧密码",
     * "newPass": "新密码"
     *
     * @return 参数
     */
    private Map<String, String> getCommitParams() {
        Map<String, String> params = new HashMap<>();
        params.put("oldPass", mOldPwd);
        params.put("newPass", mNewPwd);
        return params;
    }

}
