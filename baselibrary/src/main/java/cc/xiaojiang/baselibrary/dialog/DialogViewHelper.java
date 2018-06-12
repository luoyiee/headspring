package cc.xiaojiang.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by jinjiafeng
 * Time :18-4-21
 * 找到layout控件和设置事件
 */

 class DialogViewHelper {
    /**
     * view缓存处理
     */
    private final SparseArray<WeakReference<View>> mViewBuffer = new SparseArray<>();
    private View mContentView;

    DialogViewHelper() {

    }

     DialogViewHelper(Context context, int layoutId) {
        mContentView = LayoutInflater.from(context).inflate(layoutId, null);
    }

     View getContentView() {
        return mContentView;
    }

     void setContentView(View view) {
        mContentView = view;
    }

     void setText(int id, CharSequence charSequence) {
        TextView textView = getView(id);
        if(textView!=null){
            textView.setText(charSequence);
        }
    }

     void setOnClickListener(int id, View.OnClickListener listener) {
        final View view = getView(id);
        if(view!=null){
            view.setOnClickListener(listener);
        }
    }

    @SuppressWarnings("unchecked")
     <T> T getView(int id) {
        final WeakReference<View> reference = mViewBuffer.get(id);
        View view = null;
        if (reference != null) {
            view = reference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(id);
        }
        return (T) view;
    }
}
