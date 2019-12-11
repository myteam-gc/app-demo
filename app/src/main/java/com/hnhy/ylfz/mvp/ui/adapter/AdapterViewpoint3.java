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

import org.salient.artplayer.Comparator;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.OnWindowDetachedListener;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ijk.IjkPlayer;
import org.salient.artplayer.ui.ControlPanel;

import java.io.IOException;
import java.util.List;

/**
 * Created by guc on 2019/12/11.
 * 描述：专家视点适配器
 */
public class AdapterViewpoint3 extends CommonRecycleAdapter<Viewpoint> {

    public AdapterViewpoint3(Context context, List<Viewpoint> dataList) {
        super(context, dataList, R.layout.item_expert_viewpoint3);
//        MediaPlayerManager.instance().setMediaPlayer(new IjkPlayer());
    }


    @Override
    public void bindData(CommonViewHolder holder, Viewpoint data, int position) {

        data.listPosition = position;
        holder.setText(R.id.tv_title, data.title);
        VideoView videoView = holder.getView(R.id.video_view);
        videoView.setControlPanel(new ControlPanel(mContext));
        videoView.setComparator(mComparator);
        videoView.setUp(data.videoUrl,VideoView.WindowType.LIST,videoView);
        //设置预览图
        ImageView coverView = ((ControlPanel) videoView.getControlPanel()).findViewById(R.id.video_cover);
        SystemManager.getInstance().getSystem(SystemImageLoader.class).loadVideoScreenshot(mContext, data.videoUrl, coverView, 1000);
    }

    private Comparator mComparator = new Comparator() {
        @Override
        public boolean compare(VideoView videoView) {
            try {
                Object currentData = MediaPlayerManager.instance().getCurrentData();
                //By comparing the position on the list to distinguish whether the same video
                //根据列表位置识别是否同一个视频
                if (currentData != null && videoView != null) {
                    Object data = videoView.getData();
                    return data != null
                            && currentData instanceof Viewpoint
                            && data instanceof Viewpoint
                            && ((Viewpoint) currentData).listPosition == ((Viewpoint) data).listPosition;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    private OnWindowDetachedListener mOnWindowDetachedListener = null;

    public void setDetachAction(OnWindowDetachedListener onWindowDetachedListener) {
        mOnWindowDetachedListener = onWindowDetachedListener;
    }
}
