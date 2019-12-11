package com.hnhy.ui.customkeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by guc on 2018/7/11.
 * 描述：自定义输入键盘工具
 */
public class KeyBoardCarNumberUtil {
    private static MyKeyBoardCarNumberDialog dialog;

    public static void showKeyBoard(final Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        //点击不弹出系统软键盘
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.setInput(editText);
        } else {
            dialog = new MyKeyBoardCarNumberDialog(activity).setInput(editText);
        }
        dialog.setShowPreView(false);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    public static boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    /**
     * 隐藏
     */
    public static void hideKeyBoard() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
