package com.hnhy.framework.system;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hnhy.framework.frame.BaseSystem;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.util.FrameworkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Author: wjy
 * Date: 2018/8/13
 * Description:
 */
public class SystemImagePick extends BaseSystem {
    private static final int CODE_TAKE_PHOTO = 1000;
    private static final int CODE_PICK_ALBUM = 2000;
    public static final int TYPE_TAKE_PHOTO = 0;
    private static final String TAG_HOOK_FRAGMENT = "tag_hook_fragment";
    public static final int TYPE_PICK_ALBUM = 1;
    private static final int PHOTO_REQUEST_CUT = 3000;

    private FragmentManager mFragmentManager;
    private Fragment mHookFragment;
    private String mDirImage;
    private String mTaskPhotoFilePath;
    private Callback mCallback;


    private int mCurrentType;
    /**
     * 是否需要裁减
     */
    private boolean isNeedCut = false;

    private File tempFile;

    @Override
    protected void init() {
        mDirImage = mContext.getExternalCacheDir() + "/images/";
    }

    @Override
    protected void destroy() {

    }

    public void pickImage(int type, Callback callback, boolean needCut) {
        isNeedCut = needCut;
        pickImage(type, callback);
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

                switch (requestCode) {
                    case CODE_TAKE_PHOTO:
                        if (isNeedCut) {
                            startPhotoZoom(FrameworkUtils.getUriForFile(tempFile));
                        } else {
                            mCallback.onPickImages(new String[]{mTaskPhotoFilePath});
                        }
                        break;
                    case CODE_PICK_ALBUM:
                        Uri selectedImage = data.getData();
                        break;
                    case PHOTO_REQUEST_CUT:
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mTaskPhotoFilePath);
                            if (bm != null)
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        mCallback.onPickImages(new String[]{mTaskPhotoFilePath});
                        break;
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
        tempFile = new File(mTaskPhotoFilePath);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        Uri uri = FrameworkUtils.getUriForFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mHookFragment.startActivityForResult(intent, CODE_TAKE_PHOTO);
    }

    //开始裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mHookFragment.startActivityForResult(intent, PHOTO_REQUEST_CUT);
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
