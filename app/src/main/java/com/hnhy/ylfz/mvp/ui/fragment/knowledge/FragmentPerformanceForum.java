package com.hnhy.ylfz.mvp.ui.fragment.knowledge;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hnhy.framework.frame.LazyBaseFragment;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.Constant;
import com.hnhy.ylfz.mvp.model.bean.Forum;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterForum;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by guc on 2019/12/10.
 * 描述：知识--绩效论坛
 */
public class FragmentPerformanceForum extends LazyBaseFragment {

    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private List<Forum>  mDatas;
    private AdapterForum mAdapter;

    public static FragmentPerformanceForum getInstance() {
        FragmentPerformanceForum letter = new FragmentPerformanceForum();
        return letter;
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
        Forum forum = new Forum();
        forum.title = "出院患者四级手术比例";
        forum.picUrl = Constant.DEFAULT_PICTURE;
        forum.date = "2019-11-10 06:46:47";
        forum.content = "　　【指标属性】定量指标，国家监测指标。\n" +
                "　　【计量单位】%\n" +
                "　　【指标定义】\n" +
                "　　考核年度出院患者总人次中施行手术治疗台次数所占的比例。\n" +
                "　　【计算方法】\n" +
                "　　出院患者手术占比=出院患者手术台次数/同期患者出院总人次数×100%\n" +
                "　　【指标说明】\n" +
                "　　（1）分子：此处出院患者手术台次数是指出院手术人数，即同一次住院就诊期间患有同一疾病或不同疾病施行多次手术者，按1人统计。统计单位以人数计算，总数为手术和介入治疗人数累加求和。从数据提取和数据质量角度考虑，本次考核来源于病案首页的手术台次数均按手术人数进行统计。\n" +
                "　　（2）分母：此处同期出院患者总人次数是指出院人数，即考核年度内所有住院后出院患者的人数。包括医嘱离院、医嘱转其他医疗机构、非医嘱离院、死亡及其他人数，不含家庭病床撤床人数。\n" +
                "　　（3）根据《医疗机构手术分级管理办法（试行）》（卫办医政发〔2012〕94号）规定，手术是指医疗机构及其医务人员使用手术器械在人体局部进行操作，以去除病变组织、修复损伤、移植组织或器官、植入医疗器械、缓解病痛、改善机体功能或形态等为目的的诊断或者治疗措施。\n" +
                "　　（4）手术和介入治疗目录详见《手术操作分类代码国家临床版2.0》中的类别标识。\n" +
                "　　【指标意义】\n" +
                "　　手术量尤其是疑难复杂手术的数量与医院的规模，人员、设备、设施等综合诊疗技术能力，临床管理流程成正相关，鼓励三级医院优质医疗资源服务于疑难危重患者，尤其是能够提供安全有保障的高质量医疗技术服务。\n" +
                "　　【指标导向】逐步提高。\n" +
                "　　【数据来源】病案首页。\n" +
                "　　【指标解释】国家卫生健康委病案管理质量控制中心。";
        mDatas.add(forum);
        mAdapter = new AdapterForum(mcontext,mDatas);
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
