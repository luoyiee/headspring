package cc.xiaojiang.liangbo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Locale;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.AP1Utils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;


/**
 * Created by zhu on 16-10-24.
 */

public class AP1View2 extends View {
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private float mAngle = 0f;
    private float mPreAngle = 0f;
    /**
     * value text size
     */
    private Rect mValueBounds = new Rect();
    private float mValueTextHeight;
    private float mValueTextWidth;
    /**
     * type text size
     */
    private Rect mTypeBounds = new Rect();
    private float mTypeTextHeight;
    private float mTypeTextWidth;
    /**
     * rate text size
     */
    private Rect mRateBounds = new Rect();
    private float mRateTextHeight;
    private float mRateTextWidth;
    /**
     * 圆环半径
     */
    private int mAnnulusRadius;
    /**
     * 文字画笔
     */
    private int mTextColor = Color.WHITE;
    private int mTextTypeSize = 16;
    private int mTextValueSize = 48;
    private int mTextUnitSize = 12;
    private int mTextRateSize = 14;
    private Paint mTextPaint;
    /**
     * 圆环背景色
     */
    private int mBackAnnulusColor = Color.argb(51, 255, 255, 255);
    private int mBackColor = Color.argb(25, 255, 255, 255);
    private int mBackAnnulusSize = 1;
    private Paint mBackAnnulusPaint;
    /**
     * 圆环前景色
     */
    private int mForeAnnulusColor = Color.WHITE;
    private int mForeAnnulusSize = 2;
    private Paint mForeAnnulusPaint;
    /**
     * 圆环指示点颜色
     */
    private int mIndicatorColor = Color.WHITE;
    private int mIndicatorSize = 8;
    private Paint mIndicatorPaint;

    /**
     * text valueStr
     */
    private int value = 0;
    private String type = getContext().getString(R.string.air_purifier_view2_type);
    private String valueStr = "";
    private String unit = getContext().getString(R.string.air_purifier_view2_type_unit);
    private String rateStr = getContext().getString(R.string.air_purifier_view2_rate_string);
    private String rate = getContext().getString(R.string.air_purifier_offline);


    public AP1View2(Context context) {
        this(context, null);
    }

    public AP1View2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initForeAnnulusPaint();
        initBackAnnulusPaint();
        initTextPaint();
        initIndicatorPaint();
    }

    private void initIndicatorPaint() {
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(ScreenUtils.dip2px(mContext, mIndicatorSize));
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initTextPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
    }

    private void initBackAnnulusPaint() {
        mBackAnnulusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initForeAnnulusPaint() {
        mForeAnnulusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForeAnnulusPaint.setColor(mForeAnnulusColor);
        mForeAnnulusPaint.setStrokeWidth(ScreenUtils.dip2px(mContext, mForeAnnulusSize));
        mForeAnnulusPaint.setStyle(Paint.Style.STROKE);
        mForeAnnulusPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getAnnulusRadius();
        canvas.translate(getPaddingLeft() + mWidth / 2, getPaddingTop() + mHeight / 2);
        drawBackAnnulus(canvas);
        drawForeAnnulus(canvas);
        drawText(canvas);
    }


    /**
     * 画当前进度圆环
     */
    private void drawForeAnnulus(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90);
        mRectF = new RectF(-mAnnulusRadius, -mAnnulusRadius, mAnnulusRadius, mAnnulusRadius);
        canvas.drawArc(mRectF, 0, 360-mAngle, false, mForeAnnulusPaint);
        /**
         * draw circle indicator
         */
        canvas.rotate(360-mAngle);
        canvas.drawPoint(mAnnulusRadius, 0, mIndicatorPaint);
        canvas.restore();
    }


    private void drawText(Canvas canvas) {
        canvas.save();
        /**
         *  draw valueStr
         */
        valueStr = String.format(Locale.getDefault(), "%03d", value);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, mTextValueSize));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getTextBounds(valueStr, 0, valueStr.length(), mValueBounds);
        mValueTextWidth = mValueBounds.right - mValueBounds.left;
        mValueTextHeight = mValueBounds.bottom - mValueBounds.top;
        canvas.drawText(valueStr, 0, mValueTextHeight / 2, mTextPaint);
        /**
         *  draw unit
         */
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, mTextUnitSize));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(unit, mValueTextWidth / 2 + 10, mValueTextHeight / 2, mTextPaint);

        /**
         *  draw rate
         */
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, mTextRateSize));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getTextBounds(rateStr, 0, rateStr.length(), mRateBounds);
        mRateTextHeight = mRateBounds.bottom - mRateBounds.top;
        canvas.drawText(rateStr + rate, 0, mValueTextHeight / 2 + mRateTextHeight * 3, mTextPaint);
        /**
         *  draw type
         */
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, mTextTypeSize));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getTextBounds(type, 0, type.length(), mTypeBounds);
        mTypeTextHeight = mTypeBounds.bottom - mTypeBounds.top;
        canvas.drawText(type, 0, -mValueTextHeight / 2 - mTypeTextHeight * 2, mTextPaint);
        /**
         *  draw line
         */
        mTextPaint.setColor(mBackAnnulusColor);
        canvas.drawLine(-mAnnulusRadius * 2 / 3, mValueTextHeight / 2 + mRateTextHeight,
                mAnnulusRadius * 2 / 3, mValueTextHeight / 2 + mRateTextHeight, mBackAnnulusPaint);

        canvas.restore();

    }

    /**
     * 画背景圆环
     */
    private void drawBackAnnulus(Canvas canvas) {
        canvas.save();
        mBackAnnulusPaint.setColor(mBackColor);
        mBackAnnulusPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mAnnulusRadius, mBackAnnulusPaint);
        mBackAnnulusPaint.setColor(mBackAnnulusColor);
        mBackAnnulusPaint.setStyle(Paint.Style.STROKE);
        mBackAnnulusPaint.setStrokeWidth(ScreenUtils.dip2px(mContext, mBackAnnulusSize));
        canvas.drawCircle(0, 0, mAnnulusRadius, mBackAnnulusPaint);
        canvas.restore();
    }


    private void getAnnulusRadius() {
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        mAnnulusRadius = Math.min(mWidth, mHeight) / 2 - ScreenUtils.dip2px(mContext, mIndicatorSize);
    }

    private void startAnimation(float preAngle, float angle) {
        ValueAnimator animator = ValueAnimator.ofFloat(preAngle, angle);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    public void setValue(int value) {

        this.value = value;
        float angle = 0f;
        if (value >= 0) {
            if (value <= 75) {
                angle = value / 75f * 288;
                rate = "优";
            } else if (value <= 250) {
                angle = 288 + ((value - 75) / 175f * 36);
                rate = "良";
            } else if (value <= 2000) {
                angle = (288 + 36) + ((value - 250) / 1750f * 36);
                rate = "差";
            } else {
                angle = 360f;
                rate = "爆表";
            }
            rate = AP1Utils.getRate(mContext, value);
            startAnimation(mPreAngle, angle);
            mPreAngle = angle;
        }
    }

}
