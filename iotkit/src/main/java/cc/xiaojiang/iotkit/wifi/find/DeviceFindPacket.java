package cc.xiaojiang.iotkit.wifi.find;//package cc.xiaojiang.icx.iotkit.device;

/**
 * Created by facexxyz on 2018/3/25.
 */

public class DeviceFindPacket {
    private String msg_type;
    private String msg_id;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    /**
     * data : {"product_key":"ss","app_port":"123"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * product_key : ss
         * app_port : 123
         */

        private String product_key;
        private String app_port;

        public String getProduct_key() {
            return product_key;
        }

        public void setProduct_key(String product_key) {
            this.product_key = product_key;
        }

        public String getApp_port() {
            return app_port;
        }

        public void setApp_port(String app_port) {
            this.app_port = app_port;
        }
    }
}
