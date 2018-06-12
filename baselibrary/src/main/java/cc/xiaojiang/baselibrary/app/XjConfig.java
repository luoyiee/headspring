package cc.xiaojiang.baselibrary.app;

import android.content.Context;
import android.os.Handler;

/**
 * Created by jjf on 2017/3/29
 */

public final class XjConfig {

    public static Configurator init(Context context) {
        Configurator.getInstance()
                .getLatteConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }

    private static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    private static <T> T getConfiguration(@ConfigKeys.Type int key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigKeys.HANDLER);
    }
}