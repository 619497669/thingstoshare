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

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class LoginActivity extends AppCompatActivity {

    Button button_login , button_regist , button_found;
    String username , password;
    AutoCompleteTextView username_view;
    EditText password_edittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_view = (AutoCompleteTextView) findViewById(R.id.login_name);
        password_edittext = (EditText) findViewById(R.id.password);
        button_found = (Button) findViewById(R.id.found_password);
        button_login = (Button) findViewById(R.id.email_sign_in_button);
        button_regist = (Button) findViewById(R.id.register);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        button_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,FoundPasswordAcvitity.class);
                startActivity(intent);
            }
        });
        button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);

            }
        });

    }

    protected void attemptLogin(){

        // 初始化错误信息为null
        username_view.setError(null);
        password_edittext.setError(null);

        // 获取输入信息.
        final String username = username_view.getText().toString();
        String password = password_edittext.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && isPasswordValid(password) == false ) {
            password_edittext.setError(getString(R.string.error_invalid_password));
            focusView = password_edittext;
            cancel = true;

        }

        // 检查用户名
        if ( TextUtils.isEmpty(username) ) {
            username_view.setError(getString(R.string.error_field_required));
            focusView = username_view;
            cancel = true;
        }
        if (cancel){ //非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        }else {

            BmobUser userLogin = new BmobUser();
            userLogin.setUsername(username);
            userLogin.setPassword(password);
            userLogin.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser user, BmobException e) {
                    if (e == null){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);

                    }else {

                        Toast.makeText(LoginActivity.this,"用户名或密码错误,并且检查网络连接",Toast.LENGTH_LONG).show();
                    }

                }
            });
           /* PersonDao personDao = new PersonDao();
            boolean success = personDao.chechLogin(username,password);
            if (success){//信息合法
                APPglobal.USERNAME = username;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
            }*/
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
}
