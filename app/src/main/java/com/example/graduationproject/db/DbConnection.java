package com.example.graduationproject.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class DbConnection extends DataSupport{
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
