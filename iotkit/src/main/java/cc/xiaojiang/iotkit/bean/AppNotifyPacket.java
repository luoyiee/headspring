package cc.xiaojiang.iotkit.bean;

public class AppNotifyPacket {

    /**
     * msg_type : app_notify
     * msg_id : “1223634”
     * result_code : 0
     */

    private String msg_type;
    private String msg_id;
    private String result_code;

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
}
