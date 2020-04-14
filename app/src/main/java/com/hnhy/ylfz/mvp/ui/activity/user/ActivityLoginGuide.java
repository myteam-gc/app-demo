package com.hnhy.ylfz.mvp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityLoginGuide extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guide);
        ButterKnife.bind(this);
        setLightStatusBar();
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登录
                Intent it = new Intent(this, ActivityLogin.class);
                if (getIntent().getExtras() != null) {
                    it.putExtras(getIntent().getExtras());
                }
                startActivity(it);
                break;
            case R.id.btn_register://注册
                startActivity(new Intent(this, ActivityRegister.class));
                break;
        }
    }
}
