package cc.xiaojiang.baselibrary.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import cc.xiaojiang.baselibrary.R;

/**
 * Time :18-4-20
 * @author jinjiafeng
 */

public class CommonDialog extends Dialog {

    private final AlertController mAlert;

    private CommonDialog(@NonNull Context context) {
        this(context,0);
    }

    private CommonDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog);
        mAlert = new AlertController(this, getWindow());
    }

    public void setText(int id, CharSequence charSequence) {
        mAlert.setText(id, charSequence);
    }

    public void setOnClickListener(int id, View.OnClickListener listener) {
        mAlert.setOnClickListener( id, listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
       return mAlert.getView(id);
    }


    public static class Builder {

        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams();
            P.mContext = context;
            P.mThemeId = themeResId;
        }

        public Builder setContentView(@LayoutRes int layoutId){
            P.mView = null;
            P.mLayoutId = layoutId;
            return this;
        }

        public Builder setContentView(View view){
            P.mView = view;
            P.mLayoutId = 0;
            return this;
        }

        /**
         *设置结束dialog的监听回调
         * @param onCancelListener 监听回调
         * @return Builder
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setText(@IdRes int id,CharSequence charSequence) {
            P.mTestArray.put(id,charSequence);
            return this;
        }

        public Builder setOnclickListener(@IdRes int id,View.OnClickListener listener) {
            P.mOnclickArray.put(id,listener);
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setFullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder setWidthHeight(int width,int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder fromBottom(boolean isAnimation){
            if(isAnimation){
                P.mAnimation = R.style.dialog_from_bottom;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder addDefaultAnimation(){
            P.mAnimation = R.style.dialog_scale_anim;
            return this;
        }

        public Builder setAnimation(@StyleRes int animationId){
            P.mAnimation = animationId;
            return this;
        }

        public Builder setCancelable(boolean cancelable){
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public CommonDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final CommonDialog dialog = new CommonDialog(P.mContext, P.mThemeId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public CommonDialog show() {
            final CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}

