package com.hnhy.ylfz.mvp.ui.activity.noble;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hnhy.ylfz.R;
import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ui.customkeyboard.KeyBoardCarNumberUtil;

/**
 * Created by guc on 2019/7/15.
 * 描述：测试
 */
public class ActivityTest extends BaseActivity {
    private EditText mEtInput;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mEtInput = findViewById(R.id.et_input);
        mEtInput.setOnTouchListener((View v, MotionEvent event) -> {
            KeyBoardCarNumberUtil.showKeyBoard(ActivityTest.this, mEtInput);
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (KeyBoardCarNumberUtil.isShowing()) {
            KeyBoardCarNumberUtil.hideKeyBoard();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyBoardCarNumberUtil.hideKeyBoard();
    }
}
