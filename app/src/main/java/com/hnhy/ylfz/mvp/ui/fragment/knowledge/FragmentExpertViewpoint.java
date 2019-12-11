package com.hnhy.ylfz.mvp.ui.fragment.knowledge;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hnhy.framework.frame.LazyBaseFragment;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.Constant;
import com.hnhy.ylfz.mvp.model.bean.Viewpoint;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterViewpoint3;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.salient.artplayer.MediaPlayerManager;

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
    private AdapterViewpoint3 mAdapter;

    public static FragmentExpertViewpoint getInstance() {
        FragmentExpertViewpoint viewpoint = new FragmentExpertViewpoint();
        return viewpoint;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            MediaPlayerManager.instance().releasePlayerAndView(mcontext);
        }
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
        generateData();
        mAdapter = new AdapterViewpoint3(mcontext, mDatas);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mcontext));
        mRcvContent.setAdapter(mAdapter);
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("onRefresh", "onRefresh: ");
                refreshLayout.finishRefresh();
            }
        });
    }

    private void generateData() {
        //假数据
        Viewpoint viewpoint = new Viewpoint();
        viewpoint.title = "2020，向火星进发！中国探测火星飞控团队惊艳亮相";
        viewpoint.videoUrl = "http://vd3.bdstatic.com/mda-jmak78f83ncdwp4j/sc/mda-jmak78f83ncdwp4j.mp4";
        viewpoint.image = Constant.DEFAULT_PICTURE;
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "禹神传之寻找神力--云南虫谷 定档预告";
        viewpoint.videoUrl = "http://vfx.mtime.cn/Video/2018/05/23/mp4/180523094716353341.mp4";
        viewpoint.image = "http://img5.mtime.cn/mg/2018/05/23/094631.41151478.jpg";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "江湖儿女 预告片";
        viewpoint.videoUrl = "http://vfx.mtime.cn/Video/2018/05/24/mp4/180524090638262382.mp4";
        viewpoint.image = "http://img5.mtime.cn/mg/2018/05/24/090636.74156218.jpg";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "一生有你 先导预告";
        viewpoint.videoUrl = "http://vfx.mtime.cn/Video/2018/06/21/mp4/180621213804379695.mp4";
        viewpoint.image = "http://img5.mtime.cn/mg/2018/06/21/214739.42705452.jpg";
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "[东方新闻]国家卫健委将出台措施完善养老服务体系";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201910/8a38cfd5348d4ffb9bd41536685d4b23/files/db90a123cc7643fda09252d0485bd2d4.mp4";
        viewpoint.image = Constant.DEFAULT_PICTURE;
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "我国即将进入流感流行季 每年致5-10%成人 20-30%儿童发病";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201911/5f2b020200d343de88128b399707adc7/files/2759585ec3e54d41a78fa33e08b10234.mp4";
        viewpoint.image = Constant.DEFAULT_PICTURE;
        mDatas.add(viewpoint);
        viewpoint = new Viewpoint();
        viewpoint.title = "“光明行”援外项目使超过万名患者重获光明";
        viewpoint.videoUrl = "http://www.nhc.gov.cn/wjw/spxw/201911/afbb4e141fb54f6384a657c3ca1297ab/files/da9382a0d58a442ebe79f7c71483493c.mp4";
        viewpoint.image = Constant.DEFAULT_PICTURE;
        mDatas.add(viewpoint);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
