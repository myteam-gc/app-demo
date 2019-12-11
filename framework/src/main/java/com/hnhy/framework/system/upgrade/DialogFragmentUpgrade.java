package com.hnhy.framework.system.upgrade;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hnhy.framework.R;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemDownload;
import com.hnhy.framework.system.download.CallbackDownload;
import com.hnhy.framework.system.download.Task;
import com.hnhy.framework.util.FrameworkUtils;
import com.hnhy.ui.NumberProgressBar;

/**
 * Author: wjy
 * Date: 2018/7/26
 * Description:
 */
public class DialogFragmentUpgrade extends DialogFragment implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_update_info;
    //    private TextView tv_size;
//    private TextView tv_cancel;
//    private TextView tv_sure;
    private BeanVersion mBeanVersion;
    private Button btn_ok;
    private NumberProgressBar npb;
    //    private ProgressBar pb_progress_bar;
//    private TextView tv_progress;

    private Handler handler = new Handler();

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    getActivity().moveTaskToBack(true);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lib_update_app_dialog2, container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mBeanVersion = (BeanVersion) getArguments().get("data");

        tv_title = view.findViewById(R.id.tv_title);
        tv_update_info = view.findViewById(R.id.tv_update_info);
        btn_ok = view.findViewById(R.id.btn_ok);
        setBtnTheme();
        LinearLayout ll_close = view.findViewById(R.id.ll_close);
        npb = view.findViewById(R.id.npb);
//        tv_cancel = view.findViewById(R.id.btn_ok);
//        tv_sure = view.findViewById(R.id.ll_close);
//        tv_progress = view.findViewById(R.id.tv_progress);
//        pb_progress_bar = view.findViewById(R.id.pb_progress);
//
//        tv_cancel.setOnClickListener(this);
//        tv_sure.setOnClickListener(this);
//
        if (mBeanVersion.forceUpdate) {
            ll_close.setVisibility(View.GONE);
        }

        tv_title.setText("是否升级到" + mBeanVersion.newVersion + "版本？");//
        tv_update_info.setText(mBeanVersion.updateJournal);// +
        float i = Float.parseFloat(mBeanVersion.fileSize) / (1024 * 1024);
        String substring = "新版本大小：" + String.valueOf(i).substring(0, 4) + "M\n\n" + mBeanVersion.updateJournal;
        tv_update_info.setText(substring);

        btn_ok.setOnClickListener(this);
        ll_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            SystemDownload systemDownload = SystemManager.getInstance().getSystem(SystemDownload.class);
            Task task = new Task.Builder()
                    .setUrl(mBeanVersion.fileUrl)
                    .setSaveFileDir(mBeanVersion.mSaveFileDir)
                    .setSaveFileName(mBeanVersion.mSaveFileName)
                    .build();

            systemDownload.download(task);
            systemDownload.setCallbackDownload(0, new CallbackDownload() {
                @Override
                public void onWait(Task task) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_ok.setVisibility(View.GONE);
                            npb.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onStart(Task task) {
                }

                @Override
                public void onProgress(final Task task) {
                    npb.setProgress(task.progress);
                    npb.setMax(100);
                }

                @Override
                public void onSuccess(Task task) {
//                    tv_progress.setText("下载完成!");
//                    tv_sure.setEnabled(true);
//                    tv_cancel.setEnabled(true);
                    getDialog().dismiss();
                    FrameworkUtils.App.installApp(task.saveFilePath);
                }

                @Override
                public void onFailure(Task task) {
                    Toast.makeText(DialogFragmentUpgrade.this.getContext(), "下载失败:" + task.msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete(Task task) {

                }
            });
            Toast.makeText(DialogFragmentUpgrade.this.getContext(), "开始下载", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.ll_close) {
            dismiss();
        }
    }

    private void setBtnTheme() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, getSolidRectDrawable(10, Color.parseColor("#C62828")));
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, getSolidRectDrawable(10, Color.parseColor("#F44336")));
        //设置不能用的状态
        //默认其他状态背景
        GradientDrawable gray = getSolidRectDrawable(10, Color.GRAY);
        stateListDrawable.addState(new int[]{}, gray);
        btn_ok.setBackgroundDrawable(stateListDrawable);
//        mNumberProgressBar.setProgressTextColor(color);
//        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        btn_ok.setTextColor(Color.WHITE);
    }

    public GradientDrawable getSolidRectDrawable(int cornerRadius, int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 设置矩形的圆角半径
        gradientDrawable.setCornerRadius(cornerRadius);
        // 设置绘画图片色值
        gradientDrawable.setColor(solidColor);
        // 绘画的是矩形
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gradientDrawable;
    }
}