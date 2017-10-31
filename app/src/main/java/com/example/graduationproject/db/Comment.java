package com.example.graduationproject.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class Comment extends BmobObject {

    private String content;
    private String username;
    private User user;
    private ShareVideo shareVideo;
    private ShareNews shareNews;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShareVideo getShareVideo() {
        return shareVideo;
    }

    public void setShareVideo(ShareVideo shareVideo) {
        this.shareVideo = shareVideo;
    }

    public ShareNews getShareNews() {
        return shareNews;
    }

    public void setShareNews(ShareNews shareNews) {
        this.shareNews = shareNews;
    }
}
