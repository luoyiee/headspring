package cc.xiaojiang.headspring.utils;

import android.net.Uri;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TakePhotoUtils {
    public static Uri uri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/xjiang/" + System
                .currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.e("文件不存在!");
        }
        return size;
    }
}
