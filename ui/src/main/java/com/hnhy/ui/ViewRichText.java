package com.hnhy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;


/**
 * Created by guc on 2019/6/11.
 * 描述：说明 和 内容同行 但样式不一样
 */
public class ViewRichText extends AppCompatTextView {
    private String mTitle;
    private String mContent;
    private int mTitleTextColor;
    private int mContentTextColor;

    public ViewRichText(Context context) {
        this(context, null);
    }

    public ViewRichText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewRichText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewRichText);
        mTitle = a.getString(R.styleable.ViewRichText_title);
        mContent = a.getString(R.styleable.ViewRichText_content);
        if (mContent == null) mContent = "暂无";
        mTitleTextColor = a.getColor(R.styleable.ViewRichText_titleTextColor, Color.parseColor("#666666"));
        mContentTextColor = a.getColor(R.styleable.ViewRichText_contentTextColor, Color.parseColor("#333333"));
        a.recycle();
        setContent(mContent);
    }

    public void setContent(String content) {
        if (mTitle == null || "".equals(mTitle)) {
            setText(content);
        } else {
            mContent = content == null ? "暂无" : content;
            String str = mTitle + mContent;
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(mTitleTextColor), 0, mTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mContentTextColor), mTitle.length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }

    }
}
