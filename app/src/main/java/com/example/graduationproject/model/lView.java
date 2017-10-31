package com.example.graduationproject.model;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/9/21 0021.
 */


public class lView  {
    private int portrait;
    private String nickname;
    private String text;
    private BmobFile imageId;

    public int getPortrait() {
        return portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    public String getImageId() {
        return imageId.getFileUrl();
    }
}
