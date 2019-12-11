package com.hnhy.ylfz.mvp.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.ui.adapter.CommonRecycleAdapter;
import com.hnhy.ui.adapter.CommonViewHolder;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.model.bean.Viewpoint;
import com.hnhy.ylfz.utils.ImageUtils;

import org.salient.artplayer.Comparator;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.OnWindowDetachedListener;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

import java.util.List;

/**
 * Created by guc on 2019/12/11.
 * 描述：专家视点适配器
 */
public class AdapterViewpoint3 extends CommonRecycleAdapter<Viewpoint> {

    public AdapterViewpoint3(Context context, List<Viewpoint> dataList) {
        super(context, dataList, R.layout.item_expert_viewpoint3);
        openTiny();
//        MediaPlayerManager.instance().setMediaPlayer(new IjkPlayer());
//        MediaPlayerManager.instance().releasePlayerAndView(context);
//        MediaPlayerManager.instance().setMediaPlayer(new SystemMediaPlayer());
    }


    @Override
    public void bindData(CommonViewHolder holder, Viewpoint data, int position) {

        data.listPosition = position;
        holder.setText(R.id.tv_title, data.title);
        VideoView videoView = holder.getView(R.id.video_view);
        videoView.setControlPanel(new ControlPanel(mContext));
        videoView.setComparator(mComparator);
        videoView.setOnWindowDetachedListener(mOnWindowDetachedListener);
        videoView.setUp(data.videoUrl, VideoView.WindowType.LIST, data);
        //设置预览图
        ImageView coverView = ((ControlPanel) videoView.getControlPanel()).findViewById(R.id.video_cover);
        SystemManager.getInstance().getSystem(SystemImageLoader.class).displayImage(mContext, coverView, data.image);
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

    private void openTiny() {
        this.setDetachAction(new OnWindowDetachedListener() {
            @Override
            public void onDetached(VideoView videoView) {
                //开启小窗
                VideoView tinyVideoView = new VideoView(videoView.getContext());
                //set url and data
                tinyVideoView.setUp(videoView.getDataSourceObject(), VideoView.WindowType.TINY, videoView.getData());
                //set control panel
                ControlPanel controlPanel = new ControlPanel(videoView.getContext());
                tinyVideoView.setControlPanel(controlPanel);
                //set cover
                ImageView coverView = controlPanel.findViewById(R.id.video_cover);
                SystemManager.getInstance().getSystem(SystemImageLoader.class).loadVideoScreenshot(mContext, ((Viewpoint) videoView.getData()).videoUrl, coverView, 1000);
                //set parent
                tinyVideoView.setParentVideoView(videoView);
                //set LayoutParams
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(16 * 45, 9 * 45);
                layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
                layoutParams.setMargins(0, ImageUtils.dp2px(mContext, 110), 0, 0);
                //start tiny window
                tinyVideoView.startTinyWindow(layoutParams);
            }
        });
    }

    private void closePlay() {
        this.setDetachAction(new OnWindowDetachedListener() {
            @Override
            public void onDetached(VideoView videoView) {
                MediaPlayerManager.instance().releasePlayerAndView(mContext);
            }
        });
    }
}
