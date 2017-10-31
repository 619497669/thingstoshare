package com.example.graduationproject.Dao;

import com.example.graduationproject.db.DbConnection;
import com.example.graduationproject.model.Person;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class PersonDao {
    private static List<DbConnection> personList=null;//保存联系人数据
    //获取所有联系人
    public static List<DbConnection> getPersonList(){
        if(null==personList){
            personList = new ArrayList<>();
            //把数据库中已有的数据拿出来
            List<DbConnection> dbConnections = DataSupport.findAll(DbConnection.class);
            for (DbConnection dbConnection : dbConnections){
                personList.add(dbConnection);
            }
            }

        return personList;
        }

    /**
     * 查找用户名是否存在
     * @param username
     * @return
     */
    public boolean checkUsername(String username){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            DbConnection usename =personList.get(i);
            if(username.equals(usename.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * 插入联系人
     * @return
     */
    public  boolean insert(Person person){
        if (checkUsername(person.getUsername())){
            return false;
        }try {
            DbConnection db = new DbConnection();
            db.setUsername(person.getUsername());
            db.setPassword(person.getPassword());
            db.save();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    /*
    * 检查登录
    * */
    public boolean chechLogin(String username,String password){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            DbConnection dbConnection=personList.get(i);
            if(username.equals(dbConnection.getUsername()) && password.equals(dbConnection.getPassword())){
                return true;
            }
        }
        return false;
    }

    }
