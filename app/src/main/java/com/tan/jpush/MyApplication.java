package com.tan.jpush;

import android.app.Application;

import com.example.jpushlibrary.JpushUtils;

/**
 * Created by Administrator on 2019/7/8/008.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        JpushUtils.getInstance(this).init();
    }
}
