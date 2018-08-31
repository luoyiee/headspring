package cc.xiaojiang.liangbo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.orhanobut.logger.Logger;

import java.util.Locale;

import cc.xiaojiang.liangbo.BuildConfig;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ScreenUtils;

/**
 * 水波进度条
 * Created by littlejie on 2017/2/26.
 */

public class WaveProgressView extends View {

    public static final int DEFAULT_SIZE = 150;
    private int mBackColor = Color.argb(25, 255, 255, 255);
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#4BC28A");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    private static final String TAG = WaveProgressView.class.getSimpleName();
    private String unit = "%";
    //浅色波浪方向
    private static final int L2R = 0;
    private static final int R2L = 1;
    /**
     * value text size
     */
    private Rect mValueBounds = new Rect();

    private int mDefaultSize;
    //圆心
    private Point mCenterPoint;
    //半径
    private float mRadius;
    //圆的外接矩形
    private RectF mRectF;
    //深色波浪移动距离
    private float mDarkWaveOffset;
    //浅色波浪移动距离
    private float mLightWaveOffset;
    //浅色波浪方向
    private boolean isR2L;
    //是否锁定波浪不随进度移动
    private boolean lockWave;

    //是否开启抗锯齿
    private boolean antiAlias;
    //最大值
    private float mMaxValue = 100;
    //当前值
    private float mValue = 0;
    //当前进度
    private float mPercent;

    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize = 15;

    private Paint mPercentPaint;
    private float mValueSize = 15;
    private int mValueColor;

    //圆环宽度
    private float mCircleWidth = 15;
    //圆环
    private Paint mCirclePaint;
    //背景
    private Paint mBackPaint;
    //圆环颜色
    private int mCircleColor;
    //背景圆环颜色
    private int mBgCircleColor;

    //水波路径
    private Path mWaveLimitPath;
    private Path mWavePath;
    //水波高度
    private float mWaveHeight = 28;
    //水波数量
    private int mWaveNum;
    //深色水波
    private Paint mWavePaint;
    //深色水波颜色
    private int mDarkWaveColor;
    //浅色水波颜色
    private int mLightWaveColor;

    //深色水波贝塞尔曲线上的起始点、控制点
    private Point[] mDarkPoints;
    //浅色水波贝塞尔曲线上的起始点、控制点
    private Point[] mLightPoints;

    //贝塞尔曲线点的总个数
    private int mAllPointCount;
    private int mHalfPointCount;

    private ValueAnimator mProgressAnimator;
    private int mDarkWaveAnimTime = 2000;
    private ValueAnimator mDarkWaveAnimator;
    private int mLightWaveAnimTime = 2000;
    private ValueAnimator mLightWaveAnimator;
    //前一次绘制时的进度
    private float mPrePercent;
    //当前进度值
    private String mPercentValue;
    private Paint mTextUnitPaint;
    private int mValueTextWidth;
    private int mOffSet;

    public WaveProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = ScreenUtils.dip2px(context, DEFAULT_SIZE);
        mRectF = new RectF();
        mCenterPoint = new Point();

