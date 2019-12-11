package com.hnhy.framework.system.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hnhy.framework.Engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author: hardcattle
 * Time: 2018/4/20 上午9:25
 * Description:
 */
public class Request {

    public static final int TYPE_MULTIPART = 2;

    public static final int TYPE_GET = 0;
    public static final int TYPE_POST = 1;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final int TYPE_SPECIAL = 3;

    private final String mUrl;
    private final int mRequestType;
    private final Map<String, String> mRequestParams;
    private final List<String> mUploadFilePaths;
    private final boolean mUseCache;
    private final boolean mWrapperResponse;
    private final boolean mWrapperWholeResponse;


    public Request(Builder builder) {
        mUrl = builder.url;
        mRequestType = builder.requestType;
        mRequestParams = builder.requestParams;
        mUploadFilePaths = builder.filePaths;
        mUseCache = builder.useCache;
        mWrapperResponse = builder.wrapperResponse;
        mWrapperWholeResponse = builder.wrapperWholeResponse;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getRequestType() {
        return mRequestType;
    }

    public Map<String, String> getRequestParams() {
        return mRequestParams;
    }

    public boolean isUseCache() {
        return mUseCache;
    }

    public boolean isWrapperResponse() {
        return mWrapperResponse;
    }

    public boolean isWrapperWholeResponse() {
        return mWrapperWholeResponse;
    }

    public okhttp3.Request getRequest(Object tag) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        if (tag != null) {
            builder.tag(new RequestTag(tag));
        } else {
            builder.tag(new RequestTag(""));
        }

        if (mRequestType == TYPE_GET) {
            builder.get();
            builder.url(getGetUrl(mUrl, mRequestParams));
        } else if (mRequestType == TYPE_POST) {
            builder.url(mUrl);
            RequestBody requestBody = getRequestBody(mRequestParams);
            builder.post(requestBody);
        } else if (mRequestType == TYPE_MULTIPART) {
            builder.url(mUrl);
            RequestBody requestBody = getRequestBody2(mRequestParams, mUploadFilePaths);
            builder.post(requestBody);
        } else if (mRequestType == TYPE_SPECIAL) {
            builder.url(mUrl);
            Gson gson = new Gson();
            String strEntity = gson.toJson(mRequestParams);
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
            builder.post(body);
            builder.header("Content-type", "application/json;charset=UTF-8");
        }

        return builder.build();
    }

    /**
     * 获取GET 请求的Url
     *
     * @param url    url
     * @param params 参数
     * @return 完整url
     */
    private String getGetUrl(String url, Map<String, String> params) {
        StringBuilder urlSb = new StringBuilder();
        urlSb.append(url);
        if (params != null) {
            urlSb.append("?");
            List<String> keyList = new ArrayList<>(params.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                urlSb.append(keyList.get(i));
                urlSb.append("=");
                urlSb.append(params.get(keyList.get(i)));
                if (i != keyList.size() - 1) {
                    urlSb.append("&");
                }
            }
        }
        return urlSb.toString();
    }

    /**
     * 适用存参数的POST 请求
     *
     * @param params 请求参数
     * @return 请求体
     */
    private RequestBody getRequestBody(Map<String, String> params) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (params != null) {
            List<String> keyList = new ArrayList<>(params.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                if (!TextUtils.isEmpty(params.get(keyList.get(i)))) {
                    formBody.add(keyList.get(i), params.get(keyList.get(i)));//添加键值对参数
                }
            }
        }
        return formBody.build();
    }

    private RequestBody getRequestBody2(Map<String, String> params, List<String> filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params != null) {
            List<String> keyList = new ArrayList<>(params.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                if (!TextUtils.isEmpty(params.get(keyList.get(i)))) {
                    builder.addFormDataPart(keyList.get(i), params.get(keyList.get(i)));//添加键值对参数
                }
            }
        }

        if (filePaths != null && filePaths.size() > 0) {
            for (String filePath : filePaths) {
                File file = new File(filePath);
                builder.addFormDataPart("files", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }


        return builder.build();
    }

    public static class Builder {
        String url;
        String baseUrl;
        String relativeUrl;
        int requestType = TYPE_GET;
        Map<String, String> requestParams;
        List<String> filePaths;
        boolean useCache = false;
        boolean wrapperResponse = true;
        boolean wrapperWholeResponse = false;

        public Builder() {
            baseUrl = Engine.getInstance().mConfiguration.getCurrentModelApiUrl();
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setRelativeUrl(String relativeUrl) {
            this.relativeUrl = relativeUrl;
            return this;
        }

        public Builder setRequestType(int requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder setRequestParams(Map<String, String> requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public Builder setUploadFilePaths(List<String> filePaths) {
            this.filePaths = filePaths;
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Builder setWrapperResponse(boolean wrapperResponse) {
            this.wrapperResponse = wrapperResponse;
            return this;
        }

        public Builder setWrapperWholeResponse(boolean wrapperWholeResponse) {
            this.wrapperWholeResponse = wrapperWholeResponse;
            return this;
        }

        public Request build() {
            if (TextUtils.isEmpty(relativeUrl)) {
                url = baseUrl;
            } else {
                url = baseUrl + relativeUrl;
            }
            return new Request(this);
        }
    }
}
