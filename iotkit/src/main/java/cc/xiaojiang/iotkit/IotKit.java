package cc.xiaojiang.iotkit;

import android.app.Application;

import com.google.gson.JsonObject;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.account.IotKitAccountConfig;
import cc.xiaojiang.iotkit.ble.IotKitBleManager;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.util.IotDbUtils;


/**
 * Created by facexxyz on 18-3-26.
 */

public class IotKit {
    private static final String TAG = "IotKit";
    private static boolean init = false;


    // TODO: 2018/6/12 权限控制
    public static void init(Application context, IotKitAccountConfig iotKitAccountConfig) {
        if (context == null) {
            throw new NullPointerException("iotKit developToken context is null!");
        }
        if (iotKitAccountConfig == null) {
            throw new NullPointerException("iotKit IotKitAccountConfig is null!");
        }
        if (!init) {
            init = true;
            //log
            Logger.addLogAdapter(new AndroidLogAdapter());
            //db
            IotDbUtils.init(context, "xj_iotkit");
            //account
            IotKitAccountManager.getInstance().init(iotKitAccountConfig);
            //init
//            IotKitDeviceManager.getInstance().init(context);
            //connect
            IotKitConnectionManager.getInstance().init(context);

        } else {
            throw new RuntimeException("iotKit has already developToken!");
        }
    }

}
