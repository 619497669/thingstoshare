package com.example.graduationproject.ViewUi;

import android.app.Fragment;

import cn.jzvd.JZMediaManager;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class VideoFragmentItem extends Fragment {

    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            // load data here：fragment可见时执行加载数据或者进度条等
        }else {
            // fragment is no longer visible：不可见时不执行操作
            JZMediaManager.instance().mediaPlayer.pause();
        }
    }
}
