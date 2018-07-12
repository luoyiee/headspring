package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;



public class ScreenUtils {
    public static int dip2px(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dp * density);
    }


    //px转dp
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics);
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        }
        return dm.heightPixels;
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        // 给白纸设置宽高
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);
        }
        return dm.widthPixels;
    }

    private static float sNoncompatDensity;
    /**
     * font->density
     */
    private static float sNoncompatScaledDensity;

    /**
     * 头条屏幕适配方案(https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
     * @param activity  activity
     */
    public static void setCustomDensity(@NonNull Activity activity){
        final Context application =activity.getApplication();
        final DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if(sNoncompatDensity == 0){
            sNoncompatDensity = displayMetrics.density;
            sNoncompatScaledDensity = displayMetrics.scaledDensity;
            //监听当用户在系统设置中设置字体大小,会调用监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if(newConfig!=null && newConfig.fontScale>0){
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //360dp为设计师给的图片尺寸(1920*1080)
        final float targetDensity = displayMetrics.widthPixels/360;
        final float targetScaledDensity = targetDensity*(sNoncompatScaledDensity/sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity;
        displayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
