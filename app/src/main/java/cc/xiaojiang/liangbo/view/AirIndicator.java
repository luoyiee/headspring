package cc.xiaojiang.liangbo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;


public class AirIndicator extends View {
    private int mSize;
    private Paint mOuterPaint;
    private Paint mTextPaint;
    private RectF mOuterRectF;
    private Context mContext;
    private int mMax;
    private int mMin;
    private int mValue;

    public AirIndicator(Context context) {
        this(context, null);
    }

    public AirIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStrokeWidth(ScreenUtils.dip2px(mContext, 8));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, 10));
        mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSize = Math.min(getWidth(), getHeight()) - ScreenUtils.dip2px(mContext, 16);
        mOuterRectF = new RectF(-mSize / 2, -mSize / 2, mSize / 2, mSize / 2);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mOuterPaint.setColor(ContextCompat.getColor(mContext, R.color.text_light_white));
        canvas.drawArc(mOuterRectF, 130, 280, false, mOuterPaint);
        mOuterPaint.setColor(ContextCompat.getColor(mContext, R.color.white));

        canvas.drawArc(mOuterRectF, 130, 280f / (mMax - mMin) * mValue, false, mOuterPaint);

        canvas.drawText(String.valueOf(mMin), -mSize / 2f * (float) Math.sin(Math.PI / 180f * 40)
                , mSize / 2f *
                        (float) Math.cos(Math.PI / 180f * 40) + ScreenUtils.dip2px(mContext, 16),
                mTextPaint);
        canvas.drawText(String.valueOf(mMax), mSize / 2f * (float) Math.sin(Math.PI / 180f * 40),
                mSize / 2f *
                        (float) Math.cos(Math.PI / 180f * 40) + ScreenUtils.dip2px(mContext, 16),
                mTextPaint);

    }

    public void setMin(int min) {
        mMin = min;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setValue(int value) {
        if (mValue < mMin) {
            mValue = mMin;
        }
        if (mValue > mMax) {
            mValue = mMax;
        }
        mValue = value;
        invalidate();
    }


}
