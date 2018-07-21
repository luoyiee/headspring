package cc.xiaojiang.liangbo.http;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }
}
