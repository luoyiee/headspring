package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.activity.ShareActivity;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.model.event.ShareBitmapEvent;

/**
 * @author :jinjiafeng
 * date:  on 18-7-9
 * description:
 */
public class ScreenShotUtils {
    public static void share(BaseActivity activity) {
        Bitmap bitmap = ScreenShotUtils.screenShot(activity);
        EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
        activity.startToActivity(ShareActivity.class);
    }

    /**
     * 获取当前屏幕截图，不包含状态栏（Status Bar）。
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);


        int width = ScreenUtils.getScreenWidth(activity);
        int height = ScreenUtils.getScreenHeight(activity);
        int toolBarHeight = getToolBarHeight(activity);
        Bitmap ret = Bitmap.createBitmap(bmp, 0,
                statusBarHeight + toolBarHeight,
                width, height - statusBarHeight - toolBarHeight - ScreenUtils.dip2px(activity,
                        160));
        view.destroyDrawingCache();
        return ret;
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

    public static int getToolBarHeight(Activity activity) {
        int[] attrs = new int[]{R.attr.actionBarSize};
        TypedArray ta = activity.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

}
