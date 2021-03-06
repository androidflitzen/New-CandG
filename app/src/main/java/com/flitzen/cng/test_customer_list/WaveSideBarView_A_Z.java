package com.flitzen.cng.test_customer_list;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.flitzen.cng.R;

import java.util.Arrays;
import java.util.List;

public class WaveSideBarView_A_Z extends View {

    private static final String TAG = "WaveSlideBarView";

    private static final double ANGLE = Math.PI * 45 / 180;
    private static final double ANGLE_R = Math.PI * 90 / 180;
    private OnTouchLetterChangeListener listener;

    private   List<String> mLetters;

    private int mChoose = -1;

    private int oldChoose ;

    private int newChoose ;

    private Paint mLettersPaint = new Paint();

    private Paint mTextPaint = new Paint();
    private Paint mWavePaint = new Paint();

    private float mTextSize;
    private float mLargeTextSize;
    private int mTextColor;
    private int mWaveColor;
    private int mTextColorChoose;
    private int mWidth;
    private int mHeight;
    private int mItemHeight;
    private int mPadding;

    private Path mWavePath = new Path();

    private Path mBallPath = new Path();

    private int mCenterY;

    private int mRadius;

    private int mBallRadius;
    ValueAnimator mRatioAnimator;

    private float mRatio;

    private float mPosX, mPosY;

    private float mBallCentreX;

    public WaveSideBarView_A_Z(Context context) {
        this(context, null);
    }

    public WaveSideBarView_A_Z(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSideBarView_A_Z(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.waveSideBarLetters));

