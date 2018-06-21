package cc.xiaojiang.headerspring.http;


import cc.xiaojiang.baselibrary.http.ApiException;
import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.iotkit.ble.BleProto;

public class HttpResultFunc<T> implements io.reactivex.functions.Function<BaseModel<T>, T> {

    @Override
    public T apply(BaseModel<T> input) {
        if (!(BaseModel.SUCCESS == input.getCode())) {
            throw new ApiException(input.getMsg());
        }
        return input.getData();
    }
}
