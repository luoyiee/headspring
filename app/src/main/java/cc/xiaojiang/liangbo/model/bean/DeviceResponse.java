package cc.xiaojiang.liangbo.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class DeviceResponse {
    /**
     * code : 1000
     * msg : ok
     * data : [{"productIcon":"http://s.y.xiaojiang.cc/2aff48b15cbdbd7358e2df2622ba0b60",
     * "isAdmin":"Y","productKey":"52fcf8178313732334104b7b714b67c8",
     * "deviceId":"00FBuyxQykI3mDWqsdYz","productName":"测试authＵＳＥＲ","deviceNickname":null}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * productIcon : http://s.y.xiaojiang.cc/2aff48b15cbdbd7358e2df2622ba0b60
         * isAdmin : Y
         * productKey : 52fcf8178313732334104b7b714b67c8
         * deviceId : 00FBuyxQykI3mDWqsdYz
         * productName : 测试authＵＳＥＲ
         * deviceNickname : null
         */

        private String productIcon;
        private String isAdmin;
        private String productKey;
        private String deviceId;
        private String productName;
        private String deviceNickname;

        public String getProductIcon() {
            return productIcon;
        }

        public void setProductIcon(String productIcon) {
            this.productIcon = productIcon;
        }

        public String getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDeviceNickname() {
            return deviceNickname;
        }

        public void setDeviceNickname(String deviceNickname) {
            this.deviceNickname = deviceNickname;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.productIcon);
            dest.writeString(this.isAdmin);
            dest.writeString(this.productKey);
            dest.writeString(this.deviceId);
            dest.writeString(this.productName);
            dest.writeString(this.deviceNickname);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.productIcon = in.readString();
            this.isAdmin = in.readString();
            this.productKey = in.readString();
            this.deviceId = in.readString();
            this.productName = in.readString();
            this.deviceNickname = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
