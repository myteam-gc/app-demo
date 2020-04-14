package com.hnhy.ylfz.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.ylfz.R;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lljjcoder.style.citylist.utils.CityListLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityIdentificationInfo extends BaseActivity {

    @BindView(R.id.tv_province_city)
    TextView mTvProvinceCity;
    @BindView(R.id.iv1)
    ImageView mIv1;
    @BindView(R.id.iv2)
    ImageView mIv2;
    @BindView(R.id.iv3)
    ImageView mIv3;
    @BindView(R.id.iv4)
    ImageView mIv4;

    public static void jump(Context context) {
        Intent intent = new Intent(context, ActivityIdentificationInfo.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar();
        setContentView(R.layout.activity_identification_info);
        ButterKnife.bind(this);
        /**
         * 预先加载一级列表显示 全国所有城市市的数据
         */
        CityListLoader.getInstance().loadCityData(this);
    }

    @OnClick(R.id.tv_province_city)
    public void onViewClicked() {
        JDCityPicker cityPicker = new JDCityPicker();
        JDCityConfig jdCityConfig = new JDCityConfig.Builder().setJDCityShowType(JDCityConfig.ShowType.PRO_CITY).build();

//        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                mTvProvinceCity.setText(province.getId().equals(city.getId()) ? province.getName() : province.getName() + city.getName());
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

}
