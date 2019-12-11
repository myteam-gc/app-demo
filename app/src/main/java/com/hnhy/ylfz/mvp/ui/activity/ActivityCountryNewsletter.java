package com.hnhy.ylfz.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ui.adapter.ViewPageAdapter;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.fragment.newsletter.FragmentNewsLetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/9.
 * 描述：国考快讯
 */
public class ActivityCountryNewsletter extends BaseActivity {

    @BindView(R.id.tab_title)
    TabLayout mTabTitle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ViewPageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_country_newsletter);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mViewpager.setOffscreenPageLimit(3);
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles,getResources().getStringArray(R.array.titles_country_newsletter));
        mFragments = new ArrayList<>();
        mFragments.add(FragmentNewsLetter.getInstance(0));
        mFragments.add(FragmentNewsLetter.getInstance(1));
        mFragments.add(FragmentNewsLetter.getInstance(2));
        mAdapter = new ViewPageAdapter(getSupportFragmentManager(),mFragments,mTitles);
        mTabTitle.setupWithViewPager(mViewpager);
        mViewpager.setAdapter(mAdapter);
    }

}
