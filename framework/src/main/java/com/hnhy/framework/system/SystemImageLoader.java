package com.hnhy.framework.system;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.hnhy.framework.R;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.logger.Logger;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Author: hardcattle
 * Time: 2018/3/26 上午9:57
 * Description:
 */

public class SystemImageLoader extends BaseSystem {
    private static final String TAG = "SystemImageLoader";

    @Override
    protected void init() {
    }

    @Override
    protected void destroy() {
    }

    //临时处理
    public void displayImage(Context context, ImageView imageView, int resId) {
        Glide.with(context).load(resId).into(imageView);
    }

    public void displayImage(Object tag, ImageView imageView, String url) {
        displayImage(tag, imageView, url, -1, -1);
    }

    public void displayImage(Object tag, ImageView imageView, String url, @DrawableRes int placeHolderId, @DrawableRes int errorId) {
        placeHolderId = placeHolderId == -1 ? R.drawable.ic_place_pic : placeHolderId;
        errorId = errorId == -1 ? R.drawable.ic_place_pic : errorId;
        RequestOptions options = new RequestOptions().placeholder(placeHolderId).error(errorId);
        if (tag instanceof FragmentActivity) {
            Glide.with((FragmentActivity) tag).load(url).apply(options).into(imageView);
        } else if (tag instanceof Activity) {
            Glide.with((Activity) tag).load(url).apply(options).into(imageView);
        } else if (tag instanceof Context) {
            Glide.with((Context) tag).load(url).apply(options).into(imageView);
        } else if (tag instanceof Fragment) {
            Glide.with((Fragment) tag).load(url).apply(options).into(imageView);
        } else if (tag instanceof android.support.v4.app.Fragment) {
            Glide.with((android.support.v4.app.Fragment) tag).load(url).apply(options).into(imageView);
        } else if (tag instanceof View) {
            Glide.with((View) tag).load(url).apply(options).into(imageView);
        } else {
            Logger.e(TAG, "the tag is error");
        }
    }

    /**

     *
     有个0- 到1秒的延迟
     * @param context 上下文
     * @param uri 视频地址
     * @param imageView 设置image
     * @param frameTimeMicros 获取某一时间帧
     */
    public void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }
            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }
}
