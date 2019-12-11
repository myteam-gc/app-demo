package com.hnhy.framework.system;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnhy.framework.Engine;
import com.hnhy.framework.R;
import com.hnhy.framework.frame.BaseSystem;

public class SystemWaterMark extends BaseSystem {

    private static final String VIEW_TAG = "view_tag";
    private boolean mEnable;

    private String mWaterMarkText;

    @Override
    protected void init() {

    }

    @Override
    protected void destroy() {
        mWaterMarkText = null;
    }

    public void onActivityStart() {
        ViewGroup rootView = getSystem(SystemPager.class).getCurrActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if (mEnable && !TextUtils.isEmpty(mWaterMarkText) && rootView.findViewWithTag(VIEW_TAG) == null) {
            View waterView = LayoutInflater.from(Engine.getInstance().mContext).inflate(R.layout.layout_watermark, null);
            waterView.setTag(VIEW_TAG);
            ((TextView) waterView.findViewById(R.id.tv_1)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_2)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_3)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_4)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_5)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_6)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_7)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_8)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_9)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_10)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_11)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_12)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_13)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_14)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_15)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_16)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_17)).setText(mWaterMarkText);
            ((TextView) waterView.findViewById(R.id.tv_18)).setText(mWaterMarkText);
            rootView.addView(waterView);
        }
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public void setWaterMarkText(String textStr) {
        mWaterMarkText = textStr;
    }
}
