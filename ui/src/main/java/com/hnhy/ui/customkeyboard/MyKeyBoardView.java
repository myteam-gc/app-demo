package com.hnhy.ui.customkeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.hnhy.ui.R;

import java.util.List;

/**
 * Created by guc on 2018/8/9.
 * 描述：自定义键盘
 */
public class MyKeyBoardView extends KeyboardView {
    private int mLabelTextSize;

    public MyKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.MyKeyBoardView, 0, 0);
        int n = a.getIndexCount();
        mLabelTextSize = (int) a.getDimension(R.styleable.MyKeyBoardView_labelTextSize, 18f);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            paint.setTypeface(font);
            paint.setAntiAlias(true);
            for (Keyboard.Key key : keys) {
                if (key.codes[0] == -4) {
                    Drawable dr = getContext().getResources().getDrawable(R.drawable.keyboard_blue);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                    if (key.label != null) {
                        paint.setColor(getContext().getResources().getColor(R.color.colorWhite));
                        paint.setTextSize(mLabelTextSize);
                        Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
                        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
                    }
                }

            }
        }
    }
}
