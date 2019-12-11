package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Forum;
import com.hnhy.ylfz.mvp.model.bean.Viewpoint;
import com.hnhy.ylfz.mvp.ui.widget.NiceImageView;
import com.hnhy.ylfz.mvp.ui.widget.VideoPlayView;
import com.hnhy.ylfz.mvp.ui.widget.VideoPlayView2;

import java.util.List;

/**
 * Created by guc on 2019/12/10.
 * 描述：专家视点适配器
 */
public class AdapterViewpoint extends CommonRecycleAdapter<Viewpoint> {

    public AdapterViewpoint(Context context, List<Viewpoint> dataList) {
        super(context, dataList, R.layout.item_expert_viewpoint);
    }

    @Override
    public void bindData(CommonViewHolder holder, Viewpoint data, int position) {
        holder.setText(R.id.tv_title,data.title);
        VideoPlayView2 videoView2 = holder.getView(R.id.video_play_view2);
        videoView2.setUrl(data.videoUrl,position);
        holder.setCommonClickListener(new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull CommonViewHolder holder) {
        super.onViewRecycled(holder);
        Log.e("onViewRecycled", "onViewRecycled: "+holder.getAdapterPosition());
    }
}
