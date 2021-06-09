package com.company1.floating_window;

import android.app.ActionBar;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;
import static android.widget.LinearLayout.VERTICAL;


public class MyService extends AccessibleExtract {
    public LinearLayout line;
    public WindowManager wm;
    Button button1;
    Button button2;
    WindowManager.LayoutParams params;
    public int onStartCommand(Intent intent, int flags, int startId) {

        List<Click> Clicks= DataSupport.findAll(Click.class);
        if(Clicks.size()>=1){
            sleep(3000);
            for(Click click:Clicks){
                Log.d("Mainactivity", String.valueOf(click.x));
                dispatchGestureView(click.x,click.y);
                sleep(3000);
            }
            Toast.makeText(this, "今日打卡已完成", Toast.LENGTH_LONG).show();
            return super.onStartCommand(intent, flags, startId);
        }




        Log.d("66666666", String.valueOf(Clicks.size()));
        line=new LinearLayout(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        line.setOrientation(VERTICAL);
        line.setLayoutParams(param);
        button1=new Button(this);
        button2=new Button(this);
        ActionBar.LayoutParams para1=new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1320);
        ActionBar.LayoutParams para2=new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button1.setLayoutParams(para1);
        button1.setTextSize(40);
        button2.setLayoutParams(para2);
        button2.setText("Stop");
        button1.setOnTouchListener(new FloatingOnTouchListener());
        button2.setOnTouchListener(new FloatingOnTouchListener());
        button2.setText("Stop");
        line.addView(button1);
        line.addView(button2);
        // 获取WindowManager
        // 创建布局参数
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        /** 设置参数 */
        params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        // 设置窗口的行为准则
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置透明度
        params.alpha = 0.7f;
        //设置内部视图对齐方式，这边位置为左边靠上
        params.gravity = Gravity.LEFT | Gravity.TOP;
        //窗口的左上角坐标
        params.x = 0;
        params.y = 0;
        //设置窗口的宽高,这里为自动
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 添加进WindowManager
        wm.addView(line, params);
        return super.onStartCommand(intent, flags, startId);
    }
    private class FloatingOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Button btn=(Button)view;
            if(btn.getText()=="Stop") {
                line.removeAllViews();
                wm.removeView(line);
            }
            else{
                switch (event.getAction()) {
                    /**
                     * 点击的开始位置
                     */
                    case MotionEvent.ACTION_DOWN:
                        wm.removeView(line);
                        line.removeAllViews();
                        wm.addView(line, params);
                        Click click = new Click();
                        click.time= Math.toIntExact(event.getEventTime());
                        click.x= (int) event.getX();
                        click.y= (int) event.getY();
                        click.save();
                        sleep(500);
                        dispatchGestureView((int) event.getX(),(int)event.getY());
                        btn.setText("点击了(" + event.getX() + "," + event.getY()+ ","+event.getEventTime()+")");
                        wm.removeView(line);
                        line.addView(button1);
                        line.addView(button2);
                        wm.addView(line, params);

                    default:
                        break;
                }
            }
            /**
             * 注意返回值
             * true：view继续响应Touch操作；
             * false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
             */
            return true;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

}