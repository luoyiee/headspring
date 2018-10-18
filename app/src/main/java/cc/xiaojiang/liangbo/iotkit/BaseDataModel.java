package cc.xiaojiang.liangbo.iotkit;

public class BaseDataModel {


    /**
     * params : {"onlineStatus":{"value":"online:1532395586"},"Switch":{"value":"1"},
     * "PM205":{"value":"15"}}
     */

    private ParamsBean params;

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * onlineStatus : {"value":"online:1532395586"}
         * Switch : {"value":"1"}
         * PM205 : {"value":"15"}
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
             * value : online:1532395586
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
             * value : 15
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
