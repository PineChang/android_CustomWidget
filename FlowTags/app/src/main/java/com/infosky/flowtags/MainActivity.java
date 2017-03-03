package com.infosky.flowtags;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.infosky.flowtags.view.FlowLayout;

public class MainActivity extends AppCompatActivity {
    FlowLayout mFlowlayout;

    String[] tagArray = {"sldkflklk","sjlkdljfklj;","jdlkjlfjljkldjfkldjflk","sjlkdljfklj;","sjlkdljfklj;","sjlkdljfklj;"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFlowlayout = (FlowLayout)findViewById(R.id.fl_layout);
        Button btn;
        for (int i = 0; i < tagArray.length; i++) {
            btn = new Button(this);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            //让这些添加进去的Button都是WrapContent,那么在测量阶段他的最大宽度也就是父容器的宽度
            lp.topMargin = 15;
            lp.leftMargin = 15;
            btn.setBackground(getResources().getDrawable(R.drawable.tv_bg));
            btn.setLayoutParams(lp);
            btn.setText(tagArray[i]);
            //每次添加子view的时候都会重新进行测量,布局,绘制;
            mFlowlayout.addView(btn);


            
        }
    }
}
