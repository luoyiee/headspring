package cc.xiaojiang.liangbo.iotkit;

public class ProductInfoModel {

    /**
     * code : 1000
     * msg : ok
     * data : {"productId":134,"productKey":"bff503","productName":"空气净化器WIFI版",
     * "productSecret":"c6311dc155d63b60e480f8f16a397dd6","productAttr":"[{\"id\":13,
     * \"attr\":\"Switch\",\"attrName\":\"设备开关\",\"dataType\":\"enum\",\"rwFlag\":\"RW\",
     * \"description\":null,\"created_at\":\"2018-04-25 10:16:37\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"135\",\"propertyValues\":[{\"value\":\"0\",
     * \"name\":\"设备关\"},{\"value\":\"1\",\"name\":\"设备开\"}]},{\"id\":41,
     * \"attr\":\"ControlMode\",\"attrName\":\"控制模式\",\"dataType\":\"enum\",\"rwFlag\":\"RW\",
     * \"description\":\"空气净化器手、自动模式切换\",\"created_at\":\"2018-05-15 11:24:29\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"136\",
     * \"propertyValues\":[{\"value\":\"00\",\"name\":\"自动\"},{\"value\":\"01\",
     * \"name\":\"手动\"}]},{\"id\":42,\"attr\":\"ControlGear\",\"attrName\":\"控制档位\",
     * \"dataType\":\"enum\",\"rwFlag\":\"RW\",\"description\":\"0档为开机默认档案\",
     * \"created_at\":\"2018-05-15 11:33:57\",\"c_id\":35,\"deviceType\":\"Air cleaner\",
     * \"featureId\":\"137\",\"propertyValues\":[{\"value\":\"0\",\"name\":\"0档\"},
     * {\"value\":\"1\",\"name\":\"1档\"},{\"value\":\"2\",\"name\":\"2档\"},{\"value\":\"3\",
     * \"name\":\"3档\"},{\"value\":\"4\",\"name\":\"4档\"},{\"value\":\"5\",\"name\":\"5档\"}]},
     * {\"id\":43,\"attr\":\"TimingShutdown\",\"attrName\":\"定时关机\",\"dataType\":\"enum\",
     * \"rwFlag\":\"RW\",\"description\":null,\"created_at\":\"2018-05-15 11:37:42\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"141\",\"propertyValues\":[{\"value\":\"0\",
     * \"name\":\"关闭定时\"},{\"value\":\"1\",\"name\":\"1小时关闭\"},{\"value\":\"2\",
     * \"name\":\"2小时关闭\"},{\"value\":\"3\",\"name\":\"4小时关闭\"},{\"value\":\"4\",
     * \"name\":\"8小时关闭\"}]},{\"id\":44,\"attr\":\"WifiState\",\"attrName\":\"WIFI状态\",
     * \"dataType\":\"enum\",\"rwFlag\":\"R\",\"description\":null,\"created_at\":\"2018-05-15
     * 11:40:32\",\"c_id\":35,\"deviceType\":\"Air cleaner\",\"featureId\":\"138\",
     * \"propertyValues\":[{\"value\":\"0\",\"name\":\"断开\"},{\"value\":\"1\",\"name\":\"连接\"}]},
     * {\"id\":45,\"attr\":\"UseTime\",\"attrName\":\"滤网使用时间\",\"dataType\":\"uint32\",
     * \"rwFlag\":\"RW\",\"description\":null,\"created_at\":\"2018-05-15 11:47:06\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"139\",\"min\":\"0\",\"max\":\"80000\",
     * \"step\":\"1\",\"unitId\":\"19\",\"unit\":\"h\",\"unitName\":\"小时\"},{\"id\":46,
     * \"attr\":\"PM205\",\"attrName\":\"PM2.5\",\"dataType\":\"uint16\",\"rwFlag\":\"RW\",
     * \"description\":null,\"created_at\":\"2018-05-15 11:51:29\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"140\",\"min\":\"0\",\"max\":\"65535\",
     * \"step\":\"1\"},{\"id\":47,\"attr\":\"TVOC\",\"attrName\":\"TVOC\",
     * \"dataType\":\"uint16\",\"rwFlag\":\"RW\",\"description\":null,\"created_at\":\"2018-05-15
     * 11:52:49\",\"c_id\":35,\"deviceType\":\"Air cleaner\",\"featureId\":\"144\",\"min\":\"0\",
     * \"max\":\"65535\",\"step\":\"1\"},{\"id\":48,\"attr\":\"CO2\",\"attrName\":\"CO2浓度\",
     * \"dataType\":\"uint16\",\"rwFlag\":\"RW\",\"description\":null,\"created_at\":\"2018-05-15
     * 11:53:55\",\"c_id\":35,\"deviceType\":\"Air cleaner\",\"featureId\":\"143\",\"min\":\"0\",
     * \"max\":\"65535\",\"step\":\"4\"},{\"id\":49,\"attr\":\"Tempture\",\"attrName\":\"温度\",
     * \"dataType\":\"uint8\",\"rwFlag\":\"RW\",\"description\":null,\"created_at\":\"2018-05-15
     * 11:55:00\",\"c_id\":35,\"deviceType\":\"Air cleaner\",\"featureId\":\"145\",\"min\":\"0\",
     * \"max\":\"255\",\"step\":\"1\",\"unitId\":\"23\",\"unit\":\"℃\",\"unitName\":\"摄氏度\"},
     * {\"id\":72,\"attr\":\"Humidity\",\"attrName\":\"     湿度\",\"dataType\":\"uint8\",
     * \"rwFlag\":\"R\",\"description\":null,\"created_at\":\"2018-06-11 10:07:58\",\"c_id\":35,
     * \"deviceType\":\"Air cleaner\",\"featureId\":\"142\",\"min\":\"0\",\"max\":\"99\",
     * \"step\":\"1\"}]","productIcon":"http://static.platform.xjiangiot
     * .com/m_8fa61466b18a8b4d20e43c272ecc9940.png","configNetworkCharacter":"详见配网说明书",
     * "configNetworkImg":"http://static.platform.xjiangiot.com/o_1cbo59osi15b21c8h1eku1jak1ttma
     * .png","hasAdmin":0,"developerId":"b0ebb9d173444f71b76059a7614c6131","netType":"wifi",
     * "dataType":"binary","moduleVendor":"","moduleType":"","pushUrl":"",
     * "createTime":1528881255652,"updateTime":1530255519196}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * productId : 134
         * productKey : bff503
         * productName : 空气净化器WIFI版
         * productSecret : c6311dc155d63b60e480f8f16a397dd6
         * productAttr : [{"id":13,"attr":"Switch","attrName":"设备开关","dataType":"enum",
         * "rwFlag":"RW","description":null,"created_at":"2018-04-25 10:16:37","c_id":35,
         * "deviceType":"Air cleaner","featureId":"135","propertyValues":[{"value":"0",
         * "name":"设备关"},{"value":"1","name":"设备开"}]},{"id":41,"attr":"ControlMode",
         * "attrName":"控制模式","dataType":"enum","rwFlag":"RW","description":"空气净化器手、自动模式切换",
         * "created_at":"2018-05-15 11:24:29","c_id":35,"deviceType":"Air cleaner",
         * "featureId":"136","propertyValues":[{"value":"00","name":"自动"},{"value":"01",
         * "name":"手动"}]},{"id":42,"attr":"ControlGear","attrName":"控制档位","dataType":"enum",
         * "rwFlag":"RW","description":"0档为开机默认档案","created_at":"2018-05-15 11:33:57","c_id":35,
         * "deviceType":"Air cleaner","featureId":"137","propertyValues":[{"value":"0",
         * "name":"0档"},{"value":"1","name":"1档"},{"value":"2","name":"2档"},{"value":"3",
         * "name":"3档"},{"value":"4","name":"4档"},{"value":"5","name":"5档"}]},{"id":43,
         * "attr":"TimingShutdown","attrName":"定时关机","dataType":"enum","rwFlag":"RW",
         * "description":null,"created_at":"2018-05-15 11:37:42","c_id":35,"deviceType":"Air
         * cleaner","featureId":"141","propertyValues":[{"value":"0","name":"关闭定时"},{"value":"1",
         * "name":"1小时关闭"},{"value":"2","name":"2小时关闭"},{"value":"3","name":"4小时关闭"},
         * {"value":"4","name":"8小时关闭"}]},{"id":44,"attr":"WifiState","attrName":"WIFI状态",
         * "dataType":"enum","rwFlag":"R","description":null,"created_at":"2018-05-15 11:40:32",
         * "c_id":35,"deviceType":"Air cleaner","featureId":"138","propertyValues":[{"value":"0",
         * "name":"断开"},{"value":"1","name":"连接"}]},{"id":45,"attr":"UseTime",
         * "attrName":"滤网使用时间","dataType":"uint32","rwFlag":"RW","description":null,
         * "created_at":"2018-05-15 11:47:06","c_id":35,"deviceType":"Air cleaner",
         * "featureId":"139","min":"0","max":"80000","step":"1","unitId":"19","unit":"h",
         * "unitName":"小时"},{"id":46,"attr":"PM205","attrName":"PM2.5","dataType":"uint16",
         * "rwFlag":"RW","description":null,"created_at":"2018-05-15 11:51:29","c_id":35,
         * "deviceType":"Air cleaner","featureId":"140","min":"0","max":"65535","step":"1"},
         * {"id":47,"attr":"TVOC","attrName":"TVOC","dataType":"uint16","rwFlag":"RW",
         * "description":null,"created_at":"2018-05-15 11:52:49","c_id":35,"deviceType":"Air
         * cleaner","featureId":"144","min":"0","max":"65535","step":"1"},{"id":48,"attr":"CO2",
         * "attrName":"CO2浓度","dataType":"uint16","rwFlag":"RW","description":null,
         * "created_at":"2018-05-15 11:53:55","c_id":35,"deviceType":"Air cleaner",
         * "featureId":"143","min":"0","max":"65535","step":"4"},{"id":49,"attr":"Tempture",
         * "attrName":"温度","dataType":"uint8","rwFlag":"RW","description":null,
         * "created_at":"2018-05-15 11:55:00","c_id":35,"deviceType":"Air cleaner",
         * "featureId":"145","min":"0","max":"255","step":"1","unitId":"23","unit":"℃",
         * "unitName":"摄氏度"},{"id":72,"attr":"Humidity","attrName":"     湿度","dataType":"uint8",
         * "rwFlag":"R","description":null,"created_at":"2018-06-11 10:07:58","c_id":35,
         * "deviceType":"Air cleaner","featureId":"142","min":"0","max":"99","step":"1"}]
         * productIcon : http://static.platform.xjiangiot.com/m_8fa61466b18a8b4d20e43c272ecc9940.png
         * configNetworkCharacter : 详见配网说明书
         * configNetworkImg : http://static.platform.xjiangiot
         * .com/o_1cbo59osi15b21c8h1eku1jak1ttma.png
         * hasAdmin : 0
         * developerId : b0ebb9d173444f71b76059a7614c6131
         * netType : wifi
         * dataType : binary
         * moduleVendor :
         * moduleType :
         * pushUrl :
         * createTime : 1528881255652
         * updateTime : 1530255519196
         */

