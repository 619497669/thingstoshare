package com.example.graduationproject.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.graduationproject.ViewUi.MyFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    private String[] titles = new String[]{
            "校园", "游戏", "动漫", "影视", "娱乐",
            "人物","体育"
    };


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

   /* public static Fragment getInstance(int position) {
       Fragment fragment = null;
        Bundle bundle = null;
        switch (position){
            case 0 :
                fragment = new MyFragment();
                bundle = new Bundle();
                bundle.putInt("type",0);
                fragment.setArguments(bundle);
                break;

        }

        return fragment;
    }*/


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MyFragment();
        Bundle bundle = null;
        switch (position){
            case 0 :
                bundle = new Bundle();
                bundle.putInt("type",0);
                fragment.setArguments(bundle);
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
