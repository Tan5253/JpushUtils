#### 最新版本

模块|Jpush
---|---
最新版本|[![](https://jitpack.io/v/Tan5253/JpushUtils.svg)](https://jitpack.io/#Tan5253/JpushUtils)

## 功能介绍
1、极光推送工具类。基于'cn.jiguang.sdk:jpush:3.1.8'、'cn.jiguang.sdk:jcore:1.2.6'

## 使用方法：
1、引用

在Project的gradle中加入：
    ```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
在Module的gradle中加入：
```

    dependencies {
        implementation 'com.github.Tan5253:JpushUtils:版本号'
    }
```
2、在模块的build.gradle中添加
```
    defaultConfig {
        ...
        manifestPlaceholders = [
                JPUSH_PKGNAME: applicationId,
                JPUSH_APPKEY : "appKey", //JPush 上注册的包名对应的 Appkey.
                JPUSH_CHANNEL: "developer-default", //暂时填写默认值即可.
        ]
    }
``` 
3、初始化。
 ``` 
    JpushUtils.getInstance(this).init()
 ``` 
4、使用方法参考JpushUtils工具类。

5、接收点击通知的事件。
 ``` 
LiveDataBus.get().with(JpushReceiver.TAG_CLICK_NOTIFICATION,Intent.class).observe(this, new Observer<Intent>() {
            @Override
            public void onChanged(@Nullable Intent o) {
               Log.d("MainActivity", "onNotificationClicked " + o);
            }
 });
  ``` 
6、接收自定义消息。
 ``` 
  LiveDataBus.get().with(JpushReceiver.TAG_RECEIVE_NOTIFICATION,Bundle.class).observe(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle o) {
                // bundle中的内容是由后台决定的。
        Log.d("MainActivity", "onReceiveCustomMessage " + bundle);
            }
 });
  ``` 
  