        mTextColor = Color.parseColor("#969696");
        mWaveColor = Color.parseColor("#c1272d");
        mTextColorChoose = context.getResources().getColor(android.R.color.white);
        mTextSize = context.getResources().getDimensionPixelSize(R  .dimen.textSize_sidebar);
        mLargeTextSize = context.getResources().getDimensionPixelSize(R.dimen.large_textSize_sidebar);
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.textSize_sidebar_padding);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaveSideBarView);
            mTextColor = a.getColor(R.styleable.WaveSideBarView_sidebarTextColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.WaveSideBarView_sidebarChooseTextColor, mTextColorChoose);
            mTextSize = a.getFloat(R.styleable.WaveSideBarView_sidebarTextSize, mTextSize);
            mLargeTextSize = a.getFloat(R.styleable.WaveSideBarView_sidebarLargeTextSize, mLargeTextSize);
            mWaveColor = a.getColor(R.styleable.WaveSideBarView_sidebarBackgroundColor, mWaveColor);
            mRadius = a.getInt(R.styleable.WaveSideBarView_sidebarRadius, context.getResources().getDimensionPixelSize(R.dimen.radius_sidebar));
            mBallRadius = a.getInt(R.styleable.WaveSideBarView_sidebarBallRadius, context.getResources().getDimensionPixelSize(R.dimen.ball_radius_sidebar));
            a.recycle();
        }

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColorChoose);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mLargeTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final float x = event.getX();

        oldChoose = mChoose;
        newChoose = (int) (y / mHeight * mLetters.size());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x < mWidth - 2 * mRadius) {
                    return false;
                }
                mCenterY = (int) y;
                startAnimator(mRatio, 1.0f);

                break;
            case MotionEvent.ACTION_MOVE:

                mCenterY = (int) y;
                if (oldChoose != newChoose) {
                    if (newChoose >= 0 && newChoose < mLetters.size()) {
                        mChoose = newChoose;
                        if (listener != null) {
                            listener.onLetterChange(mLetters.get(newChoose));
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                startAnimator(mRatio, 0f);
                mChoose = -1;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mItemHeight = (mHeight - mPadding) / mLetters.size();
        mPosX = mWidth - 1.6f * mTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLetters(canvas);

        drawWavePath(canvas);

        drawBallPath(canvas);

        drawChooseText(canvas);

    }

    private void drawLetters(Canvas canvas) {

        RectF rectF = new RectF();
        rectF.left = mPosX - mTextSize;
        rectF.right = mPosX + mTextSize;
        rectF.top = mTextSize / 2;
        rectF.bottom = mHeight - mTextSize / 2;

        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.FILL);
        mLettersPaint.setColor(Color.parseColor("#F9F9F9"));
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mTextSize, mTextSize, mLettersPaint);

        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.STROKE);
        mLettersPaint.setColor(mTextColor);
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mTextSize, mTextSize, mLettersPaint);

        for (int i = 0; i < mLetters.size(); i++) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColor);
            mLettersPaint.setAntiAlias(true);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mLettersPaint.getFontMetrics();
            float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);

            float posY = mItemHeight * i + baseline / 2 + mPadding;

            if (i == mChoose) {
                mPosY = posY;
            } else {
                canvas.drawText(mLetters.get(i), mPosX, posY, mLettersPaint);
            }
        }

    }

    private void drawChooseText(Canvas canvas) {
        if (mChoose != -1) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColorChoose);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mLetters.get(mChoose), mPosX, mPosY, mLettersPaint);

            if (mRatio >= 0.9f) {
                String target = mLetters.get(mChoose);
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
                float x = mBallCentreX;
                float y = mCenterY + baseline / 2;
                canvas.drawText(target, x, y, mTextPaint);
            }
        }
    }

    /**
     *
     *
     * @param canvas
     */
    private void drawWavePath(Canvas canvas) {
        mWavePath.reset();

        mWavePath.moveTo(mWidth, mCenterY - 3 * mRadius);

        int controlTopY = mCenterY - 2 * mRadius;


        int endTopX = (int) (mWidth - mRadius * Math.cos(ANGLE) * mRatio);
        int endTopY = (int) (controlTopY + mRadius * Math.sin(ANGLE));
        mWavePath.quadTo(mWidth, controlTopY, endTopX, endTopY);


        int controlCenterX = (int) (mWidth - 1.8f * mRadius * Math.sin(ANGLE_R) * mRatio);
        int controlCenterY = mCenterY;

        int controlBottomY = mCenterY + 2 * mRadius;
        int endBottomX = endTopX;
        int endBottomY = (int) (controlBottomY - mRadius * Math.cos(ANGLE));
        mWavePath.quadTo(controlCenterX, controlCenterY, endBottomX, endBottomY);

        mWavePath.quadTo(mWidth, controlBottomY, mWidth, controlBottomY + mRadius);

        mWavePath.close();
        canvas.drawPath(mWavePath, mWavePaint);
    }

    private void drawBallPath(Canvas canvas) {

        mBallCentreX = (mWidth + mBallRadius) - (2.0f * mRadius + 2.0f * mBallRadius) * mRatio;

        mBallPath.reset();
        mBallPath.addCircle(mBallCentreX, mCenterY, mBallRadius, Path.Direction.CW);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            mBallPath.op(mWavePath, Path.Op.DIFFERENCE);
        }

        mBallPath.close();
        canvas.drawPath(mBallPath, mWavePaint);

    }


    private void startAnimator(float... value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {

                mRatio = (float) value.getAnimatedValue();

                if (mRatio==1f&&oldChoose != newChoose){
                    if (newChoose >= 0 && newChoose < mLetters.size()) {
                        mChoose = newChoose;
                        if (listener != null) {
                            listener.onLetterChange(mLetters.get(newChoose));
                        }
                    }
                }
                invalidate();
            }
        });
        mRatioAnimator.start();
    }


    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener listener) {
        this.listener = listener;
    }

    public List<String> getLetters() {
        return mLetters;
    }

    public void setLetters(List<String> letters) {
        this.mLetters = letters;
        invalidate();
    }

    public interface OnTouchLetterChangeListener {
        void onLetterChange(String letter);
    }
}
