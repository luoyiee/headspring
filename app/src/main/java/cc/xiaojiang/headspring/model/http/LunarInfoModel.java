package cc.xiaojiang.headspring.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class LunarInfoModel {
    /**
     * lunarDayName : 初一
     * zodiac : 狗
     * lunarFestival :
     * ganzhiMonth : 己未
     * lunarMonthName : 六月
     * lunarMonth : 7
     * ganzhiDay : 丙午
     * solarTerm :
     * ganzhiYear : 戊戌
     * lunarLeapMonth :
     * day : 20180713
     * lunarDay : 13
     * lunarYear : 2018
     */
    private String lunarDayName;
    private String zodiac;
    private String lunarFestival;
    private String ganzhiMonth;
    private String lunarMonthName;
    private int lunarMonth;
    private String ganzhiDay;
    private String solarTerm;
    private String ganzhiYear;
    private String lunarLeapMonth;
    private int day;
    private int lunarDay;
    private int lunarYear;

    public String getLunarDayName() {
        return lunarDayName == null ? "" : lunarDayName;
    }

    public String getZodiac() {
        return zodiac == null ? "" : zodiac;
    }

    public String getLunarFestival() {
        return lunarFestival == null ? "" : lunarFestival;
    }

    public String getGanzhiMonth() {
        return ganzhiMonth == null ? "" : ganzhiMonth;
    }

    public String getLunarMonthName() {
        return lunarMonthName == null ? "" : lunarMonthName;
    }

    public int getLunarMonth() {
        return lunarMonth;
    }

    public String getGanzhiDay() {
        return ganzhiDay == null ? "" : ganzhiDay;
    }

    public String getSolarTerm() {
        return solarTerm == null ? "" : solarTerm;
    }

    public String getGanzhiYear() {
        return ganzhiYear == null ? "" : ganzhiYear;
    }

    public String getLunarLeapMonth() {
        return lunarLeapMonth == null ? "" : lunarLeapMonth;
    }

    public int getDay() {
        return day;
    }

    public int getLunarDay() {
        return lunarDay;
    }

    public int getLunarYear() {
        return lunarYear;
    }
}
