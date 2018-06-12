package cc.xiaojiang.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jinjiafeng
 * Time :18-4-20
 */

class AlertController {
    private final CommonDialog mDialog;
    private final Window mWindow;
    private DialogViewHelper mViewHelper;
    AlertController(CommonDialog dialog, Window window) {
        mDialog = dialog;
        mWindow = window;
    }

     void setText(int id, CharSequence charSequence) {
         mViewHelper.setText(id,charSequence);
    }

     void setOnClickListener(int id, View.OnClickListener listener) {
         mViewHelper.setOnClickListener(id,listener);
    }

     <T extends View> T getView(int id) {
        return mViewHelper.getView(id);
    }
    private void setViewHelper(DialogViewHelper viewHelper){
        mViewHelper = viewHelper;
    }

    static class AlertParams {
        /**
         * 存储控件
         */
        final SparseArray<CharSequence> mTestArray = new SparseArray<>();
        final SparseArray<View.OnClickListener> mOnclickArray = new SparseArray<>();
        /**
         * 事件监听
         */
        DialogInterface.OnCancelListener mOnCancelListener;
        DialogInterface.OnDismissListener mOnDismissListener;
        DialogInterface.OnKeyListener mOnKeyListener;
        boolean mCancelable = true;
        Context mContext;
        int mThemeId;
        int mLayoutId;
        View mView;
        int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        int mAnimation = 0;
        int mGravity = Gravity.CENTER;


        void apply(AlertController alert) {
            DialogViewHelper helper = null;
            if (mLayoutId > 0) {
                helper = new DialogViewHelper(mContext, mLayoutId);
            }
            if (mView != null) {
                helper = new DialogViewHelper();
                helper.setContentView(mView);
            }
            //设置dialog布局
            if (helper != null) {
                final View contentView = helper.getContentView();
                alert.mDialog.setContentView(contentView);
            } else {
                throw new IllegalArgumentException("请设置布局");
            }

            //设置view的text属性
            for (int i = 0; i < mTestArray.size(); i++) {
                helper.setText(mTestArray.keyAt(i), mTestArray.valueAt(i));
            }
            //设置点击事件
            for (int i = 0; i < mOnclickArray.size(); i++) {
                helper.setOnClickListener(mOnclickArray.keyAt(i), mOnclickArray.valueAt(i));
            }
            //设置主题id
            if (mThemeId > 0) {
                mContext.setTheme(mThemeId);
            }
            alert.setViewHelper(helper);

            final Window window = alert.mWindow;

            window.setGravity(mGravity);

            //设置dialog的width,height
            final WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = mWidth;
            attributes.height = mHeight;
            window.setAttributes(attributes);
            //设置动画效果
            if (mAnimation > 0) {
                window.setWindowAnimations(mAnimation);
            }
        }
    }
}
