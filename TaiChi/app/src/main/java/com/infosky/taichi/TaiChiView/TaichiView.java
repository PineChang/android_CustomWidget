package com.infosky.taichi.TaiChiView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by PineChang on 2017/2/27.
 *
 * 自定义太极图;
 * 1. 分别绘制左黑右白两个半圆， 半径为r
 2. 再绘制上黑下白两个较小的圆， 半径为1/4 r
 3. 最后绘制上白下黑两个最小的圆， 半径为 1/16 r

 */

public class TaichiView extends View {
    private Paint mPaint;
    private int   mDegrees;
    public TaichiView(Context context) {
        super(context);
    }

    public TaichiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaichiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //重写view的onDraw方法;
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //拿到这个View的宽和高中的最小值
        int w = Math.min(getWidth(),getHeight());
        //1.先在(w/2,w/2)圆心处rotate一定的角度
        canvas.rotate(mDegrees,w/2,w/2);

        //2,初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        //2.1线在画布上画一个黑圆;
        canvas.drawCircle(w/2,w/2,w/2+5,mPaint);

        //在左边画一个黑半圆
        canvas.drawArc(new RectF(0,0,w,w),90,180,true,mPaint);

        //右边画一个白半圆
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(0,0,w,w),270,180,true,mPaint);
        //在上面画一个中黑圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(w/2,w/4,w/4,mPaint);
        //在下面画一个中白圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(w/2,w/4*3,w/4,mPaint);
        //在上面画一个小白圆
        canvas.drawCircle(w/2,w/4,w/16,mPaint);
        //在下面画一个小黑圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(w/2,w/4*3,w/16,mPaint);
    }

    public  void setDegrees(int degrees){
        this.mDegrees = degrees;
        //每次设置值的时候就就让UI从新更新一下,也就是在onDraw方法,从新画一下;
        invalidate();
    }

}
