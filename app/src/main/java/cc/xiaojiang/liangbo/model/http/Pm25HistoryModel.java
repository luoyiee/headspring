package cc.xiaojiang.liangbo.model.http;

import java.util.List;

public class Pm25HistoryModel {


    private List<OutdoorBean> outdoor;
    private List<IndoorBean> indoor;

    public List<OutdoorBean> getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(List<OutdoorBean> outdoor) {
        this.outdoor = outdoor;
    }

    public List<IndoorBean> getIndoor() {
        return indoor;
    }

    public void setIndoor(List<IndoorBean> indoor) {
        this.indoor = indoor;
    }

    public static class OutdoorBean {
        /**
         * time : 20180716
         * pm25 : 31
         */

        private int time;
        private float pm25;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public float getPm25() {
            return pm25;
        }

        public void setPm25(float pm25) {
            this.pm25 = pm25;
        }
    }

    public static class IndoorBean {
        /**
         * time : 20180716
         * pm25 : 31
         */

        private int time;
        private float pm25;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public float getPm25() {
            return pm25;
        }

        public void setPm25(float pm25) {
            this.pm25 = pm25;
        }
    }
}
