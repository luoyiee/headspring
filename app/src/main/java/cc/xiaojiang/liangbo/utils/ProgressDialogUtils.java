package cc.xiaojiang.liangbo.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {

    public static ProgressDialog show(Context context) {
        return ProgressDialogUtils.show(context, "请稍等");
    }

    public static ProgressDialog show(Context context, String msg) {
        return ProgressDialog.show(context, null, msg, true, false);
    }

    public static void dismiss(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
