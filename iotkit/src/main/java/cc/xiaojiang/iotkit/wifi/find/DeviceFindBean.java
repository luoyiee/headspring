package cc.xiaojiang.iotkit.wifi.find;

public class DeviceFindBean {

    /**
     * msg_type : add_device
     * msg_id : “1223634”
     * result_code : 0
     * data : {"device_id":"647383929222"}
     */

    private String msg_type;
    private String msg_id;
    private String result_code;
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

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * device_id : 647383929222
         */

        private String device_id;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }
    }
}
