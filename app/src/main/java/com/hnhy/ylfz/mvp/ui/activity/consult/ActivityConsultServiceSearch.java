package com.hnhy.ylfz.mvp.ui.activity.consult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemPager;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.SharedPreferencesManager;
import com.hnhy.ylfz.mvp.model.bean.BeanSearch;
import com.hnhy.ylfz.mvp.model.bean.ConsultServiceHistory;
import com.hnhy.ylfz.mvp.ui.adapter.AdapterConsultService;
import com.hnhy.ylfz.mvp.ui.widget.ViewNoData;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guc on 2019/12/12.
 * 描述：服务历史搜索
 */
public class ActivityConsultServiceSearch extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener {
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvContent;
    @BindView(R.id.view_no_data)
    ViewNoData mViewNoData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private boolean hasShowHistory;
    private PopupWindow popupWindow;
    private List<BeanSearch> mDatas;
    private CommonRecycleAdapter<BeanSearch> mHistoryAdapter;

    private List<ConsultServiceHistory> mServiceHistory;
    private AdapterConsultService mAdapter;
    private ConsultServiceHistory mBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_consult_service_search);
        ButterKnife.bind(this);
        initView();
        initSearchHistory();
        initRecyclerView();
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        String key = mEtSearch.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            FrameworkUtils.Toast.showShort("搜索条件不能为空");
        } else {
            BeanSearch beanSearch = new BeanSearch(key);
            if (!isExist(beanSearch)) {
                SharedPreferencesManager.getInstance().saveSearchHistory(beanSearch);
            }
            hiddenHistroy();
            updateHistory();
            mRefreshLayout.autoRefresh(100);
            loadData();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && !hasShowHistory) {
            showHistoryPop(mLlSearch);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!hasShowHistory) {
            showHistoryPop(mLlSearch);
        }
        return false;
    }

    private void initView() {
        mEtSearch.setOnFocusChangeListener(this);
        mEtSearch.setOnTouchListener(this);
        mViewNoData.setHint1("暂无信息");
    }

    private void initRecyclerView() {
        mServiceHistory = new ArrayList<>();
        mAdapter = new AdapterConsultService(mContext, mServiceHistory);
        mRcvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvContent.setAdapter(mAdapter);
    }

    /**
     * 初始化搜索历史
     */
    private void initSearchHistory() {
        mDatas = SharedPreferencesManager.getInstance().getSearchHistory();
        mHistoryAdapter = new CommonRecycleAdapter<BeanSearch>(mContext, mDatas, R.layout.item_search_history) {
            @Override
            public void bindData(CommonViewHolder holder, BeanSearch data, int index) {
                holder.setText(R.id.tv_1, data.key);
                holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        BeanSearch search = mDatas.get(position);
                        mEtSearch.setText(search.key);
                    }

                    @Override
                    public void onItemLongClickListener(int position) {

                    }
                });
            }
        };

    }

    private void showHistoryPop(View view) {
        View rootView = View.inflate(SystemManager.getInstance().getSystem(SystemPager.class).getCurrActivity(), R.layout.pop_search_history, null);
        TextView tvNoneHint = rootView.findViewById(R.id.tv_none_hint);
        TextView tvClear = rootView.findViewById(R.id.tv_clear);
        tvNoneHint.setVisibility(mDatas.size() == 0 ? View.VISIBLE : View.GONE);
        tvClear.setVisibility(mDatas.size() == 0 ? View.GONE : View.VISIBLE);
        RecyclerView recyclerView = rootView.findViewById(R.id.rcv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mHistoryAdapter);

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance().clearSearchHistory();
                updateHistory();
            }
        });
        popupWindow = new PopupWindow(rootView, 100, 100);
        popupWindow.setFocusable(false);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hasShowHistory = false;
            }
        });
        hasShowHistory = true;
    }

    private void updateHistory() {
        mDatas = SharedPreferencesManager.getInstance().getSearchHistory();
        if (mHistoryAdapter != null)
            mHistoryAdapter.update(mDatas);
    }

    /**
     * 隐藏搜索历史
     */
    public void hiddenHistroy() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private boolean isExist(BeanSearch search) {
        for (BeanSearch search1 : mDatas) {
            if (search.compareTo(search1) == 0) {
                return true;
            }
        }
        return false;
    }


    private void loadData() {
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-26";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-26";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-25";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mBean = new ConsultServiceHistory();
        mBean.date = "2019-11-15";
        mBean.title = "系统管理员专家远程指导";
        mServiceHistory.add(mBean);
        mAdapter.notifyDataSetChanged();
        mViewNoData.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}
