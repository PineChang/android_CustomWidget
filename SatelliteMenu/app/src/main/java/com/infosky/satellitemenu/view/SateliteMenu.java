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
    //几个重要的属性
    private Position mPosition;
    public  void setPosition(Position position){
        if(mPosition==position){
            return;
        }
        this.mPosition = position;
        //在每次变幻位置时先把每个子child的动画关闭了
        View child;
        for (int i=0;i<getChildCount();i++){
            child=getChildAt(i);
            child.clearAnimation();
        }
        //在每次变幻位置时就请求layout一下
       // invalidate();invalidate会触发测量,布局,绘制
        requestLayout();//requestLayout只会请求布局;比较优化
    }
    private int mRadius;
    private int mStatus;
//----------------------------------------------------
    //菜单按钮属性,菜单分为菜单按钮和菜单项两部分;
    private View mMenuButton;
    //点击某个菜单,要实现的监听接口,也就是谁想监听我点击菜单的某一项,那么必须要实现的标准;
    public interface OnMenuItemClickListener{
        void onItemClick(View view,int position);
    }
    //将实现menuitem监听的监听器传递进来;
    private OnMenuItemClickListener mMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener){
        this.mMenuItemClickListener = menuItemClickListener;
    }
//--------------------------------------------------------------------------------

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
        switch(position){
            case LEFT_TOP:
                mPosition = Position.POS_LEFT_TOP;
                break;
            case RIGHT_TOP:
                mPosition= Position.POS_RIGHT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition= Position.POS_LEFT_BOTTOM;
                break;
            case RIGHT_BOTTOM:
                mPosition=Position.POS_RIGHT_BOTTOM;
                break;
        }
        //2拿到mRadius值
        //定义半径的默认值.注意在自定义属性format为dimension的属性,都有一个默认值,在取得这个值的时候要设置这个默认值;
        float defRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,context.getResources().getDisplayMetrics());
        mRadius = (int)typedArray.getDimension(R.styleable.SateliteMenu_radius,defRadius);
        //3拿到初始化的状态值为关闭状态
        mStatus = STATUS_CLOSE;
    }
    //当点击这个Satellite的时候传递进来的视图
    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        //把父的测量属性传递给每一个子元素
        for(int i=0,count=getChildCount();i<count;i++){
            //在测量每一子元素的时候需要将父元素的测量属性传递进来
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //菜单分为菜单按钮和菜单项两部分;
        //先布局菜单按钮
        layoutMenuButton();
        //再布局多个菜单项
         /*
        分析：
            menuButton距离每个item为radius。
            到item作直线，其夹角，应为90度均分。90/(item-1)=每个夹角的度数。
            有角度，就能求出正弦值sina。
            根据正弦公式：sina=a/c，且已知c=radius，求出a边长，即x坐标。
            有角度，就能求出正弦值cosa。
            余弦公式：cosa=b/c,且已知radius(斜边)，求出b边长，即y坐标
         */
        //拿到每个菜单项之间的夹角
        int count  = getChildCount();
        double  angle  = 90.0f/(count - 2);//减去菜单按钮,再减去1,总共减去2
        //然后开始布局每一个菜单项
        View child;
        int miWidth,miHeight;
        //遍历每一项开始,计算布局参数
        for(int i = 1;i<count;i++){//从1开始,那么就掠过了菜单按钮
            //--------------------布局子控件---------------
            child = getChildAt(i);
            child.setVisibility(View.GONE);
            miWidth = child.getMeasuredWidth();
            miHeight = child.getMeasuredHeight();
            double sinValue = 0,cosValue = 0;
            sinValue = Math.sin(Math.toRadians(angle*(i-1)));
            cosValue = Math.cos(Math.toRadians(angle*(i-1)));
            //左上
            int miLeft = (int)(mRadius*sinValue);
            int miTop  =  (int)(mRadius*cosValue);
            if(mPosition==Position.POS_RIGHT_BOTTOM ||　mPosition==Position.POS_RIGHT_TOP){
                miLeft  = getMeasuredWidth()-(int)(mRadius*sinValue)-miWidth;
            }
            if(mPosition==Position.POS_LEFT_BOTTOM || mPosition==Position.POS_RIGHT_BOTTOM){
                miTop =  getMeasuredHeight()-(int)(mRadius*sinValue)-miHeight;
            }
           child.layout(miLeft,miTop,miLeft+miWidth,miTop+miHeight);

            //------------------------给每一个子控件添加事件监听-----------------------------------------------
           final int pos = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMenuItemClickListener!=null){
                        //当点击的时候就把点击的的按钮是谁,是第几个按钮的信息传递出去
                        mMenuItemClickListener.onItemClick(view,pos);
                        //第二步,就执行动画
                        itemAinm(pos);

                    }
                    //当上面执行完后,就把状态改为 关闭状态
                    mStatus = STATUS_CLOSE;//关闭状态;
                }
            });

        }


    }
    //布局菜单按钮
    private  void layoutMenuButton(){
        //因为MenuButton是第一项
        mMenuButton = getChildAt(0);
        //初始默认值为以下
        int mbLeft = 0 ,mbTop = 0;
        int  mbWidth = mMenuButton.getMeasuredWidth();
        int  mbHeight = mMenuButton.getMeasuredHeight();
        //根据此时卫星菜单的状态来确定菜单按钮的位置
        switch(mPosition){
            case POS_LEFT_TOP:
                mbLeft = mbTop = 0;
                break;
            case POS_RIGHT_TOP:
                mbLeft = getMeasuredWidth()-mbWidth;
                mbTop = 0;
                break;
            case POS_LEFT_BOTTOM:
                mbLeft=0;
                mbTop=getMeasuredHeight()-mbHeight;
                break;
            case POS_RIGHT_BOTTOM:
                mbLeft=getMeasuredWidth()-mbWidth;
                mbTop = getMeasuredHeight()-mbHeight;
                break;
        }
        //子元素通过layout方法来布局自己
        mMenuButton.layout(mbLeft,mbTop,mbLeft+mbWidth,mbTop+mbHeight);

    }
}
