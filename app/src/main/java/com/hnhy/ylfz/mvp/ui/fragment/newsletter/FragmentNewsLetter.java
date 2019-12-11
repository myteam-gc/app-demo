package com.hnhy.ylfz.mvp.ui.fragment.newsletter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hnhy.framework.frame.LazyBaseFragment;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.Constant;
import com.hnhy.ylfz.mvp.model.bean.NewsLetter;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterNewsletter;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/9.
 * 描述：国考快讯下tab内容
 */
public class FragmentNewsLetter extends LazyBaseFragment {

    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    /**
     * 0：国家政策
     * 1：业内新闻
     * 2：健康资讯
     */
    private int mType = 0;
    private List<NewsLetter>  mDatas;
    private AdapterNewsletter mAdapter;
    private NewsLetter mNewsletter;

    public static FragmentNewsLetter getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        FragmentNewsLetter letter = new FragmentNewsLetter();
        letter.setArguments(bundle);
        return letter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_list;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        mType = getArguments().getInt("type", 0);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        switch (mType){
            case 0:
                mNewsletter = new NewsLetter();
                mNewsletter.title="《空气污染（霾）人群健康防护指南》解读";
                mNewsletter.date = "2019-12-06";
                mNewsletter.picUrl = Constant.DEFAULT_PICTURE;
                mNewsletter.url = "http://www.nhc.gov.cn/jkj/s5899tg/201912/0e179fb785e14fdc834d169381b37bd5.shtml";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="《关于加强二级公立医院绩效考核工作的通知》的政策解读";
                mNewsletter.date = "2019-12-05";
                mNewsletter.picUrl = Constant.DEFAULT_PICTURE;
                mNewsletter.url = "http://www.nhc.gov.cn/yzygj/s3586/201912/efc6f880d34643c4b6df23202ce51d30.shtml";
                mDatas.add(mNewsletter);
                break;
            case 1:
                mNewsletter = new NewsLetter();
                mNewsletter.title="于学军主持召开部分省份健康扶贫和深化医改工作推进会并在宁夏调研";
                mNewsletter.date = "2019-12-09";
                mNewsletter.picUrl = Constant.DEFAULT_PICTURE;
                mNewsletter.url = "http://www.nhc.gov.cn/guihuaxxs/s10742/201912/877ffddcbce8447aad54934b65680d86.shtml";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="2019年12月国家卫生健康委在线访谈预告";
                mNewsletter.date = "2019-12-09";
                mNewsletter.picUrl = Constant.DEFAULT_PICTURE;
                mNewsletter.url = "http://www.nhc.gov.cn/wjw/xwdt/201912/787b4eb6a0ef47888e829c5a5c7c0282.shtml";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="马晓伟赴山西省调研定点扶贫工作";
                mNewsletter.date = "2019-12-06";
                mNewsletter.picUrl = Constant.DEFAULT_PICTURE;
                mNewsletter.url = "http://www.nhc.gov.cn/caiwusi/s3578c/201912/63efa0d083af45c6935e3c2a42a8d488.shtml";
                mDatas.add(mNewsletter);
                break;
            case 2:
                mNewsletter = new NewsLetter();
                mNewsletter.title="喝酒“脸红”或“脸白”,哪个酒量会更好？答案和你想的不一样 ";
                mNewsletter.date = "2019-12-09";
                mNewsletter.picUrl ="http://5b0988e595225.cdn.sohucs.com/images/20191208/6b7e88fa31814b5e9ba896ed6c451e78.jpeg";
                mNewsletter.url = "https://www.sohu.com/a/359105601_359980?scm=0.0.0.0&spm=smpc.ch24.top-news-1.2.15759418182913Q8smGP";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="可乐杀精、喝酒壮阳？天坛张勇主任告诉你男性必知的30条知识";
                mNewsletter.date = "2019-12-06";
                mNewsletter.picUrl ="http://5b0988e595225.cdn.sohucs.com/images/20191205/619c7d1cc42c43489e41563cc3eca87e.jpeg";
                mNewsletter.url = "https://www.sohu.com/a/358558869_359980?scm=0.0.0.0&spm=smpc.ch24.top-news-2.3.15759418182913Q8smGP";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="34岁年轻歌手被诊断为皮革胃，这种病到底有多严重？ ";
                mNewsletter.date = "2019-12-08";
                mNewsletter.picUrl ="http://5b0988e595225.cdn.sohucs.com/images/20191209/42658ec5f1334ff3b72c758a5e06aef8.jpeg";
                mNewsletter.url = "https://www.sohu.com/a/359138166_100038690?scm=0.0.0.0&spm=smpc.ch24.top-news-3.1.15759418182913Q8smGP";
                mDatas.add(mNewsletter);
                mNewsletter = new NewsLetter();
                mNewsletter.title="俗语“吃饭大汗，一生白干”，具体有什么含义？听听医生怎么说";
                mNewsletter.date = "2019-12-08";
                mNewsletter.picUrl ="http://5b0988e595225.cdn.sohucs.com/images/20191209/a88fb8b06748437abc6d1bbbb32149ef.jpeg";
                mNewsletter.url = "https://www.sohu.com/a/359136038_100511?scm=0.0.0.0&spm=smpc.ch24.top-news-3.4.15759418182913Q8smGP";
                mDatas.add(mNewsletter);
                break;
        }
        mAdapter = new AdapterNewsletter(mcontext,mDatas);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mcontext));
        mRcvContent.setAdapter(mAdapter);
        mViewNoData.setVisibility(mAdapter.getItemCount()>0?View.GONE:View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
