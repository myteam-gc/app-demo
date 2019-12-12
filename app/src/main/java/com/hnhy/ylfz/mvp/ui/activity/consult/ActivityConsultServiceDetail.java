package com.hnhy.ylfz.mvp.ui.activity.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;

import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/12.
 * 描述：服务历史详情
 */
public class ActivityConsultServiceDetail extends BaseActivity {
    public static void jump(Context context) {
        Intent intent = new Intent(context, ActivityConsultServiceDetail.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_consult_service_detail);
        ButterKnife.bind(this);
    }

}
