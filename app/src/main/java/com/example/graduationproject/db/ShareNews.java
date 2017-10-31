package com.example.graduationproject.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/10/7 0007.
 */

public class ShareNews extends BmobObject {

    private User author;
    private String username;
    private String ShareWord;
    private BmobFile ShareImg;

    /*public ShareNews(String username, String shareWord, BmobFile shareImg) {
        this.username = username;
        ShareWord = shareWord;
        ShareImg = shareImg;
    }
*/

    public void setAuthor(User author) {
        this.author = author;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setShareWord(String shareWord) {
        ShareWord = shareWord;
    }

    public void setShareImg(BmobFile shareImg) {
        ShareImg = shareImg;
    }

    public User getAuthor() {
        return author;
    }


    public String getUsername() {
        return username;
    }

    public String getShareWord() {
        return ShareWord;
    }

    public String  getShareImg() {
        return ShareImg.getFileUrl();
    }
}
