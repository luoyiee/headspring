package cc.xiaojiang.headspring.http;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }
}
