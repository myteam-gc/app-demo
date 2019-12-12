package com.hnhy.ylfz.mvp.model.bean;

import android.support.annotation.NonNull;

/**
 * Created by guc on 2019/12/12.
 * 描述：服务历史搜索
 */
public class BeanSearch implements Comparable<BeanSearch> {
    public String key;//搜索内容

    public BeanSearch(String k) {
        this.key = k;
    }

    @Override
    public int compareTo(@NonNull BeanSearch o) {
        if (o != null && o.key != null && o.key.equals(key))
            return 0;
        return -1;
    }
}
