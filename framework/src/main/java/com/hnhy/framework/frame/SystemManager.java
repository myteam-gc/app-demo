package com.hnhy.framework.frame;

import com.hnhy.framework.system.SystemCrash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Author: hardcattle
 * Time: 2018/3/9 下午4:26
 * Description:
 */
public class SystemManager {
    private static SystemManager mInstance;
    private HashMap<String, BaseSystem> mSystemPool;

    private SystemManager() {
        mSystemPool = new HashMap<>();
        // 自启动必要system
        getSystem(SystemCrash.class);
    }

    public static SystemManager getInstance() {
        if (mInstance == null) {
            synchronized (SystemManager.class) {
                mInstance = new SystemManager();
            }
        }
        return mInstance;
    }

    public <T> void destroySystem(Class<T> className) {
        BaseSystem baseSystem;
        if ((baseSystem = mSystemPool.get(className.getName())) != null) {
            baseSystem.destroySystem();
            mSystemPool.remove(baseSystem);
        }
    }

    public void destroyAllSystem() {
        Iterator<Entry<String, BaseSystem>> iterator = mSystemPool.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, BaseSystem> next = iterator.next();
            next.getValue().destroySystem();
        }
        mSystemPool.clear();
    }

    public <T extends BaseSystem> T getSystem(Class<T> className) {
        if (className == null) {
            return null;
        }
        T instance = (T) mSystemPool.get(className.getName());
        if (instance == null) {
            try {
                instance = className.newInstance();
                instance.createSystem();
                mSystemPool.put(className.getName(), instance);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return instance;
    }
}
