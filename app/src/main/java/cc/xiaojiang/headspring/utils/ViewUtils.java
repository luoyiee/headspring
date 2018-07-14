package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import cc.xiaojiang.headspring.base.MyApplication;

public class ViewUtils {

    /**
     * 设置tabLayout 下划线宽度
     *
     * @param tabs tabLayout
     */
    public static void setIndicator(TabLayout tabs) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip;
        LinearLayout llTab = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (llTab != null) {
            for (int i = 0; i < llTab.getChildCount(); i++) {
                View child = llTab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.leftMargin = ScreenUtils.dip2px(MyApplication.getInstance(),28);
                params.rightMargin = ScreenUtils.dip2px(MyApplication.getInstance(),28);
                child.setLayoutParams(params);
                child.invalidate();
            }
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
