package cc.xiaojiang.headspring.http;


import java.util.logging.Logger;

import cc.xiaojiang.headspring.http.model.BaseModel;

public class HttpResultFunc<T> implements io.reactivex.functions.Function<BaseModel<T>, T> {

    @Override
    public T apply(BaseModel<T> input) {
        if (!(BaseModel.SUCCESS == input.getCode())) {
            throw new ApiException(input.getMsg());
        } /*else {
            if (input.getData() == null) {
                return (T) new Object();
            }
        }*/
        return input.getData();
    }
}
