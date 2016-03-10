package com.lcc.blog.view;

import android.support.annotation.NonNull;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public interface UserProfileView {
    void onProfile(ProfileDrawerItem profile);
    @NonNull ProfileDrawerItem getProfile();
}
