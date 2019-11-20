package cc.xiaojiang.liangbo.iotkit;


public class KzzDataModel {


    /**
     * msg_type : push
     * msg_id : 1684366951
     * params : {"onlineStatus":{"value":"online:1574219331"},"Switch":{"value":"1"},"WifiState":
     * {"value":"1"},"PM205":{"value":"35"},"ControlGear":{"value":"4"},"ControlMode":{"value":"0
     * "},"UseTime":{"value":"1252"},"Tempture":{"value":"21"},"Humidity":{"value":"47"}
     * ,"ShutdownRemainingTime":{"value":"237"},"Battery":{"value":"0"}}
     */

    private String msg_type;
    private String msg_id;
    private ParamsBean params;

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

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * onlineStatus : {"value":"online:1574219331"}
         * Switch : {"value":"1"}
         * WifiState : {"value":"1"}
         * PM205 : {"value":"35"}
         * ControlGear : {"value":"4"}
         * ControlMode : {"value":"0"}
         * UseTime : {"value":"1252"}
         * Tempture : {"value":"21"}
         * Humidity : {"value":"47"}
         * ShutdownRemainingTime : {"value":"237"}
         * Battery : {"value":"0"}
         */

        private OnlineStatusBean onlineStatus;
        private SwitchBean Switch;
        private WifiStateBean WifiState;
        private PM205Bean PM205;
        private ControlGearBean ControlGear;
        private ControlModeBean ControlMode;
        private UseTimeBean UseTime;
        private TemptureBean Tempture;
        private HumidityBean Humidity;
        private ShutdownRemainingTimeBean ShutdownRemainingTime;
        private BatteryBean Battery;

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

        public WifiStateBean getWifiState() {
            return WifiState;
        }

        public void setWifiState(WifiStateBean WifiState) {
            this.WifiState = WifiState;
        }

        public PM205Bean getPM205() {
            return PM205;
        }

        public void setPM205(PM205Bean PM205) {
            this.PM205 = PM205;
        }

        public ControlGearBean getControlGear() {
            return ControlGear;
        }

        public void setControlGear(ControlGearBean ControlGear) {
            this.ControlGear = ControlGear;
        }

        public ControlModeBean getControlMode() {
            return ControlMode;
        }

        public void setControlMode(ControlModeBean ControlMode) {
            this.ControlMode = ControlMode;
        }

        public UseTimeBean getUseTime() {
            return UseTime;
        }

        public void setUseTime(UseTimeBean UseTime) {
            this.UseTime = UseTime;
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

        public ShutdownRemainingTimeBean getShutdownRemainingTime() {
            return ShutdownRemainingTime;
        }

        public void setShutdownRemainingTime(ShutdownRemainingTimeBean ShutdownRemainingTime) {
            this.ShutdownRemainingTime = ShutdownRemainingTime;
        }

        public BatteryBean getBattery() {
            return Battery;
        }

        public void setBattery(BatteryBean Battery) {
            this.Battery = Battery;
        }

        public static class OnlineStatusBean {
            /**
             * value : online:1574219331
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

        public static class WifiStateBean {
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
             * value : 35
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
             * value : 4
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
             * value : 1252
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
             * value : 21
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
             * value : 47
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class ShutdownRemainingTimeBean {
            /**
             * value : 237
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class BatteryBean {
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
