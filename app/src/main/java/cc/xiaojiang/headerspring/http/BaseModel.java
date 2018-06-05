package cc.xiaojiang.headerspring.http;

public class BaseModel<T> {
    public static final int SUCCESS = 1000;

    private int code;
    private String msg;
    private T result;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getResult() {
        return result;
    }
}
