package cc.xiaojiang.liangbo.http;


import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class MyObserver<T> implements Observer<BaseModel<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseModel<T> baseModel) {
        if (baseModel.getCode() == BaseModel.SUCCESS) {
            onSucceed(baseModel.getData());
        } else {
            onFailed(String.valueOf(baseModel.getCode()), baseModel.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.show(e.getMessage());
        onFailed("-1", e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSucceed(T t);

    public abstract void onFailed(String code, String msg);


}
