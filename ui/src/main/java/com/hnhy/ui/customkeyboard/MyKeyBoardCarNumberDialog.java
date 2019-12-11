package com.hnhy.ui.customkeyboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hnhy.ui.R;

/**
 * Created by guc on 2018/8/9.
 * 描述：
 */
public class MyKeyBoardCarNumberDialog extends Dialog {
    private KeyBoardCarNumberView myKeyBoardView;
    private EditText mEtInput;
    private RadioButton mTvProvince, mTvLetter, mTvNumber;
    private RadioGroup mRg;
    private boolean showPreView = true;

    public MyKeyBoardCarNumberDialog(@NonNull Context context) {
        this(context, R.style.KeyboardDialog);
    }

    public MyKeyBoardCarNumberDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_keyboard_car_number);
        initView();
    }

    public MyKeyBoardCarNumberDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.layout_keyboard_car_number);
        initView();
    }

    public MyKeyBoardCarNumberDialog setInput(EditText etInput) {
        this.mEtInput = etInput;
        myKeyBoardView.setStrReceiver(mEtInput);
        return this;
    }

    public MyKeyBoardCarNumberDialog setShowPreView(boolean showPreView) {
        this.showPreView = showPreView;
        myKeyBoardView.setShowPreview(showPreView);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        p.dimAmount = 0f;
        getWindow().setAttributes(p);
        //不获取焦点
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


    }

    private void initView() {
        myKeyBoardView = findViewById(R.id.my_kv);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        myKeyBoardView.setOnInputFinishListener(new KeyBoardCarNumberView.OnInputFinishListener() {
            @Override
            public void onFinish() {
                MyKeyBoardCarNumberDialog.this.dismiss();
            }
        });

        mTvProvince = findViewById(R.id.tv_province);
        mTvLetter = findViewById(R.id.tv_letter);
        mTvNumber = findViewById(R.id.tv_number);
        mRg = findViewById(R.id.radio_group);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tv_province) {
                    myKeyBoardView.changeMode(0);
                } else if (checkedId == R.id.tv_letter) {
                    myKeyBoardView.changeMode(1);
                } else if (checkedId == R.id.tv_number) {
                    myKeyBoardView.changeMode(2);
                }
            }
        });

        myKeyBoardView.setOnChineseInputListener(new KeyBoardCarNumberView.OnChineseInputListener() {
            @Override
            public void onChineseInput() {
                if (mEtInput.getText().toString().trim().equals("")) {
                    myKeyBoardView.changeMode(1);
                    mRg.check(R.id.tv_letter);
                }
            }
        });
    }
}
