package cc.xiaojiang.headspring.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.utils.ScreenUtils;


/**
 * Created by zhu on 16-10-25.
 */

public class AP1View43 extends View {

    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private int mWidth, mHight;
    private float mTouchX, mTouchY;
    private Paint mBackPaint, mForePaint, mThumbPaint, mTextPaint;
    private Context mContext;
    private float mInterval;
    private int mGear = 0;
    private boolean isSeekable = true;
    /**
     * gear count
     */
    private int mGearCount = 4;
    /**
     * background
     */
    private int mBackgroundColor = Color.argb(76, 255, 255, 255);
    private int mBackgroundSizeDp = 2;
    private int mBackgroundSizePx;
    private int mIntervalHeightDp = 5;
    private int mIntervalHeightPx;

    /**
     * foreground
     */
    private int mForegroundColor = Color.WHITE;
    private int mForegroundSizeDp = 2;
    private int mForegroundSizePx;
    private int mTextSizeDp = 14;
    private int mTextSizePx;
    private float mTextHeight;

    /**
     * thumb
     */
    private int mThumbSizeDp = 24;
    private int mThumbSizePx;
    private Bitmap mThumbBitmap;
    private RectF mThumbRectf;

    public AP1View43(Context context) {
        this(context, null);
    }

    public AP1View43(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initSize();
        initPaint();

    }

    public void setSeekable(boolean isSeekable) {
        this.isSeekable = isSeekable;
        invalidate();
    }

   public interface OnSeekBarChangeListener {
        void onStart(int gear);

        void onChange(int gear);

        void onStop(int gear);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mOnSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = mOnSeekBarChangeListener;
    }

    private void initPaint() {
        initBackPaint();
        initForePaint();
        initTextPaint();
        mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initTextPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.argb(200, 255, 255, 255));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSizePx);
    }

    private void initBackPaint() {
        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initForePaint() {
        mForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mHight = getHeight() - getPaddingTop() - getPaddingBottom();
        mInterval = 1f * mWidth / mGearCount;
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.translate(getPaddingLeft(), getPaddingTop() + mHight / 2);
        drawBackground(canvas);
        drawForeground(canvas);


    }

    private void initSize() {
        mBackgroundSizePx = ScreenUtils.dip2px(mContext, mBackgroundSizeDp);
        mForegroundSizePx = ScreenUtils.dip2px(mContext, mForegroundSizeDp);
        mThumbSizePx = ScreenUtils.dip2px(mContext, mThumbSizeDp);
        mIntervalHeightPx = ScreenUtils.dip2px(mContext, mIntervalHeightDp);
        mIntervalHeightPx = ScreenUtils.dip2px(mContext, mIntervalHeightDp);
        mTextSizePx = ScreenUtils.dip2px(mContext, mTextSizeDp);

    }

    private void drawBackground(Canvas canvas) {
        canvas.save();
        mBackPaint.setColor(mBackgroundColor);
        mBackPaint.setStrokeWidth(mBackgroundSizePx);
        canvas.drawLine(0, 0, mWidth, 0, mBackPaint);

        for (int i = 0; i < mGearCount; i++) {
            canvas.drawLine(mInterval / 2 + i * mInterval, -mBackgroundSizePx / 2,
                    mInterval / 2 + i * mInterval, -mIntervalHeightPx, mBackPaint);
            canvas.drawText(i + "", mInterval / 2 + i * mInterval, mTextHeight + mThumbSizePx /
                    2, mTextPaint);
        }
        canvas.restore();
    }

    private void drawForeground(Canvas canvas) {
        canvas.save();
        mForePaint.setColor(mForegroundColor);
        mForePaint.setStrokeWidth(mForegroundSizePx);
        canvas.drawLine(0, 0, mInterval / 2 + mGear * mInterval, 0, mForePaint);
        mThumbBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_air_purifier_gear_thumb);
        mThumbRectf = new RectF(mInterval / 2 + mGear * mInterval - mThumbSizePx / 2,
                -mThumbSizePx / 2,
                mInterval / 2 + mGear * mInterval + mThumbSizePx / 2, mThumbSizePx / 2);
        canvas.drawBitmap(mThumbBitmap, null, mThumbRectf, mThumbPaint);
        canvas.restore();

    }


    public int getGear() {
        return mGear;
    }

    public void setGear(int mGear) {
        if (mGear >= 0 && mGear <= 5) {
            this.mGear = mGear;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isSeekable) {
            mTouchX = event.getX();
            mTouchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onStart();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mTouchX >= getPaddingLeft() && mTouchX <= getPaddingLeft() + mWidth) {
                        onChange(mTouchX);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onStop();
                    break;

            }
        }
        return true;
    }

    private void onChange(float mTouchX) {
        mGear = Math.round((mTouchX - getPaddingLeft() - mInterval / 2) / mInterval);
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onChange(mGear);
        }
    }

    private void onStop() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStop(mGear);
        }
    }

    private void onStart() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStart(mGear);
        }
    }
}
