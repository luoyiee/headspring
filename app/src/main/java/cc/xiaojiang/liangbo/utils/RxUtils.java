package cc.xiaojiang.liangbo.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chao.qu at 2017/10/20
 * @author quchao
 */

public class RxUtils {

    /**
     * 统一线程处理
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
//
//    /**
//     * 统一返回结果处理
//     * @param <T> 指定的泛型类型
//     * @return ObservableTransformer
//     */
//    public static <T> ObservableTransformer<BaseCallModel<T>, T> handleResult() {
//        return httpResponseObservable ->
//                httpResponseObservable.flatMap((Function<BaseCallModel<T>, Observable<T>>) baseResponse -> {
//            if(baseResponse.getCode() == BaseCallModel.SUCCESS && baseResponse.getResult() != null) {
//                return createData(baseResponse.getResult());
//            } else {
//                return Observable.error(new ApiException(baseResponse.getMsg()));
//            }
//        });
//    }
//
//    /**
//     * 得到 Observable
//     * @param <T> 指定的泛型类型
//     * @return Observable
//     */
//    private static <T> Observable<T> createData(final T t) {
//        return Observable.create(emitter -> {
//            try {
//                emitter.onNext(t);
//                emitter.onComplete();
//            } catch (Exception e) {
//                emitter.onError(e);
//            }
//        });
//    }
}
