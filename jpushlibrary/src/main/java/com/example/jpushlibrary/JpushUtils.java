package com.example.jpushlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class JpushUtils {
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private Context mContext;

    private volatile static JpushUtils sInstance = null;

    public static JpushUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (JpushUtils.class) {
                if (sInstance == null) {
                    sInstance = new JpushUtils(context);
                }
            }
        }
        return sInstance;
    }

    private JpushUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 初始化，会打印日志
     */
    public void debugAndInit() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mContext);
    }

    /**
     * 初始化，不打印日志
     */
    public void init() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(mContext);
    }

    /**
     * 获取设备的注册id
     */
    public String getRegistrationID() {
        return JPushInterface.getRegistrationID(mContext);
    }
    /**
     * 设置别名。这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     *
     * @param alias 每个 alias 命名长度限制为 40 字节；"" （空字符串）表示取消之前的设置。
     */
    public void setAlias(String alias) {
        if (!isValidTagAndAlias(alias)) {
            return;
        }
        Log.i("Jpush", "jpush register alias : " + alias);
        // 调用JPush API设置Alias
        mJPushHandler.sendMessage(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置标签。这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     *
     * @param tags 多个tag可以用","隔开，每个 tag 命名长度限制为 40 字节；"" （空字符串）表示取消之前的设置。
     */
    public void setTags(String tags) {
        // ","隔开的多个 转换成 Set
        String[] sArray = tags.split(",");
        Set<String> tagSet = new LinkedHashSet<>();
        for (String sTagItme : sArray) {
            // 检查 tags 的有效性
            if (!isValidTagAndAlias(sTagItme)) {
                return;
            }
            Log.i("Jpush", "jpush register tag : " + sTagItme);
            tagSet.add(sTagItme);
        }

        // 调用JPush API设置Tag
        mJPushHandler.sendMessage(mJPushHandler.obtainMessage(MSG_SET_TAGS, JPushInterface.filterValidTags(tagSet)));

    }

    private final Handler mJPushHandler = new Handler(new Handler.Callback() {
        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("Jpush", "Set alias in handler.");
                    JPushInterface.setAlias(mContext, (String) msg.obj, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("Jpush", "Set tags in handler.");
                    JPushInterface.setTags(mContext, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i("Jpush", "Unhandled msg - " + msg.what);
            }
            return true;
        }
    });

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tags success";
                    Log.i("Jpush", logs);
                    break;

                case 6002:
                    logs = "Failed to set tags due to timeout. Try again after 60s.";
                    Log.i("Jpush", logs);
                    if (isConnected(mContext)) {
                        mJPushHandler.sendMessageDelayed(mJPushHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i("Jpush", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Jpush", logs);
            }
        }

    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set alias success";
                    Log.i("Jpush", logs);
                    break;

                case 6002:
                    logs = "Failed to set alias due to timeout. Try again after 60s.";
                    Log.i("Jpush", logs);
                    if (isConnected(mContext)) {
                        mJPushHandler.sendMessageDelayed(mJPushHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i("Jpush", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Jpush", logs);
            }
        }

    };

    // 校验Tag Alias 只能是数字,英文字母和中文
    public boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 判断网络是否连接.
     * 需要权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /><br/>
     *
     * @param context
     * @return - true 网络连接 - false 网络连接异常
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isAvailable() && info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
