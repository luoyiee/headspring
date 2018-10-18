package cc.xiaojiang.liangbo.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static int getHourIndex() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getWeekIndex() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        return (index - 3) >= 0 ? (index - 3) : 6;
    }

    public static int getMonthIndex() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH) - 2;
    }

    public static int getMonthDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static String getMonth(int index, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, index + 1);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getCurrentMonth() {
        return getMonth(1, "yyyyMM");
    }

    public static String getWeek(int index, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, index);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getFirstDayWeek() {
        return getWeek(2, "yyyyMMdd");
    }

    public static String getBirthday(Long birthday) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale
                .getDefault());
        return sdf.format(new Date(birthday));
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static String formatDate(int ymdh) throws ParseException {
        Date date = DateUtils.stringToDate(String.valueOf(ymdh), "yyyyMMddhh");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%02d/%02d", month, day);
    }
}
