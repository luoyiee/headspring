package cc.xiaojiang.headspring.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-6-14
 * description:
 */
public class LoginModel {

    /**
     * accessToken :
     * eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyYW5kb20iOiJwbml3aWEiLCJyb2xlIjowLCJ2ZXJpZnlDb2RlIjo5ODA5LCJ0ZWxwaG9uZSI6MTUwNTg0MzY5NjcsInR5cGUiOiJhY2Nlc3NUb2tlbiIsImV4cCI6MTUyOTU3MjA3N30.tex2iVeryLH1LYCDK15F1fh-qoC9djt816v8Wq4UHro
     * userId : a8bfc1eae87a431c9ecfe72d70c7bdd6
     * refreshToken :
     * eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyYW5kb20iOiJTdDVkU2ciLCJyb2xlIjowLCJ2ZXJpZnlDb2RlIjo5ODA5LCJ0ZWxwaG9uZSI6MTUwNTg0MzY5NjcsInR5cGUiOiJyZWZyZXNoVG9rZW4iLCJleHAiOjE1MzIxNTMyNzd9.iWyEk2VixynVV60K6ysNCZb3wo9uV2OnbVueQ5DzSXM
     */

    private String accessToken;
    private String userId;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