        private int productId;
        private String productKey;
        private String productName;
        private String productSecret;
        private String productAttr;
        private String productIcon;
        private String configNetworkCharacter;
        private String configNetworkImg;
        private int hasAdmin;
        private String developerId;
        private String netType;
        private String dataType;
        private String moduleVendor;
        private String moduleType;
        private String pushUrl;
        private long createTime;
        private long updateTime;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSecret() {
            return productSecret;
        }

        public void setProductSecret(String productSecret) {
            this.productSecret = productSecret;
        }

        public String getProductAttr() {
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
        }

        public String getProductIcon() {
            return productIcon;
        }

        public void setProductIcon(String productIcon) {
            this.productIcon = productIcon;
        }

        public String getConfigNetworkCharacter() {
            return configNetworkCharacter;
        }

        public void setConfigNetworkCharacter(String configNetworkCharacter) {
            this.configNetworkCharacter = configNetworkCharacter;
        }

        public String getConfigNetworkImg() {
            return configNetworkImg;
        }

        public void setConfigNetworkImg(String configNetworkImg) {
            this.configNetworkImg = configNetworkImg;
        }

        public int getHasAdmin() {
            return hasAdmin;
        }

        public void setHasAdmin(int hasAdmin) {
            this.hasAdmin = hasAdmin;
        }

        public String getDeveloperId() {
            return developerId;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }

        public String getNetType() {
            return netType;
        }

        public void setNetType(String netType) {
            this.netType = netType;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getModuleVendor() {
            return moduleVendor;
        }

        public void setModuleVendor(String moduleVendor) {
            this.moduleVendor = moduleVendor;
        }

        public String getModuleType() {
            return moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
