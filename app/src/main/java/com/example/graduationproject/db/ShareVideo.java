package com.example.graduationproject.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class ShareVideo extends BmobObject {
    private String username;
    private String ShareWord;
    private BmobFile shareVideo;




    User author;

    public ShareVideo(String username, String shareWord, BmobFile shareVideo) {
        this.username = username;
        ShareWord = shareWord;
        this.shareVideo = shareVideo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getUsername() {
        return username;
    }

    public String getShareWord() {
        return ShareWord;
    }

    public String  getShareVideo() {
        return shareVideo.getFileUrl();
    }
}
