package cc.xiaojiang.liangbo.http.model;

public class BaseModel<T> {
    public static final int SUCCESS = 1000;

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
