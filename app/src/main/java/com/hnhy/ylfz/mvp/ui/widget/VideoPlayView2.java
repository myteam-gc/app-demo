package com.hnhy.ylfz.mvp.ui.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemImageLoader;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.mvp.ui.widget.banner.WeakHandler;

import java.io.IOException;

/**
 * Created by guc on 2019/12/10.
 * 描述：视频播放2
 */
public class VideoPlayView2 extends FrameLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = "VideoPlayView";
    private ImageView mIvPlay, mIvScreenshot;
    private SurfaceView mSurfaceView;
    private LinearLayout mControllerLayout;
    private Button mBtnPlayOrPause, mBtnFullScreen;
    private SeekBar mSeekBar;
    private TextView mTimePlayed, mTimeTotal;

    private String mUrl;
    private Context mContext;
    private boolean isShow = false;
    private WeakHandler mHandler = new WeakHandler();
    private MediaPlayer mPlayer;
    private int mIndex;

    public VideoPlayView2(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate(context, R.layout.view_video_play2, this);
        initViewAndEvents();
    }

    public void setUrl(String url,int index) {
        this.mUrl = url;
        this.mIndex = index;
        Log.e(TAG, "setUrl: "+ index );
        SystemManager.getInstance().getSystem(SystemImageLoader.class).loadVideoScreenshot(mContext, mUrl, mIvScreenshot, 1000);
        initSurfaceView();
        initPlayer();
        if (mPlayer != null) {
            Uri uri = Uri.parse(mUrl);
            try {
                mPlayer.setDataSource(mContext, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play://播放按钮
                play();
                mIvPlay.setVisibility(View.GONE);
                mIvScreenshot.setVisibility(View.GONE);
                break;
            case R.id.surface_view:
                switchControllerVisibility();
                break;
            case R.id.btn_pause_or_start://暂停或播放
                play();
                break;
            case R.id.btn_fullscreen://全屏播放
                FrameworkUtils.Toast.showShort("全屏播放");
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated: " + mIndex);
        if (mPlayer == null) initPlayer();
        mPlayer.reset();
        Uri uri = Uri.parse(mUrl);
        try {
            mPlayer.setDataSource(mContext, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setDisplay(holder);
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: "+mIndex);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: " + mIndex);
        stopCallback();
        reset();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {//准备好了
        Log.e(TAG, "onPrepared: 准备好了");
        mTimePlayed.setText(formatLongToTimeStr(mp.getCurrentPosition()));
        mTimeTotal.setText(formatLongToTimeStr(mp.getDuration()));
        mSeekBar.setMax(mp.getDuration());
        mSeekBar.setProgress(mp.getCurrentPosition());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {//播放完成
        mIvPlay.setVisibility(VISIBLE);
        mIvScreenshot.setVisibility(VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {//播放错误
        mIvPlay.setVisibility(VISIBLE);
        FrameworkUtils.Toast.showShort("播放错误：" + what);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, mIndex+"what:" + what + "extra:" + extra);
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mPlayer != null && fromUser) {
            mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private Runnable taskHideController = new Runnable() {
        @Override
        public void run() {
            hideController();
        }
    };
    private Runnable taskUpdateTime = new Runnable() {
        @Override
        public void run() {
            if (mPlayer == null) {
                mTimePlayed.setText("00:00");
                return;
            }
            mTimePlayed.setText(formatLongToTimeStr(mPlayer.getCurrentPosition()));
            mSeekBar.setProgress(mPlayer.getCurrentPosition());
            mHandler.postDelayed(taskUpdateTime, 1000);
        }
    };

    private void switchControllerVisibility() {
        mControllerLayout.setVisibility(isShow ? View.GONE : VISIBLE);
        isShow = !isShow;
        if (isShow) {
            mHandler.postDelayed(taskHideController, 5000);
        } else {
            mHandler.removeCallbacks(taskHideController);
        }
    }

    private void hideController() {
        mControllerLayout.setVisibility(View.GONE);
        isShow = false;
    }

    private void initViewAndEvents() {
        Log.e(TAG, "initViewAndEvents: "+mIndex );
        mIvPlay = findViewById(R.id.iv_play);
        mIvScreenshot = findViewById(R.id.iv_screenshot);
        mControllerLayout = findViewById(R.id.ll_controller);
        mSurfaceView = findViewById(R.id.surface_view);
        mBtnPlayOrPause = findViewById(R.id.btn_pause_or_start);
        mBtnFullScreen = findViewById(R.id.btn_fullscreen);
        mSeekBar = findViewById(R.id.seek_bar);
        mTimePlayed = findViewById(R.id.time_played);
        mTimeTotal = findViewById(R.id.time_all);
        mIvPlay.setOnClickListener(this);
        mSurfaceView.setOnClickListener(this);
        mBtnPlayOrPause.setOnClickListener(this);
        mBtnFullScreen.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initSurfaceView() {
        Log.e(TAG, "initSurfaceView: "+mIndex );
        mSurfaceView.setZOrderOnTop(false);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.getHolder().addCallback(this);
    }

    private void initPlayer() {
        if (mPlayer!=null)  return;
        Log.e(TAG, "initPlayer: "+mIndex );
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
    }

    private void play() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mHandler.removeCallbacks(taskUpdateTime);
            mBtnPlayOrPause.setBackground(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        } else {
            mPlayer.start();
            mHandler.postDelayed(taskUpdateTime, 1000);
            mBtnPlayOrPause.setBackground(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
        }
    }

    private String formatLongToTimeStr(long timeMills) {
        StringBuilder stringBuilder = new StringBuilder();
        int hour = (int) (timeMills / 1000 / 60 / 60);
        if (hour > 0) {
            stringBuilder.append(String.format("%02d:", hour));
        }
        int minute = (int) (timeMills % (60 * 60 * 1000) / 60 / 1000);
        stringBuilder.append(String.format("%02d:", minute));
        int seconds = (int) (timeMills % (60 * 1000) / 1000);
        stringBuilder.append(String.format("%02d", seconds));
        return stringBuilder.toString();
    }

    /**
     * 停止播放时直接释放资源，在设置播放地址时再初始化
     */
    private void stopCallback() {
        mHandler.removeCallbacks(taskUpdateTime);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void reset() {
        mIvScreenshot.setVisibility(View.VISIBLE);
        mIvPlay.setVisibility(View.VISIBLE);
    }
}
