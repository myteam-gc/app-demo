package com.hnhy.framework.system;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hnhy.framework.Constant;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.logger.Logger;
import com.hnhy.framework.system.net.Request;
import com.hnhy.framework.system.net.RequestCallback;
import com.hnhy.framework.system.net.RequestTag;
import com.hnhy.framework.system.net.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import tech.linjiang.pandora.Pandora;

/**
 * Author: hardcattle
 * Time: 2018/3/11 下午1:34
 * Description:
 */

public class SystemHttp extends BaseSystem {
    private static final String TAG = "SystemHttp";

    private static final String MSG1 = "无法连接服务器,请检查网络";
    private static final String MSG2 = "网络请求失败,请稍后再试";
    private OkHttpClient mOkHttpClient;
    private Map<Object, List<Call>> mPoolCall;
    private Gson mGson;

    @Override
    protected void init() {
        mGson = new Gson();
        mPoolCall = new HashMap<>();
        mOkHttpClient = createOkHttpClient();
    }

    @Override
    protected void destroy() {
        cancelAllRequest();
        mPoolCall = null;
        mGson = null;
        mOkHttpClient = null;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public <T> void net(final Object tag, final Request request, final RequestCallback<T> callBack) {
        if (callBack != null) {
            callBack.onStart();
        }

        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(final ObservableEmitter<Response> emitter) {
                Call call = mOkHttpClient.newCall(request.getRequest(tag));
                inputCallToPool(tag, call);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        if (!getRequestTag(call).isHasCanceled()) {
                            removeCall(tag, call);
                            Response response = getResponse(null, request, callBack.respType, MSG1);
                            emitter.onNext(response);
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) {
                        if (!getRequestTag(call).isHasCanceled()) {
                            removeCall(tag, call);
                            Response response1 = getResponse(response, request, callBack.respType, null);
                            emitter.onNext(response1);
                            emitter.onComplete();
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response response) {
                        if (callBack != null) {
                            if (response.mCode == 0) {
                                callBack.onSuccess((T) response.mWrapperData, response);
                            } else {
                                callBack.onFailure(response);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (callBack != null) {
                            Response response = getResponse(null, request, callBack.respType, MSG2);
                            callBack.onFailure(response);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (callBack != null) {
                            callBack.onComplete();
                        }
                    }
                });
    }

    public void cancelAllRequest() {
        Set<Object> keySets = mPoolCall.keySet();
        for (Object tag : keySets) {
            List<Call> calls = mPoolCall.get(tag);
            if (calls != null) {
                for (Call call : calls) {
                    if (call != null && call.isExecuted())
                        call.cancel();
                }
            }
        }

        mPoolCall.clear();
    }

    public void cancelRequest(Object tag) {
        List<Call> calls = mPoolCall.get(tag);
        if (calls != null) {
            for (Call call : calls) {
                if (call != null && call.isExecuted())
                    call.cancel();
            }
        }
        mPoolCall.remove(tag);
    }

    private void removeCall(Object tag, Call call) {
        List<Call> calls = mPoolCall.get(tag);
        if (calls != null) {
            calls.remove(call);
        }
    }

    private RequestTag getRequestTag(Call call) {
        return (RequestTag) call.request().tag();
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(createHttpLoggingInterceptor());
        builder.addInterceptor(Pandora.get().getInterceptor());
        return builder.connectTimeout(Constant.TIME_OUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(Constant.TIME_OUT_READ, TimeUnit.SECONDS)
                .writeTimeout(Constant.TIME_OUT_WRITE, TimeUnit.SECONDS)
                .build();
    }

    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.i("hnhy-http", "OkHttp====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private Response getResponse(okhttp3.Response resp, Request request, Type type, String knownMsg) {
        Response response = new Response();

        response.mUrl = request.getUrl();
        response.mFromCache = request.isUseCache(); // 临时处理
        response.mTime = System.currentTimeMillis();
        if (resp == null) {
            response.mHttpCode = -1;
            response.mCode = -1;
            response.mMessage = knownMsg;
        } else {
            response.mHttpCode = resp.code();
            if (resp.isSuccessful()) {
                try {
                    String respJson = resp.body().string();
                    int code = -1;
                    String msg = null;
                    String data = null;

                    //全包处理
                    if (request.isWrapperWholeResponse()) {
                        code = 0;
                        msg = "提交成功";
                        data = respJson;
                    } else {//半包处理
                        JsonObject root = mGson.fromJson(respJson, JsonObject.class);

                        if (root.has("success")) {
                            code = root.getAsJsonPrimitive("success").getAsBoolean() ? 0 : 1;
                        } else if (root.has("code")) {
                            code = root.getAsJsonPrimitive("code").getAsInt();
                        }

                        if (root.has("message")) {
                            msg = root.getAsJsonPrimitive("message").getAsString();
                        }

                        if (root.has("data")) {
                            data = root.get("data").toString();
                        } else if (root.has("rows")) {
                            data = root.get("rows").toString();
                        } else if (root.has("result")) {
                            data = root.get("result").toString();
                        }
                    }

                    response.mCode = code;
                    response.mMetadata = respJson;
                    response.mMessage = msg;
                    if (request.isWrapperResponse() && code == 0 && !TextUtils.isEmpty(data)) {
                        response.mWrapperData = mGson.fromJson(data, type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.mCode = -1;
                    response.mMessage = "数据转换错误";
                }
            } else {
                response.mCode = -1;
                response.mMessage = "获取数据失败";
            }
        }

        return response;
    }

    private void inputCallToPool(Object tag, Call call) {
        List<Call> calls = mPoolCall.get(tag);
        if (calls != null) {
            calls.add(call);
        } else {
            calls = new ArrayList<>();
            calls.add(call);
            mPoolCall.put(tag, calls);
        }
    }
}
