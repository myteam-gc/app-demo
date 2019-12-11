package com.hnhy.framework.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.hnhy.framework.Configuration;
import com.hnhy.framework.Engine;
import com.hnhy.framework.R;
import com.hnhy.framework.frame.BaseActivity;

import tech.linjiang.pandora.Pandora;

public class ActivityAdmin extends BaseActivity {
    private static final String[] ARRAY_MODELS = {"debug", "beta", "release"};
    private static final String[] ARRAY_SWITCH = {"open", "close"};

    private Configuration mConfiguration;

    private AppCompatSpinner mSpinnerModel, mSpinnerLog, mSpinnerCrashLog, mSpinnerCrashRestart;
    private EditText mUrl;

    private boolean mFlagFirst = true;

    public static void jump(Context context) {
        context.startActivity(new Intent(context, ActivityAdmin.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mConfiguration = Engine.getInstance().mConfiguration;

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ARRAY_MODELS);
        ArrayAdapter<String> adapterSwitch = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ARRAY_SWITCH);

        mUrl = findViewById(R.id.url);

        mSpinnerModel = findViewById(R.id.spinner_model);
        mSpinnerCrashRestart = findViewById(R.id.spinner_crash_restart);
        mSpinnerCrashLog = findViewById(R.id.spinner_crash_log);
        mSpinnerLog = findViewById(R.id.spinner_log);

        mSpinnerModel.setAdapter(adapterModel);
        mSpinnerCrashRestart.setAdapter(adapterSwitch);
        mSpinnerCrashLog.setAdapter(adapterSwitch);
        mSpinnerLog.setAdapter(adapterSwitch);

        mSpinnerModel.setSelection(mConfiguration.getCurrentModel());
        mSpinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mFlagFirst) {
                    mFlagFirst = false;
                } else {
                    mConfiguration.setModel(position);
                    update();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfiguration.setEnableLog(mSpinnerLog.getSelectedItemPosition() == 0);
                mConfiguration.setEnableCrashReset(mSpinnerCrashRestart.getSelectedItemPosition() == 0);
                mConfiguration.setEnableSaveCrashLog(mSpinnerCrashLog.getSelectedItemPosition() == 0);
                mConfiguration.setUrl(mUrl.getText().toString());
                Engine.getInstance().updateConfiguration();
                showToast("Save success");
                finish();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.open_pandora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pandora.get().open();
            }
        });
        update();
    }

    private void update() {
        mSpinnerCrashRestart.setSelection(mConfiguration.isEnableSaveCrashLog() ? 0 : 1);
        mSpinnerCrashLog.setSelection(mConfiguration.isEnableSaveCrashLog() ? 0 : 1);
        mSpinnerLog.setSelection(mConfiguration.isEnableLog() ? 0 : 1);
        mUrl.setText(mConfiguration.getCurrentModelApiUrl());
    }
}
