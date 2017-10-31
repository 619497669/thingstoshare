package com.example.graduationproject.Utils;

import com.example.graduationproject.db.Comment;
import com.example.graduationproject.db.NickNews;
import com.example.graduationproject.db.PersonNews;
import com.example.graduationproject.db.ShareNews;
import com.example.graduationproject.db.ShareVideo;
import com.example.graduationproject.model.lView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 */

public class Constans {
    public static List<lView> lViews = new ArrayList<lView>();
    public static List<PersonNews> personNewses = new ArrayList<PersonNews>();
    public static List<NickNews> nickNewses = new ArrayList<NickNews>();
    public static List<ShareNews> shareNewses = new ArrayList<ShareNews>();
    public static List<ShareVideo> shareVideos = new ArrayList<>();
    public static List<Comment> comments = new ArrayList<>();
}