        initAttrs(context, attrs);
        initPaint();
        initPath();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressView);

        mOffSet = ScreenUtils.dip2px(context, 10);

        antiAlias = typedArray.getBoolean(R.styleable.WaveProgressView_antiAlias, true);
        mDarkWaveAnimTime = typedArray.getInt(R.styleable.WaveProgressView_darkWaveAnimTime,
                mDarkWaveAnimTime);
        mLightWaveAnimTime = typedArray.getInt(R.styleable.WaveProgressView_lightWaveAnimTime,
                mLightWaveAnimTime);
        mMaxValue = typedArray.getFloat(R.styleable.WaveProgressView_maxValue, mMaxValue);
        mValue = typedArray.getFloat(R.styleable.WaveProgressView_value, mValue);
        mValueSize = typedArray.getDimension(R.styleable.WaveProgressView_valueSize, mValueSize);
        mValueColor = typedArray.getColor(R.styleable.WaveProgressView_valueColor, Color.BLACK);

        mHint = typedArray.getString(R.styleable.WaveProgressView_hint);
        mHintColor = typedArray.getColor(R.styleable.WaveProgressView_hintColor, Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.WaveProgressView_hintSize, mHintSize);

        mCircleWidth = typedArray.getDimension(R.styleable.WaveProgressView_circleWidth,
                mCircleWidth);
        mCircleColor = typedArray.getColor(R.styleable.WaveProgressView_circleColor, Color.GREEN);
        mBgCircleColor = typedArray.getColor(R.styleable.WaveProgressView_bgCircleColor, Color
                .WHITE);

        mWaveHeight = typedArray.getDimension(R.styleable.WaveProgressView_waveHeight, mWaveHeight);
        mWaveNum = typedArray.getInt(R.styleable.WaveProgressView_waveNum, 1);
        mDarkWaveColor = typedArray.getColor(R.styleable.WaveProgressView_darkWaveColor,
                DEFAULT_BEHIND_WAVE_COLOR);
        mLightWaveColor = typedArray.getColor(R.styleable.WaveProgressView_lightWaveColor,
                DEFAULT_FRONT_WAVE_COLOR);

        isR2L = typedArray.getInt(R.styleable.WaveProgressView_lightWaveDirect, R2L) == R2L;
        lockWave = typedArray.getBoolean(R.styleable.WaveProgressView_lockWave, false);

        typedArray.recycle();
    }

    private void initPaint() {
        mHintPaint = new TextPaint();
        // 设置抗锯齿,会消耗较大资源，绘制图形速度会变慢。
        mHintPaint.setAntiAlias(antiAlias);
        // 设置绘制文字大小
        mHintPaint.setTextSize(mHintSize);
        // 设置画笔颜色
        mHintPaint.setColor(mHintColor);
        // 从中间向两边绘制，不需要再次计算文字
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(antiAlias);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint.setColor(mBackColor);

        mTextUnitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextUnitPaint.setColor(Color.WHITE);
        mTextUnitPaint.setTextSize(mValueSize / 2);

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(antiAlias);
        mWavePaint.setStyle(Paint.Style.FILL);

        mPercentPaint = new Paint();
        mPercentPaint.setTextAlign(Paint.Align.CENTER);
        mPercentPaint.setAntiAlias(antiAlias);
        mPercentPaint.setColor(mValueColor);
        mPercentPaint.setTextSize(mValueSize);
    }

    private void initPath() {
        mWaveLimitPath = new Path();
        mWavePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " +
                oldh);
        int minSize = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 *
                        (int) mCircleWidth,
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - 2 * (int)
                        mCircleWidth);
        mRadius = minSize / 2;
        mCenterPoint.x = getMeasuredWidth() / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - mCircleWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - mCircleWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + mCircleWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + mCircleWidth / 2;
        Log.d(TAG, "onSizeChanged: 控件大小 = " + "(" + getMeasuredWidth() + ", " + getMeasuredHeight
                () + ")"
                + ";圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + mRadius
                + ";圆的外接矩形 = " + mRectF.toString());
        initWavePoints();
        //开始动画
        setValue(mValue);
        startWaveAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mBackPaint);
        drawCircle(canvas);
        drawLightWave(canvas);
        drawDarkWave(canvas);
        drawProgress(canvas);
    }

    /**
     * 绘制圆环
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.save();
        canvas.rotate(270, mCenterPoint.x, mCenterPoint.y);
//        int currentAngle = (int) (360 * mPercent);
        //画背景圆环
        mCirclePaint.setColor(mBgCircleColor);
        canvas.drawArc(mRectF, 0, 360, false, mCirclePaint);
//        //画圆环
//        mCirclePaint.setColor(mCircleColor);
//        canvas.drawArc(mRectF, 0, currentAngle, false, mCirclePaint);
        canvas.restore();
    }

    /**
     * 绘制浅色波浪(贝塞尔曲线)
     *
     * @param canvas
     */
    private void drawLightWave(Canvas canvas) {
        mWavePaint.setColor(mLightWaveColor);
        //从右向左的水波位移应该被减去
        drawWave(canvas, mWavePaint, mLightPoints, isR2L ? -mLightWaveOffset : mLightWaveOffset);
    }

    /**
     * 绘制深色波浪(贝塞尔曲线)
     *
     * @param canvas
     */
    private void drawDarkWave(Canvas canvas) {
        mWavePaint.setColor(mDarkWaveColor);
        drawWave(canvas, mWavePaint, mDarkPoints, mDarkWaveOffset);
    }

    private void drawProgress(Canvas canvas) {
        float y = mCenterPoint.y - (mPercentPaint.descent() + mPercentPaint.ascent()) / 2;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "mPercent = " + mPercent + "; mPrePercent = " + mPrePercent);
        }
