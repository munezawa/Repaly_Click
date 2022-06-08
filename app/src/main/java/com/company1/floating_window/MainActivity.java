package com.company1.floating_window;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  List<Click> clickList = new ArrayList<>();
    public static final int CODE_WINDOW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 申请悬浮窗权限

        // 关闭当前activity,这样只显示悬浮窗
    }


    // 权限申请成功后的回调
    public void onClick(View v){

        Connector.getDatabase();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "请打开此应用悬浮窗权限", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), CODE_WINDOW);
            }
        }
        DataSupport.deleteAll(Click.class);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }
    private void initClicks() {
        clickList.clear();
        List<Click> Clicks= DataSupport.findAll(Click.class);
        for(Click click:Clicks){
            Log.d("Mainactivity", click.toString());
            clickList.add(click);
        }
    }
    public void clicksShow(View v){
        initClicks();
        ClickAdapter Adapter=new ClickAdapter(MainActivity.this, R.layout.click_laytou, clickList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(Adapter);
    }

    public void clicksSave(View view) {
        clickList.clear();
        List<Integer> x= new ArrayList<Integer>();
        List<Integer> y= new ArrayList<Integer>();
        List<Integer> time= new ArrayList<Integer>();
        List<Click> Clicks= DataSupport.findAll(Click.class);
        for(Click click:Clicks){
                x.add(click.x);
                y.add(click.y);
                time.add(click.time);
            clickList.add(click);
        }

        TextView text=(TextView)findViewById(R.id.editTextTextPersonName2);
        SharedPreferences.Editor editor=this.getSharedPreferences(String.valueOf(text.getText()),0).edit();
        Gson gson=new Gson();
        String json=gson.toJson(x);
        editor.putString("X_rate",json);
        json=gson.toJson(y);
        editor.putString("Y_rate",json);
        json=gson.toJson(time);
        editor.putString("Time_rate",json);
        editor.apply();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        DataSupport.deleteAll(Click.class);
    }
    private void readClicks(String str) {
        clickList.clear();
        SharedPreferences reader=getSharedPreferences(str,0);
        List<Integer> x= new ArrayList<Integer>();
        List<Integer> y= new ArrayList<Integer>();
        List<Integer> time= new ArrayList<Integer>();
        List<Click> Clicks =new ArrayList<>();
        String json=reader.getString("X_rate","");
        if(!json.equals("")){
            Gson gson=new Gson();
            x=gson.fromJson(json,new TypeToken<List<Integer>>(){}.getType());
        }
        json=reader.getString("Y_rate","");
        if(!json.equals("")){
            Gson gson=new Gson();
            y=gson.fromJson(json,new TypeToken<List<Integer>>(){}.getType());
        }
        json=reader.getString("Time_rate","");
        if(!json.equals("")){
            Gson gson=new Gson();
            time=gson.fromJson(json,new TypeToken<List<Integer>>(){}.getType());
        }
        for(int i=0;i<x.size();i++){
            Click click=new Click();
            click.x=x.get(i);
            click.y=y.get(i);
            click.time=time.get(i);
            clickList.add(click);
        }
        ClickAdapter Adapter=new ClickAdapter(MainActivity.this, R.layout.click_laytou, clickList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(Adapter);

    }
    public void showHistory(View view) {
        List<String> clickList = getFilesAllName();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,clickList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//Listview里的Item里的删除图标设置监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(MainActivity.this,clickList.get(position), Toast.LENGTH_SHORT).show();
                TextView text=(TextView)findViewById(R.id.editTextTextPersonName2);;
                text.setText(clickList.get(position).substring(0,clickList.get(position).length()-4));
                readClicks(text.getText().toString());
            }
        }
        );

    }
    public  List<String> getFilesAllName() {
        File file=new File("/data/data/com.company1.floating_window/shared_prefs");
        File[] files=file.listFiles();
        if (files == null){Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getName());
        }
        return s;
    }
    public void startAccessible(View view) {
        DataSupport.deleteAll(Click.class);
        for (Click click : clickList) {
            click.save();
        }
        Log.d("error", String.valueOf(clickList.size()));
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }
}
