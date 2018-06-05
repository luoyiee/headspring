package cc.xiaojiang.baselibrary;

import android.content.Context;

public class BaseLibrary {
    public static Context context;

    public static void init(Context context) {
        BaseLibrary.context = context;
    }
}
