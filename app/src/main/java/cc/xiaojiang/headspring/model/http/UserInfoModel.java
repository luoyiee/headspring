package cc.xiaojiang.headspring.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-18
 * description:
 */
public class UserInfoModel {

    /**
     * telphone : 15912345678
     * imgUrl : http://xxxx.png
     * nickname : 路过的大神2
     * gender : M
     * birthday : 458971286000
     * area : 浙江省宁波市
     * addTime : 1531886712477
     * modifyTime : 1531886875218
     */
    private long telphone;
    private String imgUrl;
    private String nickname;
    private String gender;
    private Long birthday;
    private String area;
    private long addTime;
    private long modifyTime;

    public long getTelphone() {
        return telphone;
    }

    public String getImgUrl() {
        return imgUrl == null ? "" : imgUrl;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public Long getBirthday() {
        return birthday == null ? 0 : birthday;
    }

    public String getArea() {
        return area == null ? "" : area;
    }
}
