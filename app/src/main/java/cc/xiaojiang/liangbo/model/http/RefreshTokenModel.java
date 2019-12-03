package cc.xiaojiang.liangbo.model.http;

public class RefreshTokenModel {

    /**
     * refreshTimeout : 9331200000000
     * accessToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
     * .eyJyYW5kb20iOiJFRDRJcXoiLCJ2ZXJpZnlDb2RlIjoiNTE3OSIsInRlbHBob25lIjoxNTczNjg4MjMyMywidHlwZSI6ImFjY2Vzc1Rva2VuIiwiZXhwIjoxNTc1OTU0MjI5fQ.q6xqomyyLC6TABm0RsgWcGmU3n-PoFK8SKv9cCuIx_E
     * accessTimeout : 2177280000000
     * refreshToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
     * .eyJyYW5kb20iOiJ1RURDbFAiLCJ2ZXJpZnlDb2RlIjoiNTE3OSIsInRlbHBob25lIjoxNTczNjg4MjMyMywidHlwZSI6InJlZnJlc2hUb2tlbiIsImV4cCI6MTU3Nzk0MTQyOX0.F9iJvV4cmYGJo9u8SqO4QADSjGlS7fXIas0YUE6QI5M
     */

    private long refreshTimeout;
    private String accessToken;
    private long accessTimeout;
    private String refreshToken;

    public long getRefreshTimeout() {
        return refreshTimeout;
    }

    public void setRefreshTimeout(long refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAccessTimeout() {
        return accessTimeout;
    }

    public void setAccessTimeout(long accessTimeout) {
        this.accessTimeout = accessTimeout;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
