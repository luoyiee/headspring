package cc.xiaojiang.liangbo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ShareUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.widget.ShareSelectDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.iv_share_content)
    ImageView mIvShareContent;
    @BindView(R.id.cl_share_root)
    ConstraintLayout mClShareRoot;
    private String mBitmapFilePath;
    private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ToastUtils.show("Share Complete");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            final String error = throwable.toString();
            runOnUiThread(() -> ToastUtils.show("Share Failure" + error));
        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        mBitmapFilePath = getFilesDir().getAbsolutePath() + File.separator + "share" + File
                .separator + "temp.png";
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(ShareBitmapEvent.class);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Bitmap bitmap = ScreenShotUtils.getViewBitmap(mClShareRoot);
            new ShareSelectDialog()
                    .setOnTimeSelectedListener(platformName -> {
                        if (platformName.contains("Q")) {
                            try {
                                ShareUtils.bitmapToFile(mBitmapFilePath,bitmap);
                                ShareUtils.shareImagePath(platformName, mBitmapFilePath, mPlatformActionListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            ShareUtils.shareImage(platformName, bitmap, mPlatformActionListener);
                        }
                    })
                    .show(getSupportFragmentManager(), "");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe(sticky = true)
    public void onMessageEvent(ShareBitmapEvent event) {
        Bitmap shareBitmap = event.getShareBitmap();
        mIvShareContent.setImageBitmap(shareBitmap);
    }
}
