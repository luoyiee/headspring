package cc.xiaojiang.iotkit.bean;//package cc.xiaojiang.icx.iotkit.device;

/**
 * Created by facexxyz on 2018/3/25.
 */

public class ConfigWifiPacket {

    /**
     * msg_type : router _info
     * “product_key” : ”xq12”
     * msg_id : “1223634”
     * data : {"ssid":"test","password":"12345678"}
     */

    private String msg_type;
    private String product_key;
    private String msg_id;
    private DataBean data;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ssid : test
         * password : 12345678
         */

        private String ssid;
        private String password;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
