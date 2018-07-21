package cc.xiaojiang.liangbo.model.event;

import android.graphics.Bitmap;

/**
 * @author :jinjiafeng
 * date:  on 18-7-9
 * description:
 */
public class ShareBitmapEvent {
    private Bitmap mShareBitmap;

    public ShareBitmapEvent(Bitmap shareBitmap) {
        mShareBitmap = shareBitmap;
    }

    public Bitmap getShareBitmap() {
        return mShareBitmap;
    }
}
