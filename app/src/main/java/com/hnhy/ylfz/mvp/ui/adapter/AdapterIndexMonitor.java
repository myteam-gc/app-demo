package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.IndexMonitor;

import java.util.List;

/**
 * Created by guc on 2019/12/18.
 * 描述：指标监控适配器
 */
public class AdapterIndexMonitor extends CommonRecycleAdapter<IndexMonitor> {

    public AdapterIndexMonitor(Context context, List<IndexMonitor> dataList) {
        super(context, dataList, R.layout.item_index_monitor);
    }

    @Override
    public void bindData(CommonViewHolder holder, IndexMonitor data, int position) {
        holder.setViewVisibility(R.id.ll_header, position == 0 ? View.VISIBLE : View.GONE);
    }
}
