package cc.xiaojiang.headspring.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getDay(int index, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, index);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static int getToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(simpleDateFormat.format(new Date()));
    }

    public static int getMonthDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static String getMonth(int index, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, index);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getWeek(int index, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, index);
        return simpleDateFormat.format(calendar.getTime());
    }
}
