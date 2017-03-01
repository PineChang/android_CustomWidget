package com.infosky.satellitemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

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
                        itemAnim(pos);

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
        //菜单按钮的点击事件让卫星按钮本身成为监听者
        mMenuButton.setOnClickListener(this);

    }
    //当点击菜单按钮的时候传递进来的视图
    @Override
    public void onClick(View view) {
        //当点击菜单按钮的时候,就让自身转动一下
        rotateMenuButton(mMenuButton,360,500);
        //并且让布局好的菜单项们做一下动画,也就是点击第奇数次的时候,让这个MenuItem展开,
        //偶数的时候让MenuItem隐藏;
        toggleMenu(500);

    }
    //首先rotate一下Menubutton;
    private  void rotateMenuButton(View view,int angle,int duration){
        RotateAnimation rotAnim  = new RotateAnimation(0,angle,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotAnim.setDuration(duration);
        rotAnim.setFillAfter(true);//这个参数代表,当动画做完后,是否还让view保持原来的状态
        view.startAnimation(rotAnim);


    }

    //展开和隐藏子菜单
    private  void  toggleMenu(int duration){
        int count = getChildCount();
        for (int i = 1; i <count ; i++) {
            final View  child = getChildAt(i);


        }
    }
    //当点击某一个菜单项的时候,这个菜单项变大,其他菜单项,都变小
    private void  itemAnim(int position){
        View child;
        int count = getChildCount();
        for(int i= 1;i<count;i++){
            child = getChildAt(i);
            if(position==i){
                //独立的线程中做的
                scaleBigAnim(child);
            }else{
                //独立的线程中做的
                scaleSmallAnim(child);
            }
            //同时在做动画的的时候让做动画的菜单项变得不能点击
            //注意动画在执行的过程中也是在独立的线程中做的,所以这个方法,可以在未做完的时候得到调用
            setItemClickable(child,false);
        }
    }
    //点击的菜单项变大
    private void scaleBigAnim(View view){
        //0.5f,代表形变原点在正中央
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,3f,1.0f,3f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setDuration(800);
        set.setFillAfter(true);
        view.startAnimation(set);


    }
    //其他未点击的菜单项变小
    private void scaleSmallAnim(View view){
        //第一个变化属性
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,0f,1.0f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0f);

        AnimationSet  set  = new AnimationSet(true);
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setFillAfter(true);
        set.setDuration(500);
        view.startAnimation(set);


    }

    private void setItemClickable(View view,boolean flag){
        //让view不可点击
        view.setClickable(flag);
        //并且失去第一响应,其他的view就可以成为第一响应者了;
        view.setFocusable(flag);
    }
}
