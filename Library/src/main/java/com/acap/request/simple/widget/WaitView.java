package com.acap.request.simple.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import support.lfp.requestchain.R;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/15 18:14
 * </pre>
 */
public class WaitView extends View {

    long duration = 500; /*一次动画持续时间*/
    final Area mArea = new Area();
    final RectF mRectF = new RectF();
    final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Style mStyle;

    Drawable mD_Roatate;

    public WaitView(Context context) {
        super(context);
    }

    public WaitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mD_Roatate = getResources().getDrawable(R.drawable.ic_rotate);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int pl = getPaddingLeft(), pr = getPaddingRight(), pt = getPaddingTop(), pb = getPaddingBottom();
        int width = right - left, height = bottom - top;


        mArea.set(0, 0, width - (pl + pr), height - (pt + pb));
        mArea.offset(getPaddingLeft(), getPaddingTop());

        mRectF.set(0, 0, width, height);

        mD_Roatate.setBounds(mArea.mArea);

        mStyle = new Style(mRectF, mArea);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        /*整体背景*/
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mStyle.color_bg);
        canvas.drawRoundRect(mRectF, mStyle.bg_radius, mStyle.bg_radius, mPaint);

        /*动画*/
        float rate = (getDrawingTime() % duration) / (float) duration;

        canvas.save();
        canvas.rotate(-rate * 360, mArea.centerX(), mArea.centerY());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStyle.width);
        mPaint.setColor(mStyle.color_Center);
        mD_Roatate.draw(canvas);
        canvas.restore();

        canvas.restore();
        refresh();
    }


    private final void refresh() {
        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            postInvalidate();
        }
    }


    /*绘制范围*/
    private static final class Area {
        final Rect mArea = new Rect();


        public void set(int i, int i1, int i2, int i3) {
            mArea.set(i, i1, i2, i3);
        }

        public void offset(int paddingLeft, int paddingTop) {
            mArea.offset(paddingLeft, paddingTop);
        }

        public int left() {return mArea.left;}

        public int top() {return mArea.top;}

        public int right() {return mArea.right;}

        public int bottom() {return mArea.bottom;}

        public float getRadius() {
            return Math.min(mArea.width(), mArea.height()) / 2f;
        }

        public int centerX() {
            return mArea.centerX();
        }

        public int centerY() {
            return mArea.centerY();
        }
    }

    /*样式*/
    private static final class Style {
        float bg_radius; /*背景圆角*/
        int color_bg; /*背景颜色*/
        int color_Center; /*前景颜色*/
        float width; /*宽度*/

        /**
         * @param mRectF 整个View的范围
         * @param mArea  绘制区域范围
         */
        public Style(RectF mRectF, Area mArea) {
            bg_radius = Math.min(mRectF.width(), mRectF.height()) / 13f;
            color_bg = 0x80000000;
            color_Center = 0xffffffff;
            width = mArea.getRadius() * 0.2f;
        }


    }

}
