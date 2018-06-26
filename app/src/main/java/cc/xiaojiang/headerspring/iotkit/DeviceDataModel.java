package cc.xiaojiang.headerspring.iotkit;

public class DeviceDataModel {


    /**
     * msg_type : push
     * order_id :
     * params : {"onlineState":{"value":"online:1529982659"},"Switch":{"value":"1"},
     * "ControlMode":{"value":"1"},"ControlGear":{"value":"0"},"TimingShutdown":{"value":"0"},
     * "UseTime":{"value":"16"},"PM205":{"value":"0"},"Tempture":{"value":"27"},
     * "Humidity":{"value":"58"}}
     */

    private String msg_type;
    private String order_id;
    private ParamsBean params;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * onlineState : {"value":"online:1529982659"}
         * Switch : {"value":"1"}
         * ControlMode : {"value":"1"}
         * ControlGear : {"value":"0"}
         * TimingShutdown : {"value":"0"}
         * UseTime : {"value":"16"}
         * PM205 : {"value":"0"}
         * Tempture : {"value":"27"}
         * Humidity : {"value":"58"}
         */

        private OnlineStateBean onlineState;
        private SwitchBean Switch;
        private ControlModeBean ControlMode;
        private ControlGearBean ControlGear;
        private TimingShutdownBean TimingShutdown;
        private UseTimeBean UseTime;
        private PM205Bean PM205;
        private TemptureBean Tempture;
        private HumidityBean Humidity;

        public OnlineStateBean getOnlineState() {
            return onlineState;
        }

        public void setOnlineState(OnlineStateBean onlineState) {
            this.onlineState = onlineState;
        }

        public SwitchBean getSwitch() {
            return Switch;
        }

        public void setSwitch(SwitchBean Switch) {
            this.Switch = Switch;
        }

        public ControlModeBean getControlMode() {
            return ControlMode;
        }

        public void setControlMode(ControlModeBean ControlMode) {
            this.ControlMode = ControlMode;
        }

        public ControlGearBean getControlGear() {
            return ControlGear;
        }

        public void setControlGear(ControlGearBean ControlGear) {
            this.ControlGear = ControlGear;
        }

        public TimingShutdownBean getTimingShutdown() {
            return TimingShutdown;
        }

        public void setTimingShutdown(TimingShutdownBean TimingShutdown) {
            this.TimingShutdown = TimingShutdown;
        }

        public UseTimeBean getUseTime() {
            return UseTime;
        }

        public void setUseTime(UseTimeBean UseTime) {
            this.UseTime = UseTime;
        }

        public PM205Bean getPM205() {
            return PM205;
        }

        public void setPM205(PM205Bean PM205) {
            this.PM205 = PM205;
        }

        public TemptureBean getTempture() {
            return Tempture;
        }

        public void setTempture(TemptureBean Tempture) {
            this.Tempture = Tempture;
        }

        public HumidityBean getHumidity() {
            return Humidity;
        }

        public void setHumidity(HumidityBean Humidity) {
            this.Humidity = Humidity;
        }

        public static class OnlineStateBean {
            /**
             * value : online:1529982659
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

        public static class ControlModeBean {
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

        public static class ControlGearBean {
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

        public static class TimingShutdownBean {
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

        public static class UseTimeBean {
            /**
             * value : 16
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

        public static class TemptureBean {
            /**
             * value : 27
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class HumidityBean {
            /**
             * value : 58
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
