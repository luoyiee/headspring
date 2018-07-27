package cc.xiaojiang.liangbo.model.event;

/**
 * @author :jinjiafeng
 * date:  on 18-7-27
 * description:
 */
public class LoginEvent {
    public static final int CODE_LOGIN = 1000;
    public static final int CODE_LOGOUT = 1001;
    private int code ;

    public LoginEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
