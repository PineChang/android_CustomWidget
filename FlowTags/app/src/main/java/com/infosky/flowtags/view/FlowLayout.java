package com.infosky.flowtags.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PineChang on 2017/3/2.
 */

public class FlowLayout extends ViewGroup {
    //用一个list盛放一行的view,
    //所有行的view
    private  List<List<View>> mAllViews;
    //每一行的高度
    private List<Integer> mLineHeight;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAllViews = new ArrayList<>();
        mLineHeight = new ArrayList<>();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //每次addView的时候都会onMeasure,onLayout,onDraw
    //以下载onMeasure中解决,父view是exact还是atmost
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //拿到父view的的测量标准
        int  sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int  modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int  sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int  modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0,height=0;//整体的宽和高
        int lineWidth = 0,lineHeight= 0;
        int  childCount = getChildCount();
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            //先用父的测量标准传递进child,约束一下child后此时child就有自己
            //初步的测量标准了
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //测量child后,就要拿到child的布局间隙参数
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            //由于在android中都是border-box,那么要算这个child的整个占地面积,就要加上margin了
            int childWidth = child.getMeasuredWidth() + lp.leftMargin+lp.rightMargin;
            int childHeight = child.getMeasuredHeight() +lp.topMargin+lp.bottomMargin;
            //如果当前已经用到的累加的行宽和要遍历的到的子view宽度之和大于总宽度减去父view左右内边距,
            //那么就换新行
            if(childWidth+lineWidth>sizeWidth-getPaddingLeft()-getPaddingRight()){
                width = Math.max(width,lineWidth)//比较n-1上次的lineWidth和n-2-0从中的最大值中取得最大值
                lineWidth = childWidth;//此时是新行的的lineWidth
                height +=lineHeight;//加上当前行高
                lineHeight = childHeight;
            }else{
                //如果是同一行,那么就不用进行更新lineHeight和lineWidth;
                lineWidth +=childWidth;
                lineHeight = childHeight;//如果是2倍的单位行高,就让当前行高为两倍的行高
            }
            //如果是最后一行,那么把n-1行的数据加上去,就是最终的数据了
            if(i==childCount-1){
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }
        }
        //根据子控件重新布局自己的dimension;看一看容器父view是什么模式,如果是exact,那么就用原来的宽和高,
        //如果不是比如是wrap_content,那么就用现在求得的宽和高
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY?sizeWidth:MeasureSpec.makeMeasureSpec(width,modeWidth),
                modeHeight == MeasureSpec.EXACTLY?sizeWidth:MeasureSpec.makeMeasureSpec(width,modeHeight)
        );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            mAllViews.clear();
            mLineHeight.clear();
            List<View> lineViews = new ArrayList<>();
            int  w = getWidth();//当前ViewGroup的宽
            int lineWidth = 0,lineHeight=0;
            int childCount = getChildCount();
            View child;
            int childWidth = 0,childHeight=0;
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                //拿到当前child的布局参数
                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();

                if(childWidth+lineWidth+lp.leftMargin+lp.rightMargin>w-getPaddingLeft()-getPaddingRight()){
                    //说明要换行
                    //把上一行的的行高加进去,以及把上一行的所有views加进去
                    mLineHeight.add(lineHeight);
                    mAllViews.add(lineViews);

                    lineWidth = 0;
                    lineHeight = childHeight
                }


            }

        }
    }
}
