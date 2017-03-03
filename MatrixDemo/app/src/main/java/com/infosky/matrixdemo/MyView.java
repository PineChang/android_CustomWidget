package com.infosky.matrixdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by PineChang on 2017/3/3.
 * 切记:matrix不仅可以控制图形,可以可控制View组件的平移,旋转,和缩放等
 */

public class MyView extends View{
    public MyView(Context context) {
        super(context);
    }
    //原始图片资源
    private Bitmap bitmap;
    private Matrix matrix  = new Matrix();

    //设置位图的宽和高
    private int width ,height;
    //设置倾斜度
    private float sx = 0.0f;
    //设置位图的缩放比例
    private float  scale = 1.0f;
    //判断是缩放还是旋转
    private boolean isScale = false;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void  initView(){
        bitmap = ((BitmapDrawable)getContext().getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        //使当前视图获得焦点,这样就可以操纵他了
        this.setFocusable(true);
    }

    //将bitmap画进去


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //重置清零Matrix
        matrix.reset();
        if (!isScale) {
            matrix.setSkew(sx, 0);
        } else {
            //在x,y方向上同时设置
            matrix.setScale(scale,scale);
        }
        //根据原始位图和Matrix重新创建图片
        //挖去整个bitmap位图并且加上matrix变形,创建了一个新的位图bitmap2;
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,10,width,height,matrix,true);
        //根据绘制新位图
        canvas.drawBitmap(bitmap2,40.0f,100.3f,null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                isScale = false;
                sx +=0.1;
                postInvalidate();
                break;
            case KeyEvent.KEYCODE_S:
                isScale = true;
                if(scale<2.0f) scale -=0.1;
                postInvalidate();
                break;

        }
        return super.onKeyDown(keyCode, event);
    }
}
