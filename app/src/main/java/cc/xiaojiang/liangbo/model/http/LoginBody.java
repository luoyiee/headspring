package cc.xiaojiang.liangbo.model.http;

public class LoginBody {
    private long telphone;
    private int verifyCode;

    public void setTelphone(long telphone) {
        this.telphone = telphone;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    public long getTelphone() {
        return telphone;
    }
}
