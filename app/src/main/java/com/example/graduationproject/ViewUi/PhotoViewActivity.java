package com.example.graduationproject.ViewUi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.db.Comment;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareNews;
import com.example.graduationproject.db.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class PhotoViewActivity extends Activity {

    private String username,portarit,nickname,image,objectId,shareWord;
    private TextView shareText,commentText,uploadText,nickText;
    private CircleTextImageView portraitView;
    private ImageView imageView;

    private NickNews commentNick;
    private PersonNews commentPortrait;

    private DisplayImageOptions options;
    ImageLoader imageloader;

    ListView photoListview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

         imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(this));

        photoListview = (ListView) findViewById(R.id.photo_view_list);
        shareText = (TextView) findViewById(R.id.photo_view_text);
        commentText = (TextView) findViewById(R.id.photo_view_edit);
        uploadText = (TextView) findViewById(R.id.photo_view_button);
        nickText = (TextView) findViewById(R.id.photo_view_nick);
        portraitView = (CircleTextImageView) findViewById(R.id.photo_view_portrait);
        imageView = (ImageView) findViewById(R.id.photo_view_img);

        Intent getintent = getIntent();
        username = getintent.getStringExtra("name");
        portarit = getintent.getStringExtra("portrait");
        nickname = getintent.getStringExtra("nick");
        image = getintent.getStringExtra("image");
        shareWord = getintent.getStringExtra("shareword");
        objectId = getintent.getStringExtra("objectid");

        loadComment();

        if (portarit != null){
            imageloader.displayImage(portarit,portraitView,options);
        }else {
            portraitView.setImageResource(R.drawable.qiaoba);
        }

        if (nickname == null){
             nickText.setText("用户名称");
        }else {
            nickText.setText(nickname);
        }
        shareText.setText(shareWord);
        imageloader.displayImage(image,imageView,options);
        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComment();
            }
        });

    }

    private void uploadComment(){
        String username = MainActivity.userNmae;
        final Comment comment = new Comment();
        User user = BmobUser.getCurrentUser(User.class);
        ShareNews sharenews = new ShareNews();
        sharenews.setObjectId(objectId);
        comment.setContent(commentText.getText().toString());
        comment.setShareNews(sharenews);
        comment.setUser(user);
        comment.setUsername(username);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    Log.d("bmob", "成功");
                    Toast.makeText(PhotoViewActivity.this,"评论发表成功",Toast.LENGTH_LONG).show();
                    loadComment();
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    private void loadComment(){
        Constans.comments.clear();
        BmobQuery<Comment> commentQuery = new BmobQuery<>();
        ShareNews sharenews = new ShareNews();
        sharenews.setObjectId(objectId);
        commentQuery.addWhereEqualTo("shareNews",new BmobPointer(sharenews));
        commentQuery.order("-updatedAt");
        commentQuery.include("user");
        commentQuery.include("shareNews.author");
        commentQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                for (int i = 0 ; i < list.size(); i++){
                    Constans.comments.add(list.get(i));
                }
                loadNick();
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
                photoListview.setAdapter(new PhotoViewAdapter());

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


    class PhotoViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Constans.comments.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoloder viewHoloder = null;
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.activity_comment,null);
                viewHoloder = new ViewHoloder();
                convertView.setTag(viewHoloder);
            }else {
                viewHoloder = (ViewHoloder) convertView.getTag();
            }
            viewHoloder.comment_nick = (TextView) convertView.findViewById(R.id.comment_nick);
            viewHoloder.comment_portrait = (CircleTextImageView) convertView.findViewById(R.id.comment_portrait);
            viewHoloder.comment_word = (TextView) convertView.findViewById(R.id.comment_word);
            Comment commentWord = Constans.comments.get(position);
            if (commentWord.getUsername()!= null){
                for (int i = 0 ; i < Constans.nickNewses.size(); i++){
                    if (commentWord.getUsername().equals(Constans.nickNewses.get(i).getUsername())){
                        commentNick = Constans.nickNewses.get(i);
                        break;
                    }
                }
                for (int i = 0 ;i < Constans.personNewses.size();i++){
                    if (commentWord.getUsername().equals(Constans.personNewses.get(i).getUsername())){
                        commentPortrait = Constans.personNewses.get(i);
                        break;
                    }
                }
            }


            if (commentNick != null){
                viewHoloder.comment_nick.setText(commentNick.getNickname());
            }else {
                viewHoloder.comment_nick.setText("用户昵称");
            }

            if (commentPortrait != null){
                imageloader.displayImage(commentPortrait.getImgfile(),viewHoloder.comment_portrait);
            }else {
                viewHoloder.comment_portrait.setImageResource(R.drawable.qiaoba);
            }
            if (commentWord!= null){
                viewHoloder.comment_word.setText(commentWord.getContent());
            }


            return convertView;
        }

        class ViewHoloder{
            TextView comment_word;
            CircleTextImageView comment_portrait;
            TextView comment_nick;
        }

    }

}
