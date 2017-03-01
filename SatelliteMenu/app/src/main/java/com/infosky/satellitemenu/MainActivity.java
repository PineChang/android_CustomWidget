package com.infosky.satellitemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.infosky.satellitemenu.view.SateliteMenu;

public class MainActivity extends AppCompatActivity {
    private SateliteMenu mSatelliteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSatelliteMenu = (SateliteMenu)findViewById(R.id.sm_menu);

        mSatelliteMenu.setOnMenuItemClickListener(new SateliteMenu.OnMenuItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),view.getTag().toString(),Toast.LENGTH_SHORT);
            }
        });

    }
    //将菜单生成上去
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //当菜单点击的时候出触发的代码
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //当按钮点击的时候,改变按钮的状态
        int id = item.getItemId();
        if(id == R.id.action_leftTop){
            mSatelliteMenu.setPosition(SateliteMenu.Position.POS_LEFT_TOP);
            return true;
        }
        if(id == R.id.action_rightTop){
            mSatelliteMenu.setPosition(SateliteMenu.Position.POS_RIGHT_TOP);
            return true;
        }
        if(id== R.id.action_leftBottom){
            mSatelliteMenu.setPosition(SateliteMenu.Position.POS_LEFT_BOTTOM);
            return true;
        }
        if(id == R.id.action_rightBottom){
            mSatelliteMenu.setPosition(SateliteMenu.Position.POS_RIGHT_BOTTOM);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
