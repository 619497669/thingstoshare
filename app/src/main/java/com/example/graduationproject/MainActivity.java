package com.example.graduationproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.graduationproject.ViewUi.MyFragment;
import com.example.graduationproject.ViewUi.SetFragment;
import com.example.graduationproject.ViewUi.VideoFragment;
import com.example.graduationproject.model.lView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Uri uri;
    private List<lView> mlist;
    private File file;

    public static String userNmae ;

    //UI Object
    private TextView txt_topbar;
    private TextView txt_channel;
    private TextView txt_message;
    private TextView txt_better;
    private TextView txt_setting;
    private FrameLayout ly_content;

    //Fragment Object
    private MyFragment fg1,fg3;
    private VideoFragment fg2;
    private SetFragment fg4;
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

       // loadData();

        Intent getIntent = getIntent();
         userNmae = getIntent.getStringExtra("username");

        mlist = new ArrayList<lView>() ;
     //   intiLview();
         fManager = getFragmentManager();
       // fTransaction.add(R.id.ly_content,fg1);
        bindViews();
        txt_channel.performClick();   //模拟一次点击，既进去后选择第一项


    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        txt_channel = (TextView) findViewById(R.id.txt_channel);
        txt_message = (TextView) findViewById(R.id.txt_message);
       // txt_better = (TextView) findViewById(R.id.txt_better);
        txt_setting = (TextView) findViewById(R.id.txt_setting);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);

        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
      //  txt_better.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
   //     txt_better.setSelected(false);
        txt_setting.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_channel:
                setSelected();
                txt_channel.setSelected(true);
                if(fg1 == null){
                    fg1 = new MyFragment();
                    fTransaction.add(R.id.ly_content,fg1);
                    txt_topbar.setText("图文");
                }else{

                    txt_topbar.setText("图文");
                    fTransaction.show(fg1);
                }
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(fg2 == null){
                    fg2 = new VideoFragment();
                    fTransaction.add(R.id.ly_content,fg2);
                    txt_topbar.setText("视频");
                }else{
                    fTransaction.show(fg2);
                    txt_topbar.setText("视频");
                }
                break;
            /*case R.id.txt_better:
                setSelected();
                txt_better.setSelected(true);
                if(fg3 == null){
                    //fg3 = new MyFragment("第三个Fragment");
                    fTransaction.add(R.id.ly_content,fg3);
                }else{
                    fTransaction.show(fg3);
                }
                break;*/
            case R.id.txt_setting:
                setSelected();
                txt_setting.setSelected(true);
                if(fg4 == null){
                    fg4 = new SetFragment();
                    fTransaction.add(R.id.ly_content,fg4);
                    txt_topbar.setText("设置");
                }else{
                    fTransaction.show(fg4);
                    txt_topbar.setText("设置");
                }
                break;
        }
        fTransaction.commit();
    }


   /* private void loadData() {

        BmobQuery<PersonNews> bmobQuery = new BmobQuery<PersonNews>();
        bmobQuery.order("-createAt");
        bmobQuery.findObjects(new FindListener<PersonNews>() {
            @Override
            public void done(List<PersonNews> list, BmobException e) {
                if (e == null) {
                    System.out.println("查询成功!");
                    int n = list.size();

                    for (int i = 0; i < n; i++) {
                        Constans.personNewses.add( list.get(i) );
                    }

                    //initData();
                }
            }
        });
    }
*/
}
