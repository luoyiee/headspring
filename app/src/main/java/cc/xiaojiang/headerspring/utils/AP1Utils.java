package cc.xiaojiang.headerspring.utils;

import android.content.Context;

import cc.xiaojiang.headerspring.R;


/**
 * Created by zhu on 16-10-28.
 */

public class AP1Utils {
    public static String getRate(Context mContext, int value) {
        String rate = "-";
        if (value >= 0) {
            if (value <= 35) {
                rate = mContext.getString(R.string.air_purifier_rate_excellent);
            } else if (value <= 75) {
                rate = mContext.getString(R.string.air_purifier_rate_good);
            } else {
                rate = mContext.getString(R.string.air_purifier_rate_poor);
            }
        }
        return rate;
    }
}
