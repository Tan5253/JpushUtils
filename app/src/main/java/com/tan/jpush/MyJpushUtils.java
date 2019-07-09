package com.tan.jpush;

import android.content.Context;
import android.text.TextUtils;

import com.example.jpushlibrary.JpushUtils;

/**
 * Created by like on 2017/2/8.
 */

public class MyJpushUtils {
    private Context mContext;
    private volatile static MyJpushUtils sInstance = null;
    private String curTag;

    public static MyJpushUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MyJpushUtils.class) {
                if (sInstance == null) {
                    sInstance = new MyJpushUtils(context);
                }
            }
        }
        return sInstance;
    }

    private MyJpushUtils(Context context) {
        mContext = context.getApplicationContext();
    }

//    /**
//     * 获取设备的注册id
//     */
//    public String getRegistrationID() {
//        return JPushInterface.getRegistrationID(mContext);
//    }

    /**
     * 极光推送设置别名
     *
     * @param userId
     */
    public void setAlias(String userId) {
        JpushUtils.getInstance(mContext).setAlias(userId);
    }

    public void setTags(String tags) {
        if (!TextUtils.isEmpty(tags)) {
            if (!tags.equals(curTag)) {
                curTag = tags;
                JpushUtils.getInstance(mContext).setTags(tags);
            }
        } else {
            clearTag();
        }
    }

//    public void setTagByHouseInfoList(List<? extends BaseListInfo> list) {
//        if (list != null) {
//            // 注册极光推送标签
//            StringBuilder sb = new StringBuilder();
//            for (BaseListInfo info : list) {
//                if (info != null) {
// //                   sb.append(((HouseInfo) info).getHouseId()).append(",");
//                }
//            }
//            if (sb.length() > 0) {
//                String tag = sb.substring(0, sb.length() - 1);
//                if (!tag.equals(curTag)) {
//                    curTag = tag;
//                    JpushUtils.getInstance(mContext).setTags(tag);
//                }
//            }
//        } else {
//            clearTag();
//        }
//    }

    /**
     * 取消极光推送标签
     */
    public void clearTag() {
        curTag = "";
        JpushUtils.getInstance(mContext).setTags(curTag);
    }

    /**
     * 取消极光推送别名
     */
    public void clearAlias() {
        JpushUtils.getInstance(mContext).setAlias("");
    }

}
