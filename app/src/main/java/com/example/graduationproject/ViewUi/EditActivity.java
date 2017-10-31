package com.example.graduationproject.ViewUi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/9/29 0029.
 */

public class EditActivity extends Activity {

    Button setButton;
    EditText setEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        setButton = (Button) findViewById(R.id.name_sava);
        setEdittext = (EditText) findViewById(R.id.name_edt);

        Intent getintent = getIntent();
        setEdittext.setText(getintent.getStringExtra("nickname"));

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upNickname( setEdittext.getText().toString());
            }
        });


    }

    protected void upNickname(final String nickName){
        final String userNmae = MainActivity.userNmae;
        User user = BmobUser.getCurrentUser(User.class);
        NickNews nickNews = new NickNews(userNmae,nickName);
        nickNews.setAuthor(user);
        nickNews.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.d("bmob", "成功");
                    Toast.makeText(EditActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    SetFragment.loadNick();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }
}
