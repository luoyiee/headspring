package cc.xiaojiang.liangbo.iotkit;

public class BaseDataModel {

    /**
     * msg_type : push
     * params : {"onlineStatus":{"value":"online:1532333492"},"Switch":{"value":"1"},
     * "PM205":{"value":"0"}}
     */

    private String msg_type;
    private ParamsBean params;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * onlineStatus : {"value":"online:1532333492"}
         * Switch : {"value":"1"}
         * PM205 : {"value":"0"}
         */

        private OnlineStatusBean onlineStatus;
        private SwitchBean Switch;
        private PM205Bean PM205;

        public OnlineStatusBean getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(OnlineStatusBean onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public SwitchBean getSwitch() {
            return Switch;
        }

        public void setSwitch(SwitchBean Switch) {
            this.Switch = Switch;
        }

        public PM205Bean getPM205() {
            return PM205;
        }

        public void setPM205(PM205Bean PM205) {
            this.PM205 = PM205;
        }

        public static class OnlineStatusBean {
            /**
             * value : online:1532333492
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class SwitchBean {
            /**
             * value : 1
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class PM205Bean {
            /**
             * value : 0
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
