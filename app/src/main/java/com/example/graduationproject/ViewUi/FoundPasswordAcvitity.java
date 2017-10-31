package com.example.graduationproject.ViewUi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class FoundPasswordAcvitity extends AppCompatActivity {

    EditText loginemail;
    Button foundpassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundpassword);
        loginemail = (EditText) findViewById(R.id.email_found);
        foundpassword = (Button) findViewById(R.id.email_found_password);
        foundpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = loginemail.getText().toString();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(FoundPasswordAcvitity.this,"重置密码成功，请到"+ email +"邮箱进行重置操作",Toast.LENGTH_LONG).show();
                           // finish();
                        }else {
                            Toast.makeText(FoundPasswordAcvitity.this,"重置密码失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }



}
