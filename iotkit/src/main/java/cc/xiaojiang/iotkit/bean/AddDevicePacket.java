package cc.xiaojiang.iotkit.bean;

public class AddDevicePacket {

    /**
     * msg_type : add_device
     * msg_id : “1223634”
     * data : {"\u201cproduct_key\u201d":"\u201dxq12\u201d",
     * "\u201capp_port\u201d":"\u201d9999\u201d"}
     */

    private String msg_type;
    private String msg_id;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
