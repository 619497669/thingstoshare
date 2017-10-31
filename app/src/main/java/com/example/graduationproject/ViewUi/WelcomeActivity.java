package com.example.graduationproject.ViewUi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.graduationproject.R;

import org.litepal.LitePal;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Bmob.initialize(this,"c7d39a8a936fa6f466b267facbcee11c");

        LitePal.getDatabase();//创建数据库

        final Intent intent=new Intent();
        intent.setClass(WelcomeActivity.this,LoginActivity.class);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };

        timer.schedule(timerTask,2000);//此处的Delay可以是2*1000，代表两秒

    }
}
