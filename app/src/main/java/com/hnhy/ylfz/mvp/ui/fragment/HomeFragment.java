package com.hnhy.ylfz.mvp.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hnhy.framework.frame.BaseFragment;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.activity.ActivityCountryNewsletter;
import com.hnhy.ylfz.mvp.ui.activity.ActivityDetailInBrowser;
import com.hnhy.ylfz.mvp.ui.activity.ActivityIndexMonitor;
import com.hnhy.ylfz.mvp.ui.widget.LooperTextView;
import com.hnhy.ylfz.mvp.ui.widget.ViewCounterTips;
import com.hnhy.ylfz.mvp.ui.widget.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/5.
 * 描述：
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner)
    Banner mBanner;
    Unbinder unbinder;
    @BindView(R.id.tv_monitor_tips)
    LooperTextView mTvMonitorTips;
    @BindView(R.id.counter_tips)
    ViewCounterTips mCounterTips;
    @BindView(R.id.ll_country_newsletter)
    LinearLayout mContainerCountryNewsletter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        List<String> images = new ArrayList<String>() {
            {
                add("https://img.51miz.com/Element/00/58/81/79/73ede7c5_E588179_39772d60.jpg!/quality/90/unsharp/true/compress/true/format/jpg");
                add("https://img.51miz.com/Element/00/59/31/31/517f23f9_E593131_a0cd1b59.jpg!/quality/90/unsharp/true/compress/true/format/jpg");
                add("https://img.51miz.com/Element/00/59/47/06/40456783_E594706_33c392a8.jpg!/quality/90/unsharp/true/compress/true/format/jpg");
            }
        };
        mBanner.setImages(images).setAutoPlay(true).start();
        mBanner.setOnPageClickListener((index) ->
                showToast("点击了" + index)
        );

        mTvMonitorTips.setTipList(images);
        loadCountryNewsletter();
        mCounterTips.setEndDate(2019, 12, 31);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_more, R.id.ll_index_monitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_index_monitor://指标监控详情
                startActivity(new Intent(mContext, ActivityIndexMonitor.class));
                break;
            case R.id.tv_more://国考快讯- -更多
                startActivity(new Intent(mContext, ActivityCountryNewsletter.class));
                break;
        }
    }

    private void loadCountryNewsletter() {
        mContainerCountryNewsletter.removeAllViews();
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_country_newsletter, null);
            ImageView imageView = view.findViewById(R.id.iv_picture);
            getSystem(SystemImageLoader.class).displayImage(this, imageView, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575867592092&di=7c225e7a452b0b619860564dde0d6365&imgtype=0&src=http%3A%2F%2Ffile03.16sucai.com%2F2016%2F10%2F1100%2F16sucai_p20161022092_284.JPG");
            mContainerCountryNewsletter.addView(view);

            view.setOnClickListener((v) -> {
                ActivityDetailInBrowser.showDetail(mContext, "http://www.nhc.gov.cn/lljks/zcwj2/201904/ecfa94a982a5470a8e9ccd08183dd789.shtml");
            });
        }
    }
}