//        if (mPrePercent == 0.0f || Math.abs(mPercent - mPrePercent) >= 0.001f) {
//            mPercentValue = String.format(Locale.getDefault(), "%02d", (int) (mPercent * 100f));
//            mPrePercent = mPercent;
//        }
        mPercentValue = String.format(Locale.getDefault(), "%02d", (int) (mPercent * 100f));
        mPrePercent = mPercent;
        Logger.e("mPrePercent:" + mPercentValue);
        canvas.drawText(mPercentValue, mCenterPoint.x - mOffSet, y - mOffSet, mPercentPaint);

        mPercentPaint.getTextBounds(mPercentValue, 0, mPercentValue.length(), mValueBounds);
        mValueTextWidth = mValueBounds.right - mValueBounds.left;

        canvas.drawText(unit, mCenterPoint.x + mValueTextWidth / 2, y - mOffSet, mTextUnitPaint);

        if (mHint != null) {
            float hy = mCenterPoint.y + ScreenUtils.dip2px(getContext(), 24) - (mHintPaint
                    .descent() + mHintPaint.ascent()) / 2;
            canvas.drawText(mHint.toString(), mCenterPoint.x, hy, mHintPaint);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void drawWave(Canvas canvas, Paint paint, Point[] points, float waveOffset) {
        mWaveLimitPath.reset();
        mWavePath.reset();
        float height = lockWave ? 0 : mRadius - 2 * mRadius * mPercent;
        //moveTo和lineTo绘制出水波区域矩形
        mWavePath.moveTo(points[0].x + waveOffset, points[0].y + height);

        for (int i = 1; i < mAllPointCount; i += 2) {
            mWavePath.quadTo(points[i].x + waveOffset, points[i].y + height,
                    points[i + 1].x + waveOffset, points[i + 1].y + height);
        }
        //mWavePath.lineTo(points[mAllPointCount - 1].x, points[mAllPointCount - 1].y + height);
        //不管如何移动，波浪与圆路径的交集底部永远固定，否则会造成上移的时候底部为空的情况
        mWavePath.lineTo(points[mAllPointCount - 1].x, mCenterPoint.y + mRadius);
        mWavePath.lineTo(points[0].x, mCenterPoint.y + mRadius);
        mWavePath.close();
        mWaveLimitPath.addCircle(mCenterPoint.x, mCenterPoint.y, mRadius, Path.Direction.CW);
        //取该圆与波浪路径的交集，形成波浪在圆内的效果
        mWaveLimitPath.op(mWavePath, Path.Op.INTERSECT);
        canvas.drawPath(mWaveLimitPath, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopWaveAnimator();
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
            mProgressAnimator.removeAllUpdateListeners();
            mProgressAnimator = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWave(widthMeasureSpec, mDefaultSize),
                measureWave(heightMeasureSpec, mDefaultSize));
    }

    private void stopWaveAnimator() {
        if (mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()) {
            mDarkWaveAnimator.cancel();
            mDarkWaveAnimator.removeAllUpdateListeners();
            mDarkWaveAnimator = null;
        }
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            mLightWaveAnimator.cancel();
            mLightWaveAnimator.removeAllUpdateListeners();
            mLightWaveAnimator = null;
        }
    }

    private void initWavePoints() {
        //当前波浪宽度
        float waveWidth = (mRadius * 2) / mWaveNum;
        mAllPointCount = 8 * mWaveNum + 1;
        mHalfPointCount = mAllPointCount / 2;
        mDarkPoints = getPoint(false, waveWidth);
        mLightPoints = getPoint(isR2L, waveWidth);
    }

    /**
     * 从左往右或者从右往左获取贝塞尔点
     *
     * @return
     */
    private Point[] getPoint(boolean isR2L, float waveWidth) {
        Point[] points = new Point[mAllPointCount];
        //第1个点特殊处理，即数组的中点
        points[mHalfPointCount] = new Point((int) (mCenterPoint.x + (isR2L ? mRadius : -mRadius))
                , mCenterPoint.y);
        //屏幕内的贝塞尔曲线点
        for (int i = mHalfPointCount + 1; i < mAllPointCount; i += 4) {
            float width = points[mHalfPointCount].x + waveWidth * (i / 4 - mWaveNum);
            points[i] = new Point((int) (waveWidth / 4 + width), (int) (mCenterPoint.y -
                    mWaveHeight));
            points[i + 1] = new Point((int) (waveWidth / 2 + width), mCenterPoint.y);
            points[i + 2] = new Point((int) (waveWidth * 3 / 4 + width), (int) (mCenterPoint.y +
                    mWaveHeight));
            points[i + 3] = new Point((int) (waveWidth + width), mCenterPoint.y);
        }
        //屏幕外的贝塞尔曲线点
        for (int i = 0; i < mHalfPointCount; i++) {
            int reverse = mAllPointCount - i - 1;
            points[i] = new Point((isR2L ? 2 : 1) * points[mHalfPointCount].x - points[reverse].x,
                    points[mHalfPointCount].y * 2 - points[reverse].y);
        }
        //对从右向左的贝塞尔点数组反序，方便后续处理
        return isR2L ? reverse(points) : points;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    public float getValue() {
        return mValue;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(float value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        float start = mPercent;
        float end = 1.0f * value / mMaxValue;
        Log.d(TAG, "setValue, value = " + value + ";start = " + start + "; end = " + end);
        startAnimator(start, end, mDarkWaveAnimTime);
    }

    private void startAnimator(final float start, float end, long animTime) {
        Log.d(TAG, "startAnimator,value = " + mValue
                + ";start = " + start + ";end = " + end + ";time = " + animTime);
        //当start=0且end=0时，不需要启动动画
        if (start == 0 && end == 0) {
            return;
        }
        mProgressAnimator = ValueAnimator.ofFloat(start, end);
        mProgressAnimator.setDuration(animTime);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                if (mPercent == 0.0f || mPercent == 1.0f) {
                    stopWaveAnimator();
                } else {
                    startWaveAnimator();
                }
                mValue = mPercent * mMaxValue;
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onAnimationUpdate: percent = " + mPercent
                            + ";value = " + mValue);
                }
                invalidate();
            }
        });
        mProgressAnimator.start();
    }

    private void startWaveAnimator() {
        startLightWaveAnimator();
        startDarkWaveAnimator();
    }

    private void startLightWaveAnimator() {
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            return;
        }
        mLightWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mLightWaveAnimator.setDuration(mLightWaveAnimTime);
        mLightWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLightWaveAnimator.setInterpolator(new LinearInterpolator());
        mLightWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLightWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mLightWaveAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLightWaveOffset = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mLightWaveAnimator.start();
    }

    private void startDarkWaveAnimator() {
        if (mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()) {
            return;
        }
        mDarkWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mDarkWaveAnimator.setDuration(mDarkWaveAnimTime);
        mDarkWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDarkWaveAnimator.setInterpolator(new LinearInterpolator());
        mDarkWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDarkWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mDarkWaveAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mDarkWaveOffset = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDarkWaveAnimator.start();
    }

    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public <T> T[] reverse(T[] arrays) {
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    public int measureWave(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }
}