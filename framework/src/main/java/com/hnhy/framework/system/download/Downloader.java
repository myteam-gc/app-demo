package com.hnhy.framework.system.download;

import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.system.SystemDownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class Downloader {
    private static final String TAG = "Downloader";
    private static final int READ_STREAM_OFFSET = 1024;

    private static Downloader mInstance;

    private Callback mCallback;
    private OkHttpClient mOkHttpClient;

    private Downloader() {
        ConnectionPool connectionPool = new ConnectionPool(2, 60, TimeUnit.SECONDS);
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .connectionPool(connectionPool)
                .build();
    }

    public static Downloader getInstance() {
        if (mInstance == null) {
            mInstance = new Downloader();
        }
        return mInstance;
    }

    public void download(final Task task) {
        Logger.d(TAG, String.format("download %s", task.toString()));

        File fileDir = new File(task.saveFileDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // TODO: 2018/8/13 do wait work
        task.status = SystemDownload.STATUS_WAIT;
        task.type_callback = SystemDownload.TYPE_CALLBACK_WAIT;
        mCallback.onCallback(task);

        task.status = SystemDownload.STATUS_START;
        task.type_callback = SystemDownload.TYPE_CALLBACK_START;
        mCallback.onCallback(task);
        final Request request = new Request.Builder()
                .url(task.url)
                .tag(task.tag)
                .build();

        Observable.create(new ObservableOnSubscribe<Task>() {
            @Override
            public void subscribe(final ObservableEmitter<Task> emitter) {
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.e(TAG, "onFailure,e=" + e.getMessage());
                        task.status = SystemDownload.STATUS_FAILURE;
                        task.msg = "下载失败";
                        task.type_callback = SystemDownload.TYPE_CALLBACK_FAILURE;
                        emitter.onNext(task);
                        emitter.onComplete();
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        Logger.d(TAG, "onResponse");
                        ResponseBody body;
                        if (response.isSuccessful() && (body = response.body()) != null) {
                            saveFile(task, response, emitter);
                        } else {
                            Logger.e(TAG, String.format("error the body is null or http code = %s", response.code()));
                            task.status = SystemDownload.STATUS_FAILURE;
                            task.msg = "下载失败";
                            task.type_callback = SystemDownload.TYPE_CALLBACK_FAILURE;
                            emitter.onNext(task);
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Task>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Task task) {
                        mCallback.onCallback(task);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mCallback.onCallback(task);
                    }
                });
    }

    public void pause(int tag) {
        // TODO: 2018/8/13 do pause work
    }

    public void cancel(int tag) {
        // TODO: 2018/8/3 cancel download
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private void saveFile(Task task, Response response, ObservableEmitter<Task> emitter) {
        InputStream inputStream = response.body().byteStream();
        if (inputStream != null) {
            try {
                File file = new File(task.saveFilePath);
                file.deleteOnExit();
                file.createNewFile();

                long total = response.body().contentLength();
                byte[] buf = new byte[READ_STREAM_OFFSET];
                int len;
                long current = 0;
                FileOutputStream fos = new FileOutputStream(file);

                while ((len = inputStream.read(buf)) != -1) {
                    current += len;
                    fos.write(buf, 0, len);
                    float i = ((float) current) / ((float) total);
                    task.progress = (int) (i * 100f);
                    task.type_callback = SystemDownload.TYPE_CALLBACK_PROGRESS;
                    emitter.onNext(task);
                }
                fos.flush();
                task.status = SystemDownload.STATUS_SUCCESS;
                task.type_callback = SystemDownload.TYPE_CALLBACK_SUCCESS;
                emitter.onNext(task);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(TAG, String.format("save file error,tag=%s,msg=%s", task.tag, e.getMessage()));
                task.status = SystemDownload.STATUS_FAILURE;
                task.msg = "下载失败";
                task.type_callback = SystemDownload.TYPE_CALLBACK_FAILURE;
                emitter.onNext(task);
            }
        } else {
            Logger.e(TAG, String.format("the input stream is null,tag=%s", task.tag));
            task.status = SystemDownload.STATUS_FAILURE;
            task.msg = "下载失败";
            task.type_callback = SystemDownload.TYPE_CALLBACK_FAILURE;
            emitter.onNext(task);
        }
    }

    public interface Callback {
        void onCallback(Task task);
    }
}
