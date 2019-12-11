package com.hnhy.framework.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hnhy.framework.Engine;
import com.hnhy.framework.frame.SystemManager;
import com.hnhy.framework.system.SystemPager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class FrameworkUtils {

    /**
     * 用来判断服务是否运行.
     *
     * @param context   the context
     * @param className 判断的服务名字 eg:"com.xxx.xx..XXXService"
     * @return true 在运行, false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            ActivityManager.RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 判断应用是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isRun(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }
    public static Uri getUriForFile(File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(Engine.getInstance().mContext,
                    Engine.getInstance().mContext.getPackageName() + ".android7.fileprovider",
                    file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 待匹配字符串
     * @return true：匹配，false：不匹配
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
    /**
     * 强制类型转换
     *
     * @param obj 数据
     * @param <T> 指定类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
    public static int dp2px(final float dpValue) {
        final float scale = Engine.getInstance().mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Engine.getInstance().mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isSDCardEnable() {
        return !getSDCardPaths().isEmpty();
    }

    public static List<String> getSDCardPaths() {
        StorageManager storageManager = (StorageManager) Engine.getInstance().mContext
                .getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public static void call(final String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Engine.getInstance().mContext.startActivity(intent);
    }

    /**
     * 获取泛型类型
     *
     * @param genType class
     * @return type
     */
    public static ParameterizedType getParameterizedType(Class genType) {
        if (genType == null) {
            return null;
        }
        if (genType.getGenericSuperclass() instanceof ParameterizedType) {
            return (ParameterizedType) genType.getGenericSuperclass();
        } else if ((genType = genType.getSuperclass()) != null && genType != Object.class) {
            return getParameterizedType(genType);
        } else {
            return null;
        }
    }

    /**
     * 判断网络是否连接.
     *
     * @param context the context
     * @return boolean
     * true:网络可用,false：网络未连接
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static class App {
        public static String getVersionName() {
            return getVersionName(Engine.getInstance().mContext.getPackageName());
        }

        public static int getVersionCode() {
            return getVersionCode(Engine.getInstance().mContext.getPackageName());

        }

        /**
         * Return the application's version name.
         *
         * @param packageName The name of the package.
         * @return the application's version name
         */
        public static String getVersionName(final String packageName) {
            if (TextUtils.isEmpty(packageName)) return "";
            try {
                PackageManager pm = Engine.getInstance().mContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi == null ? null : pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return "";
            }
        }

        public static int getVersionCode(final String packageName) {
            if (TextUtils.isEmpty(packageName)) return 0;
            try {
                PackageManager pm = Engine.getInstance().mContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                return pi.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public static void installApp(String apkFilePath) {
            File file = new File(apkFilePath);
            if (!file.exists()) return;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = getUriForFile(file);
            String type = "application/vnd.android.package-archive";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(data, type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Engine.getInstance().mContext.startActivity(intent);
        }
    }

    /**
     * file
     */
    public static class Files {
        public static void writeFile(@NonNull final String filePath, @NonNull final String content, final boolean append, final boolean autoLine) {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    if (!file.createNewFile()) return;
                }

                FileOutputStream fileOutputStream = new FileOutputStream(file, append);
                fileOutputStream.write(content.getBytes());
                if (append && autoLine) {
                    fileOutputStream.write("\r\n".getBytes());
                }

                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String readFile(final String filePath) {
            // TODO: 2018/9/25 read file
            return "";
        }
    }

    /**
     * Toast
     */
    public static class Toast {
        public static void showShort(final String msg) {
            show(msg, android.widget.Toast.LENGTH_SHORT);
        }

        public static void showShort(final int resId) {
            show(Engine.getInstance().mContext.getString(resId), android.widget.Toast.LENGTH_SHORT);
        }

        public static void show(final String msg, final int tag) {
            android.widget.Toast.makeText(Engine.getInstance().mContext, msg, tag).show();
        }
    }

    /**
     * Date
     */
    public static class DateTime {
        /**
         * 日期对象转为指定格式的字符串
         *
         * @param date    日期对象
         * @param pattern 时间格式：eg:yyyy-MM-dd HH:mm:ss
         * @return 时间字符串
         */
        public static String dateFormat(final Date date, final String pattern) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
            return sdf.format(date);
        }

        /**
         * 日期对象转为指定格式的字符串
         *
         * @param timeStamp 时间戳
         * @param pattern   时间格式：eg:yyyy-MM-dd HH:mm:ss
         * @return 时间字符串
         */
        public static String dateFormat(final long timeStamp, final String pattern) {
            Date date = new Date(timeStamp);
            return dateFormat(date, pattern);
        }

        /**
         * 日期对象转为指定格式的字符串
         *
         * @param timeStamp 时间戳
         * @param pattern   时间格式：eg:yyyy-MM-dd HH:mm:ss
         * @return 时间字符串
         */
        public static String dateFormat(final String timeStamp, final String pattern) {
            try {
                Long timeMills = Long.parseLong(timeStamp);
                Date date = new Date(timeMills);
                return dateFormat(date, pattern);
            } catch (NumberFormatException e) {
                return "" + timeStamp;
            }
        }

        /**
         * 时间格式转换
         *
         * @param originPattern 原时间格式
         * @param targetPattern 目标时间格式
         * @param dateStr       时间字符串
         * @return 目标格式时间字符串
         */
        public static String dateFormatTranslate(final String originPattern, final String targetPattern, final String dateStr) {
            SimpleDateFormat sdf = new SimpleDateFormat(originPattern, Locale.CHINA);
            try {
                Date date = sdf.parse(dateStr);
                return dateFormat(date, targetPattern);
            } catch (ParseException e) {
                e.printStackTrace();
                return originPattern;
            }
        }

        /**
         * 时间字符串转为Date
         *
         * @param dateStr 日期字符串
         * @param pattern 格式化字符串
         * @return date
         */
        public static Date dateStr2Date(final String dateStr, final String pattern) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class Bitmap {
        /**
         * bitmap 转 base64
         *
         * @param bitmap  bitmap
         * @param quality 压缩质量
         * @return base64 str
         */
        public static String bitmap2Base64(final android.graphics.Bitmap bitmap, final int quality) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, quality, bos);//压缩40%
            byte[] bytes = bos.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }

        public static android.graphics.Bitmap base64ToBitmap(final String string) {
            android.graphics.Bitmap bitmap = null;
            if (TextUtils.isEmpty(string)) return null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        /**
         * bitmap 转为byte数组
         *
         * @param bitmap
         * @param format
         * @return
         */
        public static byte[] bitmap2Bytes(android.graphics.Bitmap bitmap, final android.graphics.Bitmap.CompressFormat format) {
            if (bitmap == null) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(format, 100, baos);
            return baos.toByteArray();
        }

        /**
         * byte数组转为bitmap对象
         *
         * @param bytes
         * @return
         */
        public static android.graphics.Bitmap bytes2Bitmap(final byte[] bytes) {
            return (bytes == null || bytes.length == 0)
                    ? null
                    : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        /**
         * 将Bitmap对象 保存到本地
         * 需要读写内存卡的权限，否则会发生异常
         *
         * @param mBitmap
         * @param filePath 图片路径  xxx/xxx/xx.jpg
         * @return
         */
        public static String saveBitmap(android.graphics.Bitmap mBitmap, String filePath, int options) {
            File filePic;
            try {
                filePic = new File(filePath);
                if (!filePic.exists()) {
                    filePic.getParentFile().mkdirs();
                    filePic.createNewFile();
                } else {
                    filePic.delete();
                    filePic.getParentFile().mkdirs();
                    filePic.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(filePic);
                mBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, options, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return filePic.getAbsolutePath();
        }

    }

    public static class Screen {
        public static int getScreenWidth() {
            WindowManager wm = (WindowManager) Engine.getInstance().mContext.getSystemService(Context.WINDOW_SERVICE);
            if (wm == null) {
                return Engine.getInstance().mContext.getResources().getDisplayMetrics().widthPixels;
            }
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            return point.x;
        }

        /**
         * Return the height of screen, in pixel.
         *
         * @return the height of screen, in pixel
         */
        public static int getScreenHeight() {
            WindowManager wm = (WindowManager) Engine.getInstance().mContext.getSystemService(Context.WINDOW_SERVICE);
            if (wm == null) {
                return Engine.getInstance().mContext.getResources().getDisplayMetrics().heightPixels;
            }
            Point point = new Point();
            wm.getDefaultDisplay().getRealSize(point);
            return point.y;
        }

        public static void setWindowAlpha(final float alpha) {
            if (alpha < 0 || alpha > 1) return;
            SystemPager systemPager = SystemManager.getInstance().getSystem(SystemPager.class);

            WindowManager.LayoutParams windowLP = systemPager.getCurrActivity().getWindow().getAttributes();
            windowLP.alpha = alpha;
            if (alpha == 1) {
                systemPager.getCurrActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            } else {
                systemPager.getCurrActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
            }
            systemPager.getCurrActivity().getWindow().setAttributes(windowLP);
        }

        /**
         * 设置全屏显示
         *
         * @param activity
         */
        public static void setFullScreen(@NonNull final Activity activity) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        /**
         * 获取屏幕尺寸与密度.
         *
         * @param context the context
         * @return mDisplayMetrics
         */
        public static DisplayMetrics getDisplayMetrics(Context context) {
            Resources mResources;
            if (context == null) {
                mResources = Resources.getSystem();

            } else {
                mResources = context.getResources();
            }
            // DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5,
            // xdpi=160.421, ydpi=159.497}
            // DisplayMetrics{density=2.0, width=720, height=1280,
            // scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
            DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
            return mDisplayMetrics;
        }

        /**
         * 设置屏幕为横屏
         *
         * @param activity
         */
        public static void setLandscape(@NonNull final Activity activity) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /**
         * 设置屏幕为竖屏
         *
         * @param activity
         */
        public static void setPortrait(@NonNull final Activity activity) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        /**
         * 判断是否横屏
         *
         * @param context
         * @return true:横屏   false:竖屏
         */
        public static boolean isLandscape(Context context) {
            return context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE;
        }

        /**
         * 截屏
         *
         * @param activity
         * @return
         */
        public static android.graphics.Bitmap screenShot(@NonNull final Activity activity) {
            return screenShot(activity, false);
        }

        /**
         * 返回当前界面的Bitmap对象
         *
         * @param activity          The activity.
         * @param isDeleteStatusBar true:去掉状态栏，，false:包括状态栏.
         * @return 界面的Bitmap对象
         */
        public static android.graphics.Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            android.graphics.Bitmap bmp = decorView.getDrawingCache();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            android.graphics.Bitmap ret;
            if (isDeleteStatusBar) {
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = resources.getDimensionPixelSize(resourceId);
                ret = android.graphics.Bitmap.createBitmap(
                        bmp,
                        0,
                        statusBarHeight,
                        dm.widthPixels,
                        dm.heightPixels - statusBarHeight
                );
            } else {
                ret = android.graphics.Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
            }
            decorView.destroyDrawingCache();
            return ret;
        }
    }

}
