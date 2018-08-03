package cc.xiaojiang.liangbo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;


/**
 * Created by zhu on 16-11-23.
 */

public class ImageLoader {

    /**
     * 使用glide加载图片
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                //                .applyDefaultRequestOptions(new RequestOptions().centerCrop())
                .load(url)
                .into(imageView);
    }


    /**
     * 使用glide加载图片
     */
    public static void loadImageCenterCrop(Context context, File file, ImageView imageView) {
        GlideApp.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resId)
                .into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        GlideApp.with(context)
                .load(file)
                .into(imageView);
    }

    public static void loadImageNoMemory(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .skipMemoryCache(true)
                .centerCrop()
                .into(imageView);
    }
}
