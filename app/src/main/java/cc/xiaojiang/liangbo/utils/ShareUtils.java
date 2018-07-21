package cc.xiaojiang.liangbo.utils;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class ShareUtils {

    public void shareWebPager(String platformName,String title,String titleUrl,PlatformActionListener listener){
        Platform platform = ShareSDK.getPlatform(platformName);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setTitleUrl(titleUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }

    public static void shareImage(String platformName, Bitmap bitmap,PlatformActionListener listener){
        Platform platform = ShareSDK.getPlatform(platformName);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        shareParams.setImageData(bitmap);
        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }

    /**
     *
     * @param platformName
     * @param imagePath 图片文件路径
     * @param listener
     */
    public static void shareImagePath(String platformName, String  imagePath,PlatformActionListener listener){
        Platform platform = ShareSDK.getPlatform(platformName);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        shareParams.setImagePath(imagePath);
        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }

    /**
     * bitmap保存为file
     */
    public static void bitmapToFile(String filePath, Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        }
    }

}
