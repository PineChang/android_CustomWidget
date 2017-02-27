package com.infosky.satellitemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.infosky.satellitemenu.R;

/**
 * Created by PineChang on 2017/2/27.
 */

public class SateliteMenu extends ViewGroup implements View.OnClickListener {
    //确定在那个位置的状态列表
    private final int LEFT_TOP = 1;
    private final int RIGHT_TOP=2;
    private final int LEFT_BOTTOM = 4;
    private final int RIGHT_BOTTOM = 8;
    //确定是展开还是关闭的状态列表
    private  final int STATUS_OPEN = 0;
    private  final int  STATUS_CLOSE=1;

    //确定在那个位置的枚举;
    public enum Position{
        POS_LEFT_TOP,POS_RIGHT_TOP,POS_LEFT_BOTTOM,POS_RIGHT_BOTTOM
    }

    private Position mPosition;
    private int mRadius;
    private int mStatus;

    //菜单button
    private View mMenuButton;
    //点击后触发的事件
    public interface
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public SateliteMenu(Context context) {
        super(context);
    }

    public SateliteMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SateliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       //1拿到position
       TypedArray typedArray =  context.obtainStyledAttributes(attrs, R.styleable.SateliteMenu);
        int position = typedArray.getInt(R.styleable.SateliteMenu_position,LEFT_TOP);
        //2拿到mRadius值
        //定义半径的默认值.注意在自定义属性format为dimension的属性,都有一个默认值,在取得这个值的时候要设置
        //这个默认值;
        float defRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,context.getResources().getDisplayMetrics());
        mRadius = (int)typedArray.getDimension(R.styleable.SateliteMenu_radius,defRadius);
        mstatus = STATUS_CLOSE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        //把父的测量结果传递给每一个子元素
        for(int i=0,count=getChildCount();i<count;i++){
            //测量一下每一个cell
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }
}
