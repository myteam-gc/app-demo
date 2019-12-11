package com.hnhy.ylfz.mvp.ui.activity.noble;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hnhy.ylfz.R;
import com.hnhy.ylfz.app.SharedPreferencesManager;
import com.hnhy.framework.frame.BaseActivity;
import com.hnhy.framework.system.SystemPermission;
import com.hnhy.framework.system.permission.CallbackPermission;
import com.hnhy.framework.system.permission.PermissionOption;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2018/4/23.
 * 描述：滑动页
 */
public class ActivityGuide extends BaseActivity {

    private static int[] mImages = {R.drawable.bg_guide_1, R.drawable.bg_guide_2, R.drawable.bg_guide_3};
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        ViewPager.LayoutParams params = new ViewPager.LayoutParams();
        List<ImageView> imageViewList = new ArrayList<>(3);
        for (int i = 0; i < mImages.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);//设置布局
            iv.setImageResource(mImages[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(iv);
            if (i == mImages.length - 1) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesManager.getInstance().setFirstOpenOver();
                        Intent intent = new Intent(ActivityGuide.this, ActivityLoading.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

        AdapterViewPager1 adapter = new AdapterViewPager1(imageViewList);
        mViewPager.setAdapter(adapter);

        checkPermissions();
    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.CAMERA
                , Manifest.permission.CALL_PHONE
                , Manifest.permission.ACCESS_FINE_LOCATION
        };
        PermissionOption option = new PermissionOption.Builder().setPermissions(permissions).build();
        getSystem(SystemPermission.class).request(option, new CallbackPermission() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> permissions) {
                showToast(permissions.toString() + "权限拒绝");
                finish();
            }
        });
    }

    class AdapterViewPager1 extends PagerAdapter {

        private List<ImageView> mImageViews;

        AdapterViewPager1(List<ImageView> imageViews) {
            this.mImageViews = imageViews;
        }

        @Override
        public int getCount() {
            return mImageViews == null ? 0 : mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }
    }

}
