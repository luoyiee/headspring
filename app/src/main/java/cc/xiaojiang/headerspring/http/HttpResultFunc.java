package cc.xiaojiang.headerspring.http;


import cc.xiaojiang.baselibrary.http.ApiException;

public class HttpResultFunc<T> implements io.reactivex.functions.Function<BaseModel<T>, T> {

    @Override
    public T apply(BaseModel<T> input) {
        if (!"1000".equals(input.getCode())) {
            throw new ApiException(input.getMsg());
        }
        return input.getResult();
    }
}
