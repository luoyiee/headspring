package cc.xiaojiang.iotkit.bean;

public class DevelopTokenBean {


    /**
     * code : 200
     * msg : ok
     * data : {"access_token":"0a2e9f6566711eae191cdd1c55ecfa1f","expired_at":"2018-04-26 11:32:53"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * access_token : 0a2e9f6566711eae191cdd1c55ecfa1f
         * expired_at : 2018-04-26 11:32:53
         */

        private String access_token;
        private String expired_at;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(String expired_at) {
            this.expired_at = expired_at;
        }
    }
}
