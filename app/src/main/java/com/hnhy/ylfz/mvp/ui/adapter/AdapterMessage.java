package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Message;
import com.hnhy.ylfz.mvp.ui.activity.msg.ActivityMessageDetail;

import java.util.List;

/**
 * Created by guc on 2019/12/17.
 * 描述：我的消息适配器
 */
public class AdapterMessage extends CommonRecycleAdapter<Message> {

    public AdapterMessage(Context context, List<Message> dataList) {
        super(context, dataList, R.layout.item_message);
    }

    @Override
    public void bindData(CommonViewHolder holder, Message data, int position) {
        holder.setText(R.id.tv_title, data.title)
                .setText(R.id.tv_date, data.date)
                .setText(R.id.tv_message, data.content);
        ImageView imageView = holder.getView(R.id.iv_img);
        imageView.setBackgroundResource(data.type == 0 ? R.drawable.icon_identity_apply : R.drawable.icon_warning);
        holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ActivityMessageDetail.jump(mContext, data);
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
    }
}
