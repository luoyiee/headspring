package cc.xiaojiang.baselibrary.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jjf on 2017/3/29
 */

public class ConfigKeys {
    //先定义 常量
    public static final int API_HOST = 0;
    public static final int APPLICATION_CONTEXT = 1;
    public static final int CONFIG_READY = 2;
    public static final int INTERCEPTOR = 3;
    public static final int HANDLER = 4;
    @IntDef({API_HOST, APPLICATION_CONTEXT,CONFIG_READY,INTERCEPTOR,HANDLER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}

}