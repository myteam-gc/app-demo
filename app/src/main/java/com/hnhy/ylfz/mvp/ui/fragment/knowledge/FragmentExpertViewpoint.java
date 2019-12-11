package com.hnhy.ylfz.mvp.ui.fragment.knowledge;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hnhy.framework.frame.LazyBaseFragment;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Forum;
import com.hnhy.ylfz.mvp.model.bean.Viewpoint;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterForum;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterViewpoint;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterViewpoint2;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterViewpoint3;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.OnWindowDetachedListener;
import org.salient.artplayer.VideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/10.
 * 描述：专家视点
 */
public class FragmentExpertViewpoint extends LazyBaseFragment {
    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private List<Viewpoint> mDatas;
//    private AdapterViewpoint mAdapter;
    private AdapterViewpoint mAdapter;
    public static FragmentExpertViewpoint getInstance() {
        FragmentExpertViewpoint viewpoint = new FragmentExpertViewpoint();
        return viewpoint;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_list;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        //假数据
        Viewpoint viewpoint = new Viewpoint();
        viewpoint.title = "[新闻直播间]国家卫健委 全国目前无其他新增鼠疫病例报告";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201911/52c264d7a5fa417da35de9fac6270064/files/a2479589339246268c3b490fda077ab3.mp4";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "[东方新闻]国家卫健委将出台措施完善养老服务体系";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201910/8a38cfd5348d4ffb9bd41536685d4b23/files/db90a123cc7643fda09252d0485bd2d4.mp4";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "我国即将进入流感流行季 每年致5-10%成人 20-30%儿童发病";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201911/5f2b020200d343de88128b399707adc7/files/2759585ec3e54d41a78fa33e08b10234.mp4";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "“光明行”援外项目使超过万名患者重获光明";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201911/afbb4e141fb54f6384a657c3ca1297ab/files/da9382a0d58a442ebe79f7c71483493c.mp4";
        mDatas.add(viewpoint);

        mAdapter = new AdapterViewpoint(mcontext,mDatas);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mcontext));
        mRcvContent.setAdapter(mAdapter);
        mViewNoData.setVisibility(mAdapter.getItemCount()>0?View.GONE:View.VISIBLE);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("onRefresh", "onRefresh: ");
                refreshLayout.finishRefresh();
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
