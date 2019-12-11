package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Viewpoint;

import java.io.IOException;
import java.util.List;

/**
 * Created by guc on 2019/12/11.
 * 描述：专家视点适配器
 */
public class AdapterViewpoint2 extends CommonRecycleAdapter<Viewpoint> {
    private MediaPlayer mPlayer;
    private int mCurrentPosition = -1;//记录哪个位置播放视频了

    public AdapterViewpoint2(Context context, List<Viewpoint> dataList) {
        super(context, dataList, R.layout.item_expert_viewpoint2);
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener((mediaPlayer) -> {
            mCurrentPosition = -1;
            notifyDataSetChanged();
        });
        mPlayer.setOnPreparedListener((mediaPlayer)->{
            mPlayer.start();
        });
    }

    public void destoryPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void bindData(CommonViewHolder holder, Viewpoint data, int position) {
        holder.setText(R.id.tv_title, data.title);
        SurfaceView surfaceView = holder.getView(R.id.surface_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        ImageView ivScreenShot = holder.getView(R.id.iv_screenshot);
        SystemManager.getInstance().getSystem(SystemImageLoader.class).displayImage(mContext, ivScreenShot, data.videoUrl);
        ivScreenShot.setOnClickListener((view) -> {
            mCurrentPosition = position;
            notifyDataSetChanged();
        });
        holder.getView(R.id.iv_play).setOnClickListener((view) -> {
            mCurrentPosition = position;
            notifyDataSetChanged();
        });

        if (mCurrentPosition == position) {
            mPlayer.reset();
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            //隐藏播放按钮
            holder.setViewVisibility(R.id.iv_play, View.INVISIBLE);
            holder.setViewVisibility(R.id.iv_screenshot, View.INVISIBLE);
            surfaceView.setVisibility(View.VISIBLE);
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mPlayer.reset();//重置媒体播放器
                    mPlayer.setDisplay(holder);//设置媒体播放位置
                    Uri uri = Uri.parse(data.videoUrl);
                    try {
                        mPlayer.setDataSource(mContext, uri);
                        mPlayer.prepareAsync();//网络视频使用异步
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        } else {
            holder.setViewVisibility(R.id.iv_play, View.VISIBLE);
            holder.setViewVisibility(R.id.iv_screenshot, View.VISIBLE);
            surfaceView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onViewRecycled(@NonNull CommonViewHolder holder) {
        super.onViewRecycled(holder);
        Log.e("onViewRecycled", "onViewRecycled: " + holder.getAdapterPosition());
    }
}
