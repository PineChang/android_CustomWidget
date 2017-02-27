package com.infosky.taichi;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.infosky.taichi.TaiChiView.TaichiView;

public class MainActivity extends AppCompatActivity {

    private TaichiView mTaichiView;
    //要让TaichiView转动起来,需要动态的改变TaichiView的转动角度
    private int mDegrees;
    //在主线程中创建Hadler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
                super.handleMessage(msg);
            mDegrees+=10;
            mTaichiView.setDegrees(mDegrees==360?mDegrees=0:mDegrees);
            //此时每50mshandle就会接收一次消息,实现死循环;这样mTaichiView可以无限循环;
            mHandler.sendEmptyMessageDelayed(0,50);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        //因为父容器就是FrameLayout,所以可以用普通的视图作为父视图的子元素
        mTaichiView = new TaichiView(this);
        //注意setContentView的宽和高不用指定,默认为contentView的宽和高,也就是相比父容器content宽和高
        //都是match_parent;
        setContentView(mTaichiView);
        //2s后向mHandler发送消息;启动TaichiView的旋转
        mHandler.sendEmptyMessageDelayed(0,2000);
    }
    protected void onDestroy(){
        super.onDestroy();
        //清空mHandler中的所有消息和回调
        mHandler.removeCallbacksAndMessages(null);
    }
}
