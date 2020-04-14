package com.hnhy.ylfz.mvp.ui.activity.noble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.SharedPreferencesManager;
import com.hnhy.ylfz.mvp.ui.activity.user.ActivityLoginGuide;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by guc on 2019/7/15.
 * 描述：
 */
public class ActivityLoading extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        setStatusBar();
        toMain();
    }

    private void toMain() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (SharedPreferencesManager.getInstance().isFirstOpen()) {
                            Intent it = new Intent(ActivityLoading.this, ActivityGuide.class);
                            startActivity(it);
                        } else {
                            Intent it = new Intent(ActivityLoading.this, ActivityLoginGuide.class);
                            if (getIntent().getExtras() != null) {
                                it.putExtras(getIntent().getExtras());
                            }
                            startActivity(it);
                        }
                        finish();
                    }
                });
    }

}