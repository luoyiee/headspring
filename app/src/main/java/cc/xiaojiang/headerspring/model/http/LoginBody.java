package cc.xiaojiang.headerspring.model.http;

public class LoginBody {
    private long telphone;
    private int verifyCode;
    private String source;
    private String developerId;
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

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
