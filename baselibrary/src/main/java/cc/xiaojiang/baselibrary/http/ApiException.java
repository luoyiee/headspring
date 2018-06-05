package cc.xiaojiang.baselibrary.http;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }
}
