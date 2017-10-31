package com.example.graduationproject.ViewUi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.adapter.VideoAdapter;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareVideo;
import com.example.graduationproject.db.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class MyVideoActivity extends AppCompatActivity {

    ListView mylistView ;
    String objectId;

    SensorManager sensorManager;
    JZVideoPlayer.JZAutoFullscreenListener sensorEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mylistView  = (ListView) findViewById(R.id.list_view_video);

        sensorManager = (SensorManager) getSystemService(MyVideoActivity.SENSOR_SERVICE);
        sensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();

        uploadVideo();

    }

    private void uploadVideo(){
        Constans.shareVideos.clear();
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<ShareVideo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("author",user);
        bmobQuery.order("-createdAt");
        bmobQuery.include("author");
        bmobQuery.findObjects(new FindListener<ShareVideo>() {
            @Override
            public void done(List<ShareVideo> list, BmobException e) {
                if (e == null){
                    int n = list.size();
                    for (int i = 0 ; i < n ; i++){
                        Constans.shareVideos.add(list.get(i));
                    }
                }
                loadNick();
            }
        });
    }

    public  void loadNick(){
        Constans.nickNewses.clear();
        final BmobQuery<NickNews> bmobQuery = new BmobQuery <NickNews>();
        bmobQuery.order("-createdAt");
        bmobQuery.include("author");
        bmobQuery.findObjects(new FindListener<NickNews>() {
            @Override
            public void done(List<NickNews> list, BmobException e) {
                if (e == null){
                    int  n = list.size();
                    for (int i = 0; i < n;i++){
                        Constans.nickNewses.add(list.get(i));
                    }
                }
                /*if (Constans.nickNewses != null && Constans.nickNewses.size()>0){
                    for (int i = 0 ; i < Constans.nickNewses.size() ; i++ ){
                        for (int j = 0 ; j < Constans.shareNewses.size();j++){
                            if (Constans.shareNewses.get(j).getUsername().equals(Constans.nickNewses.get(i).getUsername())){
                                nickNews = Constans.nickNewses.get(i);
                                break;
                            }
                        }

                    }
                }*/
                loadImg();
            }

        });
    }

    private void loadImg(){
        Constans.personNewses.clear();
        final BmobQuery<PersonNews> bmobQuery = new BmobQuery <PersonNews>();
        bmobQuery.order("-createdAt");
        bmobQuery.include("author");
        bmobQuery.findObjects(new FindListener<PersonNews>() {
            @Override
            public void done(List<PersonNews> list, BmobException e) {
                if (e == null){
                    int n = list.size();
                    for (int i = 0; i < n;i++){
                        Constans.personNewses.add(list.get(i));
                    }
                }
                /*if (Constans.personNewses != null && Constans.personNewses.size()>0){
                    for (int i = 0; i < Constans.personNewses.size();i++){
                        for (int j = 0 ; j < Constans.shareNewses.size() ; j++){
                            if (Constans.shareNewses.get(j).getUsername().equals(Constans.personNewses.get(i).getUsername())){
                                personnews = Constans.personNewses.get(i);
                                break;
                            }
                        }

                    }
                }*/
                final VideoAdapter videoAdapter = new VideoAdapter(MyVideoActivity.this,Constans.shareVideos,Constans.personNewses,Constans.nickNewses);
                mylistView.setAdapter(videoAdapter);
                mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ShareVideo shareVideo = Constans.shareVideos.get(position);
                         objectId = shareVideo.getObjectId();
                        Intent intent = new Intent(MyVideoActivity.this,VideoViewActivity.class);
                        intent.putExtra("name",shareVideo.getUsername());
                        intent.putExtra("portrait",videoAdapter.portrait.getImgfile());
                        intent.putExtra("nick",videoAdapter.nickname.getNickname());
                        intent.putExtra("objectid",objectId);
                        intent.putExtra("shareword",shareVideo.getShareWord());
                        intent.putExtra("video",shareVideo.getShareVideo());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        //    JZMediaManager.instance().mediaPlayer.pause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}

