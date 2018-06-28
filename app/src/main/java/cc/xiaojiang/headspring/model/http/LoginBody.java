package cc.xiaojiang.headspring.model.http;

public class LoginBody {
    private long telphone;
    private int verifyCode;
    private String source;
    private String developerKey;
    private String developerSecret;
    private String name;

    public void setTelphone(long telphone) {
        this.telphone = telphone;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDeveloperKey(String developerKey) {
        this.developerKey = developerKey;
    }

    public void setDeveloperSecret(String developerSecret) {
        this.developerSecret = developerSecret;
    }

    public void setName(String name) {
        this.name = name;
    }
}
