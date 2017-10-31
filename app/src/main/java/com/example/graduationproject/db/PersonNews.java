package com.example.graduationproject.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class PersonNews extends BmobObject {

    private String username;
    private BmobFile imgfile;
    User author;

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public PersonNews(String username, BmobFile imgfile) {
        this.username = username;
        this.imgfile = imgfile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getImgfile() {
        return imgfile.getFileUrl();
    }

    public void setImgfile(BmobFile imgfile) {
        this.imgfile = imgfile;
    }
}

