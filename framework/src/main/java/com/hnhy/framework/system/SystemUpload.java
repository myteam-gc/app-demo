package com.hnhy.framework.system;

import android.graphics.Bitmap;

import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.system.net.Request;
import com.hnhy.framework.system.net.RequestCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by guc on 2019/7/16.
 * 描述：文件上传
 */
public class SystemUpload extends BaseSystem {
    @Override
    protected void init() {

    }

    @Override
    protected void destroy() {

    }

    /**
     * 压缩图片
     *
     * @param bitmap
     * @param filePath
     */
    public static void compressBitmapToFile(Bitmap bitmap, String filePath) {
        // 0-100 100为不压缩
        int options = 25;

        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            // 把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void uploadFile(final Object tag, final String url, final List<String> filePaths, final RequestCallback<T> callback) {
        Request request = new Request.Builder()
                .setBaseUrl(url)
                .setRequestType(Request.TYPE_MULTIPART)
                .setWrapperWholeResponse(true)
                .setUploadFilePaths(filePaths)
                .build();
        uploadFile(tag,request,callback);

    }

    public <T> void uploadFile(final Object tag, final Request request, final RequestCallback<T> callback) {
        getSystem(SystemHttp.class).net(tag, request, callback);
    }

    public void cancel(Object tag) {
        getSystem(SystemHttp.class).cancelRequest(tag);
    }
}
