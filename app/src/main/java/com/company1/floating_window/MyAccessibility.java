package com.company1.floating_window;


import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MyAccessibility extends AccessibleExtract {

    private static final String TAG = "MyAccessibility";

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "config success!");

    }

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }
}