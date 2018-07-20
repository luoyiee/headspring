package cc.xiaojiang.headspring.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static int getDay(int index) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, index);
        return Integer.parseInt(simpleDateFormat.format(calendar.getTime()));
    }

    public static int getMonth(int index) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, index);
        return Integer.parseInt(simpleDateFormat.format(calendar.getTime()));
    }

    public static int getWeek(int index) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, index);
        return Integer.parseInt(simpleDateFormat.format(calendar.getTime()));
    }
}
