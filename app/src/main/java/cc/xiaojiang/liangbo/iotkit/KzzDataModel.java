package cc.xiaojiang.liangbo.iotkit;

public class KzzDataModel {


    /**
     * msg_type : push
     * params : {"onlineStatus":{"value":"online:1532157395"},"Switch":{"value":"1"},
     * "ControlMode":{"value":"1"},"ControlGear":{"value":"3"},"TimingShutdown":{"value":"0"},
     * "UseTime":{"value":"62"},"PM205":{"value":"3"},"Tempture":{"value":"26"},
     * "Humidity":{"value":"54"},"ShutdownRemainingTime":{"value":"0"}}
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
         * onlineStatus : {"value":"online:1532157395"}
         * Switch : {"value":"1"}
         * ControlMode : {"value":"1"}
         * ControlGear : {"value":"3"}
         * UseTime : {"value":"62"}
         * PM205 : {"value":"3"}
         * Tempture : {"value":"26"}
         * Humidity : {"value":"54"}
         * ShutdownRemainingTime : {"value":"0"}
         */

        private OnlineStatusBean onlineStatus;
        private SwitchBean Switch;
        private ControlModeBean ControlMode;
        private ControlGearBean ControlGear;
        private UseTimeBean UseTime;
        private PM205Bean PM205;
        private TemptureBean Tempture;
        private HumidityBean Humidity;
        private ShutdownRemainingTimeBean ShutdownRemainingTime;

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

        public ShutdownRemainingTimeBean getShutdownRemainingTime() {
            return ShutdownRemainingTime;
        }

        public void setShutdownRemainingTime(ShutdownRemainingTimeBean ShutdownRemainingTime) {
            this.ShutdownRemainingTime = ShutdownRemainingTime;
        }

        public static class OnlineStatusBean {
            /**
             * value : online:1532157395
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
             * value : 3
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
             * value : 62
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
             * value : 3
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
             * value : 26
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
             * value : 54
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
