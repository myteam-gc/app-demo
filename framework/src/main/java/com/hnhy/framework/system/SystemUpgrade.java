package com.hnhy.framework.system;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hnhy.framework.Engine;
import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.system.net.Request;
import com.hnhy.framework.system.net.RequestCallback;
import com.hnhy.framework.system.net.Response;
import com.hnhy.framework.system.upgrade.BeanVersion;
import com.hnhy.framework.system.upgrade.DialogFragmentUpgrade;
import com.hnhy.framework.util.FrameworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guc on 2018/4/26.
 * 描述：升级检测
 */
public class SystemUpgrade extends BaseSystem {

    @Override
    protected void init() {
    }

    @Override
    protected void destroy() {
    }

    public void check(final boolean autoCheck) {
        Map<String, String> params = new HashMap<>();
        params.put("versionCode", String.valueOf(FrameworkUtils.App.getVersionCode()));
        params.put("pkgName", Engine.getInstance().mContext.getPackageName());

        //测试
//        params.put("versionCode", "1");
//        params.put("pkgName", "com.huiyunit.qfqz");

        Request request = new Request.Builder().setBaseUrl(Engine.getInstance().mConfiguration.getCurrentModelUpgradeUrl())
                .setRequestParams(params)
                .build();
        getSystem(SystemHttp.class).net(getSystem(SystemPager.class).getCurrActivity(), request, new RequestCallback<BeanVersion>() {

            @Override
            public void onStart() {
                super.onStart();
                if (!autoCheck && getSystem(SystemPager.class).getCurrActivity() instanceof BaseActivity) {
                    BaseActivity a = (BaseActivity) getSystem(SystemPager.class).getCurrActivity();
                    a.showLoadingDialog(true);
                }
            }

            @Override
            public void onSuccess(BeanVersion data, Response resp) {
                super.onSuccess(data, resp);
                if (data.needUpdate) {
                    data.mSaveFileDir = Engine.getInstance().mConfiguration.getUpgradeApkDir();

                    DialogFragmentUpgrade dialogFragmentUpgrade = new DialogFragmentUpgrade();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", data);
                    dialogFragmentUpgrade.setArguments(bundle);
                    dialogFragmentUpgrade.show(((FragmentActivity) getSystem(SystemPager.class).getCurrActivity()).getSupportFragmentManager(), "dialog");
                } else {
                    if (!autoCheck) {
                        FrameworkUtils.Toast.showShort("已是最新版本");
                    }
                }
            }

            @Override
            public void onFailure(Response resp) {
                super.onFailure(resp);
                FrameworkUtils.Toast.showShort("检测升级失败");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                if (!autoCheck && getSystem(SystemPager.class).getCurrActivity() instanceof BaseActivity) {
                    BaseActivity a = (BaseActivity) getSystem(SystemPager.class).getCurrActivity();
                    a.showLoadingDialog(false);
                }
            }
        });
    }
}
