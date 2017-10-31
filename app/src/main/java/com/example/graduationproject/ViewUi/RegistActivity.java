package com.example.graduationproject.ViewUi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class RegistActivity extends AppCompatActivity {

    private AutoCompleteTextView mUserView;  //用户名
    private EditText mPasswordView;           //密码
    private EditText repassword;              //确认密码
    private EditText myEmail;                   //邮箱

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mUserView = (AutoCompleteTextView) findViewById(R.id.regist_name);//查找用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);      //查找密码控件
        repassword=(EditText) findViewById(R.id.repassword);         //重复密码控件
        myEmail = (EditText) findViewById(R.id.email);                  //验证邮箱

        //注册按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });
    }

    /**
     * 输入的检查
     */

    private void attemptLogin() {

        // 初始化控件错误信息
        mUserView.setError(null);
        mPasswordView.setError(null);
        repassword.setError(null);


        // 获取输入信息.
        String name = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        String mrepassword = repassword.getText().toString();
        String mmail = myEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && isPasswordValid(password) == false ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if(!mrepassword.equals(password)){
            repassword.setError("两次密码不一致");
            focusView = repassword;
            cancel = true;
        }

        // 检查用户名
        if ( TextUtils.isEmpty(name) || isNmaeValid(name) == false ) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        if ( isEmail(mmail) == false  ){
            myEmail.setError("邮箱格式错误");
            focusView = myEmail;
            cancel = true;
        }

        if ( cancel ) {
            focusView.requestFocus();
        } else {
            //注册逻辑实现
            /*-------------------------------*/


            String mename=mUserView.getText().toString();
            String mpassword=repassword.getText().toString();
            String memail = myEmail.getText().toString();
           // Person person=new Person(mename,mpassword);

            BmobUser myUser = new BmobUser();
            myUser.setUsername(mename);
            myUser.setPassword(mpassword);
            myUser.setEmail(memail);
            myUser.setEmailVerified(myUser.getEmailVerified());
            myUser.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null){
                        Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent();
                        intent.setClass(RegistActivity.this,LoginActivity.class);//转到登陆
                        RegistActivity.this.startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(RegistActivity.this,"注册失败,检查网络连接或用户名已存在",Toast.LENGTH_LONG).show();
                    }
                }
            });

          /*  PersonDao personDAO=new PersonDao();
            boolean isSucess=false;

            if(personDAO.checkUsername(mename)){
                Toast.makeText(RegistActivity.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();

            }
            else{

                isSucess= personDAO.insert(person); //添加到数据库

                if(isSucess){  //合法信息
                    Toast.makeText(RegistActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(RegistActivity.this,LoginActivity.class);//转到登陆
                    RegistActivity.this.startActivity(intent);
                    finish();
            *//*-------------------------------*//*
                }
                else {
                    Toast.makeText(RegistActivity.this, "信息不合法，请确认输入", Toast.LENGTH_SHORT).show();
                }
            }
*/

        }

    }

    /**
     * 密码是否和非法，至少需要6位
     * @param password
     * @return
     */

    private boolean isPasswordValid(String password) {
        return password.length()>= 6;
    }

    private boolean isNmaeValid(String name){
        return name.length() >= 6;
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
