package com.lcc.blog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lcc.blog.fragment.UserPostsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/7.
 */
public class UserFragmentAdapter extends FragmentPagerAdapter{
    List<Fragment> fragments;
    public UserFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>(2);
        fragments.add(new UserPostsFragment());
        fragments.add(new UserPostsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "文章";
    }
}
