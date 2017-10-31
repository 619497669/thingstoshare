package com.example.graduationproject.ViewUi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.adapter.VideoAdapter;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareVideo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class VideoFragment extends VideoFragmentItem  {
    ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    ShareVideo shareVideo;
    String objectId;
    public PersonNews portrait ;
    public  NickNews nickname ;

    SensorManager sensorManager;
    JZVideoPlayer.JZAutoFullscreenListener sensorEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video,container,false);
        listView = (ListView) view.findViewById(R.id.list_view_video);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.video_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.text_yellow);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressViewEndTarget(true,200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });


        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        sensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();

        uploadVideo();
        return view;
    }


    //handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    uploadVideo();
                    swipeRefreshLayout.setRefreshing(false);

                    //     listView.setAdapter(new ItemListAdapter());
//                    adapter.notifyDataSetChanged();
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void uploadVideo(){
        Constans.shareVideos.clear();
        BmobQuery<ShareVideo> bmobQuery = new BmobQuery<>();
        bmobQuery.include("author");
        bmobQuery.order("-createdAt");
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
                final VideoAdapter videoAdapter = new VideoAdapter(getActivity(),Constans.shareVideos,Constans.personNewses,Constans.nickNewses);
                listView.setAdapter(videoAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         shareVideo = Constans.shareVideos.get(position);
                        queryPortraitAndNick();
                        objectId = shareVideo.getObjectId();
                        Intent intent = new Intent(getActivity(),VideoViewActivity.class);
                        intent.putExtra("name",shareVideo.getUsername());
                        if (portrait!=null){
                            intent.putExtra("portrait",portrait.getImgfile());
                        }
                        if (nickname!=null){
                            intent.putExtra("nick",nickname.getNickname());
                        }
                        intent.putExtra("objectid",objectId);
                        intent.putExtra("shareword",shareVideo.getShareWord());
                        intent.putExtra("video",shareVideo.getShareVideo());
                        startActivity(intent);
                    }
                });
            }
        });

    }

public void queryPortraitAndNick(){

    for (int i = 0 ; i < Constans.nickNewses.size() ; i++){
        nickname = null;
        if (shareVideo.getUsername().equals(Constans.nickNewses.get(i).getUsername())){
            nickname = Constans.nickNewses.get(i);
            break;
        }
    }
    for (int i = 0; i < Constans.personNewses.size(); i ++ ){
        portrait = null;
        if (shareVideo.getUsername().equals(Constans.personNewses.get(i).getUsername())){
            portrait = Constans.personNewses.get(i);
            break;
        }
    }
    }

    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.getActivity().onBackPressed();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            onPause();
        }else {
            onResume();
        }
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
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
