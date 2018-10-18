package cc.xiaojiang.liangbo.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by zhu on 16-12-8.
 */

public class MainMenuLayout extends ViewGroup implements View.OnClickListener {

    public static final int ANIM_TIME = 2 * 100;

    /**
     * 自定义ViewGroup
     */
    private boolean isExpand = false;
    private int mMainMenuWidth;
    private int mMainMenuHeight;
    /**
     * 菜单是否展开
     */
    private boolean isMenuOpen = false;

    public MainMenuLayout(Context context) {
        this(context, null);
    }

    public MainMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (!b) {
            return;
        }
        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            View child = getChildAt(j);
            switch (j) {
                case 0:
                    child.layout(0, (getHeight() - mMainMenuHeight / 2)
                                    - child.getMeasuredHeight() / 2, child.getMeasuredWidth()
                            , (getHeight() - mMainMenuHeight / 2) + child.getMeasuredHeight() / 2);
                    child.setVisibility(GONE);
                    break;
                case 1:
                    child.layout((getWidth() - mMainMenuWidth / 2) - child.getMeasuredWidth() / 2, 0
                            , (getWidth() - mMainMenuWidth / 2) + child.getMeasuredWidth() / 2,
                            child.getMeasuredHeight());
                    child.setVisibility(GONE);
                    break;
                /**
                 *  中心点
                 */
                case 2:
                    child.layout(getWidth() - child.getMeasuredWidth(), getHeight() -
                            child.getMeasuredHeight(), getWidth(), getHeight());
                    child.setOnClickListener(this);
                    break;

            }
        }
        animateMenuIn();

    }

    /**
     * 主菜单点击事件
     */
    @Override
    public void onClick(View view) {
        for (int i = 0; i < 2; i++) {
            View child = getChildAt(i);
            child.setVisibility(VISIBLE);
        }
        if (isExpand) {
            animateMenuIn();
        } else {
            animateMenuOut();
        }
        isExpand = !isExpand;
    }

    /**
     * 菜单收起
     */
    private void animateMenuIn() {
        isMenuOpen = false;
        int count = getChildCount();
        for (int j = 0; j < count - 1; j++) {
            View child = getChildAt(j);
            switch (j) {
                /**
                 *  横向
                 */
                case 0:
                    ObjectAnimator animatorX1 = ObjectAnimator.ofFloat(child, "pivotX", getWidth
                            () - mMainMenuWidth / 2);
                    ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(child, View.SCALE_X, 1, 0);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(animatorX1).with(animatorX2);
                    animatorSet.setDuration(ANIM_TIME);
                    animatorSet.start();
                    break;
                /**
                 *  纵向
                 */
                case 1:
                    ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(child, "pivotY", getHeight
                            () - mMainMenuHeight / 2);
                    ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(child, View.SCALE_Y, 1, 0);
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    animatorSet2.play(animatorY1).with(animatorY2);
                    animatorSet2.setDuration(ANIM_TIME);
                    animatorSet2.start();
                    break;
            }
        }
    }

    /**
     * 菜单展开
     */
    private void animateMenuOut() {
        isMenuOpen = true;
        int count = getChildCount();
        for (int j = 0; j < count - 1; j++) {
            View child = getChildAt(j);
            switch (j) {
                /**
                 *  横向
                 */
                case 0:
                    ObjectAnimator animatorX1 = ObjectAnimator.ofFloat(child, "pivotX", getWidth
                            () - mMainMenuWidth / 2);
                    ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(child, View.SCALE_X, 0, 1);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(animatorX1).with(animatorX2);
                    animatorSet.setDuration(ANIM_TIME);
                    animatorSet.start();
                    break;
                /**
                 *  纵向
                 */
                case 1:
                    ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(child, "pivotY", getHeight
                            () - mMainMenuHeight / 2);
                    ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(child, View.SCALE_Y, 0, 1);
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    animatorSet2.play(animatorY1).with(animatorY2);
                    animatorSet2.setDuration(ANIM_TIME);
                    animatorSet2.start();
                    break;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = 0;
        int totalHeight = 0;
        int count = getChildCount();
        View mainMenu = getChildAt(2);
        mMainMenuWidth = mainMenu.getMeasuredWidth();
        mMainMenuHeight = mainMenu.getMeasuredHeight();
        measureChild(mainMenu, widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count - 1; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            switch (i) {
                case 0:
                    totalWidth = child.getMeasuredWidth() + mMainMenuWidth / 2;
                    break;
                case 1:
                    totalHeight = child.getMeasuredHeight() + mMainMenuHeight / 2;
                    break;
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);

    }


    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public void closeMenu() {
        isExpand = false;
        animateMenuIn();
    }

    public void openMenu() {
        isExpand = true;
        animateMenuIn();
    }
}
