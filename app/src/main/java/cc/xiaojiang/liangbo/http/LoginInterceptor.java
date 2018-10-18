package cc.xiaojiang.liangbo.http;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.activity.LoginActivity;
import cc.xiaojiang.liangbo.utils.ToastUtils;

/**
 * @author :jinjiafeng
 * date:  on 18-7-27
 * description:
 */
public class LoginInterceptor {
    public static final String INVOKER = "invoker";

    // 这里获取登录状态
    public static boolean getLogin() {
        return IotKitAccountManager.getInstance().isLogin();
    }

    /**
     * @param ctx    当前activity的上下文
     * @param target 目标activity
     * @param bundle 需要传递的参数
     * @param intent
     */
    public static void interceptor(Context ctx, String target, Bundle bundle, Intent intent) {
        if (target != null && target.length() > 0) {
            LoginCarrier invoker = new LoginCarrier(target, bundle);
            if (getLogin()) {
                invoker.invoke(ctx);
            } else {
//                ToastUtils.show("请登录后重试");
                Logger.e("need login!");
                if (intent == null) {
                    intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra(INVOKER, invoker);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            }
        }
    }

    //使用方法  LoginInterceptor.interceptor(getActivity(), ".device.MainEditActivity",null);
    public static void interceptor(Context ctx, String target, Bundle bundle) {
        interceptor(ctx, target, bundle, null);
    }
}
