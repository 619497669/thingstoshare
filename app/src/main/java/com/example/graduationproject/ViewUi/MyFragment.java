package com.example.graduationproject.ViewUi;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareNews;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MyFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private String content;
    private static String objectId;
    private static ListView listView;
    private List<String> dataList = new ArrayList<>();

    private DisplayImageOptions options;
    private ImageLoader imageloader;
    PersonNews personnews;
    NickNews nickNews;
    ShareNews shareNews;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content,container,false);
       listView = (ListView) view.findViewById(R.id.list_view);

        imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.content_swipe);
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
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        loadShare();

        return view;
    }

    //handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    loadShare();
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

    public  void  loadShare(){
        Constans.shareNewses.clear();
        final BmobQuery<ShareNews> bmobQuery = new BmobQuery();
        bmobQuery.order("-createdAt");
        bmobQuery.include("author");
        bmobQuery.findObjects(new FindListener<ShareNews>() {
            @Override
            public void done(List<ShareNews> list, BmobException e) {
                if (e == null){
                    int n = list.size();
                    for (int i = 0 ; i < n ; i++){
                        Constans.shareNewses.add(list.get(i));
                    }
                }
                loadNick();
            }
        });
    }

    public void loadImg(){
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
                swipeRefreshLayout.setRefreshing(false);
                listView.setAdapter(new ItemListAdapter());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         shareNews = Constans.shareNewses.get(position);
                        queryPortraitAndNick();
                        objectId = shareNews.getObjectId();
                        Intent intent = new Intent(getActivity(),PhotoViewActivity.class);
                        intent.putExtra("name",shareNews.getUsername());
                        if (personnews != null){
                            intent.putExtra("portrait",personnews.getImgfile());
                        }
                       if (nickNews != null){
                           intent.putExtra("nick",nickNews.getNickname());
                       }
                        intent.putExtra("objectid",objectId);
                        intent.putExtra("shareword",shareNews.getShareWord());
                        intent.putExtra("image",shareNews.getShareImg());
                        startActivity(intent);
                    }
                });
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

    public void queryPortraitAndNick(){

        for (int i = 0 ; i < Constans.nickNewses.size() ; i++){
            nickNews = null;
            if (shareNews.getUsername().equals(Constans.nickNewses.get(i).getUsername())){
                nickNews = Constans.nickNewses.get(i);
                break;
            }
        }
        for (int i = 0; i < Constans.personNewses.size(); i ++ ){
            personnews = null;
            if (shareNews.getUsername().equals(Constans.personNewses.get(i).getUsername())){
                personnews = Constans.personNewses.get(i);
                break;
            }
        }
    }

    class ItemListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Constans.shareNewses.size();
        }

        @Override
        public Object getItem(int position) {
            return Constans.shareNewses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoloder viewHoloder = null;
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fg_content_item,null);
                viewHoloder = new ViewHoloder();
                viewHoloder.portrait = (CircleTextImageView) convertView.findViewById(R.id.portrait_content);
                viewHoloder.nickname = (TextView) convertView.findViewById(R.id.nickname_content);
                viewHoloder.fg_text = (TextView) convertView.findViewById(R.id.fg_textView);
                viewHoloder.fg_image = (ImageView) convertView.findViewById(R.id.fg_iamgeview);
                convertView.setTag(viewHoloder);
            }else {
                viewHoloder = (ViewHoloder) convertView.getTag();
            }
            ShareNews sharenews = Constans.shareNewses.get(position);

            if (Constans.personNewses != null){
                for (int i = 0 ; i < Constans.personNewses.size() ; i ++){
                    personnews = null;
                    if (sharenews.getUsername().equals(Constans.personNewses.get(i).getUsername())){
                        personnews = Constans.personNewses.get(i);
                        //      imageloader.displayImage(personnews.getImgfile(),viewHoloder.portrait,options);
                        break;
                    }
                }
                for (int i = 0 ; i < Constans.nickNewses.size() ; i ++){
                    nickNews = null;
                    if (Constans.nickNewses.get(i).getUsername().equals(sharenews.getUsername())){
                        nickNews = Constans.nickNewses.get(i);
                        //        viewHoloder.nickname.setText(nickNews.getNickname());
                        break;
                    }
                }
            }


            imageloader.displayImage(sharenews.getShareImg(),viewHoloder.fg_image,options);
           /* if (sharenews.getUsername().equals(nickNews.getUsername())){
                imageloader.displayImage(personnews.getImgfile(),viewHoloder.portrait,options);
                viewHoloder.nickname.setText(nickNews.getNickname());
            }else {
                viewHoloder.portrait.setImageResource(R.drawable.qiaoba);
                viewHoloder.nickname.setText("用户昵称");
            }*/


            viewHoloder.fg_text.setText(sharenews.getShareWord());
            if (personnews == null){
                viewHoloder.portrait.setImageResource(R.drawable.qiaoba);
            }else {
                imageloader.displayImage(personnews.getImgfile(),viewHoloder.portrait,options);
            }
            if (nickNews == null){
                viewHoloder.nickname.setText("用户昵称");
            }else {
                viewHoloder.nickname.setText(nickNews.getNickname());
            }

            return convertView;
        }



        public class ViewHoloder{
            public CircleTextImageView portrait;
            public TextView nickname;
            TextView fg_text;
            ImageView fg_image;

        }
    }


}
