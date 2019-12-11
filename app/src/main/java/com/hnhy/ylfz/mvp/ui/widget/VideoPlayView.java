package com.hnhy.ylfz.mvp.ui.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ylfz.R;

/**
 * Created by guc on 2019/12/10.
 * 描述：视频播放
 */
public class VideoPlayView extends FrameLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener , MediaPlayer.OnCompletionListener , MediaPlayer.OnErrorListener , MediaPlayer.OnInfoListener {
    private static final String TAG = "VideoPlayView";
    private VideoView mVideoView;
    private ImageView mIvPlay,mIvScreenshot;
    private String mUrl;
    private Context mContext;

    public VideoPlayView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate(context, R.layout.view_video_play, this);
        initView();
    }

    private void initView() {
        mIvPlay = findViewById(R.id.iv_play);
        mIvScreenshot = findViewById(R.id.iv_screenshot);
        mVideoView = findViewById(R.id.video_view);
    }

    public void setUrl(String url) {
        this.mUrl = url;
        initVideoView();
    }

    private void initVideoView() {
        mIvPlay.setOnClickListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
//        SystemManager.getInstance().getSystem(SystemImageLoader.class).displayImage(mContext, mIvScreenshot, mUrl);
        SystemManager.getInstance().getSystem(SystemImageLoader.class).loadVideoScreenshot(mContext,mUrl,mIvScreenshot,2000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play://播放按钮
                playVideo();
                break;
        }
    }
    public void reset(){
        pause();
        mVideoView.stopPlayback();
    }
    public void pause(){
        if (mVideoView.isPlaying()){
            mVideoView.pause();
        }
        mIvScreenshot.setVisibility(View.VISIBLE);
        mIvPlay.setVisibility(View.VISIBLE);
    }

    private void playVideo() {
        if (TextUtils.isEmpty(mUrl)){
            Log.e(TAG, "未获取到视频地址" );
            return;
        }
        if (mVideoView.isPlaying()) return;
            Uri uri = Uri.parse(mUrl);
            mVideoView.setMediaController(new MediaController(getContext()));
            mVideoView.setVideoURI(uri);
            mVideoView.start();
            mVideoView.requestFocus();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {//准备好了
        mIvPlay.setVisibility(GONE);
        mIvScreenshot.setVisibility(GONE);
        Log.e(TAG, "onPrepared: 准备好了" );
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {//播放完成
        mIvPlay.setVisibility(VISIBLE);
        mIvScreenshot.setVisibility(VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {//播放错误
        mIvPlay.setVisibility(VISIBLE);
        FrameworkUtils.Toast.showShort("播放错误："+what);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "what:"+what + "extra:"+extra );
        return false;
    }
}
