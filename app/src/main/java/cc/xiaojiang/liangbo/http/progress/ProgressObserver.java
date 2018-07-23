package cc.xiaojiang.liangbo.http.progress;

import android.content.Context;
import android.widget.Toast;

import cc.xiaojiang.liangbo.http.ApiException;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {

    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable mDisposable;

    public abstract void onSuccess(T t);

    public ProgressObserver(Context context) {
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG)
                    .sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        showProgressDialog();
//        com.orhanobut.logger.Logger.d("onSubscribe");
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
//        com.orhanobut.logger.Logger.d("onNext");
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        if (e.getMessage() == null) {
            ToastUtils.show("未知错误！");
        }
        ToastUtils.show(e.getMessage());
        com.orhanobut.logger.Logger.e(e.getMessage());
        if (ApiException.class.isInstance(e)) {
            // TODO: 2018/5/16
        } else if (HttpException.class.isInstance(e)) {
            // TODO: 2018/5/16  
        } else {
            // TODO: 2018/5/16  
        }
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
//        com.orhanobut.logger.Logger.d("onComplete");
    }

    @Override
    public void onCancelProgress() {
//        com.orhanobut.logger.Logger.d("onCancelProgress");
        mDisposable.dispose();
    }


}
