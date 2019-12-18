package com.hnhy.ylfz.mvp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ui.NoScrollViewPager;
import com.hnhy.ui.adapter.ViewPageAdapter;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.activity.msg.ActivityMyMessage;
import com.hnhy.ylfz.mvp.ui.fragment.HomeFragment;
import com.hnhy.ylfz.mvp.ui.fragment.KnowledgeFragment;
import com.hnhy.ylfz.mvp.ui.fragment.MineFragment;
import com.hnhy.ylfz.mvp.ui.fragment.MissionFragment;
import com.hnhy.ylfz.mvp.ui.widget.ToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_content)
    NoScrollViewPager mVpContent;
    @BindView(R.id.nav_view)
    BottomNavigationView mNavView;
    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNavView.setLabelVisibilityMode(1);
        initView();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new KnowledgeFragment());
        fragments.add(new MissionFragment());
        fragments.add(new MineFragment());
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments);
        mVpContent.setAdapter(adapter);
        mVpContent.setOffscreenPageLimit(4);
        mNavView.setOnNavigationItemSelectedListener(this);
        mToolbar.setOnRightClickedListener(() -> startActivity(new Intent(mContext, ActivityMyMessage.class)));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int pos = 0;
        int titleId = R.string.home;
        switch (menuItem.getItemId()) {
            case R.id.tab_one:
                pos = 0;
                titleId = R.string.app_name;
                break;
            case R.id.tab_two:
                pos = 1;
                titleId = R.string.knowledge;
                break;
            case R.id.tab_three:
                pos = 2;
                titleId = R.string.mission;
                break;
            case R.id.tab_four:
                pos = 3;
                titleId = R.string.mine;
                break;
        }
        if (mCurrentPage == pos) return true;
        mCurrentPage = pos;
        mToolbar.setTitle(titleId);
        mVpContent.setCurrentItem(mCurrentPage);
        return true;
    }
}
