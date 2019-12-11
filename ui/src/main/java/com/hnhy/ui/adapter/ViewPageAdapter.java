package com.hnhy.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by guc on 2018/4/23.
 * 描述：引导页ViewPageAdapter
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> mTitles;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> mTitles) {
        super(fm);
        this.fragments = fragments;
        this.mTitles = mTitles;
    }

    public void setTitles(List<String> titles) {
        this.mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? null : mTitles.get(position % mTitles.size());
    }
}
