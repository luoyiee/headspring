package cc.xiaojiang.headerspring.utils;

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
        Glide.with(context)
                //                .applyDefaultRequestOptions(new RequestOptions().centerCrop())
                .load(url)
                .into(imageView);
    }


    /**
     * 使用glide加载图片
     */
    public static void loadImageCenterCrop(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .into(imageView);
    }
}
