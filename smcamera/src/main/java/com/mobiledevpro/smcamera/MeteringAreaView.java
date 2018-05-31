package com.mobiledevpro.smcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * View to draw metering area
 * <p>
 * Created by Dmitriy V. Chernysh on 31.05.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public class MeteringAreaView extends View {

    private int mCenterX = 500, mCenterY = 500;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRectangle = new Rect();

    public MeteringAreaView(Context context) {
        super(context);
    }

    public MeteringAreaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeteringAreaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int rectWidth = 200, rectHeight = 200;
        int startX = mCenterX - (rectWidth / 2);
        int startY = mCenterY - (rectHeight / 2);

        Log.d(Constants.LOG_TAG_DEBUG, "MeteringAreaView.onDraw(): startX - "
                + startX
                + ", startY - " + startY
                + ", width - " + rectWidth
                + ", height - " + rectHeight
        );

        mRectangle.left = startX;
        mRectangle.top = startY;
        mRectangle.right = startX + rectWidth;
        mRectangle.bottom = startY + rectHeight;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3f);

        canvas.drawRect(mRectangle, mPaint);

        Log.d(Constants.LOG_TAG_DEBUG, "MeteringAreaView.onDraw(): " + canvas.getHeight());
    }

    public void setRectangle(int centerX, int centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
        invalidate();
    }
}
