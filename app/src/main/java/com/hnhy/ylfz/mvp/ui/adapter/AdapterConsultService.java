package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;

import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.ConsultServiceHistory;
import com.hnhy.ylfz.mvp.ui.activity.consult.ActivityConsultServiceDetail;

import java.util.List;

/**
 * Created by guc on 2019/12/12.
 * 描述：服务历史
 */
public class AdapterConsultService extends CommonRecycleAdapter<ConsultServiceHistory> {

    public AdapterConsultService(Context context, List<ConsultServiceHistory> dataList) {
        super(context, dataList, R.layout.item_service_history);
    }

    @Override
    public void bindData(CommonViewHolder holder, ConsultServiceHistory data, int position) {
        holder.setText(R.id.tv_title, data.title)
                .setText(R.id.tv_date, data.date);
        holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ActivityConsultServiceDetail.jump(mContext);
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
    }
}
