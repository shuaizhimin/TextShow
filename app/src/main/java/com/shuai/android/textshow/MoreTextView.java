package com.shuai.android.textshow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * 作者: shuaizhimin
 * 描述:
 * 日期: 2017-11-08
 * 时间: 20:19
 * 版本:
 */
public class MoreTextView extends View {
    private String mText;                                 //要展示的文字
    private TextPaint mPaint = new TextPaint();
    private float mLineHeight;                              //行高
    private int mWidth;                                   //宽度
    private float mHeight;                                //高度
    private VelocityTracker mVelocityTracker;             //手势追踪
    private Scroller mScroller;
    private int mTouchSlop;                               //移动最短距离
    private int mMaximumVelocity;                         //移动最大速度值
    private int mMinimumVelocity;                         //移动最小速度值

    private int mLineCount;                               //一行文字数
    private int mRowCount;                                //一屏列数

    private TextScrollListener mScrollListener;          //滑动监听

    public MoreTextView(Context context) {
        this(context, null);
    }

    public MoreTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setTextSize(46f);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        //文字展示高度
        mLineHeight = metrics.descent - metrics.ascent;
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
        Log.e("滑动", "mMinimumVelocity:" + mMinimumVelocity);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        StaticLayout layout = new StaticLayout(mText, mPaint, getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.save();
        layout.draw(canvas);
        canvas.restore();
    }

    float mLastMotionY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTracker(event);
        float y = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaY = (int) (mLastMotionY - y);
                mLastMotionY = y;
                if (getScrollY() > 0) {
                    scrollBy(0, deltaY);
                    mScrollListener.scorll(getScrollY(),getHeight());
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                Log.e("滑动", "initialVelocity:" + initialVelocity);
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                }
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;

        }
        return true;

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {//是否已经滚动完成
            scrollTo(0, mScroller.getCurrY());//获取当前值，startScroll（）初始化后，调用就能获取区间值
            postInvalidate();
        }
    }

    /**
     * 初始化VelocityTracker
     *
     * @param event
     */
    private void initVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放VelocityTracker
     */
    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void fling(int velocityY) {
        mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, getHeight());
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getMode(widthMeasureSpec);
        mHeight = MeasureSpec.getMode(heightMeasureSpec);
    }

    public void setText(String mText) {
        this.mText = mText;
        invalidate();
    }

    public int getRowCount() {
        return mRowCount;
    }


    public void setScrollListener(TextScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    public interface TextScrollListener {
        void scorll(float y,float height);
    }
}
