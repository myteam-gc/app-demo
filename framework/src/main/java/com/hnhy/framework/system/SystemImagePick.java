package com.hnhy.framework.system;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.util.FrameworkUtils;

import java.io.File;
import java.util.Date;

/**
 * Author: wjy
 * Date: 2018/8/13
 * Description:
 */
public class SystemImagePick extends BaseSystem {
    private static final int CODE_TAKE_PHOTO = 1000;
    private static final int CODE_PICK_ALBUM = 2000;
    private static final String TAG_HOOK_FRAGMENT = "tag_hook_fragment";

    private static final int TYPE_TAKE_PHOTO = 0;
    private static final int TYPE_PICK_ALBUM = 1;

    private FragmentManager mFragmentManager;
    private Fragment mHookFragment;
    private String mDirImage;
    private String mTaskPhotoFilePath;
    private Callback mCallback;


    private int mCurrentType;

    @Override
    protected void init() {
        mDirImage = mContext.getExternalCacheDir() + "/images/";
    }

    @Override
    protected void destroy() {

    }

    public void pickImage(int type, Callback callback) {
        mCallback = callback;
        mCurrentType = type;

        startHookFragment();
        if (mHookFragment.isAdded()) {
            if (type == TYPE_TAKE_PHOTO) {
                takePhoto();
            } else {
                pickAlbum();
            }
        }
    }

    private void onAttach() {
        if (mCurrentType == TYPE_PICK_ALBUM) {
            pickAlbum();
        } else {
            takePhoto();
        }
    }

    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (mCallback != null) {
                if (requestCode == CODE_TAKE_PHOTO) {
                    mCallback.onPickImages(new String[]{mTaskPhotoFilePath});
                } else {
                    Uri selectedImage = data.getData();
                }
            }
        }
    }

    private void startHookFragment() {
        if (mFragmentManager == null) {
            mFragmentManager = getSystem(SystemPager.class).getCurrFragmentActivity().getSupportFragmentManager();
        }
        mHookFragment = mFragmentManager.findFragmentByTag(TAG_HOOK_FRAGMENT);
        if (mHookFragment == null) {
            mHookFragment = new HookFragment();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(mHookFragment, TAG_HOOK_FRAGMENT);
            transaction.commit();
        }
    }

    private void pickAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mHookFragment.startActivityForResult(intent, CODE_PICK_ALBUM);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = FrameworkUtils.DateTime.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + ".png";
        mTaskPhotoFilePath = mDirImage + fileName;
        File file = new File(mTaskPhotoFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri uri = FrameworkUtils.getUriForFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mHookFragment.startActivityForResult(intent, CODE_TAKE_PHOTO);
    }

    public interface Callback {
        void onPickImages(String[] imagePaths);
    }

    public static class HookFragment extends Fragment {

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            SystemManager.getInstance().getSystem(SystemImagePick.class).onAttach();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            SystemManager.getInstance().getSystem(SystemImagePick.class).onActivityResult(requestCode, resultCode, data);
        }
    }
}
