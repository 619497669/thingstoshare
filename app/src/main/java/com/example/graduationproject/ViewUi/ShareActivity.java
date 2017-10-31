package com.example.graduationproject.ViewUi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.db.ShareNews;
import com.example.graduationproject.db.ShareVideo;
import com.example.graduationproject.db.User;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;


/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class ShareActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private static final int REQUEST_MEDIA = 100;

   // private List<String> imagePaths = new ArrayList<>();
    private static List<String> videoId = new ArrayList<>();
    private List<MediaItem> imagePaths;
    private List<MediaItem> mMediaSelectedList;

    private GridView gridView;
    private GridAdapter gridAdapter;
    private EditText textView;
    private Button ShareButton ;
    private String TAG =ShareActivity.class.getSimpleName();

    private static Uri uri;
    private static Uri uris;
    private static String urls;
    private String str;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        gridView = (GridView) findViewById(R.id.gridView);
        textView= (EditText)findViewById(R.id.share_edit);
        ShareButton = (Button) findViewById(R.id.share_button);
        Button selectButton = (Button) findViewById(R.id.select_img_video);


        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str.equals(".jpg")){
                    uploadData(uri);
                }
                else if(str.equals(".mp4")){
                    upVideoData(uri);
                }
            }
        });

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

      /*  gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("paizhao".equals(imgs) ){

                    MediaOptions.Builder builder = new MediaOptions.Builder();
                    MediaOptions options = null;
                    options = builder.canSelectBothPhotoVideo()
                            .canSelectMultiPhoto(true).canSelectMultiVideo(true)
                            .build();
                    if (options != null) {
                        MediaPickerActivity.open(ShareActivity.this, REQUEST_MEDIA, options);
                    }
               *//*     AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(getString(R.string.select_demo))
                            .setItems(R.array.options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handleOptionDemoSelected(which);
                                }
                            });
                    dialogBuilder.show();*//*

                  *//*  PhotoPickerIntent intent = new PhotoPickerIntent(ShareActivity.this);
                    intent.setSelectModel(SelectModel.SINGLE);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);*//*
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(ShareActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });*/
       // imagePaths.add(null);
       /* gridAdapter = new GridAdapter();
        gridView.setAdapter(gridAdapter);*/

       selectButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MediaOptions.Builder builder = new MediaOptions.Builder();
               MediaOptions options = null;
               options = builder.canSelectBothPhotoVideo()
                       .canSelectMultiPhoto(true).canSelectMultiVideo(true)
                       .build();
               if (options != null) {
                   MediaPickerActivity.open(ShareActivity.this, REQUEST_MEDIA, options);
               }
           }
       });
    }



    private void uploadData(Uri uri){
        File file = new File(SetFragment.PathGetter.getPath(ShareActivity.this,uri));
        final String userNmae = MainActivity.userNmae;
        final String edittext = textView.getText().toString();
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(ShareActivity.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    User user = BmobUser.getCurrentUser(User.class);
                    ShareNews shareNews = new ShareNews();
                    shareNews.setShareWord(edittext);
                    shareNews.setShareImg(bmobFile);
                    shareNews.setUsername(userNmae);
                    shareNews.setAuthor(user);
                    shareNews.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                videoId.add(s);
                                Intent in = getIntent();
                                setResult(RESULT_OK,in);
                                finish();
                                Log.d("bmob", "成功");
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }
            }
        });
    }

    private void upVideoData(Uri uri){
        File file = new File(SetFragment.PathGetter.getPath(ShareActivity.this,uri));
        final String userNmae = MainActivity.userNmae;
        final String edittext = textView.getText().toString();
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(ShareActivity.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    User user = BmobUser.getCurrentUser(User.class);
                    ShareVideo shareVideo = new ShareVideo(userNmae,edittext,bmobFile);
                    shareVideo.setAuthor(user);
                    shareVideo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Intent in = getIntent();
                                setResult(RESULT_OK,in);
                                finish();
                                Log.d("bmob", "成功");
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                /*// 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "数量："+list.size());
                    url = list.get(0);
                    loadAdpater(list);
                    break;*/
               /* // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    loadAdpater(ListExtra);
                    break;*/
                case REQUEST_MEDIA:
                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    loadAdpater(mMediaSelectedList);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            uri = mediaItem.getUriOrigin();
                            urls = SetFragment.PathGetter.getPath(ShareActivity.this,uri);
                        }
                         str =  urls.substring(urls.lastIndexOf('.') ,urls.length());

                    } else {
                        Log.e(TAG, "Error to get media, NULL");
                    }
            }
        }
    }

    private void loadAdpater(List<MediaItem> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
     /*   if (paths.contains("paizhao")){
            paths.remove("paizhao");
        }
        paths.add(null);*/
    //    imagePaths.addAll(paths);
        gridAdapter  = new GridAdapter(paths);
        gridView.setAdapter(gridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private List<MediaItem> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(List<MediaItem> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 7){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(ShareActivity.this);
        }

        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public MediaItem getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final MediaItem path=listUrls.get(position);
            if (path.equals(null)){
                holder.image.setImageResource(R.mipmap.find_add_img);
            }else {
                if (path.getUriCropped() == null){
                    Glide.with(ShareActivity.this)
                            .load(path.getUriOrigin())
                         //   .placeholder(R.mipmap.default_error)
                           // .error(R.mipmap.default_error)
                            .centerCrop()
                            .crossFade()
                            .into(holder.image);
                }else {
                    Glide.with(ShareActivity.this)
                            .load(path.getUriCropped())
                          //  .placeholder(R.mipmap.default_error)
                          //  .error(R.mipmap.default_error)
                            .centerCrop()
                            .crossFade()
                            .into(holder.image);
                }

            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }

    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
        //    retriever.setDataSource(videoUrl, new HashMap());
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
