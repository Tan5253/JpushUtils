package com.tan.jpush;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jpushlibrary.JpushReceiver;
import com.example.jpushlibrary.JpushUtils;
import com.example.jpushlibrary.LiveDataBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("one", JpushUtils.getInstance(MainActivity.this).getRegistrationID()+"shebeiid");
        LiveDataBus.get().with(JpushReceiver.TAG_RECEIVE_NOTIFICATION,Bundle.class).observe(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle o) {
                Log.d("one", JpushUtils.getInstance(MainActivity.this).getRegistrationID()+"<>>>>>>>>>>"+o.getString("cn.jpush.android.APPKEY"));
            }
        });

        LiveDataBus.get().with(JpushReceiver.TAG_CLICK_NOTIFICATION,Intent.class).observe(this, new Observer<Intent>() {
            @Override
            public void onChanged(@Nullable Intent o) {
                Log.d("one","<>>>>>我点击了>>>>>");
            }
        });
    }
}
