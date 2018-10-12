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
            onSuccess(baseModel.getData());
        } else {
            ToastUtils.show(baseModel.getMsg());
            onError(new Throwable(baseModel.getMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);


}
