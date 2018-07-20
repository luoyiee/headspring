package cc.xiaojiang.headspring.model.http;

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
         * time : 2018072000
         * outPm25 : 37
         */

        private int time;
        private int outPm25;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getOutPm25() {
            return outPm25;
        }

        public void setOutPm25(int outPm25) {
            this.outPm25 = outPm25;
        }
    }

    public static class IndoorBean {
        /**
         * time : 2018072000
         * outPm25 : 37
         */

        private int time;
        private int outPm25;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getOutPm25() {
            return outPm25;
        }

        public void setOutPm25(int outPm25) {
            this.outPm25 = outPm25;
        }
    }
}
