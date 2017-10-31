package com.example.graduationproject.Utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by Dovar on 2016/9/28 0028.
 */
public class DRAppUtil {
    private static DRAppUtil mDRAppUtil;

    private DRAppUtil() {
    }

    /**
     * 单例获取实例
     *
     * @return
     */
    public static DRAppUtil getInstance() {
        if (mDRAppUtil == null) {
            synchronized (DRAppUtil.class) {
                if (mDRAppUtil == null) {
                    mDRAppUtil = new DRAppUtil();
                }
            }
        }
        return mDRAppUtil;
    }

    /**
     * 进入沉浸模式
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void hideSystemUI(View view) {
        //setSystemUiVisibility()是耗时任务
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 退出沉浸模式
     */
    public void showSystemUI(View view) {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
