package com.example.graduationproject.ViewUi;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.Utils.Constans;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.User;
import com.example.graduationproject.model.lView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class SetFragment extends Fragment  implements View.OnClickListener{



    // UI Object
    CircleTextImageView profileImage ;
    static TextView name_info;
    Button shareButton , myVideo , myPhotoWord , dropOut;

    private static String protraitId;
    private static String nickId;

    PersonNews personnews;
    static NickNews nickNews;


    private static final int IMAGE_REQUEST_CODE = 0;
    Uri uri;
    Intent intent;

    private DisplayImageOptions options;
    private ImageLoader imageloader;
    private List<lView> dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_set,container,false);



        imageloader = ImageLoader.getInstance();
        imageloader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        profileImage = (CircleTextImageView) view.findViewById(R.id.profile_image);
        name_info = (TextView) view.findViewById(R.id.name_set);
        shareButton = (Button) view.findViewById(R.id.share_button);
        myVideo = (Button) view.findViewById(R.id.set_video);
        myPhotoWord = (Button) view.findViewById(R.id.set_photoword);
        dropOut = (Button) view.findViewById(R.id.drop_out);

        dropOut.setOnClickListener(this);
        myPhotoWord.setOnClickListener(this);
        myVideo.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        name_info.setOnClickListener(this);

        loadImg();
        loadNick();
        return view;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.share_button:
                intent = new Intent(getActivity(),ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_image:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }
                break;
            case R.id.name_set:
                intent = new Intent(getActivity(),EditActivity.class);
                intent.putExtra("nickname",name_info.getText().toString());
                startActivity(intent);
                break;
            case R.id.set_video:
                intent = new Intent(getActivity(),MyVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.set_photoword:
                intent = new Intent(getActivity(),MyPhotoWordActivity.class);
                startActivity(intent);
                break;
            case R.id.drop_out:
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser();
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_REQUEST_CODE){
                uri = data.getData();
                uploadImg(uri);
            }
        }
    }

    //根据URI获取绝对路径
    public static class PathGetter {

        public static String getPath(final Context context, final Uri uri) {

            final boolean isUpToKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isUpToKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri The Uri to query.
         * @param selection (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }

    public void uploadImg(Uri uri){
        final String userNmae = MainActivity.userNmae;
        final String nickName = name_info.getText().toString();

        File file = new File(PathGetter.getPath(getContext(), uri));
        final BmobFile bmobFile = new BmobFile(file);

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Toast.makeText(getContext(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    User user = BmobUser.getCurrentUser(User.class);
                    PersonNews personNews = new PersonNews(userNmae,bmobFile);//是继承了BmobObject的一个类
                    personNews.setAuthor(user);
                    personNews.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Log.d("bmob", "成功");
                                loadImg();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "上传文件失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loadImg(){
        Constans.personNewses.clear();
        final BmobQuery<PersonNews> bmobQuery = new BmobQuery <PersonNews>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<PersonNews>() {
            @Override
            public void done(List<PersonNews> list, BmobException e) {
                    if (e == null){
                        int n = list.size();
                        for (int i = 0; i < n;i++){
                            Constans.personNewses.add(list.get(i));
                        }
                        if (Constans.personNewses != null && Constans.personNewses.size()>0){
                            final String userNmae = MainActivity.userNmae;
                            for (int i = 0; i < Constans.personNewses.size();i++){
                                if (userNmae.equals(Constans.personNewses.get(i).getUsername())){
                                    personnews = Constans.personNewses.get(i);
                                    break;
                                }
                            }
                        }
                        if (personnews != null){
                            imageloader.displayImage(personnews.getImgfile(),profileImage,options);
                        }
                        else {
                            profileImage.setImageResource(R.drawable.qiaoba);
                        }
                    }


            }

        });

    }

    public static void loadNick(){
        Constans.nickNewses.clear();
        final BmobQuery<NickNews> bmobQuery = new BmobQuery <NickNews>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<NickNews>() {
            @Override
            public void done(List<NickNews> list, BmobException e) {
                if (e == null){
                    int  n = list.size();
                    for (int i = 0; i < n;i++){
                        Constans.nickNewses.add(list.get(i));
                    }

                }
                if (Constans.nickNewses != null && Constans.nickNewses.size()>0){
                    final String userNmae = MainActivity.userNmae;
                    for (int i = 0 ; i < Constans.nickNewses.size() ; i++ ){
                        if (userNmae.equals(Constans.nickNewses.get(i).getUsername())){
                            nickNews = Constans.nickNewses.get(i);
                            break;
                        }
                    }
                }
                if (nickNews != null){
                    name_info.setText(nickNews.getNickname());
                }else {
                    name_info.setText("用户昵称");
                }

            }

        });

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }else {
                    Toast.makeText(getContext(),"You denied the permission",Toast.LENGTH_LONG).show();
                }
        }
    }


}
