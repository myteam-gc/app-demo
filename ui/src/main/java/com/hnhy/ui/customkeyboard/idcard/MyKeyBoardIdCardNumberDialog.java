package com.hnhy.ui.customkeyboard.idcard;

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
public class MyKeyBoardIdCardNumberDialog extends Dialog {
    private KeyBoardIdCardNumberView myKeyBoardView;
    private EditText mEtInput;
    private RadioButton mTvProvince, mTvLetter, mTvNumber;
    private RadioGroup mRg;
    private boolean showPreView = true;
    private KeyBoardIdCardNumberView.OnInputFinishListener mFinishListener;

    public MyKeyBoardIdCardNumberDialog(@NonNull Context context) {
        this(context, R.style.KeyboardDialog);
    }

    public MyKeyBoardIdCardNumberDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_keyboard_id_card_number);
        initView();
    }

    public MyKeyBoardIdCardNumberDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.layout_keyboard_id_card_number);
        initView();
    }

    public MyKeyBoardIdCardNumberDialog setInput(EditText etInput, KeyBoardIdCardNumberView.OnInputFinishListener listener) {
        this.mEtInput = etInput;
        myKeyBoardView.setStrReceiver(mEtInput);
        mFinishListener = listener;
        return this;
    }

    public MyKeyBoardIdCardNumberDialog setShowPreView(boolean showPreView) {
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
        myKeyBoardView.setOnInputFinishListener(new KeyBoardIdCardNumberView.OnInputFinishListener() {
            @Override
            public void onFinish() {
                MyKeyBoardIdCardNumberDialog.this.dismiss();
                if (mFinishListener != null) {
                    mFinishListener.onFinish();
                }
            }
        });

        mTvProvince = findViewById(R.id.tv_province);
        mTvLetter = findViewById(R.id.tv_letter);
        mTvNumber = findViewById(R.id.tv_number);

    }
}
