package com.hnhy.ylfz.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnhy.framework.frame.BaseFragment;
import com.hnhy.ui.adapter.ViewPageAdapter;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.fragment.knowledge.FragmentExpertViewpoint;
import com.hnhy.ylfz.mvp.ui.fragment.knowledge.FragmentPerformanceForum;
import com.hnhy.ylfz.mvp.ui.fragment.newsletter.FragmentNewsLetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/5.
 * 描述：知识
 */
public class KnowledgeFragment extends BaseFragment {
    @BindView(R.id.tab_title)
    TabLayout mTabTitle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ViewPageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        mViewpager.setOffscreenPageLimit(3);
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles,getResources().getStringArray(R.array.titles_knowledge));
        mFragments = new ArrayList<>();
        mFragments.add(FragmentExpertViewpoint.getInstance());
        mFragments.add(FragmentNewsLetter.getInstance(0));
        mFragments.add(FragmentPerformanceForum.getInstance());

        mAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(),mFragments,mTitles);
        mTabTitle.setupWithViewPager(mViewpager);
        mViewpager.setAdapter(mAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
