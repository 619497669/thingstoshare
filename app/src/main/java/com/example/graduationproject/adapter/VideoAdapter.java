package com.example.graduationproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareVideo;
import com.example.graduationproject.db.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class VideoAdapter extends BaseAdapter {

    Context context;
    public PersonNews portrait ;
    public  NickNews nickname ;
    public String objectId;
    public   int position;


    User s;

    private DisplayImageOptions options;
    private ImageLoader imageloader;

    List<ShareVideo> shareVideoList = new ArrayList<>();
    List<PersonNews> portraitVideoList = new ArrayList<>();
    List<NickNews> nickNameVideoList = new ArrayList<>();

    public VideoAdapter(Context context,List<ShareVideo> shareVideoList,List<PersonNews> portraitVideoList ,List<NickNews> nick) {
        this.context = context;
        this.portraitVideoList = portraitVideoList;
        nickNameVideoList = nick;
        this.shareVideoList = shareVideoList;
    }

    public void initData() {
        //Bundle bundle = getIntent().getExtras();
        //imageUrls = bundle.getStringArray(Constants.IMAGES);

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

    }


    @Override
    public int getCount() {
        return shareVideoList.size();
    }

    @Override
    public Object getItem(int position) {
        return Constans.shareVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHoloder viewHoloder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_video_item,null);
            viewHoloder = new ViewHoloder();
            convertView.setTag(viewHoloder);
        }else {
            viewHoloder = (ViewHoloder) convertView.getTag();
        }
        this.position = position;
        viewHoloder.jzVideoPlayer = (JZVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        viewHoloder.nickText_video = (TextView) convertView.findViewById(R.id.nickname_video);
        viewHoloder.portrait_video = (CircleTextImageView) convertView.findViewById(R.id.portrait_video);
        viewHoloder.textView_video = (TextView) convertView.findViewById(R.id.textView_video);
        viewHoloder.video_iamge = (ImageView) convertView.findViewById(R.id.video_image);
        imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(context));
        ShareVideo sharevideo = shareVideoList.get(position);
        objectId = sharevideo.getObjectId();
        for (int i = 0 ; i < nickNameVideoList.size() ; i++){
            nickname = null;
            if (sharevideo.getUsername().equals(nickNameVideoList.get(i).getUsername())){
                nickname = nickNameVideoList.get(i);
                break;
            }
        }
        for (int i = 0; i < portraitVideoList.size(); i ++ ){
            portrait = null;
            if (sharevideo.getUsername().equals(portraitVideoList.get(i).getUsername())){
                portrait = portraitVideoList.get(i);
                break;
            }
        }
        if (portrait == null){
            viewHoloder.portrait_video.setImageResource(R.drawable.qiaoba);
        }else {
            imageloader.displayImage(portrait.getImgfile(),viewHoloder.portrait_video,options);
        }
        if (nickname == null){
            viewHoloder.nickText_video.setText("用户昵称");
        }else {
            viewHoloder.nickText_video.setText(nickname.getNickname());
        }
        viewHoloder.textView_video.setText(sharevideo.getShareWord());
        viewHoloder.jzVideoPlayer.setUp(sharevideo.getShareVideo(), JZVideoPlayer.SCREEN_LAYOUT_LIST);
        viewHoloder.video_iamge.setImageBitmap(getNetVideoBitmap(sharevideo.getShareVideo()));
        viewHoloder.jzVideoPlayer.thumbImageView.setImageBitmap(getNetVideoBitmap(sharevideo.getShareVideo()));



        return convertView;
    }

    class ViewHoloder{
        CircleTextImageView portrait_video;
        TextView nickText_video;
        TextView textView_video;
        JZVideoPlayerStandard jzVideoPlayer;
        ImageView video_iamge ;
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }


    /**
     *  服务器返回url，通过url去获取视频的第一帧
     *  Android 原生给我们提供了一个MediaMetadataRetriever类
     *  提供了获取url视频第一帧的方法,返回Bitmap对象
     *
     *  @param videoUrl
     *  @return
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}
