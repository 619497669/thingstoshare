package com.example.graduationproject.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/5 0005.
 */

public class NickNews extends BmobObject{

    private String username;
    private String nickname;
    User author;

    public NickNews(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }
}
