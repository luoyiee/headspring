package cc.xiaojiang.liangbo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.activity.ShareActivity;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;

/**
 * @author :jinjiafeng
 * date:  on 18-7-9
 * description:
 */
public class ScreenShotUtils {
    public static void share(BaseActivity activity) {
        Bitmap bitmap = screenShot(activity);
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
        return screenShot(activity, ScreenUtils.dip2px(activity, 180));
    }

    /**
     * 获取当前屏幕截图，不包含状态栏（Status Bar）。
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(Activity activity, int height) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);

        int screenWidth = ScreenUtils.getScreenWidth(activity);
        int screenHeight = ScreenUtils.getScreenHeight(activity);
        int toolBarHeight = getToolBarHeight(activity);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight + toolBarHeight * 7 / 10,
                screenWidth,
                screenHeight - statusBarHeight - toolBarHeight * 7 / 10 - height);
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


    //    //把布局变成Bitmap
//    public static Bitmap getViewBitmap(View view) {
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();  //启用DrawingCache并创建位图
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
// 创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
//        view.setDrawingCacheEnabled(false);
//        return bitmap;//禁用DrawingCahce否则会影响性能
//    }
//
    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config
                .RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        canvas.setBitmap(null);
        return bitmap;
    }
}
