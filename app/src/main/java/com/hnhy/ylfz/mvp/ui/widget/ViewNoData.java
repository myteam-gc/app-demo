package com.hnhy.ylfz.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnhy.ylfz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/6/10.
 * 描述：无数据布局
 */
public class ViewNoData extends FrameLayout {
    @BindView(R.id.tv_hint1)
    TextView mTvHint1;
    @BindView(R.id.tv_hint2)
    TextView mTvHint2;
    @BindView(R.id.view_picture)
    View mViewPicture;
    private Context mCxt;
    private CharSequence mHint1;
    private CharSequence mHint2;
    private int mHintIconId;
    private boolean isMulLineHint;
    private int mTopMargin;
    private int mIconSize;

    public ViewNoData(@NonNull Context context) {
        this(context, null);
    }

    public ViewNoData(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewNoData(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCxt = context;
        inflate(context, R.layout.layout_no_data, this);
        ButterKnife.bind(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewNoData);
        mHintIconId = a.getResourceId(R.styleable.ViewNoData_hintIcon, R.drawable.icon_empty);
        mHint1 = a.getText(R.styleable.ViewNoData_hint1);
        mHint2 = a.getText(R.styleable.ViewNoData_hint2);
        isMulLineHint = a.getBoolean(R.styleable.ViewNoData_mulLineHint, true);
        mTopMargin = a.getDimensionPixelSize(R.styleable.ViewNoData_topIconMargin, dp2px(160));
        mIconSize = a.getDimensionPixelSize(R.styleable.ViewNoData_iconSize, dp2px(100));
        a.recycle();
        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewPicture.getLayoutParams();
        lp.setMargins(0, mTopMargin, 0, 0);
        lp.width = mIconSize;
        lp.height = mIconSize;
        mViewPicture.setBackgroundResource(mHintIconId);
        mTvHint1.setText(mHint1 == null ? "暂无" : mHint1);
        mTvHint2.setText(mHint2 == null ? "暂无" : mHint2);
        mTvHint2.setVisibility(isMulLineHint ? VISIBLE : GONE);
    }

    public void setHintIcon(@NonNull int resId) {
        mHintIconId = resId;
        mViewPicture.setBackgroundResource(mHintIconId);
    }

    public void setHint1(String hint1) {
        mHint1 = hint1;
        mTvHint1.setText(mHint1);
    }

    public void setHint1(@NonNull int resId) {
        mHint1 = mCxt.getString(resId);
        mTvHint1.setText(mHint1);
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dp(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
