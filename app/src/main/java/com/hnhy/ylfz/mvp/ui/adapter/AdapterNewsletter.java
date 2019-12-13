package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.NewsLetter;
import com.hnhy.ylfz.mvp.ui.activity.ActivityDetailInBrowser;

import java.util.List;

/**
 * Created by guc on 2019/12/10.
 * 描述：国考快讯适配器
 */
public class AdapterNewsletter extends CommonRecycleAdapter<NewsLetter> {

    public AdapterNewsletter(Context context, List<NewsLetter> dataList) {
        super(context, dataList, R.layout.item_country_newsletter);
    }

    @Override
    public void bindData(CommonViewHolder holder, NewsLetter data, int position) {
        holder.setText(R.id.tv_title,data.title)
                .setText(R.id.tv_date,data.date);
        ImageView imageView = holder.getView(R.id.iv_picture);
        Glide.with(mContext).load(data.picUrl).into(imageView);
        holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ActivityDetailInBrowser.showDetail(mContext,data.url);
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
    }
}
