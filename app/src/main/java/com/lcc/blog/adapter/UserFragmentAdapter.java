package com.lcc.blog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lcc.blog.ui.post.UserPostsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/7.
 */
public class UserFragmentAdapter extends FragmentPagerAdapter{
    List<Fragment> fragments;
    public UserFragmentAdapter(FragmentManager fm,int user_id) {
        super(fm);
        fragments = new ArrayList<>(2);
        fragments.add(UserPostsFragment.newInstance(user_id));
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
